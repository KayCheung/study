package com.housaire.rocket;

import com.housaire.rocket.annotation.EnableRocket;
import com.housaire.rocket.annotation.RocketMqConsumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.util.Objects;

/**
 * @author <a href="mailto:zhangkai@chinayie.com">张凯</a>
 * @description:
 * @date 2019/1/23 14:01
 * @see
 * @since 1.0.0
 */
@Slf4j
public class RocketMqScannerRegistrar implements ImportBeanDefinitionRegistrar
{
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry)
    {
        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableRocket.class.getName()));
        String[] consumerPackages = annoAttrs.getStringArray("value");
        if (Objects.isNull(consumerPackages) || consumerPackages.length == 0 || StringUtils.isBlank(consumerPackages[0]))
        {
            String packageName = importingClassMetadata.getClassName().substring(0,
                    importingClassMetadata.getClassName().lastIndexOf("."));
            consumerPackages = new String[]{packageName};
        }

        log.info("扫描RocketMQ的消费者并注册为Bean: '[{}]'", StringUtils.join(consumerPackages, ","));
        // 初始化scanner
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry, true);
        // 添加过滤器，只扫描注解了RocketMqConsumer和继承自RocketMqMessageListener的bean
        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(RocketMqConsumer.class);
        AssignableTypeFilter assignableTypeFilter = new AssignableTypeFilter(AbstractRocketMqMessageListener.class);
        scanner.addIncludeFilter(annotationTypeFilter);
        scanner.addIncludeFilter(assignableTypeFilter);
        // 扫描RocketMQ Consumer相关的Bean
        scanner.scan(consumerPackages);
    }
}
