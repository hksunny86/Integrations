<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Ahmad Iqbal-->
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="decorator">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
<meta name="title" content="Retailer Commission "/>
</head>
<body bgcolor="#ffffff">
<form name="retailerCommissionForm" method="post">
<div align="right">
<input type="submit" class="button" value="Search" name="_search" onclick="javascript:resetExportParameters('custtransform');"/>
</div>
</form>
<c:if test="${not empty messages}">
  <div class="infoMsg" id="successMessages">
    <c:forEach var="msg" items="${messages}">
      <c:out value="${msg}" escapeXml="false"/>
      <br/>
    </c:forEach>
  </div>
  <c:remove var="messages" scope="session"/>
</c:if>

<ec:table items="retailerCommissionModelList" var="retailerCommissionModel" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" action="${pageContext.request.contextPath}/retailercommissionmanagement.html">
  			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Retailer Commission.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Retailer Commission.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Retailer Commission" fileName="Retailer Commission.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Retailer Commission.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
  <ec:row>
    <ec:column property="name" filterable="false"/>
    <ec:column property="commAmount" filterable="false"/>

  </ec:row>
</ec:table>

</body>
</html>
