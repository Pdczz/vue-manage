package com.example.dc.pojo.media.response;

import com.example.dc.common.response.ResponseResult;
import com.example.dc.common.response.ResultCode;
import lombok.Data;
import lombok.ToString;

/**
 * Created by admin on 2018/3/5.
 */
@Data
@ToString
public class CheckChunkResult extends ResponseResult {
    public CheckChunkResult() {
    }

    public CheckChunkResult(boolean fileExist) {
        this.fileExist = fileExist;
    }

    public CheckChunkResult(ResultCode resultCode, boolean fileExist) {
        super(resultCode);
        this.fileExist = fileExist;
    }
//    @ApiModelProperty(value = "文件分块存在标记", example = "true", required = true)
    boolean fileExist;
}
