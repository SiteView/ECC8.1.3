package com.siteview.eccservice;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.jws.WebService;

import com.siteview.jsvapi.Jsvapi;

//<property name="style">
//<value>rpc</value>
//</property>
//  正常只有：wrapped ; 及出异常：message、document ;  及无结果(总为null)：rpc。


//ECC 产品 OneCMDB部分业务逻辑类，通过 web-service提供功能给 UI客户端(c#,RAP...)



@WebService(serviceName = "eccapi", endpointInterface = "com.siteview.eccservice.InterfaceEccService")
public class EccService implements InterfaceEccService
{
	static private Jsvapi	svapi		= null;
	static private int		dowhatVer	= 0;	// 这是内部调试功能：0是 缺省版的dowhat ,1是调试版1的
	static private int		precacheTPL	= 0;	// 等于1 启动时将预缓存所有 template bean
	static private int		enableHttpGetJson	= 0;	// 等于1 时将允许 http get 响应
	
	static private String	adminMd5	= null;
	static private String	userMd5		= null;
										
	
	/**
	 * ECC 业务逻辑类的构造函数，会读取 svapi.ini文件设置 svdb地址
	 * @throws Exception 
	 * 
	 */
	public EccService()
	{
		if (svapi == null)
		{
			svapi = Jsvapi.getInstance();
		}
	}
	
	public boolean enableHttpGetJson()
	{
		return enableHttpGetJson==1;
	}
	
	private String readVarInFile(String signal,boolean show, BufferedReader in)
	{
		String line;
		String vlaue = new String("");
		try
		{
			while ((line = in.readLine()) != null)
			{
				if (line.contains(signal))
				{
					vlaue = line.substring(signal.length());
					break;
				}
			}
		} catch (IOException e)
		{
			if (show)
				SystemOut.println(e);
		}
		return (vlaue);
	}
	
	private void SetStaticVar(boolean show)
	{
		try
		{
			String filename = svapi.getConfigFile();
			BufferedReader in = new BufferedReader(new FileReader(filename));
			
			dowhatVer = new Integer(readVarInFile("dowhatversion=", show, in));
			precacheTPL = new Integer(readVarInFile("precacheTemplateBean=", show, in));
			enableHttpGetJson =  new Integer(readVarInFile("enableHttpGetJson=", show, in));
			
			String admin= readVarInFile("adminPassword=", show, in);
			String user= readVarInFile("userPassword=", show, in);
			
			if(admin!=null && !admin.isEmpty())
				adminMd5= MD5(admin);
			if(user!=null && !user.isEmpty())
				userMd5= MD5(user);			
			
			if (show)
			{
				SystemOut.println("\nSetDowhatVersion: " + dowhatVer);
				SystemOut.println("SetPreCacheTemplateBean: " + precacheTPL);
				SystemOut.println("enableHttpGetJson: " + enableHttpGetJson);
				
				SystemOut.println("\nadminPassword: \"" + admin+ "\"\n   md5-digest: "+adminMd5);
				SystemOut.println(" userPassword: \"" + user + "\"\n   md5-digest: "+userMd5);
				SystemOut.println("\n\n");
			}
			
			in.close();
		} catch (Exception e)
		{
			if (show)
				SystemOut.println(e);
		}
	}
	
	private void test8(boolean show)
	{
		Map<String, Map<String, String>> fmap = new HashMap<String, Map<String, String>>();
		Map<String, String> ndata = new HashMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "LoadResource");
		boolean ret = svapi.GetUnivData(fmap, ndata, estr);
		FilterNotXMLChar.FilterMapInMap(fmap, estr);
		
		SystemOut.println("\n\n");
		
