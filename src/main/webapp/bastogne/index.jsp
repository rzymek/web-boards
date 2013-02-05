<!doctype html>
<%@page import="org.apache.commons.io.IOUtils"%>
<%@page import="java.io.InputStream"%>
<%@page import="org.apache.commons.io.FileUtils"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Bastogne</title>
<script src="../earl.ClientEngine/earl.ClientEngine.nocache.js"></script>
<script src="/_ah/channel/jsapi"></script>
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
.c {
	pointer-events: none;
}
.s { 
	pointer-events: auto;
}
</style>
</head>
<body id="body">
<% IOUtils.copy(config.getServletContext().getResourceAsStream("/bastogne/bastogne.svg"),out); %>
<div id="menu"></div>	
</body>
</html>