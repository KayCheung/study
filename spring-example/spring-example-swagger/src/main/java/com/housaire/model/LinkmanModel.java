package com.housaire.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel("联系人信息")
@Data
public class LinkmanModel
{

    @ApiModelProperty(name = "联系人ID")
    private Integer id;

    @ApiModelProperty(name = "客户ID")
    private Integer custId;

    @ApiModelProperty(name = "联系人姓名")
    private String name;

    @ApiModelProperty(name = "生日")
    private Date birthday;

    @ApiModelProperty(name = "部门")
    private String department;

    @ApiModelProperty(name = "职务")
    private String job;

    @ApiModelProperty(name = "办公电话")
    private String telephone;

    @ApiModelProperty(name = "手机号码")
    private String cellphone;

    @ApiModelProperty(name = "电子邮件")
    private String email;

    @ApiModelProperty(name = "备注")
    private String remark;

}
