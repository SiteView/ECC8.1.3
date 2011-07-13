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
 * @author yuandong���ҳ�渺���view��eccUrl.properties�ж�ȡ��߹������Ͷ�Ӧ�����ӣ���ʾ��eccbody��
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
		List<VirtualView> allVirtualView = v.getAllVirtualView();// �õ�����������ͼ���˴�ʹ�õ�һ��
		List<VirtualItem> allTopVirtualItem = allVirtualView.get(0)
				.getTopItems();// �õ����и��ڵ㣬admin��õ�7�������������һ��
		
		this.fatherVirtualItem = allTopVirtualItem.get(allTopVirtualItem.size()-1);
		
		allSonItem = allVirtualView.get(0).getSonItems(
				allTopVirtualItem.get(allTopVirtualItem.size() - 1));// �ӵ�ǰitem�еõ���item�����͵õ���������item
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
			if (zulName.equals("��Ʒ����")) {
				pic = "/main/images/about.gif";
				dscr = "�ṩ��������������Ƽ����޹�˾�ļ�飬ͬʱҲ�ṩ�˹�˾��վ�ĳ����ӣ���ӭ�û�������Ա��ȡ��Ϊȫ��ļ�������";//�����������
			}
			if (zulName.equals("������")) {
				pic = "/main/images/license.gif";
				dscr = "��ʾ�û��������ʹ�õĵ������������ʹ�õ������豸�����ѹ�����ģ�����Ϣ�������û���ʱ���˽�ϵͳ��ʹ�������";
			}
			if (zulName.equals("SysLog����")) {
				pic = "/main/images/log.gif";
				dscr = "��syslog�ɼ����ݵĲ��������ݱ������޵���Ϣ�������á�";
			}
			if (zulName.equals("�û�������־")) {
				pic = "/main/images/log.gif";
				dscr = "���ڼ�¼�����û��Ĳ�����չʾ���û���ɸѡ��������Բ�����־��ѯ��";
			}
			if(zulName.equals("��ƷURL��ַ")){
				pic = "/main/images/log.gif";
				dscr = "���ڼ�¼����ͬ���Ʒ��URL��ַ������������Ʒ�Ĵ�";
			}
			if (zulName.equals("ϵͳ���")) {
				pic = "/main/images/diagnosis.gif";
				dscr = "���ڶ�ϵͳ��������״���ķ�������ϣ������û�������Ͻ�����޸�ϵͳ��";
			}
			if (zulName.equals("����ƻ�")) {
				pic = "/main/images/task.gif";
				dscr = "ʵ�ּ��������ʱ��Ķ��壬�������û�����ʵ����Ҫ���ż������⼰��������ʱ�䡣";
			}
			if (zulName.equals("�ʼ�����")) {
				pic = "/main/images/email.gif";
				dscr = "���������ʼ����յ�ַ���ʼ����ͷ��������ã�Ϊ�ʼ����������ṩ�Ⱦ�������";
			}
			if (zulName.equals("��������")) {
				pic = "/main/images/sms.gif";
				dscr = "�������ж��Ž��յ�ַ�����ŷ��ͷ��������ã�Ϊ���ű��������ṩ�Ⱦ�������";
			}
			if (zulName.equals("��������")) {
				pic = "/main/images/settings.gif";
				dscr = "�������е�½�������ã������Ƿ���ҪIP��ַ��֤����ֹ�Ƿ����롣";
			}
			if (zulName.equals("ֵ�������")) {
				pic = "/main/images/maintain.gif";
				dscr = "ʵ��ֵ������Ӽ���ϸֵ���İ��ţ�֧�ֶ���ֵ������͵���ӡ�";
			}
			if (zulName.equals("�û�����")) {
				pic = "/main/images/user.gif";
				dscr = "ʵ���û�����ӣ��������õ����ü��û�Ȩ�޵ķ��䡣";
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
}


