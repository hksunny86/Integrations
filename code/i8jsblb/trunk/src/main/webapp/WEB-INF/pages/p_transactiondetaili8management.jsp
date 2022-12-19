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
		<meta name="title" content="Transaction Details" id="<%=ReportIDTitleEnum.TransactionDetailReport.getId()%>" />
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
				action="p_transactiondetaili8management.html" onsubmit="return validateForm(this)" >
		<table width="950px" border="0">
				<tr>
					<td class="formText" width="25%" align="right">
						Sender ID:
					</td>
					<td align="left" width="25%" >
						<html:input path="mfsId" id="mfsId" cssClass="textBox" maxlength="50" tabindex="1" onkeypress="return maskNumber(this,event)"/> 
					</td>
					<td class="formText" align="right" width="25%">
						Sender Mobile No:
					</td>
					<td align="left">
						<html:input path="saleMobileNo" id="saleMobileNo" cssClass="textBox" maxlength="11" tabindex="2" onkeypress="return maskNumber(this,event)"/> 
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">
						Recipient ID:
					</td>
					<td align="left">
						<html:input path="recipientMfsId" id="recipientMfsId" cssClass="textBox" maxlength="50" tabindex="3" onkeypress="return maskNumber(this,event)"/> 
					</td>
					<td class="formText" align="right">
						Recipient Mobile No:
					</td>
					<td align="left" >
						<html:input path="recipientMobileNo" id="recipientMobileNo" cssClass="textBox" maxlength="11" tabindex="4" onkeypress="return maskNumber(this,event)"/> 
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">
						Transaction ID:
					</td>
					<td align="left">
						<html:input path="transactionCode" id="transactionCode" cssClass="textBox" tabindex="5" maxlength="50" onkeypress="return maskNumber(this,event)"/> 
					</td>
					<td class="formText" align="right">
						Authorization Code:
					</td>
					<td align="left">
						<html:input path="authorizationCode"   id="authorizationCode" tabindex="6" cssClass="textBox" maxlength="50" />
					</td>
				</tr>
			<tr>

				<td class="formText" align="right">
					FonePay Transaction Code:
				</td>
				<td align="left">
					<html:input path="fonepayTransactionCode"   id="fonepayTransactionCode" tabindex="6" cssClass="textBox" maxlength="50" />
				</td>
				<td class="formText" align="right">
					Terminal ID:
				</td>
				<td align="left">
					<html:input path="terminalId"   id="terminalId" tabindex="6" cssClass="textBox" maxlength="200" />
				</td>
			</tr>
            <tr>
                <td class="formText" align="right">
                    Webservice Product Name:
                </td>
                <td align="left">
                    <html:input path="externalProductName"   id="externalProductName" tabindex="6" cssClass="textBox" maxlength="200" />
                </td>
				<td class="formText" align="right">
					Transaction Status:
				</td>
				<td align="left">
					<html:select path="processingStatusId" cssClass="textBox" tabindex="6" >
						<html:option value="">---All---</html:option>
						<c:if test="${supplierProcessingStatusModelList != null}">
							<html:options items="${supplierProcessingStatusModelList}" itemValue="supProcessingStatusId" itemLabel="name"/>
						</c:if>
					</html:select>
				</td>
			</tr>
			<tr>
				<td class="formText" align="right">
					Sender Distributor:
				</td>
				<td align="left">
					<html:select path="senderDistributorId" cssClass="textBox" tabindex="7" >
						<html:option value="">---All---</html:option>
						<c:if test="${distributorModelList != null}">
							<html:options items="${distributorModelList}" itemValue="distributorId" itemLabel="name"/>
						</c:if>
					</html:select>
				</td>

				<td class="formText" align="right">
					STAN :
				</td>
				<td align="left">
					<html:input path="stan"   id="stan" tabindex="6" cssClass="textBox" maxlength="200" />
				</td>
            </tr>

			<tr>
				<td class="formText" align="right">
					Sender Channel:
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
					Recipient Channel:
				</td>
				<td align="left">
					<html:select path="recipientDeviceTypeId" cssClass="textBox" tabindex="8" >
						<html:option value="">---All---</html:option>
						<c:if test="${deviceTypeModelList != null}">
							<html:options items="${deviceTypeModelList}" itemValue="deviceTypeId" itemLabel="name"/>
						</c:if>
					</html:select>
				</td>
			</tr>
				<tr>
					<td class="formText" align="right">
						Supplier:
					</td>
					<td align="left">
						<html:select path="supplierId" cssClass="textBox" tabindex="9" >
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
				Segment:
			</td>
			<td align="left">
				<html:select path="segmentId" cssClass="textBox" tabindex="10" >
					<html:option value="">---All---</html:option>
					<c:if test="${segmentModelList != null}">
						<html:options items="${segmentModelList}" itemValue="segmentId" itemLabel="name"/>
					</c:if>
				</html:select>
			</td>
				<td class="formText" align="right">
					Consumer Number:
				</td>
				<td align="left">
				<html:input path="consumerNo" id="consumerNo" tabindex="6" cssClass="textBox" maxlength="200" />
			</td>
			</tr>
				<tr>
					<td class="formText" align="right">
						Transaction Date - Start:
					</td>
					<td align="left">
				        <html:input path="startDate" id="startDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
							<img id="sDate" tabindex="11" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
							<img id="sDate" tabindex="12" title="Clear Date" name="popcal"  onclick="javascript:$('startDate').value=''"   align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
					<td class="formText" align="right">
						Transaction Date - End:
					</td>
					<td align="left">
					     <html:input path="endDate" id="endDate"  readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
					     <img id="eDate" tabindex="13" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
					     <img id="eDate" tabindex="14" title="Clear Date" name="popcal"  onclick="javascript:$('endDate').value=''"   align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr>
			
		<tr>
            <td class="formText" align="right">
                Transaction Updated On - Start:
            </td>
            <td align="left">
                <html:input path="updatedOnStartDate" id="UpdtFromStartDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
                <img id="fromStartDate" tabindex="3" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                <img id="fromStartDate" tabindex="4" title="Clear Date" name="popcal" onclick="javascript:$('UpdtFromStartDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
            </td>
            <td class="formText" align="right">
                Transaction Updated On - End:
            </td>
            <td align="left">
                <html:input path="updatedOnEndDate" id="UpdtToStartDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
                <img id="toStartDate" tabindex="5" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                <img id="toStartDate" tabindex="6" title="Clear Date" name="popcal" onclick="javascript:$('UpdtToStartDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
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
							onclick="javascript: window.location='p_transactiondetaili8management.html?actionId=${retriveAction}'"
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
		action="${contextPath}/p_transactiondetaili8management.html?actionId=${retriveAction}"
		title=""
          retrieveRowsCallback="limit"
          filterRowsCallback="limit"
          sortRowsCallback="limit"
          filterable="false"		
		>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Transaction Details.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Transaction Details.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="Transaction Details"
					fileName="Transaction Details.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="transactiondetaili8management.csv" tooltip="Export CSV"></ec:exportCsv>
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
				<ec:column property="senderBVS" sortable="false" filterable="false" title="Sender BVS" escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="senderDistributorName" filterable="false" title="Sender Distributor" escapeAutoFormat="true"/>
				<ec:column property="senderServiceOPName" filterable="false" title="Sender Service OP" escapeAutoFormat="true"/>
				<ec:column property="sendingRegionName" filterable="false" title="Sender Region" escapeAutoFormat="true"/>
				<ec:column property="sendingAreaName" filterable="false" title="Sender Area" escapeAutoFormat="true"/>

				<ec:column property="paymentMode" filterable="false" title="Payment Mode" />
