package com.siteview.base.data;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.siteview.base.manage.ManageSvapi;
import com.siteview.base.manage.Manager;
import com.siteview.base.manage.RetMapInMap;
import com.siteview.base.manage.View;
import com.siteview.base.tree.INode;
import com.siteview.base.treeInfo.MonitorInfo;
import com.siteview.ecc.util.Toolkit;
import com.siteview.jsvapi.Jsvapi;
import com.siteview.svdb.UnivData;


public class Report
{
	private final static Logger logger = Logger.getLogger(Report.class);
	private INode										m_node;
	public INode getM_node() {
		return m_node;
	}

	public void setM_node(INode m_node) {
		this.m_node = m_node;
	}

	private Date										m_latest_create_time;
	private boolean										m_simple_report	= false;

	private Date										m_begin_date;
	private Date										m_end_date;
	private float										m_disablePercent;
	
	private boolean										m_compressed=false;
	private boolean										m_dstr_need=true;	
	
	private Map<String, Map<String, String>>	m_fmap; 
	
	
	public Report(INode node)
	{
		m_compressed=false;
		m_simple_report= true;
		m_node= node;
	}
	
	/**
	 * http://download.developers.sun.com.cn/javadoc/jdk6/docs/zh/api/java/util/Date.html <br/>
	 *<br/>
	 * ���� Date ���п��Խ��ܻ򷵻��ꡢ�¡����ڡ�Сʱ�����Ӻ���ֵ�ķ����У���ʹ������ı�ʾ��ʽ�� <br/>
	 *<br/>
	 * ��� y ������ y - 1900 ��ʾ�� <br/>
	 * �·��ɴ� 0 �� 11 ��������ʾ��0 ��һ�¡�1 �Ƕ��µȵȣ���� 11 ��ʮ���¡� <br/>
	 * ���ڣ�һ���е�ĳ�죩��ͨ����ʽ������ 1 �� 31 ��ʾ�� <br/>
	 * Сʱ�ɴ� 0 �� 23 ��������ʾ����ˣ�����ҹ�� 1 a.m. ��ʱ���� 0 �㣬�����絽 1 p.m. ��ʱ���� 12 �㡣 <br/>
	 * ���Ӱ�ͨ����ʽ�� 0 �� 59 ��������ʾ�� <br/>
	 * ���� 0 �� 61 ��������ʾ��ֵ 60 �� 61 ֻ�����뷢��������������Ҳֻ����ʵ����ȷ��������� Java ʵ���С��ڰ���ǰ��������ķ�ʽ������������ͬһ�����ڷ����Ǽ������ܵģ����˹淶��ѭ ISO C �����ں�ʱ��Լ���� <br/>
	 * �����������У������ЩĿ�ĸ��跽���Ĳ�������Ҫ��ָ���ķ�Χ�ڣ����磬���԰�����ָ��Ϊ 1 �� 32 �գ�����������Ϊ 2 �� 1 �յ���ͬ���塣
	 */
	public Report(INode node, Date beginDate, Date endDate)
	{
		m_node= node;	
		m_begin_date= beginDate;
		m_end_date= endDate;
	}
	
	public Report(INode node, Map<String, Map<String, String>> fmap)
	{
		m_node= node;
		m_fmap= fmap;
	}
	public Report() {
		// TODO Auto-generated constructor stub
	}

	public void setM_simple_report(boolean m_simple_report) {
		this.m_simple_report = m_simple_report;
	}
	
	public void setCompress(boolean comp)
	{
		m_compressed= comp;
	}

	public void setDstrNeed(boolean need)
	{
		m_dstr_need= need;
	}
	
	public void display()
	{
		if(m_fmap==null)
			return;
		Jsvapi.getInstance().DisplayUtilMapInMap(m_fmap);
		logger.info(" disable Percent: " + m_disablePercent);
	}
	
	public Date getLatestCreateTime()
	{
		return m_latest_create_time;
	}
	
