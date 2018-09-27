package com.study.demo.utils;

import org.apache.curator.framework.recipes.locks.InterProcessLock;

public interface LockUtils {

	static void unlock(InterProcessLock lock) {
		try {
			lock.release();
		} catch (Exception e) {
			// ignore
		}
	}
	
}
