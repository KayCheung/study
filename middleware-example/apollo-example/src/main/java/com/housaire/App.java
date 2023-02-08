package com.housaire;

import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableApolloConfig({"application", "sms.template", "blankMobileNum"})
public class App {
    public static void main( String[] args )
    {
        ApplicationContext applicationContext = SpringApplication.run(App.class, args);
        Environment env = applicationContext.getBean(Environment.class);
        while(true) {
            System.err.println(env.getProperty("template.code"));
            System.out.println(ConfigService.getConfig("sms.template").getProperty("template.code", ""));
            System.out.println(ApolloConfigUtils.getProperty("alert.stats"));
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
