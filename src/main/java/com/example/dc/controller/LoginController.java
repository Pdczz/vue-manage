package com.example.dc.controller;

import com.example.dc.pojo.User;
import com.example.dc.service.AdminMenuService;
import com.example.dc.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.List;


@RestController
@RequestMapping("api")
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private AdminMenuService adminMenuService;

    @CrossOrigin(origins = "*")
    @PostMapping("register")
    @ResponseBody
    public ResponseEntity<Void> register(@RequestBody User user) {

        String username = user.getUsername();
        String password = user.getPassword();
        username = HtmlUtils.htmlEscape(username);
        user.setUsername(username);

        boolean exist = userService.isExist(username);
        if (exist) {
            String message = "用户名已被使用";
            //return ResultCode.REGISTER_FAIL;
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // 生成盐,默认长度 16 位
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        // 设置 hash 算法迭代次数
        int times = 2;
        // 得到 hash 后的密码
        String encodedPassword = new SimpleHash("md5", password, salt, times).toString();
        // 存储用户信息，包括 salt 与 hash 后的密码
        user.setSalt(salt);
        user.setPassword(encodedPassword);
        userService.add(user);

        //return ResultCode.SUCCESS;
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @CrossOrigin(origins = "http://admin.pdczz.com")
    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody User user) {
        String username = user.getUsername();
        Subject subject = SecurityUtils.getSubject();
//       subject.getSession().setTimeout(10000);
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, user.getPassword());
        usernamePasswordToken.setRememberMe(true);

        try {
            subject.login(usernamePasswordToken);
            User currentUser = userService.queryByUsername(username);
            return ResponseEntity.ok(currentUser.getName());
            //return ResultFactory.buildSuccessResult(username);
        } catch (AuthenticationException e) {
            String message = "账号密码错误";
            //return ResultFactory.buildFailResult(message);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @ResponseBody
    @GetMapping("logout")
    public ResponseEntity<String> logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return ResponseEntity.ok("成功登出");
    }
    @ResponseBody
    @CrossOrigin(origins = "http://admin.pdczz.com")
    @GetMapping(value = "authen")
    public ResponseEntity<String> authen(){
        return ResponseEntity.ok("身份认证成功");
    }



}
