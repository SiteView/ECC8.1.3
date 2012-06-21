package com.siteview.ecc.report;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SpectrumImage extends HttpServlet {
	private static final long serialVersionUID = 4908907544388975581L;
	private static final String GIF="image/gif;charset=GB2312";//设定输出的类型    

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		List<Color> colorlist = (List<Color>) session.getAttribute("colorlist");
		if(colorlist==null || colorlist.size()==0) return ;
		session.removeAttribute("colorlist");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType(GIF);
		// 表明生成的响应是图片
		int width = colorlist.size(), height = 300;
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		Graphics g = image.getGraphics();
		Graphics2D g2 = (Graphics2D)g;
		g2.setBackground(Color.WHITE);
		for (int i = 0; i < width; i++) {
			g2.setColor(colorlist.get(i));
			g2.drawLine(i, 0, i, 300);
		}
		g2.dispose();
		OutputStream os = response.getOutputStream();
		ImageIO.write(image, "GIF", os);
		os.flush();
		os.close();
	}
}
