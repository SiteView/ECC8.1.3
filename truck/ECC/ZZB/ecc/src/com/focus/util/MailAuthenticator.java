// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MailAuthenticator.java

package com.focus.util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MailAuthenticator extends Authenticator
{

    public MailAuthenticator(String authenName, String authenPass)
    {
        this.authenName = authenName;
        this.authenPass = authenPass;
    }

    public PasswordAuthentication getPasswordAuthentication()
    {
        String temp = null;
        if(!authenPass.equals(""))
        {
            String _tmp = authenPass;
        }
        return new PasswordAuthentication(authenName, authenPass);
    }

    String authenName;
    String authenPass;
}
