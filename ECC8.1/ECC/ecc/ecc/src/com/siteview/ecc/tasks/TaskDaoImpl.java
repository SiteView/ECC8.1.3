/**
 * 
 */
package com.siteview.ecc.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.zkoss.zk.ui.Executions;
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
 * @author yuandong显示时间段和绝对任务计划公用这个页面通过类型判断是哪个页面
 * 
 */
public class TaskDaoImpl extends GenericForwardComposer {

	private Listbox Listbox;
	private Button delButton;
	
	private ArrayList<String> nameList = new ArrayList<String>();
	private String flag = "";

	
	public void onInit() throws Exception {
		TaskPack t = new TaskPack();
		Task[] a = t.findAllTasks();
	}

	private void onInit(String s) throws Exception {
		TaskPack tp = new TaskPack();
		Task t[] = tp.findAllByType(s);

	//	 for (int i = 0; i < t.length; i++) t[i].display();

		java.util.List table = new java.util.ArrayList();

		for (int i = 0; i < t.length; i++) {
			if (t[i].getName() != null) {
				nameList.add(t[i].getName());
				table.add(t[i]);
			}

		}
		TaskItemRenderer model = new TaskItemRenderer(table);
		MakelistData(Listbox, model, model);
	}

	public void onInitAb() throws Exception {
		onInit("1");

	}

	public void onInitPer() throws Exception {
		onInit("2");

	}

	public void onInitRea() throws Exception {
		onInit("3");

	}
	
	public void onAddButton(Event event) throws SuspendNotAllowedException,
			InterruptedException {
		final Window win = (Window) Executions.createComponents(
				"/main/setting/editAbTask.zul", null, null);
		win.setTitle("添加绝对时间任务计划");
		win.setAttribute("flag", "add");
		win.setAttribute("nameList", nameList);

		win.doModal();
	}

	public void onAddButton1(Event event) throws SuspendNotAllowedException,
			InterruptedException {
		final Window win = (Window) Executions.createComponents(
				"/main/setting/editPerTask.zul", null, null);
		win.setTitle("添加时间段任务计划");
		win.setAttribute("flag", "add");
		win.setAttribute("nameList", nameList);

		win.doModal();
	}

	public void onAddBtnRelative(Event event) throws Exception{
		try{
			
			final Window win = (Window) Executions.createComponents(
					"/main/setting/editRelativeTask.zul", null, null);
			win.setAttribute("flag", "add");
			win.setAttribute("nameList", nameList);
			win.setTitle("添加相对时间任务计划");
			win.doModal();	
		}catch(Exception e){
			Messagebox.show(e.getMessage(),"错误", Messagebox.OK, Messagebox.ERROR);
		}
	}
	public static ArrayList<HashMap<String,String>> getUsingTaskList()
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
				if(!"".equals(StringTaskName.trim()) && !"".equals(StringTaskType.trim())){
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

					if("绝对时间任务计划".equals(StringTaskType)){

						map.put("1", StringTaskName);
					}else if("时间段任务计划".equals(StringTaskType)){

						map.put("2", StringTaskName);
					}else
					{

						map.put("3", StringTaskName);
					}
					tasklist.add(map);
				}
			}
		}

		return tasklist;
	}
	
	
	
	public void onDelButton(Event event) throws Exception  {
		Set<Listitem> s = Listbox.getSelectedItems();
		String flag = delButton.getPage().getId();
		if (s.size() == 0){
			try{
				Messagebox.show("没有选择任何任务", "提示", Messagebox.OK, Messagebox.INFORMATION);
			}catch(Exception e){}
			return;
		}
		int i = 0;
		try{
			i = Messagebox.show("确定要删除所选任务吗?", "询问", Messagebox.OK
					| Messagebox.CANCEL, Messagebox.QUESTION);
		}catch(Exception e){}
		
		if (i>0) {
			TaskPack tp = new TaskPack();

			View view = Toolkit.getToolkit().getSvdbView(
					event.getTarget().getDesktop());
			String loginname = view.getLoginName();
			for (Iterator<Listitem> it = s.iterator(); it.hasNext();) {

				String name = it.next().getLabel();
				
				if ((name.equals("7*24")  || name.equals("5*8") )
						&& flag.equals("perPage")){
					try{
						Messagebox.show("默认值不能够被删除！", "提示", Messagebox.OK, Messagebox.INFORMATION);
					}catch(Exception e){}
					continue;
				}
				//添加被引用不能删除部分
				//abPage//绝对时间 1
				//perPage //时间段任务计划 2
				//relativePage //相对时间任务计划 //3
				ArrayList<HashMap<String,String>> tasklist = getUsingTaskList();
				
				boolean deleteflag = true;
				if("abPage".equals(flag)){
					for(HashMap<String,String> map:tasklist){
						if(map.containsKey("1")){
							String temp = map.get("1");
							if( temp != null  || !"".equals(temp.trim())){
								if( name.equals(temp)){
									Messagebox.show("任务计划 "+name+" 正在被使用 ,不能操作，请重选", "提示", Messagebox.OK, Messagebox.INFORMATION);
									deleteflag = false;
									break;
								}
							}
						}
					}
				}
				else if("perPage".equals(flag)){
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
				}
				else {//if("relativePage".equals(flag)){
					for(HashMap<String,String> map:tasklist){
						if(map.containsKey("3")){
							String temp = map.get("3");
							if( temp != null  || !"".equals(temp.trim())){
								if( name.equals(temp)){
									Messagebox.show("任务计划 "+name+" 正在被使用 ,不能操作，请重选", "提示", Messagebox.OK, Messagebox.INFORMATION);
									deleteflag = false;
									break;
								}
							}
						}
					}
				}
				
				if(deleteflag){// 能够删除
					
					try{
						tp.deleteTaskByName(name);
						nameList.remove(name);
						if (flag.equals("abPage")){
							String minfo = loginname + " " + "在"
							+ OpObjectId.absolute_task.name + "中进行了  "
							+ OpTypeId.del.name + "操作，删除项为： " + name;
							AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.del,
							OpObjectId.absolute_task);
						}else if(flag.equals("perPage")){
							String minfo = loginname + " " + "在"
							+ OpObjectId.time_task.name + "中进行了  "
							+ OpTypeId.del.name + "操作，删除项为： " + name;
							AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.del,
							OpObjectId.time_task);
						}else
						{
							String minfo = loginname + " " + "在"
							+ OpObjectId.relative_task.name + "中进行了  "
							+ OpTypeId.del.name + "操作，删除项为： " + name;
							AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.del,
							OpObjectId.relative_task);
						}

					}catch(Exception e){e.printStackTrace();}
				}
			}
			
			if (flag.equals("abPage"))
				onInitAb();
			else if(flag.equals("perPage")){
				onInitPer();
			}else{
				onInitRea();
			}
				
		}
	}


	private void MakelistData(Listbox listb, ListModelList model,
			ListitemRenderer rend) {
		listb.setModel(model);
		listb.setItemRenderer(rend);
	}

}
