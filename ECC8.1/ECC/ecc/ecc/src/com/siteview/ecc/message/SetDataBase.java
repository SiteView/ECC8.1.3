/**
 * 
 */
package com.siteview.ecc.message;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericAutowireComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.siteview.base.manage.View;
import com.siteview.ecc.email.IniFilePack;
import com.siteview.ecc.log.AppendOperateLog;
import com.siteview.ecc.log.OpObjectId;
import com.siteview.ecc.log.OpTypeId;
import com.siteview.ecc.util.Toolkit;

/**
 * @author yuandong 数据库存取操作数据结构如下
 * 数据库类型是写死在页面上的
 * PramsCount参数个数
 * insertway 0和1代表不同插入方式
 * 当为0时可以有返回值参数
 * 包含
 * PorcName
PramDesc0
PramDesc1
PramDesc2
PramLength0
PramLength1
PramLength2
PramName0
PramName1
PramName2
PramType0
PramType1
PramType2
返回值参数为
PorcType
PorcLengt
PorcDes
当为1时不可以有返回值参数
同时
多了TableName
少了PorcName
 */
public class SetDataBase extends GenericAutowireComposer {
	private final static Logger logger = Logger.getLogger(SetDataBase.class);

	private Radio nomalInsert, storedInsert;
	private Combobox dbTypeCombobox;
	private Textbox dbTableName, storedName, connectString, returnValue;

	private String flag;
	private Row row1, row2;
	private Hbox hbox1;
	private Window setDbWin;
	private Listbox param;
	private Button applyButton;
	private Button recoverButton;
	private Button onMoreButton;

