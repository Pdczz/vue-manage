package com.example.dc.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum  ExceptionEnum {

    BOOKS_NOT_FOUND (400,"图书不存在"),
    CATEGORY_NOT_FOUND(404,"图书分类没查到"),
    UPLOAD_FILE_ERROR(500,"上传文件失败"),
    INVALID_FILE_TYPE(400,"无效的文件类型"),
    BOOKS_SAVE_ERROR(500,"图书新增失败"),
    ROLE_SAVE_ERROR(500,"用户新增失败"),
    USER_SAVE_ERROR(500,"用户新增失败"),
    BOOKS_UPDATE_ERROR(500,"图书更新失败"),
    PASSWORD_UPDATE_ERROR(500,"密码更新失败"),
    AUTHRO_FAILE(500,"认证失败"),
    ARTICLE_NOT_FOUND(404,"文章查询失败"),
    ARTICLE_DELETE_ERROR(500,"文章删除失败"),
    UPLOAD_FILE_REGISTER_FAIL(22001,"上传文件在系统注册失败，请刷新页面重试！"),
    UPLOAD_FILE_REGISTER_EXIST(22002,"上传文件在系统已存在！"),
    CHUNK_FILE_EXIST_CHECK(22003,"分块文件在系统已存在！"),
    MERGE_FILE_FAIL(22004,"合并文件失败，文件在系统已存在！"),
    MERGE_FILE_CHECKFAIL(22005,"合并文件校验失败！"),
    UPLOAD_FILE_DATABASE_EXIST(22006,"上传文件在数据库已存在！");


    private int code;
    private String msg;

}
