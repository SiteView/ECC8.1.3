package com.siteview.ecc.log;

public class OpTypeId
{
	public static final OpTypeId	signin		= new OpTypeId("0", "��¼");
	public static final OpTypeId	add			= new OpTypeId("1", "���");
	public static final OpTypeId	edit		= new OpTypeId("2", "�༭");
	public static final OpTypeId	del			= new OpTypeId("3", "ɾ��");
	public static final OpTypeId	enable		= new OpTypeId("4", "����");
	public static final OpTypeId	diable		= new OpTypeId("5", "��ֹ");
	public static final OpTypeId	many_add	= new OpTypeId("6", "�������");
	public static final OpTypeId	fast_add	= new OpTypeId("7", "�������");
	public static final OpTypeId	paste		= new OpTypeId("8", "ճ��");
	public static final OpTypeId	confirm		= new OpTypeId("9", "�¼�ȷ��");

	public String id;
	public String name;
	
	public OpTypeId(String id, String name)
	{
		this.id= id;
		this.name= name;
	}
}
