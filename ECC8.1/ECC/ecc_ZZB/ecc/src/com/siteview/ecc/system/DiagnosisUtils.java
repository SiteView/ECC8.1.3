package com.siteview.ecc.system;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;

import com.siteview.ecc.system.impl.ClientDiagnosisImpl;

public class DiagnosisUtils {
	private final static Logger logger = Logger.getLogger(DiagnosisUtils.class);
	private final static String resource = "com/siteview/ecc/system/config.xml"; 
	private static Configuration configuration = null;
	public static Configuration getConfiguration() throws ConfigurationException{
		if (configuration == null)
			configuration = new XMLConfiguration(resource); 
		return configuration; 
	}
	public static List<String> getDiagnosisClassNames(){
		List<String> retlist = new LinkedList<String>();
		try {
			for (Object obj : getConfiguration().getList("diagnosises.diagnosis")){
				if (obj instanceof String){
					retlist.add((String) obj);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retlist;
	}
	public static List<Diagnosis> getDiagnosises(){
		List<Diagnosis> retlist = new LinkedList<Diagnosis>();
		try {
			for (String className : getDiagnosisClassNames()){
				try {
					Class<?> classz = Class.forName(className);
					Object obj = classz.newInstance();
					if (obj instanceof Diagnosis){
						retlist.add((Diagnosis) obj);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			retlist.addAll(getSpecialDiagnosises());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retlist;
	}
	public static List<Diagnosis> getSpecialDiagnosises() {
		List<Diagnosis> retlist = new LinkedList<Diagnosis>();
		retlist.add(getClientDiagnosisImpl());
		return retlist;
	}

	public static Diagnosis getClientDiagnosisImpl() {
		return getClientDiagnosisImpl(Executions.getCurrent());
	}

	public static Diagnosis getClientDiagnosisImpl(Execution execution) {
		HttpServletRequest req = (HttpServletRequest) Executions.getCurrent()
				.getNativeRequest();
		return getClientDiagnosisImpl(req);
	}
	public static Diagnosis getClientDiagnosisImpl(HttpServletRequest request){
		//Object object = request.getSession().getAttribute(ClientDiagnosisImpl.class.getName());
		//if (object == null){
			Diagnosis diagnosis = new ClientDiagnosisImpl(request);
			//request.getSession().setAttribute(ClientDiagnosisImpl.class.getName(),diagnosis);
			return diagnosis;
		//}else if (object instanceof Diagnosis){
		//	return (Diagnosis)object;
		//}
		//return null;
		
	}
	
	public static void main(String[] args){
		File file = new File("E:");
		System.out.println("E:");
		System.out.println("Total:  " + file.getTotalSpace());
		System.out.println("Free:   " + file.getFreeSpace());
		System.out.println("Usable: " + file.getUsableSpace());
		file = new File("E://Debug");
		System.out.println("E://Debug");
		System.out.println("Total:  " + file.getTotalSpace());
		System.out.println("Free:   " + file.getFreeSpace());
		System.out.println("Usable: " + file.getUsableSpace());
		file = new File("/");
		System.out.println("n/");
		System.out.println("Total:  " + file.getTotalSpace());
		System.out.println("Free:   " + file.getFreeSpace());
		System.out.println("Usable: " + file.getUsableSpace());		
	}
	
	public static void main1(String[] args){
		List<Diagnosis> diagnosises = getDiagnosises();
		for (Diagnosis diagnosis : diagnosises){
			new RunThread(diagnosis).start();
		}
		while(true){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (Diagnosis diagnosis : diagnosises){
				System.out.println(diagnosis.getResultList());
			}
		}
	}
}
