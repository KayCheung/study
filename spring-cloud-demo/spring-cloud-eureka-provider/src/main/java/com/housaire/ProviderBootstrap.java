package com.housaire;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ProviderBootstrap 
{
    public static void main( String[] args )
    {
        new SpringApplicationBuilder(ProviderBootstrap.class).web(true).run(args);
    }
}
