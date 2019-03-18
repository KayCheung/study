package com.housaire.example.utils;

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
