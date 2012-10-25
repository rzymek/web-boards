<!doctype html>
<%@page import="org.apache.commons.io.IOUtils"%>
<%@page import="java.io.InputStream"%>
<%@page import="org.apache.commons.io.FileUtils"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Earl</title>
<script language="javascript"
	src="../earl.engine.Earl/earl.engine.Earl.nocache.js"></script>
</head>
<body>
<embed id="view" src="../bastogne/bastogne-opt.svg"></embed>
<%/*
String table = request.getParameter("table");
String token = earl.engine.server.EngineServiceImpl.joinChannel(table);
*/%>
<pre id="log">Loading...
</pre>
</body>
</html>
