package com.tt.ssm.server;

public class HttpResponseHead extends HttpHead {

	private int status;
	
	private String message;
	
	public HttpResponseHead() {
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
