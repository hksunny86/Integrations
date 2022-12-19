<%@ include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>
       
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
		<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
	    <script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
		<script type="text/javascript" src="${contextPath}/scripts/date-validation.js"></script>
		<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
		<meta name="title" content="Scheduled Bulk Disbursement/Payments"/>
		<meta name="decorator" content="decorator">
</head>
<body>

<spring:bind path="bulkDisbursementsModel.*">
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
	<html:form name="bulkDisbursementsForm" commandName="bulkDisbursementsModel" method="post" action="p_bulkdisbursementform.html" onsubmit="return validateForm(this);">
		<input type="hidden" name="isUpdate" id="isUpdate" value="true" />
		<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
			value="<%=PortalConstants.ACTION_UPDATE%>" />
		<input type="hidden" name="bulkDisbursementsId" value="${param.bulkDisbursementsId}">
		<input type="hidden" name="batchNumber" value="${bulkDisbursementsModel.batchNumber}">
			<tr>
				<td class="formText" align="right" bgcolor="F3F3F3">Product:</td>
				<td width="55%" align="left" bgcolor="FBFBFB"><html:input disabled="true" path="relationProductIdProductModel.name" cssClass="textBox" cssStyle="background: #D3D3D3"/></td>
			</tr>
			<tr>
				<td class="formText" align="right" bgcolor="F3F3F3">Emp/Reg No:</td>
				<td width="55%" align="left" bgcolor="FBFBFB"><html:input disabled="true" path="employeeNo" cssClass="textBox" cssStyle="background: #D3D3D3"/></td>
			</tr>
			<tr>
				<td class="formText" align="right" bgcolor="F3F3F3">Name:</td>
				<td align="left" bgcolor="FBFBFB"><html:input path="name" cssClass="textBox" cssStyle="background: #D3D3D3" disabled="true"/></td>
			</tr>
			<tr>
				<td class="formText" align="right" bgcolor="F3F3F3">Type:</td>
				<td align="left" bgcolor="FBFBFB"><html:input path="type" cssClass="textBox" cssStyle="background: #D3D3D3" disabled="true"/></td>
			</tr>
			<tr>
				<td class="formText" align="right" bgcolor="F3F3F3">CNIC:</td>
				<td align="left" bgcolor="FBFBFB"><html:input path="cnic" cssClass="textBox" cssStyle="background: #D3D3D3" disabled="true" /></td>
			</tr>
			<tr>
				<td class="formText" align="right" bgcolor="F3F3F3">Mobile No:</td>
				<td align="left" bgcolor="FBFBFB"><html:input path="mobileNo" cssClass="textBox" cssStyle="background: #D3D3D3" disabled="true" /></td>
			</tr>
			<tr>
				<td class="formText" align="right" bgcolor="F3F3F3">Cheque No:</td>
				<td align="left" bgcolor="FBFBFB"><html:input path="chequeNo" cssClass="textBox" cssStyle="background: #D3D3D3" disabled="true" /></td>
			</tr>
			
			<tr>
				<td class="formText" align="right" bgcolor="F3F3F3"><span style="color: #FF0000">*</span>Payment Date:</td>
				<td align="left" bgcolor="FBFBFB">
					<c:choose>
				      <c:when test="${bulkDisbursementsModel.posted==true and bulkDisbursementsModel.settled==false and bulkDisbursementsModel.productId=='2510801'}">
				      	<html:input path="paymentDate" cssClass="textBox" cssStyle="background: #D3D3D3" disabled="true" /></td>
				      </c:when>
				      <c:otherwise>
				      		<html:input tabindex="1" path="paymentDate" id="paymentDate" cssClass="textBox" onkeypress="return maskNumber(this,event)" />
						<img id="pDate" tabindex="2" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
						<img id="pDate" tabindex="3" title="Clear Date" name="popcal" onclick="javascript:$('paymentDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
				      </c:otherwise>
					</c:choose>	
				</td>
			</tr>
			<tr>
				<td class="formText" align="right" bgcolor="F3F3F3">Amount:</td>
				<td align="left" bgcolor="FBFBFB"><html:input path="amount" cssClass="textBox" cssStyle="background: #D3D3D3" disabled="true" /></td>
			</tr>
			<tr>
				<td class="formText" align="right" bgcolor="F3F3F3">Description:</td>
				<td align="left" bgcolor="FBFBFB">
					<c:choose>
				      <c:when test="${bulkDisbursementsModel.posted==true and bulkDisbursementsModel.settled==false and bulkDisbursementsModel.productId==2510801}">
				      	<html:textarea disabled="true" path="description" cssStyle="background: #D3D3D3"/>
				      </c:when>
				      <c:otherwise>
				      		<html:textarea tabindex="4" path="description" onkeypress="return textAreaLengthCounter(this,100);"/>
				      </c:otherwise>
					</c:choose>		
				</td>
			</tr>
			<c:if test="${bulkDisbursementsModel.productId==2510801}">
			<tr>
				<c:if test="${bulkDisbursementsModel.productId==2510801}">
					<td class="formText" align="right" bgcolor="F3F3F3">Apply Limits:</td>
					<td align="left" bgcolor="FBFBFB">
						<html:checkbox tabindex="5" path="limitApplicable"/>
					</td>
				</c:if>
			</tr>
			<!-- <tr>
					<td class="formText" align="right" bgcolor="F3F3F3">Pay Cash Via CNIC:</td>
					<td align="left" bgcolor="FBFBFB">
						<html:checkbox tabindex="6" path="payCashViaCnic"/>
					</td>
			</tr> -->
			</c:if>
			<tr>
				<td class="formText" align="right"></td>
				<td align="left">
						<input name="_submit" tabindex="7" type="submit" class="button" value="Update"/> 
						<input name="reset" tabindex="8" type="reset" class="button" value="Cancel" onclick="javascript: window.location='p_bulkdisbursementsearch.html'" />
				</td>
			</tr>
	</html:form>
	</table>
 <script type="text/javascript">
function validatePaymentDate(paymentDate){
	var currentDate = '<%=PortalDateUtils.currentFormattedDate("dd/MM/yyyy")%>';
	var _pDate = paymentDate.value;
	if(isDateSmaller(_pDate,currentDate)) {
		alert("Payment Date can't be in past.");
		return false;
	}
	return true;
}

function validateForm(theForm) {
	if(doRequired( theForm.paymentDate,'Payment Date') && validatePaymentDate(theForm.paymentDate)) {
		return true; 
	}
    return false;
}

function doRequired( field, label ) {
	if( field.value == '' || field.value.length == 0 ) {
      	alert( label + ' is required field.' );
      	return false;
    }
    return true;
}

Calendar.setup( {
	inputField  : "paymentDate", // id of the input field
    ifFormat    : "%d/%m/%Y",      // the date format
    button      : "pDate",    // id of the button
    showsTime   :  false
});
  
</script>
<v:javascript formName="bulkDisbursementsModel" staticJavascript="false"/>

</body>
</html>
