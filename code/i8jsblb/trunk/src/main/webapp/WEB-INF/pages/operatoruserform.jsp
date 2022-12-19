<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Jalil-Ur-Rehman-->
<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
<meta name="decorator" content="decorator2">
		<meta name="title" content="Operator User" />
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
		<link rel="stylesheet" type="text/css"
			href="${pageContext.request.contextPath}/styles/deliciouslyblue/calendar.css" />

		<script type="text/javascript">
		
		
		 function error(request)
		{
     	alert("An unknown error has occured. Please contact with the administrator for more details");
		}
		
function popupCallback(src,popupName,columnHashMap)
{
   if(src=="operatorName")
  {
    document.forms.operatorUserForm.operatorId.value = columnHashMap.get('PK');
    document.forms.operatorUserForm.operatorName.value = columnHashMap.get('name');
  }
}
</script>
	</head>
	<body>
   <%@include file="/common/ajax.jsp"%>
		<spring:bind path="operatorUserListViewModel.*">
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


		<form id="operatorUserForm" method="post"
			action="operatoruserform.html" onsubmit="return onFormSubmit(this);">
			<table width="100%" border="0" cellpadding="0" cellspacing="1">

				<c:if test="${not empty param.operatorUserId}">
					<input type="hidden" name="isUpdate" id="isUpdate" value="true" />
					<spring:bind path="operatorUserListViewModel.operatorId">
					    <input type="hidden" name="${status.expression}" value="${status.value}"/>
                    </spring:bind>
				</c:if>
				<spring:bind path="operatorUserListViewModel.operatorUserId">
					<input type="hidden" name="${status.expression}"
						value="${status.value}" />
				</spring:bind>
				<spring:bind path="operatorUserListViewModel.appUserId">
					<input type="hidden" name="${status.expression}"
						value="${status.value}" />
				</spring:bind>
				<spring:bind path="operatorUserListViewModel.createdBy">
					<input type="hidden" name="${status.expression}"
						value="${status.value}" />
				</spring:bind>
				<spring:bind path="operatorUserListViewModel.updatedBy">
					<input type="hidden" name="${status.expression}"
						value="${status.value}" />
				</spring:bind>

				<spring:bind path="operatorUserListViewModel.createdOn">
					<input type="hidden" name="${status.expression}"
						value="${status.value}" />
				</spring:bind>

				<spring:bind path="operatorUserListViewModel.updatedOn">
					<input type="hidden" name="${status.expression}"
						value="${status.value}" />
				</spring:bind>

				<tr>
					<td width="50%" align="right" bgcolor="F3F3F3" class="formText">
						<span style="color:#FF0000">*</span> Operator:&nbsp;
					</td>
					<td width="50%" align="left" bgcolor="FBFBFB">

						<spring:bind path="operatorUserListViewModel.operatorId">
							<select name="${status.expression}" id="${status.expression}" class="textBox" <c:if test="${not empty param.operatorUserId}">disabled='disabled'</c:if>>
								<c:forEach items="${operatorModelList}" var="operatorModelList">
									<option value="${operatorModelList.operatorId}"
										<c:if test="${status.value == operatorModelList.operatorId}">selected="selected"</c:if>>
										${operatorModelList.name}
									</option>
								</c:forEach>
							</select>
        ${status.errorMessage}
      </spring:bind>


					</td>
				</tr>

				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						<span style="color:#FF0000">*</span> First Name:&nbsp;</td>
					<td>


						<spring:bind path="operatorUserListViewModel.firstName">
							<input type="text" name="${status.expression}" class="textBox"
								maxlength="50" value="${status.value}"
								onkeypress="return maskAlphaWithSp(this,event)" />
						</spring:bind>
					</td>
				</tr>
				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						<span style="color:#FF0000">*</span> Last Name:&nbsp;
					</td>
					<td>
						<spring:bind path="operatorUserListViewModel.lastName">
							<input type="text" name="${status.expression}" class="textBox"
								maxlength="50" value="${status.value}"
								onkeypress="return maskAlphaWithSp(this,event)" />
						</spring:bind>
					</td>
				</tr>
				


					<tr bgcolor="FBFBFB">
						<td align="right" bgcolor="F3F3F3" class="formText">
							<span style="color:#FF0000">*</span>User Name:&nbsp;						</td>
						<td>
							<spring:bind path="operatorUserListViewModel.username">
							
								<input type="text" name="${status.expression}" class="textBox"
									maxlength="50" value="${status.value}" 
								<c:if test="${not empty param.operatorUserId}">readonly="readOnly"</c:if>		
									/>
							</spring:bind>
						</td>
					</tr>
					<tr bgcolor="FBFBFB">
						<td align="right" bgcolor="F3F3F3" class="formText">
							<span style="color:#FF0000">*</span> Password:&nbsp;						</td>
						<td>
							<spring:bind path="operatorUserListViewModel.password">
							
								<input type="password" name="${status.expression}" class="textBox"
									maxlength="50" value="" />
							</spring:bind>
						</td>
					</tr>
					<tr bgcolor="FBFBFB">
						<td align="right" bgcolor="F3F3F3" class="formText">
							<span style="color:#FF0000">*</span> Confirm Password:&nbsp;						</td>
						<td>
							<input type="password" name="confirmPassword" class="textBox"
								maxlength="4000" value="${status.value}" />


						</td>
					</tr>
					<tr bgcolor="FBFBFB">
						<td align="right" bgcolor="F3F3F3" class="formText">
							Password Hint:&nbsp;</td>
						<td>
							<spring:bind path="operatorUserListViewModel.passwordHint">
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

						<spring:bind path="operatorUserListViewModel.partnerGroupId">
							<select name="${status.expression}" class="textBox" id="${status.expression}"  >
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
						<span style="color:#FF0000">*</span> Connection Type:&nbsp;					</td>
					<td width="50%" align="left" bgcolor="FBFBFB">

						<spring:bind path="operatorUserListViewModel.mobileTypeId">
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
						<span style="color:#FF0000">*</span> Mobile No:&nbsp;					</td>
					<td>
						<spring:bind path="operatorUserListViewModel.mobileNo">
							<input type="text" name="${status.expression}" class="textBox"
								maxlength="11" value="${status.value}"
								onkeypress="return maskNumber(this,event)" />
						</spring:bind>
					</td>
				</tr>

