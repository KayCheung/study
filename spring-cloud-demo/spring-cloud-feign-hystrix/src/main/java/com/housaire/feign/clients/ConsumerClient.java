package com.housaire.feign.clients;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "provider-service", fallback = ComputeClientHystrix.class)
public interface ConsumerClient {

	@RequestMapping(value = "/sayhello", method = RequestMethod.GET)
	public String sayHello(@RequestParam("name") String name);
	
}
