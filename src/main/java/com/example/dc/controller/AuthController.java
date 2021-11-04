package com.example.dc.controller;

import com.example.dc.common.utils.CookieUtils;
import com.example.dc.common.utils.UserInfo;
import com.example.dc.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api")
public class AuthController {
    @Autowired
    private AuthService authService;

    @RequestMapping("/generToken")
    public String generToken(HttpServletRequest request, HttpServletResponse response){
        String token = authService.generateToken();
        CookieUtils.setCookie(request,response,"token",token,30000,false);
        return token;

//        return authService.generateToken();
    }
    @RequestMapping("/getInfoFromToken")
    public UserInfo getInfoFromToken(@RequestParam("token")String token){
        UserInfo infoFromToken = authService.getInfoFromToken(token);
        return infoFromToken;
    }
}
