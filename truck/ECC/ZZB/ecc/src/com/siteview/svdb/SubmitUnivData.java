package com.siteview.svdb;

public class SubmitUnivData extends BaseSvdb {
	
	public static SubmitUnivData getInstance()
	{
		return new SubmitUnivData();
	}
	
	//  提交给 svdb 服务器的数据/及返回数据     传入的请求       返回的错误信息 便于调试					
	// 以下 submit 函数，若传入的 fmap 中的 return 有 id= XXX， 则修改服务器端已有节点的数据 ，否则创建一个新节点（该动作内嵌了权限控制,并返回该新节点的 id）
	// fmap 提供的数据用于覆盖服务器端的数据，fmap 如果是服务器端的数据的子集，则服务器端 是否 删除与 fmap 对应的补集 ，决定于 del_supplement .
	
	/*
	 * dowhat= SubmitGroup ,		del_supplement= true/false (是否删除补集, 默认为 true ，即若不填为 true ) , parentid= XXX (新建节点时需要此值);
	 * */
	public  boolean SubmitGroup(boolean del_supplement,String parentid) throws Exception
	{
		throw new Exception("No implements now!!");
	}		

	
	//dowhat= SubmitEntity ,		del_supplement= true/false , parentid= XXX (新建节点时需要此值) ;
	public  boolean SubmitEntity(boolean del_supplement,String parentid) throws Exception
	{
		throw new Exception("No implements now!!");
	}	
	//dowhat= SubmitMonitor ,		del_supplement= true/false , parentid= XXX (新建节点时需要此值)  , autoCreateTable= true/false (默认为 false ，即若不填为 false );
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

	//dowhat= AddManyMonitor ,	    parentid= XXX ,    autoCreateTable= true/false (默认为 false ，即若不填为 false );
	public  boolean AddManyMonitor(String parentid, boolean autoCreateTable) throws Exception
	{
		throw new Exception("No implements now!!");
	}
	//dowhat= AdvanceAddManyMonitor ,	autoCreateTable= true/false (默认为 false ，即若不填为 false );
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

	//写之前会先清空该 section 
	//假如有个 section: user1, 则在map: fmap.user1 中保存所有string型value,  在map: fmap.(INT_VALUE)user1 中保存所有 int 型value
	// user 通常为 default ,当为 idc 用户时须传入 user 
	//dowhat= WriteIniFileSection, filename= XXX,  user= XXX,	section= XXX    
	public  boolean WriteIniFileSection(String filename,String user,String section) throws Exception
	{
		throw new Exception("No implements now!!");
	}


	// 添加或修改一个或多个虚拟项目
	//修改 viewItem (或按传入的 item_id 创建)，校验时只要有错整个请求都拒绝，保存前会先清空该 item 原先的数据，例如 item_id: 1.2,  则在 fmap."1.2" 中的数据如下
	//                        sv_id= XXX,  type= XXX,  withAllSubMonitor= true (默认为 false )
	//由后台创建 viewItem，例如 item_id: index-1,  则在map: fmap."index-1" 中的数据如下
	//                       parent_item_id= XXX,  sv_id= XXX,  type= XXX,  withAllSubMonitor= true (默认为 false )
	//修改报警、报告等 viewItem (或按传入的 item_id 创建)，保存前会先清空该 item 原先的数据，例如 item_id: report-1,  则在map: fmap."report-1" 中的数据如下
	//                       CheckNothing= true,   YY= XX,  YYY= XXX, ... 可以是任意数据
	//dowhat= AddViewItem ,    fileName= XXX,
	public  boolean AddViewItem(String filename) throws Exception
	{
		throw new Exception("No implements now!!");
	}
}
