package com.housaire;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy
@SpringCloudApplication
public class App
{

    public static void main(String[] args)
    {
        new SpringApplicationBuilder(App.class).web(true).run(args);
    }

    @RunWith(JUnit4.class)
    public static class UnitTest
    {

        @Test
        public void test()
        {
            System.out.println("hello");
        }

    }
}
