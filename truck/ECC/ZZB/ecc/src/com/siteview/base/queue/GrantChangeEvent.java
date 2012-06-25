package com.siteview.base.queue;

import com.siteview.actions.UserRight;

/*授权变化后触发事件*/
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
