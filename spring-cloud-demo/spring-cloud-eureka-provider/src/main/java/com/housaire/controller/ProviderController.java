package com.housaire.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProviderController {

	@RequestMapping(value = "/sayhello", method = RequestMethod.GET)
	public String sayhello(@RequestParam("name") String name) {
		return "hello " + name;
	}

}
