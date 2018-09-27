package com.study.demo;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import com.alibaba.fastjson.JSON;

public class App
{
	
	static CountDownLatch connected = new CountDownLatch(1);
	
    public static void main( String[] args ) throws IOException, InterruptedException, KeeperException
    {
    	ZooKeeper zk = new ZooKeeper("10.10.1.11:2181", 1000, new Watcher(){
			@Override
			public void process(WatchedEvent event) {
				System.err.println("第一个Watcher " + JSON.toJSON(event));
				if(event.getType() == EventType.None && event.getPath() == null) {
					connected.countDown();
					System.err.println("连接成功...");
				}
			}});
    	connected.await(2, TimeUnit.SECONDS);
    	
    	zk.create("/zk-test/test4", "hello".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
    	
    	List<String> children = zk.getChildren("/zk-test", true);
    	System.out.println(children);
    	
//    	zk.register(new Watcher(){
//
//			@Override
//			public void process(WatchedEvent event) {
//				System.out.println("第二个Watcher " + JSON.toJSONString(event));	
//			}
//    	});
    	
    	System.err.println(JSON.toJSONString(new String(zk.getData("/zk-test/test4", new Watcher() {

			@Override
			public void process(WatchedEvent event) {
				System.out.println("第二个Watcher " + JSON.toJSONString(event));	
			}
    		
    	}, null))));
    	
    	Stat stat = zk.setData("/zk-test/test4", "lily".getBytes(), -1);
    	System.err.println(JSON.toJSON(stat));
    	
    	children = zk.getChildren("/zk-test", true);
    	System.out.println(children);
    	
    	zk.create("/zk-test/test5", "hello".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
    	
    }
    
}
