package com.siteview.ecc.alert.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.siteview.base.manage.ManageSvapi;
import com.siteview.base.manage.RetMapInVector;
import com.siteview.ecc.alert.dao.bean.AccessControl;
import com.siteview.ecc.alert.dao.bean.AlertLogItem;
import com.siteview.ecc.alert.dao.bean.AlertLogQueryCondition;
import com.siteview.ecc.alert.dao.bean.ListBean;
import com.siteview.ecc.alert.dao.type.AlertStatus;
import com.siteview.ecc.alert.dao.type.AlertType;
import com.siteview.ecc.alert.dao.type.ErrorInfo;
import com.siteview.ecc.alert.util.BaseTools;

public class AlertLogDaoImpl extends AbstractDao  implements IAlertLogDao {
	//public static SimpleDateFormat STRING_TO_DATE = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");

    public AlertLogDaoImpl()
    {
    }
 
	@Override
	public ListBean queryAlertLog(AccessControl accessInformation,
			AlertLogQueryCondition queryCondition) throws Exception {
        //dowhat= QueryAlertLog,	
        //begin_year= XXX,  
        //begin_month= XXX,  
        //begin_day= XXX,  
        //begin_hour= XXX,  
        //begin_minute= XXX,  
        //begin_second= XXX,  
        //end_year= XXX,  
        //end_month= XXX,  
        //end_day= XXX,  
        //end_hour= XXX,  
        //end_minute= XXX,  
        //end_second= XXX,
        //alertName= XXX,   
        //alertReceive= XXX, 
        //alertType= XXX; 
        ////alertName、alertReceive、alertType 是 and 关系，如果为空则为全部。

        HashMap<String, String> paras = new HashMap<String, String>();

        paras.put("dowhat","QueryAlertLog");
        
        if (queryCondition.getStartTime() != null && queryCondition.getEndTime() != null){
        	if (queryCondition.getStartTime().after(queryCondition.getEndTime())){
        		throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.AlertDateTimeOrderError));
        	}
        }
		Calendar   cal   =   Calendar.getInstance();   
    	if (queryCondition.getStartTime() != null){
    		cal.setTime(queryCondition.getStartTime());

            paras.put("begin_year",BaseTools.getString(cal.get(Calendar.YEAR),4));
            paras.put("begin_month",BaseTools.getString((cal.get(Calendar.MONTH) + 1),2));
            paras.put("begin_day",BaseTools.getString(cal.get(Calendar.DAY_OF_MONTH),2));
            paras.put("begin_hour",BaseTools.getString(cal.get(Calendar.HOUR_OF_DAY),2));
            paras.put("begin_minute",BaseTools.getString(cal.get(Calendar.MINUTE),2));
            paras.put("begin_second",BaseTools.getString(cal.get(Calendar.SECOND),2));
    	}
    	if (queryCondition.getEndTime() != null){
    		cal.setTime(queryCondition.getEndTime());
            paras.put("end_year",BaseTools.getString(cal.get(Calendar.YEAR),4));
            paras.put("end_month",BaseTools.getString((cal.get(Calendar.MONTH) + 1),2));
            paras.put("end_day",BaseTools.getString(cal.get(Calendar.DAY_OF_MONTH),2));
            paras.put("end_hour",BaseTools.getString(cal.get(Calendar.HOUR_OF_DAY),2));
            paras.put("end_minute",BaseTools.getString(cal.get(Calendar.MINUTE),2));
            paras.put("end_second",BaseTools.getString(cal.get(Calendar.SECOND),2));
    	}
    	
    	
        if(queryCondition.getLimitName())
        {
            paras.put("alertName", queryCondition.getAlertName());
        }

        if(queryCondition.getLimitReceiver())
        {
            paras.put("alertReceive", queryCondition.getAlertReceiver());
        }

        if(queryCondition.getLimitType())
        {
            paras.put("alertType", queryCondition.getAlertType().getStringVaule());
        }
        if (queryCondition.getLimitIndex())
        {
            paras.put("alertIndex", queryCondition.getAlertIndex());
        }
        
        RetMapInVector retResult = ManageSvapi.GetForestData(paras);
        ListBean retListBean = new ListBean();
        if (!retResult.getRetbool())
        {
            throw new Exception(retResult.getEstr());
        	//retListBean.setMessage(retResult.getEstr());
        	//retListBean.setSuccess(false);
        }
        /*
        List<AlertLogItem> responseData = new ArrayList<AlertLogItem>();
        for (Map<String,String> map : retResult.getVmap())
        {
            AlertLogItem item = new AlertLogItem();
            item.setAlertIndex(map.get("_AlertIndex"));
            item.setAlertName(map.get("_AlertRuleName"));
            item.setEntityName(map.get("_DeviceName"));
            item.setMonitorName(map.get("_MonitorName"));
            item.setAlertReceiver(map.get("_AlertReceive"));

            String status = (String) map.get("_AlertStatus");
            item.setAlertStatus(AlertStatus.getType(status));
            
            try{
            	
                item.setAlertTime(STRING_TO_DATE.parse((String)map.get("_AlertTime")));
            }catch(Exception e){}
            
            
            String stye = (String) map.get("_AlertType");
            item.setAlertType(AlertType.getTypeByValue(stye));
            responseData.add(item);

        }
        retListBean.setList(responseData);
        */
        retListBean.setList(retResult.getVmap());
        return retListBean;
    }

	@Override
	public ListBean queryAlertLog(AccessControl accessInformation,
			AlertLogQueryCondition queryCondition, int noFrom, int onTo)
			throws Exception {
        //dowhat= QueryAlertLog,	
        //begin_year= XXX,  
        //begin_month= XXX,  
        //begin_day= XXX,  
        //begin_hour= XXX,  
        //begin_minute= XXX,  
        //begin_second= XXX,  
        //end_year= XXX,  
        //end_month= XXX,  
        //end_day= XXX,  
        //end_hour= XXX,  
        //end_minute= XXX,  
        //end_second= XXX,
        //alertName= XXX,   
        //alertReceive= XXX, 
        //alertType= XXX; 
        ////alertName、alertReceive、alertType 是 and 关系，如果为空则为全部。

        HashMap<String, String> paras = new HashMap<String, String>();

        paras.put("dowhat","QueryMassAlertLog");
        
        if (queryCondition.getStartTime() != null && queryCondition.getEndTime() != null){
        	if (queryCondition.getStartTime().after(queryCondition.getEndTime())){
        		throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.AlertDateTimeOrderError));
        	}
        }
		Calendar   cal   =   Calendar.getInstance();   
    	if (queryCondition.getStartTime() != null){
    		cal.setTime(queryCondition.getStartTime());

            paras.put("begin_year",BaseTools.getString(cal.get(Calendar.YEAR),4));
            paras.put("begin_month",BaseTools.getString((cal.get(Calendar.MONTH) + 1),2));
            paras.put("begin_day",BaseTools.getString(cal.get(Calendar.DAY_OF_MONTH),2));
            paras.put("begin_hour",BaseTools.getString(cal.get(Calendar.HOUR_OF_DAY),2));
            paras.put("begin_minute",BaseTools.getString(cal.get(Calendar.MINUTE),2));
            paras.put("begin_second",BaseTools.getString(cal.get(Calendar.SECOND),2));
    	}
    	if (queryCondition.getEndTime() != null){
    		cal.setTime(queryCondition.getEndTime());
            paras.put("end_year",BaseTools.getString(cal.get(Calendar.YEAR),4));
            paras.put("end_month",BaseTools.getString((cal.get(Calendar.MONTH) + 1),2));
            paras.put("end_day",BaseTools.getString(cal.get(Calendar.DAY_OF_MONTH),2));
            paras.put("end_hour",BaseTools.getString(cal.get(Calendar.HOUR_OF_DAY),2));
            paras.put("end_minute",BaseTools.getString(cal.get(Calendar.MINUTE),2));
            paras.put("end_second",BaseTools.getString(cal.get(Calendar.SECOND),2));
    	}
    	
    	
        if(queryCondition.getLimitName())
        {
            paras.put("alertName", queryCondition.getAlertName());
        }

        if(queryCondition.getLimitReceiver())
        {
            paras.put("alertReceive", queryCondition.getAlertReceiver());
        }

        if(queryCondition.getLimitType())
        {
            paras.put("alertType", queryCondition.getAlertType().getStringVaule());
        }
        if (queryCondition.getLimitIndex())
        {
            paras.put("alertIndex", queryCondition.getAlertIndex());
        }
        paras.put("alertBeginIndex", "" + noFrom);
        paras.put("alertEndIndex", "" + onTo);
        
        RetMapInVector retResult = ManageSvapi.GetForestData(paras);
        ListBean retListBean = new ListBean();
        if (!retResult.getRetbool())
        {
            throw new Exception(retResult.getEstr());
        }
        List<Map<String, String>> maplist = retResult.getVmap();
        for (Map<String, String> map : maplist){
        	String matchNumber = map.get("matchNumber");
        	if (matchNumber!=null){
        		try{
            		retListBean.setTotalNumber(new Integer(matchNumber));
        		}catch(Exception e){
        		}
        		maplist.remove(map);
        		break;
        	}
        }
        retListBean.setList(maplist);
        return retListBean;
	}


}
