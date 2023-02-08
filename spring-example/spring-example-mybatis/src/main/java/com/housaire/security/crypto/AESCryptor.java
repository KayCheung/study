package com.housaire.security.crypto;

import com.housaire.security.utils.AESUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

/**
 * AES 加解密
 * 
 * @author Zhang Kai
 */
@Slf4j
public class AESCryptor implements Encryptor, Decryptor {
	
	private String key;
	
	public AESCryptor() {
	}
	
	public AESCryptor(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public String decrypt(String value) {
		try {
			return AESUtil.decrypt(value, getKey());
		} catch (Exception e) {
			log.warn("解密失败", e.getMessage());
			return value;
		}
	}

	@Override
	public String encrypt(String value) {
		try {
			return AESUtil.encrypt(value, getKey());
		} catch (Exception e) {
			log.warn("加密失败", e.getMessage());
			return value;
		}
	}

}
