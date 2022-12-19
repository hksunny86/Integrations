
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
		<meta name="title" content="Retailer Head Report" />
		<%@include file="/common/ajax.jsp"%>
	</head>
	<body>
  <form name="customerForm" method="post">



  </form>
  
	<ec:table filterable="false" items="transList" var="transList"
		retrieveRowsCallback="limit" filterRowsCallback="limit"
		sortRowsCallback="limit" showPagination="false"
		action="${pageContext.request.contextPath}/p_allpayretailerheadreport.html">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Retailer Head Report.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Retailer Head Report.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Retailer Head Report" fileName="Retailer Head Report.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Retailer Head Report.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
		<ec:row>
			<ec:column property="txDate" title="Transaction Date" filterable="true" format="dd/MM/yyyy" />
				<ec:column property="headUserId" title="Retailer Head Id" filterable="true" escapeAutoFormat="true" />
			<ec:column property="startDayBalance" filterable="false" title="Balance at the start of the day" format="###,###,###"/>
			<ec:column property="amountDisbursed" filterable="false" title="Balance disbursed in a day"/>
			<ec:column property="balance" filterable="false" title="Available balance" sortable="false"/>
				
			
			<ec:column property="retailerUserId" title="Retailer Id" filterable="true" escapeAutoFormat="true" />
			<ec:column property="username" title="Retailer CSC" filterable="true" escapeAutoFormat="true" />
			
			<ec:column property="amountDisbursed" title="Amount Transfered to Retailer" filterable="true" />		
			<ec:column property="noOfTx" title="Retailer total transactions for the day" filterable="true" format="###,###,###" />
			<ec:column property="amountCollected" title="Retailer Total Amount Collected for the day" filterable="true" />		
			<ec:column property="retStartDayBalance" title="Retailer Balance at the start of the day"	filterable="true" sortable="false" />
			<ec:column property="retEndDayBalance" title="Retailer Balance at the end of the day"	filterable="true" sortable="false" />
		</ec:row>
	</ec:table>

	<script type="text/javascript">
	/*	  Calendar.setup(
      {
        inputField  : "transDate", // id of the input field
        ifFormat    : "%d/%m/%Y",      // the date format
        button      : "sdate"    // id of the button
      }
      );

      Calendar.setup(
      {
        inputField  : "endDateI", // id of the input field
        ifFormat    : "%d/%m/%Y",      // the date format
        button      : "edate"    // id of the button
      }
      );
      */
      </script>
      
      </body>
</html>
