<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.util.*,java.text.*"%>
<html>
	<head>
		<title>月历</title>
		<link rel="stylesheet" type="text/css"
			href="/ecc/main/css/fisheye-menu.css" />
		<script src="/ecc/main/script/fisheye.js" type='text/javascript'></script>
	</head>
	<body style="font-size: 12px">
		<ul id="fisheye_menu">
			<%!String[] months = { "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月" };
	public final static int dom[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };%>
			<%
				/*处理事件*/
				boolean yyok = false;
				int yy = 0, mm = 0;
				String yyString = request.getParameter("year");
				Calendar cal = Calendar.getInstance();
				GregorianCalendar calendar = new GregorianCalendar(yy, mm, 1);
				if (yyString != null && yyString.length() > 0) {
					try {
						yy = Integer.parseInt(yyString);
						yyok = true;
					} catch (NumberFormatException e) {
						out.println("年份不可用");
					}

					if (!yyok)
						yy = cal.get(Calendar.YEAR);
					String mmString = request.getParameter("month");
					if (mmString == null) {
						mm = cal.get(Calendar.MONTH);
					} else {
						for (int i = 0; i < months.length; i++)
							if (months[i].equals(mmString)) {
								mm = i;
								break;
							}
					}
				}

				int lead = 0;
			%>
			<table border="0" cellpadding="1" cellspacing="1" height="100px"
				width="100%" style="font-size: 12px">
				<tr align="center">
					<td>
						<font color="#FF0000">星期日</font>
					</td>
					<td>
						星期一
					</td>
					<td>
						星期二
					</td>
					<td>
						星期三
					</td>
					<td>
						星期四
					</td>
					<td>
						星期五
					</td>
					<td>
						<font color="#FF0000">星期六</font>
					</td>
				</tr>
				<%
					/*下面是显示月历的代码*/
					lead = calendar.get(Calendar.DAY_OF_WEEK) - 1;
					int dayInMonth = dom[mm];
					if (calendar.isLeapYear(calendar.get(Calendar.YEAR)) && mm == 1)
						++dayInMonth;
					out.print("<tr>");
					for (int i = 0; i < lead; i++) {
						out.print("<td>&nbsp;</td>");
					}
					for (int i = 1; i <= dayInMonth; i++) {
						if ((i + lead) % 7 == 0 || (i + lead) % 7 == 1)
							out.print("<td align=\"center\"><span><font color=\"#FF0000\">" + i + "日</font></span><div><img id='im" + i
									+ "' src=\"/ecc/main/images/fisheye/icon" + i + ".png\" alt=\"image description\" /></div></td>");
						else
							out.print("<td align=\"center\"> <span><font>" + i
									+ "日</font></span><div><img   src=\"/ecc/main/images/fisheye/icon" + i
									+ ".png\" alt=\"image description\" /></div></td>");
						System.out.println("fisheye/icon" + i + ".png\"");
						if ((lead + i) % 7 == 0) {
							out.print("</tr></tr>");
						}
					}
					out.print("</tr>");
				%>
			</table>
		</ul>

	</body>
</html>
