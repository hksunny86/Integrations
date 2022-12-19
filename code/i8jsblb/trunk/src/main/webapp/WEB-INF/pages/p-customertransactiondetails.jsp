<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.*,com.inov8.microbank.common.util.PortalConstants'%>
<%@page import="com.inov8.microbank.server.service.smssendermodule.ResendSmsStrategyEnum" %>
<%@page import="com.inov8.microbank.common.util.IssueTypeStatusConstantsInterface"%>
<%@ page import='com.inov8.microbank.common.util.FinancialInstitutionConstants'%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>

<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<c:set var="defaultAction"><%=PortalConstants.ACTION_DEFAULT %></c:set>
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
    <script type="text/javascript" src="${contextPath}/scripts/date-validation.js"></script>
	<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
	<link type="text/css" rel="stylesheet" href="styles/ajaxtags.css" />
	<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
	<style type="text/css">
			.eXtremeTable input.button
			{
				width: 125px !important;
			}
	</style>
	<%@include file="/common/ajax.jsp"%>
	<meta name="title" content="Customer Transaction History" />	
<script type="text/javascript">
	function openIssueWindow(btnName, transactionId, transactionCodeId, transactionCode, issueStId, winId)
		{
			childWindow = window.open('p-issueupdateform.html?actionId=${defaultAction}&<%=IssueTypeStatusConstantsInterface.MGMT_PAGE_BTN_NAME%>='+btnName+'&transactionId='+transactionId+'&transactionCodeId='+transactionCodeId+'&transactionCode='+transactionCode+'&<%=IssueTypeStatusConstantsInterface.REQUEST_PARAMETER_NAME%>='+issueStId+'',winId,'width=400,height=250,menubar=no,toolbar=no,left=150,top=150,directories=no,scrollbars=no,resizable=no,status=no');		
		}
	function error(request)
	      {
	      	alert("An unknown error has occured. Please contact with the administrator for more details");
	      }
	function handleResendSmsResponse()
		{
			var response = document.getElementById('smsContent').innerHTML;
			alert( response );
		}

