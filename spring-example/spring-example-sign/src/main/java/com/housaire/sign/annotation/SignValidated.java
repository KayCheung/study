package com.housaire.sign.annotation;

import com.housaire.sign.validator.SignValidator;

import java.lang.annotation.*;

/**
 * @author: Zhang Kai
 * @email: <a href="mailto:zhangkai@kltb.com.cn">zhangkai@kltb.com.cn</a>
 * @date: 2020/5/12
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SignValidated {

    Class<? extends SignValidator> value();

}
