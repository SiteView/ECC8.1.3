import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;

import com.siteview.base.data.IniFile;
import com.siteview.base.data.IniFileKeyValue;
import com.siteview.base.data.VirtualItem;
import com.siteview.base.data.VirtualView;
import com.siteview.base.manage.ManageSvapi;
import com.siteview.base.manage.Manager;
import com.siteview.base.manage.RetMapInMap;
import com.siteview.base.manage.View;
import com.siteview.base.queue.ChangeDetailEvent;
import com.siteview.base.template.EntityTemplate;
import com.siteview.base.template.TemplateManager;
import com.siteview.base.tree.IForkNode;
import com.siteview.base.tree.INode;
import com.siteview.base.treeEdit.EntityEdit;
import com.siteview.base.treeEdit.GroupEdit;
import com.siteview.base.treeEdit.MonitorEdit;
import com.siteview.base.treeEdit.SeEdit;
import com.siteview.base.treeInfo.EntityInfo;
import com.siteview.base.treeInfo.GroupInfo;
import com.siteview.base.treeInfo.MonitorInfo;
import com.siteview.base.treeInfo.SeInfo;
import com.siteview.ecc.start.EccStarter;
import com.siteview.jsvapi.Jsvapi;
import com.siteview.svdb.UnivData;

//该类的代码为例子，最终需要用 json client 重新实现


public class editTest
{
	private final static Logger logger = Logger.getLogger(editTest.class);
	private String m_session;
	
	public static void main(String[] args)
	{	
		EccStarter s= new EccStarter("D:\\jb\\svDCM\\ecc_zk\\ecc\\WebContent");
		editTest e= new editTest();
//		e.test91(true);
//		if(true)
//			return;
		try
		{
			String temp = new String("");
			while (temp.compareToIgnoreCase("q") != 0)
			{
				if (temp.compareToIgnoreCase("change") == 0)
				{
					e.getChange();
					e.getInfoChange();
				} else if (temp.compareToIgnoreCase("edit") == 0)
				{
//					e.pasteDevice();
//					e.refresh();
//					e.selectEntity();
//					e.selectGroup();		
//					e.selectMonitor();
//					e.getEntityGroup();
					
//					e.editMonitor();
//					e.editEnity();
//					e.editGroup();
//					e.editSe();
					
//					e.addMonitor();
					e.addEntity();
					
//					e.deleteNode();
					
//					e.testVirtualViewAddItem();
//					e.testCreateVirtualView();
				} else
				{
					if (e.login("admin", temp))
					{
//						e.setInfoFocus();
//						
//						e.getChange();//（ View.getChangeTree() 方法应该定时循环调用, 以保证 getNode() 等获取树基本信息的6个函数返回最新信息 ）
//						e.constructTree();	
//						e.getI18nValue();
//						e.testIni();
//						e.testVirtualView();
//						e.getDynamicData();
						
//						e.testGetSmsDllName();
					}
				}
						
				logger.info("session is: \"" + e.m_session + "\"");
				logger.info("enter password to login, q + enter to quit.");
				Scanner in = new Scanner(System.in);
				try
				{
					temp = in.nextLine();
				} catch (Exception eee)
				{
					eee.printStackTrace();
				}
			}
		} catch (Exception ee)
		{
			ee.printStackTrace();
		}		
	}
	
	public boolean login(String LoginName, String PassWord)
	{
		try
		{
			m_session = Manager.createView(LoginName, PassWord);
			return true;
		} catch (Exception e)
		{
			m_session= "";
			e.printStackTrace();
		}	
		return false;
	}
	
	public editTest()
	{
		m_session= new String();
	}
	
	public void test()
	{
		View w = Manager.getView(m_session);;	
		
		//只有用于在树上显示的最少几项信息
		INode n= w.getNode( "1.356.1" );
		
		//包含该节点的权限，以及一些统计信息，可以确定该节点的右键菜单，以及在中间区域的显示内容
		MonitorInfo e = w.getMonitorInfo(n);

		
		try
		{
			//为编辑节点取得数据，比上两步慢
			MonitorEdit m = w.getMonitorEdit(n);
		} catch (Exception e1)
		{
			e1.printStackTrace();
		}	
		
	}
	
