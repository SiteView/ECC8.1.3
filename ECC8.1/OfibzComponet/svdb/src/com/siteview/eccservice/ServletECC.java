
package com.siteview.eccservice;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import com.siteview.jsvapi.Jsvapi;


import net.sf.json.JSONObject;
import net.sf.json.JSONArray; 


public class ServletECC extends HttpServlet
{
	EccService service= null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		Date date = new Date();
		String pinfo = request.getPathInfo();
		SystemOut.println("  " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds() + "(h:m:s) doPost: " + pinfo);		
		
		BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String content = br.readLine();
		int len = content.length();
		if (len < 300)
			SystemOut.println("    " + content);
		else
			SystemOut.println("    Lenght of incoming data by post: " + len);
		if (content!=null && !content.isEmpty())
			pinfo= content;
	
		if(tryFunction_submit("/submitunivdata/",pinfo,out))
			return;			
		if(tryFunction("/getunivdata/",pinfo,out))
			return;
		if(tryFunction("/getforestdata/",pinfo,out))
			return;
		PrintBad(out);
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		Date date = new Date();
		String pinfo= request.getPathInfo();
		SystemOut.println("  " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds() + "(h:m:s): " );
		int len = pinfo.length();
		if (len < 300)
			SystemOut.println("    " + pinfo);
		else
			SystemOut.println("    Lenght of PathInfo: " + len);		
	
