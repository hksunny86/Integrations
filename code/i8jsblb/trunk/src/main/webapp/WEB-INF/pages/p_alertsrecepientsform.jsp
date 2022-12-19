<%@ include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>
     
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
	<meta name="title" content="Alert Recipient"/>
	<meta name="decorator" content="decorator">
</head>
<body>

<spring:bind path="alertsRecipientsModel.*">
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
	<table width="100%" border="0">
	<html:form name="alertsRecipientsForm" commandName="alertsRecipientsModel" method="post" action="p_alertsrecepientsform.html" onsubmit="return validateForm(this);">
		<c:if test="${not empty param.alertsRecipientsId}">
			<input type="hidden" name="isUpdate" id="isUpdate" value="true" />
			<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
				value="<%=PortalConstants.ACTION_UPDATE%>" />
		</c:if>
		<c:if test="${empty param.alertsRecipientsId}">
			<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
				value="<%=PortalConstants.ACTION_CREATE%>" />
		</c:if>
		<input type="hidden" name="alertsRecipientsId" value="${param.alertsRecipientsId}">
			<tr>
				<td class="formText" align="right" bgcolor="F3F3F3"><span style="color: #FF0000">*</span>Name:</td>
				<td width="25%" align="left" bgcolor="FBFBFB"><html:input tabindex="1" path="name" cssClass="textBox" onkeypress="return maskAlphaWithSp(this,event)"/></td>
			</tr>
			<tr>
				<td class="formText" align="right" bgcolor="F3F3F3"><span style="color: #FF0000">*</span>Mobile No:</td>
				<td align="left" bgcolor="FBFBFB"><html:input path="mobileNo" tabindex="2" cssClass="textBox" maxlength="11" onkeypress="return maskNumber(this,event)" /></td>
			</tr>
			<tr>
				<td class="formText" align="right" bgcolor="F3F3F3"><span style="color: #FF0000">*</span>Email ID:</td>
				<td align="left" bgcolor="FBFBFB"><html:input path="emailId" tabindex="3" cssClass="textBox" maxlength="50" /></td>
			</tr>
			<tr>
				<td class="formText" align="right" bgcolor="F3F3F3"><span style="color: #FF0000">*</span>Alert Type:</td>
				<td align="left" bgcolor="FBFBFB">
					<c:if test="${empty param.alertsRecipientsId}">
						<html:select path="alertConfigId" tabindex="4" cssClass="textBox">
							<html:option value="">---All---</html:option>
							<c:if test="${alertsConfigModelList != null}">
								<html:options items="${alertsConfigModelList}" itemValue="alertsConfigId" itemLabel="alertName" />
		 					</c:if>
		 				</html:select>	
					</c:if>
					<c:if test="${not empty param.alertsRecipientsId}"> 
						<html:select path="alertConfigId" tabindex="4" cssClass="textBox" disabled="true" cssStyle="background: #D3D3D3">
						<html:option value="">---All---</html:option>
							<c:if test="${alertsConfigModelList != null}">
								<html:options items="${alertsConfigModelList}" itemValue="alertsConfigId" itemLabel="alertName" />
		 					</c:if>
		 				</html:select>
					</c:if> 
				</td>
			</tr>
			<tr>
				<td class="formText" align="right" bgcolor="F3F3F3">
					Description:
				</td>
				<td align="left" bgcolor="FBFBFB">
					<html:textarea tabindex="5" path="description" rows="4" cols="21" onkeypress="return textAreaLengthCounter(this,100);"/>
				</td>
			</tr>
			<tr>
				<td class="formText" align="right" width="20%" bgcolor="F3F3F3">
					Active:
				</td>
				<td align="left" bgcolor="FBFBFB">
					<html:checkbox tabindex="6" path="isActive" />
				</td>
			</tr>
			<tr>
				<td class="formText" align="right"></td>
				<td align="left">
					<input name="_search" tabindex="7" type="submit" class="button" value="Submit"/> 
					<c:if test="${empty param.alertsRecipientsId}"> 
						<input name="reset" tabindex="8" type="reset" class="button" value="Cancel" />
					</c:if>
					<c:if test="${not empty param.alertsRecipientsId}"> 
						<input type="reset" class="button" value="Cancel" name="_reset" onclick="javascript: window.location='p_alertsrecepientssearch.html?actionId=2'" tabindex="8" />
					</c:if>	
				</td>
			</tr>
	</html:form>
	</table>
 <script type="text/javascript">

function validateForm(theForm) {
	if(doRequired( theForm.name, 'Name' ) && doRequired( theForm.mobileNo, 'Mobile No.' )
		&& doRequired( theForm.emailId, 'Email ID' ) && doRequired( theForm.alertConfigId, 'Alert Type' ) 
		&& checkMobile(theForm.mobileNo) && isEmail(theForm.emailId)) {
			return true;
	}
    return false;
}

highlightFormElements();

function doRequired( field, label ) {
	if( field.value == '' || field.value.length == 0 ) {
      	alert( label + ' is required field.' );
      	return false;
    }
    return true;
}

function checkMobile(number) {
	if (/^(03)[0-9]{9}$/.test(number.value)){
    	return (true)
    }
    alert("Mobile No. format is not correct.")
    return (false)
}
  
</script>
<v:javascript formName="alertsRecipientsModel" staticJavascript="false"/>

</body>
</html>
