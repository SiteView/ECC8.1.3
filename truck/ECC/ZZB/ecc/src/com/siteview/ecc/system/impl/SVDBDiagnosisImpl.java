package com.siteview.ecc.system.impl;

import com.siteview.ecc.system.Diagnosis;
import com.siteview.svdb.UnivData;

public class SVDBDiagnosisImpl extends Diagnosis {
	@Override
	public String getDescription() {
		return "��� svdb server ����ͨ��,�Լ�  svdb �Ƿ�����.";
	}

	@Override
	public String getName() {
		return "svdb ���";
	}

	@Override
	public void execute() throws Exception {
		String ip = UnivData.getSvdbAddr();
		getResultList().add("svdb server : " + ip);
	}

}
