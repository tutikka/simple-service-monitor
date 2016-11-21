package com.tt.ssm.service;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Response {

	public static final int STATUS_OK = 0;
	
	public static final int STATUS_WARNING = 1;
	
	public static final int STATUS_ERROR = 2;
	
	private int status;
	
	private long time;
	
	private String message;
	
	private Date updated;
	
	public Response() {
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{ \"status\": \"");
		sb.append(formatStatus(status));
		sb.append("\", \"time\": ");
		sb.append(time);
		sb.append(", \"message\": \"");
		sb.append(message);
		sb.append("\" }");
		return (sb.toString());
	}

	public static String formatStatus(int status) {
		switch (status) {
		case STATUS_OK : return ("OK");
		case STATUS_WARNING : return ("WARNING");
		case STATUS_ERROR : return ("ERROR");
		}
		return ("");
	}
	
	public static String formatUpdated(Date updated) {
		if (updated == null) {
			return ("");
		} else {
			return (new SimpleDateFormat("HH:mm:ss").format(updated));
		}
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
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
