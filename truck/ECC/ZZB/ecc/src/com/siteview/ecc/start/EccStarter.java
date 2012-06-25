package com.siteview.ecc.start;

import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * 
 * ����Ҫ��ʼ�������õ�web.xml�м���
 *
 */
public class EccStarter extends HttpServlet {

	private ServletContext context;
	private String webDir;
	private static EccStarter eccStarter=new EccStarter();
	
	public EccStarter()
	{
		super();
		eccStarter=this;
	}
	
	/**
	 * ������������ģʽ���Գ����ڳ����ͷ����������伴�ɣ��ֶ����� web dir ��
	 * <br/> EccStarter s= new EccStarter("D:\\jb\\svDCM\\ecc_zk\\ecc\\WebContent");
	 */
	public EccStarter(String web_dir)
	{
		super();
		eccStarter=this;
		webDir= web_dir;
	}
	public static EccStarter getInstance()
	{
		return eccStarter;
	}
	public ServletContext getContext()
	{
		return context;
	}
	public void init(ServletConfig config) throws ServletException 
	{
		super.init(config);
		context=config.getServletContext();
		webDir=context.getRealPath("/");
			
		String starterListenersName[]=config.getInitParameter("uniqueStarterListenerNameList").split(",");
		String starterListenersClass[]=config.getInitParameter("starterListenersClass").split(",");
		for(int i=0;i<starterListenersName.length;i++)
		{
			try
			{
				Object listener=Class.forName(starterListenersClass[i]).newInstance();

				((StarterListener)listener).startInit(this);

				context.setAttribute(starterListenersName[i], listener);
				
			}catch(Exception e)
			{
				System.err.println("EccStarter Error:"+e.getMessage());
				e.printStackTrace();
			}
			
		}
		
	} 
	public StarterListener getStarterListener(String uniqueStarterListenerName)
	{
		return (StarterListener)context.getAttribute(uniqueStarterListenerName);
	}
	public String getRealPath(String path)
	{
		return context.getRealPath(path);
	}
	public String getWebDir()
	{
		return webDir;
	}
	public static void main(String[] args) {
	}
}
