package com.housaire.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.housaire.feign.clients.ConsumerClient;

@RestController
public class ConsumerController {

	@Autowired
	ConsumerClient client;
	
	@RequestMapping("/sayhello")
	public String sayHello() {
		return client.sayHello("feign");
	}
	
}
