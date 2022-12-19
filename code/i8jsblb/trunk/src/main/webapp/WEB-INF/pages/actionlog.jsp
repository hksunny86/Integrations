<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Ahmad Iqbal1-->

<%@include file="/common/taglibs.jsp"%>
<html>

	<head>
<meta name="decorator" content="decorator">
		<link rel="stylesheet"
			href="${pageContext.request.contextPath}/styles/extremecomponents.css"
			type="text/css">
		<meta name="title" content="Action Log" /> 
        <script type="text/javascript">
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
					<c:out value="${msg}" escapeXml="false" />
					<br />
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>
			
			<ec:table 
				items="actionLogList" 
				var="actionLogListViewModel" 
				retrieveRowsCallback="limit" 
				filterRowsCallback="limit" 
				sortRowsCallback="limit" 
				action="${pageContext.request.contextPath}/actionlog.html"
				title="">
				<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
					<ec:exportXls fileName="Action Log.xls" tooltip="Export Excel" />
				</authz:authorize>
				<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
					<ec:exportXlsx fileName="Action Log.xlsx" tooltip="Export Excel" />
				</authz:authorize>
				<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
					<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="Action Log" fileName="Action Log.pdf" tooltip="Export PDF"/>
				</authz:authorize>
				<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
					<ec:exportCsv fileName="Action Log.csv" tooltip="Export CSV"></ec:exportCsv>
				</authz:authorize>
				<ec:row>
    				<ec:column property="actionLogId" filterable="false" title="Action Log ID" escapeAutoFormat="true"/>   						   						 
    				<ec:column property="deviceUserId" filterable="true" title="Inov8 MWallet ID" escapeAutoFormat="true"/>
    				<ec:column property="userName" filterable="true" title="User Name"/>
    				<ec:column property="usecaseName" filterable="true" title="Use Case Name"/>
    				<ec:column property="startTime" filterable="false" title="Start Time" cell="date" format="dd/MM/yyyy hh:mm a"/>   				
    				<ec:column property="endTime" filterable="false" cell="date"  format="dd/MM/yyyy hh:mm a" title="End Time"/>   						   						 
    				<ec:column alias=" "  title="Detail " sortable="false" filterable="false" style="text-align:center;">
						<a href="${pageContext.request.contextPath}/actionlogdetail.html?actionLogId=${actionLogListViewModel.actionLogId}" onclick="return openActionLogDetailWindow(${actionLogListViewModel.actionLogId})">Detail</a>						
    				
    				</ec:column>
      			
      			</ec:row>		
      		</ec:table>	


	</body>
    

</html>











































































