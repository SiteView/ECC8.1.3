package com.siteview.base.queue;

import java.util.HashMap;

import com.siteview.actions.UserRight;

public class OnlineEvent implements IQueueEvent {

	public final static int TYPE_ONLINE=0;
	public final static int TYPE_OFFLINE=1;
	public final static int TYPE_MESSAGE=2;
	public final static int TYPE_OTHER=3;
	
	private  String fromIp;	
	public String getFromIp() {
		return fromIp;
	}
	public void setFromIp(String fromIp) {
		this.fromIp = fromIp;
	}
	private  String fromUserid;
	private  String fromUserName;
	
	private  String toUSerid;
	private  String toUserName;

	private  String onOrOffUSerid;

	private  String onOrOffUserName;

	private int type=-1;
	private long createTime=System.currentTimeMillis();
	
	private String message=null;

	private HashMap<String,Object> attribute=new  HashMap<String,Object>();

	public String getOnOrOffUserName() {
		return onOrOffUserName;
	}

	public void setOnOrOffUserName(String onOrOffUserName) {
		this.onOrOffUserName = onOrOffUserName;
	}
	public String getOnOrOffUSerid() {
		return onOrOffUSerid;
	}
	public void setOnOrOffUSerid(String onOrOffUSerid) {
		this.onOrOffUSerid = onOrOffUSerid;
	}

	public String getFromUserid() {
		return fromUserid;
	}
	public void setFromUserid(String fromUserid) {
		this.fromUserid = fromUserid;
	}

	public String getFromUserName() {
		return fromUserName;
	}
	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public String getToUSerid() {
		return toUSerid;
	}
	public void setToUSerid(String toUSerid) {
		this.toUSerid = toUSerid;
	}

	public String getToUserName() {
		return toUserName;
	}
	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}
	public void setAttribute(String name,Object value)
	{
		attribute.put(name, value);
	}
	public Object getAttribute(String name)
	{
		return attribute.get(name);
	}
	public OnlineEvent(int type)
	{
		this.type=type;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public int getType() {
		return type;
	}

	public long getCreateTime() {
		return createTime;
	}
	@Override
	public boolean filterUserRight(UserRight userRight) {
		if(toUSerid==null)
			return true;/*全体*/
		else if(toUSerid.equals(userRight.getUserid()))
			return true;/*目标*/
		else  if(userRight.getUserid().equals(fromUserid))
			return true;/*自身*/
		
		return false;
	}

}
