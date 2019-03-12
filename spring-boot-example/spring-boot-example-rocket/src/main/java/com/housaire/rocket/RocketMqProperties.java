package com.housaire.rocket;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:cheungkay@sina.com">张凯</a>
 * @description:
 * @date 2019/1/22 20:37
 * @see
 * @since 1.0.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.rocketmq")
public class RocketMqProperties
{
    /** NameServer的地址列表，若是集群，用 ';' 作为地址的分隔符*/
    private String namesrvAddr;

    /** 生产组的名称 */
    private String producerGroupName;

    /** 客户端实例名称 */
    private String instanceName = "DEFAULT";

    /** 轮询从NameServer获取路由信息的时间间隔 */
    private int pollNameServerInterval = 1000 * 30;

    /** 定期发送注册心跳到broker的间隔 */
    private int heartbeatBrokerInterval = 1000 * 30;

    /** 默认的发送超时时间 3000 */
    private int sendMsgTimeout = 3000;

    /** 同步发送失败的话，rocketmq内部重试多少次 */
    private int retryTimesWhenSendFailed = 2;

    /** 异步发送失败的话，rocketmq内部重试多少次 */
    private int retryTimesWhenSendAsyncFailed = 2;

    /** 发送的结果如果不是SEND_OK状态，是否当作失败处理而尝试重发 */
    private boolean retryAnotherBrokerWhenNotStoreOK = false;

    /** 客户端验证，允许发送的最大消息体大小 */
    private int maxMessageSize = 1024 * 1024 * 4; // 4M

    /** consume queue流控的阈值 */
    private int pullThresholdForQueue = 1000;

    /** 拉取的间隔 */
    private long pullInterval = 0;

    /** 一次最大拉取的批量大小 */
    private int pullBatchSize = 32;

    /** 批量消费的最大消息条数 */
    private int consumeMessageBatchMaxSize = 1;

    /**
     *<pre>
     * 一个消息如果消费失败的话，最多重新消费多少次才投递到死信队列
     *     - 并行：默认16次
     *     - 串行：默认无限大（Interge.MAX_VALUE）。由于顺序消费的特性必须等待前面的消息成功消费才能消费后面的，默认无限大即一直不断消费直到消费完成。
     * </pre>
     **/
    private int maxReconsumeTimes = -1;

    /** 串行消费使用，如果返回ROLLBACK或者SUSPEND_CURRENT_QUEUE_A_MOMENT，再次消费的时间间隔 */
    private long suspendCurrentQueueTimeMillis = 1000;

    /** 消费的最长超时时间 */
    private long consumeTimeout = 15;
}
