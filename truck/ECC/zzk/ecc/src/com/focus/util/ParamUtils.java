// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ParamUtils.java

package com.focus.util;

import javax.servlet.http.HttpServletRequest;

public class ParamUtils
{

    public ParamUtils()
    {
    }

    public static String convertString(String stringname, int length, String defaultstring)
    {
        String result = null;
        if(stringname == null || stringname.equals(""))
        {
            result = defaultstring;
        } else
        {
            int stringltemp = stringname.length();
            if(stringltemp > length)
                result = stringname.substring(0, length) + "...";
            else
                result = stringname;
        }
        return result;
    }

    public static int getInt(HttpServletRequest request, String s, int defaultInt)
    {
        int j = 0;
        try
        {
            String temp = getString(request, s);
            if(temp == null)
                j = defaultInt;
            else
                j = Integer.parseInt(temp);
        }
        catch(NumberFormatException e)
        {
            j = 0;
        }
        return j;
    }

    public static String getString(HttpServletRequest request, String s, String defaultString)
        throws Exception
    {
        String s1 = getString(request, s);
        if(s1 == null)
            return defaultString;
        else
            return s1;
    }

    private static String getString(HttpServletRequest request, String s)
    {
        String temp = null;
        try
        {
            temp = request.getParameter(s).trim();
            if(temp != null && temp.equalsIgnoreCase("null"))
                temp = "";
        }
        catch(Exception exception) { }
        return temp;
    }

    public static String getPageInfo(String form, Integer AllRecordCount, int PageNo, String jspName, int PageCount)
    {
        StringBuffer sb = new StringBuffer();
        int aYu = 0;
        int aTemp = 0;
        boolean IsPrevious = false;
        boolean IsNext = false;
        boolean IsBegin = false;
        boolean IsEnd = false;
        if(PageCount != 20 || PageCount != 40 || PageCount != 60)
            PageCount = 20;
        aYu = AllRecordCount.intValue() % PageCount;
        aTemp = AllRecordCount.intValue() / PageCount;
        if(aYu > 0)
            aTemp++;
        if(PageNo > 1)
        {
            IsPrevious = true;
            IsBegin = true;
        }
        if(aTemp > 1 && PageNo != aTemp)
        {
            IsNext = true;
            IsEnd = true;
        }
        String next = jspName + "?pageno=" + String.valueOf(PageNo + 1);
        String prev = jspName + "?pageno=" + String.valueOf(PageNo - 1);
        String last = jspName + "?pageno=" + String.valueOf(aTemp);
        sb.append("<td align=\"left\" valign=\"middle\">&nbsp;&nbsp;\u6BCF\u9875\u663E\u793A\u6570\u91CF\uFF1A");
        sb.append("<a href=" + jspName + "?pagecounts=20" + ">20</a>&nbsp;");
        sb.append("<a href=" + jspName + "?pagecounts=40" + ">40</a>&nbsp;");
        sb.append("<a href=" + jspName + "?pagecounts=60" + ">60</a>");
        sb.append("</td>");
        sb.append("<td align=\"right\" valign=\"middle\">" + PageNo + "/" + aTemp + "&nbsp");
        if(IsBegin)
            sb.append("<a href=\"exquery.jsp?pageno=1\">\u7B2C\u4E00\u9875</a>&nbsp");
        else
            sb.append("\u7B2C\u4E00\u9875&nbsp");
        if(IsPrevious)
            sb.append("<a href=" + prev + " onclick=\"pageJump('" + (PageNo - 1) + "');\">\u4E0A\u4E00\u9875</a>&nbsp");
        else
            sb.append("\u4E0A\u4E00\u9875&nbsp");
        if(IsNext)
            sb.append("<a href=" + next + " onclick=\"pageJump('" + (PageNo + 1) + "');\">\u4E0B\u4E00\u9875</a>&nbsp");
        else
            sb.append("\u4E0B\u4E00\u9875&nbsp");
        if(IsEnd)
            sb.append("<a href=" + last + " onclick=\"pageJump('" + aTemp + "');\">\u5C3E\u9875</a>&nbsp");
        else
            sb.append("\u5C3E\u9875&nbsp");
        sb.append("\u8DF3\u5230");
        sb.append("<select name=\"pageno\" onchange = \"" + form + ".submit();\">");
        for(int i = 1; i < aTemp + 1; i++)
            if(PageNo == i)
                sb.append("<option value=" + i + " selected>\u7B2C" + i + "\u9875</option>");
            else
                sb.append("<option value=" + i + ">\u7B2C" + i + "\u9875</option>");

        sb.append("</select></td>");
        sb.append("<script language=\"javascript\">");
        sb.append("function pageJump(str){");
        sb.append(form + ".pageno.value=str;");
        sb.append(form + ".submit();");
        sb.append("}");
        sb.append("</script>");
        return sb.toString();
    }

    public static String convertString2(String stringname, int length, String defaultstring)
    {
        String result = null;
        if(stringname == null || stringname.equals(""))
        {
            result = defaultstring;
        } else
        {
            int stringltemp = stringname.length();
            if(stringltemp > length)
                result = stringname.substring(0, length);
            else
                result = stringname;
        }
        return result;
    }

