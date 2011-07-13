package com.siteview.ecc.monitorbrower;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.siteview.base.data.ReportDate;
import com.siteview.base.data.Report.DstrItem;
import com.siteview.base.manage.View;
import com.siteview.base.template.MonitorTemplate;
import com.siteview.base.tree.EntityNode;
import com.siteview.base.tree.GroupNode;
import com.siteview.base.tree.INode;
import com.siteview.base.tree.SeNode;
import com.siteview.base.treeInfo.MonitorInfo;
import com.siteview.ecc.alert.util.LocalIniFile;
import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.treeview.EccTreeModel;

public class MonitorDaomImpl implements IMonitorDao {
	
	private int okCount = 0;
	private int errorCount = 0;
	private int warnCount = 0;

	private int badCount = 0;//bad
	private int forbidCount = 0;//disable
	private int nullCount = 0;//null
	
	
	
	private static List<MonitorBean> monitorBean = new ArrayList<MonitorBean>();
	private static List<MonitorBean> errorMonitorBean = new ArrayList<MonitorBean>();
	private LocalIniFile iniFile = new LocalIniFile("MonitorBrowse.ini");
	private LocalIniFile monitorStaticalIni = new LocalIniFile(
			"MonitorBrowseData.ini");

	private EccTreeModel model ;
	private View		 view;
	
	public MonitorDaomImpl(EccTreeModel model,View view){
		this.model = model;
		this.view = view;
	}
	/**
	 * 浏览次数最多的
	 * 
	 * @param topN
	 *            浏览次数最多的前N个检测器
	 * */
	public List<MonitorBean> getBrowseMost(int topN) throws Exception {
		List<MonitorItem> mi = new ArrayList<MonitorItem>();
		iniFile.load();
		List<String> monitors = queryAllMonitorId();
		for (String id : iniFile.getSectionList()){
			if (monitors.contains(id)) continue;
			iniFile.deleteSection(id);
		}
		iniFile.saveChange();
		for (String monitorId : monitors) {
			if (iniFile.getSectionList().contains(monitorId)) {
				int tmpInt = 0;
				try {
					tmpInt = Integer.parseInt(iniFile.getValue(monitorId,
							"monitorBrowseCount"));
				} catch (Exception e) {
				}
				mi.add(new MonitorItem(monitorId, tmpInt));
			}
		}
		Collections.sort(mi, new Comparator<MonitorItem>() {
			public int compare(MonitorItem bean1, MonitorItem bean2) {
				return bean2.count - bean1.count;
			}
		});
		List<MonitorBean> beans = new ArrayList<MonitorBean>();
		int count = 0;
		for (MonitorItem mitem : mi) {
			if (count >= topN)
				break;
			beans.add(queryMonitorInfo(mitem.id));
			count++;
		}
		stasticSatus(beans);
		return beans;
	}

