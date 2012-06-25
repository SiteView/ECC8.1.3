// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TimeCounter.java

package com.focus.util;

import java.util.ArrayList;

public final class TimeCounter
{

    public TimeCounter()
    {
        time = System.currentTimeMillis();
        access_count = 1;
        count_per_20second = 1;
        sessionidList = new ArrayList();
    }

    public long time;
    public int access_count;
    public int count_per_20second;
    ArrayList sessionidList;
}
