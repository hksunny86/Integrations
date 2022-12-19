<!--Author: Mohammad Shehzad Ashraf-->

<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

   <head>  
<meta name="decorator" content="decorator2">
   
   <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
      
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
       
		<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
      <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

     <meta name="title" content="Add Concern"/>

    
</head>

   <body bgcolor="#ffffff"  >
   
      <spring:bind path="concernModel.*">
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
	
	
     
	  
        <html:form name="concernForm" commandName="concernModel"  onsubmit="return onFormSubmit(this)"
         action="p_createconcernform.html?actionId=1">
          
    
         <table width="100%" border="0" cellpadding="0" cellspacing="1">
           <tr bgcolor="FBFBFB">
             <td colspan="2" align="center">&nbsp;</td>
           </tr>
           

           <tr bgcolor="FBFBFB">
             <td align="right" height="16" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Category:</td>
             <td align="left">
                   <html:select path="concernCategoryId"  cssClass="textBox" tabindex="1">
                      <html:options items="${concernCategoryModelList}" itemLabel="name" itemValue="concernCategoryId"/>
			       </html:select>
             </td>
           </tr>

           <tr>
             <td height="20" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Priority:</td>
             <td width="58%" align="left" bgcolor="FBFBFB">
                 <html:select path="concernPriorityId" cssClass="textBox" tabindex="2">
                     <html:options items="${concernPriorityModelList}" itemLabel="name" itemValue="concernPriorityId"/>
			     </html:select>
             </td>
           </tr>
           <tr>
             <td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Raised To:</td>
             <td align="left" bgcolor="FBFBFB">
				<html:select path="recipientPartnerId" cssClass="textBox" tabindex="3">
                     <html:options items="${concernPartnerModelList}" itemLabel="name" itemValue="concernPartnerId"/>
			     </html:select>
			 </td>
           </tr>

           <tr>
             <td align="right" height="16" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Title:</td>
             <td align="left" bgcolor="FBFBFB">
				<html:input path="title" cssClass="textBox" maxlength="250" onkeypress="return maskCommon(this,event)" tabindex="4" />
             </td>
           </tr>

           <tr>
             <td align="right"  bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Comments:</td>
             <td align="left" bgcolor="FBFBFB">
                 <html:textarea path="comments"  rows="5" cssClass="textBox" cssStyle="overflow: auto; width: 163px; height: 102px" onkeypress="return maskCommon(this,event)" onkeyup="textAreaLengthCounter(this,1000);" tabindex="5"  /></td>
           </tr>
           
           <tr>
             <td align="right" bgcolor="F3F3F3" class="formText">&nbsp;</td>
             <td align="left" bgcolor="FBFBFB">
             <input type="submit" class="button" value=" Create Concern " tabindex="6"/> 
             <input type="button" class="button" value="Cancel" tabindex="7" onclick="javascript: cancelAction();"/>
                  </td>
           </tr>
		</table>

		</html:form>
      
      
      <script language="javascript" type="text/javascript">
      document.concernForm.concernCategoryId.focus();
      function submitForm(theForm)
      {
		if(theForm.title.value == ''){
		    alert('Title is required.');
		    theForm.title.focus();
		    return false;
		}
		if(theForm.comments.value == ''){
		    alert('Comments are required.');
		    theForm.comments.focus();
		    return false;
		}
// 		if( theForm.title.value != '' && !isAlphaWithSpace(theForm.title)){
// 			alert('Please enter valid Title.');
// 			theForm.title.focus();
// 			return false;
// 		}
		      
      	if(!validateFormChar(theForm)){
      		return false;
      	}		      
		      	
      	//submitting form
        if (confirm('Are you sure you want to proceed?')==true)
        {
          return true;
        }
        else
        {
          return false;
        }
  }
      
      function onFormSubmit(theForm)
      {

      	if(submitForm(theForm)){
      		return true;
	    }else{
		    return false;
	    }
	    
        // return validatemfsAccountModel(theForm); 
      }
      
      function cancelAction(){
         window.location = "${pageContext.request.contextPath}/p_myconcerns.html?actionId=2&_formSubmit=true";    
      }
      
      </script>
    
      <script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"> </script>
   </body>
</html>





