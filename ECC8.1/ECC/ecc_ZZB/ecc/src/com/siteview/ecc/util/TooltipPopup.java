package com.siteview.ecc.util;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;

import com.siteview.base.manage.View;
import com.siteview.base.tree.INode;
import com.siteview.base.treeEdit.EntityEdit;
import com.siteview.base.treeInfo.EntityInfo;
import com.siteview.base.treeInfo.MonitorInfo;
import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.treeview.EccWebAppInit;

public class TooltipPopup extends TooltipPopupBase {
	
	private Label idLbl=new Label();
	private Label nameLbl=new Label();
	private Label statusLbl=new Label();
	private Label descLbl=new Label();
	private Label typeLbl=new Label();
	private Label creaTimeLbl=new Label();
	private Label miaoshuLbl=new Label();
	private Label ipAddrLbl=new Label();
	
	PopupOpenListener openListener=new PopupOpenListener();
	public TooltipPopup() {
		super();
		super.setWidth("300px");
	}
	public void onCreate()
	{
		super.onCreate();
		addEventListener("onOpen",openListener);
	}
	public String getDesc() {
		return descLbl.getValue();
	}
	public void setDesc(String text) {
		this.descLbl.setValue(text);
	}
	private void initItem(EccTreeItem node)
	{
		while(getRows().getLastChild()!=null)
			getRows().removeChild(getRows().getLastChild());
		Row row;
		Div div;
		//row=new Row();
		//row.setParent(rows);
		//new Label(" ID:").setParent(row);不显示ID
		//idLbl.setParent(row);
		
		//row=new Row();
		//row.setParent(rows);
		//new Label(" 名称:").setParent(row);
		//nameLbl.setParent(row);不显示名称

		if(node.getType().equals(INode.ENTITY))
		{
			row=new Row();
			row.setParent(getRows());
			div=new Div();
			row.appendChild(div);
			new Label(" 类型:").setParent(div);
			typeLbl.setParent(div);

			row=new Row();
			row.setParent(getRows());
			div=new Div();
			row.appendChild(div);
			new Label(" 地址:").setParent(div);
			ipAddrLbl.setParent(div);
			
		}
		row=new Row();
		row.setParent(getRows());
		div=new Div();
		row.appendChild(div);
		new Label(" 状态:").setParent(div);
		statusLbl.setParent(div);
		
		if(node.getType().equals(INode.ENTITY))
		{
			row=new Row();
			row.setParent(getRows());
			div=new Div();
			row.appendChild(div);
			new Label(" 描述:").setParent(div);
			miaoshuLbl.setParent(div);
			
			row=new Row();
			row.setParent(getRows());
			div=new Div();
			row.appendChild(div);
			new Label(" 创建:").setParent(div);
			creaTimeLbl.setParent(div);
			
		}
		row=new Row();
		div=new Div();
		row.setParent(getRows());
		row.appendChild(div);
		new Label(" 信息:").setParent(div);
		descLbl.setMultiline(true);
		descLbl.setParent(div);
	}
	public void refresh(EccTreeItem node)
	{
		initItem(node);
		setTitle(node.getTitle());

		if(node.getType().equals(INode.ENTITY))
		{
			
			View view=Toolkit.getToolkit().getSvdbView(getDesktop());
			EntityInfo entityInfo=view.getEntityInfo(node.getValue());
			StringBuffer sb=new StringBuffer();
			typeLbl.setValue(entityInfo.getDeviceType());
			if(entityInfo.getSvDescription().length()>0)
				miaoshuLbl.setValue(entityInfo.getSvDescription());
			else
				miaoshuLbl.setValue("无");
			if(entityInfo.getIpAdress()!=null)
				ipAddrLbl.setValue(entityInfo.getIpAdress());
			
			if(entityInfo.getCreatTime().length()!=0)
				creaTimeLbl.setValue(entityInfo.getCreatTime());
			else
			{
				if(node.getChildRen().size()>0)
				{
					MonitorInfo monitorInfo=view.getMonitorInfo(node.getChildRen().get(0).getValue());
					creaTimeLbl.setValue(monitorInfo.getCreateTime());
				}
				
			}
		}
	
		setDesc(Toolkit.getToolkit().getINodeDesc(getDesktop().getSession(),node.getValue()));
		setSvId(node.getId());
		setName(node.getTitle());
		setImage(EccWebAppInit.getInstance().getImage(node.getType(), node.getStatus()));		
		setStatus(node.getStatus());

	}
	public void setStatus(int status)
	{
		statusLbl.setValue(Toolkit.getToolkit().changeStatusToChinese(status));
		for(Object row:getRows().getChildren())
		{
			((Row)row).setStyle(Toolkit.getToolkit().getStatusStyle(status));
			for(Object com:((Row)row).getChildren())
				if(com instanceof Label)
					((Label)com).setStyle(Toolkit.getToolkit().getStatusStyle(status));
		}
	}
	public void setName(String name)
	{
		nameLbl.setValue(name);
	}
	public void setSvId(String id)
	{
		idLbl.setValue(id);
	}
	
}
