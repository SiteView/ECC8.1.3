package com.siteview.svdb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.siteview.base.data.ReportDateError;
import com.siteview.svdb.dao.bean.ReportData;

public class ReportDataDaoImpl implements ReportDataDao {
	private static final Log log = LogFactory.getLog(ReportDataDaoImpl.class);
	public final static SimpleDateFormat SDF = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss:SSSZ");
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
	public ReportData getReportData(String id, Date createTime)
			throws Exception {
		Connection connection = null;
		try {
			connection = DBConnectionManager.getConnection();

			PreparedStatement statement = connection
					.prepareStatement("select id,createTime,name from REPORT where id=? and createTime=?");
			statement.setString(1, id);
			statement.setString(2, SDF.format(createTime));
//			System.out.println("1@select id,createTime,name from REPORT where id="+id+" and createTime="+SDF.format(createTime));

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
			if (connection != null)
				connection.rollback();
			log.error(e.getMessage());
			throw new RuntimeException(e);
		} finally {
			if (connection != null)
				connection.close();
		}

	}
	protected Map<String, String> getReportValues(String id, Date time)
			throws Exception {
		Connection connection = null;
		try {
			connection = DBConnectionManager.getConnection();

			PreparedStatement statement = connection
					.prepareStatement("select reportId,reportCreateTime,skey,value from REPORTDATA where reportId=? and reportCreateTime=?");
			statement.setString(1, id);
			statement.setString(2, SDF.format(time));
//			System.out.println("2@select reportId,reportCreateTime,skey,value from REPORTDATA where reportId="+id+" and reportCreateTime="+SDF.format(time));

			ResultSet resultSet = statement.executeQuery();

			Map<String, String> retmap = new HashMap<String, String>();

			while (resultSet.next()) {
				retmap.put(resultSet.getString("skey"), resultSet
						.getString("value"));
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
//			System.out.println("3@select id,createTime,name from REPORT where id="+id+" and createTime>="+SDF.format(begin)+" and createTime<="+SDF.format(end));
			ResultSet resultSet = statement.executeQuery();

			List<ReportData> retlist = new LinkedList<ReportData>();

			while (resultSet.next()) {
				try{
					ReportData data = new ReportData();
					data.setId(id);
					data
							.setCreateTime(SDF.parse(resultSet
									.getString("CreateTime")));
					data.setName(resultSet.getString("name"));
					data.setValue(getReportValues(data.getId(), data
							.getCreateTime()));
					retlist.add(data);
				}catch(Exception e){
					e.printStackTrace();
				}
			}

			resultSet.close();
			statement.close();
			connection.commit();
			return retlist;
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
				if (value == null)
					continue;
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
			if (connection != null)
				connection.rollback();
			log.error(e.getMessage() + "------------------------" + SDF.format(data.getCreateTime()));
			throw new RuntimeException(e);
		} finally {
			if (connection != null)
				connection.close();
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
			for (String key : data.getValueKeys()) {
				String value = data.getValue(key);
				if (value == null)
					continue;
				statement.setString(3, key);
				statement.setString(4, value);
				statement.execute();
			}
			connection.commit();
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
	public void update(List<ReportData> datalist) throws Exception {
		Connection connection = null;
		try {
			connection = DBConnectionManager.getConnection();
			for (ReportData data : datalist) {
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
					if (value == null)
						continue;
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
			if (connection != null)
				connection.rollback();
			log.error(e.getMessage());
			throw new RuntimeException(e);
		} finally {
			if (connection != null)
				connection.close();
		}
	}

	public Map<String, List<ReportData>> queryReportdataByTime(String[] idArr,
			Date begin, Date end) throws SQLException {
		
		Connection connection = null;
		try {
			Map<String, List<ReportData>> retMap = new FastMap<String, List<ReportData>>();
			if (idArr.length == 0 ) return retMap;
			connection = DBConnectionManager.getConnection();
			
			StringBuilder sb = new StringBuilder();
			for (String id : idArr) {
				if (sb.length()>0)
					sb.append(",");
				sb.append("'").append(id).append("'");
			}
			
			PreparedStatement statement = connection
					.prepareStatement("select id,createtime,skey,value from view_report where id in (" + sb.toString() + ") and createTime>=? and createTime<=? order by id");
			statement.setString(1, SDF.format(begin == null ? new Date() : begin));
			statement.setString(2, SDF.format(end == null ? new Date() : end));
//			System.out.println("4@select id,createtime,skey,value from view_report where id in (" + sb.toString() + ") and createTime>=a and createTime<=b");
			ResultSet resultSet = statement.executeQuery();

			if (resultSet == null) return retMap;

			String id = null;
			String createtime = null;
			List<ReportData> rd = new FastList<ReportData>();
			ReportData d = new ReportData();
			int size = 0;
			System.out.println("resultSet.next()  >  "+resultSet.next());
			while (resultSet.next()) {
				size ++;
				try{
					id = (id == null ? resultSet.getString(1) : id);
					createtime = (createtime == null ? resultSet.getString(2) : createtime);
					if (id.equals(resultSet.getString(1))==false) {
						retMap.put(id, rd);
						id = resultSet.getString(1);
						rd = new FastList<ReportData>();
						d = new ReportData();
						createtime = resultSet.getString(2);
					}
					if (createtime.equals(resultSet.getString(2))==false) {
						rd.add(d);
						d = new ReportData();
						createtime = resultSet.getString(2);
					}
//					if(sb.toString().contains("1.31.2.1.1")){
//						System.out.println(resultSet.getString(1)+"	"+resultSet.getString(2)+"	"+resultSet.getString(3)+"	"+resultSet.getString(4));
//					}
					Date tempCreateTime = null;
					try{
						tempCreateTime = SDF.parse(resultSet.getString(2));
					}catch(Exception e){}
					if(tempCreateTime!=null){
						d.setCreateTime(tempCreateTime);
						d.setId(id);
						d.setName("");
						d.setValue(resultSet.getString(3), resultSet.getString(4));
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
//			System.out.println(sb.toString()+"		"+begin+"		"+end+"		"+size);

			if (id == null)
				return retMap;
			rd.add(d);
			retMap.put(id, rd);
			return retMap;
		} catch (Exception e) {
			if (connection != null)
				connection.rollback();
			log.error(e.getMessage());
			throw new RuntimeException(e);
		} finally {
			if (connection != null)
				connection.close();
		}
		// return null;
	}
	public Map<String, List<ReportData>> queryReportdataByCount(String[] idArr,int count) throws SQLException {
		Connection connection = null;
		try {
			connection = DBConnectionManager.getConnection();
			Map<String, List<ReportData>> retMap = new FastMap<String, List<ReportData>>();
			for (String id : idArr) {//查询40个时间
//				PreparedStatement statement = connection.
//				prepareStatement("select top " + count + " rt.createtime from report as rt  where rt.id=? order by rt.createtime desc");
//				statement.setString(1, id);
				Statement statement = connection.createStatement();
				String temp40id = "\'"+ id + "\'";
				String sql40 = "select top " + count + " rt.createtime from report as rt  " +
						" where rt.id= " + temp40id + 
						" order by rt.createtime desc";
				
				ResultSet resultSet40Times = statement.executeQuery(sql40);
				if(resultSet40Times == null) return retMap;
				LinkedList<String> timelist = new LinkedList<String>();
				while(resultSet40Times.next()){
					if (resultSet40Times.getString(1) == null) continue;
					String temp = resultSet40Times.getString(1);
					timelist.add(temp);
				}
				LinkedList<ReportData> rds = new LinkedList<ReportData>();
				int q = 0;
				for(String tempTime:timelist){//40
					tempTime = "\'"+tempTime+"\'";
					String tempid = "\'"+ id + "\'";
					
					String sql =
						" select r.reportcreatetime,r.reportid,r.skey,r.value "+ 
						" from reportdata as r"+
						" where r.reportcreatetime = "+ tempTime +
						" and  r.reportid = "+ tempid ;
					Statement statementNew = connection.createStatement();
					ResultSet resultSet = statementNew.executeQuery(sql);
					
					ReportData rd = new ReportData();
					while (resultSet.next()) {
						rd.setCreateTime(SDF.parse(resultSet.getString(1)));
						rd.setId(resultSet.getString(2));
						rd.setValue(resultSet.getString(3), resultSet.getString(4));
					}
					rds.add(rd);
				}
				retMap.put(id, rds);
			}
			return retMap;
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
	public Map<String, List<ReportData>> queryReportdataByCountOLD(String[] idArr,int count) throws SQLException {
		Connection connection = null;
		try {
			connection = DBConnectionManager.getConnection();
			StringBuilder sb = new StringBuilder("(");
			for (int i = 0; i < idArr.length; i++) {
				sb.append("'").append(idArr[i]).append("'");
				if (i == (idArr.length - 1)) {
					sb.append(")");
				} else {
					sb.append(",");
				}
			}
			//select * from reportdata where reportcreatetime in(select top 40 createtime from report r where id=? order by r.createtime desc) and  reportid=? order by reportcreatetime
			PreparedStatement statement = connection
					.prepareStatement("select top " + count + " id,createtime from report where id in "	+ sb + " order by createtime desc");
//			System.out.println("6@select top " + count + " id,createtime from report where id in "	+ sb + " order by createtime");
			log.info("select top " + count + " id,createtime from report where id in "	+ sb + " order by createtime");
			ResultSet resultSet = statement.executeQuery();
			Map<String, List<ReportData>> retMap = new FastMap<String, List<ReportData>>();
			if (resultSet == null) return retMap;

			String id = null;
			List<ReportData> rds = new FastList<ReportData>();
			while (resultSet.next()) {
				try{
					if (id == null) id = resultSet.getString(1);
					
					if (id == null) continue;
					
					if (!id.equals(resultSet.getString(1))) {
						retMap.put(id, rds);
						id = resultSet.getString(1);
						rds = new FastList<ReportData>();
					}
					
					Map<String, String> values = getReportValues(resultSet.getString(1),SDF.parse(resultSet.getString(2)));
					ReportData r = new ReportData();
					r.setCreateTime(SDF.parse(resultSet.getString(2)));
					r.setId(id);
					r.setValue(values);
					rds.add(r);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			if (id != null) retMap.put(id, rds);
			return retMap;
		} catch (Exception e) {
			if (connection != null)
				connection.rollback();
			log.error(e.getMessage());
			throw new RuntimeException(e);
		} finally {
			if (connection != null)
				connection.close();
		}
		// return null;
	}
	public static void main(String[] args) throws Exception {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, -200);
		ReportDataDao d = DaoFactory.getReportDataDao();
		// Map<String, List<ReportData>> rdata = ((ReportDataDaoImpl)
		// d).queryReportdataByTime(new String[] {"1.5.7.1","1.5.7.2"},
		// c.getTime(), new Date());
		NumberFormat df = NumberFormat.getIntegerInstance();
		df.setMaximumFractionDigits(2);
		System.out.println(df.format(0.65));
	}
	@Override
	public Map<String, List<ReportDateError>> queryReportErrordataByCount(
			String[] idArr, int count) throws Exception {
		Connection connection = null;
		try {
			connection = DBConnectionManager.getConnection();
			Map<String, List<ReportDateError>> retMap = new FastMap<String, List<ReportDateError>>();
			for (String id : idArr) {//查询40个时间
//				PreparedStatement statement = connection.
//				prepareStatement("select top " + count + " rt.createtime from report as rt  where rt.id=? order by rt.createtime desc");
//				statement.setString(1, id);
				Statement statement = connection.createStatement();
				String temp40id = "\'"+ id + "\'";
				String sql40 = "select top " + count + " rt.createtime from report as rt  " +
						" where rt.id= " + temp40id + 
						" order by rt.createtime desc";
				
				ResultSet resultSet40Times = statement.executeQuery(sql40);
				if(resultSet40Times == null) return retMap;
				LinkedList<String> timelist = new LinkedList<String>();
				while(resultSet40Times.next()){
					if (resultSet40Times.getString(1) == null) continue;
					String temp = resultSet40Times.getString(1);
					timelist.add(temp);
				}
				List<ReportDateError> rds = new LinkedList<ReportDateError>();
				int q = 0;
				for(String tempTime:timelist){
					tempTime = "\'"+tempTime+"\'";
					String tempid = "\'"+ id + "\'";
					
					String sql =
						" select r.reportcreatetime,r.reportid,r.skey,r.value "+ 
						" from reportdata as r"+
						" where r.reportcreatetime = "+ tempTime +
						" and  r.reportid = "+ tempid ;
					Statement statementNew = connection.createStatement();
					ResultSet resultSet = statementNew.executeQuery(sql);
					
					ReportDateError rd = new ReportDateError();
					while (resultSet.next()) {
						rd.setCreateTime(SDF.parse(resultSet.getString(1)));
						rd.setId(resultSet.getString(2));
						rd.setValue(resultSet.getString(3), resultSet.getString(4));
					}
					rds.add(rd);
				}
				retMap.put(id, rds);
			}
			return retMap;
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
	public Map<String, List<ReportDateError>> queryReportErrordataByTime (
			String[] idArr, Date begin, Date end) throws Exception  {

		Connection connection = null;
		try {
			Map<String, List<ReportDateError>> retMap = new FastMap<String, List<ReportDateError>>();
			if (idArr.length == 0 ) return retMap;
			connection = DBConnectionManager.getConnection();
			
			StringBuilder sb = new StringBuilder();
			for (String id : idArr) {
				if (sb.length()>0)
					sb.append(",");
				sb.append("'").append(id).append("'");
			}
			
			PreparedStatement statement = connection
					.prepareStatement("select id,createtime,skey,value from view_report where id in (" + sb.toString() + ") and createTime>=? and createTime<=?");
			statement.setString(1, SDF.format(begin == null ? new Date() : begin));
			statement.setString(2, SDF.format(end == null ? new Date() : end));
//			System.out.println("4@select id,createtime,skey,value from view_report where id in (" + sb.toString() + ") and createTime>=a and createTime<=b");
			ResultSet resultSet = statement.executeQuery();
			
			if (resultSet == null) return retMap;

			String id = null;
			String createtime = null;
			List<ReportDateError> rd = new FastList<ReportDateError>();
			ReportDateError d = new ReportDateError();
			while (resultSet.next()) {
				try{
					id = (id == null ? resultSet.getString(1) : id);
					createtime = (createtime == null ? resultSet.getString(2) : createtime);
					if (!id.equals(resultSet.getString(1))) {
						retMap.put(id, rd);
						id = resultSet.getString(1);
						rd = new FastList<ReportDateError>();
						d = new ReportDateError();
						createtime = resultSet.getString(2);
					}
					if (!createtime.equals(resultSet.getString(2))) {
						rd.add(d);
						d = new ReportDateError();
						createtime = resultSet.getString(2);
					}
	
					d.setCreateTime(SDF.parse(resultSet.getString(2)));
					d.setId(id);
					d.setName("");
					d.setValue(resultSet.getString(3), resultSet.getString(4));
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			if (id == null)
				return retMap;
			rd.add(d);
			retMap.put(id, rd);
			return retMap;
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
	
}
