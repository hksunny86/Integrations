<%@ include file="/common/taglibs.jsp" %>
<html>
<head>
<meta name="decorator" content="decorator">
<title>Framework Checked Exception Occurred</title>
</head>
<body>
<p>
    ${requestScope.exception.message}
</p>


<%--
Exception ex = (Exception) request.getAttribute("exception");
ex.printStackTrace(new java.io.PrintWriter(out));
--%>
</body>
</html>
