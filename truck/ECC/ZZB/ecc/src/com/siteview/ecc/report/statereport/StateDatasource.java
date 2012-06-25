package com.siteview.ecc.report.statereport;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.apache.log4j.Logger;

import com.siteview.ecc.report.beans.StateBean;

public class StateDatasource implements JRDataSource {
	private static final Logger logger = Logger.getLogger(StateDatasource.class);
	private StateBean sbean;
	private String previous = "";
	public StateDatasource(StateBean sbean){
		this.sbean = sbean;
	}
	@Override
	public Object getFieldValue(JRField arg0) throws JRException {
		String name = arg0.getName();
		previous = name;
		if("title".equals(name)){
			return sbean.getMonitorName()+"的状态统计报告";
		}
		if(name.equals("okpercent")){
			String state = sbean.getPercentOk();
			return state;
		}else if(name.equals("warnpercent")){
			String state = sbean.getPercentWarn();
			return state;
		}else if(name.equals("errorpercent")){
			String state = sbean.getPercentError();
			return state;
		}else if(name.equals("disablepercent")){
			String state = sbean.getPercentDisable();
			return state;
		}else if(name.equals("badpercent")){
			String state = sbean.getPercentBad();
			sbean = null;
			return state;
		}
		return null;
	}

	@Override
	public boolean next() throws JRException {
		logger.info("***********************"+previous);
		if("title".equals(previous)) return false;
		if(sbean!=null) return true;
		return false;
	}

}