	public boolean isExpired(MonitorInfo info)
	{
		try
		{
			Date td = Toolkit.getToolkit().parseDate(info.getCreateTime());
			if (td.getTime() <= m_latest_create_time.getTime())
			{
//				logger.info(" return cached Simple report of "+m_node.getSvId());
				return false;
			}
		} catch (ParseException e1)
		{
		}
		logger.info(" Simple report of "+m_node.getSvId()+ " is expired! ");
		return true;
	}
	
	public Map<String, Map<String, String>> getRawMap()
	{
		return m_fmap;
	}
	
	/**
	 * //ͳ�Ƽ������      id �����Ƕ��ŷָ��Ķ��id  ,�������豸id ����id ,Ĭ��ѹ�����ݣ�����������Ϊ true ��
	 *<br/> ��ע��<400�����ݲ�ѹ����>=400��ѹ����200������ѹ�����ݵĸ�������Ϊ50000)
	 *<br/> dowhat= QueryReportData  ,  id= XXX ,      compress=true/false,     dstrNeed=true/false Ĭ�ϲ���dstr������������Ϊ false),    byCount= 123 (�����ֵ�򰴸�����ѯ�������ǰ�ʱ��) 
	 *<br/>                            begin_year= XXX,  begin_month= XXX,  begin_day= XXX,  begin_hour= XXX,  begin_minute= XXX,  begin_second= XXX,  
	 *<br/>                            end_year= XXX,    end_month= XXX,    end_day= XXX,    end_hour= XXX,    end_minute= XXX,    end_second= XXX,
	 *<br/> //��ϸ�����������£����µ�������׷�� 2007-03-15 12:38:02=20.5747,2007-02-27 14:20:21=(status)bad,2007-02-26 19:10:21=(status)null,2007-02-24 19:16:21=(status)disable,2007-01-15 09:26:01=27.8916,
	 *<br/> //��ϸ���������ǽ�ԭʼ���ݸ���ѹ����ƽ�����������ݣ������ֵ����Сֵ�� dstr ��������ԭʼ����
	 */	
	public Map<String, Map<String, String>> load()  throws Exception
	{
		if( !m_node.getType().equals(INode.MONITOR) )
			throw new Exception(" Refuse to query report of this node, id: " + m_node.getSvId() + " (" + m_node.getType() + ")");
		
		m_fmap = UnivData.queryReportData(m_node.getSvId(), m_compressed, m_dstr_need, null, null,m_simple_report ? 40 : null, m_begin_date, m_end_date);
		
		if(m_simple_report)
		{
			String ltime=null;
			if(m_fmap.containsKey(m_node.getSvId()))
			{
				Map<String, String> rdata= m_fmap.get(m_node.getSvId());
				if(rdata!=null && rdata.containsKey("latestCreateTime"))
					ltime= rdata.get("latestCreateTime");
			}
			if(ltime!=null)
			{
				try
				{
					Date td = Toolkit.getToolkit().parseDate(ltime);
					m_latest_create_time= td;
					return m_fmap;
				} catch (ParseException e1)
				{
					throw new Exception(" Error in parsing latestCreateTime ! ");
				}
			}
		}
		return m_fmap;
	}
	
