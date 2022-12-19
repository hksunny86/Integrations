<!--Author: omar-->

<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

	<head>
<meta name="decorator" content="decorator">
		<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
		<script src="${contextPath}/scripts/jquery-1.7.2.min.js"></script>
		<script type="text/javascript">
			var jq = $.noConflict();
			jq(document).ready(function($){
				$('#ec_table tbody tr').filter(':odd').addClass('odd');
			    $('#ec_table tbody tr').filter(':even').addClass('even');
			}
		);
		</script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>

		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/prototype.js"></script>

		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<meta name="title" content="Handler Details" />
		<%@include file="/common/ajax.jsp"%>
		<script type="text/javascript">
		function isNumericWithHyphin(code) {
			return ((code >= zeroCharCode && code <= nineCharCode)
			|| 
			code == 46 ||   /*delete */
			code == 8 || 	/*back */
			code == 36 ||	/*home */
			code == 35 || 	/*end */
			code == 37 || 	/* left arrow */
			code == 39 || 	/* right arrow  */
			code == 38 || 	/* up arrow */
			code == 40 ||   /* down arrow */
			code == 9  ||	/* tab */	
			code == 13 ||   /* enter */
			code == 16 ||   /* shift */
			code == 17 ||   /* ctrl */
			code == 18 ||   /* alt */
			code == minusCharCode /* minus character */
			);
			}
		
		function maskCNIC(obj, e) {
			var code = e.charCode ? e.charCode : e.keyCode;
			if (!isNumericWithHyphin(code)||  code == 38 || code == 35 || code == 36 || code == 37  || code == 39 || code == 40) {
				return false;
   			}
   			return true;
		}

		function initProgress()
		{
			if(document.getElementById('ajaxMsgDiv')){
				document.getElementById('ajaxMsgDiv').style.display = "none";
			}
			if (confirm('If information is verified then press OK to continue')==true)
		    {
		    	return true;
		    }
		    else
			{
			  $('errorMsg').innerHTML = "";
			  $('successMsg').innerHTML = "";
			  Element.hide('successMsg');
			  Element.hide('errorMsg'); 
			  return false;
			}
		}
		
	var isErrorOccured = false;
		function resetProgress()
		{
		    if(!isErrorOccured)
		    {
			    // clear error box
			    $('errorMsg').innerHTML = "";
		  		Element.hide('errorMsg');
			    // display success message
			    Element.show('successMsg');
			} 
				isErrorOccured = false;
		}

		function resetProgressDeactivate(){
			window.location.reload();

/*		    if(!isErrorOccured){
			    // clear error box
			    $('errorMsg').innerHTML = "";
		  		Element.hide('errorMsg'); 
			    $('successMsg').innerHTML = $F('message');
			    // display success message
			    Element.show('successMsg');
			    
			    var isDisable = $('btn_blockUnblock').getAttribute('disableAfterClick');
			    if(isDisable=='true'){
			    	document.getElementById('btn_blockUnblock').disabled = true;
			    }
				//Element.hide('btn_blockUnblock');
			} 
			isErrorOccured = false;
*/		}

		function resetAgentWeb(){
			window.location.reload();
			/* if(!isErrorOccured)
		    {
			    // clear error box
			    $('errorMsg').innerHTML = "";
		  		Element.hide('errorMsg');
			    // display success message
			    Element.show('successMsg');
			} 
				isErrorOccured = false; */
		}

		function resetProgressLock(){
			window.location.reload();
/*
		    if(!isErrorOccured){
			    // clear error box
			    $('errorMsg').innerHTML = "";
		  		Element.hide('errorMsg'); 
			    $('successMsg').innerHTML = $F('lock_message');
			    // display success message
			    Element.show('successMsg');
			    
			    var isDisable = $('btn_lockUnlock').getAttribute('disableAfterClick');
			    if(isDisable=='true'){
			    	document.getElementById('btn_lockUnlock').disabled = true;
			    }
				//Element.hide('btn_actDeact');
			} 
			isErrorOccured = false;
*/
		}
	</script>
	<%
		String backButtonPermission = PortalConstants.CSR_GP_READ + "," + PortalConstants.HOME_PAGE_QUICK_SEARCH_AGNT_READ;

		String deactivatePermission = PortalConstants.CSR_GP_UPDATE;
		deactivatePermission +=	"," + PortalConstants.PG_GP_UPDATE;
		deactivatePermission +=	"," + PortalConstants.DEACTIVATE_HNDLR_UPDATE;

		String activatePermission = PortalConstants.MNG_GEN_ACC_UPDATE;
		activatePermission += "," + PortalConstants.PG_GP_UPDATE;
		activatePermission += "," + PortalConstants.REACTIVATE_HNDLR_UPDATE;

		String unlockPermission = PortalConstants.MNG_GEN_ACC_UPDATE;
		unlockPermission +=	"," + PortalConstants.CSR_GP_UPDATE;
		unlockPermission +=	"," + PortalConstants.PG_GP_UPDATE;
		unlockPermission +=	"," + PortalConstants.UNBLOCK_HNDLR_UPDATE;

		String lockPermission = PortalConstants.MNG_GEN_ACC_UPDATE;
		lockPermission +=	"," + PortalConstants.PG_GP_UPDATE;
		lockPermission +=	"," + PortalConstants.BLOCK_HNDLR_UPDATE;

		String updatePINPermission = PortalConstants.FRGT_BNK_PIN_UPDATE;
		updatePINPermission +=	"," + PortalConstants.CSR_GP_UPDATE;
		updatePINPermission +=	"," + PortalConstants.PG_GP_UPDATE;
		updatePINPermission +=	"," + PortalConstants.REGENERATE_HNDLR_PIN_UPDATE;
				  
		String updateLoginPINPermission = PortalConstants.REGENERATE_HNDLR_LOGIN_PIN_UPDATE;
		updateLoginPINPermission +=	"," + PortalConstants.CSR_GP_UPDATE;
		updateLoginPINPermission +=	"," + PortalConstants.PG_GP_UPDATE;		  

		String resetPasswordAgentWeb = PortalConstants.CSR_GP_UPDATE;
		resetPasswordAgentWeb +=	"," + PortalConstants.PG_GP_UPDATE;
		resetPasswordAgentWeb +=	"," + PortalConstants.RESET_AGNT_PASS_UPDATE;

		String complaintPermission = PortalConstants.CSR_GP_CREATE + "," + PortalConstants.ADD_COMP_CREATE;

		String agentTxHistoryPermission = PortalConstants.HNDLR_TX_HIST_READ;
		agentTxHistoryPermission += "," + PortalConstants.ADMIN_GP_READ;
		agentTxHistoryPermission += "," + PortalConstants.PG_GP_READ;
		agentTxHistoryPermission += "," + PortalConstants.CSR_GP_READ;
		agentTxHistoryPermission += "," + PortalConstants.MNO_GP_READ;

		String closeAccountPermission =	PortalConstants.MNG_HNDLR_ACC_CLOSURE_UPDATE;
		closeAccountPermission +=	"," + PortalConstants.PG_GP_UPDATE;	
		closeAccountPermission +=	"," + PortalConstants.ADMIN_GP_READ;	
		
		String blacklistMarkingPermission =	PortalConstants.MNG_BLKLST_MRKNG_UPDATE;
		
		
		String isAgentWebEnablePermission = PortalConstants.ADMIN_GP_READ;
		isAgentWebEnablePermission += "," + PortalConstants.CSR_GP_READ;
		isAgentWebEnablePermission += "," + PortalConstants.PG_GP_READ;
		isAgentWebEnablePermission += "," + PortalConstants.MNG_IS_AGENT_WEB_ENABLE_UPDATE;
		
		String referrer = request.getParameter("referrer");
		if(null==referrer)
			referrer = request.getHeader("Referer");	
	 %>

	</head>

	<body bgcolor="#ffffff">

		<div id="successMsg" class ="infoMsg" style="display:none;"></div>
		<div id="errorMsg" class ="errorMsg" style="display:none;"></div>
		<c:if test="${not empty sessionScope.ajaxMessageToDisplay}" >
			<div id="ajaxMsgDiv" class ="infoMsg">
				<c:out value="${sessionScope.ajaxMessageToDisplay}" /><c:remove var="ajaxMessageToDisplay" scope="session" />
			</div>
		</c:if>
		
		<spring:bind path="retailerContactListViewFormModel.*">
			<c:if test="${not empty status.errorMessages}">
				<div class="errorMsg">
					<c:forEach var="error" items="${status.errorMessages}">
						<c:out value="${error}" escapeXml="false" />
						<br />
					</c:forEach>
				</div>
			</c:if>
		</spring:bind>

		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" />
					<br />
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>

		<table width="100%" border="0" cellpadding="0" cellspacing="1">
			<form name="RetailerContactForm" method="post" commandName="retailerContactListViewFormModel"
				action="searchHandler.jsf">

				<c:if test="${not empty param.appUserId}">
					<input type="hidden" name="isUpdate" id="isUpdate" value="true" />
				</c:if>

				<input type="hidden" name="appUserId" value="${param.appUserId}">

					<table width="100%" border="0" cellpadding="0" cellspacing="1">
						<tr height="25">
							<td height="25" align="right" width="20%" bgcolor="F3F3F3" class="formText">Handler ID:&nbsp;</td>
							<td height="25" align="left" bgcolor="FBFBFB">${mfsId}</td>
						</tr>
						<tr height="25">
							<td height="25" align="right" bgcolor="F3F3F3" class="formText">Name:&nbsp;</td>
							<td height="25" align="left" bgcolor="FBFBFB">${retailerContactListViewFormModel.firstName}&nbsp;${retailerContactListViewFormModel.lastName}</td>
						</tr>
						<tr height="25">
							<td align="right" bgcolor="F3F3F3" class="formText">CNIC:&nbsp;</td>
							<td align="left" bgcolor="FBFBFB">${retailerContactListViewFormModel.cnicNo}</td>
						</tr>
						<tr height="25">
							<td align="right" bgcolor="F3F3F3" class="formText">Mobile No:&nbsp;</td>
							<td bgcolor="FBFBFB">${retailerContactListViewFormModel.mobileNumber}</td>
						</tr>
						<tr height="25">
							<td align="right" bgcolor="F3F3F3" class="formText">Status:&nbsp;</td>
							<td bgcolor="FBFBFB">
								<c:choose>
									<c:when test="${retailerContactListViewFormModel.accountClosedUnsettled and not retailerContactListViewFormModel.accountClosedSettled}">Closed Unsettled</c:when>
									<c:when test="${retailerContactListViewFormModel.accountClosedUnsettled and retailerContactListViewFormModel.accountClosedSettled}">Closed Settled</c:when>									
									<c:when test="${deviceAccLocked && not retailerContactListViewFormModel.accountClosedUnsettled}">Blocked</c:when>
									<c:when test="${not deviceAccEnabled && not retailerContactListViewFormModel.accountClosedUnsettled}">Deactivated</c:when>
									<c:when test="${credentialsExpired && not retailerContactListViewFormModel.accountClosedUnsettled}">Credentials Expired</c:when>
									<c:when test="${not deviceAccLocked && deviceAccEnabled && not credentialsExpired && not retailerContactListViewFormModel.accountClosedUnsettled}">Active</c:when>
									<c:otherwise>Unknown</c:otherwise>
								</c:choose>
								${statusDetails}
							</td>
						</tr>
						<c:if test="${not empty appUserHistoryViewModelList}">
						<tr>
							<td width="100%" align="left" colspan="2">
								<div class="eXtremeTable">
									<table id="ec_table" class="tableRegion" width="100%" cellspacing="0" cellpadding="0" border="0">
										<thead>
											<tr>
												<td class="tableHeader">
													Account Closing Date
												</td>
												<td class="tableHeader">
													Closed By
												</td>
												<td class="tableHeader">
													Account Closing Comments
												</td>
												<td class="tableHeader">
													Account Settlement Date
												</td>
												<td class="tableHeader">
													Settled By
												</td>
												<td class="tableHeader">
													Account Settlement Comments
												</td>
											</tr>
										</thead>
										<tbody class="tableBody">
											<c:forEach items="${appUserHistoryViewModelList}" var="appUserHistoryViewModel" varStatus="iterationStatus">
												<tr>
													<td align="center">
														<fmt:formatDate value="${appUserHistoryViewModel.closedOn}" pattern="dd/MM/yyyy"/>
													</td>
													<td>
														${appUserHistoryViewModel.closedByUsername}
													</td>
													<td>
														${appUserHistoryViewModel.closingComments}
													</td>
													<td align="center">
														<fmt:formatDate value="${appUserHistoryViewModel.settledOn}" pattern="dd/MM/yyyy"/>
													</td>
													<td>
														${appUserHistoryViewModel.settledByUsername}
													</td>
													<td>
														${appUserHistoryViewModel.settlementComments}
													</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</div>
							</td>
						</tr>
						</c:if>
						<tr height="25">
							<td width="100%" colspan="2">
						

					 <c:set var="encryptAppUserId"><security:encrypt strToEncrypt="${appUserId}"/></c:set>
			         <c:set var="disableLockBtnAfterClick" value="true"/>
			         <c:set var="disableDeactivateBtnAfterClick" value="true"/>
					<c:set var="encryptSMAccountId"><security:encrypt strToEncrypt="${requestScope.smAccountId}"/></c:set>
						
			
			<input type="hidden" id="notifyBySMS${encryptAppUserId}" value="true"/>	
			<input type="hidden" id="usecaseId" value="<%=PortalConstants.REACTIVATE_HANDLER_USECASE_ID%>"/>	
			<input type="hidden" id="usecaseIdLock" value="<%=PortalConstants.UNBLOCK_HANDLER_USECASE_ID%>"/>	
			<input type="hidden" id="actionID" value="<%=PortalConstants.ACTION_UPDATE%>"/>	
			<input type="hidden" id="appUsrId_${encryptAppUserId}" value="${encryptAppUserId}"/>
			<input type="hidden" id="message" value=""/>
			<input type="hidden" id="referrer" value="${referrer}"/>					
			<input type="hidden" id="appUser${encryptAppUserId}" value="${encryptAppUserId}" name="appUser${encryptAppUserId}" />
			<input type="hidden" id="smAcId${encryptSMAccountId}" value="${encryptSMAccountId}" name="smAcId${encryptSMAccountId}" />
			<input type="hidden" id="usecaseId${encryptAppUserId}" value="<%=PortalConstants.FORGOT_VERIFLY_PIN_USECASE_ID%>"/>	
			<input type="hidden" id="usecaseIdWeb" value="<%=PortalConstants.RESET_PASSWORD_WEB_USECASE_ID%>"/>	
			<input type="hidden" id="usecaseIdBlacklisted" value="<%=PortalConstants.MARK_BLACKLISTED_USECASE_ID%>"/>					
						
			
			<c:choose>
				<c:when test="${not retailerContactListViewFormModel.accountClosedUnsettled and not retailerContactListViewFormModel.accountClosedSettled }">

					 <authz:authorize ifAnyGranted="<%=lockPermission%>">
					 	<authz:authorize ifAnyGranted="<%=unlockPermission%>">
					 		<c:set var="disableLockBtnAfterClick" value="false"/>
					 	</authz:authorize>
					 </authz:authorize>	 	 
					 <authz:authorize ifAnyGranted="<%=deactivatePermission%>">
					 	<authz:authorize ifAnyGranted="<%=activatePermission%>">
					 		<c:set var="disableDeactivateBtnAfterClick" value="false"/>
					 	</authz:authorize>
					 </authz:authorize>	 	 
					<authz:authorize ifAnyGranted="<%=blacklistMarkingPermission%>">
						<c:set var="disableBlacklistBtnAfterClick" value="false"/>
					</authz:authorize>
					<authz:authorize ifAnyGranted="<%=isAgentWebEnablePermission%>">
						<c:set var="disableAgentWebBtnAfterClick" value="false"/>
					 </authz:authorize>
					 
					 <c:choose>
					 	<c:when test="${not deviceAccLocked}">
					 	
						 	<authz:authorize ifAnyGranted="<%=lockPermission%>">
								<input id="btn_lockUnlock" type="button" class="button" value="   Block   " onclick="openlockunlockWindow('${encryptAppUserId}',usecaseIdLock.value,actionID.value,'${mfsId}','true')"/>							
				            </authz:authorize>
				            <authz:authorize ifNotGranted="<%=lockPermission%>">
								<input id="btn_lockUnlock" type="button" class="button" value="    Block    " disabled="disabled" />
				            </authz:authorize>
					 	</c:when>
					 	<c:otherwise>
						 	<authz:authorize ifAnyGranted="<%=unlockPermission%>">
								<input id="btn_lockUnlock" type="button" class="button" value="  Unblock  " disableAfterClick="${disableLockBtnAfterClick}" onclick="openlockunlockWindow('${encryptAppUserId}',usecaseIdLock.value,actionID.value,'${mfsId}','true')"/>
				            </authz:authorize>
				            <authz:authorize ifNotGranted="<%=unlockPermission%>">
								<input id="btn_lockUnlock" type="button" class="button" value="  Unblock  " disabled="disabled" />
				            </authz:authorize>
					 	</c:otherwise>
					 </c:choose>	
					 
					 <c:choose>
					 	<c:when test="${deviceAccEnabled}">
						 	 <authz:authorize ifAnyGranted="<%=deactivatePermission%>">
						 	 <c:if test="${not deviceAccLocked}">	
								<input id="btn_blockUnblock" type="button" class="button" value="Deactivate" disableAfterClick="${disableDeactivateBtnAfterClick}" onclick="openlockunlockWindow('${encryptAppUserId}',usecaseId.value,actionID.value,'${mfsId}','false')"/>
							</c:if>	
							<c:if test="${deviceAccLocked}">	
								<input id="btn_blockUnblock" type="button" class="button" value="Deactivate" disabled="disabled"/>
							</c:if>
				            </authz:authorize>
				            <authz:authorize ifNotGranted="<%=deactivatePermission%>">	            
								<input id="btn_blockUnblock" type="button" class="button" value="Deactivate" disabled="disabled" />
				            </authz:authorize>
					 	</c:when>
					 	<c:otherwise>
						 	 <authz:authorize ifAnyGranted="<%=activatePermission%>">
						 	 <c:if test="${not deviceAccLocked}">
								<input id="btn_blockUnblock" type="button" class="button" value="Reactivate" disableAfterClick="${disableDeactivateBtnAfterClick}" onclick="openlockunlockWindow('${encryptAppUserId}',usecaseId.value,actionID.value,'${mfsId}','false')"/>
								</c:if>
								<c:if test="${deviceAccLocked}">
								<input id="btn_blockUnblock" type="button" class="button" value="Reactivate"  disabled="disabled"/>
								</c:if>
				            </authz:authorize>
				            <authz:authorize ifNotGranted="<%=activatePermission%>">
								<input id="btn_blockUnblock" type="button" class="button" value="Reactivate" disabled="disabled" />
				            </authz:authorize>				 	
					 	</c:otherwise>	 	
						</c:choose>	


					<%--Blacklisting By Waqar--%>
					<c:choose>
						<c:when test="${blacklisted}">
							<authz:authorize ifAnyGranted="<%=blacklistMarkingPermission%>">
								<input id="btn_blacklisted" type="button" class="button" value="Unmark Blacklisted" disableAfterClick="${disableBlacklistBtnAfterClick}" onclick="openmarkunmarkBlacklistWindow('${encryptAppUserId}',usecaseIdBlacklisted.value,actionID.value,'${mfsId}','${blacklisted}')"/>&nbsp;
							</authz:authorize>
							<authz:authorize ifNotGranted="<%=blacklistMarkingPermission%>">
								<input id="btn_blacklisted" type="button" class="button" value="Unmark Blacklisted" disabled="disabled" />
							</authz:authorize>
						</c:when>
						<c:otherwise>
							<authz:authorize ifAnyGranted="<%=blacklistMarkingPermission%>">
								<input id="btn_blacklisted" type="button" class="button" value="Mark Blacklisted" disableAfterClick="${disableBlacklistBtnAfterClick}" onclick="openmarkunmarkBlacklistWindow('${encryptAppUserId}',usecaseIdBlacklisted.value,actionID.value,'${mfsId}','${blacklisted}')"/>
							</authz:authorize>
							<authz:authorize ifNotGranted="<%=blacklistMarkingPermission%>">
								<input id="btn_blacklisted" type="button" class="button" value="Mark Blacklisted" disabled="disabled" />
							</authz:authorize>
						</c:otherwise>
					</c:choose>

					<authz:authorize ifAnyGranted="<%=PortalConstants.UPDATE_HNDLR_MOBILE%>">
						<c:if test="${not deviceAccLocked and deviceAccEnabled and not credentialsExpired}">
							<input type="button" class="button" value="Update Mobile No."  onclick="javascript:document.location='p_mobilehistory.html?appUserId=${encryptAppUserId}&mfsId=${mfsId}&handlerId=${param.handlerId}&mobileNo=${retailerContactListViewFormModel.mobileNumber}';"  />
						</c:if>
						<c:if test="${deviceAccLocked or not deviceAccEnabled or credentialsExpired}">
							<input type="button" class="button" value="Update Mobile No." disabled="disabled" />
						</c:if>
					</authz:authorize>
					<authz:authorize ifAnyGranted="<%=PortalConstants.UPDATE_HNDLR_CNIC%>">
						<c:if test="${not deviceAccLocked and deviceAccEnabled and not credentialsExpired}">
							<input type="button" class="button" value="Update CNIC."  onclick="javascript:document.location='p_cnichistory.html?appUserId=${encryptAppUserId}&mfsId=${mfsId}&handlerId=${param.handlerId}&mobileNo=${retailerContactListViewFormModel.mobileNumber}&nic=${retailerContactListViewFormModel.cnicNo}';"  />
						</c:if>
						<c:if test="${deviceAccLocked or not deviceAccEnabled or credentialsExpired}">
							<input type="button" class="button" value="Update CNIC." disabled="disabled" />
						</c:if>
					</authz:authorize>
					<c:if test="${appUserTypeId == 6}">	
						<authz:authorize ifAnyGranted="<%=closeAccountPermission%>">
							<input id="btn_closeAccount" type="button" class="button" value="Close Account" onclick="openAccountClosingWindow('${requestScope.appUserId}','${mfsId}')"/>
						</authz:authorize>
						<authz:authorize ifNotGranted="<%=closeAccountPermission%>">
							<input id="btn_settlement" type="button" class="button" value="Close Account" disabled="disabled"/> 
						</authz:authorize>
					</c:if>					
				
						<authz:authorize ifAnyGranted="<%=updatePINPermission%>">
							<c:choose>
								   <c:when test="${(not empty smAccountId) && deviceAccEnabled && (not deviceAccLocked)}">
				    			        <input type="button"  class="button" value="Regenerate MPIN" id="btnSmAcId${encryptSMAccountId}"/>
								   </c:when>
								   <c:otherwise>
								       <input type="button"  class="button" value="Regenerate MPIN" id="btnSmAcId${encryptSMAccountId}" disabled="disabled"/>   
								   </c:otherwise>
							</c:choose>


						<ajax:htmlContent baseUrl="${contextPath}/p_forgotveriflypinChange.html" 
							eventType="click" 
							source="btnSmAcId${encryptSMAccountId}" 
							target="successMsg" 
							parameters="appUserId={appUser${encryptAppUserId}},mfsId=${mfsId},smAcId={smAcId${encryptSMAccountId}},usecaseId={usecaseId${encryptAppUserId}},actionId={actionID},handlerMobileNo=${retailerContactListViewFormModel.mobileNumber}"
							errorFunction="globalAjaxErrorFunction"
							preFunction="initProgress"
							postFunction="resetProgress"
							/>
			            
						</authz:authorize>
						<authz:authorize ifAnyGranted="<%=updateLoginPINPermission%>">
							<c:choose>
								   <c:when test="${deviceAccEnabled && (not deviceAccLocked)}">
				    			        <input type="button"  class="button" value="Regenerate Login PIN" id="btnRegPin${encryptAppUserId}"/>
								   </c:when>
								   <c:otherwise>
								       <input type="button"  class="button" value="Regenerate Login PIN" id="btnRegPin${encryptAppUserId}" disabled="disabled"/>   
								   </c:otherwise>
							</c:choose>	
							<input type="hidden" value="${encryptAppUserId}" name="appUser${encryptAppUserId}" id="appUser${encryptAppUserId}" />
							<ajax:htmlContent baseUrl="${contextPath}/changeuserdevicepin.html" 
								eventType="click" 
								source="btnRegPin${encryptAppUserId}" 
								target="successMsg" 
								parameters="appUserId={appUser${encryptAppUserId}},mfsId=${mfsId}"
								errorFunction="globalAjaxErrorFunction"
								preFunction="initProgress"
								postFunction="resetProgress"
								/> 
						</authz:authorize>	
						
				         <authz:authorize ifAnyGranted="<%=resetPasswordAgentWeb%>">
								<input type="button" id="rstPass${encryptAppUserId}" name="rstPass${encryptAppUserId}" value="Reset Portal Password" class="button" onclick="openResetPasswordWindow('${encryptAppUserId}','${mfsId}','${username}')"  />
	
							<c:if test="${headRetailer == 'true'}">

								<input type="hidden" id="usecaseIdWeb" value="<%=PortalConstants.RESET_PASSWORD_WEB_USECASE_ID%>"/>	
								<ajax:htmlContent baseUrl="${contextPath}/p_pgforgotpasswordupdate.html" 
									eventType="click" 
									source="chgPin${encryptAppUserId}" 
									target="successMsg" 
									parameters="appUserId={appUser${encryptAppUserId}},usecaseId={usecaseIdWeb},actionId={actionID}"
									errorFunction="globalAjaxErrorFunction"
									preFunction="initProgress"
									postFunction="resetProgress"
									/>
							</c:if>										  
							</authz:authorize> 
							<authz:authorize ifAnyGranted="<%=agentTxHistoryPermission%>"> 
								<input type="button" class="button" value="View Transaction History"  tabindex="47" onclick="javascript:document.location='p_handlerTransactionHistory.html?handlerId=${mfsId}&hId=${handlerId}'"/>&nbsp;	
							</authz:authorize>
							</br>
						 	<authz:authorize ifAnyGranted="<%=complaintPermission%>">
								<input type="button" class="button" value="Add Complaint" tabindex="48" onclick="javascript:document.location='p_complaintform.html?appUserId=${encryptAppUserId}&actionId=1&usrType=12';"/>&nbsp;
							</authz:authorize>	
				</c:when>	            				            
