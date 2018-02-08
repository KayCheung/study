package com.housaire.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.housaire.service.ConsumerService;

@RestController
public class ConsumerController {

	@Autowired
	ConsumerService consumerService;
	
	@RequestMapping(value = "/sayhello", method = RequestMethod.GET)
    public String sayhello() {
        return consumerService.sayHello();
    }
	
}
