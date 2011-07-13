package com.siteview.base.data;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.siteview.base.data.Report.DstrItem;
import com.siteview.base.manage.ManageSvapi;
import com.siteview.base.manage.RetMapInVector;
import com.siteview.base.treeInfo.MonitorInfo;
import com.siteview.ecc.reportserver.ReturnBean;
import com.siteview.ecc.util.Toolkit;
import com.siteview.jsvapi.Jsvapi;
import com.siteview.svdb.UnivData;

public class ReportDate {
	private Date m_begin_date;
	private Date m_end_date;

	private Map<String, Map<String, String>> m_fmap;
	public Map<String, Map<String, String>> getM_fmap()
	{
		return m_fmap;
	}

	public void setM_fmap(Map<String, Map<String, String>> m_fmap)
	{
		this.m_fmap = m_fmap;
	}

	private String nodeids = null;

	public String getNodeids() {
		return nodeids;
	}

	public void setNodeids(String nodeids) {
		this.nodeids = nodeids;
	}

	public String[] getNodeidsArray() {
		String[] a = this.getNodeids().split(",");
		return a;
	}

	public ReportDate(Date begin_date, Date end_date) {
		this.m_begin_date = begin_date;
		this.m_end_date = end_date;
	}

	public Date getM_begin_date() {
		return m_begin_date;
	}
	public Date getM_end_date() {
		return m_end_date;
	}

	public Map<String, Map<String, String>> getReportDate(String nodeids) throws Exception {
		this.setNodeids(nodeids);
		m_fmap = UnivData.queryReportData(nodeids, false, true, null, null, null, m_begin_date, m_end_date);
		return m_fmap;
	}
	/*
	 * 新封报告接口
	 */
	public Map<String, Map<String, String>> getReportDate(String nodeids,String dstrStatusNoNeed,Boolean dstrNeed,String return_value_filter) throws Exception {
		this.setNodeids(nodeids);
		m_fmap = UnivData.queryReportData(nodeids, false, dstrNeed, dstrStatusNoNeed, return_value_filter, null, m_begin_date, m_end_date);
		return m_fmap;
	}

	/**
	 * 根据topn报告需求，能够获取多个时间段的数据，
	 * 整合到一个结果集map中，其中有的数据是不准确的，
	 * 因为topn报告中未用到改数据，比如errorPercent
	 * ,okPercent,warnPercent等
	 * @param nodeids
	 * @param dstrStatusNoNeed
	 * @param dstrNeed
	 * @param return_value_filter
	 * @param dateArgs
	 * @return
	 * @throws Exception
	 */
	public Map<String, Map<String, String>> getReportDate(String nodeids,String dstrStatusNoNeed,Boolean dstrNeed,String return_value_filter,ReturnBean... dateArgs) throws Exception {
		Map<String, Map<String, String>> rtnMap = new HashMap<String, Map<String, String>>();
		if( ( dateArgs.length == 0 ) )
			throw new IllegalArgumentException("请输入您要查询的时间！");
		Arrays.sort(dateArgs, new Comparator<ReturnBean>(){
			@Override
			public int compare(ReturnBean o1, ReturnBean o2){
				return o1.getBeginTime().compareTo(o2.getBeginTime());
			}
		});
		for(ReturnBean rb : dateArgs){
			m_fmap = UnivData.queryReportData(nodeids, false, dstrNeed, dstrStatusNoNeed, return_value_filter, null, rb.getBeginTime() , rb.getEndTime());
			for( String id : nodeids.split(",") ) {
				
				//dstr节点
				String mkey = "(dstr)" + id;
				Map<String, String> value = rtnMap.get(mkey);
				if( value != null){
					value.putAll(m_fmap.get(mkey));
				}else{
					rtnMap.put(mkey, m_fmap.get(mkey));
				}
				
				//return_节点
				int size = getReturnSize(id);
				for( int i = 0; i < size; i++ ) {
					mkey = "(Return_" + i + ")" + id;
					
					value = rtnMap.get(mkey);
					if(value == null){
						rtnMap.put(mkey, m_fmap.get(mkey));
						continue;
					}
					double min = 0,max=0,average=0;
					
					try{min = Integer.parseInt(value.get("min"));}catch(Exception e){}
					try{max = Integer.parseInt(value.get("max"));}catch(Exception e){}
					try{average = Integer.parseInt(value.get("average"));}catch(Exception e){}
					
					value = m_fmap.get(mkey);
					try{min = Math.min(min,Double.parseDouble(value.get("min")));}catch(Exception e){}
					try{max = Math.max(max,Double.parseDouble(value.get("max")));}catch(Exception e){}
					try{average = (average + Double.parseDouble(value.get("average"))) / 2;}catch(Exception e){}
					String detail = value.get("detail");
					
					value = rtnMap.get(mkey);
					value.put("min", ""+min);
					value.put("max", ""+max);
					value.put("average", ""+average);
					value.put("detail", value.get("detail")+detail);
				}
				//1.26.42.5总的描述节点
				value= rtnMap.get(id);
				if(value == null){
					rtnMap.put(id, m_fmap.get(mkey));
				}
//						double okPercent = Double.parseDouble(propertyValue.get("okPercent"));
//						double warnPercent = Double.parseDouble(propertyValue.get("okPercent"));
//						double errorPercent = Double.parseDouble(propertyValue.get("okPercent"));
			}
		}
		m_fmap = rtnMap;
		return m_fmap;
	}
	private Date m_latest_create_time;
	private float m_disablePercent;