</script>


	<%
		String smsPermission = PortalConstants.CSR_GP_UPDATE;
		smsPermission += "," + PortalConstants.PG_GP_UPDATE;
		smsPermission += "," + PortalConstants.RESEND_SMS_UPDATE;
		smsPermission += "," + PortalConstants.ADMIN_GP_UPDATE;

		String chargebackPermission = PortalConstants.MNG_CHARGBACK_UPDATE;
		chargebackPermission +=	"," + PortalConstants.PG_GP_UPDATE;
		chargebackPermission +=	"," + PortalConstants.REQ_CHARGEBACK_UPDATE;
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
		
		<table width="750px" border="0">
			<html:form name='customertransactionForm'
				commandName="extendedTransactionDetailListViewModel" method="post"
				action="p-customertransactiondetails.html" onsubmit="return validateForm(this)">
				<tr>
					<td class="formText" align="right" width="18%">
						Sender Mobile No:
					</td>
					<td align="left">
						<html:input path="saleMobileNo" id="saleMobileNo"  cssClass="textBox" maxlength="11" tabindex="1" onkeypress="return maskNumber(this,event)"/> 
					</td>
					<td class="formText" align="right">
						Recipient Mobile No:
					</td>
					<td align="left" >
						<html:input path="notificationMobileNo" id="notificationMobileNo" cssClass="textBox" maxlength="11" tabindex="2" onkeypress="return maskNumber(this,event)"/> 
					</td>
				</tr>		
				<tr>
					<td class="formText" align="right">
						Transaction ID:
					</td>
					<td align="left">
						<html:input path="transactionCode" id="transactionCode" cssClass="textBox" tabindex="3" maxlength="50" onkeypress="return maskNumber(this,event)"/> 
					</td>
					<td class="formText" align="right">
						Transaction Status:
					</td>
					<td align="left">
						<html:select path="supProcessingStatusId" cssClass="textBox" tabindex="4" >
							<html:option value="">---All---</html:option>
							<c:if test="${supplierProcessingStatusList != null}">
								<html:options items="${supplierProcessingStatusList}" itemValue="supProcessingStatusId" itemLabel="name"/>
							</c:if>
						</html:select>
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">
						Supplier:
					</td>
					<td align="left">
						<html:select path="supplierId" cssClass="textBox" tabindex="5" >
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
						<html:select path="productId" cssClass="textBox" tabindex="6" >
							<html:option value="">---All---</html:option>
							<c:if test="${productModelList != null}">
								<html:options items="${productModelList}" itemValue="productId" itemLabel="name"/>
							</c:if>
						</html:select>
					</td>				
				</tr>
				<tr>
					<td class="formText" align="right">
						Start Date:
					</td>
					<td align="left">
				        <html:input path="startDate" id="startDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="7"/>
							<img id="sDate" tabindex="7" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
							<img id="sDate" tabindex="8" title="Clear Date" name="popcal"  onclick="javascript:$('startDate').value=''"   align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
					<td class="formText" align="right">
						End Date:
					</td>
					<td align="left">
					     <html:input path="endDate" id="endDate"  readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
					     <img id="eDate" tabindex="9" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
					     <img id="eDate" tabindex="10" title="Clear Date" name="popcal"  onclick="javascript:$('endDate').value=''"   align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">

					</td>
					<td align="left">
					<input name="_search" type="submit" class="button" value="Search" tabindex="11"  /> 	
					<input name="reset" type="reset" 
						onclick="javascript: window.location='p-customertransactiondetails.html?actionId=2&mfsId=${param.mfsId}&mobileNo=${param.mobileNo}&appUserId=${param.appUserId}&referrer=${param.referrer}'"
						class="button" value="Cancel" tabindex="12" />
					</td>
					<td class="formText" align="right">

					</td>
					<td align="left">
					</td>
				</tr>

				<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
					value="<%=PortalConstants.ACTION_RETRIEVE%>">
				<input type="hidden" name="mfsId" value="${param.mfsId}">
				<input type="hidden" name="mobileNo" value="${param.mobileNo}">
				<input type="hidden" name="appUserId" value="${param.appUserId}">
				<input type="hidden" name="referrer" value="${param.referrer}"/>
			</html:form>
		</table>
		
		<ec:table filterable="false" items="transactionDetailModelList"
			var="transactionDetailModel" width="100%"
			action="${pageContext.request.contextPath}/p-customertransactiondetails.html?actionId=${retriveAction}"
			title="" retrieveRowsCallback="limit" filterRowsCallback="limit"
			sortRowsCallback="limit">

		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
			<ec:exportXls fileName="Customer Transactions.xls" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
			<ec:exportXlsx fileName="Customer Transactions.xlsx" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
			<ec:exportPdf headerBackgroundColor="#b6c2da" headerTitle="Customer Transactions" fileName="Customer Transactions.pdf"
				tooltip="Export PDF" view="com.inov8.microbank.common.util.CustomPdfView" />			
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
			<ec:exportCsv fileName="Customer Transactions.csv" tooltip="Export CSV"></ec:exportCsv>
		</authz:authorize>
			<ec:row>
				<c:set var="transactionDetailTransactionId">
					<security:encrypt strToEncrypt="${transactionDetailModel.transactionId}"/>
				</c:set>
				<c:set var="transactionDetailTransactionCodeId">
					<security:encrypt strToEncrypt="${transactionDetailModel.transactionCodeId}"/>
				</c:set>				
			
				
				<ec:column property="createdOn" cell="date"
					format="dd/MM/yyyy hh:mm a" filterable="false" title="Transaction Date" />
			
				<ec:column property="transactionCode"
					title="Transaction ID" escapeAutoFormat="true">		
					
					<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
						<a href="p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode=${transactionDetailModel.transactionCode}"
							onclick="return openTransactionWindow('${transactionDetailModel.transactionCode}','${retriveAction}')">
							${transactionDetailModel.transactionCode} </a>					
					</authz:authorize>
					<authz:authorize ifNotGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
						${transactionDetailModel.transactionCode}	
					</authz:authorize>
										
				</ec:column>
				
				<ec:column property="supplierName" title="Supplier" />
				
				<ec:column property="productName" title="Product" />
					
				<ec:column property="deviceTypeName" title="Channel" />
				<ec:column property="agent1Id" title="Sender Agent ID" escapeAutoFormat="true"/>				
				<ec:column property="mfsId" title="Sender ID" escapeAutoFormat="true"/>
				
				<ec:column property="saleMobileNo" title="Sender Mob No" escapeAutoFormat="true" />
				<ec:column property="senderCnic" title="Sender CNIC" escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="accountNick" title="Sender Account Nick" />									
					
				<ec:column property="bankAccountNoLastFive" filterable="false" title="Sender Account No" escapeAutoFormat="true" />
				
				<ec:column property="paymentMode" title="Payment Mode" />

				<ec:column property="amount" calc="total" calcTitle="Total:"
					cell="currency" format="0.00" filterable="false" title="Amount" style="text-align: right"/>										
					
				<ec:column property="serviceChargesExclusive" filterable="false" calc="total"
					cell="currency" format="0.00" title="Service Charges" style="text-align: right"/>						
				
				<ec:column property="totalAmount" filterable="false" calc="total"
					cell="currency" format="0.00" title="Total Amount Charged" style="text-align: right"/>
					
				<ec:column property="agent2Id" title="Receiver Agent ID" escapeAutoFormat="true"/>
				<ec:column property="recipientId" title="Recipient ID" escapeAutoFormat="true"/>
				
				<ec:column property="notificationMobileNo" title="Recipient Mob No" escapeAutoFormat="true" />
				<ec:column property="recipientCnic" title="Recipient CNIC" escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="recipientAccountNick"  title="Recipient Account Nick" />									
					
				<ec:column property="recipientAccountNumber" filterable="false" title="Recipient Account No" escapeAutoFormat="true"/>					
				<ec:column property="cashDepositorCnic" title="Cash Depositor CNIC" escapeAutoFormat="true" style="text-align: center"/>				
				<ec:column property="processingStatusName"  title="Status" />
				
				<ec:column property="veriflyStatus" title="Verifly Status" sortable="false" style="text-align: center" alias=" " >
