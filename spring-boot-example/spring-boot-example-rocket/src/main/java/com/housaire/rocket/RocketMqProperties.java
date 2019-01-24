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
    private String namesrvAddr;

    private String producerGroupName;
}
