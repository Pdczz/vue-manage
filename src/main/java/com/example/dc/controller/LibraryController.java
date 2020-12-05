package com.example.dc.controller;

import com.example.dc.common.utils.StringUtils;
import com.example.dc.pojo.Book;
import com.example.dc.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api")
public class LibraryController {
    @Autowired
    private BookService bookService;


    /**
     * 查询所有
     * @return
     */
    @GetMapping("books")
    public ResponseEntity<List<Book>> queryBooks(){
        return ResponseEntity.ok(bookService.queryBooks());
    }
    /**
     * 新增或修改
     * @param book
     * @return
     */
    @PostMapping("admin/content/books")
    public ResponseEntity<Void> addOrEdit(@RequestBody Book book){
        bookService.addOrEdit(book);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    /**
     * 删除
     * @param id
     * @return
     */
    @Transactional
    @PostMapping("admin/content/books/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id")Integer id){
        bookService.deleteById(id);
        return ResponseEntity.ok("删除成功");
    }
    /**
     * 根据分类查询
     * @param cid
     * @return
     */
    @GetMapping("categories/{cid}")
    public ResponseEntity<List<Book>> queryByCategory(@PathVariable("cid")Integer cid){
        return ResponseEntity.ok(bookService.queryByCategory(cid));
    }

    /**
     * 根据关键字查询
     * @param keywords
     * @return
     */
    @GetMapping("search")
    public ResponseEntity<List<Book>> searchResult(@RequestParam("keywords") String keywords) {
        // 关键词为空时查询出所有书籍
        if ("".equals(keywords)) {
            return ResponseEntity.ok(bookService.queryBooks());
        } else {
            return ResponseEntity.ok(bookService.Search(keywords));
        }
    }
    @PostMapping("covers")
    public String coversUpload(MultipartFile file) throws Exception {
        String folder = "/usr/local/img";
        File imageFolder = new File(folder);
        String substring = file.getOriginalFilename()
                .substring(file.getOriginalFilename().length() - 4);
        String s = StringUtils.getRandomString(6) + substring;
        File f = new File(imageFolder,s);
        if (!f.getParentFile().exists())
            f.getParentFile().mkdirs();
        try {
            file.transferTo(f);
            String imgURL = "http://localhost:9090/api/file/" + f.getName();
            return imgURL;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

}
