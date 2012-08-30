package com.siteview.eccservice;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class GetFileWithBase64 {
	
	public static RetMapInMap tryGetFile(Map<String, String> inwhat)
	{
		String dowhat = new String(" ");
		try
		{
			dowhat = inwhat.get("dowhat");
		} catch (Exception e)
		{
		}
		
		try
		{
			if (dowhat.equals("GetFileWithBase64"))
				return GetFile(inwhat);
			if (dowhat.equals("GetFileNameList"))
				return GetFileNameList(inwhat);
			if (dowhat.equals("DeleteFile"))
				return DeleteFile(inwhat);			
			if (dowhat.equals("UploadFileWithBase64"))
				return UploadFile(inwhat);	
			
			throw new Exception("exception");
		} catch (Exception e)
		{
			boolean ret=false;
			StringBuilder estr=new StringBuilder(" Exception to "+dowhat+"; ");
			Map<String, Map<String, String>> fmap = new HashMap<String, Map<String, String>>();
			Map<String, String> ndata1 = new HashMap<String, String>();
			ndata1.put("return", "false");
			fmap.put("return", ndata1);			
			return new RetMapInMap(ret, estr.toString(), fmap);
		}
	}	
	
	public static RetMapInMap UploadFile(Map<String, String> inwhat)
	{
		boolean ret=false;
		StringBuilder estr=new StringBuilder();
		Map<String, Map<String, String>> fmap = new HashMap<String, Map<String, String>>();
		
		String absoluteFileName = new String("");
		if(inwhat.containsKey("absoluteFileName"))
			absoluteFileName= inwhat.get("absoluteFileName");
		
		String FileName = new String("");
		if(inwhat.containsKey("FileName"))
			FileName= inwhat.get("FileName");
		
		String ValueBase64 = new String("");
		if(inwhat.containsKey("ValueBase64"))
			ValueBase64= inwhat.get("ValueBase64");		
		
		if (absoluteFileName.isEmpty() && FileName.isEmpty())
		{
			estr.append(" 传入的文件名为空; ");
			Map<String, String> ndata1 = new HashMap<String, String>();
			ndata1.put("return", "false");
			fmap.put("return", ndata1);
			return new RetMapInMap(ret, estr.toString(), fmap);
		}
		
		if (absoluteFileName.isEmpty())
			absoluteFileName= System.getProperty("user.dir")+  FileName;
		
		OutputStream outfile= null;
		try
		{
			SystemOut.println("  UploadFile file: " + absoluteFileName);
			byte[] fdata = decode(ValueBase64);
			outfile = new FileOutputStream(absoluteFileName);
			outfile.write(fdata, 0, fdata.length);
			 
			Map<String, String> ndata1 = new HashMap<String, String>();
			ndata1.put("return", "true");
			fmap.put("return", ndata1);
			ret = true;
		} catch (Exception e)
		{
			SystemOut.println("   上载文件发生异常  " + absoluteFileName + "   ; ");
			estr.append("   上载文件发生异常  " + absoluteFileName + "   ; ");

			Map<String, String> ndata1 = new HashMap<String, String>();
			ndata1.put("return", "false");
			fmap.put("return", ndata1);	
		} finally
		{
			try
			{		
				if (outfile != null)
					outfile.close();
			} catch (Exception e2)
			{
				e2.printStackTrace();
			}
		}
		return new RetMapInMap(ret, estr.toString(), fmap);	
	}

	public static RetMapInMap DeleteFile(Map<String, String> inwhat)
	{
		boolean ret=false;
		StringBuilder estr=new StringBuilder();
		Map<String, Map<String, String>> fmap = new HashMap<String, Map<String, String>>();
		
		String absoluteFileName = new String("");
		if(inwhat.containsKey("absoluteFileName"))
			absoluteFileName= inwhat.get("absoluteFileName");
		
		String FileName = new String("");
		if(inwhat.containsKey("FileName"))
			FileName= inwhat.get("FileName");
		
		if (absoluteFileName.isEmpty() && FileName.isEmpty())
		{
			estr.append(" 传入的文件名为空; ");
			Map<String, String> ndata1 = new HashMap<String, String>();
			ndata1.put("return", "false");
			fmap.put("return", ndata1);
			return new RetMapInMap(ret, estr.toString(), fmap);
		}
		
		if (absoluteFileName.isEmpty())
			absoluteFileName= System.getProperty("user.dir")+  FileName;
		
		try
		{
			SystemOut.println("  DeleteFile: " + absoluteFileName);
			File f = new File(absoluteFileName);
			if (!f.exists())
				throw new Exception("exception");
			ret = f.delete();
			
			HashMap<String, String> ndata1 = new HashMap<String, String>();
			if (ret)
				ndata1.put("return", "true");
			else
				ndata1.put("return", "false");
			fmap.put("return", ndata1);
		} catch (Exception e)
		{
			SystemOut.println("   删除文件发生异常  " + absoluteFileName + "   ; ");
			estr.append("   删除文件发生异常  " + absoluteFileName + "   ; ");

			HashMap<String, String> ndata1 = new HashMap<String, String>();
			ndata1.put("return", "false");
			fmap.put("return", ndata1);	
		}
		
		return new RetMapInMap(ret, estr.toString(), fmap);	
	}
	
	public static RetMapInMap GetFileNameList(Map<String, String> inwhat)
	{
		boolean ret=false;
		StringBuilder estr=new StringBuilder();
		Map<String, Map<String, String>> fmap = new HashMap<String, Map<String, String>>();
		
		String absoluteFileName = new String("");
		if(inwhat.containsKey("absoluteDirName"))
			absoluteFileName= inwhat.get("absoluteDirName");
		
		String FileName = new String("");
		if(inwhat.containsKey("DirName"))
			FileName= inwhat.get("DirName");
		
		if (absoluteFileName.isEmpty() && FileName.isEmpty())
		{
			estr.append(" 传入的目录名为空; ");
			Map<String, String> ndata1 = new HashMap<String, String>();
			ndata1.put("return", "false");
			fmap.put("return", ndata1);
			return new RetMapInMap(ret, estr.toString(), fmap);
		}
		
		if (absoluteFileName.isEmpty())
			absoluteFileName= System.getProperty("user.dir")+  FileName;
		
		try
		{
			SystemOut.println("  GetFileNameList: " + absoluteFileName);
			File f = new File(absoluteFileName);
			if (!f.exists())
				throw new Exception("exception");
			
			Map<String, String> ndata1 = new HashMap<String, String>();
			Map<String, String> ndata2 = new HashMap<String, String>();
			Map<String, String> ndata3 = new HashMap<String, String>();
            java.lang.String as[] = f.list();
			for (int i = 0; i < as.length; i++)
            {
                java.io.File file1 = new File(f, as[i]);
                if(file1.isDirectory())
                	ndata3.put(as[i], "directory");
                else
                	ndata2.put(as[i], "file");
            }
			
			ndata1.put("return", "true");
			fmap.put("return", ndata1);
			fmap.put("files", ndata2);
			fmap.put("directories", ndata3);
			ret = true;
		} catch (Exception e)
		{
			SystemOut.println("   获取文件名列表发生异常  " + absoluteFileName + "   ; ");
			estr.append("   获取文件名列表发生异常  " + absoluteFileName + "   ; ");

			HashMap<String, String> ndata1 = new HashMap<String, String>();
			ndata1.put("return", "false");
			fmap.put("return", ndata1);	
		}
		
		return new RetMapInMap(ret, estr.toString(), fmap);	
	}
	
	public static RetMapInMap GetFile(Map<String, String> inwhat)
	{
		boolean ret=false;
		StringBuilder estr=new StringBuilder();
		Map<String, Map<String, String>> fmap = new HashMap<String, Map<String, String>>();
		
		String absoluteFileName = new String("");
		if(inwhat.containsKey("absoluteFileName"))
			absoluteFileName= inwhat.get("absoluteFileName");
		
		String FileName = new String("");
		if(inwhat.containsKey("FileName"))
			FileName= inwhat.get("FileName");
		
		String fileMark = new String("");
		if(inwhat.containsKey("fileMark"))
			fileMark= inwhat.get("fileMark");		
		
		if (absoluteFileName.isEmpty() && FileName.isEmpty())
		{
			estr.append(" 传入的文件名为空; ");
			HashMap<String, String> ndata1 = new HashMap<String, String>();
			ndata1.put("return", "false");
			fmap.put("return", ndata1);
			return new RetMapInMap(ret, estr.toString(), fmap);
		}
		
		if (absoluteFileName.isEmpty())
			absoluteFileName= System.getProperty("user.dir")+  FileName;
		
		try
		{
			if (fileMark.compareToIgnoreCase("false")==0)
			{
				SystemOut.println("  GetFileWithBase64 file: " + absoluteFileName);
				String str = encode(loadFileBytes(absoluteFileName));
				HashMap<String, String> ndata1 = new HashMap<String, String>();
				ndata1.put("ValueBase64", str);
				ndata1.put("return", "true");
				fmap.put("return", ndata1);
				ret = true;
			}
			else
			{
				SystemOut.println("  GetFileWithBase64 fileMark: " + absoluteFileName);
				File f = new File(absoluteFileName);
				if( !f.exists() )
					throw new Exception("exception");
				HashMap<String, String> ndata1 = new HashMap<String, String>();
				ndata1.put("fileMark", new Long(f.lastModified()).toString() );
				ndata1.put("return", "true");
				fmap.put("return", ndata1);
				ret = true;
			}
		} catch (Exception e)
		{
			SystemOut.println("   获取文件发生异常  " + absoluteFileName + "   ; ");
			estr.append("   获取文件发生异常  " + absoluteFileName + "   ; ");

			HashMap<String, String> ndata1 = new HashMap<String, String>();
			ndata1.put("return", "false");
			fmap.put("return", ndata1);	
		}
		
		return new RetMapInMap(ret, estr.toString(), fmap);	
	}


	private static char[] base64EncodeChars = new char[] { 'A', 'B', 'C', 'D',
			'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
			'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
			'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
			'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
			'4', '5', '6', '7', '8', '9', '+', '/' };

	private static byte[] base64DecodeChars = new byte[] { -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59,
			60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
			10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1,
			-1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37,
			38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1,
			-1, -1 };

	public static byte[] loadFileBytes(String filename) throws IOException {
		FileInputStream in = null;
		try {
			in = new FileInputStream(filename);

			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int ch;
			while ((ch = in.read()) != -1)
				buffer.write((byte) ch);
			in.close();
			return buffer.toByteArray();
		} finally {
			in.close();
		}
	}

	public static String encode(byte[] data) {
		StringBuffer sb = new StringBuffer();
		int len = data.length;
		int i = 0;
		int b1, b2, b3;
		while (i < len) {
			b1 = data[i++] & 0xff;
			if (i == len) {
				sb.append(base64EncodeChars[b1 >>> 2]);
				sb.append(base64EncodeChars[(b1 & 0x3) << 4]);
				sb.append("==");
				break;
			}
			b2 = data[i++] & 0xff;
			if (i == len) {
				sb.append(base64EncodeChars[b1 >>> 2]);
				sb.append(base64EncodeChars[((b1 & 0x03) << 4)
						| ((b2 & 0xf0) >>> 4)]);
				sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);
				sb.append("=");
				break;
			}
			b3 = data[i++] & 0xff;
			sb.append(base64EncodeChars[b1 >>> 2]);
			sb.append(base64EncodeChars[((b1 & 0x03) << 4)
					| ((b2 & 0xf0) >>> 4)]);
			sb.append(base64EncodeChars[((b2 & 0x0f) << 2)
					| ((b3 & 0xc0) >>> 6)]);
			sb.append(base64EncodeChars[b3 & 0x3f]);
		}
		return sb.toString();
	}

	public static byte[] decode(String str) throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer();
		byte[] data = str.getBytes("US-ASCII");
		int len = data.length;
		int i = 0;
		int b1, b2, b3, b4;
		while (i < len) {
			/* b1 */
			do {
				b1 = base64DecodeChars[data[i++]];
			} while (i < len && b1 == -1);
			if (b1 == -1)
				break;
			/* b2 */
			do {
				b2 = base64DecodeChars[data[i++]];
			} while (i < len && b2 == -1);
			if (b2 == -1)
				break;
			sb.append((char) ((b1 << 2) | ((b2 & 0x30) >>> 4)));
			/* b3 */
			do {
				b3 = data[i++];
				if (b3 == 61)
					return sb.toString().getBytes("ISO-8859-1");
				b3 = base64DecodeChars[b3];
			} while (i < len && b3 == -1);
			if (b3 == -1)
				break;
			sb.append((char) (((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2)));
			/* b4 */
			do {
				b4 = data[i++];
				if (b4 == 61)
					return sb.toString().getBytes("ISO-8859-1");
				b4 = base64DecodeChars[b4];
			} while (i < len && b4 == -1);
			if (b4 == -1)
				break;
			sb.append((char) (((b3 & 0x03) << 6) | b4));
		}
		return sb.toString().getBytes("ISO-8859-1");
	}
}
