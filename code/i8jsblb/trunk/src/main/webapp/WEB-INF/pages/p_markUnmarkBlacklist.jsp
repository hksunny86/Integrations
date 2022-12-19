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
		
	<html:form name="appUserMobileHistoryForm" commandName="appUserMobileHistoryModel" method="post" action="p_cnichistory.html">
		<input type="hidden" name="isUpdate" id="isUpdate" value="true" />
		<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_UPDATE%>" />
		<input type="hidden" name="appUserId" value="${param.appUserId}">
		<input type="hidden" name="customerId" value="${customerId}" />
		<input type="hidden" name="agentId" value="${agentId}" />
		<input type="hidden" name="handlerId" value="${handlerId}" />
		<input type="hidden" name="mfsId" value="${mfsId}" />
		
		
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


	</body>
</html>