package com.kltb.spring.starter;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Desc:
 *
 * @author: Zhang Kai
 * @email: <a href="mailto:zhangkai@kltb.com.cn">zhangkai@kltb.com.cn</a>
 * @date: 2020/8/10
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(XxlJobImportSelector.class)
public @interface EnableXxlJob {
}
