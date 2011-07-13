package com.siteview.ecc.general;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.zk.ui.util.GenericAutowireComposer;
import org.zkoss.zul.Label;

import com.siteview.base.data.IniFile;
import com.siteview.base.manage.ManageSvapi;
import com.siteview.base.manage.Manager;
import com.siteview.base.manage.RetMapInMap;

/*dowhat= encrypt ,		X1= , X2= , X3= ,	...		//逐个加密完后返回
 dowhat= decrypt ,		X1= , X2= , X3= ,	...		//逐个解密完后返回
 从ini和加密狗中读取相关数据，解密后输出
 */
public class License extends GenericAutowireComposer {

	private static Label pointUsedLabel;
	private static Label deviceUsedLabel;
	private static Label pointLabel;
	private static Label deviceLabel;
	private static Label endTimeLabel;
	private static Label usingLabel;
//	private Label versionLabel;
	private static Map<String, Map<String, String>> m_fmap = new HashMap<String, Map<String, String>>();
	private Map<String, String> kmap = new HashMap<String, String>();

	// {return={LRzJJL4KNPRcQKIQ=30, RRI4ePccLQ4zQQLN=50, zNz4N71cQ4KNN47O=200, P7zPOPNe4OQG77KGR7K7eNGK7czLKNce=2009-03-16}}
	//lasttime;nw;point;starttime

	public static Map<String, Map<String, String>> dodecrypt(String[] x1)
			throws Exception {
		HashMap<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "decrypt");
		for (int i = 0; i <= x1.length-1; i++)
			ndata.put(x1[i], "");
		/*
		 * ndata.put("X2", x2); ndata.put("X2", x3); ndata.put("X2", x4);
		 * ndata.put("X2", x5);
		 */

		RetMapInMap ret = ManageSvapi.GetUnivData(ndata);
		if (!ret.getRetbool())
			throw new Exception("Failed to load :" + ret.getEstr());


		/*
		 * if (m_fmap.containsKey("return")) m_fmap.remove("return");
		 * Jsvapi.DisplayUtilMapInMap(m_fmap);
		 */
		// logger.info("!!!!!!!!!!这是map");
		return ret.getFmap();
	}

	public static void onRefresh() throws Exception
	{
		
		IniFile ini = new IniFile("general.ini");
		
		ini.load();
		// ini.display();
		String[] x = new String[4];
		// ArrayList<String> j=null;
		x[0] = ini.getValue("license", "lasttime");
		x[1] = ini.getValue("license", "nw");
		x[2] = ini.getValue("license", "point");
		/*
		 * i.add(ini.getValue("license", "shownw")); i.add(ini.getValue("license", "showpoint")); i.add(ini.getValue("license", "showtime"));
		 */
		x[3] = ini.getValue("license", "starttime");
		String specialUserName = ini.getValue("license", "Customer");
		// logger.info(i+"!!!!!!!!!!这是i");
		m_fmap = dodecrypt(x);
		// pointLabel.setValue(m_fmap.get("return").get(x[2]));
		// deviceLabel.setValue(m_fmap.get("return").get(x[1]));
		SimpleDateFormat t=new SimpleDateFormat("yyyy-MM-dd");
		String s = m_fmap.get("return").get(x[3]);
		int i=Integer.parseInt(m_fmap.get("return").get(x[0]));
		Calendar cal = Calendar.getInstance();
		cal.setTime(t.parse(s));
		cal.add(Calendar.DAY_OF_YEAR, i);
		Date d = cal.getTime();
		endTimeLabel.setValue(t.format(d));
		usingLabel.setValue(specialUserName);
//		versionLabel.setValue("SiteView ECC 8.1.3");
		
		// 正式版用户的授权数据是从加密狗上读取的	
		Map<String, String> licensedata = Manager.getLicenseData();		
		pointLabel.setValue(getValueInRMap(licensedata, "monitorNum"));
		deviceLabel.setValue(getValueInRMap(licensedata, "networkNum"));
		pointUsedLabel.setValue(getValueInRMap(licensedata, "pointUsed"));
		deviceUsedLabel.setValue(getValueInRMap(licensedata, "networkUsed"));
		
		// 正式版用户不显示软件到期时间
		if (getValueInRMap(licensedata, "isTrial").equals("false"))
			endTimeLabel.setVisible(false);
	}
	
	private static String getValueInRMap(Map<String, String> rmap, String key)
	{
		if(rmap==null || key==null)
			return new String("");
		String v= rmap.get(key);
		if(v==null)
			return new String("");
		return v;
	}
	
	/**
	 * 返回可用点数
	 * @return
	 */
	public int getAvalibelPoint(){
		Map<String, String> licensedata = Manager.getLicenseData();		
		int totalPoint = Integer.parseInt(getValueInRMap(licensedata, "monitorNum"));
		int usedPoint = Integer.parseInt(getValueInRMap(licensedata, "pointUsed"));
		int availabelPoint = totalPoint - usedPoint;
		return availabelPoint;
	}
	
	/**
	 * 返回可用网络设备数
	 * @return
	 */
	public int getAvalibelDevicePoint(){
		Map<String, String> licensedata = Manager.getLicenseData();		
		int totalPoint = Integer.parseInt(getValueInRMap(licensedata, "networkNum"));
		int usedPoint = Integer.parseInt(getValueInRMap(licensedata, "networkUsed"));
		int availabelPoint = totalPoint - usedPoint;
		return availabelPoint;
	}
}
