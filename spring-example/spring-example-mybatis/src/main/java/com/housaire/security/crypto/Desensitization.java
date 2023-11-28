package com.housaire.security.crypto;

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.*;

/**
 * Desc:
 *
 * @author: Zhang Kai
 * @email: <a href="mailto:zhangkai@kltb.com.cn">zhangkai@kltb.com.cn</a>
 * @date: 2020/8/19
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Desensitization {

    /**
     * 脱敏类型
     * @return
     */
    Type value();

    public static enum Type {
        CHINESE_NAME {
            @Override
            public String desensitize(String originValue) {
                return DesensitizeUtils.chineseName(originValue);
            }
        },
        ID_CARD {
            @Override
            public String desensitize(String originValue) {
                return DesensitizeUtils.idCardNum(originValue);
            }
        },
        FIXED_PHONE {
            @Override
            public String desensitize(String originValue) {
                return DesensitizeUtils.fixedPhone(originValue);
            }
        },
        MOBILE_PHONE {
            @Override
            public String desensitize(String originValue) {
                return DesensitizeUtils.mobilePhone(originValue);
            }
        },
        ADDRESS {
            @Override
            public String desensitize(String originValue) {
                int sensitiveSize = 0;
                if (StringUtils.isNotBlank(originValue) && originValue.length() > 1) {
                    sensitiveSize = originValue.length() / 2;
                }
                return DesensitizeUtils.address(originValue, sensitiveSize);
            }
        },
        EMAIL {
            @Override
            public String desensitize(String originValue) {
                return DesensitizeUtils.email(originValue);
            }
        },
        BANK_CARD {
            @Override
            public String desensitize(String originValue) {
                return DesensitizeUtils.bankCard(originValue);
            }
        },
        CNAPS_CODE {
            @Override
            public String desensitize(String originValue) {
                return DesensitizeUtils.cnapsCode(originValue);
            }
        };

        public abstract String desensitize(String originValue);

    }

}
