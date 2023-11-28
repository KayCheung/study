package com.housaire.sign.algorithm;

import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;

/**
 * @author: Zhang Kai
 * @email: <a href="mailto:zhangkai@kltb.com.cn">zhangkai@kltb.com.cn</a>
 * @date: 2020/5/12
 */
public class MD5Encryptor implements Encryptor {

    @Override
    public String encrypt(String value) {
        try {
            return DigestUtils.md5DigestAsHex(value.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
