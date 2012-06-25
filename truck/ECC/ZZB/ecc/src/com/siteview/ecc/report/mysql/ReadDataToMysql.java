package com.siteview.ecc.report.mysql;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import com.focus.db.Column;
import com.focus.db.DBCon;
import com.focus.db.DBConIni;
import com.focus.db.QueryResult;
import com.focus.util.Util;
import com.siteview.base.data.IniFile;
import com.siteview.ecc.start.EccStarter;
import com.siteview.ecc.start.StarterListener;
import com.siteview.ecc.util.Toolkit;
import com.siteview.jsvapi.ForestVector;
import com.siteview.jsvapi.StringMap;
import com.siteview.svdb.ForestData;
import com.siteview.svdb.UnivData;

public class ReadDataToMysql implements StarterListener,Runnable{


	int maxStringLen=250;
	int threadPRIORITY=-1;
	Date svdbDataStart=null;
	Date svdbDataInit=null;
	long svdbDateReadMinute=15L;
	long svdbHistoryReadDay=7L;
	public boolean importToSQLDB=false;
	
	Map<String,String> sqlDefMap=new HashMap<String,String>(); 
	
	ArrayList<String> tableNameArray=new ArrayList<String>(); 
	Map<String,String> pstmSqlMap=new HashMap<String,String>();
	Map<String,List<String>> tableFieldMap=new HashMap<String,List<String>>();
	Map<String,List<String>> tableFieldTypeMap=new HashMap<String,List<String>>();
	Map<String,Map<String,String>> tableFieldLabelMap=new HashMap<String,Map<String,String>>();
	
	Map<String, Map<String, String>> monitorInfo = null;	
	Map<String, InitInfoBean> initInfoMap = null;
	
