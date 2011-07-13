package com.siteview.ecc.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.sys.WebAppCtrl;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Image;
import org.zkoss.zul.Include;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;

import com.siteview.actions.LoginUserRight;
import com.siteview.actions.UserRight;
import com.siteview.base.manage.View;
import com.siteview.base.queue.QueueManager;
import com.siteview.base.tree.INode;
import com.siteview.base.treeInfo.EntityInfo;
import com.siteview.base.treeInfo.GroupInfo;
import com.siteview.base.treeInfo.MonitorInfo;
import com.siteview.base.treeInfo.SeInfo;
import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.treeview.EccTreeModel;
import com.siteview.svecc.zk.test.SVDBViewFactory;

public class Toolkit 
{
	public static Toolkit getToolkit()
	{
		return new Toolkit();
	}
	public View getSvdbView(Desktop desktop)
	{
		if(desktop==null)
			desktop=Executions.getCurrent().getDesktop();
		return getSvdbView(desktop.getSession());
	}
	public View getSvdbView(HttpSession session)
	{
		
		Object usersessionid = session.getAttribute("usersessionid");
		if (usersessionid == null) {
			Executions.getCurrent().sendRedirect("/index.jsp", "_top");
			return null;
		}

		try 
		{
			View view=SVDBViewFactory.getView(usersessionid.toString());
			return view;			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public View getSvdbView(Session session)
	{
		return getSvdbView((HttpSession)session.getNativeSession());
	}

	public String getINodeDesc(Session session,INode node)
	{
		if(node==null)
			return "";
		if(node instanceof GroupInfo)
		{
			StringBuilder sb=new StringBuilder();
			sb.append("设备总数:").append(((GroupInfo)node).get_sub_entity_sum(session)).append(",");
			sb.append("监测器总数:").append(((GroupInfo)node).get_sub_monitor_sum(session)).append(",");
			sb.append("其中正常数:").append(((GroupInfo)node).get_sub_monitor_ok_sum(session)).append(
					",");
			sb.append("错误数:").append(((GroupInfo)node).get_sub_monitor_error_sum(session)).append(
					",");
			sb.append("危险数:").append(((GroupInfo)node).get_sub_monitor_warning_sum(session))
					.append(",");
			sb.append("禁止数:").append(((GroupInfo)node).get_sub_monitor_disable_sum(session));
			return sb.toString();
		}
		else if(node instanceof MonitorInfo)
			return ((MonitorInfo)node).getDstr();
		else if(node instanceof EntityInfo)
		{
			StringBuilder sb=new StringBuilder();
			sb.append("监测器总数:").append(((EntityInfo)node).get_sub_monitor_sum(session)).append(",");
			sb.append("其中正常数:").append(((EntityInfo)node).get_sub_monitor_ok_sum(session)).append(
					",");
			sb.append("错误数:").append(((EntityInfo)node).get_sub_monitor_error_sum(session)).append(
					",");
			sb.append("危险数:").append(((EntityInfo)node).get_sub_monitor_warning_sum(session))
					.append(",");
			sb.append("禁止数:").append(((EntityInfo)node).get_sub_monitor_disable_sum(session));
			return sb.toString();
		}
		else if(node instanceof SeInfo)
		{
			StringBuilder sb=new StringBuilder();
			sb.append("设备总数:").append(((SeInfo)node).get_sub_entity_sum(session)).append(",");
			sb.append("监测器总数:").append(((SeInfo)node).get_sub_monitor_sum(session)).append(",");
			sb.append("其中正常数:").append(((SeInfo)node).get_sub_monitor_ok_sum(session)).append(
					",");
			sb.append("错误数:").append(((SeInfo)node).get_sub_monitor_error_sum(session)).append(
					",");
			sb.append("危险数:").append(((SeInfo)node).get_sub_monitor_warning_sum(session))
					.append(",");
			sb.append("禁止数:").append(((SeInfo)node).get_sub_monitor_disable_sum(session));
			return sb.toString();
		}
		else
			 return getINodeDesc(session,(INode)getInfoObject(getSvdbView(session),node));
	}
	public String getHoverImage(String src)
	{
		int idx=src.lastIndexOf(".");
		return new StringBuilder(src.substring(0,idx)).append("_hover.gif").toString();
	}
	public int changeStatusToInt(String status)
	{
		if(status.equals(INode.OK))
			return EccTreeItem.STATUS_OK;
		else if(status.equals(INode.ERROR))
			return EccTreeItem.STATUS_ERROR;
		else if(status.equals(INode.WARNING))
			return EccTreeItem.STATUS_WARNING;
		else if(status.equals(INode.DISABLE))
			return EccTreeItem.STATUS_DISABLED;
		else if(status.equals(INode.BAD))
			return EccTreeItem.STATUS_BAD;
		else
			return EccTreeItem.STATUS_NULL;
		
	}
	public String changeStatusToChinese(int status)
	{
		switch(status)
		{
			case EccTreeItem.STATUS_OK:
				return "正常";
			case EccTreeItem.STATUS_ERROR:
				return "错误";
			case EccTreeItem.STATUS_WARNING:
				return "危险";
			case EccTreeItem.STATUS_DISABLED:
				return "禁止";
			case EccTreeItem.STATUS_BAD:
				return "定义错误";
			case EccTreeItem.STATUS_NULL:
				return "无数据";
		}
		return "null";	
	}
	public String changeStatusToString(int status)
	{
		switch(status)
		{
			case EccTreeItem.STATUS_OK:
				return INode.OK;
			case EccTreeItem.STATUS_ERROR:
				return INode.ERROR;
			case EccTreeItem.STATUS_WARNING:
				return INode.WARNING;
			case EccTreeItem.STATUS_DISABLED:
				return INode.DISABLE;
			case EccTreeItem.STATUS_BAD:
				return INode.BAD;
			case EccTreeItem.STATUS_NULL:
				return INode.NULL;
	
		}
		return INode.NULL;
	}
	public INode getInfoObject(View view,INode inode)
	{
		if(view==null || inode==null)
			return null;
		if(inode.getType().equals(INode.ENTITY))
			return view.getEntityInfo(inode);
		else if(inode.getType().equals(INode.MONITOR))
			return view.getMonitorInfo(inode);
		else if(inode.getType().equals(INode.SE))
			return view.getSeInfo(inode);
		else if(inode.getType().equals(INode.GROUP))
			return view.getGroupInfo(inode);
		else
			return null;
	}
	public void expandTreeAndShowList(Desktop desktop,INode iinfo)
	{
		Desktop curDeskTop = Executions.getCurrent().getDesktop();
		if(desktop==null || desktop.equals(curDeskTop))
			desktop=Executions.getCurrent().getDesktop();
		
		if(autoExpandTreeNode((Tree)desktop.getPage("eccmain").getFellow("tree"),iinfo))
		{
			Include 
			 eccBody=(Include)desktop.getPage("eccmain").getFellow("eccBody");
			 eccBody.setSrc(new StringBuffer("/main/control/eccbody.zul?type=").append(
					 iinfo.getType()).append("&id=").append(iinfo.getSvId())
						.toString());

		}
	}
	public boolean autoExpandTreeNode(Tree tree,INode iinfo) {
		EccTreeModel eccTreeModle=(EccTreeModel)tree.getModel();
		if(eccTreeModle==null || iinfo==null)
			return false;
		EccTreeItem eccTreeItem = eccTreeModle.findNode(iinfo.getSvId());

		ArrayList<EccTreeItem> pathList = new ArrayList<EccTreeItem>();
		if(eccTreeModle.getPathList(pathList, eccTreeItem)==false)
			return false;

		Treechildren treechildren = tree.getTreechildren();/* 从树的根开始处理 */
		Treeitem treeitem = null;
		EccTreeItem eccTreeitem = null;
		try{
			for (EccTreeItem needOpen : pathList) /* 所有需要展开的节点在pathList中 */
			{
				if(treechildren == null){
					continue;
				}
				for (Object obj : treechildren.getItems()) {
					treeitem = (Treeitem) obj;
					eccTreeitem = (EccTreeItem) treeitem.getValue();
					if (needOpen.getId().equals(eccTreeitem.getId())) {
						treeitem.setOpen(true);
						treechildren = treeitem.getTreechildren();
						break;
					}
				}

			}
		}catch(Exception e){
			e.printStackTrace();
		}

		if (treeitem != null && eccTreeitem != null) {
			tree.selectItem(treeitem);
			// Include
			// eccBody=(Include)desktop.getPage("eccmain").getFellow("eccBody");
			// eccBody.invalidate();
			return true;

		}
		
		return false;
	}
	
	/**
	 * 在使用虚拟视图时，点击Listbox中的列表或者下划线时选中树中节点
	 * @param tree
	 * @param iinfo
	 * @param itemId
	 * @return boolean
	 */
	public boolean autoExpandTreeNode(Tree tree, INode iinfo, String itemId) {
		EccTreeModel eccTreeModle=(EccTreeModel)tree.getModel();
		if(eccTreeModle==null || iinfo==null)
			return false;
		EccTreeItem eccTreeItem = eccTreeModle.findNodeInVirtrualView(iinfo.getSvId(), itemId);

		ArrayList<EccTreeItem> pathList = new ArrayList<EccTreeItem>();
		if(eccTreeModle.getPathList(pathList, eccTreeItem)==false)
			return false;

		Treechildren treechildren = tree.getTreechildren();/* 从树的根开始处理 */
		Treeitem treeitem = null;
		EccTreeItem eccTreeitem = null;
		try{
			for (EccTreeItem needOpen : pathList) /* 所有需要展开的节点在pathList中 */
			{
				if(treechildren == null){
					continue;
				}
				for (Object obj : treechildren.getItems()) {
					treeitem = (Treeitem) obj;
					eccTreeitem = (EccTreeItem) treeitem.getValue();
					if (needOpen.getId().equals(eccTreeitem.getId())) {
						treeitem.setOpen(true);
						treechildren = treeitem.getTreechildren();
						break;
					}
				}

			}
		}catch(Exception e){
			e.printStackTrace();
		}

		if (treeitem != null && eccTreeitem != null) {
			tree.selectItem(treeitem);
			return true;
		}
		
		return false;
	}
	
	public String getStatusStyle(int status)
	{
		switch(status)
		{
			case EccTreeItem.STATUS_ERROR:
				return "border:none;color:#000000;background-color:#FF5952";
			case EccTreeItem.STATUS_OK:
				return "border:none;color:#000000;background-color:#BDEF84";
			case EccTreeItem.STATUS_WARNING:
				return "border:none;color:#000000;background-color:#FFD763";
			case EccTreeItem.STATUS_DISABLED:
				return "border:none;color:#000000;background-color:#E78E84";
			case EccTreeItem.STATUS_BAD:
				return "border:none;color:#000000;background-color:#D6D7D6";
			case EccTreeItem.STATUS_NULL:
				return "border:none;color:#FFFFFF;background-color:#717171";
			case EccTreeItem.STATUS_ALL:
				return "border:none;color:#000000;background-color:#FFFFFF";
			case EccTreeItem.STATUS_ERROR|EccTreeItem.STATUS_WARNING:
				return "border:none;color:#000000;background-color:#FFDBC6";
			case EccTreeItem.STATUS_BAD|EccTreeItem.STATUS_ERROR|EccTreeItem.STATUS_NULL:
				return "border:none;color:#000000;background-color:#FF5952";
		}
		return "border:none;color:#000000;background-color:#FFFFFF";
	}
	public UserRight getUserRight(Desktop desktop)
	{
		if(desktop==null)
			desktop=Executions.getCurrent().getDesktop();
		return getUserRight(desktop.getSession());
	}
	public UserRight getUserRight(Session session)
	{
		return getUserRight((HttpSession)session.getNativeSession());
	}
	public void setUserRight(Session session,UserRight userRight)
	{
		session.setAttribute("userright",userRight);
	}
	public LoginUserRight getUserRight(HttpSession session)
	{
		LoginUserRight userright=(LoginUserRight)session.getAttribute("userright");
		if(userright==null)
		{
			View view=getSvdbView(session);
			if(view==null)
				return null;
			String userid=view.getLoginData().get("section");
			userright=new LoginUserRight(userid);
			userright.setAdmin(view.isAdmin());
			session.setAttribute("userright",userright);
		}
		return userright;
	}
	public void cleanSession(HttpSession session)
	{
		Enumeration enum1=session.getAttributeNames();
		ArrayList<String> list=new ArrayList<String>(); 
		while(enum1.hasMoreElements())
			list.add(enum1.nextElement().toString());
		for(String name:list)
			session.removeAttribute(name);
	}
	
	/**
	 *  用户 F5 刷新页面时，销毁相关资源，避免长期运行条件下的资源泄露
	 */
	public void InvalidateEccMainPage(HttpSession session, String usersessionid)
	{
		if(session==null || usersessionid==null)
			return;
		Desktop desktop= (Desktop)session.getAttribute("desktop_" + usersessionid);
		removeDesktopAndSession(desktop);		
		cleanSession(session);
		session.setAttribute("usersessionid", usersessionid);
	}
	
	/**
	 * 销毁 desktop 
	 */
	public void removeDesktop(Desktop desktop)
	{
		destroyDesktopSession(desktop, false);
	}
	
	/**
	 * 销毁 desktop 和 zksession
	 */
	public void removeDesktopAndSession(Desktop desktop)
	{
		destroyDesktopSession(desktop, true);
	}
	
	/**
	 * 销毁 desktop zksession
	 * @param willDestroySession 是否要销毁 zksession
	 */
	private void destroyDesktopSession(Desktop desktop, boolean willDestroySession)
	{
		if (desktop == null)
			return;
		
		try
		{
			WebAppCtrl ctrl = (WebAppCtrl) desktop.getWebApp();
			org.zkoss.zk.ui.Session zkSession = desktop.getSession();
			if (zkSession != null)
			{
				if (ctrl.getDesktopCache(zkSession) != null && desktop.isAlive())
				{
					try
					{
						ctrl.getDesktopCache(zkSession).removeDesktop(desktop);
					} catch (Exception e)
					{
						e.printStackTrace();
					}
				}
				if (willDestroySession)
					ctrl.sessionDestroyed(zkSession);
			}
		} catch (Exception e2)
		{
			e2.printStackTrace();
		}
		
	}
	public void showOK(String msg)
	{
		Clients.evalJavaScript("alert('"+msg+"!')");
	}
	public void showError(String msg)
	{
		try{
		Messagebox.show(msg,"提示", Messagebox.OK, Messagebox.ERROR);
		}catch(Exception e)
		{
			e.printStackTrace(System.err);
		}
	}
	public String getCookie(String key)
	{
		return getCookie(key,Executions.getCurrent().getDesktop());
	}
	public String getCookie(String key,Desktop desktop)
	{
		if(desktop==null)
			return null;
		HttpServletRequest req = (HttpServletRequest) desktop.getExecution().getNativeRequest();
		return getCookie(key,req);
	}
	public String getCookie(String key,HttpServletRequest req)
	{
		if(req.getCookies()!=null)
		for (Cookie cookie : req.getCookies())
			if (cookie.getName().equals(key))
				return cookie.getValue();
		return null;
	}
	public void setCookie(String key,String value,int validTime)
	{
		setCookie(key,value,Executions.getCurrent().getDesktop(),validTime);
	}
	public void setCookie(String key,String value,Desktop desktop,int validTime)
	{
		HttpServletResponse resp = (HttpServletResponse) desktop.getExecution().getNativeResponse();
		setCookie(key,value,resp,validTime);
	}
	public void setCookie(String key,String value,HttpServletResponse resp,int validTime)
	{
		Cookie cookie = new Cookie(key, value);
		cookie.setPath("/");
		cookie.setMaxAge(validTime);
		resp.addCookie(cookie);
	}
	public Image createImage(BufferedImage bi)
	{
		String id="sessionChart:"+System.currentTimeMillis();
		try{
			Executions.getCurrent().getDesktop().getSession().setAttribute(id, bi);
		}catch(Exception e){
			
		}
		return new Image("/main/report/createImage.jsp?id="+id);
	}
	public void removeCurrentQueue()
	{
		
		Execution execution=Executions.getCurrent();
		if(execution!=null)
		{
			Desktop desktop=execution.getDesktop();
			if(desktop!=null)
			{
				if(desktop.hasPage("eccmain"))
				{
					Page page=(Page)desktop.getPage("page");
					if(page.hasFellow("header_timer"))
					{
						Timer timer=(Timer)page.getFellow("header_timer");
						QueueManager.getInstance().removeQueue(timer);
					}
				}
			}
		}
				
	}
	/**
	 ** 
	 * 返回毫秒
	 * 
	 * @param date
	 *            日期
	 * @return 返回毫秒
	 */
	public long getMillis(java.util.Date date)
	{
		return date.getTime();
		/*java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(date);
		return c.getTimeInMillis();*/
	}
	
	/**
	 * 日期相减
	 * 
	 * @param date
	 *            日期
	 * @param day
	 *            天数
	 * @return 返回相减后的日期
	 */
	public java.util.Date delDay(java.util.Date date, int day)
	{
		return new Date(date.getTime()- 24L*60*60*1000*day );
	}
	public java.util.Date delTime(java.util.Date date, long time)
	{
		return new Date(date.getTime()-time);
		
	}

	public String formatDate(Date date)
	{
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}
	public String formatDate(Date date,String format)
	{
		return new SimpleDateFormat(format).format(date);
	}
	public String formatDate()
	{
		return formatDate(new Date());
	}
	public String formatDate(long time)
	{
		return formatDate(new Date(time));
	}
	public Date parseDate(String date) throws ParseException
	{
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
	}
	/*
	 *   logger.log(Level.SEVERE, "嚴重訊息");
		  logger.log(Level.WARNING, "警示訊息");
		  logger.log(Level.CONFIG, "一般訊息");
		  logger.log(Level.CONFIG, "設定方面的訊息");
		  logger.log(Level.FINE, "細微的訊息");
		  logger.log(Level.FINER, "更細微的訊息");
		  logger.log(Level.FINEST, "最細微的訊息");
	 */
	public Logger getLoger()
	{
		return Logger.getLogger("eccweb813");
	}
	
	/**
	 * 删除一个目录下的所有文件
	 * @param path
	 * @return
	 */
	public void deleteFolder(String path)
	{
		File file = new File(path);
		if(file==null || !file.exists()) return;
		if(file.isFile()) 
		{
			file.delete();
		}else if(file.isDirectory())
		{
			File[] subFile = file.listFiles();
			if(subFile==null || subFile.length<=0) return;
			for(File f :subFile)
			{
				deleteFolder(f.getPath());
			}
			file.delete();
		}
	}
	
	public static void main(String[] args)
	{
		String tt = "D:\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\ecc\\main\\report\\statreport\\20090908_1252387485531.html_files";
		Toolkit.getToolkit().deleteFolder(tt);
	}
}
