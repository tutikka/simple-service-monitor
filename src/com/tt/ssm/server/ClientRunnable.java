package com.tt.ssm.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tt.ssm.misc.Logger;
import com.tt.ssm.service.Response;
import com.tt.ssm.service.Service;
import com.tt.ssm.service.ServiceDeserializer;
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
			if (requestHead.getUri().toLowerCase().endsWith("/status")) {
				if ("get".equalsIgnoreCase(requestHead.getMethod())) {
					handleGetStatus(out);
					return;
				}
			}
			if (requestHead.getUri().toLowerCase().endsWith("/services")) {
				if ("get".equalsIgnoreCase(requestHead.getMethod())) {
					handleGetServices(out);
					return;
				}
				if ("post".equalsIgnoreCase(requestHead.getMethod())) {
					handlePostService(requestHead, in, out);
					return;
				}
				if ("delete".equalsIgnoreCase(requestHead.getMethod())) {
					handleDeleteService(requestHead, in, out);
					return;
				}
				if ("put".equalsIgnoreCase(requestHead.getMethod())) {
					handlePutService(requestHead, in, out);
					return;
				}
			}
			handleNotFound(out);
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
	
	private void handleGetStatus(OutputStream out) throws Exception {
		logger.i("handleGetStatus");
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		int oks = 0;
		int warnings = 0;
		int errors = 0;
		List<Service> services = ServiceManager.getInstance().list();
		for (Service service : services) {
			Response response = service.getResponse();
			if (response != null && "OK".equals(response.getStatus())) {
				oks++;
			}
			if (response != null && "WARNING".equals(response.getStatus())) {
				warnings++;
			}
			if (response != null && "ERROR".equals(response.getStatus())) {
				errors++;
			}
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<!DOCTYPE html>\n");
		sb.append("<html lang='en'>\n");
		sb.append("  <head>\n");
		sb.append("    <title>Simple Service Monitor - Status</title>\n");
		sb.append("    <meta charset='utf-8'>\n");
		sb.append("    <link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css' integrity='sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u' crossorigin='anonymous'>\n");
		sb.append("    <link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css' integrity='sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp' crossorigin='anonymous'>\n");
		sb.append("  </head>\n");
		sb.append("  <body>\n");
		sb.append("    <div class='container'>\n");
		sb.append("      <h3>Simple Service Monitor - Status</h3>\n");
		if (errors > 0) {
			sb.append("<div class='alert alert-danger'>" + errors + " service(s) reported errors</div>\n");
		} else if (warnings > 0) {
			sb.append("<div class='alert alert-warning'>" + warnings + " service(s) reported warnings</div>\n");
		} else if (oks > 0) {
			sb.append("<div class='alert alert-success'>" + oks + " service(s) ok</div>\n");
		}
		sb.append("      <table class='table'>\n");
		for (Service service : ServiceManager.getInstance().list()) {
			sb.append("<tr>");
			sb.append("<td class='text-left'>");
			sb.append(service.getName());
			sb.append("</td>");
			sb.append("<td class='text-left'>");
			sb.append(service.getResponse() == null ? "- ? -" : "<strong>" + service.getResponse().getStatus() + "</strong>");
			sb.append("</td>");
			sb.append("<td class='text-left'>");
			sb.append(service.getResponse() == null ? "- ? -" : service.getResponse().getTime() + " ms");
			sb.append("</td>");
			sb.append("<td class='text-right'>");
			sb.append(service.getResponse() == null ? "- ? -" : sdf.format(service.getResponse().getUpdated()));
			sb.append("</td>");
			sb.append("</tr>\n");
		}
		sb.append("      </table>\n");
		sb.append("      <hr>\n");
		sb.append("      <p class='text-right text-muted'>" + sdf.format(new Date()) + "</p>\n");
		sb.append("    </div>\n");
		sb.append("    <script src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js' integrity='sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa' crossorigin='anonymous'></script>\n");
		sb.append("  <body>\n");
		sb.append("</html>");
		HttpResponseHead responseHead = new HttpResponseHead();
		responseHead.setVersion("HTTP/1.1");
		responseHead.setStatus(200);
		responseHead.setMessage("OK");
		responseHead.addHeader("Content-Length", "" + sb.length());
		responseHead.addHeader("Cache-Control", "private, max-age=0");
		responseHead.addHeader("Expires", "-1");
		responseHead.addHeader("Content-Type", "text/html; charset=UTF-8");
		responseHead.addHeader("Server", "ssm");
		responseHead.addHeader("Accept-Ranges", "none");
		responseHead.addHeader("Connection", "close");
		HttpIO.writeResponseHead(responseHead, out);
		out.write(sb.toString().getBytes("UTF-8"));
		logger.i("handleGetStatus completed");
	}
	
	private void handlePostService(HttpRequestHead requestHead, InputStream in, OutputStream out) throws Exception {
		logger.i("handlePostService");
		long l = Long.parseLong(requestHead.getHeaderFirstValue("Content-Length"));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] b = new byte[32 * 1024];
		long t = 0;
		int i = -1;
		while ((i = in.read(b)) != -1) {
			baos.write(b, 0, i);
			t += i;
			if (t >= l) {
				break;
			}
		}
		Gson gson = new GsonBuilder().registerTypeAdapter(Service.class, new ServiceDeserializer()).create();
		Service service = gson.fromJson(new String(baos.toByteArray(), "UTF-8"), Service.class);
		ServiceManager.getInstance().schedule(service);
		HttpResponseHead responseHead = new HttpResponseHead();
		responseHead.setVersion("HTTP/1.1");
		responseHead.setStatus(200);
		responseHead.setMessage("OK");
		responseHead.addHeader("Cache-Control", "private, max-age=0");
		responseHead.addHeader("Expires", "-1");
		responseHead.addHeader("Server", "ssm");
		responseHead.addHeader("Connection", "close");
		HttpIO.writeResponseHead(responseHead, out);
		logger.i("handlePostService completed");
	}
	
	private void handlePutService(HttpRequestHead requestHead, InputStream in, OutputStream out) throws Exception {
		logger.i("handlePutService");
		String id = requestHead.getParameters().get("id");
		if (id == null) {
			handleBadRequest(out);
			return;
		}
		long l = Long.parseLong(requestHead.getHeaderFirstValue("Content-Length"));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] b = new byte[32 * 1024];
		long t = 0;
		int i = -1;
		while ((i = in.read(b)) != -1) {
			baos.write(b, 0, i);
			t += i;
			if (t >= l) {
				break;
			}
		}
		Gson gson = new GsonBuilder().registerTypeAdapter(Service.class, new ServiceDeserializer()).create();
		Service service = gson.fromJson(new String(baos.toByteArray(), "UTF-8"), Service.class);
		int numUpdated = ServiceManager.getInstance().update(id, service);
		if (numUpdated == 0) {
			handleNotFound(out);
			return;
		}
		HttpResponseHead responseHead = new HttpResponseHead();
		responseHead.setVersion("HTTP/1.1");
		responseHead.setStatus(200);
		responseHead.setMessage("OK");
		responseHead.addHeader("Cache-Control", "private, max-age=0");
		responseHead.addHeader("Expires", "-1");
		responseHead.addHeader("Server", "ssm");
		responseHead.addHeader("Connection", "close");
		HttpIO.writeResponseHead(responseHead, out);
		logger.i("handlePutService completed");
	}
	
	private void handleDeleteService(HttpRequestHead requestHead, InputStream in, OutputStream out) throws Exception {
		logger.i("handleDeleteService");
		String id = requestHead.getParameters().get("id");
		if (id == null) {
			handleBadRequest(out);
			return;
		}
		int numCancelled = ServiceManager.getInstance().cancel(id);
		if (numCancelled == 0) {
			handleNotFound(out);
			return;
		}
		HttpResponseHead responseHead = new HttpResponseHead();
		responseHead.setVersion("HTTP/1.1");
		responseHead.setStatus(200);
		responseHead.setMessage("OK");
		responseHead.addHeader("Cache-Control", "private, max-age=0");
		responseHead.addHeader("Expires", "-1");
		responseHead.addHeader("Server", "ssm");
		responseHead.addHeader("Connection", "close");
		HttpIO.writeResponseHead(responseHead, out);
		logger.i("handleDeleteService completed");
	}
	
	private void handleGetServices(OutputStream out) throws Exception {
		logger.i("handleGetServices");
		List<Service> services = ServiceManager.getInstance().list();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String s = gson.toJson(services);
		HttpResponseHead responseHead = new HttpResponseHead();
		responseHead.setVersion("HTTP/1.1");
		responseHead.setStatus(200);
		responseHead.setMessage("OK");
		responseHead.addHeader("Content-Length", "" + s.length());
		responseHead.addHeader("Cache-Control", "private, max-age=0");
		responseHead.addHeader("Expires", "-1");
		responseHead.addHeader("Content-Type", "application/json; charset=UTF-8");
		responseHead.addHeader("Server", "ssm");
		responseHead.addHeader("Accept-Ranges", "none");
		responseHead.addHeader("Connection", "close");
		HttpIO.writeResponseHead(responseHead, out);
		out.write(s.getBytes("UTF-8"));
		logger.i("handleGetServices completed");
	}
	
	private void handleBadRequest(OutputStream out) throws Exception {
		logger.i("handleBadRequest");
		String s = "<html><head><title>400 - Bad Request</title><body>The request sent by the client was malformed.</body></html>";
		HttpResponseHead responseHead = new HttpResponseHead();
		responseHead.setVersion("HTTP/1.1");
		responseHead.setStatus(400);
		responseHead.setMessage("Bad Request");
		responseHead.addHeader("Content-Length", "" + s.length());
		responseHead.addHeader("Content-Type", "text/html; charset=UTF-8");
		responseHead.addHeader("Server", "ssm");
		responseHead.addHeader("Connection", "close");
		HttpIO.writeResponseHead(responseHead, out);
		out.write(s.getBytes("UTF-8"));
		logger.i("handleBadRequest completed");
	}
	
	private void handleNotFound(OutputStream out) throws Exception {
		logger.i("handleNotFound");
		String s = "<html><head><title>404 - Not Found</title><body>The requested resource was not found.</body></html>";
		HttpResponseHead responseHead = new HttpResponseHead();
		responseHead.setVersion("HTTP/1.1");
		responseHead.setStatus(404);
		responseHead.setMessage("Not Found");
		responseHead.addHeader("Content-Length", "" + s.length());
		responseHead.addHeader("Content-Type", "text/html; charset=UTF-8");
		responseHead.addHeader("Server", "ssm");
		responseHead.addHeader("Connection", "close");
		HttpIO.writeResponseHead(responseHead, out);
		out.write(s.getBytes("UTF-8"));
		logger.i("handleNotFound completed");
	}
	
	private boolean closeConnection(HttpRequestHead head) {
		String connection = head.getHeaderFirstValue("Connection");
		return (connection != null && "close".equalsIgnoreCase(connection));
	}
	
}
