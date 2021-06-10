package com.example.dc.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.dc.common.response.ResponseResult;
import com.example.dc.pojo.media.response.CheckChunkResult;
import com.example.dc.service.MediaUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/media/upload")
public class MediaUploadController {
    @Autowired
    MediaUploadService mediaUploadService;

    @PostMapping("/register")
    public ResponseResult register(String identifier, String filename, Long totalSize) {
        return mediaUploadService.register(identifier,filename,totalSize);
    }


   /* @PostMapping("/checkchunk")
    public CheckChunkResult checkchunk(String fileMd5, Integer chunk, Integer chunkSize) {
        return mediaUploadService.checkchunk(fileMd5,chunk,chunkSize);
    }*/


    @PostMapping("/uploadchunk")
    public ResponseResult uploadchunk(MultipartFile file, Integer chunkNumber, String identifier) {
        return mediaUploadService.uploadchunk(file,identifier,chunkNumber);
    }
    @GetMapping("/uploadchunk")
    public ResponseResult uploadchunk(@RequestParam("chunkNumber") Integer chunkNumber, @RequestParam("identifier") String identifier, @RequestParam("filename") String filename) {

        return mediaUploadService.register2(identifier,chunkNumber,filename);
    }


    @PostMapping("/mergechunks")
    public ResponseResult mergechunks(@RequestBody String request) {
        JSONObject jsonObject = JSON.parseObject(request);
        Long fileSize = jsonObject.getLong("fileSize");
        String fileMd5 = jsonObject.getString("fileMd5");
        String fileName = jsonObject.getString("fileName");
        return mediaUploadService.mergechunks(fileMd5,fileName,fileSize);
    }
    @GetMapping("/test")
    @ResponseBody
    public String test(){
        File f=new File("/usr/local/videos/test");
        boolean flag=false;
        try {
            flag = f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (flag){
            return "success";
        }
        return "fail";
    }
}
