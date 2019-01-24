package com.housaire;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;


@SpringBootApplication
@MapperScan("com.housaire")
public class Bootstrap
{
    public static void main(String[] args)
    {
        SpringApplication.run(Bootstrap.class, args);
    }
}
