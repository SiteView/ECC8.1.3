package com.siteview.ecc.controlpanel;

import org.zkoss.zul.impl.api.XulElement;

import com.siteview.base.treeInfo.IInfo;

public interface IEccColumnSource
{
	public ListDataBean getValue(Object rowValue);
	public int getColCount();
	public String getTitle(int idxCol);
	public boolean isNumber(int idxCol);
	public int forceColWidth(int idxCol);/*-1 is no force*/
}
