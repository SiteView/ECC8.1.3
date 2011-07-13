// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Encript.java

package com.focus.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Encript
{
	private final static Logger logger = Logger.getLogger(Encript.class);

    public Encript()
    {
        iterationCount = 23;
        String passPhrase = "dengzelinhust";
        logger.info("111111111");
        try
        {
            java.security.spec.KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
            ecipher = Cipher.getInstance(key.getAlgorithm());
            dcipher = Cipher.getInstance(key.getAlgorithm());
            java.security.spec.AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
            ecipher.init(1, key, paramSpec);
            dcipher.init(2, key, paramSpec);
            logger.info("2222222222");
        }
        catch(InvalidAlgorithmParameterException invalidalgorithmparameterexception) { }
        catch(InvalidKeySpecException invalidkeyspecexception) { }
        catch(NoSuchPaddingException nosuchpaddingexception) { }
        catch(NoSuchAlgorithmException nosuchalgorithmexception) { }
        catch(InvalidKeyException invalidkeyexception) { }
    }

    public String encrypt(String str)
    {
        try
        {
            byte utf8[] = str.getBytes("UTF8");
            byte enc[] = ecipher.doFinal(utf8);
            return (new BASE64Encoder()).encode(enc);
        }
        catch(BadPaddingException badpaddingexception) { }
        catch(IllegalBlockSizeException illegalblocksizeexception) { }
        catch(UnsupportedEncodingException unsupportedencodingexception) { }
        return null;
    }

    public String decrypt(String str)
    {
        try
        {
            byte dec[] = (new BASE64Decoder()).decodeBuffer(str);
            byte utf8[] = dcipher.doFinal(dec);
            return new String(utf8, "UTF8");
        }
        catch(BadPaddingException badpaddingexception) { }
        catch(IllegalBlockSizeException illegalblocksizeexception) { }
        catch(UnsupportedEncodingException unsupportedencodingexception) { }
        catch(IOException ioexception) { }
        return null;
    }

    public static void main(String args[])
    {
        try
        {
            Encript encrypter = new Encript();
            String encrypted = encrypter.encrypt("dddd1234");
            String decrypted = encrypter.decrypt("BWQ0p4KgnX0=");
            logger.info(encrypted);
            logger.info(decrypted);
        }
        catch(Exception exception) { }
    }

    Cipher ecipher;
    Cipher dcipher;
    byte salt[] = {
        -87, -101, -56, 50, 86, 53, -29, 3
    };
    int iterationCount;
}
