<!--Title: i8Microbank-->
<!--Author: Rizwan Munir-->
<%@page import="com.inov8.microbank.common.util.PortalConstants"%>
<%@include file="/common/taglibs.jsp"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<c:set var="actionId"><%=PortalConstants.KEY_ACTION_ID %></c:set>
<c:set var="updateAction"><%=PortalConstants.ACTION_UPDATE %></c:set>
<c:set var="i"><%=1 %></c:set>
<html>

	<head>
	<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
	<meta name="title" content="Retagging Mobile No/CNIC" />
	<meta name="decorator" content="decorator">
	<style type="text/css">
		table{clear:both;}
	</style>
	<script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
	<!-- turab added below for jquery -->
     <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.11.0.js"></script>
     <link rel="stylesheet" href="${contextPath}/styles/jquery-ui-custom.min.css" type="text/css">
     <script type="text/javascript" src="${contextPath}/scripts/jquery-ui-custom.min.js"></script>
<%--     <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/prototype.js"></script>
 --%>	<%@include file="/common/ajax.jsp"%>
     
	<script>
	var jq = $.noConflict();
	jq(document).ready(
		function($) {
			
			jq("#comments").keyup(function(){
			    if($(this).val().length > 250){
			        $(this).val($(this).val().substr(0, 249));
			}
			});
			
	});
	
	function validateDescription(){
		var description = document.getElementById("comments").value;
		if(description != null && description != undefined && description.length > 255){
			alert("\'Comments\' box can not have more than 255 characters.");
			return false;
		}else{
			return true;
		}
	
	}
	
	function error(request) {
		alert("An unknown error has occured. Please contact with the administrator for more details");
	}
	
	function endProgress(){
		/* if(document.forms[0].empAppUserName.value == 'null' || document.forms[0].empAppUserName.value == ''){
			document.forms[0].empAppUserName.value = '';
			document.forms[0].empAppUserModelId.value = '';
			Element.show('errorMessages');
			$('errorMessages').innerHTML = "User is not associated with any sale hierarchy";
		}
		
		if(document.forms[0].errMsg.value == 'null'){
			document.forms[0].errMsg.value = '';
		} */
		
		alert("inside ");
	}
	
	</script>
	</head>

	<body bgcolor="#ffffff">
	<spring:bind path="retagMobileCnicVo.*">
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
	<html:form name="retagMobileCnicVoForm" id="retagMobileCnicVoForm" commandName="retagMobileCnicVo" method="post" action="p_retagmobilecnic.html">
		<input type="hidden" name="isUpdate" id="isUpdate" value="true" />
		<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_UPDATE%>" />	
		<input type="hidden" name="<%=PortalConstants.KEY_USECASE_ID %>" value="<%=PortalConstants.UPDATE_HANDLER_MOBILE_USECASE_ID %>" />
				
		<input type="hidden" name="actionAuthorizationId" value="${param.authId}" />
        <input type="hidden" name="resubmitRequest" value="${param.isReSubmit}" />
        
        <html:hidden  path="appUserId" id="appUserId" />
        <html:hidden  path="pendingTrxExists" id="pendingTrxExists"/>
        <html:hidden  path="customerExits" id="customerExits"/>
        <html:hidden  path="walkinExists" id="walkinExists"/>
		<html:hidden  path="isCnicUpdate" id="isCnicUpdate"/>
		
		<tr>
			<td class="formText" align="right" bgcolor="F3F3F3"><span style="color: #FF0000">*</span>Current Mobile No:</td>
			<td align="left" bgcolor="FBFBFB"><html:input tabindex="0" maxlength="11" id="oldMobileNo" path="oldMobileNo" cssClass="textBox" style="background: #D3D3D3" onkeypress="return maskInteger(this,event)"  readonly="true"/></td>
			
			<td class="formText" align="right" bgcolor="F3F3F3"><span style="color: #FF0000">*</span>Current CNIC:</td>
			<td align="left" bgcolor="FBFBFB"><html:input tabindex="0" maxlength="11" id="oldCnic" path="oldCnic" cssClass="textBox" style="background: #D3D3D3" onkeypress="return maskInteger(this,event)" readonly="true"/></td>
		</tr>
		<tr>
			<td align="right" bgcolor="F3F3F3" class="formText"><span style="color: #FF0000">*</span>ReTag:</td>
			<td align="left" bgcolor="FBFBFB" class="text">

				<html:radiobutton id="retagMobile" name="retagMobile" value="0" path="isMobileUpdate"
										 onclick="disableEnableFileds('newMobileNo','newCnic')"/>CNIC No

				<html:radiobutton id="retagMobile" name="retagMobile" value="1" path="isMobileUpdate"
									  onclick="disableEnableFileds('newCnic', 'newMobileNo')"/>Mobile No
			</td>
			<td align="right" bgcolor="F3F3F3" class="formText"></td>
			<td align="left" bgcolor="FBFBFB" class="text"></td>
		</tr>
		<tr>
			<td class="formText" align="right" bgcolor="F3F3F3"><span style="color: #FF0000">*</span>New Mobile No:</td>
			<td align="left" bgcolor="FBFBFB"><html:input tabindex="1" maxlength="11" id="newMobileNo" path="newMobileNo" cssClass="textBox" onkeypress="return maskInteger(this,event)" /></td>
			
			<td class="formText" align="right" bgcolor="F3F3F3"><span style="color: #FF0000">*</span>New CNIC:</td>
			<td align="left" bgcolor="FBFBFB"><html:input tabindex="2" maxlength="13" id="newCnic" path="newCnic" cssClass="textBox" onkeypress="return maskInteger(this,event)" /></td>
		</tr>
		<tr>
			<td class="formText" align="right" bgcolor="F3F3F3">Comments:</td>
			<td align="left" bgcolor="FBFBFB"><html:textarea tabindex="3" rows="4" cols="21" path="comments" id="comments"/></td>
			<td class="formText" align="right" bgcolor="F3F3F3"></td>
			<td align="left" bgcolor="FBFBFB"></td>
		</tr>
		<tr>
			<td align="center" colspan="4">
				<input name="_search" tabindex="4" type="button" id="btnSubmit" class="button" value="Update"/> 	
				<input name="reset" tabindex="5" type="reset" class="button" value="Cancel" onclick="javascript: window.location='p_walkincustomers.html?actionId=${retriveAction}'" />		
			</td>
		</tr>
	</html:form>
	</table>

		<div class="eXtremeTable">
			<table class="tableRegion" width="100%" cellspacing="0">
				<tr>
					<td class="tableHeader"><b>Re-Tagging Date</b></td>
					<td class="tableHeader"><b>Re-Tagged By</b></td>
					<td class="tableHeader"><b>Old CNIC No.</b></td>
					<td class="tableHeader"><b>Old MSISDN</b></td>
					<td class="tableHeader"><b>New CNIC No.</b></td>
					<td class="tableHeader"><b>New MSISDN</b></td>
					<td class="tableHeader"><b>Pair Sequence</b></td>
				</tr>
				<% int  j=0; %>
				<c:forEach items="${retagMobileHistoryList}" var="mobileHistory" varStatus="i">
					<% j=j+1; %>
				</c:forEach>
				<c:forEach items="${retagMobileHistoryList}" var="mobileHistory" varStatus="i">
					<c:set var="rowCssClass" value="even"/>
			   		<c:if test="${i.count%2!=0}">
		   				<c:set var="rowCssClass" value="odd" scope="page"/>
	   				</c:if>
					<tr class="${rowCssClass}">
						<td><fmt:formatDate value="${mobileHistory.createdOn}" type="both" pattern="dd/MM/yyyy hh:mm a" /></td>
						<td>${mobileHistory.updatedByAppUserModel.firstName}</td>
						<td>${mobileHistory.currentNic}</td>
						<td>${mobileHistory.currentMobileNo}</td>
						<td>${mobileHistory.newNic}</td>
						<td>${mobileHistory.newMobileNo}</td>
						<td><%=j%></td>
					</tr>
					<% j=j-1; %>
				</c:forEach>
			</table>
		</div>		
