/**
 * 
 */
package com.siteview.ecc.treeview.report;

import java.util.ArrayList;

import com.siteview.base.tree.INode;
/**
 * 简化生成不带虚视图的树,对应PredigestTreeItem.java
 * 代码从PredigestTreeItem.java拷贝过来,去除status相关部分
 * @author : di.tang
 * @date: 2009-3-18
 * @company: siteview
 */
public class PredigestTreeItem {
	private String title;
	private String id;
	private String parentid;
	private String type;
	private ArrayList<PredigestTreeItem> childRen = new ArrayList<PredigestTreeItem>();

	private INode value;
	private int status = 11;

	public INode getValue() {
		return value;
	}

	public void setValue(INode value) {
		this.value = value;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ArrayList<PredigestTreeItem> getChildRen() {
		return childRen;
	}

	public void setChildRen(ArrayList<PredigestTreeItem> childRen) {
		this.childRen = childRen;
	}

	public String toString() {
		return title;
	}

	public PredigestTreeItem(String parentid, String id, String title,
			String type) {
		this.parentid = parentid;
		this.id = id;
		this.title = title;
		this.type = type;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void addChild(PredigestTreeItem treeItem) {
		getChildRen().add(treeItem);
	}

	public void deleteChild(PredigestTreeItem treeItem) {
		getChildRen().remove(treeItem);
	}

	public void updateChild(String id, PredigestTreeItem theItem) {
		for (int i = 0; i < getChildRen().size(); i++) {
			if (getChildRen().get(i).getId().equals(id)) {
				// 树变化时，只改它自己 （还有孙子呢）
				getChildRen().get(i).setTitle(theItem.getTitle());
				// getChildRen().remove(i);
				// getChildRen().add(i,theItem);
			}
		}
	}
}