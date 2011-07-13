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
import java.util.Map;
import java.util.Properties;

import org.zkoss.zk.ui.Executions;
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
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;

import com.siteview.base.data.VirtualItem;
import com.siteview.base.data.VirtualView;
import com.siteview.base.manage.View;
import com.siteview.ecc.start.EccStarter;
import com.siteview.ecc.start.StarterListener;
import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.util.Toolkit;


/**
 * @author yuandong这个页面负责从view和eccUrl.properties中读取左边功能树和对应的链接，显示到eccbody上
 * 
 */
public class report extends GenericAutowireComposer implements StarterListener{
	private Listbox mainMenu;
	private View v = Toolkit.getToolkit().getSvdbView(
			Executions.getCurrent().getDesktop().getSession());
	private List<VirtualItem> allSonItem = new ArrayList<VirtualItem>();
	private Map<String, String> urlMap = new HashMap<String, String>();
//	private static EccStarter eccStarter=new EccStarter();
	private VirtualItem fatherVirtualItem = null;

	public void onInit(Event event) {
		EccStarter starter =EccStarter.getInstance();
		loadUrl(starter);
		List<VirtualView> allVirtualView = v.getAllVirtualView();// 得到所有虚拟视图，此处使用第一个
		ArrayList<VirtualItem> allTopVirtualItem = allVirtualView.get(0)
				.getTopItems();// 得到所有根节点，admin会得到7个：报表是第六个
		this.fatherVirtualItem = allTopVirtualItem.get(allTopVirtualItem.size()-2);
		allSonItem = allVirtualView.get(0).getSonItems(
				allTopVirtualItem.get(allTopVirtualItem.size() - 2));// 从当前item中得到报表item这样就得到了所有子item
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
			if (zulName.equals("统计报告")) {
				pic = "/main/images/report.gif";
				dscr = "对定义监测器数据进行统计分析，可以定义日/周/月报多种类型，支持报告自动生成与实时报告展示。";
			}
			if (zulName.equals("趋势报告")) {
				pic = "/main/images/report.gif";
				dscr = "展示选定的监测器数据在一段时间内的运行情况，通过提供的预测值，可提早监控容易出问题的参数，提早对系统进行升级维护，从而防止意外的服务停顿事件。";
			}
			if (zulName.equals("TopN报告")) {
				pic = "/main/images/report.gif";
				dscr = "方便用户查看一定时间范围内某一监测器的某项监测指标的N个数据统计情况，可以定义日/周/月报多种类型，支持报告自动生成与实时报告展示。";
			}
			if (zulName.equals("状态统计报告")) {
				pic = "/main/images/report.gif";
				dscr = "方便用户查看一定时间范围内某一监测器的某项监测指标的N个数据统计情况，可以定义日/周/月报多种类型，支持报告自动生成与实时报告展示。";
			}
			if (zulName.equals("对比报告")) {
				pic = "/main/images/contrast.gif";
				dscr = " 显示了不同设备的监测器在相同时段内的监测数据的对比。通过数据的显示，可以对选择监测器的不同指标进行详细显示，可以对监测器数据情况进行详细的把握。";
			}
			if (zulName.equals("时段对比报告")) {
				pic = "/main/images/contrast.gif";
				dscr = "显示了同一监测器在不同时间段内的监测数据的对比。通过数据的显示，可以对选择监测器在不同时间段的数据进行详细显示，可以对监测器进行详细的把握。";
			}
			if (zulName.equals("监测器信息报告")) {
				pic = "/main/images/info.gif";
				dscr = "展示了整体视图中读取的所有监测器信息列表，可以根据筛选条件进行分类显示。";
			}
			if (zulName.equals("SysLog查询")) {
				pic = "/main/images/log.gif";
				dscr = "Syslog查询用来查询对应时间段内，与SyslogMsg信息相匹配的符合参数Facility和Level的Syslog日志。支持用户定义条件筛选查询。";
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
						getTreeItem(root,i.getItemDataZulName());//报告 
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
	
	
	
	
	
	
	
	
	
	
	

	@Override
	public void destroyed(EccStarter starter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startInit(EccStarter starter) {
		// TODO Auto-generated method stub
		
	}

}
