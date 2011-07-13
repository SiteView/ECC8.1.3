/**
 * 
 */
package com.siteview.ecc.tasks;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Include;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timebox;
import org.zkoss.zul.Window;

import com.siteview.base.manage.View;
import com.siteview.ecc.log.AppendOperateLog;
import com.siteview.ecc.log.OpObjectId;
import com.siteview.ecc.log.OpTypeId;
import com.siteview.ecc.util.Toolkit;

/**
 * @author yuandong
 * 添加和编辑时间段任务计划公用这个页面
 */
public class EditPerTask extends GenericForwardComposer {
	private Checkbox cb0;
	private Checkbox cb1;
	private Checkbox cb2;
	private Checkbox cb3;
	private Checkbox cb4;
	private Checkbox cb5;
	private Checkbox cb6;
	private Timebox tb01;
	private Timebox tb02;
	private Timebox tb11;
	private Timebox tb12;
	private Timebox tb21;
	private Timebox tb22;
	private Timebox tb31;
	private Timebox tb32;
	private Timebox tb41;
	private Timebox tb42;
	private Timebox tb51;
	private Timebox tb52;
	private Timebox tb61;
	private Timebox tb62;
	private Textbox nameTextbox;
	private Textbox description;
	private Window editPerTask;
	private Include eccBody;
	private SimpleDateFormat DATE_TO_STRING = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
	@SuppressWarnings("deprecation")
	public void onInit() throws Exception {
		try{
			if (editPerTask.getAttribute("flag").equals("edit")) {
				TaskPack tp = new TaskPack();
				Task t = tp.findByName((String) editPerTask.getAttribute("itemId"));
				t.display();//add
				nameTextbox.setValue(t.getName());
				nameTextbox.setDisabled(true);
				cb0.setChecked(t.getAllow0().equals("1"));
				cb1.setChecked(t.getAllow1().equals("1"));
				cb2.setChecked(t.getAllow2().equals("1"));
				cb3.setChecked(t.getAllow3().equals("1"));
				cb4.setChecked(t.getAllow4().equals("1"));
				cb5.setChecked(t.getAllow5().equals("1"));
				cb6.setChecked(t.getAllow6().equals("1"));
				description.setValue(t.getDescription());
				// 下面是string-》date
				
				String start0 = "1970-01-01 "+t.getStart0()+":00"; //1970-01-01 09:00:00
				String end0   = "1970-01-01 "+t.getEnd0()+":00";
				tb01.setValue(DATE_TO_STRING.parse(start0));
				tb02.setValue(DATE_TO_STRING.parse(end0));
				
				String start1 = "1970-01-01 "+t.getStart1()+":00"; //1970-01-01 09:00:00
				String end1   = "1970-01-01 "+t.getEnd1()+":00";
				tb11.setValue(DATE_TO_STRING.parse(start1));
				tb12.setValue(DATE_TO_STRING.parse(end1));
				
				String start2 = "1970-01-01 "+t.getStart2()+":00"; //1970-01-01 09:00:00
				String end2   = "1970-01-01 "+t.getEnd2()+":00";
				tb21.setValue(DATE_TO_STRING.parse(start2));
				tb22.setValue(DATE_TO_STRING.parse(end2));
				
				String start3 = "1970-01-01 "+t.getStart3()+":00"; //1970-01-01 09:00:00
				String end3   = "1970-01-01 "+t.getEnd3()+":00";
				tb31.setValue(DATE_TO_STRING.parse(start3));
				tb32.setValue(DATE_TO_STRING.parse(end3));
				
				String start4 = "1970-01-01 "+t.getStart4()+":00"; //1970-01-01 09:00:00
				String end4   = "1970-01-01 "+t.getEnd4()+":00";
				tb41.setValue(DATE_TO_STRING.parse(start4));
				tb42.setValue(DATE_TO_STRING.parse(end4));
				
				String start5 = "1970-01-01 "+t.getStart5()+":00"; //1970-01-01 09:00:00
				String end5   = "1970-01-01 "+t.getEnd5()+":00";
				tb51.setValue(DATE_TO_STRING.parse(start5));
				tb52.setValue(DATE_TO_STRING.parse(end5));
				
				String start6 = "1970-01-01 "+t.getStart6()+":00"; //1970-01-01 09:00:00
				String end6   = "1970-01-01 "+t.getEnd6()+":00";
				tb61.setValue(DATE_TO_STRING.parse(start6));
				tb62.setValue(DATE_TO_STRING.parse(end6));
				


			} else {
				//此处添加相对时间初始化奇数位开始偶数为结束			
				Date d = new Date();
				d.setHours(0);
				d.setMinutes(0);
				Date de=new Date();
				de.setHours(23);
				de.setMinutes(59);
				tb01.setValue(d);
				tb02.setValue(de);
				tb11.setValue(d);
				tb12.setValue(de);
				tb21.setValue(d);
				tb22.setValue(de);
				tb31.setValue(d);
				tb32.setValue(de);
				tb41.setValue(d);
				tb42.setValue(de);
				tb51.setValue(d);
				tb52.setValue(de);
				tb61.setValue(d);
				tb62.setValue(de);
				description.setValue("");
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	@SuppressWarnings("deprecation")
	public void onApply(Event event) throws Exception {
		ArrayList<String> abTaskList = (ArrayList<String>)editPerTask.getAttribute("abTaskList");
		ArrayList<String> perTaskList = (ArrayList<String>)editPerTask.getAttribute("perTaskList");
		ArrayList<String> reTaskList = (ArrayList<String>)editPerTask.getAttribute("reTaskList");
		if(perTaskList == null){
			abTaskList = new ArrayList<String>();
			perTaskList = new ArrayList<String>();
			reTaskList = new ArrayList<String>();
		}
		String nameTextboxValue = nameTextbox.getValue().trim();
		
		if ("".equals(nameTextboxValue)) {
			Messagebox.show("任务计划名称不能为空！", "提示", Messagebox.OK, Messagebox.INFORMATION);
			nameTextbox.focus();
			return;
		} else if (((String) editPerTask.getAttribute("flag")).equals("add") && perTaskList.contains(nameTextboxValue)) {
			Messagebox.show("该名称已经存在,请重新输入！", "提示", Messagebox.OK, Messagebox.INFORMATION);
			nameTextbox.focus();
			return;
		} else if (((String)editPerTask.getAttribute("flag")).equals("add") && (abTaskList.contains(nameTextboxValue) || reTaskList.contains(nameTextboxValue))){
			try{
				Messagebox.show("该名称在其他任务中已经存在,请重新输入！", "提示", Messagebox.OK, Messagebox.INFORMATION);
			}catch(Exception e){}
			nameTextbox.focus();
			return;
		}
		if(cb0.isChecked()&&(tb01.getValue().compareTo(tb02.getValue()) >= 0)) {Messagebox.show("结束时间要大于开始时间", "提示", Messagebox.OK, Messagebox.INFORMATION); return;}
		if(cb1.isChecked()&&(tb11.getValue().compareTo(tb12.getValue()) >= 0)) {Messagebox.show("结束时间要大于开始时间", "提示", Messagebox.OK, Messagebox.INFORMATION); return;}
		if(cb2.isChecked()&&(tb21.getValue().compareTo(tb22.getValue()) >= 0)) {Messagebox.show("结束时间要大于开始时间", "提示", Messagebox.OK, Messagebox.INFORMATION); return;}
		if(cb3.isChecked()&&(tb31.getValue().compareTo(tb32.getValue()) >= 0)) {Messagebox.show("结束时间要大于开始时间", "提示", Messagebox.OK, Messagebox.INFORMATION); return;}
		if(cb4.isChecked()&&(tb41.getValue().compareTo(tb42.getValue()) >= 0)) {Messagebox.show("结束时间要大于开始时间", "提示", Messagebox.OK, Messagebox.INFORMATION); return;}
		if(cb5.isChecked()&&(tb51.getValue().compareTo(tb52.getValue()) >= 0)) {Messagebox.show("结束时间要大于开始时间", "提示", Messagebox.OK, Messagebox.INFORMATION); return;}
		if(cb6.isChecked()&&(tb61.getValue().compareTo(tb62.getValue()) >= 0)) {Messagebox.show("结束时间要大于开始时间", "提示", Messagebox.OK, Messagebox.INFORMATION); return;}
		
//		if ((tb01.getValue().compareTo(tb02.getValue()) >= 0
//				|| tb11.getValue().compareTo(tb12.getValue()) >= 0
//				|| tb21.getValue().compareTo(tb22.getValue()) >= 0
//				|| tb31.getValue().compareTo(tb32.getValue()) >= 0
//				|| tb41.getValue().compareTo(tb42.getValue()) >= 0
//				|| tb51.getValue().compareTo(tb52.getValue()) >= 0
//				|| tb61.getValue().compareTo(tb62.getValue()) >= 0)) {
//			Messagebox.show("结束时间要大于开始时间"); return;
//		}
		else {
			Task t = new Task();
			TaskPack tp = new TaskPack();
			t.setType("2");
			t.setName(nameTextbox.getValue().trim());//bug
			t.setDescription(description.getValue());
			t.setAllow0(cb0.isChecked() ? "1" : "0");
			t.setAllow1(cb1.isChecked() ? "1" : "0");
			t.setAllow2(cb2.isChecked() ? "1" : "0");
			t.setAllow3(cb3.isChecked() ? "1" : "0");
			t.setAllow4(cb4.isChecked() ? "1" : "0");
			t.setAllow5(cb5.isChecked() ? "1" : "0");
			t.setAllow6(cb6.isChecked() ? "1" : "0");
			
			t.setStart0(tb01.getValue().getHours()+":"+tb01.getValue().getMinutes());	
			t.setStart1(tb11.getValue().getHours()+":"+tb11.getValue().getMinutes());
			t.setStart2(tb21.getValue().getHours()+":"+tb21.getValue().getMinutes());
			t.setStart3(tb31.getValue().getHours()+":"+tb31.getValue().getMinutes());	
			t.setStart4(tb41.getValue().getHours()+":"+tb41.getValue().getMinutes());
			t.setStart5(tb51.getValue().getHours()+":"+tb51.getValue().getMinutes());
			t.setStart6(tb61.getValue().getHours()+":"+tb61.getValue().getMinutes());	

			t.setEnd0(tb02.getValue().getHours()+":"+tb02.getValue().getMinutes());	
			t.setEnd1(tb12.getValue().getHours()+":"+tb12.getValue().getMinutes());	
			t.setEnd2(tb22.getValue().getHours()+":"+tb22.getValue().getMinutes());	
			t.setEnd3(tb32.getValue().getHours()+":"+tb32.getValue().getMinutes());	
			t.setEnd4(tb42.getValue().getHours()+":"+tb42.getValue().getMinutes());	
			t.setEnd5(tb52.getValue().getHours()+":"+tb52.getValue().getMinutes());	
			t.setEnd6(tb62.getValue().getHours()+":"+tb62.getValue().getMinutes());	
			
			tp.createTask(t);

		}
		View view = Toolkit.getToolkit().getSvdbView(event.getTarget().getDesktop());
		String loginname = view.getLoginName();
		
		if(editPerTask.getAttribute("flag").equals("edit"))//编辑部分
		{
			String minfo=loginname+" "+"在"+OpObjectId.time_task.name+"中进行了  "+OpTypeId.edit.name+"操作, 编辑项为:"+nameTextbox.getValue();
			AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.edit, OpObjectId.time_task);		
			Session session = this.session;
			session.setAttribute(TaskConstant.edit_period_section,nameTextboxValue);
		}else{	//添加											
			String minfo=loginname+" "+"在"+OpObjectId.time_task.name+"中进行了  "+OpTypeId.add.name+"操作，添加项为:"+nameTextbox.getValue();
			AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.add, OpObjectId.time_task);		
			Session session = this.session;
			session.setAttribute(TaskConstant.add_period_section,nameTextboxValue);
		}	
		String targetUrl = "/main/setting/taskperiod.zul";
		eccBody = (Include) (this.desktop.getPage("eccmain")
				.getFellow("eccBody"));
		eccBody.setSrc(null);
		eccBody.setSrc(targetUrl);
		editPerTask.detach();

	}
}
