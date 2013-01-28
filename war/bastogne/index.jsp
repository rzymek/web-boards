<!doctype html>
<%@page import="org.apache.commons.io.IOUtils"%>
<%@page import="java.io.InputStream"%>
<%@page import="org.apache.commons.io.FileUtils"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Bastogne</title>
<script language="javascript"
	src="../earl.ClientEngine/earl.ClientEngine.nocache.js"></script>
<style>
#menu { 
	position:fixed; 
	right:0px; 
	top:0; 
	width:45px;
	text-align:right; 
}
#menu button { 
	width: 45px; 
	height: 45px;
	text-align: center;
	padding:0px; 
}
#log {
	display: none;
	text-align: left; 
	width: 250px;
	font-size: 10px;
	background-color: white;
	border: solid 1px black;
	position: fixed; 
	right:45px; 
	top:0px;
	white-space: pre;
}
</style>
</head>
<body id="body">
<%
IOUtils.copy(config.getServletContext().getResourceAsStream("/bastogne/bastogne.svg"),out);
%>
<div id="menu"></div>	
<div id="log"></div>
<div id="viewport.x" style="position:fixed; right:0;top:0;width:1px; height:1px;"></div>
<div id="viewport.width" style="position:fixed; right:0;top:0;width:100%; height:1px;"></div>
</body>
</html>