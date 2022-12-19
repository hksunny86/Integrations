
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<meta name="decorator" content="decorator">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
<meta name="title" content="Agent Head Summary Report"/>
<script type="text/javascript" src="<c:url value='/scripts/activatedeactivate.js'/>"></script>
	<script type="text/javascript">
		function actdeact(request)
		{
			isOperationSuccessful(request);
		}
	</script>
	<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
		<script type="text/javascript" 
			src="${contextPath}/scripts/date-validation.js"></script>
<link rel="stylesheet"
			href="${pageContext.request.contextPath}/styles/extremecomponents.css"
			type="text/css">
		<link rel="stylesheet" type="text/css"
			href="${pageContext.request.contextPath}/styles/deliciouslyblue/calendar.css" />	    
	
</head>
<body bgcolor="#ffffff">
<table width="85%">

		<form name="allpayretailerreportform"
			action="p_allpaydistheadsummaryreport.html?actionId=2" method="post">
		<tr>
			<td align="right" class="formText">
				From Date:
			</td>
			<td align="left">
				<spring:bind path="distHeadSummaryViewModel.startDate">
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
			<spring:bind path="distHeadSummaryViewModel.endDate">
				<input name="${status.expression }" id="${status.expression}"
						class="textBox" value="${status.value}" />
			</spring:bind>
				<img id="eDate" tabindex="2" readonly="true" name="eDate"
					align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
			</td>
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


<ec:table showPagination="false" items="transList" var="transList" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" action="${pageContext.request.contextPath}/p_allpaydistheadsummaryreport.html">
  		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
			<ec:exportXls fileName="Agent Head Summary Report.xls" tooltip="Export Excel"/>
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
			<ec:exportXlsx fileName="Agent Head Summary Report.xlsx" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
			<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Agent Head Summary Report" fileName="Agent Head Summary Report.pdf" tooltip="Export PDF"/>
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
			<ec:exportCsv fileName="Agent Head Summary Report.csv" tooltip="Export CSV"></ec:exportCsv>
		</authz:authorize>
  <ec:row>    
    <ec:column property="location"  title="Location" sortable="false" filterable="false"/>
    <ec:column property="region"  title="Region" sortable="false" filterable="false"/>
    <ec:column property="openingBalance"  title="Opening Balance" sortable="false" filterable="false"/>
    <ec:column property="stockReceived"  title="Stock Received from AllPay" sortable="false" filterable="false"/>
    <ec:column property="headIssuedAmount"  title="Issuance To Retailer Head" sortable="false" filterable="false"/>
    <ec:column property="closingBalance"  title="Closing Balance" sortable="false" filterable="false"/>          
  </ec:row>
</ec:table>

	<script type="text/javascript">
      
			Calendar.setup(
      		{
		       inputField  : "startDate", // id of the input field
			   button      : "sDate",    // id of the button
		    }
      		);
			Calendar.setup(
		    {
		      inputField  : "endDate", // id of the input field
		      button      : "eDate",    // id of the button
		  	  isEndDate: true
		    }
		    );
      </script>
</body>
</html>
