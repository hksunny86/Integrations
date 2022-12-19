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
		<meta name="title" content="Sender Redeem Transaction Detail"  id="<%=ReportIDTitleEnum.SenderRedeemTransactionDetail.getId()%>"  />
      <script language="javascript" type="text/javascript">
		var jq=$.noConflict();
		var serverDate ="<%=PortalDateUtils.getServerDate()%>";
		var username = "<%=UserUtils.getCurrentUser().getUsername()%>";
        var appUserId= "<%=UserUtils.getCurrentUser().getAppUserId()%>";
        var email= "<%=UserUtils.getCurrentUser().getEmail()%>";
      
	      function openTransactionDetailWindow(transactionCode)
		  {
		      var popupWidth = 550;
			  var popupHeight = 350;
			  var popupLeft = (window.screen.width - popupWidth)/2;
			  var popupTop = (window.screen.height - popupHeight)/2;
		      var action = 'p_showtransactiondetail.html?actionId='+${retriveAction}+'&transactionCodeId='+transactionCode+'&isMfs=true';
              newWindow = window.open( action , 'TransactionDetail','width='+popupWidth+',height='+popupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
              if(window.focus) newWindow.focus();
                    return false;
		  }
      
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
	
		<div id="rsp" class="ajaxMsg"></div>
	
		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" /><br/>
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>
			<html:form name="transactionDetailI8Form"
				commandName="extendedTransactionDetailPortalListModel" method="post"
				action="p_searchSenderRedeemTransactionDetail.html" onsubmit="return validateForm(this)" >
		<table width="750px" border="0">
		<tr>
					<td class="formText" align="right">
						Transaction ID:
					</td>
					<td align="left">
						<html:input path="transactionCode" id="transactionCode" cssClass="textBox" tabindex="1" maxlength="50" onkeypress="return maskNumber(this,event)"/> 
					</td>
				</tr>
				<tr>
					<td class="formText" width="18%" align="right">
						Sender ID:
					</td>
					<td align="left" width="32%" >
						<html:input path="mfsId" id="mfsId" cssClass="textBox" maxlength="50" tabindex="2" onkeypress="return maskNumber(this,event)"/> 
					</td>
					<td class="formText" align="right" width="18%">
						Sender Mobile No:
					</td>
					<td align="left">
						<html:input path="saleMobileNo" id="saleMobileNo" cssClass="textBox" maxlength="11" tabindex="3" onkeypress="return maskNumber(this,event)"/> 
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">
						Recipient ID:
					</td>
					<td align="left">
						<html:input path="recipientMfsId" id="recipientMfsId" cssClass="textBox" maxlength="50" tabindex="4" onkeypress="return maskNumber(this,event)"/> 
					</td>
					<td class="formText" align="right">
						Recipient Mobile No:
					</td>
					<td align="left" >
						<html:input path="recipientMobileNo" id="recipientMobileNo" cssClass="textBox" maxlength="11" tabindex="5" onkeypress="return maskNumber(this,event)"/> 
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">
						Supplier:
					</td>
					<td align="left">
						<html:select path="supplierId" cssClass="textBox" tabindex="6" >
							<html:option value="">---All---</html:option>
							<c:if test="${supplierModelList != null}">
								<html:options items="${supplierModelList}" itemValue="supplierId" itemLabel="name"/>
							</c:if>
						</html:select>
					</td>
					<td class="formText" align="right">
						Product:
					</td>
					<td align="left">
						<html:select path="productId" cssClass="textBox" tabindex="7" >
							<html:option value="">---All---</html:option>
							<html:option value="50011">Send Money</html:option>
							<html:option value="50010">Wallet to CNIC</html:option>
						</html:select>
					</td>				
				</tr>
				<tr>
					<td class="formText" align="right">
						Start Date:
					</td>
					<td align="left">
				        <html:input path="startDate" id="startDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
							<img id="sDate" tabindex="11" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
							<img id="sDate" tabindex="12" title="Clear Date" name="popcal"  onclick="javascript:$('startDate').value=''"   align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
					<td class="formText" align="right">
						End Date:
					</td>
					<td align="left">
					     <html:input path="endDate" id="endDate"  readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
					     <img id="eDate" tabindex="13" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
					     <img id="eDate" tabindex="14" title="Clear Date" name="popcal"  onclick="javascript:$('endDate').value=''"   align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
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
							onclick="javascript: window.location='p_searchSenderRedeemTransactionDetail.html?actionId=${retriveAction}'"
							class="button" value="Cancel" tabindex="14" />
					</td>
					<td class="formText" align="right">
					</td>
					<td align="left">
					</td>
				</tr>
			</table>
		</html:form>
		
		<ec:table items="transactionDetailPortalList" var="transactionDetailPortalModel"
		action="${contextPath}/p_searchSenderRedeemTransactionDetail.html?actionId=${retriveAction}"
		title=""
          retrieveRowsCallback="limit"
          filterRowsCallback="limit"
          sortRowsCallback="limit"
          filterable="false"		
		>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Sender Redeem Transaction Details.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Sender Redeem Transaction Details.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<%-- <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="Sender Redeem Transaction Details Report"
					fileName="Sender Redeem Transaction Details.pdf" tooltip="Export PDF" />
			</authz:authorize> --%>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Sender Redeem  transactiondetaili8management.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>
 			<ec:column property="transactionCode" filterable="false" title="Transaction ID" escapeAutoFormat="true">
				  	<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
						<a href="p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode=${transactionDetailPortalModel.transactionCode}" onClick="return openTransactionWindow('${transactionDetailPortalModel.transactionCode}')">
						  ${transactionDetailPortalModel.transactionCode}
						</a>
					</authz:authorize>
					<authz:authorize ifNotGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
						${transactionDetailPortalModel.transactionCode}
					</authz:authorize>
				</ec:column>
				<ec:column property="agent1Id" filterable="false" title="Sender Agent ID" escapeAutoFormat="true"/>
				<ec:column property="senderAgentAccountNo" filterable="false" title="Sender Agent Account No." escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="mfsId" filterable="false" title="Sender ID" escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="saleMobileNo" filterable="false" title="Sender Mobile No." escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="senderCnic" filterable="false" title="Sender CNIC" escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="deviceType" title="Sender Channel"/>
				<ec:column property="paymentMode" filterable="false" title="Payment Mode" />
                <%--<ec:column property="bankName" filterable="false" title="Bank" /> --%>
				<%--<ec:column property="bankAccountNo" filterable="false" title="Sender Account No." escapeAutoFormat="true" /> --%>
				<ec:column property="createdOn" cell="date" format="dd/MM/yyyy hh:mm a" filterable="false" title="Transaction Execution Date"/>
				<ec:column property="updatedOn" cell="date" format="dd/MM/yyyy hh:mm a" filterable="false" title="Transaction Completion date"/>
				<ec:column property="supplierName" filterable="false" title="Supplier" />
				<ec:column property="productName" filterable="false" title="Product"/>
				<%-- <ec:column property="authorizationCode" filterable="false" title="Authorization Code" /> --%>
				<%-- <ec:column property="isManualOTPinString" title="Is Manual Code"/> --%>
				<ec:column property="agent2Id" filterable="false" title="Recipient Agent ID" escapeAutoFormat="true"/>								
				<ec:column property="recipientAgentAccountNo" title="Recipient Agent Account No."/>				
				<ec:column property="recipientMfsId" filterable="false" title="Recipient ID" escapeAutoFormat="true" style="text-align: center"/>
				<%-- <ec:column property="recipientAccountNo" filterable="false" title="Recipient Account No." escapeAutoFormat="true" style="text-align: center"/> --%>
				<ec:column property="recipientMobileNo" filterable="false" title="Recipient Mobile No." escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="recipientCnic" filterable="false" title="Recipient CNIC" escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="recipientDeviceType" title="Recipient Channel"/>
				<ec:column property="transactionAmount" filterable="false" title="Amount" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
				<ec:column property="inclusiveCharges" filterable="false" title="Inclusive Charges" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>			
				<ec:column property="exclusiveCharges" filterable="false" title="Exclusive Charges" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>				
 			    <ec:column property="totalAmount" filterable="false" title="Total Customer Charges" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>		
 			    <%--
				<ec:column property="toSupplier" filterable="false" title="To Supplier" calc="total" calcTitle="Total:"  cell="currency"  format="0.00"/>								
				<ec:column property="totalBankComm" filterable="false" title="To Bank" calc="total" calcTitle="Total:"  cell="currency"  format="0.00"/>								
 			    <ec:column property="mcpl" filterable="false" title="MCPL Commission" cell="currency" format="0.00" calc="total" calcTitle="Total:"/>
 			    --%>
				<ec:column property="fed" filterable="false" title="FED Charges" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>			
				<ec:column property="wht" filterable="false" title="W. Holding Tax" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>			
				<ec:column property="akblCommission" filterable="false" title="Bank Commission" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>			
				<ec:column property="agentCommission" filterable="false" title="Agent 1 Commission" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
				<ec:column property="agent2Commission" filterable="false" title="Agent 2 Commission" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
<%-- 				<ec:column property="franchise1Commission" filterable="false" title="Franchise 1 Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/> --%>
				<ec:column property="salesTeamCommission" filterable="false" title="Sales Team Commission" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
				<ec:column property="othersCommission" filterable="false" title="Other Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="cashDepositorCnic" filterable="false" title="Cash Depositor CNIC" style="text-align: center"/>
				<ec:column property="consumerNo" title="Consumer #" escapeAutoFormat="true"/>
				<ec:column property="billDueDate" cell="date" format="dd/MM/yyyy" filterable="false" title="Bill Due Date"/>
				<ec:column property="processingStatusName" filterable="false"  title="Status" />
				<ec:column property="failureReason" filterable="false"  title="Failure Reason" />
				<ec:column property="reversedByName" title="Reversed By"/>
				<ec:column property="reversedDate" title="Reversed On" cell="date" format="dd/MM/yyyy hh:mm a"/>
				<ec:column property="reversedComments" title="Reversal Comments"/>
   			
 		</ec:row>
		</ec:table>

		<%-- <ajax:select source="supplierId" target="productId"
		baseUrl="${contextPath}/p_refData.html"
		parameters="supplierId={supplierId},rType=1,actionId=${retriveAction}" errorFunction="error"/> --%>

		
		<script language="javascript" type="text/javascript">
		
			document.forms[0].mfsId.focus();
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