	public void display() {
		if (m_fmap == null)
			return;
		Jsvapi.getInstance().DisplayUtilMapInMap(m_fmap);
	}

	public Date getLatestCreateTime() {
		return m_latest_create_time;
	}

	public boolean isExpired(MonitorInfo info) {
		try {
			Date td = Toolkit.getToolkit().parseDate(info.getCreateTime());
			if (td.getTime() <= m_latest_create_time.getTime()) {
				// logger.info(" return cached Simple report of "+m_node.
				// getSvId());
				return false;
			}
		} catch (ParseException e1) {
		}
		return true;
	}

	public Map<String, Map<String, String>> getRawMap() {
		return m_fmap;
	}

	public float getDisablePercentOfSimpleReport() {
		return m_disablePercent;
	}

	public Map<Date, DstrItem> getDstr(String nodeid) {
		Map<Date, DstrItem> a = new LinkedHashMap<Date, DstrItem>();
		Map<Date, DstrItem> tempa = new HashMap<Date, DstrItem>();
		if (m_fmap == null)
			return a;
		m_disablePercent = 0;
		String mkey = "(dstr)" + nodeid;
		if (m_fmap.containsKey(mkey)) {
			Map<String, String> rdata = m_fmap.get(mkey);
			if (rdata != null) {
				int rsize = rdata.size();
				for (String key : rdata.keySet()) {
					try {
						Date td = Toolkit.getToolkit().parseDate(key);
						String value = rdata.get(key);

						int index = value.indexOf(" ");
						String v1 = value.substring(0, index).trim();
						String v2 = value.substring(index + 1).trim();
						if ("disable".equals(v1) || "bad".equals(v1))
							++m_disablePercent;

						tempa.put(td, new DstrItem(v1, v2));
					} catch (ParseException e1) {
						--rsize;
					}
				}
				m_disablePercent = (m_disablePercent / rsize) * 100;
			}
		}
		if (tempa.isEmpty())
			return a;
		ArrayList<Date> list = new ArrayList<Date>(tempa.keySet());
		Collections.sort(list, new Comparator<Date>() {
			public int compare(Date o1, Date o2) {
				return o1.compareTo(o2);
			}
		});
		int size = list.size();
		for (int i = 0; i < size; ++i) {
			Date d = list.get(i);
			DstrItem item = tempa.get(d);
			a.put(d, item);
		}
		return a;
	}

	public Map<Date, String> getReturnValueDetail(String nodeid, int index) {
		String detail = getReturnValue(nodeid, "detail", index);
		Map<Date, String> a = new LinkedHashMap<Date, String>();
		Map<Date, String> tempa = new HashMap<Date, String>();
		if (detail == null || detail.isEmpty())
			return a;

		String[] s = detail.split(",");
		int size = s.length;
		for (int i = 0; i < size; ++i) {
			try {
				String value = s[i];
				int ti = value.indexOf("=");
				String v1 = value.substring(0, ti).trim();
				String v2 = value.substring(ti + 1).trim();

				Date td = Toolkit.getToolkit().parseDate(v1);
				tempa.put(td, v2);
			} catch (ParseException e1) {
			}
		}
		if (tempa.isEmpty())
			return a;
		ArrayList<Date> list = new ArrayList<Date>(tempa.keySet());
		Collections.sort(list, new Comparator<Date>() {
			public int compare(Date o1, Date o2) {
				return o1.compareTo(o2);
			}
		});
		size = list.size();
		for (int i = 0; i < size; ++i) {
			Date d = list.get(i);
			String item = tempa.get(d);
			a.put(d, item);
		}
		return a;
	}

	public String getReturnValue(String nodeid, String key, int index) {
		if (m_fmap == null)
			return null;
		String mkey = "(Return_" + new Integer(index).toString() + ")" + nodeid;
		String str = null;
		if (m_fmap.containsKey(mkey)) {
			Map<String, String> rdata = m_fmap.get(mkey);
			if (rdata != null && rdata.containsKey(key))
				str = rdata.get(key);
		}
		return str;
	}

	public int getReturnSize(String nodeid) {
		if (m_fmap == null)
			return 0;
		int size = 0;
		
			Map<String, String> rdata = m_fmap.get(nodeid);
			if (rdata != null) {
				for (String key : rdata.keySet()) {
					String value = rdata.get(key);
					if (value != null && value.equals("ReturnValue"))
						++size;
				}
			}
		
		return size;
	}

	public String getPropertyValue(String nodeid, String key) {
		if (m_fmap == null)
			return null;
		
		
			Map<String, String> rdata = m_fmap.get(nodeid);
			if (rdata != null )
				return rdata.get(key);
		
		return null;
	}
	public void setPropertyValue(String nodeid, String key,String value) {
		if (m_fmap == null)
			m_fmap=new HashMap<String,Map<String, String>>();
		
		Map<String, String> map=m_fmap.get(nodeid);
		if(map==null)
		{
			map=new HashMap<String, String>();
			m_fmap.put(nodeid, map);
		}
		map.put(key, value);
		return;
	}
}
