import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ���Ǹ������ļ������࣬������ȡ������ini�����ļ�
 * 
 * @author Ф���
 * @version 2011-07-15
 */
public final class SetOther {
	/**
	 * ��ini�����ļ��ж�ȡ������ֵ
	 * 
	 * @param file
	 *            �����ļ���·��
	 * @param section
	 *            Ҫ��ȡ�ı������ڶ�����
	 * @param variable
	 *            Ҫ��ȡ�ı�������
	 * @param defaultValue
	 *            �������Ʋ�����ʱ��Ĭ��ֵ
	 * @return ������ֵ
	 * @throws IOException
	 *             �׳��ļ��������ܳ��ֵ�io�쳣
	 */
	public static String getProfileString(String file, String section,
			String variable, String defaultValue) throws IOException {
		String strLine, value = "";
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		boolean isInSection = false;
		try {
			while ((strLine = bufferedReader.readLine()) != null) {
				strLine = strLine.trim();
				strLine = strLine.split("[;]")[0];
				String[] strArray = strLine.split("=");
				if(strArray.length==2){
					value = strArray[1].trim();
					if(value.equals(defaultValue.toString())) isInSection = true; 
				}
				 
//				isInSection = true;
				if (isInSection == true) {
					strLine = strLine.trim();
					if (strArray.length == 1) {
						value = strArray[0].trim();
						if (value.equalsIgnoreCase(variable)) {
							value = "";
							return value;
						}
					} else if (strArray.length == 2) {
						value = strArray[0].trim();
						if (value.equalsIgnoreCase(variable)) {
							value = strArray[1].trim();
							return value;
						}
					} else if (strArray.length > 2) {
						value = strArray[0].trim();
						if (value.equalsIgnoreCase(variable)) {
							value = strLine.substring(strLine.indexOf("=") + 1)
									.trim();
							return value;
						}
					}
				}
			}
		} finally {
			bufferedReader.close();
		}
		return defaultValue;
	}

	/**
	 * �޸�ini�����ļ��б�����ֵ
	 * 
	 * @param file
	 *            �����ļ���·��
	 * @param section
	 *            Ҫ�޸ĵı������ڶ�����
	 * @param variable
	 *            Ҫ�޸ĵı�������
	 * @param value
	 *            ��������ֵ
	 * @throws IOException
	 *             �׳��ļ��������ܳ��ֵ�io�쳣
	 */
	public static boolean setProfileString(String file, String section,
			String variable, String value) throws IOException {
		String fileContent, allLine, strLine, newLine, remarkStr;
		String getValue;
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		boolean isInSection = false;
		fileContent = "";
		try {

			while ((allLine = bufferedReader.readLine()) != null) {
				allLine = allLine.trim();
				if (allLine.split("[;]").length > 1)
					remarkStr = ";" + allLine.split(";")[1];
				else
					remarkStr = "";
				strLine = allLine.split(";")[0];
//				Pattern p;
//				Matcher m;
//				p = Pattern.compile("//[//s*.*//s*//]");
//				m = p.matcher((strLine));
//				if (m.matches()) {
//					p = Pattern.compile("//[//s*" + section + "//s*//]");
//					m = p.matcher(strLine);
//					if (m.matches()) {
//						isInSection = true;
//					} else {
//						isInSection = false;
//					}
//				}
				isInSection = true;
				if (isInSection == true) {
					strLine = strLine.trim();
					String[] strArray = strLine.split("=");
					getValue = strArray[0].trim();
					if (getValue.equalsIgnoreCase(variable)) {
						newLine = getValue + " = " + value + " " + remarkStr;
						fileContent += newLine + "/r/n";
						while ((allLine = bufferedReader.readLine()) != null) {
							fileContent += allLine + "/r/n";
						}
						bufferedReader.close();
						BufferedWriter bufferedWriter = new BufferedWriter(
								new FileWriter(file, false));
						bufferedWriter.write(fileContent);
						bufferedWriter.flush();
						bufferedWriter.close();

						return true;
					}
				}
				fileContent += allLine + "/r/n";
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			bufferedReader.close();
		}
		return false;
	}

	/**
	 * �������
	 */
	public static void main(String[] args) throws Exception {
		// String value = SetOther.getProfileString("d:/1.ini", "Option",
		// "OracleDB", "default");
		// System.out.println(value);
		// try {
		System.out.println(SetOther.setProfileString("d:/1.ini", "Settings",
				"SampSize", "55555"));
		System.out.println(SetOther.getProfileString("d:/1.ini", "Settings",
				"SampSize", "666"));
		// } catch (IOException e) {
		// System.out.println(e.toString());
		// }
		//  
	}
}
