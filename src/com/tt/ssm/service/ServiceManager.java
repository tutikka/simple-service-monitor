package com.tt.ssm.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
		ScheduledFuture<?> future = ses.scheduleAtFixedRate(new ServiceRunner(service, callbacks), service.getInterval(), service.getInterval(), TimeUnit.MILLISECONDS);
		futures.add(new FutureWrapper(future, service));
		for (Callback callback : callbacks) {
			callback.onServiceScheduled(service);
		}
		logger.i("schedule completed");
	}
	
	public void cancel() {
		logger.i("cancel");
		for (Iterator<FutureWrapper> iterator = futures.iterator(); iterator.hasNext(); ) {
			FutureWrapper wrapper = iterator.next();
			logger.i("cancelling service " + wrapper.getService());
			wrapper.getFuture().cancel(false);
			iterator.remove();
			for (Callback callback : callbacks) {
				callback.onServiceCancelled(wrapper.getService());
			}
			logger.i("service cancelled");
		}
		logger.i("cancel completed");
	}
	
	public void cancel(Service service) {
		logger.i("cancel");
		for (Iterator<FutureWrapper> iterator = futures.iterator(); iterator.hasNext(); ) {
			FutureWrapper wrapper = iterator.next();
			if (wrapper.getService().equals(service)) {
				logger.i("cancelling service " + wrapper.getService());
				wrapper.getFuture().cancel(false);
				iterator.remove();
				for (Callback callback : callbacks) {
					callback.onServiceCancelled(wrapper.getService());
				}
				logger.i("service cancelled");
			}
		}
		logger.i("cancel completed");
	}
	
	public List<Service> list() {
		logger.i("list");
		List<Service> services = new ArrayList<>();
		for (Iterator<FutureWrapper> iterator = futures.iterator(); iterator.hasNext(); ) {
			FutureWrapper wrapper = iterator.next();
			services.add(wrapper.getService());
		}
		logger.i("list completed");
		return (services);
	}
	
	public void save(File file) {
		logger.i("save");
		List<Service> services = new ArrayList<>();
		for (Iterator<FutureWrapper> iterator = futures.iterator(); iterator.hasNext(); ) {
			FutureWrapper wrapper = iterator.next();
			services.add(wrapper.getService());
		}
		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			FileOutputStream out = new FileOutputStream(file, false);
			out.write(gson.toJson(services).getBytes("UTF-8"));
			out.flush();
			out.close();
		} catch (Exception e) {
			logger.e("error saving services: " + e.getMessage());
		}
		logger.i("save completed");
	}
	
	public void open(File file) {
		logger.i("open");
		try {
			Gson gson = new GsonBuilder().registerTypeAdapter(Service.class, new ServiceDeserializer()).create();
			FileReader in = new FileReader(file);
			Service[] services = gson.fromJson(in, Service[].class);
			for (Service service : services) {
				schedule(service);
			}
			in.close();
		} catch (Exception e) {
			logger.e("error opening services: " + e.getMessage());
			e.printStackTrace();
		}
		logger.i("open completeted");
	}
	
	public void close() {
		logger.i("close");
		ses.shutdown();
		logger.i("close completed");
	}
	
	public interface Callback {
		
		public void onServiceScheduled(Service service);
		
		public void onServiceResponded(Service service);
		
		public void onServiceCancelled(Service service);
		
	}
	
	private class FutureWrapper {
		
		private ScheduledFuture<?> future;
		
		private Service service;
		
		private FutureWrapper(ScheduledFuture<?> future, Service service) {
			this.future = future;
			this.service = service;
		}

		public ScheduledFuture<?> getFuture() {
			return future;
		}

		public Service getService() {
			return service;
		}
		
	}
	
}
