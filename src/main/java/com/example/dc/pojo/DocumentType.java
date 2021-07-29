package com.example.dc.pojo;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "document_type")
public class DocumentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String dtype;
    @Column(name = "document_cid")
    private Integer document_cid;
}
