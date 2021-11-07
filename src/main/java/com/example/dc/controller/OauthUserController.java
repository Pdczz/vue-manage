package com.example.dc.controller;

import com.example.dc.pojo.GithubUser;
import com.example.dc.pojo.OauthUser;
import com.example.dc.service.OauthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/oauth")
public class OauthUserController {
    @Autowired
    private OauthUserService oauthUserService;
    @PostMapping("/getuserId/{id}")
    public ResponseEntity<OauthUser> findOauthUserById(@PathVariable("id") Integer id){
        OauthUser oauthUserById = oauthUserService.findOauthUserById(id);

        return ResponseEntity.ok(oauthUserById);
    }
    @PostMapping("/getappId/{id}")
    public ResponseEntity<OauthUser> findOauthUserByAppId(@PathVariable("id") String id){
        OauthUser oauthUserById = oauthUserService.findOauthUserByAppId(id);
        return ResponseEntity.ok(oauthUserById);
    }

}
