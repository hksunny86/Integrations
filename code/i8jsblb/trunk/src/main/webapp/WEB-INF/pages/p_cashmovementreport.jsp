<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<%@page import="com.inov8.microbank.common.util.IssueTypeStatusConstantsInterface"%>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
<%@ page import='com.inov8.microbank.common.util.FinancialInstitutionConstants'%>
<%@page import="com.inov8.microbank.common.util.CashBankReconciliationReportConstants" %>

<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<html>
	<head>
<meta name="decorator" content="decorator">
	<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
	<script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/date-validation.js"></script>
    
	<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
	<link type="text/css" rel="stylesheet" href="styles/ajaxtags.css" />
	<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
	<%@include file="/common/ajax.jsp"%>
	<meta name="title" content="Cash-Bank Reconciliation" />
    <script language="javascript" type="text/javascript">
      function error(request)
      {
      	alert("An unknown error has occured. Please contact with the administrator for more details");
      }
    </script>
	</head>
	<body bgcolor="#ffffff">

		<div id="rsp" class="ajaxMsg"></div>

		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" /><br/>
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>
		<table width="750px" border="0">
			<html:form name='agentCashViewForm' commandName="extendedAgentCashViewModel" method="post"
				action="p_cashmovementreport.html" onsubmit="return validateForm(this)" >
				<tr>
					<td class="formText" align="right">
						Agent ID:
					</td>
					<td align="left">
						<html:input path="agentId" id="agentId" onkeypress="return maskNumber(this,event)" cssClass="textBox" tabindex="1" maxlength="13"/>
					</td>
					<td class="formText" align="right">
						MSISDN:
					</td>
					<td align="left">
						<html:input path="msisdn" id="msisdn" onkeypress="return maskNumber(this,event)" cssClass="textBox" tabindex="2" maxlength="11"/>
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">
						<span style="color:#FF0000">*</span>Start Date:
					</td>
					<td align="left">
				        <html:input path="startDate" id="startDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
							<img id="sDate" tabindex="7" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
							<img id="sDate" tabindex="8" title="Clear Date" name="popcal" onclick="javascript:$('startDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
					<td class="formText" align="right">
						<span style="color:#FF0000">*</span>End Date:
					</td>
					<td align="left">
					     <html:input path="endDate" id="endDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
					     <img id="eDate" tabindex="9" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
					     <img id="eDate" tabindex="10" title="Clear Date" name="popcal" onclick="javascript:$('endDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">

					</td>
					<td align="left">
						<input name="_search" type="submit" class="button" value="Search" tabindex="12" />
						<input name="reset" type="reset"
							onclick="javascript: window.location='p_cashmovementreport.html?actionId=${retriveAction}'"
							class="button" value="Cancel" tabindex="13" />
					</td>
					<td colspan="2">
					</td>
				</tr>
				<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
			</html:form>
		</table>
		<c:if test="${not empty requestScope.reportHeaderMap}">
			<table width="750px">
				<c:forEach var="labelValue" items="${requestScope.reportHeaderMap}">
					<tr>
						<td align="left" class="formText" width="35%">
							<B>${labelValue.key}</B>
						</td>
						<td align="left" class="formText">
							${labelValue.value}
						</td>
					</tr>
				</c:forEach>
			</table>
		</c:if>
		<ec:table items="agentCashViewModelList" var="agentCashViewModel"
		action="${contextPath}/p_cashmovementreport.html"
		title="" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" filterable="false" sortable="false">
		<c:if test="${not empty agentCashViewModelList}">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls view="com.inov8.microbank.common.util.CashBankReconciliationXlsView" fileName="Cash Bank Reconciliation.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Cash Bank Reconciliation.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Cash Bank Reconciliation.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Cash Bank Reconciliation.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
		</c:if>
			<ec:row>
				
				<ec:column property="transactionDate" cell="date" format="dd/MM/yyyy hh:mm a" filterable="false" title="Date" width="120px"/>
				<ec:column property="description" filterable="false" title="Description"/>
				<ec:column property="bankDebitAmount" filterable="false" title="Debit(Bank)" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="bankCreditAmount" filterable="false" title="Credit(Bank)" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="bankBalance" filterable="false" title="Balance(Bank)" calc="com.inov8.microbank.common.util.CustomCalc" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="cashIn" filterable="false" title="Cash IN" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="cashOut" filterable="false" title="Cash OUT" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="cashBalance" filterable="false" title="Balance(Cash)" calc="com.inov8.microbank.common.util.CustomCalc" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="totalBalance" filterable="false" title="Total Cash & Bank" calc="com.inov8.microbank.common.util.CustomCalc" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="commission" filterable="false" title="Commission(After Tax)" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			</ec:row>
		</ec:table>

		<script language="javascript" type="text/javascript">
			document.forms[0].agentId.focus();
	        function validateForm(form){	        	 	        	
	        	
	        	var isValid = true;
	        	
	        	if( form.agentId.value == '' && form.msisdn.value == '' )
	        	{
	        		alert( "Agent ID or MSISDN is required" );
	        		isValid = false;
	        	}
	        	else
	        	{
		        	var currentDate = "<%=PortalDateUtils.getServerDate()%>";
		        	var _fDate = form.startDate.value;
		  			var _tDate = form.endDate.value;		
		  			var startlbl = "Start Date";
		  			var endlbl   = "End Date";
		        	isValid = validateDateRangeMandatory(_fDate,_tDate,startlbl,endlbl,currentDate);
		        	if( isValid )
		        	{
		        		isValid = validateFormChar(form);
		        	}
	        	}

	        	return isValid;
	        }

      		Calendar.setup(
      		{
		       inputField  : "startDate", // id of the input field
		       button      : "sDate",    // id of the button
		       ifFormat    : "%d/%m/%Y",
		       showsTime   :   false
		    }
      		);
			Calendar.setup(
		    {
		      inputField  : "endDate", // id of the input field
		      button      : "eDate",    // id of the button
		      ifFormat    : "%d/%m/%Y",
		      showsTime   :   false
		    }
		    );

      	</script>
	</body>
</html>
