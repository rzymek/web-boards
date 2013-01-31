<!doctype html>
<%@page import="org.apache.commons.io.IOUtils"%>
<%@page import="java.io.InputStream"%>
<%@page import="org.apache.commons.io.FileUtils"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Bastogne 5.2</title>
<script language="javascript"
	src="../earl.ClientEngine/earl.ClientEngine.nocache.js"></script>
</head>
<body id="body">
<%
InputStream in = config.getServletContext().getResourceAsStream("/bastogne/bastogne5.2.svg");
IOUtils.copy(in,out);
%>
<img id="menu" src="../menu.svg" style="position:fixed; right:0;top:0;width:50px">
<input id="chat" type="text" size="200"></input><br>
<table width="100%">
	<tr>
		<td id="controls" width="30%" valign="top">
			<button type="button" id="roll2d6" style="height: 50px; margin-bottom: 15px">Roll 2d6</button>
			<button type="button" id="rolld6" style="height: 50px; margin-bottom: 15px">Roll d6</button>
			<button type="button" id="toggle" style="height: 50px; margin-bottom: 15px">Toggle units</button>
			<button type="button" id="deselect" style="height: 50px; margin-bottom: 15px">Deselect</button>
			<button type="button" id="mark" style="height: 50px; margin-bottom: 15px">Mark</button><br>
			<button type="button" id="flip" style="height: 50px; margin-bottom: 15px">Flip</button><br>
			<button type="button" id="debug">Debug</button>
		</td>
		<td id="log" style="white-space: pre;">Loading...</td>
	</tr>
</table>
</body>
</html>
