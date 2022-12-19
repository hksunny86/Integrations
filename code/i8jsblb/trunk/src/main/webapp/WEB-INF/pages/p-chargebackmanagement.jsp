
<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.*,com.inov8.microbank.common.util.PortalConstants'%>

<html>
<head>
<meta name="decorator" content="decorator">
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
<script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
<link rel="stylesheet" type="text/css"
	href="styles/deliciouslyblue/calendar.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/extremecomponents.css"
	type="text/css">
<%@include file="/common/ajax.jsp"%>

<meta name="title" content="View Chargebacks" />
<%
	String rifcPermission = PortalConstants.VIEW_CHARGEBACKS_RIFC_UPDATE;
	rifcPermission += "," + PortalConstants.PG_GP_UPDATE;

	String rifaPermission = PortalConstants.VIEW_CHARGEBACKS_RIFA_UPDATE;
	rifaPermission += "," + PortalConstants.PG_GP_UPDATE;
%>

<script type="text/javascript">
var jq=$.noConflict();
var serverDate ="<%=PortalDateUtils.getServerDate()%>";
</script>
</head>
<body bgcolor="#ffffff"  onunload="javascript:closeChild();" >
<div id="smsContent" class="ajaxMsg"></div>
<c:if test="${not empty messages}">
	<div class="infoMsg" id="successMessages"><c:forEach var="msg"
		items="${messages}">
		<c:out value="${msg}" escapeXml="false" />
		<br />
	</c:forEach></div>
	<c:remove var="messages" scope="session" />
</c:if>

	<html:form name='chargebackForm'
		commandName="extendedChargebackListViewModel" method="post"
		action="p-chargebackmanagement.html" onsubmit="return validateForm()" >


		<table width="750px">
		<tr>
			<td width="33%" align="right" class="formText">Sender ID:</td>
			<td width="17%" align="left"><html:input onkeypress="return maskCommon(this,event)" tabindex="1" path="mfsId" id="mfsId"
				cssClass="textBox" maxlength="12" /> ${status.errorMessage}</td>
			<td width="16%" align="right" class="formText">Recipient ID:</td>
			<td width="34%" align="left"><html:input onkeypress="return maskCommon(this,event)" tabindex="1" path="recipientMfsId" id="recipientMfsId"
				cssClass="textBox" maxlength="11" /> ${status.errorMessage}</td>
		</tr>
		<tr>
		  <td align="right" class="formText">Issue Code: </td>
		  <td align="left">
		  <html:input onkeypress="return maskCommon(this,event)" tabindex="4" path="issueCode" id="issueCode"
				cssClass="textBox" maxlength="50" />
		  </td>
			<td align="right" class="formText">Start Date:</td>
			<td align="left"><html:input path="startDate" id="startDate" readonly="true" tabindex="-1"
				cssClass="textBox" maxlength="10" />
			<img id="sDate" tabindex="2" readonly="true" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
			<img id="sDate" tabindex="3" title="Clear Date" name="popcal" align="middle" onclick="javascript:$('startDate').value=''"   align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />			</td>

	  </tr>

		<tr>
			<td class="formText" align="right">Transaction ID:</td>
			<td align="left"><html:input onkeypress="return maskCommon(this,event)" path="transactionCode" tabindex="7"
				id="transactionCode" cssClass="textBox" maxlength="50" />
			${status.errorMessage}</td>
		  <td align="right" class="formText">End Date:</td>
		  <td align="left">
		    <html:input path="endDate" id="endDate" readonly="true" tabindex="-1"
				cssClass="textBox" maxlength="10" />  
			<img id="eDate" tabindex="5" readonly="true" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
			<img id="eDate" tabindex="6" title="Clear Date" name="popcal" align="middle" onclick="javascript:$('endDate').value=''"   align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />	
		  </td>
		</tr>

		<tr>
			<td class="formText" align="right">Supplier:</td>
			<td align="left"><html:select path="supplierId" tabindex="8"
				cssClass="textBox">
				<html:option value="">---All---</html:option>
				<c:if test="${supplierModelList != null}">
					<html:options items="${supplierModelList}" itemValue="supplierId"
						itemLabel="name" />
				</c:if>
			</html:select></td>
			<td class="formText" align="right"></td>
			<td align="left"></td>
		</tr>

		<tr>
			<td class="formText" align="right">Product:</td>
			<td align="left"><html:select path="productId" tabindex="9"
				cssClass="textBox">
				<html:option value="">---All---</html:option>
				<c:if test="${productModelList != null}">
					<html:options items="${productModelList}" itemValue="productId"
						itemLabel="name" />
				</c:if>
			</html:select></td>
			<td class="formText" align="right"></td>
			<td align="left"></td>
		</tr>

		<tr>

			<td class="formText" align="right">Authorization Code:</td>
			<td align="left"><html:input onkeypress="return maskCommon(this,event)" path="authorizationCode" tabindex="10"
				id="bankResponseCode" cssClass="textBox" maxlength="50" />
			${status.errorMessage}</td>
		</tr>


		<tr>
			<td align="left" class="formText"></td>
			<td>
				<input name="_search" type="submit" class="button"	tabindex="11" value="Search" />
				<input name="reset" type="reset" class="button" tabindex="12"
				value="Cancel"
				onClick="javascript:window.location='p-chargebackmanagement.html';" />
			</td>
		</tr>
	</table></html:form>


	<c:set var="RIFC" scope="page">
		<security:encrypt strToEncrypt="<%=IssueTypeStatusConstantsInterface.CHARGEBACK_RIFC.toString()%>"/>
	</c:set>
	<c:set var="RIFA" scope="page">
		<security:encrypt strToEncrypt="<%=IssueTypeStatusConstantsInterface.CHARGEBACK_INVALID.toString()%>"/>
	</c:set>
	<c:set var="OPEN" scope="page">
		<security:encrypt strToEncrypt="<%=IssueTypeStatusConstantsInterface.CHARGEBACK_OPEN.toString()%>"/>
	</c:set>
	<c:set var="NEW" scope="page">
		<security:encrypt strToEncrypt="<%=IssueTypeStatusConstantsInterface.CHARGEBACK_NEW.toString()%>"/>
	</c:set>
	<c:set var="INOV8_RIFC" scope="page">
		<security:encrypt strToEncrypt="<%=IssueTypeStatusConstantsInterface.INOV8_CHARGEBACK_RIFC.toString()%>"/>
	</c:set>
	<c:set var="INOV8_RIFA" scope="page">
		<security:encrypt strToEncrypt="<%=IssueTypeStatusConstantsInterface.INOV8_CHARGEBACK_RIFM.toString()%>"/>
	</c:set>

