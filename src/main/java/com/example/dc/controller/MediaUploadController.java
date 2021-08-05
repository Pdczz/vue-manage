package com.example.dc.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.dc.common.response.CommonCode;
import com.example.dc.common.response.QueryResponseResult;
import com.example.dc.common.response.QueryResult;
import com.example.dc.common.response.ResponseResult;
import com.example.dc.pojo.media.MediaFile;
import com.example.dc.pojo.media.response.CheckChunkResult;
import com.example.dc.service.MediaUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
    //查看是否有分块，如果有则从分块数量-1开始上传
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

    @GetMapping("/mediaList")
    public QueryResponseResult<MediaFile> queryMedia(){
        List<MediaFile> mediaFiles = mediaUploadService.queryAllMedia();
        QueryResult<MediaFile> mediaFileQueryResult = new QueryResult<MediaFile>();
        mediaFileQueryResult.setList(mediaFiles);
        if (!CollectionUtils.isEmpty(mediaFiles)){
            mediaFileQueryResult.setTotal(mediaFiles.size());
        }
        return new QueryResponseResult<>(CommonCode.SUCCESS,mediaFileQueryResult);
    }

    @GetMapping("/category/mediaList/{cid}")
    public QueryResponseResult<MediaFile> queryMediaByCategory(@PathVariable("cid") Integer cid){
        List<MediaFile> mediaFiles=new ArrayList<>();
        if (cid==0){
            mediaFiles = mediaUploadService.queryAllMedia();
        }else {
            mediaFiles = mediaUploadService.queryMediaByCategory(cid);
        }

        QueryResult<MediaFile> mediaFileQueryResult = new QueryResult<MediaFile>();
        mediaFileQueryResult.setList(mediaFiles);
        if (!CollectionUtils.isEmpty(mediaFiles)){
            mediaFileQueryResult.setTotal(mediaFiles.size());
        }
        return new QueryResponseResult<>(CommonCode.SUCCESS,mediaFileQueryResult);
    }

    @RequestMapping("/preview1")
    public void er(HttpServletResponse response){
        File file = new File("F:\\develop\\video\\d\\f\\df4db83c81f32e4bbf2f5ba5ded3cf7b\\df4db83c81f32e4bbf2f5ba5ded3cf7b.png");
        if (file.exists()){
            byte[] data = null;
            try {
                FileInputStream input = new FileInputStream(file);
                data = new byte[input.available()];
                input.read(data);
                response.getOutputStream().write(data);
                input.close();
            } catch (Exception e) {
                System.out.println(e);
            }

        }else{
            return;
        }

    }
    @ResponseBody
    @RequestMapping("/previewPDF")
    public void findPdf(@RequestParam("path") String path,@RequestParam("Name") String Name, HttpServletResponse response) throws IOException{
        response.setContentType("application/pdf");
        FileInputStream in = new FileInputStream(new File("F:\\develop\\video\\7\\3\\73717375addc3a3099431107ed11e61b\\73717375addc3a3099431107ed11e61b.doc"));
        OutputStream out = response.getOutputStream();
        byte[] b = new byte[512];
        while ((in.read(b))!=-1) {
            out.write(b);
        }
        out.flush();
        in.close();
        out.close();
    }

    @ResponseBody
    @RequestMapping("/preview3")
    public void devDoc(HttpServletRequest request, HttpServletResponse response, String storeName) throws Exception {
        request.setCharacterEncoding("UTF-8");
        String ctxPath = request.getSession().getServletContext().getRealPath("");
        String downLoadPath = "F:\\develop\\video\\0\\6\\06fbd76769a7154e119af56e1646ac87\\06fbd76769a7154e119af56e1646ac87.pdf";
        response.setContentType("application/pdf");
        FileInputStream in = new FileInputStream(new File(downLoadPath));
        OutputStream out = response.getOutputStream();
        byte[] b = new byte[1024];
        while ((in.read(b))!=-1) {
            out.write(b);
        }
        out.flush();
        in.close();
        out.close();
    }
    @ResponseBody
    @RequestMapping("/preview")
    public void download( HttpServletResponse response
    ) throws IOException {
        String filePath = "F:\\develop\\video\\0\\6\\06fbd76769a7154e119af56e1646ac87\\06fbd76769a7154e119af56e1646ac87.pdf";
        System.out.println("filePath:" + filePath);
        File f = new File(filePath);
        if (!f.exists()) {
            response.sendError(404, "File not found!");
            return;
        }
        BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
        byte[] bs = new byte[1024];
        int len = 0;
        response.reset(); // 非常重要
        if (true) { // 在线打开方式
            URL u = new URL("file:///" + filePath);
            String contentType = u.openConnection().getContentType();
            response.setContentType(contentType);
            response.setHeader("Content-Disposition", "inline;filename="
                    + "2019年上半年英语四级笔试准考证(戴林峰).pdf");
            // 文件名应该编码成utf-8，注意：使用时，我们可忽略这句
        } else {
            // 纯下载方式
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment;filename="
                    + "2019年上半年英语四级笔试准考证(戴林峰).pdf");
        }
        OutputStream out = response.getOutputStream();
        while ((len = br.read(bs)) > 0) {
            out.write(bs, 0, len);
        }
        out.flush();
        out.close();
        br.close();
    }


}
