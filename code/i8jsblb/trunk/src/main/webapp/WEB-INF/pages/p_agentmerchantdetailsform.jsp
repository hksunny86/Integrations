<!--Author: Muhammad Atif Hussain-->
<%@page import="org.springframework.web.bind.ServletRequestUtils"%>
<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="expires" content="0" />
<meta name="decorator" content="decorator2">
	<meta http-equiv="pragma" content="no-cache" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta name="title" content="Agent/Merchant Details Form" />
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css" />
	<%--<link href="http://code.jquery.com/ui/1.9.1/themes/base/jquery-ui.css" rel="stylesheet" type="text/css" />--%>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/prototype.js"></script>
	<%@include file="/common/ajax.jsp"%>
<%--	<script type="text/javascript" src="http://code.jquery.com/jquery-1.8.2.js"></script>
	<script type="text/javascript" src="http://code.jquery.com/ui/1.9.1/jquery-ui.js"></script>--%>
	<script type="text/javascript">
	
		function error(request) {
			alert("An unknown error has occured. Please contact with the administrator for more details");
		}
		
		function passwordChanged(){
          if(document.forms[0].isPasswordChanged != undefined){
              document.forms[0].isPasswordChanged.value = true;
          }
      }
      
		function agentTypeChange(){
		  	if(document.forms[0].isCreatedFranchise != undefined && document.forms[0].isCreatedFranchise){
				if(document.getElementById('isHead').value=='true'){
						alert("Agent type can not be changed to Head Agent");
						document.getElementById('agentTypeId').value = "";
						return false;
				}
			}
		}
		
		function deleteHiddenAgentTypeLevelId(){
		  	
			 var elem = document.getElementById('distributorLevelId');
			 if(elem!=undefined){
				 elem.parentNode.removeChild(elem);
			 }
			 return false;
		}
		
		
		
		function checkHeadAgent(){
			//turab CR May 10, 2018
			if('${agentMerchantDetailModel.isHead}'=='true' && document.getElementById('isHead').value!='true' && document.getElementById('actionId').value==3)
			{
				/*alert("Direct Agent cannot be moved to Sub Agent Level")
				document.getElementById('agentTypeId').value ="";
				return false;*/

				alert("Direct Agent is now going to be a Sub Agent\n please select Parent Agent.");
			    document.getElementById("directChangedToSub").value="true";
			}
			
			if('${agentMerchantDetailModel.isHead}'=='false' && document.getElementById('isHead').value!='false' && document.getElementById('actionId').value==3)
			{
				alert("Sub Agent cannot be moved to Direct Agent level")
				document.getElementById('agentTypeId').value ="";
                document.getElementById("directChangedToSub").value="false";
				return false;
			}
			
			if(document.getElementById('isHead').value=='true'){
				document.getElementById('parentAgentIdRow').style.display = "none";
                document.getElementById("directChangedToSub").value="false";
				document.forms.agentMerchantDetailForm.parentAgentId.value ="";
	           	document.forms.agentMerchantDetailForm.ultimateParentAgentId.value ="";
	           	document.forms.agentMerchantDetailForm.parentAgentName.value ="";
	           	document.forms.agentMerchantDetailForm.ultimateParentAgentName.value ="";
	           	
	           	if('${agentMerchantDetailModel.initialAppFormNo}'==null || '${agentMerchantDetailModel.initialAppFormNo}'=='')//Dont Set incase of update 	
				{
					document.getElementById('retailerId').value = "-2";
					document.getElementById('partnerGroupId').value = "-2";
                    document.getElementById("directChangedToSub").value="false";
				}
				
				/* if(document.forms[0].retailerId.value == '' ||document.forms[0].retailerId.value == '-2' || document.forms[0].retailerId.value.length == 0){
				
					partnerGroupSelect = document.getElementById('partnerGroupId');
 					partnerGroupSelect.options[partnerGroupSelect.options.length] = new Option("[AUTO_GENERATED]", "-2");
					document.getElementById('partnerGroupId').value = "-2";
					document.getElementById('partnerGroupId').disabled = "true";
					
					retailerSelect = document.getElementById('retailerId');
 					retailerSelect.options[retailerSelect.options.length] = new Option("[AUTO_GENERATED]", "-2");
					document.getElementById('retailerId').value = "-2";
					document.getElementById('retailerId').disabled = "true";
				} */
			}else{
				document.getElementById('parentAgentIdRow').style.display = "";
				
				document.forms.agentMerchantDetailForm.parentAgentId.value ="";
	           	document.forms.agentMerchantDetailForm.ultimateParentAgentId.value ="";
	           	document.forms.agentMerchantDetailForm.retailerId.value ="";
	        	document.forms.agentMerchantDetailForm.partnerGroupId.value ="";
	           	document.forms.agentMerchantDetailForm.parentAgentName.value ="";
	           	document.forms.agentMerchantDetailForm.ultimateParentAgentName.value ="";
				/* partnerGroupSelect = document.getElementById('partnerGroupId');
				document.getElementById('partnerGroupId').disabled = "";
				partnerGroupSelect.removeChild( partnerGroupSelect.options[partnerGroupSelect.options.length-1] );
				
				retailerSelect = document.getElementById('retailerId');
				document.getElementById('retailerId').disabled = "";
				retailerSelect.removeChild( retailerSelect.options[retailerSelect.options.length-1] ); */
			}
		}
		
		function checkHeadAgentOnLoad(){
			/* if(document.getElementById('isError').value && document.getElementById('parentAgentId').value == ""){
				document.getElementById('isHead').value = 'true';
			}
 */
			if(document.getElementById('isHead').value=='true'){
				document.getElementById('parentAgentIdRow').style.display = "none";
				document.getElementById('parentAgentId').value = "";
				
				if('${agentMerchantDetailModel.initialAppFormNo}'==null || '${agentMerchantDetailModel.initialAppFormNo}'=='')//Dont Set incase of update 	
				{
					document.getElementById('retailerId').value = "-2";
					document.getElementById('partnerGroupId').value = "-2";
				}
					
				/* if(document.forms[0].retailerId.value == '' || document.forms[0].retailerId.value.length == 0){
				
					partnerGroupSelect = document.getElementById('partnerGroupId');
 					partnerGroupSelect.options[partnerGroupSelect.options.length] = new Option("[AUTO_GENERATED]", "-2");
					document.getElementById('partnerGroupId').value = "-2";
					document.getElementById('partnerGroupId').disabled = "true";
					
					retailerSelect = document.getElementById('retailerId');
 					retailerSelect.options[retailerSelect.options.length] = new Option("[AUTO_GENERATED]", "-2");
					document.getElementById('retailerId').value = "-2";
					document.getElementById('retailerId').disabled = "true";
				} */
			}else{
				document.getElementById('parentAgentIdRow').style.display = "";
				
				/* partnerGroupSelect = document.getElementById('partnerGroupId');
				document.getElementById('partnerGroupId').disabled = "";
				
				retailerSelect = document.getElementById('retailerId');
				document.getElementById('retailerId').disabled = ""; */
			}
		}
		
	function initProgress(){
		if(document.forms[0].empId.value == ''){
    		alert('Emp Id is mandatory for Name Fetch');
    		return false;
    	}
		
		var empIdValue = document.forms[0].empId.value;
    	
		var charpos = empIdValue.search("[^0-9]"); 
		if(charpos >= 0) { 
			alert('Employee Id must be numeric.');
			return false; 
		}		
		
    	$('errorMessages').innerHTML = "";
    	Element.hide('errorMessages');
    	return true;
	}
	
	function endProgress(){
		if(document.forms[0].empName.value == 'null' || document.forms[0].empName.value == ''){
			document.forms[0].empName.value = '';
			Element.show('errorMessages');
			$('errorMessages').innerHTML = "User is not associated with any sale hierarchy";
		}
		
		if(document.forms[0].errMsg.value == 'null'){
			document.forms[0].errMsg.value = '';
		}
	}
	
	function resetEmployeeName(){
		document.forms[0].empName.value = '';
	}
	
	</script>

	<style>