<ec:table filterable="false" items="transactionDetailModelList"
	var="transactionDetailModel"
	action="${pageContext.request.contextPath}/p-chargebackmanagement.html"
	title=""
    retrieveRowsCallback="limit"
    filterRowsCallback="limit"
    sortRowsCallback="limit"	
	>
	<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
		<ec:exportXls fileName="View Chargebacks.xls" tooltip="Export Excel" />
	</authz:authorize>
	<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
		<ec:exportXlsx fileName="View Chargebacks.xlsx" tooltip="Export Excel" />
	</authz:authorize>
	<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
		<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
			headerTitle="View Chargebacks" fileName="viewChargeBack.pdf"
			tooltip="Export PDF" />
	</authz:authorize>
	<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
		<ec:exportCsv fileName="View Chargebacks.csv" tooltip="Export CSV"></ec:exportCsv>
	</authz:authorize>

	<ec:row>
		<c:set var="transactionDetailTransactionId">
			<security:encrypt strToEncrypt="${transactionDetailModel.transactionId}"/>
		</c:set>
		<c:set var="transactionDetailTransactionCodeId">
			<security:encrypt strToEncrypt="${transactionDetailModel.transactionCodeId}"/>
		</c:set>
		<c:set var="transactionDetailIssueId">
			<security:encrypt strToEncrypt="${transactionDetailModel.issueId}"/>
		</c:set>
		<c:set var="transactionDetailIssueTypeStatusId">
			<security:encrypt strToEncrypt="${transactionDetailModel.issueTypeStatusId}"/>
		</c:set>						
	
        <ec:column property="issueCode" filterable="false" title="Issue Code">

	        <authz:authorize ifAnyGranted="<%=PortalConstants.TX_RPT_READ%>">        
	            <a href="p_issuehistorymanagement.html?${retriveAction}" onclick="return openIssueWindow('${transactionDetailModel.issueCode}','${retriveAction}')">
	                ${transactionDetailModel.issueCode}
	        </authz:authorize>
	        <authz:authorize ifNotGranted="<%=PortalConstants.TX_RPT_READ%>">        
				${transactionDetailModel.issueCode}
	        </authz:authorize>
	        
       </ec:column>
		<ec:column property="transactionCode" filterable="false"
			title="Transaction ID" escapeAutoFormat="true">
			

			<authz:authorize ifAnyGranted="<%=PortalConstants.TX_RPT_READ%>">        
	            <a href="p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode=${transactionDetailModel.transactionCode}" onclick="return openTransactionWindow('${transactionDetailModel.transactionCode}','${retriveAction}')">
	                ${transactionDetailModel.transactionCode}
	            </a>
	        </authz:authorize>
	        <authz:authorize ifNotGranted="<%=PortalConstants.TX_RPT_READ%>">        
				${transactionDetailModel.transactionCode}
	        </authz:authorize>
	        
		</ec:column>
		<ec:column property="agent1Id" filterable="false" title="Sender Agent ID" escapeAutoFormat="true"/>
		<ec:column property="mfsId" filterable="false" title="Sender ID" />
		<ec:column property="saleMobileNo" filterable="false" title="Sender Mobile No." escapeAutoFormat="true" style="text-align: center"/>
		<ec:column property="senderCnic" filterable="false" title="Sender CNIC" escapeAutoFormat="true" style="text-align: center"/>
		<ec:column property="accountNick" filterable="false"
			title="Account Nick" style="white-space: wrap" />
		<ec:column property="paymentMode" filterable="false"
			title="Payment Mode" />
		<ec:column property="createdOn" cell="date" format="dd/MM/yyyy hh:mm a"
			filterable="false" title="Date" />
		<ec:column property="supplierName" filterable="false" title="Supplier" />
		<ec:column property="productName" filterable="false" title="Product" />
		<ec:column property="amount"  calc="total" calcTitle="Total:"  cell="currency"  format="0.00" filterable="false" title="Amount" />
		<ec:column property="serviceCharges" filterable="false" calc="total" cell="currency"  format="0.00"
			title="Service Charges" />
		<ec:column property="bankAccountNoLastFive" filterable="false" title="Account No." />
		<ec:column property="totalAmount" filterable="false"  calc="total" cell="currency"  format="0.00"
			title="Total Amount Charged" />
		<ec:column property="agent2Id" filterable="false" title="Receiver Agent ID" escapeAutoFormat="true"/>								
		<ec:column property="recipientMfsId" filterable="false" title="Recipient ID" style="text-align: center"/>
		<ec:column property="notificationMobileNo" filterable="false" title="Recipient Mobile No." escapeAutoFormat="true" style="text-align: center"/>
		<ec:column property="recipientCnic" filterable="false" title="Recipient CNIC" escapeAutoFormat="true" style="text-align: center"/>
		<ec:column property="authorizationCode" filterable="false"
			title="Authorization Code" />
		<ec:column property="cashDepositorCnic" filterable="false" title="Cash Depositor CNIC" escapeAutoFormat="true" style="text-align: center"/>
			<ec:column property="processingStatusName" filterable="false"  title="Status" />
			<ec:column property="veriflyStatus" alias=" " title="Verifly Status" style="text-align: center" filterable="false" sortable="false">
			</ec:column>
