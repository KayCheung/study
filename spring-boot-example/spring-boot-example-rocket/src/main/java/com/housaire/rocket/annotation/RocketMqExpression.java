package com.housaire.rocket.annotation;

import java.lang.annotation.*;

/**
 * @author <a href="mailto:cheungkay@sina.com">张凯</a>
 * @description:
 * @date 2019/1/23 09:29
 * @see RocketMqConsumer
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface RocketMqExpression
{
    /**
     * subscription expression.it only support or operation such as "tag1 || tag2 || tag3"
     *
     * @return
     */
    String value();
}
