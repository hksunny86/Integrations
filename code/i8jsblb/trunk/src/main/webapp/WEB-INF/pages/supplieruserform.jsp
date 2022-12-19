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
<meta name="title" content="Supplier User"/>

		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
		<link rel="stylesheet" type="text/css"
			href="styles/deliciouslyblue/calendar.css" />

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
<script type="text/javascript">

		 function error(request)
		{
     	alert("An unknown error has occured. Please contact with the administrator for more details");
		}

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
   <%@include file="/common/ajax.jsp"%>
<spring:bind path="supplierUserFormListViewModel.*">
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



<form id="supplierUserForm" method="post" action="supplieruserform.html" onsubmit="return onFormSubmit(this);">
<table width="100%" border="0" cellpadding="0" cellspacing="1">

 <c:if test="${not empty param.supplierUserId}">
          <input type="hidden" name="isUpdate" id="isUpdate" value="true"/>
          <spring:bind path="supplierUserFormListViewModel.supplierId">
               <input type="hidden" name="${status.expression}" value="${status.value}"/>
          </spring:bind>
        </c:if>
        <spring:bind path="supplierUserFormListViewModel.supplierUserId">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>


  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Supplier:&nbsp;</td>
    <td>
    <spring:bind path="supplierUserFormListViewModel.supplierId">
							<select name="${status.expression}" class="textBox" id="${status.expression}" <c:if test="${not empty param.supplierUserId}">disabled='disabled'</c:if>>
								<c:forEach items="${supplierModelList}"
									var="supplierModelList">
									<option value="${supplierModelList.supplierId}"
										<c:if test="${status.value == supplierModelList.supplierId}">selected="selected"</c:if>>
										${supplierModelList.name}
									</option>
								</c:forEach>
							</select>
        ${status.errorMessage}
      </spring:bind>
    </td>
  </tr>

  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>First Name:&nbsp;</td>
    <td><spring:bind path="supplierUserFormListViewModel.firstName">
      <input type="text" name="${status.expression}" class="textBox" maxlength="50" value="${status.value}" onkeypress="return maskAlphaWithSp(this,event)"/>
      </spring:bind>
    </td>
  </tr>
  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Last Name:&nbsp;</td>
    <td><spring:bind path="supplierUserFormListViewModel.lastName">
      <input type="text" name="${status.expression}" class="textBox" maxlength="50" value="${status.value}" onkeypress="return maskAlphaWithSp(this,event)"/>
      </spring:bind>
    </td>
  </tr>
  
    
				
				
				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>
						User Name:&nbsp;
					</td>
					<td>
						<spring:bind path="supplierUserFormListViewModel.username">
						
							<input type="text" name="${status.expression}" class="textBox"
								maxlength="50" value="${status.value}" 
								
							<c:if test="${not empty param.supplierUserId}">readonly="readOnly"</c:if>	
								/>
						</spring:bind>
					</td>
				</tr>
				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>
						Password:&nbsp;
					</td>
					<td>
						<spring:bind path="supplierUserFormListViewModel.password">
						
							<input type="password" name="${status.expression}" class="textBox"
								maxlength="4000" value="" />
						</spring:bind>
					</td>
				</tr>
				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>
						Confirm Password:&nbsp;
					</td>
					<td>
						
						<input type="password" name="confirmPassword" class="textBox" maxlength="4000" />
						
					</td>
				</tr>
				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						Password Hint:&nbsp;
					</td>
					<td>
						<spring:bind path="supplierUserFormListViewModel.passwordHint">
							<input type="text" name="${status.expression}" class="textBox"
								maxlength="4000" value="${status.value}" />
						</spring:bind>
					</td>
				</tr>
				
				


    <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Partner Group:&nbsp;</td>
    <td>
						<spring:bind path="supplierUserFormListViewModel.partnerGroupId">
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
    <td>
    <spring:bind path="supplierUserFormListViewModel.mobileTypeId">
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
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Mobile No:&nbsp;</td>
    <td><spring:bind path="supplierUserFormListViewModel.mobileNo">
      <input type="text" name="${status.expression}" class="textBox" maxlength="11" value="${status.value}"onkeypress="return maskNumber(this,event)"/>
      </spring:bind>
    </td>
  </tr>
  
    <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">Email:&nbsp;</td>
    <td><spring:bind path="supplierUserFormListViewModel.email">
							<input type="text" name="${status.expression}" class="textBox"
								maxlength="50" value="${status.value}" />
						</spring:bind>
    </td>
  </tr>
  
  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">Fax:&nbsp;</td>
    <td><spring:bind path="supplierUserFormListViewModel.fax">
							<input type="text" name="${status.expression}" class="textBox"
								maxlength="11" value="${status.value}" onkeypress="return maskNumber(this,event)"/>
						</spring:bind>
    </td>
  </tr>
  
  
  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">Date of Birth:&nbsp;</td>
    <td>
				    <spring:bind path="supplierUserFormListViewModel.dob">
							<input type="text" name="${status.expression}" readonly="readonly" class="textBox" id="${status.expression}"
								maxlength="10" value="${status.value}" />
						</spring:bind>
						<img id="sDate" tabindex="4"  name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0"  />
						<img id="clearDate" tabindex="5" name="clearDate" title="Clear Date" onclick="javascript:$('dob').value=''"  align="middle" style="cursor:pointer" src="images/refresh.png" border="0" /> 
						
    
    </td>
  </tr>
  
  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">NIC:&nbsp;</td>
    <td><spring:bind path="supplierUserFormListViewModel.nic">
							<input type="text" name="${status.expression}" class="textBox"
								maxlength="13" value="${status.value}" onkeypress="return maskNumber(this,event)"/>
						</spring:bind>
    </td>
  </tr>
  
   <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">Mother Maiden Name:&nbsp;</td>
    <td><spring:bind path="supplierUserFormListViewModel.motherMaidenName">
							<input type="text" name="${status.expression}" class="textBox"
								maxlength="50" value="${status.value}" onkeypress="return maskAlphaWithSp(this,event)"/>
						</spring:bind>
    </td>
  </tr>
  
  
  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">Address1:&nbsp;</td>
    <td><spring:bind path="supplierUserFormListViewModel.address1">
      <input type="text" name="${status.expression}" class="textBox"  value="${status.value}" maxlength="250" />
      </spring:bind>
    </td>
  </tr>

  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">Address2:&nbsp;</td>
    <td><spring:bind path="supplierUserFormListViewModel.address2">
      <input type="text" name="${status.expression}" class="textBox"  value="${status.value}" maxlength="250" />
      </spring:bind>
    </td>
  </tr>
  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">City:&nbsp;</td>
    <td><spring:bind path="supplierUserFormListViewModel.city">
      <input type="text" name="${status.expression}" class="textBox" maxlength="50" value="${status.value}" onkeypress="return maskAlphaWithSp(this,event)"/>
      </spring:bind>
    </td>
  </tr>
  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">State:&nbsp;</td>
    <td><spring:bind path="supplierUserFormListViewModel.state">
      <input type="text" name="${status.expression}" class="textBox" maxlength="50" value="${status.value}" onkeypress="return maskAlphaWithSp(this,event)"/>
      </spring:bind>
    </td>
  </tr>

  
 
  
  
<tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">Zip Code:&nbsp;</td>
    <td><spring:bind path="supplierUserFormListViewModel.zip">
							<input type="text" name="${status.expression}" class="textBox"
								maxlength="10" value="${status.value}" onkeypress="return maskAlphaNumeric(this,event)"/>
						</spring:bind>
    </td>
  </tr>
  
  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">Country:&nbsp;</td>
    <td><spring:bind path="supplierUserFormListViewModel.country">
      <input type="text" name="${status.expression}" class="textBox" maxlength="50" value="${status.value}" onkeypress="return maskAlphaWithSp(this,event)"/>
      </spring:bind>
    </td>
  </tr>
  <tr>
    <td align="right" bgcolor="F3F3F3" class="formText">Description:&nbsp;</td>
    <td align="left" bgcolor="FBFBFB"><spring:bind path="supplierUserFormListViewModel.description"><textarea name="${status.expression}" cols="50" rows="5" onkeyup="textAreaLengthCounter(this,250);" onkeypress="return maskCommon(this,event)" class="textBox">${status.value}</textarea></spring:bind></td>
  </tr>
  <tr>
    <td align="right" bgcolor="F3F3F3" class="formText">Comments:&nbsp;</td>
    <td align="left" bgcolor="FBFBFB"><spring:bind path="supplierUserFormListViewModel.comments"><textarea name="${status.expression}" cols="50" on rows="5"  onkeyup="textAreaLengthCounter(this,250);" onkeypress="return maskCommon(this,event)" class="textBox">${status.value}</textarea></spring:bind></td>
  </tr>
  				 <tr>
    <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Verified:&nbsp;</td>
    <td width="51%" align="left" bgcolor="FBFBFB" class="formText">
		    <spring:bind path="supplierUserFormListViewModel.verified">
			    <input name="${status.expression}" type="checkbox" value="true" 	
			    <c:if test="${status.value==true}">checked="checked"</c:if>
				<c:if test="${empty param.supplierUserId && empty param._save }">checked="checked"</c:if> 
				<c:if test="${status.value==false && not empty param._save}">unchecked="checked"</c:if>
			    />			    
		     </spring:bind>
		    </td>
		  </tr>
  <tr>
    <td   align="right" bgcolor="F3F3F3" class="formText">Account Enabled:&nbsp;</td>
    <td  align="left" bgcolor="FBFBFB" class="formText">
    <spring:bind path="supplierUserFormListViewModel.accountEnabled">
          <input type="hidden" name="_${status.expression}"/>
       <input name="${status.expression}" type="checkbox" value="true" 	
