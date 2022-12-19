
<!--Author: Mohammad Shehzad Ashraf-->
<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE%></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="decorator">
<%@include file="/common/ajax.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/prototype.js"></script>
<script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css" />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<c:choose>
	<c:when test="${not empty param.appUserId or not empty appUserId}">
		<meta name="title" content="Edit User" />
	</c:when>
	<c:otherwise>
		<meta name="title" content="Add User" />
	</c:otherwise>
</c:choose>
<script language="javascript" type="text/javascript">
      	var jq=$.noConflict();
	      function error(request)
	      {
	      	alert("An unknown error has occured. Please contact with the administrator for more details");
	      }
	      function loadPartnerGroup()
	      {
	      	var obj = new AjaxJspTag.Select("${contextPath}/partnerTypeFormRefDataController.html", {
			parameters: "appUserTypeId={appUserTypeId},actionType=2,id={partnerId}",target: "partnerGroupId",
			source: "partnerId",errorFunction: error});	
			obj.execute();
			onChangeAppUserType();
	      }
	      var isEmployeeIdRequired = true;
	      var isTellerIdRequired = true;	      
	      
	      function onChangeAppUserType() {
	    	  
	    	  var userTypeId =  document.getElementById('appUserTypeId').value;
	    	  
	    	  if(userTypeId==1){
	    		  isEmployeeIdRequired = true;
	    		  document.getElementById('employeeIdMandatory').style.display = '';
	    	  }	else if(userTypeId==6){
	    		  isEmployeeIdRequired = true;
	    		  isTellerIdRequired = false;
	    		  //isTellerIdRequired = true;
	    		  document.getElementById('employeeIdMandatory').style.display = '';
	    		  //document.getElementById('tellerIdMandatory').style.display = '';
	    	  }
	    	  else{
	    		  isEmployeeIdRequired = false;
	    	      isTellerIdRequired = false;	
	    		  document.getElementById('employeeIdMandatory').style.display = 'none';
	    		  //document.getElementById('tellerIdMandatory').style.display = 'none';
	    	  }
	      }
	      
	      var str =null;	      
	      
      </script>
