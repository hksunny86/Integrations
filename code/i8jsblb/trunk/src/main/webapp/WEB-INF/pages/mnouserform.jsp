
<jsp:directive.page import="com.inov8.microbank.common.util.PortalDateUtils"/><!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Asad Hayat-->
<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="decorator2">
	
<meta name="title" content="Service Operator User"/>
<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
		<link rel="stylesheet" type="text/css"
			href="${pageContext.request.contextPath}/styles/deliciouslyblue/calendar.css" />
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
			
		<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>

	<script type="text/javascript">
   	function error(request)
	{
     	alert("An unknown error has occured. Please contact with the administrator for more details");
	}
	</script>



</head>
<body>
<%@include file="/common/ajax.jsp"%>
<spring:bind path="mnoUserModel.*">
  <c:if test="${not empty status.errorMessages}">
    <div class="errorMsg">
      <c:forEach var="error" items="${status.errorMessages}">
        <c:out value="${error}" escapeXml="false"/>
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

		
			
	
	

<form id="mnoUserForm" method="post" action="mnouserform.html" onsubmit="return onFormSubmit(this);">
<table width="100%" border="0" cellpadding="0" cellspacing="1">

  <c:if test="${not empty param.mnoUserId}">
    <input type="hidden" name="isUpdate" id="isUpdate" value="true"/>
    <spring:bind path="mnoUserModel.mnoUserId">
    	<input type="hidden" name="${status.expression}" value="${status.value}"/>
    </spring:bind>
    <spring:bind path="mnoUserModel.appUserId">
    	<input type="hidden" name="${status.expression}" value="${status.value}"/>
    </spring:bind>
  </c:if>

  <tr>
    <td width="50%" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Service Operator:&nbsp;</td>
      <td align="left">
        <spring:bind path="mnoUserModel.mnoId">
            <select name="${status.expression}" class="textBox" id="${status.expression}">
              <c:forEach items="${mnoModelList}" var="mnoModelList">
                <option value="${mnoModelList.mnoId}"
                  <c:if test="${status.value == mnoModelList.mnoId}">selected="selected"</c:if>/>
                  ${mnoModelList.name}
                </option>
              </c:forEach>
            </select>
        </spring:bind>
      </td>
  </tr>
  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>First Name:&nbsp;</td>
    <td>
      <spring:bind path="mnoUserModel.firstName">
        <c:if test="${not empty mnoUserModel.firstName}">
          <input type="text" name="${status.expression}" class="textBox" maxlength="50" value="${status.value}" onkeypress="return maskAlphaWithSp(this,event)"></c:if>
        <c:if test="${empty mnoUserModel.firstName}">
          <input type="text" name="${status.expression}" class="textBox" maxlength="50" value="${status.value}" onkeypress="return maskAlphaWithSp(this,event)"/>
        </c:if>
      </spring:bind>
    </td>
  </tr>
  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Last Name:&nbsp;</td>
    <td>
      <spring:bind path="mnoUserModel.lastName">
        <c:if test="${not empty mnoUserModel.lastName}">
          <input type="text" name="${status.expression}" class="textBox" maxlength="50" value="${status.value}" onkeypress="return maskAlphaWithSp(this,event)"></c:if>
        <c:if test="${empty mnoUserModel.lastName}">
          <input type="text" name="${status.expression}" class="textBox" maxlength="50" value="${status.value}" onkeypress="return maskAlphaWithSp(this,event)">
        </c:if>
      </spring:bind>
    </td>
  </tr>

  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>User Name:&nbsp;</td>
    <td><spring:bind path="mnoUserModel.username">
      <input type="text" name="${status.expression}" class="textBox" maxlength="50" value="${status.value}"
      <c:if test="${not empty param.mnoUserId}">readonly="readOnly"</c:if>
      >
      </spring:bind>
    </td>
  </tr>
  
  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Password:&nbsp;</td>
    <td><spring:bind path="mnoUserModel.password">
      <input type="password" name="${status.expression}" class="textBox" maxlength="4000" value="">
      </spring:bind>
    </td>
  </tr>
  
  <tr>
  	<td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Confirm Password:&nbsp;</td>
    <td><input type="password" name="confirmPassword" class="textBox" maxlength="4000" value="${status.value}"/></td>
  </tr>



  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">Password Hint:&nbsp;</td>
    <td><spring:bind path="mnoUserModel.passwordHint">
      <input type="text" name="${status.expression}" class="textBox" maxlength="11" value="${status.value}"/>
      </spring:bind>
    </td>
  </tr>

  	<tr bgcolor="FBFBFB">
      <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Partner Group:&nbsp;</td>
      <td align="left">
						<spring:bind path="mnoUserModel.partnerGroupId">
							<select name="${status.expression}" class="textBox" id="${status.expression}"> 
								<c:forEach items="${partnerGroupModelList}"
									var="partnerGroupModelList">
									<option value="${partnerGroupModelList.partnerGroupId}"
										<c:if test="${status.value == partnerGroupModelList.partnerGroupId}">selected="selected"</c:if>>
										${partnerGroupModelList.name}
									</option>
								</c:forEach>
							</select>
					        ${status.errorMessage}
					      </spring:bind>
      </td>
    </tr>



  	<tr bgcolor="FBFBFB">
      <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Connection Type:&nbsp;</td>
      <td align="left">
        <spring:bind path="mnoUserModel.mobileTypeId">

            <select name="${status.expression}" class="textBox" id="${status.expression}">

              <c:forEach items="${mobileTypeModelList}" var="mobileTypeModelList"> 
                <option value="${mobileTypeModelList.mobileTypeId}"
                  <c:if test="${status.value == mobileTypeModelList.mobileTypeId}">selected="selected"</c:if>/>
                  ${mobileTypeModelList.name}
                </option>
              </c:forEach>
            </select>

        </spring:bind>
      </td>
    </tr>
  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Mobile No:&nbsp;</td>
    <td><spring:bind path="mnoUserModel.mobileNo">
      <input type="text" name="${status.expression}" class="textBox" maxlength="11" value="${status.value}" onkeypress="return maskNumber(this,event)"/>
      </spring:bind>
    </td>
  </tr>
  
    <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">Email:&nbsp;</td>
    <td><spring:bind path="mnoUserModel.email">
      <input type="text" name="${status.expression}" class="textBox" maxlength="50" value="${status.value}"/>
      </spring:bind>
    </td>
  </tr>
    <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">Fax:&nbsp;</td>
    <td><spring:bind path="mnoUserModel.fax">
      <input type="text" name="${status.expression}" class="textBox" maxlength="11" value="${status.value}" onkeypress="return maskNumber(this,event)"/>
      </spring:bind>
    </td>
  </tr>
  
  <tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						Date of Birth:&nbsp;
					</td>
					<td>
						<spring:bind path="mnoUserModel.dob">
							<input type="text" name="${status.expression}" class="textBox"
								id="${status.expression}" maxlength="10" value="${status.value}"
								readonly="readonly" />
						</spring:bind>
						<img id="sDate" tabindex="4" name="popcal" align="top"
							style="cursor:pointer" src="${pageContext.request.contextPath}/images/cal.gif" border="0" />
						<img id="sDate" tabindex="5" name="popcal" title="Clear Date"
							onclick="javascript:$('dob').value=''" align="middle"
							style="cursor:pointer" src="${pageContext.request.contextPath}/images/refresh.png" border="0" />
					</td>
				</tr>
				
								<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						NIC:&nbsp;
					</td>
					<td>
						<spring:bind path="mnoUserModel.nic">
							<input type="text" name="${status.expression}" class="textBox"
								maxlength="13" value="${status.value}"
								onkeypress="return maskNumber(this,event)" />
						</spring:bind>
					</td>
				</tr>

				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						Mother Maiden Name:&nbsp;
					</td>
					<td>
						<spring:bind path="mnoUserModel.motherMaidenName">
							<input type="text" name="${status.expression}" class="textBox"
								maxlength="50" value="${status.value}"
								onkeypress="return maskAlphaWithSp(this,event)" />
						</spring:bind>
					</td>
				</tr>
				
  
  
  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">Address1:&nbsp;</td>
    <td><spring:bind path="mnoUserModel.address1">
      <input type="text" name="${status.expression}" class="textBox" onkeyup="textAreaLengthCounter(this,250);"  maxlength="250" value="${status.value}"/>
      </spring:bind>
    </td>
  </tr>
  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">Address2:&nbsp;</td>
    <td><spring:bind path="mnoUserModel.address2">
      <input type="text" name="${status.expression}" class="textBox" onkeyup="textAreaLengthCounter(this,250);"  maxlength="250" value="${status.value}"/>
      </spring:bind>
    </td>
  </tr>
  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">City:&nbsp;</td>
    <td><spring:bind path="mnoUserModel.city">
      <input type="text" name="${status.expression}" class="textBox" maxlength="50" value="${status.value}" onkeypress="return maskAlphaWithSp(this,event)"/>
      </spring:bind>
    </td>
  </tr>
  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">State:&nbsp;</td>
    <td><spring:bind path="mnoUserModel.state">
      <input type="text" name="${status.expression}" class="textBox" maxlength="50" value="${status.value}" onkeypress="return maskAlphaWithSp(this,event)"/>
      </spring:bind>
    </td>
  </tr>
  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">Zip Code:&nbsp;</td>
    <td><spring:bind path="mnoUserModel.zip">
      <input type="text" name="${status.expression}" class="textBox" maxlength="10" value="${status.value}" onkeypress="return maskAlphaNumeric(this,event)"/>
      </spring:bind>
    </td>
  </tr>
  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">Country:&nbsp;</td>
    <td><spring:bind path="mnoUserModel.country">
      <input type="text" name="${status.expression}" class="textBox" maxlength="50" value="${status.value}" onkeypress="return maskAlphaWithSp(this,event)"/>
      </spring:bind>
    </td>
  </tr>
      
  <tr>
    <td align="right" bgcolor="F3F3F3" class="formText">Description:&nbsp;</td>
    <td align="left" bgcolor="FBFBFB"><spring:bind path="mnoUserModel.description"><textarea name="${status.expression}" cols="50" rows="5" class="textBox" onkeypress="return maskCommon(this,event)" onkeyup="textAreaLengthCounter(this,250);">${status.value}</textarea></spring:bind></td>
  </tr>
  
  <tr>
    <td align="right" bgcolor="F3F3F3" class="formText">Comments:&nbsp;</td>
    <td align="left" bgcolor="FBFBFB"><spring:bind path="mnoUserModel.comments"><textarea name="${status.expression}" cols="50" rows="5" class="textBox" onkeypress="return maskCommon(this,event)" onkeyup="textAreaLengthCounter(this,250);">${status.value}</textarea></spring:bind></td>
  </tr>

  <tr>
    <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Verified:&nbsp;</td>
    <td width="51%" align="left" bgcolor="FBFBFB" class="formText">
    <spring:bind path="mnoUserModel.verified">
	    <input name="${status.expression}" type="checkbox" value="true" 	
			    <c:if test="${status.value==true}">checked="checked"</c:if>
				<c:if test="${empty param.mnoUserId && empty param._save }">checked="checked"</c:if> 
				<c:if test="${status.value==false && not empty param._save}">unchecked="checked"</c:if>

	    />			    
     </spring:bind>
    </td>
  </tr>

  
  <tr>
    <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Account Enabled:&nbsp;</td>
    <td width="51%" align="left" bgcolor="FBFBFB" class="formText">
    <spring:bind path="mnoUserModel.accountEnabled">
	    <input name="${status.expression}" type="checkbox" value="true" 	
							<c:if test="${status.value==true}">checked="checked"</c:if>
								<c:if test="${empty param.mnoUserId && empty param._save }">checked="checked"</c:if> 
								<c:if test="${status.value==false && not empty param._save}">unchecked="checked"</c:if>

	    />			    
     </spring:bind>
    </td>
  </tr>

      <tr bgcolor="FBFBFB" >
            <td align="right" bgcolor="F3F3F3" class="formText">Account Expired:&nbsp;</td>
            <td align="left" class="formText">
		    <spring:bind path="mnoUserModel.accountExpired">
			    <input name="${status.expression}" type="checkbox" value="true" 	
			    <c:if test="${status.value==true}">checked="checked"</c:if>/>			    
		    </spring:bind>
			</td>
      </tr>
          
      <tr bgcolor="FBFBFB" >
            <td align="right" bgcolor="F3F3F3" class="formText">Account Locked:&nbsp;</td>
            <td align="left" class="formText">
		    <spring:bind path="mnoUserModel.accountLocked">
			    <input name="${status.expression}" type="checkbox" value="true" 	
