<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: JALIL-UR-REHMAN-->
<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
<meta name="decorator" content="decorator2">
		<meta name="title" content="Change Password" />

		
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
		<script type="text/JavaScript">
		//courtesy of BoogieJack.com
		function killCopy(e){
		return false
		}
		function reEnable(){
		return true
		}
		document.onselectstart=new Function ("return false")
		if (window.sidebar){
		document.onmousedown=killCopy
		document.onclick=reEnable
		}
		</script>	
	</head>
	<body>

		<spring:bind path="changePasswordListViewFormModel.*">
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


		<form id="changePasswordForm" method="post" action="changepasswordform.html"
			onsubmit="return onFormSubmit(this);">
			<table width="100%" border="0" cellpadding="0" cellspacing="1">

				
					<input type="hidden" name="isUpdate" id="isUpdate" value="false"/>
				
				


				<tr>
					<td width="50%" align="right" bgcolor="F3F3F3" class="formText">
						<span style="color:#FF0000">*</span>Old Password:&nbsp;
					</td>
					<td width="50%" align="left" bgcolor="FBFBFB">

						<spring:bind path="changePasswordListViewFormModel.oldPassword">
							
								<input type="password" name="${status.expression}"
									class="textBox" maxlength="50" value="" />
							</spring:bind>

					</td>
				</tr>

				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						<span style="color:#FF0000">*</span>New Password:&nbsp;
					</td>
					<td>


						<spring:bind path="changePasswordListViewFormModel.newPassword">
							
								<input type="password" name="${status.expression}"
									class="textBox" maxlength="50" value="" />
							</spring:bind>

					</td>

					
				</tr>
				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						<span style="color:#FF0000">*</span>Confirm Password:&nbsp;
					</td>
					<td>
								<input type="password" name="confirmPassword" class="textBox"
								maxlength="4000" />

					</td>
				</tr>


	<tr bgcolor="FBFBFB">
					<td height="19" colspan="2" align="center"></td>
				</tr>
				<tr bgcolor="FBFBFB">
					<td colspan="2" align="center">
						<input type="submit" value="  Save  "/>
						<input type="reset" value="Cancel" onclick="javascript: window.location='home.html?actionId=2'"/>
					</td>
				</tr>



	
	
			</table>
		</form>


		<script type="text/javascript">
{

	

document.forms[0].oldPassword.focus();
highlightFormElements();
      highlightFormElements();
      
      
  function onFormSubmit(theForm) {
  
    


		
	
			 if( document.forms.changePasswordForm.newPassword.value == document.forms.changePasswordForm.confirmPassword.value )
				{
					return validateChangePasswordListViewFormModel(document.forms.changePasswordForm);
 				}	 
		 		else	
 				{
					alert("Password and Confirm Password does not match.");
					document.forms.changePasswordForm.oldPassword.focus();
				}
  	
    return false;
  }

  
}
</script>

		<v:javascript formName="changePasswordListViewFormModel"
			staticJavascript="false" />
		<script type="text/javascript"
			src="<c:url value="/scripts/validator.jsp"/>"></script>

	</body>
</html>