	public List<MonitorBean> getErrorMost(int topN) throws Exception {
//		if(errorMonitorBean.size()>0){
//			stasticSatus(errorMonitorBean);
//			return errorMonitorBean;
//		}
		
		List<MonitorItem> mi = new ArrayList<MonitorItem>();
		monitorStaticalIni.load();
		if (monitorStaticalIni.getSectionList() != null
				&& monitorStaticalIni.getSectionList().size() > 0) {
			List<String> monitors = queryAllMonitorId();
			for (String id : monitorStaticalIni.getSectionList()){
				if (monitors.contains(id)) continue;
				monitorStaticalIni.deleteSection(id);
			}
			monitorStaticalIni.saveChange();
			
			for (String monitorId : monitors) {
				if (monitorStaticalIni.getSectionList().contains(monitorId)) {
					int tmpInt = 0;
					try {
						tmpInt = Integer.parseInt(monitorStaticalIni.getValue(monitorId,
								"errorCount"));						
					} catch (Exception e) {
					}
					mi.add(new MonitorItem(monitorId, tmpInt));
				}
			}
		} else {
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			ReportDate rd = new ReportDate(c.getTime(), new Date());
			rd.getReportDate(queryAllMonitorAsId());
			String[] nodeIds = rd.getNodeidsArray();
			for (String nodeId : nodeIds) {
				int error = 0;
				Map<Date, DstrItem> dstr = rd.getDstr(nodeId);
				for (Date dateKey : dstr.keySet()) {
					DstrItem dstrValue = dstr.get(dateKey);
					if (dstrValue == null)
						continue;
					if (dstrValue.status.equals("error")) {
						error++;
					}
				}
				if (error >= 0)
					mi.add(new MonitorItem(nodeId, error));
			}
		}
		Collections.sort(mi, new Comparator<MonitorItem>() {
			public int compare(MonitorItem bean1, MonitorItem bean2) {
				return bean2.count -bean1.count ;
			}
		});
		List<MonitorBean> beans = new ArrayList<MonitorBean>();
		int count = 0;
		for (MonitorItem mitem : mi) {
			if (count >= topN)
				break;
			MonitorBean mb = queryMonitorInfo(mitem.id);
			beans.add(mb);
			count++;
		}
		stasticSatus(beans);
//		errorMonitorBean.addAll(beans);
		return beans;
	}

	@Override
	public List<MonitorBean> queryMonitorInfo(MonitorFilter filter)
			throws Exception {
		List<MonitorBean> beans = new ArrayList<MonitorBean>();
		for (String monitorId : queryAllMonitorId()) {
			MonitorBean mb = queryMonitorInfo(monitorId);
			if(mb == null) continue;
			if (filter.containsShowAndHidden(mb.getStatus())
					&& filter.containsMonitorType(mb.getMonitorType())
					&& filter.containsDescript(mb.getDescript())
					&& filter.containsMonitorName(mb.getMonitorName())
					&& filter.containsGroupName(mb.getGroup())
					&& filter.containsEntityName(mb.getEntity())) {
//				logger.info("find monitorId:"+monitorId);
				beans.add(mb);
			}
		}
		stasticSatus(beans);
		return beans;
	}

	@Override
	public MonitorBean queryMonitorInfo(String monitorId) throws Exception {
		try{
			INode node = this.getView().getNode(monitorId);
			if (node == null)
				return null;
			MonitorInfo info = this.getView().getMonitorInfo(node);
			MonitorTemplate template = info.getMonitorTemplate();
			if(template == null)return null;
			String monitorType = template.get_sv_name();
			String dstr = info.getDstr();
			String status = info.getStatus();
			String monitorName = node.getName();
			String createTime = info.getCreateTime();
			int index = monitorId.lastIndexOf(".");
			String entityId = monitorId.substring(0, index).trim();
			String groupId = entityId.substring(0, entityId.lastIndexOf(".")).trim();
			String entityName = findEntityNameById(entityId);
			int edi = entityName.indexOf(":");
			if (edi > 0)
				entityName = entityName.substring(edi + 1);
			MonitorBean bean = new MonitorBean(monitorId, status,
					findSeNameById(groupId)+findGroupNameById(groupId), entityName,
					monitorName, createTime, dstr, monitorType);
//			MonitorBean bean = new MonitorBean(monitorId, status,
//			"SiteView ECC 8.1.3" + findGroupNameById(groupId), entityName,
//			monitorName, createTime, dstr, monitorType);
			return bean;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}


		
	}

