package com.siteview.ecc.monitorbrower;

import jxl.*;
import jxl.write.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class Test
{
    public void exportProduct()
    {
        try
        {  
            //类login(javabean)的managed-bean-scope属性设置成session，这样可以调用login中当前用户的id。这个非常有用。
            String userId = "1231231231";
            //设置导出文件在服务器上的存储路径，getBasedir()和getSeparator()根据服务器OS来判断当前路径和连接符，Windows 和Linux不一样。
            String storedir  ="";
            //System.out.println("storedir:" + storedir);
            //System.out.println("main.isDirExists(storedir):" + main.isDirExists(storedir));
            //如果storedir文件不存在，就创建它
            if(true)
                {
                storedir = storedir + "exportExcel.xls";
                File file = new File(storedir);
                if(file.exists())
                    file.delete();
                //创建一个excel文件
                jxl.write.WritableWorkbook wwb = Workbook.createWorkbook(new File(storedir));
                //创建一个工作簿
                jxl.write.WritableSheet ws = wwb.createSheet("data", 0);
                //定义excel的文本单元格
                jxl.write.Label label;
                //定义excel的数值单元格
                jxl.write.Number number;
                //格式化数值
                jxl.write.NumberFormat nf = new jxl.write.NumberFormat("#.####");
                jxl.write.WritableCellFormat wcNf = new jxl.write.WritableCellFormat(nf);
                //从0开始，设置列标题
                label = new jxl.write.Label(0,0, "物品编码");
                ws.addCell(label);
                label = new jxl.write.Label(1,0, "物品描述");
                ws.addCell(label);
                label = new jxl.write.Label(2,0, "库存数量");
                ws.addCell(label);
                label = new jxl.write.Label(3,0, "入库日期");
                ws.addCell(label);
                label = new jxl.write.Label(4,0, "在库天数");
                ws.addCell(label);
                label = new jxl.write.Label(5,0, "在库月份");
                ws.addCell(label);
                label = new jxl.write.Label(6,0, "入库数量");
                ws.addCell(label);
                label = new jxl.write.Label(7,0, "成本");
                ws.addCell(label);
                label = new jxl.write.Label(8,0, "总成本");
                ws.addCell(label);

                //关闭文件
                wwb.write();
                wwb.close();
                Test my = new Test();
                //弹出下载框
                my.exportProduct();
                }            
        }catch(Exception ex){ex.printStackTrace();}
    }
    
    public static void main(String args[])
    {
    	new Test();
    }
}