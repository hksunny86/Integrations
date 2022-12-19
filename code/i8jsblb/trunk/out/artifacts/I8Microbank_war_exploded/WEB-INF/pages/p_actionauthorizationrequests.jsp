<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
<%@ page import='com.inov8.microbank.common.util.PortalDateUtils'%>

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
	<meta name="title" content="List Authorizations" />
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
    </script>

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
		
			<html:form name='extendedActionAuthorizationModelForm' commandName="extendedActionAuthorizationModel" method="post"
				action="p_actionauthorizationrequests.html" onsubmit="return validateFormData(this)" >
	 			<table width="100%" border="0">
					<tr>
					<td class="formText" width="20%" align="right">
						Action ID:
					</td>
					<td align="left" width="30%" >
						<html:input path="actionAuthorizationId" id="actionAuthorizationId" cssClass="textBox" maxlength="50" tabindex="1"  onkeypress="return maskNumber(this,event)"/>
					</td>
						<td class="formText" align="right">
						Segment:
					</td>
						<td align="left">
							<html:select path="segmentId" cssClass="textBox" tabindex="10" >
								<html:option value="">---All---</html:option>
								<c:if test="${segmentModelList != null}">
									<html:options items="${segmentModelList}" itemValue="segmentId" itemLabel="name"/>
								</c:if>
							</html:select>
						</td>
				</tr>
				<tr>
					<td class="formText" align="right">
						Created By:
					</td>
					<td align="left">
						<html:select path="createdById" cssClass="textBox" tabindex="6" >
							<html:option value="">---All---</html:option>
							<c:if test="${bankUserAppUserModelList != null}">
								<html:options items="${bankUserAppUserModelList}" itemValue="appUserId" itemLabel="username"/>
							</c:if>
						</html:select>
					</td>
					<td class="formText" align="right">
						Checked By:
					</td>
					<td align="left" >
						<html:select path="checkedById" cssClass="textBox" tabindex="6" >
							<html:option value="">---All---</html:option>
							<c:if test="${bankUserAppUserModelList != null}">
								<html:options items="${bankUserAppUserModelList}" itemValue="appUserId" itemLabel="username"/>
							</c:if>
						</html:select>
					</td>
				</tr>

				<tr>
					<td class="formText" align="right">
						Action Type:
					</td>
					<td align="left">
						<html:select path="usecaseId" cssClass="textBox" tabindex="6" >
							<html:option value="">---All---</html:option>
							<c:if test="${usecaseModelList != null}">
								<html:options items="${usecaseModelList}" itemValue="usecaseId" itemLabel="name"/>
							</c:if>
						</html:select>
					</td>
					<td class="formText" align="right">
						Authorization Status:
					</td>
					<td align="left">
						<html:select path="actionStatusId" cssClass="textBox" tabindex="6" >
							<html:option value="">---All---</html:option>
							<c:if test="${actionStatusModel != null}">
								<html:options items="${actionStatusModel}" itemValue="actionStatusId" itemLabel="name"/>
							</c:if>
						</html:select>
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">
						Created On From Date:
					</td>
					<td align="left">
				        <html:input path="createdOnFromDate" id="createdOnFromDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
							<img id="CreatedFromDate" tabindex="7" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
							<img id="CreatedFromDate" tabindex="8" title="Clear Date" name="popcal" onclick="javascript:$('createdOnFromDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
					<td class="formText" align="right">
						Created On To Date:
					</td>
					<td align="left">
					     <html:input path="createdOnToDate" id="createdOnToDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
					     <img id="CreatedToDate" tabindex="9" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
					     <img id="CreatedToDate" tabindex="10" title="Clear Date" name="popcal" onclick="javascript:$('createdOnToDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">

					</td>
					<td align="left">
						<input name="_search" type="submit" class="button" value="Search" tabindex="12"/>
						<input name="reset" type="reset"
							onclick="javascript: window.location='p_actionauthorizationrequests.html?actionId=${retriveAction}'"
							class="button" value="Cancel" tabindex="13" />
					</td>
					<td class="formText" align="right">

					</td>
					<td align="left">&nbsp;</td>
				</tr>
				<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
			</table></html:form>
		

		<ec:table items="actionAuthorizationModellist" var="actionAuthorizationModel"
		action="${contextPath}/p_actionauthorizationrequests.html"
		title="" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" filterable="false">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="List Authorizations.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="List Authorizations.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da" headerTitle="List Authorizations"
					fileName="List Authorizations.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="List Authorizations.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>

			<ec:row>
				<ec:column property="actionAuthorizationId" title="Action ID" escapeAutoFormat="True" style="text-align: center"/>
				<ec:column property="relationUsecaseIdUsecaseModel.name" title="Action Type" escapeAutoFormat="True" style="text-align: center"/>
				<ec:column property="relationActionStatusIdActionStatusModel.name" title="Authorization Status" escapeAutoFormat="True" style="text-align: center"/>
				<ec:column property="escalationLevel" title="Escalation Level" escapeAutoFormat="True" style="text-align: center"/>
				<ec:column property="relationCreatedByAppUserModel.username" title="Created By" escapeAutoFormat="true"/>
				<ec:column property="createdOn" cell="date" format="dd/MM/yyyy hh:mm a" title="Created On"/>

				<ec:column property="relationCheckedByAppUserModel.username" title="Checked By" escapeAutoFormat="true"/>
				<ec:column property="checkedOn" cell="date" format="dd/MM/yyyy hh:mm a" title="Checked On"/>
				<ec:column property="intimatedTo" title="Intimated To" escapeAutoFormat="true"/>
				<ec:column property="intimatedOn" cell="date" format="dd/MM/yyyy hh:mm a" title="Intimated On"/>
				<ec:column property="relationSegmentIdSegmentModel.name" filterable="false"  title="Segment" />
				<ec:column alias="" title=""  style="text-align: center" sortable="false" viewsAllowed="html">	
					<input type="button" class="button" value="View Detail" name="ActionDetail${actionAuthorizationModel.actionAuthorizationId}" onclick="openActionDetailWindow(${actionAuthorizationModel.actionAuthorizationId},${actionAuthorizationModel.usecaseId})" />
				</ec:column>
	
			</ec:row>
		</ec:table>


		<script language="javascript" type="text/javascript">
			
      		Calendar.setup(
      		{
		       inputField  : "createdOnFromDate", // id of the input field
			   button      : "CreatedFromDate",    // id of the button
		    }
      		);
			Calendar.setup(
		    {
		      inputField  : "createdOnToDate", // id of the input field
		      button      : "CreatedToDate",    // id of the button
		  	  isEndDate: true
		    }
		    );

		
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
				else if (usecaseId ==<%=PortalConstants.UPDATE_ACCOUNT_TO_BLINK_USECASE_ID%>)
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
				else if (usecaseId ==<%=PortalConstants.UPDATE_AGENT_GUOUP_TAGGING_USECASE_ID%>)
					url = 'p-agenttaggingauthorizationdetail.html?actionAuthorizationId='+actionAuthorizationId+'&escalateRequest=true&resolveRequest=false';
				else if (usecaseId ==<%=PortalConstants.CREATE_AGENT_GUOUP_TAGGING_USECASE_ID%>)
					url = 'p-agenttaggingauthorizationdetail.html?actionAuthorizationId='+actionAuthorizationId+'&escalateRequest=true&resolveRequest=false';
				else if (usecaseId ==<%=PortalConstants.TRANS_REDEMPTION_USECASE_ID%>)
					url = 'p-trxredeemauthorizationdetail.html?actionAuthorizationId='+actionAuthorizationId+'&escalateRequest=true&resolveRequest=false';
				else if (usecaseId ==<%=PortalConstants.TRANSACTION_REVERSAL_USECASE_ID%>)
					url = 'p-manualreversalauthorizationdetail.html?actionAuthorizationId='+actionAuthorizationId+'&escalateRequest=true&resolveRequest=false';
				else if (usecaseId ==<%=PortalConstants.BULK_MANUAL_ADJUSTMENT_USECASE_ID%>)
					url = 'p-bulkmanualadjustmentauthorizationdetail.html?actionAuthorizationId='+actionAuthorizationId+'&escalateRequest=true&resolveRequest=false';
				else if (usecaseId ==<%=PortalConstants.UPDATE_BULK_DISBURSEMENT_USECASE_ID%>)
					url = 'p-bulkdisbursementauthorizationdetail.html?actionAuthorizationId='+actionAuthorizationId+'&escalateRequest=true&resolveRequest=false';
				else if (usecaseId ==<%=PortalConstants.KEY_MFS_DEBIT_CARD_UPDATE_USECASE_ID%>)
					url = 'p-updatedebitcardrequestauthorizationdetail.html?actionAuthorizationId='+actionAuthorizationId+'&escalateRequest=true&resolveRequest=false';
				else if (usecaseId ==<%=PortalConstants.UPDATE_CUSTOMER_NAME_USECASE_ID%>)
					url = 'p_updateCustomerNameAauthorization.html?actionAuthorizationId='+actionAuthorizationId+'&escalateRequest=true&resolveRequest=false';
				else if (usecaseId ==<%=PortalConstants.BULK_AUTO_REVERSAL_USECASE_ID%>)
					url = 'p-bulkreversalauthorizationdetail.html?actionAuthorizationId='+actionAuthorizationId+'&escalateRequest=true&resolveRequest=false';
				else
					url = 'p-authorizationdetailform.html?actionAuthorizationId='+actionAuthorizationId+'&escalateRequest=true&resolveRequest=false';

				childWindow=window.open(url,'Action Detail Window', 'width='+popupWidth+',height='+popupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
			    if(window.focus) childWindow.focus();
			    return false;
			}
			
			function validateFormData(form){
				var isValid=validateForm(form);
				if(isValid){
					var _fDate =undefined;
					var _tDate = undefined;
					
					if(form.createdOnFromDate!=undefined)
					{
						_fDate = form.createdOnFromDate.value;
					}
					if(form.createdOnToDate!=undefined)
					{
						_tDate = form.createdOnToDate.value;
					}
					
					var startlbl = "Created On From Date";
					var endlbl = "Created On To Date";

					isValid=	isValidDateRange(_fDate,_tDate,startlbl,endlbl,serverDate);
				}
				return isValid;
			}

      	</script>
      	<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>
      	
	</body>
</html>