    public static String getPageInfo2(String form, Integer AllRecordCount, int PageNo, String jspName, int PageCount)
    {
        StringBuffer sb = new StringBuffer();
        int aYu = 0;
        int aTemp = 0;
        boolean IsPrevious = false;
        boolean IsNext = false;
        boolean IsBegin = false;
        boolean IsEnd = false;
        aYu = AllRecordCount.intValue() % PageCount;
        aTemp = AllRecordCount.intValue() / PageCount;
        if(aYu > 0)
            aTemp++;
        if(PageNo > 1)
        {
            IsPrevious = true;
            IsBegin = true;
        }
        if(aTemp > 1 && PageNo != aTemp)
        {
            IsNext = true;
            IsEnd = true;
        }
        String next = jspName + "?pageno=" + String.valueOf(PageNo + 1);
        String prev = jspName + "?pageno=" + String.valueOf(PageNo - 1);
        String last = jspName + "?pageno=" + String.valueOf(aTemp);
        sb.append("<td align=\"left\" valign=\"middle\">&nbsp;&nbsp;\u5171\u6709\uFF1A");
        sb.append(AllRecordCount);
        sb.append("\u7BC7\u6587\u7AE0");
        sb.append("</td>");
        sb.append("<td align=\"right\" valign=\"middle\">" + PageNo + "/" + aTemp + "&nbsp");
        if(IsBegin)
            sb.append("<a href=" + jspName + "?pageno=1>\u7B2C\u4E00\u9875</a>&nbsp");
        else
            sb.append("\u7B2C\u4E00\u9875&nbsp");
        if(IsPrevious)
            sb.append("<a href=" + prev + " onclick=\"pageJump('" + (PageNo - 1) + "');\">\u4E0A\u4E00\u9875</a>&nbsp");
        else
            sb.append("\u4E0A\u4E00\u9875&nbsp");
        if(IsNext)
            sb.append("<a href=" + next + " onclick=\"pageJump('" + (PageNo + 1) + "');\">\u4E0B\u4E00\u9875</a>&nbsp");
        else
            sb.append("\u4E0B\u4E00\u9875&nbsp");
        if(IsEnd)
            sb.append("<a href=" + last + " onclick=\"pageJump('" + aTemp + "');\">\u5C3E\u9875</a>&nbsp");
        else
            sb.append("\u5C3E\u9875&nbsp");
        sb.append("\u8DF3\u5230");
        sb.append("<select name=\"pageno\" onchange = \"" + form + ".submit();\">");
        for(int i = 1; i < aTemp + 1; i++)
            if(PageNo == i)
                sb.append("<option value=" + i + " selected>\u7B2C" + i + "\u9875</option>");
            else
                sb.append("<option value=" + i + ">\u7B2C" + i + "\u9875</option>");

        sb.append("</select></td>");
        sb.append("<script language=\"javascript\">");
        sb.append("function pageJump(str){");
        sb.append(form + ".pageno.value=str;");
        sb.append(form + ".submit();");
        sb.append("}");
        sb.append("</script>");
        return sb.toString();
    }

    public static String getPageInfo3(String form, Integer AllRecordCount, int PageNo, String jspName, int PageCount)
    {
        StringBuffer sb = new StringBuffer();
        int aYu = 0;
        int aTemp = 0;
        boolean IsPrevious = false;
        boolean IsNext = false;
        boolean IsBegin = false;
        boolean IsEnd = false;
        aYu = AllRecordCount.intValue() % PageCount;
        aTemp = AllRecordCount.intValue() / PageCount;
        if(aYu > 0)
            aTemp++;
        if(PageNo > 1)
        {
            IsPrevious = true;
            IsBegin = true;
        }
        if(aTemp > 1 && PageNo != aTemp)
        {
            IsNext = true;
            IsEnd = true;
        }
        String next = jspName + "&pageno=" + String.valueOf(PageNo + 1);
        String prev = jspName + "&pageno=" + String.valueOf(PageNo - 1);
        String last = jspName + "&pageno=" + String.valueOf(aTemp);
        sb.append("<td align=\"left\" valign=\"middle\">&nbsp;&nbsp;\u5171\u6709\uFF1A");
        sb.append(AllRecordCount);
        sb.append("\u6761\u6D88\u606F");
        sb.append("</td>");
        sb.append("<td align=\"right\" valign=\"middle\">" + PageNo + "/" + aTemp + "&nbsp");
        if(IsBegin)
            sb.append("<a href=" + jspName + "&pageno=1>\u7B2C\u4E00\u9875</a>&nbsp");
        else
            sb.append("\u7B2C\u4E00\u9875&nbsp");
        if(IsPrevious)
            sb.append("<a href=" + prev + " onclick=\"pageJump('" + (PageNo - 1) + "');\">\u4E0A\u4E00\u9875</a>&nbsp");
        else
            sb.append("\u4E0A\u4E00\u9875&nbsp");
        if(IsNext)
            sb.append("<a href=" + next + " onclick=\"pageJump('" + (PageNo + 1) + "');\">\u4E0B\u4E00\u9875</a>&nbsp");
        else
            sb.append("\u4E0B\u4E00\u9875&nbsp");
        if(IsEnd)
            sb.append("<a href=" + last + " onclick=\"pageJump('" + aTemp + "');\">\u5C3E\u9875</a>&nbsp");
        else
            sb.append("\u5C3E\u9875&nbsp");
        sb.append("\u8DF3\u5230");
        sb.append("<select name=\"pageno\" onchange = \"" + form + ".submit();\">");
        for(int i = 1; i < aTemp + 1; i++)
            if(PageNo == i)
                sb.append("<option value=" + i + " selected>\u7B2C" + i + "\u9875</option>");
            else
                sb.append("<option value=" + i + ">\u7B2C" + i + "\u9875</option>");

        sb.append("</select></td>");
        sb.append("<script language=\"javascript\">");
        sb.append("function pageJump(str){");
        sb.append(form + ".pageno.value=str;");
        sb.append(form + ".submit();");
        sb.append("}");
        sb.append("</script>");
        return sb.toString();
    }
}
