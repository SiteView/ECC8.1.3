package com.siteview.ecc.report.statereport;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

import com.siteview.ecc.report.beans.StateBean;

public class ImageDatasource implements JRDataSource {
	private static final Logger logger = Logger.getLogger(ImageDatasource.class);
	private StateBean stateBean = null;
	
	private List<Color> colorlist = null;
	
	private int index = -1;
	private boolean flag = true;
	
	public ImageDatasource(Object data){
		if((data instanceof StateBean) == true)
		{
			stateBean = (StateBean) data;
		}else if(data instanceof List){
			colorlist = (List<Color>)data;
		}
	}
	
	@Override
	public Object getFieldValue(JRField arg0) throws JRException {
		String name  = arg0.getName();
		if(!name.equals("image")) return null;
		try {
			flag = false;
			if(stateBean!=null){
				StateBean tmpBean = stateBean.cloneThis();
				return create3DPieChart(tmpBean.getMonitorName(),createDataset(tmpBean),600,300);
				
			}else if(this.colorlist!=null){
				return this.getSpectrumImage(colorlist, 800, 50);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JRException(e);
		}
		return null;
	}

	@Override
	public boolean next() throws JRException {
		logger.info("++++++++++++++++++++++++++++++++++++++++++++"+index);
		return flag;
	}
	private PieDataset createDataset(StateBean sb) {
		DefaultPieDataset localDefaultPieDataset = new DefaultPieDataset();
		localDefaultPieDataset.setValue("正常", sb.getOk());
		localDefaultPieDataset.setValue("警告", sb.getWarn());
		localDefaultPieDataset.setValue("错误", sb.getError());
		localDefaultPieDataset.setValue("禁止", sb.getDisable());
		localDefaultPieDataset.setValue("停止", sb.getBad());
		return localDefaultPieDataset;
	}
	public InputStream create3DPieChart(String title,PieDataset data,int pngwidth,int pngheight) throws IOException{
	    JFreeChart localJFreeChart = ChartFactory.createPieChart3D(title, data, true, true, false);
	    PiePlot3D localPiePlot3D = (PiePlot3D)localJFreeChart.getPlot();
	    localPiePlot3D.setStartAngle(290.0D);
	    localPiePlot3D.setDirection(Rotation.CLOCKWISE);
	    localPiePlot3D.setForegroundAlpha(0.5F);
	    localPiePlot3D.setNoDataMessage("No data to display");
	    localPiePlot3D.setSectionPaint(0,new Color(152,251,152));
	    localPiePlot3D.setSectionPaint(1,new Color(238,221,130));
	    localPiePlot3D.setSectionPaint(2,new Color(255, 62, 150));
	    localPiePlot3D.setSectionPaint(3,new Color(139,0,0));
	    localPiePlot3D.setSectionPaint(4,new Color(181,181,181));
	    ChartRenderingInfo info = new ChartRenderingInfo();
		BufferedImage bi = localJFreeChart.createBufferedImage(pngwidth, pngheight, BufferedImage.SCALE_FAST, info);
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		ImageOutputStream imOut = ImageIO.createImageOutputStream(bs);
		ImageIO.write(bi, "GIF", imOut);
		// scaledImage1为BufferedImage，jpg为图像的类型
		InputStream istream = new ByteArrayInputStream(bs.toByteArray());
		return istream;
	}
	
	public InputStream getSpectrumImage(List<Color> colorlist,int width,int height) throws IOException{
		BufferedImage image = null;
		Graphics2D g2 = null;
		int step = width / colorlist.size();
		
		if (step >= 1) {
			image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
			g2 = (Graphics2D) image.getGraphics();
			g2.setBackground(Color.WHITE);
			for (int i = 0; i < colorlist.size(); i++) {
				g2.setColor(colorlist.get(i));
				g2.fillRect(i*step, 0, i*step + step, height);
			}
		} else {
			image = new BufferedImage(colorlist.size(), height,BufferedImage.TYPE_INT_RGB);
			g2 = (Graphics2D) image.getGraphics();
			g2.setBackground(Color.WHITE);
			for (int i = 0; i < colorlist.size(); i++) {
				g2.setColor(colorlist.get(i));
				g2.drawLine(i, 0, i, height);
			}
		}
		g2.dispose();

		Image scaleImage = image.getScaledInstance (width, height,Image.SCALE_SMOOTH);
		
		image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
		g2 = (Graphics2D) image.getGraphics();
		g2.drawImage(scaleImage, 0, 0, null);
		g2.dispose();
		
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		ImageOutputStream imOut = ImageIO.createImageOutputStream(bs);
		ImageIO.write(image, "GIF", imOut);
		InputStream istream = new ByteArrayInputStream(bs.toByteArray());
		return istream;
	}
	
	public static void main(String [] args) throws IOException {
		List<Color> colorlist  = new ArrayList<Color>();
		for (int i = 0; i<9000; i++) {
			if (new Random().nextBoolean())
				colorlist.add(Color.red);
			else if (i > 600)
				colorlist.add(Color.green);
			else
				colorlist.add(Color.yellow);
		}
		int width = 600;
		int height = 30;
		BufferedImage image = null;
		Graphics2D g2 = null;
		int step = width / colorlist.size();
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		ImageOutputStream imOut = ImageIO.createImageOutputStream(bs);
		
		if (step >= 1) {
			image = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			g2 = (Graphics2D) image.getGraphics();
			g2.setBackground(Color.WHITE);
			for (int i = 0; i < colorlist.size(); i++) {
				g2.setColor(colorlist.get(i));
				g2.fillRect(i*step, 0, i*step + step, height);
			}
		} else {
			image = new BufferedImage(colorlist.size(), height,
					BufferedImage.TYPE_INT_RGB);
			g2 = (Graphics2D) image.getGraphics();
			g2.setBackground(Color.WHITE);
			for (int i = 0; i < colorlist.size(); i++) {
				g2.setColor(colorlist.get(i));
				g2.drawLine(i, 0, i, height);
			}
		}
		g2.dispose();

		Image scaleImage = image.getScaledInstance (width, height,Image.SCALE_SMOOTH);
		
		image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		g2 = (Graphics2D) image.getGraphics();
		g2.drawImage(scaleImage, 0, 0, null);
		g2.dispose();

		
		ImageIO.write(image, "GIF", imOut);
		byte[] contentByte = bs.toByteArray();
		FileOutputStream f = new FileOutputStream("d:\\test.gif");
		f.write(contentByte);
		f.flush();
		f.close();
		bs.close();
		imOut.close();
	}
}
