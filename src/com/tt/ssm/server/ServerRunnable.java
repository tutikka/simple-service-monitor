package com.tt.ssm.server;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.tt.ssm.misc.Logger;

public class ServerRunnable implements Runnable {

	private static Logger logger = Logger.getLogger(ServerRunnable.class);
	
	private ExecutorService executorService;
	
	private ServerSocket serverSocket;
	
	private String host;
	
	private int port;
	
	private boolean running = true;
	
	public ServerRunnable(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public boolean requestStart() {
		logger.i("requestStart");
		try {
			executorService = Executors.newCachedThreadPool();
			serverSocket = new ServerSocket(port, 10, InetAddress.getByName(host));
			return (true);
		} catch (Exception e) {
			logger.e("error starting server: " + e.getMessage());
			return (false);
		} finally {
			logger.i("requestStart completed");
		}
	}
	
	public boolean requestStop() {
		logger.i("requestStop");
		try {
			running = false;
			serverSocket.close();
			executorService.shutdown();
			return (true);
		} catch (Exception e) {
			logger.e("error stopping server: " + e.getMessage());
			return (false);
		} finally {
			logger.i("requestStop completed");
		}
	}
	
	@Override
	public void run() {
		logger.i("run");
		while (running) {
			try {
				executorService.submit(new ClientRunnable(serverSocket.accept()));
			} catch (Exception e) {
				logger.e("error accepting connections: " + e.getMessage());
			}
		}
		logger.i("run completed");
	}
	
}