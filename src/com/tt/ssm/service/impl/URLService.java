package com.tt.ssm.service.impl;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

import com.google.gson.Gson;
import com.tt.ssm.misc.Constants;
import com.tt.ssm.misc.Logger;
import com.tt.ssm.service.Response;
import com.tt.ssm.service.Service;

public class URLService extends Service {
	
	private static Logger logger = Logger.getLogger(URLService.class);
	
	private String url;
	
	private int expectedResponseCode;
	
	public URLService(String name, String group, String url, int expectedResponseCode, long interval, long warning, long error) {
		logger.i("init");
		super.setId(UUID.randomUUID().toString());
		super.setType(TYPE_URL);
		super.setName(name);
		super.setGroup(group);
		super.setInterval(interval);
		super.setWarning(warning);
		super.setError(error);
		this.url = url;
		this.expectedResponseCode = expectedResponseCode;
	}

	@Override
	public String toString() {
		Gson gson = new Gson();
		return (gson.toJson(this));
	}
	
	@Override
	public String getDestination() {
		return (url);
	}
	
	@Override
	public void request() {
		logger.i("request");
		Response response = new Response();
		long start = System.currentTimeMillis();
		long end;
		HttpURLConnection connection = null;
		try {
			logger.i("connecting to url " + this.url);
			URL url = new URL(this.url);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Host", url.getHost());
			connection.setRequestProperty("Accept", "*/*");
			connection.setRequestProperty("User-Agent", Constants.TITLE + "/" + Constants.VERSION);
			connection.setRequestProperty("Cache-Control", "no-cache, must-revalidate");
			connection.setRequestProperty("Pragma", "no-cache");
			connection.setDoInput(true);
			connection.setDoOutput(false);
			int responseCode = connection.getResponseCode();
			logger.i("server responded with code " + responseCode);
			if (responseCode == expectedResponseCode) {
				response.setStatus(Response.STATUS_OK);
			} else {
				response.setStatus(Response.STATUS_ERROR);
			}
			response.setMessage("HTTP " + responseCode);
		} catch (Exception e) {
			response.setStatus(Response.STATUS_ERROR);
			response.setMessage(e.getMessage());
		} finally {
			connection.disconnect();
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
		logger.i("request completed");
	}
	
}
