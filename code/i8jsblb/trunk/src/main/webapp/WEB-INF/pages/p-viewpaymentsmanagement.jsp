
<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.*,com.inov8.microbank.common.util.*'%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<c:set var="defaultAction"><%=PortalConstants.ACTION_DEFAULT %></c:set>
<html>
<head>
<meta name="decorator" content="decorator">
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/prototype.js"></script>
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
<meta name="title" content="View Payments" />
</head>
<body bgcolor="#ffffff" onunload="javascript:closeChild();" >

<div id="smsContent" class="ajaxMsg"></div>
<c:if test="${not empty messages}">
	<div class="infoMsg" id="successMessages"><c:forEach var="msg"
		items="${messages}">
		<c:out value="${msg}" escapeXml="false" />
		<br />
	</c:forEach></div>
	<c:remove var="messages" scope="session" />
</c:if>
<table width="100%">
	<html:form commandName="extendedPaymentsListViewModel" method="post"
		action="p-viewpaymentsmanagement.html" onsubmit="return validateForm()"  >

		<tr>
			<td align="right" class="formText" width="33%">Inov8 MWallet ID:</td>
			<td width="17%">			
			<html:input onkeypress="return maskCommon(this,event)" path="mfsId" id="mfsId" cssClass="textBox"  tabindex="1" maxlength="8"/> 						
			</td>

			<td align="right" class="formText" width="16%">Mobile #:</td>
			<td width="34%">
			<html:input onkeypress="return maskCommon(this,event)" path="currentMobileNo" cssClass="textBox" tabindex="2" maxlength="11"/>			
			</td>

		</tr>

		<tr>
		<td align="right" class="formText">Transaction Code:</td> 
			<td>
				<html:input onkeypress="return maskCommon(this,event)" path="transactionCode" cssClass="textBox" tabindex="3" maxlength="50"/>
		
			</td>
			
			<td class="formText" align="right" >Start Date:</td>
			<td align="left" >
			<html:input path="startDate"id="startDate" tabindex="-1" readonly="true" cssClass="textBox" /> 
			<img
				id="sDate" tabindex="4" name="popcal" align="top"
				style="cursor:pointer" src="images/cal.gif" border="0" /> 
			<img
				id="sDate" tabindex="5" title="Clear Date" name="popcal"
				onclick="javascript:$('startDate').value=''" align="middle"
				style="cursor:pointer" src="images/refresh.png" border="0" />
		</td>
		</tr>   
		<tr>
		
		<td  align="right" class="formText">Service:</td>
	    				<td  align="left">
	    				  <html:select path="productId" cssClass="textBox" tabindex="6">
			                 <html:option value=""  >---All---</html:option>
			                 <c:if test="${custDisputeServiceListViewModelList !=null}">
                                 <html:options items="${custDisputeServiceListViewModelList}" itemLabel="productName" itemValue="productId"/>
			                 </c:if>
			              </html:select>    
			</td>
			
		
			<td class="formText" align="right">End Date:</td>
			<td align="left">
			
			<html:input path="endDate" id="endDate" tabindex="-1"	cssClass="textBox" readonly="true" maxlength="10"  /> 			
			<img id="eDate" tabindex="7"
				name="popcal" align="top" style="cursor:pointer" src="images/cal.gif"
				border="0" />			
			<img id="eDate" tabindex="8" title="Clear Date"
				name="popcal" onclick="javascript:$('endDate').value=''"
				align="middle" style="cursor:pointer" src="images/refresh.png"
				border="0" /></td>
		</tr>
		
		
		<tr>
		
		<td  align="right" class="formText">Bank:</td>
	    				<td  align="left">
	    				  <html:select path="bankId" cssClass="textBox" tabindex="6">
								<html:option value="">---All---</html:option>
								<c:if test="${bankModelList!=null}">
									<html:options items="${bankModelList}"
										itemLabel="name" itemValue="bankId" />
								</c:if>
			              </html:select>    
			</td>
			<td class="formText" align="right"></td>
			<td align="left">&nbsp;</td>
		</tr>		
		
		<tr>
			<td align="right" class="formText">&nbsp;</td>
			<td><span class="formText">
			
			
			<input type ="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>" />
				<security:isauthorized action="<%=PortalConstants.ACTION_RETRIEVE%>">
						<jsp:attribute name="ifAccessAllowed">
						<input type="submit" class="button" value="Search"	name="_search" tabindex="9"	/>
						</jsp:attribute>
						<jsp:attribute name="ifAccessNotAllowed"> 
  				              <input type="submit" class="button" value="Search" name="_search" tabindex="-1" disabled="disabled"/>
						</jsp:attribute>
			        </security:isauthorized> 
			
			<input	name="reset" type="reset" class="button" value="Cancel" tabindex="10" onclick="javascript: window.location='p-viewpaymentsmanagement.html?actionId=${retriveAction}'"/>
			</span>
			</td>
			<td class="formText" align="right">&nbsp;</td>
			<td align="left">&nbsp;</td>
		</tr>
	</html:form>