<%--		<ec:column alias=" " title="Verifly Status" style="text-align: center" filterable="false" sortable="false">--%>
<%--		   <c:if test="${veriflyRequired}">--%>
<%--                        ${transactionDetailModel.veriflyStatus}--%>
<%--                    </c:if>--%>
<%--		</ec:column>--%>
		<ec:column property="rifc" title="RIFC" style="text-align: center"
			filterable="false" sortable="false" >

			<c:set var="issueTypeStatus">
				<security:encrypt strToEncrypt="<%=IssueTypeStatusConstantsInterface.CHARGEBACK_RIFC.toString()%>"/>
			</c:set>

			
			<c:if test="${transactionDetailIssueTypeStatusId == NEW}">
				<div id="DIV_RIFC_${transactionDetailIssueId}">
				
			        <authz:authorize ifAnyGranted="<%=rifcPermission%>">        
						<input type="button" class="button" value="Resolve"
							name="ChargebackRIFC${transactionDetailTransactionId}"
							onclick="chargebackRIFCWindow('ChargebackRIFC${transactionDetailTransactionId}','${transactionDetailTransactionId}','${transactionDetailTransactionCodeId}','${transactionDetailModel.transactionCode}','${transactionDetailIssueId}','${issueTypeStatus}','pg_issue_chargeback','${retriveAction}')">
			        </authz:authorize>
			        <authz:authorize ifNotGranted="<%=rifcPermission%>">        
						<input type="button" class="button" value="Resolve"
							name="ChargebackRIFC${transactionDetailTransactionId}" disabled="disabled"  >
			        </authz:authorize>
			        
				</div>
			</c:if>
			<c:if test="${transactionDetailIssueTypeStatusId == RIFC || transactionDetailIssueTypeStatusId == INOV8_RIFC}">YES</c:if>&nbsp;
       </ec:column>
		<ec:column property="rifm" title="RIFA" style="text-align: center"
			filterable="false" sortable="false" >
			<c:set var="issueTypeStatusRIFM">
				<security:encrypt strToEncrypt="<%=IssueTypeStatusConstantsInterface.CHARGEBACK_INVALID.toString()%>"/>
			</c:set>
				
			<c:if test="${transactionDetailIssueTypeStatusId == NEW}">
				<div id="DIV_INVALID_${transactionDetailIssueId}">
			        <authz:authorize ifAnyGranted="<%=rifaPermission%>">        
						<input type="button" class="button" value="Resolve"
							name="ChargebackRIFM${transactionDetailTransactionId}"
							onclick="chargebackRIFMWindow('ChargebackRIFM${transactionDetailTransactionId}','${transactionDetailTransactionId}','transactionCodeId=${transactionDetailTransactionCodeId}','${transactionDetailModel.transactionCode}','${transactionDetailIssueId}','${issueTypeStatusRIFM}','pg_issue_chargeback','${retriveAction}')">
			        </authz:authorize>
			        <authz:authorize ifNotGranted="<%=rifaPermission%>">        
						<input type="button" class="button" value="Resolve"
							name="ChargebackRIFM${transactionDetailTransactionId}" disabled="disabled" >
			        </authz:authorize>
			        
				</div>
			</c:if>
			<c:if test="${transactionDetailIssueTypeStatusId == RIFA || transactionDetailIssueTypeStatusId == INOV8_RIFA}">YES</c:if>&nbsp;
       </ec:column>

	</ec:row>
