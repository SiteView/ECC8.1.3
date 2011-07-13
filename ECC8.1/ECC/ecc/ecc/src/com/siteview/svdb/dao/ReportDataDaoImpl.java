package com.siteview.svdb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.siteview.svdb.dao.bean.ReportData;

public class ReportDataDaoImpl implements ReportDataDao {
	private static final Log log = LogFactory.getLog(ReportDataDaoImpl.class);
	public final static SimpleDateFormat SDF = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");

	@Override
	public void delete(String id, Date createTime) throws Exception {
		Connection connection = null;
		try {
			connection = DBConnectionManager.getConnection();
			PreparedStatement statement = connection
					.prepareStatement("delete from REPORTDATA where reportId=? and reportCreateTime=?");
			statement.setString(1, id);
			statement.setString(2, SDF.format(createTime));
			statement.execute();
			statement = connection
					.prepareStatement("delete from REPORT where id=? and createTime=?");
			statement.setString(1, id);
			statement.setString(2, SDF.format(createTime));
			statement.execute();
			connection.commit();
		} catch (Exception e) {
			if (connection != null) connection.rollback();
			log.error(e.getMessage());
			throw new RuntimeException(e);
		} finally {
			if (connection != null) connection.close();
		}

	}

	@Override
	public ReportData getReportData(String id, Date createTime)
			throws Exception {
		Connection connection = null;    
	    try {    
	        connection = DBConnectionManager.getConnection();    
	   
	        PreparedStatement statement = connection    
	                .prepareStatement("select id,createTime,name from REPORT where id=? and createTime=?");    
	        statement.setString(1, id);    
	        statement.setString(2, SDF.format(createTime));    
	        ResultSet resultSet = statement.executeQuery();    
	   
	        ReportData data = null;    
	   
	        if (resultSet.next()) {    
	            data = new ReportData();    
	            data.setId(id);    
	            data.setCreateTime(createTime);
	            data.setName(resultSet.getString("name"));
	            data.setValue(this.getReportValues(id, createTime));
	        }    
	        resultSet.close();    
	        statement.close();    
	        connection.commit();    
	        return data;    
	    } catch (Exception e) {    
			if (connection != null) connection.rollback();
			log.error(e.getMessage());
			throw new RuntimeException(e);
		} finally {
			if (connection != null) connection.close();
	    }    


	}
	private Map<String, String> getReportValues(String id, Date time)
			throws Exception {
		Connection connection = null;
		try {
			connection = DBConnectionManager.getConnection();

			PreparedStatement statement = connection
					.prepareStatement("select reportId,reportCreateTime,skey,value from REPORTDATA where reportId=? and reportCreateTime=?");
			statement.setString(1, id);
			statement.setString(2, SDF.format(time));
			ResultSet resultSet = statement.executeQuery();

			Map<String, String> retmap = new HashMap<String, String>();

			while (resultSet.next()) {
				retmap.put(resultSet.getString("skey"), resultSet.getString("value"));
			}
			resultSet.close();
			statement.close();
			connection.commit();
			return retmap;
		} catch (Exception e) {
			if (connection != null)
				connection.rollback();
			log.error(e.getMessage());
			throw new RuntimeException(e);
		} finally {
			if (connection != null)
				connection.close();
		}
	}

	@Override
	public List<ReportData> getReportData(String id, Date begin, Date end)
			throws Exception {
		Connection connection = null;    
	    try {    
	        connection = DBConnectionManager.getConnection();    
	   
	        PreparedStatement statement = connection    
	                .prepareStatement("select id,createTime,name from REPORT where id=? and createTime>=? and createTime<=?");    
	        statement.setString(1, id);    
	        statement.setString(2, SDF.format(begin));    
	        statement.setString(3, SDF.format(end));    
	        ResultSet resultSet = statement.executeQuery();    
	   
	        List<ReportData> retlist = new LinkedList<ReportData>();    
	   
	        while (resultSet.next()) {    
	        	ReportData data = new ReportData();    
	            data.setId(id);    
	            data.setCreateTime(SDF.parse(resultSet.getString("CreateTime")));
	            data.setName(resultSet.getString("name"));
	            data.setValue(getReportValues(data.getId(),data.getCreateTime()));
	            retlist.add(data);
	        }    
	   
	        resultSet.close();    
	        statement.close();    
	        connection.commit();    
	        return retlist;    
	    } catch (Exception e) {    
			if (connection != null) connection.rollback();
			log.error(e.getMessage());
			throw new RuntimeException(e);
		} finally {
			if (connection != null) connection.close();
	    }    
	}

