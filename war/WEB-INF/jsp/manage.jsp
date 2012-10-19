<%@page import="earl.manager.ManagerServlet"%>
<%@page import="earl.engine.client.Table"%>
<%@page import="java.util.List"%>
<html>
<head>
<title>Earl</title>
</head>
<body>
	<a href="/earl/start?<%=ManagerServlet.copyParams(request)%>" />Start new
	game
	</a>
	<br /> Your games:
	<ul>
		<%
			List<Table> started = (List<Table>) request.getAttribute("earl.started");
			for (Table t : started) {
		%>
		<li><a
			href="/game/?table=<%=t.id + '&' + ManagerServlet.copyParams(request)%>"><%=t.opponent%>
				- <%=t.started%></a></li>
		<%
			}
		%>
	</ul>
	<br /> Join:
	<ul>
		<%
			List<Table> invitations = (List<Table>) request.getAttribute("earl.invitations");
			for (Table t : invitations) {
		%>
		<li><a
			href="/earl/join?table=<%=t.id + '&' + ManagerServlet.copyParams(request)%>"><%=t.opponent%>
				- <%=t.started%></a></li>
		<%
			}
		%>
	</ul>
</body>
</html>
