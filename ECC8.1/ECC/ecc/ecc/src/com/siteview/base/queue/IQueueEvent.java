package com.siteview.base.queue;

import java.io.Serializable;

import com.siteview.actions.UserRight;

public interface IQueueEvent extends Serializable{
	public boolean filterUserRight(UserRight userRight);/*�û���Ȩ�޴�����¼�,����true*/
}