<script language="javascript" type="text/javascript">
	highlightFormElements();
	
	function disableEnableFileds(dis, en) {
		_en = en;
		document.getElementById(dis).disabled = true;
		document.getElementById(dis).value="";
		document.getElementById(dis).style.background = "#D3D3D3";
		document.getElementById(en).disabled = false;
		document.getElementById(en).style.background  = "white";
	}
	
	function submitForm()
	{
		var theForm = document.getElementById('retagMobileCnicVoForm');
		
		if(theForm.customerExits.value=='true'){
			alert('BB User exits against provided Mobile/CNIC');
			return false;
		}
		else if(theForm.pendingTrxExists.value=='true'){	
			theForm.submit('Inprocess Transaction/s exists against provided Mobile/CNIC.');
			return false;
		}

		else  if(theForm.isCnicUpdate.value=='true'){
			alert('CNIC Already Exists.');
			return false;
		}

		else if(theForm.walkinExists.value=='true')
		{

			if(confirm('Walkin Customer already exists against provided Mobile/CNIC.\n Do you want to proceed?'))
			{
				theForm.submit();	
			}
		}
		else
		{
			if(confirm('Are you sure you want to proceed?')){
				theForm.submit();	
			}
		}
			
	}
	
	function validateForm() {
		var theForm = document.getElementById('retagMobileCnicVoForm');	 
		var isvalid = true;
	    
	    if(theForm.retagMobile.value=='')
	    {
	    	alert("ReTag option is not selected");
	    	return false;
	    }
	    
	    if(theForm.newMobileNo.value!='' || theForm.newCnic.value!=''){
	    	if((theForm.newMobileNo.value!='' && theForm.newCnic.value!='')){
	    		isvalid = false;
	    		alert("Both Mobile No and CNIC cannot be updated at same time.");
	    	}
	    	
	    	if(theForm.newMobileNo.value!='' && isvalid){
		    		
	    		if(!checkMobile(theForm.newMobileNo)){
	    			isvalid = false;	
	    		}
	    		
	    		if((theForm.newMobileNo.value==theForm.oldMobileNo.value) && isvalid)
	    		{
	    			alert("Current Mobile No cannot be used as New Mobile No");
	    			isvalid =false;
	    		}
	    	}
	    	
	    	if(theForm.newCnic.value!='' && isvalid){
	    		
	    		if(!checkCNIC(theForm.newCnic)){
	    			isvalid = false;	
	    		}
	    		
	    		if((theForm.newCnic.value==theForm.oldCnic.value) && isvalid)
	    		{
	    			alert("Current CNIC cannot be used as New CNIC");
	    			isvalid =false;
	    		}
	    	}
	    	
	    	if( !validateDescription() && isvalid){
	    		isvalid = false;
	    	}
	    }
	    else
	    {
	    	alert("Kindly provide New Mobile No or New CNIC");
	    	isvalid = false;
	    }
	     
	    return isvalid;
	        	
	}
	        
	function checkMobile(number) {
		if (/^(03)[0-9]{9}$/.test(number.value)) {
    		return true;
    	}else{
    		alert("Mobile No. format is incorrect.\nIt should start with 03XXXXXXXXX and lenght should be 11 digits.");
    		return false;
    	}
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
<%--			<script>
				$.ajax{
					function(){

						$('#abc').html(msg);
						$('#sbmit').hide();

					}

				}


			</script>--%>
	<ajax:updateField parser="new ResponseXmlParser()" source="btnSubmit" action="btnSubmit" target="pendingTrxExists,customerExits,walkinExists,isCnicUpdate,errMsg" baseUrl="${contextPath}/checkpendingwalkintrans.html" parameters="retagMobile={retagMobile},newMobileNo={newMobileNo},newCnic={newCnic},appUserId={appUserId}"  preFunction="validateForm" postFunction="submitForm" errorFunction="error"></ajax:updateField>

	</body>
</html>