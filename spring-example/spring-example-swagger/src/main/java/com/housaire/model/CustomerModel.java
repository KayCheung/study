package com.housaire.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("客户信息")
public class CustomerModel
{

    @ApiModelProperty(name = "客户ID")
    private Integer id;

    @ApiModelProperty(name = "客户名称")
    private String name;

    @ApiModelProperty(name = "客户别名")
    private String alias;

    @ApiModelProperty(name = "英文名称")
    private String englishName;

    @ApiModelProperty(name = "客户类型")
    private Integer type;

    @ApiModelProperty(name = "所属省份")
    private Integer province;

    @ApiModelProperty(name = "所属城市")
    private Integer city;

    @ApiModelProperty(name = "所属区县")
    private Integer county;

    @ApiModelProperty(name = "地址")
    private String address;

    @ApiModelProperty(name = "客户电话")
    private String telephone;

    @ApiModelProperty(name = "传真")
    private String fax;

    @ApiModelProperty(name = "门户网站")
    private String website;

    @ApiModelProperty(name = "注册日期")
    private Date registrationDate;

    @ApiModelProperty(name = "注册资金")
    private BigDecimal registeredCapital;

    @ApiModelProperty(name = "规模")
    private Integer scale;

    @ApiModelProperty(name = "客户来源")
    private Integer source;

    @ApiModelProperty(name = "主营产品")
    private Integer products;

    @ApiModelProperty(name = "年产量")
    private Integer yearOutput;

    @ApiModelProperty(name = "年交易量")
    private Integer tradeVolume;

    @ApiModelProperty(name = "业务负责人ID")
    private Integer followUserId;

    @ApiModelProperty(name = "统一社会信用代码")
    private String creditCode;

}
