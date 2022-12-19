<%@page import="com.inov8.microbank.common.util.PortalConstants"%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<%@include file="/common/taglibs.jsp"%>
<html>
	<head>
<meta name="decorator" content="decorator">
   		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script> 
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
		<script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
		
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/deliciouslyblue/calendar.css" />	    
		<script type="text/javascript" src="<c:url value='/scripts/activatedeactivate.js'/>"></script>
		<script type="text/javascript">
		var jq=$.noConflict();
		var serverDate ="<%=PortalDateUtils.getServerDate()%>";
			function actdeact(request)
			{
				isOperationSuccessful(request);
			}
			function error(request)
		      {
		      	alert("An unknown error has occured. Please contact with the administrator for more details");
		      }

			function openActionLogDetailWindow(actionLogId)
			{
				var url = 'actionlogdetail.html?actionLogId='+actionLogId;
				var popupWidth = 800;
				var popupHeight = 550;
				var popupLeft = (window.screen.width - popupWidth)/2;
				var popupTop = (window.screen.height - popupHeight)/2;
				childWindow=window.open(url,'Action Log Detail Window', 'width='+popupWidth+',height='+popupHeight+'' +
						',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
				if(window.focus) childWindow.focus();
			}

		    function openActionDetailWindow(actionAuthorizationId,usecaseId)
			{
			    var url = null;
		        var popupWidth = 800;
				var popupHeight = 550;
				var popupLeft = (window.screen.width - popupWidth)/2;
				var popupTop = (window.screen.height - popupHeight)/2;
				
		        if( usecaseId == <%=PortalConstants.ONE_TIME_PIN_RESET_USECASE_ID %>)
					url = 'p_resettransactioncodeauthorizationDetail.html?actionAuthorizationId='+actionAuthorizationId+'&escalateRequest=true&resolveRequest=false';		
				else if (usecaseId ==<%=PortalConstants.RESEND_SMS_USECASE_ID %>)
					url = 'p-resendsmsauthorizationdetail.html?actionAuthorizationId='+actionAuthorizationId+'&escalateRequest=true&resolveRequest=false';
				else if (usecaseId ==<%=PortalConstants.RESET_AGENT_PASSWORD_PORTAL_USECASE_ID%>)
					url = 'p-resetportalpasswordauthorizationdetail.html?actionAuthorizationId='+actionAuthorizationId+'&escalateRequest=true&resolveRequest=false';
				else if (usecaseId ==<%=PortalConstants.RESET_USER_PASSWORD_PORTAL_USECASE_ID%>)
					url = 'p-resetuserportalpasswordauthorizationdetail.html?actionAuthorizationId='+actionAuthorizationId+'&escalateRequest=true&resolveRequest=false';
				else if ((usecaseId ==<%=PortalConstants.DEACTIVATE_CUSTOMER_USECASE_ID%>) || (usecaseId ==<%=PortalConstants.BLOCK_CUSTOMER_USECASE_ID%>)
						||(usecaseId ==<%=PortalConstants.REACTIVATE_CUSTOMER_USECASE_ID%>) || (usecaseId ==<%=PortalConstants.UNBLOCK_CUSTOMER_USECASE_ID%>)
						||(usecaseId ==<%=PortalConstants.DEACTIVATE_AGENT_USECASE_ID%>) || (usecaseId ==<%=PortalConstants.BLOCK_AGENT_USECASE_ID%>)
						||(usecaseId ==<%=PortalConstants.REACTIVATE_AGENT_USECASE_ID%>) || (usecaseId ==<%=PortalConstants.UNBLOCK_AGENT_USECASE_ID%>))
					url = 'p-lockunlockaccountauthorizationdetail.html?actionAuthorizationId='+actionAuthorizationId+'&escalateRequest=true&resolveRequest=false';	
				
				else if (usecaseId ==<%=PortalConstants.LINK_PAYMENT_MODE_USECASE_ID %>)
					url = 'p-linkpaymentmodeauthorizationdetail.html?actionAuthorizationId='+actionAuthorizationId+'&escalateRequest=true&resolveRequest=false';
				else if (usecaseId ==<%=PortalConstants.DELETE_PAYMENT_MODE_USECASE_ID %> || usecaseId ==<%=PortalConstants.DELINK_PAYMENT_MODE_USECASE_ID%>
							|| usecaseId ==<%=PortalConstants.RELINK_PAYMENT_MODE_USECASE_ID%>)
					url = 'p-allpaypaymentmodeauthorizationdetail.html?actionAuthorizationId='+actionAuthorizationId+'&escalateRequest=true&resolveRequest=false';		
				else if (usecaseId ==<%=PortalConstants.I8_USER_MANAGEMENT_CREATE_USECASE_ID %>)
					url = 'p-createnewuserauthorizationdetailfrom.html?actionAuthorizationId='+actionAuthorizationId+'&escalateRequest=true&resolveRequest=false';
				else if (usecaseId ==<%=PortalConstants.I8_USER_MANAGEMENT_UPDATE_USECASE_ID%>)
					url = 'p-updateuserauthorizationdetailform.html?actionAuthorizationId='+actionAuthorizationId+'&escalateRequest=true&resolveRequest=false';	
				else if (usecaseId ==<%=PortalConstants.RETAILER_CONTACT_FORM_USECASE_ID%>)
					url = 'p-createagentauthorizationdetailform.html?actionAuthorizationId='+actionAuthorizationId+'&escalateRequest=true&resolveRequest=false';	
				else if (usecaseId ==<%=PortalConstants.RETAILER_FORM_USECASE_ID%>)
					url = 'p-createfranchiseauthorizationdetailform.html?actionAuthorizationId='+actionAuthorizationId+'&escalateRequest=true&resolveRequest=false';	
				else if (usecaseId ==<%=PortalConstants.RETAILER_FORM_UPDATE_USECASE_ID%>)
					url = 'p-updatefranchiseauthorizationdetailform.html?actionAuthorizationId='+actionAuthorizationId+'&escalateRequest=true&resolveRequest=false';	
				else if (usecaseId ==<%=PortalConstants.MFS_ACCOUNT_CREATE_USECASE_ID%>)
					url = 'p-createcustomerauthorizationdetail.html?actionAuthorizationId='+actionAuthorizationId+'&escalateRequest=true&resolveRequest=false';	
				else if (usecaseId ==<%=PortalConstants.MFS_ACCOUNT_UPDATE_USECASE_ID%>)
					url = 'p-updatecustomerauthorizationdetail.html?actionAuthorizationId='+actionAuthorizationId+'&escalateRequest=true&resolveRequest=false';	
				else if (usecaseId ==<%=PortalConstants.USER_GROUP_CREATE_USECASE_ID%> || usecaseId ==<%=PortalConstants.USER_GROUP_UPDATE_USECASE_ID%>)
					url = 'p-usergroupauthorizationdetail.html?actionAuthorizationId='+actionAuthorizationId+'&escalateRequest=true&resolveRequest=false';
				else if (usecaseId ==<%=PortalConstants.MANUAL_ADJUSTMENT_USECASE_ID%>)
					url = 'p-manualadjustmentauthorizationdetail.html?actionAuthorizationId='+actionAuthorizationId+'&escalateRequest=true&resolveRequest=false';
				else if (usecaseId ==<%=PortalConstants.CREATE_L2_USECASE_ID%>)
					url = 'p-createl2accountauthorizationdetail.html?actionAuthorizationId='+actionAuthorizationId+'&escalateRequest=true&resolveRequest=false';
				else if (usecaseId ==<%=PortalConstants.UPDATE_L2_USECASE_ID%>)
					url = 'p-updatel2accountauthorizationdetail.html?actionAuthorizationId='+actionAuthorizationId+'&escalateRequest=true&resolveRequest=false';
				else if (usecaseId ==<%=PortalConstants.CREATE_L3_USECASE_ID%>)
					url = 'p-createl3accountauthorizationdetail.html?actionAuthorizationId='+actionAuthorizationId+'&escalateRequest=true&resolveRequest=false';
				else if (usecaseId ==<%=PortalConstants.UPDATE_L3_USECASE_ID%>)
					url = 'p-updatel3accountauthorizationdetail.html?actionAuthorizationId='+actionAuthorizationId+'&escalateRequest=true&resolveRequest=false';
				else if (usecaseId ==<%=PortalConstants.UPDATE_P2P_DETAILS_USECASE_ID%>)
					url = 'p-updatep2ptxauthorizationdetail.html?actionAuthorizationId='+actionAuthorizationId+'&escalateRequest=true&resolveRequest=false';
				else if (usecaseId ==<%=PortalConstants.UPDATE_USECASE%>)
					url = 'p-updateusecaseauthorizationdetail.html?actionAuthorizationId='+actionAuthorizationId+'&escalateRequest=true&resolveRequest=false';			
				else if (usecaseId ==<%=PortalConstants.UPDATE_CUSTOMER_MOBILE_USECASE_ID%> || usecaseId ==<%=PortalConstants.UPDATE_AGENT_MOBILE_USECASE_ID%> || usecaseId ==<%=PortalConstants.UPDATE_HANDLER_MOBILE_USECASE_ID%>)
					url = 'p-updatemobileauthorizationdetail.html?actionAuthorizationId='+actionAuthorizationId+'&escalateRequest=true&resolveRequest=false';
				else if (usecaseId ==<%=PortalConstants.UPDATE_CUSTOMER_ID_DOC_NO_USECASE_ID%> || usecaseId ==<%=PortalConstants.UPDATE_AGENT_ID_DOC_NO_USECASE_ID%> || usecaseId ==<%=PortalConstants.UPDATE_HANDLER_ID_DOC_NO_USECASE_ID%>)
					url = 'p-updatecnicauthorizationdetail.html?actionAuthorizationId='+actionAuthorizationId+'&escalateRequest=true&resolveRequest=false';
				else if (usecaseId ==<%=PortalConstants.UPDATE_ACCOUNT_TO_BLINK_USECASE_ID%> || usecaseId ==<%=PortalConstants.UPDATE_ACCOUNT_TO_BLINK_USECASE_ID%> || usecaseId ==<%=PortalConstants.UPDATE_ACCOUNT_TO_BLINK_USECASE_ID%>)
					url = 'p-updatecnicauthorizationdetail.html?actionAuthorizationId='+actionAuthorizationId+'&escalateRequest=true&resolveRequest=false';
				else if (usecaseId ==<%=PortalConstants.KEY_MFS_DEBIT_CARD_UPDATE_USECASE_ID%>)
					url = 'p-updatedebitcardrequestauthorizationdetail.html?actionAuthorizationId='+actionAuthorizationId+'&escalateRequest=true&resolveRequest=false';
				else if (usecaseId ==<%=PortalConstants.UPDATE_CUSTOMER_NAME_USECASE_ID%>)
					url = 'p_updateCustomerNameAauthorization.html?actionAuthorizationId='+actionAuthorizationId+'&escalateRequest=true&resolveRequest=false';

				childWindow=window.open(url,'Action Detail Window', 'width='+popupWidth+',height='+popupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
			    if(window.focus) childWindow.focus();
			    return false;
			}

		</script>
		<meta name="title" content="User Action Logs" />
	</head>
	<body bgcolor="#ffffff">
  	<%@include file="/common/ajax.jsp"%>

	<div id="successMsg" class ="infoMsg" style="display:none;"></div>
		<c:if test="${not empty messages}">
	    	<div class="infoMsg" id="successMessages">
	      		<c:forEach var="msg" items="${messages}">
		        	<c:out value="${msg}" escapeXml="false"/>
		        	<br/>
	      		</c:forEach>
	    	</div>
	    	<c:remove var="messages" scope="session"/>
	  	</c:if>

		<html:form name="mnologsListViewSearchForm" commandName="mnologsListViewSearchModel" method="post" action="p_mnologsmanagement.html" onsubmit="return validateForm(this)">
			<input type="hidden" name="appUserId" value="${param.appUserId}">
			<table border="0" width="750px" >
				<tr>
					<td  align="right"  class="formText">User Action:
		            </td>
					<td align="left">
						<html:select path="usecaseId" tabindex="1" cssClass="textBox">
							<html:option value="">---All---</html:option>
							<c:if test="${usecaseModelList != null}">
								<html:options items="${usecaseModelList}" itemValue="usecaseId" itemLabel="name" />
							</c:if>
						</html:select>
					</td>
					<td  align="right" class="formText" width="20%">
						Client IP:
					</td>
					<td align="left" width="30%" >
						<html:input path="clientIp" id="clientIp" cssClass="textBox" onkeypress="return maskNumber(this,event)"/>
					</td>

				</tr>
				<tr>
					<td  align="right" class="formText" width="20%">
						User Name:
					</td>
					<td align="left"  width="30%">
						<html:input path="userName" id="userName" cssClass="textBox"/>
					</td>
					<td  align="right" class="formText" width="20%">
						Reference:
					</td>
					<td align="left" width="30%" >
						<html:input path="customField11" id="customField11" cssClass="textBox"/>
					</td>
				</tr>
				<tr>
					<td  align="right" class="formText" width="20%">
						Action Authorization ID:
					</td>
					<td align="left"  width="30%">
						<html:input path="actionAuthorizationId" id="actionAuthorizationId" cssClass="textBox"/>
					</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
	            	<td width="20%"  align="right"  class="formText">From Date:</td>
	             	<td width="30%" align="left" >
	             		<html:input path="fromDate" cssClass="textBox" readonly="true" tabindex="1"/>
						<img id="fDate" tabindex="2" name="popcal" align="top" style="cursor:pointer" src="${pageContext.request.contextPath}/images/cal.gif" border="0" />
						<img id="fDate" tabindex="3" name="popcal" title="Clear Date" onclick="javascript:$('fromDate').value=''" align="middle" style="cursor:pointer"
							src="${pageContext.request.contextPath}/images/refresh.png" border="0" />
					</td>
					<td width="20%"  align="right"  class="formText">To Date:</td>
		             <td width="30%" align="left" >
	             		<html:input path="toDate" cssClass="textBox" readonly="true" tabindex="4"/>
						<img id="tDate" tabindex="5" name="popcal" align="top"  style="cursor:pointer" src="${pageContext.request.contextPath}/images/cal.gif" border="0" />
						<img id="tDate" tabindex="6" name="popcal" title="Clear Date" onclick="javascript:$('toDate').value=''" align="middle" style="cursor:pointer"
							src="${pageContext.request.contextPath}/images/refresh.png" border="0" />
					</td>
		       	</tr>
			  <tr>
				<td></td>
			    <td align="left" >
			    	<input type="submit"  class="button" value="Search" tabindex="3" name="_search"/>
			    	<input type="reset" class="button" value="Cancel"  name="_cancel" onClick="javascript: window.location='p_mnologsmanagement.html'">
			   	</td>
			  </tr>
	 	 	</table>
		</html:form>

		<ec:table filterable="false" items="mnologsListViewModels"
			var="mnologsListViewModel" retrieveRowsCallback="limit"
			filterRowsCallback="limit" sortRowsCallback="limit"
			action="${pageContext.request.contextPath}/p_mnologsmanagement.html"
			title="">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="User Action Logs.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="User Action Logs.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="Logs" fileName="User Action Logs.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="User Action Logs.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>
				<ec:column filterable="false" property="startTime" cell="date" format="dd/MM/yyyy hh:mm a" alias="Date" title="Date"/>
				<ec:column filterable="false" property="trxId" title="Transaction ID"  />
				<ec:column filterable="false" property="customField11" title="Reference ID"  />
				<ec:column filterable="true" property="enquiryScreen" title="Enquiry Screen"  />
				<ec:column filterable="false" property="userName" title="User Name (Maker)"  />
				<c:choose>
					<c:when test="${actionType1 eq null}">
						<ec:column filterable="true" property="usecaseName" title="Action Type 1"  />
					</c:when>
					<c:otherwise>
						<ec:column filterable="true" property="actionType1" title="Action Type 1"  />
					</c:otherwise>
				</c:choose>
				<ec:column filterable="true" property="oldSenderNIC" title="Old Sender CNIC"  />
				<ec:column filterable="true" property="newSenderNIC" title="New Sender CNIC"  />
				<ec:column filterable="true" property="actionType2" title="Action Type 2"  />
				<ec:column filterable="true" property="oldSenderMobile" title="Old Sender Mobile"  />
				<ec:column filterable="true" property="newSenderMobile" title="New Sender Mobile"  />
				<ec:column filterable="true" property="actionType3" title="Action Type 3"  />
				<ec:column filterable="true" property="oldRecipientNIC" title="Old Receipient CNIC"  />
				<ec:column filterable="true" property="newRecipientNIC" title="New Receipient CNIC"  />
				<ec:column filterable="true" property="actionType4" title="Action Type 4"  />
				<ec:column filterable="true" property="oldRecipientMobile" title="Old Receipient Mobile"  />
				<ec:column filterable="true" property="newRecipientMobile" title="New Receipient Mobile"  />
				<ec:column filterable="false" property="actionAuthorizationId" title="Action Authorization ID"  />
				<ec:column filterable="false" property="actionStatus" title="Authorization Status"  />
				<ec:column filterable="false" property="checkedBy" title="Action Checker"  />
				<ec:column filterable="false" property="comments" title="Comments"  />

				<%--<ec:column filterable="false" property="actionAuthorizationId" title="Authorization ID" escapeAutoFormat="true" >
					<a onclick="openActionDetailWindow(${mnologsListViewModel.actionAuthorizationId},${mnologsListViewModel.usecaseId});">${mnologsListViewModel.actionAuthorizationId}</a>
				</ec:column>
				<ec:column filterable="false" property="clientIp" title="Client Ip"  />
				<ec:column filterable="false" property="actionStatus" title="Action Status"  />
				<ec:column escapeAutoFormat="true" property="actionLogId" title="View Input/Output XML" >
					<a onclick="openActionLogDetailWindow(${mnologsListViewModel.actionLogId});">Detail</a>
				</ec:column>--%>
			</ec:row>
		</ec:table>
		<script language="javascript" type="text/javascript">
		
	      function confirmUpdateStatus(link)
	      {
	        if (confirm('Are you sure you want to update status?')==true)
	        {
	          window.location.href=link;
	        }
	      }
	      
	      Calendar.setup(
	      {
	        inputField  : "fromDate", // id of the input field
	        button      : "fDate"    // id of the button
	      }
	      );
	      
	   	  Calendar.setup(
	      {
	        inputField  : "toDate", // id of the input field
	        button      : "tDate",    // id of the button
	        isEndDate: true
	      }
	      );
	      
	      </script>
		<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>
	</body>
</html>