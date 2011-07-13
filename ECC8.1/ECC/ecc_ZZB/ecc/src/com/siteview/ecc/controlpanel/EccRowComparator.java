package com.siteview.ecc.controlpanel;

import java.util.Comparator;
import java.util.Map;

public class EccRowComparator implements Comparator {

	EccListModel model;
	boolean ascending = false;
	boolean isNumber = false;
	int idxCol = 0;

	public static Comparator createComparator(EccListModel model,int idxCol, boolean isNumber, boolean ascending)
	{
		return new EccRowComparator(model,idxCol,isNumber,ascending);
	}
	public EccRowComparator(EccListModel model,int idxCol, boolean isNumber, boolean ascending) {
		this.model=model;
		this.ascending = ascending;
		this.isNumber = isNumber;
		this.idxCol = idxCol;
	}

	@Override
	public int compare(Object row1, Object row2) {

		if(row1==null||row2==null)
			return 0;
		ListDataBean bean = model.getValue(row1);
		Map<Integer, String> map = null;
		if(bean.getLineNum()!=0){
			map = bean.getValueByIndex(bean.getLineNum());
		}else{
			map = bean.getValueByIndex(-1);
		}
		
		String v1 = map.get(idxCol);
//		String v2=model.getValue(row1, idxCol).getDescriptioon();
		bean = model.getValue(row2);
		if(bean.getLineNum()!=0){
			map = bean.getValueByIndex(bean.getLineNum());
		}else{
			map = bean.getValueByIndex(-1);
		}
		String v2 = map.get(idxCol);
//		String v2=model.getValue(row2, idxCol).getDescriptioon();
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
