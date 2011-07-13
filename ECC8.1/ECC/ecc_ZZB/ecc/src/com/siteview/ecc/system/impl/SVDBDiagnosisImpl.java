package com.siteview.ecc.system.impl;

import com.siteview.ecc.system.Diagnosis;
import com.siteview.svdb.UnivData;

public class SVDBDiagnosisImpl extends Diagnosis {
	@Override
	public String getDescription() {
		return "检测 svdb server 的联通性,以及  svdb 是否正常.";
	}

	@Override
	public String getName() {
		return "svdb 检测";
	}

	@Override
	public void execute() throws Exception {
		String ip = UnivData.getSvdbAddr();
		getResultList().add("svdb server : " + ip);
	}

}
