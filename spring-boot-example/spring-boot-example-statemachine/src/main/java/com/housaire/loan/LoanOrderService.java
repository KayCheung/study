package com.housaire.loan;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Desc:
 *
 * @Author Zhang Kai
 * @Email <a href="mailto:zhangkai@kltb.com.cn">zhangkai@kltb.com.cn</a>
 * @Date 2023/3/8 0008
 * @Since 1.0
 */
@Slf4j
@Service
public class LoanOrderService {

    private Map<String, LoanOrderResponse> orders = new HashMap<>();
    private Map<String, PayRequest> pays = new HashMap<>();

    public LoanOrderResponse apply(LoanOrderRequest request) {
        PayRequest payRequest = PayRequest.builder()
                .payId(IdUtil.fastSimpleUUID())
                .amount(request.getPrice())
                .build();
        LoanOrderResponse order = LoanOrderResponse.builder()
                .orderId(IdUtil.fastSimpleUUID())
                .payId(payRequest.getPayId())
                .price(request.getPrice())
                .state(LoanOrderState.INIT)
                .build();
        orders.put(order.getOrderId(), order);
        pays.put(payRequest.getPayId(), payRequest);
        return order;
    }

    public PayResponse pay(PayRequest request) {
        PayRequest payRequest = pays.get(request.getPayId());
        return PayResponse.builder().payId(request.getPayId()).build();
    }

    public LoanOrderResponse result(String orderId) {
        return orders.get(orderId);
    }

}
