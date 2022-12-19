<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.IssueTypeStatusConstantsInterface"%>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
<%@ page import='com.inov8.microbank.common.util.PortalDateUtils'%>
<%@ page import='com.inov8.microbank.common.util.FinancialInstitutionConstants'%>

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
    <script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
    
	<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
	<link type="text/css" rel="stylesheet" href="styles/ajaxtags.css" />
	<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
	<%@include file="/common/ajax.jsp"%>
	<meta name="title" content="Teller Transaction Report" />
    <script language="javascript" type="text/javascript">
		var jq=$.noConflict();
		var serverDate ="<%=PortalDateUtils.getServerDate()%>";
		
      function openTransactionDetailWindow(transactionCode)
	  {
	      var action = 'p_showtransactiondetail.html?actionId='+${retriveAction}+'&transactionCodeId='+transactionCode+'&isMfs=true';
             newWindow = window.open( action , 'TransactionDetail', 'width=550,height=350,menubar=no,toolbar=no,left=150,top=150,directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
             if(window.focus) newWindow.focus();
                   return false;
	  }

      function error(request)
      {
      	alert("An unknown error has occured. Please contact with the administrator for more details");
      }
     
    </script>
	<%
		String chargebackUpdatePermission = PortalConstants.ADMIN_GP_UPDATE;
		chargebackUpdatePermission += "," + PortalConstants.PG_GP_UPDATE;
		chargebackUpdatePermission += "," + PortalConstants.CSR_GP_UPDATE;
		chargebackUpdatePermission += "," + PortalConstants.REQ_CHARGEBACK_UPDATE;
 	%>

	</head>
	<body bgcolor="#ffffff" onunload="javascript:closeChild();">

		<div id="rsp" class="ajaxMsg"></div>

		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" /><br/>
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>
		
			<html:form name='extendedAllTransactionsForm' commandName="extendedTransactionAllViewModel" method="post"
				action="p_tellertransactiondetail.html" onsubmit="return validateForm(this)" >
				<table width="770px" border="0">
				<tr>
					<td class="formText" width="20%" align="right">
						Recipient Mobile No:
					</td>
					<td align="left" width="30%" >
						<html:input path="recipientMobileNo" id="mfsId" cssClass="textBox" maxlength="50" tabindex="1" onkeypress="return maskNumber(this,event)"/>
					</td>
					<td class="formText" align="right">
						Recipient CNIC:
					</td>
					<td align="left" >
						<html:input path="recipientCnic" id="recipientCnic" cssClass="textBox" maxlength="13" tabindex="4" onkeypress="return maskNumber(this,event)"/>
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
						<!-- Recipient CNIC: -->
					</td>
					<td align="left" ><!--
						<html:input path="recipientCnic" id="recipientCnic" cssClass="textBox" maxlength="13" tabindex="4" onkeypress="return maskNumber(this,event)"/>
					--></td>
				</tr>

				<tr>
					<td class="formText" align="right">
						Transaction ID:
					</td>
					<td align="left">
						<html:input path="transactionCode" id="transactionCode" cssClass="textBox" tabindex="5" maxlength="50" onkeypress="return maskNumber(this,event)"/>
					</td>
					<td class="formText" align="right">
						Product:
					</td>
					<td align="left">
						<html:select path="productName" cssClass="textBox" tabindex="6" >
							<html:option value="">---All---</html:option>
							<c:if test="${productList != null}">
								<html:options items="${productList}" itemValue="name" itemLabel="name"/>
							</c:if>
						</html:select>
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">
						Start Date:
					</td>
					<td align="left">
				        <html:input path="startDate" id="startDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
							<img id="sDate" tabindex="7" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
							<img id="sDate" tabindex="8" title="Clear Date" name="popcal" onclick="javascript:$('startDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
					<td class="formText" align="right">
						End Date:
					</td>
					<td align="left">
					     <html:input path="endDate" id="endDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
					     <img id="eDate" tabindex="9" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
					     <img id="eDate" tabindex="10" title="Clear Date" name="popcal" onclick="javascript:$('endDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">

					</td>
					<td align="left">
						<input name="_search" type="submit" class="button" value="Search" tabindex="12"/>
						<input name="reset" type="reset"
							onclick="javascript: window.location='p_alltransactionsmanagement.html?actionId=${retriveAction}'"
							class="button" value="Cancel" tabindex="13" />
					</td>
					<td class="formText" align="right">

					</td>
					<td align="left">&nbsp;</td>
				</tr>
				<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
				</table>
			</html:form>
		

		<ec:table items="transactionAllViewModelList" var="transactionAllViewModel"
		action="${contextPath}/p_alltransactionsmanagement.html"
		title="" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" filterable="false">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Teller Transaction Report.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Teller Transaction Report.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da" headerTitle="Teller Transaction Report"
					fileName="Teller Transaction Report.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Teller Transaction Report.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>

			<ec:row>
				<c:set var="transactionId">
					<security:encrypt strToEncrypt="${transactionAllViewModel.transactionId}"/>
				</c:set>
				<c:set var="transactionCodeId">
					<security:encrypt strToEncrypt="${transactionAllViewModel.transactionCodeId}"/>
				</c:set>
				<ec:column property="transactionCode" title="Transaction ID" escapeAutoFormat="true">
					<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
						<a href="p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode=${transactionAllViewModel.transactionCode}" onclick="return openTransactionWindow('${transactionAllViewModel.transactionCode}')">
						  ${transactionAllViewModel.transactionCode}
						</a>
					</authz:authorize>
					<authz:authorize ifNotGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
						${transactionAllViewModel.transactionCode}
					</authz:authorize>
				</ec:column>
				<ec:column property="createdOn" cell="date" format="dd/MM/yyyy hh:mm a" title="Date"/>
				<ec:column property="mfsId" title="Sender ID" escapeAutoFormat="True" style="text-align: center"/>
				<ec:column property="saleMobileNo" title="Sender Mobile No." escapeAutoFormat="True" style="text-align: center"/>
				<ec:column property="senderCnic" title="Sender CNIC" escapeAutoFormat="True" style="text-align: center"/>
				<ec:column property="agent1Id" title="Sender Agent ID" escapeAutoFormat="true"/>
				<ec:column property="recipientMfsId" title="Recipient ID" escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="recipientMobileNo" title="Recipient Mobile No." escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="recipientCnic" title="Recipient CNIC" escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="agent2Id" title="Receiver Agent ID" escapeAutoFormat="true"/>
				<ec:column property="productName" title="Product"/>
				<ec:column property="billDueDate" cell="date" format="dd/MM/yyyy" filterable="false" title="Bill Due Date"/>
				<ec:column property="transactionAmount" title="Amount" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="serviceChargesInclusive" title="Inclusive Charges" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="serviceChargesExclusive" title="Exclusive Charges" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
 			    <ec:column property="totalAmount" title="Total Customer Charges" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="processingStatusName" title="Status" />
				<authz:authorize ifAnyGranted="<%=chargebackUpdatePermission%>">
					<ec:column alias="" title="Chargeback" style="text-align: center" sortable="false" viewsAllowed="html">
					<c:set var="issueTypeStatusId">
						<security:encrypt strToEncrypt="<%=IssueTypeStatusConstantsInterface.CHARGEBACK_NEW.toString()%>"/>
					</c:set>

					<c:choose>
						<c:when test="${!transactionAllViewModel.issue}">
							<input type="button" class="button" value="Chargeback" name="Chargeback${transactionId}"
									onclick="openChargebackWindow1('Chargeback${transactionId}','${transactionId}','${transactionCodeId}','${transactionAllViewModel.transactionCode}','${issueTypeStatusId}','pg_dispute')" />
						</c:when>
						<c:otherwise>
							<input type="button" class="button" value="Chargeback" name="ChargebackDisabled${transactionDetailTransactionId}" disabled="disabled" />
						</c:otherwise>
					</c:choose>
				</ec:column>
				</authz:authorize>
				<authz:authorize ifNotGranted="<%=chargebackUpdatePermission%>">
					<ec:column alias="" title="Chargeback" style="text-align: center" sortable="false" viewsAllowed="html">
							<input type="button" class="button" value="Chargeback" name="ChargebackDisabled${transactionDetailTransactionId}" disabled="disabled" />
					</ec:column>
				</authz:authorize>
				<authz:authorize ifAnyGranted="<%=PortalConstants.UPDATE_P2P_DETAILS%>">
					<ec:column alias="" title="Edit" style="text-align: center" sortable="false" viewsAllowed="html">
							<input type="button" class="button" value="Edit" onclick="javascript:changeInfo('${contextPath}/p_updatep2ptx.html?transactionCode=${transactionAllViewModel.transactionCode}');" />
					</ec:column>
				</authz:authorize>
			</ec:row>
		</ec:table>

		<ajax:select source="supplierId" target="productId"
		baseUrl="${contextPath}/p_refData.html"
		parameters="supplierId={supplierId},rType=1,actionId=${retriveAction}" errorFunction="error"/>


		<script language="javascript" type="text/javascript">

			document.forms[0].recipientMobileNo.focus();
			function openTransactionWindow(transactionCode)
			{
		        var popupWidth = 550;
				var popupHeight = 350;
				var popupLeft = (window.screen.width - popupWidth)/2;
				var popupTop = (window.screen.height - popupHeight)/2;
		        newWindow=window.open('p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode='+transactionCode,'TransactionSummary', 'width='+popupWidth+',height='+popupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
			    if(window.focus) newWindow.focus();
			    return false;
			}
			
      		Calendar.setup(
      		{
		       inputField  : "startDate", // id of the input field
			   button      : "sDate",    // id of the button
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
			function openChargebackWindow1(btnName, transactionId, transactionCodeId, transactionCode, issueTypeStatusId, winId)
			{
				var popupWidth = 550;
				var popupHeight = 350;
				var popupLeft = (window.screen.width - popupWidth)/2;
				var popupTop = (window.screen.height - popupHeight)/2;
				childWindow = window.open('p-issueupdateform.html?<%=IssueTypeStatusConstantsInterface.MGMT_PAGE_BTN_NAME%>='+btnName+'&transactionId='+transactionId+'&transactionCodeId='+transactionCodeId+'&transactionCode='+transactionCode+'&<%=IssueTypeStatusConstantsInterface.REQUEST_PARAMETER_NAME%>='+issueTypeStatusId,winId,'width='+popupWidth+',height='+popupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+',directories=no,scrollbars=no,resizable=no,status=no');
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
	      
	      function changeInfo(link) {
				if (confirm('If customer information is verified then press OK to continue.')==true) {
					window.location.href=link;
				}
			}
	      
      	</script>
      	<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>
	</body>
</html>
