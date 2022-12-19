<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<html>
	<head>
	    <script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
		<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
		<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
		<%@include file="/common/ajax.jsp"%>
		<meta name="title" content="Bulk Payments Receiving Report" />
		<meta name="decorator" content="decorator">
		<script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
      	<script language="javascript" type="text/javascript">
      		var jq=$.noConflict();
			var serverDate ="<%=PortalDateUtils.getServerDate()%>";
	      function error(request)
	      {
	      	alert("An unknown error has occured. Please contact with the administrator for more details");
	      }
        </script>
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
		
			<html:form name='bulkPaymentViewForm' commandName="bulkPaymentViewModel" method="post"
				action="p_bulkpaymentsdetail.html" onsubmit="return validateCurrentForm(this)" >
				<table width="770px" border="0">
					<tr>
						<td class="formText" align="right">Service:</td>
						<td align="left" >
							<html:select id="serviceId" path="serviceId" cssClass="textBox" tabindex="1">
								<html:option value="">--- Select ---</html:option>
								<c:if test="${serviceList != null}">
									<html:options items="${serviceList}" itemValue="id" itemLabel="label"/>
								</c:if>
							</html:select>
						</td>
						<td align="right" class="formText">Product:</td>
						<td align="left"><html:select id="productId" path="productId" cssClass="textBox" tabindex="2">
							<html:option value="">---Select---</html:option>
							<c:if test="${productList != null}">
								<html:options items="${productList}" itemValue="id" itemLabel="label"/>
							</c:if>
						</html:select>
						</td>
					</tr>
					<tr>

					<td class="formText" align="right" width="20%">
						Status:
					</td>
					<td align="left">
						<select name="processingStatusId" class="textBox" tabindex="2">
							<option value="">---All---</option>
						  	<option value="1">Complete</option>
						  	<option value="4">In-process</option>
						</select>
					</td>
						<td class="formText" align="right">Batch Number:</td>

						<td align="left"><html:input path="batchNumber" id="batchNumber" cssClass="textBox" maxlength="50" tabindex="3" onkeypress="return maskInteger(this,event)" onblur="return maskNumber(this,event)" /></td>
						</td>

					</tr>
				<tr>
					<td class="formText" align="right">
						Recipient CNIC:
					</td>
					<td align="left">
						<html:input path="recipientCnic" cssClass="textBox" maxlength="50" tabindex="3" onkeypress="return maskNumber(this,event)"/> 
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
						Recipient Agent ID:
					</td>
					<td align="left">
						<html:input path="agent2Id" cssClass="textBox" tabindex="6"/>
					</td>	
				</tr>
				<tr>
					<td class="formText" align="right">
						Disbursement Start Date:
					</td>
					<td align="left">
				        <html:input path="disStartDate" id="disStartDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
							<img id="disSDate" tabindex="7" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
							<img id="disSDate" tabindex="8" title="Clear Date" name="popcal"  onclick="javascript:$('disStartDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
					<td class="formText" align="right">
						Disbursement End Date:
					</td>
					<td align="left">
					     <html:input path="disEndDate" id="disEndDate"  readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
					     <img id="disEDate" tabindex="9" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
					     <img id="disEDate" tabindex="10" title="Clear Date" name="popcal"  onclick="javascript:$('disEndDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">
						Payment Start Date:
					</td>
					<td align="left">
				        <html:input path="payStartDate" id="payStartDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
							<img id="paySDate" tabindex="11" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
							<img id="paySDate" tabindex="12" title="Clear Date" name="popcal"  onclick="javascript:$('payStartDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
					<td class="formText" align="right">
						Payment End Date:
					</td>
					<td align="left">
					     <html:input path="payEndDate" id="payEndDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
					     <img id="payEDate" tabindex="13" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
					     <img id="payEDate" tabindex="14" title="Clear Date" name="popcal"  onclick="javascript:$('payEndDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">
					</td>
					<td align="left">
						<input name="_search" type="submit" class="button" value="Search" tabindex="13"/> 
						<input name="reset" type="reset" 
							onclick="javascript: window.location='p_bulkpaymentsdetail.html?actionId=${retriveAction}'"
							class="button" value="Cancel" tabindex="14" />
					</td>
					<td class="formText" align="right">
					</td>
					<td align="left">
					</td>
				</tr>

				<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
					value="<%=PortalConstants.ACTION_RETRIEVE%>">
			</table>
			</html:form>


		<ajax:select source="serviceId" target="productId"
					 baseUrl="${contextPath}/p_productRefData.html" errorFunction="error"
					 parameters="serviceId={serviceId}"/>

		<ec:table items="bulkPaymentsViewModelList" var="bulkPaymentsViewModel"
		action="${contextPath}/p_bulkpaymentsdetail.html?actionId=${retriveAction}"
		title=""
          retrieveRowsCallback="limit"
          filterRowsCallback="limit"
          sortRowsCallback="limit"
          filterable="false"		
		>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Bulk Payments Receiving Report.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Bulk Payments Receiving Report.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="Bulk Payments Receiving Report"
					fileName="Bulk Payments Receiving Report.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Bulk Payments Receiving Report.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			
			<ec:row>
				<ec:column property="transactionCode" filterable="false" title="Transaction ID" escapeAutoFormat="true">
				  	<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
						<a href="p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode=${bulkPaymentsViewModel.transactionCode}" onclick="return openTransactionWindow('${bulkPaymentsViewModel.transactionCode}')">
						  ${bulkPaymentsViewModel.transactionCode}
						</a>
					</authz:authorize>
					<authz:authorize ifNotGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
						${bulkPaymentsViewModel.transactionCode}
					</authz:authorize>
				</ec:column>
				<ec:column property="batchNumber" title="Batch No." style="text-align: center"/>
				<ec:column property="paymentTypeName" title="Payment Type" style="text-align: center"/>
				<ec:column property="recipientMobileNo" title="Recipient Mobile No." escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="recipientCnic" title="Recipient CNIC" escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="agent2Id" title="Agent ID" style="text-align: left"/>

				<ec:column property="transactionAmount" filterable="false" title="Amount" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
				<ec:column property="inclusiveCharges" filterable="false" title="Inclusive Charges" calc="total" calcTitle="Total:" cell="currency"  format="0.00" style="text-align: right"/>
				<ec:column property="exclusiveCharges" filterable="false" title="Exclusive Charges" calc="total" calcTitle="Total:" cell="currency"  format="0.00" style="text-align: right"/>
				<ec:column property="totalAmount" filterable="false" title="Total Amount" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>

				<ec:column property="akblCommission" filterable="false" title="Bank Commission" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
				
				<ec:column property="salesTeamCommission" filterable="false" title="Sales Team Commission" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
				<ec:column property="othersCommission" filterable="false" title="Other Commission" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
				<ec:column property="fed" filterable="false" title="FED" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
				<ec:column property="wht" filterable="false" title="WHT" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>


				<ec:column property="createdOn" cell="date" format="dd/MM/yyyy hh:mm a" filterable="false" title="Disbursement Date"/>
				<ec:column property="updatedOn" cell="date" format="dd/MM/yyyy hh:mm a" filterable="false" title="Payment Date"/>
				<ec:column property="processingStatusName" title="Processing Status"/>
				
			</ec:row>
		</ec:table>

		
		<script language="javascript" type="text/javascript">

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
		
	        function validateCurrentForm(form){
	        
	        	var isValid	=	validateForm(form);
	        	
	        	if(!isValid)
	        	{
	        		return isValid;
	        	}
	        	var currentDate = "<%=PortalDateUtils.getServerDate()%>";
	        	var _disFDate = form.disStartDate.value;
		  		var _disTDate = form.disEndDate.value;
		  		var startlbl = "Disbursement Start Date";
		  		var endlbl   = "Disbursement End Date";
	        	var isValidDisDate = isValidDateRange(_disFDate,_disTDate,startlbl,endlbl,currentDate);
	        	
	        	var _payFDate = form.payStartDate.value;
		  		var _payTDate = form.payEndDate.value;
		  		startlbl = "Payment Start Date";
		  		endlbl   = "Payment End Date";
	        	var isValidPayDate = isValidDateRange(_payFDate,_payTDate,startlbl,endlbl,currentDate);
	        	
	        	isValid = isValidPayDate && isValidDisDate;
	        	
	        	return isValid;
	        }

      		Calendar.setup(
      		{
		       inputField  : "disStartDate", // id of the input field
			   button      : "disSDate",    // id of the button
		    });
      		
			Calendar.setup(
		    {
		      inputField  : "disEndDate", // id of the input field
		      button      : "disEDate",    // id of the button
			  isEndDate: true	
		    });
		    
		    Calendar.setup(
      		{
		       inputField  : "payStartDate", // id of the input field
			   button      : "paySDate",    // id of the button
		    });
      		
			Calendar.setup(
		    {
		      inputField  : "payEndDate", // id of the input field
		      button      : "payEDate",    // id of the button
			  isEndDate: true	
		    });
      	</script>
      	<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>
	</body>
</html>
