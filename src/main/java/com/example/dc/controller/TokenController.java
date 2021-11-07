package com.example.dc.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.dc.common.enums.ExceptionEnum;
import com.example.dc.common.exception.LyException;
import com.example.dc.pojo.GithubUser;
import com.example.dc.service.TokenService;
import org.apache.commons.lang.StringUtils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import java.util.HashMap;

@RestController
@RequestMapping("api")
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("githubToken")
    public ResponseEntity<GithubUser> getToken(@RequestParam("code") String code)  {
            return ResponseEntity.ok(tokenService.getToken(code));
    }
}
