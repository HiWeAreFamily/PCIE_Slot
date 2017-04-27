package org.group.service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorFactory {

	private static ThreadPoolExecutor instance;

	private ThreadPoolExecutorFactory() {

	}

	public static ThreadPoolExecutor getInstance() {

		if (instance == null) {
			instance = new ThreadPoolExecutor(180, 180, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1200),
					new ThreadPoolExecutor.AbortPolicy());
		}
		return instance;

	}

}
