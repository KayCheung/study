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
public @interface RocketMqMessageFilter
{
    /**
     * full class name,must extend org.apache.rocketmq.common.filter. MessageFilter
     *
     * @return
     */
    String fullClassName();

    /**
     * class source code,used UTF-8 file encoding,must be responsible for your code safety
     *
     * @return
     */
    String filterClassSource();
}
