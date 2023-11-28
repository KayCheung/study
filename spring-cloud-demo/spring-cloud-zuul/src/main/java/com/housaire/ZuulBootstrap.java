package com.housaire;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.cloud.netflix.zuul.filters.route.RestClientRibbonCommandFactory;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandFactory;
import org.springframework.context.annotation.Bean;


@EnableEurekaClient
@EnableZuulProxy
@SpringCloudApplication
public class ZuulBootstrap
{

    @Bean
    public TomcatEmbeddedServletContainerFactory webServerFactory() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
        factory.addConnectorCustomizers((Connector connector) -> {
            // connector.setProperty("relaxedPathChars", "[]{}"); // "<>[\]^`{|}
            connector.setProperty("relaxedQueryChars", "[]{}");
        });
        return factory;
    }

    @Bean
    public GatewayZuulFilter gatewayZuulFilter() {
        ProxyRequestHelper helper = new ProxyRequestHelper();
        RibbonCommandFactory<?> ribbonCommandFactory = new RestClientRibbonCommandFactory(new SpringClientFactory());
        return new GatewayZuulFilter(helper, ribbonCommandFactory);
    }

    public static void main(String[] args)
    {
        new SpringApplicationBuilder(ZuulBootstrap.class).web(true).run(args);
    }

}