	public static void main(String[] args)
	{
		
		View w=null;
		try
		{
			String session = Manager.createView("admin", "siteview");
			w= Manager.getView(session);
			//w.getChangeTree();
		} catch (Exception e)
		{
			e.printStackTrace();
		}	
		if(w==null)
			return;
		INode n= w.getNode("1.9.6.53");
		if(n==null)
			return;
		
		Date begin= new Date(2009-1900, 2-1 ,10, 8, 42, 00);
		Date end= new Date(2009-1900, 2-1 ,26, 15, 04, 50);
		logger.info("   @@@@@@@      begin:   "+begin.toLocaleString());
		logger.info("   @@@@@@@        end:   "+end.toLocaleString());
//		Report r= new Report(n, b, e);
		
		Report r= new Report(n);
		try
		{
			r.load();
		} catch (Exception ee)
		{
			ee.printStackTrace();
		}
		Map<Date, DstrItem> res= r.getDstr();
		for(Date d : res.keySet()){
			logger.info(d.toLocaleString()+"===================="+res.get(d).status+"===================="+res.get(d).value);
		}
	}


//	   -- Display UtilMapInMap begin (4 node) -- 
//	     ---- (Return_1)1.356.2 (13 key) ----
//	        min= "0"
//	        detail= ""
//	        max= "0"
//	        sv_drawimage= "0"
//	        sv_drawtable= "1"
//	        when_max= ""
//	        MonitorName= "127.0.0.1:Cpu"
//	        ReturnName= "CPU��ϸʹ����(%)"
//	        sv_drawmeasure= "0"
//	        sv_primary= "1"
//	        sv_baseline= "0"
//	        latest= "0"
//	        average= "0"
//	     ---- (Return_0)1.356.2 (13 key) ----
//	        min= "8"
//	        detail= "2009-02-26 16:14:34=46,2009-02-26 16:04:33=30,2009-02-26 15:54:32=75,2009-02-26 15:44:31=46,2009-02-26 15:34:35=57,2009-02-26 15:24:33=46,2009-02-26 15:14:33=19,2009-02-26 15:04:33=46,2009-02-26 14:54:32=35,2009-02-26 14:44:31=46,2009-02-26 14:34:35=75,2009-02-26 14:24:33=47,2009-02-26 14:14:33=46,2009-02-26 14:04:31=57,2009-02-26 13:54:33=35,2009-02-26 13:44:33=57,2009-02-26 13:34:32=46,2009-02-26 13:24:35=46,2009-02-26 13:14:34=8,2009-02-26 13:04:33=58,2009-02-26 12:54:31=66,2009-02-26 12:44:34=85,2009-02-26 12:34:33=54,2009-02-26 12:24:33=44,2009-02-26 12:14:34=19,2009-02-26 12:04:36=19,2009-02-26 11:54:32=32,2009-02-26 11:44:33=18,2009-02-26 11:34:32=44,2009-02-26 11:24:34=46,2009-02-26 11:14:36=55,2009-02-26 11:04:34=47,2009-02-26 10:54:34=66,2009-02-26 10:44:34=46,2009-02-26 10:34:32=33,2009-02-26 10:24:32=46,2009-02-26 10:14:35=18,2009-02-26 10:04:33=44,2009-02-26 09:54:33=44,2009-02-26 09:44:32=36,"
//	        max= "85"
//	        sv_drawimage= "1"
//	        sv_drawtable= "1"
//	        when_max= "2009-02-26 12:44:34"
//	        MonitorName= "127.0.0.1:Cpu"
//	        ReturnName= "CPU�ۺ�ʹ����(%)"
//	        sv_drawmeasure= "1"
//	        sv_primary= "1"
//	        sv_baseline= "1"
//	        latest= "46"
//	        average= "44.575"
//	     ---- (dstr)1.356.2 (41 key) ----
//	        2009-02-26 14:34:35= "warning CPU�ۺ�ʹ����(%)=75,CPU��ϸʹ����(%)=CPU0:75."
//	        2009-02-26 12:44:34= "warning CPU�ۺ�ʹ����(%)=85,CPU��ϸʹ����(%)=CPU0:85."
//	        2009-02-26 12:54:31= "ok      CPU�ۺ�ʹ����(%)=66,CPU��ϸʹ����(%)=CPU0:66."
//	        2009-02-26 13:24:35= "ok      CPU�ۺ�ʹ����(%)=46,CPU��ϸʹ����(%)=CPU0:46."
//	        2009-02-26 10:44:34= "ok      CPU�ۺ�ʹ����(%)=46,CPU��ϸʹ����(%)=CPU0:46."
//	        2009-02-26 10:24:32= "ok      CPU�ۺ�ʹ����(%)=46,CPU��ϸʹ����(%)=CPU0:46."
//	        MonitorName= "127.0.0.1:Cpu"
//	        2009-02-26 14:44:31= "ok      CPU�ۺ�ʹ����(%)=46,CPU��ϸʹ����(%)=CPU0:46."
//	        2009-02-26 13:04:33= "ok      CPU�ۺ�ʹ����(%)=58,CPU��ϸʹ����(%)=CPU0:58."
//	        2009-02-26 11:14:36= "ok      CPU�ۺ�ʹ����(%)=55,CPU��ϸʹ����(%)=CPU0:55."
//	        2009-02-26 15:24:33= "ok      CPU�ۺ�ʹ����(%)=46,CPU��ϸʹ����(%)=CPU0:46."
//	        2009-02-26 10:14:35= "ok      CPU�ۺ�ʹ����(%)=18,CPU��ϸʹ����(%)=CPU0:18."
//	        2009-02-26 15:04:33= "ok      CPU�ۺ�ʹ����(%)=46,CPU��ϸʹ����(%)=CPU0:46."
//	        2009-02-26 11:24:34= "ok      CPU�ۺ�ʹ����(%)=46,CPU��ϸʹ����(%)=CPU0:46."
//	        2009-02-26 10:04:33= "ok      CPU�ۺ�ʹ����(%)=44,CPU��ϸʹ����(%)=CPU0:44."
//	        2009-02-26 15:54:32= "warning CPU�ۺ�ʹ����(%)=75,CPU��ϸʹ����(%)=CPU0:75."
//	        2009-02-26 14:54:32= "ok      CPU�ۺ�ʹ����(%)=35,CPU��ϸʹ����(%)=CPU0:35."
//	        2009-02-26 09:44:32= "ok      CPU�ۺ�ʹ����(%)=36,CPU��ϸʹ����(%)=CPU0:36."
//	        2009-02-26 14:24:33= "ok      CPU�ۺ�ʹ����(%)=47,CPU��ϸʹ����(%)=CPU0:47."
//	        2009-02-26 14:14:33= "ok      CPU�ۺ�ʹ����(%)=46,CPU��ϸʹ����(%)=CPU0:46."
//	        2009-02-26 13:44:33= "ok      CPU�ۺ�ʹ����(%)=57,CPU��ϸʹ����(%)=CPU0:57."
//	        2009-02-26 11:04:34= "ok      CPU�ۺ�ʹ����(%)=47,CPU��ϸʹ����(%)=CPU0:47."
//	        2009-02-26 10:54:34= "ok      CPU�ۺ�ʹ����(%)=66,CPU��ϸʹ����(%)=CPU0:66."
//	        2009-02-26 16:14:34= "ok      CPU�ۺ�ʹ����(%)=46,CPU��ϸʹ����(%)=CPU0:46."
//	        2009-02-26 13:54:33= "ok      CPU�ۺ�ʹ����(%)=35,CPU��ϸʹ����(%)=CPU0:35."
//	        2009-02-26 14:04:31= "ok      CPU�ۺ�ʹ����(%)=57,CPU��ϸʹ����(%)=CPU0:57."
//	        2009-02-26 13:34:32= "ok      CPU�ۺ�ʹ����(%)=46,CPU��ϸʹ����(%)=CPU0:46."
//	        2009-02-26 15:34:35= "ok      CPU�ۺ�ʹ����(%)=57,CPU��ϸʹ����(%)=CPU0:57."
//	        2009-02-26 15:14:33= "ok      CPU�ۺ�ʹ����(%)=19,CPU��ϸʹ����(%)=CPU0:19."
//	        2009-02-26 16:04:33= "ok      CPU�ۺ�ʹ����(%)=30,CPU��ϸʹ����(%)=CPU0:30."
//	        2009-02-26 12:34:33= "ok      CPU�ۺ�ʹ����(%)=54,CPU��ϸʹ����(%)=CPU0:54."
//	        2009-02-26 10:34:32= "ok      CPU�ۺ�ʹ����(%)=33,CPU��ϸʹ����(%)=CPU0:33."
//	        2009-02-26 12:04:36= "ok      CPU�ۺ�ʹ����(%)=19,CPU��ϸʹ����(%)=CPU0:19."
//	        2009-02-26 13:14:34= "ok      CPU�ۺ�ʹ����(%)=8,CPU��ϸʹ����(%)=CPU0:8."
//	        2009-02-26 12:24:33= "ok      CPU�ۺ�ʹ����(%)=44,CPU��ϸʹ����(%)=CPU0:44."
//	        2009-02-26 11:44:33= "ok      CPU�ۺ�ʹ����(%)=18,CPU��ϸʹ����(%)=CPU0:18."
//	        2009-02-26 11:54:32= "ok      CPU�ۺ�ʹ����(%)=32,CPU��ϸʹ����(%)=CPU0:32."
//	        2009-02-26 12:14:34= "ok      CPU�ۺ�ʹ����(%)=19,CPU��ϸʹ����(%)=CPU0:19."
//	        2009-02-26 11:34:32= "ok      CPU�ۺ�ʹ����(%)=44,CPU��ϸʹ����(%)=CPU0:44."
//	        2009-02-26 09:54:33= "ok      CPU�ۺ�ʹ����(%)=44,CPU��ϸʹ����(%)=CPU0:44."
//	        2009-02-26 15:44:31= "ok      CPU�ۺ�ʹ����(%)=46,CPU��ϸʹ����(%)=CPU0:46."
//	     ---- 1.356.2 (10 key) ----
//	        latestDstr= "CPU�ۺ�ʹ����(%)=46, CPU��ϸʹ����(%)=CPU0:46, "
//	        okPercent= "92.50"
//	        warnPercent= "7.50"
//	        (Return_1)1.356.2= "ReturnValue"
//	        latestStatus= "ok"
//	        latestCreateTime= "2009-02-26 16:14:37"
//	        MonitorName= "127.0.0.1:Cpu"
//	        (Return_0)1.356.2= "ReturnValue"
//	        errorPercent= "0.00"
//	        errorCondition= "[CPU�ۺ�ʹ����(%) >= 98]"
//	   -- Display UtilMapInMap end (4 node) --
//

