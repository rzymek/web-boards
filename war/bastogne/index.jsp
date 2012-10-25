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
<%
InputStream in = config.getServletContext().getResourceAsStream("/bastogne/bastogne5.2.svg");
IOUtils.copy(in,out);
%>
<pre id="log">Loading...
</pre>
</body>
</html>
