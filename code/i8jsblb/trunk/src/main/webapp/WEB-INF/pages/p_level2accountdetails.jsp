<!--Author: omar-->

<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta name="decorator" content="decorator">

<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/scripts/colorbox.css" />
<script src="${pageContext.request.contextPath}/scripts/jquery-1.7.2.min.js"></script>
<script src="${pageContext.request.contextPath}/scripts/jquery.colorbox.js"></script>
<script>
			var jq = $.noConflict();
			jq(document).ready(function($){
				//Examples of how to assign the Colorbox event to elements
				jq(".group1").colorbox({rel:'group1'});
				
				jq(".group1").colorbox({
					inline:true,
	                innerWidth:600,
	                height:300,
		            onLoad: function() {
		                    // Remove print if it is re-opened after ajax
		                    $("#cboxPrint").remove();
		            },
		    });
				
			    jq(".group1").colorbox({
			    	iframe:true,
		            innerWidth:800,
		            height:"95%",
		            onComplete: function() {
		                    jq("#cboxContent iframe").ready(function(){
		                            // Add print box once content loaded
		                            jq("#cboxCurrent").append($('<img style="cursor: pointer;" title="Print" id="cboxPrint" src="${contextPath}/images/ah_images/print.png" />'));
		                            jq("#cboxPrint").click( function() {
		                                    printprepare();
		                            });
		                    });
		            },
		    });

			    $('#ec_table tbody tr').filter(':odd').addClass('odd');
			    $('#ec_table tbody tr').filter(':even').addClass('even');
			});
			
			var doAjaxBeforeUnloadEnabled = true; // We hook into window.onbeforeunload and bind some jQuery events to confirmBeforeUnload.  This variable is used to prevent us from showing both messages during a single event.


			var doAjaxBeforeUnload = function (evt) {
				
			    if (!doAjaxBeforeUnloadEnabled) {
			    
			        return;
			    }
			    
			    doAjaxBeforeUnloadEnabled = false;
			    jq.ajax({
			        url: "${contextPath}/p_mnomfsaccountdetailsajx.html?appUserId=${level2AccountModel.appUserId}",
			        success: function (a) {
			            console.debug("Ajax call finished");
			        },
			        async: false /* Not recommended.  This is the dangerous part.  Your mileage may vary. */
			    });
			}
			
			window.onbeforeunload = doAjaxBeforeUnload;
		    $(window).unload(doAjaxBeforeUnload);
			
			function printprepare(){
			    try{
			            var print_frame = document.getElementById('ifrmPrint');
			            var print_head = jq("#cboxContent iframe").contents().find("head").html();
			            var print_body = jq("#cboxContent iframe").contents().find("body").html()
			            var print_doc = (print_frame.contentWindow || print_frame.contentDocument);
			            if (print_doc.document) print_doc = print_doc.document;
			            // Write printable content
			            print_doc.write("<html><head>");
			            print_doc.write(print_head);
			            print_doc.write('</head><body onload="this.focus(); this.print();">');
			            print_doc.write(print_body);
			            print_doc.write("</body></html>");
			            print_doc.close();
			    }
			    catch(e){
			    	alert("printing in error state");
			            self.print();
			    }
			}
			
			function img_find() {
			    var imgs = document.getElementsByTagName("img");
			    var imgSrcs = [];

			    for (var i = 0; i < imgs.length; i++) {
			        imgSrcs.push(imgs[i].src);
			    }

			    return imgSrcs;
			}
			
			function print_imgs(){
				var imgSrcs = img_find();
				try{
					 var print_frame = document.getElementById('ifrmPrint');
			         var print_doc = (print_frame.contentWindow || print_frame.contentDocument);
			         if (print_doc.document) print_doc = print_doc.document;
		            // Write printable content
		            print_doc.write("<html><head>");
		            print_doc.write("<title>printing all images</title>");
		            print_doc.write('</head><body onload="this.focus(); this.print();">');
		            for(var i=0; i< imgSrcs.length; i++){
		            	print_doc.write("<img src='" + imgSrcs[i] + "' /><br>");
		            }
		            print_doc.write("</body></html>");
		            print_doc.close();
		    }
		    catch(e){
		    		alert("printing in error state");
		            self.print();
		    }
			}
		</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/prototype.js"></script>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<meta name="title" content="Customer Details" />
		<%@include file="/common/ajax.jsp"%>
	<%
		String backButtonPermission = PortalConstants.CSR_GP_READ + "," + PortalConstants.HOME_PAGE_QUICK_SEARCH_CUST_READ;

		String editButtonsPermission = PortalConstants.MNG_GEN_ACC_UPDATE;
		editButtonsPermission += "," + PortalConstants.PG_GP_UPDATE;
		editButtonsPermission += "," + PortalConstants.DEACTIVATE_BB_CUST_UPDATE;
		editButtonsPermission += "," + PortalConstants.REACTIVATE_BB_CUST_UPDATE;
		editButtonsPermission += "," + PortalConstants.BLOCK_BB_CUST_UPDATE;
		editButtonsPermission += "," + PortalConstants.UNBLOCK_BB_CUST_UPDATE;
		editButtonsPermission += "," + PortalConstants.REGENERATE_BB_CUST_PIN_UPDATE;
		editButtonsPermission += "," + PortalConstants.CSR_GP_UPDATE;

		String deactivatePermission = PortalConstants.MNG_GEN_ACC_UPDATE;
		deactivatePermission +=	"," + PortalConstants.CSR_GP_UPDATE;
		deactivatePermission +=	"," + PortalConstants.PG_GP_UPDATE;
		deactivatePermission +=	"," + PortalConstants.DEACTIVATE_BB_CUST_UPDATE;

		String activatePermission = PortalConstants.MNG_GEN_ACC_UPDATE;
		activatePermission +=	"," + PortalConstants.PG_GP_UPDATE;
		activatePermission +=	"," + PortalConstants.REACTIVATE_BB_CUST_UPDATE;

		String unlockPermission = PortalConstants.MNG_GEN_ACC_UPDATE;
		unlockPermission +=	"," + PortalConstants.CSR_GP_UPDATE;
		unlockPermission +=	"," + PortalConstants.PG_GP_UPDATE;
		unlockPermission +=	"," + PortalConstants.UNBLOCK_BB_CUST_UPDATE;

		String lockPermission = PortalConstants.MNG_GEN_ACC_UPDATE;
		lockPermission +=	"," + PortalConstants.PG_GP_UPDATE;
		lockPermission += "," + PortalConstants.BLOCK_BB_CUST_UPDATE;
		
		String updatePINPermission = PortalConstants.FRGT_BNK_PIN_UPDATE;
		updatePINPermission +=	"," + PortalConstants.CHNG_BNK_PIN_U;
		updatePINPermission +=	"," + PortalConstants.CSR_GP_UPDATE;
		updatePINPermission +=	"," + PortalConstants.PG_GP_UPDATE;

		String regeneratePINPermission = PortalConstants.REGENERATE_BB_CUST_PIN_UPDATE;

		String bbStmtPermission = PortalConstants.ADMIN_GP_READ;
		bbStmtPermission += "," + PortalConstants.CSR_GP_READ;
		bbStmtPermission += "," + PortalConstants.PG_GP_READ;
		bbStmtPermission += "," + PortalConstants.CUST_BB_STMT_READ;

		String txHistPermission = PortalConstants.ADMIN_GP_READ;
		txHistPermission += "," + PortalConstants.CSR_GP_READ;
		txHistPermission += "," + PortalConstants.PG_GP_READ;
		txHistPermission += "," + PortalConstants.CUST_TX_HIST_READ;

		String cashPaymentPermission = PortalConstants.CSR_GP_READ;
		cashPaymentPermission += "," + PortalConstants.PG_GP_READ;
		cashPaymentPermission += "," + PortalConstants.CUST_CP_REQ_READ;
		
		String complaintPermission = PortalConstants.CSR_GP_CREATE + "," + PortalConstants.ADD_COMP_CREATE;
		String accountClosingPermission = PortalConstants.MNG_CUST_ACC_CLOSURE_UPDATE;

		String closeAccountPermission =	PortalConstants.MNG_CUST_ACC_CLOSURE_UPDATE;
		closeAccountPermission +=	"," + PortalConstants.PG_GP_UPDATE;	
		closeAccountPermission +=	"," + PortalConstants.ADMIN_GP_READ;
		
		String referrer = request.getParameter("referrer");
		if(null==referrer)
			referrer = request.getHeader("Referer");
	 %>
	 <script type="text/javascript">
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
			if (confirm('If customer information is verified then press OK to continue')==true)
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
		}
		function resetProgressLock(){
			window.location.reload();
		}
		var childWindow;
			function openAccountClosingWindow(appUserId,customerId)
			{
				var popupWidth = 550;
				var popupHeight = 350;
				var popupLeft = (window.screen.width - popupWidth)/2;
				var popupTop = (window.screen.height - popupHeight)/2;
				childWindow = window.open('p-accountclosingupdateform.html?appUserId='+appUserId+'&customerId='+customerId+'&appUserTypeID=2','_blank','width='+popupWidth+',height='+popupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+',directories=no,scrollbars=no,resizable=no,status=no');
			
			}
			
			function openlockunlockWindow(encryptAppUserId,usecaseId,actionId,mfsId,isLockUnlock)
			{
				var popupWidth = 550;
				var popupHeight = 350;	       
				var popupLeft = (window.screen.width - popupWidth)/2;
				var popupTop = (window.screen.height - popupHeight)/2;
				
				var btnLabel;
				if(usecaseId==<%=PortalConstants.REACTIVATE_CUSTOMER_USECASE_ID%>){
					btnLabel= document.getElementById('btn_actDeact').value.trim();
					
					if(btnLabel=="Deactivate")
						usecaseId=<%=PortalConstants.DEACTIVATE_CUSTOMER_USECASE_ID%>;	
				}
				else if(usecaseId==<%=PortalConstants.UNBLOCK_CUSTOMER_USECASE_ID%>){
					btnLabel= document.getElementById('btn_lockUnlock').value.trim();
					if(btnLabel=="Block")
						usecaseId=<%=PortalConstants.BLOCK_CUSTOMER_USECASE_ID%>;
				}
				
						
				var url = 'p-lockunlockaccountform.html?appUserId='+encryptAppUserId+'&usecaseId='+usecaseId+'&mfsId='+mfsId+'&actionId='+actionId+'&isLockUnlock='+isLockUnlock+'&isAgent=false';
		        newWindow=window.open(url,'Action Detail Window', 'width='+popupWidth+',height='+popupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
			    if(window.focus) newWindow.focus();
			    return false;		
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
	</head>

	<body bgcolor="#ffffff" onunload="javascript:closeChild();">
		
        <div id="profile_table_header">
        
        	<div class="row_div">
                <div class="heading">Quick Actions:</div>
                <c:choose>
				<c:when test="${not level2AccountModel.accountClosedUnsettled and not level2AccountModel.accountClosedSettled }">
					
				<c:choose>
					<c:when test="${not empty param.appUserId}">
					
					 <authz:authorize ifAnyGranted="<%=editButtonsPermission%>">
					
			         <c:set var="disableLockBtnAfterClick" value="true"/>
			         <c:set var="disableDeactivateBtnAfterClick" value="true"/>
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
					 
					 <c:if test="${level2AccountModel.registrationStateId == 3}">
					 <c:choose>
					 	<c:when test="${not deviceAccLocked}">
						 	<authz:authorize ifAnyGranted="<%=lockPermission%>">
								<input id="btn_lockUnlock" type="button" class="button" value="   Block   " disableAfterClick="${disableLockBtnAfterClick}" onclick="openlockunlockWindow('${param.appUserId}',usecaseIdLock.value,actionID.value,'${level2AccountModel.customerId}','true')"/>
				            </authz:authorize>
				            <authz:authorize ifNotGranted="<%=lockPermission%>">
								<input id="btn_lockUnlock" type="button" class="button" value="    Block    " disabled="disabled" />
				            </authz:authorize>
					 	</c:when>
					 	<c:otherwise>
						 	<authz:authorize ifAnyGranted="<%=unlockPermission%>">
								<input id="btn_lockUnlock" type="button" class="button" value="  Unblock  " disableAfterClick="${disableLockBtnAfterClick}" onclick="openlockunlockWindow('${param.appUserId}',usecaseIdLock.value,actionID.value,'${level2AccountModel.customerId}','true')"/>
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
									<input id="btn_actDeact" type="button" class="button" value="Deactivate" disableAfterClick="${disableDeactivateBtnAfterClick}" onclick="openlockunlockWindow('${param.appUserId}',usecaseId.value,actionID.value,'${level2AccountModel.customerId}','false')"/>
								</c:if>	
								<c:if test="${deviceAccLocked}">
									<input id="btn_actDeact" type="button" class="button" value="Deactivated" disabled="disabled"/>
								</c:if>
				            </authz:authorize>
				            <authz:authorize ifNotGranted="<%=deactivatePermission%>">	            
								<input id="btn_actDeact" type="button" class="button" value="Deactivate" disabled="disabled" />
				            </authz:authorize>
					 	</c:when>
					 	<c:otherwise>
						 	 <authz:authorize ifAnyGranted="<%=activatePermission%>">
						 	 <c:if test="${not deviceAccLocked}">
								<input id="btn_actDeact" type="button" class="button" value="Reactivate" disableAfterClick="${disableDeactivateBtnAfterClick}" onclick="openlockunlockWindow('${param.appUserId}',usecaseId.value,actionID.value,'${level2AccountModel.customerId}','false')"/>
								</c:if>
								<c:if test="${deviceAccLocked}">
								<input id="btn_actDeact" type="button" class="button" value="Reactivate"  disabled="disabled"/>
								</c:if>
				            </authz:authorize>
				            <authz:authorize ifNotGranted="<%=activatePermission%>">
								<input id="btn_actDeact" type="button" class="button" value="Reactivate" disabled="disabled" />
				            </authz:authorize>
					 	
					 	</c:otherwise>
					 </c:choose>	
					<%-- <authz:authorize ifAnyGranted="<%=PortalConstants.UPDATE_CUSTOMER_MOBILE%>">
					<c:if test="${deviceAccEnabled and not deviceAccLocked}">
						<input type="button" class="button" value="Update Mobile No."  onclick="javascript:document.location='p_mobilehistory.html?appUserId=${param.appUserId}&customerId=${level2AccountModel.customerId}&mobileNo=${level2AccountModel.mobileNo}';"  />
					</c:if>
					<c:if test="${not deviceAccEnabled or deviceAccLocked}">
						<input type="button" class="button" value="Update Mobile No." disabled="disabled" />
					</c:if>
					</authz:authorize>
					<authz:authorize ifAnyGranted="<%=PortalConstants.UPDATE_CUSTOMER_CNIC%>">
					<c:if test="${deviceAccEnabled and not deviceAccLocked}">
						<input type="button" class="button" value="Update CNIC"  onclick="javascript:document.location='p_cnichistory.html?appUserId=${param.appUserId}&customerId=${level2AccountModel.customerId}&nic=${level2AccountModel.applicant1DetailModel.idNumber}';"  />
					</c:if>
					<c:if test="${not deviceAccEnabled or deviceAccLocked}">
						<input type="button" class="button" value="Update CNIC" disabled="disabled" />
					</c:if>
					</authz:authorize> --%>
					</c:if>
					<authz:authorize ifAnyGranted="<%=PortalConstants.UPDATE_CUSTOMER_MOBILE%>">
					<c:if test="${not deviceAccLocked}">
						<input type="button" class="button" value="Update Mobile No."  onclick="javascript:document.location='p_mobilehistory.html?appUserId=${param.appUserId}&customerId=${level2AccountModel.customerId}&mobileNo=${level2AccountModel.mobileNo}';"  />
					</c:if>
					<c:if test="${deviceAccLocked}">
						<input type="button" class="button" value="Update Mobile No." disabled="disabled" />
					</c:if>
					</authz:authorize>
					<authz:authorize ifAnyGranted="<%=PortalConstants.UPDATE_CUSTOMER_CNIC%>">
					<c:if test="${not deviceAccLocked}">
						<input type="button" class="button" value="Update CNIC"  onclick="javascript:document.location='p_cnichistory.html?appUserId=${param.appUserId}&customerId=${level2AccountModel.customerId}&nic=${level2AccountModel.applicant1DetailModel.idNumber}';"  />
					</c:if>
					<c:if test="${deviceAccLocked}">
						<input type="button" class="button" value="Update CNIC" disabled="disabled" />
					</c:if>
					</authz:authorize>
					<authz:authorize ifAnyGranted="<%=closeAccountPermission%>">
						<input id="btn_closeAccount" type="button" class="button" value="Close Account" onclick="openAccountClosingWindow('${level2AccountModel.appUserId}','${level2AccountModel.customerId}')"  /> 
					</authz:authorize>
					<authz:authorize ifNotGranted="<%=closeAccountPermission%>">
						<input id="btn_closeAccount" type="button" class="button" value="Close Account" disabled="disabled" /> 
					</authz:authorize>
					 
						<input type="hidden" id="usecaseId" value="<%=PortalConstants.REACTIVATE_CUSTOMER_USECASE_ID%>"/>	
						<input type="hidden" id="usecaseIdLock" value="<%=PortalConstants.UNBLOCK_CUSTOMER_USECASE_ID%>"/>	
						<input type="hidden" id="actionID" value="<%=PortalConstants.ACTION_UPDATE%>"/>	
						<input type="hidden" id="message" value=""/>
						<input type="hidden" id="appUsrId_${param.appUserId}" value="${param.appUserId}"/>
						<input type="hidden" id="referrer" value="${referrer}"/>
									
			<c:set var="smartMoneyAccountId"><security:encrypt strToEncrypt="${smAccountId}"/></c:set>
			<c:if test="${level2AccountModel.registrationStateId == 3 or level2AccountModel.registrationStateId == 2}">
            <authz:authorize ifAnyGranted="<%=regeneratePINPermission%>">
				<c:choose>
					   <c:when test="${(not empty smAccountId) && veriflyRequired && (not deviceAccLocked)}">
	    			        <input type="button"  class="button" value="Regenerate PIN" id="btnSmAcId${smartMoneyAccountId}"/>   						       
					   </c:when>
					   <c:otherwise>
					       <input type="button"  class="button" value="Regenerate PIN" id="btnSmAcId${smartMoneyAccountId}" disabled="disabled"/>   
					   </c:otherwise>
				</c:choose>
            </authz:authorize>
            <authz:authorize ifNotGranted="<%=regeneratePINPermission%>">	            
	            <input type="button"  class="button" value="Regenerate PIN" id="btnSmAcId${smartMoneyAccountId}" disabled="disabled" />
            </authz:authorize>
			<authz:authorize ifAnyGranted="<%=updatePINPermission%>">
				<c:choose>
					   <c:when test="${(not empty smAccountId) && veriflyRequired && deviceAccEnabled && (not deviceAccLocked) && (not credentialsExpired)}">
	    			        <input type="button"  class="button" value="Change PIN" id="btnChngPin"/>   						       
					   </c:when>
					   <c:otherwise>
					       <input type="button"  class="button" value="Change PIN" id="btnChngPin" disabled="disabled"/>   
					   </c:otherwise>
				</c:choose>
            </authz:authorize>
            <authz:authorize ifNotGranted="<%=updatePINPermission%>">	            
	            <input type="button"  class="button" value="Change PIN" id="btnChngPin" disabled="disabled" />
            </authz:authorize>
					<input type="hidden" value="${param.appUserId}" name="appUser${param.appUserId}" id="appUser${param.appUserId}" />
					<input type="hidden" value="${smartMoneyAccountId}" name="smAcId${smartMoneyAccountId}" id="smAcId${smartMoneyAccountId}" />
					<input type="hidden" id="usecaseId${param.appUserId}" value="<%=PortalConstants.FORGOT_VERIFLY_PIN_USECASE_ID%>"/>
					<ajax:htmlContent baseUrl="${contextPath}/p_forgotveriflypinChange.html" 
						eventType="click" 
						source="btnSmAcId${smartMoneyAccountId}" 
						target="successMsg" 
						parameters="appUserId={appUser${param.appUserId}},mfsId=${pageMfsId},smAcId={smAcId${smartMoneyAccountId}},usecaseId={usecaseId${param.appUserId}},actionId={actionID},mobileNo=${level2AccountModel.mobileNo}"
						errorFunction="globalAjaxErrorFunction"
						preFunction="initProgress"
						postFunction="resetProgress"
						/>
						
					<ajax:htmlContent baseUrl="${contextPath}/p_pinchange.html" 
						eventType="click" 
						source="btnChngPin" 
						target="successMsg" 
						parameters="mobileNo=${level2AccountModel.mobileNo},actionId={actionID}"
						errorFunction="globalAjaxErrorFunction"
						preFunction="initProgress"
						postFunction="resetProgress"
						/>
				</c:if>
				</authz:authorize>
				<c:if test="${level2AccountModel.registrationStateId == 3}">
				<authz:authorize ifNotGranted="<%=editButtonsPermission%>">
				 <c:choose>
					 	<c:when test="${not deviceAccLocked}">
						 	<input id="btn_lockUnlock" type="button" class="button" value="    Block    " disabled="disabled" />				            
					 	</c:when>
					 	<c:otherwise>
							<input id="btn_lockUnlock" type="button" class="button" value="  Unblock  " disabled="disabled" />				            
					 	</c:otherwise>
					 </c:choose>	
					 
					 <c:choose>
					 	<c:when test="${deviceAccEnabled}">						 	 	            
							<input id="btn_actDeact" type="button" class="button" value="Deactivate" disabled="disabled" />
					 	</c:when>
					 	<c:otherwise>
							<input id="btn_actDeact" type="button" class="button" value="Reactivate" disabled="disabled" />					 	
					 	</c:otherwise>
					 </c:choose>					 					 
					       <input type="button"  class="button" value="Regenerate PIN" id="btnSmAcId${smartMoneyAccountId}" disabled="disabled"/>  					
	 			</authz:authorize>
	 			</c:if>
	 			
					</c:when>
				</c:choose>
			</c:when>
			</c:choose>
			<input type="button" class="button" value="Back" tabindex="49" onclick="javascript:document.location='p_pgsearchuserinfo.html?actionId=2';" />		
                <div style="clear:both;"></div>
            </div>
            
            <div class="row_div">
                <div class="heading">View Customer Details:</div>

                <c:if test="${level2AccountModel.registrationStateId == 3 or level2AccountModel.registrationStateId == 2 }">

					<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_REQ_CHARGEBACK_READ%>">
							<input type="button" class="button" value="View Chargeback" tabindex="47" onclick="javascript:document.location='p_customerchargebacklist.html?searchKey=${pageMfsId}&actionId=2';" />&nbsp;
						</authz:authorize>
						<authz:authorize ifNotGranted="<%=PortalConstants.PERMS_REQ_CHARGEBACK_READ%>">
							<input type="button" class="button" value="View Chargeback" tabindex="47" disabled="disabled" />&nbsp;
						</authz:authorize>
						<authz:authorize ifAnyGranted="<%=bbStmtPermission%>">
							<input type="button" class="button" value="View BB Statement" tabindex="48" onclick="javascript:document.location='viewcustomerbbstatementmanagement.html?appUserId=${param.appUserId}&actionId=2&referrer=<%=referrer%>'" />&nbsp;
						</authz:authorize>
						<authz:authorize ifAnyGranted="<%=txHistPermission%>">
							<input type="button" class="button" value="Transaction History" tabindex="49" onclick="javascript:document.location='p-customertransactiondetails.html?actionId=2&mfsId=${pageMfsId}&mobileNo=${level2AccountModel.mobileNo}&appUserId=${param.appUserId}&referrer=<%=referrer%>'" />&nbsp;
						</authz:authorize>
						<!--<authz:authorize ifAnyGranted="<%=cashPaymentPermission%>">
						<input type="button" class="button" value="Cash Payment Requests" tabindex="50" onclick="javascript:document.location='p_cashwithdrawaltransdetails.html?actionId=2&appUserId=${param.appUserId}&registered=true&referrer=<%=referrer%>'" />&nbsp;
						</authz:authorize>
						commented on request of QA-->
						<authz:authorize ifAnyGranted="<%=complaintPermission%>">
						<input type="button" class="button" value="Add Complaint" tabindex="51" onclick="javascript:document.location='p_complaintform.html?appUserId=${param.appUserId}&actionId=1&usrType=2';" />&nbsp;
						</authz:authorize>
						</c:if>
				<c:if test="${ level2AccountModel.registrationStateId == 5 or level2AccountModel.registrationStateId == 6}">
					<authz:authorize ifAnyGranted="<%=bbStmtPermission%>">
						<input type="button" class="button" value="View BB Statement" tabindex="48" onclick="javascript:document.location='viewcustomerbbstatementmanagement.html?appUserId=${param.appUserId}&actionId=2&referrer=<%=referrer%>'" />&nbsp;
					</authz:authorize>
</c:if>
				<c:choose>
						<c:when test="${level2AccountModel.accountClosedUnsettled and not level2AccountModel.accountClosedSettled}">
							<authz:authorize ifAnyGranted="<%=closeAccountPermission%>">
							<input id="btn_settlement" type="button" class="button" value="Settle Account" onclick="openAccountClosingWindow('${level2AccountModel.appUserId}','${level2AccountModel.customerId}')"  /> 
							</authz:authorize>
							<authz:authorize ifNotGranted="<%=closeAccountPermission%>">
							<input id="btn_settlement" type="button" class="button" value="Settle Account" disabled="disabled" /> 
							</authz:authorize>
						</c:when>
					</c:choose>
            	<div style="clear:both;"></div>
            </div>
        </div>
        
        <div id="successMsg" class ="infoMsg" style="display:none;"></div>
		<div id="errorMsg" class ="errorMsg" style="display:none;"></div>
		<c:if test="${not empty sessionScope.ajaxMessageToDisplay}" >
			<div id="ajaxMsgDiv" class ="infoMsg">
				<c:out value="${sessionScope.ajaxMessageToDisplay}" /><c:remove var="ajaxMessageToDisplay" scope="session" />
			</div>
		</c:if>
		
		<spring:bind path="level2AccountModel.*">
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

     
        	<table class="profile_table" width="98%" border="0" cellpadding="0" cellspacing="1" style="margin:0px auto;">
            <tr>
            	<td width="50%" valign="top">
                	<fieldset>
                    	<legend>Level 2 Account Details</legend>
                        <table width="100%" border="0" cellpadding="0" cellspacing="1">
                            <tr>
                                <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Initail Application Form No.:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">${level2AccountModel.initialAppFormNo}</td>
                            </tr>
                            <tr>
                                <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Mobile No:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">${level2AccountModel.mobileNo}</td>
                            </tr>
                        </table>
                    </fieldset>
                	<fieldset>
                        <legend>Account Relationship Information</legend>
                        <table width="100%" border="0" cellpadding="0" cellspacing="1">
                        	<tr>
                            	<td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Purpose of Account:</td>
                              	<td width="60%" align="left" bgcolor="FBFBFB">${level2AccountModel.accountPurpose}</td>
                            </tr>
                            <tr>
                             	<td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Type of Account:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">${level2AccountModel.accounttypeName}</td>
                             </tr>
                             <tr>
                             	<td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Nature of Account:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">Current</td>
                             </tr>
                             <tr>
                               	<td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Segment:</td>
                               	<td width="60%" align="left" bgcolor="FBFBFB">${level2AccountModel.segmentName}</td>
                             </tr>
                             <tr>
                             	<td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Account Title:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">${level2AccountModel.customerAccountName}</td>
                             </tr>
                             <tr>
                               	<td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Currency:</td>
                               	<td width="60%" align="left" bgcolor="FBFBFB">PKR</td>
                             </tr>
                             <tr>
                              	<td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Tax Regime:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">${level2AccountModel.taxRegimeName}</td>
                              </tr>
                              <tr>
                              	<td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Fed (%age):</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">${level2AccountModel.fed}</td>
                              </tr>
                          </table>
                      </fieldset>             
                      <fieldset>
                    	<legend>Personal Information</legend>
                        <table width="100%" border="0" cellpadding="0" cellspacing="1">
                        	<tr>
                            	<td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Title:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">
                               		<c:if test="${level2AccountModel.applicant1DetailModel.title==1}">Mr.</c:if>
									<c:if test="${level2AccountModel.applicant1DetailModel.title==2}">Mrs.</c:if>
									<c:if test="${level2AccountModel.applicant1DetailModel.title==3}">Ms.</c:if>
									<c:if test="${level2AccountModel.applicant1DetailModel.title==4}">Dr.</c:if>
									<c:if test="${level2AccountModel.applicant1DetailModel.title==5}">Prof.</c:if>
									<c:if test="${level2AccountModel.applicant1DetailModel.title==6}">Miss.</c:if>
                                </td>
                            </tr>
                            <tr>
                            	<td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Gender:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">${level2AccountModel.applicant1DetailModel.gender}</td>
                             </tr>
                             <tr>
                             	<td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Applicant Name:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">${level2AccountModel.applicant1DetailModel.name}</td>
                             </tr>
                             <tr>
                             	<td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">ID Document Type:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">
                                    <c:if test="${level2AccountModel.applicant1DetailModel.idType==1}">CNIC</c:if>
									<c:if test="${level2AccountModel.applicant1DetailModel.idType==2}">NICOP</c:if>
									<c:if test="${level2AccountModel.applicant1DetailModel.idType==3}">Passport</c:if>
									<c:if test="${level2AccountModel.applicant1DetailModel.idType==4}">NARA</c:if>
									<c:if test="${level2AccountModel.applicant1DetailModel.idType==5}">POC</c:if>
									<c:if test="${level2AccountModel.applicant1DetailModel.idType==6}">SNIC</c:if>
                                </td>
                             </tr>
                             <tr>
                             	<td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">ID Document Number.:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">${level2AccountModel.applicant1DetailModel.idNumber}</td>
                             </tr>
                             <tr>
                             	<td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Date of ID Expiry:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB"><fmt:formatDate value="${level2AccountModel.applicant1DetailModel.idExpiryDate}" pattern="dd/MM/yyyy"/></td>
                             </tr>
                            <tr>
                             	<td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">NTN #:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">${level2AccountModel.applicant1DetailModel.ntn}</td>
                             </tr>
                             <tr>
                             	<td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Date of Birth:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB"><fmt:formatDate value="${level2AccountModel.applicant1DetailModel.dob}" pattern="dd/MM/yyyy"/></td>
                             </tr>
                             <tr>
                             	<td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Place of Birth:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">${level2AccountModel.applicant1DetailModel.birthPlaceName}</td>
                             </tr>
                             <tr>
                             	<td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Date of Visa Expiry:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB"><fmt:formatDate value="${level2AccountModel.applicant1DetailModel.visaExpiryDate}" pattern="dd/MM/yyyy"/></td>
                             </tr>
                             <tr>
                             	<td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Mother's Maiden Name:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">${level2AccountModel.applicant1DetailModel.motherMaidenName}</td>
                             </tr>
                             <tr>
                             	<td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Residential Status:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">
                                	<c:if test="${level2AccountModel.applicant1DetailModel.residentialStatus == 1}">Resident</c:if>
                                	<c:if test="${level2AccountModel.applicant1DetailModel.residentialStatus == 0}">Non-Resident</c:if>
                                </td>
                             </tr>
							<tr>
                             	<td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">US Citizen:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">
                                	<c:if test="${level2AccountModel.applicant1DetailModel.usCitizen}">Yes</c:if>
                                	<c:if test="${!level2AccountModel.applicant1DetailModel.usCitizen}">No</c:if>
                                </td>
                             </tr>
                             <tr>
                             	<td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Natonality:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">${level2AccountModel.applicant1DetailModel.nationality}</td>
                             </tr>
                             <tr>
                             	<td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Phone #:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">${level2AccountModel.applicant1DetailModel.landLineNo}</td>
                             </tr>
                             <tr>
                             	<td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Home #:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">${level2AccountModel.applicant1DetailModel.contactNo}</td>
                             </tr>
                             <tr>
                             	<td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Fax #:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">${level2AccountModel.applicant1DetailModel.fax}</td>
                             </tr>
                            <tr>
                             	<td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Mobile #:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">${level2AccountModel.mobileNo}</td>
                             </tr>
                             <tr>
                             	<td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Email:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">${level2AccountModel.applicant1DetailModel.email}</td>
                             </tr>
                             <tr>
                             	<td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Father/Husband Name:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">${level2AccountModel.applicant1DetailModel.fatherHusbandName}</td>
                             </tr>
                             <tr>
                             	<td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Name of Employee/Business:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">${level2AccountModel.applicant1DetailModel.employerName}</td>
                             </tr>
                             <tr>
                             	<td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Occupation:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">${level2AccountModel.applicant1DetailModel.occupationName}</td>
                             </tr>
                             <tr>
                             	<td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Profession:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">${level2AccountModel.applicant1DetailModel.professionName}</td>
                             </tr>
                             <tr>
                             	<td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Mailing Address:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">
                                	<c:if test="${level2AccountModel.applicant1DetailModel.mailingAddressType==1}">Resident</c:if>
                                	<c:if test="${level2AccountModel.applicant1DetailModel.mailingAddressType==0}">Office/Business</c:if>
                                </td>
                             </tr>
                             <tr>
                             	<td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Residential Address:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">${level2AccountModel.applicant1DetailModel.residentialAddress}</td>
                             </tr>
                             <tr>
                             	<td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">City:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">${level2AccountModel.applicant1DetailModel.residentialCityName}</td>
                             </tr>
                             <tr>
                             	<td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Office/Business Address:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">${level2AccountModel.applicant1DetailModel.buisnessAddress}</td>
                             </tr>
                             <tr>
                             	<td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">City:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">${level2AccountModel.applicant1DetailModel.buisnessCityName}</td>
                             </tr>
                             <tr>
                             	<td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Marital Status:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">
                                	<c:if test="${level2AccountModel.applicant1DetailModel.maritalStatus == 1}">Single</c:if>
                                	<c:if test="${level2AccountModel.applicant1DetailModel.maritalStatus == 2}">Married</c:if>
                                	<c:if test="${level2AccountModel.applicant1DetailModel.maritalStatus == 3}">Widowed</c:if>
                                	<c:if test="${level2AccountModel.applicant1DetailModel.maritalStatus == 4}">Seperated</c:if>
                                	<c:if test="${level2AccountModel.applicant1DetailModel.maritalStatus == 5}">Divorced</c:if>
                                </td>
                             </tr>
                         </table>
                      </fieldset>
                </td>
                <td width="50%" valign="top">
                	<fieldset class="${fn:toLowerCase(level2AccountModel.accountState)}">
                    	<legend>Status</legend>
                        <table width="100%" border="0" cellpadding="0" cellspacing="1">
                            <tr>
                                <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Account Type:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">${level2AccountModel.accounttypeName}</td>
                            </tr>
                            <tr>
                                <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Segment:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">${level2AccountModel.segmentName}</td>
                            </tr>
                            <tr>
                                <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Registration State:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">
                                    <c:if test="${level2AccountModel.registrationStateId == 1}">Bulk Request Received</c:if>
                                	<c:if test="${level2AccountModel.registrationStateId == 2}">Request Received</c:if>
                                	<c:if test="${level2AccountModel.registrationStateId == 3}">Verified</c:if>
                                	<c:if test="${level2AccountModel.registrationStateId == 4}">Discrepent</c:if>
                                	<c:if test="${level2AccountModel.registrationStateId == 5}">Decline</c:if>
                                	<c:if test="${level2AccountModel.registrationStateId == 6}">Reject</c:if>
                               </td>
                            </tr>
                            <tr>
                                <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Current Status:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">
                                	<c:choose>
										<c:when test="${level2AccountModel.accountClosedUnsettled and not level2AccountModel.accountClosedSettled}">Closed Unsettled</c:when>
										<c:when test="${level2AccountModel.accountClosedUnsettled and level2AccountModel.accountClosedSettled}">Closed Settled</c:when>
										<c:when test="${deviceAccLocked && not level2AccountModel.accountClosedUnsettled}">Blocked</c:when>
										<c:when test="${not deviceAccEnabled && not level2AccountModel.accountClosedUnsettled}">Deactivated</c:when>
										<c:when test="${credentialsExpired && not level2AccountModel.accountClosedUnsettled}">Credentials Expired</c:when>
										<c:when test="${not deviceAccLocked && deviceAccEnabled && not credentialsExpired && not level2AccountModel.accountClosedUnsettled}">Active</c:when>
										<c:otherwise>Unknown</c:otherwise>
									</c:choose>
									${statusDetails}
                                </td>
                                
                            </tr>
                            <tr>
                                <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Account State:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB"><span class="state">${level2AccountModel.accountState}</span></td>
                            </tr>
                            <tr>
                                <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Remaining Limits:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">
                                	<table width="95%" border="0" cellpadding="0" cellspacing="1" style="margin:0px auto; border:1px solid #999;">
                                        <tr align="center">
                                            <td width="33%" height="23"><b>Limits</b></td>
                                            <td width="34%" bgcolor="#8ec6e5"><b>Debit</b></td>
                                            <td width="33%" bgcolor="#8ec6e5"><b>Credit</b></td>
                                        </tr>
                                        <tr align="center">
                                            <td width="33%" height="23" bgcolor="#8ec6e5"><b>Daily</b></td>
                                            <td width="34%" bgcolor="#f8f8f8">${level2AccountModel.remainingDailyDebitLimit}</td>
                                            <td width="33%" bgcolor="#f8f8f8">${level2AccountModel.remainingDailyCreditLimit}</td>
                                        </tr>
                                        <tr align="center">
                                            <td width="33%" height="23" bgcolor="#8ec6e5"><b>Monthly</b></td>
                                            <td width="34%" bgcolor="#f8f8f8">${level2AccountModel.remainingMonthlyDebitLimit}</td>
                                            <td width="33%" bgcolor="#f8f8f8">${level2AccountModel.remainingMonthlyCreditLimit}</td>
                                        </tr>
                                        <tr align="center">
                                            <td width="33%" height="23" bgcolor="#8ec6e5"><b>Yearly</b></td>
                                            <td width="34%" bgcolor="#f8f8f8">${level2AccountModel.remainingYearlyDebitLimit}</td>
                                            <td width="33%" bgcolor="#f8f8f8">${level2AccountModel.remainingYearlyCreditLimit}</td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </fieldset>
                    <fieldset>
                    	<legend>Next of Kin</legend>
                        <table width="100%" border="0" cellpadding="0" cellspacing="1">
                            <tr>
                                <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Name:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">${level2AccountModel.nokDetailVOModel.nokName}</td>
                            </tr>
                            <tr>
                                <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Relationship:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">${level2AccountModel.nokDetailVOModel.nokRelationship}</td>
                            </tr>
                            <tr>
                                <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Telephone/Mobile #:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">${level2AccountModel.nokDetailVOModel.nokContactNo}</td>
                            </tr>
                            <tr>
                                <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">ID Document Type:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">
                                    <c:if test="${level2AccountModel.nokDetailVOModel.nokIdType==1}">CNIC</c:if>
									<c:if test="${level2AccountModel.nokDetailVOModel.nokIdType==2}">NICOP</c:if>
									<c:if test="${level2AccountModel.nokDetailVOModel.nokIdType==3}">Passport</c:if>
									<c:if test="${level2AccountModel.nokDetailVOModel.nokIdType==4}">NARA</c:if>
									<c:if test="${level2AccountModel.nokDetailVOModel.nokIdType==5}">POC</c:if>
									<c:if test="${level2AccountModel.nokDetailVOModel.nokIdType==6}">SNIC</c:if>
                               </td>
                            </tr>
                            <tr>
                                <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">ID Document Numer:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">${level2AccountModel.nokDetailVOModel.nokIdNumber}</td>
                            </tr>
                            <tr>
                                <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Address:</td>
                                <td width="60%" align="left" bgcolor="FBFBFB">${level2AccountModel.nokDetailVOModel.nokMailingAdd}</td>
                            </tr>
                        </table>
                    </fieldset>
                </td>
            </tr>
            <c:if test="${not empty appUserHistoryViewModelList}">
            <tr>
            	<td colspan="2">
            		<fieldset>
			        	<legend>Account History</legend>
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
	        		</fieldset>
            	</td>
            </tr>
            </c:if>
        </table>
<!---******************************END LEVEL 0 FORM ********************************************************-->
	</body>
</html>