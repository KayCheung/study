package com.housaire.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("客户查询条件")
public class CustomerQueryModel
{

    @ApiModelProperty(name = "客户名称")
    private String name;

    @ApiModelProperty(name = "客户类型")
    private Integer type;

    @ApiModelProperty(name = "业务负责人ID")
    private Integer followUserId;

    @ApiModelProperty(name = "企业认证状态")
    private Integer enterpriseAuthStatus;

    @ApiModelProperty(name = "CA认证状态")
    private Integer caAuthStatus;

    @ApiModelProperty(name = "银行认证状态")
    private Integer bankAuthStatus;

    @ApiModelProperty(name = "客户评级")
    private Integer rating;

    @ApiModelProperty(name = "客户状态")
    private Byte status;

}
