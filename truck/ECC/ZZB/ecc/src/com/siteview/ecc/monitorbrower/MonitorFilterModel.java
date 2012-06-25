package com.siteview.ecc.monitorbrower;

import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.siteview.ecc.alert.control.TooltipPopup;

public class MonitorFilterModel extends ListModelList implements
		ListitemRenderer {


	/**
	 * 
	 */
	private static final long serialVersionUID = 5035422231440697657L;

	public MonitorFilterModel() throws Exception {
		addAll(CustomViewSetting.getCustomView());
	}

	@Override
	public void render(Listitem arg0, Object arg1) throws Exception {
		CVBean bean = (CVBean) arg1;
		arg0.setValue(bean);
		TooltipPopup popup = new TooltipPopup();
		popup.onCreate();
		popup.setStyle("border:none;color:#FFFFFF;background-color:#717171");
		popup.setTitle(bean.getTitile());
		popup.addDescription("筛选名：", bean.getTitile());
		popup.addDescription("组名：", bean.getGroupName());
		popup.addDescription("设备名：", bean.getEntityName());
		popup.addDescription("监测器名：", bean.getMonitorName());
		popup.addDescription("监测器类型：", bean.getMonitorTypeName());
		popup.addDescription("显示类型：", bean.getShowHideName());
		popup.addDescription("描述：", bean.getMonitorDescripe());
		popup.addDescription("排序：", bean.getSortName());

//		popup.setParent(arg0);
//		arg0.setTooltip(popup);
		Listcell l1 = new Listcell(bean.getTitile());
		l1.setHeight("23px");
		l1.setTooltip(popup);
		popup.setParent(l1);
		l1.setParent(arg0);

		Listcell l2 = new Listcell(bean.getGroupName());
		l2.setHeight("23px");
		l2.setTooltip(popup);
		popup.setParent(l2);
		l2.setParent(arg0);

		Listcell l3 = new Listcell(bean.getEntityName());
		l3.setHeight("23px");
		l3.setTooltip(popup);
		popup.setParent(l3);
		l3.setParent(arg0);

		Listcell l4 = new Listcell(bean.getMonitorName());
		l4.setHeight("23px");
		l4.setTooltip(popup);
		popup.setParent(l4);
		l4.setParent(arg0);

		Listcell l5 = new Listcell(bean.getMonitorTypeName());
		l5.setHeight("23px");
		l5.setTooltip(popup);
		popup.setParent(l5);
		l5.setParent(arg0);

		Listcell l6 = new Listcell(bean.getShowHideName());
		l6.setHeight("23px");
		l6.setTooltip(popup);
		popup.setParent(l6);
		l6.setParent(arg0);

		Listcell l7 = new Listcell(bean.getMonitorDescripe());
		l7.setHeight("23px");
		l7.setTooltip(popup);
		popup.setParent(l7);
		l7.setParent(arg0);

		Listcell l8 = new Listcell(bean.getSortName());
		l8.setHeight("23px");
		l8.setTooltip(popup);
		popup.setParent(l8);
		l8.setParent(arg0);

		// new Listcell(bean.getTitile()).setParent(arg0);
		// new Listcell(bean.getGroupName()).setParent(arg0);
		// new Listcell(bean.getEntityName()).setParent(arg0);
		// new Listcell(bean.getMonitorName()).setParent(arg0);
		// new Listcell(bean.getMonitorTypeName()).setParent(arg0);
		// new Listcell(bean.getShowHideName()).setParent(arg0);
		// new Listcell(bean.getMonitorDescripe()).setParent(arg0);
		// new Listcell(bean.getSortName()).setParent(arg0);
	}

}
