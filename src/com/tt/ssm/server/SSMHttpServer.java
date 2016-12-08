package com.tt.ssm.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.tt.ssm.misc.Logger;

public class SSMHttpServer {

	private static Logger logger = Logger.getLogger(SSMHttpServer.class);
	
	private static SSMHttpServer instance;
	
	private ExecutorService executorService;
	
	private ServerRunnable serverRunnable;
	
	public static SSMHttpServer getInstance() {
		if (instance == null) {
			instance = new SSMHttpServer();
		}
		return (instance);
	}
	
	private SSMHttpServer() {
		logger.i("init");
	}
	
	public boolean start(String host, int port) {
		logger.i("start");
		executorService = Executors.newSingleThreadExecutor();
		serverRunnable = new ServerRunnable(host, port);
		boolean result = serverRunnable.requestStart();
		if (result) {
			executorService.submit(serverRunnable);
		}
		logger.i("start completed");
		return (result);
	}
	
	public boolean stop() {
		logger.i("stop");
		boolean result = serverRunnable.requestStop();
		if (result) {
			executorService.shutdown();
		}
		logger.i("stop completed");
		return (result);
	}
	
	public boolean isRunning() {
		if (executorService == null) {
			return (false);
		}
		if (executorService.isShutdown()) {
			return (false);
		}
		if (executorService.isTerminated()) {
			return (false);
		}
		return (true);
	}
	
}