	private void stasticSatus(List<MonitorBean> mb){
//		for(MonitorBean m : mb){
//			if(m == null) continue;
//			String status = m.getStatus();
//			if (status == null || INode.NULL.equals(status) || "".equals(status)) {
//				badCount++;
//			}
//			if (status.equals(INode.BAD) || status.equals(INode.ERROR))
//				errorCount++;
//			if (status.equals(INode.OK))
//				okCount++;
//			if (status.equals(INode.WARNING))
//				warnCount++;
//			if (status.equals(INode.DISABLE))
//				forbidCount++;
//		}
		for(MonitorBean m : mb){
			if(m == null) continue;
			String status = m.getStatus();
			if (status == null || INode.NULL.equals(status) || "".equals(status)) {
				nullCount++;
			}
			if (status.equals(INode.ERROR))
				errorCount++;
			if (status.equals(INode.BAD))
				badCount++;
			if (status.equals(INode.OK))
				okCount++;
			if (status.equals(INode.WARNING))
				warnCount++;
			if (status.equals(INode.DISABLE))
				forbidCount++;
		}
		monitorBean.clear();
		monitorBean.addAll(mb);
	}
	public String findEntityNameById(String entityId) {
		EntityNode node = this.getView().getEntityNode(entityId);
		return node != null ? node.getName() : "";
	}
	public String findGroupNameById(String groupId) {
		GroupNode gn = this.getView().getGroupNode(groupId);
		if (gn == null)
			return "";
		else {
			String gName = gn.getName();
			int index = groupId.lastIndexOf(".");
			return findGroupNameById(groupId.substring(0, index)) + "/" + gName;
		}
	}
	
	public String findSeNameById(String groupId){
		GroupNode gn = this.getView().getGroupNode(groupId);
		if (gn == null){
			SeNode sn = this.getView().getSeNode(groupId);
			return sn.getName();
		}else {
			int index = groupId.indexOf(".");
			return findSeNameById(groupId.substring(0, index));
		}
	}

	private String queryAllMonitorAsId() {
		StringBuilder sb = new StringBuilder();
		for (String id : queryAllMonitorId()) {
			sb.append(id).append(",");
		}
		return sb.toString();
	}

	private List<String> queryAllMonitorId() {
		EccTreeItem root = model.getRoot();
		if (root == null)
			return null;
		// 整体视图节点
		for (EccTreeItem item : root.getChildRen()) {
			root = item;
			break;
		}
		List<String> idList = new ArrayList<String>();
		for (EccTreeItem itm : root.getChildRen()) {
			root = itm;
			if (root.getValue() == null)
				continue;
			iteratorEccTreeitem(idList, root);
		}
		return idList;
	}

	private void iteratorEccTreeitem(List<String> ids, EccTreeItem eccItem) {
		if (eccItem == null)
			return;
		INode node = eccItem.getValue();
		if (node.getType().equals(INode.MONITOR)) {
			ids.add(node.getSvId());
			return;
		} else {
			List<EccTreeItem> subItems = eccItem.getChildRen();
			if (subItems == null || subItems.size() == 0)
				return;
			else {
				for (EccTreeItem item : subItems) {
					iteratorEccTreeitem(ids, item);
				}
			}
		}
	}

	public int getOkCount() {
		return okCount;
	}

	public void setOkCount(int okCount) {
		this.okCount = okCount;
	}

	public int getBadCount() {
		return badCount;
	}

	public void setBadCount(int badCount) {
		this.badCount = badCount;
	}

	public int getForbidCount() {
		return forbidCount;
	}

	public void setForbidCount(int forbidCount) {
		this.forbidCount = forbidCount;
	}

	public int getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(int errorCount) {
		this.errorCount = errorCount;
	}

	public int getWarnCount() {
		return warnCount;
	}

	public void setWarnCount(int warnCount) {
		this.warnCount = warnCount;
	}

	public int getNullCount() {
		return nullCount;
	}

	public void SetNullCount(int nullCount) {
		this.nullCount = nullCount;
	}	
	
	public List<MonitorBean> getMonitorBean() {
		return monitorBean;
	}

	public LocalIniFile getIniFile() {
		return iniFile;
	}

	public LocalIniFile getMonitorStaticalIni() {
		return monitorStaticalIni;
	}

	public View getView() {
		return view;
	}

	class MonitorItem {
		private String id = "";
		private int count = 0;

		public MonitorItem(String id, int count) {
			this.id = id;
			this.count = count;
		}
	}
}
