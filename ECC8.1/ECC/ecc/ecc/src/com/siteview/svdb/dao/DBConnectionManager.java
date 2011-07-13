package com.siteview.svdb.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;

public class DBConnectionManager {
	//private static final Log log = LogFactory.getLog(DBConnectionManager.class);
	
	//private final static SimpleDateFormat SDF = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");

	/*
 	static{
		try {
			createMainTable();
			createValueTable();
			log.info("数据库不存在，已经建立了一个。");
		} catch (SQLException e) {
			//e.printStackTrace();
			log.info("数据库已经存在。");
		}
	}
	public static void main(String[] args) throws Exception{
		//createMainTable();
		//createValueTable();
		ReportData data = new ReportData();
		data.setId("aaa");
		data.setCreateTime(SDF.parse("2009-11-12 17:11:58"));
		data.setValue("aaaaaaaa1", "ccssssssssssssssssss");
		data.setValue("aaaaaaaa2", "ccccacc3");
		data.setValue("aaaaaaaa3", "ccccc22");
		data.setValue("aaaaaaaa4", "cccccc1");
		new ReportDataDaoImpl().update(data);
		
		for (ReportData data1 : new ReportDataDaoImpl().getReportData("aaa", SDF.parse("1999-10-22 22:22:22"), new Date())){
			System.out.println(data1.getCreateTime());
		}
	}
	private static void createMainTable() throws SQLException {    
	    Statement statement = getConnection().createStatement();    
	    String sql = "create table REPORT("   
	            + "   id                  VARCHAR(255)           not null,"   
	            + "   createTime          VARCHAR(19)            not null,"   
	            + "   name                VARCHAR(255),"   
	            + "   constraint P_KEY_REPORT primary key (id,createTime))";    
	    statement.execute(sql);    
	    sql = "create unique index REPORT_INDEX on REPORT (id,createTime ASC)";    
	    statement.execute(sql);    
	    statement.close();    
	}   
	private static void createValueTable() throws SQLException {    
	    Statement statement = getConnection().createStatement();    
	    String sql = "create table REPORTDATA("   
	            + "   reportId            VARCHAR(255)           not null,"   
	            + "   reportCreateTime    VARCHAR(19)            not null,"   
	            + "   skey                 VARCHAR(255)            not null,"   
	            + "   value               VARCHAR(255),"   
	            + "   constraint P_KEY_REPORTDATA primary key (reportId,reportCreateTime,skey))";    
	    statement.execute(sql);    
	    sql = "create unique index REPORTDATA_INDEX on REPORTDATA (reportId,reportCreateTime,skey ASC)";    
	    statement.execute(sql);    
	    sql = "ALTER TABLE REPORTDATA ADD CONSTRAINT Id_CreateTime_FK Foreign Key (reportId,reportCreateTime) REFERENCES REPORT (id,createTime)";    
	    statement.execute(sql);    
	    statement.close();    
	} 
	*/
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
		return BasicDataSourceFactory.createDataSource(loadFile("database.properties"));
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
