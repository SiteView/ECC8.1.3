package com.siteview.ecc.dutytable;

/**
 * 说明 关键字分两大类 值班表设置 (值班表的添加，编辑) 值班表详细信息设置(值班表详细信息的添加,编辑)
 * 不同的关键字是不能混用的
 */

public class DutyConstant {
	
	//值班表设置
	public static final String  Add_DutySet_Section 			= "Add_DutySet_Section";//state 1
	public static final String  Edit_DutySet_Section      		= "Edit_DutySet_Section";//state 2
	
	//值班表详细信息设置
	//state 3
	public static final String  Add_DutyFather_Section 				= "Add_DutyFather_Section";//增加父类的section
	public static final String  Add_DutySon_Section					= "Add_DutySon_Section";//增加子类的section

	//state 4
	public static final String  Edit_DutyFather_Section 			= "Edit_DutyFather_Section";//编辑父类的section
	public static final String  Edit_DutySon_Section		      	= "Edit_DutySon_Section";//编辑子类的section
	
	
	/**
	 * 注意点 设置 每一个 Section 值的时候 一定要设置 state 的状态值
	 */
	public static final String State                                =	"State";
}