<%-- 				<c:when test="${retailerContactListViewFormModel.accountClosedUnsettled and not retailerContactListViewFormModel.accountClosedSettled}">
					<c:if test="${appUserTypeId == 6}">	
					<authz:authorize ifAnyGranted="<%=closeAccountPermission%>">
						<input id="btn_settlement" type="button" class="button" value="Settle Account" onclick="openAccountClosingWindow('${appUserId}','${mfsId}')"  /> 
					</authz:authorize>
					<authz:authorize ifNotGranted="<%=closeAccountPermission%>">
						<input id="btn_settlement" type="button" class="button" value="Settle Account" disabled="disabled"/> 
					</authz:authorize>
					</c:if>
				</c:when> --%>
			</c:choose>
			
			
			
			<%--AgentWeb Enable/Disable By Waqar--%>
				<authz:authorize ifAnyGranted="<%=isAgentWebEnablePermission%>">
					<c:choose>
						   <c:when test="${isEnabledAgentWeb}">
						   		
		    			        <input type="button"  class="button" value="Disable Agent Web Portal" id="btn_agentWeb${isEnabledAgentWeb}"/>
						   </c:when>
						   <c:otherwise>
						       
						       <input type="button"  class="button" value="Enable Agent Web Portal" id="btn_agentWeb${isEnabledAgentWeb}" />   
						   </c:otherwise>
					</c:choose>	
					<input type="hidden" value="${encryptAppUserId}" name="appUser${encryptAppUserId}" id="appUser${encryptAppUserId}" />
					<ajax:htmlContent baseUrl="${contextPath}/enableDisableAgentWebPortal.html" 
						eventType="click" 
						source="btn_agentWeb${isEnabledAgentWeb}" 
						target="successMsg" 
						parameters="appUserId={appUser${encryptAppUserId}},mfsId=${mfsId},isEnabledAgentWeb=${isEnabledAgentWeb},handlerId=${param.handlerId}"
						errorFunction="globalAjaxErrorFunction"
						preFunction="initProgress"
						postFunction="resetAgentWeb"
						/> 
				</authz:authorize>
			
			
			
				<input type="button" class="button" value="Back" tabindex="49" onclick="javascript:document.location='<%=referrer%>';" />
							</td>
						</tr>
					</table>
			</form>
		</table>
