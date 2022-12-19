<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.*,com.inov8.microbank.common.util.PortalConstants'%>
<%@page import="com.inov8.microbank.server.service.smssendermodule.ResendSmsStrategyEnum"%>
<%@page import="com.inov8.microbank.common.util.IssueTypeStatusConstantsInterface"%>
<%@ page import='com.inov8.microbank.common.util.FinancialInstitutionConstants'%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE%></c:set>
<c:set var="defaultAction"><%=PortalConstants.ACTION_DEFAULT%></c:set>
<c:set var="INITIATOR_SMS_STRATEGY"><%=ResendSmsStrategyEnum.INITIATOR_SMS_STRATEGY%></c:set>
<c:set var="RECIPIENT_SMS_STRATEGY"><%=ResendSmsStrategyEnum.RECIPIENT_SMS_STRATEGY%></c:set>
<c:set var="WALKIN_DEPOSITOR_SMS_STRATEGY"><%=ResendSmsStrategyEnum.WALKIN_DEPOSITOR_SMS_STRATEGY%></c:set>
<c:set var="WALKIN_BENEFICIARY_SMS_STRATEGY"><%=ResendSmsStrategyEnum.WALKIN_BENEFICIARY_SMS_STRATEGY%></c:set>
<html>
<head>
<meta name="decorator" content="decorator">
<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
<script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
<script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
<script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
<script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
<script type="text/javascript" src="${contextPath}/scripts/jquery-1.7.2.min.js"></script>
<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css" />
<link type="text/css" rel="stylesheet" href="styles/ajaxtags.css" />
<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
<style type="text/css">
.eXtremeTable input.button {
	width: 125px !important;
}
</style>
<%@include file="/common/ajax.jsp"%>
<meta name="title" content="Handler Transaction History" />
<script language="javascript" type="text/javascript">
	var jq=$.noConflict();
	var serverDate ="<%=PortalDateUtils.getServerDate()%>";
	
	function handleResendSmsResponse()
	{
		var response = document.getElementById('smsContent').innerHTML;
		alert( response );
	}
	
	function error(request)
	{
		alert("An unknown error has occured. Please contact with the administrator for more details");
	}
	      
	function openTransactionDetailWindow(transactionCode)
	{
	    var action = 'allpaytransactiondetailmanagement.html?transactionCodeId='+transactionCode;
	          newWindow = window.open( action , 'TransactionDetail', 'width=550,height=350,menubar=no,toolbar=no,left=150,top=150,directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
		if(window.focus) newWindow.focus();
	    	return false;
	}
	  
  </script>
<%
	String smsPermission = PortalConstants.CSR_GP_UPDATE;
	smsPermission += "," + PortalConstants.RET_GP_UPDATE;
	smsPermission += "," + PortalConstants.PG_GP_UPDATE;
	smsPermission += "," + PortalConstants.RESEND_SMS_UPDATE;
	smsPermission += "," + PortalConstants.ADMIN_GP_UPDATE;

	String quickAgentTxHistPermission = PortalConstants.RET_GP_READ;
	quickAgentTxHistPermission += ","
			+ PortalConstants.HOME_PAGE_QUICK_AGNT_TX_HIST_READ;
%>
</head>
<body bgcolor="#ffffff" onunload="javascript:closeChild();">
	<div id="smsContent" style="display:none;"></div>
	<c:if test="${not empty messages}">
		<div class="infoMsg" id="successMessages">
			<c:forEach var="msg" items="${messages}">
				<c:out value="${msg}" escapeXml="false" />
				<br />
			</c:forEach>
		</div>
		<c:remove var="messages" scope="session" />
	</c:if>
		<html:form name='customertransactionForm' commandName="handlerTransactionHistoryVO" method="post"
			action="p_handlerTransactionHistory.html" onsubmit="return validateForm(this)">
			<table width="750px" border="0">
			<tr>
				<td class="formText" align="right" width="18%">Sender Mobile No:</td>
				<td align="left"><html:input path="saleMobileNo" id="saleMobileNo" cssClass="textBox" maxlength="11" tabindex="1"
						onkeypress="return maskNumber(this,event)" /></td>
				<td class="formText" align="right">Recipient Mobile No:</td>
				<td align="left"><html:input path="recipientMobileNo" id="recipientMobileNo" cssClass="textBox" maxlength="11" tabindex="2"
						onkeypress="return maskNumber(this,event)" /></td>
			</tr>
			<tr>
				<td class="formText" align="right">Transaction ID:</td>
				<td align="left"><html:input path="transactionCode" id="transactionCode" cssClass="textBox" tabindex="3" maxlength="50"
						onkeypress="return maskNumber(this,event)" /></td>
				<td class="formText" align="right">Transaction Status:</td>
				<td align="left"><html:select path="processingStatusName" cssClass="textBox" tabindex="4">
						<html:option value="">---All---</html:option>
						<c:if test="${supplierProcessingStatusList != null}">
							<html:options items="${supplierProcessingStatusList}" itemValue="name" itemLabel="name" />
						</c:if>
					</html:select></td>
			</tr>
			<tr>
				<td class="formText" align="right">Supplier:</td>
				<td align="left"><html:select path="supplierid" cssClass="textBox" tabindex="5">
						<html:option value="">---All---</html:option>
						<c:if test="${supplierModelList != null}">
							<html:options items="${supplierModelList}" itemValue="supplierId" itemLabel="name" />
						</c:if>
					</html:select></td>
				<td class="formText" align="right">Product:</td>
				<td align="left"><html:select path="productId" cssClass="textBox" tabindex="6">
						<html:option value="">---All---</html:option>
						<c:if test="${productModelList != null}">
							<html:options items="${productModelList}" itemValue="productId" itemLabel="name" />
						</c:if>
					</html:select></td>
			</tr>
			<tr>
				<td class="formText" align="right">Start Date:</td>
				<td align="left"><html:input path="startDate" id="startDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="7" /> <img
					id="sDate" tabindex="7" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" /> <img id="sDate" tabindex="8"
					title="Clear Date" name="popcal" onclick="javascript:$('startDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png"
					border="0" /></td>
				<td class="formText" align="right">End Date:</td>
				<td align="left"><html:input path="endDate" id="endDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10" /> <img id="eDate"
					tabindex="9" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" /> <img id="eDate" tabindex="10"
					title="Clear Date" name="popcal" onclick="javascript:$('endDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png"
					border="0" /></td>
			</tr>
			<tr>
				<td class="formText" align="right"></td>
				<td align="left"><input name="_search" type="submit" class="button" value="Search" tabindex="11" /> <input name="reset" type="reset"
					onclick="javascript: window.location='p_handleraccountdetails.html?handlerId=${handlerTransactionHistoryVO.handlerPk}'" class="button" value="Cancel" tabindex="12" /></td>
				<td class="formText" align="right"></td>
				<td align="left"></td>
			</tr>
			</table>
			<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
			<html:hidden path="handlerMfsId" value="${handlerMfsId}"/>
			<html:hidden path="handlerPk" value="${handlerPk}"/>
		</html:form>
		
	<ec:table filterable="false" items="handlerTransactionModelList" var="transactionDetInfoListViewModel" retrieveRowsCallback="limit"
		filterRowsCallback="limit" sortRowsCallback="limit"
		action="${pageContext.request.contextPath}/allpayagenttransactiondetails.html?actionId=2&agentId=${param.agentId}" title="">
		<authz:authorize ifAnyGranted="<%=PortalConstants.EXPORT_XLS_READ%>">
			<ec:exportXls fileName="Handler Transactions.xls" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
			<ec:exportXlsx fileName="Handler Transactions.xlsx" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.EXPORT_PDF_READ%>">
			<ec:exportPdf headerBackgroundColor="#b6c2da" headerTitle="Handler Transactions" fileName="Handler-Transactions.pdf" tooltip="Export PDF"
				view="com.inov8.microbank.common.util.CustomPdfView" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
			<ec:exportCsv fileName="Handler Transactions.csv" tooltip="Export CSV"></ec:exportCsv>
		</authz:authorize>
		<ec:row>
			<c:set var="transactionId">
				<security:encrypt strToEncrypt="${transactionDetInfoListViewModel.transactionId}" />
			</c:set>
			<ec:column property="createdOn" filterable="false" title="Transaction Date" cell="date" format="dd/MM/yyyy hh:mm a" />
			<ec:column property="transactionCode" title="Transaction ID" escapeAutoFormat="true" />
			<ec:column property="suppliername" title="Supplier" />
			<ec:column property="productname" title="Product" />
			<ec:column property="deviceType" title="Channel" />
			<ec:column property="agent1Id" title="Sender Agent ID" escapeAutoFormat="true" />
			<ec:column property="mfsId" title="Sender Id" escapeAutoFormat="true" />
			<ec:column property="saleMobileNo" title="Sender Mobile No" escapeAutoFormat="true" />
			<ec:column property="senderCnic" title="Sender CNIC" escapeAutoFormat="true" style="text-align: center" />
			<ec:column property="paymentMode" title="Sender Payment Mode" />
			<ec:column property="senderAccountNick" title="Sender Account Nick" />
			<ec:column property="bankAccountNo" title="Sender Account No" escapeAutoFormat="true" />
			<ec:column property="amount" filterable="false" title="Amount" calc="total" calcTitle="Total:" cell="currency" format="0.00"
				style="text-align: right" />
			<ec:column property="serviceChargesExclusive" filterable="false" title="Service Charges" calc="total" calcTitle="Total:" cell="currency"
				format="0.00" style="text-align: right" />
			<ec:column property="totalChargedFromCustomer" filterable="false" title="Total Charged From Customer" calc="total" calcTitle="Total:"
				cell="currency" format="0.00" style="text-align: right" />
			<ec:column property="agent2Id" title="Receiver Agent ID" escapeAutoFormat="true" />
			<ec:column property="recipientMfsId" title="Recipient ID" escapeAutoFormat="true" />
			<ec:column property="recipientMobileNo" title="Recipient Mobile No" escapeAutoFormat="true" />
			<ec:column property="recipientCnic" title="Recipient CNIC" escapeAutoFormat="true" style="text-align: center" />
			<ec:column property="recipientAccountNick" title="Recipient Account Nick" />
			<ec:column property="recipientAccountNo" title="Recipient Account No" escapeAutoFormat="true" />
			<ec:column property="processingStatusName" title="Status" />
			<ec:column property="cashDepositorCnic" title="Cash Depositor CNIC" escapeAutoFormat="true" style="text-align: center" />
			<ec:column alias="initiatorSms" title="Resend SMS" style="text-align: center" sortable="false" filterable="false" viewsAllowed="html">
				<authz:authorize ifAnyGranted="<%=smsPermission%>">
					<c:choose>
						<c:when test="${transactionDetInfoListViewModel.initiatorResendSmsAvailable}">
							<input type="button" class="button" value="${transactionDetInfoListViewModel.initiatorResendSmsBtnLabel}" id="initiatorSms${transactionId}"
								onclick="openResendSmsWindow('${transactionId}','${INITIATOR_SMS_STRATEGY}','${param.appUserId}','${transactionDetInfoListViewModel.transactionCode}')" />
						</c:when>
						<c:otherwise>
							<input type="button" class="button" value="N/A" disabled="disabled" />
						</c:otherwise>
					</c:choose>
				</authz:authorize>
				<authz:authorize ifNotGranted="<%=smsPermission%>">
					<input type="button" class="button" value="N/A" disabled="disabled" />
				</authz:authorize>
			</ec:column>
			<ec:column alias="recipientSms" title="Resend SMS" style="text-align: center" sortable="false" filterable="false" viewsAllowed="html">
				<authz:authorize ifAnyGranted="<%=smsPermission%>">
					<c:choose>
						<c:when test="${transactionDetInfoListViewModel.recipientResendSmsAvailable}">
							<input type="button" class="button" value="${transactionDetInfoListViewModel.recipientResendSmsBtnLabel}" id="recipientSms${transactionId}"
								onclick="openResendSmsWindow('${transactionId}','${RECIPIENT_SMS_STRATEGY}','${param.appUserId}','${transactionDetInfoListViewModel.transactionCode}')" />
						</c:when>
						<c:otherwise>
							<input type="button" class="button" value="N/A" disabled="disabled" />
						</c:otherwise>
					</c:choose>
				</authz:authorize>
				<authz:authorize ifNotGranted="<%=smsPermission%>">
					<input type="button" class="button" value="N/A" disabled="disabled" />
				</authz:authorize>
			</ec:column>
			<ec:column alias="walkInDepositorSms" title="Resend SMS" style="text-align: center" sortable="false" filterable="false" viewsAllowed="html">
				<authz:authorize ifAnyGranted="<%=smsPermission%>">
					<c:choose>
						<c:when test="${transactionDetInfoListViewModel.walkinDepositorSmsAvailable}">
							<input type="button" class="button" value="Depositor" id="walkInDepositorSms${transactionId}"
								onclick="openResendSmsWindow('${transactionId}','${WALKIN_DEPOSITOR_SMS_STRATEGY}','${param.appUserId}','${transactionDetInfoListViewModel.transactionCode}')" />
						</c:when>
						<c:otherwise>
							<input type="button" class="button" value="N/A" disabled="disabled" />
						</c:otherwise>
					</c:choose>
				</authz:authorize>
				<authz:authorize ifNotGranted="<%=smsPermission%>">
					<input type="button" class="button" value="N/A" disabled="disabled" />
				</authz:authorize>
			</ec:column>
			<ec:column alias="walkInBeneficiarySms" title="Resend SMS" style="text-align: center" sortable="false" filterable="false" viewsAllowed="html">
				<authz:authorize ifAnyGranted="<%=smsPermission%>">
					<c:choose>
						<c:when test="${transactionDetInfoListViewModel.walkinBeneficiarySmsAvailable}">
							<input type="button" class="button" value="Beneficiary" id="walkInBeneficiarySms${transactionId}"
								onclick="openResendSmsWindow('${transactionId}','${WALKIN_BENEFICIARY_SMS_STRATEGY}','${param.appUserId}','${transactionDetInfoListViewModel.transactionCode}')" />
						</c:when>
						<c:otherwise>
						<input type="button" class="button" value="N/A" disabled="disabled" />
						</c:otherwise>
					</c:choose>
				</authz:authorize>
				<authz:authorize ifNotGranted="<%=smsPermission%>">
					<input type="button" class="button" value="N/A" disabled="disabled" />
				</authz:authorize>
			</ec:column>
		</ec:row>
	</ec:table>
	<ajax:select source="supplierid" target="productId" baseUrl="${contextPath}/p_refData.html"
		parameters="supplierId={supplierid},rType=1,actionId=${retriveAction}" errorFunction="error" />
	<script type="text/javascript">
			
			var childWindow;   
			
			function openResendSmsWindow(tId,strategy,appUserId,txCode){
				var popupWidth = 550;
				var popupHeight = 350;	       
				var popupLeft = (window.screen.width - popupWidth)/2;
				var popupTop = (window.screen.height - popupHeight)/2;		
				var url = 'p-resendsmsform.html?tId='+tId+'&resendSmsStrategy='+strategy+'&appUserId='+appUserId+'&txCode='+txCode;
		        newWindow=window.open(url,'Action Detail Window', 'width='+popupWidth+',height='+popupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
			    if(window.focus) newWindow.focus();
			    return false;		
			}

		function closeChild() {
			try {
				if (childWindow != undefined) {
					childWindow.close();
					childWindow = undefined;
				}
			} catch (e) {
			}
		}

		Calendar.setup({
			inputField : "startDate", // id of the input field
			button : "sDate" // id of the button
		});
		Calendar.setup({
			inputField : "endDate", // id of the input field
			button : "eDate", // id of the button
			isEndDate : true
		});
	</Script>
	<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>
</body>
</html>
