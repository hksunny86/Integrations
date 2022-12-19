
<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.*,com.inov8.microbank.common.util.PortalConstants'%>

<html>
	<head>
<meta name="decorator" content="decorator">
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
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

		<meta name="title" content="Transaction Details" />
	</head>
	<body bgcolor="#ffffff" onunload="javascript:closeChild();">
		<div id="smsContent" class="ajaxMsg"></div>
		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" />
					<br />
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>

		<table width="100%">
			<html:form name='transactionDetailForm'
				commandName="extendedTransactionDetailListViewModel" method="post"
				action="p-transactiondetailmanagement.html"
				onsubmit="return validateForm()">
				<tr>
					<td width="33%" align="right" class="formText">
						Inov8 MWallet ID:
					</td>
					<td width="17%" align="left">
						<html:input path="mfsId"
							onkeypress="return maskCommon(this,event)" id="mfsId"
							cssClass="textBox" tabindex="1" maxlength="8" />
						${status.errorMessage}
					</td>
					<td width="16%" align="right" class="formText">
						Start Date:
					</td>
					<td width="34%" align="left">
						<html:input path="startDate" id="startDate" tabindex="-1"
							readonly="true" cssClass="textBox" maxlength="10" />
						<img id="sDate" tabindex="2" readonly="true" name="popcal"
							align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
						<img id="sDate" tabindex="3" title="Clear Date" name="popcal"
							align="middle" onclick="javascript:$('startDate').value=''"
							align="middle" style="cursor:pointer" src="images/refresh.png"
							border="0" />
					</td>
				</tr>

				<tr>
					<td class="formText" align="right">
						Transaction Code:
					</td>
					<td align="left">
						<html:input path="transactionCode"
							onkeypress="return maskCommon(this,event)" tabindex="4"
							id="transactionCode" cssClass="textBox" maxlength="50" />
						${status.errorMessage}
					</td>
					<td class="formText" align="right">
						End Date:
					</td>
					<td align="left" valign="top">
						<html:input path="endDate" id="endDate" readonly="true"
							tabindex="-1" cssClass="textBox" maxlength="10" />
						<img id="eDate" tabindex="5" name="popcal" align="top"
							style="cursor:pointer" src="images/cal.gif" border="0" />
						<img id="eDate" tabindex="6" title="Clear Date" name="popcal"
							align="middle" onclick="javascript:$('endDate').value=''"
							align="middle" style="cursor:pointer" src="images/refresh.png"
							border="0" />
					</td>
				</tr>

				<tr>
					<td class="formText" align="right">
						Supplier:
					</td>
					<td align="left">
						<html:select path="supplierId" tabindex="7" cssClass="textBox">
							<html:option value="">---All---</html:option>
							<c:if test="${supplierModelList != null}">
								<html:options items="${supplierModelList}"
									itemValue="supplierId" itemLabel="name" />
							</c:if>
						</html:select>
					</td>
					<td class="formText" align="right">Recepient Mob#:</td>
					<td align="left">
					<html:input path="notificationMobileNo"
							onkeypress="return maskCommon(this,event)" tabindex="4"
							id="notificationMobileNo" cssClass="textBox" maxlength="50" />
						${status.errorMessage}
					</td>
				</tr>

				<tr>
					<td class="formText" align="right">
						Product:
					</td>
					<td align="left">
						<html:select path="productId" tabindex="8" cssClass="textBox">
							<html:option value="">---All---</html:option>
							<c:if test="${productModelList != null}">
								<html:options items="${productModelList}" itemValue="productId"
									itemLabel="name" />
							</c:if>
						</html:select>
					</td>
					<td class="formText" align="right">Sender Mob#:</td>
					<td align="left">
					<html:input path="saleMobileNo"
							onkeypress="return maskCommon(this,event)" tabindex="4"
							id="saleMobileNo" cssClass="textBox" maxlength="50" />
						${status.errorMessage}
					</td>
				</tr>

				<tr>

					<td class="formText" align="right">
						Authorization Code:
					</td>
					<td align="left">
						<html:input path="authorizationCode"
							onkeypress="return maskCommon(this,event)" tabindex="9"
							id="bankResponseCode" cssClass="textBox" maxlength="50" />
						${status.errorMessage}
					</td>

				</tr>


				<tr>
					<td align="left" class="formText"></td>
					<td>
						<authz:authorize ifAnyGranted="<%=PortalConstants.TX_RPT_READ%>">
							<input name="_search" type="submit" class="button" tabindex="10"
								value="Search" />							
						</authz:authorize>
						<authz:authorize ifNotGranted="<%=PortalConstants.TX_RPT_READ%>">
							<input name="_search" type="submit" class="button" tabindex="10"
								value="Search" tabindex="-1" disabled="disabled" />							
						</authz:authorize>
						
						<input name="reset" type="reset" class="button" tabindex="11"
							value="Cancel"
							onclick="javascript: window.location='p-transactiondetailmanagement.html'" />
					</td>
				</tr>

			</html:form>
		</table>

		<ec:table filterable="false" items="transactionDetailModelList"
			var="transactionDetailModel"
			action="${pageContext.request.contextPath}/p-transactiondetailmanagement.html"
			title="" retrieveRowsCallback="limit" filterRowsCallback="limit"
			sortRowsCallback="limit">
			
			<authz:authorize ifAnyGranted="<%=PortalConstants.EXP_RPT_PDF_XLS_READ%>">
				<ec:exportXls fileName="Transaction Details.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Collection Payment Transactions.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf headerBackgroundColor="#b6c2da"
					headerTitle="Transaction Details" fileName="Transaction Details.pdf"
					tooltip="Export PDF" view="com.inov8.microbank.common.util.CustomPdfView" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Transaction Details.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>

			<ec:row>
				<c:set var="transactionDetailTransactionId">
					<security:encrypt strToEncrypt="${transactionDetailModel.transactionId}"/>
				</c:set>
				<c:set var="transactionDetailTransactionCodeId">
					<security:encrypt strToEncrypt="${transactionDetailModel.transactionCodeId}"/>
				</c:set>				
			
			
				<ec:column property="transactionCode" filterable="false"
					title="Transaction Code" escapeAutoFormat="true">		
					
					<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
						<a href="p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode=${transactionDetailModel.transactionCode}"
							onclick="return openTransactionWindow('${transactionDetailModel.transactionCode}','${retriveAction}')">
							${transactionDetailModel.transactionCode} </a>					
					</authz:authorize>
					<authz:authorize ifNotGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
						${transactionDetailModel.transactionCode}	
					</authz:authorize>
										
				</ec:column>
				
				<ec:column property="mfsId" filterable="false" title="Inov8 MWallet ID" />
				
				<ec:column property="deviceTypeName" filterable="false" title="Channel" />
				<ec:column property="accountNick" filterable="false"
					title="Account Nick" />
				
				<ec:column property="paymentMode" filterable="false"
					title="Payment Mode" />
				
				<ec:column property="createdOn" cell="date"
					format="dd/MM/yyyy hh:mm a" filterable="false" title="Date/Time" />
				
				<ec:column property="supplierName" filterable="false"
					title="Supplier" />
				
				<ec:column property="productName" filterable="false" title="Product" />
					<ec:column property="saleMobileNo" filterable="false"
					title="Sender Mob#" />
				
				<ec:column property="amount" calc="total" calcTitle="Total:"
					cell="currency" format="0.00" filterable="false" title="Amount" />
				
				<ec:column property="serviceCharges" filterable="false" calc="total"
					cell="currency" format="0.00" title="Service Charges" />
								
				<ec:column property="commSharing" filterable="false" calc="total"
					cell="currency" format="0.00" title="Commission Share" />										
					
				<ec:column property="serviceFeeSharing" filterable="false" calc="total"
					cell="currency" format="0.00" title="Service Charges Share" />															
					
				<ec:column property="bankAccountNoLastFive" filterable="false" title="Account No." />
				
				<ec:column property="totalAmount" filterable="false" calc="total"
					cell="currency" format="0.00" title="Total Amount Charged" />
				
				<ec:column property="authorizationCode" filterable="false"
					title="Authorization Code" />
					
					<ec:column property="notificationMobileNo" filterable="false"
					title="Recepient Mob#" />
				
				<ec:column property="processingStatusName" filterable="false"  title="Status" />
				
				<ec:column property="veriflyStatus" title="Verifly Status" filterable="false"
					       sortable="false" style="text-align: center"
					       alias=" " >
