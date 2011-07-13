package com.siteview.ecc.report.statisticalreport;

import java.util.Date;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.CreateEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Window;

import com.siteview.base.data.IniFile;
import com.siteview.base.data.UserRightId;
import com.siteview.ecc.alert.SelectTree;
import com.siteview.ecc.report.common.GroupLinkListener;
import com.siteview.ecc.report.dao.ReportDaoImpl;
import com.siteview.ecc.util.LinkCheck;
import com.siteview.ecc.util.Toolkit;

/**
 *修改报表功能处理类
 * 
 * @company: siteview
 * @author:di.tang
 * @date:2009-4-16
 */
public class ModifyReportComposer extends GenericForwardComposer {
	private IniFile	ini					= null;
	private ReportItem reportItem ;
	Tree			monitortree;
	Combobox		viewNamecombobox;
	Textbox			Title;
	Textbox			Descript;
	Listbox			Period;					// 报告类型
	Checkbox		Graphic;
	Listbox			ComboGraphic;
	Checkbox		ListError;
	Checkbox		ListDanger;
	Textbox			EmailSend;
	Checkbox		Parameter;
	Checkbox		Deny;						// 标止报告
	Listbox 		fileType;
	Listbox			Generate;
	Listbox			EndTime1;
	Listbox			EndTime2;
	Listbox			WeekEndTime;
	Window			modifyreportwindow;
	Label 			groupLink;
	Datebox Begin_Date;
	Datebox End_Date;
	
