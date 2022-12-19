<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Jawwad Farooq-->
<%@include file="/common/taglibs.jsp"%>

<html>
<head>
<meta name="decorator" content="decorator2">
<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
<meta name="title" content="Switch"/>
</head>
<body>

<spring:bind path="switchModel.*">
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

    <form method="post" action="switchform.html" onsubmit="return onFormSubmit(this);" name="SwitchForm" id="SwitchForm">
    	<table width="100%" border="0" cellpadding="0" cellspacing="1">

  <c:if test="${not empty param.switchId}">
    <input type="hidden" name="isUpdate" id="isUpdate" value="true"/>
    <spring:bind path="switchModel.name">
    <input type="hidden" name="${status.expression}" value="${status.value}"/>
    
    </spring:bind>
       
  </c:if>
  
  <spring:bind path="switchModel.switchId">
    	<input type="hidden" name="${status.expression}" value="${status.value}"/>
    	
    </spring:bind>
  
  
  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Switch:&nbsp;</td>
    <td>
      <spring:bind path="switchModel.name">
          <input type="text" name="${status.expression}" class="textBox" maxlength="50" value="${status.value}" 
          <c:if test="${not empty param.switchId}">disabled='disabled' </c:if> >
      </spring:bind>
           
    </td>
  </tr>

  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Class Name:&nbsp;</td>
    <td>
      <spring:bind path="switchModel.className">
          <input type="text" name="${status.expression}" class="textBox" maxlength="250" value="${status.value}">
      </spring:bind>
    </td>
  </tr>

  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>User Name:&nbsp;</td>
    <td>
      <spring:bind path="switchModel.userId">
          <input type="text" name="${status.expression}" class="textBox" maxlength="50" value="${status.value}">
      </spring:bind>
    </td>
  </tr>

  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Password:&nbsp;</td>
    <c:if test="${empty param.switchId}">
	    <td><spring:bind path="switchModel.password">
    	  <input type="password" name="${status.expression}" class="textBox" maxlength="50" value="">
	      </spring:bind>
    	</td>
    </c:if>
    <c:if test="${not empty param.switchId}">
	    <td>
    	  <input type="password" name="password" class="textBox" maxlength="50" value="">
	    </td>
    </c:if>
    
  </tr>
  
  
  <tr bgcolor="FBFBFB">
  	<td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Confirm Password:&nbsp;</td>
    <td><input type="password" name="confirmPassword" class="textBox" maxlength="50" value="${status.value}"/></td>
  </tr>

 <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">URL:&nbsp;</td>
    <td><spring:bind path="switchModel.url">
      <input type="text" name="${status.expression}" class="textBox" maxlength="250" value="${status.value}">
      </spring:bind>
    </td>
  </tr>
  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText">Payment Gateway Code:&nbsp;</td>
    <td><spring:bind path="switchModel.paymentGatewayCode">
      <input type="text" name="${status.expression}" class="textBox" maxlength="50" value="${status.value}">
      </spring:bind>
    </td>
  </tr>
    <tr>
    <td align="right" bgcolor="F3F3F3" class="formText">Description:&nbsp;</td>
    <td align="left" bgcolor="FBFBFB">
    	<spring:bind path="switchModel.description">
	    <textarea name="${status.expression}" cols="50" rows="5" class="textBox" onkeypress="return maskCommon(this,event)" onkeyup="textAreaLengthCounter(this,250);" type="_moz">${status.value}</textarea>
	    </spring:bind>
    </td>
  </tr>

<tr>
    <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Active:&nbsp;</td>
    <td width="51%" align="left" bgcolor="FBFBFB" class="formText">
    <spring:bind path="switchModel.active">
	    <input name="${status.expression}" type="checkbox" value="true" 	
	    <c:if test="${status.value==true}">checked="checked"</c:if>
				<c:if test="${empty param.switchId && empty param._save }">checked="checked"</c:if> 
				<c:if test="${status.value==false && not empty param._save}">unchecked="checked"</c:if>

	    />			    
     </spring:bind>
    </td>
  </tr>  
  
  <tr>
    <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">CVV:&nbsp;</td>
    <td width="51%" align="left" bgcolor="FBFBFB" class="formText">
    <spring:bind path="switchModel.cvvRequired">
	    <input name="${status.expression}" type="checkbox" value="true" 	
<c:if test="${status.value==true}">checked="checked"</c:if>
				<c:if test="${empty param.switchId && empty param._save }">checked="checked"</c:if> 
				<c:if test="${status.value==false && not empty param._save}">unchecked="checked"</c:if>

	    />			    
     </spring:bind>
    </td>
  </tr> 
  
  <tr>
    <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">TPIN:&nbsp;</td>
    <td width="51%" align="left" bgcolor="FBFBFB" class="formText">
    <spring:bind path="switchModel.tpinRequired">
	    <input name="${status.expression}" type="checkbox" value="true" 	
<c:if test="${status.value==true}">checked="checked"</c:if>
				<c:if test="${empty param.switchId && empty param._save }">checked="checked"</c:if> 
				<c:if test="${status.value==false && not empty param._save}">unchecked="checked"</c:if>

	    />			    
     </spring:bind>
    </td>
  </tr>  

  <tr>
    <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">MPIN:&nbsp;</td>
    <td width="51%" align="left" bgcolor="FBFBFB" class="formText">
    <spring:bind path="switchModel.mpinRequired">
	    <input name="${status.expression}" type="checkbox" value="true" 	
<c:if test="${status.value==true}">checked="checked"</c:if>
				<c:if test="${empty param.switchId && empty param._save }">checked="checked"</c:if> 
				<c:if test="${status.value==false && not empty param._save}">unchecked="checked"</c:if>

	    />			    
     </spring:bind>
    </td>
  </tr>

  <tr bgcolor="FBFBFB">
    <td height="19" align="left"></td>
  </tr>

  <tr bgcolor="FBFBFB">
    <td colspan="2" align="left">
       <GenericToolbar:toolbar formName="SwitchForm" />
</td>
  </tr>
</table>
    </form>

<script language="javascript" type="text/javascript">
 if(document.forms[0].isUpdate==null)
    document.forms.SwitchForm.name.focus();
else

    document.forms[0].className.focus();
highlightFormElements();

function onFormSubmit(theForm) 
{
    /*if(!validateFormChar(theForm)){
      	return false;
    }*/
    
    if( document.forms.SwitchForm.password.value == '' && document.forms.SwitchForm.isUpdate == null  )
	{
		alert("Password and confirmed password is required");
		document.forms.SwitchForm.password.focus();
		return false;
		
	}
    
    if( document.forms.SwitchForm.isUpdate != null && document.forms.SwitchForm.isUpdate.value == 'true' )
  	{
		if( document.forms.SwitchForm.password.value == document.forms.SwitchForm.confirmPassword.value )
		{
			return validateSwitchModel(theForm);
	 	}	 
		alert("Password and Confirm Password do not match");
		document.forms.SwitchForm.password.focus();
	    return false;
	}
    else
    {
 		if( document.forms.SwitchForm.password.value == document.forms.SwitchForm.confirmPassword.value 
 	  	|| document.forms.SwitchForm.password.value == '' )
  		{
  			return validateSwitchModel(theForm);
	  	}
  		alert("Password and Confirm Password do not match");
	    return false;
	}
}
</script>

<v:javascript formName="switchModel" staticJavascript="false" />
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>



</body>
</html>
