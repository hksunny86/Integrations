<!--Title: i8Microbank-->
<!--Author: Rizwan Munir-->
<%@page import="com.inov8.microbank.common.util.PortalConstants"%>
<%@include file="/common/taglibs.jsp"%>
<c:set var="actionId"><%=PortalConstants.KEY_ACTION_ID %></c:set>
<c:set var="updateAction"><%=PortalConstants.ACTION_UPDATE %></c:set>
<html>

	<head>
	<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
	<meta name="title" content="Update Transaction Details" />
	<meta name="decorator" content="decorator">
	<style type="text/css">
		table{clear:both;}
	</style>
	<script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.11.0.js"></script>
    <link rel="stylesheet" href="${contextPath}/styles/jquery-ui-custom.min.css" type="text/css">
    <script type="text/javascript" src="${contextPath}/scripts/jquery-ui-custom.min.js"></script>
	<script type="text/javascript">
		var jq = $.noConflict();
		jq(document).ready(
			function($) {
				jq("#comments").keyup(
					function(){
			    		if($(this).val().length > 250){
			        		$(this).val($(this).val().substr(0, 249));
						}
					}
				);
			}
		);		
	</script>
	</head>

	<body bgcolor="#ffffff">
	<spring:bind path="p2pDetailModel.*">
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
	<html:form name="p2pDetailForm" commandName="p2pDetailModel" method="post" action="p_updatep2ptx.html" onsubmit="return validateForm(this);">
		<input type="hidden" name="actionAuthorizationId" value="${param.authId}" />
        <input type="hidden" name="resubmitRequest" value="${param.isReSubmit}" />
        <input type="hidden" name="usecaseId" value = "<%=PortalConstants.UPDATE_P2P_DETAILS_USECASE_ID %>"/>
		<input type="hidden" name="isUpdate" id="isUpdate" value="true" />
		<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_UPDATE%>" />
		<html:hidden path="senderCNICChanged" />
		<html:hidden path="senderMobileChanged" />
		<html:hidden path="recipientCNICChanged" />
		<html:hidden path="recipientMobileChanged" />
		<html:hidden path="transactionCode" />
		<html:hidden path="transactionAmount" />
		<html:hidden path="createdOn" />
		<html:hidden path="supProcessingStatusId" />
		<html:hidden path="processingStatusName" />
		<html:hidden path="agent1Id" />
		<html:hidden path="agent1MobileNo" />
		<html:hidden path="agent1Name" />
			<tr>
				<td class="formText" align="right" bgcolor="F3F3F3">Transaction ID:</td>
				<td align="left" bgcolor="FBFBFB"><input type="text" value="${p2pDetailModel.transactionCode}" disabled="disabled" class="textBox" style="background: #D3D3D3"/></td>
				<td class="formText" align="right" bgcolor="F3F3F3">Agent ID:</td>
				<td align="left" bgcolor="FBFBFB"><input type="text" value="${p2pDetailModel.agent1Id}" disabled="disabled" class="textBox" style="background: #D3D3D3"/></td>
			</tr>
			<tr>
				<td class="formText" align="right" bgcolor="F3F3F3">Agent Name:</td>
				<td align="left" bgcolor="FBFBFB"><input type="text" value="${p2pDetailModel.agent1Name}" disabled="disabled" class="textBox" style="background: #D3D3D3"/></td>
				<td class="formText" align="right" bgcolor="F3F3F3">Agent Mobile:</td>
				<td align="left" bgcolor="FBFBFB"><input type="text" value="${p2pDetailModel.agent1MobileNo}" disabled="disabled" class="textBox" style="background: #D3D3D3"/></td>
			</tr>
			<tr>
				<td class="formText" align="right" bgcolor="F3F3F3">Date:</td>
				<td align="left" bgcolor="FBFBFB">
					<div  class="textBox" style="background: #D3D3D3">
						<fmt:formatDate  type="both" dateStyle="short" timeStyle="short" value="${p2pDetailModel.createdOn}" pattern="dd/MM/yyyy hh:mm a" />
					</div>
				</td>
				<td class="formText" align="right" bgcolor="F3F3F3">Amount:</td>
				<td align="left" bgcolor="FBFBFB"><input type="text" value="${p2pDetailModel.transactionAmount}" disabled="disabled" class="textBox" style="background: #D3D3D3"/></td>
			</tr>
			
			
			<tr>
				<td class="formText" align="right" bgcolor="F3F3F3">Sender Mobile No:</td>
				<td align="left" bgcolor="FBFBFB"><html:input tabindex="1" maxlength="11" path="senderMobile" cssClass="textBox" onkeypress="return maskInteger(this,event)"/></td>
				<td class="formText" align="right" bgcolor="F3F3F3">Sender CNIC:</td>
				<td align="left" bgcolor="FBFBFB"><html:input tabindex="2" maxlength="13" path="senderCNIC" cssClass="textBox" onkeypress="return maskInteger(this,event)"/></td>
			</tr>
			<tr>
				<td class="formText" align="right" bgcolor="F3F3F3">Recipient Mobile No:</td>
				<td align="left" bgcolor="FBFBFB"><html:input tabindex="3" maxlength="11" path="recipientMobile" cssClass="textBox" onkeypress="return maskInteger(this,event)"/></td>
				<td class="formText" align="right" bgcolor="F3F3F3">Recipient CNIC:</td>
				<td align="left" bgcolor="FBFBFB"><html:input tabindex="4" maxlength="13" path="recipientCNIC" cssClass="textBox" onkeypress="return maskInteger(this,event)"/></td>
			</tr>
			<tr>
				<td class="formText" align="right" bgcolor="F3F3F3"><span style="color: #FF0000">*</span>Comments:</td>
				<td align="left" bgcolor="FBFBFB" colspan="3"><html:textarea tabindex="3" rows="4" cols="21" path="comments"/></td>
			</tr>
			<tr>
				<td class="formText" align="right">&nbsp;</td>
				<td align="left">
					<input name="_search" tabindex="4" type="submit" class="button" value="Update"/> 
					<input name="reset" tabindex="5" type="reset" class="button" value="Cancel" onclick="javascript: document.location='p_updatep2ptxreport.html'" />
				</td>
			</tr>
	</html:form>
	</table>
	
	<div class="eXtremeTable">
		<table class="tableRegion" width="100%" cellspacing="0">
			<tr>
				<td class="tableHeader"><b>Sender Mobile No</b></td>
				<td class="tableHeader"><b>Sender CNIC</b></td>
				<td class="tableHeader"><b>Recipient Mobile No</b></td>
				<td class="tableHeader"><b>Recipient CNIC</b></td>
				<td class="tableHeader"><b>Updated On</b></td>
				<td class="tableHeader"><b>Updated By</b></td>
				<td class="tableHeader"><b>Comments</b></td>
			</tr>
			<c:forEach items="${p2pDetailModelList}" var="p2pDetailModel" varStatus="i">
				<c:set var="rowCssClass" value="even"/>
			   	<c:if test="${i.count%2!=0}">
		   			<c:set var="rowCssClass" value="odd" scope="page"/>
	   			</c:if>
				<tr class="${rowCssClass}">
					<td>${p2pDetailModel.senderMobile}</td>
					<td>${p2pDetailModel.senderCNIC}</td>
					<td>${p2pDetailModel.recipientMobile}</td>
					<td>${p2pDetailModel.recipientCNIC}</td>
					<td><fmt:formatDate value="${p2pDetailModel.createdOn}" type="both" pattern="dd/MM/yyyy hh:mm a" /></td>
					<td>${p2pDetailModel.updatedByName}</td>
					<td>${p2pDetailModel.comments}</td>
				</tr>			
			</c:forEach>
		</table>
	</div>
	
