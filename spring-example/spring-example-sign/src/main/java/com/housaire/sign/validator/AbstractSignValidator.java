package com.housaire.sign.validator;

import com.housaire.sign.SignContext;
import com.housaire.sign.algorithm.Encryptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.Map;

/**
 * @author: Zhang Kai
 * @email: <a href="mailto:zhangkai@kltb.com.cn">zhangkai@kltb.com.cn</a>
 * @date: 2020/5/12
 */
@Slf4j
public abstract class AbstractSignValidator implements SignValidator{

    @Override
    public boolean validate(SignContext context) {
        String secret = context.getSecret();
        Assert.notNull(context, "SignContext不能为空");
        Assert.hasText(secret, "secret不存在");
        // 组装签名串
        String signString = compose(context, secret);
        log.info("服务器组装后的签名串为：{}", signString);
        // 获取签名加密器
        Encryptor encryptor = context.getSignType().getEncryptor();
        // 加密签名串
        String sign = encryptor.encrypt(signString);
        log.info("服务器生成的签名为：{}, 客户端传过来的签名为：{}", sign, context.getSign());
        // 校验时间
        validateTimestamp(context);
        // 比较签名
        return context.getSign().equals(sign.toUpperCase());
    }

    /**
     * 校验时间
     * @param context
     */
    protected abstract void validateTimestamp(SignContext context);

    /**
     * 组装签名串
     * @param name
     * @param value
     * @param context
     * @param secret
     * @return
     */
    protected abstract String compose(String name, String value, SignContext context, String secret);

    /**
     * 组装签名串
     * @param signString
     * @param context
     * @param secret
     * @return
     */
    protected abstract String afterSignCompose(String signString, SignContext context, String secret);

    /**
     * 是否跳过签名串的组装
     * @param key
     * @param values
     * @return
     */
    protected abstract boolean skipCompose(String key, String[] values);

    /**
     * 组装签名串
     * @param context
     * @param secret
     * @return
     */
    private String compose(SignContext context, String secret) {
        StringBuilder signParam = new StringBuilder();
        Iterator<Map.Entry<String, String[]>> it = context.getParameterMap().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String[]> entry = it.next();

            String[] values = entry.getValue();
            // 值为空或者签名相关的参数不参与验签
            if(skipCompose(entry.getKey(), values)) {
                continue;
            }
            signParam.append(compose(entry.getKey(), values[0], context, secret));
        }
        return afterSignCompose(signParam.toString(), context, secret);
    }

}
