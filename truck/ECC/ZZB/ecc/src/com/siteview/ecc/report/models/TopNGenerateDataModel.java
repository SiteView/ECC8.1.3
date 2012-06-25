package com.siteview.ecc.report.models;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.siteview.base.data.IniFile;
import com.siteview.ecc.report.beans.TopNGenerateDataBean;

public class TopNGenerateDataModel  extends ListModelList implements ListitemRenderer{
	
	private static final String INI_FILE = "topnreportgenerate.ini";
	
	private IniFile file;
	
	private String section;
	
	
	public TopNGenerateDataModel(String section){
		super();
		file = new IniFile(INI_FILE);
		this.section = section;
		try {
			file.load();
			getTopNGenerateTimeDate(section);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获得topN报告产生时间数据
	 * @param section
	 */
	private void getTopNGenerateTimeDate(String section){
		List<String> sections = file.getSectionList();	
		List<TopNGenerateDataBean> beans = new ArrayList<TopNGenerateDataBean>();
		if(sections==null && sections.size()==0)
			return;
		for(String sec : sections){
			//该报告已删除掉，或不存在
			if(sec.equalsIgnoreCase("TempSection(Please_modify_it)"))
				continue;
			int oneIndex = sec.indexOf("$");
			if(sec.substring(0, oneIndex).trim().equalsIgnoreCase(section)){
				TopNGenerateDataBean bean = new TopNGenerateDataBean();
				int twoIndex = sec.indexOf("$", oneIndex+1);
				int threeIndex = sec.indexOf("$", twoIndex+1);
				bean.setSection(sec.substring(0, oneIndex));
				bean.setStartTime(sec.substring(oneIndex,twoIndex));
				bean.setEndTime(sec.substring(twoIndex,threeIndex));
				beans.add(bean);
			}
		}
		addAll(beans);
	}
	@Override
	public void render(Listitem arg0, Object arg1) throws Exception
	{
		Listitem item = arg0;
		TopNGenerateDataBean m = (TopNGenerateDataBean) arg1;
//		item.setId(m.getSection());
		new Listcell(m.getStartTime()+"~"+m.getEndTime()).setParent(item);
	}
}
