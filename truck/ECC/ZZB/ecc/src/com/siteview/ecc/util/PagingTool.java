package com.siteview.ecc.util;

import org.zkoss.zul.Paging;

public class PagingTool extends Paging 
{
	
	public String getInfoTags() 
	{
		
		String old=super.getInfoTags();
		int idx=old.lastIndexOf("</div>");
		if(idx==-1)
			return old;		
		
		StringBuffer sb = new StringBuffer();
		sb.append(old.substring(0,idx));
		sb.append("&nbsp;&nbsp;Ã¿Ò³<input onchange='setZKAttr(document.getElementById(\""+this.getUuid()+"\"),\"pgsz\",this.value)' type='text' size='2' value='"+super.getPageSize()+"'>Ìõ");
		sb.append("</div>");
		
		return sb.toString();
	}

	public PagingTool() {
		super();
		super.setAutohide(true);
	}
}