</table>
<ec:table items="paymentsModelList" var="paymentsModel"
	filterable="false"
	action="${pageContext.request.contextPath}/p-viewpaymentsmanagement.html?actionId=${retriveAction}"
	title=""
	retrieveRowsCallback="limit" filterRowsCallback="limit"
	sortRowsCallback="limit"
	>
	<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
		<ec:exportXls fileName="View Payments.xls" tooltip="Export Excel" />
	</authz:authorize>
	<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
		<ec:exportXlsx fileName="View Payments.xlsx" tooltip="Export Excel" />
	</authz:authorize>
	<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
		<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
			headerTitle="View Payments" fileName="View Payments.pdf" tooltip="Export PDF" />
	</authz:authorize>
	<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
		<ec:exportCsv fileName="View Payments.csv" tooltip="Export CSV"></ec:exportCsv>	
	</authz:authorize>
	<ec:row>
	<c:set var="paymentsModelTransactionId">
			<security:encrypt strToEncrypt="${paymentsModel.transactionId}"/>
	</c:set>
	<c:set var="paymentsModelTransactionCodeId">
			<security:encrypt strToEncrypt="${paymentsModel.transactionCodeId}"/>
	</c:set>	
	
		<ec:column property="transactionCode" filterable="false"
			title="Transaction Code" escapeAutoFormat="true">
			<input type ="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>" />

					<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
						<a href="p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode=${paymentsModel.transactionCode}" onclick="return openTransactionWindow('${paymentsModel.transactionCode}')">
						${paymentsModel.transactionCode} </a>
					</authz:authorize>
					<authz:authorize ifNotGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
						${paymentsModel.transactionCode}
					</authz:authorize>

			        
		</ec:column>
		<ec:column property="mfsId" filterable="false" title="Inov8 MWallet ID" />
		<ec:column property="saleMobileNo" filterable="false"
			title="Subscriber Number" escapeAutoFormat="true"/>

		<ec:column property="notificationMobileNo" filterable="false"
			title="Sale Number" escapeAutoFormat="true"/>

		<%if(UserUtils.getCurrentUser().getAppUserTypeId().longValue() != UserTypeConstantsInterface.MNO.longValue() ){%>
			<ec:column property="consumerNo" filterable="false"
				title="Reference Number" escapeAutoFormat="true"/>			
		<%}%>
				
		<ec:column property="createdOn" cell="date" format="dd/MM/yyyy hh:mm a"
			filterable="false" title="Date" />
		<ec:column property="productName" filterable="false" title="Service" />
		<ec:column property="bankName" filterable="false" title="Bank" />
		<ec:column property="bankAccountNoLastFive" filterable="false" title="Account No." escapeAutoFormat="true"/>
		<ec:column property="supplierCommission" filterable="false" title="Commission"  calc="total" calcTitle="Total:" cell="currency" format="0.00"/>		
  	    <ec:column property="amount" calc="total" calcTitle="Total:" cell="currency" format="0.00" filterable="false" title="Amount" />				

	<ec:column property="processingStatusName" filterable="false"  title="Status" />
		<ec:column alias=" " title="Dispute" filterable="false"
			sortable="false" viewsAllowed="html">
				<c:set var="issueTypeId">
					<security:encrypt strToEncrypt="<%=IssueTypeStatusConstantsInterface.DISPUTE_NEW.toString()%>"/>
				</c:set>
			
			<c:choose>
				<c:when test="${!paymentsModel.issue}">
				
				<input type ="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_DEFAULT%>" />
				<security:isauthorized action="<%=PortalConstants.ACTION_DEFAULT%>">
						<jsp:attribute name="ifAccessAllowed">
						<input type="button" class="button" value="Disputed"
						name="Dispute${paymentsModelTransactionId}"
						onclick="openIssueWindow('Dispute${paymentsModelTransactionId}', '${paymentsModelTransactionId}','${paymentsModelTransactionCodeId}', '${paymentsModel.transactionCode}', '${issueTypeId}', 'pg_dispute')"/>
						</jsp:attribute>
						<jsp:attribute name="ifAccessNotAllowed"> 
					    <input type="button" class="button" value="Disputed"
						name="Dispute${paymentsModelTransactionId}"
						disabled="disabled"/>
						</jsp:attribute>
			        </security:isauthorized> 
				</c:when>
				<c:otherwise>
					<input type="button" class="button" value="Disputed"
						name="DisputeDisabled${paymentsModelTransactionId}"
						disabled="disabled" />
				</c:otherwise>
			</c:choose>
		</ec:column>
		<ec:column alias="SMS" title="SMS" filterable="false" sortable="false"
			viewsAllowed="html">
			
			<input type ="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_DEFAULT%>" />
				<security:isauthorized action="<%=PortalConstants.ACTION_DEFAULT%>">
						<jsp:attribute name="ifAccessAllowed">
							<input type="button" class="button" value="Resend SMS"
								id="resendSms${paymentsModelTransactionId}" />
							<input type="hidden" value="${paymentsModelTransactionId}"
								name="tId${paymentsModelTransactionId}"
								id="tId${paymentsModelTransactionId}" />
							<ajax:htmlContent baseUrl="${contextPath}/p_resendSms.html?actionId=${defaultAction}"
								source="resendSms${paymentsModelTransactionId}"
								target="smsContent"
								parameters="tId={tId${paymentsModelTransactionId}}" />
						</jsp:attribute>
						<jsp:attribute name="ifAccessNotAllowed"> 
					   <input type="button" class="button" value="Resend SMS"
						id="resendSms${paymentsModelTransactionId}" disabled="disabled"/>
				</jsp:attribute>
			        </security:isauthorized> 
			
			
		</ec:column>



	</ec:row>
