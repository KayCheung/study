package com.housaire;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class App 
{
    public static void main( String[] args ) throws InterruptedException
    {
    	ApplicationContext applicationContext = SpringApplication.run(App.class, args);
    	
    	MessageRetryService service = applicationContext.getBean(MessageRetryService.class);
    	service.call("测试一下Spring的重试框架");
    	System.err.println("测试完成");
    	Thread.currentThread().join();
    }
}
