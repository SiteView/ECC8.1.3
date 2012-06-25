package com.siteview.ecc.reportserver;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

import org.apache.log4j.Logger;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
/** 
*����:zipѹ������ѹ(֧�������ļ���) 
*˵��:������ͨ��ʹ��Apache Ant���ṩ��zip����org.apache.tools.zipʵ����zipѹ���ͽ�ѹ����. 
*   ���������java.util.zip����֧�ֺ��ֵ����⡣ 
*   ʹ��java.util.zip��ʱ,��zip�ļ���������Ϊ���ĵ��ļ�ʱ, 
*   �ͻ�����쳣:"Exception  in thread "main " java.lang.IllegalArgumentException  
*               at   java.util.zip.ZipInputStream.getUTF8String(ZipInputStream.java:285) 
*ע��: 
*   1��ʹ��ʱ��ant.jar�ŵ�classpath��,������ʹ��import org.apache.tools.zip.*; 
*   2��Apache Ant ���ص�ַ:http://ant.apache.org/ 
*   3��Ant ZIP API:http://www.jajakarta.org/ant/ant-1.6.1/docs/mix/manual/api/org/apache/tools/zip/ 
*   4��������ʹ��Ant 1.7.1 �е�ant.jar 
* 
*�������ѧϰ�ο�. 
* 
*@author Winty 
*@date   2008-8-3 
*@Usage: 
*   ѹ��:java AntZip -zip "directoryName" 
*   ��ѹ:java AntZip -unzip "fileName.zip" 
*/ 

public class AntZip
{
	private final static Logger logger = Logger.getLogger(AntZip.class);
    private ZipFile         zipFile; 
    private ZipOutputStream zipOut;     //ѹ��Zip 
    private ZipEntry        zipEntry; 
    private static int      bufSize;    //size of bytes 
    private byte[]          buf; 
    private int             readedBytes; 
     
    public AntZip()
    { 
        this(512); 
    } 

    public AntZip(int bufSize)
    { 
        this.bufSize = bufSize; 
        this.buf = new byte[this.bufSize]; 
    } 
     
    //ѹ���ļ����ڵ��ļ� 
    public void doZip(String zipDirectory)
    {
    	//zipDirectoryPath:��Ҫѹ�����ļ����� 
        File file; 
        File zipDir; 

        zipDir = new File(zipDirectory); 
        String zipFileName = zipDir.getName() + ".zip";//ѹ�������ɵ�zip�ļ��� 
				FileOutputStream fos=null;
				BufferedOutputStream bos=null;
        try
        { 
        	  fos=new FileOutputStream(zipFileName);
        	  bos=new BufferedOutputStream(fos);
            this.zipOut = new ZipOutputStream(bos); 
            handleDir(zipDir , this.zipOut); 
        }
        catch(IOException ioe)
        { 
            ioe.printStackTrace(); 
        } 
        finally
        {
        	try{this.zipOut.close(); }catch(Exception e){}
        	try{bos.close();}catch(Exception e){}
        	try{fos.close();}catch(Exception e){}
        }
    } 

    //��doZip����,�ݹ����Ŀ¼�ļ���ȡ 
    private void handleDir(File dir , ZipOutputStream zipOut)throws IOException
    { 
        FileInputStream fileIn; 
        File[] files; 

        files = dir.listFiles(); 
     
        if(files.length == 0)
        {
        	//���Ŀ¼Ϊ��,�򵥶�����֮. 
            //ZipEntry��isDirectory()������,Ŀ¼��"/"��β. 
            try{this.zipOut.putNextEntry(new ZipEntry(dir.toString() + "/")); }finally{
            this.zipOut.closeEntry(); 
          }
        } 
        else
        {//���Ŀ¼��Ϊ��,��ֱ���Ŀ¼���ļ�. 
            for(File fileName : files)
            { 
                //logger.info(fileName); 

                if(fileName.isDirectory())
                { 
                    handleDir(fileName , this.zipOut); 
                } 
                else
                { 
                    fileIn = new FileInputStream(fileName); 
                    try{
			                    this.zipOut.putNextEntry(new ZipEntry(fileName.toString())); 
			
			                    while((this.readedBytes = fileIn.read(this.buf))>0)
			                    { 
			                        this.zipOut.write(this.buf , 0 , this.readedBytes); 
			                    } 
										}finally{
                   		 try{this.zipOut.closeEntry(); }catch(Exception e){}
                   		 try{fileIn.close();}catch(Exception e){}
                  	}
                } 
            } 
        } 
    } 

    //��ѹָ��zip�ļ� 
    public void unZip(String unZipfileName)
    {
    	//unZipfileName��Ҫ��ѹ��zip�ļ��� 
        FileOutputStream fileOut=null; 
        File file=null; 
        InputStream inputStream=null; 

        try
        { 
            this.zipFile = new ZipFile(unZipfileName); 

            for(Enumeration entries = this.zipFile.getEntries(); entries.hasMoreElements();)
            { 
                ZipEntry entry = (ZipEntry)entries.nextElement(); 
                file = new File(entry.getName()); 

                if(entry.isDirectory())
                { 
                    file.mkdirs(); 
                } 
                else
                { 
                    //���ָ���ļ���Ŀ¼������,�򴴽�֮. 
                    File parent = file.getParentFile(); 
                    if(!parent.exists())
                    { 
                        parent.mkdirs(); 
                    } 

                    inputStream = zipFile.getInputStream(entry); 

                    fileOut = new FileOutputStream(file); 
                    try
                    {
	                    while(( this.readedBytes = inputStream.read(this.buf) ) > 0)
	                    { 
	                        fileOut.write(this.buf , 0 , this.readedBytes ); 
	                    } 
	                  }finally
	                  {
	                  	try{fileOut.close();}catch(Exception e){}
	                  	try{inputStream.close();}catch(Exception e){}
	                  }
                }    
            }        
            
        }
        catch(IOException ioe)
        { 
            ioe.printStackTrace(); 
        } 
        finally
        {
        	try{fileOut.close();}catch(Exception e){}
        	try{inputStream.close();}catch(Exception e){}
        	try{this.zipFile.close(); }catch(Exception e){}
        }
    } 

    //���û�������С 
    public void setBufSize(int bufSize)
    { 
        this.bufSize = bufSize; 
    } 

    //����AntZip�� 
    public static void main(String[] args)throws Exception
    { 
        if(args.length==2)
        { 
            String name = args[1]; 
            AntZip zip = new AntZip(); 

            if(args[0].equals("-zip")) 
                zip.doZip(name); 
            else if(args[0].equals("-unzip")) 
                zip.unZip(name); 
        } 
        else
        { 
            logger.info("Usage:"); 
            logger.info("ѹ��:java AntZip -zip directoryName"); 
            logger.info("��ѹ:java AntZip -unzip fileName.zip"); 
            throw new Exception("Arguments error!"); 
        } 
    } 
}