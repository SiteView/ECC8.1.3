package com.siteview.ecc.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.zul.Messagebox;

/**
 * 
 * @功能 :对话框的封装
 * @创建人 gao_jie
 * @创建日期 Mar 4, 2009
 * @版本 1.0
 * 
 */
public class Message {
	private static final Log log = LogFactory.getLog(Message.class);
	/**
	 * 显示提示信息
	 * 
	 * @param value
	 */
	public static void showInfo(String value) {
		try {
			Messagebox.show(value, "提示", Messagebox.OK, Messagebox.INFORMATION);
		} catch (InterruptedException e) {
			log.error(e);
		}
	}

	/**
	 * 显示询问信息
	 * 
	 * @param value
	 * @return
	 */
	public static int showQuestion(String value) {
		try {
			return Messagebox.show(value, "询问", Messagebox.OK | Messagebox.NO,
					Messagebox.QUESTION);
		} catch (InterruptedException e) {
			log.error(e);
		}
		return Messagebox.NO;
	}

	/**
	 * 显示警告
	 * 
	 * @param value
	 */
	public static void showWarning(String value) {
		try {
			Messagebox.show(value, "警告", Messagebox.OK, Messagebox.EXCLAMATION);
		} catch (InterruptedException e) {
			log.error(e);
		}
	}

	/**
	 * 显示错误
	 * 
	 * @param value
	 */
	public static void showError(String value) {
		try {
			Messagebox.show(value, "错误", Messagebox.OK, Messagebox.ERROR);
		} catch (InterruptedException e) {
			log.error(e);
		}
	}
}