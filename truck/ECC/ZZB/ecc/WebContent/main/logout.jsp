
<%@page import="com.siteview.base.manage.Manager,com.siteview.ecc.util.Toolkit"%>
<%
Object strSession=session.getAttribute("usersessionid");
if(strSession!=null)
{
	Toolkit.getToolkit().InvalidateEccMainPage(session, (String)strSession);
	Manager.invalidateView(strSession.toString());
}
Toolkit.getToolkit().cleanSession(session);
Toolkit.getToolkit().setCookie("autoLogin","OFF",response,-1);
response.sendRedirect("../index.jsp");
%>
