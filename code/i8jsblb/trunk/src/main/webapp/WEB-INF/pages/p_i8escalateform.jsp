<!--Author: Mohammad Shehzad Ashraf-->

<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>
<c:set var="defaultAction"><%=PortalConstants.ACTION_DEFAULT %></c:set>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta name="decorator" content="decorator2">
<script type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<meta name="title" content="i8 Escalate" />
<script type="text/javascript">
         
      </script>


</head>

<body bgcolor="#ffffff">

<spring:bind path="i8EscalateModel.*">
	<c:if test="${not empty status.errorMessages}">
		<div class="errorMsg"><c:forEach var="error"
			items="${status.errorMessages}">
			<c:out value="${error}" escapeXml="false" />
			<br />
		</c:forEach></div>
	</c:if>
</spring:bind>

<c:if test="${not empty messages}">
	<div class="infoMsg" id="successMessages"><c:forEach var="msg"
		items="${messages}">
		<c:out value="${msg}" escapeXml="false" />
		<br />
	</c:forEach></div>
	<c:remove var="messages" scope="session" />
</c:if>


<table width="100%" border="0" cellpadding="0" cellspacing="1">

	<html:form name="i8EscForm" commandName="i8EscalateModel"
		onsubmit="return onFormSubmit(this)" action="p_i8escalateform.html">

		<input type="hidden" name="<%=PortalConstants.KEY_USECASE_ID%>"
			value="<%=PortalConstants.I8_ESCALATE_FORM_USECASE_ID%>">
		<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="${defaultAction}">
			
		<input type="hidden" name="issueId" value="${param.issueId}" />
		<input type="hidden" name="transactionId" value="${param.transactionId}" />
			
			
		<tr bgcolor="FBFBFB">
			<td colspan="2" align="center">&nbsp;</td>
		</tr>
		<tr>
			<td height="16" align="right" bgcolor="F3F3F3" class="formText">Transaction	Code:</td>
			<td width="58%" align="left" bgcolor="FBFBFB">
			
			<html:input
				path="transactionCode" cssClass="textBox" readonly="true"
				tabindex="-1" maxlength="13"
				onkeypress="return maskCommon(this,event)" />
				
				</td>
		</tr>

		<tr>
			<td height="16" align="right" bgcolor="F3F3F3" class="formText">Escalate
			to Partner:</td>
			<td width="58%" align="left" bgcolor="FBFBFB">
			
			<html:input
				path="escalateToPartner" cssClass="textBox" readonly="true"
				tabindex="-1" maxlength="13"
				onkeypress="return maskCommon(this,event)" />
				
				</td>
		</tr>


		<tr>
			<td height="16" align="right" bgcolor="F3F3F3" class="formText">
				<span style="color:#FF0000">*</span>Issue Detail:
			</td>
			<td width="58%" align="left" bgcolor="FBFBFB">
			<html:textarea
				path="issueDetail" cssClass="textBox" cols="30" rows="5"
				tabindex="1" onkeypress="return maskCommon(this,event)"
				onkeyup="textAreaLengthCounter(this,250);" />
				
				</td>

		</tr>


		<tr>
			<td height="16" colspan="2" align="right" bgcolor="FBFBFB"
				class="formText">&nbsp;</td>
		</tr>

		<tr>
			<td height="16" align="right" bgcolor="FBFBFB" class="formText"></td>
			<td width="58%" align="left" bgcolor="FBFBFB">
			<input
				type="submit" style="width='140px'" class="button"
				value="Dispute Resolution" tabindex="2"
				 />
				
			</td>
		</tr>

		<tr bgcolor="FBFBFB">
			<td colspan="2" align="center">&nbsp;</td>
		</tr>

		<tr>
			<td>
	</html:form>
</table>


<script language="javascript" type="text/javascript">
      
      document.forms[0].issueDetail.focus();
      
      function onFormSubmit(theForm)
      {
     	 if(!validateFormChar(theForm)){
      		return false;
	     	}
    
    	if(document.forms[0].issueDetail.value == ''  ){
    		alert('Issue Detail is a required field.');
    		document.forms[0].issueDetail.focus();
    		return false;
    	}
    
        if (confirm('Are you sure you want to proceed?'))
        {  	
      		return true;
      	}
      	
      	return false;	
      }
      
      </script>

<v:javascript formName="mfsAccountModel" staticJavascript="false" />
<script type="text/javascript"
	src="<c:url value="/scripts/validator.jsp"/>"> </script>
</body>
</html>





