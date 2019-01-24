package com.housaire.rocket.annotation;

import java.lang.annotation.*;

/**
 * @author <a href="mailto:zhangkai@chinayie.com">张凯</a>
 * @description:
 * @date 2019/1/22 20:56
 * @see RocketMqExpression
 * @see RocketMqMessageFilter
 * @see RocketMqMessageSelector
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface RocketMqConsumer
{
    /**
     * topic to subscribe.
     *
     * @return
     */
    String topic();

    /**
     * Group Name
     *
     * @return
     */
    String groupName();

}
