// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SanRenUtil.java

package com.focus.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.focus.db.DBCon;
import com.focus.db.QueryResult;

// Referenced classes of package com.focus.util:
//            CourseStructure

public class SanRenUtil
{
	private final static Logger logger = Logger.getLogger(SanRenUtil.class);

    public SanRenUtil()
    {
    }

    public ArrayList getCourseList(QueryResult qr, String superiorid, HashMap hm, int deep, int cur)
        throws Exception
    {
        ArrayList hmRet = new ArrayList();
        if(cur > deep)
            return hmRet;
        Object range = hm.get(superiorid);
        if(range != null)
        {
            String ranges[] = range.toString().split(":");
            for(int i = Integer.parseInt(ranges[0]); i < Integer.parseInt(ranges[1]) + 1; i++)
            {
                ArrayList hmtmp = getCourseList(qr, qr.getObject(i, "COURSETYPEID").toString(), hm, deep, cur + 1);
                if(hmtmp.size() != 0)
                {
                    CourseStructure cs = new CourseStructure();
                    cs.add(qr.getObject(i, "TYPENAME").toString(), hmtmp);
                    hmRet.add(cs);
                } else
                {
                    for(int j = Integer.parseInt(ranges[0]); j < Integer.parseInt(ranges[1]) + 1; j++)
                        hmRet.add(qr.getObject(j, "COURSETYPEID") + ":" + superiorid + ":" + qr.getObject(j, "TYPENAME"));

                    return hmRet;
                }
            }

        }
        return hmRet;
    }

    public HashMap getRange(QueryResult qr)
        throws Exception
    {
        HashMap hm = new HashMap();
        int index = 0;
        for(int i = 0; i < qr.size(); i++)
        {
            if(!qr.getObject(index, "SUPERIORID").equals(qr.getObject(i, "SUPERIORID")))
            {
                hm.put(qr.getObject(index, "SUPERIORID").toString(), index + ":" + (i - 1));
                index = i;
            }
            if(i == qr.size() - 1)
                hm.put(qr.getObject(index, "SUPERIORID").toString(), index + ":" + i);
        }

        return hm;
    }

