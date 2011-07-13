// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CacheMap.java

package com.focus.util;

import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

public class CacheMap
{

    public CacheMap()
    {
        admin = null;
        thread_start = false;
        admin = new GeneralCacheAdministrator();
    }

    public CacheMap(int capacity)
    {
        admin = null;
        thread_start = false;
        admin = new GeneralCacheAdministrator();
        admin.setCacheCapacity(capacity);
    }

    public Object put(Object key, Object cacheRs)
    {
        if(!cache_used)
        {
            return cacheRs;
        } else
        {
            admin.putInCache(key.toString(), cacheRs);
            return cacheRs;
        }
    }

    public Object get(Object key)
    {
        try
        {
            return admin.getFromCache(key.toString());
        }
        catch(NeedsRefreshException e)
        {
            admin.cancelUpdate(key.toString());
        }
        return null;
    }

    public Object safeGet(Object key)
    {
        return get(key);
    }

    public Object remove(Object key)
    {
        Object o = get(key);
        admin.flushEntry(key.toString());
        admin.removeEntry(key.toString());
        return o;
    }

    Object really_remove(Object key)
    {
        Object o = get(key);
        remove(key);
        return o;
    }

    GeneralCacheAdministrator admin;
    public static long timeoutValue = 0x36ee80L;
    public static long total_size = 0L;
    public static long max_size = 0x19000000L;
    public boolean thread_start;
    public static boolean cache_used = true;
    public static boolean cache_check_onsart = false;
    public static long cache_check_sleep = 0x493e0L;

}
