<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<%@page import="com.inov8.microbank.common.util.PortalConstants"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<meta name="decorator" content="decorator">
<meta name="title" content="Agent WHT Report 231-A" />
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/prototype.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/date-validation.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/popup.js"></script>
<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
<link rel="stylesheet" type="text/css" href="${contextPath}/styles/deliciouslyblue/calendar.css" />
<%@include file="/common/ajax.jsp"%>
<script type="text/javascript" src="${contextPath}/scripts/jquery-1.7.2.min.js"></script>
<script language="javascript" type="text/javascript">
	var serverDate ="<%=PortalDateUtils.getServerDate()%>";
	var jq=$.noConflict();

	function error(request) {
		alert("An unknown error has occured. Please contact with the administrator for more details");
	}

	function init() {
		$('errorMsg').innerHTML = "";
		$('successMsg').innerHTML = "";
		Element.hide('successMsg');
		Element.hide('errorMsg');
	}
	
</script>
</head>
<body>
	<div id="successMsg" class="infoMsg" style="display:none;"></div>
	<div id="errorMsg" class="errorMsg" style="display:none;"></div>
	<input id="message" value="777" type="hidden" />
	<html:form name="wHTSummaryForm" commandName="wHTSummaryViewModel" method="post" action="p_whtAgentCustReport231A.html"
		onsubmit="return validateThisForm(this);" >
		<table width="850px" border="0">
			<tr>
				<td class="formText" width="18%" align="right">TaxPayer ID:</td>
				<td align="left">
					<html:input tabindex="1" maxlength="13" path="agentId" cssClass="textBox" onkeypress="return maskInteger(this,event)"/>
				</td>
				<td class="formText" width="18%" align="right">TaxPayer CNIC:</td>
				<td align="left">
					<html:input tabindex="2" maxlength="15" path="taxPayerCNIC" cssClass="textBox"/>
				</td>
			</tr>
			<tr>
				<td class="formText" width="18%" align="right"><span class="asterisk">*</span>Start Date:</td>
				<td align="left" width="32%">
					<html:input path="startDate" id="startDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10" /> <img
						id="sDate" tabindex="11" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" /> <img id="sDate" tabindex="12"
						title="Clear Date" name="popcal" onclick="javascript:$('startDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png"
						border="0" />
				</td>
				<td class="formText" align="right"><span class="asterisk">*</span>End Date:</td>
				<td align="left"><html:input path="endDate" id="endDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10" /> <img
					id="eDate" tabindex="13" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" /> <img id="eDate" tabindex="14"
					title="Clear Date" name="popcal" onclick="javascript:$('endDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png"
					border="0" /></td>
			</tr>
			<tr>
				<td class="formText" align="right"></td>
				<td align="left"><input name="_search" type="submit" class="button" value="Search" tabindex="13" /></td>
				<td class="formText" align="right"></td>
				<td align="left"></td>
			</tr>
		</table>
	</html:form>
	<!-- data table result -->
	<ec:table items="resultList" var="model" action="${contextPath}/p_whtAgentCustReport231A.html" title="" retrieveRowsCallback="limit"
		filterRowsCallback="limit" sortRowsCallback="limit" filterable="false">
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
			<ec:exportXls fileName="Agent_WHT_Report_231A.xls" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
			<ec:exportXlsx fileName="Agent_WHT_Report_231A.xlsx" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
			<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da" headerTitle="Agent WHT Report _231A"
				fileName="Agent_WHT_Report_231A.pdf" tooltip="Export PDF" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
			<ec:exportCsv fileName="Agent_WHT_Report_231A.csv" tooltip="Export CSV"></ec:exportCsv>
		</authz:authorize>
		<ec:row>
			<ec:column property="agentId" title="TaxPayer ID" escapeAutoFormat="true"/>
			<ec:column property="accType" title="Account Type" escapeAutoFormat="true"/>
			<ec:column property="paymentSection" title="Payment Section" escapeAutoFormat="true"/>
			<ec:column property="taxPayerCategory" title="Tax Payer Category" escapeAutoFormat="true"/>
			<ec:column property="WHTRate" title="Tax Rate" escapeAutoFormat="true"/>
			<ec:column property="taxPayerNTN" title="TaxPayer NTN" escapeAutoFormat="true" />
			<ec:column property="taxPayerCNIC" title="TaxPayer CNIC" escapeAutoFormat="true" />
			<ec:column property="taxPayerName" title="TaxPayer Name" escapeAutoFormat="true" />
			<ec:column property="taxPayerCity" title="TaxPayer City" escapeAutoFormat="true" />
			<ec:column property="taxPayerAddress" title="TaxPayer Address" escapeAutoFormat="true" />
			<ec:column property="taxPayerStatus" title="TaxPayer Status" escapeAutoFormat="true" />
			<ec:column property="businessName" title="TaxPayer Business Name" escapeAutoFormat="true" />
			<ec:column property="agentCommissionSum" title="Taxable Amount" escapeAutoFormat="true" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
			<ec:column property="agentWHTSum" title="Tax Amount" escapeAutoFormat="true" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
		
			
		</ec:row>
	</ec:table>
	<script type="text/javascript">
		function validateThisForm(form){
			var currentDate = "<%=PortalDateUtils.getServerDate()%>";
	        var _fDate = form.startDate.value;
	  		var _tDate = form.endDate.value;
		  	var startlbl = "Start Date";
		  	var endlbl   = "End Date";
	        var isValid = validateDateRangeMandatory(_fDate,_tDate,startlbl,endlbl,currentDate);
	        return isValid;
	    }
	
		Calendar.setup({
			inputField : "startDate", // id of the input field
			button : "sDate", // id of the button
		});
		Calendar.setup({
			inputField : "endDate", // id of the input field
			button : "eDate", // id of the button
			isEndDate : true
		});
	</script>
	<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>
</body>
</html>