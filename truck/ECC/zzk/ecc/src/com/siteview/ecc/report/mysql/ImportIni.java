package com.siteview.ecc.report.mysql;
import java.util.Date;
import java.util.HashMap;

import org.zkoss.zul.Textbox;

import com.siteview.base.data.IniFile;
import com.siteview.ecc.util.Toolkit;
public class ImportIni extends HashMap {
	private static IniFile svdbToSql;
	public static ImportIni getInstance()
	{
		return new ImportIni();
	}
	public IniFile getSvdbToSqlIni()
	{
		if(svdbToSql==null)
		{
			svdbToSql=new IniFile("svdb_tosql.ini");
			try
			{
				svdbToSql.load();
			}catch(Exception e)
			{
			}
			
			String sections1=svdbToSql.getValue("web", "siteviewDS");
			String sections2=svdbToSql.getValue("web", "siteviewDS");
			if(sections1==null||sections2==null)
			{
				try{
				svdbToSql.createSection("web");
				svdbToSql.createSection("sqlDef");
				}catch(Exception e){e.printStackTrace();}
				setDefault();
			}
		}
		
		return svdbToSql;
	}
	public void setDefault()
	{
		try
		{
			getSvdbToSqlIni().setKeyValue("web", "importToSQLDB","false");
			
			getSvdbToSqlIni().setKeyValue("web", "siteviewDS","com.mysql.jdbc.Driver,jdbc:mysql://localhost:6066/svecc,root,,false,mysql");
			getSvdbToSqlIni().setKeyValue("web", "svdbHistoryReadDay", "10");
			getSvdbToSqlIni().setKeyValue("web", "svdbDataInit", Toolkit.getToolkit().formatDate());
			getSvdbToSqlIni().setKeyValue("web", "svdbDataStart", Toolkit.getToolkit().formatDate(new Date(),"yyyy-01-01 00:00:00"));
			getSvdbToSqlIni().setKeyValue("web", "svdbDateReadMinute", "15");
			
			
			getSvdbToSqlIni().setKeyValue("sqlDef", "import.table", "create table IF NOT EXISTS :tableName(datatime char(19),monitorid varchar(30),status smallint,dstr text CHARACTER SET UTF8");
			getSvdbToSqlIni().setKeyValue("sqlDef", "import.table.end", "ENGINE = MYISAM");
			getSvdbToSqlIni().setKeyValue("sqlDef", "import.index", "CREATE INDEX :indexName ON :tableName(datatime,monitorid)");
			getSvdbToSqlIni().setKeyValue("sqlDef", "import.insert", "insert into :tableName(datatime,monitorid,status,dstr");
			
			getSvdbToSqlIni().setKeyValue("sqlDef", "import.int", "int");
			getSvdbToSqlIni().setKeyValue("sqlDef", "import.string", "varchar(250) CHARACTER SET UTF8");
			getSvdbToSqlIni().setKeyValue("sqlDef", "import.string.len", "250");
			getSvdbToSqlIni().setKeyValue("sqlDef", "import.float", "float");
			
			getSvdbToSqlIni().setKeyValue("sqlDef", "thread.PRIORITY", "1");
			getSvdbToSqlIni().setKeyValue("sqlDef", "mysql.tmp", "c:/tomysql.tmp/");
			
			getSvdbToSqlIni().saveChange();
			getSvdbToSqlIni().load();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
