package com.example.dc.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "oauth_user")
public class OauthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="app_id")
    private String appId;
    private String username;
    private String avatar;
    private String email;
    @Column(name="create_time")
    private Date createTime;
}
