<html>
<head>
<title>Earl</title>
</head>
<%
String table = request.getParameter("table");
String token = earl.engine.server.EngineServiceImpl.joinChannel(table);
%>

<frameset rows="0,*" frameborder="0" border="0" framespacing="0">
	<frame name="engine" src="engine.html?<%=request.getQueryString()%>&token=<%=token%>"
		marginheight="0" marginwidth="0" scrolling="no" noresize>
	<frame name="view" src="../bastogne/bastogne-opt.svg" marginheight="0"
		marginwidth="0" scrolling="auto">
	<noframes>Frame support required.
	</noframes>
</frameset>
</html>
