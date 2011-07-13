package com.siteview.ecc.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.zul.Messagebox;

/**
 * 
 * @���� :�Ի���ķ�װ
 * @������ gao_jie
 * @�������� Mar 4, 2009
 * @�汾 1.0
 * 
 */
public class Message {
	private static final Log log = LogFactory.getLog(Message.class);
	/**
	 * ��ʾ��ʾ��Ϣ
	 * 
	 * @param value
	 */
	public static void showInfo(String value) {
		try {
			Messagebox.show(value, "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
		} catch (InterruptedException e) {
			log.error(e);
		}
	}

	/**
	 * ��ʾѯ����Ϣ
	 * 
	 * @param value
	 * @return
	 */
	public static int showQuestion(String value) {
		try {
			return Messagebox.show(value, "ѯ��", Messagebox.OK | Messagebox.NO,
					Messagebox.QUESTION);
		} catch (InterruptedException e) {
			log.error(e);
		}
		return Messagebox.NO;
	}

	/**
	 * ��ʾ����
	 * 
	 * @param value
	 */
	public static void showWarning(String value) {
		try {
			Messagebox.show(value, "����", Messagebox.OK, Messagebox.EXCLAMATION);
		} catch (InterruptedException e) {
			log.error(e);
		}
	}

	/**
	 * ��ʾ����
	 * 
	 * @param value
	 */
	public static void showError(String value) {
		try {
			Messagebox.show(value, "����", Messagebox.OK, Messagebox.ERROR);
		} catch (InterruptedException e) {
			log.error(e);
		}
	}
}