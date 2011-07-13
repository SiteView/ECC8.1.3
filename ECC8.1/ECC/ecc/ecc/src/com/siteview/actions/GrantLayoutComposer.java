package com.siteview.actions;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.ComposerExt;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkex.zul.East;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Treeitem;

import com.siteview.base.tree.INode;
import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.treeview.EccWebAppInit;
import com.siteview.ecc.treeview.controls.BaseTreeitem;
import com.siteview.ecc.util.Message;
import com.siteview.ecc.util.Toolkit;

public class GrantLayoutComposer extends GenericForwardComposer implements
		ComposerExt{


	private GrantRightWindow grantWindow;
	private Div actionDiv;
	private Label curNodeTitle;
	private Image curNodeImg;
	private Checkbox checkAll;
	private Checkbox checkLeftAll;
	
	private GrantRightTree grantTree;
	private Checkbox applyToChildren;
	private EccTreeItem curEccTreeItem;
	private LinkedHashMap<String, GrantActionPanel> panelList = new LinkedHashMap<String, GrantActionPanel>();
	private Button applyActionGrant;
	private Button applyVisibleGrant;
	private Listbox userSelect;
	private East eastActionChosePanel;
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		grantTree.addEventListener("onSelect", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				SelectEvent e = (SelectEvent) event;
				if (!e.getSelectedItems().isEmpty()) {
					curEccTreeItem = (EccTreeItem) (((Treeitem) (e
							.getSelectedItems().iterator().next())).getValue());
					refreshActionCheck();
						
				}
			}
		});

		applyToChildren.addEventListener("onCheck", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				refreshActionCheck();
			}
		});

		checkAll.addEventListener("onCheck", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {

				for (GrantActionPanel panel : panelList.values())
					panel.setCheckedAll(checkAll.isChecked());

			}
		});
		checkLeftAll.addEventListener("onCheck", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				grantTree.getLeftAllCheck(checkLeftAll.isChecked());
			}
		});
		applyActionGrant.addEventListener("onClick", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if(grantTree.getUserRight().isAdmin())
				{
					Message.showInfo("����Ա������Ȩ������ӵ��ȫ������!");
					return;
				}
				try
				{
					commitActionGrant();
					Message.showInfo("�ɹ�!");
				}catch(Exception e)
				{
					Toolkit.getToolkit().showError(e.getMessage());
				}
			}
		});

		applyVisibleGrant.addEventListener("onClick", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if(grantTree.getUserRight().isAdmin())
				{
					Message.showInfo("����Ա������Ȩ������ӵ��ȫ������!");
					return;
				}
				try
				{
					commitVisibleGrant();
					Message.showInfo("�ɹ�!");
				}catch(Exception e)
				{
					Toolkit.getToolkit().showError(e.getMessage());
				}
				
			}
		});
		
	}

	private void refreshActionCheck() {
		curNodeTitle.setValue(curEccTreeItem.getTitle());
		if (curEccTreeItem.getType().equals(INode.GROUP)
				|| curEccTreeItem.getType().equals(INode.ENTITY)
				|| curEccTreeItem.getType().equals(INode.MONITOR))
			curNodeImg.setSrc(EccWebAppInit.getInstance().getImage(
					curEccTreeItem.getType(), curEccTreeItem.getStatus()));
		else
			curNodeImg.setSrc(EccWebAppInit.getInstance().getImage(
					curEccTreeItem.getType()));

		while (actionDiv.getLastChild() != null)
			actionDiv.removeChild(actionDiv.getLastChild());

		panelList.clear();

		if (dispPanel(INode.SE)){
			panelList.put(INode.SE, makePanel("SE����Ȩ��", INode.SE));
		}
		if (dispPanel(INode.GROUP)){
			panelList.put(INode.GROUP, makePanel("�����Ȩ��", INode.GROUP));
		}
		if (dispPanel(INode.ENTITY)){
			panelList.put(INode.ENTITY, makePanel("�豸����Ȩ��", INode.ENTITY));
		}
		if (dispPanel("monitor")){
			panelList.put(INode.MONITOR, makePanel("���������Ȩ��", INode.MONITOR));
		}
/*		if (dispPanel_alert("Alert")){
			panelList.put("alert", makePanel("��������Ȩ��", "alert"));
		}*/
/*		if (dispPanel_report("Report")){
			panelList.put("report", makePanel("�������Ȩ��", "report"));
		}*/
		if (dispPanel_alertRule("AlertRule")){
			panelList.put("alertRule", makePanel("�����������Ȩ��", "alertRule"));
		}
		if (dispPanel_alertStrategy("AlertStrategy")){
			panelList.put("alertStrategy", makePanel("�������Բ���Ȩ��", "alertStrategy"));
		}
		if (dispPanel_statisticReport("ReportStatistic")){
			panelList.put("reportStatistic", makePanel("ͳ�Ʊ������Ȩ��", "reportStatistic"));
		}
		if (dispPanel_topNReport("ReportTopN")){
			panelList.put("reportTopN", makePanel("TopN�������Ȩ��", "reportTopN"));
		}
	}



	private GrantActionPanel makePanel(String title, String target) {
		GrantActionPanel panel = new GrantActionPanel(target, title);
		panel.setParent(actionDiv);
		panel.refresh(grantTree.getUserRight(), curEccTreeItem);//��ʼ����ʱ�� checkbox ѡ��
		return panel;

	}

	private boolean dispPanel(String actionTarget) {
		if (curEccTreeItem == null){
			return false;
		}
		if (!applyToChildren.isChecked()){
			if (!curEccTreeItem.getType().equals(actionTarget)){
				return false;
			}
		}
		if (curEccTreeItem.getType().equals("WholeView")) {
			return false;
		} else if (curEccTreeItem.getType().equals(INode.SE)) {
			return true;
		} else if (curEccTreeItem.getType().equals(INode.GROUP)) {
			if (actionTarget.equals("se")){
				return false;
			}
			return true;
		} else if (curEccTreeItem.getType().equals(INode.ENTITY)) {
			if (actionTarget.equals("se") || actionTarget.equals("group")){
				return false;
			}
			return true;
		}
		return false;
	}
