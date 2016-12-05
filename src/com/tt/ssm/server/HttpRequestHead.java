package com.tt.ssm.server;

public class HttpRequestHead extends HttpHead {
	
	private String method;
	
	private String uri;
	
	private String query;
	
	public HttpRequestHead() {
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

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
	
}
