package com.housaire.sign.validator;

import com.housaire.sign.SignContext;
import com.housaire.sign.SignType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author: Zhang Kai
 * @email: <a href="mailto:zhangkai@kltb.com.cn">zhangkai@kltb.com.cn</a>
 * @date: 2020/5/12
 */
@Slf4j
@Component
public class StandardSignValidator extends AbstractSignValidator {

    private static final String SIGN = "sign";

    private static final String APP_KEY = "appId";

    @Override
    protected void validateTimestamp(SignContext context) {
    }

    @Override
    protected String compose(String name, String value, SignContext context, String secret) {
        return name.concat("=").concat(value).concat("&");
    }

    @Override
    protected String afterSignCompose(String signString, SignContext context, String secret) {
        return signString.concat(secret);
    }

    @Override
    protected boolean skipCompose(String key, String[] values) {
        if (key.equals(SIGN)) {
            return true;
        }
        return false;
    }

    @Override
    public SignContext getSignContext(Map<String, String[]> parameterMap) {
        return new StandardSignContext(parameterMap);
    }

    public static class StandardSignContext implements SignContext {

        private Map<String, String[]> parameterMap;

        public StandardSignContext(Map<String, String[]> parameterMap) {
            this.parameterMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
            this.parameterMap.putAll(parameterMap);
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            return parameterMap;
        }

        @Override
        public String getSign() {
            return getValue(SIGN);
        }

        @Override
        public String getSecret() {
            return getValue(APP_KEY);
        }

        @Override
        public SignType getSignType() {
            return SignType.MD5;
        }

        @Override
        public Long getTimestamp() {
            return null;
        }
    }

}
