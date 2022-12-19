<!--Author: Asad hayathttp://www.leviton.com/sections/prodinfo/newprod/npleadin.htm-->
<%@ include file="/common/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

	<head>
<meta name="decorator" content="decorator2">
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />


		<meta name="title" content="Service Type " />
		
	  <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
		
	</head>
	<body>
		<spring:bind path="serviceTypeModel.*">
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
	

		<html:form id="serviceTypeModel" method="post" action="servicetypeform.html"
			commandName="serviceTypeModel" onsubmit="return onFormSubmit(this);">
			
			<table width="100%" border="0" cellpadding="0" cellspacing="1">

				<c:if test="${not empty param.serviceTypeId}">
					<input type="hidden" name="isUpdate" id="isUpdate" value="true" />
				</c:if>

				
				<html:hidden id="serviceTypeId" path="serviceTypeId"/>
				
				
				<tr>
             		<td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Service Type:&nbsp;</td>
             		<td width="58%" align="left" bgcolor="FBFBFB">
             		<c:if test="${not empty param.serviceTypeId}">
             		
                   		<html:input path="name" cssClass="textBox" maxlength="50" readonly="readonly" onkeypress="return maskAlphaWithSp(this,event)"/>
                   		</c:if>
                   		<c:if test="${empty param.serviceTypeId}">
             		
                   		<html:input path="name" cssClass="textBox" maxlength="50" onkeypress="return maskAlphaWithSp(this,event)"/>
                   		</c:if>
                   	</td>
           		</tr>
							
				<tr>
             		<td height="16" align="right" bgcolor="F3F3F3" class="formText">Description:&nbsp;</td>
             		<td width="58%" align="left" bgcolor="FBFBFB">
                   		<html:textarea path="description"  cssClass="textBox" cols ="5" onkeypress="return maskCommon(this,event)" onkeyup="textAreaLengthCounter(this,250);"/>
                   	</td>
           		</tr>
				
				<tr>
             		<td height="16" align="right" bgcolor="F3F3F3" class="formText">Comments:&nbsp;</td>
             		<td width="58%" align="left" bgcolor="FBFBFB">
                   		<html:textarea path="comments"  cssClass="textBox" cols ="5" onkeypress="return maskCommon(this,event)" onkeyup="textAreaLengthCounter(this,250);"/>
                   	</td>
           		</tr>
							
				<tr>
					
					<td colspan="2" align="center" class="formText"><GenericToolbar:toolbar formName="serviceTypeModel" /></td>
					
				</tr>
			
			</table>
		</html:form>
		
		
  <script type="text/javascript">

	
	document.forms[0].name.focus();

		highlightFormElements();

		function onFormSubmit(theForm) {
		    /*if(!validateFormChar(theForm)){
      		    return false;
            }*/
			return validateServiceTypeModel(theForm);
		}		

	</script>

	
      <v:javascript formName="serviceTypeModel" staticJavascript="false"/>
      <script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"> </script>	
	</body>
</html>

