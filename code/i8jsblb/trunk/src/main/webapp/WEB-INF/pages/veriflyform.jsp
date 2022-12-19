<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Jawwad Farooq-->
<%@include file="/common/taglibs.jsp"%>

<html>
<head>
<meta name="decorator" content="decorator2">
<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
	
<meta name="title" content="Verifly"/>
</head>
<body>

<spring:bind path="veriflyModel.*">
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
            <c:out value="${msg}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session"/>
</c:if>

    <form method="post" action="veriflyform.html" onsubmit="return onFormSubmit(this);" id="VeriflyForm">
    	<table width="100%" border="0" cellpadding="0" cellspacing="1">

  <c:if test="${not empty param.veriflyId}">
    <input type="hidden" name="isUpdate" id="isUpdate" value="true"/>
    <spring:bind path="veriflyModel.veriflyId">
    	<input type="hidden" name="${status.expression}" value="${status.value}"/>
    </spring:bind>
    <spring:bind path="veriflyModel.veriflyModeId">
        <input type="hidden" name="${status.expression}" value="${status.value}"/>
    </spring:bind>
  </c:if>
  
  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Name:&nbsp;</td>
    <td>
      <spring:bind path="veriflyModel.name">
          <input type="text" name="${status.expression}" <c:if test="${not empty param.veriflyId}">readonly="readOnly"</c:if> class="textBox"onkeypress="return maskAlphaWithSp(this,event)"   maxlength="50" value="${status.value}">
      </spring:bind>
    </td>
  </tr>
  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">Verifly Mode:&nbsp;</td>
    <td>
       <spring:bind path="veriflyModel.veriflyModeId">
                             <c:if test="${empty param.veriflyId}">
                   <select name="${status.expression}" class="textBox">
                     
                     <c:forEach items="${veriflyModeModelList}" var="veriflyModeModelList">
                       <option value="${veriflyModeModelList.veriflyModeId}" <c:if test="${status.value == veriflyModeModelList.veriflyModeId}">selected="selected"</c:if>>
                       ${veriflyModeModelList.modeName}
                       </option>
                     </c:forEach>
                   </select>
                 </c:if>
                                         <c:if test="${not empty param.veriflyId}">
                   <select name="${status.expression}" class="textBox" disabled="disabled">
                     
                     <c:forEach items="${veriflyModeModelList}" var="veriflyModeModelList">
                       <option value="${veriflyModeModelList.veriflyModeId}" <c:if test="${status.value == veriflyModeModelList.veriflyModeId}">selected="selected"</c:if>>
                       ${veriflyModeModelList.modeName}
                       </option>
                     </c:forEach>
                   </select>
                 </c:if>
            
       </spring:bind>
    </td>
  </tr>

  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>User Name:&nbsp;</td>
    <td>
      <spring:bind path="veriflyModel.userId">
          <input type="text" name="${status.expression}"  class="textBox" maxlength="50" value="${status.value}">
      </spring:bind>
    </td>
  </tr>

  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Password:&nbsp;</td>
    <c:if test="${empty param.veriflyId}">
	    <td><spring:bind path="veriflyModel.password">
    	  <input type="password" name="${status.expression}" class="textBox" 
    	  
    	  maxlength="50" value="${status.value}">
	      </spring:bind>
    	</td>
    </c:if>
    <c:if test="${not empty param.veriflyId}">
	    <td>
    	  <input type="password" name="password" class="textBox" maxlength="50" value="">
	    </td>
    </c:if>
    
    
  </tr>
  
  
  <tr bgcolor="FBFBFB">
  	<td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Confirm password:&nbsp;</td>
    <td><input type="password" name="confirmPassword" class="textBox" maxlength="50" value="${status.value}"/></td>
  </tr>

 <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>URL:&nbsp;</td>
    <td><spring:bind path="veriflyModel.url">
      <input type="text" name="${status.expression}" class="textBox" maxlength="250" value="${status.value}">
      </spring:bind>
    </td>
  </tr>
  
  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Key:&nbsp;</td>
    <td>
      <spring:bind path="veriflyModel.key">
          <input type="text" name="${status.expression}" onkeypress="return maskNumber(this,event)" class="textBox" maxlength="4000" value="${status.value}">
      </spring:bind>
    </td>
  </tr>

  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">Mod:&nbsp;</td>
    <td>
      <spring:bind path="veriflyModel.mod">
          <input type="text" name="${status.expression}" onkeypress="return maskNumber(this,event)"   class="textBox" maxlength="50" value="${status.value}">
      </spring:bind>
    </td>
  </tr>
  
    <tr>
    <td align="right" bgcolor="F3F3F3" class="formText">Description:&nbsp;</td>
    <td align="left" bgcolor="FBFBFB">
    	<spring:bind path="veriflyModel.description">
	    <textarea name="${status.expression}" cols="50" rows="5" class="textBox" onkeypress="return maskCommon(this,event)" onkeyup="textAreaLengthCounter(this,250);" type="_moz">${status.value}</textarea>
	    </spring:bind>
    </td>
  </tr>
<tr>
    <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Active:&nbsp;</td>
    <td width="51%" align="left" bgcolor="FBFBFB" class="formText">
    <spring:bind path="veriflyModel.active">
	    <input name="${status.expression}" type="checkbox" value="true" 	
	    
	    	    <c:if test="${status.value==true}">checked="checked"</c:if>
	    <c:if test="${empty param.veriflyId && empty param._save }">checked="checked"</c:if> 
								<c:if test="${status.value==false && not empty param._save}">unchecked="checked"</c:if>
	    
	    />			    
     </spring:bind>
    </td>
  </tr>
  

  <tr bgcolor="FBFBFB">
    <td height="19" colspan="2" align="center"></td>
  </tr>

  <tr bgcolor="FBFBFB">
    <td colspan="2" align="center">
<GenericToolbar:toolbar formName="VeriflyForm" />
</td>
  </tr>
</table>
    </form>

<script language="javascript" type="text/javascript">
highlightFormElements();

function onFormSubmit(theForm) 
{
    /*if(!validateFormChar(theForm)){
      	return false;
    }*/
    
       if( document.forms.VeriflyForm.isUpdate != null && document.forms.VeriflyForm.isUpdate.value == 'true' )
  	{
		if( document.forms.VeriflyForm.password.value == document.forms.VeriflyForm.confirmPassword.value )
		{
			if (document.forms.VeriflyForm.password.value=' ')
			{
				document.forms.VeriflyForm.password.value='*****';
				document.forms.VeriflyForm.confirmPassword.value='*****';
			}
			
			return validateVeriflyModel(theForm);
	 	}	 
		alert("Password and confirm password fields do not match.");
		document.forms.VeriflyForm.password.focus();
	    return false;
	}
	
	if( document.forms.VeriflyForm.password.value == '' )
	{
		alert("Password and confirmed password is required");
		document.forms.VeriflyForm.password.focus();
		return false;
		
	}
 	if( document.forms.VeriflyForm.password.value == document.forms.VeriflyForm.confirmPassword.value 
 	  || document.forms.VeriflyForm.password.value == '*****' )
  	{
  		return validateVeriflyModel(theForm);
  	}
  	
  	alert("Password and Confirm Password fields do not match.");
    return false;
}
</script>

<v:javascript formName="veriflyModel" staticJavascript="false" />
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>



</body>
</html>
