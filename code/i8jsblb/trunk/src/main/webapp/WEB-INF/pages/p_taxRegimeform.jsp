
<!--Author: Mohammad Shehzad Ashraf-->
<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE%></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="decorator">
<%@include file="/common/ajax.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/prototype.js"></script>
<script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css" />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<c:choose>
	<c:when test="${not empty param.taxRegimeId or not empty taxRegimeId}">
		<meta name="title" content="Edit Tax Regime" />
	</c:when>
	<c:otherwise>
		<meta name="title" content="Add Tax Regime" />
	</c:otherwise>
</c:choose>
<script language="javascript" type="text/javascript">
      	var jq=$.noConflict();
      	 jq(document).ready(
            function($){
		      	jq('#fed').keypress(function(event) {
					if(event.which < 46 || event.which > 59) {
						event.preventDefault();
					} // prevent if not number/dot		
					if(event.which == 46 && jq(this).val().indexOf('.') != -1) {
						event.preventDefault();
					} // prevent if already dot
				});
            });
	      function error(request)
	      {
	      	alert("An unknown error has occured. Please contact with the administrator for more details");
	      }
      </script>
</head>
<body bgcolor="#ffffff">
	<spring:bind path="taxRegimeModel.*">
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
	<html:form id="taxRegimeForm" name="taxRegimeForm" commandName="taxRegimeModel" onsubmit="return onFormSubmit(this)" action="p_taxRegimeform.html" >
	<table width="100%" border="0" cellpadding="0" cellspacing="1">
			<c:if test="${not empty param.taxRegimeId or not empty taxRegimeId}">
				<input type="hidden" name="isUpdate" id="isUpdate" value="true" />
			</c:if>
			<c:if test="${not empty param.taxRegimeId }">
				<input type="hidden" name="taxRegimeId" value="${param.taxRegimeId}" />
			</c:if>
			<%-- <html:hidden path="<%=PortalConstants.KEY_CREATE_UPDATE_TAX_REGIME_USECASE_ID%>" /> --%>
			<%-- <html:hidden path="<%=PortalConstants.KEY_ACTION_ID%>" /> --%>
			
			<input type="hidden" name="actionAuthorizationId" value="${param.authId}" />
   			<input type="hidden" name="resubmitRequest" value="${param.isReSubmit}" />
   			
			<html:hidden path="createdOn" />
			<html:hidden path="updatedOn" />
			<html:hidden path="createdBy" />
			<html:hidden path="updatedBy" />
			<html:hidden path="versionNo" />
			<%--<tr bgcolor="FBFBFB">
				<td colspan="2" align="center">&nbsp;</td>
			</tr>--%>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Territory/Province:</td>
				<td align="left" bgcolor="FBFBFB">
					<html:input path="name" tabindex="1" cssClass="textBox" maxlength="50" onkeypress="return maskAlphaNumericWithSp(this,event)" />
				</td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>FED (%):</td>
				<td align="left" bgcolor="FBFBFB">
					<html:input path="fed" tabindex="2" cssClass="textBox" maxlength="5" onkeypress="return checkNumeric(this,event)" />
				</td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText">Description:</td>
				<td align="left" bgcolor="FBFBFB">
					<html:input path="description" tabindex="3" cssClass="textBox" maxlength="50" onkeypress="return maskAlphaNumericWithSp(this,event)" />
				</td>
			</tr>
			
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText">Active:</td>
				<td width="58%" align="left" bgcolor="FBFBFB"><html:checkbox path="isActive" tabindex="15" />
			</tr>
			<tr>
				<td height="16" colspan="2" align="right" bgcolor="FBFBFB" class="formText">&nbsp;</td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="FBFBFB" class="formText"></td>
				<td width="58%" align="left" bgcolor="FBFBFB">
				<c:choose>
						<c:when test="${not empty param.taxRegimeId or not empty taxRegimeId}">
							
							<input type="button" class="button" value="Update" tabindex="16" onclick="javascript:submitForm(document.forms.userManagementForm,null);"/>						
							
							<input type="button" class="button" value="Cancel" tabindex="16"
								onclick="javascript:window.location='p_taxregimedefaultfedrate.html?actionId=${retriveAction}';" />
						</c:when>
						<c:otherwise>
						    <input type="button" style="width='140px'" class="button" value="Create" tabindex="15"
										onclick="javascript:submitForm(document.forms.taxRegimeForm,null);" />
 							<input type="button" class="button" value="Cancel" tabindex="16"
								onclick="javascript:window.location='p_taxregimedefaultfedrate.html?actionId=${retriveAction}'" />
						</c:otherwise>
				</c:choose></td>
			</tr>
			<tr bgcolor="FBFBFB">
				<td colspan="2" align="center">&nbsp;</td>
			</tr>
	</table>
	</html:form>
	<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE%></c:set>
	
	<script language="javascript" type="text/javascript">
      highlightFormElements();
      
      function submitForm(theForm)
      {
		  resetForm();
      	if(!isFormDataChanged())
      	{
      		return;
      	}
		else if(jq("#name").val()=="" ||jq("#name").val()==null )
		  {
			  document.getElementById("name").style.borderColor="red";
			  alert('Name is required.');
			  return;
		  }
		else if(jq("#fed").val()=="" ||jq("#fed").val()==null )
		{
			document.getElementById("fed").style.borderColor="red";
			alert('FED is required.');
			return;
		}

		else if(jq("#fed").val() < 0 || jq("#fed").val() > 99){ //fed value/rate
			  alert('FED rate should be between 0 to 99!');
			  return;
		  }
		var currServerDate = '<%=PortalDateUtils.currentFormattedDate("dd/MM/yyyy")%>';
          
    
      	//submitting form
        if (confirm('Are you sure you want to proceed?')==true)
        {
          document.taxRegimeForm.submit();
        }
        else
        {
          return false;
        }
  }

	function onFormSubmit(theForm) {
			submitForm(theForm);
		}

		
		function validateType(field, fname) {
		    
		    var re = "[^A-Za-z0-9]";
		    
		    var flag = true;
		    
            for(var i=0; i<field.value.length;i++){
                var charpos = field.value[i].search(re);
                if(charpos >= 0){
                   flag=false;
					break;       
                }
            }  		    
            
		    if(!flag) {
				alert(fname + ' contains invalid character(s), must be alpha-numeric.');
				field.focus();
				flag = false; 
		    }
		    
		    return flag;
		}

	  function resetForm() {
		  jq('#taxRegimeForm').find(':input:not(:button)').each(function () {
			  this.style.borderColor = "#e6e6e6";
		  });
	  }
	</script>
</body>
</html>
