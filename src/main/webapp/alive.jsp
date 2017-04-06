<%@ page import="java.util.*,java.sql.*,javax.naming.*,javax.sql.*,org.apache.tomcat.dbcp.dbcp.BasicDataSource" %>

<%@ page session="false" %>

<% 
/**
Alive JSP
Requires Java 1.5 or later.
Written for Tomcat
This page will iterate through the below queries from the given JNDI data sources, 
and report if they succeed. Combined with Alert site, this will allow monitorting of app availability
and datapool availability.

Each query should be written to return a singe column aliased as 'result' and a sysdate (to avoid caching). 

Place this JSP in the Webroot of your applications WAR

**/
Map<String,String> connectionList = new TreeMap<String,String>();

/** START MODIFICAIONS HERE **/ 
connectionList.put("gde/auth","select 1 as result, sysdate from dual");
/** END MODIFICAIONS HERE **/ 
%>

<html>
<head><title>Alive</title></head>
<body>

<% for (String key: connectionList.keySet()) {
Context initCtx = null;
Context env = null;
DataSource pool = null;
Connection conn = null;
Statement stmt = null;
ResultSet rs = null;   	
%>
<h2><%=key%></h2>
<%
         
try{
	initCtx = new InitialContext();
	env = (Context)initCtx.lookup("java:comp/env");
	pool = (DataSource) env.lookup (key); 	
	conn = pool.getConnection( );
	stmt = conn.createStatement( );
	rs = stmt.executeQuery(connectionList.get(key));
	
	
	
   while( rs.next( )){ %>          
		<span style="color: green;">'<%=key%>' Alive: <%= rs.getString("result") %> @ <%= rs.getString("sysdate") %></span>
        <br/>
        <span style="color: green;"><%= "-> Active Connections: N/A"  %></span>
        <br/>
        <span style="color: green;"><%= "->Idle Connections: N/A"  %></span>        
        <br/>
        <span style="color: green;"><%= "->Max Active Connections: N/A"  %></span>
        <br/>
        <span style="color: green;"><%= "->Max Idle Connections: N/A"  %></span>
        <br/>        		           
<%} //while           
} catch (Exception e) {   %>
		<span style="color: red;">'<%=key%>' Dead</span> 
<% 
	} finally {           
    	try{                
	        stmt.close( );
	        conn.close( );                
    	} catch (Exception sqle){ } }
}
%>

</body>
</html>>