<%--				<ec:column title="Verifly Status" filterable="false"--%>
<%--					sortable="false" style="text-align: center"--%>
<%--					alias=" " >--%>
<%--                    <c:if test="${veriflyRequired}">--%>
<%--                        ${transactionDetailModel.veriflyStatus}--%>
<%--                    </c:if>--%>
				</ec:column>	
					
				<ec:column alias="" title="Chargeback" style="text-align: center"
					filterable="false" sortable="false" viewsAllowed="html">
					<%--	<ec:column alias="" title="Chargeback" filterable="false" sortable="false"  viewsDenied="pdf">  --%>

					<c:set var="issueTypeStatusId">
						<security:encrypt strToEncrypt="<%=IssueTypeStatusConstantsInterface.CHARGEBACK_NEW.toString()%>"/>
					</c:set>
					
					<c:choose>
						<c:when test="${!transactionDetailModel.issue}">

							<authz:authorize ifAnyGranted="<%=PortalConstants.MNG_CHARGBACK_UPDATE%>">
								<input type="button" class="button" value="Chargeback"
									name="Chargeback${transactionDetailTransactionId}"
									onclick="openChargebackWindow1('Chargeback${transactionDetailTransactionId}','${transactionDetailTransactionId}','${transactionDetailTransactionCodeId}','${transactionDetailModel.transactionCode}','${issueTypeStatusId}','pg_dispute')" />
							</authz:authorize>
							<authz:authorize ifNotGranted="<%=PortalConstants.MNG_CHARGBACK_UPDATE%>">
								<input type="button" class="button" value="Chargeback"
									name="ChargebackDisabled${transactionDetailTransactionId}"
									disabled="disabled" />							
							</authz:authorize>
							
						</c:when>
						<c:otherwise>
							<input type="button" class="button" value="Chargeback"
								name="ChargebackDisabled${transactionDetailTransactionId}"
								disabled="disabled" />
						</c:otherwise>
					</c:choose>
				</ec:column>

				<ec:column alias="" title="SMS" style="text-align: center"
					filterable="false" sortable="false" viewsAllowed="html">
					<%--<ec:column alias="" title="SMS" filterable="false" sortable="false" viewsDenied="xls,pdf" >--%>
					
					<authz:authorize ifAnyGranted="<%=PortalConstants.RSND_SMS_AG_TX_UPDATE%>">
						<input type="button" class="button" value="Resend SMS"
							id="resendSms${transactionDetailTransactionId}" />					
					</authz:authorize>
					<authz:authorize ifNotGranted="<%=PortalConstants.RSND_SMS_AG_TX_UPDATE%>">
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
						parameters="tId={tId${transactionDetailTransactionId}}" />
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
      highlightFormElements();
      document.forms[0].mfsId.focus();
      
      function openTransactionWindow(transactionCode){
		newWindow=window.open('p_transactionissuehistorymanagement.html?transactionCode='+transactionCode,'TransactionSummary', 'width=550,height=350,menubar=no,toolbar=no,left=150,top=150,directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
		if(window.focus) newWindow.focus();
		return false;
      }
  
  
	function openChargebackWindow1(btnName, transactionId, transactionCodeId, transactionCode, issueTypeStatusId, winId)
	{
		childWindow = window.open('p-issueupdateform.html?<%=IssueTypeStatusConstantsInterface.MGMT_PAGE_BTN_NAME%>='+btnName+'&transactionId='+transactionId+'&transactionCodeId='+transactionCodeId+'&transactionCode='+transactionCode+'&<%=IssueTypeStatusConstantsInterface.REQUEST_PARAMETER_NAME%>='+issueTypeStatusId,winId,'width=400,height=250,menubar=no,toolbar=no,left=150,top=150,directories=no,scrollbars=no,resizable=no,status=no');		
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

	function clearSDate(){
		document.forms[0].startDate.value='';
	}

    function validateForm(){
    	return validateFormChar(document.forms[0]);
    }	
	
</script>
	</body>
</html>
