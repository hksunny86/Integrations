<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Jawwad Farooq-->
<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>

<html>
<head>
<meta name="decorator" content="decorator2">

<meta name="title" content="Channel"/>
</head>
<body>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
<spring:bind path="deviceTypeModel.*">
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


    <form method="post" action="devicetypeform.html" onsubmit="return onFormSubmit(this);" id="DeviceTypeForm">
    	<table width="100%" border="0" cellpadding="0" cellspacing="1">

  <c:if test="${not empty param.deviceTypeId}">
    <input type="hidden" name="isUpdate" id="isUpdate" value="true"/>
    <spring:bind path="deviceTypeModel.deviceTypeId">
    	<input type="hidden" name="${status.expression}" value="${status.value}"/>
    </spring:bind>
    <spring:bind path="deviceTypeModel.versionNo">
    	<input type="hidden" name="${status.expression}" value="${status.value}"/>
    </spring:bind>
  </c:if>

  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Channel Name:&nbsp;</td>
    <td><spring:bind path="deviceTypeModel.name">
      <input type="text" name="${status.expression}" class="textBox" maxlength="50" value="${status.value}" onkeypress="return maskAlphaWithSp(this,event)"  >
      </spring:bind>
    </td>
  </tr>
  <tr>
    <td align="right" bgcolor="F3F3F3" class="formText">Description:&nbsp;</td>
    <td align="left" bgcolor="FBFBFB"><spring:bind path="deviceTypeModel.description">
    <textarea name="${status.expression}" cols="50" rows="5" class="textBox">${status.value}</textarea>
    </spring:bind></td>
  </tr>


  <tr>
    <td align="right" bgcolor="F3F3F3" class="formText">Comments:&nbsp;</td>
    <td align="left" bgcolor="FBFBFB"><spring:bind path="deviceTypeModel.comments">
    <textarea name="${status.expression}" cols="50" rows="5" class="textBox">${status.value}</textarea>
    </spring:bind></td>
  </tr>
  <tr bgcolor="FBFBFB">
    <td height="19" colspan="2" align="center"></td>
  </tr>
  <tr bgcolor="FBFBFB">
    <td colspan="2" align="center">
    		<GenericToolbar:toolbar formName="DeviceTypeForm" />
</td>
  </tr>
</table>
    </form>

<script language="javascript" type="text/javascript">

document.forms.DeviceTypeForm.name.focus();
highlightFormElements();

function onFormSubmit(theForm) 
{
 		return validateDeviceTypeModel(theForm);
}
</script>

<v:javascript formName="deviceTypeModel" staticJavascript="false" />
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>



</body>
</html>
