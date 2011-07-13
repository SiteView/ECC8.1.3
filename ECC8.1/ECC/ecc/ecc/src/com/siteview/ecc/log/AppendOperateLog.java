package com.siteview.ecc.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.siteview.base.manage.ManageSvapi;
import com.siteview.base.manage.RetMapInMap;
import com.siteview.base.manage.View;
import com.siteview.base.tree.INode;


public class AppendOperateLog
{
	private static final Log log = LogFactory.getLog(AppendOperateLog.class);
	private final static SimpleDateFormat SDF = new SimpleDateFormat ("yyyy-M-d HH:mm:ss");
	private static final List<Map<String, String>>	m_data_to_append= new CopyOnWriteArrayList<Map<String, String>>();
	private static final SentThread sendth= new SentThread(); 
	static
	{
		sendth.start();	
	}
	
	static public void addOneLog(View view, INode node, OpTypeId Type, OpObjectId Object)
	{
		String uid= view.getLoginName();
		String info= uid+ " " + Type.name + Object.name + " " + node.getName() + "(" + node.getSvId() + ")";
		addOneLog(uid, info, Type, Object);
	}
	
	static public void addOneLog(String loginName, String info, OpTypeId type, OpObjectId object)
	{
		try
		{
			if(loginName==null || "".equals(loginName) || info==null || type==null || object==null)
				return;	
			 //String _OperateTime= Toolkit.getToolkit().formatDate();
			 //2002-10-08 15:09:22  ==> 2002-10-8 15:09:22
			 //统一 日期记录的格式.
			/*
			 java.util.GregorianCalendar now = new java.util.GregorianCalendar();
			 int year = now.get(now.YEAR);
			 int month = now.get(now.MONTH)+1;
			 int day   = now.get(now.DAY_OF_MONTH);
			 
			 int hour = now.get(now.HOUR_OF_DAY);
			 String hourValue = String.valueOf(hour);
			 if(hour<10){
				 hourValue = "0"+hourValue;
			 }
			 int minute = now.get(now.MINUTE);
			 String minuteValue = String.valueOf(minute);
			 if(minute<10){
				 minuteValue ="0"+minuteValue;
			 }
			 int second = now.get(now.SECOND);
			 String secondValue = String.valueOf(second);
			 if(second<10){
				 secondValue = "0"+secondValue;
			 }

			 String _OperateTime = year+"-"+month+"-"+day+" "+hourValue+":"+minuteValue+":"+secondValue;
			 */
			String _OperateTime = SDF.format(new Date());
			 Map<String, String> alog= new FastMap<String, String>();
			 alog.put("_UserID", loginName);
			 alog.put("_OperateTime", _OperateTime);
			 alog.put("_OperateObjInfo", info);
			 alog.put("_OperateType", type.id);
			 alog.put("_OperateObjName", object.id);
			 
			 m_data_to_append.add(alog);
		} catch (Exception e)
		{
			e.printStackTrace();
		} 
	}
	
	public static void sentOutOperateLog() 
	{
		if(m_data_to_append.isEmpty())
			return;
		
		List<Map<String, String>> data= new FastList<Map<String, String>>();
		data.addAll(m_data_to_append);

		Map<String, String> ndata = new FastMap<String, String>();
		ndata.put("dowhat", "AppendOperateLog");
		for(Map<String, String> logValue: data)
		{
			try
			{
				if (logValue.isEmpty()) continue;
				Map<String, Map<String, String>> fdata= new FastMap<String, Map<String, String>>();
				fdata.put("OperateLog_1", logValue);
				RetMapInMap rmap= ManageSvapi.SubmitUnivData(fdata, ndata);
				if(!rmap.getRetbool())
					throw new Exception(rmap.getEstr());
				m_data_to_append.remove(logValue);
			} catch (Exception e)
			{
				e.printStackTrace();
				log.error(e);
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static class SentThread extends Thread
	{
		public SentThread(){
			this.setName(SentThread.class.getName());
		}
		public void run()
		{
			while (true)
			{
				try
				{
					Thread.sleep(5 * 1000);
					sentOutOperateLog();
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
