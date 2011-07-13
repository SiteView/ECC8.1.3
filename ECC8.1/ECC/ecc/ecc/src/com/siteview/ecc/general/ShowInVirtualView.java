/**
 * 
 */
package com.siteview.ecc.general;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericAutowireComposer;
import org.zkoss.zul.Image;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Panel;

import com.siteview.base.data.VirtualItem;
import com.siteview.base.data.VirtualView;
import com.siteview.base.data.ZulItem;
import com.siteview.base.manage.View;
import com.siteview.ecc.general.report.ItemRenderer;
import com.siteview.ecc.general.report.SelectListener;
import com.siteview.ecc.start.EccStarter;
import com.siteview.ecc.start.StarterListener;
import com.siteview.ecc.util.Toolkit;

/**
 * 这个类负责从view和eccUrl.properties中读取信息
 * 并根据虚拟视图功能节点下的子节点信息，将该功能节点的子节点显示到eccbody上
 * @author MaKun
 * 
 */
public class ShowInVirtualView extends GenericAutowireComposer {
	private Listbox mainMenu;
	private Panel title;
	private String pic = "";
	private String dscr = "";
	
	private HashMap<String, String> urlMap = new HashMap<String, String>();

	public void onInit(Event event) {
		EccStarter starter =EccStarter.getInstance();
		loadUrl(starter);
		Session session = Executions.getCurrent().getDesktop().getSession();
		ShowInVirtualViewBean bean = (ShowInVirtualViewBean)session.getAttribute("showInVirtualViewBean");
		String nodeName = bean.getNodeName();
		ArrayList<String> sonNames = bean.getSonNames();
		title.setTitle(nodeName);
		for(String sonName : sonNames){
			Listitem item = new Listitem();
			try {
				makeListitem(item, sonName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			mainMenu.appendChild(item);
		}
	}
	
	/**
	 * 读取/main/eccUrl.properties文件
	 * @param starter
	 */
	private void loadUrl(EccStarter starter) {

		InputStreamReader isr=null;
		FileInputStream fis=null;
		BufferedReader bufReader=null;
		try{
			fis=new FileInputStream(new File(starter
					.getRealPath("/main/eccUrl.properties")));

			isr=		new InputStreamReader(fis);
			bufReader = new BufferedReader(isr);
			Properties urlProp = new Properties();
			urlProp.load(bufReader);
			for (Object key : urlProp.keySet())
				urlMap.put(key.toString(), urlProp.get(key).toString());

		} catch (IOException e) {
			System.err
			.println("Ingored: failed to load eccUrl.properties file, \nCause: "
					+ e.getMessage());
		}finally
		{
			try{bufReader.close();}catch(Exception e){}
			try{fis.close();}catch(Exception e){}
			try{isr.close();}catch(Exception e){}
		}

	}

	/**
	 * 根据nodeName创建Listitem
	 * @param item
	 * @param nodeName
	 * @throws Exception
	 */
	private void makeListitem(Listitem item, String nodeName) throws Exception {
		String zType = "";
		for(String key : VirtualItem.allZulItem.keySet()){
			ZulItem zItem = VirtualItem.allZulItem.get(key);
			if(nodeName.equals(zItem.zulName)){
				zType = zItem.zulType;
			}
		}
		item.addEventListener("onClick", new SelectListener(zType));
		Listcell lc1 = new Listcell();
		Listcell lc2 = new Listcell();
		item.setHeight("22px");

		setImageDscr(nodeName);
		Image image = new Image();
		image.setSrc(pic);
		image.setParent(lc1);

		Label lb = new Label(" " + nodeName);
		lb.setParent(lc1);

		Label lb1 = new Label(dscr);
		lb1.setParent(lc2);

		lc1.setParent(item);
		lc2.setParent(item);
	}

	/**
	 * 根据itemName匹配对应的pic和dscr
	 * @param itemName
	 */
	private void setImageDscr(String itemName) {
		//from alert
		if (itemName.equals("报警规则")) {
			pic = "/images/alarmstart.gif";
			dscr = "报警规则的定义，当某个监测器的状态符合报警条件时通知用户，通知方式有：Email、短信、脚本、声音。";//描述添加在这
		}
		if (itemName.equals("报警日志")) {
			pic = "/main/images/alarmhistory.gif";
			dscr = "查询所有报警日志，可以根据筛选条件进行查询。";
		}
		//from set
		if (itemName.equals("产品介绍")) {
			pic = "/main/images/about.gif";
			dscr = "提供北京游龙网网络科技有限公司的简介，同时也提供了公司网站的超链接，欢迎用户浏览，以便获取更为全面的技术服务。";//描述添加在这
		}
		if (itemName.equals("软件许可")) {
			pic = "/main/images/license.gif";
			dscr = "显示用户购买和已使用的点数、购买和已使用的网络设备数、已购买功能模块的信息，方便用户及时地了解系统的使用情况。";
		}
		if (itemName.equals("SysLog设置")) {
			pic = "/main/images/log.gif";
			dscr = "对syslog采集数据的参数及数据保存期限等信息进行设置。";
		}
		if (itemName.equals("用户操作日志")) {
			pic = "/main/images/log.gif";
			dscr = "用于记录所有用户的操作并展示，用户可筛选条件定义对操作日志查询。";
		}
		if(itemName.equals("产品URL地址")){
			pic = "/main/images/log.gif";
			dscr = "用于记录其他同类产品的URL地址，方便其他产品的打开";
		}
		if (itemName.equals("系统诊断")) {
			pic = "/main/images/diagnosis.gif";
			dscr = "用于对系统基本运行状况的分析与诊断，便于用户根据诊断结果，修复系统。";
		}
		if (itemName.equals("任务计划")) {
			pic = "/main/images/task.gif";
			dscr = "实现监测器工作时间的定义，更方便用户根据实际需要安排监测器监测及报警工作时间。";
		}
		if (itemName.equals("邮件设置")) {
			pic = "/main/images/email.gif";
			dscr = "用来进行邮件接收地址及邮件发送服务器配置，为邮件报警发送提供先决条件。";
		}
		if (itemName.equals("短信设置")) {
			pic = "/main/images/sms.gif";
			dscr = "用来进行短信接收地址及短信发送服务器配置，为短信报警发送提供先决条件。";
		}
		if (itemName.equals("基本设置")) {
			pic = "/main/images/settings.gif";
			dscr = "用来进行登陆访问设置，配置是否需要IP地址验证，防止非法接入。";
		}
		if (itemName.equals("值班表设置")) {
			pic = "/main/images/maintain.gif";
			dscr = "实现值班表的添加及详细值班表的安排，支持多种值班表类型的添加。";
		}
		if (itemName.equals("用户管理")) {
			pic = "/main/images/user.gif";
			dscr = "实现用户的添加，管理设置的设置及用户权限的分配。";
		}
		//from report
		if (itemName.equals("统计报告")) {
			pic = "/main/images/report.gif";
			dscr = "对定义监测器数据进行统计分析，可以定义日/周/月报多种类型，支持报告自动生成与实时报告展示。";
		}
		if (itemName.equals("趋势报告")) {
			pic = "/main/images/report.gif";
			dscr = "展示选定的监测器数据在一段时间内的运行情况，通过提供的预测值，可提早监控容易出问题的参数，提早对系统进行升级维护，从而防止意外的服务停顿事件。";
		}
		if (itemName.equals("TopN报告")) {
			pic = "/main/images/report.gif";
			dscr = "方便用户查看一定时间范围内某一监测器的某项监测指标的N个数据统计情况，可以定义日/周/月报多种类型，支持报告自动生成与实时报告展示。";
		}
		if (itemName.equals("状态统计报告")) {
			pic = "/main/images/report.gif";
			dscr = "方便用户查看一定时间范围内某一监测器的某项监测指标的N个数据统计情况，可以定义日/周/月报多种类型，支持报告自动生成与实时报告展示。";
		}
		if (itemName.equals("对比报告")) {
			pic = "/main/images/contrast.gif";
			dscr = " 显示了不同设备的监测器在相同时段内的监测数据的对比。通过数据的显示，可以对选择监测器的不同指标进行详细显示，可以对监测器数据情况进行详细的把握。";
		}
		if (itemName.equals("时段对比报告")) {
			pic = "/main/images/contrast.gif";
			dscr = "显示了同一监测器在不同时间段内的监测数据的对比。通过数据的显示，可以对选择监测器在不同时间段的数据进行详细显示，可以对监测器进行详细的把握。";
		}
		if (itemName.equals("监测器信息报告")) {
			pic = "/main/images/info.gif";
			dscr = "展示了整体视图中读取的所有监测器信息列表，可以根据筛选条件进行分类显示。";
		}
		if (itemName.equals("SysLog查询")) {
			pic = "/main/images/log.gif";
			dscr = "Syslog查询用来查询对应时间段内，与SyslogMsg信息相匹配的符合参数Facility和Level的Syslog日志。支持用户定义条件筛选查询。";
		}
		//from task
		if (itemName.equals("绝对时间任务计划")) {
			pic = "/main/images/task.gif";
			dscr = "绝对时间任务计划的添加，根据需要设置工作时间，监测器则根据定义时间进行工作。";//描述添加在这
		}
		if (itemName.equals("时间段任务计划")) {
			pic = "/main/images/task.gif";
			dscr = "时间段任务计划的添加，根据需要设置工作时间，监测器则根据定义时间进行工作。";
		}
		if (itemName.equals("相对时间任务计划")) {
			pic = "/main/images/task.gif";
			dscr = "相对时间任务计划的添加，根据需要设置工作时间，支持间断与连续时间设置，监测器则根据定义时间进行工作。";
		}
	}

	public class SelectListener implements org.zkoss.zk.ui.event.EventListener {

		private static final long serialVersionUID = 1L;
		String nodeName;

		public SelectListener(String name) {
			nodeName = name;
		}

		public void onEvent(Event event) throws Exception {
			Include eccBody = (Include) (event.getTarget().getDesktop()
					.getPage("eccmain").getFellow("eccBody"));

			String targetUrl = urlMap.get(nodeName) ;

			eccBody.setSrc(targetUrl);
		}
	}


}
