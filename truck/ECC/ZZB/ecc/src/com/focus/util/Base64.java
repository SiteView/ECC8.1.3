// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Base64.java

package com.focus.util;


public class Base64
{

    public Base64()
    {
    }

    public static String decode(String encoded)
    {
        StringBuffer sb = new StringBuffer();
        int maxturns;
        if(encoded.length() % 3 == 0)
            maxturns = encoded.length();
        else
            maxturns = encoded.length() + (3 - encoded.length() % 3);
        byte unenc[] = new byte[4];
        int i = 0;
        int j = 0;
        for(; i < maxturns; i++)
        {
            boolean skip = false;
            byte b;
            if(i < encoded.length())
                b = (byte)encoded.charAt(i);
            else
                b = 0;
            if(b >= 65 && b < 91)
                unenc[j] = (byte)(b - 65);
            else
            if(b >= 97 && b < 123)
                unenc[j] = (byte)(b - 71);
            else
            if(b >= 48 && b < 58)
                unenc[j] = (byte)(b + 4);
            else
            if(b == 43)
                unenc[j] = 62;
            else
            if(b == 47)
                unenc[j] = 63;
            else
            if(b == 61)
            {
                unenc[j] = 0;
            } else
            {
                char c = (char)b;
                if(c == '\n' || c == '\r' || c == ' ' || c == '\t')
                    skip = true;
            }
            if(!skip && ++j == 4)
            {
                int res = (unenc[0] << 18) + (unenc[1] << 12) + (unenc[2] << 6) + unenc[3];
                for(int k = 16; k >= 0; k -= 8)
                {
                    byte c = (byte)(res >> k);
                    if(c > 0)
                        sb.append((char)c);
                }

                j = 0;
                unenc[0] = 0;
                unenc[1] = 0;
                unenc[2] = 0;
                unenc[3] = 0;
            }
        }

        return sb.toString();
    }

    public static String encode(String plain)
    {
        if(plain.length() > 76)
            return null;
        StringBuffer sb = new StringBuffer();
        byte enc[] = new byte[3];
        boolean end = false;
        int i = 0;
        int j = 0;
        while(!end) 
        {
            char _ch = plain.charAt(i);
            if(i == plain.length() - 1)
                end = true;
            enc[j++] = (byte)plain.charAt(i);
            if(j == 3 || end)
            {
                int res = (enc[0] << 16) + (enc[1] << 8) + enc[2];
                int lowestbit = 18 - j * 6;
                for(int toshift = 18; toshift >= lowestbit; toshift -= 6)
                {
                    int b = res >>> toshift;
                    b &= 0x3f;
                    if(b >= 0 && b < 26)
                        sb.append((char)(b + 65));
                    if(b >= 26 && b < 52)
                        sb.append((char)(b + 71));
                    if(b >= 52 && b < 62)
                        sb.append((char)(b - 4));
                    if(b == 62)
                        sb.append('+');
                    if(b == 63)
                        sb.append('/');
                    if(sb.length() % 76 == 0)
                        sb.append('\n');
                }

                if(end)
                {
                    if(j == 1)
                        sb.append("==");
                    if(j == 2)
                        sb.append('=');
                }
                enc[0] = 0;
                enc[1] = 0;
                enc[2] = 0;
                j = 0;
            }
            i++;
        }
        return sb.toString();
    }
}
