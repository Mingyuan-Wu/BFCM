package com.seanco.bfcm.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseInfo {
    private long code;
    private String msg;
    private Object obj;

    public static ResponseInfo success() {
        return new ResponseInfo(ResponseInfoEnum.SUCCESS.getCode(), ResponseInfoEnum.SUCCESS.getMsg(), null);
    }

    public static ResponseInfo success(Object obj) {
        return new ResponseInfo(ResponseInfoEnum.SUCCESS.getCode(), ResponseInfoEnum.SUCCESS.getMsg(), obj);
    }

    public static ResponseInfo error(ResponseInfoEnum responseInfoEnum) {
        return new ResponseInfo(responseInfoEnum.getCode(), responseInfoEnum.getMsg(), null);
    }

    public static ResponseInfo error(ResponseInfoEnum responseInfoEnum, Object obj) {
        return new ResponseInfo(responseInfoEnum.getCode(), responseInfoEnum.getMsg(), obj);
    }
}
