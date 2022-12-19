<!--Author: Basit Mehr -->
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<%@page import="com.inov8.microbank.common.util.AuthenticationUtil"%>
<%@page import="com.inov8.microbank.common.util.PortalConstants"%>
<%@include file="/common/taglibs.jsp"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<html>
	<head>
<meta name="decorator" content="decorator">
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/prototype.js"></script>
	<script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>

		<link rel="stylesheet"
			href="${pageContext.request.contextPath}/styles/extremecomponents.css"
			type="text/css">
			
	<%--<c:choose >
		<c:when test="${ ((not empty param.derelinkFromMicrobank) and (empty param.deletelinkFromMicrobank)) or (empty param.derelinkFromMWallet and empty param.deletelinkFromMicrobank) }">
			<meta name="title" content="De-Link/Re-Link Payment Mode Report" />
		</c:when>
		<c:otherwise>
			<meta name="title" content="Delete Payment Mode" />
		</c:otherwise>			
	</c:choose>--%>
	<%
		String delinkRelinkPermission = PortalConstants.PG_GP_UPDATE + "," + PortalConstants.DE_LINK_PAY_MOD_UPDATE + "," + PortalConstants.RE_LINK_PAY_MOD_UPDATE;
		String delinkPermission = PortalConstants.PG_GP_UPDATE + "," + PortalConstants.DE_LINK_PAY_MOD_UPDATE;
		String relinkPermission = PortalConstants.PG_GP_UPDATE + "," + PortalConstants.RE_LINK_PAY_MOD_UPDATE;
		String deleteLinkPermission = PortalConstants.PG_GP_DELETE + "," + PortalConstants.DELETE_PAY_MOD_DELETE;
	%>
		<script type="text/javascript">
			var jq=$.noConflict();
		</script>
		<meta name="title" content="De-Link/Re-Link Payment Mode Report" id="<%=ReportIDTitleEnum.LinkDelinkPaymentModeReport.getId()%>" />
		<%@include file="/WEB-INF/pages/export_zip.jsp"%>
		<script type="text/javascript">
		var delinkPayModeAllowed = <%=AuthenticationUtil.checkRightsIfAny( delinkPermission )%>;
		var relinkPayModeAllowed = <%=AuthenticationUtil.checkRightsIfAny( relinkPermission )%>;
		var deleteLinkPayModeAllowed = <%=AuthenticationUtil.checkRightsIfAny( deleteLinkPermission )%>;


		var serverDate ="<%=PortalDateUtils.getServerDate()%>";
		var username = "<%=UserUtils.getCurrentUser().getUsername()%>";
		var appUserId= "<%=UserUtils.getCurrentUser().getAppUserId()%>";
		var email= "<%=UserUtils.getCurrentUser().getEmail()%>";

		function initProgress()
		{
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

		function initDelProgress()
		{
			if (confirm('To confirm deletion of payment mode, press OK')==true)
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
		    	var btnId = $(this).action;
		    	var btnVal = document.getElementById(btnId).value;
		    	if( (btnVal=='De-Link' && !delinkPayModeAllowed) || ( btnVal=='Re-Link' && !relinkPayModeAllowed ) )
		    	{
		    		document.getElementById(btnId).disabled = true;
		    	}

		    	if( btnVal=='Re-Link' )
		    	{
		    		if( deleteLinkPayModeAllowed )
		    		{
		    			document.getElementById( 'd' + btnId).disabled = false;
		    		}
		    		else
		    		{
		    			document.getElementById( 'd' + btnId).disabled = true;
		    		}
		    	}
		    	else if( btnVal=='De-Link' )
		    	{
		    		document.getElementById( 'd' + btnId).disabled = true;
		    	}

			    // clear error box
			    $('errorMsg').innerHTML = "";
			  	Element.hide('errorMsg'); 
			    
			    $('successMsg').innerHTML = $F('message');
			    // display success message
			    Element.show('successMsg');
			}
				isErrorOccured = false;
		}

		function processDelete()
		{
			if(!isErrorOccured){					
			    // clear error box
			    $('errorMsg').innerHTML = "";
			  	Element.hide('errorMsg'); 
			    
			    $('successMsg').innerHTML = $F('message');
			    // display success message
			    Element.show('successMsg');
			    divId = $F('divsToDisable');
			    
			    deletePaymentModeRow( divId );
			    if($('link_'+divId) != null){
			    	$('link_'+divId).innerHTML = "&nbsp;";
			    }
			    if($('del_'+divId) != null){
			    	$('del_'+divId).innerHTML = "&nbsp;";
			    }
			 }
		}
		
		function reportError(request, obj) 
		{
			//alert($F('message'));
		  var msg = "";
		  if($(obj).value=="De-Link")
		  	msg = "Payment Mode could not be De-Activated";
		  else
		   	msg = "Payment Mode could not be Re-Activated";

		  $('successMsg').innerHTML = "";
		  Element.hide('successMsg'); 
		  $('errorMsg').innerHTML = msg;
		  Element.show('errorMsg');
		  isErrorOccured = true;
		}

		function reportDeleteError(request, obj) 
		{
		  var msg = "Record could not be deleted. Please contact administrator.";
				
		  $('successMsg').innerHTML = "";
		  Element.hide('successMsg'); 
		  $('errorMsg').innerHTML = msg;
		  Element.show('errorMsg');
		  isErrorOccured = true;
		}

		function deletePaymentModeRow(id)
  		{
  			var paymentModeDiv = $('del_'+id);
  			var paymentModeDivParents = Element.ancestors( paymentModeDiv );
  			Element.hide( paymentModeDivParents[1] );//delete payment mode row
  		}

	</script>		
	<%@include file="/common/ajax.jsp"%>
	</head>


	<div id="successMsg" class ="infoMsg" style="display:none;"></div>
	<div id="errorMsg" class ="errorMsg" style="display:none;"></div>

		<spring:bind path="delinkRelinkPaymentModeVieModel.*">
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

		
			<html:form name="delinkrelinkpaymentmodeForm"
				action="p_allpayderelinkpaymentmode.html" method="post"
				commandName="delinkRelinkPaymentModeVieModel" onsubmit="return validateForm()"  >
				<table width="750px"><tr>
					<td width="33%" align="right" class="formText">
						First Name:
					</td>
					<td width="17%" align="left">
					
					<c:choose>
						<c:when test="${not empty param.fromAppUserId}">
							<html:input path="firstName" readonly="true" cssClass="textBox" maxlength="50" tabindex="1" onkeypress="return maskCommon(this,event)"  />
						</c:when>					
						<c:otherwise>
							<html:input path="firstName" cssClass="textBox" maxlength="50" tabindex="1" onkeypress="return maskCommon(this,event)"  />
						</c:otherwise>
					</c:choose>						
					
					</td>
					<td width="16%" align="right"><span class="formText">Last Name: </span></td>
					<td width="34%" align="left">


					<c:choose>
						<c:when test="${not empty param.fromAppUserId}">
							<html:input path="lastName" readonly="true" cssClass="textBox" maxlength="50" tabindex="2" onkeypress="return maskCommon(this,event)"  />
						</c:when>					
						<c:otherwise>
							<html:input path="lastName" cssClass="textBox" maxlength="50" tabindex="2" onkeypress="return maskCommon(this,event)"  />
						</c:otherwise>
					</c:choose>						


						<input type="hidden" id="message" value=""/>
						<input type="hidden" id="divsToDisable" value=""/>						
						
						<input type="hidden" id="derelinkFromMicrobank" name="derelinkFromMicrobank" value="${param.derelinkFromMicrobank}"/>
						<input type="hidden" id="deletelinkFromMicrobank" name="deletelinkFromMicrobank" value="${param.deletelinkFromMicrobank}"/>

						<input type="hidden" id="fromAppUserId" name="fromAppUserId" value="${fromAppUserId}"/>
						

					</td>

				</tr>
				<tr>
				  <td class="formText" align="right"> Agent ID: </td>
					<td align="left">
					
						<c:choose>
							<c:when test="${not empty param.fromAppUserId}">
								<html:input path="mfsId" readonly="true" cssClass="textBox" maxlength="12" onkeypress="return maskCommon(this,event)"  tabindex="3"/>
							</c:when>					
							<c:otherwise>
								<html:input path="mfsId" cssClass="textBox" maxlength="12" onkeypress="return maskCommon(this,event)"  tabindex="3"/>
							</c:otherwise>
						</c:choose>						
					
					
					</td>
					<td class="formText" align="right"> NIC #: </td>
					<td align="left">
						<c:choose>
							<c:when test="${not empty param.fromAppUserId}">
								<html:input path="nic" readonly="true" cssClass="textBox" maxlength="13" onkeypress="return maskCommon(this,event)" tabindex="4"/>
							</c:when>					
							<c:otherwise>
								<html:input path="nic" cssClass="textBox" maxlength="13" onkeypress="return maskCommon(this,event)" tabindex="4"/>
							</c:otherwise>
						</c:choose>						
					</td>
				</tr>
				<tr>
				  <td class="formText" align="right"> Account Nick: </td>
					<td align="left">
						<html:input path="accountNick" cssClass="textBox" onkeypress="return maskCommon(this,event)" maxlength="50" tabindex="5"/><c:if test="${not empty param.fromAppUserId}">&nbsp;<img id="dobD" tabindex="6"  name="popcal" title="Clear Account Nick" align="middle" onclick="javascript:$('accountNick').value=''"   align="middle" style="cursor:pointer" src="images/refresh.png" border="0" /></c:if></td>
					<td align="left">&nbsp;</td>
					<td align="left">
						
					</td>
				</tr>

				<tr>
					<td class="formText" align="right"></td>
					<td align="left">
						<input name="_search" type="submit" class="button" value="Search" tabindex="7"/>
						<c:choose>
							<c:when test="${not empty param.fromAppUserId}">
	 	    	                  <input name="reset" type="reset" class="button" value="Cancel"
										onclick="window.location='p_mnonewmfsaccountform.html?appUserId=${fromAppUserId}'" tabindex="8"/>
							</c:when>					
							<c:otherwise>
	 	    	                  <input name="reset" type="reset" class="button" value="Cancel"
										onclick="javascript: window.location='p_allpayderelinkpaymentmode.html?actionId=${retriveAction}'" tabindex="8"/>
							</c:otherwise>
						</c:choose>
					</td>
					<td align="left">&nbsp;</td>
					<td align="left">&nbsp;

					</td>
		    </tr>

				<tr>
					<td colspan="4" align="left" height="10">&nbsp;
						
					</td>
				</tr>

				<input type="hidden" name="<%=PortalConstants.KEY_USECASE_ID%>"
					value="<%=PortalConstants.RELINK_PAYMENT_MODE_USECASE_ID%>">
				<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
			</table></html:form>
		

		<ec:table filterable="false" items="delinkRelinkPaymentModeModelList"
			var="delinkRelinkPaymentModeRec" retrieveRowsCallback="limit"
			filterRowsCallback="limit" sortRowsCallback="limit"
			action="${pageContext.request.contextPath}/p_allpayderelinkpaymentmode.html?actionId=${retriveAction}">
			
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="delinkRelinkPaymentMode.xls"
					tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="De-Link/Re-Link Payment Mode Report.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="De-Link/Re-Link Payment Mode"
					fileName="De-Link/Re-Link Payment Mode.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="De-Link/Re-Link Payment Mode Report.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>		

			<ec:row>
				<c:set var="delinkRelinkAppUserId">
					<security:encrypt strToEncrypt="${delinkRelinkPaymentModeRec.appUserId}"/>
				</c:set>
				<c:set var="delinkRelinkSmartMoneyAccountId">
					<security:encrypt strToEncrypt="${delinkRelinkPaymentModeRec.smartMoneyAccountId}"/>
				</c:set>
				<ec:column property="accountNo" accountCell="true" escapeAutoFormat="true"/>
				<ec:column property="accountNick" title="Account Nick"
					filterable="false" />
				<ec:column property="paymentModeName" title="Payment Mode"
					filterable="false" />
				<ec:column property="fullName" title="Name" filterable="false"	sortable="true" />
				<ec:column property="mfsId" title="Agent ID" sortable="true" filterable="false" escapeAutoFormat="true" />
				<ec:column property="mobileNo" title="Mobile#" filterable="false" escapeAutoFormat="true"/>
				<ec:column property="fullAddress" sortable="false" title="Address"	filterable="false" />
				<ec:column property="nic" title="NIC #" filterable="false" escapeAutoFormat="true"/>
				<ec:column property="accountOpeningDate" filterable="false" cell="date"	format="dd/MM/yyyy" title="A/C Opening Date" />
				<ec:column property="username" title="User Name"/>
				<ec:column property="areaName" title="Area"/>




	<c:if test="${ ((not empty param.derelinkFromMicrobank) and (empty param.deletelinkFromMicrobank)) or (empty param.derelinkFromMWallet and empty param.deletelinkFromMicrobank) }">
	
				<ec:column alias=" "  sortable="false" style="vertical-align: middle" filterable="false" viewsAllowed="html" > 
					<div id="link_${delinkRelinkAppUserId}${delinkRelinkSmartMoneyAccountId}">
					<c:choose>
						<c:when test='${delinkRelinkPaymentModeRec.smaActive}'>				
				
						<authz:authorize ifAnyGranted="<%=delinkPermission%>">
							<input id="btn_${delinkRelinkAppUserId}${delinkRelinkSmartMoneyAccountId}"
										type="button" class="button" 
										value="De-Link" onclick="linkDelinkAccountWindow('${delinkRelinkAppUserId}','${delinkRelinkSmartMoneyAccountId}','${delinkRelinkPaymentModeRec.mfsId}','<%=PortalConstants.DELINK_PAYMENT_MODE_USECASE_ID%>','3','false')" />
						</authz:authorize>	            
						<authz:authorize ifNotGranted="<%=delinkPermission%>">
							<input id="btn_${delinkRelinkAppUserId}${delinkRelinkSmartMoneyAccountId}"
								type="button" class="button" 
								value="De-Link" disabled="disabled" />						
						</authz:authorize>										
													        
						</c:when>
						<c:otherwise>

							<authz:authorize ifAnyGranted="<%=relinkPermission%>">
								<input id="btn_${delinkRelinkAppUserId}${delinkRelinkSmartMoneyAccountId}"
									type="button" class="button" 
									value="Re-Link" onclick="linkDelinkAccountWindow('${delinkRelinkAppUserId}','${delinkRelinkSmartMoneyAccountId}','${delinkRelinkPaymentModeRec.mfsId}','<%=PortalConstants.RELINK_PAYMENT_MODE_USECASE_ID%>','3','false')" />							
							</authz:authorize>	            
							<authz:authorize ifNotGranted="<%=relinkPermission%>">
								<input id="btn_${delinkRelinkAppUserId}${delinkRelinkSmartMoneyAccountId}"
									type="button" class="button" 
									value="Re-Link" disabled="disabled" />
							</authz:authorize>				
													 					         
						</c:otherwise>
					</c:choose>
					<authz:authorize ifAnyGranted="<%=delinkRelinkPermission%>">
						<input id="smAcId_${delinkRelinkAppUserId}${delinkRelinkSmartMoneyAccountId}" 
							type="hidden"
							value="${delinkRelinkSmartMoneyAccountId}"/>
							<input id="apUsrId_${delinkRelinkAppUserId}${delinkRelinkSmartMoneyAccountId}" 
								type="hidden"
								value="${delinkRelinkAppUserId}"/>
						<input type="hidden" id="usecaseId" value="<%=PortalConstants.RELINK_PAYMENT_MODE_USECASE_ID%>"/>	
	
					<!-- 	
							<ajax:updateField
							  baseUrl="${contextPath}/p_delinkrelinkpaymentmodechange.html"
							  source="btn_${delinkRelinkAppUserId}${delinkRelinkSmartMoneyAccountId}"
							  target="btn_${delinkRelinkAppUserId}${delinkRelinkSmartMoneyAccountId},message"
							  action="btn_${delinkRelinkAppUserId}${delinkRelinkSmartMoneyAccountId}"
							  parameters="actionId=3,smAcId={smAcId_${delinkRelinkAppUserId}${delinkRelinkSmartMoneyAccountId}},apUsrId={apUsrId_${delinkRelinkAppUserId}${delinkRelinkSmartMoneyAccountId}},usecaseId={usecaseId},btn=btn_${delinkRelinkAppUserId}${delinkRelinkSmartMoneyAccountId}"
							  parser="new ResponseXmlParser()"
							  preFunction="initProgress"
							  postFunction="resetProgress" 
							  errorFunction="reportError"
							  />
					 -->		  		
					</authz:authorize>
				 </div> 
				</ec:column>
	</c:if>


				<c:if test="${ ((empty param.derelinkFromMicrobank) and (not empty param.deletelinkFromMicrobank)) or (empty param.derelinkFromMWallet and empty param.deletelinkFromMicrobank) }">		
						<ec:column alias=" "  sortable="false" style="vertical-align: middle" filterable="false" viewsAllowed="html" > 
							<div id="del_${delinkRelinkAppUserId}${delinkRelinkSmartMoneyAccountId}">
								<authz:authorize ifAnyGranted="<%=deleteLinkPermission%>">
									<c:choose>
										<c:when test='${delinkRelinkPaymentModeRec.smaActive}'>
											<input id="dbtn_${delinkRelinkAppUserId}${delinkRelinkSmartMoneyAccountId}" type="button" class="button" value="Delete" disabled="disabled"/>
										</c:when>
										<c:otherwise>
											<input id="dbtn_${delinkRelinkAppUserId}${delinkRelinkSmartMoneyAccountId}" type="button" class="button" value="Delete" onclick="linkDelinkAccountWindow('${delinkRelinkAppUserId}','${delinkRelinkSmartMoneyAccountId}','${delinkRelinkPaymentModeRec.mfsId}','<%=PortalConstants.DELETE_PAYMENT_MODE_USECASE_ID%>','4','true')"/>
										</c:otherwise>
									</c:choose>
									<input id="smAcId_${delinkRelinkAppUserId}${delinkRelinkSmartMoneyAccountId}" type="hidden" value="${delinkRelinkSmartMoneyAccountId}"/>
											<input id="apUsrId_${delinkRelinkAppUserId}${delinkRelinkSmartMoneyAccountId}" type="hidden" value="${delinkRelinkAppUserId}"/>
											<input type="hidden" id="usecaseId" value="<%=PortalConstants.RELINK_PAYMENT_MODE_USECASE_ID%>"/>
											
										<!-- <ajax:updateField
											  baseUrl="${contextPath}/p_delinkrelinkpaymentmodechange.html"
											  source="dbtn_${delinkRelinkAppUserId}${delinkRelinkSmartMoneyAccountId}"
											  target="message,divsToDisable"
											  action="dbtn_${delinkRelinkAppUserId}${delinkRelinkSmartMoneyAccountId}"
											  parameters="actionId=4,smAcId={smAcId_${delinkRelinkAppUserId}${delinkRelinkSmartMoneyAccountId}},apUsrId={apUsrId_${delinkRelinkAppUserId}${delinkRelinkSmartMoneyAccountId}},usecaseId={usecaseId},isDeleted=true"
											  parser="new ResponseXmlParser()"
											  preFunction="initDelProgress"
											  postFunction="processDelete" 
											  errorFunction="reportDeleteError"
									  		/>  -->
								</authz:authorize>	            
								<authz:authorize ifNotGranted="<%=deleteLinkPermission%>">
									<input id="dbtn_${delinkRelinkAppUserId}${delinkRelinkSmartMoneyAccountId}" type="button" class="button" value="Delete" disabled="disabled"/>
								</authz:authorize>				
								 </div> 						
						</ec:column>
				</c:if>
				
			</ec:row>
		</ec:table>
	
		<script type="text/javascript">
	        highlightFormElements();
	        document.forms[0].firstName.focus();
	        	        
	        var childWindow;
	
			function linkDelinkAccountWindow(encryptAppUserId,smaAccountId,mfsId,usecaseId,actionId,isDelete)
			{
				var popupWidth = 550;
				var popupHeight = 350;	       
				var popupLeft = (window.screen.width - popupWidth)/2;
				var popupTop = (window.screen.height - popupHeight)/2;		
				var url = 'p-allpayredelinkpaymentmodeform.html?actionId='+actionId+'&smAcId='+smaAccountId+'&apUsrId='+encryptAppUserId+'&usecaseId='+usecaseId+'&isDeleted='+isDelete+'&mfsId='+mfsId;
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
		<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>
			
		
</html>
