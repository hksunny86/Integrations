<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Asad Hayat-->
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<meta name="decorator" content="decorator">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
<meta name="title" content="Channel " />
<script type="text/javascript" src="<c:url value='/scripts/activatedeactivate.js'/>"></script>
	<script type="text/javascript">
		function actdeact(request)
		{
			isOperationSuccessful(request);
		}
	</script>
</head>
<body bgcolor="#ffffff">
<div align="right"><a href="devicetypeform.html" class="linktext"> Add Channel </a><br>&nbsp;&nbsp;</div>
<c:if test="${not empty messages}">
    <div class="infoMsg" id="successMessages">
        <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session"/>
</c:if>

<ec:table
items="deviceTypeModelList"
var = "deviceTypeModel"
retrieveRowsCallback="limit"
filterRowsCallback="limit"
sortRowsCallback="limit"
action="${pageContext.request.contextPath}/devicetypemanagement.html"
>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
			<ec:exportXls fileName="Channel.xls" tooltip="Export Excel"/>
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
			<ec:exportXlsx fileName="Channel.xlsx" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
			<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Channel" fileName="Channel.pdf" tooltip="Export PDF"/>
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
			<ec:exportCsv fileName="Channel.csv" tooltip="Export CSV"></ec:exportCsv>
		</authz:authorize>

<ec:row>
  <ec:column property="name" title="Channel Name" />
  <ec:column property="description" />
  <ec:column  property="deviceTypeId" title="Deactivate" filterable="false" sortable="false" viewsDenied="xls" width="10">
             <%-- <a href="javascript:confirmUpdateStatus('${pageContext.request.contextPath}/devicetypemanagement.html?&_setActivate=${!deviceTypeModel.active}&deviceTypeId=${deviceTypeModel.deviceTypeId}');"><c:if test="${deviceTypeModel.active}">Deactivate</c:if><c:if test="${!deviceTypeModel.active}">Activate</c:if></a> --%>
             
             
			
			<tags:activatedeactivate 
			id="${deviceTypeModel.deviceTypeId}" 
			model="com.inov8.microbank.common.model.DeviceTypeModel" 
			property="active"
			propertyValue="${deviceTypeModel.active}"
			callback="actdeact"
			error="defaultError"
			/>
             
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
