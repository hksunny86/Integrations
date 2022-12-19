<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Asad Hayat-->
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<meta name="decorator" content="decorator">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
<meta name="title" content="Switch Finder " />
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
<div align="right"><a href="switchfinderform.html" class="linktext">Add Switch Finder</a><br>&nbsp;&nbsp;</div>
<ec:table
items="switchFinderModelList"
var = "switchFinderModel"
retrieveRowsCallback="limit"
filterRowsCallback="limit"
sortRowsCallback="limit"
action="${pageContext.request.contextPath}/switchfindermanagement.html"
>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Switch Finder.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Switch Finder.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Switch Finder" fileName="Switch Finder.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Switch Finder.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
<ec:row>
  <ec:column property="switchName" title="Switch Finder"/>
  <ec:column property="bankName" title="Bank"/>
  <ec:column property="paymentModeName" title="Payment Mode"/>
  <ec:column property="switchFinderId" title="Edit" filterable="false" sortable="false" viewsDenied="xls" style="text-align:center;">
      <a href="${pageContext.request.contextPath}/switchfinderform.html?switchFinderId=${switchFinderModel.switchFinderId}">Edit</a>
  </ec:column>
</ec:row>
</ec:table>
</body>




</html>