	public float getDisablePercentOfSimpleReport()
	{
		return m_disablePercent;
	}
	
	public Map<Date, DstrItem> getDstr()
	{
		Map<Date, DstrItem> a= new LinkedHashMap<Date, DstrItem>();
		Map<Date, DstrItem> tempa= new HashMap<Date, DstrItem>();
		if(m_fmap==null)
			return a;
		m_disablePercent= 0;
		String mkey = "(dstr)" + m_node.getSvId();
		if(m_fmap.containsKey(mkey))
		{
			Map<String, String> rdata= m_fmap.get(mkey);
			if(rdata!=null)
			{
				int rsize= rdata.size();
				for(String key:rdata.keySet())
				{
					try
					{
						Date td = Toolkit.getToolkit().parseDate(key);
						String value= rdata.get(key);
						
						int index= value.indexOf(" ");
						String v1= value.substring(0, index).trim();
						String v2= value.substring(index+1).trim();
						if(v1.equals("disable"))
							++m_disablePercent;
						
						tempa.put(td, new DstrItem(v1,v2));
					} catch (ParseException e1)
					{
						--rsize;
					}
				}
				m_disablePercent= (m_disablePercent/rsize) * 100;
			}
		}
		if(tempa.isEmpty())
			return a;
		ArrayList<Date> list = new ArrayList<Date>(tempa.keySet());
		Collections.sort(list, new Comparator<Date>()
		{
			public int compare(Date o1, Date o2)
			{
				return o1.compareTo(o2);
			}
		});
		int size = list.size();
		for (int i = 0; i < size; ++i)
		{
			Date d= list.get(i);
			DstrItem item= tempa.get(d); 
			a.put(d, item);
		}
		return a;
	}
	
