package com.siteview.ecc.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.ComposerExt;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import com.siteview.base.data.IniFile;
import com.siteview.base.data.UserEdit;
import com.siteview.base.manage.Manager;
import com.siteview.base.manage.View;
import com.siteview.base.tree.MonitorNode;
import com.siteview.ecc.start.EccStarter;

public class UpgradeFromMMC extends GenericForwardComposer implements ComposerExt
{
	private final static Logger logger = Logger.getLogger(GenericForwardComposer.class);
	
	private Label	message;
	private Textbox	name;
	private Textbox	pwd;
	private Button	btnUpgrade;
	
	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo)
	{
		// TODO Auto-generated method stub
		return compInfo;
	}
	
	@Override
	public void doBeforeComposeChildren(Component comp) throws Exception
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean doCatch(Throwable ex) throws Exception
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void doFinally() throws Exception
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void doAfterCompose(Component comp) throws Exception
	{
		super.doAfterCompose(comp);
		
		Components.addForwards(comp, this);
		
		EventListener evl = new EventListener()
		{
			public void onEvent(Event event) throws Exception
			{
				String str = (String) event.getData();
				try
				{
					message.setValue(str);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		};
		message.addEventListener("onMessage", evl);
		
		message.setValue("hello");
	}
	
	public void onClick$btnUpgrade()
	{
		String error = adminLogin();
		if (error != null)
		{
			message.setValue(error);
			return;
		} else
		{
			message.setValue("");
		}
		try
		{
			Event event = new Event("onMessage", message, "  开始升级... \n");
			Events.sendEvent(message, event);
			Clients.showBusy(null, true);
			
			upgradeUserIni();
			message.setValue("\n  数据升级成功！ \n");
			Manager.instantUpdate();
		} catch (Exception e)
		{
			e.printStackTrace();
			message.setValue("  升级失败 ： \n");
			message.setValue(e.toString());
		}
		Clients.showBusy(null, false);
	}
	
	private String adminLogin()
	{
		String error = null;
		try
		{
			ArrayList<String> array = Manager.getOnlineLoginName();
			if (array != null && !array.isEmpty())
			{
				error = "有其他用户在线，拒绝升级。请其他所有用户登出，然后执行本功能。";
				return error;
			}
			String sid = Manager.createView(name.getValue(), pwd.getValue());
			View view = Manager.getView(sid);
			if (!view.isAdmin())
				error = "您的身份不是管理员，拒绝升级。请以管理员登陆，然后执行本功能。";
			Manager.invalidateView(sid);
		} catch (WrongValueException e)
		{
			error = e.toString();
			logger.info(error);
		} catch (Exception e)
		{
			error = e.toString();
			logger.info(error);
		}
		return error;
	}
	
	private void upgradeUserIni() throws Exception
	{
		IniFile ini = new IniFile("user.ini");
		ini.load();
		// ini.display();
		
		String newname = backupUserIni();
		IniFile newini = new IniFile(newname, "defalut");
		newini.setFmap(ini.getFmap());
		newini.saveChange();
		
		List<String> a = ini.getSectionList();
		if (a == null)
			return;
		for (String key : a)
			upgradeUserIni(key);
	}
	
	private void upgradeUserIni(String section) throws Exception
	{
		IniFile ini = new IniFile("user.ini", section);
		ini.load();
		
		UserEdit user = new UserEdit(ini);
		if (user.isAdmin())
		{
			ini.deleteKey(ini.getSections(), "groupright");
			ini.deleteKey(ini.getSections(), "ungroupright");
			List<String> a = ini.getKeyList(ini.getSections());
			if (a != null)
			{
				for (String key : a)
				{
					if (key == null || key.isEmpty())
						continue;
					if (isOneSvid(key))
						ini.deleteKey(ini.getSections(), key);
				}
			}
		} else
		{
			String groupright = ini.getValue(ini.getSections(), "groupright");
			String ungroupright = ini.getValue(ini.getSections(), "ungroupright");
			ini.setKeyValue(ini.getSections(), "groupright", "");
			ini.setKeyValue(ini.getSections(), "ungroupright", "");
			
			setNodeVisible(user, groupright, true);
			setNodeVisible(user, ungroupright, false);
			
			List<String> a = ini.getKeyList(ini.getSections());
			if (a != null)
			{
				for (String key : a)
				{
					if (!isOneSvid(key))
						continue;
					MonitorNode node = new MonitorNode();
					node.setId(key);
					try
					{
						String tempNodeRight = ini.getValue(ini.getSections(), key);
						user.setNodeEditRight(node, null);
						user.setNodeEditRight(node, tempNodeRight);
					} catch (Exception e)
					{
						logger.info(e);
						user.setNodeEditRight(node, null);
					}
				}
			}
		}
		ini.saveChange();
	}
	
	private String backupUserIni()
	{
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String name = "user_backup_" + f.format(new Date()) + "_";
		String newfileName = name + "0.ini";
		
		int count = 0;
		while (true)
		{
			IniFile tryini = new IniFile(newfileName);
			try
			{
				tryini.load();
				newfileName = name + ++count + ".ini";
			} catch (Exception e)
			{
				break;
			}
		}
		return newfileName;
	}
	
	private boolean isOneSvid(String str)
	{
		if (str == null || str.isEmpty())
			return false;
		String u = str.toUpperCase();
		String l = str.toLowerCase();
		if (u.equals(l))
			return true;
		else
			return false;
	}
	
	private void setNodeVisible(UserEdit user, String right, boolean visible)
	{
		if (right == null || user == null)
			return;
		if (right.isEmpty())
			return;
		String[] s = right.split(",");
		if (s == null)
			return;
		
		int size = s.length;
		for (int i = 0; i < size; ++i)
		{
			String id = s[i];
			if (id == null || id.isEmpty())
				continue;
			MonitorNode node = new MonitorNode();
			node.setId(id);
			try
			{
				user.setNodeVisible(node, visible);
			} catch (Exception e)
			{
				logger.info(e);
			}
		}
	}
	
	public static void main(String[] args)
	{
		EccStarter s = new EccStarter("D:\\jb\\svDCM\\ecc_zk\\ecc\\WebContent");
		UpgradeFromMMC u = new UpgradeFromMMC();
		try
		{
			// u.backupUserIni();
			u.upgradeUserIni();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	// 4.8=
	// "editmonitor=0,monitorrefresh=0,delmonitor=0,addmonitor=0,applySub=0,addsongroup=0,delgroup=0,editgroup=0,grouprefresh=0,deldevice=0,editdevice=0,devicerefresh=0,copydevice=0,adddevice=0,testdevice=0,"
	// 2.35.57= "deldevice=0,editdevice=0,devicerefresh=1,copydevice=0,adddevice=0,testdevice=1,editmonitor=0,monitorrefresh=1,delmonitor=0,addmonitor=0,"
	// groupright= "2.35,2.35.7,2.35.11,2.35.13,2.35.15,2.35.17,2.35.19,2.35.21,"
	// ungroupright= "1,1.13,1.13.3,1.13.4,1.13.5,1.13.31,1.13.33,1.356,1.357,1.358,1.359,1.360,1.361,"
	
}
