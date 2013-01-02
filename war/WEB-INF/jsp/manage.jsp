<%@page import="java.util.Collection"%>
<%@page import="earl.manager.ManagerServlet"%>
<%@page import="earl.manager.Table"%>
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

	<br /> Your games:
	<ul>
		<%
			Collection<Table> started = (Collection<Table>) request.getAttribute("earl.started");
			for (Table t : started) {
		%>
		<li><a href="/bastogne/?table=<%=t.id%>"><%=t%></a></li>
		<%
			}
		%>
	</ul>
	<br /> Join:
	<ul>
		<%
			Collection<Table> invitations = (Collection<Table>) request.getAttribute("earl.invitations");
			for (Table t : invitations) {
		%>
		<li><a href="/bastogne/?table=<%=t.id%>"><%=t%></a></li>
		<%
			}
		%>
	</ul>
</body>
</html>
