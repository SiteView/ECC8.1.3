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
            //��login(javabean)��managed-bean-scope�������ó�session���������Ե���login�е�ǰ�û���id������ǳ����á�
            String userId = "1231231231";
            //���õ����ļ��ڷ������ϵĴ洢·����getBasedir()��getSeparator()���ݷ�����OS���жϵ�ǰ·�������ӷ���Windows ��Linux��һ����
            String storedir  ="";
            //System.out.println("storedir:" + storedir);
            //System.out.println("main.isDirExists(storedir):" + main.isDirExists(storedir));
            //���storedir�ļ������ڣ��ʹ�����
            if(true)
                {
                storedir = storedir + "exportExcel.xls";
                File file = new File(storedir);
                if(file.exists())
                    file.delete();
                //����һ��excel�ļ�
                jxl.write.WritableWorkbook wwb = Workbook.createWorkbook(new File(storedir));
                //����һ��������
                jxl.write.WritableSheet ws = wwb.createSheet("data", 0);
                //����excel���ı���Ԫ��
                jxl.write.Label label;
                //����excel����ֵ��Ԫ��
                jxl.write.Number number;
                //��ʽ����ֵ
                jxl.write.NumberFormat nf = new jxl.write.NumberFormat("#.####");
                jxl.write.WritableCellFormat wcNf = new jxl.write.WritableCellFormat(nf);
                //��0��ʼ�������б���
                label = new jxl.write.Label(0,0, "��Ʒ����");
                ws.addCell(label);
                label = new jxl.write.Label(1,0, "��Ʒ����");
                ws.addCell(label);
                label = new jxl.write.Label(2,0, "�������");
                ws.addCell(label);
                label = new jxl.write.Label(3,0, "�������");
                ws.addCell(label);
                label = new jxl.write.Label(4,0, "�ڿ�����");
                ws.addCell(label);
                label = new jxl.write.Label(5,0, "�ڿ��·�");
                ws.addCell(label);
                label = new jxl.write.Label(6,0, "�������");
                ws.addCell(label);
                label = new jxl.write.Label(7,0, "�ɱ�");
                ws.addCell(label);
                label = new jxl.write.Label(8,0, "�ܳɱ�");
                ws.addCell(label);

                //�ر��ļ�
                wwb.write();
                wwb.close();
                Test my = new Test();
                //�������ؿ�
                my.exportProduct();
                }            
        }catch(Exception ex){ex.printStackTrace();}
    }
    
    public static void main(String args[])
    {
    	new Test();
    }
}