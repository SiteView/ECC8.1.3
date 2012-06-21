package com.siteview.ecc.treeview;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import com.siteview.ecc.start.EccStarter;
import com.siteview.ecc.start.StarterListener;
import com.siteview.ecc.util.Toolkit;


public class EccWebAppInit implements StarterListener {
	private final static Logger logger = Logger.getLogger(EccWebAppInit.class);

	private HashMap<String,String> imgMap = new HashMap<String,String>();
	private HashMap<String,String> actImgMap = new HashMap<String,String>();
	
	
	public static EccWebAppInit getInstance()
	{
		return (EccWebAppInit) EccStarter.getInstance()
		.getStarterListener("eccWebAppInit");
	}
	@Override
	public void destroyed(EccStarter starter) {
		
	}
	@Override
	public void startInit(EccStarter starter) {
		loadProperites(starter.getServletContext());
	}
	
	final private String BLANK_IMG = "/main/images/none.gif";
	
	public static String getWebDir() {
		return EccStarter.getInstance().getWebDir();
	}


	public String getActionImage(String action) {
		Object img = actImgMap.get(action);
		if(img==null)
		{
			 return "/main/images/action/"+action+".gif";
				//logger.info("action="+action+" not found images!");
		}
		return (img == null) ? BLANK_IMG : img.toString();
	}

	public String getStatusImage(String status) {
		Object img = imgMap.get(status);
		if(img==null)
			logger.info("status=status_"+status+" not found images!");
		return (img == null) ? BLANK_IMG : img.toString();
	}

	public String getImage(String type) {
		Object img = imgMap.get(type);
		if(img==null)
			logger.info("type="+type+" not found images!");
		return (img == null) ? BLANK_IMG : img.toString();
	}
	public String getImage(String type,String status) {
		Object img = imgMap.get(new StringBuffer(type).append("_").append(status).toString());
		return (img == null) ? BLANK_IMG : img.toString();
	}
	public String getImage(String type,int status) {
		return getImage(type,Toolkit.getToolkit().changeStatusToString(status));
	}


	private void loadProperites(ServletContext context) {
		loadImageProperties(context);
	}

	
	private void loadImageProperties(ServletContext context)
	{
		
		InputStreamReader isr=null;
	  FileInputStream fis=null;
	  BufferedReader bufReader=null;
		try {
			
			

			fis=new FileInputStream(new File(context
							.getRealPath("/main/eccImages.properties")));
							
			isr=		new InputStreamReader(fis);
			bufReader = new BufferedReader(isr);
			
			
			Properties imgProp=new Properties();
			imgProp.load(bufReader);
			for(Object key:imgProp.keySet())
			{
				if(key.toString().startsWith("act_"))
					actImgMap.put(key.toString().substring("act_".length()),imgProp.get(key).toString());
				else	
					imgMap.put(key.toString(),imgProp.get(key).toString());
			}

		} catch (IOException e) {
			System.err.
					println("Ingored: failed to load eccImages.properties file, \nCause: "
							+ e.getMessage());
		}finally
		{
			try{bufReader.close();}catch(Exception e){}
			try{fis.close();}catch(Exception e){}
			try{isr.close();}catch(Exception e){}
		}
	}
	
}
