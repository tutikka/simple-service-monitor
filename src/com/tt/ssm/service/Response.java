package com.tt.ssm.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;

public class Response {

	public static final String STATUS_OK = "OK";
	
	public static final String STATUS_WARNING = "WARNING";
	
	public static final String STATUS_ERROR = "ERROR";
	
	private String status;
	
	private long time;
	
	private String message;
	
	private Date updated;
	
	public Response() {
	}

	@Override
	public String toString() {
		Gson gson = new Gson();
		return (gson.toJson(this));
	}
	
	public static String formatUpdated(Date updated) {
		if (updated == null) {
			return ("");
		} else {
			return (new SimpleDateFormat("HH:mm:ss").format(updated));
		}
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	
}
