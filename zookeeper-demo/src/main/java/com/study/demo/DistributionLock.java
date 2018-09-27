package com.study.demo;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class DistributionLock {
	
	public static void main(String[] args) {
		
		CuratorFramework client = CuratorFrameworkFactory.builder()
				.connectString("10.10.1.11:2181")
				.sessionTimeoutMs(1000)
				.retryPolicy(new ExponentialBackoffRetry(3000, 3))
				.build();
		
		client.start();
		
		CountDownLatch stoped = new CountDownLatch(10);
		CyclicBarrier cyclicBarrier = new CyclicBarrier(10);
		for (int i = 0; i < 10; i++) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						System.out.println(Thread.currentThread().getName() + " 等待其他线程进入运行状态");
						cyclicBarrier.await();
						System.out.println(Thread.currentThread().getName() + " 进入运行状态");
					} catch (Exception e) {
						e.printStackTrace();
					}
					InterProcessLock lock = new InterProcessMutex(client, "/mutex_lock");
					try {
						lock.acquire();
						System.err.println(Thread.currentThread().getName() + " 获得了锁！");
						Thread.sleep(1000L);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						try {
							lock.release();
							System.err.println(Thread.currentThread().getName() + " 释放了锁！");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					stoped.countDown();
				}
			});
			t.start();
		}
		try {
			stoped.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("分布式锁测试完成...");
	}
	
}