</ec:table>
<%-- 
Please refer to com.inov8.microbank.common.util.PortalConstants.REF_DATA_REQUEST_PARAM for parameter name
and com.inov8.microbank.common.util.PortalConstants.REF_DATA_**** for the possible values
--%>

<ajax:select source="supplierId" target="productId"
	baseUrl="${contextPath}/p_refData.html"
	parameters="supplierId={supplierId},rType=1" />
	
<script type="text/javascript">
      
      function openTransactionWindow(transactionCode){
		newWindow=window.open('p_transactionissuehistorymanagement.html?transactionCode='+transactionCode,'TransactionSummary', 'width=550,height=350,menubar=no,toolbar=no,left=150,top=150,directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
		if(window.focus) newWindow.focus();
		return false;
	  }
	  function openIssueWindow(issueCode){
		newWindow=window.open('p_issuehistorymanagement.html?issueCode='+issueCode,'IssueHistory', 'width=550,height=350,menubar=no,toolbar=no,left=150,top=150,directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
		if(window.focus) newWindow.focus();
		return false;
	  }
      
      
      function chargebackRIFCWindow(btnName,transactionId,transactionCodeId,transactionCode,issueId,issueTypeStatus,winId){
      	childWindow = window.open('p-issueupdateform.html?<%=IssueTypeStatusConstantsInterface.MGMT_PAGE_BTN_NAME%>='+btnName+'&transactionId='+transactionId+'&transactionCodeId='+transactionCodeId+'&transactionCode='+transactionCode+'&issueId='+issueId+'&<%=IssueTypeStatusConstantsInterface.REQUEST_PARAMETER_NAME%>='+issueTypeStatus,''+winId+'','width=550,height=350,menubar=no,toolbar=no,left=150,top=150,directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
      }
      
      function chargebackRIFMWindow(btnName,transactionId,transactionCodeId,transactionCode,issueId,issueTypeStatusRIFM,winId){
      	childWindow = window.open('p-issueupdateform.html?divId=<%=IssueTypeStatusConstantsInterface.MGMT_PAGE_BTN_NAME%>='+btnName+'&transactionId='+transactionId+'&transactionCodeId='+transactionCodeId+'&transactionCode='+transactionCode+'&issueId='+issueId+'&<%=IssueTypeStatusConstantsInterface.REQUEST_PARAMETER_NAME%>='+issueTypeStatusRIFM,''+winId+'','width=550,height=350,menubar=no,toolbar=no,left=150,top=150,directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
      }
      
      function escalateToI8Window(btnName,transactionId,transactionCodeId,transactionCode,issueId,issueTypeStatusI8,winId){
      	childWindow = window.open('p-issueupdateform.html?<%=IssueTypeStatusConstantsInterface.MGMT_PAGE_BTN_NAME%>='+btnName+'&transactionId='+transactionId+'&transactionCodeId='+transactionCodeId+'&transactionCode='+transactionCode+'&issueId='+issueId+'&<%=IssueTypeStatusConstantsInterface.REQUEST_PARAMETER_NAME%>='+issueTypeStatusI8,''+winId+'','width=550,height=350,menubar=no,toolbar=no,left=150,top=150,directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
      }

      
      
      highlightFormElements();
      document.forms[0].mfsId.focus();
      
	 var childWindow;     

       function closeChild()
       {
          try
              {
              if(childWindow != undefined)
               {
                   childWindow.close();
                   childWindow=undefined;
               }
		      }catch(e){}
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
