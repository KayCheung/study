package com.housaire.security.crypto;

import java.lang.annotation.*;

/**
 * 解密注解
 * 
 * @author Zhang Kai
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Decryption {
	
	/**
	 * key
	 * @return
	 */
	String key() default "enc";
	
	/**
	 * 解密器
	 * @return
	 */
	Class<? extends Decryptor> decryptor() default AESCryptor.class;

}
