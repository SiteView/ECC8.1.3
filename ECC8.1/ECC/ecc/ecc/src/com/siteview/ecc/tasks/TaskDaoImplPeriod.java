/**
 * 
 */
package com.siteview.ecc.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Window;
import org.zkoss.zul.api.Toolbarbutton;

import com.siteview.base.manage.View;
import com.siteview.ecc.email.IniFilePack;
import com.siteview.ecc.log.AppendOperateLog;
import com.siteview.ecc.log.OpObjectId;
import com.siteview.ecc.log.OpTypeId;
import com.siteview.ecc.tasks.TaskPack;
import com.siteview.ecc.tasks.Task;
import com.siteview.ecc.util.Toolkit;

/**
 * @author yuandong
 * 
 */
public class TaskDaoImplPeriod extends GenericForwardComposer {

	private Listbox Listbox;
	private Button delButton;
	
	private ArrayList<String> abTaskList = new ArrayList<String>();
	private ArrayList<String> perTaskList = new ArrayList<String>();
	private ArrayList<String> reTaskList = new ArrayList<String>();
	private String add_section 	= "";
	private String edit_section = "";
	

	public void onInitPer() throws Exception {
		TaskPack tp = new TaskPack();
		Task abT[] = tp.findTaskAbsloute();
		Task perT[] = tp.findTaskPeriod();
		Task reT[] = tp.findTaskRelative();
		
		java.util.ArrayList<Task> table = new ArrayList<Task>();

		for (int i = 0; i < perT.length; i++) {
			if (perT[i].getName() != null) {
				perTaskList.add(perT[i].getName());
				table.add(perT[i]);
			}
		}
		for (int i = 0; i < abT.length; i++) {
			if (abT[i].getName() != null) {
				abTaskList.add(abT[i].getName());
			}
		}
		for (int i = 0; i < reT.length; i++) {
			if (reT[i].getName() != null) {
				reTaskList.add(reT[i].getName());
			}
		}
		Listbox.getPagingChild().setMold("os");
		TaskItemRenderer model = new TaskItemRenderer(table);
		MakelistData(Listbox, model, model);
		
		Session session = this.session;
		Object addObj = session.getAttribute(TaskConstant.add_period_section);
		session.removeAttribute(TaskConstant.add_period_section);//remove;
		
		Object editObj = session.getAttribute(TaskConstant.edit_period_section);
		session.removeAttribute(TaskConstant.edit_period_section);//remove;
		if(addObj!= null){
			this.add_section = (String)addObj;
			FindTaskSelectedListitem(add_section);
		}else if(editObj!=null){
			this.edit_section = (String)editObj;
			FindTaskSelectedListitem(edit_section);
		}
	}
	
	public void FindTaskSelectedListitem(String taskName){
		if (Listbox != null){
			for (int i = 0; i < Listbox.getModel().getSize(); i++) {
				Task task = (Task)Listbox.getModel().getElementAt(i);
				if(taskName.equals(task.getName())){
					Listbox.setSelectedIndex(i);
					break;
				}
			}
		}
	}
	
	public void onAddButton1(Event event) throws Exception{
		try{
			final Window win = (Window) Executions.createComponents(
					"/main/setting/editPerTask.zul", null, null);
			win.setTitle("添加时间段任务计划");
			win.setAttribute("flag", "add");
			win.setAttribute("abTaskList", abTaskList);
			win.setAttribute("perTaskList", perTaskList);
			win.setAttribute("reTaskList", reTaskList);
			win.doModal();
		}catch(Exception e){
			Messagebox.show("添加时间段任务计划 出错", "错误", Messagebox.OK,Messagebox.ERROR);
		}
	}

