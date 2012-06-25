package com.siteview.ecc.treeview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zul.AbstractTreeModel;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.event.TreeDataEvent;

import com.siteview.base.data.VirtualItem;
import com.siteview.base.data.VirtualView;
import com.siteview.base.manage.Manager;
import com.siteview.base.manage.View;
import com.siteview.base.queue.ChangeDetailEvent;
import com.siteview.base.queue.IQueueEvent;
import com.siteview.base.tree.IForkNode;
import com.siteview.base.tree.INode;
import com.siteview.ecc.timer.TimerListener;
import com.siteview.ecc.util.Toolkit;
import com.siteview.svecc.zk.test.SVDBViewFactory;

public class DefaultViewTreeModel extends EccTreeModel  
{	
	public DefaultViewTreeModel(EccTreeItem root) 
	{
		super(root);
	}
	
	public static void removeInstance(Session session)
	{
		session.removeAttribute("DefaultViewTreeModelForVirtualViewEdditting_"+VirtualView.DefaultView);
	}
	public static void removeInstance(Session session,String rootid)
	{
		session.removeAttribute("DefaultViewTreeModelForVirtualViewEdditting_"+rootid);
	}
	public static DefaultViewTreeModel getInstance(Session session) 
	{
		return getInstance(session,VirtualView.DefaultView);
	}
	public static DefaultViewTreeModel getInstance(Session session,String rootid) 
	{
		DefaultViewTreeModel model=(DefaultViewTreeModel)session.getAttribute("DefaultViewTreeModelForVirtualViewEdditting_"+rootid);
		if(model==null)
		{
			model = new DefaultViewTreeModel(new EccTreeItem(null,rootid,"ÕûÌåÊ÷",""));
//			session.setAttribute("DefaultViewTreeModelForVirtualViewEdditting_"+rootid, model);
		}
	
		return model;
	}
}