		SystemOut.println("test8 GetUnivData:" + ret);
		SystemOut.println("test8 estr:" + estr);
		SystemOut.println("test8 get " + fmap.size() + " node");
		if (fmap.containsKey("property"))
			SystemOut.println("test8 get " + fmap.get("property").size() + " key");
	}
	
	public keyValue[] test3(keyValue[] inwhat)
	{
		SystemOut.println("---------- test3 receive data --------");
		try
		{
			for (keyValue one : inwhat)
				SystemOut.println("   " + one.key + "  " + one.value);	
		} catch (Exception e)
		{
			SystemOut.println(e);	
		}
		SystemOut.println("----------  end of  test3 receive data --------");
		keyValue[] ret = inwhat.clone();
		return ret;
	}
	
	public RetMapInVector php_GetUnivData2(keyValue[] inwhat)
	{
		HashMap<String, String> ndata = new HashMap<String, String>();
		try
		{
			for (keyValue one : inwhat)
				ndata.put(one.key, one.value);
		} catch (Exception e)
		{
			SystemOut.println(e);	
		}
		return GetUnivData2(ndata);
	}
	
	public RetMapInVector php_SubmitUnivData2(keyValue[][] inlist, keyValue[] inwhat)
	{
		Vector<Map<String, String>> invmap = new Vector<Map<String, String>>();
		Map<String, String> ndata = new HashMap<String, String>();
		try
		{
			for (keyValue one : inwhat)
				ndata.put(one.key, one.value);
			
			for (keyValue[] onelist : inlist)
			{
				HashMap<String, String> data = new HashMap<String, String>();	
				for (keyValue one : onelist)
					data.put(one.key, one.value);
				invmap.add(data);
			}
		} catch (Exception e)
		{
			SystemOut.println(e);	
		}
		return SubmitUnivData2(invmap,ndata);	
	}
	
	public RetMapInVector php_GetForestData(keyValue[] inwhat)
	{
		HashMap<String, String> ndata = new HashMap<String, String>();
		try
		{
			for (keyValue one : inwhat)
				ndata.put(one.key, one.value);
		} catch (Exception e)
		{
			SystemOut.println(e);	
		}
		return GetForestData(ndata);	
	}
	
	/**
	 * 测试接口
	 */
	public String test1(String str)
	{
		return new String(str);
	}
	
	/**
	 * 测试接口
	 */
	public Map<String, String> test2(Map<String, Map<String, String>> fmap)
	{
		SystemOut.println("EccService test2 ，测试返回特殊字符 <>&= ");
		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("return", "ok");
		ndata.put(" <>&=", "good < > & = 数据库 ！·#￥%￥￥……%%—《》》？><??>?!@#$#%$@#$(<>)_+_+JHGJ~``");
		return ndata;
	}
	
	/**
	 * 此函数对应于 Jsvapi.GetUnivData
	 * 
	 * @return 从 ecc api(业务逻辑层)返回的数据 RetMapInMap
	 * @inwhat 传入的请求
	 * 
	 */
	public RetMapInMap GetUnivData(Map<String, String> inwhat)
	{
		long start = System.currentTimeMillis();
		
		Map<String, Map<String, String>> fmap = new HashMap<String, Map<String, String>>();
		StringBuilder estr = new StringBuilder();
		boolean ret = true;
		if (!checkmd5(inwhat))
		{
			SystemOut.println("EccService md5 授权失败;   ");
			return new RetMapInMap(false, " md5 授权失败;   ", fmap);
		}
			
		String dowhat = new String(" ");
		try
		{
			dowhat = inwhat.get("dowhat");
			if (dowhat.equals("ResetUserPassword"))
				return ResetUserPassword(inwhat);
			
			if (dowhat.equals("EmailTest"))
			{
				String byJava = new String("");
				if (inwhat.containsKey("byJava"))
				{
					byJava = inwhat.get("byJava");
					if (byJava.compareToIgnoreCase("true") == 0)
					{
						TestFunc func = new TestFunc();
						return func.test88(inwhat);
					}
				}
			}
		} catch (Exception e)
		{
		}
		
		try
		{
			if (dowhat.equals("GetFileWithBase64")|| dowhat.equals("GetFileNameList") || dowhat.equals("DeleteFile") || dowhat.equals("UploadFileWithBase64") ) 
			{
				RetMapInMap rmap= GetFileWithBase64.tryGetFile(inwhat);
				fmap= rmap.getFmap();
				estr= new StringBuilder(rmap.getEstr());
				ret = rmap.getRetbool();
			} else
			{
				ret = svapi.GetUnivData(fmap, inwhat, estr);
				FilterNotXMLChar.FilterMapInMap(fmap, estr);
			}
		} catch (Exception e)
		{
			estr.append(e + " ;  ");
			SystemOut.println(e);
			ret = false;
		}
		SystemOut.println("EccService GetUnivData dowhat: " + dowhat + "\n                         /ret: " + ret + "   run:" + (float) (System.currentTimeMillis() - start) / 1000 + " s");
		return new RetMapInMap(ret, estr.toString(), fmap);
	}
	
	/**
	 * 此函数对应于 Jsvapi.SubmitUnivData
	 * 
	 * @return 从 ecc api(业务逻辑层)返回的数据 RetMapInMap
	 * @fmap 要提交给 ecc api 的数据
	 * @inwhat 传入的请求
	 * 
	 */
	public RetMapInMap SubmitUnivData(Map<String, Map<String, String>> fmap, Map<String, String> inwhat)
	{
		Map<String,RetMapInMap> retValue = new HashMap<String,RetMapInMap>();
		
		long start = System.currentTimeMillis();
		
		if (!checkmd5(inwhat))
		{
			SystemOut.println("EccService md5 授权失败;   ");
			fmap.clear();
			return new RetMapInMap(false, " md5 授权失败;   ", fmap);
		}
		
		String dowhat = new String(" ");
		try
		{
			dowhat = inwhat.get("dowhat");
		} catch (Exception e)
		{
		}
		
		StringBuilder estr = new StringBuilder();
		boolean ret = true;
		try
		{
			ret = svapi.SubmitUnivData(fmap, inwhat, estr);
		} catch (Exception e)
		{
			estr.append(e + " ;  ");
			SystemOut.println(e);
			ret = false;
		}
		SystemOut.println("EccService SubmitUnivData dowhat: " + dowhat + "\n                         /ret: " + ret + "   run:" + (float) (System.currentTimeMillis() - start) / 1000 + " s");
		return new RetMapInMap(ret, estr.toString(), fmap);
	}
	
	/**
	 * 此函数对应于 Jsvapi.GetForestData
	 * 
	 * @return 从 ecc api(业务逻辑层)请求获得的树数据 RetMapInVector
	 * @inwhat 传入的请求
	 * 
	 */
	public RetMapInVector GetForestData(Map<String, String> inwhat)
	{
		long start = System.currentTimeMillis();
		
		Vector<Map<String, String>> vmap = new Vector<Map<String, String>>();
		StringBuilder estr = new StringBuilder();
		boolean ret = true;		
		if (!checkmd5(inwhat))
		{
			SystemOut.println("EccService md5 授权失败;   ");
			return new RetMapInVector(false, " md5 授权失败;   ", vmap);
		}
		
		String dowhat = new String(" ");
		try
		{
			dowhat = inwhat.get("dowhat");
		} catch (Exception e)
		{
		}
		
		try
		{
			ret = svapi.GetForestData(vmap, inwhat, estr);
			FilterNotXMLChar.FilterMapInVector(vmap, estr);
		} catch (Exception e)
		{
			estr.append(e + " ;  ");
			SystemOut.println(e);
			ret = false;
		}
		SystemOut.println("EccService GetForestData dowhat: " + dowhat + "\n                         /ret: " + ret + "   run:" + (float) (System.currentTimeMillis() - start) / 1000 + " s");
		return new RetMapInVector(ret, estr.toString(), vmap);
	}
	

	public RetMapInVector GetUnivData2(Map<String, String> inwhat)
	{
		RetMapInMap rmap= GetUnivData(inwhat);
		Vector<Map<String, String>> vmap = new Vector<Map<String, String>>();	
		String estr= rmap.getEstr();
		boolean ret = rmap.getRetbool();
		
		try
		{			
			HashMap<String, String> ndata= new HashMap<String, String>();
			
			Integer index= new Integer(0);  
			for (String key : rmap.getFmap().keySet())
			{
				vmap.add(rmap.getFmap().get(key));
				ndata.put(index.toString(),key);
				++index;
			}
			vmap.add(0,ndata);
		} catch (Exception e)
		{
			estr= new String(estr + e + " ;  ");
			SystemOut.println(e);
			ret = false;
		}
		return new RetMapInVector(ret, estr.toString(), vmap);
	}
	

	public RetMapInVector SubmitUnivData2(Vector<Map<String, String>> invmap,Map<String, String> inwhat)
	{
		Vector<Map<String, String>> vmap = new Vector<Map<String, String>>();
		StringBuilder estr = new StringBuilder();
		boolean ret = true;
		
		try
		{
			boolean inok= false;
			Map<String, Map<String, String>> infmap = new HashMap<String, Map<String, String>>();
			if (invmap.size() >= 1)
			{
				Map<String, String> names = invmap.get(0);
				if ((names.size() + 1) == invmap.size())
				{
					inok= true;
					for (int i = 1; i < invmap.size(); ++i)
					{
						Map<String, String> ndata = invmap.get(i);
						Integer ikey= new Integer(i-1); 
						infmap.put(names.get(ikey.toString()).toString(), ndata);
					}
				}
			}
			
			RetMapInMap rmap = SubmitUnivData(infmap, inwhat);
			estr=estr.append(rmap.getEstr());
			ret= rmap.getRetbool();
			
			HashMap<String, String> ndata= new HashMap<String, String>();
			if(!inok)
				estr=estr.append(" 要提交给服务器的数据 invmap 非法;  ");
			
			Integer index= new Integer(0);  
			for (String key : rmap.getFmap().keySet())
			{
				vmap.add(rmap.getFmap().get(key));
				ndata.put(index.toString(),key);
				++index;
			}
			vmap.add(0,ndata);
		} catch (Exception e)
		{
			estr.append(e + " ;  ");
			e.printStackTrace();
			ret = false;
		}
		return new RetMapInVector(ret, estr.toString(), vmap);
	}
	
	public static String MD5(String s)
	{
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try
		{
			byte[] strTemp = s.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			for (int k = 0, i = 0; i < j; i++)
			{
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			String ret = new String(str);
			return ret;
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	private boolean checkmd5(Map<String, String> inwhat)
	{
		if (adminMd5 == null || userMd5 == null)
			return true;

		try
		{
			String md5 = inwhat.get("md5-digest");
			if (md5 == null || md5.isEmpty())
				return false;
			
			if(md5.equals(userMd5)|| md5.equals(adminMd5))
				return true;	
		} catch (Exception e)
		{
		}	
		return false;
	}
	
	synchronized static private void setUserMd5(String tmd5)
	{
		userMd5= tmd5;
	}
	
	private RetMapInMap ResetUserPassword(Map<String, String> inwhat)
	{
		boolean ret = true;
		StringBuilder estr = new StringBuilder("");
		Map<String, Map<String, String>> fmap = new HashMap<String, Map<String, String>>();
		
		String newUserPassword= null;	
		try
		{
			newUserPassword= inwhat.get("newUserPassword");
		} catch (Exception e)
		{
		}
		String md5= null;
		try
		{
			md5= inwhat.get("md5-digest");
		} catch (Exception e)
		{
		}
		
		if(adminMd5==null || adminMd5.isEmpty())
		{
			ret= false;
			estr.append("  服务器端未设置\"管理口令md5密串\", ResetUserPassword ;   ");
		}
		else if (md5 == null || !md5.equals(adminMd5))
		{
			ret = false;
			estr.append("  提供的\"管理口令md5密串\"无效, ResetUserPassword ;   ");
		}
		
		if(ret)
		{
			if(newUserPassword==null || newUserPassword.isEmpty())
			{
				setUserMd5(null);
				estr.append("  服务器端的\"一般口令md5密串\"已经置空，服务器端将接受所有请求, ResetUserPassword   ;   ");
			}
			else
			{
				String tempmd5 = MD5(newUserPassword);
				setUserMd5(tempmd5);
				estr.append("  服务器端的\"一般口令md5密串\"已经重置为："+tempmd5+" , ResetUserPassword   ;   ");
			}
		}
		
		SystemOut.println(estr);		
		HashMap<String, String> ndata1 = new HashMap<String, String>();
		if(ret)
			ndata1.put("return", "true");
		else
			ndata1.put("return", "false");
		fmap.put("return", ndata1);
		return new RetMapInMap(ret, estr.toString(), fmap);
	}
}
