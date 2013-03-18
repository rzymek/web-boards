<%@page import="java.util.Collection"%>
<%@page import="webboards.server.servlet.ManagerServlet"%>
<%@page import="webboards.server.entity.*"%>
<%@page import="com.google.appengine.api.users.UserServiceFactory"%>
<html>
<head>
<title>WebBoards</title>
</head>
<body>
	Hello <%= request.getUserPrincipal().getName() %><br/>
	<form type="get">
		Start new game as: <select name="side">
			<option>US</option>
			<option>GE</option>
		</select><input type="submit" value="Start" />
	</form>
	<% Collection<Table> list;Collection<TableSearch> list1; %>
	<br /> Your games:
	<ul>
		<%
			list1 = (Collection<TableSearch>) request.getAttribute("webboards.started");
			for (TableSearch t : list1) {
		%>
		<li><a href="/bastogne/?table=<%=t.id%>"><%=t%></a></li>
		<%
			}
		%>
	</ul>
	<br /> Waiting for opponent:
	<ul>
		<%
			list = (Collection<Table>) request.getAttribute("webboards.waiting");
			for (Table t : list) {
		%>
		<li><a href="/bastogne/?table=<%=t.id%>"><%=t%></a></li>
		<%
			}
		%>
	</ul>
	<br /> Join:
	<ul>
		<%
			list1 = (Collection<TableSearch>) request.getAttribute("webboards.invitations");
			for (TableSearch t : list1) {
		%>
		<li><a href="/bastogne/?table=<%=t.id%>"><%=t%></a></li>
		<%
			}
		%>
	</ul>
	<a href="<% UserServiceFactory.getUserService().createLogoutURL(request.getRequestURI()); %>">Logout</a>
</body>
</html>
