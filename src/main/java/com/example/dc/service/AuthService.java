package com.example.dc.service;

import com.example.dc.common.enums.ExceptionEnum;
import com.example.dc.common.exception.LyException;
import com.example.dc.common.utils.JwtUtils;
import com.example.dc.common.utils.UserInfo;
import com.example.dc.config.JwtProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@EnableConfigurationProperties(JwtProperties.class)
@Slf4j
public class AuthService {
    @Autowired
    private JwtProperties prop;

    public String generateToken(){
        //生成token
        String token= null;
        try {
            token = JwtUtils.generateToken(new UserInfo(123l,"dc"),prop.getPrivateKey(),prop.getExpire());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }
    public UserInfo getInfoFromToken(String token){
        UserInfo infoFromToken=null;
        try {
            infoFromToken = JwtUtils.getInfoFromToken(token, prop.getPublicKey());
        } catch (Exception e) {
            throw new LyException(ExceptionEnum.TOKEN_EXPIRE);
//            e.printStackTrace();
        }
        return infoFromToken;
    }
}
