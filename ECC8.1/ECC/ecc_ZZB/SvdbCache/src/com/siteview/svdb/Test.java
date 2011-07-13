package com.siteview.svdb;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

public class Test {

	/**
	 * @param args
	 * @throws RemoteException 
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) throws Exception {
		com.siteview.cxf.client.SvdbApiImplService service = new com.siteview.cxf.client.SvdbApiImplService(new URL("http://localhost/SvdbCache/services/SvdbApiImpl"));
		SvdbApiImplProxy proxy = new SvdbApiImplProxy((String)"http://localhost/SvdbCache/services/SvdbApiImpl");
		for(;;){
			proxy.appendRecord("aaaaa", "aaa=123#bbb=1234");
			//service.getSvdbApiImpl().appendRecord("aaaaa", "aaa=123#bbb=1234");
		}

	}

}
