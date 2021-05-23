package com.example.dc.controller;

import com.example.dc.common.vo.PageResult;
import com.example.dc.pojo.JotterArticle;
import com.example.dc.service.JotterArticleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api")
public class JotterArticleController {
    @Autowired
    private JotterArticleService jotterArticleService;

    /**
     * 新增
     * @param article
     * @return
     */
    @PostMapping("admin/content/article")
    public ResponseEntity<String> saveArticle(@RequestBody JotterArticle article) {
        jotterArticleService.insert(article);
        return ResponseEntity.ok("保存成功");
    }

    /**
     * 前台分页查询
     * @param size
     * @param page
     * @return
     */
    @GetMapping("article/{size}/{page}")
    public ResponseEntity<PageResult<JotterArticle>> listArticles(@PathVariable("size") Integer size, @PathVariable("page") Integer page) {
        return ResponseEntity.ok(jotterArticleService.list(page, size));
    }
    /**
     * 后台分页查询
     * @param size
     * @param page
     * @return
     */
    @GetMapping("adminArticle/{size}/{page}")
    public ResponseEntity<PageResult<JotterArticle>> listAdminArticles(@PathVariable("size") Integer size, @PathVariable("page") Integer page) {
        return ResponseEntity.ok(jotterArticleService.list(page, size));
    }


    @GetMapping("/page")
    public ResponseEntity<PageResult<JotterArticle>> listArticles2(@RequestParam("pid") Integer pid) {
        return ResponseEntity.ok(jotterArticleService.list(pid, 9));
    }


    /**
     * 根据id查询文章
     * @param id
     * @return
     */
    @GetMapping("article")
    public ResponseEntity<JotterArticle> getOneArticle(@RequestParam("id") Integer id) {
        return ResponseEntity.ok(jotterArticleService.findById(id));
    }

    /**
     * 根据id删除文章
     * @param id
     * @return
     */
    @DeleteMapping("admin/content/article/{id}")
    public ResponseEntity<String> deleteArticle(@PathVariable("id") Integer id) {
        jotterArticleService.delete(id);
        return ResponseEntity.ok("删除成功");
    }




}
