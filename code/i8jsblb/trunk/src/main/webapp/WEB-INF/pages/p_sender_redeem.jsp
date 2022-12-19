<%@include file="/common/taglibs.jsp"%>
<%@ page import='java.util.Calendar'%>
<%@page import="com.inov8.microbank.common.util.IssueTypeStatusConstantsInterface"%>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
<%@ page import='com.inov8.microbank.common.util.SupplierProcessingStatusConstants'%>
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
    <script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
    
	<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
	<link type="text/css" rel="stylesheet" href="styles/ajaxtags.css" />
	<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
	<%@include file="/common/ajax.jsp"%>
	<meta name="title" content="Sender Redeem" />
    <script language="javascript" type="text/javascript">
    	var jq=$.noConflict();
		var serverDate ="<%=PortalDateUtils.getServerDate()%>";
      function error(request)
      {
      	alert("An unknown error has occured. Please contact with the administrator for more details");
      }
       
    </script>
	<%
	String senderRedeemUpdatePermission = PortalConstants.PG_GP_UPDATE;
	senderRedeemUpdatePermission += "," + PortalConstants.SENDER_REDEEM_U;
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
		
			<html:form name='senderRedeemViewForm' commandName="extendedSenderRedeemViewModel" method="post"
				action="p_sender_redeem.html" onsubmit="return validateForm(this)" >
				<table width="750px" border="0">
				<tr>
					<td class="formText" align="right">
						Transaction ID:
					</td>
					<td align="left">
						<html:input path="transactionCode" id="transactionCode" cssClass="textBox" tabindex="6" maxlength="20" onkeypress="return maskNumber(this,event)"/>
					</td>
					
				</tr>
				<tr>
					<td class="formText" width="20%" align="right">
						Sender Mobile No.:
					</td>
					<td align="left" width="30%" >
						<html:input path="saleMobileNo" id="saleMobileNo" cssClass="textBox" maxlength="11" tabindex="2" onkeypress="return maskNumber(this,event)"/>
					</td>
					<td class="formText" align="right" width="20%">
						Sender CNIC:
					</td>
					<td align="left" width="30%">
						<html:input path="senderCnic" id="senderCnic" cssClass="textBox" maxlength="13" tabindex="3" onkeypress="return maskNumber(this,event)"/>
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">
						Transaction Status:
					</td>
					<td align="left">
						<html:select path="supProcessingStatusId" cssClass="textBox" tabindex="7" >
							<html:option value="">---All---</html:option>
							<c:if test="${supplierProcessingStatusList != null}">
								<html:options items="${supplierProcessingStatusList}" itemValue="supProcessingStatusId" itemLabel="name"/>
							</c:if>
						</html:select>
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">

					</td>
					<td align="left">
						<input name="_search" type="submit" class="button" value="Search" tabindex="12" />
						<input name="reset" type="reset"
							onclick="javascript: window.location='p_sender_redeem.html?actionId=${retriveAction}'"
							class="button" value="Cancel" tabindex="13" />
					</td>
					<td colspan="2">
						&nbsp;
					</td>
				</tr>
				<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
			</table>
			</html:form>
		

		<ec:table items="senderRedeemViewModelList" var="senderRedeemViewModel"
		action="${contextPath}/p_sender_redeem.html"
		title="" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" filterable="false">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Sender Redeem.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Sender Redeem.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Cash Reversal Transactions"
				fileName="Sender Redeem.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Sender Redeem.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>

			<ec:row>
				<c:set var="transactionId">
					<security:encrypt strToEncrypt="${senderRedeemViewModel.transactionId}"/>
				</c:set>
				<c:set var="transactionCodeId">
					<security:encrypt strToEncrypt="${senderRedeemViewModel.transactionCodeId}"/>
				</c:set>
				<ec:column property="transactionCode" title="Transaction ID" escapeAutoFormat="True">
					<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
						<a href="p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode=${senderRedeemViewModel.transactionCode}" onClick="return openTransactionWindow('${senderRedeemViewModel.transactionCode}')">
						  ${senderRedeemViewModel.transactionCode}
						</a>
					</authz:authorize>
					<authz:authorize ifNotGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
						${senderRedeemViewModel.transactionCode}
					</authz:authorize>
				</ec:column>
				<ec:column property="createdOn" cell="date" format="dd/MM/yyyy hh:mm a"  title="Date"/>
				<ec:column property="saleMobileNo" title="Sender Mobile No." escapeAutoFormat="True" style="text-align: center"/>
				<ec:column property="senderCnic" title="Sender CNIC" escapeAutoFormat="True" style="text-align: center"/>
				<ec:column property="agent1Id" title="Agent ID" escapeAutoFormat="true"/>
				<ec:column property="recipientMobileNo" title="Recipient Mobile No." escapeAutoFormat="True" style="text-align: center"/>
				<ec:column property="recipientCnic" title="Recipient CNIC" escapeAutoFormat="True" style="text-align: center"/>
				<ec:column property="productName" title="Product"/>
				<ec:column property="transactionAmount" title="Amount" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="serviceChargesExclusive" title="Exclusive Charges" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
 			    <ec:column property="totalAmount" title="Total Customer Charges" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="processingStatusName" title="Status"/>
				<ec:column property="updateP2PFlagString" filterable="false" sortable="false" title="Available for Payment" />
				<ec:column alias="" title="Action" viewsAllowed="html" sortable="false">
					<c:choose>
	 			    	<c:when test="${senderRedeemViewModel.reversable}">
	 			    		<authz:authorize ifAnyGranted="<%=senderRedeemUpdatePermission%>">
	 			    			<input type="button" class="button" value="Reverse" id="${transactionId}" name="${transactionId}" onclick="openTransactionReversalWindow('${transactionId}','${transactionId}','${senderRedeemViewModel.transactionCode}','${senderRedeemViewModel.transactionCodeId}')"/>
	 			    		</authz:authorize>
	 			    		<authz:authorize ifNotGranted="<%=senderRedeemUpdatePermission%>">
	 			    			<input type="button" class="button" value="Reverse" id="${transactionId}" name="${transactionId}" disabled="disabled"/>
	 			    		</authz:authorize>
	 			    	</c:when>
 			    	</c:choose>
				</ec:column>
				<ec:column property="reversedByName" title="Reversed By"/>
				<ec:column property="reversedDate" title="Reversed On" cell="date" format="dd/MM/yyyy hh:mm a"/>
				<ec:column property="reversedComments" title="Reversal Comments"/>
			</ec:row>
		</ec:table>

		<ajax:select source="supplierId" target="productId"
		baseUrl="${contextPath}/p_refData.html"
		parameters="supplierId={supplierId},rType=1,actionId=${retriveAction}" errorFunction="error"/>


		<script language="javascript" type="text/javascript">

			document.forms[0].agent1Id.focus();

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
			function openTransactionReversalWindow( btnName, transactionId, transactionCode, transactionCodeId )
			{
				var popupWidth = 400;
				var popupHeight = 250;
				var popupLeft = (window.screen.width - popupWidth)/2;
				var popupTop = (window.screen.height - popupHeight)/2;
				var url = 'p_transactionreversalform.html?<%=IssueTypeStatusConstantsInterface.MGMT_PAGE_BTN_NAME%>='+btnName+'&transactionId='+transactionId+'&transactionCode='+transactionCode+'&transactionCodeId='+transactionCodeId;
				childWindow = window.open( url, 'TransactionReversalForm', 'width=' + popupWidth + ',height='+popupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+' ,directories=no,scrollbars=no,resizable=no,status=no' );
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
	      function error(request)
      	  {
      		alert("An unknown error has occured. Please contact with the administrator for more details");
      	  }
      	</script>
      	<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>
	</body>
</html>
