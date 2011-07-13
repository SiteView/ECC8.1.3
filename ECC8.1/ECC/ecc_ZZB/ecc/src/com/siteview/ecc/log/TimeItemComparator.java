/**
 * 
 */
package com.siteview.ecc.log;

import java.util.Calendar;
import java.util.Comparator;

import com.siteview.ecc.log.beans.LogValueBean;

/**
 * @author yuandong
 *
 */
public class TimeItemComparator implements Comparator {
	//实现时间的正确排序
    private boolean asc = false;
    private int v;  

    public Calendar splitToCalender(String s){
    	String c[]=s.split("[-: ]");
    	Calendar ca=Calendar.getInstance();
    	ca.set(Integer.valueOf(c[0]),Integer.valueOf(c[1]), Integer.valueOf(c[2]), Integer.valueOf(c[3]), Integer.valueOf(c[4]), Integer.valueOf(c[5]));
		return ca;
    	
    	
    }
    public TimeItemComparator(boolean ascending) {
       asc = ascending;

    }
    public int compare(Object o1, Object o2) {       
    	String l1 = ((LogValueBean)o1).getOperateTime();
    	String l2 = ((LogValueBean)o2).getOperateTime();
    	Calendar c1 = splitToCalender(l1);
    	Calendar c2 = splitToCalender(l2);  
    	v = c1.compareTo(c2);  
       return asc ? v: -v;
    }
}
