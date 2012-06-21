package com.siteview.ecc.simplereport;

public class HistoryBean
{
	private String				name;
	private String				datev;
	private String				destr;
	private String              state;

	/**
	 * 
	 * @param name
	 * @param returnvalue
	 * @param maxvalue
	 * @param averagevalue
	 * @param lastestvalue
	 */
	public HistoryBean(String name, String datev, String destr,String state)
	{
		this.name = name;
		this.datev = datev;
		this.destr = destr;
		this.state=state;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public String getDate()
	{
		return this.datev;
	}
	
	public String getDestr()
	{
		return this.destr;
	}
	public String getState()
	{
		return state;
	}
	
}
