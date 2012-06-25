package com.siteview.ecc.system.impl;

import com.siteview.ecc.system.Diagnosis;

public class TestDiagnosisImpl extends Diagnosis {

	@Override
	public String getDescription() {
		return "test description";
	}

	@Override
	public String getName() {
		return "test-name";
	}

	@Override
	public void execute() throws Exception {
		for (int i = 0 ; i<10 ;i++){
			Thread.sleep(1000);
			getResultList().add("status :" + i);
		}
	}
}
