package com.housaire.loan;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Desc:
 *
 * @Author Zhang Kai
 * @Email <a href="mailto:zhangkai@kltb.com.cn">zhangkai@kltb.com.cn</a>
 * @Date 2023/3/8 0008
 * @Since 1.0
 */
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class LoanOrderController {

    private final LoanOrderService loanOrderService;

    @PostMapping("/apply")
    public ResponseEntity<LoanOrderResponse> apply(@RequestBody LoanOrderRequest request) {
        return ResponseEntity.ok(loanOrderService.apply(request));
    }

    @PostMapping("/pay")
    public ResponseEntity<PayResponse> pay(@RequestBody PayRequest request) {
        return ResponseEntity.ok(loanOrderService.pay(request));
    }

    @PostMapping("/result/{orderId}")
    public ResponseEntity<LoanOrderResponse> result(@PathVariable("orderId") String orderId) {
        return ResponseEntity.ok(loanOrderService.result(orderId));
    }

}
