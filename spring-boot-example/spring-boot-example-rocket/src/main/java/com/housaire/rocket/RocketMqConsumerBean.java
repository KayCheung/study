package com.housaire.rocket;

import com.housaire.rocket.annotation.RocketMqConsumer;
import com.housaire.rocket.annotation.RocketMqExpression;
import com.housaire.rocket.annotation.RocketMqMessageFilter;
import com.housaire.rocket.annotation.RocketMqMessageSelector;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.filter.ExpressionType;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:cheungkay@sina.com">张凯</a>
 * @description:
 * @date 2019/1/23 11:47
 * @see
 * @since 1.0.0
 */
@Slf4j
public class RocketMqConsumerBean implements DisposableBean, BeanPostProcessor
{

    private RocketMqProperties rocketMqProperties;

    private final Set<DefaultMQPushConsumer> consumers = new HashSet<>();

    public RocketMqConsumerBean(RocketMqProperties rocketMqProperties)
    {
        this.rocketMqProperties = rocketMqProperties;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException
    {
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(RocketMqConsumer.class) && bean instanceof AbstractRocketMqMessageListener)
        {
            try
            {
                log.info("RocketMQ Consumer 注册 '[{}]'", beanName);
                RocketMqConsumer rocketMqConsumer = beanClass.getAnnotation(RocketMqConsumer.class);
                DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(rocketMqConsumer.groupName());
                consumer.setMessageModel(rocketMqConsumer.model());
                consumer.setNamesrvAddr(rocketMqProperties.getNamesrvAddr());
                if (beanClass.isAnnotationPresent(RocketMqExpression.class))
                {
                    RocketMqExpression rocketMqExpression = beanClass.getAnnotation(RocketMqExpression.class);
                    consumer.subscribe(rocketMqConsumer.topic(), rocketMqExpression.value());
                    log.info("通过表达式进行订阅消息：'[{}: {} - {}]'", rocketMqConsumer.groupName(),
                            rocketMqConsumer.topic(), rocketMqExpression.value());
                }
                else if (beanClass.isAnnotationPresent(RocketMqMessageFilter.class))
                {
                    RocketMqMessageFilter rocketMqMessageFilter = beanClass.getAnnotation(RocketMqMessageFilter.class);
                    consumer.subscribe(rocketMqConsumer.topic(), rocketMqMessageFilter.fullClassName(), rocketMqMessageFilter.filterClassSource());
                    log.info("通过消息过滤进行订阅消息：'[{}: {{} - {},{}]'", rocketMqConsumer.groupName(),
                            rocketMqConsumer.topic(), rocketMqMessageFilter.fullClassName(), rocketMqMessageFilter.filterClassSource());
                }
                else if (beanClass.isAnnotationPresent(RocketMqMessageSelector.class))
                {
                    RocketMqMessageSelector rocketMqMessageSelector = beanClass.getAnnotation(RocketMqMessageSelector.class);
                    MessageSelector messageSelector;
                    if (ExpressionType.isTagType(rocketMqMessageSelector.type()))
                    {
                        messageSelector = MessageSelector.byTag(rocketMqMessageSelector.expression());
                        log.info("通过Tag进行订阅消息：'[{}: {} - {}]'", rocketMqConsumer.groupName(),
                                rocketMqConsumer.topic(), rocketMqMessageSelector.expression());
                    }
                    else
                    {
                        messageSelector = MessageSelector.bySql(rocketMqMessageSelector.expression());
                        log.info("通过SQL进行订阅消息：'[{}: {} - {}]'", rocketMqConsumer.groupName(),
                                rocketMqConsumer.topic(), rocketMqMessageSelector.expression());
                    }
                    consumer.subscribe(rocketMqConsumer.topic(), messageSelector);
                }
                else
                {
                    throw new BeanInitializationException("创建RocketMq消费者失败 [" + beanName + "], 请标识消费者需要消费的消息类型, 参考：RocketMqExpression, RocketMqMessageFilter, RocketMqMessageSelector");
                }
                consumer.registerMessageListener((MessageListenerConcurrently) bean);
                consumer.start();
                consumers.add(consumer);
            }
            catch (MQClientException e)
            {
                log.error("创建RocketMq消费者失败 [" + beanName + "]", e);
                throw new BeanInitializationException("创建RocketMq消费者失败 [" + beanName + "]", e);
            }
            log.info("RocketMQ Consumer 注册成功 '{}'", consumers.parallelStream()
                    .map(c -> c.getMessageListener().getClass().getName()).collect(Collectors.toList()));
        }
        return bean;
    }

    @Override
    public void destroy()
    {
        for (MQPushConsumer consumer : consumers)
        {
            consumer.shutdown();
        }
    }

}