</head>
<body bgcolor="#ffffff">
	<spring:bind path="userManagementModel.*">
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
		<html:form name="userManagementForm" commandName="userManagementModel" onsubmit="return onFormSubmit(this)" action="p_i8userformtest.html" >
			<c:if test="${not empty param.appUserId or not empty appUserId}">
				<input type="hidden" name="isUpdate" id="isUpdate" value="true" />
			</c:if>
			<c:if test="${not empty param.appUserId }">
				<input type="hidden" name="appUserId" value="${param.appUserId}" />
			</c:if>
			<c:if test="${not empty appUserId}">
				<input type="hidden" name="appUserId" value="${appUserId}" />
			</c:if>
			<input type="hidden" name="actionAuthorizationId" value="${param.authId}" />
			<input type="hidden" name="resubmitRequest" value="${param.isReSubmit}" />
			<html:hidden path="<%=PortalConstants.KEY_USECASE_ID%>" />
			<html:hidden path="<%=PortalConstants.KEY_ACTION_ID%>" />
			<html:hidden path="appUserType" />
			<html:hidden path="mobileType" />
			<html:hidden path="partnerGroup" />
			<html:hidden path="partnerType" />
			<tr bgcolor="FBFBFB">
				<td colspan="2" align="center">&nbsp;</td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>User Type:</td>
				<td align="left" bgcolor="FBFBFB">
					<html:select path="appUserTypeId" cssClass="textBox" tabindex="1" disabled="${not empty param.appUserId or not empty appUserId}">
						<html:options items="${appUserTypeModelList}" itemLabel="name" itemValue="appUserTypeId" />
					</html:select>
				</td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Partner:</td>
				<td width="58%" align="left" bgcolor="FBFBFB"><c:choose>
						<c:when test="${not empty partnerRefDataList }">
							<html:select path="partnerId" cssClass="textBox" tabindex="2"  disabled="${not empty param.appUserId or not empty appUserId}">
								<html:options items="${partnerRefDataList}" itemLabel="nameField" itemValue="idField" />
							</html:select>
						</c:when>
						<c:otherwise>
							<html:select path="partnerId" cssClass="textBox" tabindex="2">
							</html:select>
						</c:otherwise>
					</c:choose></td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Partner Group:</td>
				<td width="58%" align="left" bgcolor="FBFBFB"><c:choose>
						<c:when test="${not empty partnerGroupModelList }">
							<html:select path="partnerGroupId" cssClass="textBox" tabindex="2">
								<html:options items="${partnerGroupModelList}" itemLabel="name" itemValue="partnerGroupId" />
							</html:select>
						</c:when>
						<c:otherwise>
							<html:select path="partnerGroupId" cssClass="textBox" tabindex="2">
							</html:select>
						</c:otherwise>
					</c:choose></td>
			</tr>
			<tr bgcolor="FBFBFB">
				<td align="right" bgcolor="F3F3F3" class="formText"><span id="employeeIdMandatory" style="color: #FF0000">*</span>Employee ID:</td>
				<td align="left"><html:input path="employeeId" tabindex="3" cssClass="textBox" maxlength="7" onkeypress="return maskInteger(this,event)" /></td>
			</tr>
			<tr bgcolor="FBFBFB">
				<td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>First Name:</td>
				<td align="left"><html:input path="firstName" tabindex="5" cssClass="textBox" maxlength="50" onkeypress="return maskAlphaWithSp(this,event)" /></td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Last Name:</td>
				<td width="58%" align="left" bgcolor="FBFBFB"><html:input path="lastName" cssClass="textBox" tabindex="6" maxlength="50"
						onkeypress="return maskAlphaWithSp(this,event)" /></td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>User ID:</td>
				<td align="left" bgcolor="FBFBFB"><html:input path="userId" tabindex="7" cssClass="textBox" maxlength="20"
						onkeypress="return maskAlphaNumericWithPeriodForUserName(this,event)" /></td>
			</tr>
			<tr>
				<td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Password:</td>
				<td align="left" bgcolor="FBFBFB"><html:password path="password" showPassword="true" cssClass="textBox" tabindex="8" maxlength="50"
						onchange="javascript: passwordChanged();" /></td>
			</tr>
			<tr>
				<td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Confirm Password:</td>
				<td align="left" bgcolor="FBFBFB"><html:password path="confirmPassword" showPassword="true" cssClass="textBox" tabindex="9" maxlength="50"
						onchange="javascript: passwordChanged();" /></td>
			</tr>
			<!--
			<tr bgcolor="FBFBFB" id="tellerIdMandatory">
				<td align="right" bgcolor="F3F3F3" class="formText">Teller ID:</td>
				<td align="left"><html:input path="tellerId" tabindex="4" cssClass="textBox" maxlength="20" /></td>
			</tr>
			-->
			<tr>
				<td align="right" bgcolor="F3F3F3" class="formText">E-mail:</td>
				<td align="left" bgcolor="FBFBFB"><html:input path="email" cssClass="textBox" tabindex="10" maxlength="50" /></td>
			</tr>
			<tr>
				<td align="right" bgcolor="F3F3F3" class="formText">DOB:</td>
				<td align="left" bgcolor="FBFBFB"><html:input path="dob" cssClass="textBox" tabindex="11" maxlength="50"
						onkeypress="return maskdate2(this,event)" /> <img id="calButton" tabindex="12" readonly="true" name="popcal" align="top"
					style="cursor:pointer" src="images/cal.gif" border="0" /> <img id="dobD" tabindex="13" name="popcal" title="Clear Date" align="middle"
					onclick="javascript:$('dob').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" /> <label class="formText">dd/mm/yyyy</label></td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000; display: none">*</span>Mobile #:</td>
				<td align="left" bgcolor="FBFBFB"><html:input path="mobileNo" cssClass="textBox" tabindex="14" maxlength="11"
						onkeypress="return maskNumber(this,event)" /></td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText">Active:</td>
				<td width="58%" align="left" bgcolor="FBFBFB"><html:checkbox path="isActive" tabindex="15" />
			</tr>
			<tr>
				<td height="16" colspan="2" align="right" bgcolor="FBFBFB" class="formText">&nbsp;</td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="FBFBFB" class="formText"></td>
				<td width="58%" align="left" bgcolor="FBFBFB"><c:choose>
						<c:when test="${not empty param.appUserId or not empty appUserId}">
							<security:isauthorized action="<%=PortalConstants.ACTION_UPDATE%>">
								<jsp:attribute name="ifAccessAllowed">
							<input type="button" class="button" value="Update" tabindex="16" onclick="javascript:submitForm(document.forms.userManagementForm,null);"
										<c:if test="${disable ne null}">disabled="disabled"</c:if> />						
						</jsp:attribute>
								<jsp:attribute name="ifAccessNotAllowed">
							<input type="button" class="button" value="Update" tabindex="16" disabled="disabled" />						
						</jsp:attribute>
							</security:isauthorized>
							<input type="button" class="button" value="Cancel" tabindex="16"
								onclick="javascript:window.location='p_i8usermanagement.html?actionId=${retriveAction}';" />
						</c:when>
						<c:otherwise>
							<security:isauthorized action="<%=PortalConstants.ACTION_CREATE%>">
								<jsp:attribute name="ifAccessAllowed">
						      <input type="button" style="width='140px'" class="button" value="Create New User" tabindex="15"
										onclick="javascript:submitForm(document.forms.userManagementForm,null);" />
						</jsp:attribute>
								<jsp:attribute name="ifAccessNotAllowed">
					      <input type="button" style="width='140px'" class="button" value="Create New User" tabindex="-1" disabled="disabled" />
						</jsp:attribute>
							</security:isauthorized>
							<input type="button" class="button" value="Cancel" tabindex="16"
								onclick="javascript:window.location='p_i8usermanagement.html?actionId=${retriveAction}'" />
						</c:otherwise>
					</c:choose></td>
			</tr>
			<tr bgcolor="FBFBFB">
				<td colspan="2" align="center">&nbsp;</td>
			</tr>
			<c:if test="${not empty param.appUserId or not empty appUserId}">
				<html:hidden path="isPasswordChanged" />
			</c:if>
			<tr>
				<td>
		</html:form>
	</table>
	<c:choose>
		<c:when test="${not empty param.appUserId or not empty appUserId}">
			<form action="${contextPath}/p_pgsearchuserinfo.html" method="post" name="userInfoListViewForm" id="userInfoListViewModel">
				<input type="hidden" value="<c:out value="${appUserModel.searchMfsId}"/>" id="userId" name="userId" />
				<input type="hidden" value="<c:out value="${appUserModel.searchFirstName}"/>" id="firstName" name="firstName" />
				<input type="hidden" value="<c:out value="${appUserModel.searchLastName}"/>" id="lastName" name="lastName" />
				<input type="hidden" value="<c:out value="${appUserModel.searchNic}"/>" id="nic" name="nic" />
			</form>
		</c:when>
		<c:otherwise>
		</c:otherwise>
	</c:choose>
	<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE%></c:set>
	<ajax:select source="appUserTypeId" target="partnerId" baseUrl="${contextPath}/p_i8UsersRefData.html"
		parameters="appUserTypeId={appUserTypeId},actionId=${retriveAction}" postFunction="loadPartnerGroup" errorFunction="error" />
	<ajax:select source="partnerId" target="partnerGroupId" baseUrl="${contextPath}/partnerTypeFormRefDataController.html"
		parameters="appUserTypeId={appUserTypeId},actionType=2,id={partnerId}" errorFunction="error" />
	<script language="javascript" type="text/javascript">
      
      function passwordChanged(){
          if(document.forms[0].isPasswordChanged != undefined){
              //alert('passwordChanged is not Undefined!');
              document.forms[0].isPasswordChanged.value = true;
          }
      }
      
      highlightFormElements();
      <c:choose>
		<c:when test="${not empty param.appUserId or not empty appUserId}">
          function disableFields(){
              document.forms[0].appUserTypeId.readOnly = true;    
              document.forms[0].userId.readOnly = true;
              document.forms[0].partnerId.readOnly = true;
          }
          disableFields();
          document.userManagementForm.firstName.focus();
        </c:when>
        <c:otherwise>
	      function onCancel(a, b)
          {
             document.forms[0].reset();
          }
          document.userManagementForm.appUserTypeId.focus();
      	</c:otherwise>
      </c:choose>
      
      
      
      
      function submitForm(theForm)
      {	
      	if(!isFormDataChanged())
      	{
      		return;
      	}
      	
		var currServerDate = '<%=PortalDateUtils.currentFormattedDate("dd/MM/yyyy")%>';
          
      	_form = theForm;
      	
      	_appUserTypeId = _form.appUserTypeId;
      	_userId = _form.userId;
      	_firstName 	= _form.firstName;
      	_lastName 	= _form.lastName;
      	_partnerId = _form.partnerId;
      	_password = _form.password;
      	_confirmPassword = _form.confirmPassword;
      	_email = _form.email;
      	_dob = _form.dob;
      	_mobileNo 	= _form.mobileNo;
      	_isActive = _form.isActive ;
      	_partnerGroupId=_form.partnerGroupId;
      	_employeeId = _form.employeeId;
      	//_tellerId = _form.tellerId;
      	
      	
      	_firstName.value = trim(_firstName.value);
      	_lastName.value = trim(_lastName.value);
      	_mobileNo.value = trim(_mobileNo.value);
      	_email.value = trim(_email.value);
      	_password.value = trim(_password.value);
      	_confirmPassword.value = trim(_confirmPassword.value);
      	_employeeId.value = trim(_employeeId.value);
      	
      	 _form.appUserType.value = _appUserTypeId.options[_appUserTypeId.selectedIndex].innerHTML;
      	 _form.partnerGroup.value = _partnerGroupId.options[_partnerGroupId.selectedIndex].innerHTML;
      	 _form.partnerType.value = _partnerId.options[_partnerId.selectedIndex].innerHTML;
		
 		if(_appUserTypeId.value == ''){
 			alert('Please select User Type.');
 			_appUserTypeId.focus();
 			return;
 		}	
 		
 		if(_partnerId.value == ''){      // for partner
 			alert('Please select Partner.');
 			_partnerId.focus();
 			return;
 		}
 		if(_employeeId.value=='' && isEmployeeIdRequired){
 			alert('Please enter Employee ID.');
 			_employeeId.focus();
 			return;
 		}
 		/*
 		if(_tellerId.value != '') {
 			
 			if(!validateType(_tellerId, "Teller ID")) {
 	 			_tellerId.focus();
 	 			return false;
 			}
 		}
 		*/
 		if(_firstName.value == ''){
 			alert('Please enter First Name.');
 			_firstName.focus();
 			return;
 		}	
 		
 		if(!isAlphaWithSpace(_firstName)){
 			alert('Please enter correct First Name.');
 			_firstName.focus();
 			return;
 		}	

 		if(_lastName.value == ''){
 			alert('Please enter Last Name.');
 			_lastName.focus();
 			return;
 		}		

 		if(!isAlphaWithSpace(_lastName)){
 			alert('Please enter correct Last Name.');
 			_lastName.focus();
 			return;
 		}	
        
        if(_userId.value == ''){
 			alert('Please enter User Id.');
 			_userId.focus();
 			return;
 		}

        //////////////////////////////////////////////////
        if(document.forms[0].isUpdate == undefined){  // new record case
            //alert('In the new case');
            if(_password.value == ''){
                alert('Please enter Password.');
 			    _password.focus();
 			    return;
            }
        
            if(_confirmPassword.value == ''){
                alert('Please enter Confirm Password.');
 			    _confirmPassword.focus();
 			    return;
            }
        
            if(_password.value != _confirmPassword.value){
                alert('Passwords you typed do not match.');
                return;
            }
        }
        else{ //update case
            //alert('In the update case');
            if(_password.value != _confirmPassword.value){
                alert('Passwords you typed do not match.');
                return;
            }
        }
        
        //////////////////////////////////////////////////
        
        
        
        //dob		
		if(trim(_dob.value) != '' && !isDate1(_dob)){
			//alert('Please enter valid date.');
			_dob.focus();
			return;
		}
        
		if( trim(_dob.value) != '' && isDateGreater(_dob.value,currServerDate)){
			alert('Future date of birth is not allowed.');
			_dob.focus();
			return;
		}else if( isAgeLessThan18Years(_dob.value)){
			alert('User age cannot be less than 18 years');
			_dob.focus();
			return;
		}	
        
		if(_mobileNo.value == '' && _email.value == '') {
 			alert('Please enter either Mobile # or Email.');
 			_mobileNo.focus();
 			return;
 		}
		
        if(_email.value != ''){   // check the format of the email address here TODO
            if(!isEmail(_email)){
                alert('Please enter correct Email.');
                return;
            }
        }

		if(_mobileNo.value != ''){
	 		if(!isNumberWithZeroes(_mobileNo) || _mobileNo.value.length < 11) {
	 			alert('Please enter correct Mobile #.');
	 			_mobileNo.focus();
	 			return;
	 		}

			var mobileNumber = _mobileNo.value;
        	if(mobileNumber.charAt(0)!="0" || mobileNumber.charAt(1)!="3"){
       			alert('Mobile # should start from 03XXXXXXXXX');
       			_mobileNo.focus();
       			return;
        	}
		}
		
		if(!validateFormChar(theForm)){
      		return false;
      	}
		

      	//submitting form
        if (confirm('Are you sure you want to proceed?')==true)
        {
          document.userManagementForm.submit();
        }
        else
        {
          return false;
        }
  }
	  function isAgeLessThan18Years(dateOfBirth){
		if(trim(dateOfBirth) != null && dateOfBirth != '' && dateOfBirth.length == 10){
			dateOfBirth = dateOfBirth.split("/");
	  		var birthDay = dateOfBirth[0];
	  		var birthMonth = dateOfBirth[1];
	  		var birthYear = dateOfBirth[2];
	  		var age = calculate_age(birthMonth,birthDay,birthYear);
			if(age < 18){
				return true;
			}
		}
		return false;
	  }

		function calculate_age(birth_month,birth_day,birth_year){
    		var today_date = '<%=PortalDateUtils.currentFormattedDate("dd/MM/yyyy")%>';
			today_date = today_date.split("/");
			today_year = today_date[2];
			today_month = today_date[1];
			today_day = today_date[0];
			age = today_year - birth_year;
			if (today_month < birth_month) {
				age--;
			}
			if ((birth_month == today_month) && (today_day < birth_day)) {
				age--;
			}
			return age;
		}

		function onFormSubmit(theForm) {
			submitForm(theForm);
		}

		Calendar.setup({
			inputField : "dob", // id of the input field
			ifFormat : "%d/%m/%Y", // the date format
			button : "calButton", // id of the button
			showsTime : false
		});
		
		
	      
		function validateType(field, fname) {
		    
		    var re = "[^A-Za-z0-9]";
		    
		    var flag = true;
		    
            for(var i=0; i<field.value.length;i++){
                var charpos = field.value[i].search(re);
                if(charpos >= 0){
                   flag=false;
					break;       
                }
            }  		    
            
		    if(!flag) {
				alert(fname + ' contains invalid character(s), must be alpha-numeric.');
				field.focus();
				flag = false; 
		    }
		    
		    return flag;
		}		

	</script>
</body>
</html>
