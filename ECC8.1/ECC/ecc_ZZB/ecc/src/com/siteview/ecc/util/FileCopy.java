package com.siteview.ecc.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class FileCopy {
	public  void copy(String src, String target)  {
		BufferedReader br =null;
		BufferedWriter bw =null;
		try{
		br = new BufferedReader(new FileReader(src));
		bw = new BufferedWriter(new FileWriter(target));
		String s;
		while (br.ready()) {
			s = br.readLine();
			bw.write(s);
			bw.newLine();
		}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try{br.close();}catch(Exception e){}
			try{bw.close();}catch(Exception e){}
		}
	}

}
