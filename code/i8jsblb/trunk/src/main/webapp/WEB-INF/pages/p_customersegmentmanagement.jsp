<!--Title: i8Microbank-->
<!--Author: Hassan Javaid-->

<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.*,com.inov8.microbank.common.util.PortalConstants'%>
<html>
	<head>
<meta name="decorator" content="decorator">
		<link rel="stylesheet"
			href="${pageContext.request.contextPath}/styles/extremecomponents.css"
			type="text/css">
		<meta name="title" content="Customer Segment" />
		<script type="text/javascript" src="<c:url value='/scripts/activatedeactivate.js'/>"></script>
	<script type="text/javascript">
		function actdeact(request)
		{
			isOperationSuccessful(request);
		}
	</script>
	<%
		String createPermission = PortalConstants.MNG_CUST_SEGMENT_CREATE;
		createPermission +=	"," + PortalConstants.PG_GP_CREATE;
		createPermission +=	"," + PortalConstants.ADMIN_GP_CREATE;

		String updatePermission =	PortalConstants.MNG_CUST_SEGMENT_UPDATE;
		updatePermission +=	"," + PortalConstants.PG_GP_UPDATE;	
		updatePermission +=	"," + PortalConstants.ADMIN_GP_UPDATE;
	 %>
	
	
	</head>
	<body bgcolor="#ffffff">
	
	<div id="successMsg" class ="infoMsg" style="display:none;"></div>
		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" />
					<br />
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>
		
	<authz:authorize ifAnyGranted="<%=createPermission %>">
    <div align="right"><a href="p_customersegmentform.html?actionId=1" class="linktext"> Add Customer Segment </a><br>&nbsp;&nbsp;</div>
	</authz:authorize>
		<ec:table items="cutomerSegmentModelList" var="segmentModel"
			retrieveRowsCallback="limit" filterRowsCallback="limit"
			sortRowsCallback="limit"
			action="${contextPath}/p_customersegmentmanagement.html"
			title="" width="80%">
			
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Customer Segment.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Customer Segment.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Customer Segment" fileName="Customer Segment.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Customer Segment.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>
				<ec:column property="name" title="Account Type" width="20%"/>
				<ec:column property="description" title="Description" width="50%" style="word-break: break-all;"/>
				<ec:column property="segmentId" title="Edit" filterable="false" escapeAutoFormat="true" sortable="false" viewsAllowed="html" style="text-align:center;" width="15%">
				<authz:authorize ifAnyGranted="<%=updatePermission %>">			
					<a href="${contextPath}/p_customersegmentform.html?segmentId=${segmentModel.segmentId}">Edit</a>		
				</authz:authorize>
				<authz:authorize ifNotGranted="<%=updatePermission %>">
					<input type="button" class="button" style="width='90px'" value="Edit" disabled="disabled" />		
				</authz:authorize>
  				</ec:column>
  				<ec:column  property="isActive" title="Deactivate" filterable="false" sortable="false" viewsAllowed="html" style="text-align:center;" width="15%">
  				<authz:authorize ifAnyGranted="<%=updatePermission %>">
    			<tags:activatedeactivate 
					id="${segmentModel.segmentId}" 
					model="com.inov8.microbank.common.model.SegmentModel" 
					property="isActive"
					propertyValue="${segmentModel.isActive}"
					callback="actdeact"
					error="defaultError"
				/>
				</authz:authorize>
				<authz:authorize ifNotGranted="<%=updatePermission %>">
				<c:choose>
				<c:when test="${segmentModel.isActive}"><input id="btn_deAct"type="button" class="button" style="width='90px';"value="Deactivate" disabled="disabled" /></c:when>
				<c:otherwise><input id="btn_act"type="button" class="button" style="width='90px';"value="Activate" disabled="disabled" /></c:otherwise>
				</c:choose>
				</authz:authorize>
  				</ec:column>  

			</ec:row>
		</ec:table>

		<script language="javascript" type="text/javascript">
		function confirmUpdateStatus(link)
		{
		    if (confirm('Are you sure you want to update status?')==true)
		    {
		      window.location.href=link;
		    }
		}
		</script>
	</body>
</html>
