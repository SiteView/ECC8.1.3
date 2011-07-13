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
 * ����ฺ���view��eccUrl.properties�ж�ȡ��Ϣ
 * ������������ͼ���ܽڵ��µ��ӽڵ���Ϣ�����ù��ܽڵ���ӽڵ���ʾ��eccbody��
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
	 * ��ȡ/main/eccUrl.properties�ļ�
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
	 * ����nodeName����Listitem
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
	 * ����itemNameƥ���Ӧ��pic��dscr
	 * @param itemName
	 */
	private void setImageDscr(String itemName) {
		//from alert
		if (itemName.equals("��������")) {
			pic = "/images/alarmstart.gif";
			dscr = "��������Ķ��壬��ĳ���������״̬���ϱ�������ʱ֪ͨ�û���֪ͨ��ʽ�У�Email�����š��ű���������";//�����������
		}
		if (itemName.equals("������־")) {
			pic = "/main/images/alarmhistory.gif";
			dscr = "��ѯ���б�����־�����Ը���ɸѡ�������в�ѯ��";
		}
		//from set
		if (itemName.equals("��Ʒ����")) {
			pic = "/main/images/about.gif";
			dscr = "�ṩ��������������Ƽ����޹�˾�ļ�飬ͬʱҲ�ṩ�˹�˾��վ�ĳ����ӣ���ӭ�û�������Ա��ȡ��Ϊȫ��ļ�������";//�����������
		}
		if (itemName.equals("������")) {
			pic = "/main/images/license.gif";
			dscr = "��ʾ�û��������ʹ�õĵ������������ʹ�õ������豸�����ѹ�����ģ�����Ϣ�������û���ʱ���˽�ϵͳ��ʹ�������";
		}
		if (itemName.equals("SysLog����")) {
			pic = "/main/images/log.gif";
			dscr = "��syslog�ɼ����ݵĲ��������ݱ������޵���Ϣ�������á�";
		}
		if (itemName.equals("�û�������־")) {
			pic = "/main/images/log.gif";
			dscr = "���ڼ�¼�����û��Ĳ�����չʾ���û���ɸѡ��������Բ�����־��ѯ��";
		}
		if(itemName.equals("��ƷURL��ַ")){
			pic = "/main/images/log.gif";
			dscr = "���ڼ�¼����ͬ���Ʒ��URL��ַ������������Ʒ�Ĵ�";
		}
		if (itemName.equals("ϵͳ���")) {
			pic = "/main/images/diagnosis.gif";
			dscr = "���ڶ�ϵͳ��������״���ķ�������ϣ������û�������Ͻ�����޸�ϵͳ��";
		}
		if (itemName.equals("����ƻ�")) {
			pic = "/main/images/task.gif";
			dscr = "ʵ�ּ��������ʱ��Ķ��壬�������û�����ʵ����Ҫ���ż������⼰��������ʱ�䡣";
		}
		if (itemName.equals("�ʼ�����")) {
			pic = "/main/images/email.gif";
			dscr = "���������ʼ����յ�ַ���ʼ����ͷ��������ã�Ϊ�ʼ����������ṩ�Ⱦ�������";
		}
		if (itemName.equals("��������")) {
			pic = "/main/images/sms.gif";
			dscr = "�������ж��Ž��յ�ַ�����ŷ��ͷ��������ã�Ϊ���ű��������ṩ�Ⱦ�������";
		}
		if (itemName.equals("��������")) {
			pic = "/main/images/settings.gif";
			dscr = "�������е�½�������ã������Ƿ���ҪIP��ַ��֤����ֹ�Ƿ����롣";
		}
		if (itemName.equals("ֵ�������")) {
			pic = "/main/images/maintain.gif";
			dscr = "ʵ��ֵ������Ӽ���ϸֵ���İ��ţ�֧�ֶ���ֵ������͵���ӡ�";
		}
		if (itemName.equals("�û�����")) {
			pic = "/main/images/user.gif";
			dscr = "ʵ���û�����ӣ��������õ����ü��û�Ȩ�޵ķ��䡣";
		}
		//from report
		if (itemName.equals("ͳ�Ʊ���")) {
			pic = "/main/images/report.gif";
			dscr = "�Զ����������ݽ���ͳ�Ʒ��������Զ�����/��/�±��������ͣ�֧�ֱ����Զ�������ʵʱ����չʾ��";
		}
		if (itemName.equals("���Ʊ���")) {
			pic = "/main/images/report.gif";
			dscr = "չʾѡ���ļ����������һ��ʱ���ڵ����������ͨ���ṩ��Ԥ��ֵ�������������׳�����Ĳ����������ϵͳ��������ά�����Ӷ���ֹ����ķ���ͣ���¼���";
		}
		if (itemName.equals("TopN����")) {
			pic = "/main/images/report.gif";
			dscr = "�����û��鿴һ��ʱ�䷶Χ��ĳһ�������ĳ����ָ���N������ͳ����������Զ�����/��/�±��������ͣ�֧�ֱ����Զ�������ʵʱ����չʾ��";
		}
		if (itemName.equals("״̬ͳ�Ʊ���")) {
			pic = "/main/images/report.gif";
			dscr = "�����û��鿴һ��ʱ�䷶Χ��ĳһ�������ĳ����ָ���N������ͳ����������Զ�����/��/�±��������ͣ�֧�ֱ����Զ�������ʵʱ����չʾ��";
		}
		if (itemName.equals("�Աȱ���")) {
			pic = "/main/images/contrast.gif";
			dscr = " ��ʾ�˲�ͬ�豸�ļ��������ͬʱ���ڵļ�����ݵĶԱȡ�ͨ�����ݵ���ʾ�����Զ�ѡ�������Ĳ�ָͬ�������ϸ��ʾ�����ԶԼ�����������������ϸ�İ��ա�";
		}
		if (itemName.equals("ʱ�ζԱȱ���")) {
			pic = "/main/images/contrast.gif";
			dscr = "��ʾ��ͬһ������ڲ�ͬʱ����ڵļ�����ݵĶԱȡ�ͨ�����ݵ���ʾ�����Զ�ѡ�������ڲ�ͬʱ��ε����ݽ�����ϸ��ʾ�����ԶԼ����������ϸ�İ��ա�";
		}
		if (itemName.equals("�������Ϣ����")) {
			pic = "/main/images/info.gif";
			dscr = "չʾ��������ͼ�ж�ȡ�����м������Ϣ�б����Ը���ɸѡ�������з�����ʾ��";
		}
		if (itemName.equals("SysLog��ѯ")) {
			pic = "/main/images/log.gif";
			dscr = "Syslog��ѯ������ѯ��Ӧʱ����ڣ���SyslogMsg��Ϣ��ƥ��ķ��ϲ���Facility��Level��Syslog��־��֧���û���������ɸѡ��ѯ��";
		}
		//from task
		if (itemName.equals("����ʱ������ƻ�")) {
			pic = "/main/images/task.gif";
			dscr = "����ʱ������ƻ�����ӣ�������Ҫ���ù���ʱ�䣬���������ݶ���ʱ����й�����";//�����������
		}
		if (itemName.equals("ʱ�������ƻ�")) {
			pic = "/main/images/task.gif";
			dscr = "ʱ�������ƻ�����ӣ�������Ҫ���ù���ʱ�䣬���������ݶ���ʱ����й�����";
		}
		if (itemName.equals("���ʱ������ƻ�")) {
			pic = "/main/images/task.gif";
			dscr = "���ʱ������ƻ�����ӣ�������Ҫ���ù���ʱ�䣬֧�ּ��������ʱ�����ã����������ݶ���ʱ����й�����";
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
