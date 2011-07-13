package com.siteview.ecc.setmonitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Window;

import com.siteview.base.manage.ManageSvapi;
import com.siteview.base.manage.Manager;
import com.siteview.base.manage.RetMapInMap;
import com.siteview.base.manage.View;
import com.siteview.base.template.MonitorTemplate;
import com.siteview.base.template.TemplateManager;
import com.siteview.base.tree.INode;
import com.siteview.base.treeEdit.MonitorEdit;
import com.siteview.ecc.log.AppendOperateLog;
import com.siteview.ecc.log.OpObjectId;
import com.siteview.ecc.log.OpTypeId;
import com.siteview.ecc.report.common.ChartUtil;
import com.siteview.ecc.setmonitor.models.SetMonitorModel;
import com.siteview.ecc.util.Toolkit;
import com.siteview.svdb.UnivData;

/**
 * @author yuandong流程如下
 * 从selecttree中的到选择的监测器，存入List<INode> listNode，并得到每个监测器的monitoredit；当选择monitorType时，
 * 从中筛选出类型相同的监测器传到editalertexpression页面，点击确定时最后收集所有信息
 * 提交给后台
 * 
 */
/**
 * @author yuandong
 * 
 */
public class SetBatch extends GenericForwardComposer
{
	
	private Listbox			monitor;
	private Tree			monitorTree;
	private View			v;
	private Combobox		monitorType, cbErrorFrequencyUnit, cbFrequencyUnit;
	private MonitorTemplate	monitorTemplate;
	private static String	Alert_TargetUrl	= "/main/setmonitor/editalertexpression.zul";
	private Textbox			tberror;
	private Textbox			tbdanger;
	private Textbox			tbnormal;
	private Tabbox			tabbox;
	private List<INode>		listNode		= new ArrayList<INode>();
	private Intbox			ibErrorFrequency, ibFrequency;
	private Checkbox		chCheckError;
	private Window			setBatchWin;
	private Button			applyButton;
	private List<String>	changedMonitors	= new ArrayList<String>();						// 收集修改过的监测器传到父页面
																							
	private Listbox			monitorlistbox;
	Label					pall, pok, pwarn, perror, pforbid, pbad,pnull;
	
	private Map<String, Map<String, String>> allmonitors = null;
	private boolean			savedataFlag	= false;
	// private MonitorSelectTree tree=(MonitorSelectTree)monitorTree;
	public SetBatch()
	{
		super();
		v = Toolkit.getToolkit().getSvdbView(Executions.getCurrent().getDesktop().getSession());

	}
	