	public void onCreate$groupLink(Event event){
		boolean isLink = LinkCheck.getLinkCheck().CanSeeLink(UserRightId.WholeView);
		if(isLink){
			String style = "color:#18599C;cursor:pointer;text-decoration: underline;";
			groupLink.setStyle(style);
		}
	}
	public void onClick$groupLink(Event e){
		Comboitem item = viewNamecombobox.getSelectedItem();
		if(item!=null){
			groupLink.addEventListener(Events.ON_CLICK,new GroupLinkListener(item.getLabel()));
		}
	}
	private String title = "";
	/**
	 * 显示报表内容
	 * 
	 * @param e
	 */
	public void onCreate$modifyreportwindow(CreateEvent e) {
		this.ini = (IniFile) e.getArg().get("THEREPORTINI");
		reportItem = (ReportItem) e.getArg().get("reportItem");
		
		
		if (this.ini != null) {
			title = getValue("Title");
			if(title!=null){
				int index = title.indexOf('|');
				if(index > 0){
					title = title.substring(0, index);
				}
			}
		  Title.setValue(title);
			  
			Descript.setValue(getValue("Descript"));
			setCheckbox(Graphic, "Graphic");
			setCheckbox(ListError, "ListError");
			setCheckbox(ListDanger, "ListDanger");
			setCheckbox(Parameter, "Parameter");
			setCheckbox(Deny, "Deny");
			for(int i=0;i<fileType.getItems().size();i++)
				if(fileType.getItemAtIndex(i).getValue().toString().equals(getValue("fileType")))
				{
					fileType.setSelectedIndex(i);
					break;
				}
		
			
			setListbox(Period, "Period");
			String p = getValue("Period");
			if (p != null && p.equals("周报")) {
				Period.setDisabled(false);
			}
			
			if(p.split("Other").length>0){
				String [] datas=p.split("@timestart@");
				if(datas.length>0){
					String[] data=datas[1].split("@timeend@");
					if(data.length>0){
						Begin_Date.setValue(new Date(data[0]));
						End_Date.setValue(new Date(data[1]));
					}
				}
			}
			setListbox(ComboGraphic, "ComboGraphic");
			setListbox(Generate, "Generate");
			setListbox(WeekEndTime, "WeekEndTime");
			setListbox(Period, "Period");
			String endtime = getValue("EndTime");

			setListboxByValue(EndTime1, endtime.substring(0, endtime.indexOf(":")));
			setListboxByValue(EndTime2, endtime.substring(endtime.indexOf(":") + 1));
			// 显示当前报告对应的监测器节点
			String ids = getValue("GroupRight");
			this.execution.setAttribute("all_selected_ids", ids);
			try {
				((SelectTree) monitortree).onCreate();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			EmailSend.setValue(getValue("EmailSend"));
		}

	}
	/**
	 * @param o
	 * @param oname
	 */
	public void onSelect$viewNamecombobox(Event event){
		String av = (String) viewNamecombobox.getSelectedItem().getValue();
		((SelectTree) monitortree).setViewName(av);
	}

	private void setCheckbox(Checkbox o, String oname) {
		String g = getValue(oname);
		if (g != null) {
			if (g.equals("Yes")) {
				o.setChecked(true);
			} else {
				o.setChecked(false);
			}
		}
	}

	private void setListbox(Listbox o, String oname) {
		String g = getValue(oname);
		if (g != null) {
			setListboxByValue(o, g);
		}
	}

	private void setListboxByValue(Listbox o, String g) {
		if (g != null) {
			for (Object i : o.getItems()) {
				Listitem x = (Listitem) i;
				if (x.getLabel().toString() != null && x.getLabel().equals(g)) {
					o.setSelectedItem(x);
					break;
				}
				if (x.getValue() != null && x.getValue().toString().equals(g)) {

					o.setSelectedItem(x);
					break;
				}
			}
		}
	}

	private String getValue(String name) {
		return this.ini.getValue(reportItem.getReportID(), name);
	}

	/**
	 * 保存修改后的报表内容
	 */
	public void onSaveTheModifyReport(Event event) {
		try {
			if (Title.getValue() == null || Title.getValue().equals("")) {
				Messagebox.show("报告标题不能为空,请重新输入!", "提示", Messagebox.OK,
						Messagebox.INFORMATION);
				Title.focus();
				return;
			}
			for(String section : ini.getSectionList()){
				String existName = ini.getValue(section, "Title");
				if(!title.equals(Title.getValue().trim())
						&& Title.getValue().trim().equals(existName)){
					Messagebox.show("报告名称已存在,请重新输入!", "提示", Messagebox.OK,
							Messagebox.INFORMATION);
					Title.focus();
					return;
				}
			}
			String sections = reportItem.getReportID();
			// ini.createSection(sections);
			ini.setKeyValue(sections, "creatTime", Toolkit.getToolkit().formatDate(new Date(Long.parseLong(sections))));
			ini.setKeyValue(sections, "MonitorNumber",((SelectTree) monitortree).getSelectedIds().size());
			ini.setKeyValue(sections, "Title", Title.getValue().trim());
			Listitem a = Period.getSelectedItem();
			String stringbuffer = null; 
			Date tmStart=Begin_Date.getValue();
			Date tmEnd=End_Date.getValue();
			
			if(!"Other".equals(a.getValue().toString())){
				if(a==null)
					ini.setKeyValue(sections, "Period", "0");
				ini.setKeyValue(sections, "Period", a.getValue().toString());
			}else {
				if(a==null){
					ini.setKeyValue(sections, "Period", "0");
				}else {
	//				stringbuffer=a.getValue().toString()+"@timestart@"+tmStart+timeStart+"@timeend@"+tmEnd+timeEnd;
					stringbuffer=a.getValue().toString()+"@timestart@"+tmStart+"@timeend@"+tmEnd;
					ini.setKeyValue(sections, "Period", stringbuffer);
				}
			}
//			ini.setKeyValue(sections, "Period", a.getValue().toString());

			if (Graphic.isChecked()) {
				ini.setKeyValue(sections, "Graphic", "Yes");
				a = ComboGraphic.getSelectedItem();
				ini.setKeyValue(sections, "ComboGraphic", a.getLabel());
			} else {
				ini.setKeyValue(sections, "Graphic", "No");
			}
			try {
				if (EmailSend.getValue() != null && !EmailSend.getValue().equals("")) {
					String constr = "/.+@.+\\.[a-z]+/";
					EmailSend.setConstraint(constr);
					ini.setKeyValue(sections, "EmailSend", EmailSend.getValue());
				}else{
					ini.deleteKey(sections, "EmailSend");
				}
			} catch (Exception x) {
				Messagebox.show("E_Mail格式不正确,请重新输入!", "提示", Messagebox.OK, Messagebox.INFORMATION);
				EmailSend.focus();
				return;
			}			
			a = WeekEndTime.getSelectedItem();
			ini.setKeyValue(sections, "WeekEndTime", a.getValue().toString());
			/*if (Deny.isChecked()) {
				ini.setKeyValue(sections, "Deny", "Yes");
			} else {
				ini.setKeyValue(sections, "Deny", "No");
			}*/
			ini.setKeyValue(sections, "fileType", fileType.getSelectedItem().getValue().toString());
			if (ListDanger.isChecked()) {
				ini.setKeyValue(sections, "ListDanger", "Yes");
			} else {
				ini.setKeyValue(sections, "ListDanger", "No");
			}
			if (ListError.isChecked()) {
				ini.setKeyValue(sections, "ListError", "Yes");
			} else {
				ini.setKeyValue(sections, "ListError", "No");
			}
			ini.setKeyValue(sections, "ListAlert", "Yes");
			ini.setKeyValue(sections, "Descript", Descript.getValue());
			a = EndTime1.getSelectedItem();
			if (a == null) {
				a = EndTime1.getItemAtIndex(0);
			}
			Listitem b = EndTime2.getSelectedItem();
			ini.setKeyValue(sections, "EndTime", a.getLabel() + ":" + b.getLabel());
			// ListClicket
			// StartTime
			// StatusResult
			// SumCheckItem
			// cksum
			a = Generate.getSelectedItem();
			if (a == null) {
				a = Generate.getItemAtIndex(0);
			}
			ini.setKeyValue(sections, "Generate", a.getLabel());
			if (Parameter.isChecked()) {
				ini.setKeyValue(sections, "Parameter", "Yes");
			} else {
				ini.setKeyValue(sections, "Parameter", "No");
			}

			String nodeids = getNodeids();
			if (nodeids == null) {
				Messagebox.show("请选择监测器!", "提示", Messagebox.OK, Messagebox.INFORMATION);
				return;
			}
			ini.setKeyValue(sections, "GroupRight", nodeids);
			b = null;
			ini.saveChange();

			ini.load();
			
			
			
			if (this.modifyreportwindow != null) {
				modifyreportwindow.setAttribute("saveOK","true");
				this.modifyreportwindow.detach();
			}
			
			Executions.getCurrent().getDesktop().getSession().setAttribute("statistical_report_id", sections);
		} catch (WrongValueException e) {
			Toolkit.getToolkit().showError(e.getMessage());
		} catch (Exception e) {
			Toolkit.getToolkit().showError(e.getMessage());
		}

	}

	/**
	 * 获取选择的监测器节点
	 * 
	 * @return
	 */
	public String getNodeids() {
		String nodeids = null;
		try {
			SelectTree sTree = (SelectTree) monitortree;
			nodeids = sTree.getAllSelectedIds();
		} catch (NullPointerException e) {

		}
		return nodeids;
	}
}
