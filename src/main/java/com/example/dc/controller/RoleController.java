package com.example.dc.controller;

import com.example.dc.pojo.AdminMenu;
import com.example.dc.pojo.AdminPermission;
import com.example.dc.pojo.AdminRole;
import com.example.dc.service.AdminMenuService;
import com.example.dc.service.AdminPermissionService;
import com.example.dc.service.AdminRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api")
public class RoleController {
    @Autowired
    private AdminRoleService adminRoleService;
    @Autowired
    private AdminPermissionService adminPermissionService;
    @Autowired
    private AdminMenuService adminMenuService;

    @GetMapping("admin/role")
    public ResponseEntity<List<AdminRole>> listRoles(){//全部role都有权限
        List<AdminRole> adminRoles = adminRoleService.listWithPermsAndMenus();
        return ResponseEntity.ok(adminRoles);
    }

    /**
     * 更新role
     * @param requestRole
     * @return
     */
    @PutMapping("admin/role")
    public ResponseEntity<String> editRole(@RequestBody AdminRole requestRole) {
        adminRoleService.editRole(requestRole);
        return ResponseEntity.ok("修改用户成功");
    }
    @PostMapping("admin/role")
    public ResponseEntity<String> addRole(@RequestBody AdminRole requestRole) {
        adminRoleService.add(requestRole);
        return ResponseEntity.ok("新增用户成功");
    }
    @GetMapping("admin/role/perm")
    public ResponseEntity<List<AdminPermission>> listPerms(){
        return ResponseEntity.ok(adminPermissionService.queryAll());
    }
    @GetMapping("admin/role/menu")
    public ResponseEntity<List<AdminMenu>> listMenu(){
        return ResponseEntity.ok(adminMenuService.getMenusByCurrentUser());
    }

    @PutMapping("admin/role/menu")
    public ResponseEntity<Void> updateRoleMenu(@RequestParam("rid") int rid, @RequestBody LinkedHashMap menusIds) {
        adminRoleService.updateRoleMenu(rid, menusIds);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
