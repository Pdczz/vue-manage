package com.example.dc.pojo;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer cid;
    private String cover;
    private String title;
    private String author;
    private String date;
    private String press;
    private String abs;
    @Transient
    private String cname;


}
