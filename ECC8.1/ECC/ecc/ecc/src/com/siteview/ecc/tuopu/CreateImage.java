package com.siteview.ecc.tuopu;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CreateImage extends HttpServlet {
	
	private static final String GIF="image/gif;charset=GBK";//�趨���������    
	
	/**
	 * �Ѵ�ͼƬ��С
	 */
	public void doGet(HttpServletRequest req ,HttpServletResponse resp){
	    try {
	    	String imagePath = req.getParameter("imageName");
	    	
	        try {
	        	imagePath = new String(imagePath.getBytes("iso8859-1"), "UTF-8");
	        	} catch (Exception e) {
        		e.printStackTrace();
        	}
        	System.out.println(imagePath);
	    	ServletOutputStream output = resp.getOutputStream();
			resp.setContentType(GIF);//�趨���������          //�õ�ͼƬ����ʵ·��         
			imagePath = getServletContext().getRealPath(imagePath);         //�õ�ͼƬ���ļ���      
			File f = new File(imagePath);
			BufferedImage bi = ImageIO.read(f);
			bi.getScaledInstance (30,30,Image.SCALE_SMOOTH);
			ImageIO.write(bi, "GIF", output);
		} catch (IOException e) {
			e.printStackTrace();
		}     
	}
	
	public void doPost(HttpServletRequest req ,HttpServletResponse resp){
		doGet(req,resp);
	}
}
