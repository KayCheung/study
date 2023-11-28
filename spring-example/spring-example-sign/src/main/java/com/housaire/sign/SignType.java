package com.housaire.sign;

import com.housaire.sign.algorithm.Encryptor;
import com.housaire.sign.algorithm.MD5Encryptor;
import com.housaire.sign.algorithm.RSA2Encryptor;

/**
 * @author: Zhang Kai
 * @email: <a href="mailto:zhangkai@kltb.com.cn">zhangkai@kltb.com.cn</a>
 * @date: 2020/5/12
 */
public enum SignType {

    MD5 {
        private Encryptor encryptor = new MD5Encryptor();
        @Override
        public Encryptor getEncryptor() {
            return encryptor;
        }
    },

    RSA2 {
        private Encryptor encryptor = new RSA2Encryptor();
        @Override
        public Encryptor getEncryptor() {
            return encryptor;
        }
    };

    public abstract Encryptor getEncryptor();

}
