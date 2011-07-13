package com.siteview.ecc.alert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;
import org.zkoss.zul.api.Textbox;

import com.siteview.base.data.IniFile;
import com.siteview.base.manage.View;
import com.siteview.base.template.EntityTemplate;
import com.siteview.base.template.MonitorTemplate;
import com.siteview.base.template.TemplateManager;
import com.siteview.base.tree.INode;
import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.alert.util.DictionaryFactory;
import com.siteview.ecc.monitorbrower.EntitySelectTree;
import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.util.Message;
import com.siteview.ecc.util.Toolkit;
import com.siteview.svdb.UnivData;

public class StrategyView extends Window {

	private static final long serialVersionUID = 8714368973294319836L;
	
	private boolean isEdit = false;
	
	private String selectItem = "";

	public void onCreate() {
		try {
			selectItem = (String) getAttribute("selectedItem");			
			Map<String, String> valueMap = new FastMap<String,String>();
			if (selectItem != null) {
				isEdit = true;
				Map<String, Map<String, String>> map = DictionaryFactory.getAlertPloy().getFmap();
				valueMap = map.get(selectItem);
			}
			getStrategyNameLabel().setValue(selectItem);
			getTree().addEventListener(Events.ON_SELECT, new TreeSelectedListener(valueMap));
		} catch (Exception e) {
			Message.showError(e.getMessage());
		}
	}

	public void onSave() throws InterruptedException {
		try {
			//策略名
			String strategyname = getStrategyNameLabel().getValue();
			if (strategyname == null || "".equals(strategyname))
				throw new Exception("请输入告警策略的名称！");
			
			//应用到那些结点
			Treeitem item = getTree().getSelectedItem();
			if (item == null)
				throw new Exception("请选择要应用策略的节点！");
			EccTreeItem eccItem = (EccTreeItem) item.getValue();
			List<String> entitys = new FastList<String>();
			findEntity(entitys, eccItem);
			
			//要应用的监测器类型
			Set<Listitem> typeList = getTypelist().getSelectedItems();
			if (typeList == null || typeList.size() == 0) 
				throw new Exception("请选择要应用策略的监测器类型！");
			StringBuilder sb = new StringBuilder();
			String typeStr = "";
			for (Listitem temp : typeList) {
				sb.append(temp.getValue()).append(",");
			}
			int index = sb.lastIndexOf(",");
			if (index >= 0) typeStr = sb.substring(0, index);
		
		
			IniFile iniFile = DictionaryFactory.getAlertPloy();
			if (isEdit) {
				iniFile.deleteSection(selectItem);
				iniFile.createSection(strategyname);
				try {iniFile.saveChange();} catch (Exception e) {e.printStackTrace();};
				for (String en : entitys) {
					iniFile.setKeyValue(strategyname, en, typeStr);
				}
			} else {
				iniFile.createSection(strategyname);
				try {iniFile.saveChange();} catch (Exception e) {e.printStackTrace();};
				for (String en : entitys) {
					iniFile.setKeyValue(strategyname, en, typeStr);
				}
			}
			iniFile.saveChange();
			this.detach();
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), "提示", Messagebox.OK, Messagebox.INFORMATION);
		}
	}
	
	public Textbox getStrategyNameLabel() {
		return (Textbox) BaseTools.getComponentById(this, "strategyName");
	}
	public EntitySelectTree getTree() {
		return (EntitySelectTree) BaseTools.getComponentById(this, "tree");
	}
	
	public Listbox getTypelist() {
		return (Listbox) BaseTools.getComponentById(this, "typelist");
	}
	
	public Button getOkButton() {
		return (Button) BaseTools.getComponentById(this, "button_ok");
	}
	
	public View getView() {
		return Toolkit.getToolkit().getSvdbView(this.getDesktop());
	}
	
	private void initTypelist(EccTreeItem eccTreeItem, List<String> selectedType) throws Exception {
		BaseTools.clear(getTypelist());
		List<TemplateItem> typeMap = findTypelist(eccTreeItem);
		for (TemplateItem key : typeMap) {
			Listitem item = BaseTools.addRow(getTypelist(), key.templateId+"", key.templateName);
			if (selectedType != null && selectedType.contains(key.templateId)) 
				item.setSelected(true);
		}
	}
	
	public void findEntity(List<String> entitys, EccTreeItem eccTreeItem) {
		if (eccTreeItem == null) return;
		if (eccTreeItem.getType() == INode.ENTITY) {
			entitys.add(eccTreeItem.getId());
			return;
		} else {
			for (EccTreeItem key : eccTreeItem.getChildRen()) {
				entitys.add(eccTreeItem.getId());
				findEntity(entitys,key);
			}
		}
	}
	
	public List<TemplateItem> findTypelist(EccTreeItem eccTreeItem) throws Exception {
		List<TemplateItem> templateItem = new ArrayList<TemplateItem>();
		if (eccTreeItem == null) return templateItem;
		if (eccTreeItem.getType() == INode.ENTITY) {
			INode node = eccTreeItem.getValue();      
			EntityTemplate templ = getView().getEntityInfo(node).getDeviceTemplate();
			for (String key : templ.getSubMonitorTemplateId()) {
				MonitorTemplate mt = TemplateManager.getMonitorTemplate(key);
				templateItem.add(new TemplateItem(key, mt.get_sv_name()));
			}
		} else if (eccTreeItem.getType() == INode.GROUP || eccTreeItem.getType() == INode.SE) {
			Map<String, Map<String, String>> templInfo = UnivData.getAllMonitorTempletInfo();
			templInfo.remove("return");
			templInfo.remove("monitors");
			for (String key : templInfo.keySet()) {
				templateItem.add(new TemplateItem(key, templInfo.get(key).get("sv_name")));
			}
		}
		Collections.sort(templateItem, new Comparator<TemplateItem>(){
			@Override
			public int compare(TemplateItem o1, TemplateItem o2) {
				return o1.templateName.compareTo(o2.templateName);
			}});
		return templateItem;
	}
	
	class TreeSelectedListener implements EventListener {
		private Map<String, String> valueMap;
		
		public TreeSelectedListener(Map<String, String> valueMap) {
			this.valueMap = valueMap;
		}
		@Override
		public void onEvent(Event event) throws Exception {
			try {
				Treeitem item = getTree().getSelectedItem();
				if (item == null) return;
				EccTreeItem eccItem = (EccTreeItem) item.getValue();
				List<String> typeList = new FastList<String>();
				String itemId = eccItem.getId();
				String typeStr = valueMap.get(itemId);
				if (typeStr != null) 
					typeList = Arrays.asList(typeStr.split(","));
				initTypelist((EccTreeItem) item.getValue(),typeList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	static class TemplateItem  {
		String templateId;
		String templateName;
		public TemplateItem(String templateId, String templateName) {
			super();
			this.templateId = templateId;
			this.templateName = templateName;
		}
		
	}
}
