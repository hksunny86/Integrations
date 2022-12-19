
<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Jalil-Ur-Rehman-->
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<meta name="decorator" content="decorator">
 <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
<meta name="title" content="MNO Dialing Code "/>
</head>
<body bgcolor="#ffffff">
   <div align="right"><a href="mnodialingcodeform.html" class="linktext"> Add MNO Dialing Code </a><br>&nbsp;&nbsp;</div>
<c:if test="${not empty messages}">
  <div class="infoMsg" id="successMessages">
    <c:forEach var="msg" items="${messages}">
      <c:out value="${msg}" escapeXml="false"/>
      <br/>
    </c:forEach>
  </div>
  <c:remove var="messages" scope="session"/>
</c:if>
<ec:table 
items="mnoDialingCodeModelList" 
var="mnoDialingCodeListViewModel" 
retrieveRowsCallback="limit"
filterRowsCallback="limit"
sortRowsCallback="limit"
action="${pageContext.request.contextPath}/mnodialingcodemanagement.html"
title="">
  		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
			<ec:exportXls fileName="MNO Dialing Code.xls" tooltip="Export Excel"/>
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
			<ec:exportXlsx fileName="MNO Dialing Code.xlsx" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
			<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="MNO Dialing Code" fileName="MNO Dialing Code.pdf" tooltip="Export PDF"/>
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
			<ec:exportCsv fileName="MNO Dialing Code.csv" tooltip="Export CSV"></ec:exportCsv>
		</authz:authorize>
  <ec:row>
    <ec:column property="name"  title="Service Operator"  />
    <ec:column property="code"  title="Dialing Code"  />
    <ec:column property="description"  title="Description"  />

  </ec:row>
</ec:table>

</body>
</html>
