package com.housaire.rocket;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author <a href="mailto:cheungkay@sina.com">张凯</a>
 * @description:
 * @date 2019/1/23 10:55
 * @see
 * @since 1.0.0
 */
@Slf4j
public abstract class AbstractRocketMqMessageListener<T> implements MessageListenerConcurrently
{
    private static Gson gson = new Gson();

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context)
    {
        try
        {
            Class<T> type = null;
            for (MessageExt messageExt : msgs)
            {
                log.info("消息 [{}] 执行第 [{}] 次", messageExt.getMsgId(), messageExt.getReconsumeTimes());
                Method[] methods = this.getClass().getDeclaredMethods();
                for (Method method : methods)
                {
                    if (method.getName().equals("handleMessage"))
                    {
                        type = (Class<T>) method.getParameterTypes()[0];
                        break;
                    }
                }
                if (type.equals(String.class))
                {
                    handleMessage((T) new String(messageExt.getBody(), "UTF-8"), messageExt);
                }
                else
                {
                    handleMessage(gson.fromJson(new String(messageExt.getBody(), "UTF-8"), type), messageExt);
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        catch (Exception e)
        {
            log.error("消息处理异常", e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
    }

    public abstract void handleMessage(T data, MessageExt messageExt);

}
