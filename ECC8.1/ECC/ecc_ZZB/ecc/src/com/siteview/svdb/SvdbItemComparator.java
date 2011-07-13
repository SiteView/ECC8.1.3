package com.siteview.svdb;

import java.util.Comparator;

public class SvdbItemComparator implements Comparator {

	boolean ascending = false;
	boolean isNumber = false;
	String propKey ;

	public SvdbItemComparator(String propKey, boolean isNumber, boolean ascending) {
		this.ascending = ascending;
		this.isNumber = isNumber;
		this.propKey = propKey;
	}
	@Override
	public int compare(Object row1, Object row2) {

		if(row1==null||row2==null)
			return 0;
		String v1=((SvdbItem)row1).getPropMap().get(propKey);;
		String v2=((SvdbItem)row2).getPropMap().get(propKey);;
		try {
			if (isNumber)
			{
				if(ascending)
					return (int) (Long.valueOf(v1) - Long.valueOf(v2));
				else
					return (int) (Long.valueOf(v2) - Long.valueOf(v1));
			}
			else
			{
				if(ascending)
					return v1.compareTo(v2);
				else
					return v2.compareTo(v1);
			}
		} catch (Exception e) {
			return 0;
		}
	}

}
