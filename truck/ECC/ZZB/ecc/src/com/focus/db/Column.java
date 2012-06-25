// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Column.java

package com.focus.db;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;

public class Column
    implements Serializable
{

    public Column()
    {
        tableName = null;
        name = null;
        comment = null;
        dataType = null;
        length = 0;
        isNullable = true;
        isCheck = false;
        dispLength = 0;
        DSN = null;
        isPk = false;
        isReadOnly = false;
        scale = 0;
        value_vec = new ArrayList();
    }

    public Column cloneMe()
    {
        Column c = new Column();
        c.setTableName(getTableName());
        c.setName(getName());
        c.setComment(getComment());
        c.setDataType(getDataType());
        c.setIsNullable(isNullable());
        c.setLength(getLength());
        return c;
    }

    public String getComment()
    {
        return comment;
    }

    public String getDataType()
    {
        return dataType;
    }

    public String getFullName()
    {
        if(tableName.trim().equals(""))
            return name;
        else
            return "" + tableName + "." + name;
    }

    public int getLength()
    {
        return length;
    }

    public String getName()
    {
        return name;
    }

    public String getTableName()
    {
        return tableName;
    }

    public boolean isNullable()
    {
        return isNullable;
    }

    public void setCheckValue(boolean b)
    {
        isCheck = b;
    }

    public void setComment(String n)
    {
        comment = n;
    }

    public void setDataType(String dataType)
    {
        this.dataType = dataType;
    }

    public void setIsNullable(boolean b)
    {
        isNullable = b;
    }

    public void setLength(int l)
    {
        length = l;
    }

    public void setName(String n)
    {
        if(n != null)
            name = n;
    }

    public void setTableName(String tableName)
    {
        if(tableName != null)
            this.tableName = tableName.toUpperCase();
    }

    public String toString()
    {
        return "" + getTableName() + "." + getName();
    }

    public void checkValue(int i)
        throws SQLException
    {
    }

    public int getDispLength()
    {
        return dispLength;
    }

    public String getDSN()
    {
        return DSN;
    }

    public int getScale()
    {
        return scale;
    }

    public Object getValue(int row)
    {
        Object value = value_vec.get(row);
        if(value == null)
            return null;
        if(value instanceof String)
            return value.toString().trim();
        else
            return value;
    }

    public boolean isPk()
    {
        return isPk;
    }

    public boolean isReadOnly()
    {
        return isReadOnly;
    }

    public void setDispLength(int length)
    {
        dispLength = length;
    }

    public void setDSN(String DSN)
    {
        this.DSN = DSN;
    }

    public void setIsPk(boolean isPk)
    {
        this.isPk = isPk;
    }

    public void setIsReadOnly(boolean isReadOnly)
    {
        this.isReadOnly = isReadOnly;
    }

    public void setScale(int scale)
    {
        this.scale = scale;
    }

    public void setValue(int row, Object obj)
        throws SQLException
    {
        value_vec.set(row, obj);
        if(isCheck)
            checkValue(row);
    }

    private String tableName;
    private String name;
    private String comment;
    private String dataType;
    private int length;
    private boolean isNullable;
    boolean isCheck;
    private int dispLength;
    private String DSN;
    private boolean isPk;
    private boolean isReadOnly;
    private int scale;
    public ArrayList value_vec;
}
