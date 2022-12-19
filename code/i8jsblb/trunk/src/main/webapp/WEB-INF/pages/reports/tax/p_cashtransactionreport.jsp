<%@ page import='com.inov8.microbank.common.util.*'%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<%@page import="com.inov8.microbank.common.util.PortalConstants"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<meta name="decorator" content="decorator">
<meta name="title" content="CNIC based Transaction Report"   id="<%=ReportIDTitleEnum.CNICbasedTransactionReport.getId()%>" />
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
	var username = "<%=UserUtils.getCurrentUser().getUsername()%>";
    var appUserId= "<%=UserUtils.getCurrentUser().getAppUserId()%>";
    var email= "<%=UserUtils.getCurrentUser().getEmail()%>";

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
	<%@include file="/WEB-INF/pages/export_zip.jsp"%>	
	<%-- <script type="text/javascript" src="${contextPath}/scripts/exportzip.js"></script> --%>
</head>
<body>
	<div id="successMsg" class="infoMsg" style="display:none;"></div>
	<div id="errorMsg" class="errorMsg" style="display:none;"></div>
	<input id="message" value="777" type="hidden" />
	<html:form name="cashTransReportForm" commandName="cashTransReportViewModel" method="post" action="p_cashtransactionreport.html"
		onsubmit="return validateForm(this);" >
		<table width="850px" border="0">
			<tr>
				<td class="formText" width="18%" align="right">Transaction ID:</td>
				<td align="left" width="32%">
					<html:input path="transactionCode" id="transactionCode" cssClass="textBox" tabindex="1" maxlength="20" onkeypress="return maskNumber(this,event)"/> 
				</td>
				<td class="formText" align="right"></td>
				<td align="left">
				</td>
			</tr>

			<tr>
				<td class="formText" width="18%" align="right">Sender Agent ID:</td>
				<td align="left" width="32%">
					<html:input path="senderAgentId" id="senderAgentId" cssClass="textBox" tabindex="3" maxlength="20" onkeypress="return maskNumber(this,event)"/> 
				</td>
				<td class="formText" align="right">Receiver Agent ID:</td>
				<td align="left">
					<html:input path="recipientAgentId" id="recipientAgentId" cssClass="textBox" tabindex="4" maxlength="20" onkeypress="return maskNumber(this,event)"/> 
				</td>
			</tr>
			<tr>
				<td class="formText" width="18%" align="right">Sender Mobile No:</td>
				<td align="left" width="32%">
					<html:input path="senderCustomerMobile" id="senderCustomerMobile" cssClass="textBox" tabindex="5" maxlength="11" onkeypress="return maskNumber(this,event)"/> 
				</td>
				<td class="formText" align="right">Receiver Mobile No:</td>
				<td align="left">
					<html:input path="recipientCustomerMobile" id="recipientCustomerMobile" cssClass="textBox" tabindex="6" maxlength="11" onkeypress="return maskNumber(this,event)"/> 
				</td>
			</tr>
			<tr>
				<td class="formText" width="18%" align="right">Sender CNIC:</td>
				<td align="left" width="32%">
					<html:input path="senderCustomerCNIC" id="senderCustomerCNIC" cssClass="textBox" tabindex="5" maxlength="13" onkeypress="return maskNumber(this,event)"/> 
				</td>
				<td class="formText" align="right">Receiver CNIC:</td>
				<td align="left">
					<html:input path="recipientCustomerCNIC" id="recipientCustomerCNIC" cssClass="textBox" tabindex="6" maxlength="13" onkeypress="return maskNumber(this,event)"/> 
				</td>
			</tr>

			<tr>
				<td class="formText" width="18%" align="right">Start Date:</td>
				<td align="left" width="32%">
					<html:input path="startDate" id="startDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10" /> <img
						id="sDate" tabindex="11" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" /> <img id="sDate" tabindex="12"
						title="Clear Date" name="popcal" onclick="javascript:$('startDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png"
						border="0" />
				</td>
				<td class="formText" align="right">End Date:</td>
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
	<ec:table items="resultList" var="model" action="${contextPath}/p_cashtransactionreport.html" title="" retrieveRowsCallback="limit"
		filterRowsCallback="limit" sortRowsCallback="limit" filterable="false">
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
			<ec:exportXls fileName="CNIC_Based_Trans_Report.xls" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
			<ec:exportXlsx fileName="CNIC_Based_Trans_Report.xlsx" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
			<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da" headerTitle="CNIC based Transaction Report"
				fileName="CNIC_Based_Trans_Report.pdf" tooltip="Export PDF" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
			<ec:exportCsv fileName="CNIC_Based_Trans_Report.csv" tooltip="Export CSV"></ec:exportCsv>
		</authz:authorize>
		<ec:row>
			<ec:column property="transactionCode" title="Transaction ID" escapeAutoFormat="true"/>
