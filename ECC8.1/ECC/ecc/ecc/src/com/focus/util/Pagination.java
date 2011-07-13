// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Pagination.java

package com.focus.util;

import java.util.ArrayList;

public class Pagination
{

    public Pagination()
    {
        cur_page = 1;
        total_row = 0;
        per_page_row = 50;
        total_page = 1;
        cur_page_row = 0;
    }

    public int getTotalRow()
    {
        return total_row;
    }

    public int getPerPagerow()
    {
        return per_page_row;
    }

    public int getCurPage()
    {
        return cur_page;
    }

    public void setCurPage(String curPage)
    {
        try
        {
            cur_page = Integer.parseInt(curPage);
        }
        catch(Exception exception) { }
    }

    public void setPerPage(String perPage)
    {
        try
        {
            per_page_row = Integer.parseInt(perPage);
        }
        catch(Exception exception) { }
    }

    public void setTotalRow(String totalRow)
    {
        try
        {
            total_row = Integer.parseInt(totalRow);
        }
        catch(Exception exception) { }
        recount();
    }

    void recount()
    {
        double num_pages = ((double)total_row + 0.0D) / (double)per_page_row;
        if(num_pages - (double)(int)num_pages == 0.0D)
            total_page = (int)num_pages;
        else
            total_page = (int)num_pages + 1;
        if(total_page > cur_page)
            cur_page_row = per_page_row;
        else
            cur_page_row = total_row - (cur_page - 1) * per_page_row;
    }

    public int getCurPageRow()
    {
        recount();
        return cur_page_row;
    }

    public int getTotalPage()
    {
        recount();
        return total_page;
    }

    public boolean dispHomePage()
    {
        recount();
        return cur_page != 1;
    }

    public boolean dispPrevPage()
    {
        recount();
        return cur_page > 1;
    }

    public boolean dispNextPage()
    {
        recount();
        return cur_page < total_page;
    }

    public boolean dispEndPage()
    {
        recount();
        return total_row != 0 && getCurPage() != total_page;
    }

    public int getCurStartindex()
    {
        return (getCurPage() - 1) * per_page_row;
    }

    public int getCurMaxindex()
    {
        return getCurStartindex() + getCurPageRow();
    }

    public Integer[] getPages()
    {
        int totalPage = getTotalPage();
        int curPage = getCurPage();
        ArrayList al = new ArrayList();
        for(int i = 1; i < 10 && i < total_page; i++)
            if(i != cur_page)
                al.add(new Integer(i));

        Integer pages[] = new Integer[al.size()];
        al.toArray(pages);
        return pages;
    }

    int cur_page;
    int total_row;
    int per_page_row;
    int total_page;
    int cur_page_row;
}