	@Override
	public void insert(ReportData data) throws Exception {
		Connection connection = null;
		try {
			connection = DBConnectionManager.getConnection();
			PreparedStatement statement = connection
					.prepareStatement("insert into REPORT (id ,createTime,name) values(?,?,?)");
			statement.setString(1, data.getId());
			statement.setString(2, SDF.format(data.getCreateTime()));
			statement.setString(3, data.getName());
			statement.execute();
			for (String key : data.getValueKeys()) {
				String value = data.getValue(key);
				if (value == null) continue;
				statement = connection
						.prepareStatement("insert into REPORTDATA (reportId ,reportCreateTime,skey,value) values(?,?,?,?)");
				statement.setString(1, data.getId());
				statement.setString(2, SDF.format(data.getCreateTime()));
				statement.setString(3, key);
				statement.setString(4, value);
				statement.execute();
			}
			connection.commit();
		} catch (Exception e) {
			if (connection != null) connection.rollback();
			log.error(e.getMessage());
			throw new RuntimeException(e);
		} finally {
			if (connection != null) connection.close();
		}
	}

	@Override
	public void update(ReportData data) throws Exception {
		Connection connection = null;
		try {
			connection = DBConnectionManager.getConnection();
			PreparedStatement statement = connection
					.prepareStatement("delete from REPORTDATA where reportId=? and reportCreateTime=?");
			statement.setString(1, data.getId());
			statement.setString(2, SDF.format(data.getCreateTime()));
			statement.execute();
			
			statement = connection
					.prepareStatement("insert into REPORTDATA (reportId ,reportCreateTime,skey,value) values(?,?,?,?)");
			statement.setString(1, data.getId());
			statement.setString(2, SDF.format(data.getCreateTime()));
			for (String key : data.getValueKeys()){
				String value = data.getValue(key);
				if (value == null) continue;
				statement.setString(3, key);
				statement.setString(4, value);
				statement.execute();
			}
			connection.commit();
		} catch (Exception e) {
			if (connection != null) connection.rollback();
			log.error(e.getMessage());
			throw new RuntimeException(e);
		} finally {
			if (connection != null) connection.close();
		}
	}

	@Override
	public void update(List<ReportData> datalist) throws Exception {
		Connection connection = null;
		try {
			connection = DBConnectionManager.getConnection();
			for (ReportData data : datalist){
				PreparedStatement statement = connection
				.prepareStatement("delete from REPORTDATA where reportId=? and reportCreateTime=?");
				statement.setString(1, data.getId());
				statement.setString(2, SDF.format(data.getCreateTime()));
				statement.execute();
				statement = connection
				.prepareStatement("delete from REPORT where id=? and createTime=?");
				statement.setString(1, data.getId());
				statement.setString(2, SDF.format(data.getCreateTime()));
				statement.execute();
				statement = connection
				.prepareStatement("insert into REPORT (id ,createTime,name) values(?,?,?)");
				statement.setString(1, data.getId());
				statement.setString(2, SDF.format(data.getCreateTime()));
				statement.setString(3, data.getName());
				statement.execute();
				for (String key : data.getValueKeys()) {
					String value = data.getValue(key);
					if (value == null) continue;
					statement = connection
							.prepareStatement("insert into REPORTDATA (reportId ,reportCreateTime,skey,value) values(?,?,?,?)");
					statement.setString(1, data.getId());
					statement.setString(2, SDF.format(data.getCreateTime()));
					statement.setString(3, key);
					statement.setString(4, value);
					statement.execute();
				}
			}
			connection.commit();
		} catch (Exception e) {
			if (connection != null) connection.rollback();
			log.error(e.getMessage());
			throw new RuntimeException(e);
		} finally {
			if (connection != null) connection.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, -200);
		ReportDataDao d = DaoFactory.getReportDataDao();
		List<ReportData> rdata = d.getReportData("1.22.14.1.1", c.getTime(), new Date());
		for (ReportData rd : rdata) {
			System.out.println(rd.getId() + "--" + rd.getName() + "--" + rd.getValueKeys());
		}
	}
}
