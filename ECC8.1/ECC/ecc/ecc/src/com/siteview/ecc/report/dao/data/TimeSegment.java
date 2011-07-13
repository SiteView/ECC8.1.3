/**
 * ecc:����11:17:30
 */
package com.siteview.ecc.report.dao.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * ��������ʱ��β���,��Ϊ��ѯ�������б��ʱ��β���
 * 
 * @author : di.tang
 * @date: 2009-3-19
 * @company: siteview
 */
public class TimeSegment {
	private final static Logger logger = Logger.getLogger(TimeSegment.class);
	public final static String DATE_FORMAT = "yyyy-MM-dd";
	private String[] temp = null;
	private String formatdate = null;

	public TimeSegment(String formatdate) {
		if (formatdate != null){
			this.formatdate = formatdate;
			temp = formatdate.split("-");
		}
	}

	public String getYear( ) {
		if (temp.length > 1) {
			return temp[0];
		}
		return null;
	}
	public String getMonth( ) {
		if (temp.length > 1) {
			return temp[1];
		}
		return null;
	}
	public String getDay( ) {
		if (temp.length > 2) {
			return temp[2];
		}
		return null;
	}
	public String getHour( ) {
		if (temp.length > 3) {
			return temp[3];
		}
		return null;
	}
	/**
	 * ����ת�����ַ����������ת��������ΪNULL���򷵻�ΪNULL��Ĭ�ϸ�ʽΪ���ڣ�
	 * 
	 * @param date
	 *            Date ��ת��������
	 * @param pattern
	 *            String ת�����ַ��͵����ڸ�ʽ * Date and Time Pattern Result
	 *            *"yyyy.MM.dd G 'at' HH:mm:ss z" 2001.07.04 AD at 12:08:56 PDT
	 *            "EEE, MMM d, ''yy" Wed, Jul 4, '01 "h:mm a" 12:08 PM
	 *            "hh 'o''clock' a, zzzz" 12 o'clock PM, Pacific Daylight Time
	 *            "K:mm a, z" 0:08 PM, PDT "yyyyy.MMMMM.dd GGG hh:mm aaa"
	 *            02001.July.04 AD 12:08 PM "EEE, d MMM yyyy HH:mm:ss Z" Wed, 4
	 *            Jul 2001 12:08:56 -0700 "yyMMddHHmmssZ" 010704120856-0700
	 * @return String �����ַ���
	 */
	public static String dateToString(Date date, String pattern) {
		if (date == null) {
			return null;
		}
		String format = pattern;
		if ((format == null) || (format != null && format.equals(""))) {
			format = TimeSegment.DATE_FORMAT;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	public void test() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
		try {
			Date ss = sdf.parse("2004-03-23 18:18:23");

			logger.info(new Date().toLocaleString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

/*	public static void main(String[] args) {
		TimeSegment tTimeSegment = new TimeSegment();
		tTimeSegment.getYear(new Date());
		logger.info(tTimeSegment.getMonth(new Date()));
		logger.info(tTimeSegment.dateToString(new Date(), "yyyy-MM-dd"));
		logger.info(new Date());
		String[] a = "2009-03-02 18:12:25".split("-");
		logger.info(a[0]);
		logger.info(a[1]);
		logger.info(a[2]);

		tTimeSegment.test();
	}*/
}
