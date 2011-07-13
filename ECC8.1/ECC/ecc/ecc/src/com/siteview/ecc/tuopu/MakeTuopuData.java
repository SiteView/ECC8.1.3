package com.siteview.ecc.tuopu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.zkoss.zk.ui.Desktop;

import com.siteview.base.data.IniFile;
import com.siteview.base.data.QueryInfo;
import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.treeview.EccWebAppInit;
import com.siteview.ecc.util.Toolkit;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

	public class MakeTuopuData 
	{	
		private final static Logger logger = Logger.getLogger(MakeTuopuData.class);
	    private Document domTree = null;
	    
	    private String message;
	    private String strSessionId;
	    
	    Map<String, Map<String, String>> mapIn;
	    Desktop desktop=null;
	    
	    public MakeTuopuData(Desktop desktop, String strName)
	    {
			try
			{
				this.desktop = desktop;
				
				strSessionId = desktop.getSession().getAttribute("usersessionid").toString();
				
	////////////////////////////////////////////
				
				//ԭShowTuopu���߼�:
				
				//����frameset.js��main_2.htm��widgets.htm���ļ���tuopu �ļ�Ŀ¼
				String strSrcPath1 = EccWebAppInit.getWebDir() + 
				"main\\tuoplist\\bak\\main_2.htm";
				String strSrcPath2 = EccWebAppInit.getWebDir() + 
				"main\\tuoplist\\bak\\frameset.js";
				
				String strDestPath1 = EccWebAppInit.getWebDir() + 
				"main\\tuoplist\\" + strName + ".files\\main_2.htm";
				String strDestPath2 = EccWebAppInit.getWebDir()
				+ "main\\tuoplist\\" + strName + ".files\\frameset.js";
				
	
				delFile(strDestPath1);
				delFile(strDestPath2);
				
	//			copyFile(strSrcPath1, strDestPath1);
				copyFile(strSrcPath2, strDestPath2);
				
				//��ȡ../tuoplist/�µ�strPageid.htm���var g_FileList  
				//�����滻../tuoplist/strPageid.files/main_2.htm���var g_FileList��ֵ��
				String strSrcPath3 = EccWebAppInit.getWebDir() + 
				"main\\tuoplist\\" + strName + ".htm";
				
				String strContent = readTxt(strSrcPath3, "UTF-8");
				
	//			int nStartPos = strContent.indexOf("new FileEntry");
	//			int nEndPos = strContent.substring(nStartPos - 1).indexOf(");");
	//
	//			String strGfileList = strContent.substring(nStartPos - 1).substring(0, nEndPos);
	
				//����main_2.html�е�mulu.filesΪָ�����˵���ȷ�ļ�·�����ݹ��滻��
				String strContent1 = readTxt(strSrcPath1, "UTF-8");
				String strReplaceId = "../";
				strReplaceId += strName;
				strReplaceId += ".files/";
				
				String strContentTmp = strContent1.replaceAll("../tuoplist/mulu.files/", strReplaceId);
				
				//��strPageid.htm���var g_FileList���滻
				//../tuoplist/strPageid.files/main_2.htm���var g_FileList��ֵ��
				//String strContentMain = strContentTmp.replaceAll("ReplaceFileEntry", strGfileList);
				
				//��������Ƿ���Բ����˲����ˣ�
				createFile(strDestPath1, strContentTmp);
				
				//����˵���ʾλ�����⣬ ��Tomact��visio.css���ִ�Сд�� ԭ����Visio.css����������
				//�趯̬�����ơ�
				String strCssPath = EccWebAppInit.getWebDir() + "main\\tuoplist\\";
				strCssPath += strName;
				strCssPath += ".files\\visio.css";
				
	//			File cssfile = new File(strCssPath);			
	//			if(!cssfile.exists())
				{
					String strCssOldath = EccWebAppInit.getWebDir() + "main\\tuoplist\\";
					strCssOldath += strName;
					strCssOldath += ".files\\Visio.css";
					
					File cssfile = new File(strCssOldath);
					cssfile.renameTo(new File(strCssPath));
				}
				
	////////////////////////////////////////////
				
				//ԭMaketuopudata���߼�:
	//			domTree = new Document();
				String strPath = EccWebAppInit.getWebDir() + "main\\tuoplist\\";
				strPath += strName;			
				strPath += ".files\\data.xml";
				
		    	//1����ʼ������ȡdata.xml����
				this.domTree = ReadXml(strPath);
		        String xpathString = ".//Page";  
		        
				NodeList pPageList = selectNodes(domTree, xpathString);
				mapIn =  new HashMap();
				
				//2��ȡ�������û������shape����
				for (int i=0; i<pPageList.getLength(); i++) 
				{
					Node pPage = pPageList.item(i);
					
					xpathString = ".//Shape";
					NodeList pShapeList = selectNodes(pPage, xpathString);
					for (int j=0; j<pShapeList.getLength(); j++)
					{
						Node pShape = pShapeList.item(j);
						if(IsHyperlinkNodesExisted(pShape))
						{
							Map<String, String> value = new LinkedHashMap(); 
							value.put("Des", RetrievePropertyValue(pShape, "SV_Des"));
							value.put("IP", RetrievePropertyValue(pShape, "SV_IP"));
							value.put("Link", RetrievePropertyValue(pShape, "SV_Link"));
	//						value.put("SV_App", RetrievePropertyValue(pShape, "SV_App"));
							value.put("Group", RetrievePropertyValue(pShape, "SV_Group"));
							value.put("Entity", RetrievePropertyValue(pShape, "SV_Entity"));
							value.put("Monitor", RetrievePropertyValue(pShape, "SV_Monitor"));
							
							mapIn.put(pShape.getAttributes().getNamedItem("ID").getNodeValue(), value);
						}
					}
				}
				
				//������Ϣ
				logger.info("mapIn:");
				for(String key1 : mapIn.keySet())
		        {	
					logger.info(key1);
					
					for(String key2 : mapIn.get(key1).keySet())
		            {
		                if (!mapIn.get(key1).get(key2).equals(""))
		                {
		                	logger.info("     " + key2 + " : " + mapIn.get(key1).get(key2).toString());
		                }
		            }
		        }
				
				//3���ӷ�������������shape������ص�����
				Map<String, Map<String, String>> result = GetTuopuDataFromServer(mapIn);
		        
				//û������Ļ��ҵ���Ϣ
				Map<String,Map<String, String>> resultCopy = new HashMap<String,Map<String, String>>();
				for(String key1 : result.keySet())
		        {	
					Map<String, String> sonLinkedHashMap = new LinkedHashMap<String, String>();
	
					List<String> bad_keys = new ArrayList<String>();
					List<String> disable_keys = new ArrayList<String>();
					List<String> error_keys = new ArrayList<String>();
					List<String> warning_keys = new ArrayList<String>();
					List<String> ok_keys = new ArrayList<String>();
					List<String> other_keys = new ArrayList<String>();
					List<String> all_keys = new ArrayList<String>();
	
					for(String key2 : result.get(key1).keySet())
		            {
	//				     monitor46Svid : 1.50.3.131
	//				     monitor46Des : Service��ClipBook �Ƿ�������=False, ����״̬=Stopped, ����״̬=OK, ��Ӧ�Ľ�������=NA, ����ʵ������(��)=0, 
	//				     monitor46State : error
	
						String value = result.get(key1).get(key2);
						if("".equals(value)){
							continue;
						}
						if(key2.startsWith("monitor")){
							if("bad".equals(value)){
								String temp = key2.replace("State", "");
								bad_keys.add(temp+"Svid");
								bad_keys.add(temp+"Des");
								bad_keys.add(temp+"State");
							}else if("disable".equals(value)){
								String temp = key2.replace("State", "");
								disable_keys.add(temp+"Svid");
								disable_keys.add(temp+"Des");
								disable_keys.add(temp+"State");
							}else if("error".equals(value)){
								String temp = key2.replace("State", "");
								error_keys.add(temp+"Svid");
								error_keys.add(temp+"Des");
								error_keys.add(temp+"State");
							}else if("warning".equals(value)){
								String temp = key2.replace("State", "");
								warning_keys.add(temp+"Svid");
								warning_keys.add(temp+"Des");
								warning_keys.add(temp+"State");
							}else if("ok".equals(value)){
								String temp = key2.replace("State", "");
								ok_keys.add(temp+"Svid");
								ok_keys.add(temp+"Des");
								ok_keys.add(temp+"State");
							}
						}else
						{
							other_keys.add(key2);
						}
		            }
					//��������� key д��ֵ
					if(bad_keys.size()>1){
						all_keys.addAll(bad_keys);
					}
					if(error_keys.size()>1){
						all_keys.addAll(error_keys);
					}
					if(disable_keys.size()>1){
						all_keys.addAll(disable_keys);
					}
					if(warning_keys.size()>1){
						all_keys.addAll(warning_keys);
					}
					if(ok_keys.size()>1){
						all_keys.addAll(ok_keys);
					}
					if(other_keys.size()>1){
						all_keys.addAll(other_keys);
					}
					
					for(String key2 : all_keys){
						sonLinkedHashMap.put(key2, result.get(key1).get(key2).toString());
					}				
					resultCopy.put(key1, sonLinkedHashMap);
		        }
				
				
				
				
				//������Ϣ
				logger.info("GetTuopuDataFromServer result:");			
				StringBuffer strDebugContent = new StringBuffer("");
				for(String key1 : result.keySet())
		        {	
					logger.info(key1);
	
					strDebugContent.append(key1);
					strDebugContent.append("\r\n");
	
					for(String key2 : result.get(key1).keySet())
		            {
		                if (!result.get(key1).get(key2).equals(""))
		                {
		                	logger.info("     " + key2 + " : " + result.get(key1).get(key2).toString());
		                	
		                	strDebugContent.append("     " + key2 + " : " + result.get(key1).get(key2).toString());
		    				strDebugContent.append("\r\n");	                	
		                }
		            }
					
	//				delFile("e:\\tuopudatadebug.txt");
	//				createFile("e:\\tuopudatadebug.txt", strDebugContent.toString());
		        }
				//������Ϣ
				for(String key1 : resultCopy.keySet())
				{	
					logger.info(key1);
				
					strDebugContent.append(key1);
					strDebugContent.append("\r\n");
				
					for(String key2 : resultCopy.get(key1).keySet())
				    {
				        if (!resultCopy.get(key1).get(key2).equals(""))
				        {
				        	logger.info("     " + key2 + " : " + resultCopy.get(key1).get(key2).toString());
				        	
				        	strDebugContent.append("     " + key2 + " : " + resultCopy.get(key1).get(key2).toString());
							strDebugContent.append("\r\n");	                	
				        }
				    }
					
				//	delFile("e:\\tuopudatadebug.txt");
				//	createFile("e:\\tuopudatadebug.txt", strDebugContent.toString());
				}
	
	
			
				//���ݺ�̨���ݹ����µĲ˵��ڵ㣨���ݼ����״̬�ȣ�
				for(String key1 : resultCopy.keySet())
		        {
					//ɾ��ԭ�������в˵��ڵ㣨���ݼ����״̬�ȣ�
	//				Node pShapeNode = this.domTree.getElementById(key1);
					String strSelect = String.format("//Shape[@ID=\"%s\"]", key1);
			    	Node pShapeNode  = selectSingleNode(this.domTree, strSelect);
			    	
					if(pShapeNode == null)
						continue;
	
					//ɾ��ԭ��������
					DeleteHyperlinkNodes(pShapeNode);
					
					
					Node pMenuNode  = selectSingleNode(pShapeNode, "Scratch/B/SolutionXML/HLURL:Hyperlinks");
					
	//				if(result.get(key1).containsKey("Link"))
	//				{	
						InsertHyperlinkNode(this.domTree, pMenuNode, resultCopy.get(key1), strSessionId);					
	//				}
	//				else
	//				{
	//					 InsertHyperlinkNode(this.domTree, pMenuNode, result.get(key1), strSessionId);				
	//				}
		        }
	
	/*			
				//���ݺ�̨���ݹ����µĲ˵��ڵ㣨���ݼ����״̬�ȣ�
				for(String key1 : result.keySet())
		        {
					//ɾ��ԭ�������в˵��ڵ㣨���ݼ����״̬�ȣ�
	//				Node pShapeNode = this.domTree.getElementById(key1);
					String strSelect = String.format("//Shape[@ID=\"%s\"]", key1);
			    	Node pShapeNode  = selectSingleNode(this.domTree, strSelect);
			    	
					if(pShapeNode == null)
						continue;
	
					//ɾ��ԭ��������
					DeleteHyperlinkNodes(pShapeNode);
					
					
					Node pMenuNode  = selectSingleNode(pShapeNode, "Scratch/B/SolutionXML/HLURL:Hyperlinks");
					
	//				if(result.get(key1).containsKey("Link"))
	//				{	
						InsertHyperlinkNode(this.domTree, pMenuNode, result.get(key1), strSessionId);					
	//				}
	//				else
	//				{
	//					 InsertHyperlinkNode(this.domTree, pMenuNode, result.get(key1), strSessionId);				
	//				}
		        }
	*/			
				//����data.xml�ļ�
				FileOutputStream fos = new FileOutputStream(strPath);
				try{
				OutputFormat formatter = new OutputFormat();
				formatter.setPreserveSpace(true);
				XMLSerializer serializer =new XMLSerializer(fos, formatter);
				serializer.serialize(this.domTree);			
			}finally{
				fos.close();
			}
			
			//��ȡ*.files\vml_*.tpl����������״̬���´���vml_*.html�Ըı�ڵ���ɫ�ȡ�
			String strTplFile = strPath;
			String strNew = "", strOld = "", strTmpContent = "";
			//���ñ߿��ȼ���ɫ
			IniFile iniGen = new IniFile("tuopuset.ini");
			try
			{
				iniGen.load();
			} catch (Exception e1)
			{
			}
			String c1,c2,c3;
			String w1,w2,w3;
			if (iniGen.getSectionList().isEmpty())
			{
				c1="red";
				c2="yellow";
				c3="green";
				w1="3";
				w2="3";
				w3="0";
			} else
			{
				String tempc = "";
				tempc = iniGen.getValue("tuopusetid", "c1");
				c1=tempc;
				tempc = iniGen.getValue("tuopusetid", "c2");
				c2=tempc;
				tempc = iniGen.getValue("tuopusetid", "c3");
				c3=tempc;
				tempc = iniGen.getValue("tuopusetid", "w1");
				w1=tempc;
				tempc = iniGen.getValue("tuopusetid", "w2");
				w2=tempc;
				tempc = iniGen.getValue("tuopusetid", "w3");
				w3=tempc;
			}
			
			for (int i=0; i<pPageList.getLength(); i++) 
			{			
//				if(m_iVersion02==0)
				{
					//��visio 2003 ִ��
					strNew = String.format("vml_%d.tpl", (i+1));
					strTplFile = strPath.replace("data.xml", strNew);
				}
//				else
//				{
//					//��visio 2002 ִ��
//					strNew.Format("_vml_%d.tpl", (j+1));
//					strHtmFile.Replace(".xml", strNew);
//				}
				
				//��tpl�ļ��� ����ȡ����
				strContent = readTxt(strTplFile, "UTF-8");
								
				//���ݺ�̨���ݸı�߿���ɫ��ShapeId + IP��Group�ȣ�
				for(String key1 : result.keySet())
		        {	
//					Node pShapeNode = this.domTree.getElementById(key1);
					mapIn.get(key1).keySet();
					
					//��������ͼ��Ԫ״̬�滻�ļ�����
					if(result.get(key1).containsKey("StatState"))
					{
						if(result.get(key1).get("StatState").equals("error") || result.get(key1).get("StatState").equals("bad"))
						{
							strOld = String.format("fillcolor=\"%s\\+color\"", GetFillColorStr(mapIn.get(key1)));
							strNew = String.format("filled=\"f\"  stroked=\"t\" strokecolor=\"%s\" fillcolor=\"%s\" strokeweight=\"%spt\"", c1, c1, w1);
							
//							int index = strContent.indexOf(strOld);
							if(!w1.equals("0"))
							{
							strTmpContent = strContent.replaceAll(strOld, strNew);
							strContent = strTmpContent;
							}
						}
						else if(result.get(key1).get("StatState").equals("warning"))
						{
							strOld = String.format("fillcolor=\"%s\\+color\"", GetFillColorStr(mapIn.get(key1)));
							strNew = String.format("filled=\"f\"  stroked=\"t\" strokecolor=\"%s\" fillcolor=\"%s\" strokeweight=\"%spt\"", c2, c2, w2);
							
//							int index = strContent.indexOf(strOld);
							if(!w2.equals("0"))
							{
							strTmpContent = strContent.replaceAll(strOld, strNew);							
							strContent = strTmpContent;		
							}
						}
						else
						{
							strOld = String.format("fillcolor=\"%s\\+color\"", GetFillColorStr(mapIn.get(key1)));
							strNew = String.format("filled=\"f\"  stroked=\"t\" strokecolor=\"%s\" fillcolor=\"%s\" strokeweight=\"%spt\"", c3, c3, w3);
							
//							int index = strContent.indexOf(strOld);
							if(!w3.equals("0"))
							{
							strTmpContent = strContent.replaceAll(strOld, strNew);							
							strContent = strTmpContent;
							}
						}
					}
				}				
				
				strTmpContent = strContent.replaceAll("href=\"#\"", "href=\"javascript:void(null)\"");
				strContent = strTmpContent;
				
				String strCurUrl = "";
				
//				if(nIsMainTain == 1)
				{
					//if(nIsMainTainLeader == 1)
//						strCurUrl.Format("parent.location.replace(\"../../../fcgi-bin/showmaintain.exe?pageid=%s&version=0&usrleader=%d&maintain=%d\")", strPageId, nIsMainTainLeader, nIsMainTain);
				}
//				else
//					strCurUrl = String.format("parent.location.replace(\"../../../main/tuoplist/showtuopu.zul?name=%s&version=0\")", strName);
				    strCurUrl = String.format("top.location.replace(\"../../../main/tuoplist/showtuopu.zul?name=%s\")", strName);				
					
//				strTmpContent = strContent.replaceAll("parent.location.reload()", strCurUrl);//mf:tuopo�Զ�ˢ��
				strTmpContent = this.myReplace(strContent, "parent.location.reload()", strCurUrl);//mf:tuopo�Զ�ˢ��
				strContent = strTmpContent;
				
				int nflagpos =  strContent.indexOf("*#siteview7endflag#*");	
				if(nflagpos == -1)
				{
					strTmpContent = strContent;
				}
				else
				{
					strTmpContent = strContent.substring(0, nflagpos);
				}
				
				strContent = strTmpContent;
				
				String strHtmFile = strTplFile.replace(".tpl", ".htm");
				
				createFile(strHtmFile, strContent, "UTF-8");
			}
			
	//			CStringList lstShape, lstColor, lstWeight;
	
				//GetStatusFromPingIni(cTDL,strXMLFile);
				
				//4������shape��������ݹ���˵����������������ɫ��
	//			FormatXML(pDoc, pPageNode, cTDL, lstShape, lstColor, lstWeight);
	
	//			CString strNew = _T("");
	//			CString strHtmFile = strXMLFile;
	//			if(m_iVersion02==0)
	//			{
	//				//��visio 2003 ִ��
	//				strNew.Format("vml_%d.tpl", (j+1));
	//				strHtmFile.Replace("data.xml", strNew);
	//			}
	//			else
	//			{
	//				//��visio 2002 ִ��
	//				strNew.Format("_vml_%d.tpl", (j+1));
	//				strHtmFile.Replace(".xml", strNew);
	//			}
				
				//5�����Ľڵ���ɫ���Ե�
	//			OperaHtm(strHtmFile, lstShape, lstColor, lstWeight);
	//		}
		}
		catch (Exception ex) 
		{
			
		}	
    }
    


    //////////////////////���XML�������װ�Ĳ�������////////////////////////
    
    //��ȡָ��Xml�ļ�
	public static Document ReadXml(String xmlFilename) throws IOException 
	{
		Document docIn = null;  
		try 
	      {
	        // Convert filename into a DOM tree
	        DocumentBuilderFactory domFactory =
	          DocumentBuilderFactory.newInstance();
	        domFactory.setNamespaceAware(true);
	        DocumentBuilder builder = domFactory.newDocumentBuilder();
	        docIn  = builder.parse(new File(xmlFilename));
	      }
	      catch (SAXException e) 
	      {
	        throw new IOException("Error in document parsing: " + e.getMessage());
	      }
	      catch (ParserConfigurationException e) 
	      {
	        throw new IOException("Error in configuring parser: " + e.getMessage());
	      }
	      
	      return docIn;
	}
	
	//���ҽڵ㣬�����ص�һ�����������ڵ�
	public static Node selectSingleNode(Object source, String express) 
	{
	     Node result = null;
	     XPathFactory xpathFactory = XPathFactory.newInstance();	   
	     XPath xpath = xpathFactory.newXPath();
	
//	     xpath.setNamespaceContext(new NamespaceContextProvider("v",
//		 "http://schemas.microsoft.com/visio/2003/SVGExtensions/"));
	     
	     xpath.setNamespaceContext(new NamespaceContextProvider("HLURL",
		 "urn:schemas-microsoft-com:office:visio:dghlinkext"));	     
	     try {
	      result = (Node) xpath.evaluate(express, source, XPathConstants.NODE);
	     } catch (XPathExpressionException e) {
	      e.printStackTrace();
	     }
	
	     return result;
	}
	
	//���ҽڵ㣬���ط��������Ľڵ㼯��
	public static NodeList selectNodes(Object source, String express) 
	{
	     NodeList result = null;
	     XPathFactory xpathFactory = XPathFactory.newInstance();
	     XPath xpath = xpathFactory.newXPath();
	     
//	     xpath.setNamespaceContext(new NamespaceContextProvider("v", 
//	    		 "http://schemas.microsoft.com/visio/2003/SVGExtensions/"));
	
	     xpath.setNamespaceContext(new NamespaceContextProvider("HLURL",
		 "urn:schemas-microsoft-com:office:visio:dghlinkext"));
	
	     try {
	      result = (NodeList) xpath.evaluate(express, source, XPathConstants.NODESET);
	     } catch (XPathExpressionException e) {
	      e.printStackTrace();
	     }
	
	     return result;
	}
	
	//��ȡ�û��Զ�������ֵ(SV_IP��)
	public static String RetrievePropertyValue(Node pShape, String strPropName)
	{
	//	logger.info(pShape.getAttributes().getNamedItem("ID").getNodeValue());
	//	logger.info(strPropName);
		
		String strSelect = "";
		String strValue = "";
	
		try
		{
			strSelect = String.format("Prop[@NameU=\"%s\"]", strPropName);
	    	
	    	Node pNode  = selectSingleNode(pShape, strSelect);
	    	if(pNode != null)
	    	{
	    		Node pChildNode = selectSingleNode(pNode, "Value");
	    		if(pChildNode != null)
	    		{
					strValue = pChildNode.getTextContent();
					//logger.info(strValue);
	    		}	    		
	    	}
		}
		catch (Exception ex) 
		{
			return "";    		
		}
		
		return strValue;
	}
	
	//HLURL�ڵ��Ƿ���� ?
	public static boolean IsHyperlinkNodesExisted(Node pShape)
	{
	//	logger.info(pShape.getAttributes().getNamedItem("ID").getNodeValue());
		
//		String strXpath = "Scratch/B/SolutionXML/HLURL:Hyperlinks/HLURL:Hyperlink";
		String strXpath = "Scratch/B/SolutionXML/HLURL:Hyperlinks";
		
		try
		{
			Node pNode  = selectSingleNode(pShape, strXpath);
			if(pNode != null)
			{
	    		return true;
			}
		}
		catch (Exception ex) 
		{
			return false;    		
		}
		
		return false;
	}
	
	//����Xml�ڵ�
	public static void  InsertNode(Document docIn, Node parentNode, String strNodeName, String strNodeText, String szNodeNamespaceURI , String szAttr1Name, String szAttr1Value)
	{
		try
		{
			Element pElement = null;
			if(szNodeNamespaceURI.equals(""))    		
				pElement = docIn.createElement(strNodeName);
			else
				pElement = docIn.createElementNS(szNodeNamespaceURI, strNodeName);
			
//			pElement.setNodeValue(strNodeText);
			pElement.setTextContent(strNodeText);
			
			if(!szAttr1Name.equals(""))
				pElement.setAttribute(szAttr1Name, szAttr1Value);
			
//			parentNode.insertBefore(pElement, arg1)(pElement);		
			parentNode.appendChild(pElement);
		}
		catch (Exception ex) 
		{
			return ;    		
		}		
	}
	
	//
	private static String GetStateFormStr(String strState)
	{
		String strType = "";
		
		if(strState.equals("ok"))
		{
			strType = "1";
		}
		else if(strState.equals("warning"))
		{
			strType = "2";
		}
		else if(strState.equals("error"))
		{
			strType = "3";
		}
		else if(strState.equals("disable"))
		{
			strType = "4";
		}
		else if(strState.equals("bad"))
		{
			strType = "5";
		}
		else
		{
			strType = strState;
		}

		return strType;
	}
	
	private static String GetFillColorStr(Map<String, String> map)
	{
		String strType = "";
		
		for(String key : map.keySet())
		{
			if(!map.get(key).equals(""))
			{
				strType += String.format("SV_%s:%s", key, map.get(key));
				break;
			}
		}

		return strType;
	}	
	
	//����˵���
	public static void InsertMenuItem(Document docIn, Node parentNode, String strID, String strDes, String strAddress, String strState)
	{
		Element pElement = docIn.createElementNS("urn:schemas-microsoft-com:office:visio:dghlinkext", "HLURL:Hyperlink");
		pElement.setAttribute("ID", strID);
		parentNode.appendChild(pElement);
		
		//����
		InsertNode(docIn, pElement,  "HLURL:Description", strDes, "urn:schemas-microsoft-com:office:visio:dghlinkext", "", "");
		
		//С����
		InsertNode(docIn, pElement,  "HLURL:Address", strAddress, "urn:schemas-microsoft-com:office:visio:dghlinkext", "", "");
		InsertNode(docIn, pElement,  "HLURL:SubAddress", "", "urn:schemas-microsoft-com:office:visio:dghlinkext", "", "");
		InsertNode(docIn, pElement,  "HLURL:AbsoluteURL", strAddress, "urn:schemas-microsoft-com:office:visio:dghlinkext", "", "");
		
		//<HLURL:ExtraInfo></HLURL:ExtraInfo>
		InsertNode(docIn, pElement,  "HLURL:ExtraInfo","", "urn:schemas-microsoft-com:office:visio:dghlinkext", "", "");
		InsertNode(docIn, pElement,  "HLURL:Default", "", "urn:schemas-microsoft-com:office:visio:dghlinkext", "", "");
		
		//״̬
		InsertNode(docIn, pElement,  "HLURL:Status", GetStateFormStr(strState), "urn:schemas-microsoft-com:office:visio:dghlinkext", "", "");
		
		if(!strAddress.equals("#"))
			InsertNode(docIn, pElement,  "HLURL:NewWindow", "3", "urn:schemas-microsoft-com:office:visio:dghlinkext", "", "");
		else
			InsertNode(docIn, pElement,  "HLURL:NewWindow", "0", "urn:schemas-microsoft-com:office:visio:dghlinkext", "", "");		
	}
	
	//����HyperlinkNode�ڵ�
	public static void InsertHyperlinkNode(Document docIn, Node parentNode, Map<String, String> menuInfo, String strId)
	{		
		try
		{
			String strIndex = "", strID = "";
			String strStateKey = "", strStateValue = ""; 
			String strDesKey = "", strDesValue = ""; 
			String strSvidKey = "", strSvidValue = "";
			String strAddress = "";
			
			boolean bAddMenu = false;
			
			//�û�������
			if(menuInfo.containsKey("Des"))
			{
				strID = "0";
				strDesValue = menuInfo.get("Des");
				strStateValue = "-100";
				strAddress = "#";
				
				InsertMenuItem(docIn, parentNode, strID, strDesValue, strAddress, strStateValue);
			}
			
			//�û������ʶ�˵���
			if(menuInfo.containsKey("IP"))
			{
				strID = "0";
				strDesValue = "SV_IP:" + menuInfo.get("IP");
				strStateValue = "-100";
				strAddress = "#";
				if ("true".equalsIgnoreCase(BaseTools.getMakeTuopuDataString("disable"))){
					strAddress = BaseTools.getMakeTuopuDataString("linkurl");
					if (strAddress == null) {
						strAddress = "#";
					}else{
						strAddress = strAddress.replaceAll("\\$SV_IP\\$", menuInfo.get("IP"));
					}
				}
				InsertMenuItem(docIn, parentNode, strID, strDesValue, strAddress, strStateValue);
			}
			else if(menuInfo.containsKey("Entity"))
			{
				strID = "0";
				strDesValue = "SV_Entity:" + menuInfo.get("Entity");
				strStateValue = "-100";
				strAddress = "#";

				InsertMenuItem(docIn, parentNode, strID, strDesValue, strAddress, strStateValue);
			}
			else if(menuInfo.containsKey("Group"))
			{
				strID = "0";
				strDesValue = "SV_Group:" + menuInfo.get("Group");
				strStateValue = "-100";
				strAddress = "#";

				InsertMenuItem(docIn, parentNode, strID, strDesValue, strAddress, strStateValue);
			}			
			else if(menuInfo.containsKey("Monitor"))
			{
				strID = "0";
				strDesValue = "SV_Monitor:" + menuInfo.get("Monitor");
				strStateValue = "-100";
				strAddress = "#";

				InsertMenuItem(docIn, parentNode, strID, strDesValue, strAddress, strStateValue);	
			}
			else if(menuInfo.containsKey("Link"))
			{
				strID = "0";
				strDesValue = "Go To Page:" + menuInfo.get("Link");
//				strDesValue = "SV_Link:" + menuInfo.get(key);
				strStateValue = menuInfo.get("StatState");
				strAddress = "../showtuopu.zul?name=" + menuInfo.get("Link");

				InsertMenuItem(docIn, parentNode, strID, strDesValue, strAddress, strStateValue);
				
			}
			else
			{
				
			}
			
			//������б�
			int i = 0;
			bAddMenu = false;
			for (String key : menuInfo.keySet())
			{
				if(key.indexOf("monitor") != -1 && key.indexOf("Svid") != -1)
				{
					strIndex = key.substring(0, key.length() - 4);
					strStateKey = strIndex + "State"; 
					strDesKey = strIndex + "Des";
					strSvidKey = key;
					
					if(!menuInfo.containsKey(strStateKey) || !menuInfo.containsKey(strDesKey) || !menuInfo.containsKey(strSvidKey))
					{
						bAddMenu = false;
						continue;
					}
					else
					{
						bAddMenu = true;	
						
						strDesValue = menuInfo.get(strDesKey);
						strSvidValue = menuInfo.get(strSvidKey);
						strStateValue = menuInfo.get(strStateKey);
						strAddress = "../../TreeView/WSimpleReport.zul?monitorId=" + menuInfo.get(strSvidKey)+ "&sid=" + strId;
						
						if(strDesValue.equals("") || strStateValue.equals(""))
						{
							bAddMenu = false;
							continue;
						}
						
						i++;
						strID = String.valueOf(i);
					}
				}
				
				if(bAddMenu)
				{	
//						if(i>=5)
//							 return;
					InsertMenuItem(docIn, parentNode, strID, strDesValue, strAddress, strStateValue);
				}
				
				bAddMenu = false;
			}
		}
		catch (Exception ex) 
		{
			return ;
		}		
		
		//varType ��ʱ����
//		Element pElement = this.domTree.createElementNS("HLURL:Hyperlink", "urn:schemas-microsoft-com:office:visio:dghlinkext");    	
//		pElement.setAttribute("ID", menuInfo.get("ID"));
//		parentNode.appendChild(pElement);
//		
//	//	for(String key : menuInfo.keySet())
//		{	
//			InsertNode(pElement,  "HLURL:Description", menuInfo.get("DESC").toString(), "urn:schemas-microsoft-com:office:visio:dghlinkext", "", "");
//
//			InsertNode(pElement,  "HLURL:Address", menuInfo.get("ADDR").toString(), "urn:schemas-microsoft-com:office:visio:dghlinkext", "", "");
//			InsertNode(pElement,  "HLURL:SubAddress", "", "urn:schemas-microsoft-com:office:visio:dghlinkext", "", "");
//			//<HLURL:ExtraInfo></HLURL:ExtraInfo>
//			InsertNode(pElement,  "HLURL:ExtraInfo","", "urn:schemas-microsoft-com:office:visio:dghlinkext", "", "");
//			InsertNode(pElement,  "HLURL:Default", "", "urn:schemas-microsoft-com:office:visio:dghlinkext", "", "");
//			InsertNode(pElement,  "HLURL:AbsoluteURL", menuInfo.get("ADDR").toString(), "urn:schemas-microsoft-com:office:visio:dghlinkext", "", "");
//			
//			InsertNode(pElement,  "HLURL:Status", menuInfo.get("STAT").toString(), "urn:schemas-microsoft-com:office:visio:dghlinkext", "", "");
//			
//		}
//		
//		if(menuInfo.get("ADDR").equals("#"))
//		{
//			InsertNode(pElement, "HLURL:NewWindow", "0", "urn:schemas-microsoft-com:office:visio:dghlinkext", "", "");
//		}
//		else
//		{
//			InsertNode(pElement, "HLURL:NewWindow", "3", "urn:schemas-microsoft-com:office:visio:dghlinkext", "", "");
//		}
	}
	
	//ɾ��HyperlinkNode�ڵ�
	public void  DeleteHyperlinkNodes(Node pShape)
	{
//		Node pParent = pShape.getParentNode();
		
		String strXpath = "Scratch/B/SolutionXML/HLURL:Hyperlinks/HLURL:Hyperlink";    	
		
		try
		{
			NodeList pNodeList = selectNodes(pShape, strXpath);
			
			for (int i=0; i<pNodeList.getLength(); i++) 
			{
				Node pNode = pNodeList.item(i);
				Node pParent = pNode.getParentNode(); 
				pParent.removeChild(pNode);
			}
	
		}
		catch (Exception ex) 
		{
			return ;    		
		}
	}
	
	//////////////////////���XML�������װ�Ĳ�������////////////////////////


	//////////////////////ȡ����ͼ���ݺ���///////////////////////////////////
	
	//ȡ����ͼ����
	public Map<String, Map<String, String>> GetTuopuDataFromServer (Map<String, Map<String, String>> mapIn)
	{	
		Map<String, Map<String, String>> resultData = new HashMap();
		
		try
		{
			//��ȡƥ��������������Ҫ��ֵ
			Map<String, Map<String, String>> mapName = QueryNameInfo();// Group Entity Monitor key(svid) + value(needtype + sv_name)
			Map<String, Map<String, String>> mapMachineName = QueryMachineNameInfo();//Ip��key(svid) + value(needtype + _MachineName) ==
			//Des(ֱ�Ӹ�ֵ���б�) ��App(��ʱû��) ��
			
			if(mapName == null || mapMachineName == null)
				return null;
	
			//��ȡ���µ����м������
			Map<String, Map<String, String>> mapTree = new HashMap(); //����monitor�Ƚ�
			
			List<Map<String, String>> tree =  Toolkit.getToolkit().getSvdbView(desktop).getRawTreeData();
			
			for(Map<String, String> map : tree)
			{
				mapTree.put(map.get("sv_id"), map);
			}
			
	        //ƥ���������� + ������� --> TuopuData          
			
	        String strState = "ok";
	        for (String key : mapIn.keySet())
	        {
	        	Map<String ,String> map = mapIn.get(key);
	        	
	        	Map<String, String> tmpValue = new HashMap<String, String>();
	
	            int j = 0;                
	            for(String key1 : map.keySet())
	            {
	                //
	                if(key1.equals("Group") && !map.get("Group").equals(""))
	                {
	                    j = 0;
	                    strState = "ok";
	                    tmpValue.put("StatState", strState); // Ӧ����ͳ�Ƴ�����
	                    tmpValue.put("Group", map.get("Group"));
	                    for(String key2 : mapName.keySet())
	                    {
//	                    	if(mapName.get(key2).isEmpty())
//	                    		continue;

	                    	if (mapName.get(key2).get("needtype").equals("group") && mapName.get(key2).get("sv_name").indexOf(map.get("Group")) != -1)
	                        {
	                            //���ɼ�����б����
	                        	Map<String, String> tmpValue1 = MakeMonitorInfoFromTree(tree, mapTree, key2, tmpValue.keySet().size(), false);
	
	                            tmpValue.put("Svid" + String.valueOf(j), key2);
	                            tmpValue.put("Svid" + String.valueOf(j) + "Name", mapName.get(key2).get("sv_name"));
	
	                            for(String key3 : tmpValue1.keySet())
	                            {
	                                tmpValue.put(key3, tmpValue1.get(key3));
	                            }
	
	                            j++;
	                        }
	                    }
	                }
	                else if (key1.equals("Entity") && !map.get("Entity").equals(""))
	                {
	                    j = 0;
	                    strState = "ok";
	                    tmpValue.put("StatState", strState);
	                    tmpValue.put("Entity", map.get("Entity"));
	                    for(String key2 : mapName.keySet())
	                    {
//	                    	if(mapName.get(key2).isEmpty())
//	                    		continue;
	                    	
	                    	if (mapName.get(key2).get("needtype").equals("entity") && mapName.get(key2).get("sv_name").indexOf(map.get("Entity")) != -1)
	                        {
	                            //���ɼ�����б����
	                        	Map<String, String> tmpValue1 = MakeMonitorInfoFromTree(tree, mapTree, key2, tmpValue.keySet().size(), false);
	                            tmpValue.put("Svid" + String.valueOf(j), key2);
	                            tmpValue.put("Svid" + String.valueOf(j) + "Name", mapName.get(key2).get("sv_name"));
	
	                            for(String key3 : tmpValue1.keySet())
	                            {
	                                tmpValue.put(key3, tmpValue1.get(key3));
	                            }
	                            
	                            j++;
	                        }
	                    }
	                }
	                else if (key1.equals("Monitor") && !map.get("Monitor").equals(""))
	                {
	                    j = 0;
	                    strState = "ok";
	                    tmpValue.put("StatState", strState);
	                    tmpValue.put("Monitor", map.get("Monitor"));
	                    for(String key2 : mapName.keySet())
	                    {
//	                    	if(mapName.get(key2).isEmpty())
//	                    		continue;
	                    	
	                        if (mapName.get(key2).get("needtype").equals("monitor") && mapName.get(key2).get("sv_name").indexOf(map.get("Monitor")) != -1)
	                        {
	                            //���ɼ�����б����
	                        	Map<String, String> tmpValue1 = MakeMonitorInfoFromTree(tree, mapTree, key2, tmpValue.keySet().size(), true);
	                            tmpValue.put("Svid" + String.valueOf(j), key2);
	                            tmpValue.put("Svid" + String.valueOf(j) + "Name", mapName.get(key2).get("sv_name"));
	
	                            for(String key3 : tmpValue1.keySet())
	                            {
	                                tmpValue.put(key3, tmpValue1.get(key3));
	                            }
	                            
	                            j++;
	                        }
	                    }
	                }                    
	                else if (key1.equals("IP") && !map.get("IP").equals(""))
	                {
	                    j = 0;
	                    strState = "ok";
	                    tmpValue.put("StatState", strState);
	                    tmpValue.put("IP", map.get("IP"));
	                    for(String key2 : mapMachineName.keySet())
	                    {
//	                    	logger.info(key2);
	                    	
	                    	if(!mapMachineName.get(key2).containsKey("_MachineName"))
	                    		continue;
	                    	
	                    	//if (mapMachineName[key2]["needtype"] == "entity" && mapMachineName[key2]["_MachineName"] == mapIn[key]["IP"])
	                        if (mapMachineName.get(key2).get("needtype").equals("entity") && mapMachineName.get(key2).get("_MachineName").equals(map.get("IP")))
	                        {
	                            //���ɼ�����б����
	                        	Map<String, String> tmpValue1 = MakeMonitorInfoFromTree(tree, mapTree, key2, tmpValue.keySet().size(), false);
	                            tmpValue.put("Svid" + String.valueOf(j), key2);
	                            tmpValue.put("Svid" + String.valueOf(j) + "Name", mapName.get(key2).get("sv_name"));
	
	                            for(String key3 : tmpValue1.keySet())
	                            {
	                                tmpValue.put(key3, tmpValue1.get(key3));
	                            }
	                            
	                            j++;
	                        }
	                    }
	                }
	                else if (key1.equals("Des") && !map.get("Des").equals(""))
	                {
	                    //ֱ�Ӽ��뷵���б��� ���ڵ�һλ ��ʱ����
	                    strState = "ok";
	                    tmpValue.put("StatState", strState); // Ӧ����ͳ�Ƴ�����
	                    tmpValue.put("Des", map.get("Des"));	                	
	
	                }
	                else if (key1.equals("App") && !map.get("App").equals(""))
	                {
	                    //��ʱ����
	                    strState = "ok";
	                    tmpValue.put("StatState", strState); // Ӧ����ͳ�Ƴ�����
	                    tmpValue.put("App", map.get("App"));
	                }
	                else if (key1.equals("Link") && !map.get("Link").equals(""))
	                {
						//��ȡ���ӵ�������ͼ״̬
						ChildTuopuData childTuopu = new ChildTuopuData(desktop, map.get("Link"));//map.get("Link") = ip��ַ
						strState = childTuopu.getStatu();
	                    tmpValue.put("StatState", strState); // Ӧ����ͳ�Ƴ�����
	                    tmpValue.put("Link", map.get("Link"));
	                }
	                else
	                { 
	                    
	                }
	            }
	
	            resultData.put(key, tmpValue);
	        }
	
	        String strStatState = "ok";
	
	        //ͳ�� ÿһ��ͼ ����״̬
	        for(String key1 : resultData.keySet())
	        {
	        	Map<String,String> map = resultData.get(key1);
	            strStatState = map.get("StatState");
	            for(String key2 : map.keySet())
	            {
	                if ("error".equals(strStatState))
                    {
                        //�Ѿ��Ǵ��� ����ѭ�� ͳ����һ��ͼ
                        break;
                    }
	                //if ("StatState".equals(key2)) continue;

	                
	                String value2 = map.get(key2);
	                if (value2 == null) continue;

                    logger.info("key2 value == " + key2);
	                
                    if (! (key2.indexOf("State") > 0)) continue;
                    
                    logger.info("State value == " + value2);

                    if ("disable".equals(value2))
                    {
                        continue;
                    }else if ("bad".equals(value2))
                    {
                        strStatState = "error";
                    }
                    else if ("ok".equals(value2))
                    {                                
                        continue;//strStatState = value2; //ok warning error
                    }
                    else if ("error".equals(value2))
                    {
                        strStatState = "error"; 
                    }
                    else if ("warning".equals(strStatState) && "ok".equals(value2))
                    {
                        continue;
                    }
                    else
                    {
                        continue;
                    }
	            }
	            
	            map.put("StatState", strStatState);
	        }
		}
		catch (Exception ex)
		{
			logger.info(ex.toString());			
		}
		
        return resultData;		
	}
	
	//��ȡ������б�����
    //strSvid + .  -->Group Entity IP
    //strSvid == --> monitor	
	public Map<String, String> MakeMonitorInfoFromTree(List<Map<String, String>>  tree, Map<String, Map<String, String>> mapTree, String strSvid, int index, boolean bIsMonitor)
	{
		Map<String, String> value = new HashMap();
        int k = index;

        if (bIsMonitor)
        {
            if (mapTree.containsKey(strSvid))
            {
                //Monitor                        
                value.put("monitor" + String.valueOf(k) + "State", mapTree.get(strSvid).get("status"));
                value.put("monitor" + String.valueOf(k) + "Des", mapTree.get(strSvid).get("sv_name") + " " + mapTree.get(strSvid).get("dstr"));
                value.put("monitor" + String.valueOf(k) + "Svid", mapTree.get(strSvid).get("sv_id"));
            }
        }
        else
        {
            strSvid += ".";
            for (int i = 0; i < tree.size(); i++)
            {
                String strId = "";

                //��ڵ�svid
                strId = tree.get(i).get("sv_id");

                if (tree.get(i).get("type").equals("monitor"))
                {
                    if (strId.indexOf(strSvid) == 0)
                    {
                        //Group Entity IP
                        value.put("monitor" + String.valueOf(k) + "State", tree.get(i).get("status"));
                        value.put("monitor" + String.valueOf(k) + "Des", tree.get(i).get("sv_name") + " " + tree.get(i).get("dstr"));
                        value.put("monitor" + String.valueOf(k) + "Svid", tree.get(i).get("sv_id"));
                        k++;
                    }
                    else
                    {
                        //
                    }
                }
            }
        }
        
		return value;
	}
	
	//QueryNameInfo
	public static Map<String, Map<String, String>> QueryNameInfo()
	{
		QueryInfo q = new QueryInfo();
		
		q.needkey= "sv_name";
		q.setNeedType_all();
		
		try
		{
			return q.load();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return  null;
	}
	
	//QueryMachineNameInfo
	public static Map<String, Map<String, String>> QueryMachineNameInfo()
	{
		QueryInfo q= new QueryInfo();
		
		q.needkey= "sv_name,_MachineName";
		q.setNeedType_all();
//		q.setNeedType_entity();
		
		try
		{
			return q.load();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return  null;
	}
	
	//////////////////////ȡ����ͼ���ݺ���///////////////////////////////////
	
	
	//////////////////////�ļ���������///////////////////////////////////////
	
	//�滻�ļ����ݣ� ���java��replaceall��������ʽ����������
	public static String myReplace(String strSrc, String strOld, String strNew)
	{		
		StringBuffer strSrcBuf = new StringBuffer("");
		String strTmpBuf = "";
		strSrcBuf.append(strSrc);
		
		if(strSrcBuf.indexOf(strOld) != -1)
		{
			strTmpBuf = strSrcBuf.replace(strSrcBuf.indexOf(strOld), strSrcBuf.indexOf(strOld) + strOld.length(), strNew).toString();			
		}
		
		return strTmpBuf;
	}
	
	//�滻�ļ����ݣ� ���java��replaceall��������ʽ����������
	public static String myReplaceAll(String source, String toReplace, String replacement)
	{
			int idx = source.lastIndexOf( toReplace );
			if (idx != -1) 
			{
				StringBuffer ret = new StringBuffer(source);
				ret.replace(idx, idx+toReplace.length(), replacement);
				while((idx=source.lastIndexOf(toReplace, idx-1)) != -1) 
				{
					ret.replace( idx, idx+toReplace.length(), replacement );
				}
				source = ret.toString();
			}

			return source;
	}
	
	/**  
	 * �½�Ŀ¼  
	 * @param folderPath Ŀ¼  
	 * @return ����Ŀ¼�������·��  
	 */  
	public static String createFolder(String folderPath) {   
	    String txt = folderPath;   
	    try {   
	        java.io.File myFilePath = new java.io.File(txt);   
	        txt = folderPath;   
	        if (!myFilePath.exists()) {   
	            myFilePath.mkdir();   
	        }   
	    }   
	    catch (Exception e) {   
//	        message = "����Ŀ¼��������";   
	    }   
	    return txt;   
	}   
	   
	/**  
	 * �༶Ŀ¼����  
	 * @param folderPath ׼��Ҫ�ڱ���Ŀ¼�´�����Ŀ¼��Ŀ¼·�� ���� c:myf  
	 * @param paths ���޼�Ŀ¼����������Ŀ¼�Ե��������� ���� a|b|c  
	 * @return ���ش����ļ����·�� ���� c:myfac  
	 */  
	public String createFolders(String folderPath, String paths){   
	    String txts = folderPath;   
	    try{   
	        String txt;   
	        txts = folderPath;   
	        StringTokenizer st = new StringTokenizer(paths,"|");   
	        for(int i=0; st.hasMoreTokens(); i++){   
	                txt = st.nextToken().trim();   
	                if(txts.lastIndexOf("/")!=-1){    
	                    txts = createFolder(txts+txt);   
	                }else{   
	                    txts = createFolder(txts+txt+"/");       
	                }   
	        }   
	   }catch(Exception e){   
	       message = "����Ŀ¼��������";   
	       }   
	        return txts;   
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
			        message = "�����ļ���������";   
			        }
			    finally
			    {
			    	try{myFile.close();}catch(Exception e){}
			    }       
	    }    
	  
   /**  
	 * ɾ���ļ���  
	 * @param folderPath �ļ�����������·��  
	 * @return  
	 */  
	public static void delFolder(String folderPath) {   
	    try {   
	        delAllFile(folderPath); //ɾ����������������   
	        String filePath = folderPath;   
	        filePath = filePath.toString();   
	        java.io.File myFilePath = new java.io.File(filePath);   
	        myFilePath.delete(); //ɾ�����ļ���   
	    }   
	    catch (Exception e) {   
//	        message = ("ɾ���ļ��в�������");   
	    }   
	}   
	   
	   
	/**  
	 * ɾ��ָ���ļ����������ļ�  
	 * @param path �ļ�����������·��  
	 * @return  
	 * @return  
	 */  
	public static boolean delAllFile(String path) {   
	 boolean bea = false;   
	    File file = new File(path);   
	    if (!file.exists()) {   
	        return bea;   
	    }   
	    if (!file.isDirectory()) {   
	        return bea;   
	    }   
	    String[] tempList = file.list();   
	    File temp = null;   
	    for (int i = 0; i < tempList.length; i++) {   
	        if (path.endsWith(File.separator)) {   
	            temp = new File(path + tempList[i]);   
	        }else{   
	            temp = new File(path + File.separator + tempList[i]);   
	        }   
	        if (temp.isFile()) {   
	            temp.delete();   
	        }   
	        if (temp.isDirectory()) {   
	            delAllFile(path+"/"+ tempList[i]);//��ɾ���ļ���������ļ�   
	            delFolder(path+"/"+ tempList[i]);//��ɾ�����ļ���   
	                bea = true;   
	            }   
	        }   
	        return bea;   
	    }   
	  
	  
	/**  
	 * ���������ļ��е�����  
	 * @param oldPath ׼��������Ŀ¼  
	 * @param newPath ָ������·������Ŀ¼  
	 * @return  
	 */  
	public static void copyFolder(String oldPath, String newPath) {   
	    try {   
	        new File(newPath).mkdirs(); //����ļ��в����� �������ļ���   
	        File a=new File(oldPath);   
	        String[] file=a.list();   
	        File temp=null;   
	        for (int i = 0; i < file.length; i++) {   
	            if(oldPath.endsWith(File.separator)){   
	                temp=new File(oldPath+file[i]);   
	            }else{   
	                temp=new File(oldPath+File.separator+file[i]);   
	            }   
	            if(temp.isFile()){   
	                FileInputStream input = null;   
	                FileOutputStream output = null;   
	                
	                try
	                {
 	                input = new FileInputStream(temp);   
	                output = new FileOutputStream(newPath + "/" + (temp.getName()).toString());   
	                byte[] b = new byte[1024 * 5];   
	                int len;   
	                while ((len = input.read(b)) != -1) {   
	                    output.write(b, 0, len);   
	                }   
	                output.flush();   
	              }finally{
	                try{output.close(); }catch(Exception e){}
	                try{input.close();  }catch(Exception e){}
	              }
	            }   
	            if(temp.isDirectory()){//��������ļ���   
	                copyFolder(oldPath+"/"+file[i],newPath+"/"+file[i]);   
	            }   
	        }   
	    }catch (Exception e) {   
//	        message = "���������ļ������ݲ�������";   
	        }   
	    }   
	  
	  
	 /**  
	 * �ƶ��ļ�  
	 * @param oldPath  
	 * @param newPath  
	 * @return  
	 */  
		public void moveFile(String oldPath, String newPath) {   
		    copyFile(oldPath, newPath);   
		    delFile(oldPath);   
		}   
	       
	  
	 /**  
	 * �ƶ�Ŀ¼  
	 * @param oldPath  
	 * @param newPath  
	 * @return  
	 */  
	public void moveFolder(String oldPath, String newPath) {   
	    copyFolder(oldPath, newPath);   
	    delFolder(oldPath);   
	}   
	public String getMessage(){   
	    return this.message;   
	}  
	    
    /**
     * ���Ƶ����ļ�
     * @param oldPathFile ׼�����Ƶ��ļ�Դ
     * @param newPathFile �������¾���·�����ļ���
     * @return
     */
    public static void copyFile(String oldPathFile, String newPathFile) 
    {
    	InputStream inStream=null;
    	FileOutputStream fs =null;
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPathFile);
            if (oldfile.exists()) { //�ļ�����ʱ
                inStream = new FileInputStream(oldPathFile); //����ԭ�ļ�
                fs = new FileOutputStream(newPathFile);
                
			                byte[] buffer = new byte[1444];
			                while((byteread = inStream.read(buffer)) != -1){
			                    bytesum += byteread; //�ֽ��� �ļ���С
			//                    logger.info(bytesum);
			                    fs.write(buffer, 0, byteread);
			                }

            }
        }
        catch (Exception e) 
        {
//            message = ("���Ƶ����ļ���������");
        }
        finally
        {
        	try{inStream.close();}catch(Exception e){};
        	try{fs.close();}catch(Exception e){};
        }
    }
    
    /**
     * ɾ���ļ�
     * @param filePathAndName �ı��ļ���������·�����ļ���
     * @return Boolean �ɹ�ɾ������true�����쳣����false
     */
    public static boolean delFile(String filePathAndName) 
    {
    	boolean bea = false;
        try 
        {
            String filePath = filePathAndName;
            File myDelFile = new File(filePath);
            if(myDelFile.exists()){
             myDelFile.delete();
             bea = true;
            }
            else
            {
             bea = false;
//             message = (filePathAndName+"ɾ���ļ���������");
            }
        }
        catch (Exception e) 
        {
//            message = e.toString();
        }
        
        return bea;
    }   
    
    /**
     * �½��ļ�
     * @param filePathAndName �ı��ļ���������·�����ļ���
     * @param fileContent �ı��ļ�����
     * @return
     */
    public void createFile(String filePathAndName, String fileContent) 
    {
     
        try {
            String filePath = filePathAndName;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            if (!myFilePath.exists()) {
                myFilePath.createNewFile();
            }
//            FileWriter resultFile = new FileWriter(myFilePath);
//            PrintWriter myFile = new PrintWriter(resultFile);

						FileOutputStream fos=null;
						OutputStreamWriter osw=null;
            PrintWriter myFile = null;
            try
            {
						fos=new FileOutputStream(myFilePath);
						osw=new OutputStreamWriter(fos, "UTF-8");
            myFile = new PrintWriter(osw);

            String strContent = fileContent;            
            myFile.println(strContent);
          }finally
            {
	            try{myFile.close();}catch(Exception r){}
	            try{osw.close();}catch(Exception r){}
	            try{fos.close();}catch(Exception r){}
	            
	          }
//            resultFile.close();
        }
        catch (Exception e) 
        {
//            message = "�����ļ���������";
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
    
	//////////////////////�ļ���������///////////////////////////////////////    
}


//���xml�����ƿռ��д��Ҫ��Provider
class NamespaceContextProvider implements NamespaceContext 
{
    String boundPrefix, boundURI;

    NamespaceContextProvider(String prefix, String URI) {
        boundPrefix = prefix;
        boundURI = URI;
    }

    public String getNamespaceURI(String prefix) {
        if(prefix.equals(boundPrefix)) {
            return boundURI;
        } else if(prefix.equals(XMLConstants.XML_NS_PREFIX)) {
            return XMLConstants.XML_NS_URI;
        } else if(prefix.equals(XMLConstants.XMLNS_ATTRIBUTE)) {
            return XMLConstants.XMLNS_ATTRIBUTE_NS_URI;
        } else {
            return XMLConstants.DEFAULT_NS_PREFIX;
        }
    }

    public String getPrefix(String namespaceURI) {
        if(namespaceURI.equals(boundURI)) {
            return boundPrefix;
        } else if(namespaceURI.equals(XMLConstants.XML_NS_URI)) {
            return XMLConstants.XML_NS_PREFIX;
        } else if(namespaceURI.equals(XMLConstants.XMLNS_ATTRIBUTE_NS_URI)) {
            return XMLConstants.XMLNS_ATTRIBUTE;
        } else {
            return null;
        }
    }
    
    public Iterator getPrefixes(String namespaceURI) {
        // not implemented for the example
        return null;
    }    
}

//MakeTuopuData svg��ʽ��������		
//try {
//    // Parse the barChart.svg file into a Document.
//    String parser = XMLResourceDescriptor.getXMLParserClassName();
//    SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
////      URL url = new URL(getCodeBase(), "barChart.svg");
//    URL url = new URL(getCodeBase(), "TieTongJF.svg");            
//    doc = f.createDocument(url.toString());
//
//    svg = doc.getDocumentElement();
//    
//
////      Element elm = doc.getElementById("group772-317");
////      ChangeElement(elm);
////      XPathEvaluator("myLine.svg");
////      XPathEvaluator("data.xml");
//    XPathEvaluator("TieTongJF.svg");
////      XPathEvaluator("test11.svg");
//    
//
////      String xpathString = "//target[@name='init']/property[" +
////      "starts-with(@name, 'parser')]";
////      String xpathString = "//Shape[@ID='1180']";
////      String xpathString = "//defs";
//    String xpathString = "//v:cp[@v:nameU=\"SV_IP\"]";
//    
//    
//    NodeList results = selectNodes(svg, xpathString);
//    
////      results = this.doc.getDocumentElement().getChildNodes();
//		for (int i=0; i<results.getLength(); i++) 
//		{
//			Node node = results.item(i);
////			logger.info(node.getAttributes().getLength());
//			
////			for (int j=0; j<node.getAttributes().getLength(); j++)
////			{			
////				logger.info(node.getAttributes().item(j).getNamespaceURI());
////				logger.info(node.getAttributes().item(j).getNodeName());
////				logger.info(node.getAttributes().item(j).getNodeValue());
////			}
//			
//			logger.info(node.getAttributes().getNamedItemNS("http://schemas.microsoft.com/visio/2003/SVGExtensions/", "nameU").getNodeValue());
//			logger.info(node.getAttributes().getNamedItemNS("http://schemas.microsoft.com/visio/2003/SVGExtensions/", "val").getNodeValue());
//			
//			logger.info(node.getParentNode().getNodeName());
//			logger.info(node.getParentNode().getParentNode().getNodeName());
//			ChangeElement((Element)node.getParentNode().getParentNode());
////			logger.info(node.toString());
////			logger.info(node.getNodeValue());
////			logger.info(node.getTextContent());
////			logger.info(node.getAttributes().getNamedItem("nameU").getNodeValue());
////			logger.info(node.getParentNode().getAttributes().getNamedItem("v:val"));
////			if(node.getParentNode().getParentNode() != null)
////			{
////				logger.info(node.getParentNode().getParentNode().getAttributes().getNamedItem("id"));
////			}
//			
//			logger.info("Result: ");
//			switch (node.getNodeType()) 
//			{
//				case Node.ELEMENT_NODE: logger.info("Element node named " +
//						  node.getNodeName());
////	                      node.getAttributes().getNamedItem("ID").getNodeValue().toString());
//				                   break;
//				case Node.ATTRIBUTE_NODE: logger.info(
//				                      "Attribute node named " +
//				                        node.getNodeName() + " with value '" +
//				                        node.getNodeValue() + "'");
//				                   break;
//				case Node.TEXT_NODE:    logger.info("Text: '" +
//				                     node.getNodeValue() + "'");
//				                   break;
//				default: logger.info(node);
//			}
//		}
//		
////      XPath xpath = XPathFactory.newInstance().newXPath();
////      String expression = "//v:custProps";
////      InputSource inputSource = new InputSource("TieTongJF.svg");
////      NodeList  nodes =  (NodeList)xpath.evaluate(expression, inputSource, XPathConstants.NODESET);
////      for(int i = 0; i < nodes.getLength(); i++)
////      {
////      	logger.info(nodes.item(i).getNodeName());
////      	logger.info(nodes.item(i).getNodeValue());
////      	logger.info(nodes.item(i).toString());
////      }
//    
////      doc.getElementsByTagName("SV_IP");
//
//    
////      // Change the document viewBox.
////      svg.setAttributeNS(null, "viewBox", "40 95 370 265");
////
////      // Make the text look nice.
////      svg.setAttributeNS(null, "text-rendering", "geometricPrecision");
////
////      // Remove the xml-stylesheet PI.
////      for (Node n = svg.getPreviousSibling();
////              n != null;
////              n = n.getPreviousSibling()) {
////          if (n.getNodeType() == Node.PROCESSING_INSTRUCTION_NODE) {
////              doc.removeChild(n);
////              break;
////          }
////      }
////
////      // Remove the Batik sample mark 'use' element.
////      for (Node n = svg.getLastChild();
////              n != null;
////              n = n.getPreviousSibling()) {
////          if (n.getNodeType() == Node.ELEMENT_NODE
////                  && n.getLocalName().equals("use")) {
////              svg.removeChild(n);
////              break;
////          }
////      }
//} catch (Exception ex) {
//	logger.info(ex.toString());
//}

////svg�����������    
//public void ChangeElement(Element elm)
//{
////	logger.info(elm.getAttributes().getNamedItem("id").getNodeValue());
////  Element elm = doc.getElementById("group772-317");
////  elm.setAttribute("transform","translate(100, 0)");
//	
//  elm.setAttribute("fill", "red"); // --> ��Щͼû�з�Ӧ
//  
//  elm.setAttribute("stroke","green"); // --> ��Щͼû�з�Ӧ
//  
//  ((EventTarget) elm).addEventListener("mousedown", new EventListener() {
//      public void handleEvent(Event evt) {
//          ((Element)evt.getTarget()).setAttributeNS(null, "fill", "orange");	            
//      }
//  }, false);
//}
