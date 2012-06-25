package com.siteview.ecc.report.mysql;

import java.util.Date;
import java.util.HashMap;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.ComposerExt;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.api.Listbox;

import com.siteview.actions.ImageButton;
import com.siteview.base.data.IniFile;
import com.siteview.ecc.timer.EccHeaderInfo;
import com.siteview.ecc.util.Toolkit;

public class SqlSetting extends GenericForwardComposer implements ComposerExt{
	
	Button  btnApplySetting;
	Button  btnRestoreSetting;
	
	Checkbox importToSQLDB;
	
	Textbox siteviewDS;
	Textbox svdbHistoryReadDay;
	Textbox svdbDataInit;
	Textbox svdbDataStart;
	Textbox svdbDateReadMinute;
	
	Textbox import_int;
	Textbox import_float;
	Textbox import_string;
	Textbox import_string_len;
	Textbox import_table;
	Textbox import_table_end;
	Textbox import_index;
	Textbox import_insert;
	Textbox mysql_tmp;
	Listbox thread_PRIORITY;
	
	ImageButton btnHlpShow;
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		self.setAttribute("Composer", this);
		btnApplySetting.addEventListener("onClick",new EventListener(){
			@Override
			public void onEvent(Event event) throws Exception {
				
				save();
			}});
		
		btnRestoreSetting.addEventListener("onClick",new EventListener(){
			@Override
			public void onEvent(Event event) throws Exception {
				ImportIni.getInstance().setDefault();
				refresh();
			}});

		setHelpShow(false,comp.getPage());
		btnHlpShow.setAttribute("dispHlp",Boolean.FALSE);
		btnHlpShow.setClickListener(new EventListener(){
			@Override
			public void onEvent(Event event) throws Exception {
				if(btnHlpShow.getAttribute("dispHlp").equals(Boolean.FALSE))
				{
					btnHlpShow.setAttribute("dispHlp",Boolean.TRUE);
					setHelpShow(true,event.getPage());
				}
				else
				{
					btnHlpShow.setAttribute("dispHlp",Boolean.FALSE);
					setHelpShow(false,event.getPage());
				}
					
				
			}});
		
		refresh();
		
	}
	private void setHelpShow(boolean show,Page page)
	{
			for(int i=0;i<100;i++)
			{
				if(page.hasFellow("hlp"+i))
				{
					((Label)page.getFellow("hlp"+i)).setStyle("padding-left:5px;background:yellow");
					page.getFellow("hlp"+i).setVisible(show);
				}else
					break;
						
			}
	}
	public void refresh()
	{
		IniFile ini=ImportIni.getInstance().getSvdbToSqlIni();
		
		importToSQLDB.setChecked(ini.getValue("web", "importToSQLDB").equals("true")?true:false);
		
		siteviewDS.setValue(ini.getValue("web", "siteviewDS"));
		svdbHistoryReadDay.setValue(ini.getValue("web", "svdbHistoryReadDay"));
		svdbDataInit.setValue(ini.getValue("web", "svdbDataInit"));
		svdbDataStart.setValue(ini.getValue("web", "svdbDataStart"));
		svdbDateReadMinute.setValue(ini.getValue("web", "svdbDateReadMinute"));
		
		import_int.setValue(ini.getValue("sqlDef", "import.int"));
		import_float.setValue(ini.getValue("sqlDef", "import.float"));
		import_string.setValue(ini.getValue("sqlDef", "import.string"));
		import_string_len.setValue(ini.getValue("sqlDef", "import.string.len"));
		import_table.setValue(ini.getValue("sqlDef", "import.table"));
		import_table_end.setValue(ini.getValue("sqlDef", "import.table.end"));
		import_index.setValue(ini.getValue("sqlDef", "import.index"));
		import_insert.setValue(ini.getValue("sqlDef", "import.insert"));
		mysql_tmp.setValue(ini.getValue("sqlDef", "mysql.tmp"));
		thread_PRIORITY.setSelectedIndex(Integer.parseInt(ini.getValue("sqlDef", "thread.PRIORITY")));
		
	}
	public void save()
	{
		IniFile ini=ImportIni.getInstance().getSvdbToSqlIni();
		try{
				ini.setKeyValue("web","importToSQLDB",importToSQLDB.isChecked()?"true":"false");
			
				ini.setKeyValue("web","siteviewDS",siteviewDS.getValue());
				ini.setKeyValue("web","svdbHistoryReadDay",svdbHistoryReadDay.getValue());
				ini.setKeyValue("web","svdbDataInit",svdbDataInit.getValue());
				ini.setKeyValue("web","svdbDataStart",svdbDataStart.getValue());
				
				ini.setKeyValue("web","svdbDateReadMinute",svdbDateReadMinute.getValue());
				
				ini.setKeyValue("sqlDef","import.int",import_int.getValue());
				ini.setKeyValue("sqlDef","import.float",import_float.getValue());
				ini.setKeyValue("sqlDef","import.string",import_string.getValue());
				ini.setKeyValue("sqlDef","import.string.len",import_string_len.getValue());
				ini.setKeyValue("sqlDef","import.table",import_table.getValue());
				ini.setKeyValue("sqlDef","import.table.end",import_table_end.getValue());
				ini.setKeyValue("sqlDef","import.index",import_index.getValue());
				ini.setKeyValue("sqlDef","import.insert",import_insert.getValue());
				ini.setKeyValue("sqlDef","mysql.tmp",mysql_tmp.getValue());
				ini.setKeyValue("sqlDef","thread.PRIORITY",thread_PRIORITY.getSelectedIndex());
				ini.saveChange();
				ini.load();
		}catch(Exception e)
		{
			Toolkit.getToolkit().showError(e.getMessage());
		}
		
		ReadDataToMysql readDataToMysql=ReadDataToMysql.getInstance();
		try
		{
			readDataToMysql.reloadIni();
			
		}catch(Exception e)
		{
			Toolkit.getToolkit().showError(e.getMessage());
		}
	}

}
