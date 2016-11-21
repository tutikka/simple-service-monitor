package com.tt.ssm.service;

public abstract class Service implements Comparable<Service> {
	
	private String id;
	
	private String name;
	
	private String group;
	
	private long interval;
	
	private long warning;
	
	private long error;
	
	private Response response;
	
	public abstract void request();
	
	public abstract String getType();
	
	public abstract String getDestination();
	
	@Override
	public int compareTo(Service o) {
		return (this.id.compareTo(o.id));
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public long getWarning() {
		return warning;
	}

	public void setWarning(long warning) {
		this.warning = warning;
	}

	public long getError() {
		return error;
	}

	public void setError(long error) {
		this.error = error;
	}

	public long getInterval() {
		return interval;
	}

	public void setInterval(long interval) {
		this.interval = interval;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

}
