<%@page import="org.apache.catalina.tribes.membership.MemberImpl"%>
<%@page import="org.apache.catalina.core.StandardEngine"%>
<%@page import="org.apache.catalina.tribes.Member"%>
<%@page import="java.util.Properties"%>
<%@page import="org.apache.catalina.tribes.MembershipService"%>
<%@page import="org.apache.catalina.tribes.group.GroupChannel"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%@page import="org.apache.tomcat.jdbc.pool.DataSource"%>
<%@page import="javax.naming.Context"%>
<%@page import="java.io.StringReader"%>
<%@page import="org.xml.sax.InputSource"%>
<%@page import="java.util.TreeMap"%>
<%@page import="org.json.simple.parser.JSONParser"%>
<%@page import="org.json.simple.JSONArray"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="org.tmatesoft.svn.core.SVNErrorCode"%>
<%@page import="org.tmatesoft.svn.core.SVNException"%>
<%@page import="org.tmatesoft.svn.core.auth.BasicAuthenticationManager"%>
<%@page import="java.util.Scanner"%>
<%@page import="java.io.FileNotFoundException"%>
<%@page import="org.tmatesoft.svn.core.ISVNLogEntryHandler"%>
<%@page import="java.util.regex.Matcher"%>
<%@page import="org.tmatesoft.svn.core.SVNLogEntry"%>
<%@page import="java.util.regex.Pattern"%>
<%@page import="org.tmatesoft.svn.core.wc.SVNWCUtil"%>
<%@page import="org.tmatesoft.svn.core.auth.ISVNAuthenticationManager"%>
<%@page import="org.tmatesoft.svn.core.SVNURL"%>
<%@page import="org.tmatesoft.svn.core.io.SVNRepositoryFactory"%>
<%@page import="org.tmatesoft.svn.core.io.SVNRepository"%>
<%@page import="org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory"%>
<%@page import="org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl"%>
<%@page import="org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory"%>
<%@page import="java.util.HashSet"%>
<%@page import="org.apache.catalina.Session"%>
<%@page import="org.apache.catalina.Manager"%>
<%@page import="java.io.ObjectOutputStream"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="org.apache.tomcat.util.modeler.BaseModelMBean"%>
<%@page import="javax.management.MBeanServerFactory"%>
<%@page import="com.sun.jmx.mbeanserver.JmxMBeanServer"%>
<%@page import="com.sun.jmx.interceptor.DefaultMBeanServerInterceptor"%>
<%@page import="org.apache.catalina.ha.tcp.SimpleTcpCluster"%>
<%@page import="java.lang.reflect.Type"%>

<!-- %@page import="com.google.gson.reflect.TypeToken"%>
<!-- %@page import="com.google.gson.Gson"%-->

<%@page import="java.util.Iterator"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="org.json.simple.JSONAware"%>
<%@page import="java.io.Serializable"%>
<%@page import="org.json.simple.JSONValue"%>
<%@page import="java.lang.management.MemoryUsage"%>
<%@page import="java.lang.management.MemoryMXBean"%>
<%@page import="org.apache.catalina.core.ApplicationContextFacade"%>
<%@page import="java.util.jar.Manifest"%>
<%@page import="java.net.URL"%>
<%@page import="java.lang.reflect.Method"%>
<%@page import="javax.management.Query"%>
<%@page import="java.lang.management.ManagementFactory"%>
<%@page import="javax.management.ObjectName"%>
<%@page import="java.util.Set"%>
<%@page import="javax.management.MBeanServer"%>
<%@page import="java.io.File"%>
<%@page import="org.apache.log4j.FileAppender"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="java.util.Enumeration"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="javax.xml.bind.Unmarshaller"%>
<%@page import="javax.xml.bind.JAXBContext"%>
<%@page import="javax.xml.bind.annotation.XmlElement"%>
<%@page import="javax.xml.bind.annotation.XmlRootElement"%>

<%@ page language="java" pageEncoding="ISO-8859-1"%>

<%@page import="java.net.InetAddress"%>

<%@ page session="false" %>

