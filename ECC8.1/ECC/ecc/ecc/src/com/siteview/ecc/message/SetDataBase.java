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
 * @author yuandong ���ݿ��ȡ�������ݽṹ����
 * ���ݿ�������д����ҳ���ϵ�
 * PramsCount��������
 * insertway 0��1����ͬ���뷽ʽ
 * ��Ϊ0ʱ�����з���ֵ����
 * ����
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
����ֵ����Ϊ
PorcType
PorcLengt
PorcDes
��Ϊ1ʱ�������з���ֵ����
ͬʱ
����TableName
����PorcName
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
			ini.setKeyValue("DataBaseConfig", "PramDesc0", "�ֻ�����");
			ini.setKeyValue("DataBaseConfig", "PramName1", "context");
			ini.setKeyValue("DataBaseConfig", "PramType1", "String");
			ini.setKeyValue("DataBaseConfig", "PramLength1", "150");
			ini.setKeyValue("DataBaseConfig", "PramDesc1", "��������");
			ini.saveChange();
			ini.load();//���¼���
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
		// �˴����ϲ����б������ʾ
		int count = 0;
		if (ini.getValue("DataBaseConfig", "PramsCount") != null){
			count = Integer.valueOf(ini.getValue("DataBaseConfig", "PramsCount"));
		}
		setDbWin.setAttribute("isExist", " ");
		
		while (param.getChildren().size() != 1) {// ÿ������ҳ��ˢ��listbox����listheader
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
			Combobox cb2 = myComboBox("�ֻ�����", "��������", "����ʱ��");
			cb2.setHeight("15px");
			cb2.setWidth("80px");
			// ȡֵ
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
		}else{ //�ڳ�ʼ���������  DataBaseConfig InsertWay ���ǲ����ڵ�
			nomalInsert.setChecked(true);
			row1.setVisible(false);
			row2.setVisible(false);
			hbox1.setVisible(true);
			flag = "nomal";
		}

		if (ini.getValue("DataBaseConfig", "InsertWay") != null)
			if (ini.getKeyList("DataBaseConfig").contains("PorcType")) {
				returnValue
						.setValue("�������ͣ�"
								+ ini.getValue("DataBaseConfig", "PorcType")
								+ "��  ��������"
								+ ini.getValue("DataBaseConfig", "PorcLength")
								+ "��  ��������"
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
		// ��ɾ��ֻ��ɾ��listbox�е���ʵ���ݣ����޸�ini
		if (param.getSelectedCount() == 0) {
			try{
				Messagebox.show("��ѡ��Ҫɾ���Ĳ�����", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
			}catch(Exception e){}
			return;
		}
		try{
			if (Messagebox.show("ȷ��Ҫɾ����ѡ������", "ѯ��", Messagebox.OK
					| Messagebox.CANCEL, Messagebox.QUESTION) == Messagebox.OK) {
				Set s = param.getSelectedItems();
				Object[] arr = s.toArray();
				for (int i = 0; i < arr.length; i++)
					param.removeChild((Component) arr[i]);
			}
			View view = Toolkit.getToolkit().getSvdbView(
					event.getTarget().getDesktop());
			String loginname = view.getLoginName();
			String minfo = loginname + " " + "��" + OpObjectId.sms_set.name
					+ "�н�����  " + OpTypeId.del.name + "�����������ݿ���ɾ����������Ϣ�� ";
			AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.del,
					OpObjectId.sms_set);

			applyButton.setDisabled(false);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void onAdd(Event event)  throws Exception{
		// �����ֻ�����listbox�е���ʵ���ݣ����޸�ini
		if (param.getChildren().size() >= 4)
			try{
				Messagebox.show("�������ܶ�������", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
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
			//����Ĭ�ϵĲ���
			cb1.setHeight("15px");
			cb1.setWidth("80px");
			Intbox t2 = new Intbox();
			t2.setHeight("15px");
			t2.setWidth("80px");
			Combobox cb2 = myComboBox("�ֻ�����", "��������", "����ʱ��");
			cb2.setSelectedIndex(2);
			//����Ĭ�ϵĲ���
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
			String minfo = loginname + " " + "��" + OpObjectId.sms_set.name
					+ "�н�����  " + OpTypeId.add.name + "�����������ݿ��������������Ϣ�� ";
			AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.add,
					OpObjectId.sms_set);
		}
		applyButton.setDisabled(false);
	}

	public void onApply() throws Exception{
		// ��ɾ����ǰ����Ϣ�ٴ洢
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
					Messagebox.show("�����ַ�������Ϊ�գ�", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
				}catch(Exception e){}
				return;
			}
			ini.setKeyValue("DataBaseConfig", "ConnectionString", connectString.getValue().trim());
			// �˴����ϲ����б�����洢
			List<?> l = param.getChildren();
			if (l.size() > 4 || l.size() < 3) {
				try{
					Messagebox.show("�����ĸ���ֻ����2����3����", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
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
				// ȡ������ֵ���Ҹ�ֵ��ArrayList<String>�Ա��ڽ���������֤�ʹ洢��ini
				Listitem li = (Listitem) it.next();
				List<?> l1 = li.getChildren();// �õ�����
				if (!(((Textbox) (((Listcell) l1.get(0)).getChildren()).get(0))
						.getValue()).equals(""))
					al[0].add(((Textbox) (((Listcell) l1.get(0)).getChildren())
							.get(0)).getValue());
				else {
					try{
						Messagebox.show("���������Ʋ���Ϊ�գ�");
					}catch(Exception e){}
					return;
				}
				al[1].add(((Textbox) (((Listcell) l1.get(1)).getChildren()).get(0)).getValue());
				if ((((Intbox) (((Listcell) l1.get(2)).getChildren()).get(0)).getValue()) != null)
					al[2].add(String.valueOf(((Intbox) (((Listcell) l1.get(2)).getChildren()).get(0)).getValue()));
				else {
					try{
						Messagebox.show("�����ĳ��Ȳ���Ϊ�գ�", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
					}catch(Exception e){}
					return;
				}
				al[3].add(((Textbox) (((Listcell) l1.get(3)).getChildren()).get(0)).getValue());
			}
			// ���濪ʼ��֤�������岻����ͬ������ʱ�������datetime���ͣ����������б����������ֻ�����Ͷ�������
			for (int j = 0; j < i; j++) {
				if (al[3].get(j).equals("����ʱ��") && !al[1].get(j).equals("DateTime")) {
					try{
						Messagebox.show("����ʱ��ֻ����DateTime���ͣ�", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
					}catch(Exception e){}
					return;
				}
			}
			if (!al[3].contains("�ֻ�����")) {
				try{
					Messagebox.show("�����б�������ֻ����룡", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
				}catch(Exception e){}
				return;
			} else if (!al[3].contains("��������")) {
				try{
					Messagebox.show("�����б�������������ݣ�", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
				}catch(Exception e){}
				return;
			}
			if (al[3].get(0).equals(al[3].get(1))) {
				try{
					Messagebox.show("�����в�ͬ������ͬ���", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
				}catch(Exception e){}
				return;
			}
			if (i == 3) {
				if (al[3].get(0).equals(al[3].get(2))
						|| al[3].get(1).equals(al[3].get(2))) {
					try{
						Messagebox.show("�����в�ͬ������ͬ���", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
					}catch(Exception e){}
					return;
				}
			}
			for (int k = 0; k < i; k++) {// ͨ����֤��洢
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
						Messagebox.show("��������Ϊ�գ�", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
					}catch(Exception e){}
					return;
				}
				ini.setKeyValue("DataBaseConfig", "TableName", dbTableName.getValue().trim());				
			} else {
				flag = "stored";
				ini.setKeyValue("DataBaseConfig", "InsertWay", "0");
				if (storedName.getValue() == null || storedName.getValue().trim().equals("")) {
					try{
						Messagebox.show("�洢�������Ʋ���Ϊ�գ�", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
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

			// �˴����ϲ����б������ʾ
			int count = 0;
			if (ini.getValue("DataBaseConfig", "PramsCount") != null)
				count = Integer.valueOf(ini
						.getValue("DataBaseConfig", "PramsCount"));

			while (param.getChildren().size() != 1) {// ÿ������ҳ��ˢ��listbox����listheader
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
				Combobox cb2 = myComboBox("�ֻ�����", "��������", "����ʱ��");
				cb2.setHeight("15px");
				cb2.setWidth("80px");
				// ȡֵ
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
							.setValue("�������ͣ�"
									+ ini.getValue("DataBaseConfig", "PorcType")
									+ "��  ��������"
									+ ini.getValue("DataBaseConfig", "PorcLength")
									+ "��  ��������"
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
			if (ini.getKeyList("DataBaseConfig") != null){//�����ļ��в����������ֵ
				logger.info("not null");
				if (ini.getKeyList("DataBaseConfig").contains("PorcType")) {
					win.setAttribute("isExist", "yes");// ������ڸ�key��isexist=yes
					win.setAttribute("porcLength", ini.getValue("DataBaseConfig",
							"PorcLength"));
					win.setAttribute("porcType", ini.getValue("DataBaseConfig",
							"PorcType"));
					win.setAttribute("porcInfo", ini.getValue("DataBaseConfig",
							"PorcDes"));
				}else{
					win.setAttribute("isExist", "no");// �з���ֵ��isexist=yes
				}
			
			}else{
				logger.info("is null");
				win.setAttribute("isExist", "no");// �з���ֵ��isexist=yes
			}
			 
			win.setTitle("�༭����ֵ");
//			win.setMaximizable(true);
			win.doModal();
			applyButton.setDisabled(false);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private Combobox myComboBox(String s1, String s2, String s3) {
		//����item��combox��
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
