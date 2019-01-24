package com.housaire.rocket.annotation;

import java.lang.annotation.*;

/**
 * @author <a href="mailto:zhangkai@chinayie.com">张凯</a>
 * @description:
 * @date 2019/1/23 09:30
 * @see RocketMqConsumer
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface RocketMqMessageSelector
{
    /**
     * @return
     * @see org.apache.rocketmq.common.filter.ExpressionType
     */
    String type();

    /**
     * expression content.
     *
     * @return
     */
    String expression();
}
