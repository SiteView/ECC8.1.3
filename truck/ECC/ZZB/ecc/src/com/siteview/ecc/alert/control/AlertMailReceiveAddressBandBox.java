package com.siteview.ecc.alert.control;

import java.util.Arrays;

import com.siteview.ecc.alert.dao.IAlertDao;
import com.siteview.ecc.alert.util.DictionaryFactory;

public class AlertMailReceiveAddressBandBox extends AbstractCheckBandBox {
	private static final long serialVersionUID = -706352362592721862L;
	@Override
	public String[] getSelectArray() {
		try {
			IAlertDao alertDao = DictionaryFactory.getIAlertDao();
			//≈≈–Ú
			String emailAddress[] =  alertDao.getEmailAdresss();
			Arrays.sort(emailAddress);
			return emailAddress;
			
		} catch (Exception e) {
			e.printStackTrace();
			return new String[0];
		}
	}

}
