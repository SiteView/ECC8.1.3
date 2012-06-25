package com.siteview.ecc.controlpanel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import com.siteview.actions.ActionMenuOpenListener;
import com.siteview.base.manage.View;
import com.siteview.base.tree.INode;
import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.treeview.EccWebAppInit;
import com.siteview.ecc.util.Toolkit;
import com.siteview.ecc.util.TooltipPopup;

public abstract class EccListModel extends ListModelList implements RowRenderer,ListitemRenderer,IEccColumnSource,IconRenderer
{
	protected EccTreeItem parentNode=null;
	private ArrayList<EventListener> eventListeners=new ArrayList<EventListener>();
	private EventListener actionMenuCliclListener=null;
	public void addClickTitleListener(EventListener l)
	{
		if(eventListeners.indexOf(l)==-1)
			eventListeners.add(l);
	}
	public void setParentNode(EccTreeItem parentNode) 
	{
		if(!this.parentNode.equals(parentNode))
		{
			this.parentNode = parentNode;
			refresh();
		}
	}
	protected View view=null;
	
	int filter=EccTreeItem.STATUS_ALL;
	public int getFilter() {
		return filter;
	}
	public void setFilter(int filter) {
			this.filter = filter;
			refresh();
	}
	boolean inherit=false;/*是否显示继承的子孙数据*/
	public boolean isInherit() {
		return inherit;
	}
	public void setInherit(boolean inherit) {
		if(this.inherit !=inherit)
		{
			this.inherit = inherit;
			refresh();
		}
	}
	public EccListModel(View view,EccTreeItem parentNode)
	{
		super();
		this.parentNode=parentNode;
		this.view=view;
		refresh();
	}
	public EccListModel(View view,EccTreeItem parentNode,boolean displayInherit)
	{
		super();
		this.parentNode=parentNode;
		this.view=view;
		this.inherit=displayInherit;
		refresh();
	}
	public EccListModel(View view,EccTreeItem parentNode,boolean displayInherit,int filter)
	{
		super();
		this.parentNode=parentNode;
		this.view=view;
		this.inherit=displayInherit;
		this.filter=filter;
		refresh();
	}
	
	/*刷新数据*/
	public abstract void refresh();
	public int getColCount() {
			return 0;
	}
	
