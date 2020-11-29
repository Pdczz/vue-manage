package com.example.dc.controller;

import com.example.dc.pojo.AdminMenu;
import com.example.dc.service.AdminMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api")
public class MenuController {
    @Autowired
    private AdminMenuService adminMenuService;
    @GetMapping("menu")
    public ResponseEntity<List<AdminMenu>> menu() {
        return ResponseEntity.ok(adminMenuService.getMenusByCurrentUser());
    }
}
