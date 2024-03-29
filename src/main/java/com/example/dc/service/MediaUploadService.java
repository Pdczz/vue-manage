package com.example.dc.service;


import com.example.dc.common.enums.ExceptionEnum;
import com.example.dc.common.exception.LyException;
import com.example.dc.common.response.CommonCode;
import com.example.dc.common.response.ResponseResult;
import com.example.dc.mapper.DocumentCategoryMapper;
import com.example.dc.mapper.DocumentTypeMapper;
import com.example.dc.mapper.MediaFileMapper;
import com.example.dc.pojo.DocumentType;
import com.example.dc.pojo.media.MediaFile;
import com.example.dc.pojo.media.response.CheckChunkResult;
import com.example.dc.pojo.media.response.MediaCode;
import com.example.dc.pojo.media.response.MediaExist;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

@Service
public class MediaUploadService {
    @Autowired
    MediaFileMapper mediaFileMapper;
    @Autowired
    DocumentCategoryMapper documentCategoryMapper;
    @Autowired
    DocumentTypeMapper documentTypeMapper;

    //上传文件根目录
    @Value("${xc-service-manage-media.upload-location}")
    String uploadPath;

    /*@Autowired
    private RabbitTemplate rabbitTemplate;*/

   /* @Value("${xc-service-manage-media.mq.routingkey-media-video}")
    public  String routingkey_media_video;*/

    /*** 根据文件md5得到文件路径 * 规则：
     *  * 一级目录：md5的第一个字符
     *  * 二级目录：md5的第二个字符
     *  * 三级目录：md5
     *  * 文件名：md5+文件扩展名
     *  * @param fileMd5 文件md5值
     *  * @param fileExt 文件扩展名
     *  * @return 文件路径 */
    private String getFilePath(String fileMd5, String fileExt){
        String filePath = uploadPath + "/" + fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 + "/" + fileMd5 + "." + fileExt;
        return filePath;
    }
    //得到文件目录相对路径，路径中去掉根目录
    private String getFileFolderRelativePath(String fileMd5, String fileExt){
        String filePath = fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 + "/";
        return filePath;
    }


    //得到文件所在目录
    private String getFileFolderPath(String fileMd5){
        String fileFolderPath = uploadPath + "/" + fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 ;/*+ "/"*/
        return fileFolderPath;
    }
    //创建文件目录
    private boolean createFileFold(String fileMd5){
        //创建上传文件目录
        String fileFolderPath = getFileFolderPath(fileMd5);
        File fileFolder = new File(fileFolderPath);
        if (!fileFolder.exists()) {
            //创建文件夹
            boolean mkdirs = fileFolder.mkdirs();
            return mkdirs;
        }
        return true;
    }



