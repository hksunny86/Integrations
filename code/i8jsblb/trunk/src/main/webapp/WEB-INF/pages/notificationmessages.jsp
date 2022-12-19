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
		<meta name="title" content="Notification Message" />

	</head>

	<body bgcolor="#ffffff">
			

		   <div align="right"><a href="notificationmessageform.html" class="linktext"> Add Notification Message </a><br>&nbsp;&nbsp;</div>

			
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
				items="notificationmessageList" 
				var="notificationMessageListViewModel" 
				retrieveRowsCallback="limit" 
				filterRowsCallback="limit" 
				sortRowsCallback="limit" 
				action="${pageContext.request.contextPath}/notificationmessages.html"
				title="">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Notification Message.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Notification Message.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Notification Message" fileName="Notification Message.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Notification Message.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
				<ec:row>
    				<ec:column property="name" filterable="true" title="Message Type"/>
    				<ec:column property="smsMessageText" filterable="true" title="Notification Message"/>
    				<ec:column property="emailMessageText"  filterable="true" title="Email Message"/>
    				<ec:column alias=" " filterable="false"  title="Edit" sortable="false" viewsDenied="xls">
						<a href="${pageContext.request.contextPath}/notificationmessageform.html?mesId=${notificationMessageListViewModel.notificationMessageId}">Edit</a>						
				</ec:column>
      			</ec:row>		
      		</ec:table>	


	</body>
		

</html>











































