    public static long toLong(String time)
        throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = sdf.parse(time);
        return date.getTime();
    }

    public static long toLong(String time, String format)
        throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = sdf.parse(time);
        return date.getTime();
    }

    public static HashMap getColCourse(HashMap hm, int count)
    {
        HashMap hmret = new HashMap();
        Object obj[] = hm.keySet().toArray();
        int count1 = 0;
        for(int i = 0; i < obj.length; i++)
        {
            HashMap tmp = (HashMap)hm.get(obj[i]);
            count1 += tmp.size();
            if(count1 < count)
            {
                hmret.put(obj[i], tmp);
                hm.remove(obj[i]);
            }
        }

        return hmret;
    }

    public static String getFileSize(String size)
    {
        float fsize = Float.parseFloat(size);
        float tmp = fsize / 1048576F;
        logger.info(tmp);
        if(tmp > 1.0F)
        {
            size = String.valueOf(tmp);
            if(size.indexOf(".") != -1)
                size = size.substring(0, size.indexOf(".") + 2) + "M";
        } else
        {
            tmp = fsize / 1024F;
            size = String.valueOf(tmp);
            if(size.indexOf(".") != -1)
                size = size.substring(0, size.indexOf(".") + 2) + "K";
        }
        return size;
    }

    private static HashMap makeBlock(QueryResult qr)
        throws Exception
    {
        HashMap hm = new HashMap();
        String id = null;
        String lastid = null;
        HashMap hmtmp = null;
        for(int i = 0; i < qr.size(); i++)
        {
            String superiorid = ((String) (qr.getObject(i, "SUPERIORID")));
            if(!superiorid.equals(id))
            {
                if(id != null)
                    hm.put(lastid, hmtmp);
                lastid = superiorid;
                hmtmp = new HashMap();
                hmtmp.put(qr.getObject(i, "COURSETYPEID").toString(), qr.getObject(i, "TYPENAME"));
                id = superiorid;
                if(i == qr.size() - 1)
                    hm.put(superiorid, hmtmp);
            } else
            {
                hmtmp.put(qr.getObject(i, "COURSETYPEID").toString(), qr.getObject(i, "TYPENAME"));
                if(i == qr.size() - 1)
                    hm.put(superiorid, hmtmp);
            }
        }

        return hm;
    }

    private static HashMap makeTypeLevel(DBCon con, HashMap hmRoot, HashMap hmBlock)
        throws Exception
    {
        HashMap hmRet;
        QueryResult qr;
        hmRet = new HashMap();
        if(hmBlock.size() == 0)
            return null;
        Object obj[] = hmBlock.keySet().toArray();
        String ids = "";
        for(int i = 0; i < obj.length; i++)
            ids = ids + obj[i] + ",";

        ids = ids.substring(0, ids.length() - 1);
        logger.info("ids:" + ids);
        qr = null;
        try
        {
            con = DBCon.getConnection("portalDS");
            String sql = "select * from sr_course_type where coursetypeid in (" + ids + ") order by superiorid";
            con.setSQL(sql);
            qr = con.query();
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
        for(int i = 0; i < qr.size(); i++)
            if(qr.getObject(i, "SUPERIORID").toString().compareTo("0") == 0)
            {
                hmRoot.put(qr.getObject(i, "COURSETYPEID") + ":" + qr.getObject(i, "TYPENAME"), hmBlock.get(qr.getObject(i, "COURSETYPEID").toString()));
            } else
            {
                String id = null;
                String coursetypeid = ((String) (qr.getObject(i, "COURSETYPEID")));
                if(!coursetypeid.equals(id))
                {
                    id = coursetypeid;
                    HashMap map = new HashMap();
                    map.put(coursetypeid + ":" + qr.getObject(i, "TYPENAME"), hmBlock.get(coursetypeid));
                    hmRet.put(qr.getObject(i, "SUPERIORID").toString(), map);
                }
            }

        return hmRet;
    }

    public static HashMap getTypeLevel(QueryResult qr, String pageSize, String pageNum)
    {
        HashMap root;
        DBCon con = null;
        root = new HashMap();
        try
        {
            con = DBCon.getConnection("portalDS");
            HashMap hm = makeBlock(qr);
            do
                hm = makeTypeLevel(con, root, hm);
            while(hm != null);
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
        return root;
    }

    public static void getNamePath(HashMap ret, HashMap hm, String names, String ids)
    {
        Object obj[] = hm.keySet().toArray();
        HashMap tmp = null;
        int index = -1;
        String name = "";
        String idses = "";
        for(int i = 0; i < obj.length; i++)
        {
            index = obj[i].toString().indexOf(":");
            if(index != -1)
            {
                name = obj[i].toString().substring(index + 1, obj[i].toString().length());
                idses = obj[i].toString().substring(0, index);
                logger.info("name:" + name + " ids:" + idses);
                tmp = (HashMap)hm.get(obj[i].toString());
                logger.info("tmp:" + tmp);
                String tmpstr1;
                String tmpstr2;
                if(names.equals(""))
                {
                    tmpstr1 = name;
                    tmpstr2 = idses;
                } else
                {
                    tmpstr1 = names + "--" + name;
                    tmpstr2 = ids + ":" + idses;
                }
                getNamePath(ret, tmp, tmpstr1, tmpstr2);
            } else
            {
                logger.info("here");
                ret.put(ids + ":" + obj[i].toString(), names + "--" + hm.get(obj[i]));
            }
        }

    }

    public static String TimeFormate(String time)
    {
        String times = "";
        times = time.substring(0, time.lastIndexOf(":"));
        return times;
    }

    public static ArrayList getCourseList()
        throws Exception
    {
        ArrayList al;
        QueryResult qr;
        al = new ArrayList();
        String sql = "select * from (select * from sr_course_type where superiorid=0 union select * from sr_course_type where notendflag='N') t order by t.superiorid,t.ordernum";
        DBCon con = null;
        qr = null;
        try
        {
            con = DBCon.getConnection("portalDS");
            con.setSQL(sql);
            qr = con.query();
        }
        catch(Exception ex)
        {
            logger.info(ex.getMessage());
        }
        finally
        {
            if(con != null)
                con.close();
        }
        organizeCoursetype(qr, al);
        logger.info("last size:" + al.size());
        return al;
    }

    private static void organizeCoursetype(QueryResult qr, ArrayList al)
        throws Exception
    {
        String id = null;
        ArrayList alTmp = null;
        for(int i = 0; i < qr.size(); i++)
        {
            if(!qr.getObject(i, "SUPERIORID").toString().equals("0"))
                break;
            id = ((String) (qr.getObject(i, "COURSETYPEID")));
            alTmp = getSubCourseList(qr, id, i);
            CourseStructure cs = new CourseStructure();
            cs.add(qr.getObject(i, "TYPENAME") + ":" + qr.getObject(i, "COURSETYPEID"), alTmp);
            al.add(cs);
        }

    }

    private static ArrayList getSubCourseList(QueryResult qr, String id, int idx)
        throws Exception
    {
        boolean hit = false;
        ArrayList al = new ArrayList();
        for(int i = 0; i < qr.size(); i++)
        {
            if(idx != i && qr.getObject(i, "TOPID").toString().equals(id))
            {
                hit = true;
                al.add(qr.getObject(i, "TYPENAME") + ":" + qr.getObject(i, "COURSETYPEID"));
                continue;
            }
            if(hit)
                break;
        }

        return al;
    }

    public static void main(String args[])
        throws Exception
    {
        ArrayList al = getCourseList();
        for(int i = 0; i < al.size(); i++)
        {
            CourseStructure cs = (CourseStructure)al.get(i);
            String key = cs.getKey();
            ArrayList tmp = cs.getItems();
            logger.info("key" + i + ":" + key);
            for(int j = 0; j < tmp.size(); j++)
            {
                String item = ((String) (tmp.get(j)));
                logger.info("items" + j + ":" + item);
            }

        }

    }
}
