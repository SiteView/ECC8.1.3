package com.siteview.ecc.tuopu;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

import com.siteview.base.data.IniFile;
import com.siteview.base.manage.View;
import com.siteview.ecc.log.AppendOperateLog;
import com.siteview.ecc.log.OpObjectId;
import com.siteview.ecc.log.OpTypeId;
import com.siteview.ecc.treeview.EccWebAppInit;
import com.siteview.ecc.util.Toolkit;

public class TuopuList extends GenericForwardComposer
{	
	private final static Logger logger = Logger.getLogger(TuopuList.class);
	//�����б����
	private Button		btnDel;
	private Button		btnRsort;
	private Button		btnRefresh;
	private Listbox     tuopListBox;
	
	//�༭����ͼ��ʾ����
	private Textbox     showname;
	private Textbox     name;
	private Button		btnUpdateName;
	private Window      winEditTuopu;
	private Window 		winTuopulist;
	
	public TuopuList()
	{
		
	}
	
	public void onCreate$TuopuList()
	{
		
	}
	
	//�༭����ͼ��ʾ�������
	public void onClick$btnUpdateName(Event event)
	{
		IniFile ini = new IniFile("tuopfile.ini");
		try 
		{
			ini.load();
			if("".equals(showname.getValue().trim())){
				Messagebox.show("��ʾ���Ʋ���Ϊ�գ�", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
				return;
			}
			ini.setKeyValue("filename", name.getValue(), showname.getValue().trim());
			ini.saveChange();			
			
			View view = Toolkit.getToolkit().getSvdbView(Executions.getCurrent().getDesktop());
			String loginname = view.getLoginName();
			String minfo = loginname + "��" + OpObjectId.tupo_view.name + "�н�����"+OpTypeId.edit.name+""+OpObjectId.tupo_view.name+"����";
			AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.edit, OpObjectId.tupo_view);

			
			//���ڹ���ʱ����������б�ؼ��� ���ﲻ��ֱ�ӵ��ã� ��Ϊ�ڿؼ��ڲ�ͬ��ҳ�档
//			Listbox tmpBox = (Listbox)winEditTuopu.getAttribute("tuopuListBox");
			
			Grid tmpBox = (Grid)winEditTuopu.getAttribute("tuopList");
			
			//��������
			winEditTuopu.detach();
			
//			Events.sendEvent(new Event("SomethingHappens", 
//					self.getDesktop().getPage("").getFellow("main")));
//			Listbox tmpBox = ((Listbox)self.getDesktop().getPage("tuopulist").getFellow("winTuopulist").getFellow("tuopListBox"));
			
			//ˢ�������б�
			((TuopulistModel)tmpBox.getModel()).refresh();		
			tmpBox.invalidate();
//			onClick$btnRefresh();			
		}
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//ɾ��ѡ�е�����ͼ
	public void onClick$btnDel()
	{
		try 
		{
			String strPath = EccWebAppInit.getWebDir() + "\\main\\tuoplist\\";
			String strName = "", strTmp = "";
			
			if(tuopListBox.getSelectedItem() == null){
				Messagebox.show("��ѡ����Ӧ������ͼ","��ʾ",Messagebox.OK,Messagebox.INFORMATION);
			}else{
				Iterator itr = tuopListBox.getSelectedItems().iterator();
				
				IniFile ini = new IniFile("tuopfile.ini");
				ini.load();

				if(Messagebox.OK == Messagebox.show("�Ƿ�ɾ����ѡ��ͼ��", "ѯ��", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION))
				{
			        while (itr.hasNext()) 
			        {
			        	Listitem item = (Listitem) itr.next();
			            if (item != null) 
			            {
			            	//ɾ��ѡ������ͼ�ļ���
			            	strName = item.getAttribute("htmlname").toString();
			            	strName = item.getAttribute("htmlname").toString().substring(0, strName.length() - 4);
			            	strTmp = strPath + strName + ".htm";
			            	MakeTuopuData.delFile(strTmp);
			            	strTmp = strPath + strName + ".vsd";
			            	MakeTuopuData.delFile(strTmp);
			            	strTmp = strPath + strName + ".files\\";
			            	MakeTuopuData.delFolder(strTmp);
			            	
			            	//ɾ��ѡ������ͼ��ini�����Ϣ
			    			ini.deleteKey("filename", strName + ".htm");
			    			ini.deleteKey("filename", strName + ".vsd");
			            	
//			            	item.setParent(null);
//			            	tuopListBox.removeItemFromSelection(item);
//			            	tuopListBox.removeItemFromSelectionApi(item);
			            }
			        }
					View view = Toolkit.getToolkit().getSvdbView(Executions.getCurrent().getDesktop());
					String loginname = view.getLoginName();
					String minfo = loginname + "��" + OpObjectId.tupo_view.name + "�н�����"+OpTypeId.del.name+""+OpObjectId.tupo_view.name+"����";
					AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.del, OpObjectId.tupo_view);
			        ini.saveChange();
			        ((TuopulistModel)tuopListBox.getModel()).refresh();
//			        tuopListBox.clearSelection();		        
//			        tuopListBox.getPage().invalidate();
			        tuopListBox.invalidate();
				}
			}
			
			        
	        	        
		}
		catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 		
		
	}
	
	//���û�ָ����˳����������ͼ
	public void onClick$btnRsort()
	{
		try 
		{
			Messagebox.show("��ʱֻ�ü�����", "ѯ��", Messagebox.OK, Messagebox.QUESTION);
		}
		catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	public void onCreate$winTuopulist(){
		Timer timer = new Timer();
		timer.setRepeats(true);
		timer.setDelay(3*60*1000);
		timer.addEventListener(Events.ON_TIMER, new EventListener(){
			@Override
			public void onEvent(Event arg0) throws Exception {
				logger.info("system refresh toplist*****************");
				onClick$btnRefresh();
			}
		});
		winTuopulist.appendChild(timer);
	}
	//ˢ������ͼ�б�
	public void onClick$btnRefresh()
	{
		((TuopulistModel)tuopListBox.getModel()).refresh();		
		tuopListBox.getPage().invalidate();
	}
}
