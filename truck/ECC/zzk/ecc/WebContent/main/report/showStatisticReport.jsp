<%@page contentType="text/html; charset=GBK"%>
<jsp:useBean id="htmlShow" scope="request" class="com.siteview.ecc.report.statisticalreport.ShowHtml"/>
<jsp:setProperty name="htmlShow" property="*" />  
<header>
<%String id = request.getParameter("reportGenID"); 
  String name = com.siteview.ecc.util.Toolkit.getToolkit().formatDate(new java.util.Date(Long.parseLong(id)), "yyyyMMdd_")+ id;
  request.setAttribute("foldername",name);
  %>
<base href="<%=(request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/ecc/main/report/statreport/"+request.getAttribute("foldername")+"/")%>">
</header>
<%=htmlShow.getContent()%>