<script type="text/javascript">
	var childWindow;
	
	function openResetPasswordWindow(encryptAppUserId,mfsId,username)
	{
		var popupWidth = 550;
		var popupHeight = 350;
		var usecaseId = <%=PortalConstants.RESET_AGENT_PASSWORD_PORTAL_USECASE_ID%>;	       
		var popupLeft = (window.screen.width - popupWidth)/2;
		var popupTop = (window.screen.height - popupHeight)/2;		
		var url = 'p-resetportalpasswordform.html?appUserId='+encryptAppUserId+'&username='+username+'&mfsId='+mfsId+'&usecaseId='+usecaseId+'&isAgent=false&isHandler=true&notifyBySMS=true';
        newWindow=window.open(url,'Action Detail Window', 'width='+popupWidth+',height='+popupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
	    if(window.focus) newWindow.focus();
	    return false;		
	}
	function openlockunlockWindow(encryptAppUserId,usecaseId,actionId,mfsId,isLockUnlock)
	{
		var popupWidth = 550;
		var popupHeight = 350;	       
		var popupLeft = (window.screen.width - popupWidth)/2;
		var popupTop = (window.screen.height - popupHeight)/2;
		
		var btnLabel;
		if(usecaseId==<%=PortalConstants.REACTIVATE_HANDLER_USECASE_ID%>){
			btnLabel= document.getElementById('btn_blockUnblock').value.trim();
			
			if(btnLabel=="Deactivate")
				usecaseId=<%=PortalConstants.DEACTIVATE_HANDLER_USECASE_ID%>;	
		}
		else if(usecaseId==<%=PortalConstants.UNBLOCK_HANDLER_USECASE_ID%>){
			btnLabel= document.getElementById('btn_lockUnlock').value.trim();
			if(btnLabel=="Block")
				usecaseId=<%=PortalConstants.BLOCK_HANDLER_USECASE_ID%>;
		}
				
		var url = 'p-lockunlockaccountform.html?appUserId='+encryptAppUserId+'&usecaseId='+usecaseId+'&mfsId='+mfsId+'&actionId='+actionId+'&isLockUnlock='+isLockUnlock+'&isAgent=false'+'&isHandler=true';
        newWindow=window.open(url,'Action Detail Window', 'width='+popupWidth+',height='+popupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
	    if(window.focus) newWindow.focus();
	    return false;		
	}
	
	//Mark blacklisted script
	function openmarkunmarkBlacklistWindow(encryptAppUserId,usecaseId,actionId,mfsId,blacklisted)
	{
		var popupWidth = 550;
		var popupHeight = 350;
		var popupLeft = (window.screen.width - popupWidth)/2;
		var popupTop = (window.screen.height - popupHeight)/2;

		var btnLabel= document.getElementById('btn_blacklisted').value.trim();
		if(blacklisted=="true"){
			usecaseId=<%=PortalConstants.UNMARK_BLACKLISTED_USECASE_ID%>;
			blacklisted=false;
		}
		else{
			usecaseId=<%=PortalConstants.MARK_BLACKLISTED_USECASE_ID%>;
			blacklisted=true;
		};

		var url = 'p-markunmarkblacklisted.html?appUserId='+encryptAppUserId+'&mfsId='+mfsId+'&actionId='+actionId+'&blacklisted='+blacklisted+'&isHandler=true'+'&usecaseId='+usecaseId;
		newWindow=window.open(url,'Action Detail Window', 'width='+popupWidth+',height='+popupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
		if(window.focus) newWindow.focus();
		return false;
	}
	
	function openAccountClosingWindow(appUserId,retailerId)
	{
		var popupWidth = 550;
		var popupHeight = 350;
		var popupLeft = (window.screen.width - popupWidth)/2;
		var popupTop = (window.screen.height - popupHeight)/2;
		childWindow = window.open('p-handleraccountclosingform.html?retailerId='+retailerId+'&appUserId='+appUserId+'&appUserTypeId=12','_blank','width='+popupWidth+',height='+popupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+',directories=no,scrollbars=no,resizable=no,status=no');
	
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
</script>
	</body>

</html>
