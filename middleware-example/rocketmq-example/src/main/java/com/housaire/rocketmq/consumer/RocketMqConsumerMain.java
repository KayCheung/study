package com.housaire.rocketmq.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @author <a href="mailto:zhangkai@chinayie.com">张凯</a>
 * @description:
 * @date 2019/4/11 16:11
 * @see
 * @since 1.0.0
 */
public class RocketMqConsumerMain
{

    public static void main(String[] args) throws MQClientException
    {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("ROCKETMQ_PRODUCER");
        consumer.setNamesrvAddr("127.0.0.1:9876");

        DefaultMQPullConsumer pullConsumer = new DefaultMQPullConsumer("ROCKETMQ_PRODUCER");



        consumer.subscribe("test", MessageSelector.byTag(""));

        consumer.registerMessageListener(new MessageListenerOrderly()
        {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext)
            {

                list.forEach(messageExt -> System.out.println(new String(messageExt.getBody())));

                return ConsumeOrderlyStatus.SUCCESS;
            }
        });

        consumer.start();
    }

}
