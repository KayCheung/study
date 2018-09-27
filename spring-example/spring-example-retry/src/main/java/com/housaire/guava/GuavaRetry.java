package com.housaire.guava;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.github.rholder.retry.Attempt;
import com.github.rholder.retry.AttemptTimeLimiter;
import com.github.rholder.retry.RetryException;
import com.github.rholder.retry.RetryListener;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import com.google.common.base.Predicates;

public class GuavaRetry {

	public static void main(String[] args) throws ExecutionException, RetryException {
		RetryCallable callable = new RetryCallable();
		callable.setMessage(null);
		Retryer<Object> retryer = RetryerBuilder.newBuilder()
				.retryIfException()
				.retryIfResult(Predicates.instanceOf(Object.class))
				.withWaitStrategy(WaitStrategies.join(WaitStrategies.exponentialWait(1000, 10000, TimeUnit.SECONDS)))
				.withStopStrategy(StopStrategies.stopAfterAttempt(5))
				.withAttemptTimeLimiter(new AttemptTimeLimiter<Object>() {
					@Override
					public Object call(Callable<Object> callable) throws Exception {
						return null;
					}
				})
				.withRetryListener(new RetryListener() {
					@Override
					public <V> void onRetry(Attempt<V> attempt) {
						System.out.println("第" + attempt.getAttemptNumber() + "次尝试" + (attempt.hasException() ? "失败" : "成功"));
						System.out.println("距离第一次重试的延迟: " + attempt.getDelaySinceFirstAttempt());
					}
				}).build();
		retryer.call(callable);
		System.out.println("Hello");
	}
	
	public static class RetryCallable implements Callable<Object> {
		private String message = null;
		
		public void setMessage(String message) {
			this.message = message;
		}
		
		@Override
		public Object call() throws Exception {
			if(null == message) {
				throw new RuntimeException("消息为空不能处理");
			}
			return null;
		}
	}

}
