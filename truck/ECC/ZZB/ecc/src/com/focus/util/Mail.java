// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Mail.java

package com.focus.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;

// Referenced classes of package com.focus.util:
//            GetEmailHost, MailAuthenticator

public class Mail
{
	private final static Logger logger = Logger.getLogger(Mail.class);

    public Mail()
    {
        to = null;
        userName = null;
        bodytext = new StringBuilder();
        pwd = null;
        inbox = null;
        store = null;
        from = null;
        host = null;
        contentText = null;
        subject = null;
        filename = new ArrayList();
        recipientsTO = null;
        recipientsCC = null;
        recipientsBCC = null;
    }

    public Mail(String username, String pwd)
    {
        to = null;
        userName = null;
        bodytext = new StringBuilder();
        this.pwd = null;
        inbox = null;
        store = null;
        from = null;
        host = null;
        contentText = null;
        subject = null;
        filename = new ArrayList();
        recipientsTO = null;
        recipientsCC = null;
        recipientsBCC = null;
        userName = username;
        this.pwd = pwd;
    }

    public Mail(String from)
    {
        to = null;
        userName = null;
        bodytext = new StringBuilder();
        pwd = null;
        inbox = null;
        store = null;
        this.from = null;
        host = null;
        contentText = null;
        subject = null;
        filename = new ArrayList();
        recipientsTO = null;
        recipientsCC = null;
        recipientsBCC = null;
        this.from = from;
        debug = false;
    }

    public void addRecipientTO(String mailAddr)
    {
        if(recipientsTO == null)
            recipientsTO = new ArrayList();
        try
        {
            recipientsTO.add(new InternetAddress(mailAddr));
        }
        catch(AddressException e)
        {
            e.printStackTrace();
        }
    }

    public void addRecipientCC(String mailAddr)
    {
        if(recipientsCC == null)
            recipientsCC = new ArrayList();
        try
        {
            recipientsCC.add(new InternetAddress(mailAddr));
        }
        catch(AddressException e)
        {
            e.printStackTrace();
        }
    }

