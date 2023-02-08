package com.housaire.security.crypto;

import java.lang.annotation.*;

/**
 * 加密注解
 * 
 * @author Zhang Kai
 */
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Encryption {
	
	/**
	 * key
	 * @return
	 */
	String key() default "enc";

	/**
	 * 加密器
	 * @return
	 */
	Class<? extends Encryptor> encryptor() default AESCryptor.class;
	
}
