package com.siteview.ecc.treeview.windows;

import java.util.List;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.siteview.base.data.IniFile;

public class UrlTransStepInfo extends GenericForwardComposer
{
	
	Listbox UrlTrans;
	Window UrlTransStepInfo;
	String monitorid;
	private static String		stepdetail_TargetUrl	= "/main/TreeView/wstepdetail.zul";
	String url;
	String limited;
	UrlTransStepData data=new UrlTransStepData();
	
	//private String	INI_FILE	= "_UrlStepInfo.ini";
	public UrlTransStepInfo()
	{
		
		
	}
	
	public void onCreate$UrlTransStepInfo()
	{
		monitorid=(String)UrlTransStepInfo.getAttribute("monitorid");
		url=(String)UrlTransStepInfo.getAttribute("url");
		limited=(String)UrlTransStepInfo.getAttribute("limitStep");
		builddata();
	}
	
	public void builddata()
	{
		UrlTransModel model=new UrlTransModel(monitorid,this);
		UrlTrans.setModel(model);
		UrlTrans.setItemRenderer(model);
	}
	//���
	public void  onClick$btnok()
	{
		int count=Integer.parseInt(limited);
		if(UrlTrans.getItemCount()>=count)
		{
			try
			{
				Messagebox.show("�����������ޣ�", "��ʾ", Messagebox.OK, Messagebox.EXCLAMATION);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		final Window win = (Window) Executions.createComponents(stepdetail_TargetUrl, null, null);
		win.setAttribute("monitorid", monitorid);
		win.setAttribute("isedit", false);
		int nindex= UrlTrans.getItemCount()+1;
		win.setAttribute("url", url);
		win.setAttribute("nIndex", Integer.toString(nindex) );
		try
		{
			win.doModal();
			//ˢ������
			builddata();
		} catch (Exception e)
		{
		}
	}
	public void onClick$btnclose()
	{
		try
		{
			UrlTransStepInfo.detach();
		}catch(Exception e)
		{}
	}
	//ɾ��
	public void onClick$btncancel()
	{
		if(UrlTrans.getSelectedCount()==0)
		{
			try
			{
				Messagebox.show("��ѡ��ɾ���У�", "��ʾ", Messagebox.OK, Messagebox.EXCLAMATION);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		if(UrlTrans.getSelectedCount()>1)
		{
			try
			{
				Messagebox.show("ֻ��ɾ��һ�У�", "��ʾ", Messagebox.OK, Messagebox.EXCLAMATION);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		if(UrlTrans.getSelectedIndex()!=(UrlTrans.getItemCount()-1))
		{
			try
			{
				Messagebox.show("ֻ��ɾ�����һ�У�", "��ʾ", Messagebox.OK, Messagebox.EXCLAMATION);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		int r;
		try
		{
			r = Messagebox.show("�Ƿ�ɾ������Ϣ��" , "ѯ��", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION);
			if (r == Messagebox.CANCEL)
			{
				return;
			}
		
		} catch (InterruptedException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		int index=UrlTrans.getItemCount();
		String nindex=Integer.toString(index);
		try
		{
			data.DeleteStep(nindex, monitorid);
			builddata();
		}
		catch(Exception e)
		{
			
		}
		
	}
	
}
