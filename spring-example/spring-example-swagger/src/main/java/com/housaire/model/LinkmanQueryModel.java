package com.housaire.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("联系人查询条件")
@Data
public class LinkmanQueryModel
{

    @ApiModelProperty(name = "联系人姓名")
    private String name;

    @ApiModelProperty(name = "客户ID")
    private Integer custId;

    @ApiModelProperty(name = "职务")
    private String job;

    @ApiModelProperty(name = "办公电话")
    private String telephone;

    @ApiModelProperty(name = "手机号码")
    private String cellphone;

    @ApiModelProperty(name = "电子邮件")
    private String email;

}
