<%@page contentType="text/html; charset=GBK"%>
<jsp:useBean id="htmlShow" scope="session" class="com.siteview.ecc.report.statisticalreport.ShowHtml"/>
<jsp:setProperty name="htmlShow" property="*" />  
<header>
<base href="<%=(request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/ecc/main/report/statreport/")%>">
</header>
<%=htmlShow.getContent()%>
