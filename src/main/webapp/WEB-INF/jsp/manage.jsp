<%@page import="java.util.Collection"%>
<%@page import="webboards.server.servlet.ManagerServlet"%>
<%@page import="webboards.server.entity.Table"%>
<html>
<head>
<title>Earl</title>
</head>
<body>
	<form type="get">
		Start new game as: <select name="side">
			<option>US</option>
			<option>GE</option>
		</select><input type="submit" value="Start" />
	</form>
	<% Collection<Table> list; %>
	<br /> Your games:
	<ul>
		<%
			list = (Collection<Table>) request.getAttribute("webboards.started");
			for (Table t : list) {
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
			list = (Collection<Table>) request.getAttribute("webboards.invitations");
			for (Table t : list) {
		%>
		<li><a href="/bastogne/?table=<%=t.id%>"><%=t%></a></li>
		<%
			}
		%>
	</ul>
</body>
</html>
