package com.siteview.ecc.timer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.zkoss.zul.Timer;
import org.zkoss.zul.Tree;

import com.siteview.actions.LoginUserRight;
import com.siteview.actions.UserRight;
import com.siteview.base.manage.Manager;
import com.siteview.base.manage.View;
import com.siteview.base.queue.ChangeDetailEvent;
import com.siteview.base.queue.IQueueEvent;
import com.siteview.base.queue.OnlineEvent;
import com.siteview.base.queue.QueueManager;
import com.siteview.base.queue.SimpleQueue;
import com.siteview.base.tree.INode;
import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.treeview.EccTreeModel;
import com.siteview.ecc.util.Toolkit;

/*取得方法
 * com.siteview.ecc.timer.EccTimer timer=(com.siteview.ecc.timer.EccTimer)component.getPage("eccmain").getFellow("header_timer");
 * timer.refresh(ids,true);
 * */
public class EccTimer extends Timer {
	public Map<String, TimerListener> timerListener = new ConcurrentHashMap<String, TimerListener>();
	private String[] eccTimerOrder;
	private int origin_interval = 0;
	private long setting_last = 0;

	public String[] getEccTimerOrder() {
		if (eccTimerOrder == null)
			eccTimerOrder = EccTimerConfig.getInstance().getEccTimerOrder();
		return eccTimerOrder;
	}
	public void onCreate()
	{
		QueueManager queueManager=QueueManager.getInstance();
		UserRight userRight=Toolkit.getToolkit().getUserRight(super.getDesktop());
		if(userRight==null)
			return;
		queueManager.getQueue(this);
		OnlineEvent onlineEvent=new OnlineEvent(OnlineEvent.TYPE_ONLINE);
		if(userRight instanceof LoginUserRight)
			onlineEvent.setFromIp(((LoginUserRight)userRight).getLoginIp());
		onlineEvent.setOnOrOffUSerid(userRight.getUserid());
		onlineEvent.setOnOrOffUserName(userRight.getUserName());
		queueManager.addEvent(onlineEvent);
	}
	public View getView() {
		return Toolkit.getToolkit().getSvdbView(super.getDesktop());
	}

	public void addTimerListener(String key, TimerListener listener) {
		if(listener!=null)
			timerListener.put(key, listener);
	}

	/*
	 * actionType=ChangeDetailEvent.TYPE_ADD
	 */
	public void refresh(INode[] nodes, int actionType) {
		// ArrayList<String> idsa=getView().getChangeTree();取不到,所以删除
		for (INode node : nodes) {
			IQueueEvent event = new ChangeDetailEvent(node.getSvId(),
					actionType, node);
			QueueManager.getInstance().addEvent(event);
		}
		onTimer();

		origin_interval = super.getDelay();
		setting_last = System.currentTimeMillis();
		super.setDelay(1000);// 强制将刷新时间设置为1秒
	}

	public void refresh(List<ChangeDetailEvent> ids) {
		for (ChangeDetailEvent changeDetail : ids)
			fireEvent(changeDetail);

	}
	
	public void refresh(){
		fireEvent(new OnlineEvent(OnlineEvent.TYPE_OTHER));
	}

	public ArrayList<ChangeDetailEvent> makeDetailEvent(List<String> ids) {
		ArrayList<ChangeDetailEvent> detail = new ArrayList<ChangeDetailEvent>();

		if (ids != null) {
			EccTreeModel eccTreeModel = (EccTreeModel) ((Tree) getPage()
					.getFellow("tree")).getModel();
			
			View view=getView();
			if(view==null)
				return null;
			
			for (String id : ids) {
				INode node = view.getNode(id);
				EccTreeItem target = eccTreeModel.findNode(id);
				ChangeDetailEvent detailEvent = new ChangeDetailEvent();
				detailEvent.setSvid(id);
				detailEvent.setData(node);

				if (node == null)/* 删除 */
				{
					detailEvent.setType(ChangeDetailEvent.TYPE_DELETE);
					detail.add(detailEvent);
				} else if (target == null)/* 添加 */
				{
					detailEvent.setType(ChangeDetailEvent.TYPE_ADD);
					detail.add(detailEvent);
				} else/* 修改 */
				{
					detailEvent.setType(ChangeDetailEvent.TYPE_MODIFY);
					detail.add(detailEvent);
				}
			}
		}

		return detail;

	}

	public void refreshFromSvdb() {
		try {
			View view=getView();
			if(view==null)
				return;
			List<ChangeDetailEvent> ids = view.getChangeTree();

			if (ids != null){ 
				refresh(ids);
			}else
			{
				Date date = new Date();
				int min = date.getMinutes();
				if(min%10==0){
					refresh();
				}
			}

		} catch (Exception e1) {
			e1.printStackTrace();

		}

	}

	public void onTimer() {

		if (setting_last != 0
				&& System.currentTimeMillis() - setting_last > 5000) {
			super.setDelay(origin_interval);// 强制将刷新时间恢复
			setting_last = 0;
		}

		SimpleQueue queue = null;
		synchronized(this){
			queue = QueueManager.getInstance().getQueue(this);
		}
		
		while (queue != null) {
			IQueueEvent event = queue.popEvent();

			if (event == null)
				break;
			fireEvent(event);
		}


		refreshFromSvdb();
		

	}

	public void fireEvent(IQueueEvent event) {
		
		while(!Manager.isInstanceUpdated()){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for (String key : getEccTimerOrder()) {
			try {
				Manager.instantUpdate();
				TimerListener l = timerListener.get(key);
				if (l != null)
					synchronized(l){
						l.notifyChange(event);/* 通知改变 */
					}
				Thread.yield();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
