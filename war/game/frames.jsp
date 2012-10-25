<html>
<head>
<title>Earl</title>
</head>
<%
	String table = request.getParameter("table");
	String token = earl.engine.server.EngineServiceImpl.joinChannel(table);
%>
<frameset rows="0,*">
	<frame name="engine"
		src="engine.html?<%=request.getQueryString()%>&token=<%=token%>">
	<frame name="view" src="../bastogne/bastogne-opt.svg">
</frameset>
</html>
