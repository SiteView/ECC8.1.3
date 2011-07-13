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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericAutowireComposer;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;

import com.siteview.actions.EccActionManager;
import com.siteview.base.data.VirtualItem;
import com.siteview.base.data.VirtualView;
import com.siteview.base.manage.View;
import com.siteview.ecc.alert.control.MenuHBox;
import com.siteview.ecc.start.EccStarter;
import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.treeview.windows.ConstantValues;
import com.siteview.ecc.util.Toolkit;

/**
 * @author yuandong这个页面负责从view和eccUrl.properties中读取左边功能树和对应的链接，显示到eccbody上
 * 
 */
public class Set extends GenericAutowireComposer {
	private Listbox mainMenu;
	private View v = Toolkit.getToolkit().getSvdbView(
			Executions.getCurrent().getDesktop().getSession());
	private ArrayList<VirtualItem> allSonItem = new ArrayList<VirtualItem>();
	private HashMap<String, String> urlMap = new HashMap<String, String>();
	private VirtualItem fatherVirtualItem = null;

	public void onInit(Event event) {
		EccStarter starter = EccStarter.getInstance();
		loadUrl(starter);
		List<VirtualView> allVirtualView = v.getAllVirtualView();// 得到所有虚拟视图，此处使用第一个
		List<VirtualItem> allTopVirtualItem = allVirtualView.get(0)
				.getTopItems();// 得到所有根节点，admin会得到7个：设置是最后一个
		
		this.fatherVirtualItem = allTopVirtualItem.get(allTopVirtualItem.size()-1);
		
		allSonItem = allVirtualView.get(0).getSonItems(
				allTopVirtualItem.get(allTopVirtualItem.size() - 1));// 从当前item中得到子item这样就得到了所有子item
		ItemRenderer model = new ItemRenderer(allSonItem);
		MakelistData(mainMenu, model, model);

	}

	private void MakelistData(Listbox listb, ListModelList model,
			ListitemRenderer rend) {
		listb.setModel(model);
		listb.setItemRenderer(rend);

	}