<script language="javascript" type="text/javascript">

	function validateForm(theForm) {
	    if(	validateMobileNo( theForm.senderMobile, 'Sender Mobile No.' )
	    	&& validateCNIC( theForm.senderCNIC, 'Sender CNIC' )
	    	&& validateMobileNo( theForm.recipientMobile, 'Recipient Mobile No.' )
	    	&& validateCNIC( theForm.recipientCNIC, 'Recipient CNIC' )
	    	&& doRequired( theForm.comments, 'Comments' )) {
	    	
	    	if(theForm.senderMobile.value == theForm.recipientMobile.value){
        		alert("Sender and Recipient Mobile No. cannot be the same");
        		theForm.senderMobile.focus();
        		return false;
	    	}
	    	if(theForm.senderCNIC.value == theForm.recipientCNIC.value){
        		alert("Sender and Recipient CNIC cannot be the same");
        		theForm.senderCNIC.focus();
        		return false;
	    	}
	    	
	    	var valueChanged = false;
	    	var oldSenderMobile = "<c:out value='${p2pDetailModel.senderMobile}' />";
	    	if(theForm.senderMobile.value != oldSenderMobile){
	    		theForm.senderMobileChanged.value = true;
	    		valueChanged = true;
	    	}
	    	var oldSenderCNIC = "<c:out value='${p2pDetailModel.senderCNIC}' />";
	    	if(theForm.senderCNIC.value != oldSenderCNIC){
	    		theForm.senderCNICChanged.value = true;
	    		valueChanged = true;
	    	}
	    	var oldRecipientMobile = "<c:out value='${p2pDetailModel.recipientMobile}' />";
	    	if(theForm.recipientMobile.value != oldRecipientMobile){
	    		theForm.recipientMobileChanged.value = true;
	    		valueChanged = true;
	    	}
	    	var oldRecipientCNIC = "<c:out value='${p2pDetailModel.recipientCNIC}' />";
	    	if(theForm.recipientCNIC.value != oldRecipientCNIC){
	    		theForm.recipientCNICChanged.value = true;
	    		valueChanged = true;
	    	}

	    	
	    	if(	valueChanged != true){
        		alert("At least one field must be updated before submitting the form");
        		theForm.senderMobile.focus();
        		return false;
	    	}
	    	
			return true;
	    }
	    return false;
	}

	function validateMobileNo(field, label){
    	
    	if(doRequired(field, label) && isValidMinLength(field, label, 11)){
			var mobileNumber = field.value;
        	if(mobileNumber.charAt(0)!="0" || mobileNumber.charAt(1)!="3"){
        		alert(label +" should start from 03XXXXXXXXX");
        		field.focus();
        		return false;
        	}else{
        		return true;
        	}
 		}
        return false;
	}

	function validateCNIC(field, label){
    	if(doRequired(field, label) && isValidMinLength(field, label, 13)){
    		return true;
    	}
        return false;
	}
	        
	function checkMobile(number, label) {
		if (/^(03)[0-9]{9}$/.test(number.value)) {
    		return true;
    	}else{
    		alert(label +" format is not correct.");
    		return false;
    	}
	}
	
	function isValidMinLength( field, label, minlength ){
      	if( field.value != '' && field.value.length < minlength ){
      		alert( label + ' cannot be less than ' + minlength + ' digits' );
      		return false;
      	}
    	return true;
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