<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.IssueTypeStatusConstantsInterface"%>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>

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
	<%@include file="/common/ajax.jsp"%>
	<meta name="title" content="Beneficiary(Walk-in) Transaction History" />
    <script language="javascript" type="text/javascript">

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
		
		<table width="750px" border="0">
			<html:form name='transactionAllViewModelForm'
				commandName="extendedTransactionAllViewModel" method="post"
				action="p_walkincustomerltransactiondetails.html" onsubmit="return validateForm(this)">
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
						<html:input path="recipientMobileNo" id="notificationMobileNo" cssClass="textBox" maxlength="11" tabindex="2" onkeypress="return maskNumber(this,event)"/> 
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
						<html:select path="processingStatusName" cssClass="textBox" tabindex="4" >
							<html:option value="">---All---</html:option>
							<c:if test="${supplierProcessingStatusList != null}">
								<html:options items="${supplierProcessingStatusList}" itemValue="name" itemLabel="name"/>
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
				  <security:isauthorized action="<%=PortalConstants.ACTION_RETRIEVE%>">
						<jsp:attribute name="ifAccessAllowed">
								<input name="_search" type="submit" class="button" value="Search" tabindex="11"  /> 
		          		</jsp:attribute>
						<jsp:attribute name="ifAccessNotAllowed">  
					          <input name="_search" type="submit" class="button" value="Search" tabindex="-1" disabled="disabled" /> 
						</jsp:attribute>
		         </security:isauthorized> 
					<input name="reset" type="reset" 
						onclick="javascript: window.location='p_walkincustomerltransactiondetails.html?actionId=${retriveAction}&cnic=${param.cnic}'"
						class="button" value="Cancel" tabindex="12" />
					</td>
					<td class="formText" align="right">

					</td>
					<td align="left">
					</td>
				</tr>

				<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
					value="<%=PortalConstants.ACTION_RETRIEVE%>">
				<input type="hidden" name="cnic" value="${param.cnic}">
			</html:form>
		</table>
		
		
		<ec:table items="transactionAllViewModelList" var="transactionAllViewModel"
		action="${contextPath}/p_walkincustomerltransactiondetails.html"
		title="" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" filterable="false">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Walkin Customer Transaction Details.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Walkin Customer Transaction Details.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="Walk-in Customer Transactions"
					fileName="Walkin Customer Transaction Details.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Walkin Customer Transaction Details.csv" tooltip="Export CSV"></ec:exportCsv>
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
				<ec:column property="createdOn" cell="date" format="dd/MM/yyyy hh:mm a" filterable="false" title="Date"/>
				<ec:column property="mfsId" filterable="false" title="Sender ID" style="text-align: center"/>
				<ec:column property="saleMobileNo" filterable="false" title="Sender Mobile No." escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="senderCnic" filterable="false" title="Sender CNIC" escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="agent1Id" filterable="false" escapeAutoFormat="true" title="Sender Agent ID"/>
				<ec:column property="recipientMfsId" filterable="false" title="Recipient ID" escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="recipientMobileNo" filterable="false" title="Recipient Mobile No." escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="recipientCnic" filterable="false" title="Recipient CNIC" escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="agent2Id" filterable="false" escapeAutoFormat="true" title="Receiver Agent ID"/>
				<ec:column property="productName" filterable="false" title="Product"/>
				<ec:column property="transactionAmount" filterable="false" title="Amount" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="serviceChargesInclusive" filterable="false" title="Inclusive Charges" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="serviceChargesExclusive" filterable="false" title="Exclusive Charges" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
 			    <ec:column property="totalAmount" filterable="false" title="Total Customer Charges" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="processingStatusName" filterable="false" title="Status" />
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
			</ec:row>
		</ec:table>
		<input type="button" class="button" value="Back" onclick="javascript: window.location.href='p_walkincustomers.html?actionId=2'" />

		<ajax:select source="supplierId" target="productId"
		baseUrl="${contextPath}/p_refData.html"
		parameters="supplierId={supplierId},rType=1,actionId=${retriveAction}" errorFunction="error"/>


		<script language="javascript" type="text/javascript">

			var popupWidth = 550;
			var popupHeight = 350;
			var popupLeft = (window.screen.width - popupWidth)/2;
			var popupTop = (window.screen.height - popupHeight)/2;
			function openTransactionWindow(transactionCode)
			{
		        newWindow=window.open('p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode='+transactionCode,'TransactionSummary', 'width='+popupWidth+',height='+popupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
			    if(window.focus) newWindow.focus();
			    return false;
			}

			var childWindow;
			function openChargebackWindow1(btnName, transactionId, transactionCodeId, transactionCode, issueTypeStatusId, winId)
			{
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
	      
document.forms[0].cnic.focus();

			function validateForm(form){
	        	var currentDate = "<%=PortalDateUtils.getServerDate()%>";
	        	var _fDate = form.startDate.value;
		  		var _tDate = form.endDate.value;
		  		var startlbl = "Start Date";
		  		var endlbl   = "End Date";
	        	var isValid = validateDateRange(_fDate,_tDate,startlbl,endlbl,currentDate);
	        	
	        	if( isValid )
	        	{
	        		isValid = validateFormChar(form);
	        	}
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
