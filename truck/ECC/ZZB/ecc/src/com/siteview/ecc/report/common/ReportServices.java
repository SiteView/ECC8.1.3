package com.siteview.ecc.report.common;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import com.siteview.base.data.Report;
import com.siteview.base.data.ReportDate;
import com.siteview.base.data.ReportDateError;
import com.siteview.base.manage.View;
import com.siteview.base.tree.INode;
import com.siteview.ecc.report.beans.TendencyCheckDataBean;
import com.siteview.ecc.simplereport.SimpleReport;
import com.siteview.ecc.util.Toolkit;

public class ReportServices {
	public static final String REPORTTYPE_DAYREPORT = "daymodel";// 日报
	public static final String REPORTTYPE_MONTHREPORT = "monthmodel";// 月报
	public static final String REPORTTYPE_WEEKREPORT = "weekmodel";// 周报
	
	public static List<TendencyCheckDataBean> getRuntimeData(Report report)
	{
		List<TendencyCheckDataBean> beans = new ArrayList<TendencyCheckDataBean>();
		for(int i=0;i<report.getReturnSize();i++)
		{
			TendencyCheckDataBean bean = new TendencyCheckDataBean();
			bean.setId(i);
			bean.setAverage(report.getReturnValue("average", i));
			bean.setLatest(report.getReturnValue("latest", i));
			bean.setMax(report.getReturnValue("max", i));
			bean.setMax_when(report.getReturnValue("when_max", i));
			bean.setMin(report.getReturnValue("min", i));
			bean.setName(report.getReturnValue("MonitorName", i));
			bean.setType(report.getReturnValue("ReturnName", i));
			bean.setColor( (i % 2 == 0) );
			if(ChartUtil.isShowReport(report.getM_node(),i)){
				beans.add(bean);
			}
		}
		return beans;
	}
	
