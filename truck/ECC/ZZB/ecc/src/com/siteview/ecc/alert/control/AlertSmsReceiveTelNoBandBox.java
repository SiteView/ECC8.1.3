package com.siteview.ecc.alert.control;

import java.util.Arrays;

import com.siteview.ecc.alert.dao.IAlertDao;
import com.siteview.ecc.alert.util.DictionaryFactory;

public class AlertSmsReceiveTelNoBandBox extends AbstractCheckBandBox {
	private static final long serialVersionUID = -706352362592721862L;

	@Override
	public String[] getSelectArray() {
		try {
			IAlertDao alertDao = DictionaryFactory.getIAlertDao();
			//≈≈–Ú
			String telephoneNo[] =  alertDao.getTelphoneNo();
			Arrays.sort(telephoneNo);
			return telephoneNo;
			
		} catch (Exception e) {
			e.printStackTrace();
			return new String[0];
		}
	}

}
