<%@ include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>
       
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
	<meta name="title" content="Error Alert Configurations"/>
	<meta name="decorator" content="decorator">
</head>
<body>

<spring:bind path="alertsConfigModel.*">
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
	<html:form name="alertsConfigForm" commandName="alertsConfigModel" method="post" action="p_alertconfigform.html" onsubmit="return validateForm(this);">
		<c:if test="${not empty param.alertsConfigId}">
			<input type="hidden" name="isUpdate" id="isUpdate" value="true" />
			<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
				value="<%=PortalConstants.ACTION_UPDATE%>" />
		</c:if>
		<c:if test="${empty param.alertsConfigId}">
			<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
				value="<%=PortalConstants.ACTION_CREATE%>" />
		</c:if>
		<input type="hidden" name="alertsConfigId" value="${param.alertsConfigId}">
			<tr>
				<td class="formText" align="right" bgcolor="F3F3F3"><span style="color: #FF0000">*</span>Alert Type:</td>
				<td width="25%" align="left" bgcolor="FBFBFB"><html:input tabindex="1" disabled="true" cssStyle="background: #D3D3D3" path="alertName" cssClass="textBox" /></td>
			</tr>
			<tr>
				<td class="formText" align="right" bgcolor="F3F3F3"><span style="color: #FF0000">*</span>Failure Rate(%age):</td>
				<td align="left" bgcolor="FBFBFB"><html:input tabindex="2" path="rate" cssClass="textBox" onkeypress="return maskNumber(this,event)" /></td>
			</tr>
			<tr>
				<td class="formText" align="right" bgcolor="F3F3F3"><span style="color: #FF0000">*</span>Frequency(Min.):</td>
				<td align="left" bgcolor="FBFBFB"><html:input tabindex="3" path="alertInterval" cssClass="textBox" onkeypress="return maskNumber(this,event)" /></td>
			</tr>
			<tr>
				<td class="formText" align="right" bgcolor="F3F3F3"><span style="color: #FF0000">*</span>Minimum Requests:</td>
				<td align="left" bgcolor="FBFBFB"><html:input tabindex="4" path="alertRequests" cssClass="textBox" onkeypress="return maskNumber(this,event)" /></td>
			</tr>
			<tr>
				<td class="formText" align="right" width="20%" bgcolor="F3F3F3">
					Active:
				</td>
				<td align="left" bgcolor="FBFBFB">
					<html:checkbox tabindex="5" path="isActive" />
				</td>
			</tr>
			<tr>
				<td class="formText" align="right"></td>
				<td align="left">
						<input name="_search" tabindex="6" type="submit" class="button" value="Update"/> 
						<input name="reset" tabindex="7" type="reset" class="button" value="Cancel" />
				</td>
			</tr>
	</html:form>
	</table>
 <script type="text/javascript">

function validateForm(theForm) {
	if(doRequired( theForm.alertName, 'Alert Type' ) && doRequired( theForm.rate, 'Failure Rate(%age)' )
		&& doRequired( theForm.alertInterval, 'Frequency(Min.)' ) && doRequired( theForm.alertRequests, 'Minimum Requests' )) {
		if(isPositiveInt( theForm.rate, 'Failure Rate(%age)' ) && checkPercentage(theForm.rate, 'Failure Rate(%age)') 
		&& isPositiveInt( theForm.alertInterval, 'Frequency(Min.)' ) && isPositiveInt( theForm.alertRequests, 'Minimum Requests' ) ) {
			 return true;
		}
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

function isPositiveInt(field, label) {
   if(!(field.value  % 1 === 0) || field.value <= 0){
   		alert( label + ' must be positive integer.' );
   		return false;
   }
   return true;
}

function checkPercentage(feild, label) {
    var x = parseFloat(feild.value);
	if (isNaN(x) || x < 1 || x > 100) {
	 alert(label + ' should be between 1 and 100. ')
    	 return (false)
	}
	return (true)
}
  
</script>
<v:javascript formName="alertsConfigModel" staticJavascript="false"/>

</body>
</html>
