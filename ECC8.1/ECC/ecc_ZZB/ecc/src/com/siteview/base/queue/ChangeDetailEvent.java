package com.siteview.base.queue;

import com.siteview.actions.UserRight;
import com.siteview.base.tree.INode;
import com.siteview.ecc.treeview.EccTreeItem;

public class ChangeDetailEvent implements IQueueEvent{
	
	public static final int TYPE_ADD=0;
	public static final int TYPE_DELETE=1;
	public static final int TYPE_MODIFY=2;
	
	private String svid;
	private int type;
	private INode data;
	
	public ChangeDetailEvent(){
	}
	/* 返回值为false的user对事件彻底不处理
	 * 返回值为true的user对事件进行处理
	 * */
	@Override
	public boolean filterUserRight(UserRight userRight) {
		return (userRight.canSeeTreeNode(svid));
	}
	public ChangeDetailEvent(String svid,int type,INode data)
	{
		this.svid=svid;
		this.type=type;
		this.data=data;
	}
	public INode getData() {
		return data;
	}
	public void setData(INode node) {
		this.data = node;
	}

	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getSvid() {
		return svid;
	}
	public void setSvid(String svid) {
		this.svid = svid;
	}
}
