package com.siteview.base.treeEdit;


import com.siteview.base.manage.View;
import com.siteview.base.tree.*;


public interface IEdit extends INode
{
	// crud:  create, read, update, delete	

	
	/**
	 * �������ݣ��Ա� UI �༭�������б༭Ȩ��
	 */	
	boolean teleLoad() throws Exception;
	
	/**
	 * �����б༭Ȩ��
	 */
	boolean teleSave(View view) throws Exception;
	
	/**
	 * У��������Ŀ��ȷ�ԡ������Ե� �� �����б༭Ȩ��
	 *  <br/>Ŀǰ���۽������UI��У�飬������ֻҪ�б༭Ȩ�޾ͷ��� true
	 */
	boolean check();
}
