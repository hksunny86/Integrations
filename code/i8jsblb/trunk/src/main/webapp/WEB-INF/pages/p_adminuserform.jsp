
<!--Author: Mohammad Shehzad Ashraf-->

<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<c:set var="updateAction"><%=PortalConstants.ACTION_UPDATE %></c:set>
<c:set var="createAction"><%=PortalConstants.ACTION_CREATE %></c:set>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
   
   <head>  
<meta name="decorator" content="decorator2">
     
      <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script> 
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/prototype.js"></script>
		<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
      <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
      <c:choose>
		<c:when test="${not empty param.appUserId}">
	      <meta name="title" content="Edit User"/>
        </c:when>
        <c:otherwise>
	      <meta name="title" content="Add User"/>
      	</c:otherwise>
      </c:choose>
      <script language="javascript" type="text/javascript">
	      function error(request)
	      {
	      	alert("An unknown error has occured. Please contact with the administrator for more details");
	      }
      </script>
</head>

   <body bgcolor="#ffffff"  >
    
      <spring:bind path="userManagementModel.*">
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
      
	    
        <html:form name="userManagementForm" commandName="userManagementModel"  onsubmit="return onFormSubmit(this)"
         action="p_adminuserform.html">
           
           <c:if test="${not empty param.appUserId}">
             <input type="hidden" name="isUpdate" id="isUpdate" value="true"/>
           </c:if>

           <input type="hidden" name="appUserId" value="${param.appUserId}" />
		   
           <html:hidden path="<%=PortalConstants.KEY_USECASE_ID %>"/>
           <html:hidden path="<%=PortalConstants.KEY_ACTION_ID %>"/>
         
		   <tr bgcolor="FBFBFB">
             <td colspan="2" align="center">&nbsp;</td>
           </tr>
           <tr bgcolor="FBFBFB">
             <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>First Name:</td>
             <td align="left">
                   <html:input  path="firstName"  tabindex="1" cssClass="textBox" maxlength="50" onkeypress="return maskAlphaWithSp(this,event)"  /></td>
           </tr>

           <tr>
             <td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Last Name:</td>
             <td width="58%" align="left" bgcolor="FBFBFB">
                 <html:input  path="lastName" cssClass="textBox" tabindex="2"  maxlength="50" onkeypress="return maskAlphaWithSp(this,event)"  /></td>
           </tr>
           <tr>
             <td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>User Name:</td>
             <td align="left" bgcolor="FBFBFB">	
                 <html:input  path="userId" tabindex="3" cssClass="textBox" maxlength="50" onkeypress="return maskCommon(this,event)"  />		 
			 </td>
           </tr>
           
           
           <tr>
             <td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Partner Group:</td>
             <td align="left" bgcolor="FBFBFB">
			 <html:select path="partnerGroupId" cssClass="textBox" tabindex="4">
                 <html:options items="${partnerGroupModelList}" itemLabel="name" itemValue="partnerGroupId"/>
			 </html:select>
			 </td>
           </tr>
     
           <tr>
             <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>E-mail:</td>
             <td align="left" bgcolor="FBFBFB">
                  <html:input  path="email" cssClass="textBox" tabindex="4" maxlength="50"  /></td>
           </tr>

           <tr>
             <td height="16" align="right" bgcolor="F3F3F3" class="formText">Active: </td>
             <td width="58%" align="left" bgcolor="FBFBFB">
           	 <html:checkbox path="isActive" tabindex="5"/>           
           </tr>
           
		    <tr>
             <td height="16" colspan="2" align="right" bgcolor="FBFBFB" class="formText">&nbsp;</td>
           </tr>
		   
           <tr>
             <td height="16" align="right" bgcolor="FBFBFB" class="formText"></td>
             <td width="58%" align="left" bgcolor="FBFBFB">
                   <c:choose>
					<c:when test="${not empty param.appUserId}">
					
						<authz:authorize ifAnyGranted="<%=PortalConstants.ADM_USR_MGMT_UPDATE%>">
							<input type="button" class="button" value="Update"  tabindex="6" onclick="javascript:submitForm(document.forms.userManagementForm,null);" <c:if test="${disable ne null}">disabled="disabled"</c:if>  />						
						</authz:authorize>
						<authz:authorize ifNotGranted="<%=PortalConstants.ADM_USR_MGMT_UPDATE%>">
							<input type="button" class="button" value="Update"  tabindex="-1" disabled="disabled"/>												
						</authz:authorize>
						<input type="button" class="button" value="Cancel"  tabindex="7" onclick="javascript:window.location='p_adminusermanagement.html';" />
			        </c:when>
			        <c:otherwise>
			        
			        	<authz:authorize ifAnyGranted="<%=PortalConstants.ADM_USR_MGMT_CREATE%>">
						      <input type="button" style="width='140px'" class="button" value="Create New User" tabindex="6"  onclick="javascript:submitForm(document.forms.userManagementForm,null);" />
			        	</authz:authorize>
			        	<authz:authorize ifNotGranted="<%=PortalConstants.ADM_USR_MGMT_CREATE%>">
						      <input type="button" style="width='140px'" class="button" value="Create New User" tabindex="-1"   disabled="disabled" />
			        	</authz:authorize>			        						
				      	<input type="button" class="button" value="Cancel"  tabindex="7" onclick="javascript:window.location='p_adminusermanagement.html';" />
			      	</c:otherwise>
			      </c:choose>			 
			  </td>
             </tr>

           <tr bgcolor="FBFBFB">
             <td colspan="2" align="center">&nbsp;</td>
           </tr>
        <tr><td>
		</html:form>
      </table>
       
      <c:choose>
		<c:when test="${not empty param.appUserId}">
            <form action="${contextPath}/p_pgsearchuserinfo.html" method="post" name="userInfoListViewForm" id="userInfoListViewModel">
                <input type="hidden" value="<c:out value="${appUserModel.searchMfsId}"/>" id="userId" name="userId"/>
                <input type="hidden" value="<c:out value="${appUserModel.searchFirstName}"/>" id="firstName" name="firstName"/>
                <input type="hidden" value="<c:out value="${appUserModel.searchLastName}"/>" id="lastName" name="lastName"/>
                <input type="hidden" value="<c:out value="${appUserModel.searchNic}"/>" id="nic" name="nic"/>
            </form>
        </c:when>
        <c:otherwise>
      	</c:otherwise>
      </c:choose>
     <c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
      
      <script language="javascript" type="text/javascript">
      
     
      highlightFormElements();
      <c:choose>
		<c:when test="${not empty param.appUserId}">
          function disableFields(){
              document.forms[0].userId.disabled = true;
          }
          disableFields();
          document.userManagementForm.firstName.focus();
        </c:when>
        <c:otherwise>
	      function onCancel(a, b)
          {
             document.forms[0].reset();
          }
          document.userManagementForm.firstName.focus();
      	</c:otherwise>
      </c:choose>
      
      
      
      
      function submitForm(theForm)
      {	
		var currServerDate = '<%=PortalDateUtils.currentFormattedDate("dd/MM/yyyy")%>';
          
      	_form = theForm;
      	
      	_userId = _form.userId;
      	_firstName 	= _form.firstName;
      	_lastName 	= _form.lastName;
      	_email = _form.email;
      	_isActive = _form.isActive ;
      	
      	
      	_firstName.value = trim(_firstName.value);
      	_lastName.value = trim(_lastName.value);
      	_email.value = trim(_email.value);
		
 		if(_firstName.value == ''){
 			alert('Please enter First Name.');
 			_firstName.focus();
 			return;
 		}	
 		
 		if(!isAlphaWithSpace(_firstName)){
 			alert('Please enter correct First Name.');
 			_firstName.focus();
 			return;
 		}	

 		if(_lastName.value == ''){
 			alert('Please enter Last Name.');
 			_lastName.focus();
 			return;
 		}		

 		if(!isAlphaWithSpace(_lastName)){
 			alert('Please enter correct Last Name.');
 			_lastName.focus();
 			return;
 		}	
        
        if(_userId.value == ''){
 			alert('Please enter User Name.');
 			_userId.focus();
 			return;
 		}

        if(_email.value == ''){   // check the format of the email address here TODO
            alert('Please enter Email.');
            _email.focus();
            return;
        }
        
        if(_email.value != ''){   // check the format of the email address here TODO
            if(!isEmail(_email)){
                alert('Please enter correct Email.');
                return;
            }
        }

		if(!validateFormChar(theForm)){
      		return false;
      	}
		

      	//submitting form
        if (confirm('Are you sure you want to proceed?')==true)
        {
          document.userManagementForm.submit();
        }
        else
        {
          return false;
        }
  }
      
      function onFormSubmit(theForm)
      {
      	submitForm(theForm);
        // return validatemfsAccountModel(theForm); 
      }
      
//  End -->
      
      
      </script>
      
   </body>
</html>
