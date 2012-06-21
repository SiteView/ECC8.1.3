package com.siteview.ecc.simplereport;

public class StatisticsBean implements  java.io.Serializable
{
	private String				name;
	private String				returnvalue;
	private String				maxvalue;
	private String				averagevalue;
	private String				lastestvalue;
	/**
	 * 
	 * @param name
	 * @param returnvalue
	 * @param maxvalue
	 * @param averagevalue
	 * @param lastestvalue
	 */
	public StatisticsBean(String name, String returnvalue, String maxvalue, String averagevalue, String lastestvalue)
	{
		this.name = name;
		this.returnvalue = returnvalue;
		this.maxvalue = maxvalue;
		this.averagevalue = averagevalue;
		this.lastestvalue = lastestvalue;
	}
	public StatisticsBean(String returnvalue, String maxvalue, String averagevalue, String lastestvalue)
	{
		this.returnvalue = returnvalue;
		this.maxvalue = maxvalue;
		this.averagevalue = averagevalue;
		this.lastestvalue = lastestvalue;
	}
	public String getName()
	{
		return this.name;
	}
	
	public String getReturnvalue()
	{
		return this.returnvalue;
	}
	
	public String getMaxvalue()
	{
		return this.maxvalue;
	}
	
	public String getAveragevalue()
	{
		return this.averagevalue;
	}
	
	public String getLastestvalue()
	{
		return this.lastestvalue;
	}
	
	
}
