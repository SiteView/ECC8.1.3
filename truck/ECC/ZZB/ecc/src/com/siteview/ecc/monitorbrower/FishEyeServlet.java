package com.siteview.ecc.monitorbrower;

import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.siteview.ecc.controlpanel.MonitorDetailInfo;

public class FishEyeServlet extends HttpServlet {
	private static final long serialVersionUID = 3096772363180158578L;
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException {
		OutputStream output = null;
        try{
        	res.setContentType("image/gif;charset=GBK"); 
        	res.setHeader("Content-Disposition", "attachment; filename=report.gif");//

        	output = res.getOutputStream();
            String nodeid = req.getParameter("nodeid");
            if (nodeid == null) throw new Exception("nodeid is null!");
            String year = req.getParameter("year");
            if (year == null) throw new Exception("year is null!");
            String month = req.getParameter("month");
            if (month == null) throw new Exception("month is null!");
            String day = req.getParameter("dayOfMonth");
            if (day == null) throw new Exception("dayOfMonth is null!");
            String sessionid = req.getParameter("sessionid");
            if (sessionid == null) throw new Exception("sessionid is null!");
            BufferedImage image = getBufferedImage(sessionid,nodeid, year + "-" + getString(month,2) + "-" + getString(day,2));
            if (image!=null)
            	ImageIO.write(image, "GIF", output);
        } catch (Exception e){
            e.printStackTrace();
        }finally{
        	try{output.close();}catch(Exception e){}
        }
    }
	//private static Map<String,BufferedImage> imageMap = new ConcurrentHashMap<String,BufferedImage>();
	
	public static BufferedImage getBufferedImage(String sessionid,String nodeid,String date) throws Exception{
		/*
		String key = nodeid + "-" + date;
		logger.info("getBufferedImage:" + key);
		BufferedImage image = imageMap.get(key);
		if (image == null){
            image = MonitorDetailInfo.getBufferedImage(sessionid,nodeid, date);
           	imageMap.put(key, image);
		}
		return image;
		*/
		return  MonitorDetailInfo.getBufferedImage(sessionid,nodeid, date);
		
	}
	
	private static String getString(String str, int len) {
		int j = len - str.length();
		String s2 = "";
		for (int i = 0; i < j; i++)
			s2 += "0";
		s2 += str;
		return s2;
	}


}