<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						Email:&nbsp;					</td>
					<td>
						<spring:bind path="operatorUserListViewModel.email">
							<input type="text" name="${status.expression}" class="textBox"
								maxlength="50" value="${status.value}" />
						</spring:bind>
					</td>
				</tr>

				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						Fax No:&nbsp;					</td>
					<td>
						<spring:bind path="operatorUserListViewModel.fax">
							<input type="text" name="${status.expression}" class="textBox"
								maxlength="11" value="${status.value}"
								onkeypress="return maskNumber(this,event)" />
						</spring:bind>
					</td>
				</tr>

<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						Date Of Birth:&nbsp;					</td>
					<td>
						<spring:bind path="operatorUserListViewModel.dob">
							<input type="text" name="${status.expression}" class="textBox" readonly="readonly"
								maxlength="50" value="${status.value}" readonly="readonly" />
						</spring:bind>
						
						
							<img id="calButton" tabindex="4"  name="popcal" align="top" style="cursor:pointer" src="${pageContext.request.contextPath}/images/cal.gif" border="0" />
						<img id="clearDate" tabindex="5" name="clearDate" title="Clear Date" onclick="javascript:$('dob').value=''"  align="middle" style="cursor:pointer" src="${pageContext.request.contextPath}/images/refresh.png" border="0" /> 
						
						
											</td>
				</tr>
				
				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						NIC:&nbsp;					</td>
					<td>
						<spring:bind path="operatorUserListViewModel.nic">
							<input type="text" name="${status.expression}" class="textBox"
								maxlength="13" value="${status.value}"
								onkeypress="return maskNumber(this,event)" />
						</spring:bind>
					</td>
				</tr>
