package com.siteview.ecc.log;

public class OpTypeId
{
	public static final OpTypeId	signin		= new OpTypeId("0", "登录");
	public static final OpTypeId	add			= new OpTypeId("1", "添加");
	public static final OpTypeId	edit		= new OpTypeId("2", "编辑");
	public static final OpTypeId	del			= new OpTypeId("3", "删除");
	public static final OpTypeId	enable		= new OpTypeId("4", "允许");
	public static final OpTypeId	diable		= new OpTypeId("5", "禁止");
	public static final OpTypeId	many_add	= new OpTypeId("6", "批量添加");
	public static final OpTypeId	fast_add	= new OpTypeId("7", "快速添加");
	public static final OpTypeId	paste		= new OpTypeId("8", "粘贴");
	public static final OpTypeId	confirm		= new OpTypeId("9", "事件确认");

	public String id;
	public String name;
	
	public OpTypeId(String id, String name)
	{
		this.id= id;
		this.name= name;
	}
}
