package com.housaire.rocket.annotation;

import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.lang.annotation.*;

/**
 * @author <a href="mailto:cheungkay@sina.com">张凯</a>
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

    /**
     * 消息模式
     * @see  MessageModel
     * @return
     */
    MessageModel model() default MessageModel.CLUSTERING;

}
