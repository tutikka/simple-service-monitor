package com.tt.ssm.misc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Logger {

	private static final SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	
	private static Map<String, Logger> loggers = new HashMap<>();
	
	private String className;
	
	private Logger(String className) {
		this.className = className;
	}
	
	public static Logger getLogger(Class<?> clazz) {
		if (loggers.containsKey(clazz.getName())) {
			return (loggers.get(clazz.getName()));
		} else {
			Logger logger = new Logger(clazz.getName());
			loggers.put(clazz.getName(), logger);
			return (logger);
		}
	}
	
	public void i(String message) {
		StringBuilder sb = new StringBuilder();
		sb.append(SDF.format(new Date()));
		sb.append(" [INFO   ] ");
		sb.append(className);
		sb.append(" ");
		sb.append(message);
		System.out.println(sb.toString());
	}
	
	public void w(String message) {
		StringBuilder sb = new StringBuilder();
		sb.append(SDF.format(new Date()));
		sb.append(" [WARNING] ");
		sb.append(className);
		sb.append(" ");
		sb.append(message);
		System.out.println(sb.toString());
	}
	
	public void e(String message) {
		StringBuilder sb = new StringBuilder();
		sb.append(SDF.format(new Date()));
		sb.append(" [ERROR  ] ");
		sb.append(className);
		sb.append(" ");
		sb.append(message);
		System.out.println(sb.toString());
	}
	
}