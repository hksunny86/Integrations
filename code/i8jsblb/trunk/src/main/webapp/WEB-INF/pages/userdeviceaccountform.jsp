<!--Author: Asad hayathttp://www.leviton.com/sections/prodinfo/newprod/npleadin.htm-->
<%@ include file="/common/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

	<head>
<meta name="decorator" content="decorator2">
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />


		<meta name="title" content="MWallet/Device Account" />
		
	  <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
 <script type="text/javascript">		
function clearAppUser()
{
<c:if test="${empty param.userDeviceAccountsId}">
	document.forms[0].appUserFirstName.value = '';
	</c:if>
}
</script>
	</head>
	<body>
		<spring:bind path="userDeviceAccountsModel.*">
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
	

		<html:form id="userDeviceAccountsModel" method="post" action="userdeviceaccountform.html"
			commandName="userDeviceAccountsModel"  onsubmit="return onFormSubmit(this);">
			
			<table width="100%" border="0" cellpadding="0" cellspacing="1">
                
				<c:if test="${not empty param.userDeviceAccountsId}">
					<input type="hidden" name="isUpdate" id="isUpdate" value="true" />
					 
					<spring:bind path="userDeviceAccountsModel.userDeviceAccountsId">
					    <input type="hidden" name="${status.expression}" value="${status.value}" />					
				    </spring:bind>
				    <spring:bind path="userDeviceAccountsModel.deviceTypeId">
				        <input type="hidden" name="${status.expression}" value="${status.value}" />					
				    </spring:bind>
				    <%--
				    <input type="hidden" name="userDeviceId" value="${param.userDeviceId}" />
				    --%>
				</c:if>

				
				  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>MWallet/Device Account ID:&nbsp;</td>
    <td><spring:bind path="userDeviceAccountsModel.userId">
      <input type="text" onkeypress="return maskNumber(this,event)" name="${status.expression}" class="textBox" maxlength="8" value="${status.value}"
      <c:if test="${not empty param.userDeviceAccountsId}">readonly="readOnly"</c:if>
      />
      </spring:bind>
    </td>
  </tr>
				
				
				
				<tr>
    				<td width="50%" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>App. User:&nbsp;</td>
    				<td width="50%" align="left" bgcolor="FBFBFB" valign="top">
    				  <c:choose>
    				    <c:when test="${not empty param.userDeviceAccountsId&&param.flag}">
      						
        					<input type="hidden" name="appUserId" value="${userDeviceAccountsModel.appUserId}" />		
        					<input type="text" name="appUserFirstName" readonly="readonly" class="textBox" value="${userDeviceAccountsModel.appUserIdAppUserModel.firstName} ${userDeviceAccountsModel.appUserIdAppUserModel.lastName}"/>
        					<input name="lookupButton" disabled="disabled" type="button" value="-o" class="button" onclick="javascript:callLookup('appUser','appUserFormListViewPopup',400,200,'appUserFormListViewPopup.accountEnabled=true')" />
        					<img title="Clear App. User"  src="${pageContext.request.contextPath}/images/refresh.png" border="0" />
      					
      					</c:when>
      					<c:otherwise>
      					
      						<input type="hidden" name="appUserId" value="${param.appUserId}" />
        					<input type="text" name="appUserFirstName" readonly="readonly" class="textBox" value="${param.appUserFirstName}"/>
        					<input name="lookupButton" type="button" value="-o" class="button" onclick="javascript:callLookup('appUser','appUserFormListViewPopup',400,200,'appUserFormListViewPopup.accountEnabled=true')"/>
        					<img title="Clear App. User" onclick="clearAppUser();"  style="cursor:pointer" src="${pageContext.request.contextPath}/images/refresh.png" border="0" />
      					
      					</c:otherwise>
      					</c:choose>
    				</td>
  				</tr>  
				
				
				
 <tr bgcolor="FBFBFB">
      <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Device Type:&nbsp;</td>
      <td align="left">
      
        <spring:bind path="userDeviceAccountsModel.deviceTypeId">
            <select name="${status.expression}" class="textBox" <c:if test="${not empty param.userDeviceAccountsId}">disabled='disabled'</c:if>>
              <c:forEach items="${deviceTypeModelList}" var="deviceTypeModelList">
                <option value="${deviceTypeModelList.deviceTypeId}"
                  <c:if test="${status.value == deviceTypeModelList.deviceTypeId}">selected="selected"</c:if>/>
                  ${deviceTypeModelList.name}
                </option>
              </c:forEach>
            </select>

        </spring:bind>
      </td>
    </tr>

         <!-- Added by Noor-ul-haque  -->
			<tr bgcolor="FBFBFB">
				<td align="right" bgcolor="F3F3F3" class="formText">
					Is Commissioned:&nbsp;
				</td>
				<td align="left" class="formText">
					<spring:bind path="userDeviceAccountsModel.commissioned">
						<input name="${status.expression}" type="checkbox" value="true" 
							<c:if test="${empty param.userDeviceAccountsId}">disabled='disabled'</c:if>								
							<c:if test="${status.value==true}">checked="checked"</c:if> 
						/>
					</spring:bind>
				</td>
			</tr>
				

				<tr bgcolor="FBFBFB" >
            <td align="right" bgcolor="F3F3F3" class="formText">PIN Change Required:&nbsp;</td>
            <td align="left" class="formText">
		    <spring:bind path="userDeviceAccountsModel.pinChangeRequired">
			    <input name="${status.expression}" type="checkbox" value="true" 	
			    <c:if test="${status.value==true}">checked="checked"</c:if>/>			    
		    </spring:bind>
			</td>
      </tr>   
  
  <tr>
    <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Account Enabled:&nbsp;</td>
    <td width="51%" align="left" bgcolor="FBFBFB" class="formText">
    <spring:bind path="userDeviceAccountsModel.accountEnabled">
	    <input name="${status.expression}" type="checkbox" value="true" 	
	    <c:if test="${status.value==true}">checked="checked"</c:if>
	    <c:if test="${empty param.userDeviceAccountsId && empty param._save }">checked="checked"</c:if> 
								<c:if test="${status.value==false && not empty param._save}">unchecked="checked"</c:if>
	    />			    
     </spring:bind>
    </td>
  </tr>
  
        <tr bgcolor="FBFBFB" >
            <td align="right" bgcolor="F3F3F3" class="formText">Account Expired:&nbsp;</td>
            <td align="left" class="formText">
		    <spring:bind path="userDeviceAccountsModel.accountExpired">
			    <input name="${status.expression}" type="checkbox" value="true" 	
			    <c:if test="${status.value==true}">checked="checked"</c:if>/>			    
		    </spring:bind>
			</td>
      </tr>
          
      <tr bgcolor="FBFBFB" >
            <td align="right" bgcolor="F3F3F3" class="formText">Account Locked:&nbsp;</td>
            <td align="left" class="formText">
		    <spring:bind path="userDeviceAccountsModel.accountLocked">
			    <input name="${status.expression}" type="checkbox" value="true" 	
			    <c:if test="${status.value==true}">checked="checked"</c:if>/>			    
		    </spring:bind>
			</td>
      </tr>    
      
      <tr bgcolor="FBFBFB" >
            <td align="right" bgcolor="F3F3F3" class="formText">Credentials Expired:&nbsp;</td>
            <td align="left" class="formText">
		    <spring:bind path="userDeviceAccountsModel.credentialsExpired">
			    <input name="${status.expression}" type="checkbox" value="true" 	
			    <c:if test="${status.value==true}">checked="checked"</c:if>/>			    
		    </spring:bind>
			</td>
      </tr>
  
             
				<tr>
					
					<td colspan="2" align="middle" class="formText"><GenericToolbar:toolbar formName="userDeviceAccountsModel" /></td>

		
					
				</tr>
			
			</table>
		</html:form>
		
  <v:javascript formName="userDeviceAccountsModelNew" staticJavascript="false"/>
  <script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"> </script>	
  <script type="text/javascript">

		highlightFormElements();

		function popupCallback(src,popupName,columnHashMap)
		{

			if(src=="appUser")
  			{
    			document.forms.userDeviceAccountsModel.appUserId.value = columnHashMap.get('PK');
    			document.forms.userDeviceAccountsModel.appUserFirstName.value = columnHashMap.get('firstName') + ' ' + columnHashMap.get('lastName') ;
  				
 			 }
		
		
		}

		function onFormSubmit(theForm) {
			return validateUserDeviceAccountsModelNew(theForm);
		}		

	</script>

	
	
	</body>
</html>

