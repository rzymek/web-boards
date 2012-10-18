<%@page import="earl.manager.ManagerServlet"%>
<%
	response.sendRedirect("/earl?"+ManagerServlet.copyParams(request));
%>