// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   QueryResult.java

package com.focus.db;

import java.io.Serializable;
import java.util.ArrayList;

// Referenced classes of package com.focus.db:
//            Column, RecordRS

public class QueryResult
    implements Serializable
{

    public QueryResult()
    {
        isDirty = false;
        tableSrc = null;
        changeCode = false;
        all_numrow = -1;
        cols = new Column[0];
        DBType = null;
        pageIdx = null;
        recNumPerPage = null;
        totalPageNum = null;
    }

    public int size()
    {
        if(cols.length == 0)
            return 0;
        else
            return cols[0].value_vec.size();
    }

    public String getColType(int index)
    {
        return cols[index].getDataType();
    }

    public int getCol(String colName)
        throws Exception
    {
        for(int i = 0; i < cols.length; i++)
            if(cols[i].getFullName().equalsIgnoreCase(colName) || cols[i].getName().equalsIgnoreCase(colName))
                return i;

        throw new Exception("\u67E5\u8BE2\u4E2D\u672A\u6307\u5B9A:" + colName);
    }

    public Column[] getCols()
    {
        return cols;
    }

    public void setComment(String colname[], String comment[])
    {
        for(int i = 0; i < cols.length; i++)
        {
            for(int j = 0; j < colname.length; j++)
                if(cols[i].getName().equalsIgnoreCase(colname[j]))
                    cols[i].setComment(comment[j]);

        }

    }

    public Object getObject(int row_idx, int col_idx)
    {
        if(cols[col_idx].value_vec.size() == 0)
            return "";
        Object value = cols[col_idx].value_vec.get(row_idx);
        try
        {
            if(value == null)
                return "";
            if(value instanceof byte[])
                value = new String((byte[])value);
            if(value != null && changeCode && (value instanceof String))
                return new String(value.toString().trim().getBytes("8859_1"), "GBK");
            if(value != null && (value instanceof String))
                return value.toString().trim();
        }
        catch(Exception e)
        {
            return "";
        }
        return value;
    }

    public Object getObject(int row_idx, String colName)
        throws Exception
    {
        return getObject(row_idx, getCol(colName));
    }

    public void setObject(int row_idx, int col_idx, Object value)
    {
        cols[col_idx].value_vec.set(row_idx, value);
    }

    public void setObject(int row_idx, String colName, Object value)
        throws Exception
    {
        setObject(row_idx, getCol(colName), value);
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < cols.length; i++)
            sb.append(cols[i].getName()).append(",");

        return sb.toString();
    }

 
    boolean isDirty;
    String tableSrc[];
    public boolean changeCode;
    public int all_numrow;
    public Column cols[];
    public String DBType;
    public String pageIdx;
    public String recNumPerPage;
    public String totalPageNum;
}
