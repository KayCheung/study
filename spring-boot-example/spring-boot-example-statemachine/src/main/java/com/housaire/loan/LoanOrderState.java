package com.housaire.loan;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Desc:
 *
 * @Author Zhang Kai
 * @Email <a href="mailto:zhangkai@kltb.com.cn">zhangkai@kltb.com.cn</a>
 * @Date 2023/3/8 0008
 * @Since 1.0
 */
@Getter
@RequiredArgsConstructor
public enum LoanOrderState {

    INIT(),
    PAYING(),
    SUCCESS(),
    FAIL()

}