	private String prefix="ecc_";
	private boolean index_created=false;
	
	
	/* 启动web的时候同步svdb
	 * 之后循环检查svdb中监测数据,并更新mysql统计数据
	 * */
	@Override
	public void run() 
	{
		if(!importToSQLDB)
			return;
		
		DBCon con=null;
		try
		{
			//启动创建表
			long l=System.currentTimeMillis();
			
			con=DBCon.getConnection("siteviewDS");
			
			con.setAutoCommit(false);
			
			autoCreateTable(con);
			
			con.commit();
			
			Toolkit.getToolkit().getLoger().log(Level.INFO,"初始化mysql,保持数据结构同svdb一致,花费时间:"+(System.currentTimeMillis()-l)/1000+"秒");
			
		}catch(Exception e)
		{
			if(con!=null)
				con.rollback();
			Toolkit.getToolkit().getLoger().log(Level.WARNING,"数据库同步程序未正常启动"+e.getMessage());
			return;
		}
		finally
		{
			if(con!=null)
				con.close();
		}

		try
		{
			readMonitorInfo();
			//周期检查并更新最新数据
			while(true)
			{
				long l=System.currentTimeMillis();
				
				Iterator iterator=monitorInfo.keySet().iterator();
				while(iterator.hasNext())
				{
					
					if(!importToSQLDB)
						return;

					
					String monitorid=iterator.next().toString();
					InitInfoBean initInfoBean=getInitInfoBean(monitorid);
					while(true)
					{
						if(!importToSQLDB)
							return;

						readData(initInfoBean);
						if(initInfoBean.getLastUpdateCount()==0&&initInfoBean.getDirection()==initInfoBean.DIRCTION_BACK)
						{
							finishMonitorHistory(initInfoBean);/*该monitor的历史数据导入过程结束*/
							
							if(initInfoBean.getAllUpdateCount()==0)
								Toolkit.getToolkit().getLoger().info("monitor:"+initInfoBean.getMonitorId()+"无数据");

							continue;
						}
						
						if(initInfoBean.getDirection()==initInfoBean.DIRCTION_FORWARD)
							break;
					}
					
				}
				
				//历史数据已经建立完全,现在开始检索表索引
				if(!index_created)
				{
					for(InitInfoBean initInfoBean:initInfoMap.values())
						createIndex(initInfoBean.getTableName());
					index_created=true;
				}

				Thread.currentThread().sleep(svdbDateReadMinute*60*1000);/*无最新记录，则等待svdbDateReadMinute分钟再检查*/
				readMonitorInfo();
			}
				
		}catch(Exception e)
		{
			e.printStackTrace();
		}

	}
	private void readMonitorInfo()
	{
		try 
		{
			monitorInfo=UnivData.queryInfo("sv_id,sv_monitortype,sv_name","monitor","default");
			Map<String,Map<String,String>> monitorLabelInfo=UnivData.queryAllMonitorInfo();
			Iterator iterator=monitorInfo.keySet().iterator();
			while(iterator.hasNext())
			{
				String monitorid=iterator.next().toString();
				Map<String,String> map=monitorInfo.get(monitorid);
				//map.put("OkConditon", monitorLabelInfo.get(monitorid).get("OkConditon"));
				//map.put("MonitorFrequency", monitorLabelInfo.get(monitorid).get("MonitorFrequency"));
				//map.put("GroupName", monitorLabelInfo.get(monitorid).get("GroupName"));
				//map.put("WarnConditon", monitorLabelInfo.get(monitorid).get("WarnConditon"));
				map.put("ErrorConditon", monitorLabelInfo.get(monitorid).get("ErrorConditon"));
				//map.put("MonitorType", monitorLabelInfo.get(monitorid).get("MonitorType"));
				//map.put("MonitorName", monitorLabelInfo.get(monitorid).get("MonitorName"));
			}
			
			//ForestData.getInstance().trace(monitorInfo);
		} catch (Exception e) {
			Toolkit.getToolkit().getLoger().log(Level.WARNING,"读取monitorID失败:" + e.getMessage());
		}

	}
	private void finishMonitorHistory(InitInfoBean initInfoBean) throws Exception
	{
		DBCon con=null;
		try
		{
			con=DBCon.getConnection("siteviewDS");
			con.setSQL("update ecc_initinfo set startTime=:time,readDirection='1' where monitorid=:monitorid");
			con.setParameter("monitorid", initInfoBean.getMonitorId());
			con.setParameter("time", Toolkit.getToolkit().formatDate(this.svdbDataStart));
			con.execute();
		}
		finally
		{
			if(con!=null)
				con.close();
		}
		
		initInfoBean.setDirection(InitInfoBean.DIRCTION_FORWARD);
		initInfoBean.setStartTime(this.svdbDataStart);
		//createIndex(initInfoBean.getTableName());
		
		Toolkit.getToolkit().getLoger().log(Level.INFO,"完成monitor:"+initInfoBean.getMonitorId()+"历史数据导入ecc_"+initInfoBean.getTableName());

	}
	private Map<String, InitInfoBean> getInitInfoMap()
	{
		if(initInfoMap!=null)
			return initInfoMap;
		
		initInfoMap=new HashMap<String, InitInfoBean>();
		DBCon con=null;
		try
		{
			
			try
			{
				con=DBCon.getConnection("siteviewDS");
			}catch(Exception e)
			{
				return null;
			}
			
			con.setSQL("select monitorid,startTime,endTime,readDirection from ecc_initinfo");
			QueryResult rs=con.query();

			if(rs.size()==0)
			{
				String dateStr=Toolkit.getToolkit().formatDate(svdbDataInit);
				Iterator iterator=monitorInfo.keySet().iterator();
				while(iterator.hasNext())
				{
					String monitorid=iterator.next().toString();
					String tableName=monitorInfo.get(monitorid).get("sv_monitortype");
					con.setSQL("insert into ecc_initinfo(monitorid,startTime,endTime,readDirection) values(:monitorid,:startTime,:endTime,'0')");
					con.setParameter("monitorid",monitorid);
					con.setParameter("startTime",dateStr);
					con.setParameter("endTime",dateStr);
					con.execute();
					InitInfoBean initInfoBean=new InitInfoBean(tableName,monitorid,svdbDataInit,svdbDataInit,InitInfoBean.DIRCTION_BACK);
					initInfoMap.put(monitorid,initInfoBean);
					Thread.yield();
				}
			}
			else
			{
				for(int i=0;i<rs.size();i++)
				{
					String monitorid=rs.getObject(i,"monitorid").toString();
					Map<String,String> info=monitorInfo.get(monitorid);
					if(info==null)
						continue;
					
					Date startTime=Toolkit.getToolkit().parseDate(rs.getObject(0,"startTime").toString());
					Date endTime=Toolkit.getToolkit().parseDate(rs.getObject(0,"endTime").toString());
					int direction=Integer.parseInt(rs.getObject(i,"readDirection").toString());
					String tableName=info.get("sv_monitortype");
					
					InitInfoBean initInfoBean=new InitInfoBean(tableName,monitorid,startTime,endTime,direction);
					initInfoMap.put(monitorid,initInfoBean);
					Thread.yield();
				}
			}
			
			return initInfoMap;
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(con!=null)
				con.close();
		}
		return null;
	}
	private int getPRIORITY()
	{
		if(threadPRIORITY==-1)
		{
			String p=sqlDefMap.get("thread.PRIORITY");
			if(p.trim().equals("0"))
				threadPRIORITY=Thread.MIN_PRIORITY;
			else if(p.trim().equals("1"))
				threadPRIORITY=Thread.NORM_PRIORITY;
			else
				threadPRIORITY=Thread.MAX_PRIORITY;
		}
		return threadPRIORITY;
		
	}
	private InitInfoBean getInitInfoBean(String monitorid)
	{
		InitInfoBean bean=getInitInfoMap().get(monitorid);
		if(bean!=null)
			return bean;
		
		DBCon con=null;
		try
		{
			String dateStr=Toolkit.getToolkit().formatDate(svdbDataInit);
			con=DBCon.getConnection("siteviewDS");
			con.setSQL("insert into ecc_initinfo(monitorid,startTime,endTime,readDirection) values(:monitorid,:startTime,:endTime,'1')");
			con.setParameter("monitorid",monitorid);
			con.setParameter("startTime",dateStr);
			con.setParameter("endTime",dateStr);
			con.execute();
			String tableName=monitorInfo.get(monitorid).get("sv_monitortype");
			bean=new InitInfoBean(tableName,monitorid,svdbDataInit,svdbDataInit,InitInfoBean.DIRCTION_FORWARD);
			initInfoMap.put(monitorid,bean);
			return bean;
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{
			if(con!=null)
				con.close();
		}
		
		
		return null;
		
	}
	private InitInfoBean readData(InitInfoBean initInfoBean) throws Exception
	{
		DBCon con=null;
		try{
			
			con=DBCon.getConnection("siteviewDS");
			con.setAutoCommit(false);
			
			
		if(initInfoBean.getDirection()==InitInfoBean.DIRCTION_BACK) 
		{
			
			Date newStartTime=Toolkit.getToolkit().delTime(initInfoBean.getStartTime(),(svdbHistoryReadDay*24*60*60*1000));/*每次读svdbHistoryDateReadMinute分钟历史数据*/

			List<ImportTask> taskPool=new ArrayList<ImportTask>();
			
			//String sql="update ecc_initinfo set startTime='"+Toolkit.getToolkit().formatDate(newStartTime)+"'";
			WriteToDataBase writer=new WriteToDataBase(maxStringLen,sqlDefMap.get("mysql.tmp"),newStartTime,initInfoBean.getStartTime(),pstmSqlMap,tableFieldMap,tableFieldTypeMap);
			
			//writeThread.start();不启动线程
			
			initInfoBean.setLastUpdateCount(querySvdb(con,initInfoBean,writer,newStartTime,initInfoBean.getStartTime()));
			
			
			if(initInfoBean.getLastUpdateCount()>0)
			{
				con.setSQL("update ecc_initinfo set startTime=:time where monitorid=:monitorid");
				con.setParameter("monitorid", initInfoBean.getMonitorId());
				con.setParameter("time", Toolkit.getToolkit().formatDate(newStartTime));
				con.execute();
				con.commit();

				//Toolkit.getToolkit().getLoger().log(Level.INFO,"*******************"+initInfoBean.getMonitorId()+"成功导入ecc_"+initInfoBean.getTableName()+" 历史数据("+initInfoBean.getLastUpdateCount()+")条************"+Toolkit.getToolkit().formatDate(newStartTime)+"~"+Toolkit.getToolkit().formatDate(initInfoBean.getStartTime()));
				
				initInfoBean.setStartTime(newStartTime);
			}
		}			
		else{
				
				Date newEndTime=new Date();			/*每次读到当前时间*/
				//String sql="update ecc_initinfo set endTime='"++"'";
				WriteToDataBase writer=new WriteToDataBase(maxStringLen,sqlDefMap.get("mysql.tmp"),initInfoBean.getEndTime(),newEndTime,pstmSqlMap,tableFieldMap,tableFieldTypeMap);
				
				initInfoBean.setLastUpdateCount(querySvdb(con,initInfoBean,writer,initInfoBean.getEndTime(),newEndTime));
				
				if(initInfoBean.getLastUpdateCount()>0)
				{
					con.setSQL("update ecc_initinfo set endTime=:time where monitorid=:monitorid");
					con.setParameter("monitorid", initInfoBean.getMonitorId());
					con.setParameter("time", Toolkit.getToolkit().formatDate(newEndTime));
					con.execute();
					con.commit();
					//Toolkit.getToolkit().getLoger().log(Level.INFO,"*******************成功导入ecc_"+monitorInfo.get(initInfoBean.getMonitorId()).get("sv_monitortype")+" 最新数据("+initInfoBean.getLastUpdateCount()+")条************"+Toolkit.getToolkit().formatDate(initInfoBean.getEndTime())+"~"+Toolkit.getToolkit().formatDate(newEndTime));
					initInfoBean.setEndTime(newEndTime);
				}
			}
		

		
		}catch(Exception e)
		{
			if(con!=null)
				con.rollback();
			throw new Exception(e.getMessage());
		}
		finally
		{
			if(con!=null)
				con.close();
		}
		return initInfoBean;

	}

	private void createIndex(String tableName)
	{
		DBCon con=null;
		try
		{
			con=DBCon.getConnection("siteviewDS");
			
			Toolkit.getToolkit().getLoger().log(Level.INFO,"数据导入完毕,尝试为ecc_"+tableName+"建立索引.");
			
				//con.setSQL("CREATE INDEX idx_"+tableName+" ON ecc_"+tableName+"(datatime,monitorid)");
			String sql=sqlDefMap.get("import.index");
			sql=Util.getInstance().replace(sql, ":indexName", "idx_"+tableName);
			sql=Util.getInstance().replace(sql, ":tableName", "ecc_"+tableName);
			con.setSQL(sql);
			con.execute();

			//Toolkit.getToolkit().getLoger().log(Level.INFO,"建立索引完毕,导数程序进入休眠.");
			
		}catch(Exception e)
		{
			//Toolkit.getToolkit().getLoger().log(Level.WARNING,e.getMessage());
		}
		finally
		{
			if(con!=null)
				con.close();
		}
	}
	private boolean exsistMysqlTmpFile(DBCon con,String tablename,String monitorid,Date tmStart,Date tmEnd) throws Exception
	{
		String toMysqlDataFile=sqlDefMap.get("mysql.tmp")+"ecc_"+tablename+"_"+monitorid+"_"+Toolkit.getToolkit().formatDate(tmStart,"yyyyMMddHHmmss")+"_"+Toolkit.getToolkit().formatDate(tmEnd,"yyyyMMddHHmmss")+".txt";
		File file=new File(toMysqlDataFile);
		if(file.exists())
		{
			con.setSQL("LOAD DATA INFILE \""+toMysqlDataFile+"\" INTO TABLE ecc_"+tablename+" FIELDS TERMINATED BY '\t' OPTIONALLY ENCLOSED BY '\"' LINES TERMINATED BY '\n'");
			con.execute();
			Toolkit.getToolkit().getLoger().log(Level.INFO,"*******************从存在的数据文件导入ecc_"+tablename+"************"+toMysqlDataFile);
		}
		return true;
	}
	
	private long querySvdb(DBCon con,InitInfoBean initInfoBean,WriteToDataBase writeThread,Date tmStart,Date tmEnd) throws Exception
	{
		
		//if(con.getDBType().equals("mysql"))
		//if(exsistMysqlTmpFile(con,tableName,monitorid,tmStart,tmEnd))
		//	return Integer.MAX_VALUE;不必要节约1次文件的时间
		
		
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String begin_year = new SimpleDateFormat("yyyy").format(tmStart);
		String begin_month = new SimpleDateFormat("MM").format(tmStart);
		String begin_day = new SimpleDateFormat("dd").format(tmStart);
		String begin_hour = new SimpleDateFormat("HH").format(tmStart);
		String begin_minute = new SimpleDateFormat("mm").format(tmStart);
		String begin_second = new SimpleDateFormat("ss").format(tmStart);

		String end_year = new SimpleDateFormat("yyyy").format(tmEnd);
		String end_month = new SimpleDateFormat("MM").format(tmEnd);
		String end_day = new SimpleDateFormat("dd").format(tmEnd);
		String end_hour = new SimpleDateFormat("HH").format(tmEnd);
		String end_minute = new SimpleDateFormat("mm").format(tmEnd);
		String end_second = new SimpleDateFormat("ss").format(tmEnd);
		
		
		long l=System.currentTimeMillis();		
		
		
				
		
		ForestVector value=null; 
		try
		{
			StringMap param = new StringMap();
				
			 param.set("dowhat","QueryRecordsByTime");
			 param.set("compress", "false");
			 param.set("dstrNeed", "true");
			 param.set("dstrStatusNoNeed", "null,ok,disable,bad");
			 param.set("return_value_filter","");
			 long l2=System.currentTimeMillis();
			 value = ForestData
					.getInstance().queryRecordsByTimeFast(param,initInfoBean.getMonitorId(), begin_year,
							begin_month, begin_day, begin_hour,
							begin_minute, begin_second, end_year,
							end_month, end_day, end_hour, end_minute,
							end_second);
			
		} catch (Exception e) {
					throw new Exception("读monitor历史数据失败:"+initInfoBean.getMonitorId()+":"+begin_year+"-"+begin_month+"-"+begin_day+" "+begin_hour+":"+begin_minute+":"+begin_second+"~"+end_year+"-"+end_month+"-"+end_day+" "+end_hour+":"+end_minute+":"+end_second+ e.getMessage());
				}
				
		long count=value.size();
		if(count<=0)
		{
			//if(initInfoBean.getDirection()==InitInfoBean.DIRCTION_BACK)
			//	Toolkit.getToolkit().getLoger().info("monitor:"+initInfoBean.getMonitorId()+"无数据"+Toolkit.getToolkit().formatDate(tmStart)+"~"+Toolkit.getToolkit().formatDate(tmEnd));
			return 0;
		}

		if(con.getDBType().toLowerCase().equals("mysql"))
			writeThread.writeToMySql(con,new ImportTask(initInfoBean.getTableName(),value,initInfoBean.getMonitorId()));
		else	
			writeThread.writeToSql(con,new ImportTask(initInfoBean.getTableName(),value,initInfoBean.getMonitorId()));
				
		return count;
	}

	private void autoCreateTable(DBCon con)
	{

		try
		{
			con.setSQL("create table IF NOT EXISTS ecc_initinfo(monitorid varchar(30),startTime char(19),endTime char(19),readDirection char(1),INDEX(monitorid))");
			con.execute();
			//con.setSQL("CREATE INDEX idx_initinfo ON ecc_initinfo(monitorid)");
			//con.execute();
			
			// 读取模板列表信息
			Map<String, Map<String, String>> map = UnivData.getAllMonitorTempletInfo();
			map.remove("return");
			map.remove("monitors");

			 
			for (Object tableName : map.keySet().toArray()) 
			{
				tableNameArray.add(tableName.toString());
				
				StringBuffer pstmSql=new StringBuffer();
				StringBuffer valuesSql=new StringBuffer();
				
				
				//logger.info("表名:" + tableName + ",表中文名:"+ map.get(tableName).get("sv_label"));
				Map<String, Map<String, String>> info = null;
				try 
				{
					//读取模板详细信息
					info = UnivData.getMonitorTemplet(tableName.toString());

				} catch (Exception e) {
					System.err.println(tableName + "="+ map.get(tableName).get("sv_label")+ " load error:" + e.getMessage());
					e.printStackTrace();
					if (info==null) continue;
				}
				
				
				// 创建
					StringBuffer sql=new StringBuffer();
					//sql.append("create table IF NOT EXISTS ecc_").append(tableName).append("(datatime DATETIME,monitorid varchar(30),status smallint,dstr text");
					sql.append(Util.getInstance().replace(sqlDefMap.get("import.table"),":tableName","ecc_"+tableName));
					//pstmSql.append("insert into ecc_").append(tableName).append("(datatime,monitorid,status,dstr");
					pstmSql.append(Util.getInstance().replace(sqlDefMap.get("import.insert"),":tableName","ecc_"+tableName));
					valuesSql.append(")values(?,?,?,?");
					
					ArrayList<String> fieldList=new ArrayList<String>();
					ArrayList<String> fieldTypeList=new ArrayList<String>();
					for (int i = 1;; i++) 
					{
						Map<String, String> returnMap = info.get("ReturnItem_"	+ i);
						if (returnMap == null)
							break;
	
						String fieldName = returnMap.get("sv_name");
						String fieldType = returnMap.get("sv_type");
						String fieldLabel=  returnMap.get("sv_label");
						
						Map<String,String> labelMap=tableFieldLabelMap.get(tableName);
						if(labelMap==null)
						{
							labelMap=new HashMap<String,String>();
							tableFieldLabelMap.put(tableName.toString(), labelMap);
						}
						labelMap.put(fieldName, fieldLabel);
						
						//if(fieldType.toLowerCase().equals("string"))
						//	continue;
						
						sql.append(",ecc_").append(fieldName).append(" ").append(getSqlType(fieldType));
						//sql.append(",max_").append(fieldName).append(" ").append(getSqlType(fieldType));
						
						pstmSql.append(",ecc_").append(fieldName);
						//pstmSql.append(",ecc_").append(fieldName).append(",max_").append(fieldName);
						valuesSql.append(",?");
						fieldList.add(fieldName);
						fieldTypeList.add(fieldType);
					}
	
					//sql.append(",index(datatime,monitorid))");暂时不建立索引
					sql.append(")").append(sqlDefMap.get("import.table.end"));
					con.setSQL(sql.toString());
					con.execute();
					
					
					pstmSql.append(valuesSql).append(")");
					pstmSqlMap.put(tableName.toString(),pstmSql.toString());
					tableFieldMap.put(tableName.toString(), fieldList);
					tableFieldTypeMap.put(tableName.toString(), fieldTypeList);
					
					
					/*
					String sqlIndex=sqlDefMap.get("import.index");
					sqlIndex=Util.getInstance().replace(sqlIndex, ":indexName", "idx_"+tableName);
					sqlIndex=Util.getInstance().replace(sqlIndex, ":tableName", "ecc_"+tableName);
					con.setSQL(sqlIndex);
					try{con.execute();}catch(Exception e){}
					*/
				// 更改
				ArrayList<Column> columnArray=con.getcolumns("ecc_"+tableName);
				ArrayList<String> fieldNames=getNameArray(columnArray);
				ArrayList<String> validFieldArray=new ArrayList<String> (); 
				for (int i = 1;; i++) 
				{
					Map<String, String> returnMap = info.get("ReturnItem_"	+ i);
					if (returnMap == null)
							break;

						String fieldName = returnMap.get("sv_name");
						String fieldType = returnMap.get("sv_type");
						String fieldLabel=  returnMap.get("sv_label");
						
						//if(fieldType.toLowerCase().equals("string"))
						//	continue;
						
						validFieldArray.add("ecc_"+fieldName.toLowerCase());
						
						if(!fieldNames.contains("ecc_"+fieldName.toLowerCase()))
						{
								con.setSQL("alter table ecc_"+tableName+" add column ecc_"+fieldName+" "+getSqlType(fieldType));
								con.execute();
								//con.setSQL("alter table ecc_"+tableName+" add column max_"+fieldName+" "+getSqlType(fieldType));
								//con.execute();
						}else
						{
							if(!isSameColumn(getSqlType(fieldType),"ecc_"+fieldName,columnArray))
							{
								con.setSQL("alter table ecc_"+tableName+" modify column ecc_"+fieldName+" "+getSqlType(fieldType));
								con.execute();
								//con.setSQL("alter table ecc_"+tableName+" modify column max_"+fieldName+" "+getSqlType(fieldType));
								//con.execute();
							}
						}
				}
				
				//删除
				
				for(String inTable:fieldNames)
					if(inTable.toLowerCase().startsWith("ecc_"))
					if(!inTable.toLowerCase().equals("datatime")&&!inTable.toLowerCase().equals("monitorid")&&!inTable.toLowerCase().equals("status")&&!inTable.toLowerCase().equals("dstr"))
						if(!validFieldArray.contains(inTable.toLowerCase()))
						{
							con.setSQL("alter table ecc_"+tableName+" drop column "+inTable);
							con.execute();
							//con.setSQL("alter table ecc_"+tableName+" drop column max_"+inTable);
							//con.execute();
						}


			}
			
			// UnivData.trace(map);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		
	}
	private boolean isSameColumn(String type,String filed,ArrayList<Column> columnArray)
	{
		String colType;
		for(Column col:columnArray)
		{
			if(col.getName().toLowerCase().equals(filed.toLowerCase()))
			{
				colType=col.getDataType().toLowerCase();
				if(colType.equals("decimal"))
					colType="int";
				if(type.toLowerCase().startsWith(colType))
					return true;
				else
					return false;
			}
		}
		return false;
	}
	private ArrayList<String> getNameArray( ArrayList<Column> column)
	{
		ArrayList<String> names=new ArrayList<String>();
		for(Column col:column)
			names.add(col.getName().toLowerCase());
		return names;
	}
	private String getSqlType(String svdbType)
	{
		if(svdbType.equals("Int"))
			return sqlDefMap.get("import.int");
		else if(svdbType.equals("Float"))
			return sqlDefMap.get("import.float");
		else
			return sqlDefMap.get("import.string");
 
	}
	@Override
	public void destroyed(EccStarter starter) {
	}
	@Override
	public void startInit(EccStarter starter) 
	{
		try
		{
			DBCon.debug_sql=Boolean.parseBoolean(starter.getInitParameter("debug_sql"));
			reloadIni();
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static ReadDataToMysql getInstance() {

		return (ReadDataToMysql) EccStarter.getInstance()
		.getStarterListener("ReadDataToMysql");
	}
	public void reloadIni() throws Exception
	{
			IniFile ini=ImportIni.getInstance().getSvdbToSqlIni();
			Map<String,String> webMap=ini.getSectionData("web");
			
			
			Map<String,String> sqlProp=ini.getSectionData("sqlDef");
			for (Object key : sqlProp.keySet())
				sqlDefMap.put(key.toString().trim(), sqlProp.get(key).toString().trim());
	
			this.maxStringLen=Integer.parseInt(this.sqlDefMap.get("import.string.len"));
			this.svdbDataStart=Toolkit.getToolkit().parseDate(webMap.get("svdbDataStart"));
		
			String initTime=webMap.get("svdbDataInit");
			if(initTime.equals("now"))
				this.svdbDataInit=new Date();
			else
				this.svdbDataInit=Toolkit.getToolkit().parseDate(initTime);
		
			this.svdbDateReadMinute=Long.parseLong(webMap.get("svdbDateReadMinute"));
			this.svdbHistoryReadDay=Long.parseLong(webMap.get("svdbHistoryReadDay"));

			try
			{
				DBConIni.getInstance().addDataSource("siteviewDS",webMap.get("siteviewDS"));
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			
			
			if(webMap.get("importToSQLDB").equals("true"))
			{
				if(this.importToSQLDB==false)
				{
					this.importToSQLDB=true;
					Thread copyThread=new Thread(this);
					copyThread.setName("ReadDataToMysql -- ReadDataToMysql.java");
					copyThread.setPriority(getPRIORITY());
					copyThread.start();
				}
			}else
			{
				this.importToSQLDB=false;
			}
	}
	public String getTableName(String monitorid)
	{
		return monitorInfo.get(monitorid).get("sv_monitortype");
		
	}
	public String getMonitorName(String monitorid)
	{
		return monitorInfo.get(monitorid).get("sv_name");
	}
	public String getErrorCondition(String monitorid)
	{
		return monitorInfo.get(monitorid).get("ErrorConditon");
	}
	
}
