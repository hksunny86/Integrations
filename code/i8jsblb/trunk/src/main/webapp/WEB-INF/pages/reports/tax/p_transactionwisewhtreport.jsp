<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.*'%>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set> 
<html>
	<head>
<meta name="decorator" content="decorator">
		<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
	    <script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
		<script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
		
		<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
		<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
		<%@include file="/common/ajax.jsp"%>
		<meta name="title" content="WHT Transaction wise Report"     id="<%=ReportIDTitleEnum.WHTTransactionwiseReport.getId()%>" />
      	<script language="javascript" type="text/javascript">
      		var jq=$.noConflict();
			var serverDate ="<%=PortalDateUtils.getServerDate()%>";
			var username = "<%=UserUtils.getCurrentUser().getUsername()%>";
        	var appUserId= "<%=UserUtils.getCurrentUser().getAppUserId()%>";
        	var email= "<%=UserUtils.getCurrentUser().getEmail()%>";
        	
	      function error(request)
	      {
	      	alert("An unknown error has occured. Please contact with the administrator for more details");
	      }
        </script>
        <%@include file="/WEB-INF/pages/export_zip.jsp"%>	
		<%-- <script type="text/javascript" src="${contextPath}/scripts/exportzip.js"></script> --%>
	</head>
	<body bgcolor="#ffffff">
		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" /><br/>
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>
		
		<html:form name='whtReportTransactionwiseRptForm' commandName="commissionTransactionWHListViewModel" method="post"
				action="p_transactionwisewhtreport.html" onsubmit="return validateForm(this)" >
				<table width="750px" border="0">
				<tr>
					<td align="right" class="formText">
						Start Date:
					</td>
					<td align="left">
				        <html:input path="startDate" id="startDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
						<img id="sDate" tabindex="2" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
						<img id="sDate" tabindex="3" title="Clear Date" name="popcal" onclick="javascript:$('startDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
					<td align="right" class="formText">
						End Date:
					</td>
					<td align="left">
					     <html:input path="endDate" id="endDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
					     <img id="eDate" tabindex="7" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
					     <img id="eDate" tabindex="8" title="Clear Date" name="popcal" onclick="javascript:$('endDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr>
				<tr>
					<td align="right" class="formText" width="18%">
						Transaction ID:
					</td>
					<td align="left">
						<html:input path="transactionCode" id="transactionCode" cssClass="textBox" maxlength="12" tabindex="1" onkeypress="return maskNumber(this,event)"/>
					</td>
				</tr>
				
				<tr>
					<td class="formText" align="right">
						&nbsp;
					</td>
					<td align="left">
						<input name="_search" type="submit" class="button" value="Search" tabindex="4" />
						<input name="reset" type="reset"
							onclick="javascript: window.location='p_transactionwisewhtreport.html?actionId=${retriveAction}'"
							class="button" value="Cancel" tabindex="5" />
					</td>
					
				</tr>
				<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
			</table>
		</html:form>
		

	<ec:table items="commissionTransactionWHListViewModelList" var="commissionTransactionWHListViewModel"
		action="${contextPath}/p_transactionwisewhtreport.html?actionId=${retriveAction}"
		title="" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" filterable="false">
			
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="WHT Transaction wise Report.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="WHT Transaction wise Report.xlsx" tooltip="Export Excel" />
			</authz:authorize>	
			<%-- <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="WHT Transaction wise Report" fileName="Transaction WHT Report.pdf" tooltip="Export PDF" />
			</authz:authorize> By Hassan : Formatting Issue due to excess columns--%>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="WHT Transaction wise Report.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>	
			
		<ec:row>
			<ec:column property="transactionCode" filterable="false" title="Transaction ID" escapeAutoFormat="true">
				  	<%-- <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
						<a href="p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode=${commissionTransactionWHListViewModel.transactionCode}" onclick="return openTransactionWindow('${commissionTransactionWHListViewModel.transactionCode}')">
						  ${commissionTransactionWHListViewModel.transactionCode}
						</a>
					</authz:authorize>
					<authz:authorize ifNotGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
						${commissionTransactionWHListViewModel.transactionCode}
					</authz:authorize> --%>
			</ec:column>
			<ec:column property="senderAgentId" title="Sender Agent ID" />
			<ec:column property="senderId" title="Sender ID" />
			<ec:column property="agent2Id" title="Agent2 ID" />
			<ec:column property="senderMobileNo" title="Sender Mobile No" />
			<ec:column property="senderCNIC" title="Sender CNIC" escapeAutoFormat="true"/>
			<ec:column property="senderChannel" title="Sender Channel" />
			<ec:column property="paymentMode" title="Payment Mode" />
			<ec:column property="accountNo" title="Account No" />
			<ec:column property="transactionDate" title="Transaction Date" cell="date" format="dd/MM/yyyy"/>
			<ec:column property="product" title="Product" />
			<ec:column property="recipientId" title="Recipient ID" />
			<ec:column property="recipientAccountNo" title="Recipient Account No." />
			<ec:column property="recipientMobileNo" title="Recipient Mobile No." />
			<ec:column property="recipeintCNIC" title="Recipient CNIC" escapeAutoFormat="true"/>
			<ec:column property="amount" title="Amount" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="inclusiveCharges" title="Inclusive Charges" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="exclusiveCharges" title="Exclusive Charges" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<!-- Bank commission -->
			<ec:column property="bankGrossCommission" title="Bank Gross Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="bankWHT" title="Bank WHT" />
			<ec:column property="bankNetCommission" title="Bank Net Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<!-- agent 1 commission -->
			<ec:column property="agent1GrossCommission" title="Agent 1 Gross Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="agent1WH" title="Agent 1 WHT" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="agent1NetCommission" title="Agent 1 Net Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<!-- agent 2 commission -->
			<ec:column property="agent2GrossCommission" title="Agent 2 Gross Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="agent2WH" title="Agent 2 WHT" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="agent2NetCommission" title="Agent 2 Net Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<!--  sales team commission -->
			<ec:column property="salesTeamGrossCommission" title="SalesTeam Gross Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="salesTeamWHT" title="SalesTeam WHT" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="salesTeamNetCommission" title="SalesTeam Net Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<!-- Agent Retention -->
			<ec:column property="agentRetUserId" title="Agent Ret. User ID" />
			<ec:column property="agent1RetGrossCommission" title="Agent Ret. Gross Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="agent1RetWHT" title="Agent Ret. WHT" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="agent1RetNetCommission" title="Agent Ret. Net Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<%--  agent 1 hierarchy commission -->
			<ec:column property="agent1HierarchyGrossCommission" title="Agent 1 Hierarchy Gross Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="agent1HierarchyWHT" title="Agent 1 Hierarchy WHT" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="agent1HierarchyNetCommission" title="Agent 1 Hierarchy Net Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			
			<!--  agent 2 hierarchy commission -->
			<ec:column property="agent2HierarchyGrossCommission" title="Agent 2 Hierarchy Gross Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="agent2HierarchyWHT" title="Agent 2 Hierarchy WHT" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="agent2HierarchyNetCommission" title="Agent 2 Hierarchy Net Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			--%>
	
			<ec:column property="hierarchyA1UserId" title="Hierarchy1 Level1 User ID" />
			<ec:column property="hierarchyA1GrossCom" title="Hierarchy1 Level1 Gross Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="hierarchyA1WHT" title="Hierarchy1 Level1 WHT" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="hierarchyA1Comm" title="Hierarchy1 Level1 Net Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>

			<ec:column property="hierarchyB1UserId" title="Hierarchy1 Level2 User ID" />
			<ec:column property="hierarchyB1GrossCom" title="Hierarchy1 Level2 Gross Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="hierarchyB1WHT" title="Hierarchy1 Level2 WHT" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="hierarchyB1Comm" title="Hierarchy1 Level2 Net Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>

			<ec:column property="hierarchyC1UserId" title="Hierarchy1 Level3 User ID" />
			<ec:column property="hierarchyC1GrossCom" title="Hierarchy1 Level3 Gross Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="hierarchyC1WHT" title="Hierarchy1 Level3 WHT" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="hierarchyC1Comm" title="Hierarchy1 Level3 Net Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>

			<ec:column property="hierarchyD1UserId" title="Hierarchy1 Level4 User ID" />
			<ec:column property="hierarchyD1GrossCom" title="Hierarchy1 Level4 Gross Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="hierarchyD1WHT" title="Hierarchy1 Level4 WHT" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="hierarchyD1Comm" title="Hierarchy1 Level4 Net Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>

			<ec:column property="hierarchyA2UserId" title="Hierarchy2 Level1 User ID" />
			<ec:column property="hierarchyA2GrossCom" title="Hierarchy2 Level1 Gross Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="hierarchyA2WHT" title="Hierarchy2 Level1 WHT" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="hierarchyA2Comm" title="Hierarchy2 Level1 Net Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>

			<ec:column property="hierarchyB2UserId" title="Hierarchy2 Level2 User ID" />
			<ec:column property="hierarchyB2GrossCom" title="Hierarchy2 Level2 Gross Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="hierarchyB2WHT" title="Hierarchy2 Level2 WHT" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="hierarchyB2Comm" title="Hierarchy2 Level2 Net Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>

			<ec:column property="hierarchyC2UserId" title="Hierarchy2 Level3 User ID" />
			<ec:column property="hierarchyC2GrossCom" title="Hierarchy2 Level3 Gross Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="hierarchyC2WHT" title="Hierarchy2 Level3 WHT" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="hierarchyC2Comm" title="Hierarchy2 Level3 Net Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>

			<ec:column property="hierarchyD2UserId" title="Hierarchy2 Level4 User ID" />
			<ec:column property="hierarchyD2GrossCom" title="Hierarchy2 Level4 Gross Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="hierarchyD2WHT" title="Hierarchy2 Level4 WHT" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="hierarchyD2Comm" title="Hierarchy2 Level4 Net Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			
			<!-- other commission -->
			<ec:column property="otherGrossCommission" title="Other Gross Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="otherWHT" title="Other WHT" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="otherNetCommission" title="Other Net Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<!--  total commission -->
			<ec:column property="totalGrossCommission" title="Total Gross Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="totalWHT" title="Total WHT" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="totalNetCommission" title="Total Net Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="processingStatusName" title="Status"></ec:column>
		</ec:row>
	</ec:table>
	

		<script language="javascript" type="text/javascript">

			document.forms[0].transactionCode.focus();

			function openTransactionWindow(transactionCode)
			{
		        var popupWidth = 550;
				var popupHeight = 350;
				var popupLeft = (window.screen.width - popupWidth)/2;
				var popupTop = (window.screen.height - popupHeight)/2;
		        newWindow=window.open('p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode='+transactionCode,'TransactionSummary', 'width='+popupWidth+',height='+popupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
			    if(window.focus) newWindow.focus();
			    return false;
			}

			Calendar.setup(
      		{
		       inputField  : "startDate", // id of the input field
		       button      : "sDate"    // id of the button
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
      	<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>
	</body>
</html>
