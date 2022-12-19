<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Asad Hayat-->
<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
<meta name="decorator" content="decorator2">
		<meta name="title" content="Customer" />

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


function popupCallback(src,popupName,columnHashMap)
{
   if(src=="supplierName")
  {
    document.forms.supplierUserForm.supplierId.value = columnHashMap.get('PK');
    document.forms.supplierUserForm.supplierName.value = columnHashMap.get('name');
  }
}
</script>
	</head>
	<body>
		<spring:bind path="customerListViewModel.*">
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



		<form id="customerForm" method="post" action="customerform.html"
			onsubmit="return onFormSubmit(this);">
			<table width="100%" border="0" cellpadding="0" cellspacing="1">

				<c:if test="${not empty param.customerId}">
					<input type="hidden" name="isUpdate" id="isUpdate" value="true" />
					<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_UPDATE%>" />
				</c:if>
				<c:if test="${empty param.customerId}">
					
					<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_CREATE%>" />
				</c:if>
				<input type="hidden" name="<%=PortalConstants.KEY_USECASE_ID%>"	value="<%=PortalConstants.CUSTOMER_FORM_USECASE_ID%>" />
				
					
				<spring:bind path="customerListViewModel.customerId">
					<input type="hidden" name="${status.expression}"
						value="${status.value}" />
				</spring:bind>
				<spring:bind path="customerListViewModel.appUserId">
					<input type="hidden" name="${status.expression}"
						value="${status.value}" />
				</spring:bind>
				<spring:bind path="customerListViewModel.createdBy">
					<input type="hidden" name="${status.expression}"
						value="${status.value}" />
				</spring:bind>

				<spring:bind path="customerListViewModel.updatedBy">
					<input type="hidden" name="${status.expression}"
						value="${status.value}" />
				</spring:bind>

				<spring:bind path="customerListViewModel.createdOn">
					<input type="hidden" name="${status.expression}"
						value="${status.value}" />
				</spring:bind>

				<spring:bind path="customerListViewModel.updatedOn">
					<input type="hidden" name="${status.expression}"
						value="${status.value}" />
				</spring:bind>

				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						<span style="color:#FF0000">*</span>First Name:&nbsp;
					</td>
					<td>
						<spring:bind path="customerListViewModel.firstName">
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
						<spring:bind path="customerListViewModel.lastName">
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
							<spring:bind path="customerListViewModel.username">
																
				<c:choose>
					<c:when test="${not empty errorCode}">
			        <input type="text" name="${status.expression}" class="textBox"
							maxlength="50" value="${status.value}"/>
			        </c:when>
			        <c:otherwise>
					<input type="text" name="${status.expression}" class="textBox"
							maxlength="50" value="${status.value}"<c:if test="${not empty param.customerId}">readonly="readOnly"</c:if>/>
			      	</c:otherwise>
			      </c:choose>
							</spring:bind>
						</td>
					</tr>
					<tr bgcolor="FBFBFB">
						<td align="right" bgcolor="F3F3F3" class="formText">
							<span style="color:#FF0000">*</span>Password:&nbsp;
						</td>
						<td>
							<spring:bind path="customerListViewModel.password">
								<input type="password" name="${status.expression}"
									class="textBox" maxlength="4000" value="" />
							</spring:bind>
						</td>
					</tr>
					<tr bgcolor="FBFBFB">
						<td align="right" bgcolor="F3F3F3" class="formText">
							<span style="color:#FF0000">*</span>Confirm Password:&nbsp;
						</td>
						<td>
							<input type="password" name="confirmPassword" class="textBox"
								maxlength="4000" value="${status.value}" />


						</td>
					</tr>
					<tr bgcolor="FBFBFB">
						<td align="right" bgcolor="F3F3F3" class="formText">
							Password Hint:&nbsp;
						</td>
						<td>
							<spring:bind path="customerListViewModel.passwordHint">
								<input type="text" name="${status.expression}" class="textBox"
									maxlength="50" value="${status.value}" />
							</spring:bind>
						</td>
					</tr>
				

				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						<span style="color:#FF0000">*</span>Connection Type:&nbsp;
					</td>
					<td>
						<spring:bind path="customerListViewModel.mobileTypeId">
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
						<spring:bind path="customerListViewModel.mobileNo">
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
						<spring:bind path="customerListViewModel.email">
							<input type="text" name="${status.expression}" class="textBox"
								maxlength="50" onkeyup="textAreaLengthCounter(this,50);" value="${status.value}" />
						</spring:bind>
					</td>
				</tr>

				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						Fax No:&nbsp;
					</td>
					<td>
						<spring:bind path="customerListViewModel.fax">
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
						<spring:bind path="customerListViewModel.dob">
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
						<spring:bind path="customerListViewModel.nic">
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
						<spring:bind path="customerListViewModel.motherMaidenName">
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
						<spring:bind path="customerListViewModel.address1">
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
						<spring:bind path="customerListViewModel.address2">
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
						<spring:bind path="customerListViewModel.city">
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
						<spring:bind path="customerListViewModel.state">
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
						<spring:bind path="customerListViewModel.zip">
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
						<spring:bind path="customerListViewModel.country">
							<input type="text" name="${status.expression}" class="textBox"
								maxlength="50" value="${status.value}"
								onkeypress="return maskAlphaWithSp(this,event)" />
						</spring:bind>
					</td>
				</tr>
				
				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						Reference Name 1:&nbsp;
					</td>
					<td>
						<spring:bind path="customerListViewModel.referringName1">
							<input type="text" name="${status.expression}" class="textBox"
								maxlength="50" value="${status.value}"
								onkeypress="return maskAlphaWithSp(this,event)" />
						</spring:bind>
					</td>
				</tr>


				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						Reference NIC 1:&nbsp;
					</td>
					<td>
						<spring:bind path="customerListViewModel.referringNic1">
							<input type="text" name="${status.expression}" class="textBox"
								maxlength="13" value="${status.value}"
								onkeypress="return maskNumber(this,event)" />
						</spring:bind>
					</td>
				</tr>


				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						Reference Name 2:&nbsp;
					</td>
					<td>
						<spring:bind path="customerListViewModel.referringName2">
							<input type="text" name="${status.expression}" class="textBox"
								maxlength="50" value="${status.value}"
								onkeypress="return maskAlphaWithSp(this,event)" />
						</spring:bind>
					</td>
				</tr>

				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						Reference NIC 2:&nbsp;
					</td>
					<td>
						<spring:bind path="customerListViewModel.referringNic2">
							<input type="text" name="${status.expression}" class="textBox"
								maxlength="13" value="${status.value}"
								onkeypress="return maskNumber(this,event)" />
						</spring:bind>
					</td>
				</tr>


				<tr>
					<td align="right" bgcolor="F3F3F3" class="formText">
						Description:&nbsp;
					</td>
					<td align="left" bgcolor="FBFBFB">
						<spring:bind path="customerListViewModel.description">
							<textarea name="${status.expression}" cols="50" rows="5"
								onkeyup="textAreaLengthCounter(this,250);" class="textBox"
								onkeypress="return maskCommon(this,event)" type="_moz">${status.value}</textarea>
						</spring:bind>
					</td>
				</tr>

				<tr>
					<td align="right" bgcolor="F3F3F3" class="formText">
						Comments:&nbsp;
					</td>
					<td align="left" bgcolor="FBFBFB">
						<spring:bind path="customerListViewModel.comments">
							<textarea name="${status.expression}" cols="50" rows="5"
								onkeyup="textAreaLengthCounter(this,250);" class="textBox"
								onkeypress="return maskCommon(this,event)" type="_moz">${status.value}</textarea>
						</spring:bind>
					</td>
				</tr>

				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">Registered:&nbsp;</td>
					<td align="left" class="formText">
						<spring:bind path="customerListViewModel.register">
							<input type="hidden" name="_${status.expression}" />
							<c:if test="${not empty param.customerId}">
								<input name="${status.expression}" type="radio" value="true"
									<c:if test="${status.value}">checked="checked"</c:if> />Yes&nbsp;&nbsp;&nbsp;&nbsp;
               <input name="${status.expression}" type="radio"
									value="false"
									<c:if test="${!status.value}">checked="checked"</c:if> />No
               </c:if>
							<c:if test="${empty param.customerId}">
								<input name="${status.expression}" type="radio" value="true"
									<c:if test="${!status.value && empty param._save}">checked="checked"</c:if>
									<c:if test="${status.value && not empty param._save}">checked="checked"</c:if>
									 />Yes&nbsp;&nbsp;&nbsp;&nbsp;
               <input name="${status.expression}" type="radio"
									value="false"
									<c:if test="${status.value && empty param._save}">checked="checked"</c:if> 
									<c:if test="${!status.value && not empty param._save}">checked="checked"</c:if>
									/>No
               </c:if>
						</spring:bind>
					</td>
				</tr>


  							 <tr>
    <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Verified:&nbsp;</td>
    <td width="51%" align="left" bgcolor="FBFBFB" class="formText">
		    <spring:bind path="customerListViewModel.verified">
			    <input name="${status.expression}" type="checkbox" value="true" 	

			    <c:if test="${status.value==true}">checked="checked"</c:if>
				<c:if test="${empty param.customerId && empty param._save }">checked="checked"</c:if> 
				<c:if test="${status.value==false && not empty param._save}">unchecked="checked"</c:if>
			    />			    
		     </spring:bind>
		    </td>
		  </tr>
				<tr>
					<td align="right" bgcolor="F3F3F3" class="formText">
						Account Enabled:&nbsp;
					</td>
					<td align="left" bgcolor="FBFBFB" class="formText">
						<spring:bind path="customerListViewModel.accountEnabled">
							<input type="hidden" name="_${status.expression}" />
							<input name="${status.expression}" type="checkbox" value="true"
							<c:if test="${status.value==true}">checked="checked"</c:if>
								<c:if test="${empty param.customerId && empty param._save }">checked="checked"</c:if> 
								<c:if test="${status.value==false && not empty param._save}">unchecked="checked"</c:if>
								 />
						</spring:bind>
					</td>
				</tr>
				<tr>
					<td align="right" bgcolor="F3F3F3" class="formText">
						Account Expired:&nbsp;
					</td>
					<td align="left" bgcolor="FBFBFB" class="formText">
						<spring:bind path="customerListViewModel.accountExpired">
							<input type="hidden" name="_${status.expression}" />
							<input name="${status.expression}" type="checkbox" value="true"
								<c:if test="${status.value==true}">checked="checked"</c:if>
								<c:if test="${empty param.customerId}">unchecked="checked"</c:if> />
						</spring:bind>
					</td>
				</tr>

				<tr>
					<td align="right" bgcolor="F3F3F3" class="formText">
						Account Locked:&nbsp;
					</td>
					<td align="left" bgcolor="FBFBFB" class="formText">
						<spring:bind path="customerListViewModel.accountLocked">
							<input type="hidden" name="_${status.expression}" />
							<input name="${status.expression}" type="checkbox" value="true"
