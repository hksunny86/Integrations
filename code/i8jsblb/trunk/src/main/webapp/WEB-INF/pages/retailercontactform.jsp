<!--Author: Asad Hayat-->

<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>



<html>
	<head>
<meta name="decorator" content="decorator2">
		

		<v:javascript formName="retailerContactModel" staticJavascript="false" />
		<script type="text/javascript"
			src="<c:url value="/scripts/validator.jsp"/>"></script>

		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<meta name="title" content="Retailer Contact" />

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
			<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/toolbar.js"></script>
			

		<script language="javascript" type="text/javascript">
		
		function loadPartnerGroup()
		{	
			var obj = new AjaxJspTag.Select("/i8Microbank/partnerTypeFormRefDataController.html", {
			parameters: "id={retailerId},appUserTypeId=3,actionType=2",target: "partnerGroupId",
			source: "retailerId",errorFunction: error});
			obj.execute();
		}
		
	      function error(request)
	      {
	      	alert("An unknown error has occured. Please contact with the administrator for more details");
	      }
      </script>

	</head>
	<body>
	<%@include file="/common/ajax.jsp"%>
		<spring:bind path="retailerContactListViewFormModel.*">
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
			<form name="RetailerContactForm" method="POST"
				action="retailercontactformentry.html"
				onsubmit="return onFormSubmit(this)">

				<c:if test="${not empty param.retailerContactId}">
					<input type="hidden" name="isUpdate" id="isUpdate" value="true" />
					<spring:bind path="retailerContactListViewFormModel.areaId">
						<input type="hidden" name="${status.expression}"
							value="${status.value}" />
					</spring:bind>
					<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_UPDATE%>" />
					<spring:bind path="retailerContactListViewFormModel.retailerId">
						<input type="hidden" name="${status.expression}"
							value="${status.value}" />
					</spring:bind>
				</c:if>
				
				<c:if test="${empty param.retailerContactId}">
					
					<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_CREATE%>" />
				</c:if>
				<input type="hidden" name="<%=PortalConstants.KEY_USECASE_ID%>"	value="<%=PortalConstants.RETAILER_CONTACT_FORM_USECASE_ID%>" />
				

				<spring:bind
					path="retailerContactListViewFormModel.retailerContactId">
					<input type="hidden" name="${status.expression}"
						value="${status.value}" />
				</spring:bind>
				<spring:bind path="retailerContactListViewFormModel.versionNo">
					<input type="hidden" name="${status.expression}"
						value="${status.value}" />
				</spring:bind>
			<tr bgcolor="FBFBFB">
				<td colspan="2" align="center">
					&nbsp;
				</td>
			</tr>

			<tr bgcolor="#FBFBFB">
				<td width="50%" align="right" bgcolor="#F3F3F3" class="formText">
					<span style="color:#FF0000">*</span>Area:&nbsp;
				</td>
				<td width="50%" align="left">
					<spring:bind path="retailerContactListViewFormModel.areaId">
						<select name="${status.expression}" class="textBox"
							id="${status.expression}" <c:if test="${not empty param.retailerContactId}">disabled="disabled"</c:if>>
							
								<c:forEach items="${areaModelList}" var="areaModelList">
									<option value="${areaModelList.areaId}"
										<c:if test="${status.value == areaModelList.areaId}">selected="selected"</c:if>>
										${areaModelList.name}
									</option>
								</c:forEach>
						</select>
					</spring:bind>
				</td>
			</tr>

			<tr bgcolor="#FBFBFB">
				<td align="right" bgcolor="#F3F3F3" class="formText">
					<span style="color:#FF0000">*</span>Retailer:&nbsp;
				</td>
				<td align="left">
					<spring:bind path="retailerContactListViewFormModel.retailerId">
						        <select name="${status.expression}" class="textBox" id="${status.expression}" <c:if test="${not empty param.retailerContactId}">disabled="disabled"</c:if>>							        
							        <c:if test="${empty retailerModelList}">
							        	<option value="" />
							        </c:if>				
							     	<c:if test="${not empty retailerModelList}">
							        <c:forEach items="${retailerModelList}" var="retailerModel">							        
									<option value="${retailerModel.retailerId}" 
										<c:if test="${status.value == retailerModel.retailerId}">selected="selected"</c:if>>
										${retailerModel.name}
									</option>
								</c:forEach>
								</c:if>
						        </select>
					</spring:bind>
			</tr>
			<tr bgcolor="#FBFBFB">
				<td width="50%" align="right" bgcolor="#F3F3F3" class="formText">
					<span style="color:#FF0000">*</span>First Name:&nbsp;
				</td>
				<td width="50%" align="left">
					<spring:bind path="retailerContactListViewFormModel.firstName">
						<input type="text" name="${status.expression}"
							value="${status.value}" class="textBox" maxlength="50"
							onkeypress="return maskAlphaWithSp(this,event)" />
					</spring:bind>
				</td>
			</tr>
			<tr bgcolor="#FBFBFB">
				<td width="50%" align="right" bgcolor="#F3F3F3" class="formText">
					<span style="color:#FF0000">*</span>Last Name:&nbsp;
				</td>
				<td width="50%" align="left">
					<spring:bind path="retailerContactListViewFormModel.lastName">
						<input type="text" name="${status.expression}"
							value="${status.value}" class="textBox" maxlength="50"
							onkeypress="return maskAlphaWithSp(this,event)" />
					</spring:bind>
				</td>
			</tr>




			<tr bgcolor="FBFBFB">
				<td align="right" bgcolor="F3F3F3" class="formText">
					<span style="color:#FF0000">*</span>User Name:&nbsp;
				</td>
				<td>
					<spring:bind path="retailerContactListViewFormModel.username">

					<c:choose>
					<c:when test="${not empty errorCode}">
			        <input type="text" name="${status.expression}" class="textBox"
							maxlength="50" value="${status.value}"/>
			        </c:when>
			        <c:otherwise>
					<input type="text" name="${status.expression}" class="textBox"
							maxlength="50" value="${status.value}"<c:if test="${not empty param.retailerContactId}">readonly="readOnly"</c:if>/>
			      	</c:otherwise>
			      </c:choose>

						

					</spring:bind>
				</td>
			</tr>
			<tr bgcolor="FBFBFB">
				<td align="right" bgcolor="F3F3F3" class="formText">
					<span style="color:#FF0000">*</span> Password:&nbsp;
				</td>
				<td>
					<spring:bind path="retailerContactListViewFormModel.password">

						<input type="password" name="${status.expression}" class="textBox"
							maxlength="4000" value="" />
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
					<spring:bind path="retailerContactListViewFormModel.passwordHint">
						<input type="text" name="${status.expression}" class="textBox"
							maxlength="4000" value="${status.value}" />
					</spring:bind>
				</td>
			</tr>
			
			<tr>
					<td width="50%" align="right" bgcolor="F3F3F3" class="formText">
						<span style="color:#FF0000">*</span>Partner Group:&nbsp;
					</td>
					<td width="50%" align="left" bgcolor="FBFBFB">
						<spring:bind path="retailerContactListViewFormModel.partnerGroupId">
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
				<td align="right" bgcolor="F3F3F3" class="formText">
					<span style="color:#FF0000">*</span>Connection Type:&nbsp;
				</td>
				<td>
					<spring:bind path="retailerContactListViewFormModel.mobileTypeId">
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
				<td width="50%" align="right" bgcolor="#F3F3F3" class="formText">
					<span style="color:#FF0000">*</span>Mobile No:&nbsp;
				</td>
				<td width="50%" align="left">
					<spring:bind path="retailerContactListViewFormModel.mobileNo">
						<input type="text" name="${status.expression}"
							value="${status.value}" class="textBox" maxlength="11"
							onkeypress="return maskNumber(this,event)" />
					</spring:bind>
				</td>
			</tr>
			<tr bgcolor="FBFBFB">
				<td align="right" bgcolor="F3F3F3" class="formText">
					Email:&nbsp;
				</td>
				<td>
					<spring:bind path="retailerContactListViewFormModel.email">
						<input type="text" name="${status.expression}" class="textBox"
							maxlength="50" value="${status.value}" />
					</spring:bind>
				</td>
			</tr>

			<tr bgcolor="FBFBFB">
				<td align="right" bgcolor="F3F3F3" class="formText">
					Fax:&nbsp;
				</td>
				<td>
					<spring:bind path="retailerContactListViewFormModel.fax">
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
					<spring:bind path="retailerContactListViewFormModel.dob">
						<input type="text" name="${status.expression}" class="textBox"
							maxlength="10" value="${status.value}" readonly="readonly"/>
					</spring:bind>

					<img id="calButton" tabindex="4" name="popcal" align="top"
						style="cursor:pointer" src="${pageContext.request.contextPath}/images/cal.gif" border="0" />
					<img id="clearDate" tabindex="5" name="clearDate"
						title="Clear Date" onclick="javascript:$('dob').value=''"
						align="middle" style="cursor:pointer" src="${pageContext.request.contextPath}/images/refresh.png"
						border="0" />


				</td>
			</tr>

			<tr bgcolor="FBFBFB">
				<td align="right" bgcolor="F3F3F3" class="formText">
					NIC:&nbsp;
				</td>
				<td>
					<spring:bind path="retailerContactListViewFormModel.nic">
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
					<spring:bind
						path="retailerContactListViewFormModel.motherMaidenName">
						<input type="text" name="${status.expression}" class="textBox"
							maxlength="50" value="${status.value}"
							onkeypress="return maskAlphaWithSp(this,event)" />
					</spring:bind>
				</td>
			</tr>
			<tr bgcolor="#FBFBFB">
				<td width="50%" align="right" bgcolor="#F3F3F3" class="formText">
					Address1:&nbsp;
				</td>
				<td width="50%" align="left">
					<spring:bind path="retailerContactListViewFormModel.address1">
						<input type="text" name="${status.expression}"
							value="${status.value}" class="textBox"
							onkeyup="textAreaLengthCounter(this,250);" />
					</spring:bind>
				</td>
			</tr>
			<tr bgcolor="#FBFBFB">
				<td width="50%" align="right" bgcolor="#F3F3F3" class="formText">
					Address2:&nbsp;
				</td>
				<td width="50%" align="left">
					<spring:bind path="retailerContactListViewFormModel.address2">
						<input type="text" name="${status.expression}"
							value="${status.value}" class="textBox"
							onkeyup="textAreaLengthCounter(this,250);" />
					</spring:bind>
				</td>
			</tr>
			<tr bgcolor="#FBFBFB">
				<td width="50%" align="right" bgcolor="#F3F3F3" class="formText">
					City:&nbsp;
				</td>
				<td width="50%" align="left">
					<spring:bind path="retailerContactListViewFormModel.city">
						<input type="text" name="${status.expression}"
							value="${status.value}" class="textBox" maxlength="50"
							onkeypress="return maskAlphaWithSp(this,event)" />
					</spring:bind>
				</td>
			</tr>
			<tr bgcolor="#FBFBFB">
				<td width="50%" align="right" bgcolor="#F3F3F3" class="formText">
					State:&nbsp;
				</td>
				<td width="50%" align="left">
					<spring:bind path="retailerContactListViewFormModel.state">
						<input type="text" name="${status.expression}"
							value="${status.value}" class="textBox" maxlength="50"
							onkeypress="return maskAlphaWithSp(this,event)" />
					</spring:bind>
				</td>
			</tr>

			<tr bgcolor="FBFBFB">
				<td align="right" bgcolor="F3F3F3" class="formText">
					Zip Code:&nbsp;
				</td>
				<td>
					<spring:bind path="retailerContactListViewFormModel.zip">
						<input type="text" name="${status.expression}" class="textBox"
							maxlength="10" value="${status.value}"
							onkeypress="return maskAlphaNumeric(this,event)" />
					</spring:bind>
				</td>
			</tr>

			<tr bgcolor="#FBFBFB">
				<td width="50%" align="right" bgcolor="#F3F3F3" class="formText">
					Country:&nbsp;
				</td>
				<td width="50%" align="left">
					<spring:bind path="retailerContactListViewFormModel.country">
						<input type="text" name="${status.expression}"
							value="${status.value}" class="textBox" maxlength="50"
							onkeypress="return maskAlphaWithSp(this,event)" />
					</spring:bind>
				</td>
			</tr>



			<tr bgcolor="FBFBFB">
				<td width="50%" align="right" bgcolor="#F3F3F3" class="formText">
					Balance:&nbsp;
				</td>
				<td width="50%" align="left">
					<spring:bind path="retailerContactListViewFormModel.balance">
						
						<c:if test="${not empty param.retailerContactId}">
		<c:if test="${not empty param.retailerContactId}">
					<c:choose>
						<c:when test="${balance==0}">
								<input type="text" name="${status.expression}" class="textBox" value="0.0"  readonly="readonly" onkeypress="return maskNumber(this,event)" maxlength="14"/>	  		
						</c:when>
						<c:otherwise>
								<input type="text" name="${status.expression}" class="textBox" readonly="readonly" value="<c:out value="${balance}"></c:out>"  onkeypress="return maskNumber(this,event)" maxlength="14"/>  										
						</c:otherwise>
					</c:choose>
		
	  </c:if>
	  </c:if>
	  
	  <c:if test="${empty param.retailerContactId}">
		<input type="text" name="${status.expression}" readonly="readonly" class="textBox" value="0.0"   onkeypress="return maskNumber(this,event)" maxlength="14"/>	  
	  </c:if>
						
					</spring:bind>
				</td>
			</tr>

			
			<tr>
				<td align="right" bgcolor="F3F3F3" class="formText">
					Description:&nbsp;
				</td>
				<td align="left" bgcolor="FBFBFB">
					<spring:bind path="retailerContactListViewFormModel.description">
						<textarea name="${status.expression}" cols="50" rows="5"
							onkeyup="textAreaLengthCounter(this,250);" onkeypress="return maskCommon(this,event)" class="textBox">${status.value}</textarea>
					</spring:bind>
				</td>
			</tr>
			<tr>
				<td align="right" bgcolor="F3F3F3" class="formText">
					Comments:&nbsp;
				</td>
				<td align="left" bgcolor="FBFBFB">
					<spring:bind path="retailerContactListViewFormModel.comments">
						<textarea name="${status.expression}" cols="50" rows="5"
							onkeyup="textAreaLengthCounter(this,250);" onkeypress="return maskCommon(this,event)" class="textBox">${status.value}</textarea>
					</spring:bind>
				</td>
			</tr>
			<tr>
				<td width="49%" height="16" align="right" bgcolor="F3F3F3"
					class="formText">
					Head:&nbsp;
				</td>
				<td width="51%" align="left" bgcolor="FBFBFB" class="formText">
					<spring:bind path="retailerContactListViewFormModel.head">
						<input name="${status.expression}" type="checkbox" value="true"
							<c:if test="${status.value==true}">checked="checked"</c:if> />
					</spring:bind>
				</td>
			</tr>


			

  				 <tr>
    <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Verified:&nbsp;</td>
    <td width="51%" align="left" bgcolor="FBFBFB" class="formText">
		    <spring:bind path="retailerContactListViewFormModel.verified">
			    <input name="${status.expression}" type="checkbox" value="true" 	
			    <c:if test="${status.value==true}">checked="checked"</c:if>
			    <c:if test="${empty param.retailerContactId && empty param._save }">checked="checked"</c:if>
			    <c:if test="${status.value==false && not empty param._save }">unchecked="checked"</c:if>
			    />			    
		     </spring:bind>
		    </td>
		  </tr>
			<tr>
				<td align="right" bgcolor="F3F3F3" class="formText">
					Account Enabled:&nbsp;
				</td>
				<td align="left" bgcolor="FBFBFB" class="formText">
					<spring:bind path="retailerContactListViewFormModel.accountEnabled">
						<input name="${status.expression}" type="checkbox" value="true"
							<c:if test="${status.value==true}">checked="checked"</c:if>
							<c:if test="${empty param.retailerContactId && empty param._save }">checked="checked"</c:if> 
							<c:if test="${status.value==false && not empty param._save }">unchecked="checked"</c:if>
							/>
					</spring:bind>
				</td>
			</tr>
			<tr>
				<td align="right" bgcolor="F3F3F3" class="formText">
					Account Expired:&nbsp;
				</td>
				<td align="left" bgcolor="FBFBFB" class="formText">
					<spring:bind path="retailerContactListViewFormModel.accountExpired">
						<input name="${status.expression}" type="checkbox" value="true"
							<c:if test="${status.value==true}">checked="checked"</c:if>
							<c:if test="${empty param.retailerContactId}">unchecked="checked"</c:if> />
					</spring:bind>
				</td>
			</tr>

			<tr>
				<td align="right" bgcolor="F3F3F3" class="formText">
					Account Locked:&nbsp;
				</td>
				<td align="left" bgcolor="FBFBFB" class="formText">
					<spring:bind path="retailerContactListViewFormModel.accountLocked">
						<input name="${status.expression}" type="checkbox" value="true"
							<c:if test="${status.value==true}">checked="checked"</c:if>
							<c:if test="${empty param.retailerContactId}">unchecked="checked"</c:if> />
					</spring:bind>
				</td>
			</tr>
			<tr>
				<td align="right" bgcolor="F3F3F3" class="formText">
					Credentials Expired:&nbsp;
				</td>
				<td align="left" bgcolor="FBFBFB" class="formText">
					<spring:bind
						path="retailerContactListViewFormModel.credentialsExpired">
						<input name="${status.expression}" type="checkbox" value="true"
							<c:if test="${status.value==true}">checked="checked"</c:if>
							<c:if test="${empty param.retailerContactId}">unchecked="checked"</c:if> />
					</spring:bind>
				</td>
			</tr>
			<tr bgcolor="FBFBFB">
				<td colspan="2" align="center">
				
				<input type= "button" name = "_save" value="  Save  " onclick="javascript:onSave(document.forms.RetailerContactForm,null);" class="button"/>		  
 <c:if test="${empty param.retailerContactId}">
