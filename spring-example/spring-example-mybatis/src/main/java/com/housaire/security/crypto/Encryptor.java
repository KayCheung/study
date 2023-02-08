package com.housaire.security.crypto;

/**
 * 加密器接口
 * 
 * @author Zhang Kai
 */
public interface Encryptor extends Cryptor {

	String encrypt(String value);
	
}
