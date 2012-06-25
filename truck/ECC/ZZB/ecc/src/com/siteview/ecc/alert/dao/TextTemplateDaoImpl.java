package com.siteview.ecc.alert.dao;

import java.util.Map;

import com.siteview.base.data.IniFile;
import com.siteview.ecc.alert.util.DictionaryFactory;

public class TextTemplateDaoImpl implements ITextTemplateDao {
	private IniFile templateIniFile = null;
	
	public TextTemplateDaoImpl() throws Exception{
		templateIniFile = DictionaryFactory.getTemplate();
	}
	@Override
	public Map<String, String> getMailTemplet() {
		return templateIniFile.getSectionData("Email");
	}

	@Override
	public Map<String, String> getScriptTemplet() {
		return templateIniFile.getSectionData("Scripts");
	}

	@Override
	public Map<String, String> getSmsTemplet() {
		return templateIniFile.getSectionData("SMS");
	}
	@Override
	public Map<String, String> getWebSmsTemplet() {
		return templateIniFile.getSectionData("WebSmsConfige");
	}

}
