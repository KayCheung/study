package com.housaire.rocketmq.producer;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * @author <a href="mailto:zhangkai@chinayie.com">张凯</a>
 * @description:
 * @date 2019/4/11 14:15
 * @see
 * @since 1.0.0
 */
public class RocketMqProducerMain
{

    public static void main(String[] args) throws InterruptedException, RemotingException, MQClientException, MQBrokerException
    {

        DefaultMQProducer producer = new DefaultMQProducer("ROCKETMQ_PRODUCER");
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.start();

        producer.send(new Message("test", "hello rocketmq".getBytes()));
    }

}
