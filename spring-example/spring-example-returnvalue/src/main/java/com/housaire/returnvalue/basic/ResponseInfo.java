package com.housaire.returnvalue.basic;

import lombok.Data;

@Data
public class ResponseInfo
{
    private String code;

    private String msg;

    private Object data;
}
