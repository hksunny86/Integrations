
<%@page import="com.inov8.microbank.common.util.PortalConstants"%>
<%@include file="/common/taglibs.jsp"%>

<html>
	<head>
<meta name="decorator" content="decorator">
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
		<link rel="stylesheet" type="text/css"
			href="${pageContext.request.contextPath}/styles/deliciouslyblue/calendar.css" />
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/prototype.js"></script>
		<script type="text/javascript" 
			src="${contextPath}/scripts/date-validation.js"></script>

		<link rel="stylesheet"
			href="${pageContext.request.contextPath}/styles/extremecomponents.css"
			type="text/css">
		<meta name="title" content="Agent Retailer Transaction Report" />
		<%@include file="/common/ajax.jsp"%>
	</head>
	<table>
		<form name="allpayretailerreportform"
			action="p_allpayretailerreport.html?actionId=2" method="post">
		<tr>
			<td align="right" class="formText">
				From Date:
			</td>
			<td align="left">
				<spring:bind path="allpayRetTransViewModel.startDate">
					<input name="${status.expression }" id="${status.expression}"
						class="textBox" value="${status.value}" />
				</spring:bind>
				<img id="sDate" tabindex="2" readonly="true" name="sDate"
					align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
			</td>
		</tr>
		<tr>
			<td class="formText" align="right">
				To Date:
			</td>
			<td align="left">
			<spring:bind path="allpayRetTransViewModel.endDate">
				<input name="${status.expression }" id="${status.expression}"
						class="textBox" value="${status.value}" />
			</spring:bind>
				<img id="eDate" tabindex="2" readonly="true" name="eDate"
					align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
			</td>
		</tr>
		<tr>
			<td class="formText" align="right">

				Transaction Code:
			</td>
			<td align="left">
				<spring:bind path="allpayRetTransViewModel.code">
					<input name="${status.expression }" id="${status.expression }"
						value="${status.value }" class="textBox" />
				</spring:bind>
			</td>
		</tr>
		<tr>
			<td class="formText" align="right"></td>
			<td align="left">

			</td>
		</tr>
		<tr>
			<td class="formText" align="right"></td>
			<td align="left">

			</td>
		</tr>
		<tr>
		<td></td>
			<td>
				<input type="submit" value="Search" />
			</td>
		</tr>
		</form>
	</table>

	<ec:table filterable="false" items="transList" var="transList"
		retrieveRowsCallback="limit" filterRowsCallback="limit"
		sortRowsCallback="limit"
		action="${pageContext.request.contextPath}/p_allpayretailerreport.html">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Agent Retailer Transaction Report.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Agent Retailer Transaction Report.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Agent Retailer Transaction Report" fileName="Agent Retailer Transaction Report.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Agent Retailer Transaction Report.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
		<ec:row>
			<ec:column property="code" title="Transaction Code" filterable="true" escapeAutoFormat="true" />
			<ec:column property="txDate" title="Transaction Date"
				filterable="true" format="dd/MM/yyyy" cell ="date"/>
			<ec:column property="txTime" title="Transaction Time"
				filterable="true" sortable="true" format="dd/MM/yyyy"  />
			<ec:column property="totalAmount" title="Total Amount"
				sortable="true" filterable="true" escapeAutoFormat="true" />
			<ec:column property="productId" title="Type Of Bill"
				filterable="true" escapeAutoFormat="true" />
			<ec:column property="customerId" title="Customer Id"
				filterable="true" escapeAutoFormat="true" />
			<ec:column property="retailerId" title="Retailer Name"
				filterable="true" escapeAutoFormat="true" />
			<ec:column property="retailerLocation" title="Retailer Location"
				filterable="true" escapeAutoFormat="true" />
			<ec:column property="allpayId" title="Agent Id" filterable="true"
				escapeAutoFormat="true" />
			<ec:column property="username" title="Username" filterable="true"
				escapeAutoFormat="true" />
		</ec:row>
	</ec:table>

	<script type="text/javascript">
		Calendar.setup(
      {
        inputField  : "startDate", // id of the input field
        button      : "sdate"    // id of the button
      }
      );

      Calendar.setup(
      {
        inputField  : "endDate", // id of the input field
        button      : "edate",    // id of the button
        isEndDate: true
      }
      );
      
      </script>
</html>
