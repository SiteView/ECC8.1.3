package com.siteview.ecc.alert.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.apache.log4j.Logger;

import com.siteview.ecc.alert.dao.bean.AccessControl;
import com.siteview.ecc.alert.dao.bean.AlertLogQueryCondition;
import com.siteview.ecc.alert.dao.bean.ListBean;
import com.siteview.svdb.dao.DBConnectionManager;

public class AlertLogDaoImplOfSQL implements IAlertLogDao {
	
	private static final Logger logger = Logger.getLogger(AlertLogDaoImplOfSQL.class);
	
	public final static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSZ");
	
	@Override
	public ListBean queryAlertLog(AccessControl AccessInformation,
			AlertLogQueryCondition QueryCondition) throws Exception {
		
		Connection connection = null;
		ListBean retListBean = new ListBean();
		try {
			connection = DBConnectionManager.getConnection();

			PreparedStatement statement = connection
					.prepareStatement("select createtime,identifykey from report where id = 'alertlogs'");
			ResultSet resultSet = statement.executeQuery();
			
			List<Map<String,String>> retList = new FastList<Map<String,String>>();
			while (resultSet.next()) {
				Map<String,String> value = getValue(connection ,resultSet.getString(1),resultSet.getLong(2));
				if (this.checkAccess(value,QueryCondition)) 
					retList.add(value);
			}
			retListBean.setList(retList);
			resultSet.close();
			statement.close();
			connection.commit();
		} catch (Exception e) {
			if (connection != null)
				connection.rollback();
			logger.error(e.getMessage());
			retListBean.setMessage(e.getMessage());
			retListBean.setSuccess(false);
		} finally {
			if (connection != null)
				connection.close();
		}
		return retListBean;
	}
	
	
/**
alertlogs	2010-01-14 13:49:05:468+0800	_AlertIndex		30383
alertlogs	2010-01-14 13:49:05:468+0800	_AlertReceive	yumin@dfkl.com
alertlogs	2010-01-14 13:49:05:468+0800	_AlertRuleName	告警
alertlogs	2010-01-14 13:49:05:468+0800	_AlertStatus	0
alertlogs	2010-01-14 13:49:05:468+0800	_AlertTime		2010-01-14 13:45:32
alertlogs	2010-01-14 13:49:05:468+0800	_AlertType		1
alertlogs	2010-01-14 13:49:05:468+0800	_DeviceName		192.168.3.118(Windows)
alertlogs	2010-01-14 13:49:05:468+0800	_MonitorName	Cpu
alertlogs	2010-01-14 13:49:05:468+0800	RecordState		OK
 */
	@Override
	public ListBean queryAlertLog(AccessControl AccessInformation,
			AlertLogQueryCondition queryCondition, int noFrom, int onTo)
			throws Exception {
		String sql = "select t1.time,t2.skey,t2.value from (select * from (select id id, createtime time ," +
				"ROW_NUMBER() Over(order by createtime) as rowNum from report where id = 'alertlogs' and createtime >= ? and createtime <= ?) t1 " +
				") t1, reportdata t2 where t1.id = t2.reportid and t1.time = t2.reportcreatetime";
		
//		System.out.println("select t1.time,t2.skey,t2.value from (select * from (select id id, createtime time ," +
//				"ROW_NUMBER() Over(order by createtime) as rowNum from report where id = 'alertlogs' and createtime >= '"+SDF.format(queryCondition.getStartTime())+"' and createtime <= '"+SDF.format(queryCondition.getEndTime())+"') t1 " +
//				"where t1.rowNum between "+noFrom+ " and "+onTo+") t1, reportdata t2 where t1.id = t2.reportid and t1.time = t2.reportcreatetime");
		Connection connection = null;
		try {
			connection = DBConnectionManager.getConnection();

			PreparedStatement statement = connection
					.prepareStatement(sql);
			statement.setString(1, SDF.format(queryCondition.getStartTime()));
			statement.setString(2, SDF.format(queryCondition.getEndTime()));
//			statement.setInt(3, noFrom);
//			statement.setInt(4, onTo);
			ResultSet resultSet = statement.executeQuery();
			ListBean retListBean = new ListBean();
			
			String createtime = null;
			
			List<Map<String,String>> retList = new FastList<Map<String,String>>();
			Map<String,String> value = new FastMap<String,String>(); 
			boolean flag = false;
			while (resultSet.next()) {
				flag = true;
				if (createtime == null) createtime = resultSet.getString(1);
				if (!createtime.equals(resultSet.getString(1))) {
					if (checkAccess(value,queryCondition))  
						retList.add(value);
					value = new FastMap<String,String>();
					createtime = resultSet.getString(1);
				}
				value.put("_AlertTime", resultSet.getString(1));
				
				value.put(resultSet.getString(2), resultSet.getString(3));
			}
			if (flag && this.checkAccess(value,queryCondition)) 
				retList.add(value);//最后一个记录
			
			retListBean.setList(retList);
			retListBean.setTotalNumber(getAlertLogSize(queryCondition));
			System.out.println(retListBean.getTotalNumber() + "*********************************************");
			
//			resultSet.close();
//			statement.close();
//			
//			
//			statement = connection.prepareStatement("select count(*) from report where id = 'alertlogs' and createtime >= ? and createtime <= ?");
//			statement.setString(1, SDF.format(queryCondition.getStartTime()));
//			statement.setString(2, SDF.format(queryCondition.getEndTime()));
//			resultSet = statement.executeQuery();
//			if (resultSet.next()) {
//				retListBean.setTotalNumber(resultSet.getInt(1));
//			}
			
			resultSet.close();
			statement.close();
			connection.commit();
			return retListBean;
		} catch (Exception e) {
			if (connection != null)
				connection.rollback();
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		} finally {
			if (connection != null)
				connection.close();
		}
	}

