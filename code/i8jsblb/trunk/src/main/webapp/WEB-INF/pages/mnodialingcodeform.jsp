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
    document.forms.mnoDialingCodeForm.mnoId.value = columnHashMap.get('PK');
    document.forms.mnoDialingCodeForm.mnoName.value = columnHashMap.get('name');
  }

}
</script>
<meta name="title" content="MNO Dialing Code"/>


</head>
<body>

<spring:bind path="mnoDialingCodeModel.*">
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
<form id="mnoDialingCodeForm" method="post" action="mnodialingcodeform.html" onsubmit="return onFormSubmit(this)">
 <c:if test="${not empty param.mnoDialingCodeId}">
          <input type="hidden" name="isUpdate" id="isUpdate" value="true"/>
        </c:if>

        <spring:bind path="mnoDialingCodeModel.mnoDialingCodeId">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>
        

        <spring:bind path="mnoDialingCodeModel.createdBy">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>

 

           <spring:bind path="mnoDialingCodeModel.createdOn">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
          </spring:bind>

 
  <tr>
    <td colspan="2" align="center" bgcolor="FBFBFB">&nbsp;</td>
  </tr>
 
   <tr bgcolor="FBFBFB">
      <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>MNO:&nbsp;</td>
      <td align="left">
        <spring:bind path="mnoDialingCodeModel.mnoId">
            <select name="${status.expression}" class="textBox">              
              <c:forEach items="${mnoModelList}" var="mnoModelList">
                <option value="${mnoModelList.mnoId}"
                  <c:if test="${status.value == mnoModelList.mnoId}">selected="selected"</c:if>/>
                  ${mnoModelList.name}
                </option>
              </c:forEach>
            </select>
        </spring:bind>
      </td>
    </tr>
  
  <tr>
    <td width="46%" align="right" bgcolor="F3F3F3"  class="formText"><span style="color:#FF0000">*</span>MNO Dialing Code:&nbsp;</td>
    <td width="54%" align="left" bgcolor="FBFBFB"><spring:bind path="mnoDialingCodeModel.code">
    <input name="${status.expression}" type="text" size="40" maxlength="4" class="textBox"  value="${status.value}"/>
    </spring:bind></td>
  </tr>

 <tr>
    <td width="46%" align="right" bgcolor="F3F3F3"  class="formText">Description:&nbsp;</td>
    <td width="54%" align="left" bgcolor="FBFBFB"><spring:bind path="mnoDialingCodeModel.description">
    <textarea name="${status.expression}" cols="50" rows="5" class="textBox" onkeyup="textAreaLengthCounter(this,250);" onkeypress="return maskCommon(this,event)">${status.value}</textarea>
    </spring:bind></td>
  </tr>

 <tr>
    <td width="46%" align="right" bgcolor="F3F3F3"  class="formText">Comments:&nbsp;</td>
    <td width="54%" align="left" bgcolor="FBFBFB"><spring:bind path="mnoDialingCodeModel.comments">
    <textarea name="${status.expression}" cols="50" rows="5" class="textBox" onkeyup="textAreaLengthCounter(this,250);" onkeypress="return maskCommon(this,event)">${status.value}</textarea>
    </spring:bind></td>
  </tr>

  <tr>
    <td colspan="2" align="center" bgcolor="FBFBFB"><GenericToolbar:toolbar formName="mnoDialingCodeForm" /></td>
  </tr>

</form>
</table>

<script type="text/javascript">

document.forms[0].mnoId.focus();
highlightFormElements();

function onFormSubmit(theForm) {
/*if(!validateFormChar(theForm)){
      		return false;
}*/
return validateMnoDialingCodeModel(theForm)  ;
}

</script>

<v:javascript formName="mnoDialingCodeModel" staticJavascript="false"/>
<script type="text/javascript" src="<c:url
value="/scripts/validator.jsp"/>"></script>
</body>
</html>
