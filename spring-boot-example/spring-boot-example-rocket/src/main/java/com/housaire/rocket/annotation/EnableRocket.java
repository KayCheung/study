package com.housaire.rocket.annotation;

import com.housaire.rocket.RocketMqAutoConfiguration;
import com.housaire.rocket.RocketMqScannerRegistrar;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author <a href="mailto:cheungkay@sina.com">张凯</a>
 * @description:
 * @date 2019/1/23 13:34
 * @see
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
@Import(RocketMqScannerRegistrar.class)
@ImportAutoConfiguration(RocketMqAutoConfiguration.class)
public @interface EnableRocket
{

    String[] value() default "";

}
