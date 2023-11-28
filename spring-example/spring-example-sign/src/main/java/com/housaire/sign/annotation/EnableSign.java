package com.housaire.sign.annotation;

import com.housaire.sign.SignConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Desc:
 *
 * @author: Zhang Kai
 * @email: <a href="mailto:zhangkai@kltb.com.cn">zhangkai@kltb.com.cn</a>
 * @date: 2020/5/19
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import(SignConfiguration.class)
public @interface EnableSign {
}
