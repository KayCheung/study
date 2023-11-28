package com.housaire.sign;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author: Zhang Kai
 * @email: <a href="mailto:zhangkai@kltb.com.cn">zhangkai@kltb.com.cn</a>
 * @date: 2020/5/12
 */
public interface SignContext {

    Map<String, String[]> getParameterMap();

    String getSign();

    String getSecret();

    SignType getSignType();

    Long getTimestamp();

    default String getValue(String key) {
        String[] values = this.getParameterMap().get(key);
        if(ArrayUtils.isEmpty(values) || StringUtils.isBlank(values[0])) {
            throw new RuntimeException("缺少验签参数: " + key);
        }
        return values[0];
    }

}
