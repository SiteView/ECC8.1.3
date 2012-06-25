// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Upload.java

package com.focus.util;

import java.io.*;
import javax.servlet.ServletException;

public class Upload
{

    public Upload()
    {
        uploadDirectory = ".";
        ContentType = "";
        CharacterEncoding = "";
        strFileName = "";
        reName = null;
        iFileSize = 0L;
    }

    public void setFileReName(String name)
    {
        reName = name;
    }

    public String getFileReName()
    {
        return reName;
    }

    private String getFileName(String s)
    {
        int i = s.lastIndexOf("\\");
        if(i < 0 || i >= s.length() - 1)
        {
            i = s.lastIndexOf("/");
            if(i < 0 || i >= s.length() - 1)
                return s;
        }
        strFileName = s.substring(i + 1);
        return s.substring(i + 1);
    }

    public String getUploadFileName()
    {
        return strFileName;
    }

    public long getFileSize()
    {
        return iFileSize;
    }

    public void setUploadDirectory(String s)
    {
        uploadDirectory = s;
    }

    public void setContentType(String s)
    {
        ContentType = s;
        int j;
        if((j = ContentType.indexOf("boundary=")) != -1)
        {
            ContentType = ContentType.substring(j + 9);
            ContentType = "--" + ContentType;
        }
    }

    public void setCharacterEncoding(String s)
    {
        CharacterEncoding = s;
    }

    public void uploadFile(ByteArrayInputStream servletinputstream)
        throws ServletException, IOException
    {
        String s5 = null;
        String filename = null;
        byte Linebyte[] = new byte[4096];
        byte outLinebyte[] = new byte[4096];
        int ai[] = new int[1];
        int ai1[] = new int[1];
        String line;
        while((line = readLine(Linebyte, ai, servletinputstream, CharacterEncoding)) != null) 
        {
            int i = line.indexOf("filename=");
            if(i >= 0)
            {
                line = line.substring(i + 10);
                if((i = line.indexOf("\"")) > 0)
                    line = line.substring(0, i);
                break;
            }
        }
        filename = line;
        if(filename != null && !filename.equals("\""))
        {
            filename = getFileName(filename);
            String sContentType = readLine(Linebyte, ai, servletinputstream, CharacterEncoding);
            if(sContentType.indexOf("Content-Type") >= 0)
                readLine(Linebyte, ai, servletinputstream, CharacterEncoding);
            File file;
            if(reName == null)
                file = new File(uploadDirectory, filename);
            else
                file = new File(reName);
            FileOutputStream fileoutputstream = null;
            try
            {
            fileoutputstream=new FileOutputStream(file);
            while((sContentType = readLine(Linebyte, ai, servletinputstream, CharacterEncoding)) != null) 
            {
                if(sContentType.indexOf(ContentType) == 0 && Linebyte[0] == 45)
                    break;
                if(s5 != null)
                {
                    fileoutputstream.write(outLinebyte, 0, ai1[0]);
                    fileoutputstream.flush();
                }
                s5 = readLine(outLinebyte, ai1, servletinputstream, CharacterEncoding);
                if(s5 == null || s5.indexOf(ContentType) == 0 && outLinebyte[0] == 45)
                    break;
                fileoutputstream.write(Linebyte, 0, ai[0]);
                fileoutputstream.flush();
            }
            byte byte0;
            if(newline.length() == 1)
                byte0 = 2;
            else
                byte0 = 1;
            if(s5 != null && outLinebyte[0] != 45 && ai1[0] > newline.length() * byte0)
                fileoutputstream.write(outLinebyte, 0, ai1[0] - newline.length() * byte0);
            if(sContentType != null && Linebyte[0] != 45 && ai[0] > newline.length() * byte0)
                fileoutputstream.write(Linebyte, 0, ai[0] - newline.length() * byte0);
            iFileSize = file.length();
          }finally{
            fileoutputstream.close();
          }
        }
    }

    private String readLine(byte Linebyte[], int ai[], ByteArrayInputStream servletinputstream, String CharacterEncoding)
    {
        ai[0] = servletinputstream.read(Linebyte, 0, Linebyte.length);
        if(ai[0] == -1)
            return null;
        try
        {
            if(CharacterEncoding == null || CharacterEncoding.equals(""))
                return new String(Linebyte, 0, ai[0]);
            else
                return new String(Linebyte, 0, ai[0], CharacterEncoding);
        }
        catch(Exception _ex)
        {
            return null;
        }
    }

    private static String newline = "\n";
    private String uploadDirectory;
    private String ContentType;
    private String CharacterEncoding;
    private String strFileName;
    private String reName;
    private long iFileSize;

}