    public ResponseResult register(String fileMd5, String fileName, Long fileSize) {
        //检查文件是否上传
        // 1、得到文件的路径

        String substring = fileName.substring(fileName.lastIndexOf(".")+1);
        String fileExt=substring;
        String filePath = getFilePath(fileMd5, fileExt);
        File file = new File(filePath);
        //2、查询数据库文件是否存在
        MediaFile mediaFile=new MediaFile();
        mediaFile.setFileId(fileMd5);

        MediaFile selectOne = mediaFileMapper.selectOne(mediaFile);
//        Optional<MediaFile> optional = mediaFileRepository.findById(fileMd5);
        //文件存在直接返回
        if(file.exists() && selectOne!=null){
            throw new LyException(ExceptionEnum.UPLOAD_FILE_REGISTER_EXIST);
        }
        boolean fileFold = createFileFold(fileMd5);
        if(!fileFold){
            //上传文件目录创建失败

            throw new LyException(ExceptionEnum.UPLOAD_FILE_REGISTER_FAIL);
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }
    //得到块文件所在目录
    private String getChunkFileFolderPath(String fileMd5) {
        String fileChunkFolderPath = getFileFolderPath(fileMd5) + "/" + "chunks" + "/";
        return fileChunkFolderPath;
    }
    //检查块文件
   /* public CheckChunkResult checkchunk(String fileMd5, Integer chunk, Integer chunkSize) {
        //得到块文件所在路径
        String chunkfileFolderPath = getChunkFileFolderPath(fileMd5);
        //块文件的文件名称以1,2,3..序号命名，没有扩展名
        File chunkFile = new File(chunkfileFolderPath+chunk);
        if(chunkFile.exists()){
            return new CheckChunkResult(MediaCode.CHUNK_FILE_EXIST_CHECK,true);
        }else{
            return new CheckChunkResult(MediaCode.CHUNK_FILE_EXIST_CHECK,false);
        }
    }*/
    //块文件上传
    public ResponseResult uploadchunk(MultipartFile file, String fileMd5, Integer chunk) {
        if(file == null){
            throw new LyException(ExceptionEnum.UPLOAD_FILE_REGISTER_FAIL);
        }
        //创建块文件目录
        boolean fileFold = createChunkFileFolder(fileMd5);
        //块文件
        File chunkfile = new File(getChunkFileFolderPath(fileMd5) + chunk);
        //上传的块文件
        InputStream inputStream= null;
        FileOutputStream outputStream = null;
        try {
            inputStream = file.getInputStream();
            outputStream = new FileOutputStream(chunkfile);
            IOUtils.copy(inputStream,outputStream);
        } catch (Exception e) {
            e.printStackTrace();
            throw new LyException(ExceptionEnum.CHUNK_FILE_EXIST_CHECK);
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }
        //创建块文件目录
     private boolean createChunkFileFolder(String fileMd5){
        //创建上传文件目录
         String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
         File chunkFileFolder = new File(chunkFileFolderPath);
         if (!chunkFileFolder.exists()) {
             //创建文件夹
             boolean mkdirs = chunkFileFolder.mkdirs(); return mkdirs;
         }
         return true;
    }


    public ResponseResult mergechunks(String fileMd5, String fileName, Long fileSize) {
        //获取块文件的路径
        String chunkfileFolderPath = getChunkFileFolderPath(fileMd5);
        File chunkfileFolder = new File(chunkfileFolderPath);
        if(!chunkfileFolder.exists()){
            chunkfileFolder.mkdirs();
        }
        String substring = fileName.substring(fileName.lastIndexOf(".")+1);
        String fileExt=substring;
        //合并文件路径
        File mergeFile = new File(getFilePath(fileMd5,fileExt));
        //创建合并文件 //合并文件存在先删除再创建
        if(mergeFile.exists()){
            mergeFile.delete();
        }

        boolean newFile = false;
        try {
            newFile = mergeFile.createNewFile();
        } catch (IOException e) {
            System.out.println("185mergeFile fail");
            e.printStackTrace();
        }

        if(!newFile){
            System.out.println("newFile fail");
            throw new LyException(ExceptionEnum.MERGE_FILE_CHECKFAIL);

        }
        //获取块文件，此列表是已经排好序的列表
        List<File> chunkFiles = getChunkFiles(chunkfileFolder);
        //合并文件
        mergeFile = mergeFile(mergeFile, chunkFiles);
        if(mergeFile == null){
            System.out.println("mergeFile fail");
            throw new LyException(ExceptionEnum.MERGE_FILE_FAIL);
        }
        //校验文件
        boolean checkResult = this.checkFileMd5(mergeFile, fileMd5);
        if(!checkResult){
            System.out.println("chechResult");
            throw new LyException(ExceptionEnum.MERGE_FILE_CHECKFAIL);
        }
        //将文件信息保存到数据库
        MediaFile mediaFile = new MediaFile();
        mediaFile.setFileId(fileMd5);
        MediaFile mediaFileFromDataBase = mediaFileMapper.selectOne(mediaFile);
        //查看数据库是否有记录
        if (mediaFileFromDataBase!=null){
            throw new LyException(ExceptionEnum.UPLOAD_FILE_DATABASE_EXIST);
        }
        Integer cidByType = documentTypeMapper.findCidByType(fileExt);
        if (cidByType!=null){
            mediaFile.setCid(cidByType);
        }else {
            mediaFile.setCid(6);
        }
        mediaFile.setFileName(fileMd5+"."+fileExt);
        mediaFile.setFileOriginalName(fileName);
        //文件路径保存相对路径
        mediaFile.setFilePath(getFileFolderRelativePath(fileMd5,fileExt));
        mediaFile.setFileSize(fileSize);
        mediaFile.setUploadTime(new Date());
        mediaFile.setMimeType("");
        mediaFile.setFileType(fileExt);
        //状态为上传成功
        mediaFile.setFileStatus("301002");
        int insert = mediaFileMapper.insert(mediaFile);
        if (insert==-1){
            System.out.println("insert fail");
            throw new LyException(ExceptionEnum.MERGE_FILE_CHECKFAIL);
        }
//        MediaFile save = mediaFileRepository.save(mediaFile);
        //发送mq消息
        //sendProcessVideoMsg(mediaFile.getFileId());

        return new ResponseResult(CommonCode.SUCCESS);
    }
   /* //发送视频处理消息
    public ResponseResult sendProcessVideoMsg(String mediaId){
        Optional<MediaFile> optional = mediaFileRepository.findById(mediaId);
        if (!optional.isPresent()){
            ExceptionCast.cast(CommonCode.FAIL);
        }
        Map<String,String> map=new HashMap<>();
        map.put("mediaId",mediaId);
        String jsonString = JSON.toJSONString(map);
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.EX_MEDIA_PROCESSTASK,routingkey_media_video,jsonString);
        }catch (Exception e){
            e.printStackTrace();
            ExceptionCast.cast(CommonCode.FAIL);
        }

        return new ResponseResult(CommonCode.SUCCESS);
    }
*/


    //校验文件的md5值
    private boolean checkFileMd5(File mergeFile, String md5){
        if(mergeFile == null || StringUtils.isEmpty(md5)){
            return false;
        }
        long length = mergeFile.length();
        //进行md5校验
        FileInputStream mergeFileInputstream = null;
        try {
            mergeFileInputstream = new FileInputStream(mergeFile);
            //得到文件的md5
            String mergeFileMd5 = DigestUtils.md5Hex(mergeFileInputstream);


            //比较md5
            if(md5.equalsIgnoreCase(mergeFileMd5)){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }finally{
            try {
                mergeFileInputstream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }return false;
    }
    //获取所有块文件
    private List<File> getChunkFiles(File chunkfileFolder) {
        //获取路径下的所有块文件
        File[] chunkFiles = chunkfileFolder.listFiles();
        //将文件数组转成list，并排序
        List<File> chunkFileList = new ArrayList<File>();
        chunkFileList.addAll(Arrays.asList(chunkFiles));
        //排序
        Collections.sort(chunkFileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName())) {
                    return 1;
                }
                return -1;
            }
        });
        return chunkFileList;
    }
    //合并文件
    private File mergeFile(File mergeFile, List<File> chunkFiles){
        try {
            //创建写文件对象
            RandomAccessFile raf_write = new RandomAccessFile(mergeFile,"rw");
            //遍历分块文件开始合并
            // 读取文件缓冲区
            byte[] b = new byte[1024];
            for(File chunkFile:chunkFiles){
                RandomAccessFile raf_read = new RandomAccessFile(chunkFile,"r");
                int len = -1;
                //读取分块文件
                while((len = raf_read.read(b))!=-1){
                    //向合并文件中写数据
                    raf_write.write(b,0,len);
                }raf_read.close();
            }
            raf_write.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return mergeFile;
    }
    public ResponseResult register2(String fileMd5, Integer chunk, String fileName) {
        //检查文件是否上传
        // 1、得到文件的路径

        String substring = fileName.substring(fileName.lastIndexOf(".")+1);
        String fileExt=substring;
        String filePath = getFilePath(fileMd5, fileExt);
        File file = new File(filePath);
        //2、查询数据库文件是否存在
        MediaFile mediaFile=new MediaFile();
        mediaFile.setFileId(fileMd5);

        MediaFile selectOne = mediaFileMapper.selectOne(mediaFile);
        //文件存在直接返回
        if(file.exists() && selectOne!=null){
            return new ResponseResult(true,22002,"skipUpload");
        }
        //检查是否有chunks文件夹，如果有则说明上传过分块，如果没有则直接上传
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        File chunkFileFolder = new File(chunkFileFolderPath);
        if (chunkFileFolder.exists()){
            //得到块文件所在路径
            String chunkfileFolderPath = getChunkFileFolderPath(fileMd5);
            //块文件的文件名称以1,2,3..序号命名，没有扩展名
            File[] files = chunkFileFolder.listFiles();
            int length = files.length;
            File chunkFile = new File(chunkfileFolderPath+chunk);
            List<Integer> exitChunks=new ArrayList<>();
            for (int i = 1; i < length; i++) {
                exitChunks.add(i);
            }
            return new CheckChunkResult(CommonCode.SUCCESS,exitChunks);
        }
        boolean fileFold = createFileFold(fileMd5);
        if(!fileFold){
            //上传文件目录创建失败
            throw new LyException(ExceptionEnum.UPLOAD_FILE_REGISTER_FAIL);
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    public List<MediaFile> queryAllMedia(){
        return mediaFileMapper.selectAll();
    }
    public List<MediaFile> queryMediaByCategory(Integer cid){
        MediaFile mediaFile = new MediaFile();
        mediaFile.setCid(cid);
        return mediaFileMapper.select(mediaFile);
    }

}








