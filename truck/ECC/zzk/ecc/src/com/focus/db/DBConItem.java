// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DBConItem.java

package com.focus.db;

import java.io.Serializable;
import java.sql.*;
import java.util.HashMap;
import java.util.ArrayList;

// Referenced classes of package com.focus.db:
//            Column

public class DBConItem
    implements Serializable
{

    public static Column[] getTableColumns(Connection con, String schema, String tableName)
        throws Exception
    {
        ArrayList v;
        v = new ArrayList();
        ResultSet rs = null;
        try
        {
            DatabaseMetaData dmd = con.getMetaData();
            Column c;
            for(rs = dmd.getColumns(null, schema, tableName, null); rs.next(); v.add(c))
            {
                java.sql.ResultSetMetaData rsm = rs.getMetaData();
                String str = rs.getString("COLUMN_NAME").trim();
                c = new Column();
                c.setName(str.toUpperCase());
                c.setTableName(tableName.toUpperCase());
                str = rs.getString("TYPE_NAME").trim();
                c.setDataType(str.toUpperCase());
                c.setIsNullable(rs.getString("IS_NULLABLE").equals("YES"));
                c.setLength(rs.getInt("COLUMN_SIZE"));
            }

        }
        finally
        {
            try
            {
                rs.close();
            }
            catch(Exception exception1) { }
        }
        if(v.size() == 0)
        {
            return null;
        } else
        {
            Column columns[] = new Column[v.size()];
            v.toArray(columns);
            return columns;
        }
    }

    public void addView(String viewName, String Sql)
    {
        if(views == null)
            views = new HashMap();
        views.put(viewName, Sql);
    }

    public String getView(String viewName)
    {
        return (String)views.get(viewName);
    }

    public void moveView(String viewName)
    {
        views.remove(viewName);
    }

    public DBConItem(String alias, String DriverName, String url, String user, String password, boolean changeCode, String dbType)
    {
        DBType = null;
        this.user = null;
        this.password = null;
        tables = null;
        views = null;
        this.changeCode = false;
        driverName = null;
        dsn = null;
        this.url = null;
        dsn = alias;
        driverName = DriverName;
        this.url = url;
        this.user = user;
        this.password = password;
        this.changeCode = changeCode;
        DBType = dbType;
    }

    public String DBType;
    public String user;
    public String password;
    HashMap tables;
    HashMap views;
    public boolean changeCode;
    public String driverName;
    public String dsn;
    public String url;
}
