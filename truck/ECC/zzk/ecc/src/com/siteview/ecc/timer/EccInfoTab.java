package com.siteview.ecc.timer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.zkoss.zul.Tab;

import com.siteview.base.queue.IQueueEvent;
import com.siteview.base.queue.OnlineEvent;

public class EccInfoTab extends Tab implements TimerListener {
	private List<IQueueEvent> chatHistory = new CopyOnWriteArrayList<IQueueEvent>();
	public OnlineLayoutComposer onlineLayoutComposer = null;
	boolean onlyDispMsgCount=true;
	public boolean isOnlyDispMsgCount() {
		return onlyDispMsgCount;
	}

	public void setOnlyDispMsgCount(boolean onlyDispMsgCount) 
	{
		if(onlyDispMsgCount==false)
		{
			while (chatHistory.size() > 0)
				onlineLayoutComposer.echoEvent(chatHistory.remove(chatHistory
						.size() - 1));

			setLabel("系统信息");
		}
		this.onlyDispMsgCount = onlyDispMsgCount;
	}

	public void setOnlineLayoutComposer(OnlineLayoutComposer onlineLayoutComposer) {
		this.onlineLayoutComposer = onlineLayoutComposer;
	}

	public List<IQueueEvent> getChatHistory() {
		return chatHistory;
	}

	public void onCreate() {
		EccTimer ecctimer = (EccTimer) getDesktop().getPage("eccmain")
				.getFellow("header_timer");
		ecctimer.addTimerListener("onlineInfo", this);
	}

	@Override
	public void notifyChange(IQueueEvent event) {
		if (event instanceof OnlineEvent) {
			if (onlyDispMsgCount) {
				chatHistory.add(event);
				setLabel("系统信息(" + chatHistory.size() + ")");
			} else
				onlineLayoutComposer.echoEvent(event);
		}
	}
}