<input type= "button" name = "_cancel" value=" Cancel " onclick="javascript:revertDropdowns(document.forms.RetailerContactForm);" class="button"/>
</c:if>
 <c:if test="${not empty param.retailerContactId}">
<input type= "reset" name = "_cancel" value=" Cancel "  class="button" />
</c:if>
					
				</td>
			</tr>
			</form>
		</table>


		<ajax:select source="areaId" target="retailerId"
			baseUrl="${contextPath}/retailercontactformrefdata.html"
			parameters="areaId={areaId},rType=1" postFunction="loadPartnerGroup" errorFunction="error" />
			
			<ajax:select source="retailerId" target="partnerGroupId"
			baseUrl="${contextPath}/partnerTypeFormRefDataController.html"
			parameters="id={retailerId},appUserTypeId=3,actionType=2"  errorFunction="error" />
			

		<script type="text/javascript" language="javascript">
		
		function revertDropdowns(theForm)
{
	
	theForm.reset();
	var obj = new AjaxJspTag.Select(
	"/i8Microbank/retailercontactformrefdata.html", {
	parameters: "areaId={areaId},rType=1",
	target: "retailerId",
	postFunction: "loadPartnerGroup",
	source: "areaId",
	errorFunction: error
	});
	obj.execute();
	
	

}
		
		
		