<%-- 				<ec:column property="bankName" filterable="false" title="Bank" /> --%>
				<ec:column property="bankAccountNo" filterable="false" title="Sender Account No." escapeAutoFormat="true" />
				<ec:column property="createdOn" cell="date" format="dd/MM/yyyy hh:mm a" filterable="false" title="Transaction Date"/>
				<ec:column property="updatedOn" cell="date" format="dd/MM/yyyy hh:mm a" filterable="false" title="Transaction Last Updated On"/>
				<ec:column property="supplierName" filterable="false" title="Supplier" />
				<ec:column property="productName" filterable="false" title="Product"/>
				<ec:column property="authorizationCode" filterable="false" title="Authorization Code" />
				<ec:column property="isManualOTPinString" title="Is Manual Code"/>
				<ec:column property="agent2Id" filterable="false" title="Recipient Agent ID" escapeAutoFormat="true"/>								
				<ec:column property="recipientAgentAccountNo" title="Recipient Agent Account No."/>				
				<ec:column property="recipientMfsId" filterable="false" title="Recipient ID" escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="recipientAccountNo" filterable="false" title="Recipient Account No." escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="recipientMobileNo" filterable="false" title="Recipient Mobile No." escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="debitCardNumber" filterable="false" title="Recipient Card No." escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="recipientCnic" filterable="false" title="Recipient CNIC" escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="recipientDeviceType" title="Recipient Channel"/>
				<ec:column property="receiverBVS" sortable="false" filterable="false" title="Recipient BVS" escapeAutoFormat="true" style="text-align: center"/>



				<ec:column property="receiverDistributorName" filterable="false" title="Receiver Distributor" escapeAutoFormat="true"/>
				<ec:column property="receiverServiceOPName" filterable="false" title="Receiver Service OP" escapeAutoFormat="true"/>
				<ec:column property="receivingRegionName" filterable="false" title="Receiver Region" escapeAutoFormat="true"/>
				<ec:column property="receivingAreaName" filterable="false" title="Receiver Area" escapeAutoFormat="true"/>

				<ec:column property="transactionAmount" filterable="false" title="Amount" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
				<ec:column property="inclusiveCharges" filterable="false" title="Inclusive Charges" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>			
				<ec:column property="exclusiveCharges" filterable="false" title="Exclusive Charges" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
 			    <ec:column property="totalAmount" filterable="false" title="Total Customer Charges" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
				<ec:column property="productThresholdCharges" filterable="false" title="Product Threshold Charges" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
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
				<ec:column property="blbSettlementCommission" filterable="false" title="BLB Settlement Commission" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
				<ec:column property="othersCommission" filterable="false" title="Other Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="cashDepositorCnic" filterable="false" title="Cash Depositor CNIC" style="text-align: center"/>
				<ec:column property="consumerNo" title="Consumer #" escapeAutoFormat="true"/>
				<ec:column property="billDueDate" cell="date" format="dd/MM/yyyy" filterable="false" title="Bill Due Date"/>
				<ec:column property="titleFetchRrn" title="Title Fetch RRN" escapeAutoFormat="true"/>
				<ec:column property="billInquiryRrn" title="Bill Inquiry RRN" escapeAutoFormat="true"/>
				<ec:column property="fundTransferRrn" title="Fund Transfer RRN" escapeAutoFormat="true"/>
				<ec:column property="billPaymentRrn" title="Bill Payment RRN" escapeAutoFormat="true"/>
				<ec:column property="checkBalanceRrn" title="Check Balance RRN" escapeAutoFormat="true"/>
				<ec:column property="processingStatusName" filterable="false"  title="Status" />
				<ec:column property="updateP2PFlagString" filterable="false" sortable="false" title="Available for Payment" />
				<ec:column property="failureReason" filterable="false"  title="Failure Reason" />
				<ec:column property="reversedByName" filterable="false"  title="Reversed By" />
				<ec:column property="reversedDate" cell="date" format="dd/MM/yyyy hh:mm a" filterable="false" title="Reversed On"/>
				<ec:column property="reversedComments" filterable="false"  title="Comments" />
				<ec:column property="fonepayTransactionCode" filterable="false"  title="Third Party Transaction ID" />
				<ec:column property="stan" filterable="false"  title="Unique Identifier" />
				<ec:column property="fonepayTransactionType" filterable="false"  title="FonePay Transaction Type" />
				<ec:column property="terminalId" filterable="false"  title="Terminal Id" />
				<ec:column property="channelId" filterable="false"  title="Channel Id" />
                <ec:column property="externalProductName" filterable="false"  title="Webservice Product Name" />
				<ec:column property="nIfq" filterable="false"  title="NADRA NFIQ" />
				<ec:column property="mintaieCount" filterable="false"  title="NADRA Minutaei" />
				<ec:column property="transactionPurpose" filterable="false"  title="Transaction Purpose" />
				<ec:column property="segment" filterable="false"  title="Segment" />
				<ec:column property="bankShortName" filterable="false"  title="Bank Name" />
				<ec:column property="bankIncome" filterable="false"  title="Bank Income" />
				<ec:column property="reserved3" filterable="false"  title="Reserved3" />
				<ec:column property="reserved4" filterable="false"  title="Reserved4" />
				<ec:column property="reserved5" filterable="false"  title="Reserved5" />
				<ec:column property="reserved8" filterable="false"  title="Reserved8" />
				<ec:column property="reserved9" filterable="false"  title="Reserved9"/>
				<ec:column property="reserved10" filterable="false"  title="Reserved10" />



				<authz:authorize ifAnyGranted="<%=detailBtnPermission%>">
				<ec:column alias=" " title="Detail" sortable="false">
					<input type="button" class="button" value="Detail" onClick="return openTransactionDetailWindow('${transactionDetailPortalModel.transactionCode}')"/>
	   			</ec:column>
   			</authz:authorize>
			</ec:row>
		</ec:table>

		<ajax:select source="supplierId" target="productId"
		baseUrl="${contextPath}/p_refData.html"
		parameters="supplierId={supplierId},rType=1,actionId=${retriveAction}" errorFunction="error"/>

		
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
			
		    Calendar.setup( {inputField  : "UpdtFromStartDate",button : "fromStartDate",} );
		    Calendar.setup( {inputField  : "UpdtToStartDate",button : "toStartDate", isEndDate: true } );
			
      	</script>
     	<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>
	</body>
</html>