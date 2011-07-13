package com.siteview.ecc.tuopu;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import com.siteview.base.data.IniFile;
import com.siteview.ecc.treeview.EccWebAppInit;
import com.siteview.ecc.util.ColorPopupSelect;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class tuopuset extends GenericForwardComposer
{
	
	private Textbox			c1, c2, c3;
	private Spinner			w1, w2, w3;
	private Toolbarbutton	b1, b2, b3;
	private Panelchildren	pc;
	ColorPopupSelect		dc	= new ColorPopupSelect();
	HashMap<String, HashMap<String, String>> mapIn;
	public tuopuset()
	{
		
	}
	
	public void onCreate$pc()
	{
		
		IniFile iniGen = new IniFile("tuopuset.ini");
		try
		{
			iniGen.load();
		} catch (Exception e1)
		{
		}
		if (iniGen.getSectionList().isEmpty())
		{
			c1.setValue("red");
			c2.setValue("yellow");
			c3.setValue("green");
			w1.setValue(3);
			w2.setValue(3);
			w3.setValue(0);
		} else
		{
			String tempc = "";
			tempc = iniGen.getValue("tuopusetid", "c1");
			c1.setValue(tempc);
			tempc = iniGen.getValue("tuopusetid", "c2");
			c2.setValue(tempc);
			tempc = iniGen.getValue("tuopusetid", "c3");
			c3.setValue(tempc);
			int tempw = 0;
			tempc = iniGen.getValue("tuopusetid", "w1");
			tempw = Integer.parseInt(tempc);
			w1.setValue(tempw);
			tempc = iniGen.getValue("tuopusetid", "w2");
			tempw = Integer.parseInt(tempc);
			w2.setValue(tempw);
			tempc = iniGen.getValue("tuopusetid", "w3");
			tempw = Integer.parseInt(tempc);
			w3.setValue(tempw);
		}
		b1.setStyle("color:Black;background:" + c1.getValue());
		b2.setStyle("color:Black;background:" + c2.getValue());
		b3.setStyle("color:Black;background:" + c3.getValue());
		dc.setId("colorid");
		dc.onCreate();
		dc.setParent(pc);
		
	}
	public void onClick$btnreturn()
	{
		IniFile iniGen = new IniFile("tuopuset.ini");
		try
		{
			iniGen.load();
		} catch (Exception e1)
		{
		}
		try
		{
			if (iniGen.getSectionList() == null || iniGen.getSectionList().isEmpty())
			{
				iniGen.createSection("tuopusetid");
			}
			iniGen.setKeyValue("tuopusetid", "c1", "red");
			iniGen.setKeyValue("tuopusetid", "c2","yellow");
			iniGen.setKeyValue("tuopusetid", "c3", "green");
			iniGen.setKeyValue("tuopusetid", "w1", "3");
			iniGen.setKeyValue("tuopusetid", "w2", "3");
			iniGen.setKeyValue("tuopusetid", "w3", "0");
			iniGen.saveChange();
			c1.setValue("red");
			c2.setValue("yellow");
			c3.setValue("green");
			w1.setValue(3);
			w2.setValue(3);
			w3.setValue(0);
			b1.setStyle("color:Black;background:red" );
			b2.setStyle("color:Black;background:yellow" );
			b3.setStyle("color:Black;background:green" );
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public void onClick$b1()
	{
		getc(c1,b1);
		
		// Executions.getCurrent().sendRedirect("/main/setting/colorselect.jsp", "_blank");
	}
	
	private void getc(Textbox tx,Toolbarbutton btn)
	{
		
		dc.setAttribute("ctx", tx);
		dc.setAttribute("btn", btn);
		dc.open(tx);
		
	}
	
	public void onClick$b2()
	{
		getc(c2,b2);
		// Executions.getCurrent().sendRedirect("/main/setting/colorselect.jsp", "_blank");
	}
	
	public void onClick$b3()
	{
		getc(c3,b3);
		// Executions.getCurrent().sendRedirect("/main/setting/colorselect.jsp", "_blank");
	}
	
	 public void MakeTuopuData(String strName)
	    {
			try
			{
				
				//ԭMaketuopudata���߼�:main/setting/tuopusample/
				String strPath = EccWebAppInit.getWebDir() + "main\\setting\\tuopusample\\";
				strPath += strName;			
				strPath += ".files\\data.xml";
				//��ȡ*.files\vml_*.tpl����������״̬���´���vml_*.html�Ըı�ڵ���ɫ�ȡ�
				String strTplFile = strPath;
				String strNew = "", strOld = "", strTmpContent = "";
				String strSrcPath3 = EccWebAppInit.getWebDir() + 
				"main\\setting\\tuopusample\\" + strName + ".htm";
				
				String strContent = strSrcPath3;
				//���ñ߿��ȼ���ɫ
//				IniFile iniGen = new IniFile("tuopuset.ini");
//				try
//				{
//					iniGen.load();
//				} catch (Exception e1)
//				{
//				}
				String cc1,cc2,cc3;
				String ww1,ww2,ww3;
		
				cc1=c1.getValue();
				cc2=c2.getValue();
				cc3=c3.getValue();
				
				ww1=w1.getValue().toString();
				ww2=w2.getValue().toString();
				ww3=w3.getValue().toString();
				//��Ԥ����Ӧ��
//				if (iniGen.getSectionList().isEmpty())
//				{
//					c1="red";
//					c2="yellow";
//					c3="green";
//					w1="3";
//					w2="3";
//					w3="0";
//				} else
//				{
//					String tempc = "";
//					tempc = iniGen.getValue("tuopusetid", "c1");
//					c1=tempc;
//					tempc = iniGen.getValue("tuopusetid", "c2");
//					c2=tempc;
//					tempc = iniGen.getValue("tuopusetid", "c3");
//					c3=tempc;
//					tempc = iniGen.getValue("tuopusetid", "w1");
//					w1=tempc;
//					tempc = iniGen.getValue("tuopusetid", "w2");
//					w2=tempc;
//					tempc = iniGen.getValue("tuopusetid", "w3");
//					w3=tempc;
//				}
//				
				strNew = String.format("vml_%d.tpl", (1));
				strTplFile = strPath.replace("data.xml", strNew);
					//��tpl�ļ��� ����ȡ����
				strContent = readTxt(strTplFile, "UTF-8");
					
					//���ݺ�̨���ݸı�߿���ɫ��ShapeId + IP��Group�ȣ�
					for(int i=0;i<3;i++)
					{
						if (i==0)
						{
						strOld =String.format("fillcolor=\"%s\\+color\"", "SV_IP:127.0.0.1");
						strNew = String.format("filled=\"f\"  stroked=\"t\" strokecolor=\"%s\" fillcolor=\"%s\" strokeweight=\"%spt\"", cc1, cc1, ww1);
						if(!ww1.equals("0"))
						{
						strTmpContent = strContent.replaceAll(strOld, strNew);
						strContent = strTmpContent;
						}
						}
						if(i==1)
						{
							strOld =String.format("fillcolor=\"%s\\+color\"", "SV_IP:127.0.0.2");
							strNew = String.format("filled=\"f\"  stroked=\"t\" strokecolor=\"%s\" fillcolor=\"%s\" strokeweight=\"%spt\"", cc2, cc2, ww2);
							
							if(!ww2.equals("0"))
							{
							strTmpContent = strContent.replaceAll(strOld, strNew);							
							strContent = strTmpContent;		
							}
						}
						if(i==2)
						{
							strOld =String.format("fillcolor=\"%s\\+color\"", "SV_IP:127.0.0.3");
							strNew = String.format("filled=\"f\"  stroked=\"t\" strokecolor=\"%s\" fillcolor=\"%s\" strokeweight=\"%spt\"", cc3, cc3, ww3);
							
//							int index = strContent.indexOf(strOld);
							if(!ww3.equals("0"))
							{
							strTmpContent = strContent.replaceAll(strOld, strNew);							
							strContent = strTmpContent;
							}
						}
						
					}
							
					
					strTmpContent = strContent.replaceAll("href=\"#\"", "href=\"javascript:void(null)\"");
					strContent = strTmpContent;
					String strHtmFile = strTplFile.replace(".tpl", ".htm");
					createFile(strHtmFile, strContent, "UTF-8");
					Executions.getCurrent().sendRedirect("main/setting/tuopusample/sample.htm","_blank");
					
				
			}
			catch (Exception ex) 
			{
				
			}	
	    }
	 /**
	     * ��ȡ�ı��ļ�����
	     * @param filePathAndName ������������·�����ļ���
	     * @param encoding �ı��ļ��򿪵ı��뷽ʽ
	     * @return �����ı��ļ�������
	     */
	    public String readTxt(String filePathAndName,String encoding) throws IOException
	    {
		     encoding = encoding.trim();
		     StringBuffer str = new StringBuffer("");	     
		     String st = "";
		     FileInputStream fs=null;
		     InputStreamReader isr=null;
		     try
		     {
		      fs = new FileInputStream(filePathAndName);
		      if(encoding.equals("")){
		       isr = new InputStreamReader(fs);
		      }else{
		       isr = new InputStreamReader(fs,encoding);
		      }
		      BufferedReader br =null;
		      try
		      {
		       br= new BufferedReader(isr);	  
		       String data = "";
		       while((data = br.readLine())!=null)
		       {
		         str.append(data+"\r\n");	         
		       }
		      }
		      catch(Exception e)
		      {
		       str.append(e.toString());	       
		      }finally{
		    	  try{br.close();}catch(Exception e){}
		      }
		      st = str.toString();
		     }
		     catch(IOException es)
		     {
		      st = "";
		     }finally
		     {
		     	try{isr.close();}catch(Exception e){}
		     	try{fs.close();}catch(Exception e){}
		    }
		     
		     return st;     
	    } 
	 /**  
		 * �б��뷽ʽ���ļ�����  
		 * @param filePathAndName �ı��ļ���������·�����ļ���  
		 * @param fileContent �ı��ļ�����  
		 * @param encoding ���뷽ʽ ���� GBK ���� UTF-8  
		 * @return  
		 */  
		public void createFile(String filePathAndName, String fileContent, String encoding) {   
		    
				    PrintWriter myFile=null;
				    try {   
				        String filePath = filePathAndName;   
				        filePath = filePath.toString();   
				        File myFilePath = new File(filePath);   
				        if (!myFilePath.exists()) {   
				            myFilePath.createNewFile();   
				        }   
				        myFile = new PrintWriter(myFilePath,encoding);   
				        String strContent = fileContent;   
				        myFile.println(strContent);   
				        
				    }   
				    catch (Exception e) {   
				       String message = "�����ļ���������";   
				        }
				    finally
				    {
				    	try{myFile.close();}catch(Exception e){}
				    }       
		    }  
	
	    
		//////////////////////�ļ���������///////////////////////////////////////    
	public void onClick$btnlook()
	{
		MakeTuopuData("sample");
	}
	
	public void onClick$btntuopu()
	{
		IniFile iniGen = new IniFile("tuopuset.ini");
		try
		{
			iniGen.load();
		} catch (Exception e1)
		{
		}
		try
		{
			if (iniGen.getSectionList() == null || iniGen.getSectionList().isEmpty())
			{
				iniGen.createSection("tuopusetid");
			}
			iniGen.setKeyValue("tuopusetid", "c1", c1.getValue());
			iniGen.setKeyValue("tuopusetid", "c2", c2.getValue());
			iniGen.setKeyValue("tuopusetid", "c3", c3.getValue());
			iniGen.setKeyValue("tuopusetid", "w1", w1.getValue().toString());
			iniGen.setKeyValue("tuopusetid", "w2", w2.getValue().toString());
			iniGen.setKeyValue("tuopusetid", "w3", w3.getValue().toString());
			iniGen.saveChange();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
