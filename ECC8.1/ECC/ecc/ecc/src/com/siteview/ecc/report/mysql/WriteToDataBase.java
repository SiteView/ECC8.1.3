package com.siteview.ecc.report.mysql;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.focus.db.DBCon;
import com.focus.util.Util;
import com.siteview.base.tree.INode;
import com.siteview.ecc.util.Toolkit;
import com.siteview.jsvapi.MyBool;
import com.siteview.jsvapi.MyString;
import com.siteview.jsvapi.StringMap;
import com.siteview.jsvapi.SwigSvapi;

/*负责将数据写入到SQL关系数据库,这样节约了svdb读取下一笔数据的时间*/
public class WriteToDataBase extends Thread
{
	private final static Logger logger = Logger.getLogger(WriteToDataBase.class);
	private String mysqltmpFile;
	int okCount=0;
	int maxStringLen=250;
	List<ImportTask> taskPool=new ArrayList<ImportTask>(); 
	
	Map<String,String> pstmSqlMap;
	Map<String,List<String>> tableFieldMap;
	Map<String,List<String>> tableFieldTypeMap;
	
	Date   tmStart;
	Date   tmEnd;

	Util util=Util.getInstance();	
	boolean isRunning=true;
	
	boolean dataReadFinished=false;
	public WriteToDataBase(int maxStringLen,String mysqltmpFile,Date tmStart,Date tmEnd,Map<String,String> pstmSqlMap,Map<String,List<String>> tableFieldMap,Map<String,List<String>> tableFieldTypeMap)
	{
		super();
		this.tmStart=tmStart;
		this.tmEnd=tmEnd;

		this.pstmSqlMap=pstmSqlMap;
		this.tableFieldMap=tableFieldMap;
		this.tableFieldTypeMap=tableFieldTypeMap;
		this.mysqltmpFile=mysqltmpFile;
		this.maxStringLen=maxStringLen;
		this.setName(WriteToDataBase.class.getName());
		
	}
	
	private Map<String, String> StringMapToUtilMap(StringMap smap)
	{
		Map<String, String> ndata = new HashMap<String, String>();
		if (smap == null)
			return ndata;
		
		MyBool mb = new MyBool();
		MyString nextkey = new MyString();
		while (SwigSvapi.swig_SNextKey(smap, nextkey, mb))
		{
			String key = nextkey.getStr();
			ndata.put(key, smap.get(key));
		}
		return ndata;
	}
	public void writeToMySql(DBCon con,ImportTask task) throws Exception 
	{

		String toMysqlDataFile=this.mysqltmpFile+"ecc_"+task.tableName+"_"+task.monitorID+"_"+Toolkit.getToolkit().formatDate(this.tmStart,"yyyyMMddHHmmss")+"_"+Toolkit.getToolkit().formatDate(this.tmEnd,"yyyyMMddHHmmss")+".txt";
		
		 int idx = toMysqlDataFile.lastIndexOf("\\");
	     int idx2 = toMysqlDataFile.lastIndexOf("/");
	     if(idx < idx2)
	         idx = idx2;
	     String dir = toMysqlDataFile.substring(0, idx);
	     File fdir = new File(dir);
	     if(!fdir.exists())
	         fdir.mkdirs();
	     
		
	Toolkit toolkit=Toolkit.getToolkit();
	
	
	BufferedWriter out=null;
	FileOutputStream fos=null;
	OutputStreamWriter osw=null;
	try
	{
		fos=new FileOutputStream(toMysqlDataFile);
		osw=new OutputStreamWriter(fos);
		out=new BufferedWriter(osw);	
		List<String> fields=tableFieldMap.get(task.tableName);
		List<String> types=tableFieldTypeMap.get(task.tableName);

		long l=System.currentTimeMillis();

		StringBuffer sb=new StringBuffer();

		
			
		for (int index = 0; index < task.value.size(); ++index)
		{
			Map<String,String> map=StringMapToUtilMap(task.value.get(index));
			String time=map.get("creat_time");
			if(time==null)
				continue;
			
			sb.append("\"").append(time).append("\"\t"); 			/*取中间点时间代表区间*/
			sb.append("\"").append(task.monitorID).append("\"\t");	/*记录monitorid*/
			String status=map.get("record_status");
			sb.append(toolkit.changeStatusToInt(status)).append("\t");
			
			if(!INode.OK.equals(status))
			{
				String dstr=map.get("dstr"); 
				if(dstr!=null)
				{
					dstr=util.replace(dstr,"\"", "\\\"");
					dstr=util.replace(dstr,"\t", " ");
					dstr=util.replace(dstr,"\n", " ");
					//dstr=util.gbToUnicode(dstr);
					
					sb.append("\"").append(dstr).append("\"");
					
				}
				else
					sb.append("NULL");
			}
			
			for(int i=0;i<fields.size();i++) 
			{
					String val=map.remove(fields.get(i));
					if("null".equals(val))
						val=null;

					if(val==null)
						sb.append("\tNULL");

					else if(types.get(i).toLowerCase().equals("string"))
					{
						val=util.replace(val,"\"", "\\\"");
						val=util.replace(val,"\t", " ");
						val=util.replace(val,"\n", " ");
						val=intercept(val,maxStringLen);
						sb.append("\t\"").append(val).append("\"");
					}else 
					{
						sb.append("\t").append(val);
					}
			}
			
			sb.append("\n");
			
			out.write(sb.toString());
			sb=new StringBuffer();
			Thread.yield();
		}
		
		if(sb.length()>0)
			out.write(sb.toString());
		
		out.flush();
		//logger.info("准备提交:"+toMysqlDataFile);
		//util.writeFile(toMysqlDataFile, sb.toString());
		con.setSQL("LOAD DATA INFILE \""+toMysqlDataFile+"\" INTO TABLE ecc_"+task.tableName+" FIELDS TERMINATED BY '\t' OPTIONALLY ENCLOSED BY '\"' LINES TERMINATED BY '\n'");
		//con.setSQL("LOAD DATA INFILE \""+this.mysqltmpFile+"\" INTO TABLE ecc_"+task.tableName+" FIELDS TERMINATED BY 't' OPTIONALLY ENCLOSED BY '\"' LINES TERMINATED BY 'n' ESCAPED BY '\\'");
		//con.setSQL("LOAD DATA INFILE \""+this.mysqltmpFile+"\" INTO TABLE ecc_"+task.tableName);
		con.execute();
		//logger.info("本次提交花费时间:"+(System.currentTimeMillis()-l)+"mms,提交记录数:"+task.value.size());
	}catch(Exception e)
	{
		
		throw new Exception("写monitor数据失败:"+task.monitorID+":tableName=ecc_"+task.tableName+" insert error: ");
		
	}finally
	{
		if(out!=null)
			try{out.close();}catch(Exception e){}
		if(osw!=null)
			try{osw.close();}catch(Exception e){}
		if(fos!=null)
			try{fos.close();}catch(Exception e){}
			
	}
 
	}


