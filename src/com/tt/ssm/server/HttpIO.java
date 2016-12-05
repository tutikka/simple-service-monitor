package com.tt.ssm.server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class HttpIO {

	private static boolean isHeadTerminated(String line) {
		return (line == null || line.isEmpty());
	}
	
	public static HttpRequestHead readRequestHead(InputStream in) throws Exception {
		HttpRequestHead head = new HttpRequestHead();
		BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		String line = null;
		int i = 0;
		while (!isHeadTerminated(line = br.readLine())) {
			if (i == 0) {
				StringTokenizer st = new StringTokenizer(line, " ");
				if (st.countTokens() == 3) {
					head.setMethod(st.nextToken().trim());
					String uri = st.nextToken().trim();
					int j = uri.indexOf("?");
					if (j == -1) {
						head.setUri(uri);
					} else {
						head.setUri(uri.substring(0, j));
						head.setQuery(uri.substring(j));
					}
					head.setProtocol(st.nextToken().trim());
				}
			} else {
				int j = line.indexOf(":");
				if (j != -1) {
					String name = line.substring(0, j);
					List<String> values = Arrays.asList(line.substring(j + 2).split(","));
					head.getHeaders().put(name, values);
				}
			}
			i++;
		}
		return (head);
	}
	
	public static void writeResponseHead(HttpResponseHead head, OutputStream out) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(head.getProtocol());
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
