package com.siteview.base.treeEdit;


import com.siteview.base.manage.View;
import com.siteview.base.tree.*;


public interface IEdit extends INode
{
	// crud:  create, read, update, delete	

	
	/**
	 * 加载数据，以便 UI 编辑，必须有编辑权限
	 */	
	boolean teleLoad() throws Exception;
	
	/**
	 * 必须有编辑权限
	 */
	boolean teleSave(View view) throws Exception;
	
	/**
	 * 校验输入项目正确性、完整性等 ， 必须有编辑权限
	 *  <br/>目前讨论结果，由UI层校验，本函数只要有编辑权限就返回 true
	 */
	boolean check();
}
