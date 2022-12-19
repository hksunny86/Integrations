<!--Title: i8Microbank-->

<!--Description: Backened Application for POS terminal-->
<!--Author: Asad Hayat-->
<%@ include file="/common/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="decorator2">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
<script language="javascript" type="text/javascript">
function popupCallback(src,popupName,columnHashMap)
{
  if (src=="userName")
  {
    document.forms.tickerForm.appUserId.value = columnHashMap.get('appUserId');
    document.forms.tickerForm.userId.value = columnHashMap.get('userId');
  }
}
function clearAppUser(){
    document.forms.tickerForm.appUserId.value = '';
    document.forms.tickerForm.userId.value = '';
}
</script>
<meta name="title" content="Ticker String"/>


</head>
<body>

<spring:bind path="tickerListViewModel.*">
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


<table width="100%"  border="0" cellpadding="0" cellspacing="1">
<form id="tickerForm" method="post" action="tickerform.html" onsubmit="return onFormSubmit(this)">
 <c:if test="${not empty param.tickerId}">
          <input type="hidden" name="isUpdate" id="isUpdate" value="true"/>
        </c:if>

        <spring:bind path="tickerListViewModel.tickerId">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>
        

        <spring:bind path="tickerListViewModel.createdBy">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>

        <spring:bind path="tickerListViewModel.updatedBy">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
          </spring:bind>

           <spring:bind path="tickerListViewModel.createdOn">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
          </spring:bind>

           <spring:bind path="tickerListViewModel.updatedOn">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
          </spring:bind>
  <tr>
    <td colspan="2" align="center" bgcolor="FBFBFB">&nbsp;</td>
  </tr>
  
  <tr>
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Inov8 MWallet ID:&nbsp;</td>
    <td align="left" bgcolor="FBFBFB"><spring:bind path="tickerListViewModel.appUserId">
    
    <%-- 
    <select name="${status.expression}" class="textBox">
								
								<c:forEach items="${tickerModelList}"
									var="tickerModelList">
									<option value="${tickerModelList.appUserId}"
										<c:if test="${status.value == tickerModelList.appUserId}">selected="selected"</c:if>>
										${tickerModelList.username}
									</option>
								</c:forEach>
							</select>
        ${status.errorMessage}
    --%>
    <input type="hidden" name="${status.expression}" value="${status.value}" />
    </spring:bind>
    
     <c:choose>
    		    <c:when test="${not empty param.tickerId}">
    <input type="text" name="userId" id="userId" value="${tickerListViewModel.mfsId}" class="textBox" readonly="readonly"/>
     <c:choose>
    		    <c:when test="${empty tickerListViewModel.mfsId}">
    		   
    		   <input name="appUserLookupButton" disabled="disabled"  type="button" value="-o" class="button" onClick="javascript:callLookup('userName','userDeviceAccountsListViewPopup',500,250,'userDeviceAccountsListViewPopup.accountEnabled=1&userDeviceAccountsListViewPopup.deviceTypeId=1')"/>
		    <img title="Clear App User"  src="${pageContext.request.contextPath}/images/refresh.png" border="0" />       									          					
    		   
    		    </c:when>
      					<c:otherwise>
<input name="appUserLookupButton" type="button" value="-o"  disabled="disabled"class="button" onClick="javascript:callLookup('userName','userDeviceAccountsListViewPopup',500,250,'userDeviceAccountsListViewPopup.accountEnabled=1&userDeviceAccountsListViewPopup.deviceTypeId=1')"/>
    <img title="Clear App User"  src="${pageContext.request.contextPath}/images/refresh.png" border="0" />       									          					
      					
      					</c:otherwise>
      					</c:choose>
    
    				    </c:when>
      					<c:otherwise>
	<input type="text" name="userId" id="userId" value="${tickerListViewModel.mfsId}" class="textBox" readonly="readonly"/>
    <input name="appUserLookupButton" type="button" value="-o" class="button" onClick="javascript:callLookup('userName','userDeviceAccountsListViewPopup',500,250,'userDeviceAccountsListViewPopup.accountEnabled=1&userDeviceAccountsListViewPopup.deviceTypeId=1')"/>
    <img title="Clear App User" onclick="clearAppUser()"  style="cursor:pointer" src="${pageContext.request.contextPath}/images/refresh.png" border="0" />       					
      					</c:otherwise>
      					</c:choose>
    
    
    </td>
  </tr>
  <tr>
    <td width="46%" align="right" bgcolor="F3F3F3"  class="formText"><span style="color:#FF0000">*</span>Ticker String:&nbsp;</td>
    <td width="54%" align="left" bgcolor="FBFBFB"><spring:bind path="tickerListViewModel.tickerString"><input name="${status.expression}" type="text" onkeypress="return maskCommon(this,event)" size="40" class="textBox" maxlength="250" value="${status.value}"/></spring:bind></td>

  
  
  </tr>

  <tr>
    <td colspan="2" align="center" bgcolor="FBFBFB">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <%-- 
        <button onclick="return applyDefaultTickerString();" class="button">Default Ticker String</button> 
        --%>
        <input class="button" type="button" onclick="javascript:onSave(document.forms.tickerForm,null);" value=" Save " name="_save"/>
        <input class="button" type="button" onclick="javascript:document.forms.tickerForm.reset();" value=" Cancel " name="_cancel"/>
        <%-- 
        <GenericToolbar:toolbar formName="tickerForm" />
        --%>
    </td>
  </tr>

</form>
</table>

<script type="text/javascript">

  document.forms[0].tickerString.focus();
highlightFormElements();
function onFormSubmit(theForm) {
	return validateTickerListViewModel(theForm) ;
}
function applyDefaultTickerString(){
    document.forms[0].tickerString.value = '<c:out value="${defaultTickerString}"></c:out>';
    return false;
}
</script>

<v:javascript formName="tickerListViewModel" staticJavascript="false"/>
<script type="text/javascript" src="<c:url
value="/scripts/validator.jsp"/>"></script>
</body>
</html>
