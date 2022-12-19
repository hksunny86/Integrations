
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
<script type="text/javascript" 
	src="${contextPath}/scripts/date-validation.js"></script>

<link rel="stylesheet" type="text/css"
	href="styles/deliciouslyblue/calendar.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/extremecomponents.css"
	type="text/css">
<%@include file="/common/ajax.jsp"%>

<meta name="title" content="View Chargebacks" />
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

	<c:set var="RIFC" scope="page">
		<security:encrypt strToEncrypt="<%=IssueTypeStatusConstantsInterface.CHARGEBACK_RIFC.toString()%>"/>
	</c:set>
	<c:set var="RIFM" scope="page">
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
	<c:set var="INOV8_RIFM" scope="page">
		<security:encrypt strToEncrypt="<%=IssueTypeStatusConstantsInterface.INOV8_CHARGEBACK_RIFM.toString()%>"/>
	</c:set>

<ec:table filterable="false" items="transactionDetailModelList"
	var="transactionDetailModel"
	action="${pageContext.request.contextPath}/p_customerchargebacklist.html"
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
			<ec:exportCsv fileName="viewChargeBack.csv" tooltip="Export CSV"></ec:exportCsv>
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
			title="Transaction ID"  escapeAutoFormat="true">
			

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
		<ec:column property="mfsId" filterable="false" title="Sender ID" escapeAutoFormat="true"/>
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
		<ec:column property="bankAccountNoLastFive" filterable="false" title="Account No." escapeAutoFormat="true" />
		<ec:column property="totalAmount" filterable="false"  calc="total" cell="currency"  format="0.00"
			title="Total Amount Charged" />
		<ec:column property="agent2Id" filterable="false" title="Receiver Agent ID" escapeAutoFormat="true"/>								
		<ec:column property="recipientMfsId" filterable="false" title="Recipient ID" escapeAutoFormat="true" style="text-align: center"/>
		<ec:column property="notificationMobileNo" filterable="false" title="Recipient Mobile No." escapeAutoFormat="true" style="text-align: center"/>
		<ec:column property="recipientCnic" filterable="false" title="Recipient CNIC" escapeAutoFormat="true" style="text-align: center"/>
		<ec:column property="authorizationCode" filterable="false"
			title="Authorization Code" />
		<ec:column property="cashDepositorCnic" filterable="false" title="Cash Depositor CNIC" escapeAutoFormat="true"style="text-align: center"/>
			<ec:column property="processingStatusName" filterable="false"  title="Status" />
			<ec:column property="veriflyStatus" alias=" " title="Verifly Status" style="text-align: center" filterable="false" sortable="false">
			</ec:column>
			<ec:column property="issueStatusName" alias=" " title="Chargeback Status" style="text-align: center" filterable="false" sortable="false">
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
      
       function validateForm(){
	      	return validateFormChar(document.forms[0]);
	   }
      
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

</body>
</html>
