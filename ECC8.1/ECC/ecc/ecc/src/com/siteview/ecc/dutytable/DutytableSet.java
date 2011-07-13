package com.siteview.ecc.dutytable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericAutowireComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Include;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Window;

import com.siteview.base.manage.View;
import com.siteview.ecc.email.IniFilePack;
import com.siteview.ecc.log.AppendOperateLog;
import com.siteview.ecc.log.OpObjectId;
import com.siteview.ecc.log.OpTypeId;
import com.siteview.ecc.report.common.ChartUtil;
import com.siteview.ecc.util.Toolkit;


public class DutytableSet extends GenericAutowireComposer {

	private static final long serialVersionUID = 8003243537287099290L;
	private Listbox 								listbox_data;
	private Listbox 								dutyInof;
	private Include 								eccBody;
	private Panel 									moreInfo;
	private Button 									addDutyInfoButton;
	private Button 									delDutyInfoButton;
	
	private String									add_dutyset_section = "";//值班表 添加  
	private String									edit_dutyset_section = "";//值班表  编辑
	
	private String                                  add_fatrher_section = "";//值班表详细信息 添加 
	private String                                  add_son_section = "" ;//值班表详细信息 添加  
	
	private String                                  edit_fatrher_section = "";//值班表详细信息  编辑
	private String                                  edit_son_section = "";//值班表详细信息  编辑
	private String                                  state = "";
	private String 									type  = "";//值班表类型
	private Listhead 								listhead = new Listhead();
	
	

