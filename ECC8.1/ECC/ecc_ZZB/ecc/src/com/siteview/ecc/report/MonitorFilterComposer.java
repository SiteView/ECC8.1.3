package com.siteview.ecc.report;

import java.text.Collator;
import java.text.RuleBasedCollator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.siteview.base.manage.View;
import com.siteview.base.template.MonitorTemplate;
import com.siteview.base.tree.INode;
import com.siteview.base.treeInfo.MonitorInfo;
import com.siteview.ecc.report.beans.MonitorBean;
import com.siteview.ecc.report.common.ChartUtil;
import com.siteview.ecc.report.models.MonitorModel;
import com.siteview.ecc.util.Toolkit;

public class MonitorFilterComposer  extends GenericForwardComposer{
	public static final Pattern  p = Pattern.compile("(\\d+(\\.\\d+)?)|(\\d+)");
	
	private Window  monitorFilter;
	
	private Listbox nameListbox;
	
	private Textbox nameTextbox;
	
	private Listbox groupListbox;
	
	private Listbox groupConditionListbox;
	
	private Listbox groupData;
	
	private Listbox typeListbox;
	
	private Listbox typeConditionListbox;
	
	private Listbox typeData;
	
	private Listbox frequencyListbox;
	
	private Listbox frequencyListboxData;
	
	private Listbox keybox;
	
	private Textbox keyvalue;
		
	public void onCreate$monitorFilter(Event event) throws Exception{
		try {
			init();
		} catch (Exception e) {
			Messagebox.show("初始化窗口时出错，请重试！","错误",Messagebox.OK,Messagebox.ERROR);
			e.printStackTrace();
		}
	}
	
	private void init() throws Exception{
		MonitorImfoListbox listbox = (MonitorImfoListbox)monitorFilter.getAttribute("listbox");
		Set<String> freqCollection = new TreeSet<String>();
		for(MonitorBean key : listbox.getMonitors()) {
			freqCollection.add(key.getFrequency());
		}
		if(frequencyListboxData.getItems()!=null && frequencyListboxData.getItems().size()>0) frequencyListboxData.getItems().clear();
		Listitem blankItem = new Listitem();
		blankItem.setSelected(true);
		blankItem.appendChild(new Listcell("监测频率"));
		frequencyListboxData.appendChild(blankItem);
		Object[] obj = freqCollection.toArray();
		Arrays.sort(obj, new Comparator<Object>(){
			public int compare(Object o1, Object o2) {
				String s1 = o1.toString();
				String s2 = o2.toString();
				if(s1==null || s1.equals("")) return -1;
				double d1 = 0;
				double d2 = 0;
				Matcher m1 = p.matcher(s1);
				Matcher m2 = p.matcher(s2);
				if(m1.find()){
					String tmp = m1.group().trim();
					if(tmp!=null && !tmp.equals(""))
					d1 = Double.parseDouble(tmp);
				}
				if(m2.find()){
					String tmp = m2.group().trim();
					if(tmp!=null && !tmp.equals(""))
					d2 = Double.parseDouble(tmp);
				}
				if(d1>d2) return 1;
				else if(d2>d1) return -1;
				else return 0;
			}
			
		});
		for(Object freq : obj){
			Listitem item = new Listitem();
			if(freq!=null && !freq.equals("")){
				item.appendChild(new Listcell(freq.toString()));
				frequencyListboxData.appendChild(item);
			}
			
		}
		initillize();
	}
	
