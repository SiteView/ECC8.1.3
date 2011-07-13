package com.siteview.ecc.reportserver;

public class DateUtil {
	/**
	 * ��ʽ������
	 * 
	 * @param dateStr
	 *            �ַ�������
	 * @param format
	 *            ��ʽ
	 * @return ��������
	 */
	public static java.util.Date parseDate(String dateStr, String format) {
		java.util.Date date = null;
		// try {
		// java.text.DateFormat df = new java.text.SimpleDateFormat(format);
		// String dt=Normal.parse(dateStr).replaceAll(
		// "-", "/");
		// if((!dt.equals(""))&&(dt.length()dt+=format.substring(dt.length()).replaceAll("[YyMmDdHhSs]","0");
		// }
		// date = (java.util.Date) df.parse(dt);
		// } catch (Exception e) {
		// }
		return date;
	}

	public static java.util.Date parseDate(String dateStr) {
		return parseDate(dateStr, "yyyy/MM/dd");
	}

	public static java.util.Date parseDate(java.sql.Date date) {
		return date;
	}

	public static java.sql.Date parseSqlDate(java.util.Date date) {
		if (date != null)
			return new java.sql.Date(date.getTime());
		else
			return null;
	}

	public static java.sql.Date parseSqlDate(String dateStr, String format) {
		java.util.Date date = parseDate(dateStr, format);
		return parseSqlDate(date);
	}

	public static java.sql.Date parseSqlDate(String dateStr) {
		return parseSqlDate(dateStr, "yyyy/MM/dd");
	}

	public static java.sql.Timestamp parseTimestamp(String dateStr,
			String format) {
		java.util.Date date = parseDate(dateStr, format);
		if (date != null) {
			long t = date.getTime();
			return new java.sql.Timestamp(t);
		} else
			return null;
	}

	public static java.sql.Timestamp parseTimestamp(String dateStr) {
		return parseTimestamp(dateStr, "yyyy/MM/dd HH:mm:ss");
	}

	/**
	 * ��ʽ���������
	 * 
	 * @param date
	 *            ����
	 * @param format
	 *            ��ʽ
	 * @return �����ַ�������
	 */
	public static String format(java.util.Date date, String format) {
		String result = "";
		try {
			if (date != null) {
				java.text.DateFormat df = new java.text.SimpleDateFormat(format);
				result = df.format(date);
			}
		} catch (Exception e) {
		}
		return result;
	}

	public static String format(java.util.Date date) {
		return format(date, "yyyy/MM/dd");
	}

	/**
	 * �������
	 * 
	 * @param date
	 *            ����
	 * @return �������
	 */
	public static int getYear(java.util.Date date) {
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(date);
		return c.get(java.util.Calendar.YEAR);
	}

	/**
	 * �����·�
	 * 
	 * @param date
	 *            ����
	 * @return �����·�
	 */
	public static int getMonth(java.util.Date date) {
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(date);
		return c.get(java.util.Calendar.MONTH) + 1;
	}

	/**
	 * �����շ�
	 * 
	 * @param date
	 *            ����
	 * @return �����շ�
	 */
	public static int getDay(java.util.Date date) {
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(date);
		return c.get(java.util.Calendar.DAY_OF_MONTH);
	}

	/**
	 * ����Сʱ
	 * 
	 * @param date
	 *            ����
	 * @return ����Сʱ
	 */
	public static int getHour(java.util.Date date) {
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(date);
		return c.get(java.util.Calendar.HOUR_OF_DAY);
	}

	/**
	 * ���ط���
	 * 
	 * @param date
	 *            ����
	 * @return ���ط���
	 */
	public static int getMinute(java.util.Date date) {
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(date);
		return c.get(java.util.Calendar.MINUTE);
	}

	/**
	 * ��������
	 * 
	 * @param date
	 *            ����
	 * @return ��������
	 */
	public static int getSecond(java.util.Date date) {
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(date);
		return c.get(java.util.Calendar.SECOND);
	}

	/**
	 * ���غ���
	 * 
	 * @param date
	 *            ����
	 * @return ���غ���
	 */
	public static long getMillis(java.util.Date date) {
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(date);
		return c.getTimeInMillis();
	}

	/**
	 * �����ַ�������
	 * 
	 * @param date
	 *            ����
	 * @return �����ַ�������
	 */
	public static String getDate(java.util.Date date) {
		return format(date, "yyyy/MM/dd");
	}

	/**
	 * �����ַ���ʱ��
	 * 
	 * @param date
	 *            ����
	 * @return �����ַ���ʱ��
	 */
	public static String getTime(java.util.Date date) {
		return format(date, "HH:mm:ss");
	}

	/**
	 * �����ַ�������ʱ��
	 * 
	 * @param date
	 *            ����
	 * @return �����ַ�������ʱ��
	 */
	public static String getDateTime(java.util.Date date) {
		return format(date, "yyyy/MM/dd HH:mm:ss");
	}

	/**
	 * �������
	 * 
	 * @param date
	 *            ����
	 * @param day
	 *            ����
	 * @return ������Ӻ������
	 */
	public static java.util.Date addDate(java.util.Date date, int day) {
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTimeInMillis(getMillis(date) + ((long) day) * 24 * 3600 * 1000);
		return c.getTime();
	}

	/**
	 * �������
	 * 
	 * @param date
	 *            ����
	 * @param date1
	 *            ����
	 * @return ��������������
	 */
	public static int diffDate(java.util.Date date, java.util.Date date1) {
		return (int) ((getMillis(date) - getMillis(date1)) / (24 * 3600 * 1000));
	}
}