	public static List<TendencyCheckDataBean> getRuntimeData(Report report1,Report report2){
		List<TendencyCheckDataBean> beans = new ArrayList<TendencyCheckDataBean>();
		beans.addAll(getRuntimeData(report1));
		beans.addAll(getRuntimeData(report2));
		return beans;
	}
	
    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    
	public static List<TendencyCheckDataBean> getRuntimeData(List<ReportDate> rdList)
	{
		List<TendencyCheckDataBean> rdlist = new ArrayList<TendencyCheckDataBean>();
		View v = ChartUtil.getView();
		double sizeTotal = 0;//监测器总数
		double sizeError = 0;//有问题监测器
		for(ReportDate rd : rdList)
		{
//			for(String a:rd.getM_fmap().keySet()){
//				System.out.println("-------------------------:		"+a);
//				for(String b:rd.getM_fmap().get(a).keySet()){
//					System.out.println(a+"		"+b+"		"+rd.getM_fmap().get(a).get(b));
//				}
//			}
			
			
			for (String id : rd.getNodeidsArray())
			{
				Map<String, Map<String, String>> tempMap = rd.getM_fmap();
				if(tempMap.keySet().contains(("(dstr)"+id))){//example (dstr)1.9.7.1
					TendencyCheckDataBean bean = null;
					for(String a:tempMap.keySet()){//(dstr)1.31.2.1.6
						if(a.contains("(dstr)"+id)==false){
							continue;
						}
						ArrayList<TimeState> timeList = new ArrayList<TimeState>();
						for(String b:tempMap.get(a).keySet()){
							if(b.contains(":")){//将时间加入到 list中  2011-03-18 14:32:36
								String temp = tempMap.get(a).get(b);
								String state = "ok";//ok error warning null(4种)
								if(temp!=null){
									if(temp.contains("bad")){
										state = "ok";//不统计bad
									}else if(temp.contains("ok")){
										state = "ok";
									}else{
										state = "error";
									}
								}else{
									state = "error";
								}
								TimeState timeState = new TimeState();
								timeState.setTime(b);
								timeState.setState(state);

								timeList.add(timeState);								
							}
						}
						if(timeList.size()>0){//统计监测器
							sizeTotal++;
						}
						boolean isFirst = true;
						int flag1 =	0; //统计error次数
						int flag2 = 0;//用于统计每个监测器错误list size
						String lastState = "";
						for(int c=timeList.size();c>0;c--){
							TimeState timeState = timeList.get(c-1);
							String tempTimeStr= timeState.getTime();
							//说明 一次错误 error
							//error ok error 两次错误
							//error error error ... 一次错误
							String state = timeState.getState();
							
							if("error".equals(state)){
								System.out.println("@@flag:@@:"+flag1+"@@flag2:"+flag2);
								flag1 ++;//统计error次数
								if(isFirst){
									bean = new TendencyCheckDataBean();
									flag2++;//统计生成错误对象次数
									bean.setName(rd.getReturnValue(id, "MonitorName", 0));
									bean.setErrorValue(tempMap.get(a).get(tempTimeStr));
									bean.setStart_when(tempTimeStr.trim());
									bean.setEnd_when(tempTimeStr.trim());
									rdlist.add(bean);
									isFirst = false;
									
									lastState = "error";
								}else if("ok".equals(lastState)){
									bean = new TendencyCheckDataBean();
									flag2++;//统计生成错误对象次数
									bean.setName(rd.getReturnValue(id, "MonitorName", 0));
									bean.setErrorValue(tempMap.get(a).get(tempTimeStr));
									bean.setStart_when(tempTimeStr.trim());
									bean.setEnd_when(tempTimeStr.trim());
									rdlist.add(bean);
									
									lastState = "error";
								}else{
									if(c ==1&&rdlist.size()>0){//目的防止....... error erorr error|结束的情况
										TendencyCheckDataBean copyBean = rdlist.get(rdlist.size()-1);
										copyBean.setEnd_when(tempTimeStr);
										
										if(checkTime(copyBean)==false){//开始时间小于结束时间
											rdlist.remove(rdlist.size()-1);

										}
									}
									
									lastState = "error";
								}
							}else{//state为ok
								if("error".equals(lastState)){
									//获取前一次对象 赋值,修改最后的结束时间
									if(rdlist.size()>0&&(isFirst ==false)){
										TendencyCheckDataBean copyBean = rdlist.get(rdlist.size()-1);
										copyBean.setEnd_when(tempTimeStr);
										if(checkTime(copyBean)==false){//开始时间小于结束时间
											rdlist.remove(rdlist.size()-1);

										}
									}
									lastState="ok";
								}else{
									lastState="ok";
								}
							}
							//整个统计
							if(c==1&&flag2>0 ){//循环的最后一次 并且存在错误数据
								int time = 0;
								//累加时间 精确统计
								if(rdlist.size()-flag2<0) flag2=rdlist.size();
								for(int d =flag2;d>0;d--){
									System.out.println("rdlist.size()-d" + (rdlist.size()-d) +"@@@@@"+"rdlist.size() >  "+ rdlist.size() +":D:"+d);
									TendencyCheckDataBean tempBean = rdlist.get(rdlist.size()-d);
									time = time+countTime(tempBean);
								}
								
								bean = new TendencyCheckDataBean();
								bean.setName("统计:"+id);
								//bean.setName("统计:");
								bean.setStart_when("错误:"+flag2+"次,时长:"+time+"分");
								bean.setEnd_when("错误:"+flag2+"次,时长:"+time+"分");
								bean.setErrorValue("错误:"+flag2+"次,时长:"+time+"分");
								rdlist.add(bean);
								sizeError ++;
							}
						}
					}
				}

			}
		}
		if(rdlist.size()<1){
			TendencyCheckDataBean bean = new TendencyCheckDataBean();
			bean.setName("无错误数据");
			bean.setErrorValue("无错误数据");
			bean.setStart_when("无错误数据");
			bean.setEnd_when("无错误数据");
			rdlist.add(bean);
		}else{

			double value = 0.0;
			if(sizeTotal>0){
				value = (sizeError/sizeTotal)*100;
				TendencyCheckDataBean bean = new TendencyCheckDataBean();
				String temp = String.valueOf(value);
				if(temp.contains(".")){
					int endIndex = temp.indexOf(".");
					temp = temp.substring(0, endIndex);
				}
				bean.setName("故障统计");
				bean.setErrorValue(temp+"%");
				bean.setStart_when(temp+"%");
				bean.setEnd_when(temp+"%");
				rdlist.add(bean);
			}
		}
		
		return rdlist;
	}	
	public static int countTime(TendencyCheckDataBean bean)
	{
		int time = 0;
        try{
        	Date date1 = sdf.parse(bean.getEnd_when());
        	Date date2 = sdf.parse(bean.getStart_when());
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date2);
            long iTime = cal1.getTimeInMillis() - cal2.getTimeInMillis();
            time = Math.abs(Math.round(iTime / 1000 / 60));
        }catch(Exception e){
        	e.printStackTrace();
        }
		return time;
	}
	public static boolean checkTime(TendencyCheckDataBean bean){
        try{
        	Date date1 = sdf.parse(bean.getEnd_when());
        	Date date2 = sdf.parse(bean.getStart_when());
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date2);
            if(cal1.before(cal2)){
            	return false;
            }else{
            	return true;
            }
        }catch(Exception e){
        	e.printStackTrace();
        	return false;
        }
		
	}
	
	public static List<InputStream> buildstreamimage(Report report) throws ParseException, IOException
	{
		Map<Integer, Map<String, String>> listimage = getImagelist(report);
		if (listimage.size() == 0)
		{
			return null;
		}
		List<InputStream> list = new ArrayList<InputStream>();
		for (int key : listimage.keySet())
		{
			Map<Date, String> imgdata = report.getReturnValueDetail(key);
			Map<String, String> keyvalue = listimage.get(key);
			XYDataset data = SimpleReport.buildDataset(imgdata);
			BufferedImage temmap = null;
			String maxdate = keyvalue.get("maxdate");
			Date maxd = null;
			if (!maxdate.isEmpty())
			{
				maxd = Toolkit.getToolkit().parseDate(maxdate);
			}
			if (keyvalue.get("title").contains("%"))
			{
				temmap = SimpleReport.buildBufferImage(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("title"), data, 10,
						100, maxd, 0, true, 650, 200);
			} else {
				double maxvalue = Double.parseDouble(keyvalue.get("maxvalue"));
				double minvalue = Double.parseDouble(keyvalue.get("minvalue"));
				maxvalue = maxvalue * 1.1;
				if (maxvalue < 1) {
					maxvalue = 1;
				}
				if (keyvalue.get("minvalue").contains("-")) {
					temmap = SimpleReport.buildBufferImage(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("title"), data,
							20, maxvalue, maxd, minvalue, true, 650, 200);
				} else {
					temmap = SimpleReport.buildBufferImage(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("title"), data,
							20, maxvalue, maxd, 0, true, 650, 200);
				}
			}
			ByteArrayOutputStream bs = new ByteArrayOutputStream();
			ImageOutputStream imOut = ImageIO.createImageOutputStream(bs);
			ImageIO.write(temmap, "GIF", imOut);
			// scaledImage1为BufferedImage，jpg为图像的类型
			InputStream istream = new ByteArrayInputStream(bs.toByteArray());
			list.add(istream);
		}
		return list;
	}
	public static List<InputStream> buildImageMaps(List<ReportDate> rdList) throws Exception {
		List<InputStream> list = new ArrayList<InputStream>();
		for (ReportDate rd : rdList) {
			Map<Integer, Map<String, String>> imageList = getImagelist(rd);
			for (int key : imageList.keySet()) {
				Map<String, Map<Date, String>> imgdatas = xydatasetCreate(rd, key);
				XYDataset data = buildDataset(imgdatas);
				Map<String, String> keyvalue = imageList.get(key);
				BufferedImage temmap = null;
				String ds = keyvalue.get("latestCreateTime");
				Date date = new Date(System.currentTimeMillis());
				if(ds!=null && !ds.equals("")){
					date = Toolkit.getToolkit().parseDate(keyvalue.get(""));
				}
				if (keyvalue.get("title").contains("%")) {
					double maxvalue = Double.parseDouble(keyvalue
							.get("maxvalue"));
					double minvalue = Double.parseDouble(keyvalue
							.get("minvalue"));
					maxvalue = maxvalue * 1.1;
					minvalue = minvalue * 0.9;
					temmap = ChartUtil.getBufferedImage(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("title"), data,
							20, maxvalue, rd.getM_begin_date(),rd.getM_end_date(), minvalue, true, 650, 300,ChartUtil.REPORTTYPE_DAYREPORT);
				} else {
					double maxvalue = Double.parseDouble(keyvalue
							.get("maxvalue"));
					double minvalue = Double.parseDouble(keyvalue
							.get("minvalue"));
					maxvalue = maxvalue * 1.1;
					minvalue = minvalue * 0.9;
					if (keyvalue.get("title").contains("ms")) {
						temmap = ChartUtil.getBufferedImage(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("title"), data,
								20, maxvalue, rd.getM_begin_date(),rd.getM_end_date(), minvalue, true, 650, 300,ChartUtil.REPORTTYPE_DAYREPORT);
					} else {
						temmap = ChartUtil.getBufferedImage(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("title"), data,
								20, maxvalue, rd.getM_begin_date(),rd.getM_end_date(), minvalue, true, 650, 300,ChartUtil.REPORTTYPE_DAYREPORT);
					}
				}
				ByteArrayOutputStream bs = new ByteArrayOutputStream();
				ImageOutputStream imOut = ImageIO.createImageOutputStream(bs);
				ImageIO.write(temmap, "GIF", imOut);
				// scaledImage1为BufferedImage，jpg为图像的类型
				InputStream istream = new ByteArrayInputStream(bs.toByteArray());
				list.add(istream);
			}
		}
		return list;
	}	
	public static Map<Integer, Map<String, String>> getImagelist(Report simpleReport1)
	{
		Map<Integer, Map<String, String>> listimage1 = new LinkedHashMap<Integer, Map<String, String>>();
		for (int i = 0; i < simpleReport1.getReturnSize(); i++)
		{
			String svdrawimage = simpleReport1.getReturnValue("sv_drawimage", i);
			svdrawimage = svdrawimage.isEmpty() ? "0" : svdrawimage;
			String svprimary = simpleReport1.getReturnValue("sv_primary", i);
			svprimary = svprimary.isEmpty() ? "0" : svprimary;
			Map<String, String> keyvalue = new HashMap<String, String>();
			String max = simpleReport1.getReturnValue("max", i);
			String min = simpleReport1.getReturnValue("min", i);
			String average = simpleReport1.getReturnValue("average", i);
			String latestCreateTime = simpleReport1.getPropertyValue("latestCreateTime");
			keyvalue.put("subtitle", "最大值" + max + "平均值" + average + "最小值" + min);
			keyvalue.put("title", simpleReport1.getReturnValue("ReturnName", i));
			keyvalue.put("maxvalue", max);
			keyvalue.put("minvalue", min);
			keyvalue.put("maxdate", latestCreateTime);
			if(ChartUtil.isShowReport(simpleReport1.getM_node(), i)){
				listimage1.put(i, keyvalue);
			}
		}
		return listimage1;
	}
	
	public static Map<Integer, Map<String, String>> getImagelist(
			ReportDate rd) {
		Map<Integer, Map<String, String>> listimage1 = new LinkedHashMap<Integer, Map<String, String>>();
		String[] idList = rd.getNodeidsArray();
		int length = idList.length;
		View v = ChartUtil.getView();
		
		for (String id : idList) {
			INode node = v.getNode(id);
			// 得到该类型检测器中的指标
			for (int i = 0; i < rd.getReturnSize(id); i++) {
				double maxValue = 0;
				double minValue = 99999999;
				double average 	= 0;
				for(String tmpId : idList){
					String maxString = rd.getReturnValue(tmpId, "max", i);
					if (maxString != null && !maxString.equals("")) {
						try{
							maxValue = maxValue>Double.parseDouble((maxString))?maxValue:Double.parseDouble((maxString));
						}catch(Exception e){
							
						}
					}
					String minString = rd.getReturnValue(tmpId, "min", i);
					if (minString != null && !minString.equals("")) {
						try{
							minValue = minValue < Double.parseDouble(minString)?minValue:Double.parseDouble(minString);
						}catch(Exception e){
							
						}
					}
					String averageString = rd.getReturnValue(tmpId, "average", i);
					if (averageString != null && !averageString.equals("")) {
						try{
							average += Double.parseDouble(averageString);
						}catch(Exception e){
							
						}
					}
				}
				Map<String, String> keyvalue = new HashMap<String, String>();
				keyvalue.put("subtitle", "最大值" + maxValue + "平均值" + conv_number(average/idList.length)
						+ "最小值" + minValue);
				keyvalue.put("title", rd.getReturnValue(id, "ReturnName", i));
				keyvalue.put("maxvalue", Double.toString(maxValue));
				keyvalue.put("minvalue", Double.toString(minValue));
				keyvalue.put("average", getReportDateAvg(rd,i)+"");
				if(ChartUtil.isShowReport(node, i)){
					listimage1.put(i, keyvalue);
				}
			}
		}
		return listimage1;
	}
	 public static String conv_number(double number)
	   {
	     DecimalFormat df1=new DecimalFormat("0.0");
	     String str=df1.format(number);
	     return str;
	   } 
	private static double getReportDateAvg(ReportDate data,int index){
		String[] idList = data.getNodeidsArray();
		double average = 0d;
		for (String id : idList) {
			String averageString = data.getReturnValue(id, "average", index);
			if (averageString != null && !averageString.equals("")) {
				try{
					average += Double.parseDouble(averageString);
				}catch(Exception e){
					
				}
			}
		}
		if(idList.length<=0) return 0;
		return Math.floor(average/idList.length+0.499);
	}
	
	private static Map<String, Map<Date, String>> xydatasetCreate(
			ReportDate rd, int index) {
		Map<String, Map<Date, String>> imgdatas = new HashMap<String, Map<Date, String>>();
		for (String id : rd.getNodeidsArray()) {
			Map<Date, String> imgdata = rd.getReturnValueDetail(id,
					index);
			String name = rd.getReturnValue(id, "MonitorName", index);
			if (name == null)
				name = "";
			imgdatas.put(name, imgdata);
		}
		return imgdatas;
	}
	
	public static XYDataset buildDataset(Map<String, Map<Date, String>> map) {
		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
		int i = 1;
		for (String name : map.keySet()) {
			TimeSeries timeseries = new TimeSeries(name + i,
					org.jfree.data.time.Second.class);
			i++;
			Map<Date, String> imgdata = map.get(name);
			for (Date date1 : imgdata.keySet()) {
				int ss = date1.getSeconds();
				int mm = date1.getMinutes();
				int hh = date1.getHours();
				int d = date1.getDate();
				int m = date1.getMonth() + 1;
				int y = date1.getYear() + 1900;

				org.jfree.data.time.Second ttime = new Second(ss, mm, hh, d, m,
						y);
				String value = imgdata.get(date1);
				// equals("(status)bad")||value.trim().equals("(status)disable")
				// (status)null
				if (value.trim().startsWith("(status)")) {
					timeseries.add(ttime, null);
				} else {
					if (value.isEmpty()) {
						timeseries.add(ttime, null);
					} else {
						timeseries.add(ttime, Double.parseDouble(value));
					}

				}
			}
			timeseriescollection.addSeries(timeseries);
		}
		return timeseriescollection;
	}
	public static List<TendencyCheckDataBean> getRuntimeDatas(List<ReportDateError> rdList)
	{
		List<TendencyCheckDataBean> rdlist = new ArrayList<TendencyCheckDataBean>();
		View v = ChartUtil.getView();
		double sizeTotal = 0;//监测器总数
		double sizeError = 0;//有问题监测器
		for(ReportDateError rd : rdList)
		{
			for(String a:rd.getM_fmap().keySet()){
				System.out.println("-------------------------:		"+a);
				for(String b:rd.getM_fmap().get(a).keySet()){
					System.out.println(a+"		"+b+"		"+rd.getM_fmap().get(a).get(b));
				}
			}
			
			for (String id : rd.getNodeidsArray())
			{
				Map<String, Map<String, String>> tempMap = rd.getM_fmap();
				if(tempMap.keySet().contains(("(dstr)"+id))){//example (dstr)1.9.7.1
					TendencyCheckDataBean bean = null;
					for(String a:tempMap.keySet()){//(dstr)1.31.2.1.6
						if(a.contains("(dstr)"+id)==false){
							continue;
						}
						ArrayList<TimeState> timeList = new ArrayList<TimeState>();
						for(String b:tempMap.get(a).keySet()){
							if(b.contains(":")){//将时间加入到 list中  2011-03-18 14:32:36
								String temp = tempMap.get(a).get(b);
								String state = "ok";//ok error warning null(4种)
								if(temp!=null){
									if(temp.contains("bad")){
										state = "ok";//不统计bad
									}else if(temp.contains("ok")){
										state = "ok";
									}else{
										state = "error";
									}
								}else{
									state = "error";
								}
								TimeState timeState = new TimeState();
								timeState.setTime(b);
								timeState.setState(state);
								timeList.add(timeState);								
							}
						}
						if(timeList.size()>0){//统计监测器
							sizeTotal++;
						}
						boolean isFirst = true;
						int flag1 =	0; //统计error次数
						int flag2 = 0;//用于统计每个监测器错误list size
						String lastState = "";
						for(int c=timeList.size();c>0;c--){
							TimeState timeState = timeList.get(c-1);
							String tempTimeStr= timeState.getTime();
							//说明 一次错误 error
							//error ok error 两次错误
							//error error error ... 一次错误
							String state = timeState.getState();
							
							if("error".equals(state)){
								flag1 ++;//统计error次数
								if(isFirst){
									bean = new TendencyCheckDataBean();
									flag2++;//统计生成错误对象次数
									bean.setName(rd.getReturnValue(id, "MonitorName", 0));
									bean.setErrorValue(tempMap.get(a).get(tempTimeStr));
									bean.setStart_when(tempTimeStr.trim());
									bean.setEnd_when(tempTimeStr.trim());
									rdlist.add(bean);
									isFirst = false;
									
									lastState = "error";
								}else if("ok".equals(lastState)){
									bean = new TendencyCheckDataBean();
									flag2++;//统计生成错误对象次数
									bean.setName(rd.getReturnValue(id, "MonitorName", 0));
									bean.setErrorValue(tempMap.get(a).get(tempTimeStr));
									bean.setStart_when(tempTimeStr.trim());
									bean.setEnd_when(tempTimeStr.trim());
									rdlist.add(bean);
									
									lastState = "error";
								}else{
									if(c ==1&&rdlist.size()>0){//目的防止....... error erorr error|结束的情况
										TendencyCheckDataBean copyBean = rdlist.get(rdlist.size()-1);
										copyBean.setEnd_when(tempTimeStr);
									}
									
									lastState = "error";
								}
							}else{//state为ok
								if("error".equals(lastState)){
									//获取前一次对象 赋值,修改最后的结束时间
									if(rdlist.size()>0&&(isFirst ==false)){
										TendencyCheckDataBean copyBean = rdlist.get(rdlist.size()-1);
										copyBean.setEnd_when(tempTimeStr);
									}
									lastState="ok";
								}else{
									lastState="ok";
								}
							}
							//整个统计
							if(c==1&&flag2>0){//循环的最后一次 并且存在错误数据
								int time = 0;
								//累加时间 精确统计
								
								for(int d =flag2;d>0;d--){
									TendencyCheckDataBean tempBean = rdlist.get(rdlist.size()-d);
									time = time+countTime(tempBean);
								}
								
								bean = new TendencyCheckDataBean();
								bean.setName("统计:"+id);
								//bean.setName("统计:");
								bean.setStart_when("错误:"+flag2+"次,时长:"+time+"分");
								bean.setEnd_when("错误:"+flag2+"次,时长:"+time+"分");
								bean.setErrorValue("错误:"+flag2+"次,时长:"+time+"分");
								rdlist.add(bean);
								sizeError ++;
							}
						}
					}
				}

			}
		}
		if(rdlist.size()<1){
			TendencyCheckDataBean bean = new TendencyCheckDataBean();
			bean.setName("无错误数据");
			bean.setErrorValue("无错误数据");
			bean.setStart_when("无错误数据");
			bean.setEnd_when("无错误数据");
			rdlist.add(bean);
		}else{

			double value = 0.0;
			if(sizeTotal>0){
				value = (sizeError/sizeTotal)*100;
				TendencyCheckDataBean bean = new TendencyCheckDataBean();
				String temp = String.valueOf(value);
				if(temp.contains(".")){
					int endIndex = temp.indexOf(".");
					temp = temp.substring(0, endIndex);
				}
				bean.setName("故障统计");
				bean.setErrorValue(temp+"%");
				bean.setStart_when(temp+"%");
				bean.setEnd_when(temp+"%");
				rdlist.add(bean);
			}
		}
		
		return rdlist;
	}	
	
}
