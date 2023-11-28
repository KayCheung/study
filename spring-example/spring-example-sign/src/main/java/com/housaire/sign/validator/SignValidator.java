package com.housaire.sign.validator;

import com.housaire.sign.SignContext;

import java.util.Map;

/**
 * @author: Zhang Kai
 * @email: <a href="mailto:zhangkai@kltb.com.cn">zhangkai@kltb.com.cn</a>
 * @date: 2020/5/12
 */
public interface SignValidator {

    /**
     * 签名验证
     * @param context
     * @return
     */
    boolean validate(SignContext context);

    /**
     * 获取签名上下文
     * @param parameterMap
     * @return
     */
    SignContext getSignContext(Map<String, String[]> parameterMap);

}
