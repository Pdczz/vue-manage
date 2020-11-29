package com.example.dc.common.vo;


import com.example.dc.common.enums.ExceptionEnum;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class ExceptionResult {
    private int status;
    private String msg;
    private Long timestamp;

    public ExceptionResult(ExceptionEnum e) {
        this.status=e.getCode();
        this.msg=e.getMsg();
        this.timestamp=System.currentTimeMillis();
    }


}