<%-- 			ec:column property="transactionType" title="Tansaction Type" escapeAutoFormat="true"/> --%>
			<ec:column property="productName" title="Product" escapeAutoFormat="true"/>
			<ec:column property="senderChannel" title="Sender Channel" escapeAutoFormat="true"/>
			<ec:column property="paymentMode" title="Payment Mode" escapeAutoFormat="true"/>
			<ec:column property="initiatedOn" title="Transaction Initiated on" cell="date" format="dd/MM/yyyy hh:mm a"/>
			<ec:column property="senderAgentId" title="Sender Agent ID" escapeAutoFormat="true"/>
			<ec:column property="senderAgentAccountNo" title="Sender Agent A/C #" escapeAutoFormat="true"/>
			<ec:column property="senderAgentName" title="Sender Agent Name" escapeAutoFormat="true"/>
			<ec:column property="senderAgentAccountTitle" title="Sender Agent - A/C Title" escapeAutoFormat="true"/>
			<ec:column property="senderAgentCity" title="Sender Agent City" escapeAutoFormat="true"/>
			<ec:column property="senderAgentRegion" title="Sender Agent Region" escapeAutoFormat="true"/>
			<ec:column property="senderCustomerMobile" title="Sender Mobile No." escapeAutoFormat="true"/>
			<ec:column property="senderCustomerCNIC" title="Sender CNIC" escapeAutoFormat="true"/>
			<ec:column property="recipientCustomerMobile" title="Receiver Mobile No." escapeAutoFormat="true"/>
			<ec:column property="recipientCustomerCNIC" title="Receiver CNIC" escapeAutoFormat="true"/>
			<ec:column property="completedOn" title="Transaction Completed On" cell="date" format="dd/MM/yyyy hh:mm a"/>
			<ec:column property="recipientAgentId" title="Receiver Agent ID" escapeAutoFormat="true"/>
			<ec:column property="recipientAgentAccountNo" title="Receiver Agent A/C #" escapeAutoFormat="true"/>
			<ec:column property="recipientAgentName" title="Receiver Agent Name" escapeAutoFormat="true"/>
			<ec:column property="recipientAgentAccountTitle" title="Receiver Agent - A/C Title" escapeAutoFormat="true"/>
			<ec:column property="recipientAgentCity" title="Receiver Agent City" escapeAutoFormat="true"/>
			<ec:column property="recipientAgentRegion" title="Receiver Agent Region" escapeAutoFormat="true"/>
			<ec:column property="amount" title="Amount" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="inclusiveCharges" title="Inclusive Charges" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="exclusiveCharges" title="Exclusive Charges" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="fed" title="FED" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="senderAgentGrossCommission" title="Sender Agent Total Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="senderAgentWHT" title="Sender Agent WHT" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="senderAgentNetCommission" title="Sender Agent Net Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="recipientAgentGrossCommission" title="Receiver Agent Total Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="recipientAgentWHT" title="Receiving Agent WHT" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="recipientAgentNetCommission" title="Receiver Agent NET Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="bankCommission" title="Bank Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="status" title="Status" escapeAutoFormat="true"/>
		</ec:row>
	</ec:table>
	<script type="text/javascript">
	
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