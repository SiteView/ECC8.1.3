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
	 *            �������
	 * @param monitorCondition
	 *            ���� = ,<> ,like
	 * @param groupLogic
	 *            �����Ƶ��߼����㣬or,and
	 * @param groupCondition
	 *            �����Ƶ����� =��<>,like
	 * @param groupName
	 *            ������(List)
	 * @param typeName
	 *            ��������(List)
	 * @param typeLogic
	 *            �������Ƶ��߼����㣬or,and
	 * @param typeCondition
	 *            �����Ƶ����� =��<>,like
	 * @param freqOper
	 *            ���Ƶ�������=,<>,>,>=,<,<=
	 * @param freq
	 *            ���Ƶ��ֵ
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
	 * �����src�����ҵ�dest�ַ���������true���򷵻�false
	 * 
	 * @param src
	 *            ԭ�ַ���
	 * @param dest
	 *            Ҫ���ҵ��ַ���
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
	 * ���src��dest������ͬ����true,���򷵻�false
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
	 * ���������
	 * 
	 * @param oper
	 * @param src
	 * @param dest
	 * @return
	 */
	public boolean monitorOperator(String src) {
		// ���Ҫ�ҵ��ַ���Ϊ�գ�����ʾȫ��
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
	 * ���Ƶ��
	 * 
	 * @param src
	 * @param dest
	 * @return
	 */
	public boolean freqOperator(String src) {
		if (freq == null || freq.equals("���Ƶ��"))
			return true;
	
		if((src.endsWith("����")&&freq.endsWith("����")) || (src.endsWith("Сʱ")&&freq.endsWith("Сʱ"))){
			int si = src.indexOf("����") > 0 ? src.indexOf("����") : src.indexOf("Сʱ");
			int di = freq.indexOf("����") > 0 ? freq.indexOf("����") :freq.indexOf("Сʱ");
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
		}else if(src.endsWith("Сʱ")&& freq.endsWith("����")){
			if(oper.equals("=")||oper.equals("<=")||oper.equals("<") || oper.equals("like")) return false;
			else return true;
		}else if(src.endsWith("����")&& freq.endsWith("Сʱ")){
			if(oper.equals("=")||oper.equals(">=")||oper.equals(">") || oper.equals("like")) return false;
			else return true;
		}
		return false;
	}

	public boolean frequencyOperator(String src) {
		// ���Ҫ�ҵ��ַ���Ϊ�գ�����ʾȫ��
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
		String dest = "\r\n[���ɹ���(%) > 75]\r\n[���ɹ���(%) == 100]\r\n[���ɹ���(%) == 0]\r\n";
		Pattern p = Pattern.compile("(\\s*)*",Pattern.MULTILINE);
		String rtn = dest.replaceAll("\\s+", "");
		logger.info(rtn);
	}
}
