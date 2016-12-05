package com.tt.ssm.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import com.google.gson.Gson;
import com.tt.ssm.misc.Logger;
import com.tt.ssm.service.Service;
import com.tt.ssm.service.ServiceManager;

public class ClientRunnable implements Runnable {

	private static Logger logger = Logger.getLogger(ClientRunnable.class);
	
	private Socket socket;
	
	public ClientRunnable(Socket socket) {
		logger.i("init");
		this.socket = socket;
	}
	
	@Override
	public void run() {
		logger.i("run");
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		HttpRequestHead requestHead = null;
		try {
			in = new BufferedInputStream(socket.getInputStream());
			out = new BufferedOutputStream(socket.getOutputStream());
			requestHead = HttpIO.readRequestHead(in);
			if (requestHead.getUri().toLowerCase().endsWith("/services/")) {
				handleGetServices(out);
			} else {
				handle404(out);
			}
		} catch (Exception e) {
			logger.e("error serving client request: " + e.getMessage());
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					logger.w("error closing output stream: " + e.getMessage());
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					logger.w("error closing input stream: " + e.getMessage());
				}
			}
			if (closeConnection(requestHead)) {
				try {
					socket.close();
				} catch (Exception e) {
					logger.w("error closing socket: " + e.getMessage());
				}
			}
		}
		logger.i("run completed");
	}

	/* ********** private ********* */
	
	private void handleGetServices(OutputStream out) throws Exception {
		List<Service> services = ServiceManager.getInstance().list();
		Gson gson = new Gson();
		String s = gson.toJson(services);
		HttpResponseHead responseHead = new HttpResponseHead();
		responseHead.setProtocol("HTTP/1.1");
		responseHead.setStatus(200);
		responseHead.setMessage("OK");
		responseHead.addHeader("Content-Length", "" + s.length());
		responseHead.addHeader("Cache-Control", "private, max-age=0");
		responseHead.addHeader("Expires", "-1");
		responseHead.addHeader("Content-Type", "application/json; charset=UTF-8");
		responseHead.addHeader("Server", "ssm");
		responseHead.addHeader("Accept-Ranges", "none");
		HttpIO.writeResponseHead(responseHead, out);
		out.write(s.getBytes("UTF-8"));
	}
	
	private void handle404(OutputStream out) throws Exception {
		String s = "<html><head><title>404 - Not Found</title><body>The requested resource was not found.</body></html>";
		HttpResponseHead responseHead = new HttpResponseHead();
		responseHead.setProtocol("HTTP/1.1");
		responseHead.setStatus(404);
		responseHead.setMessage("Not Found");
		responseHead.addHeader("Content-Length", "" + s.length());
		responseHead.addHeader("Content-Type", "text/html; charset=UTF-8");
		responseHead.addHeader("Server", "ssm");
		HttpIO.writeResponseHead(responseHead, out);
		out.write(s.getBytes("UTF-8"));
	}
	
	private boolean closeConnection(HttpRequestHead head) {
		String connection = head.getHeaderFirstValue("Connection");
		return (connection != null && "close".equalsIgnoreCase(connection));
	}
	
}
