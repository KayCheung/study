package com.housaire.security.crypto;

/**
 * 解密接口
 * 
 * @author Zhang Kai
 */
public interface Decryptor extends Cryptor {

	String decrypt(String value);
	
}
