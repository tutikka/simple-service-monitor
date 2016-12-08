package com.tt.ssm.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class HttpIO {
	
	public static HttpRequestHead readRequestHead(InputStream in) throws Exception {
		HttpRequestHead head = new HttpRequestHead();
		int lineNumber = 1;
		int b;
		StringBuilder sb = new StringBuilder();
		while ((b = in.read()) != -1) {
			if (b == 10) {
				String line = sb.toString();
				if (line.isEmpty()) {
					break;
				}
				if (lineNumber == 1) {
					StringTokenizer st = new StringTokenizer(line, " ");
					if (st.countTokens() == 3) {
						String method = st.nextToken().trim();
						String uri = URLDecoder.decode(st.nextToken().trim(), "UTF-8");
						String version = st.nextToken().trim();
						int index = uri.indexOf("?");
						if (index != -1) {
							StringTokenizer st2 = new StringTokenizer(uri.substring(index + 1), "&");
							while (st2.hasMoreTokens()) {
								String[] parts = st2.nextToken().split("=");
								if (parts != null && parts.length == 2) {
									String name = parts[0];
									String value = parts[1];
									head.getParameters().put(name, value);
								}
							}
							uri = uri.substring(0, index);
						}
						head.setMethod(method);
						head.setUri(uri);
						head.setVersion(version);
					}
				} else {
					int index = line.indexOf(":");
					if (index > 0) {
						String name = line.substring(0, index).trim();
						List<String> values = Arrays.asList(line.substring(index + 1).trim().split(","));
						head.getHeaders().put(name, values);
					}
				}
				sb = new StringBuilder();
				lineNumber++;
			} else if (b == 13) {
				// ignore
			} else {
				sb.append((char) b);
			}
		}
		return (head);
	}
	
	public static void writeResponseHead(HttpResponseHead head, OutputStream out) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(head.getVersion());
		sb.append(" ");
		sb.append(head.getStatus());
		sb.append(" ");
		sb.append(head.getMessage());
		sb.append("\n");
		for (String key : head.getHeaders().keySet()) {
			sb.append(key);
			sb.append(": ");
			int i = 0;
			for (String value : head.getHeaders().get(key)) {
				if (i > 0) {
					sb.append(", ");
				}
				sb.append(value);
				i++;
			}
			sb.append("\n");
		}
		sb.append("\n");
		out.write(sb.toString().getBytes("UTF-8"));
	}
	
}
