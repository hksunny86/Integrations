<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Ahmad Iqbal-->
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="decorator">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
<meta name="title" content="Retailer Transaction "/>
</head>
<body bgcolor="#ffffff">
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
  <form name="custtransform" method="post">
  <tr>
    <td width="17%" align="right" class="formText">Device Type Name:</td>
    <td width="24%" align="left">
      <spring:bind path="retailerTransactionListViewModel.deviceTypeName">
        <select name="${status.expression}" class="textBox">
          <option value="">---All---</option>
          <c:forEach items="${DeviceTypeModelList}" var="deviceTypeModelList">
            <option value="${deviceTypeModelList.name}" <c:if test="${status.value == deviceTypeModelList.name}">selected="selected"</c:if>>
            ${deviceTypeModelList.name}</option>          </c:forEach>
        </select>
        ${status.errorMessage}      </spring:bind>
    </td>
    <td width="18%" align="right" class="formText">Payment Type Name :</td>
    <td width="41%" align="left">
      <spring:bind path="retailerTransactionListViewModel.paymentTypeName">
        <select name="${status.expression}" class="textBox">
          <option value="">---All---</option>
          <c:forEach items="${PaymentModeModelList}" var="paymentModeModelList">
            <option value="${paymentModeModelList.name}" <c:if test="${status.value == paymentModeModelList.name}">selected="selected"</c:if>
            >
            ${paymentModeModelList.name}</option></c:forEach>
        </select>
        ${status.errorMessage}</spring:bind>
        <input type="submit" class="button" value="Search" name="_search" onclick="javascript:resetExportParameters('custtransform');"/>
    </td>
  </tr>



  </form>
</table>
<ec:table items="retailerTransactionModelList" var="retailerTransactionModel" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" action="${pageContext.request.contextPath}/retailertransactionmanagement.html">
  		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
			 <ec:exportXls fileName="Retailer Transaction.xls" tooltip="Export Excel"/>
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
			<ec:exportXlsx fileName="Retailer Transaction.xlsx" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
			<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Retailer Transaction" fileName="Retailer Transaction.pdf" tooltip="Export PDF"/>
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
			<ec:exportCsv fileName="Retailer Transaction.csv" tooltip="Export CSV"></ec:exportCsv>
		</authz:authorize>
  <ec:row>
    <ec:column property="retailerName" filterable="false"/>
    <ec:column property="productName" filterable="false"/>
    <ec:column property="paymentTypeName" filterable="false"/>
    <ec:column property="deviceTypeName" filterable="false"/>
    <ec:column property="fromRetContactName" filterable="false"/>
    <ec:column property="totalAmount" filterable="false"/>
  </ec:row>
</ec:table>

</body>
</html>