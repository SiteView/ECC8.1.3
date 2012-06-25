// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CourseStructure.java

package com.focus.util;

import java.util.ArrayList;

public class CourseStructure
{

    public CourseStructure()
    {
        key = null;
        items = null;
    }

    public void add(String key, ArrayList al)
    {
        this.key = key;
        items = al;
    }

    public String getKey()
    {
        return key;
    }

    public ArrayList getItems()
    {
        return items;
    }

    private String key;
    private ArrayList items;
}
