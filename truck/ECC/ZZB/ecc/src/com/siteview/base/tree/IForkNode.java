package com.siteview.base.tree;

import java.util.List;

public interface IForkNode extends INode
{
	void setSonId(String sonid);
	String getSonId();
	List<String> getSonList();
}
