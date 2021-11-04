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

            GithubUser githubUser=new GithubUser();
            if (StringUtils.isNotEmpty(code)){
                System.out.println(code);
                //获取到授权码,获取token
                String url="https://github.com/login/oauth/access_token";
                HashMap<String, String> jsonParams = new HashMap<>();
                jsonParams.put("client_id","fe0755eb905bb2cadfb5");
                jsonParams.put("client_secret","3d5c32de1b3c16422ea6292f027bad7469833c16");
                jsonParams.put("code",code);
                String s="";
                try {
                     s = tokenService.sendPost(url, jsonParams);
                }catch (Exception exception){
                    throw new LyException(ExceptionEnum.GITHUB_LOGIN_ERROR);
//                    exception.printStackTrace();
                }

                System.out.println("token: "+s);
                String[] split = s.split("&");
                String[] token = split[0].split("=");
                String access_token = token[1];
                String githubURL="https://api.github.com/user";
//            ResponseEntity<String> forEntity = restTemplate.getForEntity("https://api.github.com/user" + s, String.class);
                if (StringUtils.isNotEmpty(access_token)){
                    HttpHeaders headers = new HttpHeaders();
                    headers.set("authorization", "Bearer "+access_token);
//                MediaType type=MediaType.parseMediaType("application/json;charset=UTF-8");

//                headers.setContentType(type);
                    //过滤掉账号认证失败的时候抛出的401异常
                    restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
                        @Override
                        public void handleError(ClientHttpResponse response) throws IOException {
                            if(response.getRawStatusCode() == 401){
                                throw new LyException(ExceptionEnum.GITHUB_CODE_EXPIRED);
                            }
                        }
                    });

                    ResponseEntity<Object> response = restTemplate.exchange(githubURL, HttpMethod.GET,new HttpEntity<byte[]>(headers),Object.class);
                    System.out.println(response);
                    Object body = response.getBody();
                    String s1 = body.toString();
                    JSON json = (JSON) JSONObject.toJSON(body);
                    githubUser = JSON.toJavaObject(json, GithubUser.class);
                    System.out.println(githubUser);

                }
            }
            return ResponseEntity.ok(githubUser);


    }
}
