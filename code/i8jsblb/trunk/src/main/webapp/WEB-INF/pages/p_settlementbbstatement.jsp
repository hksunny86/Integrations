<!--Author: Naseer Ullah-->
<%@ page import='com.inov8.microbank.common.util.*'%>
<%@page import="com.inov8.microbank.common.util.PortalConstants"%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<%@include file="/common/taglibs.jsp"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<html>
	<head>
<meta name="decorator" content="decorator">
		<script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
    	<script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
    	<script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
    	<script type="text/javascript" src="${contextPath}/scripts/date-validation.js"></script>
		<script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
		<script language="javascript" type="text/javascript">
			var jq=$.noConflict();
			var serverDate ="<%=PortalDateUtils.getServerDate()%>";
			var username = "<%=UserUtils.getCurrentUser().getUsername()%>";
        	var appUserId= "<%=UserUtils.getCurrentUser().getAppUserId()%>";
        	var email= "<%=UserUtils.getCurrentUser().getEmail()%>";
		</script>
		<%@include file="/WEB-INF/pages/export_zip.jsp"%>	
		<%-- <script type="text/javascript" src="${contextPath}/scripts/exportzip.js"></script> --%>
		<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
		<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
		<meta name="title" content="Settlement Accounts Ledger - ${param.accountTitle}"  id="<%=ReportIDTitleEnum.SettlementAccountsLedger.getId()%>"   />
		
	</head>
	<body bgcolor="#ffffff">
		<html:form action="p_settlementbbstatement.html" commandName="settlementBbStatementViewModel" onsubmit="return validateForm(this);">
			<table width="750px">
				<html:hidden path="accountId" id="accountId"/>
				<html:hidden path="accountNumber"/>
				<html:hidden path="accountTittle"/>
				
	
