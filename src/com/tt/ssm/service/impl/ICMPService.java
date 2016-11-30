package com.tt.ssm.service.impl;

import java.net.InetAddress;
import java.util.Date;
import java.util.UUID;

import com.google.gson.Gson;
import com.tt.ssm.misc.Logger;
import com.tt.ssm.service.Response;
import com.tt.ssm.service.Service;

public class ICMPService extends Service {
	
	private static Logger logger = Logger.getLogger(ICMPService.class);
	
	private String host;
	
	public ICMPService(String name, String group, String host, long interval, long warning, long error) {
		logger.i("init");
		super.setId(UUID.randomUUID().toString());
		super.setType(TYPE_ICMP);
		super.setName(name);
		super.setGroup(group);
		super.setInterval(interval);
		super.setWarning(warning);
		super.setError(error);
		this.host = host;
	}

	@Override
	public String toString() {
		Gson gson = new Gson();
		return (gson.toJson(this));
	}
	
	@Override
	public String getDestination() {
		StringBuilder sb = new StringBuilder();
		sb.append(host);
		return (sb.toString());
	}
	
	@Override
	public void request() {
		logger.i("request");
		Response response = new Response();
		long start = System.currentTimeMillis();
		long end;
		try {
			logger.i("echoing host " + host);
			InetAddress address = InetAddress.getByName(host);
			if (address.isReachable((int) getError())) {
				response.setStatus(Response.STATUS_OK);
				response.setMessage("Host available");
			} else {
				response.setStatus(Response.STATUS_ERROR);
				response.setMessage("Host not available");
			}
		} catch (Exception e) {
			response.setStatus(Response.STATUS_ERROR);
			response.setMessage(e.getMessage());
		} finally {
			end = System.currentTimeMillis();
			response.setTime(end - start);
			response.setUpdated(new Date());
		}
		if (response.getStatus() == Response.STATUS_OK) {
			if (response.getTime() > getWarning()) {
				response.setStatus(Response.STATUS_WARNING);
			}
			if (response.getTime() > getError()) {
				response.setStatus(Response.STATUS_ERROR);
			}	
		}
		setResponse(response);
		logger.i("request completeted");
	}
	
}
