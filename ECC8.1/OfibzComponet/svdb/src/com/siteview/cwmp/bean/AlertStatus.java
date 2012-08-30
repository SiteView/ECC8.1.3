package com.siteview.cwmp.bean;

public enum AlertStatus {
	NoProcess,				//����
	Revert,					//�ѻָ�
	Warning,				//����
	Processed;				//�Ѵ���
	
	public static AlertStatus getAlertStatus(String name){
		if (Revert.toString().equals(name)) return Revert;
		if (Warning.toString().equals(name)) return Warning;
		if (Processed.toString().equals(name)) return Processed;
		return NoProcess;
	}
}
