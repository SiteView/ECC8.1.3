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
	
	private static final String GIF="image/gif;charset=GBK";//设定输出的类型    
	
	/**
	 * 把大图片缩小
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
			resp.setContentType(GIF);//设定输出的类型          //得到图片的真实路径         
			imagePath = getServletContext().getRealPath(imagePath);         //得到图片的文件流      
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
