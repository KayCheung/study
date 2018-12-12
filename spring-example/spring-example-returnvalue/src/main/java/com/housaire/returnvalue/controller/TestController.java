package com.housaire.returnvalue.controller;

import com.housaire.returnvalue.basic.Raw;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController
{
    @GetMapping("/demo/sayhello")
    public String sayHello(@RequestParam(required = false, defaultValue = "游客", value = "name") String name) {
        return "你好" + name;
    }

    @Raw
    @GetMapping("/demo/sayhello2")
    public String sayHello2(@RequestParam(required = false, defaultValue = "游客", value = "name") String name) {
        return "你好" + name;
    }
}
