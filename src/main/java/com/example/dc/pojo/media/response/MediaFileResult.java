package com.example.dc.pojo.media.response;

import com.example.dc.common.response.ResponseResult;
import com.example.dc.common.response.ResultCode;
import com.example.dc.pojo.media.MediaFile;
import lombok.Data;

/**
 * Created by mrt on 2018/3/31.
 */
@Data
public class MediaFileResult extends ResponseResult {

    MediaFile mediaFile;
    public MediaFileResult(ResultCode resultCode, MediaFile mediaFile) {
        super(resultCode);
        this.mediaFile = mediaFile;
    }

    public MediaFileResult() {
    }
}
