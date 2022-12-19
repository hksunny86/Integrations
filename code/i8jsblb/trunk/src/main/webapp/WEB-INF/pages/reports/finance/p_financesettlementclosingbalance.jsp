<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE%></c:set>
<html>
<head>
<meta name="decorator" content="decorator">
<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
<script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
<script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
<script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
<script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
<script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
<script type="text/javascript" src="${contextPath}/scripts/date-validation.js"></script>
<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css" />
<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
<meta name="title" content="Settlement Closing Balance Report" />
<%@include file="/common/ajax.jsp"%>
<script language="javascript" type="text/javascript">
	var jq=$.noConflict();
	var serverDate ="<%=PortalDateUtils.getServerDate()%>";

	function error(request) {
		alert("An unknown error has occured. Please contact with the administrator for more details");
	}
</script>
</head>
<body bgcolor="#ffffff">
	<c:if test="${not empty messages}">
		<div class="infoMsg" id="successMessages">
			<c:forEach var="msg" items="${messages}">
				<c:out value="${msg}" escapeXml="false" />
				<br />
			</c:forEach>
		</div>
		<c:remove var="messages" scope="session" />
	</c:if>
	<html:form name='settlementClosingBalanceViewForm' commandName="extendedSettlementClosingBalanceViewModel" method="post"
		action="p_financesettlementclosingbalance.html" onsubmit="return validate(this)">
		<table width="750px" border="0">
			<tr>
				<td align="right" class="formText">Account Type:</td>
				<td align="left"><html:select path="accountType" id="accountType" cssClass="textBox" maxlength="11" tabindex="1"
						onkeypress="return maskNumber(this,event)">
						<html:option value="1" label="--All--" />
						<html:option value="2" label="Agent" />
						<html:option value="3" label="Customer" />
						<html:option value="4" label="Internal" />
						<html:option value="5" label="Commission" />
					</html:select></td>
				<td align="right" class="formText">Account Number:</td>
				<td align="left"><html:select path="stakeHolderBankInfoId" id="ofAccountNumber" cssClass="textBox" maxlength="11" tabindex="1"
						onkeypress="return maskNumber(this,event)">
						<html:option value="-2" label="--All--" />
						<html:options items="${ofSettlementAccountList}" itemLabel="label" itemValue="value" />
					</html:select></td>
			</tr>
			<tr>
				<td align="right" class="formText" width="18%">From Date:</td>
				<td align="left" width="32%"><html:input path="startDate" id="startDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10" />
					<img id="sDate" tabindex="5" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" /> <img id="sDate" tabindex="6"
					title="Clear Date" name="popcal" onclick="javascript:$('startDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png"
					border="0" /></td>
				<td align="right" class="formText" width="18%">To Date:</td>
				<td align="left" width="32%"><html:input path="endDate" id="endDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10" /> <img
					id="eDate" tabindex="7" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" /> <img id="eDate" tabindex="8"
					title="Clear Date" name="popcal" onclick="javascript:$('endDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png"
					border="0" /></td>
			</tr>
			<tr>
				<td class="formText" align="right">&nbsp;</td>
				<td align="left"><input name="_search" type="submit" class="button" value="Search" tabindex="9" /> <input name="reset" type="reset"
					onclick="javascript: window.location='p_financesettlementclosingbalance.html?actionId=${retriveAction}'" class="button" value="Cancel"
					tabindex="10" /></td>
				<td colspan="2">&nbsp;</td>
			</tr>
			<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
		</table>
	</html:form>
	<ec:table items="settlementClosingBalanceViewModelList" var="settlementClosingBalanceViewModel"
		action="${contextPath}/p_financesettlementclosingbalance.html?actionId=${retriveAction}" title="" retrieveRowsCallback="limit"
		filterRowsCallback="limit" sortRowsCallback="limit" filterable="false">
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
			<ec:exportXls fileName="Settlement_Closing_Balance_Report.xls" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
			<ec:exportXlsx fileName="Settlement_Closing_Balance_Report.xlsx" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
			<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da" headerTitle="Settlement Closing Balance Report"
				fileName="Settlement_Closing_Balance_Report.pdf" tooltip="Export PDF" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
			<ec:exportCsv fileName="Settlement_Closing_Balance_Report.csv" tooltip="Export CSV"></ec:exportCsv>
		</authz:authorize>
		<ec:row>
			<ec:column property="blbAccountNumber" title="BLB Account #" escapeAutoFormat="true" style="text-align: left;"/>
			<ec:column property="ofAccountNumber" title="OF Account #" escapeAutoFormat="true" style="text-align: left;"/>
			<ec:column property="coreAccountTitle" title="OF Account Title" escapeAutoFormat="true" style="text-align: left;"/>
			<ec:column property="bbAccountTitle" title="BLB Account Title" escapeAutoFormat="true"  style="text-align: left;"/>
			<ec:column property="openingBalance" sortable="false" format="0.00" cell="currency" title="Opening Balance" escapeAutoFormat="true"  style="text-align: right;"/>
			<ec:column property="debitMovement" sortable="false" format="0.00" cell="currency" title="Debit Movement" escapeAutoFormat="true"  style="text-align: right;"/>
			<ec:column property="creditMovement" sortable="false" format="0.00" cell="currency" title="Credit Movement" escapeAutoFormat="true" style="text-align: right;"/>
			<ec:column property="closingBalance" sortable="false" format="0.00" cell="currency" title="Closing Balance"  escapeAutoFormat="true" style="text-align: right;"/>
		</ec:row>
	</ec:table>
	<script language="javascript" type="text/javascript">
		function validate(form){
			var _fDate = form.startDate.value;
  			var _tDate = form.endDate.value;
	  		var startlbl = "Start Date";
	  		var endlbl   = "End Date";
        	var isValid = validateDateRangeMandatory(_fDate,_tDate,startlbl,endlbl,serverDate);
        	return isValid;
		}
		
		Calendar.setup({
			inputField : "startDate", // id of the input field
			ifFormat : "%d/%m/%Y", // the date format
			button : "sDate", // id of the button
			showsTime : false
		});
		Calendar.setup({
			inputField : "endDate", // id of the input field
			ifFormat : "%d/%m/%Y", // the date format		      
			button : "eDate", // id of the button
			showsTime : false
		});
	</script>
	
	<ajax:select source="accountType" target="ofAccountNumber"
		baseUrl="${contextPath}/p-settlementaccountrefdata.html"
		parameters="accountTypeId={accountType},actionId=${retriveAction}" errorFunction="error"/>
	
</body>
</html>
