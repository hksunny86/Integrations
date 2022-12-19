<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Asad Hayat-->
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<meta name="decorator" content="decorator">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
<meta name="title" content="Service Operator" />
<script type="text/javascript" src="<c:url value='/scripts/activatedeactivate.js'/>"></script>
	<script type="text/javascript">
		function actdeact(request)
		{
			isOperationSuccessful(request);
		}
	</script>
</head>
<body bgcolor="#ffffff">
	<div id="successMsg" class ="infoMsg" style="display:none;"></div>
	<c:if test="${not empty messages}">
	    <div class="infoMsg" id="successMessages">
	        <c:forEach var="msg" items="${messages}">
	            <c:out value="${msg}" escapeXml="false"/><br />
	        </c:forEach>
	    </div>
	    <c:remove var="messages" scope="session"/>
	</c:if>
<div align="right"><a href="mnoform.html" class="linktext">Add Service Operator</a><br>&nbsp;&nbsp;</div>
	<ec:table
		items="mnoModelList"
                var = "mnoModel"
                retrieveRowsCallback="limit"
                filterRowsCallback="limit"
                sortRowsCallback="limit"
		action="${pageContext.request.contextPath}/mnomanagement.html"
                >
            <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Service Operator.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Service Operator.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Service Operator" fileName="Service Operator.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Service Operator.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>
                  <ec:column property="name" title="Service Operator"/>
                  <ec:column property="contactName" title="Contact Name"/>
                  <ec:column property="phoneNo" title="Phone No" escapeAutoFormat="true"/>
                  <ec:column property="email" title="Email"/>
                  <ec:column  property="mnoId" title="Edit" alias="edit" filterable="false" sortable="false" width="30%" viewsDenied="xls" style="text-align:center;">
                          <a href="${pageContext.request.contextPath}/mnoform.html?mnoId=${mnoModel.mnoId}">Edit</a>
                  </ec:column>
                  <ec:column  property="mnoId" title="Deactivate" filterable="false" sortable="false" viewsDenied="xls">
                       <%--   <a href="javascript:confirmUpdateStatus('${pageContext.request.contextPath}/mnomanagement.html?&_setActivate=${!mnoModel.active}&mnoId=${mnoModel.mnoId}')"><c:if test="${mnoModel.active}">Deactivate</c:if><c:if test="${!mnoModel.active}">Activate</c:if></a> --%>

					<tags:activatedeactivate id="${mnoModel.mnoId}"
						model="com.inov8.microbank.common.model.MnoModel"
						property="active" propertyValue="${mnoModel.active}"
						callback="actdeact" error="defaultError" />
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
