<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: JALIL-UR-REHMAN-->
<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
<meta name="decorator" content="decorator2">
		<meta name="title" content="Bank User" />

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
			
		<script type="text/javascript">
		 function error(request)
		{
     	alert("An unknown error has occured. Please contact with the administrator for more details");
		}


function popupCallback(src,popupName,columnHashMap)
{
   if(src=="bankName")
  {
    document.forms.bankUserForm.bankId.value = columnHashMap.get('PK');
    document.forms.bankUserForm.bankName.value = columnHashMap.get('name');
  }
}


</script>
	</head>
	<body>
   <%@include file="/common/ajax.jsp"%>
		<spring:bind path="bankUserListViewFormModel.*">
			<c:if test="${not empty status.errorMessages}">
				<div class="errorMsg">
					<c:forEach var="error" items="${status.errorMessages}">
						<c:out value="${error}" escapeXml="false" />
						<br />
					</c:forEach>
				</div>
				
				<spring:bind path="bankUserListViewFormModel.bankId">
					    <input type="hidden" name="${status.expression}" value="${status.value}"/>
					</spring:bind>
				
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


		<form id="bankUserForm" method="post" action="bankuserform.html"
			onsubmit="return onFormSubmit(this);">
			<table width="100%" border="0" cellpadding="0" cellspacing="1">

				<c:if test="${not empty param.bankUserId}">
					<input type="hidden" name="isUpdate" id="isUpdate" value="true" />
					<spring:bind path="bankUserListViewFormModel.bankId">
					    <input type="hidden" name="${status.expression}" value="${status.value}"/>
					</spring:bind>
				</c:if>
				<spring:bind path="bankUserListViewFormModel.bankUserId">
					<input type="hidden" name="${status.expression}"
						value="${status.value}" />
				</spring:bind>
				<spring:bind path="bankUserListViewFormModel.versionNo">
					<input type="hidden" name="${status.expression}"
						value="${status.value}" />
				</spring:bind>


				<tr>
					<td width="50%" align="right" bgcolor="F3F3F3" class="formText">
						<span style="color:#FF0000">*</span>Bank:&nbsp;
					</td>
					<td width="50%" align="left" bgcolor="FBFBFB">

						<spring:bind path="bankUserListViewFormModel.bankId">
							<select name="${status.expression}" id="${status.expression}" class="textBox" <c:if test="${not empty param.bankUserId}">disabled="disabled"</c:if> >
								<c:forEach items="${bankModelList}" var="bankModelList">
									<option value="${bankModelList.bankId}"
										<c:if test="${status.value == bankModelList.bankId}">selected="selected"</c:if>>
										${bankModelList.name}
									</option>
								</c:forEach>
							</select>
        ${status.errorMessage}
      </spring:bind>


					</td>
				</tr>

				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						<span style="color:#FF0000">*</span>First Name:&nbsp;
					</td>
					<td>


						<spring:bind path="bankUserListViewFormModel.firstName">
							<input type="text" name="${status.expression}" class="textBox"
								maxlength="50" value="${status.value}"
								onkeypress="return maskAlphaWithSp(this,event)" />
						</spring:bind>
					</td>
				</tr>
				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						<span style="color:#FF0000">*</span>Last Name:&nbsp;
					</td>
					<td>
						<spring:bind path="bankUserListViewFormModel.lastName">
							<input type="text" name="${status.expression}" class="textBox"
								maxlength="50" value="${status.value}"
								onkeypress="return maskAlphaWithSp(this,event)" />
						</spring:bind>
					</td>
				</tr>



					<tr bgcolor="FBFBFB">
						<td align="right" bgcolor="F3F3F3" class="formText">
							<span style="color:#FF0000">*</span>User Name:&nbsp;
						</td>
						<td>
							<spring:bind path="bankUserListViewFormModel.username">
							
								<input type="text" name="${status.expression}" class="textBox"
									maxlength="50" value="${status.value}"
									<c:if test="${not empty param.bankUserId}">readonly="readOnly"</c:if>		
									 />
							</spring:bind>
						</td>
					</tr>
					<tr bgcolor="FBFBFB">
						<td align="right" bgcolor="F3F3F3" class="formText">
							<span style="color:#FF0000">*</span>Password:&nbsp;
						</td>
						<td>
							<spring:bind path="bankUserListViewFormModel.password">
							
								<input type="password" name="${status.expression}"
									class="textBox" maxlength="50" value="" />
							</spring:bind>
						</td>
					</tr>
					<tr bgcolor="FBFBFB">
						<td align="right" bgcolor="F3F3F3" class="formText">
							<span style="color:#FF0000">*</span>Confirm Password:&nbsp;
						</td>
						<td>

							<input type="password" name="confirmPassword" class="textBox"
								maxlength="4000" />

						</td>
					</tr>
					<tr bgcolor="FBFBFB">
						<td align="right" bgcolor="F3F3F3" class="formText">
							Password Hint:&nbsp;
						</td>
						<td>
							<spring:bind path="bankUserListViewFormModel.passwordHint">
								<input type="text" name="${status.expression}" class="textBox"
									maxlength="50" value="${status.value}" />
							</spring:bind>
						</td>
					</tr>

				<tr>
					<td width="50%" align="right" bgcolor="F3F3F3" class="formText">
						<span style="color:#FF0000">*</span>Partner Group:&nbsp;
					</td>
					<td width="50%" align="left" bgcolor="FBFBFB">
						<spring:bind path="bankUserListViewFormModel.partnerGroupId">
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


				<tr>
					<td width="50%" align="right" bgcolor="F3F3F3" class="formText">
						<span style="color:#FF0000">*</span>Connection Type:&nbsp;
					</td>
					<td width="50%" align="left" bgcolor="FBFBFB">

						<spring:bind path="bankUserListViewFormModel.mobileTypeId">
							<select name="${status.expression}" class="textBox">
								<c:forEach items="${mobileTypeModelList}"
									var="mobileTypeModelList">
									<option value="${mobileTypeModelList.mobileTypeId}"
										<c:if test="${status.value == mobileTypeModelList.mobileTypeId}">selected="selected"</c:if>>
										${mobileTypeModelList.name}
									</option>
								</c:forEach>
							</select>
					        ${status.errorMessage}
					      </spring:bind>


					</td>
				</tr>
				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						<span style="color:#FF0000">*</span>Mobile No:&nbsp;
					</td>
					<td>
						<spring:bind path="bankUserListViewFormModel.mobileNo">
							<input type="text" name="${status.expression}" class="textBox"
								maxlength="11" value="${status.value}"
								onkeypress="return maskNumber(this,event)" />
						</spring:bind>
					</td>
				</tr>
				
				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						Email:&nbsp;
					</td>
					<td>
						<spring:bind path="bankUserListViewFormModel.email">
							<input type="text" name="${status.expression}" class="textBox"
								maxlength="50"  value="${status.value}" />
						</spring:bind>
					</td>
				</tr>

				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						Fax No:&nbsp;
					</td>
					<td>
						<spring:bind path="bankUserListViewFormModel.fax">
							<input type="text" name="${status.expression}" class="textBox"
								maxlength="11" value="${status.value}"
								onkeypress="return maskNumber(this,event)" />
						</spring:bind>
					</td>
				</tr>
				

