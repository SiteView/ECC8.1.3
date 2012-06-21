<%
response.setContentType("image/gif;charset=GB2312");
String id=request.getParameter("id");
new com.siteview.ecc.simplereport.CreateImage().create(session,response,id);
session.removeAttribute(id);

%>