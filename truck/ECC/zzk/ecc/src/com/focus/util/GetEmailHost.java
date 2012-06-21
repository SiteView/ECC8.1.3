// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GetEmailHost.java

package com.focus.util;


public class GetEmailHost
{

    public GetEmailHost()
    {
    }

    public static String getSmtpHost(String email)
    {
        if(email.toLowerCase().indexOf("sina.com") != -1)
            return "smtp.sina.com";
        if(email.toLowerCase().indexOf("dragonflow.com") != -1)
            return "mail.dragonflow.com";
        if(email.toLowerCase().indexOf("3ren.com") != -1)
            return "mail.3ren.com";
        else
            return null;
    }

    public static String getPop3Host(String email)
    {
        if(email.toLowerCase().indexOf("sina.com") != -1)
            return "pop.sina.com";
        if(email.toLowerCase().indexOf("dragonflow.com") != -1)
            return "mail.dragonflow.com";
        if(email.toLowerCase().indexOf("3ren.com") != -1)
            return "mail.3ren.com";
        else
            return null;
    }
}