	/**
	 * 设置 info 信息的跟踪范围
	 */
	public void setInfoFocus()
	{
		View w = Manager.getView(m_session);
		if(w==null)
			return;	
		
		String[] ids = new String[8];
		ids[0]= "1.61.41.2.9";
		ids[1]= "1.61.41.2";
		ids[2]= "1.61.41";
		ids[3]= "1.61";
		ids[4]= "1";
		ids[5]= "1.61.44.3";
		ids[6]= "1.61.44.4";
		ids[7]= "1.61.44.5";
		w.setFocusNode(ids);
	}
	
	/**
	 * 获取变化的 info 信息
	 */
	public void getInfoChange()
	{
		View w = Manager.getView(m_session);
		if(w==null)
			return;
		
		List<String> a = w.getChangeTreeInfo();
		logger.info("---------------------------");
		if (a == null)
		{
			logger.info("  nothing change of tree info! ");
		} else
		{
			for (String key : a)
			{
				logger.info(" tree info change: " + key);
				INode n = w.getNode(key);
				if (n != null)
				{
					logger.info("   "+ n.getSvId()+ " -- " + n.getType()+ " -- " + n.getStatus()+ " " );
					logger.info("   "+ " " + n.getName());
				}
			}
		}
		logger.info("---------------------------");
	}
	
	/**
	 * 获取变化的树节点；（ View.getChangeTree() 方法应该定时循环调用, 以保证 getNode() 等获取树基本信息的6个函数返回最新信息 ）
	 */
	public void getChange()
	{
		View w = Manager.getView(m_session);
		if(w==null)
			return;
		List<ChangeDetailEvent> a = w.getChangeTree();
		logger.info("---------------------------");
		if (a == null)
		{
			logger.info("  nothing change of tree base! ");
		} else
		{
			for (ChangeDetailEvent e: a)
			{
				String key= e.getSvid(); 
				logger.info("  change: " + key);
				INode n = w.getNode(key);
				if (n != null)
				{
					logger.info("   "+ n.getSvId()+ " -- " + n.getType()+ " -- " + n.getStatus()+ " " );
					logger.info("   "+ " " + n.getName());
				}
			}
		}
		logger.info("---------------------------");
		
//		try
//		{
//			a= w.getOnlineLoginName();
//			for (String key : a)
//			{
//				logger.info("  login name: " + key);
//			}
//		} catch (Exception e)
//		{
//			e.printStackTrace();
//		}
	}
	
	/**
	 * 构造整棵树
	 */
	public void constructTree()
	{
		View w = Manager.getView(m_session);
		if(w==null)
			return;
		INode[] s = w.getSe();
		if(s==null)
			return;
		int size = s.length;
		logger.info(" se sum: "+size);
		for (int i = 0; i < size; ++i)
		{
			//构造所有se
			constructNode(w, s[i]);
		}
	}
	
	/**
	 * 构造一个树节点
	 */
	private void constructNode(View w, INode n)
	{
		n.getType();
		n.getName();
		n.getStatus();
		n.getSvId();
		// 用以上几项，在浏览器里 显示一个节点，put js, put json
		logger.info(n.getSvId()+ " -- " + n.getType()+ " -- " + n.getStatus()+ " " );
		logger.info("   "+ " " + n.getName());

		if (n.getType().compareTo(INode.MONITOR)==0)
			return;
		
		IForkNode f = (IForkNode) n;
		List<String> a = f.getSonList();
		int size = a.size();
		for (int i = 0; i < size; ++i)
		{
			//递归构造所有儿子
			String id= a.get(i);
			INode node = w.getNode(id);
			if (node != null)
				constructNode(w, node);
		}
	}
	

