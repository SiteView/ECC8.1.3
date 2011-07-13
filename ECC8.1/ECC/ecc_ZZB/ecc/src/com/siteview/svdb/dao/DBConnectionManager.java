package com.siteview.svdb.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DBConnectionManager {
	private static final Log log = LogFactory.getLog(DBConnectionManager.class);
	
	private final static SimpleDateFormat SDF = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");

 	static{
		try {
			createMainTable();
			createView();
			createIndexReport();
			createIndexReportdata();
			log.info("数据库不存在，已经建立了一个。");
		} catch (Exception e) {
			//e.printStackTrace();
			log.info("数据库已经存在。");
		}
	}
	public static void main(String[] args) throws Exception{
		//createMainTable();
		//createValueTable();
//		ReportData data = new ReportData();
//		data.setId("aaa");
//		data.setCreateTime(SDF.parse("2009-11-12 17:11:58"));
//		data.setValue("aaaaaaaa1", "ccssssssssssssssssss");
//		data.setValue("aaaaaaaa2", "ccccacc3");
//		data.setValue("aaaaaaaa3", "ccccc22");
//		data.setValue("aaaaaaaa4", "cccccc1");
//		new ReportDataDaoImpl().update(data);
//		
//		for (ReportData data1 : new ReportDataDaoImpl().getReportData("aaa", SDF.parse("1999-10-22 22:22:22"), new Date())){
//			System.out.println(data1.getCreateTime());
//		}
		createMainTable();
		ArrayList<String> monitorIdList = new ArrayList<String>();
		monitorIdList.add("1.11.1");
		monitorIdList.add("1.11.2");
		monitorIdList.add("1.11.3");
		monitorIdList.add("1.11.4");		
		monitorIdList.add("1.11.5");
		monitorIdList.add("1.11.6");
		
		DaoFactory.getTelebackupDataDao().insert(monitorIdList);
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
			

		
/*		CREATE TABLE [dbo].[telebackup](
				[monitorId] [varchar](50) COLLATE Chinese_PRC_CI_AS NOT NULL,
			 CONSTRAINT [PK_telebackup] PRIMARY KEY CLUSTERED 
			(
				[monitorId] ASC
			)WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]
			) ON [PRIMARY]*/
		
			
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
//
//	USE [test]
//	     GO
//	     /****** 对象:  Index [reportindex]    脚本日期: 03/10/2010 10:48:05 ******/
//	     CREATE NONCLUSTERED INDEX [reportindex] ON [dbo].[report] 
//	     (
//	     	[id] ASC,
//	     	[createTime] DESC
//	     )WITH (SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF) ON [PRIMARY]
	
	private static void createIndexReport() throws Exception {
		Connection connection = getConnection();
		try {
			String sql = "CREATE NONCLUSTERED INDEX reportindex ON report "
				+ "( "
				+ " id ASC ," 
				+ " createTime DESC " 
				+ ") WITH (SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF) ON [PRIMARY]";
				   
			Statement statement = connection.createStatement();
			statement.execute(sql);
			connection.commit();
			statement.close();
		} catch(Exception e){
			connection.rollback();
		} finally {
			
		}
	}
//	USE [test]
//    GO
//    /****** 对象:  Index [reportdataindex]    脚本日期: 03/10/2010 11:17:51 ******/
//    CREATE NONCLUSTERED INDEX [reportdataindex] ON [dbo].[reportdata] 
//    (
//    	[reportId] ASC,
//    	[reportCreateTime] DESC
//    )WITH (SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF) ON [PRIMARY]
//
	private static void createIndexReportdata() throws Exception {
		Connection connection = getConnection();
		try {
			String sql = "CREATE NONCLUSTERED INDEX reportdataindex ON reportdata "
				+ "( "
				+ " reportId ASC ," 
				+ " reportCreateTime DESC " 
				+ ")WITH (SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF) ON [PRIMARY]";
				   
			Statement statement = connection.createStatement();
			statement.execute(sql);
			connection.commit();
			statement.close();
		} catch(Exception e){
			connection.rollback();
		} finally {
			
		}
	}
	
	private static DataSource datasource = null;
	public static Connection getConnection() throws Exception {
		if (datasource == null){
			//datasource = getDataSource(com.mysql.jdbc.Driver.class,"jdbc:mysql://localhost/svdb","root","888888");    
			datasource = getDataSource();    
		}
        return datasource.getConnection();    
    } 
	private static Properties loadFile(String path) {
		Properties prop = null;
		try {
			InputStream in = DBConnectionManager.class.getClassLoader().getResourceAsStream(path);
			prop = new Properties();
			if (path.endsWith(".xml"))
				prop.loadFromXML(in);
			else
				prop.load(in);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prop;
	}
	
	private static DataSource getDataSource() throws Exception {
//		File f = new File("C:\\SiteView\\WebECC\\WebECC8.1.3\\Tomcat6\\bin\\dtConfig.properties");
//		Properties prop = new Properties();
//		prop.load(new FileInputStream(f));
//		String host = prop.getProperty("dbIp");
//		String port = prop.getProperty("dbPort");
//		String dbName = prop.getProperty("dbName");
//		String username = prop.getProperty("dbUser");
//		String password = prop.getProperty("dbPassword");
//		
//		
//		String url = "jdbc:sqlserver://" + host + ":" + port + ";databaseName=" + dbName;
		Properties dbProp = loadFile("database.properties");
//		dbProp.setProperty("url", url);
//		dbProp.setProperty("username", username);
//		dbProp.setProperty("password", password);
		return BasicDataSourceFactory.createDataSource(dbProp);
	}
	
	private static DataSource getDataSource(Class<?> classz,String connectURI,String username,String password) {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(classz.getName());
		ds.setUsername(username);
		ds.setPassword(password);
		ds.setUrl(connectURI);
		return ds;
	}
	
}