	private Map<String,String> getValue(Connection connection, String createtime,Long identifyKey) {
		Map<String,String> value = new FastMap<String,String>(); 
		try {
			PreparedStatement statement = connection
					.prepareStatement("select reportcreatetime,skey,value from reportdata where reportid='alertlogs' and reportcreatetime = ? and identifyKey = ?" );
			statement.setString(1, createtime);
			statement.setLong(2,identifyKey);
			ResultSet rs = statement.executeQuery();
			
			while (rs.next()) {
				value.put("_AlertTime", SDF.parse(rs.getString(1)).toLocaleString());
				value.put(rs.getString(2), rs.getString(3));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
	private boolean checkAccess(Map<String,String> value, AlertLogQueryCondition queryCondition) {
		/**
		 * _AlertTime=2010-01-14 13:49:05:468+0800, 
		 * _AlertIndex=30383, 
		 * _AlertReceive=yumin@dfkl.com,
		 * _AlertRuleName=告警, 
		 * _AlertStatus=0, 
		 * _AlertType=1, 
		 * _DeviceName=192.168.3.118(Windows), 
		 * _MonitorName=Cpu, 
		 * RecordState=OK
		 */
		 if ("30161".equals(value.get("_AlertIndex"))) {
			 System.out.println("**");
		 }
		if (queryCondition.getAlertIndex() != null && !queryCondition.getAlertIndex().equals("") && !queryCondition.getAlertIndex().equals(value.get("_AlertIndex")))
		{
			return false;
		}
		
		if (queryCondition.getAlertName() != null && !queryCondition.getAlertName().equals("") && !queryCondition.getAlertName().equals(value.get("_AlertRuleName")))
		{
			return false;
		}
		
		if (queryCondition.getAlertReceiver() != null && !queryCondition.getAlertReceiver().equals("") && !queryCondition.getAlertReceiver().equals(value.get("_AlertReceive")))
		{
			return false;
		}
		
		if (queryCondition.getAlertType() != null && !queryCondition.getAlertType().getStringVaule().equals("") && !queryCondition.getAlertType().getStringVaule().equals(value.get("_AlertType")))
		{
			return false;
		}
		return true;
	}
	
	private int getAlertLogSize(AlertLogQueryCondition queryCondition) throws SQLException {
		String sql = "select r.createtime,rd.skey,rd.value from report r,reportdata rd where r.id = 'alertlogs' and r.id = rd.reportid and r.createtime = rd.reportcreatetime and createtime >= ? and createtime <= ? and skey in ('_AlertIndex','_AlertReceive','_AlertRuleName','_AlertType')";
		int count = 0;
		Connection connection = null;
		try {
			connection = DBConnectionManager.getConnection();

			PreparedStatement statement = connection
					.prepareStatement(sql);
			statement.setString(1, SDF.format(queryCondition.getStartTime()));
			statement.setString(2, SDF.format(queryCondition.getEndTime()));
			ResultSet resultSet = statement.executeQuery();
			
			String createtime = null;
			
			Map<String,String> value = new FastMap<String,String>(); 
			boolean flag = false;
			while (resultSet.next()) {
				flag = true;
				if (createtime == null) createtime = resultSet.getString(1);
				if (!createtime.equals(resultSet.getString(1))) {
					if (this.checkAccess(value,queryCondition))  count++;
					value = new FastMap<String,String>();
					createtime = resultSet.getString(1);
				}
				value.put(resultSet.getString(2), resultSet.getString(3));
			}
			if (flag && this.checkAccess(value,queryCondition))  count++;
			
			resultSet.close();
			statement.close();
			connection.commit();
		} catch (Exception e) {
			if (connection != null)
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		} finally {
			if (connection != null)
				connection.close();
		}
		
		return count / 4;
	}
	
	public static void main(String[] args) throws Exception {
		IAlertLogDao dao = new AlertLogDaoImplOfSQL();
		AlertLogQueryCondition condition = new AlertLogQueryCondition ();
		Calendar c = Calendar.getInstance();
		c.add(Calendar.HOUR_OF_DAY,-10);
		
		condition.setStartTime(c.getTime());
		condition.setEndTime(new Date());
//		condition.setAlertIndex("30161");
		
		
		ListBean lb = dao.queryAlertLog(new AccessControl(), condition, 0, 100);
		System.out.println(lb.getList().size());
	}
}