/*	//��ʾ���� �Ҽ��˵�
	private boolean dispPanel_alert(String actionTarget) {
		if("Alert".equals(curEccTreeItem.getType())){
			if(actionTarget.equals("Alert")){
				return true;
			}
		}
		return false;
	}*/
	//��ʾ�������� �Ҽ��˵�
	private boolean dispPanel_alertRule(String actionTarget) {
		if("AlertRule".equals(curEccTreeItem.getType())){
			if(actionTarget.equals("AlertRule")){
				return true;
			}
		}
		return false;
	}
	//��ʾ�������� �Ҽ��˵�
	private boolean dispPanel_alertStrategy(String actionTarget) {
		if("AlertStrategy".equals(curEccTreeItem.getType())){
			if(actionTarget.equals("AlertStrategy")){
				return true;
			}
		}
		return false;
	}
	//��ʾ����  �Ҽ��˵�
	private boolean dispPanel_report(String actionTarget) {
		if("Report".equals(curEccTreeItem.getType())){
			if(actionTarget.equals("Report")){
				return true;
			}
		}
		return false;
	}
	//��ʾͳ�Ʊ���  �Ҽ��˵�
	private boolean dispPanel_statisticReport(String actionTarget) {
		if("ReportStatistic".equals(curEccTreeItem.getType())){
			if(actionTarget.equals("ReportStatistic")){
				return true;
			}
		}
		return false;
	}
	//��ʾTopN����  �Ҽ��˵�
	private boolean dispPanel_topNReport(String actionTarget) {
		if("ReportTopN".equals(curEccTreeItem.getType())){
			if(actionTarget.equals("ReportTopN")){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent,
			ComponentInfo compInfo) {
		return compInfo;
	}

	@Override
	public void doBeforeComposeChildren(Component comp) throws Exception {

	}

	@Override
	public boolean doCatch(Throwable ex) throws Exception {
		return false;
	}

	@Override
	public void doFinally() throws Exception {
	}

	/* �ύ���ӽڵ����� */
	private void commitVisibleGrant() throws Exception {
		
			HashMap<String,String> groupRightMap=new HashMap<String,String>();
			for (Object item : grantTree.getTreechildren().getItems())
				commitVisibleGrantChildren((BaseTreeitem) item, groupRightMap);

			StringBuffer groupRight=new StringBuffer();
			StringBuffer unGroupRight=new StringBuffer();
			for(Object svid:groupRightMap.keySet())
			{
				if(groupRightMap.get(svid).toString().equals("1"))
				{
					if(groupRight.length()>0)
						groupRight.append(",");
					groupRight.append(svid);
				}
				else
				{
					if(unGroupRight.length()>0)
						unGroupRight.append(",");
					unGroupRight.append(svid);
				}
			}
			grantTree.getUserRight().writeTreeNodeVisible(groupRight.toString(), unGroupRight.toString());
			grantTree.getUserRight().saveChange();
		
	}	

	/*�����߼�,ֻ���������
	 * 1.�޸���,�����ڵ�ɼ�,дgroupright
	 * 2.���ڵ㲻��monitor�ڵ�,дgroupright
	 * 3.����δѡ��,����ѡ��,дgroupright
	 * */
	private boolean isGroupRight(BaseTreeitem treeitem)
	{
		if(treeitem.getParentItem()==null||treeitem.getParentItem().getValue()==null)
			return treeitem.isChecked();
		else if(!((EccTreeItem)treeitem.getParentItem().getValue()).isMonitorTreeNode())
			return treeitem.isChecked();
		else if(!treeitem.getParentItem().isChecked())
			return treeitem.isChecked();
		return false;	
		
	}
	/*�����߼�,ֻ���������
	 * 1.�޸���,�����ڵ㲻�ɼ�,��дungroupright
	 * 2.���ڵ㲻��monitor�ڵ�,,��дungroupright
	 * 3.����ѡ��,���Ӳ�ѡ��,��дungroupright
	 * */
	private boolean isUnGroupRight(BaseTreeitem treeitem)
	{
		if(treeitem.getParentItem()==null||treeitem.getParentItem().getValue()==null)
			return !treeitem.isChecked();
		else if(!((EccTreeItem)treeitem.getParentItem().getValue()).isMonitorTreeNode())
			return !treeitem.isChecked();
		else if(treeitem.getParentItem().isChecked())
			return !treeitem.isChecked();
		return false;	
		
	}
	/*Ϊ��������,���ü̳л���,���Ϸ�ת�����Ĳ�д��Ȩ����*/
	private void commitVisibleGrantChildren(BaseTreeitem treeitem,
			HashMap<String,String> groupRightMap)
			throws Exception {
		EccTreeItem eccTreeItem = ((EccTreeItem) treeitem.getValue());
		if (eccTreeItem.isMonitorTreeNode()) 
		{
			if (isGroupRight(treeitem))
				groupRightMap.put(eccTreeItem.getId(),"1");
			else if(isUnGroupRight(treeitem))
				groupRightMap.put(eccTreeItem.getId(),"0");
		} else 
		{
			EccAction eccAction=EccActionManager.getInstance().getAction(eccTreeItem.getType());
			if(eccAction!=null)
			{
				String license=eccAction.getLicense();
				if(license!=null&&!license.isEmpty())
				{
					if (treeitem.isChecked())
						grantTree.getUserRight().writeTreeMenuVisible(
								license, true);
					else
						grantTree.getUserRight().writeTreeMenuVisible(
								license, false);
				}
			}
		}
		if (treeitem.getTreechildren() != null)
			for (Object item : treeitem.getTreechildren().getItems())
				commitVisibleGrantChildren((BaseTreeitem) item, groupRightMap);

	}

	/* �ύ�Ҽ��˵����� */
	private void commitActionGrant() throws Exception {
		/*if (!applyToChildren.isChecked())
			commitActionGrantOnlySelf(curEccTreeItem);ע�͵�,���Ǽ̳е������Ҽ��˵�Ȩ��
		else
		*/

		if(grantTree.getSelectedItem().getValue() instanceof EccTreeItem)
		{
			if(((EccTreeItem)grantTree.getSelectedItem().getValue()).isMonitorTreeNode())
			{
				EccTreeItem selectedEccTreeItem = (EccTreeItem)grantTree.getSelectedItem().getValue();
				
				//���û�Ȩ�޵��µ��߼����޸�
				if(applyToChildren.isChecked())
				{
					commitActionGrant_special(selectedEccTreeItem);
					grantTree.getUserRight().saveChange();

				}else{
					commitActionGrant((EccTreeItem)grantTree.getSelectedItem().getValue());
					grantTree.getUserRight().saveChange();
				}
			}
			if("Alert".equals(((EccTreeItem)grantTree.getSelectedItem().getValue()).getType())
					|| "ReportStatistic".equals(((EccTreeItem)grantTree.getSelectedItem().getValue()).getType())
					|| "ReportTopN".equals(((EccTreeItem)grantTree.getSelectedItem().getValue()).getType())){
				
				for (GrantActionPanel panel : panelList.values()) 
				{
					HashMap map =panel.getCheckedMap();
					for(Object key:map.keySet()){
						String value = (String)map.get(key);
						if("1".equals(value)){
							grantTree.getUserRight().writeTreeMenuVisible(key.toString(), true);
						}else{
							grantTree.getUserRight().writeTreeMenuVisible(key.toString(), false);
						}
					}
				}
				grantTree.getUserRight().saveChange();
			}
		}
	}

	private void commitActionGrant(EccTreeItem eccTreeItem) throws Exception
	{
		StringBuffer sb=new StringBuffer();
		
		for (GrantActionPanel panel : panelList.values()) 
		{
			String str=panel.getCheckedStr();
			if(sb.length()>0&&str.length()>0)
				sb.append(",");
			sb.append(str);

		}
		grantTree.getUserRight().writePopupMenuRight(eccTreeItem.getId(),sb.toString() );
	}
	
	//������ѡ�еĽڵ������  д��������͵Ĳ���
	private void commitActionGrant_special(EccTreeItem eccTreeItem) 
	{
		try{
			if(eccTreeItem == null) return;

			StringBuffer sb=new StringBuffer();
			
			for(EccTreeItem obj : eccTreeItem.getChildRen())
			{
				commitActionGrant_special(obj);
			}
			if(INode.SE.equals(eccTreeItem.getType())){
				for (GrantActionPanel panel : panelList.values()) {			
					String str=panel.getCheckedStr();
					if(sb.length()>0&&str.length()>0)
						sb.append(",");
					sb.append(str);
				}
			}else if(INode.GROUP.equals(eccTreeItem.getType())){
				for (GrantActionPanel panel : panelList.values()) {		
					if(INode.SE.equals(panel.getActionTarget())) continue;
					String str=panel.getCheckedStr();
					if(sb.length()>0&&str.length()>0)
						sb.append(",");
					sb.append(str);
				}
			}else if(INode.ENTITY.equals(eccTreeItem.getType())){
				for (GrantActionPanel panel : panelList.values()) {		
					if(INode.SE.equals(panel.getActionTarget())) continue;
					if(INode.GROUP.equals(panel.getActionTarget())) continue;
					String str=panel.getCheckedStr();
					if(sb.length()>0&&str.length()>0)
						sb.append(",");
					sb.append(str);
				}
			}else if(INode.MONITOR.equals(eccTreeItem.getType())){
				for (GrantActionPanel panel : panelList.values()) {		
					if(INode.SE.equals(panel.getActionTarget())) continue;
					if(INode.GROUP.equals(panel.getActionTarget())) continue;
					if(INode.ENTITY.equals(panel.getActionTarget())) continue;
					String str=panel.getCheckedStr();
					if(sb.length()>0&&str.length()>0)
						sb.append(",");
					sb.append(str);
				}
			}
			grantTree.getUserRight().writePopupMenuRight(eccTreeItem.getId(),sb.toString() );
		}catch(Exception e){
			e.printStackTrace();
		}

	}
}
