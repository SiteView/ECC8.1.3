package com.siteview.base.queue;

import java.io.Serializable;

import com.siteview.actions.UserRight;

public interface IQueueEvent extends Serializable{
	public boolean filterUserRight(UserRight userRight);/*用户有权限处理该事件,返回true*/
}
