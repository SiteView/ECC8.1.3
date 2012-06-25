// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BuildSQLStmt.java

package com.focus.db;


public class BuildSQLStmt
{

    public BuildSQLStmt()
    {
    }

    public String getSQLStmt(String title, String content, String userid, boolean needContent)
    {
        String sql = null;
        if(!needContent)
            sql = "select bid,title from blog where ";
        else
            sql = "select bid,title,content from blog where ";
        if(userid != null && !userid.equals("-1000"))
            sql = sql + " userid=" + userid + " ";
        if(title != null && title.trim() != "")
            if(sql.trim().endsWith("where"))
                sql = sql + " title like '%" + title + "%'";
            else
                sql = sql + " and title like '%" + title + "%'";
        if(content != null && content.trim() != "")
            if(sql.trim().endsWith("where"))
                sql = sql + " content like '%" + content + "%'";
            else
                sql = sql + " and content like '%" + content + "%'";
        if(sql.trim().endsWith("where"))
            if(!needContent)
                sql = "select bid,title from blog";
            else
                sql = "select bid,title,content from blog";
        return sql;
    }
}
