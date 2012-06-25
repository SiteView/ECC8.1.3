package com.siteview.ecc.report.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Tree;

import com.siteview.base.manage.View;
import com.siteview.base.tree.INode;
import com.siteview.base.treeInfo.MonitorInfo;
import com.siteview.ecc.report.MonitorFilterCondition;
import com.siteview.ecc.report.beans.MonitorBean;
import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.treeview.EccTreeModel;
import com.siteview.ecc.util.Toolkit;
import com.siteview.svdb.UnivData;

public class MonitorModel{
	private final static Logger logger = Logger.getLogger(MonitorModel.class);
	private MonitorFilterCondition 						condition;
	private static List<MonitorBean>					monitorList = new ArrayList<MonitorBean>();
	private View										view = Toolkit.getToolkit().getSvdbView(Executions.getCurrent().getDesktop());
	public MonitorModel()
	{
	}
	public MonitorModel(MonitorFilterCondition condition)
	{
		this.condition = condition;
	}
	
	/**
	 * 根据传入的数据生成列表
	 * @throws Exception 
	 */
	public List<MonitorBean> getMonitorInfoByCondition() throws Exception
	{
		if(condition==null) return getAllMonitorInfo();
		List<MonitorBean> beans = new ArrayList<MonitorBean>();
		for(MonitorBean bean : getAllMonitorInfo())
		{
			 if(condition.monitorOperator(bean.getMonitorName())
					 && condition.groupOperator(bean.getGroupName())
					 && condition.typeOperator(bean.getMonitorType())
					 && condition.freqOperator(bean.getFrequency())
					 && condition.frequencyOperator(bean.getKeyValue()))
			 {
				 beans.add(bean);
			 }
		}
		monitorList.addAll(beans);
		return beans;
	}
	
	private String getGroupItem(EccTreeItem monitorItem){
		if(monitorItem == null) return null;
		if(monitorItem.getValue()!=null && monitorItem.getValue().getType().equals(INode.GROUP)) {
			String groupName = monitorItem.getValue().getName();
			return groupName;
		}else{
			EccTreeItem parent = monitorItem.getParent();
			return getGroupItem(parent);
		}
	}
	public void init(List<MonitorBean> monitors ,EccTreeItem item,Map<String,Map<String,String>> map){
		List<EccTreeItem> items = item.getChildRen();
		
		for(EccTreeItem itm : items){
			INode node = itm.getValue();
			if(node!=null && node.getType().equals(INode.MONITOR)){
				Map<String,String> mapValue = map.get(node.getSvId());
				if(mapValue==null) continue;
				MonitorInfo info = view.getMonitorInfo(node);
				INode entityNode = view.getNode(node.getParentSvId());
				MonitorBean bean = new MonitorBean(node.getSvId(),node.getName()
						,this.getGroupItem(itm)==null?"":getGroupItem(itm)
						,info.getMonitorTemplate()!=null?info.getMonitorTemplate().get_sv_name():""
						,mapValue.get("MonitorFrequency")
						,mapValue.get("OkConditon")+mapValue.get("WarnConditon")
						+mapValue.get("ErrorConditon"),info.getCreateTime(),entityNode==null?"":entityNode.getName(),info.getStatus());
				monitors.add(bean);
			}else if(itm.getChildRen()!=null && itm.getChildRen().size()>0){
				init(monitors,itm,map);
			}
		}
	}
	

	public  List<MonitorBean> getAllMonitorInfo() throws Exception
	{	
		Tree tree = (org.zkoss.zul.Tree) Executions.getCurrent().getDesktop().getPage("eccmain").getFellow("tree");
		EccTreeModel model = (EccTreeModel)tree.getModel();
		EccTreeItem root = model.getRoot();
		if (root == null)
			return null;
		
		//整体视图节点
		for (EccTreeItem item : root.getChildRen()){
			root = item;
			break;
		}
		Map<String,Map<String,String>> map = UnivData.queryAllMonitorInfo();
		List<MonitorBean> beans = new ArrayList<MonitorBean>();
		long start = System.currentTimeMillis();
		for(EccTreeItem itm : root.getChildRen()){
			root = itm;
			if(root.getValue()==null) continue;
			init(beans,root,map);
		}
		long end = System.currentTimeMillis();
		logger.info((end-start)/1000+"秒");		
		return beans;
	}
}
	
