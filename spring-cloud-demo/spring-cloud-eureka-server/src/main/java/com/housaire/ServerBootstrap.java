package com.housaire;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class ServerBootstrap 
{
    public static void main( String[] args )
    {
    	new SpringApplicationBuilder(ServerBootstrap.class).web(true).run(args);
    }
}
