package com.siteview.ecc.alert.control;

import java.io.InputStream;

import org.zkoss.zul.Html;

import com.siteview.ecc.alert.util.BaseTools;

public class HtmlFile extends Html {
	private static final long serialVersionUID = 1L;
	private String filename = null;
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) throws Exception {
		this.filename = filename;
		init();
	}
	
	private void init() throws Exception{
		InputStream is = null;
		try{
			if (this.getFilename() == null) throw new Exception("没有设置文件名(filename)属性");
			is = this.getDesktop().getWebApp().getResourceAsStream(this.getFilename());
			this.setContent(BaseTools.inputStream2String(is));
		}finally{
			try{is.close();}catch(Exception e){}
		}
	}
}
