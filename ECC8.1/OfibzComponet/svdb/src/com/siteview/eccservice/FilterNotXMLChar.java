package com.siteview.eccservice;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class FilterNotXMLChar
{
	private static boolean isXMLCharacter(int c)
	{
		if (c <= 0xD7FF)
		{
			if (c >= 0x20)
				return true;
			else
			{
				if (c == '\n')
					return true;
				if (c == '\r')
					return true;
				if (c == '\t')
					return true;
				return false;
			}
		}
		
		if (c < 0xE000)
			return false;
		if (c <= 0xFFFD)
			return true;
		if (c < 0x10000)
			return false;
		if (c <= 0x10FFFF)
			return true;
		
		return false;
	}
	
	public static String checkCharacterData(String text)
	{
		if (text == null)
			return null;
		
		boolean hasbad= false;
		StringBuilder str = new StringBuilder("");
		char[] data = text.toCharArray();
		for (int i = 0, len = data.length; i < len; i++)
		{
			char c = data[i];
			int result = c;
			if (result >= 0xD800 && result <= 0xDBFF)
			{
				int high = c;		
				char c1= c;
				char c2=0;
				
				try
				{
					c2= text.charAt(i + 1);
					int low = text.charAt(i + 1);
					if (low < 0xDC00 || low > 0xDFFF)
					{
						hasbad= true;
						continue;
					}

					result = (high - 0xD800) * 0x400 + (low - 0xDC00) + 0x10000;
					i++;
				} catch (IndexOutOfBoundsException e)
				{
					hasbad= true;
					continue;
				}
				if (c2==0 || !isXMLCharacter(result))
				{
					hasbad= true;
					continue;
				}
				str.append(c1);
				str.append(c2);
				continue;
			}
			
			if (!isXMLCharacter(result))
			{
				hasbad= true;
				continue;
			}
			str.append(c);
		}
		if (hasbad)
			return str.toString();
		else
			return null;
	}
	
	public static void FilterMapInVector(Vector<Map<String, String>> fmap, StringBuilder estr)
	{
		if (fmap == null)
			return;
		try
		{
			for (int key = 0; key < fmap.size(); ++key)
			{
				Map<String, String> ndata = fmap.get(key);
				Integer strkey = new Integer(key + 1);
				FilterMap(strkey.toString(), ndata, estr);
			}
		} catch (Exception e)
		{
			estr.append(e + " in FilterMapInVector;  ");
			SystemOut.println(e);
		}
	}
	
	public static void FilterMapInMap(Map<String, Map<String, String>> fmap, StringBuilder estr)
	{
		if (fmap == null)
			return;
		try
		{
			for (String key : fmap.keySet())
			{
				Map<String, String> ndata = fmap.get(key);
				FilterMap(key, ndata, estr);
			}
		} catch (Exception e)
		{
			estr.append(e + " in FilterMapInVector;  ");
			SystemOut.println(e);
		}
	}
	
	public static void FilterMap(String key, Map<String, String> ndata, StringBuilder estr)
	{
		if (ndata == null)
			return;
		
		for (String nkey : ndata.keySet())
		{
			String in1= ndata.get(nkey);
			String value= checkCharacterData(ndata.get(nkey));
			String k= checkCharacterData(nkey);
			if(value==null && k==null)
				continue;

			if (value != null && k == null)
			{
				estr.append( key+ "/" + nkey + ", erased non-XMLChar; ");
				ndata.put(nkey, value);
			}
			if (value == null && k != null)
			{
				estr.append( key+ "/" + k + ", erased non-XMLChar; ");
				String invalue= ndata.get(nkey);
				ndata.remove(nkey);
				ndata.put(k, invalue);
			}
		}		
	}
}
