<!doctype html>
<%@page import="org.apache.commons.io.IOUtils"%>
<%@page import="java.io.InputStream"%>
<%@page import="org.apache.commons.io.FileUtils"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Earl</title>
<script language="javascript"
	src="../earl.ClientEngine/earl.ClientEngine.nocache.js"></script>
</head>
<body>
<%
InputStream in = config.getServletContext().getResourceAsStream("/bastogne/bastogne5.2.svg");
IOUtils.copy(in,out);
%>
<table width="100%">
	<tr>
		<td id="controls" width="30%" valign="top">
			<button type="button" id="roll2d6">Roll 2d6</button>
		</td>
		<td id="log" style="white-space: pre;">Loading...</td>
	</tr>
</table>
</body>
</html>
