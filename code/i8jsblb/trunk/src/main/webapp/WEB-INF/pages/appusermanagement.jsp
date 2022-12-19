<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Jawwad Farooq-->
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<meta name="decorator" content="decorator">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
<meta name="title" content="AppUser " />
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

<ec:table
items="appUserModelList"
var = "appUserModel"
retrieveRowsCallback="limit"
filterRowsCallback="limit"
sortRowsCallback="limit"
action="${pageContext.request.contextPath}/appusermanagement.html"
>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="AppUser.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="AppUser.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="AppUser" fileName="AppUser.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="AppUser.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
<ec:row>
  <ec:column property="firstName" />  
  <ec:column property="lastName"/>
  <ec:column property="appUserTypeName"/>
  <ec:column property="username"/>
  <ec:column property="mobileNo"/>
  <ec:column property="accountEnabled" filterable="false" sortable="false"/>
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