<%--				<ec:column title="Verifly Status" filterable="false"--%>
<%--					sortable="false" style="text-align: center"--%>
<%--					alias=" " >--%>
<%--                    <c:if test="${veriflyRequired}">--%>
<%--                        ${transactionDetailModel.veriflyStatus}--%>
<%--                    </c:if>--%>
				</ec:column>	
					
				
				<ec:column property="authorizationCode" title="Authorization Code" />
					
				<authz:authorize ifAnyGranted="<%=chargebackPermission%>">
				<ec:column alias="" title="Chargeback" style="text-align: center"
					filterable="false" sortable="false" viewsAllowed="html">
					<c:set var="issueTypeStatusId">
						<security:encrypt strToEncrypt="<%=IssueTypeStatusConstantsInterface.CHARGEBACK_NEW.toString()%>"/>
					</c:set>
					
					<c:choose>
						<c:when test="${transactionDetailModel.processingStatusName == 'Complete' && !transactionDetailModel.issue}">
							<input type="button" class="button" value="Chargeback"
								name="Chargeback${transactionDetailTransactionId}"
								onclick="openChargebackWindow1('Chargeback${transactionDetailTransactionId}','${transactionDetailTransactionId}','${transactionDetailTransactionCodeId}','${transactionDetailModel.transactionCode}','${issueTypeStatusId}','pg_dispute')" />
						</c:when>
						<c:otherwise>
							<input type="button" class="button" value="Chargeback"
								name="ChargebackDisabled${transactionDetailTransactionId}"
								disabled="disabled" />
						</c:otherwise>
					</c:choose>
				</ec:column>
				</authz:authorize>
				<authz:authorize ifNotGranted="<%=chargebackPermission%>">
				<ec:column alias="" title="Chargeback" style="text-align: center"
					filterable="false" sortable="false" viewsAllowed="html">				
							<input type="button" class="button" value="Chargeback"
								name="ChargebackDisabled${transactionDetailTransactionId}"
								disabled="disabled" />						
				</ec:column>
				</authz:authorize>
				<ec:column alias="initiatorSms" title="Resend SMS" style="text-align: center" sortable="false" filterable="false" viewsAllowed="html">
					<authz:authorize ifAnyGranted="<%=smsPermission%>">
						<c:choose>
							<c:when test="${transactionDetailModel.initiatorResendSmsAvailable}">
								<input type="button" class="button" value="${transactionDetailModel.initiatorResendSmsBtnLabel}" id="initiatorSms${transactionDetailTransactionId}" onclick="openResendSmsWindow('${transactionDetailTransactionId}','${INITIATOR_SMS_STRATEGY}','${param.appUserId}','${transactionDetailModel.transactionCode}')"/>
			
							</c:when>
							<c:otherwise>
								<input type="button" class="button" value="N/A" disabled="disabled"/>
							</c:otherwise>
						</c:choose>
					</authz:authorize>
					<authz:authorize ifNotGranted="<%=smsPermission%>">
						<input type="button" class="button" value="N/A" disabled="disabled"/>
					</authz:authorize>
				</ec:column>
				<ec:column alias="recipientSms" title="Resend SMS" style="text-align: center" sortable="false" filterable="false" viewsAllowed="html">
					<authz:authorize ifAnyGranted="<%=smsPermission%>">
						<c:choose>
							<c:when test="${transactionDetailModel.recipientResendSmsAvailable}">
								<input type="button" class="button" value="${transactionDetailModel.recipientResendSmsBtnLabel}" id="recipientSms${transactionDetailTransactionId}" onclick="openResendSmsWindow('${transactionDetailTransactionId}','${RECIPIENT_SMS_STRATEGY}','${param.appUserId}','${transactionDetailModel.transactionCode}')"/>
		
							</c:when>
							<c:otherwise>
								<input type="button" class="button" value="N/A" disabled="disabled"/>
							</c:otherwise>
						</c:choose>
					</authz:authorize>
					<authz:authorize ifNotGranted="<%=smsPermission%>">
						<input type="button" class="button" value="N/A" disabled="disabled"/>
					</authz:authorize>
				</ec:column>
				<ec:column alias="walkInDepositorSms" title="Resend SMS" style="text-align: center" sortable="false" filterable="false" viewsAllowed="html">
					<authz:authorize ifAnyGranted="<%=smsPermission%>">
						<c:choose>
							<c:when test="${transactionDetailModel.walkinDepositorSmsAvailable}">
								<input type="button" class="button" value="Depositor" id="walkInDepositorSms${transactionDetailTransactionId}" onclick="openResendSmsWindow('${transactionDetailTransactionId}','${WALKIN_DEPOSITOR_SMS_STRATEGY}','${param.appUserId}','${transactionDetailModel.transactionCode}')"/>
			
							</c:when>
							<c:otherwise>
								<input type="button" class="button" value="N/A" disabled="disabled"/>
							</c:otherwise>
						</c:choose>
					</authz:authorize>
					<authz:authorize ifNotGranted="<%=smsPermission%>">
						<input type="button" class="button" value="N/A" disabled="disabled"/>
					</authz:authorize>
				</ec:column>
				<ec:column alias="walkInBeneficiarySms" title="Resend SMS" style="text-align: center" sortable="false" filterable="false" viewsAllowed="html">
					<authz:authorize ifAnyGranted="<%=smsPermission%>">
						<c:choose>
							<c:when test="${transactionDetailModel.walkinBeneficiarySmsAvailable}">
								<input type="button" class="button" value="Beneficiary" id="walkInBeneficiarySms${transactionDetailTransactionId}" onclick="openResendSmsWindow('${transactionDetailTransactionId}','${WALKIN_BENEFICIARY_SMS_STRATEGY}','${param.appUserId}','${transactionDetailModel.transactionCode}')"/>
				
							</c:when>
							<c:otherwise>
								<input type="button" class="button" value="N/A" disabled="disabled"/>
							</c:otherwise>
						</c:choose>
					</authz:authorize>
					<authz:authorize ifNotGranted="<%=smsPermission%>">
						<input type="button" class="button" value="N/A" disabled="disabled"/>
					</authz:authorize>
				</ec:column>
				<%--
				<ec:column alias="" title="SMS" style="text-align: center"
					filterable="false" sortable="false" viewsAllowed="html">
					<c:choose>
					<c:when test="${transactionDetailModel.processingStatusName == 'Complete'}">
					
					<authz:authorize ifAnyGranted="<%=smsPermission%>">
						<input type="button" class="button" value="Resend SMS"
							id="resendSms${transactionDetailTransactionId}" />					
					</authz:authorize>
					<authz:authorize ifNotGranted="<%=smsPermission%>">
						<input type="button" class="button" value="Resend SMS"
							id="resendSms${transactionDetailTransactionId}"
							disabled="disabled" />					
					</authz:authorize>
					
					<input type="hidden"
						value="${transactionDetailTransactionId}"
						name="tId${transactionDetailTransactionId}"
						id="tId${transactionDetailTransactionId}" />
						
					<ajax:htmlContent baseUrl="${contextPath}/p_resendSms.html"
						source="resendSms${transactionDetailTransactionId}"
						target="smsContent"
						parameters="tId={tId${transactionDetailTransactionId}},appUserId=${param.appUserId}" />
					
					</c:when>
					<c:otherwise>
						<input type="button" class="button" value="Resend SMS"
							id="resendSms${transactionDetailTransactionId}"
							disabled="disabled" />					
					</c:otherwise>
					</c:choose>	
				</ec:column>
				--%>
			</ec:row>
		</ec:table>
		<input type="button" class="button" value="Back" tabindex="48" onclick="javascript: window.location.href='p_mnomfsaccountdetails.html?referrer=${param.referrer}&appUserId=${param.appUserId}&actionId=2'" />
