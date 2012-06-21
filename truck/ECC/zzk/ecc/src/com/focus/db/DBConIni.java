// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DBConIni.java

package com.focus.db;

import com.focus.util.Util;
import java.io.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

// Referenced classes of package com.focus.db:
//            DBConItem, DBCon, QueryResult

public class DBConIni
    implements Serializable
{
	
    static HashMap<String,DBConItem> dbConItempool =new HashMap<String,DBConItem>();
    static HashMap<String,ArrayList<DBCon>> dBConPool=new HashMap<String,ArrayList<DBCon>>();

	
	public static DBConIni getInstance()
	{
		return new DBConIni();
	}

    public DBConIni()
    {
    }
    public DBConItem getDBConItem(String DSN)
    {
    	return dbConItempool.get(DSN);
    }
    public  void addDataSource(String DSN,String defStr)
        throws SQLException
    {

        
    	DriverManager.setLoginTimeout(5);
        try
        {
            String[] st = defStr.split(",");
            if(st.length != 6)
            {
                throw new SQLException("\u6570\u636E\u5E93\u8FDE\u63A5\u914D\u7F6E\u9519\u8BEF,\u683C\u5F0F\u9519\u8BEF:" + DSN);
            } else
            {
                DBConItem item = new DBConItem(DSN, st[0], st[1], st[2], st[3], Boolean.parseBoolean(st[4]), st[5]);
                Class.forName(item.driverName).newInstance();
                dbConItempool.put(DSN, item);
            }
        }
        catch(Exception e)
        {
            throw new SQLException("\u6CA1\u6709\u627E\u5230\u6570\u636E\u6E90\u914D\u7F6E\u7684\u76F8\u5173\u4FE1\u606F," + e.getMessage());
        }
    }


    public DBCon getConnection(String DSN)
    throws SQLException
    {
    
    try
    {
        DBConItem item = getDBConItem(DSN);
        DBCon con = null;

        ArrayList<DBCon> list = (ArrayList<DBCon>)dBConPool.get(DSN);
        if(list == null)
        {
        	list=new ArrayList<DBCon>();
        	dBConPool.put(DSN, list);
            con = new DBCon(DriverManager.getConnection(item.url, item.user, item.password));
        } else
         {
        		
                while(list.size()>0)
                {
                    con = (DBCon)list.remove(0);
                    if(!con.isClosed())
                        break;
                    con = null;
                }

                if(con == null)
                    con = new DBCon(DriverManager.getConnection(item.url, item.user, item.password));
            }

        con.DSN = DSN;
        con.DBType = item.DBType;
        con.changeCode = item.changeCode;
        con.schema = item.user.toUpperCase();
        return con;
    }
    catch(Exception e)
    {
        throw new SQLException(e.getMessage());
    }
}

    public void closeByConfig(DBCon con)
    {
        try
        {
        	con.setAutoCommit(true);
        	ArrayList<DBCon> list = (ArrayList<DBCon>)dBConPool.get(con.DSN);
            if(!list.contains(con))
            	list.add(con);
        }
        catch(Exception e)
        {
        	con.realClose();
        }
    }
}