	public Date getOldestValueTime()
	{
		for(int i=0; i<=3; ++i)
		{
			Map<Date, String> detail= getReturnValueDetail(i);
			if(!detail.isEmpty())
			{
				Iterator it= detail.keySet().iterator();
				if(it.hasNext())
				{
					return (Date)it.next();
				}
			}
		}
		return null;
	}
	
	public Map<Date, String> getReturnValueDetail(int index)
	{
		String detail= getReturnValue("detail", index);
		Map<Date, String> a= new LinkedHashMap<Date, String>();	
		Map<Date, String> tempa= new HashMap<Date, String>();
		if(detail==null || detail.isEmpty())
			return a;
		
		String [] s= detail.split(",");
		int size= s.length;
		for (int i = 0; i < size; ++i)
		{
			try
			{
				String value= s[i];
				int ti= value.indexOf("=");
				String v1= value.substring(0, ti).trim();
				String v2= value.substring(ti+1).trim();
				
				Date td = Toolkit.getToolkit().parseDate(v1);				
				tempa.put(td, v2);
			} catch (ParseException e1)
			{
			}
		}
		if(tempa.isEmpty())
			return a;
		ArrayList<Date> list = new ArrayList<Date>(tempa.keySet());
		Collections.sort(list, new Comparator<Date>()
		{
			public int compare(Date o1, Date o2)
			{
				return o1.compareTo(o2);
			}
		});
		size = list.size();
		for (int i = 0; i < size; ++i)
		{
			Date d= list.get(i);
			String item= tempa.get(d); 
			a.put(d, item);
		}
		return a;
	}
	
