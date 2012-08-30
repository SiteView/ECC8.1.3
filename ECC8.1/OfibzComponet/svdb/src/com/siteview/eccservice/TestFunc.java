package com.siteview.eccservice;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

public class TestFunc
{
	public static void main(String argv[])
	{
	}
	
	public RetMapInMap test88(Map<String, String> inwhat)
	{
		try
		{
			return test89(inwhat);
		} catch (Exception e)
		{
			boolean ret=false;
			StringBuilder estr=new StringBuilder(" Exception to EmailTest java; ");
			Map<String, Map<String, String>> fmap = new HashMap<String, Map<String, String>>();
			Map<String, String> ndata1 = new HashMap<String, String>();
			ndata1.put("return", "false");
			fmap.put("return", ndata1);			
			return new RetMapInMap(ret, estr.toString(), fmap);
		}
	}
	
	
	public RetMapInMap test89(Map<String, String> inwhat)
	{
		long start = System.currentTimeMillis();
		
		boolean ret=false;
		StringBuilder estr=new StringBuilder();
		Map<String, Map<String, String>> fmap = new HashMap<String, Map<String, String>>();
		
		String mailServer = new String("");
		if(inwhat.containsKey("mailServer"))
			mailServer= inwhat.get("mailServer");
		
		String mailTo = new String("");
		if(inwhat.containsKey("mailTo"))
			mailTo= inwhat.get("mailTo");
		
		if (mailServer.isEmpty() || mailTo.isEmpty())
		{
			estr.append(" 传入的 mailServer 或 mailTo 为空; ");
			HashMap<String, String> ndata1 = new HashMap<String, String>();
			ndata1.put("return", "false");
			fmap.put("return", ndata1);
			return new RetMapInMap(ret, estr.toString(), fmap);
		}
		
		String mailFrom = new String("");
		if(inwhat.containsKey("mailFrom"))
			mailFrom= inwhat.get("mailFrom");
		
		String subject = new String("");
		if(inwhat.containsKey("subject"))
			subject= inwhat.get("subject");
		
		String content = new String("");
		if(inwhat.containsKey("content"))
			content= inwhat.get("content");
		
		String user = new String("");
		if(inwhat.containsKey("user"))
			user= inwhat.get("user");
		
		String password = new String("");
		if(inwhat.containsKey("password"))
			password= inwhat.get("password");
		
		String mailRepeatTimes = new String("");
		if(inwhat.containsKey("mailRepeatTimes"))
			mailRepeatTimes= inwhat.get("mailRepeatTimes");	
		int times = 1;
		if (!mailRepeatTimes.isEmpty())
			times = new Integer(mailRepeatTimes);
		if (times>100)
			times= 100;
		if(times<1)
			times=1;
		
		Integer count= new Integer(0);
		
		String source= mailTo;
		StringTokenizer stsource = new StringTokenizer(source, ",");
		while (stsource.hasMoreTokens())
		{
			String mto = stsource.nextToken();
			if( test92(false,times,mailServer,mto,mailFrom,subject,content,user,password) )
				count+= times;
		}
		if (count.intValue() > 0)
			ret = true;
		HashMap<String, String> ndata1 = new HashMap<String, String>();
		if (ret)
			ndata1.put("return", "true");
		else
			ndata1.put("return", "false");
		ndata1.put("mail_out_count", count.toString());
		ndata1.put("byJava", "true" );
		fmap.put("return", ndata1);
		SystemOut.println(" Sent out "+count+" mail.  J ");
		SystemOut.println("EccService GetUnivData dowhat: EmailTest\n                         /ret: " + ret + "   run:" + (float) (System.currentTimeMillis() - start) / 1000 + " s");
		return new RetMapInMap(ret, estr.toString(), fmap);	
	}
	
	public boolean test92(boolean auth,int repeat,String host,String to,String from,String sub,String content,String user,String pwd)
	{
		try
		{
			JavaMailSenderImpl sender = new JavaMailSenderImpl();
			sender.setHost(host);  

			Properties prop = new Properties() ;
			if (auth)
			{
				prop.put("mail.smtp.auth", "true");
				sender.setUsername(user);  
				sender.setPassword(pwd);	
			}
			sender.setJavaMailProperties(prop) ;
				
			MimeMessage message = sender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);
			helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject(sub);
			helper.setText(content);
			
			if (repeat > 100)
				repeat = 100;
			
			for (int i = 1; i <= repeat; ++i)
				sender.send(message);
			
		} catch (Exception e)
		{
			if(auth)
				return false;
			return test92(true,repeat,host,to,from,sub,content,user,pwd);
		}
		return true;
	}
	
	
}
