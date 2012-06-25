package com.siteview.base.queue;

import com.siteview.actions.UserRight;

/*��Ȩ�仯�󴥷��¼�*/
public class GrantChangeEvent implements IQueueEvent {

	UserRight newUserRight=null;
	public UserRight getNewUserRight() {
		return newUserRight;
	}

	public GrantChangeEvent(UserRight newUserRight) {
		super();
		this.newUserRight=newUserRight;
	}

	@Override
	public boolean filterUserRight(UserRight userRight) {
		return userRight.getUserid().equals(newUserRight.getUserid());
		
	}

}
