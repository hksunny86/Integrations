<!--Author: Mohammad Shehzad Ashraf-->

<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

   <head>  
<meta name="decorator" content="decorator2">
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
      <link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
      <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
      <meta name="title" content="Add Concern Category"/>

	<%
		String createPermission = PortalConstants.CONCERN_CAT_DEF_CREATE;
		createPermission +=	"," + PortalConstants.PG_GP_CREATE;
		createPermission +=	"," + PortalConstants.CSR_GP_CREATE;

		String updatePermission = PortalConstants.CONCERN_CAT_DEF_UPDATE;
		updatePermission +=	"," + PortalConstants.PG_GP_UPDATE;
		updatePermission +=	"," + PortalConstants.CSR_GP_UPDATE;
	 %>
   </head>

   <body bgcolor="#ffffff"  >
   
      <spring:bind path="concernCategoryModel.*">
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
	            <c:out value="${msg}" escapeXml="false"/><br/>
	        </c:forEach>
	    </div>
	    <c:remove var="messages" scope="session"/>
	</c:if>
	
	
      <table width="100%" border="0" cellpadding="0" cellspacing="1">
	  
        <html:form name="concernCategoryForm" commandName="concernCategoryModel"  
         action="p_opconcerncategoryform.html?actionId=1">
           
           <c:if test="${not empty param.concernCategoryId}">
             <input type="hidden" name="isUpdate" id="isUpdate" value="true"/>
             <html:hidden path="concernCategoryId"/>
             <html:hidden path="createdBy"/>
             <html:hidden path="createdOn"/>
             <html:hidden path="active"/>
           </c:if>
           
           <tr bgcolor="FBFBFB">
             <td colspan="2" align="center">&nbsp;</td>
           </tr>
           <tr>
             <td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Name:</td>
             <td width="58%" align="left" bgcolor="FBFBFB">
                   <html:input  path="name" tabindex="1" cssClass="textBox" maxlength="35" onkeypress="return maskAlphaWithSp(this,event)"  /></td>
           </tr>
           <tr>
             <td align="right" bgcolor="F3F3F3" class="formText">Description:</td>
             <td align="left" bgcolor="FBFBFB">
                <html:textarea path="description" cssClass="textBox" cssStyle="overflow: auto; width: 163px; height: 102px" tabindex="5" onkeypress="return maskCommon(this,event)"  onkeyup="textAreaLengthCounter(this,250);"   />
             </td>
           </tr>
           <tr>
             <td align="right" bgcolor="F3F3F3" class="formText">Comments:</td>
             <td align="left" bgcolor="FBFBFB">
                 <html:textarea path="comments" cssClass="textBox" cssStyle="overflow: auto; width: 163px; height: 102px" tabindex="6" onkeypress="return maskCommon(this,event)" onkeyup="textAreaLengthCounter(this,250);"   /></td>
           </tr>
           <tr>
             <td align="right" bgcolor="F3F3F3" class="formText"></td>
             <td align="left" bgcolor="FBFBFB">
			    <c:choose>
			       	<c:when test="${empty param.concernCategoryId}">
			           	<authz:authorize ifAnyGranted="<%=createPermission%>">
			           	<input type="button" value="Create Category" class="button" id="submitButton" name="submitButton" onclick="onFormSubmit(document.forms[0])"/>
						</authz:authorize>
			       	</c:when>
			       	<c:otherwise>
			           	<authz:authorize ifAnyGranted="<%=updatePermission%>">
			           	<input type="button" value="Update" class="button" id="submitButton" name="submitButton" onclick="onFormSubmit(document.forms[0])"/>
						</authz:authorize>
			       	</c:otherwise>
			    </c:choose>
                <input type="button" value="Cancel" class="button" onclick="javascript: cancelAction();"/>
           </tr>
           <tr bgcolor="FBFBFB">
             <td colspan="2" align="center">&nbsp;</td>
           </tr>           
        <tr><td>
		</html:form>
      </table>
      
    <script language="javascript" type="text/javascript">
      document.concernCategoryForm.name.focus();
      function submitForm(theForm)
      {	
 		if(!theForm.name.value || trim(theForm.name.value) == ''){
 			alert('Name is required.');
 			theForm.name.focus();
 			return false;
 		}	
 		else{
 		
 		    if(!validateFormChar(theForm)){
      			return false;
      	    }	
 		
 		    return true;
 		}	
      } 
      function onFormSubmit(theForm)
      {
      	if(submitForm(theForm)){
      		//alert("returned true");
      		theForm.submit();
	    }else{
		    return false;
	    }
      }
      function cancelAction(){
          window.location = "${pageContext.request.contextPath}/p_opconcerncatdefmanagement.html?actionId=2&_formSubmit=true";
      }
    </script>
      
      <v:javascript formName="mfsAccountModel" staticJavascript="false"/>
      <script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"> </script>
   </body>
</html>