<c:if test="${status.value==true}">checked="checked"</c:if>
								<c:if test="${status.value==false && not empty param._save}">unchecked="checked"</c:if> />

		    </spring:bind>
			</td>
      </tr>    
      
      <tr bgcolor="FBFBFB" >
            <td align="right" bgcolor="F3F3F3" class="formText">Credentials Expired:&nbsp;</td>
            <td align="left" class="formText">
		    <spring:bind path="mnoUserModel.credentialsExpired">
			    <input name="${status.expression}" type="checkbox" value="true" 	
<c:if test="${status.value==true}">checked="checked"</c:if>
								<c:if test="${status.value==false && not empty param._save}">unchecked="checked"</c:if> />

		    </spring:bind>
			</td>
      </tr>


  <tr bgcolor="FBFBFB">
    <td height="19" colspan="2" align="center"> <GenericToolbar:toolbar formName="mnoUserForm"/> </td>
</td>
  </tr>
</table>
</form>

		    <ajax:select source="mnoId"
		    target="partnerGroupId"
		    baseUrl="${contextPath}/partnerTypeFormRefDataController.html"
		    parameters="id={mnoId},appUserTypeId=7,actionType=2"
		    errorFunction="error"/>
			
<script type="text/javascript">
{
	// Sets the focus on first field
	document.forms[0].mnoId.focus();
	
	Calendar.setup(
      {
        inputField  : "dob", // id of the input field
        ifFormat    : "%d/%m/%Y",      // the date format
        button      : "popcal"    // id of the button
      }
      );
      
      
      function isDateGreaterOrEqualForCard(from, to) {

	var fromDate;
	var toDate;

	var result = false;

//  format dd/mm/yyyy
	fromDate = from.substring(6,10) + from.substring(3,5) + from.substring(0,2);
	toDate = to.substring(6,10)   + to.substring(3,5) + to.substring(0,2);
	if( fromDate != toDate && fromDate > toDate ) {
		result = true;
	}

	return result;
}
      
  function onFormSubmit(theForm) 
  {
  
  checkNumericData();
     /*if(!validateFormChar(theForm)){
      	return false;
     }*/
    
     var currServerDate = '<%=PortalDateUtils.currentFormattedDate("dd/MM/yyyy")%>';
   
  	if( document.forms.mnoUserForm.isUpdate != null && document.forms.mnoUserForm.isUpdate.value == 'true' )
  	{
		if( document.forms.mnoUserForm.password.value == document.forms.mnoUserForm.confirmPassword.value )
		{
			return validateMnoUserFormModel(theForm);
	 	}	 
		alert("Password and Confirm Password do not match");
		document.forms.mnoUserForm.password.focus();
	    return false;
	}
	if( document.forms.mnoUserForm.password.value == '' )
	{
		alert("Password and confirmed password is required.");
		document.forms.mnoUserForm.password.focus();
	}
  	else if( document.forms.mnoUserForm.password.value == document.forms.mnoUserForm.confirmPassword.value )
	{
		return validateMnoUserFormModel(theForm);
 	}	 
 	else
 	{
		alert("Password and Confirm Password do not match");
		document.forms.mnoUserForm.password.focus();
	}
  	
    return false;
  }
}
function checkNumericData(){
	if (! IsNumeric(document.forms.mnoUserForm.nic.value)){
		alert ("NIC must contain numeric data");
		return false;
	}
	
}

function IsNumeric(sText)

{
   var ValidChars = "0123456789";
   var IsNumber=true;
   var Char;

  if (sText != ''){
   for (i = 0; i < sText.length && IsNumber == true; i++) 
      { 
      Char = sText.charAt(i); 
      if (ValidChars.indexOf(Char) == -1) 
         {
         IsNumber = false;
         }
      }
      }
      //alert ('adsf');
   return IsNumber;
   
   }

</script>
<v:javascript formName="mnoUserFormModel"
			staticJavascript="false" />
		<script type="text/javascript"
			src="<c:url value="/scripts/validator.jsp"/>"></script>

</body>
</html>
