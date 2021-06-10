package com.example.dc.pojo.media.response;

import com.example.dc.common.response.ResponseResult;
import lombok.Data;

import java.util.List;

@Data
public class MediaExist extends ResponseResult {
    private List ExitChunks;
}