	/**
	 * 在中间区域显示一个设备相关信息
	 */
	public void selectEntity()
	{
		View w = Manager.getView(m_session);
		if(w==null)
			return;
		
		INode n = w.getNode("1.61.52");
		if(n==null)
			return;
		
		if (n.getType().compareTo(INode.ENTITY)==0)
		{
			EntityInfo e = w.getEntityInfo(n);

			//设备的两个相关信息
			logger.info("   DeviceType: " + e.getDeviceType());
			logger.info("   IpAdress: " + e.getIpAdress());
			Session session = Executions.getCurrent().getDesktop().getSession();
			int s= e.get_sub_monitor_sum(session);
			logger.info("   sub_monitor_sum: " + s);
			logger.info("   get_sub_monitor_disable_sum: " + e.get_sub_monitor_disable_sum(session));
	
		}
		
		n= w.getNode("1");
		if(n!=null)
		{
			logger.info("   se node != null");
			SeInfo s= w.getSeInfo(n);
			logger.info("   se name: " + s.getName());
		}
	}
	
	public void pasteDevice()
	{
		View w = Manager.getView(m_session);
		if(w==null)
			return;
		
		INode n = w.getNode("1.394");
		if(n==null)
			return;
		
		if (n.getType().compareTo(INode.GROUP)==0)
		{
			GroupInfo e = w.getGroupInfo(n);
			try
			{
				String newid= e.PasteDevice("1.394.4");
				logger.info("paste device newid:  "+newid);
				INode enode = w.getNode(newid);
				while(enode==null)
				{
					logger.info("paste device newid:  "+newid + "node:" + enode);
					w.getChangeTree();
					enode = w.getNode(newid);
					Thread.sleep(500);
				}
				EntityInfo einfo= w.getEntityInfo(enode);
				
				String qname= einfo.refresh();
				while(true)
				{
					Thread.sleep(1000);
					logger.info(" queue name: "+ qname);
					RetMapInMap fmap= einfo.getRefreshedData(qname);
					Jsvapi.getInstance().DisplayUtilMapInMap(fmap.getFmap());
					if(!fmap.getRetbool())
						return;
				}
			} catch (Exception e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	public void selectGroup()
	{
		View w = Manager.getView(m_session);
		if(w==null)
			return;
		
		INode n = w.getNode("1.394");
		if(n==null)
			return;
		
		if (n.getType().compareTo(INode.GROUP)==0)
		{
			GroupInfo e = w.getGroupInfo(n);
			Session session = Executions.getCurrent().getDesktop().getSession();
			int s= e.get_sub_monitor_sum(session);
			logger.info("   sub_monitor_sum: " + s);
			logger.info("   get_sub_monitor_disable_sum: " + e.get_sub_monitor_disable_sum(session));

			logger.info(n.getSvId()+ " -- " + n.getType()+ " -- " + n.getStatus()+ " " );
			try
			{
				logger.info(e.getLableofDisableOrEnable());
//				e.disableMonitor(null, null, w);
				e.enableMonitor(w);
				
				logger.info(n.getSvId()+ " -- " + n.getType()+ " -- " + n.getStatus()+ " " );
			} catch (Exception e1)
			{
				// TODO Auto-generated catch block 
				e1.printStackTrace();
			}
		}
	}

	/**
	 * 在中间区域显示一个监测器相关信息
	 */
	public void selectMonitor()
	{
		View w = Manager.getView(m_session);;
		if(w==null)
			return;
		
		INode n = w.getNode("1.61.41.10.1");
		if(n==null)
			return;
		if (n.getType().compareTo(INode.MONITOR)==0)
		{
			MonitorInfo e = w.getMonitorInfo(n);

			//监测器的相关信息
			e.getDstr();
			e.getCreateTime();
			
			e.getMonitorTemplate().display();
			
		}
	}
	
	
	/**
	 * 编辑一个监测器
	 */
	public void editMonitor()
	{
		View w = Manager.getView(m_session);
		if(w==null)
			return;
		
		INode n = w.getNode("1.61.45.1");
		if(n==null)
			return;
		if (n.getType().compareTo(INode.MONITOR)==0)
		{
			try
			{
				MonitorEdit m = w.getMonitorEdit(n);
//				Jsvapi.DisplayUtilMap(m.getProperty());

				m.getWholeData();
				
				// ui 编辑
				
				// 校验
				m.check();
				
				// 保存
				m.teleSave(w);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	

	/**
	 * 编辑一个设备
	 */
	public void editEnity()
	{
		View w = Manager.getView(m_session);
		if(w==null)
			return;
		
		INode n = w.getNode("1.61.45");
		if(n==null)
			return;
		if (n.getType().compareTo(INode.ENTITY)==0)
		{
			try
			{
				EntityEdit m = w.getEntityEdit(n);

//				Jsvapi.DisplayUtilMap(m.getProperty());
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 编辑一个组
	 */
	public void editGroup()
	{
		View w = Manager.getView(m_session);
		if(w==null)
			return;
		
		INode n = w.getNode("1.61");
		if(n==null)
			return;
		if (n.getType().compareTo(INode.GROUP)==0)
		{
			try
			{
				GroupEdit m = w.getGroupEdit(n);

//				Jsvapi.DisplayUtilMap(m.getProperty());
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	

	/**
	 * 编辑一个se
	 */
	public void editSe()
	{
		View w = Manager.getView(m_session);
		if(w==null)
			return;
		
		INode n = w.getNode("1");
		if(n==null)
			return;
		if (n.getType().compareTo(INode.SE)==0)
		{
			try
			{
				SeEdit m = w.getSeEdit(n);
				if(m==null)
					return ;


				logger.info("   name:" +m.getName());
				Jsvapi.getInstance().DisplayUtilMap(m.getProperty());
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 获取所有设备模板组
	 */
	public void getEntityGroup()
	{
		List<String> a = TemplateManager.getAllEntityGroupId();
		int size = a.size();
		for (int i = 0; i < size; ++i)
		{
			//递归构造所有儿子
			String id= a.get(i);
//			logger.info("   eg: "+ " " + id);	
		}
	}
	
	public void getI18nValue()
	{
		try {
			logger.info("------------  I18n test  -----------");
			logger.info("IDS_Monitor_Can_not_Disable : " + UnivData.getResource("IDS_Monitor_Can_not_Disable"));
			logger.info("IDS_Test_Email_Caption: " + UnivData.getResource("IDS_Test_Email_Caption"));
			logger.info("------------  I18n test  -----------");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testIni()
	{
		IniFileKeyValue kv= new IniFileKeyValue("general.ini", "IPCheck", "name");
		try
		{
			logger.info(" IniFileKeyValue value is: " + kv.load() );
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		IniFile ini= new IniFile("general.ini","IPCheck");
		List<String> l;
		try
		{
			ini.load();
			logger.info(" IniFile value is: " + ini.getValue("IPCheck", "name"));
			
			l= ini.getSectionList();
			logger.info(" IniFile sections: " + l);
			
			l= ini.getKeyList("IPCheck");
			logger.info(" IniFile keys: " + l);
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		View w = Manager.getView(m_session);;
		if(w==null)
			return;
		ini= w.getUserIni();
		l= ini.getKeyList(ini.getSections());
		logger.info(" user.ini keys: " + l);
		
		IniFile v= new IniFile("view1.ini");
		try
		{
			v.load();
			v.display();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 添加一个新的监测器
	 */
	public void addMonitor()
	{
		View w = Manager.getView(m_session);;
		if(w==null)
			return;
		
		INode n = w.getNode("1.60.146");
		if(n==null)
			return;
		
		if (n.getType().compareTo(INode.ENTITY)==0)
		{
			EntityInfo e = w.getEntityInfo(n);
			EntityTemplate tpl= e.getDeviceTemplate();
			Map<String, String> a= tpl.getSubMonitorTemplateLabel();
			
			logger.info(" -------------------");
			logger.info(" -------------------");
			logger.info(" 右键菜单,请选择添加一种监测器 ");
			for(String tid:a.keySet())
				logger.info(" label/id ："+ a.get(tid) +"/"+ tid);
			
			try
			{
				MonitorEdit newm= e.AddMonitor("5");
				logger.info(" -------------------");
				logger.info(" new monitor type: "+ newm.getType());
				logger.info(" -------------------");
				
//				try
//				{
//					HashMap<String, String> s= newm.getDynamicData();
//					Jsvapi.DisplayUtilMap(s);
//				} catch (Exception ee)
//				{
//					// TODO Auto-generated catch block
//					ee.printStackTrace();
//				}
//				
//				newm.getProperty(); // 页面编辑该新监测器
				
				if (!newm.check())
				{
					// 新监测器校验出错了
					logger.info(" !newm.check() ");
					return;
				} else
					newm.teleSave(w); // 保存新监测器
				
				String newid= newm.getSvId();
				logger.info(" newid: "+ newid);
				
				for (int i = 0; i <= 100; ++i)
				{
					w.getChangeTree();
					n = w.getNode(newid);
					logger.info(" newid: "+ newid + " new node: "+ n);
					Thread.sleep(500);
					if(n!=null)
						return;
				}
			} catch (Exception e1)
			{
				e1.printStackTrace();
			}
		}
	}


	/**
	 * 添加一个新的设备
	 */	
	public void addEntity()
	{
		View w = Manager.getView(m_session);
		if (w == null)
			return;
		
		INode n = w.getNode("1.394");
		if (n.getType().compareTo(INode.GROUP) == 0)
		{
			GroupInfo gi = w.getGroupInfo(n);
			
			logger.info(" -------------------");
			logger.info(" -------------------");
			logger.info(" 右键菜单,请选择添加一种设备 ");
			Map<String, Map<String, String>> a = TemplateManager.getEntityGroupTemplateLabel();
			for (String gid : a.keySet())
			{
				logger.info(" 设备组 ：" + gid);
				Map<String, String> e = a.get(gid);
				for (String eid : e.keySet())
				{
					logger.info("    label/id ：" + e.get(eid) + "/" + eid);
				}
			}
			
			try
			{
				EntityEdit newm= gi.AddDevice("_win");
				logger.info(" -------------------");
				logger.info(" new entity type: "+ newm.getType());
				logger.info(" -------------------");
				
				newm.getProperty(); // 页面编辑该新设备
				
				if (!newm.check())
				{
					// 新设备校验出错了
				} else
					newm.teleSave(w); // 保存新设备
				
			} catch (Exception e1)
			{
				e1.printStackTrace();
			}
		}
	}

	public void testCreateVirtualView()
	{
		View w = Manager.getView(m_session);
		if (w == null)
			return;
		
		logger.info(" ---------  testCreateVirtualView ---------");
		try
		{
			w.createVirtualView("虚拟视图测试3");
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testVirtualViewAddItem()
	{
		View w = Manager.getView(m_session);
		if (w == null)
			return;
		
		logger.info(" ---------  testVirtualViewAddItem ---------");
		List<VirtualView> av= w.getAllVirtualView();
		for(VirtualView v: av)
		{
			if(!v.getViewName().equals("虚拟视图3"))
				continue;
			logger.info(" ------- view name:" + v.getViewName());
			
			List<VirtualItem> vis= v.getTopItems();
			for(VirtualItem vi:vis)
				showSonVirtualItem(v, vi, "");
			
			try
			{
//				v.addItem(v.getRootItemId(), new ZulItem("Report", "报表", ""), null);

//				v.addItem("1", new ZulItem(VirtualItem.VirtualGroup.zulType, "这是一个不存在的虚拟组", ""), null);
//				v.deleteItem("3");
				
				INode node= w.getNode("1.173.4.1.1");
				v.addINode("1.5", node, true);
				v.addINode("1.4", node, true);

				
//				INode node= w.getNode("1.61.50");
//				v.changeINode("1.2", "1", node, true);
//				v.changeItem("2.1", "2", new ZulItem("ReportContrast", "对比报告", "m_SetshowSystemReport"), null);
				
				v.saveChange();
				
//				w.changeUserOfVirtualView(v, "563", false);
//				w.deleteVirtualView(v);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		

	}
	
	public void testVirtualView()
	{
		View w = Manager.getView(m_session);
		if (w == null)
			return;
		
		logger.info(" ---------  testVirtualView ---------");
		List<VirtualView> av= w.getAllVirtualView();
		for(VirtualView v: av)
		{
			if(!v.getViewName().equals("虚拟视图测试3"))
				continue;
			logger.info(" ------- view name:" + v.getViewName());
//			try
//			{
//				w.deleteVirtualView(v);
////				w.changeNameOfVirtualView(v, "虚拟视图测试2");
//			} catch (Exception e)
//			{
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			
			v.getViewName().equals(VirtualView.DefaultView);
			
			List<VirtualItem> vis= v.getTopItems();
			for(VirtualItem vi:vis)
				showSonVirtualItem(v, vi, "");
		}
	}
	
	public void showSonVirtualItem(VirtualView vv, VirtualItem vi, String head)
	{
		if (vi.getType().equals(VirtualView.Item))
			logger.info(head+ " item: "+  vi.getItemDataZulName() + " / " + vi.getItemDataZulType()+ " / " + vi.getItemId() );
		if (vi.getType().equals(VirtualView.INode))
		{
			View w = Manager.getView(m_session);
			if (w != null)
			{
				INode node = w.getNode(vi.getSvId());
				logger.info(head + " inode: " + vi.getSvId() + " / " + node.getName()+ " /itemid: " + vi.getItemId() );
			}
		}
//		//设置为 “自动添加所有子监测器“ 的节点，不显示子节点
//		if(vi.isWithAllSubMonitor())
//			return;
		
		List<VirtualItem> vis= vv.getSonItems(vi);
		for(VirtualItem v:vis)
			showSonVirtualItem(vv, v, head + "    ");
	}
	
	public void getDynamicData()
	{
		View w = Manager.getView(m_session);
		if (w == null)
			return;
		
		logger.info(" ---------  getDynamicData ---------");
		INode node= w.getNode("1.61.41.10.8");
		try
		{
			MonitorEdit m = w.getMonitorEdit(node);

			m.startMonitorDynamicData(w);
			Map<String, String> dydata = null;
			while(dydata==null)
			{
				logger.info(" get monitor Dynamic Data ");
				Thread.sleep(1000);
				dydata= m.getMonitorDynamicData(w);
			}
			Jsvapi.getInstance().DisplayUtilMap(dydata);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void deleteNode()
	{
		View w = Manager.getView(m_session);
		if (w == null)
			return;
		
		logger.info(" ---------  deleteNode ---------");
		INode node= w.getNode("1.61.41.13");
		try
		{
			EntityInfo m = w.getEntityInfo(node);
			m.deleteNode(w);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void refresh()
	{
		View w = Manager.getView(m_session);
		if (w == null)
			return;
		
		INode node= w.getNode("1.356.3");
		MonitorInfo m= w.getMonitorInfo(node);
		String qname="";
		try
		{
			qname= m.refresh();
			while(true)
			{
				Thread.sleep(1000);
				logger.info(" queue name: "+ qname);
				RetMapInMap fmap= m.getRefreshedData(qname);
				Jsvapi.getInstance().DisplayUtilMapInMap(fmap.getFmap());
				if(!fmap.getRetbool())
					return;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		logger.info(" qname: "+qname);
	}
	
	public void test91(boolean show)
	{
		logger.info("\ntry EmailTest ...");
		HashMap<String, String> ndata = new HashMap<String, String>();
		
		ndata.put("dowhat", "EmailTest");
		ndata.put("mailServer", "mail.dragonflow.com");
		ndata.put("mailTo", "jiantang.chen@dragonflow.com");
		
		ndata.put("mailFrom", "jiantang.chen@dragonflow.com"); 
		ndata.put("user", "jiantang.chen@dragonflow.com");
		ndata.put("password", "jiantang.chen");
		ndata.put("subject", "test java");
		ndata.put("content", "test java");
		
		// EmailAlert.dll 120k的那个可以发出去; 但 568k 的那个报 80040202 号错误( c++程序调用却没有这个错 )
		RetMapInMap rmap = ManageSvapi.GetUnivData(ndata);
		
		if (!show)
			return;
		
		Jsvapi.getInstance().DisplayUtilMapInMap(rmap.getFmap());
		logger.info("GetUnivData:" + rmap.getRetbool());
		logger.info("estr:" + rmap.getEstr());
	}
	
	public void testGetSmsDllName()
	{
		logger.info("\ntry GetSmsDllName ...");
		HashMap<String, String> ndata = new HashMap<String, String>();
		
		ndata.put("dowhat", "GetSmsDllName");
		RetMapInMap rmap = ManageSvapi.GetUnivData(ndata);
		
		Jsvapi.getInstance().DisplayUtilMapInMap(rmap.getFmap());
		logger.info("GetUnivData:" + rmap.getRetbool());
		logger.info("estr:" + rmap.getEstr());	
	}
}