	public void onInit()throws Exception{
		try{

			ArrayList<DutyFatherBean> dutyFatherBeans = new ArrayList<DutyFatherBean>();
			dutyFatherBeans = getAllDutyList();
			
			DutySetListbox dutySetListbox = (DutySetListbox)listbox_data;
		  	ChartUtil.clearListbox(listbox_data);
		  	
			Session session = this.session;
			Object stateObj = session.getAttribute(DutyConstant.State);
			session.removeAttribute(DutyConstant.State);//remove;
			if(stateObj != null){
				dutySetListbox.setStateObject(stateObj);
			}
			dutySetListbox.setDutyFatherBeans(dutyFatherBeans);
			dutySetListbox.onCreate();	
			
			listbox_data.getPagingChild().setMold("os");
			
			//初始化listbox_data
//			DutyFatherModel model = new DutyFatherModel(getAllDutyList());
//			MakelistData(listbox_data, model, model);
//			listbox_data.getPagingChild().setMold("os");


			if(stateObj!= null){
				state = (String)stateObj;
				if("1".equals(state)){
					getAddDutySetSelectedListitem();
				}else if("2".equals(state)){
					getEditDutySetSelectedListitem();
				}else if("3".equals(state)){
					getAddDutyInfoSelectedListitem();
				}else if("4".equals(state)){
					getEditDutyInfoSelectedListitem();
				}
			}
			dutyInof.getPagingChild().setMold("os");
			//add
			listhead.setParent(dutyInof);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 	获取所有的 dutyList信息
	 * @return
	 */
	public ArrayList<DutyFatherBean> getAllDutyList(){
		IniFilePack ini = new IniFilePack("watchsheetcfg.ini");
		Map<String, Map<String, String>> dutyMap = new HashMap<String, Map<String, String>>();
		ArrayList<DutyFatherBean> dutyList = new ArrayList<DutyFatherBean>();
		try {
			ini.load();
		} catch (Exception e) {}
		dutyMap = ini.getM_fmap();
		for(String section:ini.getSectionList()){
			DutyFatherBean dutyFatherBean = new DutyFatherBean();//Description count return type
			if(section == null){continue;}
			dutyFatherBean.setSection(section);
			if(dutyMap.get(section).get("count") == null){continue;}
			String description 		= dutyMap.get(section).get("Description");
			String count 			= dutyMap.get(section).get("count");
			String returnValue		= dutyMap.get(section).get("return");
			String type 			= dutyMap.get(section).get("type");
			if(description == null){description = "";}
			dutyFatherBean.setDescription(description);
			if(count == null){count = "";}
			dutyFatherBean.setCount(count);
			if(returnValue == null){returnValue = "";}
			dutyFatherBean.setReturnValue(returnValue);
			if(type == null){type = "";}
			dutyFatherBean.setType(type);
			dutyList.add(dutyFatherBean);
		}
		Collections.sort(dutyList, new Comparator<DutyFatherBean>(){
			@Override
			public int compare(DutyFatherBean o1, DutyFatherBean o2) {
				return o1.getSection().compareTo(o2.getSection());
			}
		});
		return dutyList;
	}
	
//state 1	
	public void getAddDutySetSelectedListitem()throws Exception{
		Session session = this.session;
		Object SectionObj = session.getAttribute(DutyConstant.Add_DutySet_Section);
		session.removeAttribute(DutyConstant.Add_DutySet_Section);//remove
		if(SectionObj != null){
			this.add_dutyset_section = (String)SectionObj;
			onShowMoreInfo(add_dutyset_section);
		}
	}
//state 2	
	public void getEditDutySetSelectedListitem()throws Exception{
		Session session = this.session;
		Object SectionObj = session.getAttribute(DutyConstant.Edit_DutySet_Section);
		session.removeAttribute(DutyConstant.Edit_DutySet_Section);//remove
		if(SectionObj != null){
			this.edit_dutyset_section = (String)SectionObj;
			onShowMoreInfo(edit_dutyset_section);
		}
	}
//state 3
	public void getAddDutyInfoSelectedListitem()throws Exception{
		Session session = this.session;
		Object FartherSectionObj = session.getAttribute(DutyConstant.Add_DutyFather_Section);
		Object SonSectionObj     = session.getAttribute(DutyConstant.Add_DutySon_Section);
		session.removeAttribute(DutyConstant.Add_DutyFather_Section);//remove
		session.removeAttribute(DutyConstant.Add_DutySon_Section);//remove
		if(FartherSectionObj != null || SonSectionObj != null){
			this.add_fatrher_section = (String)FartherSectionObj;
			this.add_son_section = (String)SonSectionObj;
			onShowMoreInfo(add_fatrher_section);
		}
	}
//state 4
	public void getEditDutyInfoSelectedListitem()throws Exception{
		Session session = this.session;
		Object FartherSectionObj = session.getAttribute(DutyConstant.Edit_DutyFather_Section);
		Object SonSectionObj     = session.getAttribute(DutyConstant.Edit_DutySon_Section);
		session.removeAttribute(DutyConstant.Edit_DutyFather_Section);//remove
		session.removeAttribute(DutyConstant.Edit_DutySon_Section);//remove
		if(FartherSectionObj != null || SonSectionObj != null){
			this.edit_fatrher_section = (String)FartherSectionObj;
			this.edit_son_section = (String)SonSectionObj;
			onShowMoreInfo(edit_fatrher_section);
		}
	}
	/**
	 * 增加值班表详细信息页面
	 * @param event
	 * @throws Exception
	 */
	public void onAddDutyInfo(Event event) throws Exception{
		try{
			String section = listbox_data.getSelectedItem().getId();
			this.session.setAttribute(DutyConstant.Add_DutyFather_Section, section);
			String path = "";
			if("day".equals(this.type)){
				path = "/main/setting/addDutyInfo_day.zul";
			}
			else if("day of week".equals(this.type)){
				path = "/main/setting/addDutyInfo_week.zul";
			}
			else if("day of month".equals(this.type)){
				path = "/main/setting/addDutyInfo_month.zul";
			}
			if("".equals(path)){
				throw new Exception("无法获取 值班表的类型");
			}
			final Window win2 = (Window) Executions.createComponents(path, null, null);
			win2.doModal();
		}catch(Exception e){
			e.printStackTrace();
			Messagebox.show(e.getMessage(),"错误", Messagebox.OK, Messagebox.ERROR);
		}
	}
	
	/**
	 * 删除 DutySet
	 * @param event
	 * @throws Exception
	 */

	public void onDel(Event event)throws Exception {
		try{
			String section = "";
			if(listbox_data.getSelectedItems().size()<1){
				Messagebox.show("您还没有选定想删除项！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				return;
			}
			if(Messagebox.CANCEL == Messagebox.show("删除将会进行，是否继续?", "询问", Messagebox.OK| Messagebox.CANCEL, Messagebox.QUESTION)){
				return;
			}
			
			View view = Toolkit.getToolkit().getSvdbView(event.getTarget().getDesktop());
			String loginname = view.getLoginName();
			
			for(Object it:listbox_data.getSelectedItems()){
				Listitem la=(Listitem)it;
				section=la.getId();				
				IniFilePack ini = new IniFilePack("watchsheetcfg.ini");
				try{ini.load();}catch(Exception e){}
				try{
					ini.deleteSection(section);
					ini.saveChange();
					String minfo=loginname+" "+"在"+OpObjectId.duty_set.name+"中进行了  "+OpTypeId.del.name+"操作，删除的值班表为： "+section;			
					AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.del, OpObjectId.duty_set);
				}catch(Exception e){
					e.printStackTrace();
					Messagebox.show(e.getMessage(),"错误", Messagebox.OK, Messagebox.ERROR);
				}
			}
			reFresh();
		}catch(Exception e){
			e.printStackTrace();
			Messagebox.show(e.getMessage(),"错误", Messagebox.OK, Messagebox.ERROR);
		}
	}
	public void reFresh() throws Exception{
		try{
			String targetUrl = "/main/setting/setmaintain.zul";
			eccBody = (Include) (this.desktop.getPage("eccmain").getFellow("eccBody"));
			eccBody.setSrc(null);
			eccBody.setSrc(targetUrl);
		}catch(Exception e){
			Messagebox.show(e.getMessage(),"错误", Messagebox.OK, Messagebox.ERROR);
		}
	}
	//clear listbox
	public void clearListbox(Component component){
		if(component.getChildren().isEmpty()==false){
			List<Listitem> r = new ArrayList<Listitem>();
			for (Object o : component.getChildren()) {
				try {
					r.add((Listitem) o);
				} catch (Exception e) {}
			}
			for (Listitem w : r) {
				component.removeChild(w) ;
			}
		}
	}
	
	public void onShowMoreInfo()throws Exception{
		if(listbox_data.getSelectedItems().size()<=0){
			addDutyInfoButton.setDisabled(true);
			delDutyInfoButton.setDisabled(true);
			moreInfo.setTitle("值班信息: ");
			clearListbox(dutyInof);
			return;
		}
		String section = listbox_data.getSelectedItem().getId();
		onShowMoreInfo(section);
	}
	
	public void onShowMoreInfo(String section)throws Exception {
		try {
			addDutyInfoButton.setDisabled(false);
			moreInfo.setTitle("值班信息: " + section);
			this.type = "";
			IniFilePack ini = new IniFilePack("watchsheetcfg.ini");
			try{
				ini.load();
			}catch(Exception e){}
			clearListbox(dutyInof);
			
			Map<String, String>  map= ini.getM_fmap().get(section);
			if("day".equals(map.get("type"))){
				this.type = "day";
			}
			if("day of week".equals(map.get("type"))){
				this.type = "day of week";
			}
			if("day of month".equals(map.get("type"))){
				this.type = "day of month";
			}
			
			if("0".equals(ini.getM_fmap().get(section).get("count"))){
				delDutyInfoButton.setDisabled(true);
				ini.deleteKey(section, "return");
				ini.saveChange();
			}else{
				delDutyInfoButton.setDisabled(false);
				if("day".equals(map.get("type"))){
					makeListbox_day(map,section);
				}
				if("day of week".equals(map.get("type"))){
					makeListbox_week(map,section);
				}
				if("day of month".equals(map.get("type"))){
					makeListbox_month(map,section);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(),"错误", Messagebox.OK, Messagebox.ERROR);
		}
	}
	//clear listheader
	public void clearListHead(Listhead listheader){
		if(listheader.getChildren().isEmpty()==false){
			List<Listheader> r = new ArrayList<Listheader>();
			for (Object o : listheader.getChildren()) {
				try {
					r.add((Listheader) o);
				} catch (Exception e) {}
			}
			for (Listheader w : r) {
				listheader.removeChild(w) ;
			}
		}
	}
	
	/**
	 * day 值班表 初始化
	 * @param map
	 * @param farther_section
	 */
	public void makeListbox_day(Map<String, String>  map,String farther_section){
		ArrayList<DayBean> daylist = new ArrayList<DayBean>();
		for(String key:map.keySet()){
			String regEx="item";  
			Pattern p=Pattern.compile(regEx);
			Matcher m=p.matcher(key);
			if(m.find()){
				String item1 = map.get(key);	
				String[] value = new String[6];
				value = item1.split(",");
				if(value.length<6){ 
					continue;
				}
				DayBean dayBean = new DayBean(key,value[0],value[1],value[2],value[3],value[4],value[5]); 
				daylist.add(dayBean);
			}
		}
		if(daylist.size()>1){
			Collections.sort(daylist, new Comparator<DayBean>(){
				@Override
				public int compare(DayBean o1, DayBean o2) {
					// TODO Auto-generated method stub
					int a1 = o1.getStartHour().compareTo(o2.getStartHour());
					int a2 = o1.getStartMinute().compareTo(o2.getStartMinute());
					int akey = o1.getKey().compareTo(o2.getKey());
					if(a1 ==0){
						if(a2 == 0){
							return akey;
						}else{return a2;}
					}else{return a1;}
				}
			});
		}
		
		this.clearListHead(listhead);
		listhead.setSizable(true);
		Listheader listheader1 = new Listheader("接收告警手机号码");
		listheader1.setSort("auto");
		listheader1.setAlign("left");
		listheader1.setParent(listhead);
		Listheader listheader2 = new Listheader("接收告警邮箱");
		listheader2.setSort("auto");
		listheader2.setAlign("left");
		listheader2.setParent(listhead);
		Listheader listheader3 = new Listheader("开始时间");
		listheader3.setSort("auto");
		listheader3.setAlign("center");
		listheader3.setParent(listhead);
		Listheader listheader4 = new Listheader("结束时间");
		listheader4.setSort("auto");
		listheader4.setAlign("center");
		listheader4.setParent(listhead);
		Listheader listheader5 = new Listheader("编辑");
		listheader5.setSort("auto");
		listheader5.setAlign("center");
		listheader5.setParent(listhead);
		
		for(DayBean dayBean:daylist)
		{	
			Listitem listItem=new Listitem();
			listItem.setHeight("30px");
			Listcell l1 = new Listcell(dayBean.getPhone());
			l1.setTooltiptext(dayBean.getPhone());
			l1.setImage("/images/sms2.gif");
			l1.setParent(listItem);
			Listcell l2 = new Listcell(dayBean.getEmail());
			l2.setImage("/images/email2.gif");
			l2.setTooltiptext(dayBean.getEmail());
			l2.setParent(listItem);
			Listcell l5 = new Listcell(dayBean.getStartHour() + ":" + dayBean.getEndHour());
			l5.setTooltiptext(dayBean.getStartHour() + ":" + dayBean.getEndHour());
			l5.setParent(listItem);
			Listcell l6 = new Listcell(dayBean.getEndHour()+ ":" + dayBean.getEndMinute());
			l6.setTooltiptext(dayBean.getEndHour()+ ":" + dayBean.getEndMinute());
			l6.setParent(listItem);
			Listcell l7 = new Listcell();
			l7.setImage("/main/images/alert/edit.gif");
			l7.addEventListener("onClick", new EditClickDayListener(listItem));
			l7.setParent(listItem);
			listItem.setAttribute(DutyConstant.Edit_DutyFather_Section, farther_section);
			listItem.setAttribute(DutyConstant.Edit_DutySon_Section, dayBean.getKey());
			listItem.setParent(dutyInof);	
			if("3".equals(this.state)){
				if(dayBean.getKey().equals(this.add_son_section)){
					dutyInof.setSelectedItem(listItem);
				}
			}
			if("4".equals(this.state)){
				if(dayBean.getKey().equals(this.edit_son_section)){
					dutyInof.setSelectedItem(listItem);
				}
			}
		}
	}
	/**
	 * week 值班表 初始化
	 * @param map
	 * @param farther_section
	 */
	public void makeListbox_week(Map<String, String>  map,String farther_section){
		ArrayList<WeekBean> weeklist = new ArrayList<WeekBean>();	
		for(String key:map.keySet()){
			String regEx="item";  
			Pattern p=Pattern.compile(regEx);
			Matcher m=p.matcher(key);
			if(m.find()){
				String item1 = map.get(key);	
				String[] value = new String[7];
				value = item1.split(",");
				if(value.length<7){ 
					continue;
				}
				WeekBean weekBean = new WeekBean(key,value[0],value[1],value[2],value[3],value[4],value[5],value[6]); 
				weeklist.add(weekBean);
			}
		}
		if(weeklist.size()>1){
			Collections.sort(weeklist, new Comparator<WeekBean>(){
				@Override
				public int compare(WeekBean o1, WeekBean o2) {
					int a1 = o1.getWeek().compareTo(o2.getWeek());
					int a2 = o1.getStartHour().compareTo(o2.getStartHour());
					int a3 = o1.getStartMinute().compareTo(o2.getStartMinute());
					if(a1 ==0){
						if(a2 == 0){
							return a3;
						}else{return a2;}
					}else{return a1;}
				}
			});
		}
		
		this.clearListHead(listhead);
		listhead.setSizable(true);
		Listheader listheader1 = new Listheader("接收告警手机号码");
		listheader1.setSort("auto");
		listheader1.setAlign("left");
		listheader1.setParent(listhead);
		Listheader listheader2 = new Listheader("接收告警邮箱");
		listheader2.setSort("auto");
		listheader2.setAlign("left");
		listheader2.setParent(listhead);
		Listheader listheader6 = new Listheader("星期");
		listheader6.setSort("auto");
		listheader6.setAlign("center");
		listheader6.setParent(listhead);
		Listheader listheader3 = new Listheader("开始时间");
		listheader3.setSort("auto");
		listheader3.setAlign("center");
		listheader3.setParent(listhead);
		Listheader listheader4 = new Listheader("结束时间");
		listheader4.setSort("auto");
		listheader4.setAlign("center");
		listheader4.setParent(listhead);
		Listheader listheader5 = new Listheader("编辑");
		listheader5.setSort("auto");
		listheader5.setAlign("center");
		listheader5.setParent(listhead);
		
		for(WeekBean weekBean:weeklist)
		{	
			Listitem listItem=new Listitem();
			listItem.setHeight("30px");
			Listcell l1 = new Listcell(weekBean.getPhone());
			l1.setImage("/images/sms2.gif");
			l1.setTooltiptext(weekBean.getPhone());
			l1.setParent(listItem);
			Listcell l2 = new Listcell(weekBean.getEmail());
			l2.setImage("/images/email2.gif");
			l2.setTooltiptext(weekBean.getEmail());
			l2.setParent(listItem);
			Listcell l3 = new Listcell(weekBean.getWeek());
			l3.setTooltiptext(weekBean.getWeek());
			l3.setParent(listItem);
			Listcell l5 = new Listcell(weekBean.getStartHour() + ":" + weekBean.getEndHour());
			l5.setTooltiptext(weekBean.getStartHour() + ":" + weekBean.getEndHour());
			l5.setParent(listItem);
			Listcell l6 = new Listcell(weekBean.getEndHour()+ ":" + weekBean.getEndMinute());
			l6.setTooltiptext(weekBean.getEndHour()+ ":" + weekBean.getEndMinute());
			l6.setParent(listItem);
			Listcell l7 = new Listcell();
			l7.setImage("/main/images/alert/edit.gif");
			l7.addEventListener("onClick", new EditClickWeekListener(listItem));
			l7.setParent(listItem);
			listItem.setAttribute(DutyConstant.Edit_DutyFather_Section, farther_section);
			listItem.setAttribute(DutyConstant.Edit_DutySon_Section, weekBean.getKey());
			listItem.setParent(dutyInof);	
			if("3".equals(this.state)){
				if(weekBean.getKey().equals(this.add_son_section)){
					dutyInof.setSelectedItem(listItem);
				}
			}
			if("4".equals(this.state)){
				if(weekBean.getKey().equals(this.edit_son_section)){
					dutyInof.setSelectedItem(listItem);
				}
			}
		}
	}
	/**
	 * month 值班表 初始化
	 * @param map
	 * @param farther_section
	 */
	public void makeListbox_month(Map<String, String>  map,String farther_section){
		ArrayList<MonthBean> monthlist = new ArrayList<MonthBean>();
		for(String key:map.keySet()){
			String regEx="item";  
			Pattern p=Pattern.compile(regEx);
			Matcher m=p.matcher(key);
			if(m.find()){
				String item1 = map.get(key);	
				String[] value = new String[7];
				value = item1.split(",");
				if(value.length<7){ 
					continue;
				}
				MonthBean monthBean = new MonthBean(key,value[0],value[1],value[2],value[3],value[4],value[5],value[6]); 
				monthlist.add(monthBean);
			}
		}
		if(monthlist.size()>1){
			Collections.sort(monthlist, new Comparator<MonthBean>(){
				@Override
				public int compare(MonthBean o1, MonthBean o2) {
					int a1 = o1.getDayInMonth().compareTo(o2.getDayInMonth());
					int a2 = o1.getStartHour().compareTo(o2.getStartHour());
					int a3 = o1.getStartMinute().compareTo(o2.getStartMinute());
					int akey = o1.getKey().compareTo(o2.getKey());
					if(a1 ==0){
						if(a2 == 0){
							if(a3 == 0){
								return akey;
							}else{return a3;}
						}else{return a2;}
					}else{return a1;}
				}
			});
		}
		
		this.clearListHead(listhead);
		listhead.setSizable(true);
		Listheader listheader1 = new Listheader("接收告警手机号码");
		listheader1.setSort("auto");
		listheader1.setAlign("left");
		listheader1.setParent(listhead);
		Listheader listheader2 = new Listheader("接收告警邮箱");
		listheader2.setSort("auto");
		listheader2.setAlign("left");
		listheader2.setParent(listhead);
		Listheader listheader6 = new Listheader("日期");
		listheader6.setSort("auto");
		listheader6.setAlign("center");
		listheader6.setParent(listhead);
		Listheader listheader3 = new Listheader("开始时间");
		listheader3.setSort("auto");
		listheader3.setAlign("center");
		listheader3.setParent(listhead);
		Listheader listheader4 = new Listheader("结束时间");
		listheader4.setSort("auto");
		listheader4.setAlign("center");
		listheader4.setParent(listhead);
		Listheader listheader5 = new Listheader("编辑");
		listheader5.setSort("auto");
		listheader5.setAlign("center");
		listheader5.setParent(listhead);
		
		for(MonthBean monthBean:monthlist)
		{	
			Listitem listItem=new Listitem();
			listItem.setHeight("30px");
			Listcell l1 = new Listcell(monthBean.getPhone());
			l1.setImage("/images/sms2.gif");
			l1.setTooltiptext(monthBean.getPhone());
			l1.setParent(listItem);
			Listcell l2 = new Listcell(monthBean.getEmail());
			l2.setImage("/images/email2.gif");
			l2.setTooltiptext(monthBean.getEmail());
			l2.setParent(listItem);
			Listcell l3 = new Listcell(monthBean.getDayInMonth());
			l3.setTooltiptext(monthBean.getDayInMonth());
			l3.setParent(listItem);
			Listcell l5 = new Listcell(monthBean.getStartHour() + ":" + monthBean.getEndHour());
			l5.setTooltiptext(monthBean.getStartHour() + ":" + monthBean.getEndHour());
			l5.setParent(listItem);
			Listcell l6 = new Listcell(monthBean.getEndHour()+ ":" + monthBean.getEndMinute());
			l6.setTooltiptext(monthBean.getEndHour()+ ":" + monthBean.getEndMinute());
			l6.setParent(listItem);
			Listcell l7 = new Listcell();
			l7.setImage("/main/images/alert/edit.gif");
			l7.addEventListener("onClick", new EditClickMonthListener(listItem));
			l7.setParent(listItem);
			listItem.setAttribute(DutyConstant.Edit_DutyFather_Section, farther_section);
			listItem.setAttribute(DutyConstant.Edit_DutySon_Section, monthBean.getKey());
			listItem.setParent(dutyInof);	
			if("3".equals(this.state)){
				if(monthBean.getKey().equals(this.add_son_section)){
					dutyInof.setSelectedItem(listItem);
				}
			}
			if("4".equals(this.state)){
				if(monthBean.getKey().equals(this.edit_son_section)){
					dutyInof.setSelectedItem(listItem);
				}
			}
		}
	}

	class EditClickDayListener implements org.zkoss.zk.ui.event.EventListener {
		private static final long serialVersionUID = 1L;
		private Listitem l;
		public EditClickDayListener(Listitem li) {
			l = li;
		}
		@Override
		public void onEvent(Event arg0) throws Exception {

				final Window win = (Window) Executions.createComponents(
						"/main/setting/editDutyInfo_day.zul", null, null);
				win.setAttribute(DutyConstant.Edit_DutyFather_Section, l.getAttribute(DutyConstant.Edit_DutyFather_Section));
				win.setAttribute(DutyConstant.Edit_DutySon_Section,l.getAttribute(DutyConstant.Edit_DutySon_Section));
				win.doModal();
		}
	}
	class EditClickWeekListener implements org.zkoss.zk.ui.event.EventListener {
		private static final long serialVersionUID = 1L;
		private Listitem l;
		public EditClickWeekListener(Listitem li) {
			l = li;
		}
		@Override
		public void onEvent(Event arg0) throws Exception {

				final Window win = (Window) Executions.createComponents(
						"/main/setting/editDutyInfo_week.zul", null, null);
				win.setAttribute(DutyConstant.Edit_DutyFather_Section, l.getAttribute(DutyConstant.Edit_DutyFather_Section));
				win.setAttribute(DutyConstant.Edit_DutySon_Section,l.getAttribute(DutyConstant.Edit_DutySon_Section));
				win.doModal();
		}
	}
	class EditClickMonthListener implements org.zkoss.zk.ui.event.EventListener {
		private static final long serialVersionUID = 1L;
		private Listitem l;
		public EditClickMonthListener(Listitem li) {
			l = li;
		}
		@Override
		public void onEvent(Event arg0) throws Exception {

				final Window win = (Window) Executions.createComponents(
						"/main/setting/editDutyInfo_month.zul", null, null);
				win.setAttribute(DutyConstant.Edit_DutyFather_Section, l.getAttribute(DutyConstant.Edit_DutyFather_Section));
				win.setAttribute(DutyConstant.Edit_DutySon_Section,l.getAttribute(DutyConstant.Edit_DutySon_Section));
				win.doModal();
		}
	}
	public void onReFresh(Event event) throws Exception{
		try{
			String targetUrl = "/main/setting/setmaintain.zul";
			eccBody = (Include) (this.desktop.getPage("eccmain").getFellow("eccBody"));
			eccBody.setSrc(null);
			eccBody.setSrc(targetUrl);
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	public void onDelInfo(Event event)throws Exception{
		try{
			if(dutyInof.getSelectedItems().size()<1){
				Messagebox.show("您还没有选定想删除项！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				return;
			}
			if(Messagebox.CANCEL == Messagebox.show("删除将会进行，是否继续?", "询问", Messagebox.OK| Messagebox.CANCEL, Messagebox.QUESTION)){
				return;
			}
			this.edit_fatrher_section = (String)dutyInof.getSelectedItem().getAttribute(DutyConstant.Edit_DutyFather_Section);
			this.edit_son_section     = (String)dutyInof.getSelectedItem().getAttribute(DutyConstant.Edit_DutySon_Section);
	
			String section	=	edit_fatrher_section;
			String itemX	=	edit_son_section;
			IniFilePack ini = new IniFilePack("watchsheetcfg.ini");
			try{
				ini.load();
			}catch(Exception e){}
			
			String count = ini.getM_fmap().get(section).get("count");
			int i = 0;
			try{
				i = Integer.parseInt(count);
			}catch(Exception e){}
			if(1 == i){
				ini.deleteKey(section, "return");
			}
			ini.deleteKey(section, itemX);
			ini.setKeyValue(section, "count", ""+(Integer.parseInt(count)-1));
			ini.saveChange();

			View view = Toolkit.getToolkit().getSvdbView(event.getTarget().getDesktop());
			String loginname = view.getLoginName();
			String minfo=loginname+" "+"在"+OpObjectId.duty_set.name+"中进行了  "+OpTypeId.del.name+"详细信息操作，添加对象为 "+section;
			AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.del, OpObjectId.duty_set);
			onShowMoreInfo();
			
		}catch(Exception e){
			e.printStackTrace();
			Messagebox.show(e.getMessage(),"错误", Messagebox.OK, Messagebox.ERROR);
		}
	}
}