.handler_table {
	font-size: 13px;
	text-align: center;
	background-color: #0292e0;
	padding: 7px;
	color: white;
}
</style> 


<%
	String readPermission = PortalConstants.ADMIN_GP_READ + "," + PortalConstants.PG_GP_READ + "," +PortalConstants.MNG_L3_KYC_READ+ "," +PortalConstants.MNG_L3_ACT_READ;
	String updatePermission = PortalConstants.ADMIN_GP_READ + "," + PortalConstants.PG_GP_READ + "," +PortalConstants.MNG_L3_AMDF_UPDATE;
	String createPermission = PortalConstants.ADMIN_GP_READ + "," + PortalConstants.PG_GP_READ + "," +PortalConstants.MNG_L3_AMDF_CREATE;

%>


</head>
<body bgcolor="#ffffff">
	<div id="successMsg" class="infoMsg" style="display: none;"></div>
	<spring:bind path="agentMerchantDetailModel.*">
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
	<div class="infoMsg" id="errorMessages" style="display:none;"></div>
	<html:form name="agentMerchantDetailForm" commandName="agentMerchantDetailModel" method="POST" action="p_agentmerchantdetailsform.html"
		onsubmit="return validateForm(this)">
		<c:if test="${not empty agentMerchantDetailModel.agentMerchantDetailId}">
			<html:hidden path="isPasswordChanged" />
		</c:if>
		<html:hidden path="agentMerchantDetailId" id="agentMerchantDetailId"/>
		<html:hidden path="isHead" value="${agentMerchantDetailModel.isHead}"/>
		<html:hidden path="versionNo"/>
		<html:hidden path="createdOn"/>
		<html:hidden path="updatedOn"/>
		<html:hidden path="createdBy"/>
		<html:hidden path="updatedBy"/>
		<html:hidden path="employeeAppUserModel.appUserId" id="empAppUserId"/>
		<html:hidden path="parentAgentId"/>
		<html:hidden path="ultimateParentAgentId"/>
		<html:hidden path="retailerId"/>
		<html:hidden path="partnerGroupId" />
		<html:hidden path="password"/>
		<html:hidden path="confirmPassword"/>
		<html:hidden path="error" id="isError"/>
		<html:hidden path="isRegionChangeAllowed" />
		<html:hidden path="directChangedToSub"/>
		
		<c:if test="${not empty agentMerchantDetailModel.agentMerchantDetailId}">
			<input type="hidden" name="isUpdate" id="isUpdate" value="true" />
			<input type="hidden" id="<%=PortalConstants.KEY_ACTION_ID%>"  name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_UPDATE%>" />
		</c:if>
		<c:if test="${empty agentMerchantDetailModel.agentMerchantDetailId}">
			<input type="hidden" id="<%=PortalConstants.KEY_ACTION_ID%>" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_CREATE%>" />
		</c:if>
		<table>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>Initial Application Form No.:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input path="initialAppFormNo" tabindex="1" cssClass="textBox" onkeypress="return maskAlphaNumericWithSpdashforAH(this,event)" maxlength="16" readonly="${not empty agentMerchantDetailModel.agentMerchantDetailId}"/></td>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Reference No.:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input path="referenceNo" cssClass="textBox" onkeypress="return maskAlphaNumericWithSp(this,event)" maxlength="16" tabindex="2" /></td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>Business Name:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input path="businessName" cssClass="textBox" maxlength="50" tabindex="1"/></td>
				
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>Agent Network:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
					
					<c:if test="${!agentMerchantDetailModel.isRegionChangeAllowed}">
						<html:hidden path="distributorId"/>
					</c:if>
					
					<html:select id="distributorId" tabindex="3" path="distributorId" cssClass="textBox" disabled="${!agentMerchantDetailModel.isRegionChangeAllowed}">
						<html:option value="">[Select]</html:option>
						<c:if test="${distributorModelList != null}">
							<html:options items="${distributorModelList}" itemLabel="name" itemValue="distributorId" />
						</c:if>
					</html:select>
				</td>
			</tr>
			<tr style="height: 70px;">
				<td colspan="4"><h2>Agent/Merchant Details</h2></td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>Employee Id:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
					<html:input path="employeeAppUserModel.employeeId" id="empId" maxlength="7" cssClass="textBox" onkeypress="return maskInteger(this,event)" onchange="resetEmployeeName();"  tabindex="5"/>
					<input id="fetchButton" name="fetchButton" type="button" value="Fetch Name" class="button"	 />
				</td>
				
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>Name of Employee:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input path="empName" id="empName" onfocus="this.blur()" readonly="true" cssClass="textBox" maxlength="100" tabindex="4"/></td>

			</tr>
			<tr>
				
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>Relationship Type:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:select id="agentRelationshipTypeId"
						path="productCatalogModel.productCatalogId" cssClass="textBox" tabindex="6">
					<html:option value="">[Select]</html:option>
						<c:if test="${agentRelationshipTypeModelList != null}">
							<html:options items="${agentRelationshipTypeModelList}" itemLabel="name" itemValue="productCatalogId" />
						</c:if>
					</html:select>
				</td>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"></td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"></td>
				
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>Region:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
					
					<c:if test="${!agentMerchantDetailModel.isRegionChangeAllowed}">
						<html:hidden path="regionId"/>
					</c:if>
					
					<html:select id="regionId"  path="regionId" cssClass="textBox" tabindex="8" disabled="${!agentMerchantDetailModel.isRegionChangeAllowed}">
						<html:option value="">[Select]</html:option>
						<c:if test="${regionList != null}">
							<html:options items="${regionList}" itemLabel="regionName" itemValue="regionId" />
						</c:if>
					</html:select>
				</td>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>Agent Type:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%">

						<%-- <c:if test="${agentMerchantDetailModel.isHead=='true'}">
							<html:hidden path="distributorLevelId" />
						</c:if> --%>
						<html:select id="agentTypeId" path="distributorLevelId" cssClass="textBox" tabindex="7" >
							<html:option value="">[Select]</html:option>
							<c:if test="${agentTypeModelList != null}">
								<html:options items="${agentTypeModelList}" itemLabel="name" itemValue="distributorLevelId" />
							</c:if>
						</html:select> 
					</td>
				</tr>
				<tr id="parentAgentIdRow">
					<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>Parent Agent:</td>
					<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
						<html:input id="parentAgentName" path="parentAgentIdRetailerContactModel.name" cssClass="textBox" maxlength="50" tabindex="-1" readonly="true"/>
						<input id="fetchParent" name="fetchParent" type="button" value="+" class="button" onclick="popupwindow();"/>						
					</td>
					<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Ultimate Parent Agent:</td>
					<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
						<html:input id="ultimateParentAgentName" path="ultimateParentAgentIdRetailerContactModel.name" cssClass="textBox" maxlength="50" tabindex="-1" readonly="true"/>
					</td>
				</tr>
				<%-- <td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>Franchise/Branch:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
					<c:if test="${agentMerchantDetailModel.isHead=='false'}">
						 <html:select id="retailerId" tabindex="9"
							path="retailerId" cssClass="textBox" cssStyle="word-wrap: break-word;">
							<html:option value="">[Select]</html:option>
							<c:if test="${retailerModelList != null}">
								<html:options items="${retailerModelList}" itemLabel="name" itemValue="retailerId" />
							</c:if>
						</html:select> 
					</c:if>
					<c:if test="${agentMerchantDetailModel.isHead=='true'}">
						<html:hidden path="retailerId" id="retailerId"/>
						<html:select tabindex="9" disabled="true" cssStyle="word-wrap: break-word;width: 175px !important; height: 100% !important;"
							path="retailerId" id="retailerId_dis" cssClass="textBox" >
							<html:option value="">[Select]</html:option>
							<c:if test="${retailerModelList != null}">
								<html:options items="${retailerModelList}" itemLabel="name" itemValue="retailerId" />
							</c:if>
						</html:select>
						
					</c:if>
				</td>
			</tr>--%>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>Area Level:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
					<html:select id="areaLevelId" path="areaLevelId"
						cssClass="textBox" tabindex="11">
						<html:option value="">[Select]</html:option>
						<c:if test="${areaLevelList != null}">
							<html:options items="${areaLevelList}" itemLabel="areaLevelName" itemValue="areaLevelId" />
						</c:if>
					</html:select>
				</td>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>Area Name:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
					<html:select id="areaId" path="areaId"
						cssClass="textBox" tabindex="10">
						<html:option value="">[Select]</html:option>
						<c:if test="${areaList != null}">
							<html:options items="${areaList}" itemLabel="name" itemValue="areaId" />
						</c:if>
					</html:select>
				</td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Tehsil/Town:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input path="town" cssClass="textBox" maxlength="50" tabindex="13" onkeypress="return maskAlphaNumericWithSp(this,event)"/></td>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">UC/Ward:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input path="uc" cssClass="textBox" maxlength="50" tabindex="14" onkeypress="return maskAlphaNumericWithSp(this,event)"/></td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Mohalla/Sector:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input path="sector" cssClass="textBox" maxlength="50" tabindex="15" onkeypress="return maskAlphaNumericWithSp(this,event)"/></td>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>Account Level Qualification:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:select id="acLevelQualificationId"  tabindex="16"
						path="acLevelQualificationId" cssClass="textBox" >
						<html:option value="">[Select]</html:option>
						<c:if test="${acLevelQualificationModelList != null}">
							<html:options items="${acLevelQualificationModelList}" itemLabel="label" itemValue="value" />
						</c:if>
					</html:select></td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>Username:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
					<html:input path="userName"  cssClass="textBox" maxlength="50" tabindex="17"/>
				</td>
			</tr>
			<%--
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>User Group:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
						<c:if test="${agentMerchantDetailModel.isHead=='false'}">
							<html:select id="partnerGroupId" tabindex="18"
								path="partnerGroupId" cssClass="textBox" cssStyle="word-wrap: break-word;">
								<html:option value="">[Select]</html:option>
								<c:if test="${partnerGroupModelList != null}">
									<html:options items="${partnerGroupModelList}" itemLabel="name" itemValue="partnerGroupId" />
								</c:if>
							</html:select>
						</c:if>
						<c:if test="${agentMerchantDetailModel.isHead=='true'}">
							<html:select tabindex="18" disabled="true"
								path="partnerGroupId" id="partnerGroupId_dis" cssClass="textBox" cssStyle="word-wrap: break-word;width: 175px !important; height: 100% !important;">
								<html:option value="">[Select]</html:option>
								<c:if test="${partnerGroupModelList != null}">
									<html:options items="${partnerGroupModelList}" itemLabel="name" itemValue="partnerGroupId" />
								</c:if>
							</html:select>
							<html:hidden path="partnerGroupId" id="partnerGroupId"/>
						</c:if>	
				</td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>Password:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:password id="password" path="password" tabindex="19" cssClass="textBox" maxlength="54" showPassword="true" onchange="javascript: passwordChanged();"/></td>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>Confirm Password:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:password id="confirmPassword" tabindex="20" path="confirmPassword" cssClass="textBox" maxlength="54" showPassword="true" onchange="javascript: passwordChanged();"/></td>
			</tr>--%>
			<tr align="center">
				<td colspan="4">
					<c:if test="${not empty agentMerchantDetailModel.agentMerchantDetailId}">
								
						<authz:authorize ifAnyGranted="<%=updatePermission%>">
							<input id="addBtn" type="submit" value="Update" tabindex="21"/>
						</authz:authorize>
						<authz:authorize ifNotGranted="<%=updatePermission%>">
							<authz:authorize ifAnyGranted="<%=readPermission%>">
								<input type="button" class="button" value="Goto KYC" name="KYCForm${agentMerchantDetailModel.initialAppFormNo}"  
							onclick="javascript:document.location='p_l3_kyc_form.html?actionId=1&initAppFormNumber=${agentMerchantDetailModel.initialAppFormNo}'" />			
							</authz:authorize>
							<authz:authorize ifNotGranted="<%=readPermission%>">
								<input type="button" class="button" value="Goto KYC" name="KYCForm${agentMerchantDetailModel.initialAppFormNo}" disabled="disabled"/>			
							</authz:authorize>	
						</authz:authorize>
					</c:if>
					<c:if test="${empty agentMerchantDetailModel.agentMerchantDetailId}">
						<authz:authorize ifAnyGranted="<%=createPermission%>">
							<input id="addBtn" type="submit" value="Save" tabindex="21"/>							
						</authz:authorize>
						<authz:authorize ifNotGranted="<%=createPermission %>">
							<input id="addBtn" type="submit" value="Save" disabled="disabled" tabindex="21"/>
						</authz:authorize>
					</c:if>
				
					<input type="reset" class="button" tabindex="22"  value="Cancel"
						onclick="javascript:window.location='home.html'"/>
				</td>
			</tr>
		</table>
	</html:form>
	<ajax:updateField parser="new ResponseXmlParser()" source="empId" action="fetchButton" target="empName,empAppUserId,errMsg" baseUrl="${contextPath}/fetchsaleusername.html" parameters="empId={empId}" preFunction="initProgress" postFunction="endProgress" ></ajax:updateField>
	<ajax:select source="distributorId" target="regionId" baseUrl="${contextPath}/p_regionrefdata.html"
		parameters="distributorId={distributorId},actionId=${retriveAction}" errorFunction="error"/>
	<ajax:select source="regionId" target="areaLevelId" baseUrl="${contextPath}/p_arealevelrefdata.html"
		parameters="regionId={regionId},actionId=${retriveAction}" errorFunction="error"/>
	<ajax:select source="areaLevelId" target="areaId" baseUrl="${contextPath}/p_areanamerefdata.html"
		parameters="areaLevelId={areaLevelId},actionId=${retriveAction}" errorFunction="error"/>
	<ajax:select source="regionId" target="agentTypeId" baseUrl="${contextPath}/p_agentlevelrefdata.html"
		parameters="regionId={regionId},actionId=${retriveAction}" errorFunction="error" postFunction="deleteHiddenAgentTypeLevelId"/>	
	<%-- <ajax:select source="retailerId" target="parentAgentId" baseUrl="${contextPath}/p_parentagentrefdata.html"
		parameters="agentLevelId={agentTypeId},retailerId={retailerId},actionId=${retriveAction}" errorFunction="error" postFunction="agentTypeChange"/> --%>
	<%-- <ajax:select source="agentTypeId" target="retailerId" baseUrl="${pageContext.request.contextPath}/p_retailerrefdata.html"
		parameters="regionId={regionId},agentLevelId={agentTypeId},actionId=${retriveAction}" errorFunction="error"/>	 --%>
	<ajax:updateField parser="new ResponseXmlParser()" source="agentTypeId" eventType="change" action="agentTypeId" target="isHead" baseUrl="${contextPath}/p_checkheadagent.html" parameters="agentLevelId={agentTypeId}" postFunction="checkHeadAgent" ></ajax:updateField>
	<%-- <ajax:select source="retailerId" target="parentAgentId" baseUrl="${contextPath}/p_parentagentrefdata.html"
		parameters="agentLevelId={agentTypeId},retailerId={retailerId},actionId=${retriveAction}" errorFunction="error" />	 --%>
	<%-- <ajax:select source="regionId" target="retailerId"
		baseUrl="${pageContext.request.contextPath}/p_retailerrefdata.html"
		parameters="regionId={regionId}" errorFunction="error"/> --%>
	<%-- <ajax:select source="retailerId" target="partnerGroupId"
		baseUrl="${pageContext.request.contextPath}/p_partnergrouprefdata.html"
		parameters="retailerId={retailerId}" errorFunction="error"/> --%>
	<script type="text/javascript">
		
		function doRequired(field, label) {
			if (field.value.trim() == '' || field.value.length == 0) {
				alert(label + ' is required field.');
				return false;
			}
			return true;
		}
		
		function popupwindow(url, title, w, h) {
			var url = 'p-parentagentpopupwindow.html?agentLevelId='+document.forms.agentMerchantDetailForm.agentTypeId.value+'&retailerId=${agentMerchantDetailModel.retailerId}';
			var title = 'Parent Agents';
			var w = 800;
			var h= 400;
			
			var left = (screen.width / 2) - (w / 2);
			var top = (screen.height / 2) - (h / 2);
			return window
					.open(
							url,
							title,
							'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes, copyhistory=no, width='
									+ w
									+ ', height='
									+ h
									+ ', top='
									+ top
									+ ', left=' + left);
		}
		function popupCallback(parentName,parentRetailerId,ultimateAgentName,ultimateAgentId,retailerId)
        {  
			document.forms.agentMerchantDetailForm.parentAgentId.value =parentRetailerId;
           	document.forms.agentMerchantDetailForm.ultimateParentAgentId.value =ultimateAgentId;
           	document.forms.agentMerchantDetailForm.retailerId.value =retailerId;
           	document.forms.agentMerchantDetailForm.parentAgentName.value =parentName;
           	document.forms.agentMerchantDetailForm.ultimateParentAgentName.value =ultimateAgentName;
        }

		function validateForm(theForm) {
			if (doRequired(theForm.businessName, 'Business Name')
					&& doRequired(theForm.initialAppFormNo, 'Initial Application Form No')
					//&& doRequired(theForm.distributorId, 'Agent Network')
					&& doRequired(theForm.empName, 'Name of Employee')
					&& doRequired(theForm.empId, 'Employee Id')
					&& doRequired(theForm.agentRelationshipTypeId, 'Relationship Type')
					&& doRequired(theForm.agentTypeId, 'Agent Type')
					//&& doRequired(theForm.regionId, 'Region')
					/* && doRequired(document.getElementById('retailerId'), 'Franchise/Branch') */
					&& doRequired(theForm.areaLevelId, 'Area Level')
					&& doRequired(theForm.areaId, 'Area Name')
					&& doRequired(theForm.acLevelQualificationId,'Account Level Qualification')
					&& doRequired(theForm.userName,'Username')
					/*&& doRequired(document.getElementById('partnerGroupId'),'User Group')
					&& doRequired(theForm.password,'Password')
					&& doRequired(theForm.confirmPassword,'Confirm Password') */) {
				
				if(document.getElementById('parentAgentIdRow').style.display != "none"){
					if(theForm.parentAgentId.value=="" || theForm.parentAgentName.value==""){
						alert('Please provide Parent Agent')
						return false;
					}
				}
				
				if((${agentMerchantDetailModel.isRegionChangeAllowed}) && (!doRequired(theForm.distributorId, 'Agent Network'))){
					return false;
				}
				
				if((${agentMerchantDetailModel.isRegionChangeAllowed}) && (!doRequired(theForm.regionId, 'Region'))){
					return false;
				}
				
				if(!isAlphanumeric(document.forms[0].initialAppFormNo)){
					alert("Special characters are not allowed for Initial Application Form No.");
					return false;
				}
				
				if(!isAlphanumeric(document.forms[0].referenceNo)){
					alert("Special characters are not allowed for Reference No.");
					return false;
				}
				
				/* if(!isAlphanumericWithSpace(document.forms[0].businessName)){
					alert("Special characters are not allowed for Business Name");
					document.forms[0].businessName.focus();
					return false;
				} */
				
				if(!isAlphanumericWithSpace(document.forms[0].town)){
					alert("Special characters are not allowed for Tehsil/Town");
					document.forms[0].town.focus();
					return false;
				}
				
				if(!isAlphanumericWithSpace(document.forms[0].uc)){
					alert("Special characters are not allowed for UC/Ward");
					document.forms[0].uc.focus();
					return false;
				}
				
				if(!isAlphanumericWithSpace(document.forms[0].sector)){
					alert("Special characters are not allowed for Mohalla/Sector");
					document.forms[0].sector.focus();
					return false;
				}
				
				/* if(!validateFormChar(theForm)){
		      		return false;
		      	} */
				

			} else {
				return false;
			}
		}
		window.onload = checkHeadAgentOnLoad();
	</script>
</body>
</html>
