package com.siteview.svdb.dao;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.siteview.svdb.utils.PropertyConfig;

public class DBConnectionManager {
	private static final Log log = LogFactory.getLog(DBConnectionManager.class);

	private final static SimpleDateFormat SDF = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	static {
		try {
			 createMainTable();
			 createView();
			log.info("数据库不存在，已经建立了一个。");
		} catch (Exception e) {
			// e.printStackTrace();
			log.info("数据库已经存在。");
		}
	}

	public static void main(String[] args) throws Exception {
		createMainTable();
		createView();
	}

	private static void createMainTable() throws Exception {
		Connection connection = getConnection();
		try {
			Statement statement = connection.createStatement();

			String sql = "if not exists ( select *  from  sysobjects where name = 'report')"
					+ "CREATE TABLE report"
					+ "(identifyKey int,"
					+ "id varchar(255) NOT NULL,"
					+ "createTime varchar(50)  NOT NULL,"
					+ "name varchar(255)  NULL,"
					+ "CONSTRAINT PK_report PRIMARY KEY CLUSTERED (identifyKey asc,id asc ,createtime asc))";
			statement.addBatch(sql);
			sql = "if not exists ( select *  from  sysobjects where name = 'reportdata')"
					+ "CREATE TABLE reportdata("
					+ "identifyKey int,"
					+ "reportId varchar(255)  NOT NULL,"
					+ "reportCreateTime varchar(50)  NOT NULL,"
					+ "skey varchar(255)  NOT NULL,value varchar(255)  NULL,"
					+ "CONSTRAINT PK_reportdata PRIMARY KEY CLUSTERED  (identifyKey asc,reportId ASC, reportCreateTime ASC,skey ASC  ))";
			statement.addBatch(sql);
			
			sql = "if not exists ( select *  from  sysobjects where name = 'telebackup')"
				+ "CREATE TABLE telebackup("
				+ "monitorId varchar(255)  NOT NULL,"
				+ "CONSTRAINT PK_telebackup PRIMARY KEY CLUSTERED  (monitorId asc))";
			statement.addBatch(sql);
				
			
			statement.executeBatch();
			connection.commit(); 
			statement.close();
		} catch (Exception e) {
			connection.rollback();
			e.printStackTrace();
		} finally {
			if (connection != null)
				connection.close();
		}
	}

	private static void createView() throws Exception {
		Connection connection = getConnection();
		try {
			String sql = "create view view_report as "
					   + "select r.id,r.createtime,rd.skey,rd.value from report r ,reportdata rd "
					   + "where r.createtime = rd.reportCreatetime and r.id = rd.reportId and r.identifyKey=rd.identifyKey;";
			Statement statement = connection.createStatement();
			statement.execute(sql);
			connection.commit();
			statement.close();
		} catch (Exception e) {
			connection.rollback();
//			e.printStackTrace();
		} finally {
			if (connection != null)
				connection.close();
		}
	}

	private static DataSource datasource = null;

	public static Connection getConnection() throws Exception {
		if (datasource == null) {
			// datasource =
			// getDataSource(com.mysql.jdbc.Driver.class,"jdbc:mysql://localhost/svdb","root","888888");
			datasource = getDataSource();
		}
		return datasource.getConnection();
	}



	private static DataSource getDataSource() throws Exception {
		Properties dbProp = PropertyConfig.loadFile("database.properties");
		return BasicDataSourceFactory.createDataSource(dbProp);
	}
/*	private static DataSource getDataSource() throws Exception {
		File f = new File(
				"C:\\SiteView\\WebECC\\WebECC8.1.3\\Tomcat6\\bin\\dtConfig.properties");
		Properties prop = new Properties();
		prop.load(new FileInputStream(f));
		String host = prop.getProperty("dbIp");
		String port = prop.getProperty("dbPort");
		String dbName = prop.getProperty("dbName");
		String username = prop.getProperty("dbUser");
		String password = prop.getProperty("dbPassword");

		String url = "jdbc:sqlserver://" + host + ":" + port + ";databaseName="
				+ dbName;
		Properties dbProp = PropertyConfig.loadFile("database.properties");
		dbProp.setProperty("url", url);
		dbProp.setProperty("username", username);
		dbProp.setProperty("password", password);
		return BasicDataSourceFactory.createDataSource(dbProp);
	}
*/
	private static DataSource getDataSource(Class<?> classz, String connectURI,
			String username, String password) {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(classz.getName());
		ds.setUsername(username);
		ds.setPassword(password);
		ds.setUrl(connectURI);
		return ds;
	}

}
