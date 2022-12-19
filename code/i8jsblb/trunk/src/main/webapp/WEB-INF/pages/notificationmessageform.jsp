<!--Author: Asad hayathttp://www.leviton.com/sections/prodinfo/newprod/npleadin.htm-->
<%@ include file="/common/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

	<head>
<meta name="decorator" content="decorator2">
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />


		<meta name="title" content="Notification Message" />
		
	  <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
		
	</head>
	<body>
		<spring:bind path="notificationMessageModel.*">
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
	

		<html:form id="notificationMessageModel" method="post"
			action="notificationmessageform.html"
			commandName="notificationMessageModel" onsubmit="return onFormSubmit(this)">
			
			<table width="100%" border="0" cellpadding="0" cellspacing="1">

				<c:if test="${not empty param.mesId}">
					<input type="hidden" name="isUpdate" id="isUpdate" value="true" />
				</c:if>

				
				
				<spring:bind path="notificationMessageModel.notificationMessageId">
             		<input type="hidden" name="${status.expression}" value="${status.value}"/>
           		</spring:bind>
				
				<spring:bind path="notificationMessageModel.versionNo">
             		<input type="hidden" name="${status.expression}" value="${status.value}"/>
           		</spring:bind>
				
						
								
				<tr>
       				
       				<td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Text Message:&nbsp;</td>
       				<td width="50%" align="left" bgcolor="FBFBFB">
		      					      			
		      			<spring:bind path="notificationMessageModel.smsMessageText">
                 				
                 				<textarea name="smsMessageText" rows="7" cols="80" class="textBox" onkeyup="textAreaLengthCounter(this,250);" onkeypress="return maskCommonNotificaitonMessage(this,event)">${notificationMessageModel.smsMessageText}</textarea>
			      			
			      		</spring:bind>
    				</td>
   					  							
   				
   				</tr>
   								
				<tr>
       				<td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Email Message:&nbsp;</td>
       				<td width="50%" align="left" bgcolor="FBFBFB">
		      					      			
		      			<spring:bind path="notificationMessageModel.emailMessageText">
                 			
                 				<textarea name="emailMessageText" rows="7" cols="80" class="textBox" onkeyup="textAreaLengthCounter(this,250);" onkeypress="return maskCommonNotificaitonMessage(this,event)">${notificationMessageModel.emailMessageText}</textarea>
			      			
			      		</spring:bind>
    				</td>
   							
   				
   				</tr>
				
				<tr>
					<td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Message Type:&nbsp;</td>
					
             		<td width="58%" align="left" bgcolor="FBFBFB">
             		
                                    
                          	<html:select path="messageTypeId" cssClass="textBox">
                     			<html:option value="" label=""/>
                     				<html:options items="${messageTypeList}" itemValue="messageTypeId" itemLabel="name"/>
                 				</html:select>
             			
             		</td>
				

				</tr>
				<tr></tr>
				<tr></tr>
				<tr>
					
					<td colspan="2" align="middle" class="formText"><GenericToolbar:toolbar formName="notificationMessageModel"/></td>
				</tr>
			
			</table>
		</html:form>
		
		
  <script type="text/javascript">

		highlightFormElements();

		function onFormSubmit(theForm) {
           return validateNotificationMessageModel(theForm);
		}		

	</script>

	
      <v:javascript formName="notificationMessageModel" staticJavascript="false"/>
      <script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"> </script>	
	</body>
</html>

