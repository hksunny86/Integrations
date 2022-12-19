<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.*'%>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<%@ page import='com.inov8.microbank.common.util.FinancialInstitutionConstants'%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>

<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<c:set var="veriflyFinancialInstitution"><%=FinancialInstitutionConstants.VERIFLY_FINANCIAL_INSTITUTION %></c:set>

<html>
	<head>
<meta name="decorator" content="decorator">
	<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
	  <script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
      <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
      <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
      <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
		<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
		<script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
		<link rel="stylesheet"
			href="${contextPath}/styles/extremecomponents.css"
			type="text/css">
		<%@include file="/common/ajax.jsp"%>
		<meta name="title" content="Book Me Transaction Details" id="<%=ReportIDTitleEnum.TransactionDetailReport.getId()%>" />
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
		<%
		String detailBtnPermission = PortalConstants.ADMIN_GP_READ;
		 %>
		
	</head>
	<body bgcolor="#ffffff">
 		<div id="successMsg" class="infoMsg" style="display:none;"></div>
		<div id="errorMsg" class="errorMsg" style="display:none;"></div>
		<input id="message" value="777" type="hidden" />
			<html:form name="transactionDetailI8Form"
				commandName="bookMeTransactionDetaili8ViewModel" method="post"
				action="p_bookmetransactiondetailmanagement.html" onsubmit="return validateForm(this)" >
		<table width="950px" border="0">
				<tr>
					<td class="formText" width="25%" align="right">
						Transaction ID:
					</td>
					<td align="left" width="25%" >
						<html:input path="transactionCode" cssClass="textBox" tabindex="5" maxlength="50" onkeypress="return maskNumber(this,event)"/>
					</td>
					<td class="formText" align="right" width="25%">
						Customer ID:
					</td>
					<td align="left">
						<html:input path="customerId" cssClass="textBox" maxlength="11" tabindex="2" onkeypress="return maskNumber(this,event)"/>
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">
						Customer Mobile:
					</td>
					<td align="left">
						<html:input path="customerMobileNumber" id="customerMobileNumber" cssClass="textBox" maxlength="11" tabindex="2" onkeypress="return maskNumber(this,event)"/>

					</td>
					<td class="formText" align="right">
						Customer CNIC:
					</td>
					<td align="left" >
						<html:input path="customerCnic" id="customerCnic" cssClass="textBox" maxlength="13" tabindex="4" onkeypress="return maskNumber(this,event)"/>
					</td>
				</tr>
			<tr>
				<td class="formText" align="right">
					Book Me Customer Mobile:
				</td>
				<td align="left">
					<html:input path="bookMeCustomerMobileNo" id="bookMeCustomerMobileNo" cssClass="textBox" maxlength="11" tabindex="2" onkeypress="return maskNumber(this,event)"/>

				</td>
				<td class="formText" align="right">
					Book Me Customer CNIC:
				</td>
				<td align="left" >
					<html:input path="bookMeCustomerCnic" id="bookMeCustomerCnic" cssClass="textBox" maxlength="13" tabindex="4" onkeypress="return maskNumber(this,event)"/>
				</td>
			</tr>
				<tr>
					<td class="formText" align="right">
						Book Me Transaction ID:
					</td>
					<td align="left">
						<html:input path="bookMeTransactionId" id="bookMeTransactionId" cssClass="textBox" tabindex="5" maxlength="50" onkeypress="return maskNumber(this,event)"/>
					</td>
					<td class="formText" align="right">
						Product:
					</td>
					<td align="left">
						<html:select path="productId" cssClass="textBox" tabindex="10" >
							<html:option value="">---All---</html:option>
							<c:if test="${productModelList != null}">
								<html:options items="${productModelList}" itemValue="productId" itemLabel="name"/>
							</c:if>
						</html:select>
					</td>				
				</tr>


			<tr>
			<td class="formText" align="right">
				Consumer Number:
			</td>
			<td align="left">
				<html:input path="consumerNo" id="consumerNo" tabindex="6" cssClass="textBox" maxlength="200" />
			</td>
			<td class="formText" align="right">
				STAN:
			</td>
			<td align="left">
				<html:input path="stan" id="stan" tabindex="6" cssClass="textBox" maxlength="200" />
			</td>
		</tr>
			<tr>
				<td class="formText" align="right">
					 Channel:
				</td>
				<td align="left">
					<html:select path="senderDeviceTypeId" cssClass="textBox" tabindex="7" >
						<html:option value="">---All---</html:option>
						<c:if test="${deviceTypeModelList != null}">
							<html:options items="${deviceTypeModelList}" itemValue="deviceTypeId" itemLabel="name"/>
						</c:if>
					</html:select>
				</td>
				<td class="formText" align="right">
					Debit Card Number:
				</td>
				<td align="left">
					<html:input path="senderDebitCardNumber" id="senderDebitCardNumber" tabindex="6" cssClass="textBox" maxlength="200" />
				</td>
			</tr>

			<tr>
				<td class="formText" align="right">
					Transaction Status:
				</td>
				<td align="left">
					<html:select path="processingStatusId" cssClass="textBox" tabindex="7" >
						<html:option value="">---All---</html:option>
						<html:option value="1">Complete</html:option>
						<html:option value="2">Failed</html:option>
					</html:select>
				</td>

                <td class="formText" align="right">
                    Book Me Status:
                </td>
                <td align="left">
                    <html:input path="bookMeStatus" id="bookMeStatus" tabindex="6" cssClass="textBox" maxlength="200" />
                </td>
			</tr>
            <tr>
                <td class="formText" align="right">
                    Created On From Date:
                </td>
                <td align="left">
                    <html:input path="createdOn" id="createdOnFromDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
                    <img id="CreatedFromDate" tabindex="7" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                    <img id="CreatedFromDate" tabindex="8" title="Clear Date" name="popcal" onclick="javascript:$('createdOnFromDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
                </td>
                <td class="formText" align="right">
                    Created On To Date:
                </td>
                <td align="left">
                    <html:input path="createdOnToDate" id="createdOnToDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
                    <img id="CreatedToDate" tabindex="9" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                    <img id="CreatedToDate" tabindex="10" title="Clear Date" name="popcal" onclick="javascript:$('createdOnToDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
                </td>
            </tr>
				<tr>
					<td class="formText" align="right">
					</td>
					<td align="left">
						<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
							   value="<%=PortalConstants.ACTION_RETRIEVE%>">
						<input name="_search" type="submit" class="button" value="Search" tabindex="13"/> 
						<input name="reset" type="reset" 
							onclick="javascript: window.location='p_bookmetransactiondetailmanagement.html?actionId=${retriveAction}'"
							class="button" value="Cancel" tabindex="14" />
					</td>
					<td class="formText" align="right">
					</td>
					<td align="left">
					</td>
				</tr>
			</table>
		</html:form>

		<ec:table items="bookMei8List" var="bookMei8List" action="${contextPath}/p_bookmetransactiondetailmanagement.html" title="" retrieveRowsCallback="limit"
				  filterRowsCallback="limit" sortRowsCallback="limit" filterable="false">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Book Me Report.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Book Me Report.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da" headerTitle="View Transactions"
							  fileName="Book Me Report.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Book Me Report.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>
				<ec:column property="transactionCode" filterable="false" title="Transaction ID" escapeAutoFormat="true"/>
				<ec:column property="customerId" filterable="false" title="Sender Agent / Customer ID" escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="customerMobileNumber" title="Agent / Customer Mobile" escapeAutoFormat="true" />
				<%--<ec:column property="channel" title="Sender Customer Channel" escapeAutoFormat="true" />--%>
				<ec:column property="customerCnic" title="Agent / Customer CNIC" escapeAutoFormat="true" />
				<ec:column property="bookMeTransactionId" title="Book Me Transaction Id" escapeAutoFormat="true" />
				<ec:column property="bookMeCustomerMobileNo" title="Book Me Customer Mobile No" escapeAutoFormat="true" />
				<ec:column property="bookMeCustomerCnic" title="Book Me Customer Cnic" escapeAutoFormat="true" />
				<ec:column property="createdOn" cell="date" format="dd/MM/yyyy hh:mm a" filterable="false" title="Transaction Date"/>
				<ec:column property="createdOn" cell="date" format="dd/MM/yyyy hh:mm a" filterable="false" title="Transaction Completed On"/>
				<ec:column property="transactionType" filterable="false"  title="Transaction Type" />
				<ec:column property="transactionAmount" filterable="false" title="Amount" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
				<ec:column property="inclusiveCharges" filterable="false" title="Inclusive Charges" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
				<ec:column property="exclusiveCharges" filterable="false" title="Exclusive Charges" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
				<ec:column property="totalAmount" filterable="false" title="Total Customer Charges" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
				<ec:column property="fed" filterable="false" title="FED Charges" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
				<ec:column property="wht" filterable="false" title="W. Holding Tax" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
				<ec:column property="totalNetFeeAfterFed" filterable="false" title="Total Net Fee After FED" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
				<ec:column property="bankCommission" filterable="false" title="Bank Commission" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
				<ec:column property="fedOnBankCommission" filterable="false" title="FED On Bank Commission" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
				<ec:column property="grossSenderAgentCommission" filterable="false" title="Gross Sender Agent Commission" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
				<ec:column property="fedShareSenderAgentCommission" filterable="false" title="FED On Gross Sender Agent Commission" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
				<ec:column property="whtSenderAgentCommission" filterable="false" title="WHT On Sender Agent Commission" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
				<ec:column property="netSenderAgentCommission" filterable="false" title="Net Sender Agent Commission" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
				<ec:column property="grossReceiverAgentCommission" filterable="false" title="Gross Receiver Agent Commission" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
				<ec:column property="fedShareReceiverAgentCommission" filterable="false" title="FED Share On Receiver Agent Commission" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
				<ec:column property="whtShareReceiverAgentCommission" filterable="false" title="WHT On Receiver Agent Commission" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
				<ec:column property="netReceiverAgentCommission" filterable="false" title="Net Receiver Agent Commission" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
				<ec:column property="grossSenderParentAgentCommission" filterable="false" title="Gross Sender Parent Agent Commission" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
				<ec:column property="fedShareSenderParentAgentCommission" filterable="false" title="FED Share Sender Parent Agent Commission" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
				<ec:column property="whtShareSenderParentAgentCommission" filterable="false" title="WHT On Sender Parent Agent Commission" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
				<ec:column property="netSenderParentAgentCommission" filterable="false" title="Net Sender Parent Agent Commission" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
				<ec:column property="grossReceiverParentCommission" filterable="false" title="Gross Receiver Parent Agent Commission" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
				<ec:column property="fedShareOnReceiverParentCommission" filterable="false" title="FED Share On Receiver Parent Agent Commission" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
				<ec:column property="whtReceiverParentCommission" filterable="false" title="WHT On Receiver Parent Agent Commission" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
				<ec:column property="netReceiverParentCommission" filterable="false" title="Net Receiver Parent Agent Commission" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
				<ec:column property="consumerNo" title="Consumer #" escapeAutoFormat="true"/>
				<ec:column property="billDueDate" cell="date" format="dd/MM/yyyy" filterable="false" title="Bill Due Date"/>
				<ec:column property="stan" filterable="false"  title="STAN" />
				<ec:column property="transactionStatus" title="Transaction Status" escapeAutoFormat="true" />
				<ec:column property="failureReason" filterable="false"  title="Failure Reason" />
				<ec:column property="purposeOfPayment" filterable="false"  title="Purpose of Payment" />
				<ec:column property="reversedBy" filterable="false"  title="Reversed By" />
				<ec:column property="reversedOn" cell="date" format="dd/MM/yyyy hh:mm a" filterable="false" title="Reversed On"/>
				<ec:column property="comments" filterable="false"  title="Comments" />
				<ec:column property="transactionType" filterable="false"  title="Transaction Type" />
				<ec:column property="terminalId" filterable="false"  title="Terminal Id" />
				<ec:column property="channelId" filterable="false"  title="Channel Id" />
				<ec:column property="nIfq" filterable="false"  title="NADRA NFIQ" />
				<ec:column property="productName" title="Product" escapeAutoFormat="true"/>
				<ec:column property="mintaieCount" filterable="false"  title="NADRA Minutaei" />
				<ec:column property="bookMeStatus" title="Book Me Status" escapeAutoFormat="true" />
				<ec:column property="transactionPurpose" filterable="false"  title="Transaction Purpose" />
				<%--<ec:column property="reversedDate" cell="date" format="dd/MM/yyyy hh:mm a" filterable="false" title="Reversed On"/>--%>
				<ec:column property="segment" filterable="false"  title="Segment" />
			</ec:row>
		</ec:table>

		<%--<ajax:select source="supplierId" target="productId"--%>
		<%--baseUrl="${contextPath}/p_refData.html"--%>
		<%--parameters="supplierId={supplierId},rType=1,actionId=${retriveAction}" errorFunction="error"/>--%>


		<script language="javascript" type="text/javascript">

			<%--document.forms[0].mfsId.focus();--%>
			<%--function openTransactionWindow(transactionCode)--%>
			<%--{--%>
		        <%--var popupWidth = 550;--%>
				<%--var popupHeight = 350;--%>
				<%--var popupLeft = (window.screen.width - popupWidth)/2;--%>
				<%--var popupTop = (window.screen.height - popupHeight)/2;--%>
		        <%--newWindow=window.open('p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode='+transactionCode,'TransactionSummary','width='+popupWidth+',height='+popupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');--%>
			    <%--if(window.focus) newWindow.focus();--%>
			    <%--return false;--%>
			<%--}--%>



            Calendar.setup(
                {
                    inputField  : "createdOnFromDate", // id of the input field
                    button      : "CreatedFromDate",    // id of the button
                }
            );
            Calendar.setup(
                {
                    inputField  : "createdOnToDate", // id of the input field
                    button      : "CreatedToDate",    // id of the button
                    isEndDate: true
                }
            );
			
		    // Calendar.setup( {inputField  : "UpdtFromStartDate",button : "fromStartDate",} );
		    // Calendar.setup( {inputField  : "UpdtToStartDate",button : "toStartDate", isEndDate: true } );
			
      	</script>
     	<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>
	</body>
</html>