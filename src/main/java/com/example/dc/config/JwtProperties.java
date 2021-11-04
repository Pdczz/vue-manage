package com.example.dc.config;

import com.example.dc.common.utils.RsaUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

@Data
@ConfigurationProperties(prefix="dc.jwt")
public class JwtProperties {
    private String secret;//密钥

    private String pubKeyPath;//公钥

    private String priKeyPath;//私钥

    private int expire;//token过期时间

    private String cookieName;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    //对象一旦实例化后，就应该读取公钥和私钥
    @PostConstruct
    public void init() throws Exception{
        //构造函数执行完成后执行
        //公钥私钥不存在，先生成
        File pubkeyPath=new File(pubKeyPath);
        File prikeyPath=new File(priKeyPath);
        if(!pubkeyPath.exists()||!prikeyPath.exists()){
            RsaUtils.generateKey(pubKeyPath,priKeyPath,secret);
        }
        //读取公钥和私钥
        this.publicKey=RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey=RsaUtils.getPrivateKey(priKeyPath);
    }

}
