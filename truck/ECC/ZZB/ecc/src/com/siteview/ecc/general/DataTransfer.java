package com.siteview.ecc.general;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timebox;

public class DataTransfer extends GenericForwardComposer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2311249377250393114L;
	private Textbox dbIp;
	private Textbox dbPort;
	private Textbox dbUser;
	private Textbox dbPassword;
	private Textbox svdbIp;
	private Textbox dbName;
	private Textbox tdDay;
	private Textbox tdHour;
	private Textbox tdMinute;
	
	private Radio radio1;
	private Radio radio2;
	
	private Datebox tdDate;
	private Timebox tdTime;
	
//	private Button modify;
	private Button refresh;
//	private Button backup;
	
	private static String installPath = "C:/SiteView/WebECC/";
	
	static{
		readInstallPath();
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
//		checkPath();
		init();
	}
	
	private void init(){		
		Properties props = new Properties();
		String path = installPath + "WebECC8.1.3/Tomcat6/bin/dtConfig.properties";
		try {
			File file = new File(path);
			if(file.exists()){
				InputStream in = new BufferedInputStream (new FileInputStream(file));
				props.load(in);
				String str_dbIp = props.getProperty ("dbIp");
				String str_dbPort = props.getProperty ("dbPort");
				String str_dbUser = props.getProperty ("dbUser");
				String str_dbPassword = props.getProperty ("dbPassword");
				String str_dbName = props.getProperty ("dbName");
				String str_svdbIp = props.getProperty ("svdbIp");
//				String str_dowhat = props.getProperty("dowhat");
//				String str_transferTypes = props.getProperty("transferType");
				String str_delayType = props.getProperty("delayType");
//				String str_delay = props.getProperty("delay");

				dbIp.setValue(str_dbIp);
				dbPort.setValue(str_dbPort);
				dbUser.setValue(str_dbUser);
				dbPassword.setValue(str_dbPassword);
				svdbIp.setValue(str_svdbIp);
				dbName.setValue(str_dbName);
				
				if("absolute".equals(str_delayType)){
					radio1.setSelected(true);
				}else if("relative".equals(str_delayType)){
					radio2.setSelected(true);
				}else{
					radio1.setSelected(true);
				}
				
				Calendar calStart = Calendar.getInstance();
				calStart.setTime(new Date());
				tdDate.setValue(calStart.getTime());
				tdTime.setValue(calStart.getTime());
				tdDay.setValue("0");
				tdHour.setValue("0");
				tdMinute.setValue("0");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void checkPath(){
		File file = new File("/");
		String str;
		try {
			str = file.getCanonicalPath();
			System.out.println("====> " + str + " ==========================");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void onClick$modify(){
		try {
			if(!validateData(false)){
				Messagebox.show("请输入完整信息！", "提示", Messagebox.OK ,Messagebox.INFORMATION);
			}else{
				try {
					modifyProperties();
					restartService("DataTransferService");
					Messagebox.show("修改成功！", "提示", Messagebox.OK ,Messagebox.INFORMATION);
				} catch (Exception e) {
					Messagebox.show("修改失败！", "提示", Messagebox.OK ,Messagebox.ERROR);
					e.printStackTrace();
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void onClick$refresh(){
		refresh.setDisabled(true);
		init();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		refresh.setDisabled(false);
	}
	
	public void onClick$backup(){
		try{
			if(!validateData(true)){
				Messagebox.show("请输入完整信息！", "提示", Messagebox.OK ,Messagebox.INFORMATION);
			}else{
				try {
					int result = Messagebox.show("是否进行数据导回？进行数据导回时系统会关闭WebECC服务，并在完成导回后重新启动服务", "询问", Messagebox.OK|Messagebox.NO ,Messagebox.QUESTION);
					if(result == Messagebox.OK){
						HashMap<String, String> map = modifyPropertiesByBackup();
						restartService("DataTransferService");
						Messagebox.show("设置成功！5秒后进行数据导回", "提示", Messagebox.OK ,Messagebox.INFORMATION);
//						Thread.sleep(10000);
//						restoreProperties(map.get("delayType"), map.get("delay"));
//						restartService("DataTransferService");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 对输入的信息进行验证
	 * @return boolean
	 */
	private boolean validateData(boolean isBackup){
		String str_dbIp = dbIp.getValue();
		if(str_dbIp == null || str_dbIp.isEmpty()){
			return false;
		}
		
		String str_dbPort = dbPort.getValue();
		if(str_dbPort == null || str_dbPort.isEmpty()){
			return false;
		}
		
		String str_dbUser = dbUser.getValue();
		if(str_dbUser == null || str_dbUser.isEmpty()){
			return false;
		}
		
		String str_dbPassword = dbPassword.getValue();
		if(str_dbPassword == null || str_dbPassword.isEmpty()){
			return false;
		}
		
		String str_svdbIp = svdbIp.getValue();
		if(str_svdbIp == null || str_svdbIp.isEmpty()){
			return false;
		}
		
		String str_dbName = dbName.getValue();
		if(str_dbName == null || str_dbName.isEmpty()){
			return false;
		}
		
		if(!isBackup){
			String str_tdDay = tdDay.getValue();
			if(str_tdDay == null || str_tdDay.isEmpty()){
				return false;
			}
			try{
				Integer.parseInt(str_tdDay);
			}catch (Exception e){
				return false;
			}
			
			String str_tdHour = tdHour.getValue();
			if(str_tdHour == null || str_tdHour.isEmpty()){
				return false;
			}
			try{
				Integer.parseInt(str_tdHour);
			}catch (Exception e){
				return false;
			}
			
			String str_tdMinute = tdMinute.getValue();
			if(str_tdMinute == null || str_tdMinute.isEmpty()){
				return false;
			}
			try{
				Integer.parseInt(str_tdMinute);
			}catch (Exception e){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 修改配置文件
	 * @param svdbIp
	 * @throws Exception 
	 */
	private void modifyProperties() throws Exception{
		ArrayList<String> data = new ArrayList<String>();
		BufferedReader read = null;
		BufferedWriter write = null;
		String _delayType = "";
		long _delay = 0l;
		String path = installPath + "WebECC8.1.3/Tomcat6/bin/dtConfig.properties";
		
		try {
			read = new BufferedReader(new FileReader(path));
			String line = read.readLine();
			while(line != null){
				if(line.startsWith("dbIp")){
					line = "dbIp=" + dbIp.getValue();
				}else if(line.startsWith("dbPort")){
					line = "dbPort=" + dbPort.getValue();
				}else if(line.startsWith("dbUser")){
					line = "dbUser=" + dbUser.getValue();
				}else if(line.startsWith("dbPassword")){
					line = "dbPassword=" + dbPassword.getValue();
				}else if(line.startsWith("dbName")){
					line = "dbName=" + dbName.getValue();
				}else if(line.startsWith("svdbIp")){
					line = "svdbIp=" + svdbIp.getValue();
				}else if(line.startsWith("delayType")){
					if(radio1.isSelected()){
						_delayType = "absolute";
					}else if(radio2.isSelected()){
						_delayType = "relative";
					}
					line = "delayType=" + _delayType;
				}else if(line.startsWith("delay")){
					Calendar ca = Calendar.getInstance();
					if(radio1.isSelected()){
						Calendar t = Calendar.getInstance();
						ca.setTime(tdDate.getValue());
						t.setTime(tdTime.getValue());
						ca.set(Calendar.HOUR_OF_DAY, t.get(Calendar.HOUR_OF_DAY));
						ca.set(Calendar.MINUTE, t.get(Calendar.MINUTE));
						
						Date nowDate = new Date();
						Date newDate = ca.getTime();
						_delay = newDate.getTime() - nowDate.getTime();
					}else if(radio2.isSelected()){
						int day = 0;
						int hour = 0;
						int minute = 0;
						
						if(tdDay.getValue() != null && !tdDay.getValue().equals("")){
							day = Integer.parseInt(tdDay.getValue());
						}
						if(tdHour.getValue() != null && !tdHour.getValue().equals("")){
							hour = Integer.parseInt(tdHour.getValue());
						}
						if(tdMinute.getValue() != null && !tdMinute.getValue().equals("")){
							minute = Integer.parseInt(tdMinute.getValue());
						}
						
						Date nowDate = new Date();
						ca.setTime(nowDate);
						ca.add(Calendar.MINUTE, minute);
						ca.add(Calendar.HOUR_OF_DAY, hour);
						ca.add(Calendar.DAY_OF_MONTH, day);
						
						Date newDate = ca.getTime();
						_delay = newDate.getTime() - nowDate.getTime();
					}
					line = "delay=" + _delay;
				}else if(line.startsWith("dowhat")){
					line = "dowhat=s2d";
				}else if(line.startsWith("transferType")){
					line = "transferType=1,2";
				}else if(line.startsWith("#dowhat")){
					line = "#dowhat=s2d";
				}else if(line.startsWith("#delayType")){
					line = "#delayType=" + _delayType;
				}else if(line.startsWith("#delay")){
					line = "#delay=" + _delay;
				}
				data.add(line);
				line = read.readLine();
			}
			
			write = new BufferedWriter(new FileWriter(path));
			for(String str : data){
				write.write(str);
				write.newLine();
			}
			read.close();
			write.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally{
			if(read != null){
				read = null;
			}
			if(write != null){
				write = null;
			}
		}
	}
	
	/**
	 * 为实现从数据库向svdb中导数据修改配置文件
	 * @param delayType
	 * @param delay
	 */
	private HashMap<String, String> modifyPropertiesByBackup(){
		ArrayList<String> data = new ArrayList<String>();
		HashMap<String, String> result = new HashMap<String, String>();
		BufferedReader read = null;
		BufferedWriter write = null;
		String delayType = "";
		String delay = "";
		String path = installPath + "WebECC8.1.3/Tomcat6/bin/dtConfig.properties";
		
		try {
			read = new BufferedReader(new FileReader(path));
			String line = read.readLine();
			while(line != null){
				if(line.startsWith("dbIp")){
					line = "dbIp=" + dbIp.getValue();
				}else if(line.startsWith("dbPort")){
					line = "dbPort=" + dbPort.getValue();
				}else if(line.startsWith("dbUser")){
					line = "dbUser=" + dbUser.getValue();
				}else if(line.startsWith("dbPassword")){
					line = "dbPassword=" + dbPassword.getValue();
				}else if(line.startsWith("dbName")){
					line = "dbName=" + dbName.getValue();
				}else if(line.startsWith("svdbIp")){
					line = "svdbIp=" + svdbIp.getValue();
				}else if(line.startsWith("dowhat")){
					line = "dowhat=d2s";
				}else if(line.startsWith("transferType")){
					line = "transferType=1,2";
				}else if(line.startsWith("delayType")){
					delayType = line.substring(line.indexOf('=') + 1);
					line = "delayType=" + "absolute";
					result.put("delayType", delayType);
				}else if(line.startsWith("delay")){
					delay = line.substring(line.indexOf('=') + 1);
					line = "delay=" + 5000;
					result.put("delay", delay);
				}else if(line.startsWith("##dowhat")){
					line = "#dowhat=s2d";
				}else if(line.startsWith("#delayType")){
					line = "#delayType=" + delayType;
				}else if(line.startsWith("#delay")){
					line = "#delay=" + delay;
				}
				data.add(line);
				line = read.readLine();
			}
			
			write = new BufferedWriter(new FileWriter(path));
			for(String str : data){
				write.write(str);
				write.newLine();
			}
			read.close();
			write.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(read != null){
				read = null;
			}
			if(write != null){
				write = null;
			}
		}
		return result;
	}
	
	/**
	 * 恢复配置文件
	 * @param delayType
	 * @param delay
	 */
	private void restoreProperties(String delayType, String delay){
		ArrayList<String> data = new ArrayList<String>();
		BufferedReader read = null;
		BufferedWriter write = null;
		String path = installPath + "WebECC8.1.3/Tomcat6/bin/dtConfig.properties";
		
		try {
			read = new BufferedReader(new FileReader(path));
			String line = read.readLine();
			while(line != null){
				if(line.startsWith("transferType")){
					line = "transferType=1,2";
				}else if(line.startsWith("delayType")){
					line = "delayType=" + delayType;
				}else if(line.startsWith("delay")){
					line = "delay=" + delay;
				}else if(line.startsWith("dowhat")){
					line = "dowhat=s2d";
				}
				data.add(line);
				line = read.readLine();
			}
			
			write = new BufferedWriter(new FileWriter(path));
			for(String str : data){
				write.write(str);
				write.newLine();
			}
			read.close();
			write.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(read != null){
				read = null;
			}
			if(write != null){
				write = null;
			}
		}
	}
	
	private void restartService(String serviceName) throws IOException, InterruptedException{
		Runtime.getRuntime().exec("cmd.exe /c net stop " + serviceName);
		Thread.sleep(1000);
		Runtime.getRuntime().exec("cmd.exe /c net start " + serviceName);
	}
	
	public static void readInstallPath(){
		File file = new File("C:\\Program Files\\installPath.properties");
		if(!file.exists()){
			return;
		}
		Properties props = new Properties();
		InputStream in = null;
		try {
			in = new BufferedInputStream (new FileInputStream(file));
			props.load(in);
			String curPath = props.getProperty("installPath");
			if(curPath != null && !curPath.isEmpty()){
				installPath = curPath;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
