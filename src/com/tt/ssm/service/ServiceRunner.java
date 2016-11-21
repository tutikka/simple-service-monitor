package com.tt.ssm.service;

import com.tt.ssm.misc.Logger;
import com.tt.ssm.service.ServiceManager.Callback;

public class ServiceRunner implements Runnable {

	private static Logger logger = Logger.getLogger(ServiceRunner.class);
	
	private Service service;
	
	private Callback callback;
	
	public ServiceRunner(Service service, Callback callback) {
		logger.i("init");
		this.service = service;
		this.callback = callback;
	}

	@Override
	public void run() {
		logger.i("run");
		service.request();
		callback.onServiceResponded(service);
		logger.i("run completed");
	}
	
}