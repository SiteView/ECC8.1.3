package com.siteview.ecc.system;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Diagnosis {
	private final static String SUCCESS = "不存在问题!";
	private final static String FAILURE = "存在问题:";
	private final static String BEGIN = "开始时间:";
	private final static String TESTING = "诊断进行中......";
	private final static SimpleDateFormat DATE_TO_STRING = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
	private List<String> retlist = new CopyOnWriteArrayList<String>();
	
	public List<String> getResultList() {
		return retlist;
	}

	public String getLastResult() {
		if (getResultList().size() == 0) return null;
		return getResultList().get(getResultList().size() - 1);
	}
	public abstract String getDescription();
	public abstract String getName();
	public abstract void execute()throws Exception;
	public void run() throws Exception{
		try{
			getResultList().clear();
			isOk = false;
			getResultList().add(BEGIN + DATE_TO_STRING.format(new Date()));
//			getResultList().add(TESTING);
			this.execute();
			getResultList().add(SUCCESS);
			isOk = true;
		}catch(Exception e){
			e.printStackTrace();
			getResultList().add(FAILURE + e.getMessage());
		}
	}
	private boolean isOk = false;
	public boolean isOk() {
		return isOk;
	}
}