<%

	if(request.getParameter("username") != null || request.getParameter("password") != null){
		if(authenticated(request.getParameter("username"), request.getParameter("password"))){
			session.setAttribute("username", request.getParameter("username"));
			session.setAttribute("password", request.getParameter("password"));
			session.setAttribute("logged-in", "1");
			session.setAttribute("message", null);
		}
		else{
			session.setAttribute("message", "User credentials are incorrect. Please try again with correct ones.");
		}
		response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/info.jsp" + (("true".equalsIgnoreCase(request.getParameter("json"))) ? "?json=true" : "")));
	} else if(!"1".equals((String)session.getAttribute("logged-in"))){
		String message = "";
		if((String)session.getAttribute("message") == null){
			message = "Please enter your or Jenkin's svn credentials to login.";
		} else {
			message = (String)session.getAttribute("message");
			if("true".equalsIgnoreCase(request.getParameter("json"))){
				out.println("Auth Failed:" + message);
				return;
			}
		}
		session.setAttribute("message", null);
	%>
		<style>
			#wrapper{
				margin:0 auto;
				background-color: #ffffff;
				width:1110px;
			}
		</style>
		<body>
		<div id="wrapper">
			<form method="post" action="info.jsp<% if("true".equalsIgnoreCase(request.getParameter("json"))) out.print("?json=true");%>">
			<div style="margin:50px auto;width:300px;height:300px;background-color:#EEE;border:1px solid #eeb">
				<div style="width:100%;padding:15px 0px;text-align:center;font-size:30px;background-color:#3498DB;color:#fff">Log in</div>
				<div style="width:280px;text-align:center;margin:auto;padding:10px 10px;height:40px;"><%=message %></div>
				<input style="width:200px;height:40px;margin:5px 50px;" type="text" placeholder="Username" name="username" />
				<input style="width:200px;height:40px;margin:5px 50px;" type="password" placeholder="Password"  name="password" />
				<input style="width:200px;height:40px;margin:5px 50px;background-color:#3498DB;color:#fff" type="submit" name="Submit"/>
			</div>
			</form>
		</div>
		</body>
	<%
	}  else{
		SimpleTcpCluster simpleTcpCluster = loadCluster();
		List<Datum<List<List<Datum<String>>>>> info = load(request, response, application, simpleTcpCluster);
		
		if("true".equalsIgnoreCase(request.getParameter("json"))){
			out.println(infoToJson(info));
			return;
		}
		%>
		<style type="text/css">
			html, body {
			    margin: 0;
			    padding: 0;
			    background-color: rgb(204, 204, 204);
			    height: 100%;
			    line-height: 18px;
			    font-family: Arial,Sans-Serif;
			    font-size: 14px;
			    background-color: rgb(255, 255, 255);
			}
			html {
			    font-family: Arial,Sans-Serif;
			    font-size: 14px;
			}
			body {
			    height: 100%;	
			    margin: 0 0 auto;
			    position: relative;
			}
			html body img, html body a img {
			    border: 0 none;
			}
			body.scrolling-enabled {
			    overflow-y: scroll;
			}
			body.scrolling-disabled {
			    overflow-y: hidden;
			}
			
			#wrapper{
				margin:0 auto;
				background-color: #ffffff;
				width:1110px;
			}
			
			input { 
				background: #fff;
				border:1px #CCC solid;
				padding:5px;
			 }
			 
			
			.table, .row, .col1, .col2, .col3 {fon-size:16px;}
			.table {display:table}
			
			.title-row{display:table-row;padding-left:10px;}
			.title-col1, .title-col2, .title-col3 {display:table-cell; height:75px;vertical-align:top;}
			.title-col1{color:#555;line-height:35px; font-size:27px;font-weight:bold;padding-left:10px;text-align:center;vertical-align:middle;width:250px;}
			.title-col2{width:8px;background-color:#fff}
			.title-col3{width:850px;padding:10px;background: #fff;border:1px solid #666;line-height:25px;}
			
			.seperator-row {display:table-row}
			.seperator-col1, .seperator-col2, .seperator-col3 {display:table-cell; height:10px;vertical-align:middle;}
			.seperator-col1{width:250px;padding-left:25px;background-color:#fff}
			.seperator-col2{width:8px;background-color:#fff}
			.seperator-col3{width:850px;padding-left:10px;background-color:#fff}
			
			.list-seperator-row {display:table-row;border-top:1px solid #555}
			.list-seperator-col1, .list-seperator-col2, .list-seperator-col3 {display:table-cell; height:10px;vertical-align:middle;}
			.list-seperator-col1{width:250px;padding-left:25px;background-color:#fff}
			.list-seperator-col2{width:8px;background-color:#fff}
			.list-seperator-col3{width:850px;padding-left:10px;background-color:#fff}
			
			.section-row{background: #3498DB;display:table-row;padding-left:10px;color:#fff;font-weight:bold;}
			.section-col1, .section-col2, .section-col3 {display:table-cell; height:25px;vertical-align:middle;}
			.section-col1{width:250px;padding-left:10px;}
			.section-col2{width:8px;background-color:#fff}
			.section-col3{width:850px;padding-left:10px;text-align: right;padding-right:10px;}
			
			.row {display:table-row}
			.col1, .col2, .col3 {display:table-cell; height:25px;vertical-align:middle;}
			.col1{width:250px;padding-right:10px;color:blue;text-align:right;}
			.col2{width:8px;background-color:#fff}
			.col3{width:850px;padding-left:10px;}
			
			.footer-row{display:table-row;}
			.footer-col1, .footer-col2, .footer-col3 {display:table-cell; height:35px;color:blue;background-color:#fff;border-top:1px solid #666;border-bottom:1px solid #666}
			.footer-col1{width:250px;border-left:1px solid #666;padding-left:10px;}
			.footer-col2{width:8px;border-top:1px solid #666;border-bottom:1px solid #666;}
			.footer-col3{width:850px;border-right:1px solid #666;color:#555}
			
			
			.row:nth-child(even) {background: #FFF}
			.row:nth-child(odd) {background: #EEE}
			 
			 
		</style>
		
		<script>
			function toggle(elem){
				if(isShow(elem))
					hide(elem);
				else
					show(elem);
			}
			
			function isShow(elem){
				return elem.title.lastIndexOf('collapse') == 0;
			}
			
			function show(elem){
				if(elem.title.lastIndexOf('expand') == 0){
					elem.title = elem.title.replace("expand", "collapse");
					elem.innerHTML = "&#9660";
					toggleAllRows(elem.title.replace("collapse", "").trim(), true);
				}
			}
			
			function hide(elem){
				if(elem.title.lastIndexOf('collapse') == 0){
					elem.title = elem.title.replace("collapse", "expand");
					elem.innerHTML = "&#9658;";
					toggleAllRows(elem.title.replace("expand", "").trim(), false);
				}
			}
			
			function toggleAllRows(title, show){
				toggleAll("row", title, show);
				toggleAll("seperator-row", title, show);
			}
			
			function toggleAll(clazz, title, show){
				var elems = document.getElementsByClassName(clazz);
				for (var i = 0; i < elems.length; i++) { 
				    if(elems[i].title == title){
				    	if(!show)
				    		elems[i].style.display = 'none';
				    	else
				    		elems[i].style.display = 'table-row';
				    }
				}
			}
			
			function showAll(){
				var elems = document.getElementsByTagName("span");
				for (var i = 0; i < elems.length; i++)
					show(elems[i]);
			}
			
			function showByTitle(title){
				var elems = document.getElementsByTagName("span");
				for (var i = 0; i < elems.length; i++){
					if(elems[i].title.indexOf(title) > -1)
						show(elems[i]);
				}
			}
			
			function hideAll(){
				var elems = document.getElementsByTagName("span");
				for (var i = 0; i < elems.length; i++)
					hide(elems[i]);
			}
			
			function hideByTitle(title){
				var elems = document.getElementsByTagName("span");
				for (var i = 0; i < elems.length; i++)
					if(elems[i].title.indexOf(title) > -1)
						hide(elems[i]);
			}
		
		</script>
		
		<body>
		<div id="wrapper">
			<div style="margin-top:50px;">
			<div class="table">
			
			<div class="title-row">
			    <div class="title-col1">META-DATA<br><span style="font-size:20px">Developer's Information</span></div>
			    <div class="title-col2"></div>
			    <div class="title-col3">
			    <%for(Datum<List<List<Datum<String>>>> data: info){%>
					<a onclick="hideAll();showByTitle('<%=data.getKey()%>');" href="javascript:void(0)"><%=data.getKey()%></a>
					&nbsp;
				<%}%>
				&nbsp;
				<a style="color:#555" onclick="showAll();" href="javascript:void(0)">Show All</a>&nbsp;&nbsp;
				<a style="color:#555" onclick="hideAll();" href="javascript:void(0)">Hide All</a>
			    </div>
			</div>
			<div class="seperator-row">
			    <div class="seperator-col1"></div>
			    <div class="seperator-col2"></div>
			    <div class="seperator-col3"></div>
			</div>
			<%
			for(Datum<List<List<Datum<String>>>> data: info){
			%>
			<div class="section-row">
			    <div title="<%=data.getDescription()%>"  class="section-col1"><a name="<%=data.getKey()%>"><%=data.getKey()%></a></div>
			    <div class="section-col2"></div>
			    <div title="Double click to view this section only" ondblclick="hideAll();showByTitle('<%=data.getKey()%>');"  class="section-col3">
			    	<span style="cursor:pointer" title="collapse <%=data.getKey()%>" onclick="toggle(this);">&#9660;</span>
			    </div>
			  </div>
			<%
				Iterator<List<Datum<String>>> iterator = data.getValue().iterator();
				while(iterator.hasNext()){
					List<Datum<String>> list = iterator.next();
			  		for(Datum<String> datum: list){
			  			%>
			  				<div title="<%=data.getKey()%>"  class="row">
							    <div title="<%=datum.getDescription()%>" class="col1"><%=datum.getKey()%></div>
							    <div class="col2"></div>
							    <div class="col3"><%=datum.getValue()%></div>
							  </div>
			  			<%
			  		}
			  		if(iterator.hasNext()){%>
			  		<div title="<%=data.getKey()%>"   class="seperator-row">
					    <div class="seperator-col1"><hr></div>
					    <div class="seperator-col2"></div>
					    <div class="seperator-col3"><hr></div>
					  </div>
			  		<%
					}
			  	}
			%>
	  		<div  class="seperator-row">
			    <div class="seperator-col1"></div>
			    <div class="seperator-col2"></div>
			    <div class="seperator-col3"></div>
			  </div>
	  		<%
			%>
			<%
			}
			%>
			<div class="footer-row">
			    <div class="footer-col1" style="padding-top:15px;font-weight:bold;font-size:18px">
			    <img width="16" height="16"  title="The Hibbert Group Logo" alt="" src="data:image/png;base64,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" />
			    	The Hibbert Group
			    </div>
			    <div class="footer-col2">&nbsp;</div>
			    <div class="footer-col3">Contact Tech Support Group for further information on this page and the information it contains:&nbsp;&nbsp;<span style="color:blue;text-decoration:underline;">techgroup@hibbertgroup.com</span></div>
			</div>
			<div class="seperator-row">
			   <div class="seperator-col1"></div>
			   <div class="seperator-col2"></div>
			   <div class="seperator-col3"></div>
			</div>
			
			</div>
			</div>
		</div>
		</body>
	<%
	}
	%>

<%!

	public static List<Datum<List<List<Datum<String>>>>> load(HttpServletRequest request, HttpServletResponse response, ServletContext application, SimpleTcpCluster simpleTcpCluster){
		List<Datum<List<List<Datum<String>>>>> info = new ArrayList<Datum<List<List<Datum<String>>>>>();
		info.add(0, new Datum<List<List<Datum<String>>>>("Server Info", loadServerInfo(request, response, simpleTcpCluster), 1, ""));
		info.add(1, new Datum<List<List<Datum<String>>>>("Application Info", loadAppInfo(request, response, simpleTcpCluster), 2, ""));
		info.add(2, new Datum<List<List<Datum<String>>>>("Frameworks Info", loadFrameworksInfo(request, response, application, simpleTcpCluster), 3, ""));
		info.add(3, new Datum<List<List<Datum<String>>>>("Memory Info", loadMemoryInfo(request, response, application, simpleTcpCluster), 4, ""));
		info.add(4, new Datum<List<List<Datum<String>>>>("Cluster Info", loadClusterInfo(request, response, application, simpleTcpCluster), 5, ""));
		info.add(5, new Datum<List<List<Datum<String>>>>("SVN Info", loadSVNInfo(request, response, application, simpleTcpCluster), 6, ""));
		info.add(6, new Datum<List<List<Datum<String>>>>("Jenkins Info", loadJenkinsInfo(request, response, application, simpleTcpCluster), 7, ""));
		info.add(7, new Datum<List<List<Datum<String>>>>("Context Info", loadContextInfo(request, response, application, simpleTcpCluster), 8, ""));
		info.add(8, new Datum<List<List<Datum<String>>>>("JNDI Info", loadJNDIInfo(request, response, application, simpleTcpCluster), 9, ""));
		return info;
	}
	
	public static List<List<Datum<String>>> loadServerInfo(HttpServletRequest request, HttpServletResponse response, SimpleTcpCluster simpleTcpCluster){
		List<List<Datum<String>>> serverInfo = new ArrayList<List<Datum<String>>>();
		List<Datum<String>> list = new  ArrayList<Datum<String>>();
		list.add(new Datum<String>("Host Name", getHostName(), 1, "Name of the machine that hosts this site."));
		list.add(new Datum<String>("IP Address", getIPaddress(request), 2, "IP address of the site."));
		list.add(new Datum<String>("External URL", getSiteUrl(request), 3, "URL of the site to access from outside network."));
		list.add(new Datum<String>("Port", request.getServerPort() + "", 4, "Port of the site."));
		list.add(new Datum<String>("OS",  System.getProperty("os.name") + " -version " + System.getProperty("os.version") + "", 5, "Operating System of the server."));
		Collections.sort(list);
		serverInfo.add(0, list);
		return serverInfo;
	}
	
	public static List<List<Datum<String>>> loadAppInfo(HttpServletRequest request, HttpServletResponse response, SimpleTcpCluster simpleTcpCluster){
		List<List<Datum<String>>> serverInfo = new ArrayList<List<Datum<String>>>();
		List<Datum<String>> list = new  ArrayList<Datum<String>>();
		list.add(new Datum<String>("Application Name", request.getSession().getServletContext().getServletContextName(), 1, "Name of the application described in web.xml"));
		list.add(new Datum<String>("HTTP Port", getTomcatPort(), 2, "Port number used for HTTP connection to this site."));
		list.add(new Datum<String>("Code Base", request.getSession().getServletContext().getRealPath(""), 3, "Deployment location"));
		list.add(new Datum<String>("Log File Path", getLogfilePath() + "", 4, "Log4j Log file location."));
		List<String> internalUrls = getInternalUrls();
		for(int i = 0; i < internalUrls.size(); i++)
			list.add(new Datum<String>("Internal URL-" + (i + 1), internalUrls.get(i) + "", 5 + i, "Internal url based of machine name or ip address."));
		Collections.sort(list);
		serverInfo.add(0, list);
		return serverInfo;
	}
	
	public static List<List<Datum<String>>> loadFrameworksInfo(HttpServletRequest request, HttpServletResponse response, ServletContext application, SimpleTcpCluster simpleTcpCluster){
		Map<String, String> frameWorks = getFrameworks(request);
		List<List<Datum<String>>> serverInfo = new ArrayList<List<Datum<String>>>();
		List<Datum<String>> list = new  ArrayList<Datum<String>>();
		list.add(new Datum<String>("Tomcat", application.getServerInfo(), 1, ""));
		list.add(new Datum<String>("Java", System.getProperty("java.version"), 2, ""));
		list.add(new Datum<String>("Servlet", application.getMajorVersion() + "." +  application.getMinorVersion(), 3, ""));
		list.add(new Datum<String>("Struts", frameWorks.get("Struts Version") == null ? "N/A" : frameWorks.get("Struts Version"), 4, ""));
		list.add(new Datum<String>("Spring", frameWorks.get("Spring Version") == null ? "N/A" : frameWorks.get("Spring Version"), 5, ""));
		list.add(new Datum<String>("Hibernate", frameWorks.get("Hibernate Version") == null ? "N/A" : frameWorks.get("Hibernate Version"), 6, ""));
		list.add(new Datum<String>("Jersey", frameWorks.get("Jersey Version") == null ? "N/A" : frameWorks.get("Jersey Version"), 7, ""));
		list.add(new Datum<String>("CXF", frameWorks.get("CXF Version") == null ? "N/A" : frameWorks.get("CXF Version"), 8, ""));
		list.add(new Datum<String>("Quartz", frameWorks.get("Quartz Version") == null ? "N/A" : frameWorks.get("Quartz Version"), 9, ""));
		list.add(new Datum<String>("JDBC", frameWorks.get("JDBC Version") == null ? "N/A" : frameWorks.get("JDBC Version"), 10, ""));
		Collections.sort(list);
		serverInfo.add(0, list);
		return serverInfo;
	}
	
	public static List<List<Datum<String>>> loadMemoryInfo(HttpServletRequest request, HttpServletResponse response, ServletContext application, SimpleTcpCluster simpleTcpCluster){
		Map<String, String> map = getMemoryInfo(simpleTcpCluster, request);
		List<List<Datum<String>>> info = new ArrayList<List<Datum<String>>>();
		List<Datum<String>> list = new  ArrayList<Datum<String>>();
		list.add(new Datum<String>("Process", map.get("Uptime") == null ? "N/A" : map.get("Process"), 1, "Tomcat process id for all contexts currently running."));
		list.add(new Datum<String>("Uptime", map.get("Uptime") == null ? "N/A" : map.get("Uptime"), 2, "Time that tomcat is ruuning uninterrupted so for."));
		list.add(new Datum<String>("Heap", map.get("Heap") == null ? "N/A" : map.get("Heap"), 3, "Heap memory info"));
		list.add(new Datum<String>("Non-Heap", map.get("Non-Heap") == null ? "N/A" : map.get("Non-Heap"), 4, "Non-Heap memory info"));
		
		list.add(new Datum<String>("Sessions Timeout Length", map.get("Sessions Timeout Length") == null ? "N/A" : map.get("Sessions Timeout Length"), 5, "Time length of a session set to expire"));
		list.add(new Datum<String>("Number of Active Sessions", map.get("Number of Active Sessions") == null ? "N/A" : map.get("Number of Active Sessions"), 6, "Number of Active Sessions"));
		list.add(new Datum<String>("Session Average Length", map.get("Session Average Length") == null ? "N/A" : map.get("Session Average Length"), 7, "Average length of one session"));
		
		list.add(new Datum<String>("Session Data Size", map.get("Session Data Size") == null ? "N/A" : map.get("Session Data Size"), 8, "Cumulative size of the session attributes"));
		list.add(new Datum<String>("Session Size", map.get("Session Size") == null ? "N/A" : map.get("Session Size"), 9, "Over all size of the http session in bulk"));
		list.add(new Datum<String>("Session Count (In Memory)", map.get("Session Count (In Memory)") == null ? "N/A" : map.get("Session Count (In Memory)"), 10, "Number of sessions in the memory"));
		list.add(new Datum<String>("Session Attribute Count", map.get("Session Attribute Count") == null ? "N/A" : map.get("Session Attribute Count"), 11, "Total number of session attributes"));
		Collections.sort(list);
		info.add(0, list);
		return info;
	}
	
	
	public static List<List<Datum<String>>> loadClusterInfo(HttpServletRequest request, HttpServletResponse response, ServletContext application, SimpleTcpCluster simpleTcpCluster){
		Map<String, String> baseMap = getClusterInfo(simpleTcpCluster, request);
		List<List<Datum<String>>> info = new ArrayList<List<Datum<String>>>();
		List<Datum<String>> baselist = new  ArrayList<Datum<String>>();
		baselist.add(new Datum<String>("Multicast Address", baseMap.get("Multicast Address") == null ? "N/A" : baseMap.get("Multicast Address"), 1, "Multicast IP address used for this cluster group"));
		baselist.add(new Datum<String>("Multicast Port", baseMap.get("Multicast Port") == null ? "N/A" : baseMap.get("Multicast Port"), 2, "Multicast port used for this cluster group"));
		baselist.add(new Datum<String>("Multicast Frequency", baseMap.get("Multicast Frequency") == null ? "N/A" : baseMap.get("Multicast Frequency"), 3, "A Frequency (milliseconds) at which the data is cast for an update across cluster"));
		baselist.add(new Datum<String>("Member Drop Time", baseMap.get("Member Drop Time") == null ? "N/A" : baseMap.get("Member Drop Time"), 4, "Time (milliseconds) to wait to assume a non-responding member is dropped"));
		baselist.add(new Datum<String>("Member Poke Time", baseMap.get("Member Poke Time") == null ? "N/A" : baseMap.get("Member Poke Time"), 4, "Number of time a non-responding member is poked to declare dead"));
		
		Collections.sort(baselist);
		info.add(0, baselist);
		
		int count = 1;
		for(Map<String, String> map: getClusterMemberInfo(simpleTcpCluster, request)){
			List<Datum<String>> list = new  ArrayList<Datum<String>>();
			list.add(new Datum<String>("Member Count", count + "", 1, "Member's serial number"));
			list.add(new Datum<String>("Member Address", map.get("Member Address") == null ? "N/A" : map.get("Member Address"), 2, "Member's IP address"));
			list.add(new Datum<String>("Member TCP Address", map.get("Member TCP Address") == null ? "N/A" : map.get("Member TCP Address"), 3, "Member's TCP Address url for multicast"));
			list.add(new Datum<String>("JVM Route", map.get("JVM Route") == null ? "N/A" : map.get("JVM Route"), 4, "JVM Route through which the member gets the serialized session data"));
			list.add(new Datum<String>("Port", map.get("Port") == null ? "N/A" : map.get("Port"), 5, "Port that member uses to send and receive multicaste data nad heart beat"));
			list.add(new Datum<String>("Ready?", map.get("Ready?") == null ? "N/A" : map.get("Ready?"), 6, "Is member healthy and alive in the cluster group?"));
			list.add(new Datum<String>("Member Alive Time", map.get("Member Alive Time") == null ? "N/A" : map.get("Member Alive Time"), 7, "Member Alive Time"));
			
			Collections.sort(list);
			info.add(count++, list);
		}
		return info;
	}
	
	public static List<List<Datum<String>>> loadSVNInfo(HttpServletRequest request, HttpServletResponse response, ServletContext application, SimpleTcpCluster simpleTcpCluster){
		Map<String, String> map = getSVNProperties(request);
		List<List<Datum<String>>> info = new ArrayList<List<Datum<String>>>();
		List<Datum<String>> list = new  ArrayList<Datum<String>>();
		list.add(new Datum<String>("Deployed Branch", map.get("Branch") == null ? "N/A" : map.get("Branch"), 1, "SVN Branch that the deployed object(s) built from"));
		list.add(new Datum<String>("Deployed Revision", map.get("Revision") == null ? "N/A" : map.get("Revision"), 2, "Revision of the SVN Branch that the deployed object(s) built from"));
		list.add(new Datum<String>("Committed By", map.get("User") == null ? "N/A" : map.get("User"), 3, "The user that comitted the revision"));
		list.add(new Datum<String>("Committed On", map.get("Date") == null ? "N/A" : map.get("Date"), 4, "The date that the deployed revision comitted on"));
		list.add(new Datum<String>("Latest Pcrs", map.get("Latest Pcrs") == null ? "N/A" : map.get("Latest Pcrs"), 5, "Latest Pcrs"));
		list.add(new Datum<String>("Recent Logs", "".equals(map.get("Recent Logs")) ? "N/A" : map.get("Recent Logs"), 6, "Recent Logs"));

		Collections.sort(list);
		info.add(0, list);
		return info;
	}
	
	public static List<List<Datum<String>>> loadJenkinsInfo(HttpServletRequest request, HttpServletResponse response, ServletContext application, SimpleTcpCluster simpleTcpCluster){
		String project = new File(request.getSession().getServletContext().getRealPath("")).getParentFile().getParentFile().getName();
		if(System.getProperty("os.name").contains("Windows"))
			project = request.getSession().getServletContext().getServletContextName();
		
		
		List<List<Datum<String>>> info = new ArrayList<List<Datum<String>>>();
		List<Datum<String>> list = new  ArrayList<Datum<String>>();
		Map<String, String> map = guessJenkinsProperties(project);
		list.add(new Datum<String>("Job Name", map.get("Job Name") == null ? "N/A" : map.get("Job Name"), 1, "Name of the Jenkins job"));
		list.add(new Datum<String>("Jenkins Url", map.get("Jenkins Url") == null ? "N/A" : map.get("Jenkins Url"), 2, "Url to access the Jenkins job"));
		list.add(new Datum<String>("Recipient(s)", map.get("Recipient(s)") == null ? "N/A" : map.get("Recipient(s)"), 3, "Email address(es) of the Recipient(s) who receive build results"));
		
		for(int i = 0; i < 5; i++) {
			if(map.get("Repository " + i) != null) {
				list.add(new Datum<String>("Repository " + i, map.get("Repository " + i) == null ? "N/A" : map.get("Repository " + i), 3 + i, "Repository url"));
			}
		}	
		
		Collections.sort(list);
		info.add(0, list);
		return info;
	}
	
	
	public static List<List<Datum<String>>> loadContextInfo(HttpServletRequest request, HttpServletResponse response, ServletContext application, SimpleTcpCluster simpleTcpCluster){
		List<List<Datum<String>>> info = new ArrayList<List<Datum<String>>>();
		int count = 0;
		for(Map<String, String> map: getContexts()){
			List<Datum<String>> list = new  ArrayList<Datum<String>>();
			list.add(new Datum<String>("Url", map.get("url") == null ? "N/A" : map.get("url"), 1, "Context url"));
			list.add(new Datum<String>("Document Base", map.get("docBase") == null ? "N/A" : map.get("docBase"), 2, "Document base of the context"));
			list.add(new Datum<String>("Startup Time", map.get("startupTime") == null ? "N/A" : map.get("startupTime"), 3, "Time taken to start up the context"));
			Collections.sort(list);
			info.add(count++, list);
		}
		return info;
	}
	
	public static List<List<Datum<String>>> loadJNDIInfo(HttpServletRequest request, HttpServletResponse response, ServletContext application, SimpleTcpCluster simpleTcpCluster){
		List<List<Datum<String>>> info = new ArrayList<List<Datum<String>>>();
		int count = 0;
		for(Map<String, String> map: getResources()){
			List<Datum<String>> list = new  ArrayList<Datum<String>>();
			list.add(new Datum<String>("Name", map.get("jndi") == null ? "N/A" : map.get("jndi"), 1, "jndi name"));
			list.add(new Datum<String>("Url", map.get("url") == null ? "N/A" : map.get("url"), 2, "jndi url"));
			list.add(new Datum<String>("Username", map.get("username") == null ? "N/A" : map.get("username"), 3, "username"));
			list.add(new Datum<String>("Alive", checkDatabase(map.get("jndi")), 3, "Alive?"));
			list.add(new Datum<String>("Connections", map.get("connections") == null ? "N/A" : map.get("connections"), 3, "Number of active connection"));
			Collections.sort(list);
			info.add(count++, list);
		}
		return info;
	}
	
	private static Map<String, String> getSVNProperties(HttpServletRequest request){
		Map<String, String> svnProperties = new HashMap<String, String>();
		File file = new File(request.getSession().getServletContext().getRealPath(""), "/WEB-INF/classes/entries");
		svnProperties = getSvnProperties(file);
		svnProperties.put("Latest Pcrs", "N/A");
		String logs = "";
		if(svnProperties.get("Branch") != null){
			List<String> latestPcrs = getLatestPcrsAndLogs(svnProperties.get("Branch"), (String)request.getSession().getAttribute("username"), (String)request.getSession().getAttribute("password"));
			Iterator<String> iterator = latestPcrs.iterator();
			svnProperties.put("Latest Pcrs", iterator.next());
			while(iterator.hasNext()) logs = logs + iterator.next() + "<br/>";
		}
		svnProperties.put("Recent Logs", logs);
		return svnProperties;
	}
	
	
	private static Map<String, String> getFrameworks(HttpServletRequest request){
		Map<String, String> frameworks = new HashMap<String, String>();
		
		try {
        	Class springClass = Class.forName("org.springframework.core.SpringVersion");
            Object springObject = springClass.newInstance();
            Method springMethod = springClass.getMethod("getVersion");
            frameworks.put("Spring Version", (String)springMethod.invoke(springObject, null));
         } catch(Exception e) {}
        
        try {
        	Class hibernateClass = Class.forName("org.hibernate.Version");
            Object hibernateObject = hibernateClass.newInstance();
            Method hibernateMethod = hibernateClass.getMethod("getVersionString");
            frameworks.put("Hibernate Version", (String)hibernateMethod.invoke(hibernateObject, null));
         } catch(Exception e) {}
        
        try {
			Class StdSchedulerFactoryClass = Class.forName("org.quartz.impl.StdSchedulerFactory");
	        Object StdSchedulerFactory = StdSchedulerFactoryClass.newInstance();
	        Method getDefaultScheduler = StdSchedulerFactoryClass.getMethod("getDefaultScheduler");
	        
	        Class SchedulerClass = Class.forName("org.quartz.Scheduler");
	        Object Scheduler = getDefaultScheduler.invoke(StdSchedulerFactory, null);
	        Method getMetaData = SchedulerClass.getMethod("getMetaData");
	        
	        Class SchedulerMetaDataClass = Class.forName("org.quartz.SchedulerMetaData");
	        Object SchedulerMetaData = getMetaData.invoke(Scheduler, null);
	        Method getVersion = SchedulerMetaDataClass.getMethod("getVersion");
	        
	        String version = (String)getVersion.invoke(SchedulerMetaData, null);
	        frameworks.put("Quartz Version", version);
		} catch (Exception e) {}
        
        try {
			Enumeration<URL> resources = Logger.class.getClassLoader()
					.getResources("META-INF/MANIFEST.MF");
			String strutsVersionTitle = "";
			String cxfVersion = "N/A";
			String jerseyVersion = "N/A";
			String strutsVersion = "N/A";
			String jdbcVersion = "N/A";
			while (resources.hasMoreElements()) {
				Manifest manifest = new Manifest(resources.nextElement().openStream());
				boolean readJersey = false;
				boolean readCXF = false;
				boolean readStruts = false;
				boolean readJdbc = false;
				for(Object key: manifest.getMainAttributes().keySet()){
					if("Specification-Title".equals(key.toString()) && 
							manifest.getMainAttributes().get(key).toString().toUpperCase().contains("CXF")){
						readCXF = true;
					}
					
					if(readCXF && "Specification-Version".equals(key.toString())){
						cxfVersion = manifest.getMainAttributes().get(key).toString();
						readCXF = false;
					}
					
					if("Implementation-Title".equals(key.toString()) && 
							manifest.getMainAttributes().get(key).toString().toLowerCase().contains("jersey")){
						readJersey = true;
					}
					
					if(readJersey && "Implementation-Version".equals(key.toString())){
						jerseyVersion = manifest.getMainAttributes().get(key).toString();
						readJersey = false;
					}
					
					if("Specification-Title".equals(key.toString()) && 
							("Struts Framework".equals(manifest.getMainAttributes().get(key).toString()) ||
									"Struts Core".equals(manifest.getMainAttributes().get(key).toString()) ||
									"Struts 2 Core".equals(manifest.getMainAttributes().get(key).toString()))){
						strutsVersionTitle = manifest.getMainAttributes().get(key).toString();
						readStruts = true;
					}
					if(readStruts && "Specification-Version".equals(key.toString())){
						strutsVersion = strutsVersionTitle + " " + manifest.getMainAttributes().get(key).toString();
						readStruts = false;
					}
					if("Bundle-Name".equals(key.toString()) && 
							("Struts Framework".equals(manifest.getMainAttributes().get(key).toString()) ||
									"Struts Core".equals(manifest.getMainAttributes().get(key).toString()) ||
									"Struts 2 Core".equals(manifest.getMainAttributes().get(key).toString()))){
						strutsVersionTitle = manifest.getMainAttributes().get(key).toString();
						readStruts = true;
					}
					if(readStruts && "Bundle-Version".equals(key.toString())){
						strutsVersion = strutsVersionTitle + " " + manifest.getMainAttributes().get(key).toString();
						readStruts = false;
					}
					if("Specification-Title".equals(key.toString()) && 
							manifest.getMainAttributes().get(key).toString().toLowerCase().contains("oracle") &&
							manifest.getMainAttributes().get(key).toString().toLowerCase().contains("jdbc") &&
							manifest.getMainAttributes().get(key).toString().toLowerCase().contains("driver")){
						readJdbc = true;
					}
					if(readJdbc && "Specification-Version".equals(key.toString())){
						jdbcVersion = manifest.getMainAttributes().get(key).toString();
						readJdbc = false;
					}
				}
			}
			frameworks.put("Struts Version", strutsVersion);
			frameworks.put("JDBC Version", jdbcVersion);
			frameworks.put("Jersey Version", jerseyVersion);
			frameworks.put("CXF Version", cxfVersion);
		} catch (Exception se) {
			se.printStackTrace();
		}
        return frameworks;
	}
	
	private static String getTomcatPort(){
		String port = "N/A";
		try{
			MBeanServer mbsBeanServer = ManagementFactory.getPlatformMBeanServer();
			Set<ObjectName> objectNames = mbsBeanServer.queryNames(
					new ObjectName("*:type=Connector,*"), 
					Query.match(Query.attr("protocol"),
					Query.value("HTTP/1.1")));
			for (ObjectName objectName: objectNames) {
				port = objectName.getKeyProperty("port");
			}
		}catch(Exception e) {}
		return port;
	}
	
	private static String getLogfilePath(){
		try{
			Enumeration<Object> appenders = Logger.getRootLogger().getAllAppenders();
			while(appenders.hasMoreElements()){
				Object object = appenders.nextElement();
				if(object instanceof FileAppender){
					FileAppender appender = (FileAppender) object;
					return new File(appender.getFile()).getAbsolutePath();
				}
			}
		}catch(Exception e) {}
		return "N/A";
	}
	
	private static List<String> getInternalUrls(){
		List<String> endPoints = new ArrayList<String>();
		try{
			MBeanServer mbsBeanServer = ManagementFactory.getPlatformMBeanServer();
			Set<ObjectName> objectNames = mbsBeanServer.queryNames(
					new ObjectName("*:type=Connector,*"), 
					Query.match(Query.attr("protocol"),
					Query.value("HTTP/1.1")));
			String scheme = "http";
			String port = "N/A";
			String hostname = InetAddress.getLocalHost().getHostName();
			InetAddress[] addresses = InetAddress.getAllByName(hostname);
			for (ObjectName objectName: objectNames) {
				scheme = mbsBeanServer.getAttribute(objectName, "scheme").toString();
				port = objectName.getKeyProperty("port");
				for (InetAddress addr : addresses) {
					String host = addr.getHostAddress();
					String ep = scheme + "://" + host + ":" + port;
					endPoints.add(ep);
				}
			}
			endPoints.add(scheme + "://" + hostname + ":" + port);
		}catch(Exception e) {}
		return endPoints;
	}

	public static String getSiteUrl(HttpServletRequest request) {
		return request.getScheme() + "://" + request.getServerName() + ":"
				+ request.getServerPort() + request.getSession().getServletContext().getContextPath();
	}
	
	private static String getIPaddress(HttpServletRequest request){
		try{
			return InetAddress.getByName(request.getServerName()).getHostAddress();
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}
	
	private static String getHostName(){
		try{
			return InetAddress.getLocalHost().getHostName();
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}
	
	private static  Map<String, String> getMemoryInfo(SimpleTcpCluster simpleTcpCluster, HttpServletRequest request){
		long mb = 1024l * 1024l;
		Map<String, String> memoryInfo = new HashMap<String, String>();
		MemoryMXBean memBean = ManagementFactory.getMemoryMXBean() ;
		MemoryUsage heap = memBean.getHeapMemoryUsage();
		MemoryUsage nonHeap = memBean.getNonHeapMemoryUsage();
		double heapPercent = (new Double((int)((new Double(heap.getUsed()) / new Double(heap.getCommitted())) * 10000d)))/100d;
		double nonHeapPercent = (new Double((int)((new Double(nonHeap.getUsed()) / new Double(nonHeap.getCommitted())) * 10000d)))/100d;
		memoryInfo.put("Process", ManagementFactory.getRuntimeMXBean().getName());
		memoryInfo.put("Uptime", getTime(ManagementFactory.getRuntimeMXBean().getUptime()));
		memoryInfo.put("Heap", (heapPercent) + "% " + String.format("(Init: %dmb, Used: %dmb, Committed: %dmb, Max.: %dmb)",
				  heap.getInit()/mb, heap.getUsed()/mb, heap.getCommitted()/mb, heap.getMax()/mb));
		memoryInfo.put("Non-Heap", (nonHeapPercent) + "% " +   String.format("(Non-Heap: Init: %dmb, Used: %dmb, Committed: %dmb, Max.: %dmb)",
				  nonHeap.getInit()/mb, nonHeap.getUsed()/mb, nonHeap.getCommitted()/mb, nonHeap.getMax()/mb));
		memoryInfo.putAll(getSessionMemorInfo(simpleTcpCluster));
		memoryInfo.putAll(getBaseSessionInfo(request.getSession().getServletContext().getContextPath()));
		
		return memoryInfo;
	}
	
	
	private static Map<String, String> getBaseSessionInfo(String ctx){
		if(ctx == null || ctx.equals(""))
			ctx = "/";
		Map<String, String> sessionInfo = new HashMap<String, String>();
		try{
			MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
			Set<ObjectName> objectNames = mBeanServer
					.queryNames(
							new ObjectName("Catalina:type=Manager,context=" + ctx + ",host=localhost"), 
							null);
			for (ObjectName objectName: objectNames) {
				try{
					sessionInfo.put("Sessions Timeout Length", getTime(1000l * Long.parseLong(mBeanServer.getAttribute(objectName, "maxInactiveInterval").toString())));
					sessionInfo.put("Number of Active Sessions", mBeanServer.getAttribute(objectName, "activeSessions").toString());
					sessionInfo.put("Session Average Length", getTime(1000l * Long.parseLong(mBeanServer.getAttribute(objectName, "sessionAverageAliveTime").toString())));
				}catch(Exception exp){
					continue;
				}
			}
		}catch(Exception e) {}
		return sessionInfo;
	}

	private static  Map<String, String> getSessionMemorInfo(SimpleTcpCluster simpleTcpCluster){
		long mb = 1024l * 1024l;
		Map<String, String> memoryInfo = new HashMap<String, String>();
		
		if(simpleTcpCluster == null)
			return memoryInfo;
		
		long sessionSize = 0;
    	long sessionDataSize = 0;
    	long sessionCount = 0;
    	long sessionAttributeCount = 0;
    	for(String managerName: simpleTcpCluster.getManagers().keySet()){
    		Manager manager = simpleTcpCluster.getManagers().get(managerName);
	    	for(Session sess: manager.findSessions()){
	    		sessionCount++;
	    		try{
	    			sessionSize += objectSize(sess);
    			}catch(Exception e){e.printStackTrace();}
	    		Enumeration<String> enumeration = sess.getSession().getAttributeNames();
	    		while(enumeration.hasMoreElements()){
	    			sessionAttributeCount++;
	    			try{
	    				sessionDataSize += objectSize(sess.getSession().getAttribute(enumeration.nextElement()));
	    			}catch(Exception e){e.printStackTrace();}
	    		}
	    	}
    	}
		memoryInfo.put("Session Size", ((new Double((int)((new Double(sessionSize) / new Double(mb)) * 10000d)))/100d) + "");
		memoryInfo.put("Session Data Size", ((new Double((int)((new Double(sessionDataSize) / new Double(mb)) * 10000d)))/100d) + "");
		memoryInfo.put("Session Count (In Memory)", sessionCount + "");
		memoryInfo.put("Session Attribute Count", sessionAttributeCount + "");
		return memoryInfo;
	}
	
	private static  Map<String, String> getClusterInfo(SimpleTcpCluster simpleTcpCluster, HttpServletRequest request){
		Map<String, String> clusterInfo = new HashMap<String, String>();
    	
		if(simpleTcpCluster == null)
			return clusterInfo;
		
		GroupChannel groupChannel = (GroupChannel)simpleTcpCluster.getChannel();
    	MembershipService membershipService =  groupChannel.getMembershipService();
    	Properties properties = membershipService.getProperties();
    	clusterInfo.put("Multicast Address", properties.getProperty("mcastAddress"));
    	clusterInfo.put("Multicast Port", properties.getProperty("mcastPort"));
    	clusterInfo.put("Multicast Frequency", properties.getProperty("mcastFrequency") + " ms");
    	clusterInfo.put("Member Drop Time", properties.getProperty("memberDropTime") + " ms");
    	try{
    		clusterInfo.put("Member Poke Time", "" + (Integer.parseInt(properties.getProperty("memberDropTime"))/Integer.parseInt(properties.getProperty("mcastFrequency"))));
    	}catch(Exception e){}
		
		return clusterInfo;
	}
	
	private static  List<Map<String, String>> getClusterMemberInfo(SimpleTcpCluster simpleTcpCluster, HttpServletRequest request){
		List<Map<String, String>> resources = new ArrayList<Map<String,String>>();
		if(simpleTcpCluster == null)
			return resources;
		StandardEngine engine = (StandardEngine)simpleTcpCluster.getContainer();
    	
		Map<String, String> map = new HashMap<String, String>();
		
		Member localMember = simpleTcpCluster.getLocalMember();
		MemberImpl localMemberImpl = (MemberImpl)localMember;
		String localHostname = localMemberImpl.getHostname();
		
		map.put("Member Address", localHostname);
		map.put("Member TCP Address", localMemberImpl.getName());
		map.put("JVM Route", engine.getJvmRoute());
		map.put("Port", localMemberImpl.getPort() + "");
		map.put("Ready?", localMemberImpl.isReady() ? "Yes" : "No");
		map.put("Member Alive Time", getTime(localMemberImpl.getMemberAliveTime()));
		resources.add(0, map);
		int index = 1;
		for(Member member: simpleTcpCluster.getMembers()){
			map = new HashMap<String, String>();
    		MemberImpl memberImpl = (MemberImpl)member;
    		String hostname = memberImpl.getHostname();
    		hostname = hostname.replace(", ", ".");
    		hostname = hostname.replace("{", "");
    		hostname = hostname.replace("}", "");
    		map.put("Member Address", hostname);
    		
    		map.put("Member TCP Address", memberImpl.getName());
    		String infoUrl = "http://" + hostname + ":" + getTomcatPort() + "/info.jsp";
    		map.put("JVM Route", "N/A" + " Please check " + "<a href='" + infoUrl + "'>" + infoUrl + "</a>");
    		map.put("Port", memberImpl.getPort() + "");
    		map.put("Ready?", memberImpl.isReady() ? "Yes" : "No");
    		map.put("Member Alive Time", getTime(memberImpl.getMemberAliveTime()));
    		resources.add(index++, map);
    	}
		
		return resources;
	}
	
	public static boolean authenticated(String username, String password)  throws SVNException {
		
		SVNRepository svnRepository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded("svn://hgsun72/repository/projects/"));
	    try {
	        svnRepository.setAuthenticationManager(new BasicAuthenticationManager(username, password));
	        svnRepository.testConnection();
	        return true;
	    } catch (SVNException e) {
	        if (e.getErrorMessage().getErrorCode() == SVNErrorCode.RA_NOT_AUTHORIZED) {
	            return false;
	        } else {
	            throw e;
	        }
	    } finally {
	        svnRepository.closeSession();
	    }
	}
	
	private static Map<String, String> getSvnProperties(File file){
		Map<String, String> svnProperties = new HashMap<String, String>();
		if(!file.exists())
			return svnProperties;
		try {
			Scanner scanner = new Scanner(file).useDelimiter("\\n");
			Map<String, String> map = new HashMap<String, String>();
			map.put("1", "Version");
			map.put("5", "Branch");
			map.put("10", "Date");
			map.put("11", "Revision");
			map.put("12", "User");
			int count = 0;
			while(scanner.hasNext()){
				count++;
				String line = scanner.next().trim();
				if(map.containsKey(count + ""))
					svnProperties.put(map.get(count + ""), line);
			}
		} catch (FileNotFoundException e) {}
		return svnProperties;
	}
	
	private static List<String> getLatestPcrsAndLogs(String svnUrl, String name, String password){
		List<String> pcrsAndLogs = new ArrayList<String>();
		try{
			
			DAVRepositoryFactory.setup();
		    SVNRepositoryFactoryImpl.setup();
		    FSRepositoryFactory.setup();
		    
		    SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(svnUrl));
		    ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(name, password);
		    repository.setAuthenticationManager(authManager);
		    long latestRevision = repository.getLatestRevision();
		    final Pattern p = Pattern.compile("-?\\d{12,}");
		    final Set<String> pcrs = new HashSet<String>();
		    final List<String> logs = new ArrayList<String>();
		    long total = repository.log(new String[] {""},
		    		latestRevision, 0l, true, true, 200, new ISVNLogEntryHandler(){
		    	public void handleLogEntry(SVNLogEntry logEntry)
	                    throws org.tmatesoft.svn.core.SVNException{
			        Matcher m = p.matcher(logEntry.getMessage());
			        while (m.find()) if(pcrs.size() < 15) pcrs.add(m.group());	
			        if(logs.size() < 15)
			        	logs.add(logEntry.getRevision() + " | " + logEntry.getAuthor() + " | " + logEntry.getMessage());
		    	}
		    });
		    String pcrsString = "";
		    Iterator<String> pcrsIterator = pcrs.iterator();
		    while(pcrsIterator.hasNext()) pcrsString = pcrsString + pcrsIterator.next() + " ";
		    pcrsAndLogs.add(0, pcrsString);
		    pcrsAndLogs.addAll(1, logs);
		}catch(Exception e) {}
		return pcrsAndLogs;
	}
	
	private static Map<String, String> guessJenkinsProperties(String project){
		Map<String, String> map = new HashMap<String, String>();
		String jobName = "N/A";
		List<String> jobs = readJobs(project);
		
		for(String job: jobs){
			String config = fromURL("http://tcc-tcdev01:10005/job/" + job + "/config.xml");
			boolean s = matches(config, "/www/dev/apps/" + project);
			if(s) {
				map.put("Job Name", job);
				map.put("Jenkins Url", "http://tcc-tcdev01:10005/job/" + job);
				map.put("Recipient(s)", "N/A");
				map.put("Repository", "N/A");
				try{
					JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
					Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
					config = config.replace("hudson.tasks.", "");
					config = config.replace("hudson.scm.SubversionSCM_-", "");
					config = config.replace("class=\"hudson.scm.SubversionSCM\"", "");
					config = config.replace("Mailer", "mailer");
					config = config.replace("ModuleLocation", "modulelocation");
					
					Project p = (Project) jaxbUnmarshaller.unmarshal(new InputSource(new StringReader(config)));
					map.put("Recipient(s)", p.getPublishers().getMailer().getRecipients());
					for(int x = 0; x < p.getScm().getLocations().getModulelocation().size(); x++)
						map.put("Repository" + " " + (x + 1), p.getScm().getLocations().getModulelocation().get(x).getRemote());
				}catch(Exception e){ }
				break;
			}
		}
		return map;
	}
	
	private static boolean matches(CharSequence source, String regex){
		try {
	        Pattern pattern = Pattern.compile(regex);
	        Matcher matcher = pattern.matcher(source);
	        if (matcher.find()) {
	            return true;
	        }
	    } catch (Exception e) {}
		 return false;
	}
	
	public static String fromURL(String url) {
		url = url.replace(" ", "%20");
		try {
			Scanner s = new Scanner(new InputStreamReader(new URL(url).openStream())).useDelimiter("\\A");
			return s.hasNext() ? s.next() : "";
		} catch (Exception e) {}
	    return "";
	}
	
	private static List<String> readJobs(String matchingString) {
		List<String> relavantJobs = new ArrayList<String>();
		try {
			URL url = new URL("http://tcc-tcdev01:10005/api/json?pretty=true");
	        final BufferedReader reader =
	            new BufferedReader(new InputStreamReader(url.openStream()));
	       	JSONObject  jobject = (JSONObject)new JSONParser().parse(reader);
	        JSONArray jsonArray = (JSONArray)jobject.get("jobs");
			Iterator<JSONObject> iterator = jsonArray.iterator();
			Map<Double, List<String>> jobs = new TreeMap<Double, List<String>>(Collections.reverseOrder());
			while(iterator.hasNext()){
				JSONObject object = iterator.next();
				String jobName = object.get("name").toString();
					double similarity = similarity(jobName, matchingString);
					if(!jobs.containsKey(similarity)) 
						jobs.put(similarity, new ArrayList<String>());
					jobs.get(similarity).add(jobName.replaceAll("\"", ""));
			}
				
			for(Double d: jobs.keySet()){
				relavantJobs.addAll(jobs.get(d));
			}
			
		} catch (Exception e) {}
		return relavantJobs;
	}

	public static double similarity(String s1, String s2) {
        if (s1.length() < s2.length()) { 
            String swap = s1; s1 = s2; s2 = swap;
        }
        int bigLen = s1.length();
        if (bigLen == 0) { return 1.0; /* both strings are zero length */ }
        return (bigLen - computeEditDistance(s1, s2)) / (double) bigLen;
    }

    public static int computeEditDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }
    
    private static List<Map<String, String>> getContexts(){
		List<Map<String, String>> resources = new ArrayList<Map<String,String>>();
		try{
			MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
			Set<ObjectName> objectNames = mBeanServer
					.queryNames(
							new ObjectName("*:j2eeType=WebModule,*"), 
							null);
			for (ObjectName objectName: objectNames) {
				try{
					Map<String, String> map = new HashMap<String, String>();
					String url = mBeanServer.getAttribute(objectName, "encodedPath").toString();
					String docBase = mBeanServer.getAttribute(objectName, "docBase").toString();
					String startupTime = mBeanServer.getAttribute(objectName, "startupTime").toString();
					map.put("url", url);
					map.put("docBase", docBase);
					map.put("startupTime", getTime(Long.parseLong(startupTime) * 1000l));
					resources.add(map);
				}catch(Exception exp){
					continue;
				}
			}
		}catch(Exception e) {}
		return resources;
	}
    
    private static List<Map<String, String>> getResources(){
		List<Map<String, String>> resources = new ArrayList<Map<String,String>>();
		try{
			MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
			Set<ObjectName> objectNames = mBeanServer
					.queryNames(
							new ObjectName("*:type=DataSource,*"), 
							null);
			for (ObjectName objectName: objectNames) {
				try{
					Map<String, String> map = new HashMap<String, String>();
					//String jndi = mBeanServer.getAttribute(objectName, "name").toString();
					String url = mBeanServer.getAttribute(objectName, "url").toString();
					String username = mBeanServer.getAttribute(objectName, "username").toString();
					String connections = mBeanServer.getAttribute(objectName, "numActive").toString() + "/" + mBeanServer.getAttribute(objectName, "maxActive").toString() + " (active/max)";
					map.put("jndi", objectName.toString().substring(objectName.toString().lastIndexOf("=\"") + 1).replaceAll("\"", ""));
					map.put("url", url);
					map.put("username", username);
					map.put("connections", connections);
					resources.add(map);
				}catch(Exception exp){
					continue;
				}
			}
		}catch(Exception e) {}
		return resources;
	}
    
    private static String checkDatabase(String jndi) {
		Map<String, String> connectionList = new TreeMap<String, String>();

		connectionList.put(jndi, "select 'Yes' as result, sysdate from dual");

		for (String key : connectionList.keySet()) {
			Context initCtx = null;
			Context env = null;
			DataSource pool = null;
			Connection conn = null;
			Statement stmt = null;
			ResultSet rs = null;
			try {
				initCtx = new InitialContext();
				env = (Context) initCtx.lookup("java:comp/env");
				pool = (DataSource) env.lookup(key);
				conn = pool.getConnection();
				stmt = conn.createStatement();
				rs = stmt.executeQuery(connectionList.get(key));

				if (rs.next()) 
					return rs.getString("result");
				else
					return "<span style=\"color:red\">No</span>";
			} catch (Exception e) {
			} finally {
				try {
					stmt.close();
					conn.close();
				} catch (Exception sqle) {
				}
			}
		}
		return "N/A";
	}

	private static String getTime(long timems){
		String timeString = "N/A";
		long ms = timems;
		long d = ms / (24 * 60 * 60 * 1000);
		ms = ms - (d * 24 * 60 * 60 * 1000);
		long h = ms / (60 * 60 * 1000);
		ms = ms - (h * 60 * 60 * 1000);
		long m = ms / (60 * 1000);
		ms = ms - (m * 60 * 1000);
		long s = ms / (1000);
		ms = ms - (s * 1000);
		return d + "d " + h + "h " + m + "m " + s + "s " + ms + "ms";
	}
	
	public static class Datum<T> implements Comparable<Datum<T>>, Serializable, JSONAware {
		private String key;
		private T value;
		private int ordinal;
		private String description;
		
		public Datum() {
			super();
		}

		public Datum(String key, T value, int ordinal, String description) {
			super();
			this.key = key;
			this.value = value;
			this.ordinal = ordinal;
			this.description = description;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public T getValue() {
			return value;
		}

		public void setValue(T value) {
			this.value = value;
		}

		public int getOrdinal() {
			return ordinal;
		}

		public void setOrdinal(int ordinal) {
			this.ordinal = ordinal;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		@Override
		public int compareTo(Datum<T> o) {
			return ordinal - o.getOrdinal();
		}
		
		public String toJSONString(){
	        StringBuffer sb = new StringBuffer();
	        
	        sb.append("{");
	        
	        sb.append("\"key\"");
	        sb.append(":");
	        sb.append("\"" + key + "\"");
	        sb.append(",");
	        
	        sb.append("\"value\"");
	        sb.append(":");
	        sb.append("\"" + (value == null ? "": JSONObject.escape(value.toString())) + "\"");
	        sb.append(",");
	        
	        sb.append("\"description\"");
	        sb.append(":");
	        sb.append("\"" + description + "\"");
	        sb.append(",");
	        
	        sb.append("\"ordinal\"");
	        sb.append(":");
	        sb.append(ordinal);
	        
	        sb.append("}");
	        
	        return sb.toString();
		}
	}
	
	private static String infoToJson(List<Datum<List<List<Datum<String>>>>> info){
		StringBuffer sb = new StringBuffer();
		
		/*sb.append("[");
		Iterator<Datum<List<List<Datum<String>>>>> iterator0 = info.iterator();
		while(iterator0.hasNext()){
			Datum<List<List<Datum<String>>>> datum = iterator0.next();
			sb.append("{");
        
	        sb.append("\"key\"");
	        sb.append(":");
	        sb.append("\"" + datum.getKey() + "\"");
	        sb.append(",");
        
	        sb.append("\"value\"");
	        sb.append(":");
       
			Iterator<List<Datum<String>>> iterator1 = datum.getValue().iterator();
			sb.append("[");
			while(iterator1.hasNext()){
				Iterator<Datum<String>> iterator2 = iterator1.next().iterator();
				sb.append("[");
				while(iterator2.hasNext()){
					sb.append(iterator2.next().toJSONString());
					if(iterator2.hasNext())
						sb.append(",");
				}
				sb.append("]");
				if(iterator1.hasNext())
					sb.append(",");
			}
			sb.append("]");
	        sb.append(",");
	        
	        sb.append("\"description\"");
	        sb.append(":");
	        sb.append("\"" + datum.getDescription() + "\"");
	        sb.append(",");
	        
	        sb.append("\"ordinal\"");
	        sb.append(":");
	        sb.append(datum.getOrdinal());
	        
	        sb.append("}");
	        
	        if(iterator0.hasNext())
	        	sb.append(",");
		}
		sb.append("]");
		Type listType = new TypeToken<ArrayList<Datum<ArrayList<ArrayList<Datum<String>>>>>>() {
        }.getType();
		//return new Gson().toJson(info, listType);
		*/
		return sb.toString();
	
	}

%>

<%!


	

%>

<%!

	public static long objectSize(Object object) throws Exception{
		if(!(object instanceof Serializable))
			throw new Exception("Object is not serializable");
		 Serializable ser = (Serializable)object;
		 ByteArrayOutputStream baos = new ByteArrayOutputStream();
		 ObjectOutputStream oos = new ObjectOutputStream(baos);
		 oos.writeObject(ser);
		 long size = baos.size();
		 baos.close();
		 oos.close();
		 return size;
	}
%>

<%!
	public static SimpleTcpCluster loadCluster() throws Exception {
		SimpleTcpCluster simpleTcpCluster = null;
		
		ArrayList<MBeanServer> mbservers = MBeanServerFactory
				.findMBeanServer(null);
	
		if (mbservers == null || mbservers.size() < 1
				|| mbservers.get(0) == null)
			throw new Exception("Can't find the MBeanServer!");
	
		MBeanServer mbserver = mbservers.get(0);
		Set<ObjectName> objectNameSet = mbserver.queryNames(null, null);
	
		for (ObjectName objectName : objectNameSet) {
	
			if ("Catalina:type=Cluster".equals(objectName.getCanonicalName())) {
				JmxMBeanServer jmxMBeanServer = (JmxMBeanServer) mbserver;
				Class<JmxMBeanServer> jmxMBeanServerClass = JmxMBeanServer.class;
				Field[] fields = jmxMBeanServerClass.getDeclaredFields();
	
				for (Field field : fields) {
					if (!"mbsInterceptor".equals(field.getName()))
						continue;
	
					field.setAccessible(true);
					DefaultMBeanServerInterceptor mbInterceptor = (DefaultMBeanServerInterceptor) field
							.get(jmxMBeanServer);
					field.setAccessible(false);
					Class<DefaultMBeanServerInterceptor> mbInterceptorClass = DefaultMBeanServerInterceptor.class;
	
					Method[] methods = mbInterceptorClass.getDeclaredMethods();
					for (Method method : methods) {
						if (!"getMBean".equals(method.getName()))
							continue;
						method.setAccessible(true);
						Object beanModelObject = method.invoke(mbInterceptor,
								objectName);
						method.setAccessible(false);
						BaseModelMBean baseModelMBean = (BaseModelMBean) beanModelObject;
						simpleTcpCluster = (SimpleTcpCluster) baseModelMBean
								.getManagedResource();
					}
				}
			}
		}
		return simpleTcpCluster;
	}
%>

<%!

	@XmlRootElement(name = "project")
	static class Project {
		private Publishers publishers;
		private Scm scm;
		
		public Publishers getPublishers() {
			return publishers;
		}
	
		@XmlElement
		public void setPublishers(Publishers publishers) {
			this.publishers = publishers;
		}
		
		public Scm getScm() {
			return scm;
		}
	
		@XmlElement
		public void setScm(Scm scm) {
			this.scm = scm;
		}
	}
%>

<%!
	@XmlRootElement(name = "publishers")
	static class Publishers {
	
	private Mailer mailer;

	public Mailer getMailer() {
		return mailer;
	}

	@XmlElement
	public void setMailer(Mailer mailer) {
		this.mailer = mailer;
	}
}

%>

<%!

	@XmlRootElement(name = "mailer")
	static class Mailer {
		
		private String recipients;
	
		public String getRecipients() {
			return recipients;
		}
	
		@XmlElement
		public void setRecipients(String recipients) {
			this.recipients = recipients;
		}
	}

%>

<%!
	@XmlRootElement(name = "modulelocation")
	static class Modulelocation {
	
		private String remote;
	
		public String getRemote(){
			return remote;
		}
	
		@XmlElement
		public void setRemote(String remote) {
			this.remote = remote;
		}
	}

%>


<%!
	@XmlRootElement(name = "locations")
	static class Locations {
	
		private List<Modulelocation> modulelocations;
	
		public List<Modulelocation> getModulelocation() {
			return modulelocations;
		}
	
		@XmlElement
		public void setModulelocation(List<Modulelocation> modulelocations) {
			this.modulelocations = modulelocations;
		}
	}
%>


<%!
	@XmlRootElement(name = "scm")
	static class Scm {
	
	private Locations locations;

	public Locations getLocations() {
		return locations;
	}

	@XmlElement
	public void setLocations(Locations locations) {
		this.locations = locations;
	}
}

%>