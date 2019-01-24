package com.housaire.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

@Data
@ApiModel("客户转移")
public class CustomerTransferModel
{

    @ApiModelProperty(name = "业务负责人Id")
    private String followUserId;

    @ApiModelProperty(name = "客户ID")
    private Set<String> custIds;

}
