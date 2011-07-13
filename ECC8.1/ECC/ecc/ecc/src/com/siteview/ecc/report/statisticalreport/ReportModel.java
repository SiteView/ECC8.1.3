package com.siteview.ecc.report.statisticalreport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;

import com.siteview.base.data.IniFile;
import com.siteview.ecc.util.Toolkit;

public class ReportModel extends ListModelList {
	private final static Logger logger = Logger.getLogger(ReportModel.class);

	private ArrayList<ReportItem> list = new ArrayList<ReportItem>();

	/**
	 * @return the list
	 */
	public ArrayList<ReportItem> getList() {
		return list;
	}




	/**
	 * @param list the list to set
	 */
	public void setList(ArrayList<ReportItem> list) {
		this.list = list;
	}




	public ReportModel(IniFile reportset) {
		try 
		{
			Map<String, Map<String, String>> map = reportset.getFmap();
			Iterator iterator = map.keySet().iterator();
			while (iterator.hasNext()) {
				String reportID = iterator.next().toString();
				if(reportID.equals("TempSection(Please_modify_it)"))
					continue;
				ReportItem item = new ReportItem(reportID,map.get(reportID));
				logger.info(item.getCreatTime());
				list.add(item);
			}
			
			Collections.sort(list,new Comparator<ReportItem>(){
				@Override
				public int compare(ReportItem o1, ReportItem o2) {
					return o1.getTitle().compareTo(o2.getTitle());
				}
			});
			addAll(list);
		} catch (Exception e) {
			Toolkit.getToolkit().showError(e.getMessage());
		}
	}


	

	private void createListHeader(Listhead head)
	{
		Listheader header=new Listheader();
		header.setLabel("");
		header.setParent(head);
		header.setAlign("left");
		/*header=new Listheader();
		header.setLabel("标题");
		header.setParent(head);
		
		header=new Listheader();
		header.setLabel("格式");
		header.setParent(head);
		
		header=new Listheader();
		header.setLabel("生成日期");
		header.setParent(head);
		header.setSort("auto");
		header.setSortDirection("ascending");

		
		header=new Listheader();
		header.setLabel("创建者");
		header.setParent(head);*/
		

	}
}
