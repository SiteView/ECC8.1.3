package com.siteview.ecc.alert.control;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Listbox;

/**
 * 使用该listbox必须保证有一个当前session里唯一的id
 * @author Administrator
 *
 */
public class RemainListbox extends Listbox{
	private final static Logger logger = Logger.getLogger(RemainListbox.class);
	
	private static final long serialVersionUID = 3581658682195475317L;

	public void onPaging(){
		Executions.getCurrent().getDesktop().getSession().setAttribute(this.getId()+"_currentPage", this.getActivePage());
	}
	
	public void onCreate(){
		setModel(getListModel());
		setItemRenderer(getItemRenderer());
	    int currentPage = 0;
		Object page = Executions.getCurrent().getDesktop().getSession().getAttribute(this.getId()+"_currentPage");
		logger.info(page+"****************************************************************************************");
		if(page!=null){
			currentPage = Integer.parseInt(page.toString());
		}
		if(this.getPageCount()>currentPage)
			this.setActivePage(currentPage);
	}
}
