package com.siteview.ecc.timer;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkex.zul.Center;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Space;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;

import com.siteview.actions.LoginUserRight;
import com.siteview.actions.UserRight;
import com.siteview.base.queue.IQueueEvent;
import com.siteview.base.queue.OnlineEvent;
import com.siteview.base.queue.QueueManager;
import com.siteview.ecc.util.Toolkit;

public class OnlineLayoutComposer extends GenericForwardComposer {
	private Grid chartGrid;
	private Div chatContent;
	private Rows	chartRows;
	private Vbox onlineInfoDiv;
	private Textbox sendMsg;
	private Button btnSend;
	private Center chatCenter;
	EccInfoTab info_tab;
	Tab tab_monitor;
	Tab tab_view;
	int oldTimerDelay=0;
	@Override
	public void doAfterCompose(Component comp) throws Exception
	{
		super.doAfterCompose(comp);
		self.setAttribute("Composer", this);
		EccTimer ecctimer=(EccTimer)desktop.getPage("eccmain").getFellow("header_timer");
		this.oldTimerDelay=ecctimer.getDelay();
		
		refreshOnlineInfo();
		
		sendMsg.addEventListener("onOK", getSendListener());
		btnSend.addEventListener("onClick", getSendListener());
		info_tab.setOnlineLayoutComposer(this);
		
		tab_monitor.addEventListener("onSelect", getTabSelectListener());
		tab_view.addEventListener("onSelect", getTabSelectListener());
		info_tab.addEventListener("onSelect", getTabSelectListener());
		
	}
	private EventListener getTabSelectListener()
	{
		EventListener evl=new EventListener(){
			@Override
			public void onEvent(Event event) throws Exception 
			{
				EccTimer ecctimer=(EccTimer)desktop.getPage("eccmain").getFellow("header_timer");
				int delay=ecctimer.getDelay();
				
				if(event.getTarget().equals(tab_monitor))
				{
					info_tab.setOnlyDispMsgCount(true);
					ecctimer.setDelay(oldTimerDelay);
				}else if(event.getTarget().equals(tab_view))
				{
					info_tab.setOnlyDispMsgCount(true);
					ecctimer.setDelay(oldTimerDelay);
				}else if(event.getTarget().equals(info_tab))
				{
					oldTimerDelay=ecctimer.getDelay();
					ecctimer.setDelay(1000);
					info_tab.setOnlyDispMsgCount(false);

				}
			}};
		return evl;
	}
	public void echoEvent(IQueueEvent event)
	{
		refreshOnlineInfo();
		if(event instanceof OnlineEvent)
		{
			Row rowitem = new Row();
			Div row=new Div();
			rowitem.appendChild(row);
			
//			chatContent.appendChild(row);
//			if(chatContent.getChildren().size()>100)
//				chatContent.removeChild(chatContent.getFirstChild());
			this.chartRows.appendChild(rowitem);
			OnlineEvent e=(OnlineEvent)event;
			rowitem.setValue(e.getCreateTime());
			rowitem.setZclass("none");
			switch(e.getType())
			{
				case OnlineEvent.TYPE_ONLINE:
					row.appendChild(new Label(e.getOnOrOffUserName()));
					row.appendChild(new Label("上线"));
					break;
				case OnlineEvent.TYPE_OFFLINE:
					row.appendChild(new Label(e.getOnOrOffUserName()));
					row.appendChild(new Label("下线"));
					break;
				case OnlineEvent.TYPE_MESSAGE:
					Label sender=new Label();
					sender.setStyle("font-weight:bold;");
					if(e.getFromUserid()==null)
						sender.setValue("系统：");
					else	
						sender.setValue(e.getFromUserName()+"：");
					row.appendChild(sender);
					row.appendChild(new Label(e.getMessage()));
					break;
					
			}
			Space space=new Space();
			space.setSpacing("10px");
			row.appendChild(space);
			
			Calendar calendar=Calendar.getInstance();
			calendar.setTimeInMillis(e.getCreateTime());
//			row.appendChild(createTimeLabel(calendar.get(Calendar.HOUR_OF_DAY)+"时"));
//			row.appendChild(createTimeLabel(calendar.get(Calendar.MINUTE)+"分"));
//			row.appendChild(createTimeLabel(calendar.get(Calendar.SECOND)+"秒"));
			
			row.appendChild(createTimeLabel(Toolkit.getToolkit().formatDate(e.getCreateTime())));
			row.appendChild(new Space());
			row.appendChild(createIpLabel(((OnlineEvent) event).getFromIp()));
			
			row.appendChild(new Separator());
			List<Row> rows = new ArrayList<Row>();
			for(Object obj : chartRows.getChildren()){
				if(obj instanceof Row){
					rows.add((Row)obj);
				}
			}
			Collections.sort(rows, new Comparator<Row>(){

				@Override
				public int compare(Row o1, Row o2) {
					long d1 = 0;
					long d2 = 0;
					try {
						d1 = Long.parseLong(o1.getValue().toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						d2 = Long.parseLong(o2.getValue().toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
					return d1<d2?1:-1;
				}});
			chatCenter.smartUpdate("scrollTop", "10000");
		}
	}
	private Label createIpLabel(String value)
	{
		Label lbl=new Label();
		lbl.setValue(value);
		lbl.setStyle("font-style:italic;color:gray");
		return lbl;
	}

	private Label createTimeLabel(String value)
	{
		Label lbl=new Label();
		lbl.setValue(value);
		lbl.setStyle("font-style:italic;color:##416AA3");
		return lbl;
	}
	private EventListener getSendListener()
	{
		EventListener l=new EventListener(){
			@Override
			public void onEvent(Event event) throws Exception 
			{
				if(sendMsg.getValue().trim().length()==0)
					return;
				UserRight userRight=Toolkit.getToolkit().getUserRight(desktop);
				OnlineEvent oe=new OnlineEvent(OnlineEvent.TYPE_MESSAGE);
				if(userRight instanceof LoginUserRight)
					oe.setFromIp(((LoginUserRight)userRight).getLoginIp());
				oe.setMessage(sendMsg.getValue());
				oe.setFromUserid(userRight.getUserid());
				oe.setFromUserName(userRight.getUserName());
				sendMsg.setValue("");
				sendMsg.setFocus(true);
				QueueManager.getInstance().addEvent(oe);
			}
			
		};
		
		return l;
	}
	
	public void refreshOnlineInfo()
	{
		while(onlineInfoDiv.getLastChild()!=null)
			onlineInfoDiv.removeChild(onlineInfoDiv.getLastChild());

		EccStatistic eccStatistic=EccStatistic.getInstance();
		
		Hbox hbox=new Hbox();
		onlineInfoDiv.appendChild(hbox);
		
		Calendar calendar=Calendar.getInstance();
		calendar.setTimeInMillis(eccStatistic.getStartTime());
		StringBuffer sb=new StringBuffer("启动时间：");
		sb.append(calendar.get(calendar.YEAR)).append("年");
		sb.append(calendar.get(calendar.MONTH)+1).append("月");
		sb.append(calendar.get(calendar.DAY_OF_MONTH)).append("日");
		hbox.appendChild(new Label(sb.toString()));
		hbox.appendChild(new Space());
		sb=new StringBuffer();
		sb.append(calendar.get(calendar.HOUR_OF_DAY)).append("时");
		sb.append(calendar.get(calendar.MINUTE)).append("分");
		sb.append(calendar.get(calendar.SECOND)).append("秒");
		hbox.appendChild(new Label(sb.toString()));
		
		hbox=new Hbox();
		onlineInfoDiv.appendChild(hbox);
		calendar.setTimeInMillis(System.currentTimeMillis());
		sb=new StringBuffer("当前时间：");
		sb.append(calendar.get(calendar.YEAR)).append("年");
		sb.append(calendar.get(calendar.MONTH)+1).append("月");
		sb.append(calendar.get(calendar.DAY_OF_MONTH)).append("日");
		hbox.appendChild(new Label(sb.toString()));
		hbox.appendChild(new Space());
		sb=new StringBuffer();
		sb.append(calendar.get(calendar.HOUR_OF_DAY)).append("时");
		sb.append(calendar.get(calendar.MINUTE)).append("分");
		sb.append(calendar.get(calendar.SECOND)).append("秒");
		hbox.appendChild(new Label(sb.toString()));
				
		File win = new File("/");
		hbox=new Hbox();
		onlineInfoDiv.appendChild(hbox);
		hbox.appendChild(new Label("内存总量："+(int)(Runtime.getRuntime().maxMemory()/1048576)+"Mb,"));
		hbox.appendChild(new Label("可用内存："+(int)(Runtime.getRuntime().freeMemory()/1048576)+"Mb,"));
		hbox.appendChild(new Label("磁盘总量："+(int)(win.getTotalSpace()/1048576)+"Mb,"));
		hbox.appendChild(new Label("剩余磁盘："+(int)(win.getFreeSpace()/1048576)+"Mb"));
		
		hbox=new Hbox();
		onlineInfoDiv.appendChild(hbox);
		hbox.appendChild(new Label("   web容器活动桌面数："+eccStatistic.getActiveDesktopCount()+","));
		hbox.appendChild(new Label("活动会话数："+eccStatistic.getActiveSessionCount()+","));		
		hbox.appendChild(new Label("历史桌面数："+eccStatistic.getTotalDesktopCount()+","));
		hbox.appendChild(new Label("历史会话数："+eccStatistic.getTotalSessionCount()+""));
		//hbox.appendChild(new Label("平均会话数："+eccStatistic.getAverageSessionCount()));
	}
}
