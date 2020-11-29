package com.example.dc.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
@Table(name = "syslog")
@Data
public class SysLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private String id;
    private Date visitTime;
    private String visitTimeStr;
    private String username;
    private String ip;
    private String url;
    private Long executionTime;
    private String method;

}