	public void onInit() throws Exception{
		IniFilePack ini = new IniFilePack("smsconfig.ini");
		try{
			ini.load();
		}catch(Exception e){}
		if(ini.getSectionData("DataBaseConfig") == null){
			ini.createSection("DataBaseConfig");
			ini.setKeyValue("DataBaseConfig", "InsertWay", "1");
			ini.setKeyValue("DataBaseConfig", "DataBaseType", "Oracle");
			ini.setKeyValue("DataBaseConfig", "PramsCount", "2");
			ini.setKeyValue("DataBaseConfig", "PramName0", "smsPhones");
			ini.setKeyValue("DataBaseConfig", "PramType0", "String");
			ini.setKeyValue("DataBaseConfig", "PramLength0", "150");
			ini.setKeyValue("DataBaseConfig", "PramDesc0", "手机号码");
			ini.setKeyValue("DataBaseConfig", "PramName1", "context");
			ini.setKeyValue("DataBaseConfig", "PramType1", "String");
			ini.setKeyValue("DataBaseConfig", "PramLength1", "150");
			ini.setKeyValue("DataBaseConfig", "PramDesc1", "短信内容");
			ini.saveChange();
			ini.load();//重新加载
		}
		if (ini.getValue("DataBaseConfig", "DataBaseType") != null){
			dbTypeCombobox.setValue(ini.getValue("DataBaseConfig","DataBaseType"));
		}
		if (ini.getValue("DataBaseConfig", "ConnectionString") != null){
			connectString.setValue(ini.getValue("DataBaseConfig","ConnectionString"));
		}
		if (ini.getValue("DataBaseConfig", "TableName") != null){
			dbTableName.setValue(ini.getValue("DataBaseConfig", "TableName"));
		}
		if (ini.getValue("DataBaseConfig", "PorcName") != null){
			storedName.setValue(ini.getValue("DataBaseConfig", "PorcName"));
		}
		// 此处加上参数列表迭代显示
		int count = 0;
		if (ini.getValue("DataBaseConfig", "PramsCount") != null){
			count = Integer.valueOf(ini.getValue("DataBaseConfig", "PramsCount"));
		}
		setDbWin.setAttribute("isExist", " ");
		
		while (param.getChildren().size() != 1) {// 每次载入页面刷新listbox保留listheader
			param.getChildren().remove(1);
		}
		for (int i = 0; i < count; i++) {
			Listitem li = new Listitem();
			Listcell lc1 = new Listcell();
			
			Listcell lc2 = new Listcell();
			Listcell lc3 = new Listcell();
			Listcell lc4 = new Listcell();
			Textbox t1 = new Textbox();
			t1.setHeight("15px");
			t1.setWidth("80px");
			Combobox cb1 = myComboBox("String", "Int", "DateTime");
			cb1.setHeight("15px");
			cb1.setWidth("80px");
			Intbox t2 = new Intbox();
			t2.setHeight("15px");
			t2.setWidth("80px");
			Combobox cb2 = myComboBox("手机号码", "短信内容", "发送时间");
			cb2.setHeight("15px");
			cb2.setWidth("80px");
			// 取值
			if (ini.getValue("DataBaseConfig", "PramName" + String.valueOf(i)) != null)
				t1.setValue(ini.getValue("DataBaseConfig", "PramName"
						+ String.valueOf(i)));
			if (ini.getValue("DataBaseConfig", "PramType" + String.valueOf(i)) != null)
				cb1.setValue(ini.getValue("DataBaseConfig", "PramType"
						+ String.valueOf(i)));
			if (ini
					.getValue("DataBaseConfig", "PramLength"
							+ String.valueOf(i)) != null)
				t2.setValue(Integer.valueOf(ini.getValue("DataBaseConfig",
						"PramLength" + String.valueOf(i))));
			if (ini.getValue("DataBaseConfig", "PramDesc" + String.valueOf(i)) != null)
				cb2.setValue(ini.getValue("DataBaseConfig", "PramDesc"
						+ String.valueOf(i)));
			t1.addEventListener("onChanging", new ItemClickListener());
			t1.setParent(lc1);
			cb1.setParent(lc2);
			cb1.addEventListener("onChange", new ItemClickListener());
			t2.setParent(lc3);
			t2.addEventListener("onChanging", new ItemClickListener());
			cb2.setParent(lc4);
			cb2.addEventListener("onChange", new ItemClickListener());
			lc1.setParent(li);
			lc2.setParent(li);
			lc3.setParent(li);
			lc4.setParent(li);
			li.setParent(param);
		}
		if (ini.getValue("DataBaseConfig", "InsertWay") != null){
			if (ini.getValue("DataBaseConfig", "InsertWay").equals("1")) {
				nomalInsert.setChecked(true);
				row1.setVisible(false);
				row2.setVisible(false);
				hbox1.setVisible(true);
				flag = "nomal";
			} else {
				storedInsert.setChecked(true);
				row1.setVisible(true);
				row2.setVisible(true);
				hbox1.setVisible(false);
				flag = "stored";
			}
		}else{ //在初始化的情况下  DataBaseConfig InsertWay 都是不存在的
			nomalInsert.setChecked(true);
			row1.setVisible(false);
			row2.setVisible(false);
			hbox1.setVisible(true);
			flag = "nomal";
		}

		if (ini.getValue("DataBaseConfig", "InsertWay") != null)
			if (ini.getKeyList("DataBaseConfig").contains("PorcType")) {
				returnValue
						.setValue("参数类型："
								+ ini.getValue("DataBaseConfig", "PorcType")
								+ "；  参数长度"
								+ ini.getValue("DataBaseConfig", "PorcLength")
								+ "；  参数描述"
								+ ini.getValue("DataBaseConfig", "PorcDes"));
				setDbWin.setAttribute("porcLength", ini.getValue(
						"DataBaseConfig", "PorcLength"));
				setDbWin.setAttribute("porcType", ini.getValue(
						"DataBaseConfig", "PorcType"));
				setDbWin.setAttribute("porcInfo", ini.getValue(
						"DataBaseConfig", "PorcDes"));
			}
	}

