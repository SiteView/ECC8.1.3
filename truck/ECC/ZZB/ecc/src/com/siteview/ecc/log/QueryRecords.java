/**
 * 
 */
package com.siteview.ecc.log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.Integer;

import java.util.Calendar;

import org.zkoss.zul.Messagebox;

import com.siteview.base.manage.ManageSvapi;
import com.siteview.base.manage.RetMapInMap;
import com.siteview.base.manage.RetMapInVector;
import com.siteview.ecc.alert.dao.bean.AlertLogItem;
import com.siteview.ecc.alert.dao.type.AlertStatus;
import com.siteview.ecc.alert.dao.type.AlertType;
import com.siteview.jsvapi.Jsvapi;

//public static SimpleDateFormat STRING_TO_DATE = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
/**
 * @author yuandong通过querycondition查询
 *         Helper.XfireCreateKeyValue("begin_year",beginTime.Year.ToString()),
 *         Helper.XfireCreateKeyValue("begin_month",beginTime.Month.ToString()),
 *         Helper.XfireCreateKeyValue("begin_day",beginTime.Day.ToString()),
 *         Helper.XfireCreateKeyValue("begin_hour",beginTime.Hour.ToString()),
 *         Helper
 *         .XfireCreateKeyValue("begin_minute",beginTime.Minute.ToString()),
 *         Helper
 *         .XfireCreateKeyValue("begin_second",beginTime.Second.ToString()),
 *         Helper.XfireCreateKeyValue("end_year",endTime.Year.ToString()),
 *         Helper.XfireCreateKeyValue("end_month",endTime.Month.ToString()),
 *         Helper.XfireCreateKeyValue("end_day",endTime.Day.ToString()),
 *         Helper.XfireCreateKeyValue("end_hour",endTime.Hour.ToString()),
 *         Helper.XfireCreateKeyValue("end_minute",endTime.Minute.ToString()),
 *         Helper.XfireCreateKeyValue("end_second",endTime.Second.ToString()) *
 *         //以下两函数对应 svapi 函数是：QueryRecords, 查询数据库监测记录 ; 记录个数极限为50000 dowhat=
 *         QueryRecordsByTime , id= XXX , begin_year= XXX, begin_month= XXX,
 *         begin_day= XXX, begin_hour= XXX, begin_minute= XXX, begin_second=
 *         XXX, end_year= XXX, end_month= XXX, end_day= XXX, end_hour= XXX,
 *         end_minute= XXX, end_second= XXX, dowhat= QueryRecordsByCount , id=
 *         XXX , count= XXX;
 */
public class QueryRecords {
	/*
	 * private String id; private String count; private String beginYear;
	 * private String beginMonth; private String beginDay; private String
	 * beginHour; private String beginMinute;
	 * 
	 * private String beginSecond; private String endYear; private String
	 * endMonth; private String endDay; private String endHour; private String
	 * endMinute; private String endSecond;
	 */

	private List<Map<String, String>> v_fmap = new ArrayList<Map<String, String>>();

	// 从dowhat和查询条件得到结果

	public QueryRecords(String dowhat, QueryCondition qc) throws Exception {
		// id=id1;out
		// count=count1;
		//Calendar cal = Calendar.getInstance();
		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", dowhat);
		ndata.put("id", qc.getId());
		if (qc.isLimitType()) {
			if (qc.getCount() != null)
				ndata.put("count", qc.getCount());
			RetMapInVector ret = ManageSvapi.GetForestData(ndata);
			if (!ret.getRetbool())
				throw new Exception("Failed to load :" + ret.getEstr());

			v_fmap = ret.getVmap();
		} else {
			if (qc.getBeginYear() != null)
				ndata.put("begin_year", qc.getBeginYear());
			if (qc.getBeginMonth() != null)
				ndata.put("begin_month", qc.getBeginMonth());
			if (qc.getBeginDay() != null)
				ndata.put("begin_day", qc.getBeginDay());
			if (qc.getBeginHour() != null)
				ndata.put("begin_hour", qc.getBeginHour());
			if (qc.getBeginMinute() != null)
				ndata.put("begin_minute", qc.getBeginMinute());
			if (qc.getBeginSecond() != null)
				ndata.put("begin_second", qc.getBeginSecond());
			if (qc.getEndYear() != null)
				ndata.put("end_year", qc.getEndYear());
			if (qc.getEndMonth() != null)
				ndata.put("end_month", qc.getEndMonth());
			if (qc.getEndDay() != null)
				ndata.put("end_day", qc.getEndDay());
			if (qc.getEndHour() != null)
				ndata.put("end_hour", qc.getEndHour());
			if (qc.getEndMinute() != null)
				ndata.put("end_minute", qc.getEndMinute());
			if (qc.getEndSecond() != null)
				ndata.put("end_second", qc.getEndSecond());
			RetMapInVector ret = ManageSvapi.GetForestData(ndata);
			if (!ret.getRetbool())
				throw new Exception("Failed to load :" + ret.getEstr());

			v_fmap = ret.getVmap();
		//	Jsvapi.DisplayUtilMapInArrayList(v_fmap);
			//筛选
			int i=0;
			while (i < v_fmap.size()) {
				if (v_fmap.get(i).get("_UserID").equals(qc.getUserId()) ||qc.getUserId().equals("null"))
					if (v_fmap.get(i).get("_OperateType").equals( qc.getOperateType())||qc.getOperateType().equals("null")) 
						if (v_fmap.get(i).get("_OperateObjName").equals(qc.getOperateObjName())||qc.getOperateObjName().equals("null"))
							{
							i++;
							continue;}
				v_fmap.remove(i);
				
			}
			if(i==0)
				Messagebox.show("返回了0条记录", "提示", Messagebox.OK, Messagebox.INFORMATION);

		}
	}

	public List<Map<String, String>> getV_map() {
		return v_fmap;

	}

}
