// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   XMLCreator.java

package com.focus.db;

import com.focus.util.DateTool;
import com.focus.util.Util;
import java.sql.Timestamp;

// Referenced classes of package com.focus.db:
//            QueryResult, Column

public class XMLCreator
{

	public static XMLCreator getInstrance()
	{
		return new XMLCreator();
	}
    public XMLCreator()
    {
    }

	public XMLCreator(QueryResult rs)
    {
        this.rs = rs;
    }

    public String decode(String s)
    {
    	Util util=Util.getInstance();
        s = util.getInstance().replace(s, "%3C", "<");
        s = util.replace(s, "%3E", ">");
        s = util.replace(s, "%40", "@");
        s = util.replace(s, "%24", "$");
        s = util.replace(s, "%25", "%");
        return s;
    }

    public String encode(String s)
    {
    	Util util=Util.getInstance();
        s = util.replace(s, "%", "%25");
        s = util.replace(s, "@", "%40");
        s = util.replace(s, "$", "%24");
        s = util.replace(s, "<", "%3C");
        s = util.replace(s, ">", "%3E");
        return s;
    }

    public StringBuffer ge()
        throws Exception
    {
        StringBuffer xml = new StringBuffer();
        for(int i = 1; i <= rs.getCols().length; i++)
            xml.append(rs.getCols()[i - 1].getName()).append(",");

        return xml;
    }

    public String getDate(int row_idx, String format, int col_idx)
        throws Exception
    {
        Object o = rs.getObject(row_idx, col_idx);
        if(o == null)
        {
            return "";
        } else
        {
            o.getClass();
            DateTool d = new DateTool((Timestamp)o);
            return d.toDateString(format);
        }
    }

    public String getDate(int row_idx, String format, String colName)
        throws Exception
    {
        Object o = rs.getObject(row_idx, colName);
        if(o == null)
        {
            return "";
        } else
        {
            o.getClass();
            DateTool d = new DateTool((Timestamp)o);
            return d.toDateString(format);
        }
    }

    public StringBuffer getXML()
        throws Exception
    {
        StringBuffer xml = new StringBuffer();
        xml.append("<result>\n");
        xml.append("<dbtype>");
        xml.append(rs.DBType);
        xml.append("</dbtype>");
        xml.append("<pageIdx>");
        xml.append(rs.pageIdx);
        xml.append("</pageIdx>\n");
        xml.append("<recNumPerPage>");
        xml.append(rs.recNumPerPage);
        xml.append("</recNumPerPage>\n");
        xml.append("<all_numrow>");
        xml.append(rs.all_numrow);
        xml.append("</all_numrow>\n");
        xml.append("<size>");
        xml.append(rs.size());
        xml.append("</size>\n");
        xml.append(getXMLColumnInfo());
        xml.append(getXMLRecordset());
        xml.append("</result>\n");
        return xml;
    }

    public StringBuffer getXMLColumnInfo()
    {
        StringBuffer xml = new StringBuffer();
        xml.append("<columns>\n");
        for(int i = 0; i < rs.getCols().length; i++)
        {
            xml.append("<col>");
            xml.append("<name>");
            xml.append(rs.getCols()[i].getFullName());
            xml.append("</name>");
            xml.append("<isPk>");
            xml.append(rs.getCols()[i].isPk());
            xml.append("</isPk>");
            xml.append("<data_type>");
            xml.append(rs.getCols()[i].getDataType());
            xml.append("</data_type>");
            xml.append("<length>");
            xml.append(rs.getCols()[i].getLength());
            xml.append("</length>");
            xml.append("<scale>");
            xml.append(rs.getCols()[i].getScale());
            xml.append("</scale>");
            xml.append("<dispLength>");
            xml.append(rs.getCols()[i].getDispLength());
            xml.append("</dispLength>");
            xml.append("<comment>");
            xml.append(rs.getCols()[i].getComment());
            xml.append("</comment>");
            xml.append("<nullAble>");
            xml.append(rs.getCols()[i].isNullable());
            xml.append("</nullAble>");
            xml.append("</col>\n");
        }

        xml.append("</columns>\n");
        return xml;
    }

    public StringBuffer getXMLRecordset()
        throws Exception
    {
        StringBuffer xml = new StringBuffer();
        xml.append("<recordset>\n");
        for(int r = 0; r < rs.size(); r++)
        {
            if(r > 0)
                xml.append("@\n");
            for(int c = 0; c < rs.getCols().length; c++)
            {
                if(c > 0)
                    xml.append("$");
                Object value = rs.getObject(r, c);
                if(rs.getColType(c) != null && rs.getColType(c).equals("timestamp"))
                    xml.append(value != null ? getDate(r, "YYYY-MM-DD", c) : "");
                else
                    xml.append(value != null ? replace(value.toString()) : "");
            }

        }

        xml.append("</recordset>\n");
        return xml;
    }

    private String replace(String colvalue)
    {
        colvalue = Util.getInstance().replace(colvalue, "&", "&amp;");
        colvalue = Util.getInstance().replace(colvalue, "<", "&lt;");
        colvalue = encode(colvalue);
        return colvalue;
    }

    QueryResult rs;
}
