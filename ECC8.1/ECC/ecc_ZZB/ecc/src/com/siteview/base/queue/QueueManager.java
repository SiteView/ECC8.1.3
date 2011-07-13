package com.siteview.base.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Timer;

import com.siteview.actions.LoginUserRight;
import com.siteview.actions.UserRight;
import com.siteview.ecc.start.EccStarter;
import com.siteview.ecc.start.StarterListener;

public class QueueManager implements StarterListener, Runnable, EventListener {
	private final static Logger logger = Logger.getLogger(QueueManager.class);

	private long checkQueueTimeout=1800;
	@Override
	public void onEvent(Event event) throws Exception {
		event.getTarget().setAttribute("lastAccessTime",
				System.currentTimeMillis());
	}

	Thread checkThread = null;
	private List<SimpleQueue> queusList = new CopyOnWriteArrayList<SimpleQueue>();

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(1800);

				ArrayList<SimpleQueue> removeList = new ArrayList<SimpleQueue>();
				SimpleQueue t[]=new SimpleQueue[queusList.size()];
				queusList.toArray(t);
				for (SimpleQueue queue : t) 
				{
					if(queue.getTimer()==null)
						removeList.add(queue);
					else if(queue.getTimer().getDesktop()==null)
						removeList.add(queue);
					else if(!queue.getTimer().getDesktop().isAlive())
						removeList.add(queue);
					else if (System.currentTimeMillis()
								- Long.parseLong(queue.getTimer().getAttribute(
										"lastAccessTime").toString()) > checkQueueTimeout*1000)
						removeList.add(queue);
				}	
				for(SimpleQueue queue : removeList)
					removeEvent(queue);

				Thread.yield();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void removeQueue(Timer timer) {
		for (SimpleQueue queue : queusList) 
		{
			if (queue.getTimer().equals(timer))
			{
				removeEvent(queue);
				return;
			}
		}
	}

	public SimpleQueue getQueue(Timer timer) {
		
		
		for (SimpleQueue queue : queusList) 
			if(queue.getTimer().equals(timer))
				return queue;
		
		SimpleQueue sq = new SimpleQueue(timer);
		timer.addEventListener("onTimer", this);
		timer.setAttribute("lastAccessTime", System.currentTimeMillis());
		queusList.add(sq);
		logger.info("a user sesson queue added......");
		return sq;
	}

	@Override
	public void destroyed(EccStarter starter) {
		if (checkThread != null)
		{
			checkThread.interrupt();
			checkThread=null;
		}
	}

	@Override
	public void startInit(EccStarter starter) {
		checkThread = new Thread(this);
		checkThread.setName(this.getClass().getName());
		checkThread.start();
		String time=starter.getInitParameter("checkQueueTimeoutSecond");
		if(time!=null)
			checkQueueTimeout=Integer.parseInt(time.toString());
	}
	/*更新在线用户的授权数据*/
	public void refreshUserRight(UserRight userRight)
	{
		try{
			for (SimpleQueue queue : queusList) 
			{
				if (queue.getUserRight().getUserid().equals(userRight.getUserid()))
				{
					queue.setUserRight(userRight);
					break;
				}
				
			}
		}catch(Exception e){}
	}
	

	public static QueueManager getInstance() {
		return (QueueManager) EccStarter.getInstance()
		.getStarterListener("eccQueueManage");
	}
	public int getQueusCount()
	{
		return queusList.size();
	}
	private void removeEvent(SimpleQueue queue)
	{
		queusList.remove(queue);
		UserRight userRight=queue.getUserRight();
		if(userRight==null||userRight.getUserid()==null)
			return;
		OnlineEvent onlineEvent=new OnlineEvent(OnlineEvent.TYPE_OFFLINE);
		onlineEvent.setFromIp(((LoginUserRight)userRight).getLoginIp());
		onlineEvent.setOnOrOffUSerid(userRight.getUserid());
		onlineEvent.setOnOrOffUserName(userRight.getUserName());
		addEvent(onlineEvent);
		
		logger.info("one timer is removed,OK.");
	}
	public void addEvent(IQueueEvent event) {
		SimpleQueue[] addList=new SimpleQueue[queusList.size()];
		queusList.toArray(addList);
		for (SimpleQueue queue : addList) 
		{
			try
			{
				if (event.filterUserRight(queue.getUserRight()))
						queue.addEvent(event);
			}catch(java.lang.IllegalStateException e)
			{
				removeEvent(queue);
			}

		}
	}

}
