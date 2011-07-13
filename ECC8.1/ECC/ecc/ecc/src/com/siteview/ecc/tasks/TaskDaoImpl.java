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
 * @author yuandong��ʾʱ��κ;�������ƻ��������ҳ��ͨ�������ж����ĸ�ҳ��
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
		win.setTitle("��Ӿ���ʱ������ƻ�");
		win.setAttribute("flag", "add");
		win.setAttribute("nameList", nameList);

		win.doModal();
	}

	public void onAddButton1(Event event) throws SuspendNotAllowedException,
			InterruptedException {
		final Window win = (Window) Executions.createComponents(
				"/main/setting/editPerTask.zul", null, null);
		win.setTitle("���ʱ�������ƻ�");
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
			win.setTitle("������ʱ������ƻ�");
			win.doModal();	
		}catch(Exception e){
			Messagebox.show(e.getMessage(),"����", Messagebox.OK, Messagebox.ERROR);
		}
	}
	public static ArrayList<HashMap<String,String>> getUsingTaskList()
	{
		//���ʼ�������...
		IniFilePack ini = new IniFilePack("emailAdress.ini");
		try{
			ini.load();
		}catch(Exception e){}
		ArrayList<HashMap<String,String>> tasklist = new ArrayList<HashMap<String,String>>();
		ArrayList<String> sectionlist = ini.getSectionList();
		for(String s:sectionlist)
		{
			String StringTaskName =  ini.getValue(s, "Schedule");//����ƻ����ͣ�����ƻ�����
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

		//�Ӷ���������...
		IniFilePack ini2 = new IniFilePack("smsphoneset.ini");
		try{
			ini2.load();
		}catch(Exception e){}
		sectionlist = ini2.getSectionList();
		for(String s:sectionlist)
		{
			String StringTaskName =  ini2.getValue(s, "Plan");//����ƻ����ͣ�����ƻ�����
			String StringTaskType =  ini2.getValue(s, "TaskType");//ʱ�������ƻ�  ����ʱ������ƻ�  ���ʱ������ƻ� 1
			HashMap<String,String> map = new HashMap<String,String>();
			if(StringTaskType != null && StringTaskName != null)
			{
				if(!"".equals(StringTaskName.trim()) && !"".equals(StringTaskType.trim())){

					if("����ʱ������ƻ�".equals(StringTaskType)){

						map.put("1", StringTaskName);
					}else if("ʱ�������ƻ�".equals(StringTaskType)){

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
				Messagebox.show("û��ѡ���κ�����", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
			}catch(Exception e){}
			return;
		}
		int i = 0;
		try{
			i = Messagebox.show("ȷ��Ҫɾ����ѡ������?", "ѯ��", Messagebox.OK
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
						Messagebox.show("Ĭ��ֵ���ܹ���ɾ����", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
					}catch(Exception e){}
					continue;
				}
				//��ӱ����ò���ɾ������
				//abPage//����ʱ�� 1
				//perPage //ʱ�������ƻ� 2
				//relativePage //���ʱ������ƻ� //3
				ArrayList<HashMap<String,String>> tasklist = getUsingTaskList();
				
				boolean deleteflag = true;
				if("abPage".equals(flag)){
					for(HashMap<String,String> map:tasklist){
						if(map.containsKey("1")){
							String temp = map.get("1");
							if( temp != null  || !"".equals(temp.trim())){
								if( name.equals(temp)){
									Messagebox.show("����ƻ� "+name+" ���ڱ�ʹ�� ,���ܲ���������ѡ", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
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
									Messagebox.show("����ƻ� "+name+" ���ڱ�ʹ�� ,���ܲ���������ѡ", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
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
									Messagebox.show("����ƻ� "+name+" ���ڱ�ʹ�� ,���ܲ���������ѡ", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
									deleteflag = false;
									break;
								}
							}
						}
					}
				}
				
				if(deleteflag){// �ܹ�ɾ��
					
					try{
						tp.deleteTaskByName(name);
						nameList.remove(name);
						if (flag.equals("abPage")){
							String minfo = loginname + " " + "��"
							+ OpObjectId.absolute_task.name + "�н�����  "
							+ OpTypeId.del.name + "������ɾ����Ϊ�� " + name;
							AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.del,
							OpObjectId.absolute_task);
						}else if(flag.equals("perPage")){
							String minfo = loginname + " " + "��"
							+ OpObjectId.time_task.name + "�н�����  "
							+ OpTypeId.del.name + "������ɾ����Ϊ�� " + name;
							AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.del,
							OpObjectId.time_task);
						}else
						{
							String minfo = loginname + " " + "��"
							+ OpObjectId.relative_task.name + "�н�����  "
							+ OpTypeId.del.name + "������ɾ����Ϊ�� " + name;
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