		if(tryFunction("/getunivdata/",pinfo,out))
			return;
		if(tryFunction("/getforestdata/",pinfo,out))
			return;		
		if(tryFunction_submit("/submitunivdata/",pinfo,out))
			return;	
		PrintBad(out);
	}
	
	public void PrintBad(PrintWriter out)
	{
		out.println("Bad request, url should look like these:<br><br>http://192.168.6.156:18080/svget/getunivdata/[{\"dowhat\":\"GetMonitorTemplet\"},{\"id\":\"5\"}]<br><br>http://192.168.6.156:18080/svget/getforestdata/[{\"dowhat\":\"GetTreeData\"},{\"parentid\":\"default\"},{\"onlySon\":\"false\"}]<br><br>");
		out.println("http://192.168.6.156:18080/svget/submitunivdata/[{\"data\":{\"OperateLog_1\":{\"_UserID\":\"1\",\"_OperateTime\":\"2008-05-12 14:28:00\",\"_OperateType\":\"4\",\"_OperateObjName\":\"3\",\"_OperateObjInfo\":\"wenchuang\"},\"OperateLog_2\":{\"_UserID\":\"1\",\"_OperateTime\":\"2008-05-12 14:28:00\",\"_OperateType\":\"4\",\"_OperateObjName\":\"3\",\"_OperateObjInfo\":\"wenchuang\"}}},{\"dowhat\":\"AppendOperateLog\"}]");
		out.println("<br><br><br>If length of your url is bigger than 500, you should post your data as this:<br>url:  //192.168.6.156:18080/svget/submitunivdata/[{\"dowhat\":\"SubmitGroup\"},{\"parentid\":\"1.91\"}]) <br>post:/submitunivdata/[{\"data\":{\"return\":{\"id\":\"\"},\"property\":{\"sv_disable\":\"false\",\"sv_name\":\"test_group\",\"sv_description\":\"test_group\"}}},{\"dowhat\":\"SubmitGroup\"},{\"parentid\":\"1.91\"}]");
	}
	
	public String getData()
	{
		try
		{
			BufferedReader in = new BufferedReader(new FileReader("D:\\Program Files\\Apache2.2\\htdocs\\yui\\phpsoap\\samples\\hellotree.data"));
			return in.readLine();
		} catch (Exception e)
		{
			Properties properties = System.getProperties();
			SystemOut.println(" 当前目录是： "+ properties.getProperty("user.dir"));   
			SystemOut.println(e);
			return new String("exception");
		}
	}
	
	public void PrintData2(PrintWriter out)
	{
		if (service == null)
			service = new EccService();

		HashMap<String, String> inwhat = new HashMap<String, String>();
		
		inwhat.put("dowhat", "GetTreeData2");
		inwhat.put("parentid", "default");
		inwhat.put("onlySon", "false");
		inwhat.put("needKeys", "sv_name,sv_id,type");
		
		RetMapInVector vmap= service.GetForestData(inwhat);
		Vector<Map<String, String>> fmap = vmap.getVmap();
		
		boolean first1 = true;
		StringBuilder ret= new StringBuilder("[");
		for (int key = 0; key < fmap.size(); ++key)
		{
			Map<String, String> ndata = fmap.get(key);
			if (ndata == null || ndata.isEmpty())
				continue;
			
			if(!first1)
				ret.append(",");
			first1= false;
			ret.append("{");
			boolean first2 = true;
			for (String nkey : ndata.keySet())
			{
				if(!first2)
					ret.append(",");
				ret.append("\"" + nkey + "\":\"" + ndata.get(nkey) + "\"");
				first2= false;
			}
			ret.append("}");
		}
		ret.append("];;");
		
		out.println(" return: "+ vmap.getRetbool());
		out.println("   estr: "+ vmap.getEstr());
		out.println("EccService-ReturnData: ");
		out.println(ret.toString());
	}
	
	public boolean tryFunction(String startstr, String pinfoin, PrintWriter out)
	{
		String pinfo = new String(pinfoin);
		try
		{
			if (pinfo == null || pinfo.isEmpty() || !pinfo.startsWith(startstr))
				return false;
			
			pinfo = pinfo.substring(startstr.length());
			JSONArray jsonArray = JSONArray.fromObject(pinfo);
			HashMap<String, String> ndata = new HashMap<String, String>();
			for (Iterator iter = jsonArray.listIterator(); iter.hasNext();)
			{
				JSONObject jobj = (JSONObject) iter.next();
				HashMap<String, String> tdata = (HashMap<String, String>) JSONObject.toBean(jobj, HashMap.class);
				for (String nkey : tdata.keySet())
					ndata.put(nkey, tdata.get(nkey));
			}
			PrintData(startstr, out, ndata, null);
			
		} catch (Exception e)
		{
			SystemOut.println(e);
//			e.printStackTrace();
			out.println(e+"<br><br><br><br><br><br>");
			return false;
		}
		return true;
	}
	
	public void PrintData(String startstr, PrintWriter out, Map<String, String> inwhat,Map<String, Map<String, String>> infmap)
	{
		if(service==null)
		{
//			Date date = new Date();
//			SystemOut.println("  new an EccService at:"+ date.getHours() + "h " + date.getMinutes() + "m " + date.getSeconds()+"s");
			service = new EccService();
		}
		if(!service.enableHttpGetJson())
		{
			out.println("Need to set \"enableHttpGetJson=1\" in \\tomcat-6.0.14\\bin\\svapi.ini");
			return;		
		}

		if (startstr.equalsIgnoreCase("/submitunivdata/"))
		{
			RetMapInMap rmap = service.SubmitUnivData(infmap, inwhat);
			Map<String, Map<String, String>> fmap = rmap.getFmap();
			
			out.println(" return: " + rmap.getRetbool());
			out.println("   estr: " + rmap.getEstr());
			out.println("EccService-ReturnData: ");
			JSONObject jsonObject = JSONObject.fromObject(fmap);
			out.println(jsonObject);
			return;
		}
		if (startstr.equalsIgnoreCase("/getunivdata/"))
		{
			RetMapInMap rmap = service.GetUnivData(inwhat);
			Map<String, Map<String, String>> fmap = rmap.getFmap();
			
			out.println(" return: " + rmap.getRetbool());
			out.println("   estr: " + rmap.getEstr());
			out.println("EccService-ReturnData: ");
			JSONObject jsonObject = JSONObject.fromObject(fmap);
			out.println(jsonObject);
			return;
		}
		if (startstr.equalsIgnoreCase("/getforestdata/"))
		{
			RetMapInVector rmap = service.GetForestData(inwhat);
			Vector<Map<String, String>> vmap = rmap.getVmap();
			
			out.println(" return: " + rmap.getRetbool());
			out.println("   estr: " + rmap.getEstr());
			out.println("EccService-ReturnData: ");
			JSONArray jsonArray = JSONArray.fromObject(vmap);
			out.println(jsonArray);
			return;
		}		
	}	
	
	public boolean tryFunction_submit(String startstr, String pinfoin, PrintWriter out)
	{
		String pinfo = new String(pinfoin);
		try
		{
			if (pinfo == null || pinfo.isEmpty() || !pinfo.startsWith(startstr))
				return false;

			pinfo = pinfo.substring(startstr.length());
			JSONArray jsonArray = JSONArray.fromObject(pinfo);
			Map<String, String> ndata = new HashMap<String, String>();
			Map<String, Map<String, String>> fmap = new HashMap<String, Map<String, String>>();

			boolean first=true;
			for (Iterator iter = jsonArray.listIterator(); iter.hasNext();)
			{
				JSONObject jobj = (JSONObject) iter.next();
				if (first)
				{
					first= false;
					JSONObject jobj2 = (JSONObject) jobj.get("data");
					HashMap<String, Object> tobj = (HashMap<String, Object>) JSONObject.toBean(jobj2, HashMap.class);
					for (String tkey : tobj.keySet())
					{
						HashMap<String, String> node = new HashMap<String, String>();
						HashMap<String, String> tdata = (HashMap<String, String>) JSONObject.toBean((JSONObject)jobj2.get(tkey), HashMap.class);
						for (String nkey : tdata.keySet())
							node.put(nkey, tdata.get(nkey));
						fmap.put(tkey, node);
					}
					continue;
				}
				
				HashMap<String, String> tdata = (HashMap<String, String>) JSONObject.toBean(jobj, HashMap.class);
				for (String nkey : tdata.keySet())
					ndata.put(nkey, tdata.get(nkey));
			}
			PrintData(startstr, out, ndata, fmap);
			
		} catch (Exception e)
		{
			SystemOut.println(e);
//			e.printStackTrace();
			out.println(e+"<br><br><br><br><br><br>");
			return false;
		}
		return true;
	}
}








