<%@page contentType="text/html; charset=GBK"%>
<jsp:useBean id="htmlShow" scope="request" class="com.siteview.ecc.report.statisticalreport.ShowHtml"/>
<jsp:setProperty name="htmlShow" property="*" />  
<header>
<%
  String id = request.getParameter("reportGenID");
  String createTimeInMillis = request.getParameter("createTimeInMillis");
  String name = createTimeInMillis + "_" + id;
  request.setAttribute("foldername",name);
  %>
<base href="<%=(request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/ecc/main/report/statreport/"+request.getAttribute("foldername")+"/")%>">
</header>
<%=htmlShow.getContent()%>