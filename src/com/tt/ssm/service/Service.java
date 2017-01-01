package com.tt.ssm.service;

public abstract class Service {
	
	public static final String TYPE_URL = "url";
	
	public static final String TYPE_TCP = "tcp";
	
	public static final String TYPE_JDBC = "jdbc";
	
	public static final String TYPE_ICMP = "icmp";
	
	private String id;
	
	private String name;
	
	private String type;
	
	private String group;
	
	private long interval;
	
	private long warning;
	
	private long error;
	
	private Response response;
	
	public abstract void request();
	
	public abstract String getDestination();

	@Override
	public int hashCode() {
		return (id.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Service) {
			Service another = (Service) obj;
			return (id.equals(another.getId()));
		} else {
			return (false);
		}
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
