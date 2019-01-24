package com.housaire.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("联系人列表信息")
public class LinkmanListModel
{

    @ApiModelProperty(name = "联系人ID")
    private Integer id;

    @ApiModelProperty(name = "联系人姓名")
    private String name;

    @ApiModelProperty(name = "客户名称")
    private String custName;

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
