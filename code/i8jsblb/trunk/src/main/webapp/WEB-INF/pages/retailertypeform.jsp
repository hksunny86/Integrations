<!--Author: Asad hayathttp://www.leviton.com/sections/prodinfo/newprod/npleadin.htm-->
<%@ include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

	<head>
<meta name="decorator" content="decorator2">
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />


		<meta name="title" content="Retailer Type " />
		
	  <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
	</head>
	<body>
		
		<spring:bind path="retailerTypeModel.*">
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
	

		<html:form id="retailerTypeModel" method="post" action="retailertypeform.html" onsubmit="return onFormSubmit(this)"
			commandName="retailerTypeModel">
			
			<table width="100%" border="0" cellpadding="0" cellspacing="1">

				<c:if test="${not empty param.retailerTypeId}">
					<input type="hidden" name="isUpdate" id="isUpdate" value="true" />
				</c:if>

				
				<html:hidden id="retailerTypeId" path="retailerTypeId"/>
				
				
				<tr>
             		<td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Retailer Type:&nbsp;</td>
             		<td width="58%" align="left" bgcolor="FBFBFB">
						<c:if test="${not empty param.retailerTypeId}">
							<html:input path="name" maxlength="50" cssClass="textBox" readonly="readonly" onkeypress="return maskAlpha(this,event)"/>						
						</c:if>
						<c:if test="${empty param.retailerTypeId}">
								<html:input path="name" maxlength="50" cssClass="textBox" onkeypress="return maskAlpha(this,event)"/>
						</c:if>
                   		

                   		

                   	</td>
           		</tr>
							
				<tr>
             		<td height="16" align="right" bgcolor="F3F3F3" class="formText">Description:&nbsp;</td>
             		<td width="58%" align="left" bgcolor="FBFBFB">
                   		   <html:textarea path="description"  cssClass="textBox" rows ="5" onkeyup="textAreaLengthCounter(this,250);" onkeypress="return maskCommon(this,event)" />
                   		   
                   	</td>
           		</tr>
				
				<tr>
             		<td height="16" align="right" bgcolor="F3F3F3" class="formText">Comments:&nbsp;</td>
             		<td width="58%" align="left" bgcolor="FBFBFB">
                   		<html:textarea path="comments"  cssClass="textBox" rows ="5" onkeyup="textAreaLengthCounter(this,250);" onkeypress="return maskCommon(this,event)" />
                   		
                   	</td>
           		</tr>
							
				<tr>
					
					<td colspan="2" align="middle" class="formText"><GenericToolbar:toolbar formName="retailerTypeModel" /></td>
					
				</tr>
			
			</table>
		</html:form>
		
		
  <script type="text/javascript">
  
		highlightFormElements();

		function onFormSubmit(theForm) {
			/*if(!validateFormChar(theForm)){
      		    return false;
            }*/
			return validateRetailerTypeModel(theForm);
		}

	</script>

	
      <v:javascript formName="retailerTypeModel" staticJavascript="false"/>
      <script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"> </script>	
	</body>
</html>

