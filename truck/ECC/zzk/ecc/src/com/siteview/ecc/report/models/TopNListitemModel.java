package com.siteview.ecc.report.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Image;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Window;

import com.siteview.base.data.IniFile;
import com.siteview.ecc.report.beans.TopNBean;
import com.siteview.ecc.util.Toolkit;

public class TopNListitemModel extends ListModelList implements ListitemRenderer {

	private static final String	INI_FILE	= "topnreportset.ini";

	private IniFile				file;
	private String				currsection	= null;
	private Window				thewindow;

	public TopNListitemModel(Window thewindow) {
		super();
		this.thewindow = thewindow;
		file = new IniFile(INI_FILE);
		try {
			file.load();
		} catch (Exception e) {
		}
		try {
			getTopNData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成topN报告数据,加载时数据
	 */
	public List<TopNBean> getTopNData() throws Exception {
		if (file == null)
			throw new Exception("构造" + INI_FILE + "时出错！");
		List<TopNBean> beans = new ArrayList<TopNBean>();
		List<String> sectionList = file.getSectionList();
		for (String section : sectionList) {
			// 该报告已删除掉，或不存在
			if (section.equalsIgnoreCase("TempSection(Please_modify_it)"))
				continue;
			Map<String, String> sectionData = file.getSectionData(section);
			TopNBean bean = new TopNBean();
			if (sectionData != null) {
				bean.setSection(section); 
				String title = sectionData.get("Title");
				int index = title.indexOf("|");
				if(index>0) title = title.substring(0,index);
				String descript=sectionData.get("Descript");
				if(descript!=null)
				{
					bean.setDescript(descript);
				}
				String deny = sectionData.get("Deny");
				if (title != null ) {
					bean.setTitle(title);
				}
				bean.setPeriod(sectionData.get("Period"));
				if (deny != null) {
					if (deny.trim().toLowerCase().equals("no")) {
						bean.setDeny("允许");
					} else
						bean.setDeny("禁止");
				}
				String filetype=sectionData.get("fileType");
				if(filetype==null||filetype.equals(""))
				{
					filetype="html";
				}
				String createTime=sectionData.get("CreateTime");
				Date createDate = new Date();
				try{
					createDate = Toolkit.getToolkit().parseDate(createTime);
				}catch(Exception e){
				}
				bean.setFiletype(filetype);
				Image editImage = new Image();
				editImage.setSrc("/main/images/alert/edit.gif");
				editImage.addForward("onClick", thewindow, "onTpenEditTop_NReport", section);
				/*
				 * editImage.addEventListener(Events.ON_CLICK, new EventListener() { public void
				 * onEvent(Event arg0) throws Exception { openEditTop_NReport(currsection); } });
				 */

				bean.setEditImage(editImage);
			}
			beans.add(bean);
		}
		
		Collections.sort(beans, new Comparator<TopNBean>(){
			@Override
			public int compare(TopNBean o1, TopNBean o2) {
				return o1.getTitle().compareTo(o2.getTitle());
			}
		});
		addAll(beans);
		return beans;
	}

	

	@Override
	public void render(Listitem arg0, Object arg1) throws Exception {
		Listitem item = arg0;
		TopNBean m = (TopNBean) arg1;
		arg0.setId(m.getSection());
		Listcell tmpli=null;
		item.addForward("onClick", thewindow, "onSelecttopNList");
		String title=m.getTitle();
		tmpli=new Listcell(title);
		tmpli.setParent(item);
		tmpli.setTooltiptext(title);
		String dstr=m.getDescript();
		tmpli=new Listcell(dstr);
		tmpli.setParent(item) ;
		tmpli.setTooltiptext(dstr);
		new Listcell(m.getPeriod()).setParent(item);
		String fileType=m.getFiletype();
		Listcell cell=new Listcell();
		cell.setParent(item);
		Image img=new Image("/main/images/filetype/"+fileType+".gif");
		img.setTooltiptext(fileType);
		img.setParent(cell);
		if(m.getDeny().equals("允许")){
			Listcell l1=new Listcell("允许");
			l1.setImage("/main/images/button/ico/enable_bt.gif");
			l1.setParent(item);
		}else{
			Listcell l1=new Listcell("禁止");
			l1.setImage("/main/images/button/ico/disable_bt.gif");
			l1.setParent(item);	
		}

		Listcell li = new Listcell();
		li.setParent(item);
		li.appendChild(m.getEditImage());
	}
	
	
}