    public void addRecipientBCC(String mailAddr)
    {
        if(recipientsBCC == null)
            recipientsBCC = new ArrayList();
        try
        {
            recipientsBCC.add(new InternetAddress(mailAddr));
        }
        catch(AddressException e)
        {
            e.printStackTrace();
        }
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public void setDebug(boolean debug)
    {
        this.debug = debug;
    }

    public void setFromAddress(String addr)
    {
        from = addr;
    }

    public void setUsername(String userName)
    {
        this.userName = userName;
    }

    public void setPwd(String pwd)
    {
        this.pwd = pwd;
    }

    public void addAttachFiles(String filepath)
    {
        filename.add(filepath);
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public void setContentText(String text)
    {
        contentText = text;
    }

    private String getTypeFromContent(String content)
    {
        if(content != null && content.toLowerCase().indexOf("<html>") != -1)
            return "text/html;charset=GB2312";
        else
            return "text/plain;charset=GB2312";
    }

    private InternetAddress[] getInternetAddr(ArrayList v)
    {
        if(v == null || v.size() <= 0)
            return null;
        InternetAddress list[] = new InternetAddress[v.size()];
        for(int i = 0; i < v.size(); i++)
            list[i] = (InternetAddress)v.get(i);

        return list;
    }

    public boolean sendMail()
    {
        Properties props = System.getProperties();
        if(from == null)
        {
            logger.info("\u53D1\u4FE1\u4EBA\u5730\u5740\u4E0D\u80FD\u4E3A\u7A7A");
            return false;
        }
        host = GetEmailHost.getSmtpHost(from);
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.auth", "true");
        if(userName == null || pwd == null)
        {
            logger.info("\u7528\u6237\u540D\u548C\u5BC6\u7801\u4E0D\u80FD\u4E3A\u7A7A\uFF0C\u5426\u5219\u8BA4\u8BC1\u4F1A\u5931\u8D25");
            return false;
        }
        MailAuthenticator ma = new MailAuthenticator(userName, pwd);
        Session session = Session.getDefaultInstance(props, ma);
        session.setDebug(debug);
        try
        {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            InternetAddress list[] = getInternetAddr(recipientsTO);
            msg.setRecipients(javax.mail.Message.RecipientType.TO, list);
            list = getInternetAddr(recipientsCC);
            if(list != null)
                msg.setRecipients(javax.mail.Message.RecipientType.CC, list);
            list = getInternetAddr(recipientsBCC);
            if(list != null)
                msg.setRecipients(javax.mail.Message.RecipientType.BCC, list);
            msg.setSubject(subject);
            Multipart mp = new MimeMultipart();
            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setText(contentText);
            String type = getTypeFromContent(contentText);
            if(type.indexOf("text/html") != -1)
                contentText = "<meta http-equiv=Content-Type content=text/html;charset=gb2312>" + contentText;
            mbp1.setContent(contentText, type);
            mp.addBodyPart(mbp1);
            MimeBodyPart mbp2;
            
            for(Object element:filename)
            {
                mbp2 = new MimeBodyPart();
                FileDataSource fds = new FileDataSource(element.toString());
                mbp2.setDataHandler(new DataHandler(fds));
                mbp2.setFileName(MimeUtility.encodeText(fds.getName(), "GB2312", "B"));
                mp.addBodyPart(mbp2);
            }

            msg.setContent(mp);
            msg.setSentDate(new Date());
            Transport.send(msg);
            return true;
        }
        catch(MessagingException mex)
        {
            mex.printStackTrace();
            Exception ex = null;
            if((ex = mex.getNextException()) != null)
                ex.printStackTrace();
        }
        catch(UnsupportedEncodingException eex)
        {
            eex.printStackTrace();
        }
        return false;
    }

    public Message[] getMail()
        throws Exception
    {
        MailAuthenticator ma = new MailAuthenticator(userName, pwd);
        Properties prop = new Properties();
        prop.put("mail.pop3.host", host);
        Session session = Session.getDefaultInstance(prop, ma);
        store = session.getStore("pop3");
        store.connect(host, userName, pwd);
        inbox = store.getDefaultFolder().getFolder("INBOX");
        inbox.open(2);
        Message msg[] = inbox.getMessages();
        FetchProfile profile = new FetchProfile();
        profile.add(javax.mail.FetchProfile.Item.ENVELOPE);
        inbox.fetch(msg, profile);
        return msg;
    }

    private void handle(Message msg)
        throws Exception
    {
        logger.info("\u90AE\u4EF6\u4E3B\u9898:" + msg.getSubject());
        logger.info("\u90AE\u4EF6\u4F5C\u8005:" + MimeUtility.decodeText(msg.getFrom()[0].toString()));
        logger.info("\u53D1\u9001\u65E5\u671F:" + msg.getSentDate());
    }

    public void handleText(Message msg)
        throws Exception
    {
        handle(msg);
        logger.info("\u90AE\u4EF6\u5185\u5BB9:" + msg.getContent());
    }

    public void handleText(Part part)
        throws Exception
    {
        logger.info("\u90AE\u4EF6\u5185\u5BB9:" + part.getContent());
    }

    public void getMailContent(Part part)
        throws Exception
    {
        String contenttype = part.getContentType();
        int nameindex = contenttype.indexOf("name");
        boolean conname = false;
        if(nameindex != -1)
            conname = true;
        bodytext = new StringBuilder();
        logger.info("CONTENTTYPE: " + contenttype);
        if(part.isMimeType("text/plain") && !conname)
            bodytext.append((String)part.getContent());
        else
        if(part.isMimeType("multipart/*"))
        {
            Multipart multipart = (Multipart)part.getContent();
            int counts = multipart.getCount();
            for(int i = 0; i < counts; i++)
                getMailContent(((Part) (multipart.getBodyPart(i))));

        } else
        if(part.isMimeType("message/rfc822"))
            getMailContent((Part)part.getContent());
    }

    public void handleMultipart(Message msg)
        throws Exception
    {
        Multipart mp = (Multipart)msg.getContent();
        int mpCount = mp.getCount();
        handle(msg);
        for(int m = 0; m < mpCount; m++)
        {
            BodyPart part = mp.getBodyPart(m);
            getMailContent(part);
            String disposition = part.getDisposition();
            if(disposition != null && disposition.equals("attachment"))
                saveAttach(part);
            else
                logger.info(bodytext);
        }

    }

    private void saveAttach(BodyPart part)
        throws Exception
    {
        String temp = part.getFileName();
        temp = MimeUtility.decodeText(temp);
        logger.info("\u6709\u9644\u4EF6:" + temp);
        InputStream in = part.getInputStream();
        FileOutputStream writer =null;
        try{
        writer=new FileOutputStream(new File("c:\\mytest\\" + temp));
        for(byte content[] = new byte[255]; in.read(content) != -1; writer.write(content));
        
      }finally
      {
      	try{writer.close();}catch(Exception e){}
        try{in.close();}catch(Exception e){}
       }
    }
    private String getSourceString(String rawStr)
    {
        boolean debug = true;
        String str = rawStr;
        String language = getLanguage(str);
        if(debug)
        {
            logger.info("language=" + language);
            logger.info("source=" + str);
        }
        try
        {
            str = MimeUtility.decodeText(str);
        }
        catch(Exception e)
        {
            if(debug)
                logger.info(e);
        }
        if(debug)
            logger.info("mime decode str=" + str);
        if(debug)
            logger.info("final source str=" + str);
        return str;
    }

    private String getLanguage(String rawStr)
    {
        String language = "gb2312";
        String str = rawStr.toLowerCase();
        int start = str.indexOf("=?");
        int end = str.indexOf("?=");
        if(end >= start)
        {
            String tmpStr = str.substring(start, end);
            if(tmpStr.indexOf("8859-1") != -1)
                language = "8859_1";
            else
            if(tmpStr.indexOf("gb2312") != -1)
                language = "gb2312";
        }
        return language;
    }

    private String base64Decoder(String s)
        throws Exception
    {
        BASE64Decoder decoder = new BASE64Decoder();
        byte b[] = decoder.decodeBuffer(s);
        return new String(b);
    }

    public void close()
        throws Exception
    {
        try{if(inbox != null) inbox.close(false);}catch(Exception e){}
        try{if(store != null) store.close();}catch(Exception e){}
    }

    public static void main(String args[])
        throws UnsupportedEncodingException
    {
        Mail mail = new Mail("zelin.deng@dragonflow.com");
        mail.setUsername("zelin.deng@dragonflow.com");
        mail.setPwd("zelin.deng");
        mail.addRecipientTO("zelin.deng@dragonflow.com");
        mail.addRecipientCC("zelin_deng@163.net");
        mail.addRecipientBCC("zl_deng@sina.com");
        String content = "hello xixi";
        mail.setContentText(content);
        mail.setSubject("\u4F60\u597Djerry");
        boolean success = mail.sendMail();
        if(success)
            logger.info("success");
        else
            logger.info("failure");
    }

    private String to;
    private String userName;
    private StringBuilder bodytext;
    private String pwd;
    private Folder inbox;
    private Store store;
    private String from;
    private String host;
    private String contentText;
    private boolean debug;
    private String subject;
    private ArrayList filename;
    private ArrayList recipientsTO;
    private ArrayList recipientsCC;
    private ArrayList recipientsBCC;
}
