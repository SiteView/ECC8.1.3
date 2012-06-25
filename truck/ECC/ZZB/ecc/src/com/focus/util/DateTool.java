// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DateTool.java

package com.focus.util;

import java.util.*;

// Referenced classes of package com.focus.util:
//            Util

public class DateTool
{

    public DateTool()
    {
        can = null;
        FORMAT = "YYYY-MM-DD";
        can = Calendar.getInstance();
    }

    public DateTool(long l)
    {
        this();
        can.setTime(new Date(l));
    }

    public DateTool(Date d)
    {
        this();
        can.setTime(d);
    }

    public String getDay()
    {
        Calendar _tmp = can;
        if(can.get(5) < 10)
        {
            Calendar _tmp1 = can;
            return "0" + can.get(5);
        } else
        {
            Calendar _tmp2 = can;
            return String.valueOf(can.get(5));
        }
    }

    public String getHour()
    {
        Calendar _tmp = can;
        if(can.get(11) < 10)
        {
            Calendar _tmp1 = can;
            return "0" + can.get(11);
        } else
        {
            Calendar _tmp2 = can;
            return String.valueOf(can.get(11));
        }
    }

    public String getMinute()
    {
        Calendar _tmp = can;
        if(can.get(12) < 10)
        {
            Calendar _tmp1 = can;
            return "0" + can.get(12);
        } else
        {
            Calendar _tmp2 = can;
            return String.valueOf(can.get(12));
        }
    }

    public String getMonth()
    {
        Calendar _tmp = can;
        if(can.get(2) + 1 < 10)
        {
            Calendar _tmp1 = can;
            return "0" + (can.get(2) + 1);
        } else
        {
            Calendar _tmp2 = can;
            return String.valueOf(can.get(2) + 1);
        }
    }

    public String getSecond()
    {
        Calendar _tmp = can;
        if(can.get(13) < 10)
        {
            Calendar _tmp1 = can;
            return "0" + can.get(13);
        } else
        {
            Calendar _tmp2 = can;
            return String.valueOf(can.get(13));
        }
    }

    public String getYear()
    {
        Calendar _tmp = can;
        return String.valueOf(can.get(1));
    }

    public String toDateString()
    {
        return toDateString(toDateString(FORMAT));
    }

    public String toDateString(String format)
    {
        String ret = format;
        if(ret.indexOf("YYYY") > -1)
            ret = Util.getInstance().replace(ret, "YYYY", String.valueOf(getYear()));
        if(ret.indexOf("YY") > -1)
            ret = Util.getInstance().replace(ret, "YY", getYear().substring(2));
        if(ret.indexOf("MM") > -1)
            ret = Util.getInstance().replace(ret, "MM", String.valueOf(getMonth()));
        if(ret.indexOf("DD") > -1)
            ret = Util.getInstance().replace(ret, "DD", String.valueOf(getDay()));
        if(ret.indexOf("HH") > -1)
            ret = Util.getInstance().replace(ret, "HH", String.valueOf(getHour()));
        if(ret.indexOf("MI") > -1)
            ret = Util.getInstance().replace(ret, "MI", String.valueOf(getMinute()));
        if(ret.indexOf("SS") > -1)
            ret = Util.getInstance().replace(ret, "SS", String.valueOf(getSecond()));
        return ret;
    }

    public String toString()
    {
        return toDateString();
    }

    public String toTimeString()
    {
        return getHour() + ":" + getMinute() + ":" + getSecond();
    }

    public String getMonthStart()
    {
        String ret = "YYYY-MM-DD";
        ret = Util.getInstance().replace(ret, "YYYY", String.valueOf(getYear()));
        ret = Util.getInstance().replace(ret, "MM", String.valueOf(getMonth()));
        ret = Util.getInstance().replace(ret, "DD", "01");
        return ret;
    }

    public String getYearStart()
    {
        String ret = "YYYY-MM-DD";
        ret = Util.getInstance().replace(ret, "YYYY", String.valueOf(getYear()));
        ret = Util.getInstance().replace(ret, "MM", "01");
        ret = Util.getInstance().replace(ret, "DD", "01");
        return ret;
    }

    public static long parseLong(String YYYYMMDDHHMISS)
    {
        return parse(YYYYMMDDHHMISS).getTime();
    }

    public static Date parse(String YYYYMMDDHHMISS)
    {
        Calendar can = Calendar.getInstance();
        StringTokenizer st = new StringTokenizer(YYYYMMDDHHMISS, " ");
        String YMD = st.nextElement().toString();
        StringTokenizer stYMD = new StringTokenizer(YMD, "-");
        int year = Integer.parseInt(stYMD.nextElement().toString());
        int month = Integer.parseInt(stYMD.nextElement().toString()) - 1;
        int day = Integer.parseInt(stYMD.nextElement().toString());
        can.set(year, month, day);
        if(st.hasMoreElements())
        {
            String HMS = st.nextElement().toString();
            StringTokenizer stHMS = new StringTokenizer(HMS, ":");
            int hour = Integer.parseInt(stHMS.nextElement().toString());
            int minute = Integer.parseInt(stHMS.nextElement().toString());
            can.set(year, month, day, hour, minute);
            if(stHMS.hasMoreElements())
            {
                int second = Integer.parseInt(stHMS.nextElement().toString());
                can.set(year, month, day, hour, minute, second);
            }
        }
        return can.getTime();
    }

    public String toGMT()
    {
        return (new Date(can.getTimeInMillis())).toGMTString();
    }

    Calendar can;
    String FORMAT;
}
