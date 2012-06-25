package com.siteview.ecc.system;

import org.apache.log4j.Logger;

public class RunThread extends Thread {
	private final static Logger logger = Logger.getLogger(RunThread.class);

	private Diagnosis diagnosis = null;
	public RunThread(Diagnosis diagnosis){
		this.diagnosis = diagnosis;
	}
	public void run(){
		if (diagnosis != null)
			try {
				logger.info("diagnosis name : " + diagnosis.getName() + "  is started!");
				diagnosis.run();
				logger.info("diagnosis name : " + diagnosis.getName() + "  is ended!");
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}
