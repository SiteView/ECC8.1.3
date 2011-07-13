package com.siteview.ecc.simplereport;

public class MonitorBean
{
	private static final long	serialVersionUID	= 1L;
	private String				name;
	private String				nomal;
	private String				danger;
	private String				error;
	private String				disable;
	private String				errorvalue;
	
	public MonitorBean(String name, String nomal, String danger, String error, String disable, String errorvalue)
	{
		this.name = name;
		this.nomal = nomal;
		this.danger = danger;
		this.error = error;
		this.disable = disable;
		this.errorvalue = errorvalue;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public String getNomal()
	{
		return this.nomal;
	}
	
	public String getDanger()
	{
		return this.danger;
	}
	
	public String getError()
	{
		return this.error;
	}
	
	public String getDisable()
	{
		return this.disable;
	}
	
	public String getErrorvalue()
	{
		return this.errorvalue;
	}
	
}