</ec:table>
<script language="javascript" type="text/javascript">

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


	function openIssueWindow(btnName, transactionId, transactionCodeId, transactionCode, issueStId, winId)
		{
			childWindow = window.open('p-issueupdateform.html?actionId=${defaultAction}&<%=IssueTypeStatusConstantsInterface.MGMT_PAGE_BTN_NAME%>='+btnName+'&transactionId='+transactionId+'&transactionCodeId='+transactionCodeId+'&transactionCode='+transactionCode+'&<%=IssueTypeStatusConstantsInterface.REQUEST_PARAMETER_NAME%>='+issueStId+'',winId,'width=400,height=250,menubar=no,toolbar=no,left=150,top=150,directories=no,scrollbars=no,resizable=no,status=no');		
		}

      function openTransactionWindow(transactionCode)
      {
		newWindow=window.open('p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode='+transactionCode,'TransactionSummary', 'width=550,height=350,menubar=no,toolbar=no,left=150,top=150,directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
		if(window.focus) newWindow.focus();
			return false;
      }


function confirmUpdateStatus(link)
{
    if (confirm('Are you sure you want to update status?')==true)
    {
      window.location.href=link;
    }
}


</script>

<script type="text/javascript">

	  document.forms[0].mfsId.focus();	
 
      function openTransactionWindow(transactionCode){
		newWindow=window.open('p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode='+transactionCode,'TransactionSummary', 'width=550,height=350,menubar=no,toolbar=no,left=150,top=150,directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
		if(window.focus) newWindow.focus();
		return false;
      }

       function validateForm(){
       	return validateFormChar(document.forms[0]);
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


















