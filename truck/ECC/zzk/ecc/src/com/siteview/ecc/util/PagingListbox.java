package com.siteview.ecc.util;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.impl.XulElement;

public class PagingListbox extends Listbox 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1423702687064621135L;

	public PagingListbox(String listID,XulElement parent) {
		super();
		init(listID,parent);
	}
	
	private void init(String listID,XulElement parent)
	{
		
		
		super.setMold("paging");
		super.getPagingChild().setMold("os");
		
		PagingTool pagingTool=new PagingTool();
		parent.appendChild(this);
		parent.appendChild(pagingTool);
		setPaginal(pagingTool);
		pagingTool.setDetailed(true);
		pagingTool.setVisible(true);
		
		
		String pageSize=Toolkit.getToolkit().getCookie(listID+"_pageRow");
		if(pageSize==null)
			pageSize="15";
		
		int pgsz=Integer.parseInt(pageSize);
		super.setPageSize(pgsz);
		super.getPaginal().setPageSize(pgsz);
		pagingTool.setPageSize(pgsz);
	}
	

}
