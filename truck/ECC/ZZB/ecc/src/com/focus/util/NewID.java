// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   NewID.java

package com.focus.util;

import com.focus.db.DBCon;
import com.focus.db.QueryResult;

// Referenced classes of package com.focus.util:
//            Util

public class NewID
{

    public NewID()
    {
    }

    public String HTMLEncode(String Str)
    {
        Str = Util.getInstance().replace(Str, "<", "&lt;");
        Str = Util.getInstance().replace(Str, ">", "&gt;");
        Str = Util.getInstance().replace(Str, " ", "&nbsp;");
        Str = Util.getInstance().replace(Str, "\n", "<br>");
        return Str;
    }

    public int getNewID(String tbl)
    {
        int intRet;
        DBCon con = null;
        String sql = "";
        QueryResult rsID = null;
        intRet = 0;
        try
        {
            con = DBCon.getConnection("portalDS");
            sql = "select max(id)+1 max from " + tbl;
            con.setSQL(sql);
            rsID = con.query();
            if(rsID != null && rsID.size() > 0 && rsID.getObject(0, 0) != null && !rsID.getObject(0, 0).toString().trim().equals(""))
                intRet = Integer.parseInt(rsID.getObject(0, 0).toString());
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            if(con != null)
                con.close();
        }
        return intRet;
    }
}
