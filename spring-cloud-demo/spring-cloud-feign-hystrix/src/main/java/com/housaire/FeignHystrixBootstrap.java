package com.housaire;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@EnableFeignClients
@EnableCircuitBreaker
@SpringBootApplication
public class FeignHystrixBootstrap 
{
    public static void main( String[] args )
    {
    	SpringApplication.run(FeignHystrixBootstrap.class, args);
    }
}
