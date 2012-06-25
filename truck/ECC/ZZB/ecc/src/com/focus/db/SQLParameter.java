// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SQLParameter.java

package com.focus.db;


public class SQLParameter
{

    public SQLParameter(String name, Object value, int pos)
    {
        this.pos = -999;
        this.name = name;
        this.value = value;
        this.pos = pos;
    }

    public String toString()
    {
        return (new StringBuffer(pos)).append(".").append(name).append("=").append(value).toString();
    }

    String name;
    Object value;
    int pos;
}