<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						Mother Maiden Name:&nbsp;					</td>
					<td>
						<spring:bind path="operatorUserListViewModel.motherMaidenName">
							<input type="text" name="${status.expression}" class="textBox"
								maxlength="50" value="${status.value}"
								onkeypress="return maskAlphaWithSp(this,event)" />
						</spring:bind>
					</td>
				</tr>

				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						Address1:&nbsp;					</td>
					<td>
						<spring:bind path="operatorUserListViewModel.address1">
							<input type="text" name="${status.expression}" class="textBox"
								maxlength="250" value="${status.value}"
								onkeyup="textAreaLengthCounter(this,250);" />
						</spring:bind>
					</td>
				</tr>

				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						Address2:&nbsp;
					</td>
					<td>
						<spring:bind path="operatorUserListViewModel.address2">
							<input type="text" name="${status.expression}" class="textBox"
								maxlength="250" value="${status.value}"
								onkeyup="textAreaLengthCounter(this,250);" />
						</spring:bind>
					</td>
				</tr>
				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						City:&nbsp;
					</td>
					<td>
						<spring:bind path="operatorUserListViewModel.city">
							<input type="text" name="${status.expression}" class="textBox"
								maxlength="50" value="${status.value}"
								onkeypress="return maskAlphaWithSp(this,event)" />
						</spring:bind>
					</td>
				</tr>

				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						State:&nbsp;					</td>
					<td>
						<spring:bind path="operatorUserListViewModel.state">
							<input type="text" name="${status.expression}" class="textBox"
								maxlength="50" value="${status.value}"
								onkeypress="return maskAlphaWithSp(this,event)" />
						</spring:bind>
					</td>
				</tr>

				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						Zip Code:&nbsp;					</td>
					<td>
						<spring:bind path="operatorUserListViewModel.zip">
							<input type="text" name="${status.expression}" class="textBox"
								maxlength="10" value="${status.value}"
								onkeypress="return maskAlphaNumeric(this,event)" />
						</spring:bind>
					</td>
				</tr>
				
				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						Country:&nbsp;					</td>
					<td>
						<spring:bind path="operatorUserListViewModel.country">
							<input type="text" name="${status.expression}" class="textBox"
								maxlength="50" value="${status.value}"
								onkeypress="return maskAlphaWithSp(this,event)" />
						</spring:bind>
					</td>
				</tr>
			
				<tr>
					<td width="46%" align="right" bgcolor="F3F3F3" class="formText">
						Description:&nbsp;
					</td>
					<td width="54%" align="left" bgcolor="FBFBFB">
						<spring:bind path="operatorUserListViewModel.description">
							<textarea name="${status.expression}" cols="50" rows="5" onkeypress="return maskCommon(this,event)"
								onkeyup="textAreaLengthCounter(this,250);" class="textBox">${status.value}</textarea>
						</spring:bind>
					</td>
				</tr>

				<tr>
					<td width="46%" align="right" bgcolor="F3F3F3" class="formText">
						Comments:&nbsp;
					</td>
					<td width="54%" align="left" bgcolor="FBFBFB">
						<spring:bind path="operatorUserListViewModel.comments">
							<textarea name="${status.expression}" cols="50" rows="5" onkeypress="return maskCommon(this,event)"
								onkeyup="textAreaLengthCounter(this,250);" class="textBox">${status.value}</textarea>
						</spring:bind>
					</td>
				</tr>
				  				 <tr>
    <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Verified:&nbsp;</td>
    <td width="51%" align="left" bgcolor="FBFBFB" class="formText">
		    <spring:bind path="operatorUserListViewModel.verified">
			    <input name="${status.expression}" type="checkbox" value="true" 	
			    <c:if test="${status.value==true}">checked="checked"</c:if>
			    <c:if test="${empty param.operatorUserId && empty param._save }">checked="checked"</c:if>
			    <c:if test="${status.value==false && not empty param._save }">unchecked="checked"</c:if>
			    />			    
		     </spring:bind>
		    </td>
		  </tr>
				<tr>
					<td width="49%" height="16" align="right" bgcolor="F3F3F3"
						class="formText">
						Account Enabled:&nbsp;
					</td>
					<td width="51%" align="left" bgcolor="FBFBFB" class="formText">
						<spring:bind path="operatorUserListViewModel.accountEnabled">
							<input type="hidden" name="_${status.expression}" />
							<input type="hidden" name="_${status.expression}" />
							<input name="${status.expression}" type="checkbox" value="true"
								<c:if test="${status.value==true}">checked="checked"</c:if>
								<c:if test="${empty param.operatorUserId && empty param._save }">checked="checked"</c:if> 
								<c:if test="${status.value==false && not empty param._save }">unchecked="checked"</c:if>
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
						<spring:bind path="operatorUserListViewModel.accountExpired">
							<input type="hidden" name="_${status.expression}" />
							<input type="hidden" name="_${status.expression}" />
							<input name="${status.expression}" type="checkbox" value="true"
								<c:if test="${status.value==true}">checked="checked"</c:if>
								<c:if test="${empty param.operatorUserId}">unchecked="checked"</c:if> />
						</spring:bind>
					</td>
				</tr>

				<tr>
					<td width="49%" height="16" align="right" bgcolor="F3F3F3"
						class="formText">
						Account Locked:&nbsp;
					</td>
					<td width="51%" align="left" bgcolor="FBFBFB" class="formText">
						<spring:bind path="operatorUserListViewModel.accountLocked">
							<input type="hidden" name="_${status.expression}" />
							<input type="hidden" name="_${status.expression}" />
							<input name="${status.expression}" type="checkbox" value="true"
								<c:if test="${status.value==true}">checked="checked"</c:if>
								<c:if test="${empty param.operatorUserId}">unchecked="checked"</c:if> />
						</spring:bind>
					</td>
				</tr>
				<tr>
					<td width="49%" height="16" align="right" bgcolor="F3F3F3"
						class="formText">
						Credentials Expired:&nbsp;
					</td>
					<td width="51%" align="left" bgcolor="FBFBFB" class="formText">
						<spring:bind path="operatorUserListViewModel.credentialsExpired">
							<input type="hidden" name="_${status.expression}" />
							<input type="hidden" name="_${status.expression}" />
							<input name="${status.expression}" type="checkbox" value="true"
								<c:if test="${status.value==true}">checked="checked"</c:if>
								<c:if test="${empty param.operatorUserId}">unchecked="checked"</c:if> />
						</spring:bind>
					</td>
				</tr>

				<tr bgcolor="FBFBFB">
					<td height="19" colspan="2" align="center">		<GenericToolbar:toolbar formName="operatorUserForm" /></td>
				</tr>
								
			</table>
		</form>
		
		     <ajax:select 
			source="operatorId" target="partnerGroupId"
			baseUrl="${contextPath}/partnerTypeFormRefDataController.html"
			parameters="appUserTypeId=1,actionType=2,id={operatorId}" errorFunction="error"/>


		<script type="text/javascript">
{

if(document.forms[0].isUpdate == null)
    document.forms[0].operatorId.focus();
else
    document.forms[0].firstName.focus();
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
     /*if(!validateFormChar(theForm)){
      		return false;
     }*/
     
  /*
  if( document.forms.operatorUserForm.password.value == document.forms.operatorUserForm.confirmPassword.value )
  	{
  
    return validateOperatorUserListViewModel(theForm);
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
    
    if( document.forms.operatorUserForm.isUpdate != null && document.forms.operatorUserForm.isUpdate.value == 'true' )
  	{
		if( document.forms.operatorUserForm.password.value == document.forms.operatorUserForm.confirmPassword.value )
		{
			return validateOperatorUserListViewModel(document.forms.operatorUserForm) ;
	 	}	 
		alert("Password and Confirm Password do not match");
		document.forms.operatorUserForm.password.focus();
	    return false;
	}
	if( document.forms.operatorUserForm.password.value == '' )
	{
		alert("Password and confirmed password is required.");
		document.forms.operatorUserForm.password.focus();
	}
  	else if( document.forms.operatorUserForm.password.value == document.forms.operatorUserForm.confirmPassword.value )
	{
		return validateOperatorUserListViewModel(document.forms.operatorUserForm) ;
 	}	 
 	else
 	{
		alert("Password and Confirm Password do not match");
		document.forms.operatorUserForm.password.focus();
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

		<v:javascript formName="operatorUserListViewModel"
			staticJavascript="false" />
		<script type="text/javascript"
			src="<c:url value="/scripts/validator.jsp"/>"></script>
	</body>
</html>
