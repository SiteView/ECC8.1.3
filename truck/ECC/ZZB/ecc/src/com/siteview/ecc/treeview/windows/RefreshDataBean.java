package com.siteview.ecc.treeview.windows;

public class RefreshDataBean 
{
	
	/**
	 * 
	 */
	private String monitorname;
	private String state;
	private String dstr;
	public RefreshDataBean(String monitorname,String state,String dstr)
	{
		this.monitorname=monitorname;
		this.state=state;
		this.dstr=dstr;
		
	}
	public String getDstr()
	{
		return dstr;
	}

	public void setDstr(String dstr)
	{
		this.dstr = dstr;
	}

	public String getState()
	{
		return state;
	}

	public void setState(String state)
	{
		this.state = state;
	}

	public String getMonitorname()
	{
		return monitorname;
	}

	public void setMonitorname(String monitorname)
	{
		this.monitorname = monitorname;
	}
	
}
