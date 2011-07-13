/**
 * 
 */
package com.siteview.ecc.tasks;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Include;
import org.zkoss.zul.Listitem;
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
 * 添加和编辑绝对任务计划公用这个页面
 */
public class EditAbTask extends GenericForwardComposer {

	private Checkbox cb0;
	private Checkbox cb1;
	private Checkbox cb2;
	private Checkbox cb3;
	private Checkbox cb4;
	private Checkbox cb5;
	private Checkbox cb6;
	private Timebox tb0;
	private Timebox tb1;
	private Timebox tb2;
	private Timebox tb3;
	private Timebox tb4;
	private Timebox tb5;
	private Timebox tb6;
	private Textbox nameTextbox;
	private Textbox description;
	private Window editAbTask;
	private Include eccBody;
	private SimpleDateFormat DATE_TO_STRING = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
	@SuppressWarnings("deprecation")
	public void onInit() throws Exception {
		try{
			if (editAbTask.getAttribute("flag").equals("edit")) {
				TaskPack tp = new TaskPack();
				Task t = tp.findByName((String) editAbTask.getAttribute("itemId"));
				
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
				tb0.setValue(DATE_TO_STRING.parse(t.getStart0()));
				tb1.setValue(DATE_TO_STRING.parse(t.getStart1()));
				tb2.setValue(DATE_TO_STRING.parse(t.getStart2()));
				tb3.setValue(DATE_TO_STRING.parse(t.getStart3()));
				tb4.setValue(DATE_TO_STRING.parse(t.getStart4()));
				tb5.setValue(DATE_TO_STRING.parse(t.getStart5()));
				tb6.setValue(DATE_TO_STRING.parse(t.getStart6()));
				

			} else {
				Date d = new Date();
				tb0.setValue(d);
				tb1.setValue(d);
				tb2.setValue(d);
				tb3.setValue(d);
				tb4.setValue(d);
				tb5.setValue(d);
				tb6.setValue(d);
				description.setValue("");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void onApply(Event event) throws Exception {
		ArrayList<String> abTaskList = (ArrayList<String>)editAbTask.getAttribute("abTaskList");
		ArrayList<String> perTaskList = (ArrayList<String>)editAbTask.getAttribute("perTaskList");
		ArrayList<String> reTaskList = (ArrayList<String>)editAbTask.getAttribute("reTaskList");
		if(perTaskList == null){
			abTaskList = new ArrayList<String>();
			perTaskList = new ArrayList<String>();
			reTaskList = new ArrayList<String>();
		}
		String nameTextboxValue = nameTextbox.getValue().trim();
		
	    if ("".equals(nameTextboxValue)){
			Messagebox.show("任务计划名称不能为空！", "提示", Messagebox.OK, Messagebox.INFORMATION);
			nameTextbox.focus();
			return;
		} 
		if(((String)editAbTask.getAttribute("flag")).equals("add")&&(abTaskList.contains(nameTextboxValue)))
		{
			Messagebox.show("该名称已经存在,请重新输入！", "提示", Messagebox.OK, Messagebox.INFORMATION);
			nameTextbox.focus();
			return;
		}else if(((String)editAbTask.getAttribute("flag")).equals("add") && (perTaskList.contains(nameTextboxValue) || reTaskList.contains(nameTextboxValue))){
			Messagebox.show("该名称在其他任务中已经存在,请重新输入！", "提示", Messagebox.OK, Messagebox.INFORMATION);
			nameTextbox.focus();
			return;
		}else{
			Task t = new Task();
			TaskPack tp = new TaskPack();
			t.setType("1");
			t.setName(nameTextboxValue);
			t.setDescription(description.getValue());
			t.setAllow0(cb0.isChecked() ? "1" : "0");
			t.setAllow1(cb1.isChecked() ? "1" : "0");
			t.setAllow2(cb2.isChecked() ? "1" : "0");
			t.setAllow3(cb3.isChecked() ? "1" : "0");
			t.setAllow4(cb4.isChecked() ? "1" : "0");
			t.setAllow5(cb5.isChecked() ? "1" : "0");
			t.setAllow6(cb6.isChecked() ? "1" : "0");
			t.setStart0(DATE_TO_STRING.format(tb0.getValue()));			
			t.setStart1(DATE_TO_STRING.format(tb1.getValue()));
			t.setStart2(DATE_TO_STRING.format(tb2.getValue()));
			t.setStart3(DATE_TO_STRING.format(tb3.getValue()));
			t.setStart4(DATE_TO_STRING.format(tb4.getValue()));
			t.setStart5(DATE_TO_STRING.format(tb5.getValue()));
			t.setStart6(DATE_TO_STRING.format(tb6.getValue()));
			tp.createTask(t);
			
		}
		View view = Toolkit.getToolkit().getSvdbView(event.getTarget().getDesktop());
		String loginname = view.getLoginName();
		if(editAbTask.getAttribute("flag").equals("edit"))//编辑部分
		{
			String minfo=loginname+" "+"在"+OpObjectId.absolute_task.name+"中进行了  "+OpTypeId.edit.name+"操作, 编辑项为:"+nameTextbox.getValue();
			AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.edit, OpObjectId.absolute_task);	
			Session session = this.session;
			session.setAttribute(TaskConstant.edit_absolutetask_section,nameTextboxValue);

		}else{	//添加											
			String minfo=loginname+" "+"在"+OpObjectId.absolute_task.name+"中进行了  "+OpTypeId.add.name+"操作，添加项为:"+nameTextbox.getValue();
			AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.add, OpObjectId.absolute_task);		
			Session session = this.session;
			session.setAttribute(TaskConstant.add_absolutetask_section,nameTextboxValue);
		}

		String targetUrl = "/main/setting/taskabsolute.zul";
		eccBody = (Include) (this.desktop.getPage("eccmain")
				.getFellow("eccBody"));
		eccBody.setSrc(null);
		eccBody.setSrc(targetUrl);
		editAbTask.detach();
	}
}
