package com.siteview.base.test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

public class Test {
	private final static Logger logger = Logger.getLogger(Test.class);
	
	public static void main(String []arg){
		Test test=new Test();
		String num=test.getWeekdayOfDateTime();
		logger.info("~~~~~~~~~~~~~~~~~~~~~~~~"+num);
	}
	public  String getWeekdayOfDateTime(){
		   DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
		   Calendar c = Calendar.getInstance();   
		   logger.info("abc@abc.com".matches("\\b[A-Z0-9._%-]+@[A-Z0-9._%-]+\\.[A-Z]{2,4}\\b"));
		      try {
		  c.setTime(df.parse(new Date().toString()));
		   } catch (Exception e) {
		  e.printStackTrace();
		   }
		   String weekday = String.valueOf(c.get(Calendar.DAY_OF_WEEK)-1);
		   return weekday;
		} 
}
