package com.tt.ssm.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.tt.ssm.misc.Logger;

public class ServiceManager {

	private static Logger logger = Logger.getLogger(ServiceManager.class);
	
	private static ServiceManager instance;
	
	private ScheduledExecutorService ses;
	
	private List<Callback> callbacks;
	
	private List<FutureWrapper> futures;
	
	private ServiceManager() {
		logger.i("init");
		ses = Executors.newScheduledThreadPool(10);
		callbacks = new ArrayList<>();
		futures = new ArrayList<>();
	}
	
	public static ServiceManager getInstance() {
		if (instance == null) {
			instance = new ServiceManager();
		}
		return (instance);
	}
	
	public synchronized void registerCallback(Callback callback) {
		logger.i("registerCallback");
		if (!callbacks.contains(callback)) {
			logger.i("registering callback " + callback.getClass().getName());
			callbacks.add(callback);
		} else {
			logger.w("callback " + callback.getClass().getName() + " has already been registered");
		}
		logger.i("registerCallback completed");
	}
	
	public synchronized void unregisterCallback(Callback callback) {
		logger.i("unregisterCallback");
		if (callbacks.contains(callback)) {
			logger.i("unregistering callback " + callback.getClass().getName());
			callbacks.remove(callback);
		} else {
			logger.w("callback " + callback.getClass().getName() + " has already been unregistered");
		}
		logger.i("unregisterCallback completed");
	}
	
	public void schedule(Service service) {
		logger.i("schedule");
		ScheduledFuture<?> future = ses.scheduleAtFixedRate(new ServiceRunner(service, callbacks), 0, service.getInterval(), TimeUnit.MILLISECONDS);
		futures.add(new FutureWrapper(future, service.getId()));
		logger.i("schedule completed");
	}
	
	public void cancel(Service service) {
		logger.i("cancel");
		for (Iterator<FutureWrapper> iterator = futures.iterator(); iterator.hasNext(); ) {
			FutureWrapper wrapper = iterator.next();
			if (wrapper.getId().equals(service.getId())) {
				logger.i("cancelling service " + wrapper.getId());
				wrapper.getFuture().cancel(false);
				iterator.remove();
				logger.i("service cancelled");
			}
		}
		logger.i("cancel completed");
	}
	
	public void close() {
		logger.i("close");
		for (Iterator<FutureWrapper> iterator = futures.iterator(); iterator.hasNext(); ) {
			FutureWrapper wrapper = iterator.next();
			logger.i("cancelling service " + wrapper.getId());
			wrapper.getFuture().cancel(false);
			iterator.remove();
			logger.i("service cancelled");
		}
		ses.shutdown();
		logger.i("close completed");
	}
	
	public interface Callback {
		
		public void onServiceResponded(Service service);
		
	}
	
	private class FutureWrapper {
		
		private ScheduledFuture<?> future;
		
		private String id;
		
		private FutureWrapper(ScheduledFuture<?> future, String id) {
			this.future = future;
			this.id = id;
		}

		public ScheduledFuture<?> getFuture() {
			return future;
		}

		public String getId() {
			return id;
		}
		
	}
	
}
