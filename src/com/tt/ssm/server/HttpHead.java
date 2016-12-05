package com.tt.ssm.server;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpHead {

	private String protocol;

	private Map<String, List<String>> headers;
	
	public HttpHead() {
		headers = new HashMap<>();
	}

	public void addHeader(String name, String value) {
		headers.put(name, Arrays.asList(new String[]{ value }));
	}
	
	public void addHeader(String name, List<String> values) {
		headers.put(name, values);
	}
	
	public List<String> getHeader(String name) {
		if (headers.containsKey(name)) {
			return (headers.get(name));
		} else {
			return (null);
		}
	}
	
	public String getHeaderFirstValue(String name) {
		if (headers.containsKey(name)) {
			return (headers.get(name).get(0));
		} else {
			return (null);
		}
	}
	
	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
	public Map<String, List<String>> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, List<String>> headers) {
		this.headers = headers;
	}
	
}
