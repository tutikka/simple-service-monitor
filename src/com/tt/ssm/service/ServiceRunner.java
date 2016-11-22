package com.tt.ssm.service;

import java.util.List;

import com.tt.ssm.misc.Logger;
import com.tt.ssm.service.ServiceManager.Callback;

public class ServiceRunner implements Runnable {

	private static Logger logger = Logger.getLogger(ServiceRunner.class);
	
	private Service service;
	
	private List<Callback> callbacks;
	
	public ServiceRunner(Service service, List<Callback> callbacks) {
		logger.i("init");
		this.service = service;
		this.callbacks = callbacks;
	}

	@Override
	public void run() {
		logger.i("run");
		service.request();
		for (Callback callback : callbacks) {
			callback.onServiceResponded(service);
		}
		logger.i("run completed");
	}
	
}