<c:if test="${status.value==true}">checked="checked"</c:if>
								<c:if test="${empty param.supplierUserId && empty param._save }">checked="checked"</c:if> 
								<c:if test="${status.value==false && not empty param._save}">unchecked="checked"</c:if>
	    />
	    </spring:bind>		
    </td>
  </tr>
  <tr>
    <td  align="right" bgcolor="F3F3F3" class="formText">Account Expired:&nbsp;</td>
    <td  align="left" bgcolor="FBFBFB" class="formText">
    <spring:bind path="supplierUserFormListViewModel.accountExpired">
          <input type="hidden" name="_${status.expression}"/>
       <input name="${status.expression}" type="checkbox" value="true" 	
<c:if test="${status.value==true}">checked="checked"</c:if>
								<c:if test="${empty param.customerId}">unchecked="checked"</c:if>
	    />		
    </spring:bind>
   </td>
  </tr>
  
  <tr>
    <td  align="right" bgcolor="F3F3F3" class="formText">Account Locked:&nbsp;</td>
    <td  align="left" bgcolor="FBFBFB" class="formText">
    <spring:bind path="supplierUserFormListViewModel.accountLocked">
      <input type="hidden" name="_${status.expression}"/>
       <input name="${status.expression}" type="checkbox" value="true" 	
			    <c:if test="${status.value==true}">checked="checked"</c:if>
								<c:if test="${status.value==false && not empty param._save}">unchecked="checked"</c:if>
	    />		
     </spring:bind>
    </td>
  </tr>
  <tr>
    <td  align="right" bgcolor="F3F3F3" class="formText">Credentials Expired:&nbsp;</td>
    <td  align="left" bgcolor="FBFBFB" class="formText">
    <spring:bind path="supplierUserFormListViewModel.credentialsExpired">
      <input type="hidden" name="_${status.expression}"/>
         <input name="${status.expression}" type="checkbox" value="true" 	
			    <c:if test="${status.value==true}">checked="checked"</c:if>
								<c:if test="${status.value==false && not empty param._save}">unchecked="checked"</c:if>
	    />		
    </spring:bind>
   </td>
  </tr>
  
  <tr bgcolor="FBFBFB">
            <td colspan="2" align="center">
            <input name="_save" type="submit" class="button" value="  Save  " onclick="bCancel=false"/>
            <input name="_cancel" type="button" class="button" value=" Cancel " onclick="/*window.location.href='supplierusermanagement.html';*/document.forms[0].reset();"/>
			      
			      
</td>
          </tr>
</form>
</table>

		      <ajax:select 
			source="supplierId" target="partnerGroupId"
			baseUrl="${contextPath}/partnerTypeFormRefDataController.html"
			parameters="appUserTypeId=5,actionType=2,id={supplierId}" errorFunction="error"/>

<script type="text/javascript">
if(document.forms[0].isUpdate == null)
    document.forms[0].supplierId.focus();
else
    document.forms[0].firstName.focus();
highlightFormElements();

      Calendar.setup(
      {
        inputField  : "dob", // id of the input field
        ifFormat    : "%d/%m/%Y",      // the date format
        button      : "sDate",    // id of the button
        showsTime   :   false
      }
      );

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
   /*if(!validateFormChar(theForm)){
       return false;*/
   
   var currServerDate = '<%=PortalDateUtils.currentFormattedDate("dd/MM/yyyy")%>';
   if( trim(theForm.dob.value) != '' && isDateGreater(theForm.dob.value,currServerDate)){
			alert('Future date of birth is not allowed.');
			document.getElementById('sDate').focus();
			return false;
		}
		
    if( document.forms.supplierUserForm.isUpdate != null && document.forms.supplierUserForm.isUpdate.value == 'true' )
  	{
		if( document.forms.supplierUserForm.password.value == document.forms.supplierUserForm.confirmPassword.value )
		{
			return validateSupplierUserFormListViewModel(document.forms.supplierUserForm) ;
	 	}	 
		alert("Password and Confirm Password do not match");
		document.forms.supplierUserForm.password.focus();
	    return false;
	}
	if( document.forms.supplierUserForm.password.value == '' )
	{
		alert("Password and confirmed password is required");
		document.forms.supplierUserForm.password.focus();
	}
  	else if( document.forms.supplierUserForm.password.value == document.forms.supplierUserForm.confirmPassword.value )
	{
		return validateSupplierUserFormListViewModel(document.forms.supplierUserForm) ;
 	}	 
 	else
 	{
		alert("Password and Confirm Password do not match");
		document.forms.supplierUserForm.password.focus();
	}
  	
    return false;
    
  }
  
  
}
</script>

<v:javascript formName="supplierUserFormListViewModel" staticJavascript="false"/>
<script type="text/javascript" src="<c:url
value="/scripts/validator.jsp"/>"></script>

</body>
</html>
