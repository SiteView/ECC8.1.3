package com.siteview.actions;

import java.util.List;

import org.zkoss.zk.ui.Session;

import com.siteview.base.data.UserRightId;
import com.siteview.base.data.VirtualView;
import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.treeview.EccTreeModel;

public class GrantTreeModel extends EccTreeModel {

	int addedSpeciaNum=0;
	public GrantTreeModel(EccTreeItem root) {
		super(root);
	}

//	public static GrantTreeModel getInstance(Session session,String rootid) 
//	{
//		GrantTreeModel model=(GrantTreeModel)session.getAttribute("grantEccTreeModel_"+rootid);
//		if(model==null)
//		{
//			//modified by xiongqimin 2009-9-10
//			Tree tree = (org.zkoss.zul.Tree) Executions.getCurrent().getDesktop().getPage("eccmain").getFellow("tree");
//			EccTreeModel eccModel = ((EccTreeModel)tree.getModel()).clone();
//			EccTreeItem item = (EccTreeItem) eccModel.getRoot();
//			EccTreeItem cloneItem = (EccTreeItem) item.cloneThis();
//			
//			System.out.println(item +" dfadfasdf  " + cloneItem +"----------" +(item==cloneItem));
//			model = new GrantTreeModel((EccTreeItem) eccModel.getRoot().cloneThis());
//			
//			
////			model = new GrantTreeModel(new EccTreeItem(null,rootid,"监测树",""));
////			session.setAttribute("grantEccTreeModel_"+rootid, model);
//			model.addSpecialNode();
//		}
//		return model;
//	}
	
	public static GrantTreeModel getInstance(Session session,String rootid) //modifyed by qimin.xiong 2009-10-10
	{
		EccTreeModel eccModel = (EccTreeModel)session.getAttribute("eccWholeViewTreeModel");
		GrantTreeModel model = null;
		if(eccModel==null)
		{			
			model = new GrantTreeModel(new EccTreeItem(null,rootid,"监测树",""));
		}else
		{
			model = new GrantTreeModel(eccModel.getRoot());
		}
		model.addSpecialNode();
		return model;
	}
	public static GrantTreeModel getInstance(Session session) 
	{
		return getInstance(session,VirtualView.DefaultView);
	}

	public void addSpecialNode()
	{
		addSpecialNode(super.getRoot());
	}

	/*特殊逻辑，授权树上要增加额外的几个节点*/
	/*1.AlertRule后面增加子节点		m_AlertRuleAdd, m_AlertRuleDel, m_AlertRuleEdit
	 *2.ReportStatistic后面增加子节点  m_reportlistAdd, m_reportlistDel, m_reportlistEdit
	 */
	public void addSpecialNode(EccTreeItem eccTreeItem)
	{
/*		if(eccTreeItem==null)
			return;
		if(eccTreeItem.getType().equals("Alert"))
		{
			List<EccTreeItem> childrenList = eccTreeItem.getChildRen();
			boolean flag = true;
			String id1 = getVirtualItemId(UserRightId.DoAlertRuleAdd);
			String id2 = getVirtualItemId(UserRightId.DoAlertRuleEdit);
			String id3 = getVirtualItemId(UserRightId.DoAlertRuleDel);
			for(EccTreeItem childEccTreeItem:childrenList){
				if(id1.equals(childEccTreeItem.getId())||id2.equals(childEccTreeItem.getId())||id3.equals(childEccTreeItem.getId())){
					flag = false;
					break;
				}
			}
			if(flag){
				eccTreeItem.addChild(new EccTreeItem(eccTreeItem, getVirtualItemId(UserRightId.DoAlertRuleAdd),"添加报警",UserRightId.DoAlertRuleAdd));
				eccTreeItem.addChild(new EccTreeItem(eccTreeItem, getVirtualItemId(UserRightId.DoAlertRuleEdit),"编辑报警",UserRightId.DoAlertRuleEdit));
				eccTreeItem.addChild(new EccTreeItem(eccTreeItem, getVirtualItemId(UserRightId.DoAlertRuleDel),"删除报警",UserRightId.DoAlertRuleDel));
				addedSpeciaNum++;
			}

		}else if(eccTreeItem.getType().equals("Report"))
		{
			List<EccTreeItem> childrenList = eccTreeItem.getChildRen();
			boolean flag = true;
			String id1 = getVirtualItemId(UserRightId.DoReportlistAdd);
			String id2 = getVirtualItemId(UserRightId.DoReportlistEdit);
			String id3 = getVirtualItemId(UserRightId.DoReportlistDel);
			for(EccTreeItem childEccTreeItem:childrenList){
				if(id1.equals(childEccTreeItem.getId())||id2.equals(childEccTreeItem.getId())||id3.equals(childEccTreeItem.getId())){
					flag = false;
					break;
				}
			}
			if(flag){
				eccTreeItem.addChild(new EccTreeItem(eccTreeItem, getVirtualItemId(UserRightId.DoReportlistAdd),"添加报告",UserRightId.DoReportlistAdd));
				eccTreeItem.addChild(new EccTreeItem(eccTreeItem, getVirtualItemId(UserRightId.DoReportlistEdit),"编辑报告",UserRightId.DoReportlistEdit));
				eccTreeItem.addChild(new EccTreeItem(eccTreeItem, getVirtualItemId(UserRightId.DoReportlistDel),"删除报告",UserRightId.DoReportlistDel));
				addedSpeciaNum++;
			}
		}
		if(addedSpeciaNum==2)
			return;
		
		EccTreeItem[] items=new EccTreeItem[eccTreeItem.getChildRen().size()];
		eccTreeItem.getChildRen().toArray(items);
		for(EccTreeItem item:items)
			addSpecialNode(item);*/
	}
}
