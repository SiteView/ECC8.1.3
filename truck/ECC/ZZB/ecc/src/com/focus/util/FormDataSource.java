// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FormDataSource.java

package com.focus.util;

import java.io.*;
import javax.activation.DataSource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FormDataSource
    implements DataSource
{

    public FormDataSource(HttpServletRequest req, HttpServletResponse resp)
    {
        this.req = req;
        this.resp = resp;
    }

    public String getContentType()
    {
        return req.getContentType();
    }

    public InputStream getInputStream()
        throws IOException
    {
        return req.getInputStream();
    }

    public String getName()
    {
        return req.getServerName();
    }

    public OutputStream getOutputStream()
        throws IOException
    {
        return resp.getOutputStream();
    }

    HttpServletRequest req;
    HttpServletResponse resp;
}
