package com.housaire.example;

import com.housaire.rocket.AbstractRocketMqMessageListener;
import com.housaire.rocket.annotation.RocketMqConsumer;
import com.housaire.rocket.annotation.RocketMqExpression;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * @author <a href="mailto:cheungkay@sina.com">张凯</a>
 * @description:
 * @date 2019/1/24 13:30
 * @see
 * @since 1.0.0
 */
@Slf4j
@RocketMqConsumer(topic = "EXAMPLE", groupName = "TEST_CONSUMER")
@RocketMqExpression("TEST")
public class ExampleMessageListener extends AbstractRocketMqMessageListener<String>
{
    @Override
    public void handleMessage(String data, MessageExt messageExt)
    {
        log.info("收到消息：{}", data);
    }
}
