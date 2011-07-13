package com.siteview.ecc.progress;

import java.io.InputStream;

import org.zkoss.util.media.AMedia;
import org.zkoss.util.media.Media;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Button;
import org.zkoss.zul.Progressmeter;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

import com.siteview.ecc.util.Toolkit;

public class BlankWindow extends Window{
	public void showUrl(String name,String format,String ctype,InputStream file)
	{
		Iframe iframe=(Iframe)super.getFirstChild();
		
		
		iframe.setContent(new AMedia(name, format,ctype,file));
		iframe.setVisible(true);
		
	}
	public void showUrl(String url)
	{
		Iframe iframe=(Iframe)super.getFirstChild();
		iframe.setSrc(url);
		iframe.setVisible(true);
		
	}
}
