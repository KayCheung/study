package com.housaire.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ConsumerController {

	@Autowired
	RestTemplate restTemplate;
	
	@RequestMapping(value = "/sayhello", method = RequestMethod.GET)
    public String sayhello() {
        return restTemplate.getForEntity("http://provider-service/sayhello?name=ribbon", String.class).getBody();
    }
	
}
