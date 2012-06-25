package com.siteview.base.queue;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.zkoss.zul.Timer;

import com.siteview.actions.UserRight;
import com.siteview.ecc.util.Toolkit;

public class SimpleQueue implements Serializable {
	private static final long serialVersionUID = -3881199271123996285L;
	private List<IQueueEvent> queus = new CopyOnWriteArrayList<IQueueEvent>();
	private Timer timer = null;

	SimpleQueue(Timer timer) {
		this.timer = timer;
	}

	public Timer getTimer() {
		return timer;
	}

	public UserRight getUserRight() {
		return Toolkit.getToolkit().getUserRight(getTimer().getDesktop());
	}

	public void setUserRight(UserRight userRight) {
		Toolkit.getToolkit().setUserRight(getTimer().getDesktop().getSession(),
				userRight);
	}

	public void addEvent(IQueueEvent event) {
		queus.add(event);
	}

	public IQueueEvent popEvent() {
		if (queus.isEmpty())
			return null;
		return queus.remove(0);
	}

	public boolean hasEvent() {
		return !queus.isEmpty();
	}
}
