package com.housaire.rocketmq.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;

/**
 * @author <a href="mailto:zhangkai@chinayie.com">张凯</a>
 * @description:
 * @date 2019/4/11 16:11
 * @see
 * @since 1.0.0
 */
public class RocketMqConsumerMain
{

    public static void main(String[] args)
    {
        DefaultMQPullConsumer consumer = new DefaultMQPullConsumer();
    }

}