function popupCallback(src,popupName,columnHashMap,param)
{
  if (src=="retailerName")
  {
    
    document.forms.RetailerContactForm.retailerId.value = columnHashMap.get('PK');
    document.forms.RetailerContactForm.retailerName.value = columnHashMap.get('name');
  }
  
  
  if (src=="areaName")
  {
    document.forms.RetailerContactForm.areaId.value = columnHashMap.get('PK');
    document.forms.RetailerContactForm.areaName.value = columnHashMap.get('name');
  }
}


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

function onFormSubmit(Form)
{
   /*if(!validateFormChar(Form)){
      return false;
   }*/
  /*
  if (validateRetailerContactModel(document.RetailerContactForm)==true)
  {
    if (confirm('Are you sure you want to save this record?')==true)
    {
      document.RetailerContactForm.submit();
    }
    else
    {
      return false;
    }
  }
  else
  {
   return false;
  }
  */
   
      var currServerDate = '<%=PortalDateUtils.currentFormattedDate("dd/MM/yyyy")%>';
   if( trim(Form.dob.value) != '' && isDateGreater(Form.dob.value,currServerDate)){
			alert('Future date of birth is not allowed.');
			document.getElementById('calButton').focus();
			return false;
		}
      if( document.forms.RetailerContactForm.isUpdate != null && document.forms.RetailerContactForm.isUpdate.value == 'true' )
  	{
		if( document.forms.RetailerContactForm.password.value == document.forms.RetailerContactForm.confirmPassword.value )
		{			
			
			return validateRetailerContactModel(document.forms.RetailerContactForm);
	 	}	 
		alert("Password and Confirm Password do not match");
		document.forms.RetailerContactForm.password.focus();
	    return false;
	}
	if( document.forms.RetailerContactForm.password.value == '' )
	{
		alert("Password and Confirm Password are required.");
		document.forms.RetailerContactForm.password.focus();
		//return validateRetailerContactModel(document.RetailerContactForm);
	}
  	else if( document.forms.RetailerContactForm.password.value == document.forms.RetailerContactForm.confirmPassword.value )
	{
		return validateRetailerContactModel(document.RetailerContactForm);
 	}	 
 	else
 	{
		alert("Password and Confirm Password do not match");
		document.forms.RetailerContactForm.password.focus();
	}
  	
    return false;
}
    
    highlightFormElements();
    if( document.forms[0].isUpdate== null )
        document.forms[0].areaId.focus();
    else
        document.forms[0].firstName.focus();
Calendar.setup(
      {
        inputField  : "dob", // id of the input field
        ifFormat    : "%d/%m/%Y",      // the date format
        button      : "calButton"    // id of the button
      }
      );
</script>


	</body>
</html>
