<%@page import="java.util.Collection"%>
<%@page import="webboards.server.servlet.ManagerServlet"%>
<%@page import="webboards.server.entity.*"%>
<%@page import="com.google.appengine.api.users.UserServiceFactory"%>
	
	
<%!
public String print_content_header(String txt){

	String res; 
	
	res =  "<div class=\"art-box art-post\">";
    res += "<div class=\"art-box-body art-post-body\">";
	res += "<div class=\"art-post-inner art-article\"> ";
	
	res+= "<h2 class=\"art-postheader\">" + txt + "</h2>";
	res += "<div class=\"art-postcontent\">";
	res += "<div class=\"art-content-layout\"> ";
    res += "<div class=\"art-content-layout-row\"> ";
	res += "<div class=\"art-layout-cell layout-item-0\" style=\"width: 100%;\"> ";  
    return res;   
}

public String print_content_footer(){

	return " </div>	</div> </div> </div> </div>	</div> </div>";

}
%>


<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

	<style type="text/css">
	<%@ include file="/css/style.css" %>
	</style>    
    
   	<style type="text/css">
	.art-post .layout-item-0 { padding-right: 10px;padding-left: 10px; }
   	.ie7 .art-post .art-layout-cell {border:none !important; padding:0 !important; }
   	.ie6 .art-post .art-layout-cell {border:none !important; padding:0 !important; }
   	</style>" ; 
   
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

<%
	Collection<TableSearch> list_w, list_i, list_s;
	
	list_w = (Collection<TableSearch>) request.getAttribute("webboards.waiting");
	list_i = (Collection<TableSearch>) request.getAttribute("webboards.invitations");
	list_s = (Collection<TableSearch>) request.getAttribute("webboards.started");
%>

<% if ( request.getParameter("menu") == null ) { %> 
	
	<%= print_content_header("Home") %>
 
    <b>Your games:</b>
	<ul>			
		<li>Started          <%= list_s.size() %></li>
		<li>Waiting for opp. <%= list_w.size() %></li>
		<li>Join to game     <%= list_i.size() %></li>
	</ul>
	<%= print_content_footer() %>
    
	<% } %>
	
	
	
	
	<% 
	if ( "new".equals( request.getParameter("menu") ) || request.getParameter("menu") == null ) { %> 
	
	<%= print_content_header("New Game") %>
	<form type="get">
		Start new game as: <select name="side">
			<option>US</option>
			<option>GE</option>
		</select><input type="submit" value="Start" />
	</form> 
	<%= print_content_footer() %>
	
	<% 	} %>
	
	
	
	<% Collection<TableSearch> list; %>
	
	
	<% 
	if ( "your".equals( request.getParameter("menu") ) || request.getParameter("menu") == null ) { %> 
	
	<%= print_content_header("Your Games") %>  
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
	
	<% if (list_i.size() == 0) { %> Sory, you haven't games here. <% } %>
	
	<%= print_content_footer() %>
	<% 	} %>
			
	
	
	
	<% 
	if ( "waiting".equals( request.getParameter("menu") ) || request.getParameter("menu") == null ) { %> 
	
	<%= print_content_header("Waiting for opponent:") %>
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
	
	<% if (list_w.size() == 0) { %> Sory, you haven't games here. <% } %>
	
	<%= print_content_footer() %>
	<% 	} %>
	
	
	<% 
	if ( "join".equals( request.getParameter("menu") ) || request.getParameter("menu") == null ) { %> 
	
	<%= print_content_header("Join") %>   
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
	
	<% if (list_i.size() == 0) { %> Sory, you haven't games here. <% } %>
	
	<%= print_content_footer() %>
	<% 	} %> 
	

</div></div>
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

