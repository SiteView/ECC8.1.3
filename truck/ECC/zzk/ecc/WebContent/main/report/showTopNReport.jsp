<%@page contentType="text/html; charset=utf-8"%>
<%
request.setCharacterEncoding("UTF-8");
%>

<jsp:useBean id="topHtmlShow" scope="session" class="com.siteview.ecc.report.topnreport.HtmlTopNReportShow"/>
<jsp:setProperty name="topHtmlShow" property="*" />  
<header>
<base href="<%=(request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/ecc/main/report/topnreport/")%>">
</header>
<%=topHtmlShow.getTopNContent()%>
