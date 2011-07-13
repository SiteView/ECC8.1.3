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
 * @author yuandong���ҳ�渺���view��eccUrl.properties�ж�ȡ��߹������Ͷ�Ӧ�����ӣ���ʾ��eccbody��
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
		List<VirtualView> allVirtualView = v.getAllVirtualView();// �õ�����������ͼ���˴�ʹ�õ�һ��
		ArrayList<VirtualItem> allTopVirtualItem = allVirtualView.get(0)
				.getTopItems();// �õ����и��ڵ㣬admin��õ�7���������ǵ�����
		this.fatherVirtualItem = allTopVirtualItem.get(allTopVirtualItem.size()-2);
		allSonItem = allVirtualView.get(0).getSonItems(
				allTopVirtualItem.get(allTopVirtualItem.size() - 2));// �ӵ�ǰitem�еõ�����item�����͵õ���������item
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
			if (zulName.equals("ͳ�Ʊ���")) {
				pic = "/main/images/report.gif";
				dscr = "�Զ����������ݽ���ͳ�Ʒ��������Զ�����/��/�±��������ͣ�֧�ֱ����Զ�������ʵʱ����չʾ��";
			}
			if (zulName.equals("���Ʊ���")) {
				pic = "/main/images/report.gif";
				dscr = "չʾѡ���ļ����������һ��ʱ���ڵ����������ͨ���ṩ��Ԥ��ֵ�������������׳�����Ĳ����������ϵͳ��������ά�����Ӷ���ֹ����ķ���ͣ���¼���";
			}
			if (zulName.equals("TopN����")) {
				pic = "/main/images/report.gif";
				dscr = "�����û��鿴һ��ʱ�䷶Χ��ĳһ�������ĳ����ָ���N������ͳ����������Զ�����/��/�±��������ͣ�֧�ֱ����Զ�������ʵʱ����չʾ��";
			}
			if (zulName.equals("״̬ͳ�Ʊ���")) {
				pic = "/main/images/report.gif";
				dscr = "�����û��鿴һ��ʱ�䷶Χ��ĳһ�������ĳ����ָ���N������ͳ����������Զ�����/��/�±��������ͣ�֧�ֱ����Զ�������ʵʱ����չʾ��";
			}
			if (zulName.equals("�Աȱ���")) {
				pic = "/main/images/contrast.gif";
				dscr = " ��ʾ�˲�ͬ�豸�ļ��������ͬʱ���ڵļ�����ݵĶԱȡ�ͨ�����ݵ���ʾ�����Զ�ѡ�������Ĳ�ָͬ�������ϸ��ʾ�����ԶԼ�����������������ϸ�İ��ա�";
			}
			if (zulName.equals("ʱ�ζԱȱ���")) {
				pic = "/main/images/contrast.gif";
				dscr = "��ʾ��ͬһ������ڲ�ͬʱ����ڵļ�����ݵĶԱȡ�ͨ�����ݵ���ʾ�����Զ�ѡ�������ڲ�ͬʱ��ε����ݽ�����ϸ��ʾ�����ԶԼ����������ϸ�İ��ա�";
			}
			if (zulName.equals("�������Ϣ����")) {
				pic = "/main/images/info.gif";
				dscr = "չʾ��������ͼ�ж�ȡ�����м������Ϣ�б����Ը���ɸѡ�������з�����ʾ��";
			}
			if (zulName.equals("SysLog��ѯ")) {
				pic = "/main/images/log.gif";
				dscr = "Syslog��ѯ������ѯ��Ӧʱ����ڣ���SyslogMsg��Ϣ��ƥ��ķ��ϲ���Facility��Level��Syslog��־��֧���û���������ɸѡ��ѯ��";
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
					if(eccItem.getTitle().equals(fatherVirtualItem.getItemDataZulName())){
						getTreeItem(root,i.getItemDataZulName());//���� 
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
