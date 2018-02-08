package com.housaire;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class App 
{
    public static void main( String[] args )
    {
    	args = new String[] {"--spring.profiles.active=native"};
    	new SpringApplicationBuilder(App.class).web(true).run(args);
    }
}
