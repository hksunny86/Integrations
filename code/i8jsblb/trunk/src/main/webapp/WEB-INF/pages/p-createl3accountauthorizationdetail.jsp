<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.*'%>

		<c:choose>
			<c:when test="${!empty status}">
				<c:set var="_status" value="${status}"/>
			</c:when>
			<c:otherwise>
				<c:set var="_status" value="${param.status}"/>
			</c:otherwise>
		</c:choose>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="cache-control" content="no-store" />
		<meta http-equiv="cache-control" content="private" />
		<meta http-equiv="cache-control" content="max-age=0, must-revalidate" />
		<meta http-equiv="expires" content="now-1" />
		<meta http-equiv="pragma" content="no-cache" />
	
		<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
		<script src="${pageContext.request.contextPath}/scripts/jquery-1.7.2.min.js"></script>
		
		<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css" type="text/css"/>
		
	<script type="text/javascript">
			
	function onFormSubmit() {
		document.getElementById("_save").disabled = true;
		validate();
	}			
			
	function refreshParent() {
		  window.opener.document.forms[0].submit();
		  window.close();
	 }
	 
	 function disableSubmit(){
 			if(!${param.resolveRequest}){
 				document.getElementById("_save").disabled = true;
  			}	
  	 } 
  	 function onChangeStatus(){
  	 
  	 		var status = document.getElementById("actionStatus").value;
  	 		if(status!=4)
  	 			document.getElementById("_save").disabled = false;
  	 		else  
  	 			document.getElementById("_save").disabled = true;	 		
  	 } 
  	
  	var jq = $.noConflict(); 
  	var doAjaxBeforeUnload = function (evt) {
 
	    doAjaxBeforeUnloadEnabled = false;
	    jq.ajax({
	        url: "${contextPath}/p_mnomfsaccountdetailsajx.html?actionAuthorizationId=${param.actionAuthorizationId}",
	        success: function (a) {
	            console.debug("Ajax call finished");
	        },
	        async: false /* Not recommended.  This is the dangerous part.  Your mileage may vary. */
	    });
	}
	
	window.onbeforeunload = doAjaxBeforeUnload;
    $(window).unload(doAjaxBeforeUnload);
			
							
		</script>
	<style>
			.header {
				background-color: #6699CC;/*308dbb original*/
				color: white;
				font-family:verdana, arial, helvetica, sans-serif;/*verdana, arial, helvetica, sans-serif*/
				font-size: 11px;
				font-weight: bold;
				text-align: center;
				padding-right: 3px;
				padding-left: 3px;
				padding-top: 4px;
				padding-bottom: 4px;
				margin: 0px;
				border-right-style: solid;
				border-right-width: 1px;
				border-color: white;
			}		
	</style>
	<%
		String updatePermission = PortalConstants.ADMIN_GP_UPDATE;
		updatePermission += "," + PortalConstants.MNG_USECASE_UPDATE;
		updatePermission += "," + PortalConstants.PG_GP_UPDATE;
		updatePermission += "," + PortalConstants.ACTION_AUTHORIZATION_UPDATE;
 	%>
	
	</head>	
	<body bgcolor="#ffffff" onload="javascript:disableSubmit();">
	<h3 class="header" id="logHeader">Action Authorization Detail </h3>
	<c:choose>
   	<c:when test="${not empty _status}">
   		<c:choose>
				<c:when test="${_status eq 'success'}">
				
					<div class="">
							   		<table class="tableRegion">
										<tr class="odd">
											<td class="formText">	
										        
										        <c:if test="${actionAuthorizationModel.actionStatusId == 3}">
													Action is authorized successfully. Changes are saved.<p>Click the button below to close the window</p>
												</c:if>
												<c:if test="${actionAuthorizationModel.actionStatusId != 3 }">
													Record Updated successfully.<p>Click the button below to close the window</p>
												</c:if>
											</td>
										</tr>
									</table>					        		
					</div>	
			</c:when>
			<c:otherwise>
				<div class="">
				   		<table class="tableRegion">
							<tr class="odd">
								<td class="formText"><c:out value="${message}" /></td>
							</tr>
						</table>
					</div>	
			</c:otherwise>
			
		</c:choose>
		<div class="eXtremeTable" style="text-align: center; padding-top: 10px;">
			<input type ="button" class="button" value = "Close Window" onclick = "refreshParent()"/>
		</div>
	</c:when>	
	<c:otherwise>
	 <c:if test="${(actionAuthorizationModel.actionStatusId == 3) || (actionAuthorizationModel.actionStatusId == 5) || (actionAuthorizationModel.actionStatusId == 6) || (actionAuthorizationModel.actionStatusId == 7)|| (actionAuthorizationModel.actionStatusId == 8) || (actionAuthorizationModel.actionStatusId == 9)}">
	<c:set var="approvedOrDenied" value="true" scope="page" />
	</c:if>	
		   		<table class="tableRegion" width="100%">
					<tr class="titleRow">
						<td>	
							<html:form  name = "actionDetailForm" id = "actionDetailForm" commandName="actionAuthorizationModel" action="p-createl3accountauthorizationdetail.html" method="post" onsubmit="return onFormSubmit()">
						       <table width="100%" border="0" cellpadding="0" cellspacing="1">
						       		
							        <tr bgcolor="FBFBFB">
							             <td colspan="2" align="center">&nbsp;</td>
						            </tr>
						           <tr>
						             <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Action ID:&nbsp;&nbsp;</td>
						             <td width="51%" align="left" bgcolor="FBFBFB">
										<html:input path="actionAuthorizationId" id="actionAuthorizationId" cssClass="textBox" maxlength="50" tabindex="1" readonly="true" />
						             </td>
						           </tr>
						            <tr>
						             <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Action Type:&nbsp;&nbsp;</td>
						             <td width="51%" align="left" bgcolor="FBFBFB">
										<html:input path="usecaseName" id="usecaseName" cssClass="textBox" maxlength="50" tabindex="1" readonly="true"/>				
						             </td>
						           </tr>
						        
						           <c:if test="${not approvedOrDenied }">
						            <tr>
						             <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Escalation Level:&nbsp;&nbsp;</td>
						             <td width="51%" align="left" bgcolor="FBFBFB">
										<html:input path="escalationLevel" id="escalationLevel" cssClass="textBox" maxlength="50" tabindex="1" readonly="true"/>
						             </td>
						           </tr>
						            </tr>
						            <tr>
						             <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Created By:&nbsp;&nbsp;</td>
						             <td width="51%" align="left" bgcolor="FBFBFB">
										<html:input path="createdByUsername" id="createdByUsername" cssClass="textBox" maxlength="50" tabindex="1" readonly="true"/>
						             </td>
						           </tr>
						            <tr>
						             <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Created On:&nbsp;&nbsp;</td>
						             <td width="51%" align="left" bgcolor="FBFBFB">
										<html:input path="createdOn" id="createdOn" cssClass="textBox" maxlength="50" tabindex="1" readonly="true"/>
						             </td>
						          
						           
						           <tr>
						             <td align="right" bgcolor="F3F3F3" class="formText">Initiator Comments:&nbsp;&nbsp; </td>
						             <td align="left" bgcolor="FBFBFB">			              
									  <html:textarea path="comments"   id="comments"  onkeypress="return maskCommon(this,event)" onkeyup="textAreaLengthCounter(this,250);" cssClass="textBox" rows="6" readonly="true" cssStyle="overflow: auto; width: 163px; height: 102px;"/>						   
						             </td>
						           </tr>
						           <tr>
						             <td align="right" bgcolor="F3F3F3" class="formText">Authorization Status:&nbsp;&nbsp; </td>
						             <td align="left" bgcolor="FBFBFB">	
										<html:select path="actionStatusId" id="actionStatus" onchange="onChangeStatus()">
											<html:options items="${actionStatusModel}" itemLabel="name" itemValue="actionStatusId" />									
										</html:select>										
						             </td>
						           </tr>
					
						           <tr>
						             <td align="right" bgcolor="F3F3F3" class="formText">Authorizer Comments:&nbsp;&nbsp; </td>
						             <td align="left" bgcolor="FBFBFB">			              
									  <html:textarea path="checkerComments"   id="checkerComments" onkeyup="textAreaLengthCounter(this,250);"  onkeypress="return maskCommon(this,event)" cssClass="textBox" rows="6"  readonly="${approvedOrDenied }"  cssStyle="overflow: auto; width: 163px; height: 102px;"  />										   
						             </td>
						           </tr>
						           
						           <tr bgcolor="FBFBFB">
						             <td colspan="2" align="center">&nbsp;</td>
						           </tr>
						           <tr>
							           <input type="hidden" name="escalateRequest" value="${param.escalateRequest}">
							           <input type="hidden" name="resolveRequest"  value="${param.resolveRequest}">
							          
							           <td align="center" colspan="2">	
							           	<authz:authorize ifAnyGranted="<%=updatePermission%>">
							           		 <input name="_save" id="_save" type="submit" class="button"  value=" Update " /> &nbsp;
										</authz:authorize>
										<authz:authorize ifNotGranted="<%=updatePermission%>">
											 <input name="_save" id="_save" type="submit" class="button"  value=" Update " disabled="disabled" /> &nbsp;
										</authz:authorize>
								           <input name="cancel" type="button" class="button" value=" Cancel " onclick="javascript:window.close();"/>								           
							           </td>
						           </tr>
						         </c:if>
						         <c:if test="${isAssignedBack}">
						         	<tr bgcolor="FBFBFB">
						             	<td colspan="2" align="center">&nbsp;</td>
						           </tr>							
									<tr>
						         		<td align="center" colspan="2">	
						        			<input name="modify" type="button" class="button" value=" Modify " onclick="javascript:doModify('p_l3accountopeningform.html?isReSubmit=true&initAppFormNumber=${level2AccountModel.initialAppFormNo}&authId=${actionAuthorizationModel.actionAuthorizationId}&actionId=1');window.close();"/>
						        		</td>			        		
						        	</tr>					        							
								</c:if>  
						            <tr bgcolor="FBFBFB">
						             <td colspan="2" align="center">&nbsp;</td>
						           </tr>
						    </table>
						</html:form>
					</td>
				</tr>
			</table>
			<h3 class="header" id="detailHeader">Agent Details </h3>
			<table  class="tableRegion" width="100%">
				<tr> 
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Registration State</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.regStateName}</td>
				</tr>
				<tr>
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Registration Date</td>
					<td width="25%" height="16" align="left"  class="formText"><fmt:formatDate  type="both" 
            			dateStyle="short" timeStyle="short" value="${level3AccountModel.createdOn}" /></td>
				
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Reg.State Comments</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.regStateComments }</td>
				</tr>
				<tr>
              		<td colspan="8" align="center" bgcolor="FBFBFB">
						<h3>
							L3 Account Opening Form
						</h3>
					</td>
                </tr>
				<tr>
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Initial Application Form No.</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.initialAppFormNo }</td>
					
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Account Title</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.acTitle }</td>
				</tr>
				<tr> 
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Account Type</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.accounttypeName}</td>
				
					<td width="25%" height="16" align="left"  bgcolor="F3F3F3" class="formText">Agent Network</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.distributorName}</td>
				</tr>
				<tr>
                	<td colspan="8" align="center" bgcolor="FBFBFB">
						<h5>Account Relationship Information</h5>
					</td>
                </tr>
                <tr>	
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Purpose of Account.</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.accountPurpose }</td>
					
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Nature of Account</td>
					<td width="25%" height="16" align="left"  class="formText">Current</td>
				</tr>
				<tr>	
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Account Title</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.acTitle }</td>
					
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Currency</td>
					<td width="25%" height="16" align="left"  class="formText">PKR</td>
				</tr>
				<tr>	
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Tax Regime</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.taxRegimeName }</td>
					
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">FED</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.fed }</td>
				</tr>
				<tr>
					<td colspan="8" align="center" bgcolor="FBFBFB">
						<h5>Business Information</h5>
					</td>
				</tr>
				<tr>	
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Business Name</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.businessName}</td>
					
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Business Address</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.businessAddress}</td> 
				</tr>
				<tr>	
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">City</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.businessAddCityName}</td>
					
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Business Commencement Date</td>
					<td width="25%" height="16" align="left"  class="formText"><fmt:formatDate  type="both" 
            			dateStyle="short" timeStyle="short" value="${level3AccountModel.commencementDate}" /></td> 
				</tr>
				<tr>	
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Postal Code</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.businessPostalCode}</td>
					
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Business Inc.Date</td>
					<td width="25%" height="16" align="left"  class="formText"><fmt:formatDate  type="both" 
            			dateStyle="short" timeStyle="short" value="${level3AccountModel.incorporationDate}" /></td> 
				</tr>
				<tr>	
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">SECP Reg. Number</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.secpRegNo}</td>
					
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">SECP Reg. Date</td>
					<td width="25%" height="16" align="left"  class="formText"><fmt:formatDate  type="both" 
            			dateStyle="short" timeStyle="short" value="${level3AccountModel.secpRegDate}" /></td> 
				</tr>
				<tr>	
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">NTN Number</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.ntn}</td>
				</tr>
				<tr>
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Sales Tax Number</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.salesTaxRegNo}</td>
					
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Membership # of CC/Trade</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.membershipNoTradeBody}</td> 
				</tr>
				<tr>	
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Trade Body</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.tradeBody}</td>
					
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Business Type</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.businessTypeName}</td> 
				</tr>
				<tr>	
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Business Nature</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.businessNatureName}</td>
					
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Location Type</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.locationTypeName}</td> 
				</tr>
				<tr>	
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Location Size</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.locationSizeName}</td>
					
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Established Since</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.estSince}</td> 
				</tr>
				<tr>	
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Corresspondance Address</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.corresspondanceAddress}</td>
					
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">City</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.corresspondanceAddCityName}</td> 
				</tr>
				<tr>	
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Postal Code</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.corresspondancePostalCode}</td>
					
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Phone Number</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.landLineNo}</td> 
				</tr>
				<tr>
					<td colspan="8" align="center" bgcolor="FBFBFB">
						<h5>Personal Information</h5>
					</td>
				</tr>
				<tr>	
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Title</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.applicant1DetailModel.titleTxt}</td>
					
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Gender</td>
					<td width="25%" height="16" align="left"  class="formText">
						<c:if test="${level3AccountModel.applicant1DetailModel.gender=='M'}">
							Male
						</c:if>
						<c:if test="${level3AccountModel.applicant1DetailModel.gender=='F'}">
							Female 
						</c:if>
						<c:if test="${level3AccountModel.applicant1DetailModel.gender=='K'}">
							Khwaja Sira
						</c:if>
					</td>
				</tr>
				<tr>
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Applicant Name</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.applicant1DetailModel.name}</td>
				</tr>
				<tr>	
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">ID Document Type</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.applicant1DetailModel.idTypeName}</td>
					
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">ID Document Number</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.applicant1DetailModel.idNumber}</td> 
				</tr>
				<tr>
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Date of ID Expiry</td>
					<td width="25%" height="16" align="left"  class="formText"><fmt:formatDate  type="both" 
            			dateStyle="short" timeStyle="short" value="${level3AccountModel.applicant1DetailModel.idExpiryDate}" /></td>
            			
            		<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Date of Visa Expiry</td>
					<td width="25%" height="16" align="left"  class="formText"><fmt:formatDate  type="both" 
            			dateStyle="short" timeStyle="short" value="${level3AccountModel.applicant1DetailModel.visaExpiryDate}" /></td>
				</tr>
				<tr>
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Date of Birth</td>
					<td width="25%" height="16" align="left"  class="formText"><fmt:formatDate  type="both" 
            			dateStyle="short" timeStyle="short" value="${level3AccountModel.applicant1DetailModel.dob}" /></td>
            			
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Place of Birth</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.applicant1DetailModel.birthPlaceName }</td>
				</tr>
				<tr>
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">NTN</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.applicant1DetailModel.ntn }</td>
					
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Mother's Maiden Name</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.applicant1DetailModel.motherMaidenName }</td>
				</tr>
				<tr>
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Residential Status</td>
					<td width="25%" height="16" align="left"  class="formText">
						<c:if test="${level3AccountModel.applicant1DetailModel.residentialStatus == 1}">
							Resident
						</c:if>
						<c:if test="${level3AccountModel.applicant1DetailModel.residentialStatus == 0}">
							Non-Resident
						</c:if>
					</td>
					
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">US Citizen</td>
					<td width="25%" height="16" align="left"  class="formText">
						<c:if test="${level3AccountModel.applicant1DetailModel.usCitizen}">Yes</c:if>
						<c:if test="${!level3AccountModel.applicant1DetailModel.usCitizen}">No</c:if> 
						</td>
				</tr>
				<tr>
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Nationality</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.applicant1DetailModel.nationality}</td>
					
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Phone Number</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.applicant1DetailModel.contactNo }</td>
				</tr>
				<tr>
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Home Number</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.applicant1DetailModel.landLineNo}</td>
					
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Fax</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.applicant1DetailModel.fax}</td>
				</tr>
				<tr>
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Mobile Number</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.applicant1DetailModel.mobileNo}</td>
					
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Email</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.applicant1DetailModel.email}</td>
				</tr>
				<tr>
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Father/Husband Name</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.applicant1DetailModel.fatherHusbandName}</td>
					
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Name of Employeer/Business</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.applicant1DetailModel.employerName}</td>
				</tr>
				<tr>
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Occupation</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.applicant1DetailModel.occupationName}</td>
					
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Profession</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.applicant1DetailModel.professionName}</td>
				</tr>
				<tr>
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Mailing Address</td>
					<td width="25%" height="16" align="left"  class="formText">
						<c:if test="${level3AccountModel.applicant1DetailModel.mailingAddressType == 1}">
							Residence
						</c:if>
						<c:if test="${level3AccountModel.applicant1DetailModel.mailingAddressType == 0}">
							Office/Business
						</c:if>
					</td>
				</tr>
				<tr>
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Business Address</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.applicant1DetailModel.buisnessAddress }</td>
					
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Business City</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.applicant1DetailModel.buisnessCityName }</td>
				</tr>
				<tr>
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Residential Address</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.applicant1DetailModel.residentialAddress }</td>
					
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Residential City</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.applicant1DetailModel.residentialCityName }</td>
				</tr>
				<tr>
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Marital Status</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.applicant1DetailModel.maritalStatusName}</td>
				</tr>
				<tr>
					<td colspan="8" align="center" bgcolor="FBFBFB">
						<h5>Next of KIN:</h5>
					</td>
				</tr>
				<tr>
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Next of KIN</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.nokDetailVOModel.nokName }</td>
					
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Relationship with KIN</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.nokDetailVOModel.nokRelationship}</td>
				</tr>
				<tr>
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Telephone/Mobile Number of KIN</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.nokDetailVOModel.nokMobile }</td>
					
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">ID Document Type</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.nokDetailVOModel.nokIdTypeName}</td>
				</tr>
				<tr>
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">ID Document Number</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.nokDetailVOModel.nokIdNumber }</td>
					
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Next of KIN Address</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.nokDetailVOModel.nokMailingAdd}</td>
				</tr>
				<tr>
					<td colspan="8" align="center" bgcolor="FBFBFB">
						<h5>Relationship Details:</h5>
					</td>
				</tr>
				<tr>
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Name of Employee</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.employeeName }</td>
					
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Emp ID</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.employeeID}</td>
				</tr>
				<tr>
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Date of Account Opening</td>
					<td width="25%" height="16" align="left"  class="formText"><fmt:formatDate  type="both" dateStyle="short" timeStyle="short" value="${level3AccountModel.accountOpeningDate}" /></td>
					
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">BVS Enable</td>
					<td width="25%" height="16" align="left"  class="formText">${level3AccountModel.bvsEnable}</td>
				</tr>
				<tr>	
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Nadra Verification</td>
					<td width="25%" height="16" align="left"  class="formText">
						<c:if test="${level3AccountModel.verisysDone}">
							Positive
						</c:if>
						<c:if test="${not level3AccountModel.verisysDone}">
							Negative
						</c:if>
					</td>
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Screening Performed</td>
					<td width="25%" height="16" align="left"  class="formText">
						<c:if test="${level3AccountModel.applicant1DetailModel.screeningPerformed}">
							Match
						</c:if>
						<c:if test="${not level3AccountModel.applicant1DetailModel.screeningPerformed}">
							Not Match
						</c:if>
					</td>
				</tr>
			</table> 
			
			<h3 class="header" id="historyHeader">Action Authorization History </h3>
			<div class="eXtremeTable">
			<table class="tableRegion" width="100%">	
			<tr class="odd">
				<td class="titleRow" style="width: 90px; font-weight: bold" >Escalation Level</td>
				<td class="titleRow" style="width: 90px; font-weight: bold">Authorization Status</td>
				<td class="titleRow" style="width: 90px; font-weight: bold">Checked By</td>
				<td class="titleRow" style="width: 90px; font-weight: bold">Checked On</td>
				<td class="titleRow" style="width: 90px; font-weight: bold">Authorizer Comments</td>
				<td class="titleRow" style="width: 90px; font-weight: bold">Intimated On</td>
				<td class="titleRow" style="width: 90px; font-weight: bold">Intimated To</td>
			</tr>
			<c:forEach items="${actionAuthorizationHistoryModelList}" var="actionHistory"  varStatus="status">  
			<tr class="odd">
				<td>${actionHistory.escalationLevel}</td>
				<td>${actionHistory.relationActionStatusIdActionStatusModel.name }</td>
				<td>${actionHistory.relationCheckedByAppUserModel.username }</td>
				<td><fmt:formatDate  type="both" 
            			dateStyle="short" timeStyle="short" value="${actionHistory.checkedOn}" /></td>
				<td>${actionHistory.checkerComments}</td>
				<td><fmt:formatDate  type="both" 
            			dateStyle="short" timeStyle="short" value="${actionHistory.intimatedOn}" /></td>
				<td>${actionHistory.intimatedTo}</td>
			</tr>	
			</c:forEach>
		    </table> 
		 </div>   
		</c:otherwise>
	</c:choose>	
	
	</body>
</html>  	