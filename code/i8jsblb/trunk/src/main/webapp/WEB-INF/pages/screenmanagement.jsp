<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Ahmad Iqbal-->
<%@include file="/common/taglibs.jsp"%>

<html>
<head>
<meta name="decorator" content="decorator">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
<meta name="title" content="Service " />
</head>
<body bgcolor="#ffffff">
<div align="right"><a href="screenform.html" class="linktext">Add Screen</a>&nbsp;&nbsp;</div>
<c:if test="${not empty messages}">
    <div class="infoMsg" id="successMessages">
        <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session"/>
</c:if>
<table width="85%" >
<form name="screenForm" method="post">

<tr>
    <td class="formText" align="right">Product : </td>
    <td align="left">
      <spring:bind path="screenListViewModel.productName">
        <select name="${status.expression}" class="textBox">
          <option value="">---All---</option>
          <c:forEach items="${ProductModelList}" var="productModelList">
            <option value="${productModelList.name}"
            <c:if test="${status.value == productModelList.name}">selected="selected"</c:if>>
              ${productModelList.name}
            </option>
          </c:forEach>
        </select>
        ${status.errorMessage}
      </spring:bind>
    </td>
    <td class="formText" align="center">Action : </td>
    <td align="left" >
      <spring:bind path="screenListViewModel.actionName">
        <select name="${status.expression}" class="textBox">
          <option value="">---All---</option>
          <c:forEach items="${ActionModelList}" var="actionModelList">
            <option value="${actionModelList.name}"
            <c:if test="${status.value == actionModelList.name}">selected="selected"</c:if>>
              ${actionModelList.name}
            </option>
          </c:forEach>
        </select>
        ${status.errorMessage}
      </spring:bind>
      <input type="submit"  class="button" value="Search" name="_search" onclick="javascript:resetExportParameters('screenForm');"/>
    </td>
</tr>
</form>
</table>
<ec:table
items="screenList"
var = "screenModel"
retrieveRowsCallback="limit"
filterRowsCallback="limit"
sortRowsCallback="limit"
action="${pageContext.request.contextPath}/screenmanagement.html"
title=""
>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
			<ec:exportXls fileName="Service.xls" tooltip="Export Excel"/>
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
			<ec:exportXlsx fileName="Service.xlsx" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
			<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Service" fileName="Service.pdf" tooltip="Export PDF"/>
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
			<ec:exportCsv fileName="Service.csv" tooltip="Export CSV"></ec:exportCsv>
		</authz:authorize>
<ec:row>
  <ec:column property="name" filterable="false"/>
  <ec:column property="productName" filterable="false"/>
  <ec:column property="actionName" filterable="false"/>
  <ec:column property="description" filterable="false"/>
  <c:if test="${screenModel.active}">
    <ec:column property="active" value="Active" alias="Status" filterable="false"/>
  </c:if>
  <c:if test="${!screenModel.active}">
    <ec:column property="active" value="Inactive" alias="Status" filterable="false"/>
  </c:if>
  <ec:column  property="screenId" title="Edit" filterable="false" sortable="false" viewsDenied="xls">
    <a href="${pageContext.request.contextPath}/screenform.html?screenId=${screenModel.screenId}">Edit</a>
  </ec:column>
  <ec:column  property="screenId" title="Deactivate" filterable="false" sortable="false" viewsDenied="xls">
    <a href="javascript:confirmUpdateStatus('${pageContext.request.contextPath}/screenmanagement.html?&_setActivate=${!screenModel.active}&screenId=${screenModel.screenId}');"><c:if test="${screenModel.active}">Deactivate</c:if><c:if test="${!screenModel.active}">Activate</c:if></a>
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

