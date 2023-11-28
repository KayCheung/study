package com.housaire.security.utils;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author Zhang Kai
 */
public final class AESUtil {

    private AESUtil() { }

    /**
     * 加密
     *
     * @param value
     * @param password
     * @return
     * @throws Exception
     */
    public static String encrypt(String value, String password) throws Exception {
        // 判断password为空null
        if (StringUtils.isBlank(password)) {
            return null;
        }
        // "算法/模式/补码方式"
        Cipher cipher = getCipher(password, Cipher.ENCRYPT_MODE);
        byte[] encrypted = cipher.doFinal(value.getBytes("utf-8"));
        // 此处使用BASE64做转码功能，同时能起到2次加密的作用。
        return Base64.getEncoder().encodeToString(encrypted).replaceAll("=", ".");
    }

    /**
     * 解密
     *
     * @param value
     * @param password
     * @return
     * @throws Exception
     */
    public static String decrypt(String value, String password) throws Exception {
        // 判断password是否为空
        if (StringUtils.isBlank(password)) {
            return null;
        }
        Cipher cipher = getCipher(password, Cipher.DECRYPT_MODE);
        String s = value.replaceAll("\\.", "=");
        // 先用base64解密
        byte[] encrypted1 = Base64.getDecoder().decode(s);
        byte[] original = cipher.doFinal(encrypted1);
        String originalString = new String(original, "utf-8");
        return originalString;
    }

    private static Cipher getCipher(String password, int mode)
            throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(password.getBytes());
        kgen.init(128, random);
        SecretKey secretKey = kgen.generateKey();
        SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(mode, skeySpec);
        return cipher;
    }

}