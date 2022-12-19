<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Jawwad Farooq-->
<%@include file="/common/taglibs.jsp"%>

<html>
<head>
<meta name="decorator" content="decorator2">

<meta name="title" content="Service"/>
<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
</head>
<body>
<spring:bind path="serviceModel.*">
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


    <form method="post" action="serviceform.html" onsubmit="return onFormSubmit(this);" id="ServiceForm">
    	<table width="100%" border="0" cellpadding="0" cellspacing="1">

  <c:if test="${not empty param.serviceId}">
    <spring:bind path="serviceModel.name">
      <input type="hidden" name="${status.expression}" value="${status.value}" >
      </spring:bind>
    <input type="hidden" name="isUpdate" id="isUpdate" value="true"/>
    <spring:bind path="serviceModel.serviceId">
    	<input type="hidden" name="${status.expression}" value="${status.value}"/>
    </spring:bind>
    <spring:bind path="serviceModel.versionNo">
    	<input type="hidden" name="${status.expression}" value="${status.value}"/>
    </spring:bind>
    <spring:bind path="serviceModel.serviceTypeIdServiceTypeModel.name">
    	<input type="hidden" name="${status.expression}" value="${status.value}"/>
    </spring:bind>
  </c:if>
  
  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Service:&nbsp;</td>
    <td><spring:bind path="serviceModel.name">
      <input type="text" name="${status.expression}" class="textBox" maxlength="50" value="${status.value}" <c:if test="${not empty param.serviceId}">disabled='disabled'</c:if> >
      </spring:bind>
    </td>
  </tr>
  
  	<tr bgcolor="FBFBFB">
      <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Service Type:&nbsp;</td>
      <td align="left">
        <spring:bind path="serviceModel.serviceTypeId">
            <select name="${status.expression}" class="textBox">
              <c:forEach items="${serviceTypeModelList}" var="serviceTypeModelList">
                <option value="${serviceTypeModelList.serviceTypeId}"
                  <c:if test="${status.value == serviceTypeModelList.serviceTypeId}">selected="selected"</c:if>/>
                  ${serviceTypeModelList.name}
                </option>
              </c:forEach>
            </select>

        </spring:bind>
      </td>
    </tr>
  
  <tr>
    <td align="right" bgcolor="F3F3F3" class="formText">Description:&nbsp;</td>
    <td align="left" bgcolor="FBFBFB"><spring:bind path="serviceModel.description"><textarea name="${status.expression}" cols="50" rows="5" class="textBox" onkeyup="textAreaLengthCounter(this,250);" onkeypress="return maskCommon(this,event)">${status.value}</textarea></spring:bind></td>
  </tr>
  <tr>
    <td align="right" bgcolor="F3F3F3" class="formText">Comments:&nbsp;</td>
    <td align="left" bgcolor="FBFBFB"><spring:bind path="serviceModel.comments"><textarea name="${status.expression}" cols="50" rows="5" class="textBox" onkeyup="textAreaLengthCounter(this,250);" onkeypress="return maskCommon(this,event)">${status.value}</textarea></spring:bind></td>
  </tr>
<tr>
    <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Active:&nbsp;</td>
    <td width="51%" align="left" bgcolor="FBFBFB" class="formText">
    <spring:bind path="serviceModel.active">
	    <input name="${status.expression}" type="checkbox" value="true" 	
  <c:if test="${status.value==true}">checked="checked"</c:if>
				<c:if test="${empty param.serviceId && empty param._save }">checked="checked"</c:if> 
				<c:if test="${status.value==false && not empty param._save}">unchecked="checked"</c:if>
	    />			    
     </spring:bind>
    </td>
  </tr>
  
  <tr bgcolor="FBFBFB">
    <td colspan="2" align="center">
    	<GenericToolbar:toolbar formName="ServiceForm" />
    </td>
  </tr>
 
</table>
    </form>

<script language="javascript" type="text/javascript">
document.forms[0].serviceTypeId.focus();

highlightFormElements();

function popupCallback(src,popupName,columnHashMap)
{
   document.forms.ServiceForm.serviceTypeId.value = columnHashMap.get('PK');
   document.forms.ServiceForm.serviceTypeName.value = columnHashMap.get('name');
}

function onFormSubmit(theForm) 
{
    /*if(!validateFormChar(theForm)){
      	return false;
    }*/
 	return validateServiceModel(theForm);
}
</script>

<v:javascript formName="serviceModel" staticJavascript="false" />
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>



</body>
</html>
