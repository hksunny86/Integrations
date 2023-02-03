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
		
		<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css" type="text/css"/>
		<meta name="title" content="Create New Agent Authorization Detail"/>
		
	<script type="text/javascript">
			
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
							<html:form  name = "actionDetailForm" id = "actionDetailForm" commandName="actionAuthorizationModel" action="p-createagentauthorizationdetailform.html" method="post" onsubmit="return validate()">
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
						        			<input name="modify" type="button" class="button" value=" Modify " onclick="window.opener.open('addAgent.jsf?isReSubmit=true&authId='+${actionAuthorizationModel.actionAuthorizationId}+'&actionId=1','_blank');window.close();"/>
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
			<h3 class="header" id="detailHeader">Agent Details</h3>
			
			<table>
			
				<tr> 		
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Agent Network Name</td>
					<td width="25%" height="16" align="left" class="formText">${retailerFormModel.distributorName}</td>
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Product Catalog</td>
					<td width="25%" height="16" align="left" class="formText">${retailerFormModel.productCatalogName}</td>										
				</tr>
				<tr>
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Region</td>
					<td width="25%" height="16" align="left"  class="formText">${retailerFormModel.regionName}</td>
			
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Sale User </td>
					<td width="25%" height="16" align="left"  class="formText">${retailerFormModel.saleUserName}</td>
				</tr>
				<tr>
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Franchise/Branch</td>
					<td width="25%" height="16" align="left"  class="formText">${retailerFormModel.retailerName}</td>
			
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Comments</td>
					<td width="25%" height="16" align="left"  class="formText">${retailerFormModel.comments}</td>
			
				</tr>
				<tr>
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Agent Levels</td>
					<td width="25%" height="16" align="left"  class="formText">${retailerFormModel.distributorLevelName}</td>
				
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Parent Agents</td>
					<td width="25%" height="16" align="left"  class="formText">${retailerFormModel.parentRetailerContactName}</td>
		
				</tr>
				<tr>
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Area Name</td>
					<td width="25%" height="16" align="left"  class="formText">${retailerFormModel.areaName}</td>
			
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Account Type</td>
					<td width="25%" height="16" align="left"  class="formText">${retailerFormModel.accountType}</td>
		
				</tr>	
				<tr>
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Agent Name</td>
					<td width="25%" height="16" align="left"  class="formText">${retailerFormModel.name}</td>
				
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Active</td>
					<td width="25%" height="16" align="left"  class="formText">${retailerFormModel.active}</td>
				
				</tr>
				<tr>
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">First Name</td>
					<td width="25%" height="16" align="left"  class="formText">${retailerFormModel.firstName}</td>
				
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Last Name</td>
					<td width="25%" height="16" align="left"  class="formText">${retailerFormModel.lastName}</td>
		
				</tr>	
				<tr>
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">User Name</td>
					<td width="25%" height="16" align="left"  class="formText">${retailerFormModel.username}</td>
				
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">User Group</td>
					<td width="25%" height="16" align="left"  class="formText">${retailerFormModel.partnerGroupName}</td>
			
				</tr>
				<tr>
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Application No</td>
					<td width="25%" height="16" align="left"  class="formText">${retailerFormModel.applicationNo}</td>
			
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">MSISDN</td>
					<td width="25%" height="16" align="left"  class="formText">${retailerFormModel.mobileNo}</td>
			
				</tr>	
				<tr>
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Account Openning</td>
					<td width="25%" height="16" align="left"  class="formText"><fmt:formatDate  type="both" 
            			dateStyle="short" timeStyle="short" value="${retailerFormModel.accountOpeningDate}"/></td>
				
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">NTN No</td>
					<td width="25%" height="16" align="left"  class="formText">${retailerFormModel.ntnNo}</td>
			
				</tr>
				<tr>
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">CNIC</td>
					<td width="25%" height="16" align="left"  class="formText">${retailerFormModel.cnicNo}</td>
				
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">CNIC Expiry</td>
					<td width="25%" height="16" align="left"  class="formText"><fmt:formatDate  type="both" 
            			dateStyle="short" timeStyle="short" value="${retailerFormModel.cnicExpiryDate}"/></td>
				
				</tr>
				<tr>
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Contact No</td>
					<td width="25%" height="16" align="left"  class="formText">${retailerFormModel.contactNo}</td>
				
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Mobile No</td>
					<td width="25%" height="16" align="left"  class="formText">${retailerFormModel.zongMsisdn}</td>
				
				</tr>
				<tr>			
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Email</td>
					<td width="25%" height="16" align="left"  class="formText">${retailerFormModel.email}</td>
					
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Nature of Business</td>
					<td width="25%" height="16" align="left"  class="formText">${retailerFormModel.natureOfBusinessName}</td>
			
				</tr>
				<tr>
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Shop No</td>
					<td width="25%" height="16" align="left"  class="formText">${retailerFormModel.shopNo}</td>
		
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Market/Plaza</td>
					<td width="25%" height="16" align="left"  class="formText">${retailerFormModel.marketPlaza}</td>
				
				</tr>
				<tr>
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">District/Tehsil/Town</td>
					<td width="25%" height="16" align="left"  class="formText">${retailerFormModel.districtTehsilTownName}</td>
				
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">City/Village</td>
					<td width="25%" height="16" align="left"  class="formText">${retailerFormModel.cityVillageName}</td>
				
				</tr>
				<tr>
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Post office</td>
					<td width="25%" height="16" align="left"  class="formText">${retailerFormModel.postOfficeName}</td>
				
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">NTN No</td>
					<td width="25%" height="16" align="left"  class="formText">${retailerFormModel.ntnNumber}</td>
			
				</tr>
				<tr>
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Neares Landmark</td>
					<td width="25%" height="16" align="left"  class="formText">${retailerFormModel.nearestLandmark}</td>
				
					<td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">Description</td>
					<td width="25%" height="16" align="left"  class="formText">${retailerFormModel.description}</td>
			
				</tr>
				<tr bgcolor="FBFBFB">
	             <td colspan="2" align="center">&nbsp;</td>
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
            			dateStyle="short" timeStyle="short" value="${actionHistory.checkedOn}"/></td>
				<td>${actionHistory.checkerComments}</td>
				<td><fmt:formatDate  type="both" 
            			dateStyle="short" timeStyle="short" value="${actionHistory.intimatedOn}"/></td>
				<td>${actionHistory.intimatedTo}</td>
			</tr>	
			</c:forEach>
		    </table> 
		 </div>   
		</c:otherwise>
	</c:choose>	
	
	</body>
</html>  	