	public Map<String ,String> getReportAttribute(int index){
		Map<String ,String> rtnMap = new HashMap<String,String>();
		Map<Date, String> detialmap = getReturnValueDetail(index);
		double min = 999999999;
		double max = 0;
		double average = 0;
		Collection<String> values = detialmap.values();
		for(String value : values)
		{
			double tmpValue = 0;
			if (value.trim().startsWith("(status)"))
			{
				tmpValue = 0;
			} else
			{
				if (value.isEmpty())
				{
					tmpValue = 0;
				} else
				{
					tmpValue = Double.parseDouble(value);
				}
			}
			max = max>tmpValue?max:tmpValue;
			min = min<tmpValue?min:tmpValue;
			average += tmpValue;
		}
		rtnMap.put("min", min+"");
		rtnMap.put("max", max+"");
		rtnMap.put("average", average/values.size()+"");
		return rtnMap;
	}
	
	public String getReturnValue(String key, int index)
	{
		if(m_fmap==null)
			return null;
		String mkey = "(Return_" + new Integer(index).toString() + ")" + m_node.getSvId();
		String str = null;
		if(m_fmap.containsKey(mkey))
		{
			Map<String, String> rdata= m_fmap.get(mkey);
			if(rdata!=null && rdata.containsKey(key))
				str= rdata.get(key);
		}
		return str;
	}
	
	public int getReturnSize()
	{
		if(m_fmap==null)
			return 0;
		int size= 0;
		if(m_fmap.containsKey(m_node.getSvId()))
		{
			Map<String, String> rdata= m_fmap.get(m_node.getSvId());
			if(rdata!=null)
			{
				for(String key:rdata.keySet())
				{
					String value= rdata.get(key);
					if(value!=null && value.equals("ReturnValue"))
						++size;
				}
			}		
		}
		return size;
	}
	
	public String getPropertyValue(String key)
	{
		if(m_fmap==null)
			return null;
		String str=null;
		if(m_fmap.containsKey(m_node.getSvId()))
		{
			Map<String, String> rdata= m_fmap.get(m_node.getSvId());
			if(rdata!=null && rdata.containsKey(key))
				str= rdata.get(key);
		}
		return str;
	}

	public static class DstrItem
	{
		public String status;
		public String value;
		public DstrItem(String in_status, String in_value)
		{
			status= in_status;
			value= in_value;
		}
	}
	
}
