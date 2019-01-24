package com.housaire.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@Data
public class ResponseBaseDto implements Serializable
{

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    private int code;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;

    public static ResponseBaseDto success(Object data) {
        ResponseBaseDto responseInfo = new ResponseBaseDto();
        responseInfo.setCode(0);
        responseInfo.setData(data);
        responseInfo.setMessage("SUCCESS");
        return responseInfo;
    }

    public static ResponseBaseDto error(Integer code) {
        return error(code, null);
    }

    public static ResponseBaseDto error(Integer code, String message) {
        ResponseBaseDto responseInfo = new ResponseBaseDto();
        responseInfo.setCode(code);
        responseInfo.setMessage(message);
        return responseInfo;
    }

}
