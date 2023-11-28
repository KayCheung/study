package com.housaire.security.crypto;

import java.lang.annotation.*;

/**
 * 加解密注解
 * 
 * @author Zhang Kai
 */
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cryption {

	Class<? extends Cryptor> value() default AESCryptor.class;

	String salt() default "cryption";
	
}
