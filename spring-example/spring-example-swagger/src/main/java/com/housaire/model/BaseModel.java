package com.housaire.model;


import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

@Data
public class BaseModel
{
    private Long id;

    //  数据是否有效
    private Integer valid;

    //  检索关键字
    private String keywords;

    private Date createdDate;

    private Date updatedDate;

    private Long createdBy;

    private Long updatedBy;

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
