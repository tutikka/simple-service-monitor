package com.tt.ssm.service.impl;

import java.net.Socket;
import java.util.Date;
import java.util.UUID;

import com.tt.ssm.misc.Logger;
import com.tt.ssm.service.Response;
import com.tt.ssm.service.Service;

public class TCPService extends Service {
	
	private static Logger logger = Logger.getLogger(TCPService.class);
	
	private String host;
	
	private int port;
	
	public TCPService(String name, String group, String host, int port, long interval, long warning, long error) {
		logger.i("init");
		super.setId(UUID.randomUUID().toString());
		super.setName(name);
		super.setGroup(group);
		super.setInterval(interval);
		super.setWarning(warning);
		super.setError(error);
		this.host = host;
		this.port = port;
	}

	@Override
	public String getType() {
		return ("TCP");
	}
	
	@Override
	public String getDestination() {
		StringBuilder sb = new StringBuilder();
		sb.append(host);
		sb.append(":");
		sb.append(port);
		return (sb.toString());
	}
	
	@Override
	public void request() {
		logger.i("request");
		Response response = new Response();
		long start = System.currentTimeMillis();
		long end;
		Socket socket = null;
		try {
			logger.i("connecting to host " + host + " on port " + port);
			socket = new Socket(host, port);
			response.setStatus(Response.STATUS_OK);
			response.setMessage("Connected");
		} catch (Exception e) {
			response.setStatus(Response.STATUS_ERROR);
			response.setMessage(e.getMessage());
		} finally {
			try {
				socket.close();
			} catch (Exception e) {
				logger.w("error closing socket: " + e.getMessage());
			}
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
