<!doctype html>
<%@page import="org.apache.commons.io.IOUtils"%>
<%@page import="java.io.InputStream"%>
<%@page import="org.apache.commons.io.FileUtils"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Earl</title>
<script language="javascript" src="../js/utils.js"></script>
<script language="javascript">
var loc = parseURL(window.location.href);
if(loc.params['gwt.codesvr'] == null) {
	var ref = parseURL(document.referrer);
	var refgwt = decodeURIComponent(ref.params['gwt.codesvr']);
	if(refgwt && refgwt != "undefined") {
		var c = "?"
		var s = "";
		for(key in loc.params) {
			if(key != "gwt.codesvr") {
				s += c+key+"="+loc.params[key];
				c = "&";			
			}
		}
		s += c+"gwt.codesvr="+refgwt;
		console.log(s);
		window.location.search = s; 
	}
}
</script>
<script language="javascript"
	src="../earl.ClientEngine/earl.ClientEngine.nocache.js"></script>
</head>
<body>
<%
InputStream in = config.getServletContext().getResourceAsStream("/bastogne/bastogne5.2.svg");
IOUtils.copy(in,out);
%>
<input id="chat" type="text" size="200"></input><br>
<table width="100%">
	<tr>
		<td id="controls" width="30%" valign="top">
			<button type="button" id="roll2d6" style="height: 50px; margin-bottom: 15px">Roll 2d6</button>
			<button type="button" id="rolld6" style="height: 50px; margin-bottom: 15px">Roll d6</button>
			<button type="button" id="toggle" style="height: 50px; margin-bottom: 15px">Toggle units</button><br>
			<button type="button" id="debug">Debug</button>
		</td>
		<td id="log" style="white-space: pre;">Loading...</td>
	</tr>
</table>
</body>
</html>
