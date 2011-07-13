package com.siteview.ecc.simplereport;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.apache.log4j.Logger;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;

import com.siteview.base.data.ReportDate;
import com.siteview.ecc.reportserver.Constand;
import com.siteview.ecc.reportserver.StatsReport;
import com.siteview.ecc.util.Toolkit;

public class ImageDataSource extends EccDataSource {
	private final static Logger logger = Logger.getLogger(ImageDataSource.class);

	int imageCount=0;
	int currentImagePos=-1;
	private String strImagePath;
	private String webImagePath;
	
	private Map<Integer, Map<String, String>> listimage;
	
	private boolean createImageAsStream=false;
	private String GraphicType;
	public ImageDataSource(String[] monitorIDArray, ReportDate reportDate,StatsReport statsReport,boolean createImageSrc,Map<String,String> monitorIdNameMap,String GraphicType) 
	{
		super(monitorIDArray, reportDate, statsReport,monitorIdNameMap);
		this.createImageAsStream=createImageSrc;
		this.GraphicType=GraphicType;
	}
	@Override
	public boolean next() throws JRException 
	{
		
		if(cancel)
			return false;

		currentImagePos++;
		
		while(currentImagePos==imageCount)
		{
			monitorPos++;
			if(monitorPos>=monitorIDArray.length)
			{
				finish=true;
				return false;
			}
			
			try
			{
				reportDate.getReportDate(monitorIDArray[monitorPos],statsReport.dstrstatusnoneed,statsReport.showdstr,statsReport.return_value_filter);
			}catch(Exception e)
			{
				finish=true;
				e.printStackTrace(); 
				return false;
			}
			
			this.listimage = statsReport.createReportImpl.getImagelist(monitorIDArray[monitorPos], reportDate);
			imageCount=this.listimage.size();
			currentImagePos=0;
		}
		


		return true;
	}
	@Override
	public Object getFieldValue(JRField jrField) throws JRException 
	{
		logger.info("createImageAsStream=" + createImageAsStream);
		try{
		if(createImageAsStream)
			return getImageStream();
		String image =  getImageSrc();
		logger.info("getImageSrc=" + image);
		return image;
		}catch(Exception e)
		{
			e.printStackTrace();
			return new JRException(e.getMessage());
		}
	}
	