<c:if test="${status.value==true}">checked="checked"</c:if>
								<c:if test="${status.value==false && not empty param._save}">unchecked="checked"</c:if> />
						</spring:bind>
					</td>
				</tr>
				<tr>
					<td align="right" bgcolor="F3F3F3" class="formText">
						Credentials Expired:&nbsp;
					</td>
					<td align="left" bgcolor="FBFBFB" class="formText">
						<spring:bind path="customerListViewModel.credentialsExpired">
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

		<GenericToolbar:toolbar formName="customerForm" />

					</td>
				</tr>
				</form>
			</table>

			<script type="text/javascript">

    

{

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
   
   
     var currServerDate = '<%=PortalDateUtils.currentFormattedDate("dd/MM/yyyy")%>';
   if( trim(theForm.dob.value) != '' && isDateGreater(theForm.dob.value,currServerDate)){
			alert('Future date of birth is not allowed.');
			document.getElementById('popcal').focus();
			return false;
		}
		
		 
    if( document.forms.customerForm.isUpdate != null && document.forms.customerForm.isUpdate.value == 'true' )
  	{
		if( document.forms.customerForm.password.value == document.forms.customerForm.confirmPassword.value )
		{
		return validateCustomerListViewModel(document.forms.customerForm) ;
	 	}	 
		alert("Password and Confirm Password do not match");
		document.forms.customerForm.password.focus();
	    return false;
	}
	if( document.forms.customerForm.password.value == '' )
	{
		alert("Password and Confirm Password is required");
		document.forms.customerForm.password.focus();
	}
  	else if( document.forms.customerForm.password.value == document.forms.customerForm.confirmPassword.value )
	{
return validateCustomerListViewModel(document.forms.customerForm) ;
 	}	 
 	else
 	{
		alert("Password and Confirm Password do not match");
		document.forms.customerForm.password.focus();
	}
  	
    return false;
  }
  
  Calendar.setup(
      {
        inputField  : "dob", // id of the input field
        ifFormat    : "%d/%m/%Y",      // the date format
        button      : "popcal"    // id of the button
      }
      );
}
</script>

			<v:javascript formName="customerListViewModel" staticJavascript="false" />
			<script type="text/javascript"
				src="<c:url
value="/scripts/validator.jsp"/>"></script>
	</body>
</html>