	public String getTitle(int idxCol) {
			return "";
		}
	public int forceColWidth(int idxCol) {
			return -1;
		}
	public boolean isNumber(int idxCol) {
			return false;
		}
	/*
	 * 根据过滤条件设置
	 */
	void addByFilter(List<EccTreeItem> list,EccTreeItem item)
	{
		if((filter&item.getStatus())==item.getStatus())
			list.add(item);
			
	}
	/*实现该方法支持缩略图*/
	/*@Override
	public void renderIcon(IconCell cell, Object data) 
	{
	}*/
	@Override
	public void render(Row parent, Object data) throws Exception 
	{
				EccTreeItem item=(EccTreeItem)data;
				parent.setValue(item);
        Image icon = new Image();
        icon.setStyle("padding: 10px 10px");
        icon.setSrc(EccWebAppInit.getInstance().getStatusImage(Toolkit.getToolkit().changeStatusToString(item.getStatus())));
        icon.setParent(parent);
        icon.setAlign("middle");
        //icon.setAttribute("eccTreeItem", item);
        //icon.setTooltip("nodeInfoTooltip");
        //icon.setAction("onmouseover:this.style.zoom=1.5;onmouseout:this.style.zoom=1");
        icon.setHover(Toolkit.getToolkit().getHoverImage(icon.getSrc()));
        Map<Integer, String> map = this.getValue(item).getValueByIndex(getColCount());
         for(int i=1;i<this.getColCount();i++)
        	 new Label(map.get(i)).setParent(parent);
         
     }
	@Override
	public void render(Listitem parent, Object data) throws Exception 
	{
		parent.setHeight("23px");
		EccTreeItem item=(EccTreeItem)data;
		parent.setValue(item);
		parent.setCheckable(true);
		
		Listcell cell1=new Listcell();
		Image img=new Image(EccWebAppInit.getInstance().getStatusImage(Toolkit.getToolkit().changeStatusToString(item.getStatus())));
		img.setHover(Toolkit.getToolkit().getHoverImage(img.getSrc()));
		img.setTooltip((TooltipPopup)Executions.getCurrent().getDesktop().getPage("eccmain").getFellow("nodeInfoTooltip"));
		img.setAttribute("eccTreeItem", item);
		img.setAlign("absmiddle");
		cell1.appendChild(img);
		cell1.setValue(item);
		cell1.setParent(parent);
		cell1.setAttribute("eccTreeItem", item);
		/*cell1.addCheckListener(new EventListener(){
			@Override
			public void onEvent(Event event) throws Exception {
				CheckableListcell cell1=(CheckableListcell)event.getTarget();
				if(cell1.isChecked())
					checkedItem.add((EccTreeItem)cell1.getAttribute("eccTreeItem"));
				else
					checkedItem.remove(cell1.getAttribute("eccTreeItem"));
			}});
		
		*/
		Listcell cell2=new Listcell();
		cell2.setValue(item);
		cell2.setParent(parent);
		
		
		Label lbl=new Label(item.getTitle());
		lbl.setTooltiptext(item.getTitle());
		lbl.setAttribute("eccTreeItem", item);
		lbl.setParent(cell2);
		if(!item.getType().equals(INode.MONITOR))
		{
			lbl.setStyle("float:left;border-bottom: 1px dashed #c0c0c0;padding-left:5px;color:#18599C");
			for(EventListener l:eventListeners)
				lbl.addEventListener("onClick", l);
		}
		
		Image imgMenu=new Image("/main/images/ic_menu.gif");
		imgMenu.setHover("/main/images/ic_menu_hover.gif");
		//imgMenu.setStyle("padding-left:10px;float:right");
		imgMenu.setAlign("right");
		imgMenu.setParent(cell2);
		imgMenu.setAttribute("eccTreeItem", item);
		imgMenu.setWidth("10px");
		//imgMenu.setPopup("action_popup");
		imgMenu.addEventListener("onClick",getActionMenuCliclListener());
		
		cell2.setAttribute("eccTreeItem", item);
		cell2.addEventListener("onRightClick", getActionMenuCliclListener());
		ListDataBean bean = this.getValue(item);
		if (bean == null) return;
		Map<Integer, String> map = bean.getValueByIndex(getColCount());
		for(int i=1;i<this.getColCount();i++)
		{
			String lblStr=map.get(i);
			/*if(lblStr==null||lblStr.trim().length()==0)
				lblStr="无";
			else
				lblStr=lblStr.trim();*/
			Listcell aCell=new Listcell(lblStr);
			/*if(forceColWidth(i)!=-1)
				aCell.setStyle("width:"+forceColWidth(i)+"px;overflow:hidden");
			*/
			
			aCell.setTooltiptext(lblStr);
			aCell.setParent(parent);
			aCell.setAttribute("eccTreeItem", item);
			aCell.addEventListener("onRightClick",getActionMenuCliclListener());
		}
		
		
	}
	private EventListener getActionMenuCliclListener()
	{
		if(this.actionMenuCliclListener==null)
		{
			actionMenuCliclListener=new ActionMenuOpenListener();
		}
		
		return actionMenuCliclListener;
	}
	@Override
	public void renderIcon(IconCell cell, Object data) 
	{
		EccTreeItem item=(EccTreeItem)data;
		cell.setValue(item);
		cell.setAttribute("eccTreeItem", item);
		//处理字符串
		String str = item.getTitle();
		if(str.getBytes().length != str.length()){	//处理包含中文情况
			if(str.getBytes().length >= 20){
				char[] chars = str.toCharArray();
				int i = 0;
				int index = 0;
				StringBuffer buff = new StringBuffer();
				while(index < 16 && index < str.length()){
					if(Character.isLetterOrDigit(chars[i])){
						buff.append(chars[i]);
						index++;
						i++;
					}else{
						buff.append(chars[i]);
						index += 2;
						i++;
					}
				}
				str = buff.toString();
				//
				if(str.length() > 13){
					try{
						str = buff.substring(0, 13);
					}catch(Exception e){
						str.substring(0, 12);
					}
				}
				str += "...";
			}
		}else{
			if(str.length() >= 20){					//处理不含中文情况
				str = str.substring(0, 17) + "...";
			}
		}
		cell.setName(str);
		//cell.setDesciption(Toolkit.getINodeDesc(cell.getDesktop().getSession(),view.getNode(item.getId())));
		cell.setImgSrc(EccWebAppInit.getInstance().getStatusImage(Toolkit.getToolkit().changeStatusToString(item.getStatus())));
		//cell.setImgSrc(EccWebAppInit.getInstance().getImage(item.getType(),item.getStatus()));
		cell.getStatusImage().setAttribute("eccTreeItem", item);
		cell.getStatusImage().setTooltip((TooltipPopup)Executions.getCurrent().getDesktop().getPage("eccmain").getFellow("nodeInfoTooltip"));
		
		//cell.getStatusImage().setAction("action:this.onmouseover='this.width=20px;this.height=20px;';this.onmouseout:'this.width=16px;this.height=16px;'");
		cell.getStatusImage().setHover(Toolkit.getToolkit().getHoverImage(cell.getStatusImage().getSrc()));
		
	}
}
