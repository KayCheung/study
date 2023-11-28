package com.housaire.config;

import com.housaire.security.plugin.SecurityCryptoPlugin;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Desc:
 *
 * @author: Zhang Kai
 * @email: <a href="mailto:zhangkai@kltb.com.cn">zhangkai@kltb.com.cn</a>
 * @date: 2020/8/18
 */
@MapperScan("com.housaire.dal")
@Configuration
public class MyBatisConfig {

    @Bean
    public SecurityCryptoPlugin securityCryptoPlugin() {
        return new SecurityCryptoPlugin();
    }

}
