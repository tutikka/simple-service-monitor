package com.tt.ssm.server;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestHead extends HttpHead {
	
	private String method;
	
	private String uri;
	
	private Map<String, String> parameters;
	
	public HttpRequestHead() {
		parameters = new HashMap<>();
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}
	
}
