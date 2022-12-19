<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.*'%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="cache-control" content="no-store" />
<meta http-equiv="cache-control" content="private" />
<meta http-equiv="cache-control" content="max-age=0, must-revalidate" />
<meta http-equiv="expires" content="now-1" />
<meta http-equiv="pragma" content="no-cache" />

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/prototype.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/date-validation.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/popup.js"></script>
<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
<link rel="stylesheet" type="text/css" href="${contextPath}/styles/deliciouslyblue/calendar.css" />

<meta name="title" content="IBFT Retry Advice History" />
<style>
h1 {
	display: block;
	padding: 20px 0px;
	margin: 0px !important;
	margin-bottom: 30px !important;
	font-size: 2.2em;
	/* font-size: 26px; */
	background-color: #ecf0f1;
	color: #f47b20;
	text-align: left;
}
</style>
</head>
<body bgcolor="#ffffff" onload="javascript:disableSubmit();">
	<ec:table items="ibftHistoryList" var="model" action="${contextPath}/p_ibftretryadvicehistory.html" title="" retrieveRowsCallback="limit"
		filterRowsCallback="limit" sortRowsCallback="limit" filterable="false" showPagination="false" sortable="false">
		<ec:row>
			<ec:column property="requestTime" title="Request Time" escapeAutoFormat="true" cell="date" format="dd/MM/yyyy hh:mm:ss a" />
			<ec:column property="mobileNo" title="Mobile No" escapeAutoFormat="true" />
			<ec:column property="accountNo" title="Account No" escapeAutoFormat="true" />
			<ec:column property="transactionAmount" title="Transaction Amount" escapeAutoFormat="true"  style="text-align: right"/>
			<ec:column property="createdOn" title="Transmission Time" escapeAutoFormat="true" cell="date" format="dd/MM/yyyy hh:mm:ss a" />
			<ec:column property="stan" title="Stan" escapeAutoFormat="true" />
			<ec:column property="retrievalReferenceNumber" title="RRN" escapeAutoFormat="true" />
			<ec:column property="transactionCode" title="Transaction ID" escapeAutoFormat="true" />
			<ec:column property="status" title="Status" escapeAutoFormat="true" />
		</ec:row>
	</ec:table>
</body>
</html>
