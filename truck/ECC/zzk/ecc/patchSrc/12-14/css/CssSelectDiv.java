package com.siteview.ecc.css;

import java.util.Map;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Panelchildren;

import com.siteview.ecc.util.Toolkit;

public class CssSelectDiv extends Panelchildren {
	public void onCreate() 
	{
		ttttt();
	}
	private void ttttt()
	{
		String curEccTheme=Toolkit.getToolkit().getCookie("eccTheme", super.getDesktop());
		if(curEccTheme==null)
			curEccTheme="eccThemeBlue";
		Map<String, String> themesMap = LoadCssFile.getCssNameMap();

		for (Object css : themesMap.keySet().toArray()) 
		{
			Div box=new Div();
			box.setAlign("center");
			box.setWidth("250px");
			box.setHeight("180px");
			box.setParent(this);
			
			box.setStyle("float:left;text-align:center;width:250px;height:180px;overflow:hidden");
			
			String name = themesMap.get(css);
			Image image=new Image();
			image.setSrc("/main/css/theme/"+css+".jpg");
			image.setParent(box);
			image.setAttribute("theme", css);
			image.setAttribute("themeName", name);
			image.setTooltiptext(name);
			
			if(curEccTheme.equals(css))
				image.setSclass("eccThemeCurrentImage");
			else
				image.setSclass("eccThemeChoseImage");
			
				

			if(!curEccTheme.equals(css))
			image.addEventListener("onClick", new EventListener(){
				@Override
				public void onEvent(Event event) throws Exception {
					String theme=event.getTarget().getAttribute("theme").toString();
					Toolkit.getToolkit().setCookie("eccTheme", "eccThemeBlue", event.getTarget().getDesktop(), Integer.MAX_VALUE);
					event.getPage().getDesktop().getExecution().sendRedirect("/","_top");
					
				}});
			
			new Label(name).setParent(box);
		}
	}
}