	private void loadUrl(EccStarter starter) {

	  InputStreamReader isr=null;
	  FileInputStream fis=null;
	  BufferedReader bufReader=null;
		try {

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

	public class ItemRenderer extends ListModelList implements ListitemRenderer {
		String pic = "";
		String dscr = "";

		public ItemRenderer(List table) {

			addAll(table);
		}

		@Override
		public void render(Listitem arg0, Object arg1) throws Exception {
			// TODO Auto-generated method stub
			Listitem item = arg0;
			VirtualItem vi = (VirtualItem) arg1;
			item.addEventListener("onClick", new SelectListener(vi));
			Listcell lc1 = new Listcell();
			Listcell lc2 = new Listcell();
			item.setHeight("22px");

			setImageDscr(vi.getItemDataZulName());
			Image image = new Image();
			image.setSrc(pic);
			image.setParent(lc1);

			Label lb = new Label(" " + vi.getItemDataZulName());
			lb.setParent(lc1);

			Label lb1 = new Label(dscr);
			lb1.setParent(lc2);

			lc1.setParent(item);
			lc2.setParent(item);
		}

		public void setImageDscr(String zulName) {
			if (zulName.equals("产品介绍")) {
				pic = "/main/images/about.gif";
				dscr = "提供北京游龙网网络科技有限公司的简介，同时也提供了公司网站的超链接，欢迎用户浏览，以便获取更为全面的技术服务。";//描述添加在这
			}
			if (zulName.equals("软件许可")) {
				pic = "/main/images/license.gif";
				dscr = "显示用户购买和已使用的点数、购买和已使用的网络设备数、已购买功能模块的信息，方便用户及时地了解系统的使用情况。";
			}
			if (zulName.equals("SysLog设置")) {
				pic = "/main/images/log.gif";
				dscr = "对syslog采集数据的参数及数据保存期限等信息进行设置。";
			}
			if (zulName.equals("用户操作日志")) {
				pic = "/main/images/log.gif";
				dscr = "用于记录所有用户的操作并展示，用户可筛选条件定义对操作日志查询。";
			}
			if(zulName.equals("产品URL地址")){
				pic = "/main/images/log.gif";
				dscr = "用于记录其他同类产品的URL地址，方便其他产品的打开";
			}
			if (zulName.equals("系统诊断")) {
				pic = "/main/images/diagnosis.gif";
				dscr = "用于对系统基本运行状况的分析与诊断，便于用户根据诊断结果，修复系统。";
			}
			if (zulName.equals("任务计划")) {
				pic = "/main/images/task.gif";
				dscr = "实现监测器工作时间的定义，更方便用户根据实际需要安排监测器监测及报警工作时间。";
			}
			if (zulName.equals("邮件设置")) {
				pic = "/main/images/email.gif";
				dscr = "用来进行邮件接收地址及邮件发送服务器配置，为邮件报警发送提供先决条件。";
			}
			if (zulName.equals("短信设置")) {
				pic = "/main/images/sms.gif";
				dscr = "用来进行短信接收地址及短信发送服务器配置，为短信报警发送提供先决条件。";
			}
			if (zulName.equals("基本设置")) {
				pic = "/main/images/settings.gif";
				dscr = "用来进行登陆访问设置，配置是否需要IP地址验证，防止非法接入。";
			}
			if (zulName.equals("值班表设置")) {
				pic = "/main/images/maintain.gif";
				dscr = "实现值班表的添加及详细值班表的安排，支持多种值班表类型的添加。";
			}
			if (zulName.equals("用户管理")) {
				pic = "/main/images/user.gif";
				dscr = "实现用户的添加，管理设置的设置及用户权限的分配。";
			}
		}
	}

	public class SelectListener implements org.zkoss.zk.ui.event.EventListener {

		private static final long serialVersionUID = 1L;
		VirtualItem i;

		public SelectListener(VirtualItem vi) {
			i = vi;
		}
		public void onEvent(Event event) throws Exception {
			try{
			Include eccBody = (Include) (event.getTarget().getDesktop().getPage("eccmain").getFellow("eccBody"));

			//整个 tree 
			Object tmpobj = event.getTarget().getDesktop().getSession().getAttribute("CurrentWindow");
			if(tmpobj!=null){
				event.getTarget().getDesktop().getSession().removeAttribute("CurrentWindow");
			}
			Tree tree = (Tree) (event.getTarget().getDesktop().getPage("eccmain").getFellow("tree"));
			Collection children = tree.getTreechildren().getChildren();
			Treeitem root = null;
			for(Object obj : children){
				if(obj instanceof Treeitem){
					root = (Treeitem)obj;
					//简化里面的逻辑
					EccTreeItem eccItem = (EccTreeItem)root.getValue();	
					if(eccItem.getTitle().equals(fatherVirtualItem.getItemDataZulName())){
						getTreeItem(root,i.getItemDataZulName());//设置
						break;
					}
				}
			}

			String targetUrl = urlMap.get(i.getItemDataZulType()) ;
			eccBody.setSrc(targetUrl);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void getTreeItem(Treeitem root,String zul_name)
	{
		if(root==null) return ;
		boolean flag = root.isOpen();
		root.setOpen(true);
		
		Treechildren tChildren = root.getTreechildren();
		if(tChildren==null) return ;
		
		Collection children = tChildren.getItems();
		Object[] objArr = children.toArray();
		if(children==null || children.size()<=0) return ;
		
		for(Object obj : objArr){
			if(obj instanceof Treeitem){
				Treeitem item = (Treeitem)obj;
				EccTreeItem eccItem = (EccTreeItem)item.getValue();
				if(eccItem.getTitle().equals(zul_name)){
					item.setSelected(true);
					break;
				}
			}
		}
		return ;
	}
  }
}


