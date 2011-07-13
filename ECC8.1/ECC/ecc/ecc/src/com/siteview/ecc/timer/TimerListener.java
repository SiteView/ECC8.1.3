package com.siteview.ecc.timer;

import com.siteview.base.queue.IQueueEvent;

public interface TimerListener {
	public void notifyChange(IQueueEvent event);
}
