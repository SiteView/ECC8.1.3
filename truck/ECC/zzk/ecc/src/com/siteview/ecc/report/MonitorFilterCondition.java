package com.siteview.ecc.report;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class MonitorFilterCondition {
	private final static Logger logger = Logger.getLogger(MonitorFilterCondition.class);

	private final String LIKE = "like";

	private final String EQU = "=";

	private final String NEQU = "<>";

	private String monitorName;

	public String getMonitorName() {
		return monitorName;
	}

	public void setMonitorName(String monitorName) {
		this.monitorName = monitorName;
	}

	public String getMonitorCondition() {
		return monitorCondition;
	}

	public void setMonitorCondition(String monitorCondition) {
		this.monitorCondition = monitorCondition;
	}

	public String getGroupLogic() {
		return groupLogic;
	}

	public void setGroupLogic(String groupLogic) {
		this.groupLogic = groupLogic;
	}

	public String getGroupCondition() {
		return groupCondition;
	}

	public void setGroupCondition(String groupCondition) {
		this.groupCondition = groupCondition;
	}

	public List<String> getGroupName() {
		return groupName;
	}

	public void setGroupName(List<String> groupName) {
		this.groupName = groupName;
	}

	public List<String> getTypeName() {
		return typeName;
	}

	public void setTypeName(List<String> typeName) {
		this.typeName = typeName;
	}

	public String getTypeLogic() {
		return typeLogic;
	}

	public void setTypeLogic(String typeLogic) {
		this.typeLogic = typeLogic;
	}

	public String getTypeCondition() {
		return typeCondition;
	}

	public void setTypeCondition(String typeCondition) {
		this.typeCondition = typeCondition;
	}

	public String getOper() {
		return oper;
	}

	public void setOper(String oper) {
		this.oper = oper;
	}

	public String getFreq() {
		return freq;
	}

	public void setFreq(String freq) {
		this.freq = freq;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	public String getKeyValueOper() {
		return keyValueOper;
	}

	public void setKeyValueOper(String keyValueOper) {
		this.keyValueOper = keyValueOper;
	}


	private String monitorCondition;
	// group
	private String groupLogic;

	private String groupCondition;

	private List<String> groupName;
	// type
	private List<String> typeName;

	private String typeLogic;

	private String typeCondition;
	//
	private String oper;

	private String freq = "";

	private String keyValue;

	private String keyValueOper;

	/**
	 * 
	 * @param monitorName
	 *            监测器名
	 * @param monitorCondition
	 *            条件 = ,<> ,like
	 * @param groupLogic
	 *            组名称的逻辑运算，or,and
	 * @param groupCondition
	 *            组名称的条件 =，<>,like
	 * @param groupName
	 *            组名称(List)
	 * @param typeName
	 *            类型名称(List)
	 * @param typeLogic
	 *            类型名称的逻辑运算，or,and
	 * @param typeCondition
	 *            组名称的条件 =，<>,like
	 * @param freqOper
	 *            监测频率运算符=,<>,>,>=,<,<=
	 * @param freq
	 *            检测频率值
	 */
	public MonitorFilterCondition(){}
	public MonitorFilterCondition(String monitorName, String monitorCondition,
			String groupLogic, String groupCondition, List<String> groupName,
			List<String> typeName, String typeLogic, String typeCondition,
			String freqOper, String freq, String keyValue, String keyValueOper) {
		super();
		this.monitorName = monitorName;
		this.monitorCondition = monitorCondition;
		this.groupLogic = groupLogic;
		this.groupCondition = groupCondition;
		this.groupName = groupName;
		this.typeName = typeName;
		this.typeLogic = typeLogic;
		this.typeCondition = typeCondition;
		this.oper = freqOper;
		this.freq = freq;
		this.keyValue = keyValue;
		this.keyValueOper = keyValueOper;
	}

	
	/**
	 * 如果在src中能找到dest字符串，返回true否则返回false
	 * 
	 * @param src
	 *            原字符串
	 * @param dest
	 *            要查找的字符串
	 */
	public boolean operatorLike(String src, String dest) {
		if(src==null) return false;
		if(dest==null) return false;
		String tmpSrc = src.replaceAll("\\s+", "").toLowerCase();
		String tmpDest = dest.replaceAll("\\s+", "").toLowerCase();
		if (tmpSrc.contains(tmpDest))
			return true;
		return false;
	}

	/**
	 * 如果src和dest内容相同返回true,否则返回false
	 * 
	 * @param src
	 * @param dest
	 * @return
	 */
	public boolean operatorEqu(String src, String dest) {
		if(src==null) return false;
		if(dest==null) return false;
		if (src.replaceAll("\\s+", "").equalsIgnoreCase(dest.replaceAll("\\s+", ""))) {
			return true;
		}
		return false;
	}

	/**
	 * 检测器运算
	 * 
	 * @param oper
	 * @param src
	 * @param dest
	 * @return
	 */
	public boolean monitorOperator(String src) {
		// 如果要找的字符串为空，则显示全部
		if (monitorName == null || monitorName.equals(""))
			return true;
		if (monitorCondition.equals("like")) {
			return operatorLike(src, monitorName);
		} else if (monitorCondition.equals("=")) {
			return operatorEqu(src, monitorName);
		} else {
			return !operatorEqu(src, monitorName);
		}
	}

	/**
	 * group
	 * 
	 * @param src
	 * @param dest
	 * @return
	 */
	public boolean groupOperator(String src) {
		if (groupName == null || groupName.size() == 0)
			return true;
		if(groupLogic.equals("or")){
			boolean flag = false;
			if (groupCondition.equals("like")) {
				for (String key : groupName) {
					if (operatorLike(src, key))
						flag=true;
				}
			} else if (groupCondition.equals("=")) {
				for (String key : groupName) {
					if (operatorEqu(src, key))
						flag=true;
				}
			} else {
				for (String key : groupName) {
					if (!operatorEqu(src, key))
						flag=true;
				}
			}
			return flag;
		}else{
			boolean flag = true;
			if (groupCondition.equals("like")) {
				for (String key : groupName) {
					if (!operatorLike(src, key))
						flag=false;
				}
			} else if (groupCondition.equals("=")) {
				for (String key : groupName) {
					if (!operatorEqu(src, key))
						flag=false;
				}
			} else {
				for (String key : groupName) {
					if (operatorEqu(src, key))
						flag=false;
				}
			}
			return flag;
		}
	}

	/**
	 * type
	 * 
	 * @param src
	 * @param dest
	 * @return
	 */
	public boolean typeOperator(String src) {
		if (typeName == null || typeName.size() == 0)
			return true;
		if(typeLogic.equals("or")){
			boolean flag = false;
			if (typeCondition.equals("like")) {
				for (String key : typeName) {
					if (operatorLike(src, key))
						flag = true;
				}
			} else if (typeCondition.equals("=")) {
				for (String key : typeName) {
					if (operatorEqu(src, key))
						flag = true;
				}
			} else {
				for (String key : typeName) {
					if (!operatorEqu(src, key))
						flag = true;
				}
			}
			return flag;
		}else{
			boolean flag = true;
			if (typeCondition.equals("like")) {
				for (String key : typeName) {
					if (!operatorLike(src, key))
						flag = false;
				}
			} else if (typeCondition.equals("=")) {
				for (String key : typeName) {
					if (!operatorEqu(src, key))
						flag = false;
				}
			} else {
				for (String key : typeName) {
					if (operatorEqu(src, key))
						flag = false;
				}
			}
			return flag;
		}
	}

	/**
	 * 检测频率
	 * 
	 * @param src
	 * @param dest
	 * @return
	 */
	public boolean freqOperator(String src) {
		if (freq == null || freq.equals("监测频率"))
			return true;
	
		if((src.endsWith("分钟")&&freq.endsWith("分钟")) || (src.endsWith("小时")&&freq.endsWith("小时"))){
			int si = src.indexOf("分钟") > 0 ? src.indexOf("分钟") : src.indexOf("小时");
			int di = freq.indexOf("分钟") > 0 ? freq.indexOf("分钟") :freq.indexOf("小时");
			if (si <= 0 || di <= 0)
				return true;
			double fs = Double
					.parseDouble(src.subSequence(0, si).toString().trim());
			double fd = Double.parseDouble(freq.subSequence(0, di).toString()
					.trim());
			if (oper.equals("=")) {
				return fd == fs;
			} else if (oper.equals("<>")) {
				return fd != fs;
			} else if (oper.equals(">=")) {
				return fd <= fs;
			} else if (oper.equals(">")) {
				return fd < fs;
			} else if (oper.equals("<")) {
				return fd > fs;
			} else if (oper.equals("<=")) {
				return fd >= fs;
			} else if (oper.equals("like")) {
				return fd == fs;
			}
		}else if(src.endsWith("小时")&& freq.endsWith("分钟")){
			if(oper.equals("=")||oper.equals("<=")||oper.equals("<") || oper.equals("like")) return false;
			else return true;
		}else if(src.endsWith("分钟")&& freq.endsWith("小时")){
			if(oper.equals("=")||oper.equals(">=")||oper.equals(">") || oper.equals("like")) return false;
			else return true;
		}
		return false;
	}

	public boolean frequencyOperator(String src) {
		// 如果要找的字符串为空，则显示全部
		if (keyValue == null || keyValue.equals(""))
			return true;
		if (keyValueOper.equals("like")) {
			return operatorLike(src, keyValue);
		} else if (keyValueOper.equals("=")) {
			return operatorEqu(src, keyValue);
		} else {
			return !operatorEqu(src, keyValue);
		}
	}
	
	public static void main(String[] args){
		String dest = "\r\n[包成功率(%) > 75]\r\n[包成功率(%) == 100]\r\n[包成功率(%) == 0]\r\n";
		Pattern p = Pattern.compile("(\\s*)*",Pattern.MULTILINE);
		String rtn = dest.replaceAll("\\s+", "");
		logger.info(rtn);
	}
}
