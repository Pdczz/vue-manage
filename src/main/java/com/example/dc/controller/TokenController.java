package com.example.dc.controller;

import com.example.dc.common.utils.HttpUtil;
import com.example.dc.service.TokenService;
import org.apache.commons.lang.StringUtils;
import org.omg.CORBA.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@RestController
@RequestMapping("api")
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("githubToken")
    public void getToken(@RequestParam("code") String code){
        if (StringUtils.isNotEmpty(code)){
            System.out.println(code);
            //获取到授权码,获取token
            String url="https://github.com/login/oauth/access_token";
            HashMap<String, String> jsonParams = new HashMap<>();
            jsonParams.put("client_id","fe0755eb905bb2cadfb5");
            jsonParams.put("client_secret","3d5c32de1b3c16422ea6292f027bad7469833c16");
            jsonParams.put("code",code);
            String s = tokenService.sendPost(url, jsonParams);
            System.out.println(s);
            String[] split = s.split("&");
            String[] token = split[0].split("=");
            String access_token = token[1];
            ResponseEntity<String> forEntity = restTemplate.getForEntity("https://api.github.com/user?" + s, String.class);
            System.out.println(forEntity);

        }
        System.out.println(666);

    }
}
