<!--Author: Asad hayathttp://www.leviton.com/sections/prodinfo/newprod/npleadin.htm-->
<%@ page import="com.inov8.microbank.common.util.PortalConstants"%>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

	<head>
<meta name="decorator" content="decorator2">
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />


		<meta name="title" content="Edit User Information" />
	</head>
	<body>
		<spring:bind path="changemobileListViewModel.*">
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
	

		<html:form id="changemobileForm" method="post"
			action="p_changemobileform.html"
			commandName="changemobileListViewModel"
			onsubmit="return onFormSubmit(this)">
			<table width="100%" border="0" cellpadding="0" cellspacing="1">

				<c:if test="${not empty param.appUserId}">
					<input type="hidden" name="isUpdate" id="isUpdate" value="true" />
				</c:if>
				
				<input type="hidden" name="appUserId" value="${param.appUserId}" >

				<input type="hidden" name="<%=PortalConstants.KEY_USECASE_ID %>"
					value="<%=PortalConstants.MNO_CHANGE_MODILE_NO_USECASE_ID%>" />
				<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID %>"
					value="3" />



				<tr bgcolor="FBFBFB">
					<td colspan="2" align="center">
						&nbsp;
					</td>
				</tr>
				<tr>
					<td width="50%" align="right" bgcolor="F3F3F3" class="formText">
						Customer ID:&nbsp;
					</td>
					<td width="50%" align="left" bgcolor="FBFBFB">
						<c:if test="${not empty changemobileListViewModel.userId}">
                            <c:out value="${changemobileListViewModel.userId}"></c:out>
                            <html:hidden path="userId"/>
						</c:if>
					</td>
				</tr>

				<tr>
					<td width="50%" align="right" bgcolor="F3F3F3" class="formText">
						&nbsp;First Name:&nbsp;
					</td>
					<td width="50%" align="left" bgcolor="FBFBFB">
						<c:if test="${not empty changemobileListViewModel.firstName}">
                            <c:out value="${changemobileListViewModel.firstName}"/>
						</c:if>
						<c:if test="${empty changemobileListViewModel.firstName}">
                            <c:out value="${changemobileListViewModel.firstName}"/>
						</c:if>
                        <html:hidden path="firstName"/>
					</td>
				</tr>

				<tr>
					<td width="50%" align="right" bgcolor="F3F3F3" class="formText">
						&nbsp;Last Name:&nbsp;
					</td>
					<td width="50%" align="left" bgcolor="FBFBFB">

						<c:if test="${not empty changemobileListViewModel.lastName}">
                            <c:out value="${changemobileListViewModel.lastName}"/>
						</c:if>
						<c:if test="${empty changemobileListViewModel.lastName}">
                            <c:out value="${changemobileListViewModel.lastName}"/>
						</c:if>
                        <html:hidden path="lastName"/>
					</td>
				</tr>

				<tr>
					<td width="50%" align="right" bgcolor="F3F3F3" class="formText">
						<span style="color:#FF0000">*</span>Mobile #:&nbsp;
					</td>
					<td width="50%" align="left" bgcolor="FBFBFB">

						<c:if test="${not empty changemobileListViewModel.mobileNo}">
							<html:input path="mobileNo" tabindex="1" id="mobileNo" cssClass="textBox" onkeypress="return maskNumber(this,event)" maxlength="11"/>
						</c:if>
						<c:if test="${empty changemobileListViewModel.mobileNo}">
							<html:input path="mobileNo" tabindex="1" id="mobileNo" onkeypress="return maskNumber(this,event)" cssClass="textBox" maxlength="11"/>
						</c:if>

					</td>
				</tr>
					<tr>
					<td width="50%" align="right" bgcolor="F3F3F3" class="formText">
						<span style="color:#FF0000">*</span>Connection Type:&nbsp;
					</td>
					<td width="50%" align="left" bgcolor="FBFBFB">

					<html:select path="mobileTypeId" cssClass="textBox" tabindex="2" >
                 		<html:options items="${MobileTypeModelList}" itemLabel="name" itemValue="mobileTypeId"/>
			 		</html:select>
					</td>
				</tr>


				<tr>
					<td align="right" bgcolor="F3F3F3" class="formText">
						Address 1:&nbsp;
					</td>
					<td align="left" bgcolor="FBFBFB">

						<c:if test="${not empty changemobileListViewModel.address1}">
                            <c:out value="${changemobileListViewModel.address1}"></c:out>
						</c:if>
						<c:if test="${empty changemobileListViewModel.address1}">
						    <c:out value="${changemobileListViewModel.address1}"></c:out>
						</c:if>
						<html:hidden path="address1"/>
					</td>
				</tr>

				<tr>
					<td align="right" bgcolor="F3F3F3" class="formText">
						Address 2:&nbsp;
					</td>
					<td align="left" bgcolor="FBFBFB">

						<c:if test="${not empty changemobileListViewModel.address2}">
                            <c:out value="${changemobileListViewModel.address2}"></c:out>
						</c:if>
						<c:if test="${empty changemobileListViewModel.address2}">
                            <c:out value="${changemobileListViewModel.address2}"></c:out>
						</c:if>
						<html:hidden path="address2"/>
					</td>
				</tr>

				<tr>
					<td align="right" bgcolor="F3F3F3" class="formText">
						City:&nbsp;
					</td>
					<td align="left" bgcolor="FBFBFB">

						<c:if test="${not empty changemobileListViewModel.city}">
                            <c:out value="${changemobileListViewModel.city}"></c:out>
						</c:if>
						<c:if test="${empty changemobileListViewModel.city}">
							<c:out value="${changemobileListViewModel.city}"></c:out>
						</c:if>
						<html:hidden path="city"/>
					</td>
				</tr>

				<tr>
					<td align="right" bgcolor="F3F3F3" class="formText">
						State:&nbsp;
					</td>
					<td align="left" bgcolor="FBFBFB">

						<c:if test="${not empty changemobileListViewModel.state}">
                            <c:out value="${changemobileListViewModel.state}"></c:out>
						</c:if>
						<c:if test="${empty changemobileListViewModel.state}">
							<c:out value="${changemobileListViewModel.state}"></c:out>
						</c:if>
						<html:hidden path="state"/>
					</td>
				</tr>


				<tr>
					<td align="right" bgcolor="F3F3F3" class="formText">
						Country:&nbsp;
					</td>
					<td align="left" bgcolor="FBFBFB">

						<c:if test="${not empty changemobileListViewModel.country}">
                            <c:out value="${changemobileListViewModel.country}"></c:out>
						</c:if>
						<c:if test="${empty changemobileListViewModel.country}">
                            <c:out value="${changemobileListViewModel.country}"></c:out>
						</c:if>
						<html:hidden path="country"/>
					</td>
				</tr>


				<tr>
					<td width="50%" align="right" bgcolor="F3F3F3" class="formText">
						NIC #:&nbsp;
					</td>
					<td width="50%" align="left" bgcolor="FBFBFB">

						

							<c:if test="${not empty changemobileListViewModel.nic}">
                                <c:out value="${changemobileListViewModel.nic}"></c:out>
							</c:if>
							<c:if test="${empty changemobileListViewModel.nic}">
                                <c:out value="${changemobileListViewModel.nic}"></c:out>
							</c:if>
                            <html:hidden path="nic"/>
						    
					</td>
				</tr>
				
				<tr>
					<td align="right" bgcolor="F3F3F3" class="formText">
						 &nbsp;&nbsp;
					</td>
					<td align="left" bgcolor="FBFBFB">

						<c:if test="${not empty param.appUserId}">
	          <input type="button" tabindex="3"  class="button" value="Update"   onclick="javascript:onSave(document.forms.changemobileForm,null);" />
    	      <input type="button" tabindex="4"  class="button" value="Cancel"   onclick="javascript: document.forms[1].submit();" />


						</c:if>
						
					</td>
				</tr>
			</table>
		</html:form>
        <c:if test="${not empty param.appUserId}">
            <form action="${contextPath}/p_changemobilenomanagement.html?actionId=${retriveAction}" name="cancelChangemobileForm" method="post">
                <input type="hidden" name="userId" id="userId" value="<c:out value="${searchMfsId}"/>"/>
                <input type="hidden" name="mobileNo" id="mobileNo" value="<c:out value="${searchMobileNo}"/>"/>
                
            </form>
        </c:if>
		<script type="text/javascript">
	
highlightFormElements();
document.forms[0].mobileNo.focus();
function onFormSubmit(theForm) {
 return /*validateChangemobileListViewModel(theForm)*/ validateChangeMobileForm();
}
function validateChangeMobileForm(){
	var _form = document.forms[0];
	var _mobileNo = _form.mobileNo;
	var _mobileTypeId = _form.mobileTypeId;
	
    if(_mobileNo.value == ''){
        alert('Please enter Mobile #.');
    	_mobileNo.focus();
        return false;
    }

	if(!isNumberWithZeroes(_mobileNo) || _mobileNo.value.length < 11 ){
		alert('Please enter correct Mobile #.');
 		_mobileNo.focus();
 		return false;
 	}
    
    if(_mobileTypeId.value == ''){
        alert('Please select Connection Type.');
        _mobileTypeId.focus();
        return false;
    }
    
   	//submitting form
    if (confirm('Are you sure you want to proceed?')==true)
    {
       return true;
    }    

}// end function
</script>
		<v:javascript formName="changemobileListViewModel"
			staticJavascript="false" />
		<script type="text/javascript"
			src="<c:url
value="/scripts/validator.jsp"/>"></script>



	</body>
</html>

