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
import com.siteview.ecc.general.Alert.ItemRenderer;
import com.siteview.ecc.general.Alert.SelectListener;
import com.siteview.ecc.start.EccStarter;
import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.util.Toolkit;


public class Task extends GenericAutowireComposer {
	private Listbox mainMenu;
	private View v = Toolkit.getToolkit().getSvdbView(
			Executions.getCurrent().getDesktop().getSession());
	private ArrayList<VirtualItem> allSonItem = new ArrayList<VirtualItem>();
	private HashMap<String, String> urlMap = new HashMap<String, String>();
//	private static EccStarter eccStarter=new EccStarter();
	private VirtualItem fatherVirtualItem_set = null;//����  -- ����ƻ�
	private VirtualItem fatherVirtualItem_task = null;//����  -- ����ƻ�

	
	public void onInit(Event event) {
		EccStarter starter =EccStarter.getInstance();
		loadUrl(starter);
		List<VirtualView> allVirtualView = v.getAllVirtualView();// �õ�����������ͼ���˴�ʹ�õ�һ��
		ArrayList<VirtualItem> allTopVirtualItem = allVirtualView.get(0)
				.getTopItems();// �õ����и��ڵ㣬admin��õ�7���������ǵ��߸�
		this.fatherVirtualItem_set = allTopVirtualItem.get(allTopVirtualItem.size()-1);//set
		
		allSonItem = allVirtualView.get(0).getSonItems(
				allTopVirtualItem.get(allTopVirtualItem.size() - 1));// �ӵ�ǰitem�еõ�����item�����͵õ���������item
		
		this.fatherVirtualItem_task = allSonItem.get(5);// task;
		allSonItem = allVirtualView.get(0).getSonItems(allSonItem.get(5));
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
		}
		finally
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
			if (zulName.equals("����ʱ������ƻ�")) {
				pic = "/main/images/task.gif";
				dscr = "����ʱ������ƻ�����ӣ�������Ҫ���ù���ʱ�䣬���������ݶ���ʱ����й�����";//�����������
			}
			if (zulName.equals("ʱ�������ƻ�")) {
				pic = "/main/images/task.gif";
				dscr = "ʱ�������ƻ�����ӣ�������Ҫ���ù���ʱ�䣬���������ݶ���ʱ����й�����";
			}
			if (zulName.equals("���ʱ������ƻ�")) {
				pic = "/main/images/task.gif";
				dscr = "���ʱ������ƻ�����ӣ�������Ҫ���ù���ʱ�䣬֧�ּ��������ʱ�����ã����������ݶ���ʱ����й�����";
			}
		}
	}

/*	public class SelectListener implements org.zkoss.zk.ui.event.EventListener {

		private static final long serialVersionUID = 1L;
		VirtualItem i;

		public SelectListener(VirtualItem vi) {
			i = vi;

		}

		public void onEvent(Event event) throws Exception {
			Include eccBody = (Include) (event.getTarget().getDesktop()
					.getPage("eccmain").getFellow("eccBody"));

			String targetUrl = urlMap.get(i.getItemDataZulType()) ;
				
			eccBody.setSrc(targetUrl);
		}
	}*/
	public class SelectListener implements org.zkoss.zk.ui.event.EventListener {

		private static final long serialVersionUID = 1L;
		VirtualItem i;

		public SelectListener(VirtualItem vi) {
			i = vi;
		}
		public void onEvent(Event event) throws Exception {
			try{
			Include eccBody = (Include) (event.getTarget().getDesktop().getPage("eccmain").getFellow("eccBody"));

			//���� tree 
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
					//��������߼�
					EccTreeItem eccItem = (EccTreeItem)root.getValue();	
					if(eccItem.getTitle().equals(fatherVirtualItem_set.getItemDataZulName())){
						getTreeItem(root,fatherVirtualItem_task.getItemDataZulName(),i.getItemDataZulName());//���� --��������ƻ�
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

	public void getTreeItem(Treeitem root,String zulNameTop,String zulNameSecond)
	{
		if(root==null) return ;
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
				if(eccItem.getTitle().equals(zulNameTop)){//�ҵ� ����ƻ�
					getTreeItem(item,zulNameSecond);				
					break;
				}
			}
		}
		return ;
	}
	public void getTreeItem(Treeitem root,String zul_name)
	{
		if(root==null) return ;
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