	private Map<String, Map<String, String>> getAllMonitorInfo(){
		if (allmonitors == null)
		try {
			allmonitors = UnivData.queryAllMonitorInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return allmonitors;
	}
	
	
	
	
	public void onCreate$setBatchWin()
	{
		pall = (Label) setBatchWin.getAttribute("all");
		pok = (Label) setBatchWin.getAttribute("ok");
		pwarn = (Label) setBatchWin.getAttribute("warn");
		perror = (Label) setBatchWin.getAttribute("error");
		pforbid = (Label) setBatchWin.getAttribute("forbid");
		pbad = (Label) setBatchWin.getAttribute("bad");
		pnull = (Label) setBatchWin.getAttribute("null");
		monitorlistbox = (Listbox) setBatchWin.getAttribute("monitorlistbox");
		monitor.getPagingChild().setMold("os");
	}
	
	/**
	 * 监测器类型改变时重新组织传向子页面的数据
	 * 
	 * @return
	 */
	public void onChangeMonitorType() throws Exception
	{
		
		// logger.info("监测器类型改变");
		// this.monitorEdit=v.getMonitorEdit(node);
		if (monitorType == null) return;
		if (monitorType.getSelectedItem() == null) return;
		String type = (String) monitorType.getSelectedItem().getValue();
		if (!type.equals(""))
		{
			
			setTemplateByType(type);
			applyButton.setDisabled(false);
			refreshAlertCondition();
		}
	}
	
	private void refreshAlertCondition()
	{
		tberror.setValue("");
		tbdanger.setValue("");
		tbnormal.setValue("");
	}
	
	public void onCheckItem(Event event) 
	{
		try{
			// Messagebox.show("有节点被选中");
			listNode = getNodeids();
			// setAllMonitorEditByNode(listNode);
			refreshListbox();
			refreshMonitorTypeCombobox();
			/* setAllMonitorEdit(); */
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	// 刷新MonitorTypecombobox
	private void refreshMonitorTypeCombobox() 
	{
		try{
			monitorType.getChildren().clear();
			monitorType.setDisabled(false);
			Set<String> s = findAllMonitorType(listNode);
			
			Map<String,String> monitorlist = new HashMap<String,String>();
			ArrayList namelist = new ArrayList();
			ArrayList idlist = new ArrayList();
			for (Iterator it = s.iterator(); it.hasNext();)
			{
				String id   = (String) it.next();
				String name =  findById(id);
				monitorlist.put(name, id);
				idlist.add(id);
				namelist.add(name);
			}

			Object []strNamelist = namelist.toArray();
			Object []strIdlist   = idlist.toArray();
			java.util.Arrays.sort(strNamelist);//排序
			
	        //查找相应 的 id
			for(int i=0;i<strNamelist.length;i++){
				String idValue = monitorlist.get(strNamelist[i]);
				strIdlist[i] = idValue;
			}
			
			for (int i=0;i<strNamelist.length;i++)
			{
				Comboitem ci = new Comboitem(strNamelist[i].toString());
				ci.setValue(strIdlist[i]);
				ci.setParent(monitorType);
			}		
			
			if (!listNode.isEmpty())
				monitorType.setSelectedIndex(0);
			onChangeMonitorType();
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	// 将修改结果返回到父页面setmonitor
	public void returnToParent() 
	{
		try{
			Object[] obj = monitorlistbox.getChildren().toArray();
			for (Object o : obj){
				if ((o instanceof Listitem)){
					monitorlistbox.removeChild((Component) o);
				}
			}
			
			//Map<String, Map<String, String>> fmap = UnivData.getInstance().queryAllMonitorInfo();
			//在获取 getAllMonitorInfo() 经常 抛出错误 一些node 不能得到处理
			ArrayList<String>	filterChangedMonitors	= new ArrayList<String>();
			
			List<Map<String, String>> resultMap = new ArrayList();
			for (int i = 0; i < changedMonitors.size(); i++)
			{
				Map<String, String> m = null;
				try{
					m = getAllMonitorInfo().get(changedMonitors.get(i));
				}catch(Exception e){
					e.printStackTrace();
				}
				if(m == null) continue;
				
				INode tempNode = v.getNode(changedMonitors.get(i));
				if(tempNode == null) continue;
				
				MonitorEdit tempMonitorEdit = null;
				try{
					tempMonitorEdit = v.getMonitorEdit(tempNode);
				}catch(Exception e1){
					e1.printStackTrace();
				}
				if(tempMonitorEdit == null) continue;
				
				String tempStatus = "";
				tempStatus = tempMonitorEdit.getStatus();
				if(tempStatus == null || "".equals(tempStatus)) continue;
				m.put("Status", tempStatus);
				
				// 加入checker
				MonitorEdit me = null ;
				try{
					me = v.getMonitorEdit(v.getNode(changedMonitors.get(i)));
				}catch(Exception e){
					e.printStackTrace();
				}
				if(me != null){
					if (me.getParameter().get("sv_checkerr") != null)
						m.put("Checkerr", me.getParameter().get("sv_checkerr"));
					resultMap.add(m);
					filterChangedMonitors.add(changedMonitors.get(i));
				}
			}
			
			int okCount = 0, badCount = 0, forbidCount = 0, errorCount = 0, warnCount = 0, nullCount = 0;
			
			for (int i = 0; i < filterChangedMonitors.size(); i++)
			{	
				String status = resultMap.get(i).get("Status");
				String st = status;
				if (st == null || st.equals("null") || st.equals("")){
					nullCount++;
					continue;
				}
				if (st.equals("bad")){badCount++;}//定义错误
				if (st.equals("error")){errorCount++;}
				if (st.equals("ok")){okCount++;}
				if (st.equals("warning")){warnCount++;}
				if (st.equals("disable")){forbidCount++;}
			}
			MonitorInfoListbox monitorInfoListbox =(MonitorInfoListbox)monitorlistbox;
			monitorInfoListbox.setChangedMonitors(filterChangedMonitors);
			monitorInfoListbox.setResultMap(resultMap);
			ChartUtil.clearListbox(monitorInfoListbox);
			monitorInfoListbox.onCreate();//java.lang.IndexOutOfBoundsException: Index: 621, Size: 621

			pall.setValue("" + filterChangedMonitors.size());
			pok.setValue(okCount + "");
			perror.setValue(errorCount + "");
			pwarn.setValue(warnCount + "");
			pforbid.setValue(forbidCount + "");
			pbad.setValue(badCount + "");
			pnull.setValue(nullCount +"");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 传递要批量修改的监测器属性的键值字典，将数据批量修改到多个监测器
	 * 
	 * @param ids
	 *            多个监测器的id,以逗号隔开
	 * @param data要修改的监测器属性的键值字典
	 * @return
	 */
	private boolean SetValueInManyMonitor(String ids, Map<String, Map<String, String>> data)
	{
		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "SetValueInManyMonitor");
		ndata.put("id", ids);
		Map<String, Map<String, String>> fdata = data;
		try
		{
			RetMapInMap rmap = ManageSvapi.SubmitUnivData(fdata, ndata);
			if (!rmap.getRetbool())
			{
				return false;
			}
		} catch (Exception e)
		{
			return false;
		}
		Manager.instantUpdate();
		return true;
	}
	

	private void refreshListbox()
	{
		monitor.getItems().clear();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		
		if (listNode.size() != 0)
		{
			allmonitors = null;//强制获取最新的数据

			Set<INode> set = new LinkedHashSet<INode>();
			for (INode node : listNode){
				set.add(node);
			}
			for(INode node : set){
				Map<String, String> m = getAllMonitorInfo().get(node.getSvId());
				String statesValues = node.getStatus();
//				if(statesValues == null || "".equals(statesValues) || "null".equals(statesValues)){
//					statesValues = "error";
//				}
				m.put("Status", statesValues);
				list.add(m);
			}

			SetMonitorModel model = new SetMonitorModel(list);
			makelistData(monitor, model, model);
		}
	}
	
	public void makelistData(Listbox listb, ListModelList model, ListitemRenderer rend)
	{
		listb.setModel(model);
		listb.setItemRenderer(rend);
	}
	
	/**
	 * 获取选择的监测器节点
	 * 
	 * @return
	 */
	public List<INode> getNodeids()
	{
		List<INode> nodes = new ArrayList<INode>();
		
		for (String id : ((MonitorSelectTree) monitorTree).getSelectedIds())
		{
			INode node = v.getNode(id);
			if (node.getType().equals(INode.MONITOR))
				nodes.add(node);
		}
		return nodes;
	}
	
	/**
	 * 找出所有的监测器类型代码
	 * 
	 * @return
	 */
	private Set<String> findAllMonitorType(List<INode> n)
	{
		Set<String> groupid = new HashSet<String>();
		List<INode> nodes = n;
		for (INode node : nodes)
		{
			groupid.add(v.getMonitorInfo(node).getMonitorType());
		}
		return groupid;
	}
	
	/**
	 * 通过代码找出监测器类型名称
	 * 
	 * @return
	 */
	public String findById(String id)
	{
		MonitorTemplate temple = TemplateManager.getMonitorTemplate(id);
		return temple==null ? "" : temple.get_sv_name();
	}
	
	
	// 从view中得到所选类型的template
	public void setTemplateByType(String type) throws Exception
	{
		if (!(type == null || type.isEmpty()))
			monitorTemplate = TemplateManager.getMonitorTemplate(type);
	}
	
	/**
	 * 通过type代码找到template
	 * 
	 * @return
	 */
	public MonitorTemplate getMonitorTemplateByMonitorEdit(MonitorEdit me)
	{
		MonitorTemplate temple = me.getMonitorTemplate();
		return temple;
	}
	
	/**
	 * 通过type代码找到node的集合
	 * 
	 * @return
	 */
	public List<INode> getNodesByTypeId(String t)
	{
		if (!t.equals(""))
		{
			List<INode> l = new ArrayList<INode>();
			for (int i = 0; i < listNode.size(); i++)
			{

				if (v.getMonitorInfo(listNode.get(i)).getMonitorType().equals(t))
				{
					l.add(listNode.get(i));
				}
			}
			return l;
		}
		return null;
	}
	
	public void onErrorButton() throws Exception
	{
		if (((MonitorSelectTree) monitorTree).getSelectedIds().size() == 0)
		{
			Messagebox.show("请选择监测器！", "提示", Messagebox.OK, Messagebox.INFORMATION);
			return;
		}
		final Window win = (Window) Executions.createComponents(Alert_TargetUrl, null, null);
		// 此处加上属性列表
		win.setTitle("编辑错误条件");
		win.setAttribute("tb", tberror);
		win.setAttribute("monitorTemplate", monitorTemplate);
		try
		{
			win.doModal();
		} catch (Exception e)
		{
			Messagebox.show(e.getMessage(), "错误", Messagebox.OK, Messagebox.ERROR);
		}
	}
	
	public void onAlertButton() throws Exception
	{
		if (((MonitorSelectTree) monitorTree).getSelectedIds().size() == 0)
		{
			Messagebox.show("请选择监测器！", "提示", Messagebox.OK, Messagebox.INFORMATION);
			return;
		}
		final Window win = (Window) Executions.createComponents(Alert_TargetUrl, null, null);
		// 此处加上属性列表
		win.setTitle("编辑警告条件");
		win.setAttribute("tb", tbdanger);
		win.setAttribute("monitorTemplate", monitorTemplate);
		try
		{
			win.doModal();
		} catch (Exception e)
		{
			Messagebox.show(e.getMessage(), "错误", Messagebox.OK, Messagebox.ERROR);
		}
	}
	
	public void onNomalButton() throws Exception
	{
		if (((MonitorSelectTree) monitorTree).getSelectedIds().size() == 0)
		{
			Messagebox.show("请选择监测器！", "提示", Messagebox.OK, Messagebox.INFORMATION);
			return;
		}
		final Window win = (Window) Executions.createComponents(Alert_TargetUrl, null, null);
		// 此处加上属性列表
		win.setTitle("编辑正常条件");
		win.setAttribute("tb", tbnormal);
		win.setAttribute("monitorTemplate", monitorTemplate);
		try
		{
			win.doModal();
		} catch (Exception e)
		{
			Messagebox.show(e.getMessage(), "错误", Messagebox.OK, Messagebox.ERROR);
		}
		
	}
	//新增加的进度条功能
	private void refreshMonitorListbox() {
		try {
			final Window win = (Window) Executions.createComponents(
					"/main/setmonitor/progress.zul", null, null);
			win.setAttribute("setBatchWin", this);
			win.setMaximizable(false);
			win.setClosable(true);
			win.doModal();
		} catch (Exception e) {
			try{
				Messagebox.show(e.getMessage(), "错误", Messagebox.OK,Messagebox.ERROR);
			}catch(Exception e1){
			}
		}
	}
	
	
	public void onOk()
	{
		if(before_savedata())
		{
			this.savedataFlag = false;
			refreshMonitorListbox();//运行 savedata()
			after_savedata();
			if(this.savedataFlag){
				this.refreshListbox();
				returnToParent();
			}else{
				return;
			}
			try{
				setBatchWin.detach();
			} catch (Exception e)
			{
			}
		}
	}
//old	
/*	public void onOk() throws Exception
	{	
		if (savedata())//更改了数据 //
		{
			this.refreshListbox();
			// detach窗体
			returnToParent();
			
		} else{
			return;
		}
		try{
			setBatchWin.detach();
		} catch (Exception e){
			
		}
	}*/
	
	
	
	public void onApply() throws Exception
	{
		if(before_savedata()){
			this.savedataFlag = false;
			refreshMonitorListbox();//运行 savedata()
			after_savedata();
			if(this.savedataFlag){
				this.refreshListbox();
				applyButton.setDisabled(true);
			}else{
				return;
			}
		}
	}
	
//old	
/*	public void onApply() throws Exception
	{
		if (savedata())
		{
			// 按钮变灰并刷新listbox
			this.refreshListbox();
			applyButton.setDisabled(true);
		}
	}	*/
	
	public void onCancel() throws Exception
	{
		
		try
		{
			setBatchWin.detach();
		} catch (Exception e)
		{
		}
	}
	
	protected boolean before_savedata() 
	{
		try{
			if (((MonitorSelectTree) monitorTree).getSelectedIds().size() == 0)
			{
				Messagebox.show("请选择监测器！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				return false;
			}
			String panelId = tabbox.getSelectedPanel().getId();
			if (panelId != null && panelId.equals("p1"))
			{
				if (ibFrequency.getValue() == null)
				{
					Messagebox.show("监测器频率没有被修改！", "提示", Messagebox.OK, Messagebox.INFORMATION);
					ibFrequency.setFocus(true);
					return false;
				}
			}
			if (panelId != null && panelId.equals("p3"))
			{
				if (ibErrorFrequency.getValue() == null)
				{
					Messagebox.show("错误效验频率没有被修改！", "提示", Messagebox.OK, Messagebox.INFORMATION);
					ibErrorFrequency.setFocus(true);
					return false;
				}
			}
			if (panelId != null && panelId.equals("p2"))
			{
				if (tberror.getValue().equals("") && tbnormal.getValue().equals("") && tbdanger.getValue().equals(""))
				{
					Messagebox.show("阀值没有被修改！", "提示", Messagebox.OK, Messagebox.INFORMATION);
					return false;
				}
			}
			if (!(tberror.getValue().equals("") && tbnormal.getValue().equals("") && tbdanger.getValue().equals("")))
			{
				if (tberror.getValue().equals(""))
				{
					Messagebox.show("请输入错误阀值！", "提示", Messagebox.OK, Messagebox.INFORMATION);
					tberror.setFocus(true);
					return false;
				}
				if (tbdanger.getValue().equals(""))
				{
					Messagebox.show("请输入警告阀值！", "提示", Messagebox.OK, Messagebox.INFORMATION);
					tbdanger.setFocus(true);
					return false;
				}
				if (tbnormal.getValue().equals(""))
				{
					Messagebox.show("请输入正常阀值！", "提示", Messagebox.OK, Messagebox.INFORMATION);
					tbnormal.setFocus(true);
					return false;
				}
			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}

	}
	
	// 存储监测器
	protected boolean savedata() throws InterruptedException
	{
		// 清空得到所有的检测器id列表
		changedMonitors.clear();
		String monitorname=monitorType.getValue();
		String svids = "";
		System.out.println(monitorType.getSelectedItem().getAttribute("monitorType")+"monitorType::::::");
		System.out.println(listNode);
		for (int i = 0; i < listNode.size(); i++)
		{
			if (monitorname.equals(listNode.get(i).getName().substring(
					0,
					listNode.get(i).getName().indexOf(":") > 0 ? listNode
							.get(i).getName().indexOf(":") : listNode.get(i)
							.getName().length()))||monitorname.equals(listNode.get(i).getName().substring(
									0,
									listNode.get(i).getName().indexOf("：") > 0 ? listNode
											.get(i).getName().indexOf("：") : listNode.get(i)
											.getName().length()))) {
				String svid = listNode.get(i).getSvId();
				String monitorone=listNode.get(i).getType();
				svids = svids + svid + ",";
				System.out.println(listNode.get(i).getName());
				if (!changedMonitors.contains(svid))
				{
					changedMonitors.add(svid);
				}
			}
		}
		 Map<String, Map<String, String>> data=buildBaseData();
		boolean rt = SetValueInManyMonitor(svids,data);
		this.savedataFlag = rt;
		if (!rt)
		{
			return false;
		}
		monitorType.setDisabled(false);
		return true;
	}
	
	protected void after_savedata()
	{
		View view = Toolkit.getToolkit().getSvdbView(Executions.getCurrent().getDesktop());
		String loginname = view.getLoginName();
		String minfo=loginname+" "+"在"+OpObjectId.monitor_set.name+"中进行了  "+OpTypeId.edit.name+"操作 ";
		AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.edit, OpObjectId.monitor_set);
	}

	
	/**
	 * 构建基础数据
	 * @return
	 */
	private  Map<String, Map<String, String>>  buildBaseData()
	{
		 Map<String, Map<String, String>> data=new HashMap<String, Map<String,String>>();
		 Map<String, String> mointorparm=new HashMap<String, String>();
		if (ibErrorFrequency.getValue() != null)
		{
			String errfreqsave = ibErrorFrequency.getValue().toString();
			mointorparm.put("sv_errfreqsave", errfreqsave);
			String errfrequint = "分钟".equals(this.cbErrorFrequencyUnit.getValue()) ? "1" : "60";
			mointorparm.put("sv_errfrequint", errfrequint);
			if (errfreqsave == null || errfreqsave.isEmpty())
			{
				errfreqsave = "0";
			}
			if (!cbErrorFrequencyUnit.getValue().equals("分钟"))
			{
				errfreqsave = Integer.toString((Integer.parseInt(errfreqsave) * 60));
			}
			mointorparm.put("sv_errfreq", errfreqsave);
			String checkerr = chCheckError.isChecked() ? "true" : "false";
			mointorparm.put("sv_checkerr", checkerr);
		}
		// 基础信息
		if (ibFrequency.getValue() != null)
		{
			String freq = ibFrequency.getValue().toString();
			mointorparm.put("_frequency1", freq);
			String frequencyUnit = "分钟".equals(this.cbFrequencyUnit.getValue()) ? "1" : "60";
			mointorparm.put("_frequencyUnit", frequencyUnit);
			String va = "1".equals(frequencyUnit) ? freq : Integer.toString((Integer.parseInt(freq) * 60));
			mointorparm.put("_frequency", va);
		}
		 // 阀值信息
		if (!tbdanger.getValue().equals("") && !tberror.getValue().equals("")&&!tbnormal.getValue().equals(""))
		{
			// 判断当前monitoredit是不是符合选择的类型
			Map<String, String> ErrorConditon = (Map<String, String>) tberror.getAttribute("expr");// 得到阀值参数！！！！！！！！！！！！
			data.put("monitor_error", ErrorConditon);
			Map<String, String> WarningConditon = (Map<String, String>) tbdanger.getAttribute("expr");
			data.put("monitor_warnning", WarningConditon);
			Map<String, String> GoodConditon = (Map<String, String>) tbnormal.getAttribute("expr");	
			data.put("monitor_good", GoodConditon);
		}
		if(mointorparm.size()>0)
		{
			data.put("monitor_parameter", mointorparm);
		}
		return data;
	}
	
}
