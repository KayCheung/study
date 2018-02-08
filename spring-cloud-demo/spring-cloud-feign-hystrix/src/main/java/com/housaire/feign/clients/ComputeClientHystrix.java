package com.housaire.feign.clients;

public class ComputeClientHystrix implements ConsumerClient {

	@Override
	public String sayHello(String name) {
		return "你挂了";
	}

}
