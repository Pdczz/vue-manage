package com.example.dc.pojo.media;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Author: mrt.
 * @Description:
 * @Date:Created in 2018/1/24 10:04.
 * @Modified By:
 */
@Data
@ToString
@Table(name = "media_file")
public class MediaFile {
    /*
    文件id、名称、大小、文件类型、文件状态（未上传、上传完成、上传失败）、上传时间、视频处理方式、视频处理状态、hls_m3u8,hls_ts_list、课程视频信息（课程id、章节id）
     */
    //文件id
    @Id
    @Column(name = "fileId")
    private String fileId;
    //文件名称
    @Column(name = "fileName")
    private String fileName;
    //文件原始名称
    @Column(name = "fileOriginalName")
    private String fileOriginalName;
    //文件路径
    @Column(name = "filePath")
    private String filePath;
    //文件url
    @Column(name = "fileUrl")
    private String fileUrl;
    //文件类型
    @Column(name = "fileType")
    private String fileType;
    //mimetype
    @Column(name = "mimeType")
    private String mimeType;
    //文件大小
    @Column(name = "fileSize")
    private Long fileSize;
    //文件状态
    @Column(name = "fileStatus")
    private String fileStatus;
    //上传时间
    @Column(name = "uploadTime")
    private Date uploadTime;
    //处理状态
    @Column(name = "processStatus")
    private String processStatus;
    //hls处理
    @Column(name = "mediaFileProcess_m3u8")
    private String mediaFileProcess_m3u8;//m3u8的list集合

    //tag标签用于查询
    @Column(name = "tag")
    private String tag;
    @Column(name = "cid")
    private Integer cid;

}
