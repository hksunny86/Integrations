
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

		<link rel="stylesheet"
			href="${pageContext.request.contextPath}/styles/extremecomponents.css"
			type="text/css">
		<meta name="title" content="Retailer Report 2" />
		<%@include file="/common/ajax.jsp"%>
	</head>
<table>
		<form name="p_allpaybillsummaryreport"
			action="p_allpaybillsummaryreport.html?actionId=2" method="post">
		<tr>
			<td align="right" class="formText">
				Date:
			</td>
			<td align="left">
				<spring:bind path="retailerBillSummaryViewModel.txDate">
					<input name="${status.expression }" id="${status.expression}"
						class="textBox" value="${status.value}" />
				</spring:bind>
				<img id="sdate" tabindex="2" readonly="true" name="tdate"
					align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
			</td>
		</tr>
		<tr>
			<td class="formText" align="right">
			
			</td>
			<td align="left">
			
			</td>
		</tr>
		<tr>
			<td class="formText" align="right">
			
			</td>
			<td align="left">
				
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
		sortRowsCallback="limit" showPagination="false"
		action="${pageContext.request.contextPath}/p_allpaybillsummaryreport.html">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Retailer Report 2.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Retailer Report 2.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Retailer Report 2" fileName="Retailer Report 2.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Retailer Report 2.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
		<ec:row>
			
			<ec:column property="txDate" title="Transaction Date"
				filterable="true" format="dd/MM/yyyy" /> 
			<ec:column property="userId" title="AllPay ID"
				filterable="true" escapeAutoFormat="true" />
			<ec:column property="username" title="Retailer CSC"
				filterable="true" />
			<ec:column property="retailerContactName" title="Retailer Name"
				filterable="true" />
			
			<ec:column property="name" title="Retailer Location" filterable="true" />
			
			<ec:column property="txCount" title="Total Bills Submitted"
				filterable="true" />
			<ec:column property="amountSubmitted" title="Total Amount Submitted"
				filterable="true" />
			
			<ec:column property="startDayBalance" title="Available Balance at the start of the day" filterable="true" sortable="false" />
			<ec:column property="endDayBalance" title="Available Balance at the end of the day" filterable="true"  sortable="false" />
			
		</ec:row>
	</ec:table>

	<script type="text/javascript">
		  Calendar.setup(
      {
        inputField  : "txDate", // id of the input field
        ifFormat    : "%d/%m/%Y",      // the date format
        button      : "tdate"    // id of the button
      }
      );
/*
      Calendar.setup(
      {
        inputField  : "endDateI", // id of the input field
        ifFormat    : "%d/%m/%Y",      // the date format
        button      : "edate"    // id of the button
      }
      );
      */
      </script>
</html>
