package com.example.dc.controller;

import com.example.dc.pojo.AdminRole;
import com.example.dc.pojo.User;
import com.example.dc.service.AdminRoleService;
import com.example.dc.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AdminRoleService adminRoleService;
    // UserController
    @PutMapping("/admin/user")
    public ResponseEntity<String> editUser(@RequestBody User requestUser) {
        userService.editUser(requestUser);
        String message = "修改用户信息成功";
        return ResponseEntity.ok(message);
    }
    @PutMapping("admin/user/status")
    public ResponseEntity<String> updateUserStatus(@RequestBody User requestUser) {
        userService.updateUserStatus(requestUser);
        return ResponseEntity.ok("用户状态更新成功");
    }

    @PutMapping("admin/user/password")
    public ResponseEntity<String> resetPassword(@RequestBody  User requestUser) {
        userService.resetPassword(requestUser);
        return ResponseEntity.ok("重置密码成功");
    }
    @RequiresPermissions("/api/admin/user")
    @GetMapping("admin/user")
    public List<User> listUsers() throws Exception {
        List<User> users =  userService.queryAll();
        List<AdminRole> roles;
        for (User user : users) {
            roles = adminRoleService.listRoleByUser(user.getId());
            user.setRoles(roles);
        }
        return users;
    }


}