	public void onDel(Event event){
		// 该删除只是删除listbox中的现实数据，不修改ini
		if (param.getSelectedCount() == 0) {
			try{
				Messagebox.show("请选择要删除的参数！", "提示", Messagebox.OK, Messagebox.INFORMATION);
			}catch(Exception e){}
			return;
		}
		try{
			if (Messagebox.show("确定要删除所选参数吗？", "询问", Messagebox.OK
					| Messagebox.CANCEL, Messagebox.QUESTION) == Messagebox.OK) {
				Set s = param.getSelectedItems();
				Object[] arr = s.toArray();
				for (int i = 0; i < arr.length; i++)
					param.removeChild((Component) arr[i]);
			}
			View view = Toolkit.getToolkit().getSvdbView(
					event.getTarget().getDesktop());
			String loginname = view.getLoginName();
			String minfo = loginname + " " + "在" + OpObjectId.sms_set.name
					+ "中进行了  " + OpTypeId.del.name + "操作，在数据库中删除了配置信息。 ";
			AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.del,
					OpObjectId.sms_set);

			applyButton.setDisabled(false);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void onAdd(Event event)  throws Exception{
		// 该添加只是添加listbox中的现实数据，不修改ini
		if (param.getChildren().size() >= 4)
			try{
				Messagebox.show("参数不能多于三个", "提示", Messagebox.OK, Messagebox.INFORMATION);
			}catch(Exception e){}
			
		else {
			Listitem li = new Listitem();
			Listcell lc1 = new Listcell();
			Listcell lc2 = new Listcell();
			Listcell lc3 = new Listcell();
			Listcell lc4 = new Listcell();
			Textbox t1 = new Textbox();
			t1.setHeight("15px");
			t1.setWidth("80px");
			Combobox cb1 = myComboBox("String", "Int", "DateTime");
		    cb1.setSelectedIndex(2);
			//设置默认的参数
			cb1.setHeight("15px");
			cb1.setWidth("80px");
			Intbox t2 = new Intbox();
			t2.setHeight("15px");
			t2.setWidth("80px");
			Combobox cb2 = myComboBox("手机号码", "短信内容", "发送时间");
			cb2.setSelectedIndex(2);
			//设置默认的参数
			cb2.setHeight("15px");
			cb2.setWidth("80px");
			lc1.appendChild(t1);
			cb1.setParent(lc2);
			t2.setParent(lc3);
			cb2.setParent(lc4);
			lc1.setParent(li);
			lc2.setParent(li);
			lc3.setParent(li);
			lc4.setParent(li);
			li.setParent(param);
			li.setFocus(true);
			View view = Toolkit.getToolkit().getSvdbView(
					event.getTarget().getDesktop());
			String loginname = view.getLoginName();
			String minfo = loginname + " " + "在" + OpObjectId.sms_set.name
					+ "中进行了  " + OpTypeId.add.name + "操作，在数据库中添加了配置信息。 ";
			AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.add,
					OpObjectId.sms_set);
		}
		applyButton.setDisabled(false);
	}

