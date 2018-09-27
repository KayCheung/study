package com.housaire;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class MessageRetryService {

	@Retryable(value = { Exception.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000l, multiplier = 5))
	public void call(String message) {
		System.err.println("重试内容为：" + message);
		if(null != message) {
			throw new RuntimeException("抛异常啦");
		}
	}
	
	@Recover
    public void recover(Exception e) {
       System.err.println(e.getMessage());
    }
	
}
