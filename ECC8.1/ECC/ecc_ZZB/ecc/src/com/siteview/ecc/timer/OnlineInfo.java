package com.siteview.ecc.timer;

public class OnlineInfo {
	private EccStatistic eccStatistic=null;
	public OnlineInfo()
	{
		this.eccStatistic=EccStatistic.getInstance();
	}
	public void refresh()
	{
		deskCount=eccStatistic.getActiveDesktopCount();
		sessionCount=eccStatistic.getActiveSessionCount();
		updateCount=eccStatistic.getActiveUpdateCount();
		average_deskCount=eccStatistic.getAverageDesktopCount();
		average_sessionCount=eccStatistic.getAverageSessionCount();
		average_updateCount=eccStatistic.getAverageUpdateCount();
		long startTime=eccStatistic.getStartTime();
		total_deskCount=eccStatistic.getTotalDesktopCount();
		total_sessionCount=eccStatistic.getTotalSessionCount();
		total_updateCount=eccStatistic.getTotalUpdateCount();
	}
	
	int deskCount=0;
	public int getDeskCount() {
		return deskCount;
	}

	int sessionCount=0;
	public int getSessionCount() {
		return sessionCount;
	}

	int updateCount=0;
	public int getUpdateCount() {
		return updateCount;
	}

	int total_deskCount=0;
	public int getTotal_deskCount() {
		return total_deskCount;
	}

	int total_sessionCount=0;
	public int getTotal_sessionCount() {
		return total_sessionCount;
	}

	int total_updateCount=0;
	public int getTotal_updateCount() {
		return total_updateCount;
	}

	double average_deskCount=0;
	public double getAverage_deskCount() {
		return average_deskCount;
	}

	double average_sessionCount=0;
	public double getAverage_sessionCount() {
		return average_sessionCount;
	}

	double average_updateCount=0;
	public double getAverage_updateCount() {
		return average_updateCount;
	}

	long startTime=0;
	public long getStartTime() {
		return startTime;
	}
}
