package com.siteview.ecc.reportserver;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.siteview.ecc.util.Toolkit;

//import com.me.util.*;

public class sendMail 
{
	private MimeMessage mimeMsg; //MIME�ʼ�����

	private Session session; //�ʼ��Ự���� 

	private Properties props; //ϵͳ���� 

	private boolean needAuth = false; //smtp�Ƿ���Ҫ��֤

	private String username = ""; //smtp��֤�û��������� 

	private String password = ""; 

	private Multipart mp; //Multipart����,�ʼ�����,����,���������ݾ���ӵ����к�������MimeMessage���� 

//	private String mailHost = "";

	/** 
	* 
	*/ 
	public sendMail() 
	{	
//		setSmtpHost(getConfig.mailHost);//���û��ָ���ʼ�������,�ʹ�getConfig���л�ȡ
//		setSmtpHost(mailHost);
		createMimeMessage();
	} 

	public sendMail(String smtp)
	{
		setSmtpHost(smtp);
		createMimeMessage();
	} 

	/** 
	* @param hostName String 
	*/ 
	public void setSmtpHost(String hostName) 
	{
		Toolkit.getToolkit().getLoger().info("����ϵͳ���ԣ�mail.smtp.host = "+hostName); 
	
		if(props == null)
			props = System.getProperties(); //���ϵͳ���Զ���
	
		props.put("mail.smtp.host",hostName); //����SMTP����
	} 

	/** 
	* @return boolean 
	*/ 
	public boolean createMimeMessage() 
	{ 
		try
		{
			Toolkit.getToolkit().getLoger().info("׼����ȡ�ʼ��Ự����");
			session = Session.getDefaultInstance(props, null); //����ʼ��Ự����
		}
		catch(Exception e)
		{
			System.err.println("��ȡ�ʼ��Ự����ʱ��������"+e);
			return false; 
		} 

		Toolkit.getToolkit().getLoger().info("׼������MIME�ʼ�����"); 

		try
		{
			mimeMsg = new MimeMessage(session); //����MIME�ʼ�����
			mp = new MimeMultipart();
	
			return true; 
		}	
		catch(Exception e)
		{	
			System.err.println("����MIME�ʼ�����ʧ�ܣ�"+e);
			return false; 
		}
	} 

	/** 
	* @param need boolean 
	*/ 
	public void setNeedAuth(boolean need) 
	{
		Toolkit.getToolkit().getLoger().info("����smtp�����֤��mail.smtp.auth = "+need);
		
		if(props == null)
			props = System.getProperties(); 
	
		if(need)
		{
			props.put("mail.smtp.auth","true");
		}
		else
		{
			props.put("mail.smtp.auth","false");
		}
	} 
	
	/** 
	* @param name String 
	* @param pass String 
	*/ 
	public void setNamePass(String name,String pass) 
	{ 
		username = name; 
		password = pass; 
	} 

	/** 
	* @param mailSubject String 
	* @return boolean 
	*/ 
	public boolean setSubject(String mailSubject) 
	{ 
		Toolkit.getToolkit().getLoger().info("�����ʼ����⣡"); 
		try
		{ 
			mimeMsg.setSubject(mailSubject); 
			return true; 
		} 
		catch(Exception e) 
		{ 
			System.err.println("�����ʼ����ⷢ������"); 
			return false;
		}
	} 

	/** 
	* @param mailBody String 
	*/ 
	public boolean setBody(String mailBody) 
	{ 
		try
		{ 
			BodyPart bp = new MimeBodyPart(); 
			bp.setContent(""+mailBody,"text/html;charset=GB2312"); 
			mp.addBodyPart(bp); 
			return true; 
		}
		catch(Exception e)
		{ 
			System.err.println("�����ʼ�����ʱ��������"+e); 
			return false; 
		} 
	} 

	/** 
	* @param name String 
	* @param pass String 
	*/ 
	public boolean addFileAffix(String filename) 
	{ 
		Toolkit.getToolkit().getLoger().info("�����ʼ�������"+filename); 

		try
		{ 
			BodyPart bp = new MimeBodyPart(); 
			FileDataSource fileds = new FileDataSource(filename); 
			bp.setDataHandler(new DataHandler(fileds)); 
			bp.setFileName(fileds.getName()); 
			mp.addBodyPart(bp); 
			return true; 
		} 
		catch(Exception e)
		{ 
			System.err.println("�����ʼ�������"+filename+"��������"+e); 
			return false; 
		} 
	} 

	/** 
	* @param name String 
	* @param pass String 
	*/ 
	public boolean setFrom(String from) 
	{ 
		Toolkit.getToolkit().getLoger().info("���÷����ˣ�"); 
	
		try
		{ 
			mimeMsg.setFrom(new InternetAddress(from)); //���÷����� 
			return true; 
		} 
		catch(Exception e) 
		{ 
			return false; 
		} 
	} 

	/** 
	* @param name String 
	* @param pass String 
	*/ 
	public boolean setTo(String to)
	{ 
		if(to == null)
			return false; 
	
		try
		{
			mimeMsg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to)); 
			return true; 
		}
		catch(Exception e) 
		{
			return false; 
		} 
	} 


	/** 
	* @param name String 
	* @param pass String 
	*/ 
	public boolean setCopyTo(String copyto) 
	{ 
		if(copyto == null)
			return false; 

		try
		{ 
			mimeMsg.setRecipients(Message.RecipientType.CC,(Address[])InternetAddress.parse(copyto)); 
			return true; 
		} 
		catch(Exception e) 
		{
			return false; 
		}
	} 

	/** 
	* @param name String 
	* @param pass String 
	*/ 
	public boolean sendout()
	{ 
		try
		{ 
			mimeMsg.setContent(mp); 
			mimeMsg.saveChanges(); 
			Toolkit.getToolkit().getLoger().info("���ڷ����ʼ�...."); 
		
			Session mailSession = Session.getInstance(props,null); 
			Transport transport =null;
			try{
			transport = mailSession.getTransport("smtp"); 
			transport.connect((String)props.get("mail.smtp.host"),username,password); 
			transport.sendMessage(mimeMsg,mimeMsg.getRecipients(Message.RecipientType.TO)); 
			//transport.send(mimeMsg); 

			Toolkit.getToolkit().getLoger().info("�����ʼ��ɹ���"); 
		}finally{
			transport.close(); 
		}
			return true;
		} 
		catch(Exception e)
		{ 
			System.err.println("�ʼ�����ʧ�ܣ�"+e);
			return false;
		} 
	} 

	/** 
	* Just do it as this 
	*/ 
	public static void main(String[] args) 
	{
		String mailbody = "�й���"+ "	csdn"; 
		sendMail themail = new sendMail("mail.dragonflow.com"); 
		themail.setNeedAuth(true); 
	
		if(themail.setSubject("����") == false) 
			return; 
	
		if(themail.setBody(mailbody) == false) 
			return; 
	
		if(themail.setTo("hailong.yi@dragonflow.com") == false) 
			return; 
	
		if(themail.setFrom("hailong.yi@dragonflow.com") == false) 
			return; 
	
		if(themail.addFileAffix("c:\\boot.ini") == false) 
			return; 
	
		themail.setNamePass("hailong.yi","password"); 
	
		if(themail.sendout() == false) 
			return;
	}
}
