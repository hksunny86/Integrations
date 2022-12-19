<!--Title: i8Microbank-->
<!--Author: Abu Turab Munir-->
<%@page import="com.inov8.microbank.common.util.PortalConstants"%>
<%@include file="/common/taglibs.jsp"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<c:set var="actionId"><%=PortalConstants.KEY_ACTION_ID %></c:set>
<c:set var="updateAction"><%=PortalConstants.ACTION_UPDATE %></c:set>
<html>

	<head>
	<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
	<c:if test="${not empty customerId}">
		<meta name="title" content="Update Customer CNIC No." />
	</c:if>
	<c:if test="${not empty agentId}">
		<meta name="title" content="Update Agent CNIC No." />
	</c:if>
	<c:if test="${not empty handlerId}">
		<meta name="title" content="Update Handler CNIC No." />
	</c:if>
	<meta name="decorator" content="decorator">
	<style type="text/css">
		table{clear:both;}
	</style>
	<script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
	<!-- turab added below for jquery -->
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.11.0.js"></script>
    <link rel="stylesheet" href="${contextPath}/styles/jquery-ui-custom.min.css" type="text/css">
    <script type="text/javascript" src="${contextPath}/scripts/jquery-ui-custom.min.js"></script>
	<script>
	var jq = $.noConflict();
	jq(document).ready(
		function($) {
			
			jq("#description").keyup(function(){
			    if($(this).val().length > 250){
			        $(this).val($(this).val().substr(0, 249));
			}
			});
			
	});
	
	function validateDescription(){
		
		var description = document.getElementById("description").value;
		
		if(description != null && description != undefined && description.length > 255){
			alert("\'Comments\' box can not have more than 255 characters.");
			return false;
		}else{
			return true;
		}
	
	}
	
	</script>
	</head>

	<body bgcolor="#ffffff">
	<spring:bind path="appUserMobileHistoryModel.*">
	  <c:if test="${not empty status.errorMessages}">
	    <div class="errorMsg">
	      <c:forEach var="error" items="${status.errorMessages}">
	        <c:out value="${error}" escapeXml="false" />
	        <br/>
	      </c:forEach>
	    </div>
	  </c:if>
	</spring:bind>
	<c:if test="${not empty messages}">
	    <div class="infoMsg" id="successMessages">
	        <c:forEach var="msg" items="${messages}">
	            <c:out value="${msg}" escapeXml="false"/><br />
	        </c:forEach>
	    </div>
	    <c:remove var="messages" scope="session"/>
	</c:if>
	<table width="100%" border="0" cellpadding="0" cellspacing="1">
			<tr>
			<c:if test="${not empty customerId}">
				<td class="formText" align="right" bgcolor="F3F3F3">Customer ID:</td>
				<td align="left" bgcolor="FBFBFB"><input type="text" value="${customerId}" disabled="disabled" class="textBox" style="background: #D3D3D3"/></td>
			</c:if>
			<c:if test="${not empty agentId}">
				<td class="formText" align="right" bgcolor="F3F3F3">Agent ID:</td>
				<td align="left" bgcolor="FBFBFB"><input type="text" value="${agentId}" disabled="disabled" class="textBox" style="background: #D3D3D3"/></td>
			</c:if>
			<c:if test="${not empty handlerId}">
				<td class="formText" align="right" bgcolor="F3F3F3">Handler ID:</td>
				<td align="left" bgcolor="FBFBFB"><input type="text" value="${mfsId}" disabled="disabled" class="textBox" style="background: #D3D3D3"/></td>
			</c:if>
			</tr>
			<tr>
				<td class="formText" align="right" bgcolor="F3F3F3">Current CNIC No:</td>
				<td align="left" bgcolor="FBFBFB"><input maxlength="13" type="text" value="${param.nic}" id="oldNic" disabled="disabled" class="textBox" style="background: #D3D3D3"/></td>
			</tr>
			<tr>
				<td class="formText" align="right" bgcolor="F3F3F3"><span style="color: #FF0000">*</span>New CNIC No:</td>
				<td align="left" bgcolor="FBFBFB"><input maxlength="13" tabindex="1" type="text" value="${newCnic}" id="newCnic" class="textBox" onkeypress="return maskInteger(this,event)"/></td>
			</tr>
	<html:form name="appUserMobileHistoryForm" commandName="appUserMobileHistoryModel" method="post" action="p_cnichistory.html" onsubmit="return validateForm(this);">
		<input type="hidden" name="isUpdate" id="isUpdate" value="true" />
		<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_UPDATE%>" />
		<input type="hidden" name="appUserId" value="${param.appUserId}">
		<input type="hidden" name="customerId" value="${customerId}" />
		<input type="hidden" name="agentId" value="${agentId}" />
		<input type="hidden" name="handlerId" value="${handlerId}" />
		<input type="hidden" name="mfsId" value="${mfsId}" />
		<input type="hidden" name="oldNic" value="${param.nic}" />
		
		<input type="hidden" name="actionAuthorizationId" value="${param.authId}" />
        <input type="hidden" name="resubmitRequest" value="${param.isReSubmit}" />
		
		<c:if test="${not empty customerId}">
			<input type="hidden" name="appUserIdAppUserModel.appUserTypeId" value="2">
			<html:hidden path="<%=PortalConstants.KEY_USECASE_ID %>" value="<%=PortalConstants.UPDATE_CUSTOMER_ID_DOC_NO_USECASE_ID %>" />
		</c:if>
		<c:if test="${not empty agentId}">
			<input type="hidden" name="appUserIdAppUserModel.appUserTypeId" value="3">
			<html:hidden path="<%=PortalConstants.KEY_USECASE_ID %>" value="<%=PortalConstants.UPDATE_AGENT_ID_DOC_NO_USECASE_ID %>" />
		</c:if>
		<c:if test="${not empty mfsId}">
			<input type="hidden" name="appUserIdAppUserModel.appUserTypeId" value="12">
			<html:hidden path="<%=PortalConstants.KEY_USECASE_ID %>" value="<%=PortalConstants.UPDATE_HANDLER_ID_DOC_NO_USECASE_ID %>" />
		</c:if>
			<tr>
				<td class="formText" align="right" bgcolor="F3F3F3"><span style="color: #FF0000">*</span>Confirm New CNIC No:</td>
				<td align="left" bgcolor="FBFBFB"><html:input tabindex="2" maxlength="13" id="cnfrmNIC" path="nic" cssClass="textBox" onkeypress="return maskInteger(this,event)" /></td>
			</tr>
			<tr>
				<td class="formText" align="right" bgcolor="F3F3F3">Comments:</td>
				<td align="left" bgcolor="FBFBFB"><html:textarea tabindex="3" rows="4" cols="21" path="description"/></td>
			</tr>
			<tr>
				<td class="formText" align="right"></td>
				<td align="left">
						<input name="_search" tabindex="4" type="submit" class="button" value="Update"/> 
						
				<c:if test="${not empty customerId}">
					<input name="reset" tabindex="5" type="reset" class="button" value="Cancel" onclick="javascript: document.location='p_mnomfsaccountdetails.html?appUserId=${param.appUserId}&actionId=2';document.getElementById('newCnic').value=''" />
				</c:if>
				<c:if test="${not empty agentId}">
					<input name="reset" tabindex="5" type="reset" class="button" value="Cancel" onclick="javascript: document.location='allpayRetailerAccountDetails.html?appUserId=${param.appUserId}&retailerContactId=${param.retailerContactId}&actionId=2';document.getElementById('newCnic').value=''" />
				</c:if>
				<c:if test="${not empty handlerId}">
					<input name="reset" tabindex="5" type="reset" class="button" value="Cancel" onclick="javascript: document.location='p_handleraccountdetails.html?appUserId=${param.appUserId}&handlerId=${param.handlerId}&actionId=2';document.getElementById('newCnic').value=''" />
				</c:if>		
						
				</td>
			</tr>
	</html:form>
	</table>

		<div class="eXtremeTable">
			<table class="tableRegion" width="100%" cellspacing="0">
				<tr>
					<td class="tableHeader"><b>CNIC No.</b></td>
					<td class="tableHeader"><b>Updated On</b></td>
					<td class="tableHeader"><b>Comments</b></td>
				</tr>
				<c:forEach items="${mobileHistoryList}" var="mobileHistory" varStatus="i">
					<c:set var="rowCssClass" value="even"/>
			   		<c:if test="${i.count%2!=0}">
		   				<c:set var="rowCssClass" value="odd" scope="page"/>
	   				</c:if>
					<tr class="${rowCssClass}">
						<td>${mobileHistory.nic}</td>
						<td><fmt:formatDate value="${mobileHistory.fromDate}" type="both" pattern="dd/MM/yyyy hh:mm a" /></td>
						<td>${mobileHistory.description}</td>
					</tr>			
				</c:forEach>
			</table>	
<script language="javascript" type="text/javascript">
	highlightFormElements();
	function validateForm(theForm) {
	    if( validateDescription() && doRequired(document.getElementById("newCnic"), 'New CNIC No.') && doRequired( theForm.nic, 'Confirm New CNIC No.' ) ) {
	    	if(checkCNIC(theForm.nic)){
	    		if(document.getElementById("cnfrmNIC").value != document.getElementById("newCnic").value) {
	       			alert("Confirm New CNIC value does not match with New CNIC ");
	        		return false;
	        	} else if (document.getElementById("newCnic").value == document.getElementById("oldNic").value){
	        		alert("This is your existing CNIC Please provide a New CNIC ");
	        		return false;
	      		} else {
	      			return true;
	      		}
	    	}
	    }
	    return false;
	}
	        
	function checkCNIC(number) {
		if (/^[0-9]{13}$/.test(number.value)) {
    		return true;
    	}else{
    		alert("CNIC format is incorrect.\nCNIC length should be of 13 digits.");
    		return false;
    	}
	}
			
	function doRequired( field, label ) {
		if( field.value == '' || field.value.length == 0 ) {
      		alert( label + ' is required field.' );
      		return false;
    	}
    	return true;
	}	
	</script>
	</body>
</html>