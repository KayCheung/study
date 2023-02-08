package com.housaire.security.crypto;

import java.lang.annotation.*;

/**
 * 注解在引用类型和集合类型上，来告知系统是否需要解析并进行加解密
 * 
 * @author Zhang Kai
 */
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Crypto {
}
