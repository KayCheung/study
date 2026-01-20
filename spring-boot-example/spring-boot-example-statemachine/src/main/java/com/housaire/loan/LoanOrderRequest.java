package com.housaire.loan;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Desc:
 *
 * @Author Zhang Kai
 * @Email <a href="mailto:zhangkai@kltb.com.cn">zhangkai@kltb.com.cn</a>
 * @Date 2023/3/8 0008
 * @Since 1.0
 */
@Setter
@Getter
@Builder
public class LoanOrderRequest {

    private int goodsId;
    private int sku;
    private BigDecimal price;
    private String userId;

}
