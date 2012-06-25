package com.siteview.base.cache;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.siteview.base.cache.bean.ReportData;
import com.siteview.ecc.report.beans.ErrorLogsBean;

public class ReportDataDaoImpl implements ReportDataDao {
	private static final Log log = LogFactory.getLog(ReportDataDaoImpl.class);
	public final static SimpleDateFormat SDF = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
	private int i=0;
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
		} catch (SQLException e) {
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
	    } catch (SQLException e) {    
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
		} catch (SQLException e) {
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
	    } catch (SQLException e) {    
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
		} catch (SQLException e) {
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
		} catch (SQLException e) {
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
		} catch (SQLException e) {
			if (connection != null) connection.rollback();
			log.error(e.getMessage());
			throw new RuntimeException(e);
		} finally {
			if (connection != null) connection.close();
		}
	}
	@Override
	public void inserts(ErrorLogsBean data) throws Exception {
		Connection connection = null;
		try {
			connection = com.siteview.svdb.dao.DBConnectionManager.getConnection();
			PreparedStatement statement = connection
					.prepareStatement("insert into reportLog (name,time,title,data,type,username,result,target) values(?,?,?,?,?,?,?,?)");
			statement.setString(1, data.getName());
			statement.setString(2, data.getTime());
			statement.setString(3, data.getTitle());
			statement.setString(4, data.getData());
			statement.setString(5, data.getType());
			statement.setString(6, data.getUsername());
			statement.setString(7, data.getResult());
			statement.setString(8, data.getTarget());
			statement.execute();
			connection.commit();
		} catch (SQLException e) {
			if (connection != null) connection.rollback();
			log.error(e.getMessage());
			throw new RuntimeException(e);
		} finally {
			if (connection != null) connection.close();
		}
	}

	public List<ErrorLogsBean> getValue() throws Exception {
		Connection connection = null;
		try {
			connection = com.siteview.svdb.dao.DBConnectionManager.getConnection();
			List<ErrorLogsBean> retlist = new ArrayList<ErrorLogsBean>(); 
			PreparedStatement statement = connection
					.prepareStatement("select * from reportLog");
			ResultSet resultSet = statement.executeQuery();
			 while (resultSet.next()) {    
				 ErrorLogsBean data = new ErrorLogsBean();
				 data.setData(resultSet.getString("data"));
				 data.setTime(resultSet.getString("time"));
				 data.setTitle(resultSet.getString("title"));
				 data.setName(resultSet.getString("name"));
				 data.setType(resultSet.getString("type"));
				 data.setUsername(resultSet.getString("username"));
		         retlist.add(data);
		        }    
		   
			connection.commit();
			return retlist;
		} catch (SQLException e) {
			if (connection != null) connection.rollback();
			log.error(e.getMessage());
			throw new RuntimeException(e);
		} finally {
			if (connection != null) connection.close();
		}
	}
	//批量删除
	public void deleteValue(ErrorLogsBean errorLogsBean) throws Exception {
		Connection connection = null;
		try {
			connection = com.siteview.svdb.dao.DBConnectionManager.getConnection();
			List<ErrorLogsBean> retlist = new ArrayList<ErrorLogsBean>(); 
			String sql="delete from reportLog where data = '" + errorLogsBean.getData() +"' and title = '"+ errorLogsBean.getTitle() +"' and type = '"+errorLogsBean.getType()+"'";
			System.out.println("sql > "+sql);
			PreparedStatement statement = connection
					.prepareStatement(sql);
			statement.execute();
			connection.commit();
		} catch (SQLException e) {
			if (connection != null) connection.rollback();
			log.error(e.getMessage());
			throw new RuntimeException(e);
		} finally {
			if (connection != null) connection.close();
		}
	}
	// 批量下载 selectValue
	public String selectValue(ErrorLogsBean errorLogsBean) throws Exception {
		Connection connection = null;
		try {
			connection = com.siteview.svdb.dao.DBConnectionManager.getConnection();
			List<ErrorLogsBean> retlist = new ArrayList<ErrorLogsBean>(); 
			String sql="select  target from reportLog where data = '" + errorLogsBean.getData() +"' and title = '"+ errorLogsBean.getTitle() +"' and type = '"+errorLogsBean.getType()+"'";
			System.out.println("sql > "+sql);
			PreparedStatement statement = connection
					.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {    
				return resultSet.getString("target");
	        }  
			connection.commit();
		} catch (SQLException e) {
			if (connection != null) connection.rollback();
			log.error(e.getMessage());
			throw new RuntimeException(e);
		} finally {
			if (connection != null) connection.close();
		}
		return null;
	}
}
