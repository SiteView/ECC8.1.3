package com.siteview.svdb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.siteview.svdb.dao.bean.ReportData;

public class TelebackupDataDaoImpl implements TelebackupDataDao {
	private static final Log log = LogFactory.getLog(TelebackupDataDaoImpl.class);

	@Override
	public void insert(ArrayList<String> ids) throws Exception {
		Connection connection = null;
		try {
			connection = DBConnectionManager.getConnection();	
			
			PreparedStatement statement = connection.prepareStatement("delete  from telebackup ");
			statement.execute();
			
			for (String id : ids) {
				if (id == null) continue;
				if ("".equals(id)) continue;
				statement = connection
						.prepareStatement("insert into telebackup (monitorId) values(?)");
				statement.setString(1, id);
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
	public boolean query(String id)throws Exception {
		Connection connection = null;
		try {
			connection = DBConnectionManager.getConnection();

			PreparedStatement statement = connection
					.prepareStatement("select monitorId from telebackup where monitorId = ?");
			statement.setString(1, id);
			ResultSet resultSet = statement.executeQuery();

			boolean flag = false;
			if(resultSet.next()){
				String temp = resultSet.getString("monitorId");
				flag =true;
			}
			resultSet.close();
			statement.close();
			connection.commit();
			return flag;
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



	public static void main(String[] args) throws Exception {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, -200);
		TelebackupDataDao d = DaoFactory.getTelebackupDataDao();
		boolean flag = d.query("1.22.6.10");
		System.out.println("&&&"+flag);
		
		
/*		ArrayList<String> selectedIds = new ArrayList<String>();
			selectedIds.add("1.22.4.1");
			selectedIds.add("1.22.4.2");
			selectedIds.add("1.22.4.3");
			selectedIds.add("1.22.4.4");
			selectedIds.add("1.22.4.5");
			selectedIds.add("1.22.4.6");
			selectedIds.add("1.22.4.7");
			
			d.insert((ArrayList<String>)selectedIds);*/

	}

}