<ajax:select source="supplierId" target="productId"
		baseUrl="${contextPath}/p_refData.html"
		parameters="supplierId={supplierId},rType=1,actionId=${retriveAction}" errorFunction="error"/>
<script type="text/javascript">
      function openTransactionWindow(transactionCode){
		newWindow=window.open('p_transactionissuehistorymanagement.html?transactionCode='+transactionCode,'TransactionSummary', 'width=550,height=350,menubar=no,toolbar=no,left=150,top=150,directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
		if(window.focus) newWindow.focus();
		return false;
      }
  
  	var childWindow;   
  	function openResendSmsWindow(tId,strategy,appUserId,txCode)
	{
		var popupWidth = 550;
		var popupHeight = 350;	       
		var popupLeft = (window.screen.width - popupWidth)/2;
		var popupTop = (window.screen.height - popupHeight)/2;		
		var url = 'p-resendsmsform.html?tId='+tId+'&resendSmsStrategy='+strategy+'&appUserId='+appUserId+'&txCode='+txCode;
        newWindow=window.open(url,'Action Detail Window', 'width='+popupWidth+',height='+popupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
	    if(window.focus) newWindow.focus();
	    return false;		
	}
  	
	function openChargebackWindow1(btnName, transactionId, transactionCodeId, transactionCode, issueTypeStatusId, winId)
	{
		childWindow = window.open('p-issueupdateform.html?<%=IssueTypeStatusConstantsInterface.MGMT_PAGE_BTN_NAME%>='+btnName+'&transactionId='+transactionId+'&transactionCodeId='+transactionCodeId+'&transactionCode='+transactionCode+'&<%=IssueTypeStatusConstantsInterface.REQUEST_PARAMETER_NAME%>='+issueTypeStatusId,winId,'width=400,height=250,menubar=no,toolbar=no,left=150,top=150,directories=no,scrollbars=no,resizable=no,status=no');		
	}
  
      
     
	  
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
     function validateForm(form){
  		var currentDate = "<%=PortalDateUtils.getServerDate()%>";
	    var _fDate = form.startDate.value;
		var _tDate = form.endDate.value;
		var startlbl = "Start Date";
		var endlbl   = "End Date";
	    var isValid = validateDateRange(_fDate,_tDate,startlbl,endlbl,currentDate);
	    return isValid;
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
