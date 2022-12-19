<%@page import="com.inov8.microbank.common.util.PortalConstants"%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<%@include file="/common/taglibs.jsp"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<html>
	<head>
		<script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
    	<script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
    	<script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
    	<script type="text/javascript" src="${contextPath}/scripts/date-validation.js"></script>
		<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
		<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
		<meta name="decorator" content="decorator">
		<meta name="title" content="Agent BB Statement"/>
	</head>
	<body bgcolor="#ffffff">
		<html:form action="p_agentbbstatement.html?actionId=${retriveAction}" commandName="agentBBStatementViewModel" onsubmit="return validateForm(this);">
			<table width="750px">
				<tr>
					<td class="formText" align="right">
						<span class="asterisk">*</span>Start Date:
					</td>
					<td align="left">
				        <html:input path="dateRangeHolderModel.fromDate" id="startDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
						<img id="sDate" tabindex="1" name="popcal" align="top" style="cursor:pointer" src="${contextPath}/images/cal.gif" border="0" />
						<img id="sDate" tabindex="2" title="Clear Date" name="popcal" onclick="javascript:$('startDate').value=''" align="middle" style="cursor:pointer" src="${contextPath}/images/refresh.png" border="0"/>
					</td>
					<td class="formText" align="right">
						<span class="asterisk">*</span>End Date:
					</td>
					<td align="left">
					     <html:input path="dateRangeHolderModel.toDate" id="endDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
					     <img id="eDate" tabindex="3" name="popcal" align="top" style="cursor:pointer" src="${contextPath}/images/cal.gif" border="0"/>
					     <img id="eDate" tabindex="4" title="Clear Date" name="popcal" onclick="javascript:$('endDate').value=''" align="middle" style="cursor:pointer" src="${contextPath}/images/refresh.png" border="0"/>
					</td>
				</tr>
				<tr>
					
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td align="left" class="formText">
						<input type="hidden" name="appUserId" value="${param.appUserId}"/>
						<input type="submit" class="button" value="Search" name="_search" tabindex="5"/>
						<input type="reset" class="button" value="Cancel" name="_reset" onclick="javascript: window.location='p_agentbbstatement.html?appUserId=${param.appUserId}&actionId=${retriveAction}'" tabindex="6"/>
					</td>
				</tr>
			</table>
		</html:form>
<c:if test="${not empty agentBBStatementViewModelList}">
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
							<b>Name:</b>
						</td>
						<td align="left" class="formText">
							${requestScope.reportHeaderMap.customerName}
						</td>
						<td align="left" class="formText" width="20%">
							BB Mobile Account #
						</td>
						<td align="right" class="formText">
							${requestScope.reportHeaderMap.zongMsisdn}
						</td>
					</tr>
										<tr>
						<td align="left" class="formText" width="20%">
							<b>Business Name:</b>
						</td>
						<td align="left" class="formText">
							${requestScope.reportHeaderMap.businessName}
						</td>
					</tr>
					<tr>
						<td align="left" class="formText" width="20%">
							Address:
						</td>
						<td align="left" class="formText">
							${requestScope.reportHeaderMap.address1}
						</td>
						<td align="left" class="formText" width="20%">
							Agent ID #
						</td>
						<td align="right" class="formText">
							${requestScope.reportHeaderMap.agentId}
						</td>
					</tr>
					<tr>
						<td align="left" class="formText" width="20%">						
													
						</td>
						<td align="left" class="formText">
							${requestScope.reportHeaderMap.address2}
						</td>
						<td align="left" class="formText" width="20%">
							Currency
						</td>
						<td align="right" class="formText">
							${requestScope.reportHeaderMap.currency}
						</td>
					</tr>
					<tr>
						<td align="left" class="formText" width="20%">
							
						</td>
						<td align="left" class="formText">
							${requestScope.reportHeaderMap.address3}
						</td>
						<td align="left" class="formText" width="20%">
							Account Type
						</td>
						<td align="right" class="formText">
							${requestScope.reportHeaderMap.accountLevel}
						</td>
					</tr>
					
				
			</table>
		</c:if>
		

		<c:if test="${not empty openingBalance}">
			<div align="right" bgcolor="F3F3F3" class="formText">
				Opening Balance : <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${openingBalance}"></fmt:formatNumber>
			</div>
		</c:if>

		<ec:table filterable="false" items="agentBBStatementViewModelList" action="${contextPath}/p_agentbbstatement.html"
			retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" showPagination="true" sortable="false">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls view="com.inov8.microbank.common.util.ViewCustomerBbStatementXlsView" fileName="Agent BB Statement.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Agent BB Statement.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomerBBStatementPDFView" headerBackgroundColor="#b6c2da"
					headerTitle="Agent BB Statement"
					fileName="Agent BB Statement.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Agent BB Statement.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>
				<ec:column property="transactionTime" title="Transaction Date" cell="date" format="dd/MM/yyyy hh:mm a"/>
				<ec:column property="transactionCode" title="Transaction ID"/>
				<ec:column property="transactionSummaryText" title="Description" viewsAllowed="html"/>
				<ec:column property="transactionSummaryTextEscape" title="Description" viewsDenied="html"/>
				<ec:column property="debitAmount" title="Debit" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="creditAmount" title="Credit" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="balanceAfterTransaction" title="Balance" cell="currency" format="0.00" style="text-align: right"/>
			</ec:row>
		</ec:table>

		<c:if test="${not empty closingBalance}">
			<div align="right" bgcolor="F3F3F3" class="formText">
				Closing Balance : <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${closingBalance}"></fmt:formatNumber>
			</div>
			<div align="left" bgcolor="F3F3F3" class="formText">* FED/WHT deducted</div>
		</c:if>

		<script type="text/javascript">
			document.forms[0].startDate.focus();
			function validateForm(form)
			{
				var currentDate = "<%=PortalDateUtils.getServerDate()%>";
	        	var _fDate = form.startDate.value;
	  			var _tDate = form.endDate.value;
		  		var startlbl = "Start Date";
		  		var endlbl   = "End Date";
	        	var isValid = validateDateRangeMandatory(_fDate,_tDate,startlbl,endlbl,currentDate);
	        	return isValid;
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