package com.siteview.svdb;

public class SubmitUnivData extends BaseSvdb {
	
	public static SubmitUnivData getInstance()
	{
		return new SubmitUnivData();
	}
	
	//  �ύ�� svdb ������������/����������     ���������       ���صĴ�����Ϣ ���ڵ���					
	// ���� submit ������������� fmap �е� return �� id= XXX�� ���޸ķ����������нڵ������ �����򴴽�һ���½ڵ㣨�ö�����Ƕ��Ȩ�޿���,�����ظ��½ڵ�� id��
	// fmap �ṩ���������ڸ��Ƿ������˵����ݣ�fmap ����Ƿ������˵����ݵ��Ӽ������������ �Ƿ� ɾ���� fmap ��Ӧ�Ĳ��� �������� del_supplement .
	
	/*
	 * dowhat= SubmitGroup ,		del_supplement= true/false (�Ƿ�ɾ������, Ĭ��Ϊ true ����������Ϊ true ) , parentid= XXX (�½��ڵ�ʱ��Ҫ��ֵ);
	 * */
	public  boolean SubmitGroup(boolean del_supplement,String parentid) throws Exception
	{
		throw new Exception("No implements now!!");
	}		

	
	//dowhat= SubmitEntity ,		del_supplement= true/false , parentid= XXX (�½��ڵ�ʱ��Ҫ��ֵ) ;
	public  boolean SubmitEntity(boolean del_supplement,String parentid) throws Exception
	{
		throw new Exception("No implements now!!");
	}	
	//dowhat= SubmitMonitor ,		del_supplement= true/false , parentid= XXX (�½��ڵ�ʱ��Ҫ��ֵ)  , autoCreateTable= true/false (Ĭ��Ϊ false ����������Ϊ false );
	public  boolean SubmitMonitor(boolean del_supplement,String parentid) throws Exception
	{
		throw new Exception("No implements now!!");
	}	
	
	//dowhat= SubmitSVSE  ;
	public  boolean SubmitSVSE() throws Exception
	{
		throw new Exception("No implements now!!");
	}	
	//dowhat= SubmitTask ,		del_supplement= true/false ;
	public  boolean SubmitTask(boolean del_supplement) throws Exception
	{
		throw new Exception("No implements now!!");
	}

	//dowhat= AddManyMonitor ,	    parentid= XXX ,    autoCreateTable= true/false (Ĭ��Ϊ false ����������Ϊ false );
	public  boolean AddManyMonitor(String parentid, boolean autoCreateTable) throws Exception
	{
		throw new Exception("No implements now!!");
	}
	//dowhat= AdvanceAddManyMonitor ,	autoCreateTable= true/false (Ĭ��Ϊ false ����������Ϊ false );
	public  boolean AdvanceAddManyMonitor(boolean autoCreateTable) throws Exception
	{
		throw new Exception("No implements now!!");
	}

	//dowhat= SetValueInManyMonitor ,	 id= X1,X2, ... X10
	public  boolean SetValueInManyMonitor(String[] id) throws Exception
	{
		throw new Exception("No implements now!!");
	}
	//dowhat= AppendOperateLog
	public  boolean AppendOperateLog() throws Exception
	{
		throw new Exception("No implements now!!");
	}

	//д֮ǰ������ո� section 
	//�����и� section: user1, ����map: fmap.user1 �б�������string��value,  ��map: fmap.(INT_VALUE)user1 �б������� int ��value
	// user ͨ��Ϊ default ,��Ϊ idc �û�ʱ�봫�� user 
	//dowhat= WriteIniFileSection, filename= XXX,  user= XXX,	section= XXX    
	public  boolean WriteIniFileSection(String filename,String user,String section) throws Exception
	{
		throw new Exception("No implements now!!");
	}


	// ��ӻ��޸�һ������������Ŀ
	//�޸� viewItem (�򰴴���� item_id ����)��У��ʱֻҪ�д��������󶼾ܾ�������ǰ������ո� item ԭ�ȵ����ݣ����� item_id: 1.2,  ���� fmap."1.2" �е���������
	//                        sv_id= XXX,  type= XXX,  withAllSubMonitor= true (Ĭ��Ϊ false )
	//�ɺ�̨���� viewItem������ item_id: index-1,  ����map: fmap."index-1" �е���������
	//                       parent_item_id= XXX,  sv_id= XXX,  type= XXX,  withAllSubMonitor= true (Ĭ��Ϊ false )
	//�޸ı���������� viewItem (�򰴴���� item_id ����)������ǰ������ո� item ԭ�ȵ����ݣ����� item_id: report-1,  ����map: fmap."report-1" �е���������
	//                       CheckNothing= true,   YY= XX,  YYY= XXX, ... ��������������
	//dowhat= AddViewItem ,    fileName= XXX,
	public  boolean AddViewItem(String filename) throws Exception
	{
		throw new Exception("No implements now!!");
	}
}
