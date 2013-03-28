<%@page import="java.util.Collection"%>
<%@page import="webboards.server.servlet.ManagerServlet"%>
<%@page import="webboards.server.entity.*"%>
<%@page import="com.google.appengine.api.users.UserServiceFactory"%>

	
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

	<style type="text/css">
	<%@ include file="/css/style.css" %>
	</style>
 	<!--[if IE 6]><link rel="stylesheet" href="/css/style.ie6.css" type="text/css" media="screen" /><![endif]-->
    <!--[if IE 7]><link rel="stylesheet" href="/css/style.ie7.css" type="text/css" media="screen" /><![endif]-->
	    
 	<script type="text/javascript" src="/css/jquery.js"></script>
    <script type="text/javascript" src="/css/script.js"></script>
    
   	<style type="text/css">
	.art-post .layout-item-0 { padding-right: 10px;padding-left: 10px; }
   	.ie7 .art-post .art-layout-cell {border:none !important; padding:0 !important; }
   	.ie6 .art-post .art-layout-cell {border:none !important; padding:0 !important; }
   	</style>
   
	<title>WebBoards</title>
</head>
<body>

<div id="art-page-background-middle-texture">
<div id="art-main">
    <div class="cleared reset-box"></div>
    <div class="art-header">
        <div class="art-header-position">
            <div class="art-header-wrapper">
                <div class="cleared reset-box"></div>
                <div class="art-header-inner">
                <div class="art-logo">
                                 <h1 class="art-logo-name"><a href="/webboards/start">Webboards</a></h1>
                                 <h2 class="art-logo-text">Hello <%= request.getUserPrincipal().getName() %></h2>
                                                 
                                </div>
                </div>
            </div>
        </div>
        
    </div>
    <div class="cleared reset-box"></div>


<div class="art-bar art-nav">
<div class="art-nav-outer">
<div class="art-nav-wrapper">
<div class="art-nav-inner">
	<ul class="art-hmenu">	
	  <li><a href="/webboards/start">Home</a></li>
	  <li><a href="/webboards/start?menu=new">Start new game</a></li>
	  <li><a href="/webboards/start?menu=your">Your games</a></li>
	  <li><a href="/webboards/start?menu=waiting">Waiting for opponent</a></li>
	  <li><a href="/webboards/start?menu=join">Join to game</a></li>
	</ul>
</div>
</div>
</div>
</div>

<div class="cleared reset-box"></div>
<div class="art-box art-sheet">
        <div class="art-box-body art-sheet-body">
            <div class="art-layout-wrapper">
                <div class="art-content-layout">
                    <div class="art-content-layout-row">
                        <div class="art-layout-cell art-content">
<div class="art-box art-post">
    <div class="art-box-body art-post-body">
<div class="art-post-inner art-article">
	
	
	<% 
	if ( "new".equals( request.getParameter("menu") ) ) { %> 
	
	<h2 class="art-postheader">New Game</h2>
    
    <div class="art-postcontent">
	<div class="art-content-layout">
    <div class="art-content-layout-row">
    <div class="art-layout-cell layout-item-0" style="width: 100%;">                           
	
	
	<form type="get">
		Start new game as: <select name="side">
			<option>US</option>
			<option>GE</option>
		</select><input type="submit" value="Start" />
	</form> 
	
	</div>
    </div>
	</div>
	</div>
	
	<% 	} 
	else {
	%>
	
	
	
	<% Collection<TableSearch> list; %>
	
	
	<% 
	if ( "your".equals( request.getParameter("menu") ) ) { %> 
	
	<h2 class="art-postheader">Your games:</h2>
	<div class="art-postcontent">
	<div class="art-content-layout">
    <div class="art-content-layout-row">
    <div class="art-layout-cell layout-item-0" style="width: 100%;">      
    
	<ul>
		<%
			list = (Collection<TableSearch>) request.getAttribute("webboards.started");
			for (TableSearch t : list) {
		%>
		<li><a href="/bastogne/?table=<%=t.id%>"><%=t%></a></li>
		<%
			}
		%>
	</ul>
	
	</div>
    </div>
	</div>
	</div>
	
	<% 	} 
	else {
	%>
			
	
	<% 
	if ( "waiting".equals( request.getParameter("menu") ) ) { %> 
	
	<h2 class="art-postheader">Waiting for opponent:</h2>
	<div class="art-postcontent">
	<div class="art-content-layout">
    <div class="art-content-layout-row">
    <div class="art-layout-cell layout-item-0" style="width: 100%;">      
    
	<ul>
		<%
			list = (Collection<TableSearch>) request.getAttribute("webboards.waiting");
			for (TableSearch t : list) {
		%>
		<li><a href="/bastogne/?table=<%=t.id%>"><%=t%></a></li>
		<%
			}
		%>
	</ul>
	</div>
    </div>
	</div>
	</div>
	
	
	<% 	} 
	else {
	%>
	
	
	<% 
	if ( "join".equals( request.getParameter("menu") ) ) { %> 
	
	<h2 class="art-postheader">Join:</h2>
	<div class="art-postcontent">
	<div class="art-content-layout">
    <div class="art-content-layout-row">
    <div class="art-layout-cell layout-item-0" style="width: 100%;">      
    
	<ul>
	
	
		<%
			list = (Collection<TableSearch>) request.getAttribute("webboards.invitations");
			for (TableSearch t : list) {
		%>
		<li><a href="/bastogne/?table=<%=t.id%>"><%=t%></a></li>
		<%
			}
		%>
	</ul>
	</div>
    </div>
	</div>
	</div>
	
	<% 	} 
	else {
	%> <h2 class="art-postheader">Home: </h2>
	<div class="art-postcontent">
	<div class="art-content-layout">
    <div class="art-content-layout-row">
    <div class="art-layout-cell layout-item-0" style="width: 100%;">      
    
    
    
    <b>Your games:</b>
	<ul>
		<%
		Collection<TableSearch> list_w, list_i, list_s;
		
			list_w = (Collection<TableSearch>) request.getAttribute("webboards.waiting");
			list_i = (Collection<TableSearch>) request.getAttribute("webboards.invitations");
			list_s = (Collection<TableSearch>) request.getAttribute("webboards.started");

		%>
			<li>Started          <%= list_s.size() %></li>
			<li>Waiting for opp. <%= list_w.size() %></li>
			<li>Join to game     <%= list_i.size() %></li>
	</ul>
	    
    
	</div>
    </div>
	</div>
	</div>
    
     <%
	}
	
	}}}
	%>
	
	

</div></div></div></div></div>
			<div class="cleared"></div>
            <div class="art-footer">
                <div class="art-footer-body">
                            <div class="art-footer-text">
                                <a href="<%= UserServiceFactory.getUserService().createLogoutURL("/") %>">Logout</a>
                            </div>
                    <div class="cleared"></div>
                </div>
            </div>
    		<div class="cleared"></div>
</div></div></div></div>



</div>    
</div>	
</body>
</html>