<%-- 				<tr>
					<td align="right" class="formText"><span class="asterisk">*</span>Account Title:</td>
					<td colspan="3">
						<html:select id="accountId" path="accountId" cssClass="textBox" tabindex="1">
							<html:option value="">---All---</html:option>
							<html:options items="${accountIdsAndTitlesList}" itemLabel="label" itemValue="value"/>
						</html:select>
					</td>
				</tr --%>
				<tr>
					<td class="formText" align="right">
						<span class="asterisk">*</span>Start Date:
					</td>
					<td align="left">
				        <html:input path="dateRangeHolderModel.fromDate" id="startDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
						<img id="sDate" tabindex="2" name="popcal" align="top" style="cursor:pointer" src="${contextPath}/images/cal.gif" border="0" />
						<img id="sDate" tabindex="3" title="Clear Date" name="popcal" onclick="javascript:$('startDate').value=''" align="middle" style="cursor:pointer" src="${contextPath}/images/refresh.png" border="0"/>
					</td>
					<td class="formText" align="right">
						<span class="asterisk">*</span>End Date:
					</td>
					<td align="left">
					     <html:input path="dateRangeHolderModel.toDate" id="endDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
					     <img id="eDate" tabindex="4" name="popcal" align="top" style="cursor:pointer" src="${contextPath}/images/cal.gif" border="0"/>
					     <img id="eDate" tabindex="5" title="Clear Date" name="popcal" onclick="javascript:$('endDate').value=''" align="middle" style="cursor:pointer" src="${contextPath}/images/refresh.png" border="0"/>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td align="left" class="formText">
						<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="${retriveAction}"/>
						<input type="hidden" name="accountTitle" value="${param.accountTitle}"> 
						<input type="submit" class="button" value="Search" name="_search" tabindex="6"/>
						<input type="reset" class="button" value="Cancel" name="_reset" onclick="javascript: window.location='p_settlementbbstatement.html?accountTitle=${param.accountTitle}&accountId=${param.accountId}&actionId=${param.actionId}'" tabindex="7"/>
					</td>
				</tr>
			</table>
		</html:form>
		<c:if test="${not empty settlementBbStatementViewModelList}">
			<table width="750px">
				
					<tr>
						<td align="left" class="formText" width="40%">
							
						</td>
						<td align="right" class="formText">
							Issued on :&nbsp; ${requestScope.reportHeaderMap.issueDate}
						</td>
					</tr>
					<tr>
						<td align="left" class="formText" width="40%">
							<b>BRANCH NAME</b>
						</td>
						<td align="right" class="formText">
							<b>Statement Period</b>
						</td>
					</tr>
					<tr>
						<td align="left" class="formText" width="40%">
							${requestScope.reportHeaderMap.branchName1}
						</td>
						<td align="right" class="formText">
							From:&nbsp;${requestScope.reportHeaderMap.statementDateRange} 
						</td>
					</tr>
					<tr>
						<td align="left" class="formText" width="40%">
							${requestScope.reportHeaderMap.branchName2}
						</td>
					</tr>
			</table>	
			<table width="750px">
					<tr>
						<td align="left" class="formText" width="20%">
							<b>Account Title:</b>
						</td>
						<td align="left" class="formText">
							${requestScope.reportHeaderMap.settlementAccTitle}
						</td>
						<td align="Right" class="formText" width="20%">
							Account #
						</td>
						<td align="right" class="formText">
							${requestScope.reportHeaderMap.settlementAccNumber}
						</td>
					</tr>	
			</table>
		</c:if>
		

		<c:if test="${not empty openingBalance}">
			<div align="right" bgcolor="F3F3F3" class="formText">
				Opening Balance : <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${openingBalance}"></fmt:formatNumber>
			</div>
		</c:if>

		<ec:table filterable="false" items="settlementBbStatementViewModelList" var="settlementBbStatementViewModel" action="${contextPath}/p_settlementbbstatement.html"
			retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" showPagination="true" sortable="false">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls view="com.inov8.microbank.common.util.SettlementBBAccountsXlsView" fileName="Settlement Accounts Ledger - ${param.accountTitle}.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Settlement Accounts Ledger - ${param.accountTitle}.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.SettlementBBAccountsPDFView" headerBackgroundColor="#b6c2da"
					headerTitle="Settlement Accounts Ledger - ${param.accountTitle}"
					fileName="Settlement Accounts Ledger - ${param.accountTitle}.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Settlement Accounts Ledger - ${param.accountTitle}.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>
				<ec:column property="transactionTime" title="Transaction Date" cell="date" format="dd/MM/yyyy hh:mm a"/>
				<ec:column property="transactionCode" title="Transaction ID">
					<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
						<a href="p-transactionsettlementaccountsdetail.html?actionId=${retriveAction}&transactionCode=${settlementBbStatementViewModel.transactionCode}" onclick="return openTransactionWindow('${settlementBbStatementViewModel.transactionCode}')" >
						${settlementBbStatementViewModel.transactionCode}</a>
					</authz:authorize>
					<authz:authorize ifNotGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
						${settlementBbStatementViewModel.transactionCode}
					</authz:authorize>
				
				</ec:column>		
				<ec:column property="transactionSummaryText" title="Description"/>
				<ec:column property="debitAmount" title="Debit" style="text-align: right" cell="currency" format="0.00"/>
				<ec:column property="creditAmount" title="Credit" style="text-align: right" cell="currency" format="0.00"/>
				<ec:column property="balanceAfterTransaction" title="Balance" style="text-align: right" cell="currency" format="0.00"/>
			</ec:row>
		</ec:table>

		<c:if test="${not empty closingBalance}">
			<div align="right" bgcolor="F3F3F3" class="formText">
				Closing Balance : <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${closingBalance}"></fmt:formatNumber>
			</div>
		</c:if>

		<script type="text/javascript">
			//document.forms[0].accountId.focus();
			function validateForm(form)
			{
				var isValid = true;
				var accountIdVal = document.getElementById("accountId").value;
				if( accountIdVal == "" || accountIdVal == 0 || accountIdVal == "0")
				{
					alert( 'Invalid Account.' );
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
				}
	        	return isValid;
	        }
	       function openTransactionWindow(transactionCode)
			{
		        var popupWidth = 550;
				var popupHeight = 350;
				var popupLeft = (window.screen.width - popupWidth)/2;
				var popupTop = (window.screen.height - popupHeight)/2;
		        newWindow=window.open('p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode='+transactionCode,'TransactionSummary','width='+popupWidth+',height='+popupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
			    if(window.focus) newWindow.focus();
			    return false;
			}

      		Calendar.setup(
		    {
		        inputField  : "startDate", // id of the input field
		        ifFormat    : "%d/%m/%Y",      // the date format
		        button      : "sDate",    // id of the button
		      	showsTime   :   false
		    }
		    );
		      
		    Calendar.setup(
		    {
		        inputField  : "endDate", // id of the input field
		        ifFormat    : "%d/%m/%Y",      // the date format
		        button      : "eDate",    // id of the button
		        showsTime   :   false,
		      }
		    );
		</script>
	</body>
</html>