	public ByteArrayInputStream getImageStream() throws Exception 
	{
		
		String id = monitorIDArray[monitorPos] + statsReport.getReportID() + currentImagePos;
		Map<Date, String> imgdata = reportDate.getReturnValueDetail(monitorIDArray[monitorPos], currentImagePos);
		Map<String, String> keyvalue = listimage.get(currentImagePos);
		XYDataset data=null;
		DefaultCategoryDataset data1=null;
		if (this.GraphicType.equals("柱状图"))
		{
			data1=SimpleReport.buildCategoryDataset(imgdata);
		}
		else
		{
		 data = SimpleReport.buildDataset(imgdata);
		}
		BufferedImage temmap = null;
		String maxdate = keyvalue.get("maxdate");
		Date maxd = null;
		if (maxdate != null && !"".equals(maxdate))
		{
			try
			{
				maxd = Toolkit.getToolkit().parseDate(maxdate);
			} catch (java.text.ParseException e)
			{
				e.printStackTrace();
			}
		}
		if (keyvalue.get("title").contains("%"))
		{
			if (this.GraphicType.equals("柱状图"))
			{
				temmap = SimpleReport.buildCategoryBufferImage(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("ytitle"), data1, 10, 100, maxd, 0, true, 563, 200);
			}
			else
			{
			temmap = SimpleReport.buildBufferImage(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("ytitle"), data, 10, 100, maxd, 0, true, 563, 200);
			}
		} else
		{
			double maxvalue = keyvalue.get("maxvalue")!=null?Double.parseDouble(keyvalue.get("maxvalue")):0;
			double minvalue = keyvalue.get("minvalue")!=null?Double.parseDouble(keyvalue.get("minvalue")):0;
			maxvalue = maxvalue * 1.1;
			if (maxvalue < 1)
			{
				maxvalue = 1;
			}
			if (keyvalue.get("minvalue")!=null&&keyvalue.get("minvalue").contains("-"))
			{
				if (this.GraphicType.equals("柱状图"))
				{
					temmap = SimpleReport.buildCategoryBufferImage(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("ytitle"), data1, 20, maxvalue, maxd, minvalue, true,
							563, 200);
				}
				else
				{
				temmap = SimpleReport.buildBufferImage(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("ytitle"), data, 20, maxvalue, maxd, minvalue, true,
						563, 200);
				}
			} else
			{
				if (this.GraphicType.equals("柱状图"))
				{
					temmap = SimpleReport.buildCategoryBufferImage(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("ytitle"), data1, 20, maxvalue, maxd, 0, true, 563, 200);
				}else
				{
				temmap = SimpleReport.buildBufferImage(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("ytitle"), data, 20, maxvalue, maxd, 0, true, 563, 200);
				}
			}
		}
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		ImageOutputStream imOut = null;
		try
		{
			imOut = ImageIO.createImageOutputStream(bs);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		try
		{
			ImageIO.write(temmap, "PNG", imOut);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		// scaledImage1为BufferedImage，jpg为图像的类型
		return new ByteArrayInputStream(bs.toByteArray());
	}
	public String getImageSrc() throws Exception
	{
		
		String id = monitorIDArray[monitorPos] + statsReport.getReportID() + currentImagePos;
		
		
		Map<Date, String> imgdata = reportDate.getReturnValueDetail(monitorIDArray[monitorPos], currentImagePos);
		Map<String, String> keyvalue = listimage.get(currentImagePos);
		XYDataset data=null;
		DefaultCategoryDataset data1=null;
		if (this.GraphicType.equals("柱状图"))
		{
			data1=SimpleReport.buildCategoryDataset(imgdata);
		}
		else
		{
		 data = SimpleReport.buildDataset(imgdata);
		}
	
		BufferedImage temmap = null;
		String maxdate = keyvalue.get("maxdate");
		Date maxd = null;
		if (maxdate != null && !"".equals(maxdate))
		{
			try
			{
				maxd = Toolkit.getToolkit().parseDate(maxdate);
			} catch (ParseException e)
			{
				e.printStackTrace();
			}
		}
		if (keyvalue.get("title") == null ? false : keyvalue.get("title").contains("%"))
		{
			if (this.GraphicType.equals("柱状图"))
			{
			temmap = SimpleReport.buildCategoryBufferImage(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("ytitle"), data1, 10, 100, maxd, 0, true, 563, 200);
			}else
			{
				temmap = SimpleReport.buildBufferImage(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("ytitle"), data, 10, 100, maxd, 0, true, 563, 200);
			}
		} else
		{
			double maxvalue = keyvalue.get("maxvalue")==null ? 0 : Double.parseDouble(keyvalue.get("maxvalue"));
			double minvalue = keyvalue.get("minvalue")==null ? 0 : Double.parseDouble(keyvalue.get("minvalue"));
			maxvalue = maxvalue * 1.1;
			if (maxvalue < 1)
			{
				maxvalue = 1;
			}
			if (keyvalue.get("minvalue") == null ? false : keyvalue.get("minvalue").contains("-"))
			{
				if (this.GraphicType.equals("柱状图"))
				{
				temmap = SimpleReport.buildCategoryBufferImage(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("ytitle"), data1, 20, maxvalue, maxd, minvalue, true,
						563, 200);
				}else
				{
					temmap = SimpleReport.buildBufferImage(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("ytitle"), data, 20, maxvalue, maxd, minvalue, true,
							563, 200);
				}
			} else
			{
				if (this.GraphicType.equals("柱状图"))
				{
					temmap = SimpleReport.buildCategoryBufferImage(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("ytitle"), data1, 20, maxvalue, maxd, 0, true, 563, 200);
				}else
				{
				temmap = SimpleReport.buildBufferImage(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("ytitle"), data, 20, maxvalue, maxd, 0, true, 563, 200);
				}
			}
		}
		
		// session.setAttribute(id, temmap);
		// list.add("/ecc/main/report/createImage.jsp?id=" + id);
		
		// 这里跟小报告的处理不一样， 原来的html用这种方式来动态生成图片
		// 现在需要存储成图片到网页目录并把uri路径列表返回出去
		String strImagePath = new StringBuffer(Constand.statreportsavepath).append(statsReport.getReportFileID()).append(".html_files\\").append(id).append(".gif").toString();
		new com.siteview.ecc.simplereport.CreateImage().create(temmap, strImagePath);
		
		return new StringBuffer("./").append( statsReport.getReportFileID()).append(".html_files/").append(id).append(".gif").toString();
	}

	@Override
	public void getExcutingInfo(StringBuffer sb)
	{
		if(monitorPos==-1||monitorPos>=monitorIDArray.length)
			return;
		sb.append("统计图数据,").append(getCurrentMonitorName()).append("(").append(currentImagePos).append("/").append(imageCount).append(")");
	}
	@Override
	public int getTaskProgress(){
		if(monitorPos==-1)
			return 0;

		return monitorPos;
	}
}