	public void onApply() throws Exception{
		// 先删除以前的信息再存储
		IniFilePack ini = new IniFilePack("smsconfig.ini");
		try{
			ini.load();
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			if (ini.getSectionList().contains("DataBaseConfig"))
					ini.deleteSection("DataBaseConfig");
					ini.createSection("DataBaseConfig");
					ini.setKeyValue("DataBaseConfig", "DataBaseType", dbTypeCombobox
					.getSelectedItem().getLabel());
			
			if (connectString.getValue() == null || connectString.getValue().trim().equals("")) {
				try{
					Messagebox.show("连接字符串不能为空！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				}catch(Exception e){}
				return;
			}
			ini.setKeyValue("DataBaseConfig", "ConnectionString", connectString.getValue().trim());
			// 此处加上参数列表迭代存储
			List<?> l = param.getChildren();
			if (l.size() > 4 || l.size() < 3) {
				try{
					Messagebox.show("参数的个数只能是2个或3个！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				}catch(Exception e){}
				return;
			}
			ArrayList<String>[] al = new ArrayList[4];
			for (int i = 0; i < 4; i++) {
				al[i] = new ArrayList();
			}
			Iterator<?> it = l.iterator();
			it.next();
			int i = 0;
			for (; it.hasNext(); i++) {
				// 取到所有值并且赋值到ArrayList<String>以便于进行输入验证和存储到ini
				Listitem li = (Listitem) it.next();
				List<?> l1 = li.getChildren();// 得到该行
				if (!(((Textbox) (((Listcell) l1.get(0)).getChildren()).get(0))
						.getValue()).equals(""))
					al[0].add(((Textbox) (((Listcell) l1.get(0)).getChildren())
							.get(0)).getValue());
				else {
					try{
						Messagebox.show("参数的名称不能为空！");
					}catch(Exception e){}
					return;
				}
				al[1].add(((Textbox) (((Listcell) l1.get(1)).getChildren()).get(0)).getValue());
				if ((((Intbox) (((Listcell) l1.get(2)).getChildren()).get(0)).getValue()) != null)
					al[2].add(String.valueOf(((Intbox) (((Listcell) l1.get(2)).getChildren()).get(0)).getValue()));
				else {
					try{
						Messagebox.show("参数的长度不能为空！", "提示", Messagebox.OK, Messagebox.INFORMATION);
					}catch(Exception e){}
					return;
				}
				al[3].add(((Textbox) (((Listcell) l1.get(3)).getChildren()).get(0)).getValue());
			}
			// 下面开始验证参数含义不能相同；发送时间必须是datetime类型，参数含义中必有两个是手机号码和短信内容
			for (int j = 0; j < i; j++) {
				if (al[3].get(j).equals("发送时间") && !al[1].get(j).equals("DateTime")) {
					try{
						Messagebox.show("发送时间只能是DateTime类型！", "提示", Messagebox.OK, Messagebox.INFORMATION);
					}catch(Exception e){}
					return;
				}
			}
			if (!al[3].contains("手机号码")) {
				try{
					Messagebox.show("参数中必须包含手机号码！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				}catch(Exception e){}
				return;
			} else if (!al[3].contains("短信内容")) {
				try{
					Messagebox.show("参数中必须包含短信内容！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				}catch(Exception e){}
				return;
			}
			if (al[3].get(0).equals(al[3].get(1))) {
				try{
					Messagebox.show("参数中不同包含相同的项！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				}catch(Exception e){}
				return;
			}
			if (i == 3) {
				if (al[3].get(0).equals(al[3].get(2))
						|| al[3].get(1).equals(al[3].get(2))) {
					try{
						Messagebox.show("参数中不同包含相同的项！", "提示", Messagebox.OK, Messagebox.INFORMATION);
					}catch(Exception e){}
					return;
				}
			}
			for (int k = 0; k < i; k++) {// 通过验证后存储
				ini.setKeyValue("DataBaseConfig", "PramName" + String.valueOf(k),
						al[0].get(k));
				ini.setKeyValue("DataBaseConfig", "PramType" + String.valueOf(k),
						al[1].get(k));
				ini.setKeyValue("DataBaseConfig", "PramLength" + String.valueOf(k),
						al[2].get(k));
				ini.setKeyValue("DataBaseConfig", "PramDesc" + String.valueOf(k),
						al[3].get(k));
			}
			ini.setKeyValue("DataBaseConfig", "PramsCount", i);
			if (nomalInsert.isChecked()) {
				flag = "nomal";
				ini.setKeyValue("DataBaseConfig", "InsertWay", "1");
				if(dbTableName.getValue() == null || dbTableName.getValue().trim().equals(""))
				{
					try{
						Messagebox.show("表名不能为空！", "提示", Messagebox.OK, Messagebox.INFORMATION);
					}catch(Exception e){}
					return;
				}
				ini.setKeyValue("DataBaseConfig", "TableName", dbTableName.getValue().trim());				
			} else {
				flag = "stored";
				ini.setKeyValue("DataBaseConfig", "InsertWay", "0");
				if (storedName.getValue() == null || storedName.getValue().trim().equals("")) {
					try{
						Messagebox.show("存储过程名称不能为空！", "提示", Messagebox.OK, Messagebox.INFORMATION);
					}catch(Exception e){}
					return;
				} 
				ini.setKeyValue("DataBaseConfig", "PorcName", storedName.getValue().trim());
				if (((String) setDbWin.getAttribute("isExist")).equals("yes")) {
					ini.setKeyValue("DataBaseConfig", "PorcLength",(String) setDbWin.getAttribute("porcLength"));
					ini.setKeyValue("DataBaseConfig", "PorcType",(String) setDbWin.getAttribute("porcType"));
					ini.setKeyValue("DataBaseConfig", "PorcDes",(String) setDbWin.getAttribute("porcInfo"));
				}
			}
			ini.saveChange();
		}catch(Exception e){
			e.printStackTrace();
		}

		applyButton.setDisabled(true);
		//recoverButton.setDisabled(false);
	}
	
	public void onRecover() throws Exception{
		IniFilePack ini = new IniFilePack("smsconfig.ini");
		try{
			ini.load();
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			if (ini.getValue("DataBaseConfig", "DataBaseType") != null)

				dbTypeCombobox.setValue(ini.getValue("DataBaseConfig",
						"DataBaseType"));
			if (ini.getValue("DataBaseConfig", "ConnectionString") != null)
				connectString.setValue(ini.getValue("DataBaseConfig",
						"ConnectionString"));
			if (ini.getValue("DataBaseConfig", "TableName") != null)
				dbTableName.setValue(ini.getValue("DataBaseConfig", "TableName"));
			if (ini.getValue("DataBaseConfig", "PorcName") != null)
				storedName.setValue(ini.getValue("DataBaseConfig", "PorcName"));
			setDbWin.setAttribute("isExist", " ");

			// 此处加上参数列表迭代显示
			int count = 0;
			if (ini.getValue("DataBaseConfig", "PramsCount") != null)
				count = Integer.valueOf(ini
						.getValue("DataBaseConfig", "PramsCount"));

			while (param.getChildren().size() != 1) {// 每次载入页面刷新listbox保留listheader
				param.getChildren().remove(1);

			}
			for (int i = 0; i < count; i++) {
				Listitem li = new Listitem();
				Listcell lc1 = new Listcell();
				
				Listcell lc2 = new Listcell();
				Listcell lc3 = new Listcell();
				Listcell lc4 = new Listcell();
				Textbox t1 = new Textbox();
				t1.setHeight("15px");
				t1.setWidth("80px");
				Combobox cb1 = myComboBox("String", "Int", "DateTime");
				cb1.setHeight("15px");
				cb1.setWidth("80px");
				Intbox t2 = new Intbox();
				t2.setHeight("15px");
				t2.setWidth("80px");
				Combobox cb2 = myComboBox("手机号码", "短信内容", "发送时间");
				cb2.setHeight("15px");
				cb2.setWidth("80px");
				// 取值
				if (ini.getValue("DataBaseConfig", "PramName" + String.valueOf(i)) != null)
					t1.setValue(ini.getValue("DataBaseConfig", "PramName"
							+ String.valueOf(i)));
				if (ini.getValue("DataBaseConfig", "PramType" + String.valueOf(i)) != null)
					cb1.setValue(ini.getValue("DataBaseConfig", "PramType"
							+ String.valueOf(i)));
				if (ini
						.getValue("DataBaseConfig", "PramLength"
								+ String.valueOf(i)) != null)
					t2.setValue(Integer.valueOf(ini.getValue("DataBaseConfig",
							"PramLength" + String.valueOf(i))));
				if (ini.getValue("DataBaseConfig", "PramDesc" + String.valueOf(i)) != null)
					cb2.setValue(ini.getValue("DataBaseConfig", "PramDesc"
							+ String.valueOf(i)));
				t1.addEventListener("onChanging", new ItemClickListener());
				t1.setParent(lc1);
				cb1.setParent(lc2);
				cb1.addEventListener("onChange", new ItemClickListener());
				t2.setParent(lc3);
				t2.addEventListener("onChanging", new ItemClickListener());
				cb2.setParent(lc4);
				cb2.addEventListener("onChange", new ItemClickListener());
				lc1.setParent(li);
				lc2.setParent(li);
				lc3.setParent(li);
				lc4.setParent(li);
				li.setParent(param);
			}
			if (ini.getValue("DataBaseConfig", "InsertWay") != null)
				if (ini.getValue("DataBaseConfig", "InsertWay").equals("1")) {
					nomalInsert.setChecked(true);
					row1.setVisible(false);
					row2.setVisible(false);
					hbox1.setVisible(true);
					flag = "nomal";
				} else {
					storedInsert.setChecked(true);
					row1.setVisible(true);
					row2.setVisible(true);
					hbox1.setVisible(false);
					flag = "stored";
				}
			if (ini.getValue("DataBaseConfig", "InsertWay") != null)
				if (ini.getKeyList("DataBaseConfig").contains("PorcType")) {
					returnValue
							.setValue("参数类型："
									+ ini.getValue("DataBaseConfig", "PorcType")
									+ "；  参数长度"
									+ ini.getValue("DataBaseConfig", "PorcLength")
									+ "；  参数描述"
									+ ini.getValue("DataBaseConfig", "PorcDes"));
					setDbWin.setAttribute("porcLength", ini.getValue(
							"DataBaseConfig", "PorcLength"));
					setDbWin.setAttribute("porcType", ini.getValue(
							"DataBaseConfig", "PorcType"));
					setDbWin.setAttribute("porcInfo", ini.getValue(
							"DataBaseConfig", "PorcDes"));
				}
		}catch(Exception e){
			e.printStackTrace();
		}
		//applyButton.setDisabled(false);
		recoverButton.setDisabled(true);
	}

	public void onMore()  {
		try{
			IniFilePack ini = new IniFilePack("smsconfig.ini");
			ini.load();
			final Window win = (Window) Executions.createComponents(
					"/main/setting/paramedit.zul", null, null);
			if (ini.getKeyList("DataBaseConfig") != null){//配置文件中不含有这个键值
				logger.info("not null");
				if (ini.getKeyList("DataBaseConfig").contains("PorcType")) {
					win.setAttribute("isExist", "yes");// 如果存在该key则isexist=yes
					win.setAttribute("porcLength", ini.getValue("DataBaseConfig",
							"PorcLength"));
					win.setAttribute("porcType", ini.getValue("DataBaseConfig",
							"PorcType"));
					win.setAttribute("porcInfo", ini.getValue("DataBaseConfig",
							"PorcDes"));
				}else{
					win.setAttribute("isExist", "no");// 有返回值则isexist=yes
				}
			
			}else{
				logger.info("is null");
				win.setAttribute("isExist", "no");// 有返回值则isexist=yes
			}
			 
			win.setTitle("编辑返回值");
//			win.setMaximizable(true);
			win.doModal();
			applyButton.setDisabled(false);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private Combobox myComboBox(String s1, String s2, String s3) {
		//三个item的combox类
		Combobox l = new Combobox();
		// public ParamCombobox(String s1,String s2,String s3){
		// l.setMold("select");
		Comboitem li1 = new Comboitem(s1);
		Comboitem li2 = new Comboitem(s2);
		Comboitem li3 = new Comboitem(s3);
		li1.setParent(l);
		li2.setParent(l);
		li3.setParent(l);
		l.setReadonly(true);
		return l;

	}

	class ItemClickListener implements org.zkoss.zk.ui.event.EventListener {
		@Override
		public void onEvent(Event arg0) throws Exception {
			// TODO Auto-generated method stub
			applyButton.setDisabled(false);
		}
	}
}
