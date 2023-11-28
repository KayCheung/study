package com.housaire;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableCircuitBreaker
@EnableEurekaClient
@SpringCloudApplication
public class ProviderBootstrap 
{
    public static void main( String[] args )
    {
        new SpringApplicationBuilder(ProviderBootstrap.class).web(true).run(args);
    }
}