	/**
	* 对UTF8编码的字符串按字节数进行截取，且保证截取后的字符串中没有乱码
	* @param src 原字符串
	* @param maxLength 截取的最大字节数
	* @return
	*/
	public  String intercept(String src,int maxLength) {
	String result = null;
	try {
	byte[] srcBytes = src.getBytes("utf8");
	if(srcBytes.length<=250)
		return src;
	//从截取后的最后一个字节开始判断
	int index = maxLength;
	//第一个0的位置为2，说明该字节为一个字符的编码的一部分
	while(getFirstZeroPosition(srcBytes[--index])==2);
	result = new String(srcBytes,0,index,"utf8");
	} catch (UnsupportedEncodingException e) {
	e.printStackTrace();
	}
	return result;
	}
	/**
	* 查找字节中第一个0的位置（从左往右）
	* @param src
	* @return
	*/
	public  byte getFirstZeroPosition(byte src) {
	byte index = 0;
	while(((src<<index++)&0x80)!=0);
	return index;
	} 
	private  String interceptKeyWord(String keyword, int maxLength,  String delimiter) {  
			            String subStr = null;  
			   
			            try {  
			                // unicode转码为utf8  
			                byte[] keywordCodes = keyword.getBytes("utf8");  
			                // utf8转码为unicode  
			                subStr = new String(keywordCodes, 0, maxLength - 1, "utf8");  
			   
			                if (subStr.lastIndexOf(delimiter) < 0) {  
			                   // 原字符串中不包含分隔符  
			                    return subStr;  
			                } else {  
			                    return subStr.substring(0,  
			                        subStr.lastIndexOf(delimiter, maxLength));  
			                }  
			            } catch (UnsupportedEncodingException e) {  
			             e.printStackTrace();  
			            }  
			              
			            return subStr;  
			        }  
	public void writeToSql(DBCon con,ImportTask task)
	{
		Toolkit toolkit=Toolkit.getToolkit();
		try
		{
		List<String> fields=tableFieldMap.get(task.tableName);
		List<String> types=tableFieldTypeMap.get(task.tableName);

		long l=System.currentTimeMillis();
		
		String sql=pstmSqlMap.get(task.tableName);
		con.setSQL(sql);
		PreparedStatement pst=con.prepareStatement(sql);
		
		
		
		for (int index = 0; index < task.value.size(); ++index)
		{
			Map<String,String> map=StringMapToUtilMap(task.value.get(index));
			
			Object time=map.get("creat_time");
			if(time==null)
				continue;
			
			pst.setObject(1, time); /*取中间点时间代表区间*/
			pst.setString(2, task.monitorID);/*记录monitorid*/
			Object status=map.get("record_status");
			pst.setInt(3,toolkit.changeStatusToInt(status.toString()) );	
			
			if(INode.OK.equals(status))
				pst.setNull(4,Types.VARCHAR);
			else
				pst.setObject(4, map.get("dstr"));
			
			int pointer=4;
			for(int i=0;i<fields.size();i++) 
			{
					String val=map.remove(fields.get(i));
					if("null".equals(val))
						val=null;
					
					if(types.get(i).toLowerCase().equals("float"))
					{
						if(val==null)
							pst.setNull(++pointer, Types.FLOAT);
						else	
							pst.setFloat(++pointer, Float.parseFloat(val));
					}else if(types.get(i).toLowerCase().equals("int"))
					{
						if(val==null)
							pst.setNull(++pointer, Types.INTEGER);
						else	
							pst.setInt(++pointer, Integer.parseInt(val));
					}else
					{
						if(val==null)
							pst.setNull(++pointer, Types.VARCHAR);
						else	
						{
							byte[] toBytes=val.getBytes();
							if(toBytes.length>250)
							{
								byte target[]=new byte[250];
								System.arraycopy(toBytes, 0, target, 0, 250);
								//logger.info("monitorid="+task.monitorID+",time="+time+" string is tool long ,change to "+new String(target));
								val=new String(target);
							}
							pst.setString(++pointer, val);
						}
					}
			}
			pst.addBatch();
			Thread.yield();
		}
		pst.executeBatch();
		logger.info("本次提交花费时间:"+(System.currentTimeMillis()-l)+"mms,提交记录数:"+task.value.size());
	}catch(Exception e)
	{
		
		logger.info("tableName=ecc_"+task.tableName+" insert error: ");
		e.printStackTrace();
		if(con!=null)
			con.rollback();
		
	}
 }
}
