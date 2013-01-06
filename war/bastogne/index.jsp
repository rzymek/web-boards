<!doctype html>
<%@page import="org.apache.commons.io.IOUtils"%>
<%@page import="java.io.InputStream"%>
<%@page import="org.apache.commons.io.FileUtils"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Bastogne</title>
<script language="javascript"
	src="../earl.ClientEngine/earl.ClientEngine.nocache.js"></script>
</head>
<body id="body">
<%
IOUtils.copy(config.getServletContext().getResourceAsStream("/bastogne/bastogne.svg"),out);
%>
<!--
<div id="menu1" style="position:fixed; right:0;top:0;width:100px;height:100px;text-align:right; border:solid 1px red"></div>
-->
<div id="menu" style="position:fixed; right:0;top:0;width:45px;text-align:right; border:solid 1px red">
	<img src="../menu.svg" alt="menu"  width="45" height="45" onclick="alert('ok')">
	<img src="../menu.svg" alt="menu"  width="45" height="45" onclick="alert('ok')">
	<img src="../menu.svg" alt="menu"  width="45" height="45" onclick="alert('ok')">
	<img src="../menu.svg" alt="menu"  width="45" height="45" onclick="alert('ok')">
	<img src="../menu.svg" alt="menu"  width="45" height="45" onclick="alert('ok')">
</div>
<div id="viewport.x" style="position:fixed; right:0;top:0;width:1px; height:1px;"></div>
<div id="viewport.width" style="position:fixed; right:0;top:0;width:100%; height:1px;"></div>
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