<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Jawwad Farooq-->
<%@include file="/common/taglibs.jsp"%>

<html>
<head>
<meta name="decorator" content="decorator2">

<meta name="title" content="Golden Number"/>
</head>
<body>
<spring:bind path="goldenNosModel.*">
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


    <form method="post" action="goldennosform.html" onsubmit="return onFormSubmit(this);" id="GoldenNosForm">
    	<table width="100%" border="0" cellpadding="0" cellspacing="1">

  <c:if test="${not empty param.goldenNosId}">
    <input type="hidden" name="isUpdate" id="isUpdate" value="true"/>
    <spring:bind path="goldenNosModel.goldenNosId">
    	<input type="hidden" name="${status.expression}" value="${status.value}"/>
    </spring:bind>
    <spring:bind path="goldenNosId.versionNo">
    	<input type="hidden" name="${status.expression}" value="${status.value}"/>
    </spring:bind>
  </c:if>

  <tr>
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Golden No:&nbsp;</td>
    <td align="left" bgcolor="FBFBFB"><spring:bind path="goldenNosModel.goldenNumber">
    <input type="text" name="${status.expression}" class="textBox" value="${status.value}" maxlength="16"/>
    </spring:bind></td>
  </tr>
  	<tr bgcolor="FBFBFB">
      <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Device Type:&nbsp;</td>
      <td align="left">
        <spring:bind path="goldenNosModel.deviceTypeId">

            <select name="${status.expression}" class="textBox">
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
  
  <tr bgcolor="FBFBFB">
    <td height="19" colspan="2" align="center"><GenericToolbar:toolbar formName="GoldenNosForm" /></td>
  </tr>
</table>
    </form>

<script language="javascript" type="text/javascript">
document.forms.GoldenNosForm.goldenNumber.focus();
highlightFormElements();

function onFormSubmit(theForm) 
{
 		return validateGoldenNosModel(theForm);
}
</script>

<v:javascript formName="goldenNosModel" staticJavascript="false" />
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>



</body>
</html>
