package com.housaire.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("客户列表信息")
public class CustomerListModel
{
    @ApiModelProperty(name = "客户ID")
    private Integer id;

    @ApiModelProperty(name = "客户名称")
    private String name;

    @ApiModelProperty(name = "客户类型")
    private String type;

    @ApiModelProperty(name = "注册资金")
    private BigDecimal registeredCapital;

    @ApiModelProperty(name = "注册日期")
    private Date registrationDate;

    @ApiModelProperty(name = "规模")
    private String scale;

    @ApiModelProperty(name = "主营产品")
    private String products;

    @ApiModelProperty(name = "业务负责人姓名")
    private String followUser;

    @ApiModelProperty(name = "统一社会信用代码")
    private String creditCode;

    @ApiModelProperty(name = "企业认证状态")
    private String enterpriseAuthStatus;

    @ApiModelProperty(name = "CA认证状态")
    private String caAuthStatus;

    @ApiModelProperty(name = "银行认证状态")
    private String bankAuthStatus;

    @ApiModelProperty(name = "客户评级")
    private String rating;

}
