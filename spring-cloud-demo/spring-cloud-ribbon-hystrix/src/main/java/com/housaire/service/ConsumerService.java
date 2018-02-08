package com.housaire.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class ConsumerService {

	@Autowired
	RestTemplate restTemplate;
	
	@HystrixCommand(fallbackMethod = "sayHelloFallback")
    public String sayHello() {
        return restTemplate.getForEntity("http://provider-service/sayhello?name=ribbon", String.class).getBody();
    }
	
	public String sayHelloFallback() {
		return "Call sayHello error.";
	}
	
}
