<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.*'%>
<c:choose>
	<c:when test="${!empty status}">
		<c:set var="_status" value="${status}" />
	</c:when>
	<c:otherwise>
		<c:set var="_status" value="${param.status}" />
	</c:otherwise>
</c:choose>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="cache-control" content="no-store" />
<meta http-equiv="cache-control" content="private" />
<meta http-equiv="cache-control" content="max-age=0, must-revalidate" />
<meta http-equiv="expires" content="now-1" />
<meta http-equiv="pragma" content="no-cache" />
<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css" type="text/css" />
<meta name="title" content="Account Closing" />
<style>
.header {
	background-color: #6699CC; /*308dbb original*/
	color: white;
	font-family: verdana, arial, helvetica, sans-serif;
	/*verdana, arial, helvetica, sans-serif*/
	font-size: 11px;
	font-weight: bold;
	text-align: center;
	padding-right: 3px;
	padding-left: 3px;
	padding-top: 4px;
	padding-bottom: 4px;
	margin: 0px;
	border-right-style: solid;
	border-right-width: 1px;
	border-color: white;
}
</style>
</head>
<body bgcolor="#ffffff">
	<center>
	<h3 class="header" id="logHeader">Applicant Information is updated to Account Screen, press OK to close this window.</h3>
	<br />
	<button onclick="window.close();">Ok</button>
	</center>
</body>
</html>
