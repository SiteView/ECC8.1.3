package com.siteview.ecc.treeview.windows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Image;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Window;

import com.siteview.base.data.IniFile;
import com.siteview.ecc.report.beans.TopNBean;

public class UrlTransModel extends ListModelList implements ListitemRenderer
{
	//private String	INI_FILE	= "_UrlStepInfo.ini";
	//private IniFile	file;
	private static String		stepdetail_TargetUrl	= "/main/TreeView/wstepdetail.zul";
	private String monitorid;
	private UrlTransStepInfo urlTransStepInfo;
	List<UrlTransBean> UrlTranslist=null;
	UrlTransStepData data1=new UrlTransStepData();
	public UrlTransModel(String monitorid,UrlTransStepInfo urlTransStepInfo)
	{
		super();
		this.monitorid=monitorid;
		this.urlTransStepInfo=urlTransStepInfo;
//		INI_FILE = monitorid.trim() + INI_FILE;
		try
		{
//			file = new IniFile(INI_FILE);
//			file.load();
			UrlTranslist=data1.GetAllUrlSteps(monitorid);
			BuildData();
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	private void BuildData()
	{
//		List<UrlTransBean> beans = new ArrayList<UrlTransBean>();
//		
//		List<String> sectionList = file.getSectionList();
		for(UrlTransBean ubean:UrlTranslist)
		{
			Image editImage = new Image();
			editImage.setSrc("/main/images/alert/edit.gif");
			editImage.addEventListener("onClick", new imgclick(ubean.getStepindex()));
			ubean.setEditImage(editImage);
			
		}
//		for (String section : sectionList)
//		{
//			HashMap<String, String> sectionData = file.getSectionData(section);
//			UrlTransBean bean = new UrlTransBean();
//			if (sectionData != null)
//			{
//				String name=sectionData.get("StepName");
//				if(name==null)
//				{
//					name="";
//				}
//				bean.setStepname(name);
//				String type=sectionData.get("StepType");
//				if (type==null)
//				{
//					type="";
//				}
//		        bean.setSteptype(type);
//				
//				beans.add(bean);
//			}
//		}
		addAll(UrlTranslist);
	}
	
	class imgclick implements EventListener
	{
	//	HashMap<String, String> sectionData;
		String nIndex;
		public imgclick(String nIndex)
		{
		// this.sectionData=sectionData;	
			this.nIndex=nIndex;
		}

		@Override
		public void onEvent(Event arg0) throws Exception
		{
			// TODO Auto-generated method stub
			final Window win = (Window) Executions.createComponents(stepdetail_TargetUrl, null, null);
			win.setAttribute("monitorid", monitorid);
			win.setAttribute("nIndex", nIndex);
			win.setAttribute("isedit", true);
			try
			{
				win.doModal();
				//Ë¢ÐÂÊý¾Ý
				urlTransStepInfo.builddata();
			} catch (Exception e)
			{
			}
		}
		
	}
	@Override
	public void render(Listitem arg0, Object arg1) throws Exception
	{
		// TODO Auto-generated method stub
		Listitem item = arg0;
		UrlTransBean ubean = (UrlTransBean) arg1;
		Listcell tmpli=null;
		String name=ubean.getStepname();
		tmpli=new Listcell(name);
		tmpli.setParent(item);
		tmpli.setTooltiptext(name);
		String type=ubean.getSteptype();
		tmpli=new Listcell(type);
		tmpli.setParent(item);
		tmpli.setTooltiptext(type);
		
		tmpli=new Listcell();
		tmpli.setParent(item);
		tmpli.appendChild(ubean.getEditImage());
		
		
	}
	
}
