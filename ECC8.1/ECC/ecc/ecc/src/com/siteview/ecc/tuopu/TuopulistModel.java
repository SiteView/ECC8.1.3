package com.siteview.ecc.tuopu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Image;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import com.siteview.base.data.IniFile;
import com.siteview.base.manage.View;
import com.siteview.ecc.controlpanel.EccListModel;
import com.siteview.ecc.controlpanel.ListDataBean;
import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.treeview.EccWebAppInit;

//import org.apache.commons.codec.DecoderException;
//import org.apache.commons.codec.EncoderException;
//import org.apache.commons.codec.binary.Base64;

public class TuopulistModel extends EccListModel
{	
	private final static Logger logger = Logger.getLogger(TuopulistModel.class);
	public TuopulistModel(View view,EccTreeItem selectedNode)
	{
		super(view, selectedNode);
	}
	
	//ˢ�������б�
	public void refresh()
	{
		clear();
		addAll(GetTuopuList().values());
	}
	
	//��ȡ�����б�����
	public LinkedHashMap<String, LinkedHashMap<String, String>> GetTuopuList()
	{
		LinkedHashMap<String, LinkedHashMap<String, String>> tuopHash = new LinkedHashMap();
		
		try
		{
			//1��ö��Ŀ¼/ecc/WebContent/main/tuoplist/��ȡʵ�ʴ��ڵ�����ͼ�б�
			String strPath = EccWebAppInit.getWebDir() + "main\\tuoplist\\";

			File dir = new File(strPath);//������ǰĿ¼���ļ���File���� 
			File list[] = dir.listFiles();//ȡ�ô���Ŀ¼�������ļ���File�������� 
			
			LinkedHashMap<String, File> tempFileMap = new LinkedHashMap<String, File>();
			ArrayList<String> tempFileList = new ArrayList<String>();
			for(File file : list){
				tempFileMap.put(file.getName(), file);
				tempFileList.add(file.getName());
			}
			Collections.sort(tempFileList);
			list = new File[tempFileList.size()];
			for(int i=0;i<tempFileList.size();i++){
				list[i] = tempFileMap.get(tempFileList.get(i));
			}
 			
			for(int i=0; i<list.length; i++)
			{
				if(list[i].isFile())
				{
					int index = list[i].getName().indexOf(".htm");
					if(index != -1)
					{
						LinkedHashMap<String, String> value = new LinkedHashMap();
						
						//
						String strTmp = list[i].getName().substring(0, index);
						
//						value.put("name", list[i].getName());
						value.put("htmlname", list[i].getName());
						value.put("showurl", "/main/tuoplist/showtuopu.zul?name=" + strTmp);
						value.put("vsdname", strTmp + ".vsd");
						value.put("downvsdurl", "/main/tuoplist/" + strTmp + ".vsd");
						value.put("edit", "�༭����");
						
						tuopHash.put(strTmp, value);
					}					
				}
			}
			
			//2�������б�������Ӧ�tuopu.ini
			IniFile ini = new IniFile("tuopfile.ini");
			
			try{
				ini.load();
			}catch(Exception e){
				e.printStackTrace();
			}
			
			for(String key : tuopHash.keySet())
			{
				if (tuopHash.get(key) == null){
					 continue;
				}
				if(ini.getKeyList("filename") == null || !ini.getKeyList("filename").contains(tuopHash.get(key).get("htmlname")))
				{
					if(ini.getFmap().get("filename")==null){
						ini.createSection("filename");
					}
					ini.setKeyValue("filename", tuopHash.get(key).get("htmlname"), tuopHash.get(key).get("htmlname"));
					ini.setKeyValue("filename", tuopHash.get(key).get("vsdname"), tuopHash.get(key).get("vsdname"));
				}
			}

			ini.saveChange();			
			
			ini.load();
			
			//3����tuopu.ini��������ͼ����
			for(String key : tuopHash.keySet())
			{	
				tuopHash.get(key).put("name", 
						ini.getValue("filename", tuopHash.get(key).get("htmlname").toString()));
			}
			
			//4�������û����ܹ��������ͼȨ�޹��˳��б���Ҫ�����������
			if(view !=null)
			{
				ini= view.getUserIni();
				if(ini!=null){
					List<String> lstTuopu = ini.getKeyList(ini.getSections());
					logger.info(" user.ini keys: " + lstTuopu);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return tuopHash;		
	}
	
	@Override
	public void render(Listitem parent, Object data) throws Exception
	{		
		parent.setHeight("30px");
		LinkedHashMap<String, String> item = (LinkedHashMap<String, String>)data;
		
		//ɾ������ͼʱ�õ�����
		parent.setAttribute("htmlname", item.get("htmlname").toString());
		
	    //ѡ��CheckBox��
		Image icon0 = new Image();        
        Listcell cell = new Listcell("");
        cell.setParent(parent);
        icon0.setParent(cell);
        
        Listcell cell1 = new Listcell("");
        cell1.setParent(parent);        
        
        //����ͼ���Ƽ���������ͼ������
        Toolbarbutton tbb = new Toolbarbutton();  
        tbb.setStyle("color:blue");
    	tbb.setLabel(item.get("name").toString());
    	tbb.setHref(item.get("showurl").toString());
    	tbb.setTarget("_blank");    
    	tbb.setParent(cell1);
    	
    	Listcell cell2 = new Listcell("");
        cell2.setParent(parent);

        //����ͼ������
        Toolbarbutton tbb1 = new Toolbarbutton();  
        tbb1.setImage("/images/resource.gif");
        tbb1.setHref(item.get("downvsdurl").toString());
        tbb1.setTarget("_blank");    
        tbb1.setParent(cell2);
    	
        Listcell cell3 = new Listcell("");
        cell3.setParent(parent);
        
        //�༭����ͼ��ʾ������
        Image icon1 = new Image();
    	icon1.setSrc("/images/edit.gif");        
        icon1.setParent(cell3);
        icon1.setAttribute("htmlname", item.get("htmlname").toString());
        icon1.setAttribute("name", item.get("name").toString());
        
        //�༭����ͼ��ʾ�����¼���
        icon1.addEventListener(Events.ON_CLICK, new EventListener() {
			@Override
			public void onEvent(Event arg0) throws Exception 
			{				
				OnEditTuopuName(arg0);
			}   
		});
	}
	
	//����ͼ��������
	@Override
	public ListDataBean getValue(Object rowValue) 
	{
		LinkedHashMap<String, String> item = (LinkedHashMap<String, String>)rowValue;
		ListDataBean bean = new ListDataBean();
		if(item==null)
			return null;
		
		bean.setName(item.get("name"));
//		switch(idxCol)
//		{
//			case 0:
//				return item.get("name");
//		}
		return bean;
	}

	//�༭ָ������ͼ����
	public void OnEditTuopuName(Event event)
	{
		Image icon = (Image)event.getTarget();
		String strName = icon.getAttribute("htmlname").toString();
		String strShowName = icon.getAttribute("name").toString();
		
		try 
		{

			//���༭���ƴ���
			final Window win = (Window) Executions.createComponents(
					"tuoplist/edittuopname.zul", null, null);					
//					"/main/tuoplist/edittuopname.zul?name=" + strName, null, null);
			
			//���ô��ڼ乲�����
//			win.setAttribute("name", strName);
//			win.setAttribute("showname", strShowName);
			((Textbox)win.getFellow("name")).setValue(strName);
			((Textbox)win.getFellow("showname")).setValue(strShowName);
			win.setAttribute("tuopuListBox", icon.getParent().getParent().getParent());
//			win.setAttribute("parent", );
			
			//��������			
			win.doModal();
			
//			txt.setReadonly(true);
//			txt.setFocus(false);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
//	//��ʼ�༭����ͼ����
//	public void OnStartEditTuopu(Event event)
//	{
//		Textbox txt = (Textbox)event.getTarget();
//		
//		txt.setReadonly(false);
//	}
//	
//	//
//	public void OnStartEditTuopu1(Event event)
//	{
//		Label tuopulabel = (Label)event.getTarget();
//		
//		Textbox txt = (Textbox)tuopulabel.getAttribute("editBox");
//		txt.setReadonly(false);
//		txt.setFocus(true);
//	}
//
//	
//	//�༭����ͼ�������
//	public void OnEditTuopuName(Event event)
//	{
//		Textbox txt = (Textbox)event.getTarget();
//		
//		try 
//		{
//			IniFile ini = new IniFile("tuopfile.ini");			
//			ini.load();
//			
//			ini.setKeyValue("filename", txt.getAttribute("htmlname").toString(), txt.getText());
//			ini.saveChange();
//			
//			txt.setReadonly(true);
//			txt.setFocus(false);
//		}
//		catch (Exception e) 
//		{
//			e.printStackTrace();
//		}
//	}
//
//	//ShowTuopu�����ʵ�ַ�ʽ
//	public void OnShowTuopu(Event event)
//	{
//		Label tuopulabel = (Label)event.getTarget();
//		
//		tuopulabel.getAttribute("url").toString();
//	}

//	public void OnClickDelBtn()
//	{
//
//	}
//
//	public void OnClickReSortBtn()
//	{
//		
//	}
//	
//	//��Filedownload��ʽ����Tuopu
//	public void OnDownLoadTuopu(Event event)
//	{
//		Label tuopulabel = (Label)event.getTarget();
//		
//		try 
//		{
//			Filedownload.save(tuopulabel.getAttribute("url").toString(), null);
//		}
//		catch (FileNotFoundException e) 
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}		
//	}
		
////////////////////////////////////////////////////////////////////////////////
	
	//Uploader������룬 ��ʱ������
	public static void Uploader(String directory, String filename, String content)
	{
		if(content == null || directory == null || filename == null)
			return;
		
	    String strDirPath = EccWebAppInit.getWebDir() + "main";
		
			OutputStream out=null;
		try 
		{	
			String path = strDirPath + directory;//��Ҫ������Ŀ¼·�� 
			File d = new File(path);//��������SubĿ¼��File���󣬲��õ�����һ������ 
			if(d.exists())
			{
				//���SubĿ¼�Ƿ���� 
	    		//d.delete(); 
				//out.println("SubĿ¼���ڣ���ɾ��"); 
			}
			else
			{ 
				d.mkdir();//����SubĿ¼ 
		        //out.println("SubĿ¼�����ڣ��ѽ���"); 
			}
			
			int index = 0; 
			String strTmp = "";
//			String strContent = content.replaceAll("\\\\r\\\\n", "\r\n");
//			String strContent = content.replaceAll("\\\\r\\\\n", "\r\n");
//			String strContent = MakeTuopuData.myReplaceAll(content, "\\r\\n", "\r\n");

//			CharTools tool = new CharTools();
//			String strTmpUrl = content;
//			if(tool.isUtf8Url(content))
//			{
//				strTmpUrl = tool.Utf8URLdecode(content);
//			}

			String strContent = MakeTuopuData.myReplaceAll(content, "\\r\\n", "\r\n");			
//			String strContent = MakeTuopuData.myReplaceAll(content, "\\r\\n", "");
//			FileWriter fw = new FileWriter(path +  "\\" + filename);//����FileWriter���󣬲�ʵ����fw
//			PrintWriter myFile = new PrintWriter(new OutputStreamWriter(new FileOutputStream(path +  "\\" + filename), "UTF-8"));
			
			out = new FileOutputStream(path +  "\\" + filename);
//			GenerateImage(strContent, out);
			logger.info("Base64.decode filename��");
			logger.info(filename);
			logger.info("Base64.decode content��");
			logger.info(content);
			logger.info("Base64.decode strContent��");
			logger.info(strContent);
			logger.info("Base64.decode Start��");
//			Base64 base64 = new Base64();
			while((index = strContent.indexOf("\r\n", 0)) >= 0)
			{
				if(index == 0)
				{
					strTmp = strContent.substring(index + 4);
					strContent = strTmp;
					continue;
				}

				String	strDecoder = strContent.substring(0, index);
				logger.info(strDecoder);
				
				logger.info("Base64.decode ��" + String.valueOf(index));
//				oldlogic
//				int		nCount = 0;
//				char[] szOutput = new char[16 * 1024];				
////				nCount = pDecoder->Decode(strDecoder, szOutput);				
//				szOutput[nCount] = 0;
//				//sFile.Write(szOutput, nCount);
				GenerateImage(strDecoder, out);
//				String szOutput1 = Base64.decode(strDecoder);
//				String szOutput1 = Base64Net.decode(strDecoder);				
//				String szOutput1 = Base64.decode(strDecoder, true);
//				byte[] szOutput1 = Base64.decode(strDecoder, false);
//				byte[] szOutput1 = Base64Chen.decode(strDecoder);
//				String szOutput1 = Base64.getFromBASE64(strDecoder);
//				byte[] szOutput1 = base64.decode(strDecoder.getBytes("UTF-8"));
				
//				logger.info(szOutput1);

				//���ַ���д���ļ�
//				fw.write(new String(szOutput1));
//				fw.write(szOutput1);
				
//				fw.write(szOutput1);
//				myFile.write(szOutput1);
//				(szOutput1);
//				out.close();
				strTmp = strContent.substring(index + 4);
				strContent = strTmp;
			}
//			
//			logger.info("Base64.decode End��");
//			if(strContent.indexOf("\r\n", 0) == -1)
//			{
//				byte[] szOutput1 = Base64.decode(strContent);
//				//���ַ���д���ļ�
//				fw.write(new String(szOutput1));
//			}
//			String szOutput1 = Base64.getFromBASE64(strContent);
//			//���ַ���д���ļ�
//			fw.write(szOutput1);	
//			logger.info(szOutput1);
//			fw.write(content);
//			myFile.close();
			
//			fw.close();
		}
		catch (Exception e) 
		{
		
		}finally
		{
			try{out.close();}catch(Exception r){}
		}		                  
	}

    // ���ֽ������ַ�������Base64���벢����ͼƬ   
	 public static boolean GenerateImage(String imgStr,OutputStream out) 
	 {   
	  if (imgStr == null) // ͼ������Ϊ��   
	   return false;   
//	  BASE64Decoder decoder = new BASE64Decoder();   
	  try {   
		   // Base64����   
//		   byte[] b = decoder.decodeBuffer(imgStr);
			  byte[] b =  Base64.decodeFast(imgStr);
		   for (int i = 0; i < b.length; ++i) {   
		    if (b[i] < 0) {// �����쳣����   
		     b[i] += 256;   
		    }   
		   }   
		//   OutputStream out = new FileOutputStream(imgFilePath);   
		   out.write(b);   
		   out.flush();   
//		   out.close();   
		   return true;   
	  }
	  catch (Exception e) 
	  {   
		  return false;   
	  }
	}
	 
//		@Override
//		public void render(Row row, Object data) throws Exception 
//		{
//		    LinkedHashMap<String, String> item = (LinkedHashMap<String, String>)data;
//	        Image icon = new Image();
////	        icon.setStyle("padding: 0px 10px");
////	        icon.setSrc(EccWebAppInit.getImage(item.getType(),item.getStatus()));
//	        icon.setParent(row);
//	        
////	        Button btn = new Button(item.get("name").toString());
////	        btn.setHref(item.get("showurl").toString());
////	        btn.setTarget("_blank");
////	        btn.setParent(row);
//	        
////	        Label label = new Label(item.get("name").toString());
////	        label.setAction("onclick:window.open('" + item.get("showurl").toString() + "')");
////	        label.setParent(row);
//	        
////	        label.setAttribute("url", item.get("showurl").toString());
////	        
	//
//////	        label.addEventListener(arg0, arg1)
////			label.addEventListener(Events.ON_CLICK, new EventListener() {
////				@Override
////				public void onEvent(Event arg0) throws Exception 
////				{				
////					OnShowTuopu(arg0);
////				}   
////	        });
	//
////	        Textbox txt = new Textbox(item.get("name").toString());
////	        txt.setReadonly(true);
////	        txt.setAction("onClick:window.open('" + item.get("showurl").toString() + "')");
////	        txt.setAttribute("htmlname", item.get("htmlname").toString());
////	        txt.setParent(row);
	//
//	        Toolbarbutton tbb = new Toolbarbutton();
//	    	tbb.setLabel(item.get("name").toString());
//	    	tbb.setHref(item.get("showurl").toString());
//	    	tbb.setTarget("_blank");
//	    	tbb.setParent(row);
//	    	
	//
//	    	tbb.addEventListener(Events.ON_CHANGE, new EventListener() {
//				@Override
//				public void onEvent(Event arg0) throws Exception 
//				{				
//					OnEditTuopuName(arg0);
//				}   
//	        });
	//
////	        txt.addEventListener(Events.ON_RIGHT_CLICK, new EventListener() {
////				@Override
////				public void onEvent(Event arg0) throws Exception 
////				{				
////					OnStartEditTuopu(arg0);
////				}   
////	        });
//	        
//	        Label label1 = new Label(item.get("vsdname").toString());
//	        //��js�ű���ʽ����tuopu
//	        label1.setAction("onclick:window.open('" + item.get("downvsdurl").toString() + "')");        
//	        label1.setParent(row);
////	        label.setAttribute("url", item.get("downvsdurl").toString());;
////			label.addEventListener(Events.ON_CLICK, new EventListener() {
////				@Override
////				public void onEvent(Event arg0) throws Exception 
////				{				
////					OnDownLoadTuopu(arg0);
////				}   
////	        });
//	        
////	        label.setAttribute("url", item.get("downvsdurl").toString());
	//
////	        new Label(item.get("vsdname").toString()).setParent(row);
//	        
//	        Label editLable = new Label(item.get("edit").toString());
//	        editLable.setParent(row);
//	        editLable.setAttribute("editBox", tbb);        
//	        editLable.addEventListener(Events.ON_CLICK, new EventListener() {
//				@Override
//				public void onEvent(Event arg0) throws Exception 
//				{				
//					OnStartEditTuopu1(arg0);
//				}   
//			});
//	    }
	 
}

