<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Ahmad Iqbal-->

<%@include file="/common/taglibs.jsp"%>
<html>

<head>
<meta name="decorator" content="decorator">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/extremecomponents.css"
	type="text/css">
<meta name="title" content="Audit Log" />
<script type="text/javascript">
    function openAuditLogDetailWindow(auditLogId)
    {
         var popupWidth = 550;
		 var popupHeight = 350;
		 var popupLeft = (window.screen.width - popupWidth)/2;
		 var popupTop = (window.screen.height - popupHeight)/2;
         newWindow=window.open('auditlogdetail.html?auditLogId='+auditLogId,'AuditLogDetail','width='+popupWidth+',height='+popupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
         if(window.focus) newWindow.focus();
               return false;
	}
	function openActionLogDetailWindow(actionLogId)
    {
        var popupWidth = 550;
		var popupHeight = 350;
		var popupLeft = (window.screen.width - popupWidth)/2;
		var popupTop = (window.screen.height - popupHeight)/2;
        var action = 'actionlogdetail.html?actionLogId='+actionLogId;
        newWindow = window.open( action , 'ActionLogDetail','width='+popupWidth+',height='+popupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
        if(window.focus) newWindow.focus();
              return false;
    }
</script>
</head>

<body bgcolor="#ffffff">

<c:if test="${not empty messages}">
    <div class="infoMsg" id="successMessages">
        <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session"/>
</c:if>
<ec:table items="auditLogList" var="auditLogListViewModel"
	retrieveRowsCallback="limit" filterRowsCallback="limit"
	sortRowsCallback="limit"
	action="${pageContext.request.contextPath}/auditlog.html" title="">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Audit Log.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Audit Log.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Audit Log" fileName="Audit Log.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Audit Log.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
	
	<ec:row>
		<ec:column property="auditLogId" title="Audit Log ID" filterable="false" escapeAutoFormat="true"/>
		<ec:column property="actionLogId" title="Action Log ID" escapeAutoFormat="true">
		    <a href="${pageContext.request.contextPath}/actionlogdetail.html?actionLogId=${auditLogListViewModel.actionLogId}" onclick="return openActionLogDetailWindow(${auditLogListViewModel.actionLogId})"><c:out value="${auditLogListViewModel.actionLogId}"/></a>
		</ec:column>
		<ec:column property="code" title="Transaction Code" escapeAutoFormat="true"/>
		<ec:column property="name" title="Integration Module" />
		<ec:column property="transactionStartTime" filterable="false" title="Start Time" cell="date" format="dd/MM/yyyy hh:mm a"/>
		<ec:column property="transactionEndTime" filterable="false" title="End Time" cell="date"  format="dd/MM/yyyy hh:mm a"/>
        <ec:column alias=" "  title="Detail " sortable="false" filterable="false" style="text-align:center;">
			<a href="${pageContext.request.contextPath}/auditlogdetail.html?auditLogId=${auditLogListViewModel.auditLogId}" onclick="return openAuditLogDetailWindow(${auditLogListViewModel.auditLogId})">Detail</a>						
        </ec:column>


	</ec:row>
</ec:table>


</body>


</html>











































































