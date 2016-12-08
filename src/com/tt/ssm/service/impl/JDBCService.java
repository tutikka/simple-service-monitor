package com.tt.ssm.service.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.UUID;

import com.google.gson.Gson;
import com.tt.ssm.misc.Logger;
import com.tt.ssm.service.Response;
import com.tt.ssm.service.Service;

public class JDBCService extends Service {
	
	private static Logger logger = Logger.getLogger(JDBCService.class);
	
	private String driver;
	
	private String url;
	
	private String username;
	
	private String password;
	
	private String query;
	
	public JDBCService(String name, String group, String driver, String url, String username, String password, String query, long interval, long warning, long error) {
		logger.i("init");
		super.setId(UUID.randomUUID().toString());
		super.setType(TYPE_JDBC);
		super.setName(name);
		super.setGroup(group);
		super.setInterval(interval);
		super.setWarning(warning);
		super.setError(error);
		this.driver = driver;
		this.url = url;
		this.username = username;
		this.password = password;
		this.query = query;
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
		Connection connection = null;
		try {
			logger.i("using jdbc driver " + driver);
			Class.forName(driver);
			if (username != null && password != null) {
				logger.i("connecting to jdbc url " + url + " (authenticating with username " + username + ")");
				connection = DriverManager.getConnection(url, username, password);
			} else {
				logger.i("connecting to jdbc url " + url + " (no authentication)");
				connection = DriverManager.getConnection(url);
			}
			logger.i("running sql query " + query);
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			if (rs != null && rs.first()) {
				response.setStatus(Response.STATUS_OK);
				response.setMessage("Query ok");
			} else {
				response.setStatus(Response.STATUS_ERROR);
				response.setMessage("No result from query");
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			response.setStatus(Response.STATUS_ERROR);
			response.setMessage(e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (Exception e) {
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
		logger.i("request completed");
	}
	
}
