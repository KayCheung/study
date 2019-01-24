package com.housaire.rocket;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * @author <a href="mailto:cheungkay@sina.com">张凯</a>
 * @description:
 * @date 2019/1/23 09:34
 * @see
 * @since 1.0.0
 */
@Slf4j
@ConditionalOnClass({DefaultMQProducer.class, DefaultMQPushConsumer.class})
@EnableConfigurationProperties(RocketMqProperties.class)
public class RocketMqAutoConfiguration
{

    @Autowired
    private RocketMqProperties rocketMqProperties;

    @Bean
    @Primary
    public MQProducer mqProducer() throws MQClientException
    {
        log.info("创建RocketMQ生产者'[{}, {}]'", rocketMqProperties.getNamesrvAddr(), rocketMqProperties.getProducerGroupName());
        DefaultMQProducer producer = new DefaultMQProducer(rocketMqProperties.getProducerGroupName());
        producer.setNamesrvAddr(rocketMqProperties.getNamesrvAddr());
        producer.start();
        log.info("RocketMQ生产者创建成功");
        return producer;
    }

    @Bean
    @Primary
    public RocketMqConsumerBean rocketMqConsumerBean()
    {
        return new RocketMqConsumerBean(rocketMqProperties);
    }

}
