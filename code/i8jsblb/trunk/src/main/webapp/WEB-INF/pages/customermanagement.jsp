
<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Asad Hayat-->
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<meta name="decorator" content="decorator">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
<meta name="title" content="Customer "/>
<script type="text/javascript" src="<c:url value='/scripts/activatedeactivate.js'/>"></script>
	<script type="text/javascript">
		function actdeact(request)
		{
			isOperationSuccessful(request);
		}
	</script>
</head>
<body bgcolor="#ffffff">

   <div align="right"><a href="customerform.html" class="linktext"> Add Customer </a><br>&nbsp;&nbsp;</div>
   <div id="successMsg" class ="infoMsg" style="display:none;"></div>
<c:if test="${not empty messages}">
  <div class="infoMsg" id="successMessages">
    <c:forEach var="msg" items="${messages}">
      <c:out value="${msg}" escapeXml="false"/>
      <br/>
    </c:forEach>
  </div>
  <c:remove var="messages" scope="session"/>
</c:if>
<table width="85%">
  <form name="customerForm" method="post">



  </form>
</table>
<ec:table items="customerModelList" var="customerModel" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" action="${pageContext.request.contextPath}/customermanagement.html">
  	<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
		<ec:exportXls fileName="Customer.xls" tooltip="Export Excel"/>
	</authz:authorize>
	<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
		<ec:exportXlsx fileName="Customer.xlsx" tooltip="Export Excel" />
	</authz:authorize>
	<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
		<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
			headerTitle="Customer" fileName="Customer.pdf" tooltip="Export PDF"/>
	</authz:authorize>
	<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
		<ec:exportCsv fileName="Customer.csv" tooltip="Export CSV"></ec:exportCsv>
	</authz:authorize>
  <ec:row>
    <ec:column property="username"  title="User Name"/>
     <ec:column property="firstName"  title="First Name"/>
    <ec:column property="lastName"  title="Last Name"/>
    <ec:column property="mobileNo"  title="Mobile No" escapeAutoFormat="true"/>
    <ec:column property="email"  title="Email"/>
     <ec:column property="customerId" title="Edit" filterable="false" sortable="false" escapeAutoFormat="true" viewsDenied="xls">
      <a href="${pageContext.request.contextPath}/customerform.html?customerId=${customerModel.customerId}">Edit</a>
    </ec:column>
    
    		<ec:column  property="active" title="Deactivate" filterable="false" sortable="false" viewsDenied="xls">
    <%-- <a href="javascript:confirmUpdateStatus('${pageContext.request.contextPath}/customermanagement.html?&_setActivate=${!customerModel.accountEnabled}&customerId=${customerModel.customerId}')">
    <c:if test="${customerModel.accountEnabled}">Deactivate</c:if>
    <c:if test="${!customerModel.accountEnabled}">Activate</c:if>
    </a> --%>
    <tags:activatedeactivate 
			id="${customerModel.appUserId}" 
			model="com.inov8.microbank.common.model.AppUserModel" 
			property="accountEnabled"
			propertyValue="${customerModel.accountEnabled}"
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
