package com.siteview.ecc.simplereport;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jfree.data.xy.XYDataset;
import org.zkoss.zul.Imagemap;

import com.siteview.base.data.ReportManager;
import com.siteview.base.manage.View;
import com.siteview.base.tree.INode;
import com.siteview.base.treeInfo.MonitorInfo;
import com.siteview.ecc.util.Toolkit;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class CreateImage {

	public void create(HttpSession session,HttpServletResponse response,String id) {
		BufferedImage chartImagemap=null;
		OutputStream output=null;
		try {
			chartImagemap=(BufferedImage)session.getAttribute(id);
			output = response.getOutputStream();
			ImageIO.write(chartImagemap, "GIF", output);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			try{output.close();}catch(Exception e){}
		}
	}
	public void create(BufferedImage chartImagemap, String strPath) {
		try {
				createDir(strPath);
				OutputStream output = new FileOutputStream(strPath);
				try{
						ImageIO.write(chartImagemap, "GIF", output);
					}finally{output.close();}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void createDir(String strPath)
	{
		int idx1=strPath.lastIndexOf("\\");
		int idx2=strPath.lastIndexOf("/");
		int idx=Math.max(idx1,idx2);
			
		if(idx>0)
		{
			String path=strPath.substring(0,idx);
			java.io.File f=new java.io.File(path);
			if(!f.exists())
				f.mkdirs();
		}
		
		
	}
	
}
