package com.siteview.base.tree;


public interface INode
{
	String	ROOT	= "";
	String	UNKNOWN	= "unknown";
	
	String	SE		= "se";
	String	GROUP	= "group";
	String	ENTITY	= "entity";
	String	MONITOR	= "monitor";
	
	String	OK		= "ok";
	String	ERROR	= "error";
	String	WARNING	= "warning";
	String	DISABLE	= "disable";
	String	NULL	= "null";
	String	BAD		= "bad";
	
	String getType();
	String getName();
	String getSvId();	
	String getParentSvId();
	String getStatus();
}
