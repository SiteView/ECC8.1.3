package com.siteview.ecc.css;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import com.siteview.ecc.start.EccStarter;
import com.siteview.ecc.start.StarterListener;

public class LoadCssFile implements StarterListener {
	private static final long serialVersionUID = -3397491246546607268L;
	private final static Map<String,String> cssNameMap = new LinkedHashMap<String,String>();
	public static Map<String, String> getCssNameMap() {
		return cssNameMap;
	}

	@Override
	public void destroyed(EccStarter starter) {
		

	}

	@Override
	public void startInit(EccStarter starter) {
		loadUrlMap(starter);
	}
	private void loadUrlMap(EccStarter starter) {

	  InputStreamReader isr=null;
	  FileInputStream fis=null;
	  BufferedReader bufReader=null;
		try {

		  fis=new FileInputStream(new File(starter
							.getRealPath("/main/themes.properties")));
							
			isr=		new InputStreamReader(fis);
			bufReader = new BufferedReader(isr);
			Properties cssProp = new Properties();
			cssProp.load(bufReader);
			for (Object key : cssProp.keySet())
				cssNameMap.put(key.toString(), cssProp.get(key).toString());
			
		} catch (IOException e) {
			System.err
					.println("Ingored: failed to load css_names.properties file, \nCause: "
							+ e.getMessage());
		}finally
		{
			try{bufReader.close();}catch(Exception e){}
			try{fis.close();}catch(Exception e){}
			try{isr.close();}catch(Exception e){}
		}

	}
}