	public  ArrayList<HashMap<String,String>> getUsingTaskList()
	{
		//从邮件设置中...
		IniFilePack ini = new IniFilePack("emailAdress.ini");
		try{
			ini.load();
		}catch(Exception e){}
		ArrayList<HashMap<String,String>> tasklist = new ArrayList<HashMap<String,String>>();
		ArrayList<String> sectionlist = ini.getSectionList();
		for(String s:sectionlist)
		{
			String StringTaskName =  ini.getValue(s, "Schedule");//任务计划类型，任务计划名称
			String StringTaskType =  ini.getValue(s, "TaskType");
			HashMap<String,String> map = new HashMap<String,String>();
			if(StringTaskType != null && StringTaskName != null)
			{
				if(!"".equals(StringTaskName.trim()) && "1".equals(StringTaskType.trim())){
					map.put(StringTaskType, StringTaskName);
					tasklist.add(map);
				}
			}
		}

		//从短信设置中...
		IniFilePack ini2 = new IniFilePack("smsphoneset.ini");
		try{
			ini2.load();
		}catch(Exception e){}
		sectionlist = ini2.getSectionList();
		for(String s:sectionlist)
		{
			String StringTaskName =  ini2.getValue(s, "Plan");//任务计划类型，任务计划名称
			String StringTaskType =  ini2.getValue(s, "TaskType");//时间段任务计划  绝对时间任务计划  相对时间任务计划 1
			HashMap<String,String> map = new HashMap<String,String>();
			if(StringTaskType != null && StringTaskName != null)
			{
				if(!"".equals(StringTaskName.trim()) && !"".equals(StringTaskType.trim())){

					if("时间段任务计划".equals(StringTaskType)){

						map.put("2", StringTaskName);
					}
					tasklist.add(map);
				}
			}
		}
		return tasklist;
	}
	
	
	
	public void onDelButton(Event event) throws Exception  {
		Set<Listitem> s = Listbox.getSelectedItems();
		if (s.size() < 1){
			Messagebox.show("没有选择任何任务", "提示", Messagebox.OK, Messagebox.INFORMATION);
			return;
		}
		if(Messagebox.CANCEL == Messagebox.show("确定要删除所选任务吗?", "询问", Messagebox.OK| Messagebox.CANCEL, Messagebox.QUESTION)){
			return;
		}
		TaskPack tp = new TaskPack();

		View view = Toolkit.getToolkit().getSvdbView(event.getTarget().getDesktop());
		String loginname = view.getLoginName();
		for (Iterator<Listitem> it = s.iterator(); it.hasNext();) {
			String name = it.next().getLabel();
			if (name.equals("7*24")  || name.equals("5*8")){
				Messagebox.show("默认值7*24和5*8不能够被删除！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				continue;
			}
			//abPage//绝对时间 1
			//perPage //时间段任务计划 2
			//relativePage //相对时间任务计划 //3
			ArrayList<HashMap<String,String>> tasklist = getUsingTaskList();
			boolean deleteflag = true;
			for(HashMap<String,String> map:tasklist){
				if(map.containsKey("2")){
					String temp = map.get("2");
					if( temp != null  || !"".equals(temp.trim())){
						if( name.equals(temp)){
							Messagebox.show("任务计划 "+name+" 正在被使用 ,不能操作，请重选", "提示", Messagebox.OK, Messagebox.INFORMATION);
							deleteflag = false;
							break;
						}
					}
				}
			}
			if(deleteflag){
				try{
					tp.deleteTaskByName(name);
					perTaskList.remove(name);
						String minfo = loginname + " " + "在"+ OpObjectId.time_task.name + "中进行了  "+ OpTypeId.del.name + "操作，删除项为： " + name;
						AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.del,
						OpObjectId.time_task);

				}catch(Exception e){
					Messagebox.show("删除时间段任务计划"+name+" 出错", "错误", Messagebox.OK,
							Messagebox.ERROR);
				}
			}
		}
		onInitPer();
	}


	private void MakelistData(Listbox listb, ListModelList model,
			ListitemRenderer rend) {
		listb.setModel(model);
		listb.setItemRenderer(rend);
	}

}
