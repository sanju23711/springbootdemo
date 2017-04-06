<%@page import="com.thg.hrws.setup.environment.Environment"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Hello world page</title>
</head>
<body>
	<c:out value="${message}"></c:out>
    <h1> ${message} Environment.INSTANCE.isBoot() <%= Environment.INSTANCE.isBoot()%></h1>
</body>
</html>