<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						Date of Birth:&nbsp;
					</td>
					<td>
						<spring:bind path="bankUserListViewFormModel.dob">
							<input type="text" name="${status.expression}" class="textBox"
								maxlength="10" value="${status.value}" readonly="readonly" />
						</spring:bind>
						<img id="calButton" tabindex="4" name="popcal" align="top"
							style="cursor:pointer" src="${pageContext.request.contextPath}/images/cal.gif" border="0" />
						<img id="calButton" tabindex="5" name="popcal" title="Clear Date"
							onclick="javascript:$('dob').value=''" align="middle"
							style="cursor:pointer" src="${pageContext.request.contextPath}/images/refresh.png" border="0" />
					</td>
				</tr>


				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						NIC:&nbsp;
					</td>
					<td>
						<spring:bind path="bankUserListViewFormModel.nic">
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
						<spring:bind path="bankUserListViewFormModel.motherMaidenName">
							<input type="text" name="${status.expression}" class="textBox"
								maxlength="50" value="${status.value}"
								onkeypress="return maskAlphaWithSp(this,event)" />
						</spring:bind>
					</td>
				</tr>

				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						Address1:&nbsp;
					</td>
					<td>
						<spring:bind path="bankUserListViewFormModel.address1">
							<input type="text" name="${status.expression}" class="textBox"
								value="${status.value}" maxlength="250"
								onkeyup="textAreaLengthCounter(this,250);" />
						</spring:bind>
					</td>
				</tr>

				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						Address2:&nbsp;
					</td>
					<td>
						<spring:bind path="bankUserListViewFormModel.address2">
							<input type="text" name="${status.expression}" class="textBox"
								value="${status.value}" maxlength="250"
								onkeyup="textAreaLengthCounter(this,250);" />
						</spring:bind>
					</td>
				</tr>
				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						City:&nbsp;
					</td>
					<td>
						<spring:bind path="bankUserListViewFormModel.city">
							<input type="text" name="${status.expression}" class="textBox"
								maxlength="50" value="${status.value}"
								onkeypress="return maskAlphaWithSp(this,event)" />
						</spring:bind>
					</td>
				</tr>

				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						State:&nbsp;
					</td>
					<td>
						<spring:bind path="bankUserListViewFormModel.state">
							<input type="text" name="${status.expression}" class="textBox"
								maxlength="50" value="${status.value}"
								onkeypress="return maskAlphaWithSp(this,event)" />
						</spring:bind>
					</td>
				</tr>

				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						Zip Code:&nbsp;
					</td>
					<td>
						<spring:bind path="bankUserListViewFormModel.zip">
							<input type="text" name="${status.expression}" class="textBox"
								maxlength="10" value="${status.value}"
								onkeypress="return maskAlphaNumeric(this,event)" />
						</spring:bind>
					</td>
				</tr>
				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						Country:&nbsp;
					</td>
					<td>
						<spring:bind path="bankUserListViewFormModel.country">
							<input type="text" name="${status.expression}" class="textBox"
								maxlength="50" value="${status.value}"
								onkeypress="return maskAlphaWithSp(this,event)" />
						</spring:bind>
					</td>
				</tr>

				<tr>
					<td align="right" bgcolor="F3F3F3" class="formText">
						Description:&nbsp;
					</td>
					<td align="left" bgcolor="FBFBFB">
						<spring:bind path="bankUserListViewFormModel.description">
							<textarea name="${status.expression}" cols="50" rows="5"
								class="textBox" onkeyup="textAreaLengthCounter(this,250);" onkeypress="return maskCommon(this,event)">${status.value}</textarea>
						</spring:bind>
					</td>
				</tr>
					
					<tr>
					<td align="right" bgcolor="F3F3F3" class="formText">
						Comments:&nbsp;
					</td>
					<td align="left" bgcolor="FBFBFB">
						<spring:bind path="bankUserListViewFormModel.comments">
							<textarea name="${status.expression}" cols="50"  onkeypress="return maskCommon(this,event)"
								onkeyup="textAreaLengthCounter(this,250);" rows="5"
								class="textBox">${status.value}</textarea>
						</spring:bind>
					</td>
				</tr>
				
				 <tr>
    <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Verified:&nbsp;</td>
    <td width="51%" align="left" bgcolor="FBFBFB" class="formText">
		    <spring:bind path="bankUserListViewFormModel.verified">
			    <input name="${status.expression}" type="checkbox" value="true" 	
								<c:if test="${status.value==true}">checked="checked"</c:if>
								<c:if test="${empty param.bankUserId && empty param._save }">checked="checked"</c:if> 
								<c:if test="${status.value==false && not empty param._save}">unchecked="checked"</c:if>
			    />			    
		     </spring:bind>
		    </td>
		  </tr>
				
				<tr>
					<td width="49%" height="16" align="right" bgcolor="F3F3F3"
						class="formText">
						Account Enabled:${status.value}&nbsp;
					</td>
					<td width="51%" align="left" bgcolor="FBFBFB" class="formText">
						<spring:bind path="bankUserListViewFormModel.accountEnabled">
							<input name="${status.expression}" type="checkbox" value="true"
								<c:if test="${status.value==true}">checked="checked"</c:if>
								<c:if test="${empty param.bankUserId && empty param._save }">checked="checked"</c:if> 
								<c:if test="${status.value==false && not empty param._save}">unchecked="checked"</c:if>
								 />
						</spring:bind>
					</td>
				</tr>
				<tr>
					<td width="49%" height="16" align="right" bgcolor="F3F3F3"
						class="formText">
						Account Expired:&nbsp;
					</td>
					<td width="51%" align="left" bgcolor="FBFBFB" class="formText">
						<spring:bind path="bankUserListViewFormModel.accountExpired">
							<input type="hidden" name="_${status.expression}" />
							<input name="${status.expression}" type="checkbox" value="true"
								<c:if test="${status.value==true}">checked="checked"</c:if>
								<c:if test="${status.value==false && not empty param._save}">unchecked="checked"</c:if> />
						</spring:bind>
					</td>
				</tr>

				<tr>
					<td width="49%" height="16" align="right" bgcolor="F3F3F3"
						class="formText">
						Account Locked:&nbsp;
					</td>
					<td width="51%" align="left" bgcolor="FBFBFB" class="formText">
						<spring:bind path="bankUserListViewFormModel.accountLocked">
							<input type="hidden" name="_${status.expression}" />
							<input name="${status.expression}" type="checkbox" value="true"
								<c:if test="${status.value==true}">checked="checked"</c:if>
								<c:if test="${status.value==false && not empty param._save}">unchecked="checked"</c:if> />

						</spring:bind>
					</td>
				</tr>
				<tr>
					<td width="49%" height="16" align="right" bgcolor="F3F3F3"
						class="formText">
						Credentials Expired:&nbsp;
					</td>
					<td width="51%" align="left" bgcolor="FBFBFB" class="formText">
						<spring:bind path="bankUserListViewFormModel.credentialsExpired">
							<input type="hidden" name="_${status.expression}" />
							<input name="${status.expression}" type="checkbox" value="true"
								<c:if test="${status.value==true}">checked="checked"</c:if>
								<c:if test="${status.value==false && not empty param._save}">unchecked="checked"</c:if> />

						</spring:bind>
					</td>
				</tr>

				<tr bgcolor="FBFBFB">
					<td height="19" colspan="2" align="center"></td>
				</tr>
				<tr bgcolor="FBFBFB">
					<td colspan="2" align="center">

		<GenericToolbar:toolbar formName="bankUserForm" />

					</td>
				</tr>
			</table>
		</form>
		
		      <ajax:select 
			source="bankId" target="partnerGroupId"
			baseUrl="${contextPath}/partnerTypeFormRefDataController.html"
			parameters="appUserTypeId=6,actionType=2,id={bankId}" errorFunction="error"/>



		<script type="text/javascript">
{

	
if(document.forms[0].isUpdate == null)
   document.forms[0].bankId.focus();
else
   document.forms[0].firstName.focus();
highlightFormElements();
      highlightFormElements();
      
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
      
  function onFormSubmit(theForm) {
  
	  /* if(!validateFormChar(theForm)){
	  	 return false;
	  } */
  /*
  if( document.forms.bankUserForm.password.value == document.forms.bankUserForm.confirmPassword.value )
  	{
    return validateBankUserListViewFormModel(theForm);
    }
    alert("Value of Password fields dont matched");
    return false;
    
    */
    
    var currServerDate = '<%=PortalDateUtils.currentFormattedDate("dd/MM/yyyy")%>';
   if( trim(theForm.dob.value) != '' && isDateGreater(theForm.dob.value,currServerDate)){
			alert('Future date of birth is not allowed.');
			document.getElementById('calButton').focus();
			return false;
		}
    
       if( document.forms.bankUserForm.isUpdate != null && document.forms.bankUserForm.isUpdate.value == 'true' )
  	{
		if( document.forms.bankUserForm.password.value == document.forms.bankUserForm.confirmPassword.value )
		{
			return validateBankUserListViewFormModel(document.forms.bankUserForm);
	 	}	 
		alert("Password and Confirm Password do not match");
		document.forms.bankUserForm.password.focus();
	    return false;
	}
	if( document.forms.bankUserForm.password.value == '' )
	{
		alert("Password and confirmed password is required.");
		document.forms.bankUserForm.password.focus();
	}
  	else if( document.forms.bankUserForm.password.value == document.forms.bankUserForm.confirmPassword.value )
	{
		return validateBankUserListViewFormModel(document.forms.bankUserForm);
 	}	 
 	else
 	{
		alert("Password and Confirm Password do not match");
		document.forms.bankUserForm.password.focus();
	}
  	
    return false;
  }
  Calendar.setup(
      {
        inputField  : "dob", // id of the input field
        ifFormat    : "%d/%m/%Y",      // the date format
        button      : "calButton",    // id of the button
        showsTime   :   false
      }
      );
  
}
</script>

		<v:javascript formName="bankUserListViewFormModel"
			staticJavascript="false" />
		<script type="text/javascript"
			src="<c:url value="/scripts/validator.jsp"/>"></script>
	</body>
</html>
