package com.siteview.controller;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import javolution.util.FastMap;

import org.ofbiz.base.util.Debug;

import com.siteview.jsvapi.Jsvapi;
import com.siteview.utils.EventXmlParse;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;

public class Httpserver implements HttpHandler {
	public static final String module = Httpserver.class.getName();
	private static final Jsvapi svapi = new Jsvapi();
	
	private int port = 8989;
	public static void main(String[] args){
		new Httpserver().start();
	}
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		try{
			String response = "Fail!";
			OutputStream out = exchange.getResponseBody();
			InputStream is = exchange.getRequestBody();
			try {
				String requestMethod = exchange.getRequestMethod();
				if ("POST".equalsIgnoreCase(requestMethod)) {
					process(is);
					response = "Success!";
				}
			} catch (Exception e) {
				response = e.getMessage();
			} finally {
				try{
					if (response == null) response = "NULL";
					response = "return message : " + response;
					//exchange.sendResponseHeaders(200, response.length());
					exchange.sendResponseHeaders(200, 0);
					out.write(response.getBytes());
				}finally{
					out.close();
				}
			}
		}catch(Exception e){
			Debug.log(e);
		}
	}
	public Httpserver(){
	}
	public Httpserver(int port){
		this();
		this.port = port;
	}

	public void start() {
		try {
			InetSocketAddress addr = new InetSocketAddress(port);
			HttpServerProvider httpServerProvider = HttpServerProvider.provider();
			HttpServer server = httpServerProvider.createHttpServer(addr, 1);
			//HttpServer server = HttpServer.create(addr, 0);
			server.createContext("/", new Httpserver());
			//server.setExecutor(Executors.newCachedThreadPool());
			server.setExecutor(null);
			server.start();
			Debug.logInfo("DCM HttpServer is listening on port " + server.getAddress().getPort(),module);
		} catch (Exception e) {
			e.printStackTrace();
			Debug.logError(e, module);
		}
	}
	public void process(InputStream is) throws Exception {
		String str = is2String(is);
		EventXmlParse exp = new EventXmlParse(str);
		Map<String,String> map = exp.getMap();
		Debug.logInfo(map.toString(),module);

        StringBuilder estr = new StringBuilder();
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("dowhat", "handleActiveAlert");
        parameters.put("eventNum", "1");
        
        Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
        fmap.put("event1", map);
        boolean ret = svapi.submitUnivData(fmap,parameters,  estr);
        if (ret)return;
        throw new Exception("Call DCM DLL Error: " + estr.toString());
    }
	private static String is2String2(InputStream is) throws IOException{
		byte[] bytes = read(is);
		return new String(bytes);
	}	
	private static String is2String(InputStream is) throws IOException{
		BufferedReader reader=new BufferedReader(new InputStreamReader(is));
		 StringBuffer jb = new StringBuffer();
		 String line = null;
		 while ((line = reader.readLine()) != null)
		      jb.append(line);
		 if (jb.length()>0){
			 jb.deleteCharAt(jb.length()-1);
			 jb.append("\t");
		 }
		 return jb.toString();
    }
	protected static byte[] read(InputStream is) {
		ByteArrayOutputStream out = null;
		try {

			out = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int len = 0;
			while ((len = is.read(buf)) > 0) {
				out.write(buf, 0, len);

				Debug.log(" Wrote " + len + " bytes to out");
			}
		} catch (IOException e) {
			Debug.logError(e, module);
		}
		return out.toByteArray();
	} 

}
