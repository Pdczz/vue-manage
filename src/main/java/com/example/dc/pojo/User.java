package com.example.dc.pojo;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Table(name = "user")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;
    private String salt;
    private String name;
    private String phone;

    private String email;
    private boolean enabled;
    @Transient
    private List<AdminRole> roles;
}