	public void onClick$ok(Event event) throws Exception{
		//monitor筛选条件
		String monitorName = nameTextbox.getValue();
		String monitorCondition = nameListbox.getSelectedItem()==null?"like":nameListbox.getSelectedItem().getLabel().trim();
		//group筛选条件
		List<String> goupName = getSelectGroupName();
		String groupconditioin = groupListbox.getSelectedItem()==null?"like":groupListbox.getSelectedItem().getLabel().trim();
		String groupLogic = groupConditionListbox.getSelectedItem()==null?"or":groupConditionListbox.getSelectedItem().getLabel().trim();
		//type筛选条件
		List<String> typeName = getSelectTypeName();
		String typeCondition = typeListbox.getSelectedItem()==null?"like":typeListbox.getSelectedItem().getLabel().trim();
		String typeLogic = typeConditionListbox.getSelectedItem()==null?"or":typeConditionListbox.getSelectedItem().getLabel().trim();
		//监测平率筛选条件
		String freOper = frequencyListbox.getSelectedItem()==null?"like":frequencyListbox.getSelectedItem().getLabel().trim();
		String freq = frequencyListboxData.getSelectedItem()==null?"or":frequencyListboxData.getSelectedItem().getLabel().trim();
		//阀值
		String keyValue = keyvalue.getValue();
		String  keyValueOper= keybox.getSelectedItem()==null?"like":keybox.getSelectedItem().getLabel().trim();
		MonitorFilterCondition filter = new MonitorFilterCondition(monitorName,monitorCondition,groupLogic
				,groupconditioin,goupName,typeName,typeLogic,typeCondition,freOper,freq,keyValue,keyValueOper);		
		MonitorImfoListbox listbox = (MonitorImfoListbox)monitorFilter.getAttribute("listbox");
	  	ChartUtil.clearListbox(listbox);
	  	MonitorFilterCondition condition = new MonitorFilterCondition();
	  	condition.setMonitorName(monitorName);
	  	condition.setMonitorCondition(monitorCondition);
	  	condition.setGroupLogic(groupLogic);
	  	condition.setGroupCondition(groupconditioin);
	  	condition.setGroupName(goupName);
	  	condition.setTypeName(typeName);
	  	condition.setTypeLogic(typeLogic);
	  	condition.setTypeCondition(typeCondition);
	  	condition.setFreq(freq);
	  	condition.setOper(freOper);
	  	condition.setKeyValue(keyValue);
	  	condition.setKeyValueOper(keyValueOper);
	  	listbox.setCondition(condition);
	  	listbox.onCreate();
//	  	MonitorModel model = new MonitorModel(filter);
//	  	List<MonitorBean> beans = model.getMonitorInfoByCondition();
//	  	for(MonitorBean bean : beans){
//	  		ChartUtil.addRow(listbox, bean, bean.getMonitorName(),bean.getGroupName(),bean.getMonitorType()
//	  				,bean.getFrequency(),bean.getKeyValue(),bean.getLatestUpdate());
//	  	}
	  	monitorFilter.detach();
	}
	
	public void onClick$cancel(Event event){
		monitorFilter.detach();
	}
	
	/**
	 *获得所有选择了的组名称
	 * @return
	 */
	private List<String> getSelectGroupName(){
		Set l = groupData.getSelectedItems();
		if(l==null || l.size()==0)
			return null;
		List<String> returnList = new ArrayList<String>();
		Iterator it = l.iterator();
		for(;it.hasNext();){
			String label = ((Listitem)it.next()).getLabel();
			returnList.add(label);
		}
		return returnList;
	}
	/**
	 * 获得所有被选中了类型名称
	 * @return
	 */
	private List<String> getSelectTypeName(){
		Set l = typeData.getSelectedItems();
		if(l==null || l.size()==0)
			return null;
		List<String> returnList = new ArrayList<String>();
		Iterator it = l.iterator();
		for(;it.hasNext();){
			String label = ((Listitem)it.next()).getLabel();
			returnList.add(label);
		}
		return returnList;
	}
	private static final RuleBasedCollator collator = (RuleBasedCollator)Collator.getInstance(Locale.CHINA);
	
	private void initillize() throws Exception{

		Set<String> monitorList = new TreeSet<String>();
		Set<String> groupList = new TreeSet<String>();
		for(MonitorBean mb : new MonitorModel().getAllMonitorInfo()){
			INode node = view.getNode(mb.getId());
			MonitorInfo info = view.getMonitorInfo(node);
			MonitorTemplate tmplate = info.getMonitorTemplate();
			monitorList.add(tmplate.get_sv_name());
			groupList.add(mb.getGroupName());
		}
		for(String group : groupList){
			if(group.isEmpty())
				continue;
			Listitem item = new Listitem();
			new Listcell(group).setParent(item);
			item.setParent(groupData);
		}
		for(String group : monitorList){
			Listitem item = new Listitem();
			new Listcell(group).setParent(item);
			item.setParent(typeData);
		}		
	}
	private View view = Toolkit.getToolkit().getSvdbView(Executions.getCurrent().getDesktop());
}
