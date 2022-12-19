<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Ahmad Iqbal-->
<%@include file="/common/taglibs.jsp"%>
<spring:bind path="screenModel.*">
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
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="decorator2">
<meta name="title" content="Screen"/>
</head>
<body>
<GenericToolbar:toolbar formName="screenForm" />
<table width="100%"  border="0" cellpadding="0" cellspacing="1">
<form id="screenForm" method="post" action="screenform.html" onsubmit="return onFormSubmit(this)">
 <c:if test="${not empty param.retailerId}">
          <input type="hidden" name="isUpdate" id="isUpdate" value="true"/>
        </c:if>

        <spring:bind path="screenModel.screenId">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>

        <spring:bind path="screenModel.createdBy">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>

        <spring:bind path="screenModel.updatedBy">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
          </spring:bind>

           <spring:bind path="screenModel.createdOn">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
          </spring:bind>

           <spring:bind path="screenModel.updatedOn">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
          </spring:bind>

           <spring:bind path="screenModel.comments">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
          </spring:bind>
  <tr>
    <td colspan="2" align="center" bgcolor="FBFBFB">&nbsp;</td>
  </tr>

  <tr>
    <td align="right" bgcolor="F3F3F3" class="formText">Input Field Group:&nbsp;&nbsp;&nbsp; </td>
    <td align="left" bgcolor="FBFBFB"><spring:bind path="screenModel.inputFieldGroupId">
    <input type="hidden" name="inputFieldGroupId" value="${status.value}">
    </spring:bind>
    <c:if test="${not empty screenModel.inputFieldGroupIdFieldGroupModel}">
      <input type="text" name="inputFieldGroupName" readonly="readonly" class="textBox" value="${screenModel.inputFieldGroupIdFieldGroupModel.name}"/>
      <input name="inputFieldGroupLookupButton" type="button" value="-o" class="button" onclick="javascript:callLookup('inputFieldGroupName','fieldGroupPopup',400,200)">
    </c:if>
    <c:if test="${empty screenModel.inputFieldGroupIdFieldGroupModel}">
      <input type="text" name="inputFieldGroupName" readonly="readonly" class="textBox"/>
      <input name="inputFieldGroupLookupButton" type="button" value="-o" class="button" onclick="javascript:callLookup('inputFieldGroupName','fieldGroupPopup',400,200)" >
    </c:if>
			    </td>
  </tr>
  <tr>
    <td align="right" bgcolor="F3F3F3" class="formText">Output Field Group:&nbsp;&nbsp;&nbsp; </td>
    <td align="left" bgcolor="fbfbfb">
      <spring:bind path="screenModel.outputFieldGroupId">
        <input type="hidden" name="outputFieldGroupId" value="${status.value}">
      </spring:bind>
      <c:if test="${not empty screenModel.outputFieldGroupIdFieldGroupModel}">
        <input type="text" name="outputFieldGroupName" readonly="readonly" class="textBox" value="${screenModel.outputFieldGroupIdFieldGroupModel.name}"/>
        <input name="outputFieldGroupLookupButton" type="button" value="-o" class="button" onclick="javascript:callLookup('outputFieldGroupName','fieldGroupPopup',400,200)">
      </c:if>
      <c:if test="${empty screenModel.outputFieldGroupIdFieldGroupModel}">
      <input type="text" name="outputFieldGroupName" readonly="readonly" class="textBox"/>
      <input name="outputFieldGroupLookupButton" type="button" value="-o" class="button" onclick="javascript:callLookup('outputFieldGroupName','fieldGroupPopup',400,200)" >
      </c:if>
    </td>
  </tr>
  <tr>
    <td width="50%" align="right" bgcolor="F3F3F3" class="formText">Product:&nbsp;&nbsp;&nbsp; </td>
    <td width="50%" align="left" bgcolor="FBFBFB"><spring:bind path="screenModel.productId">
			    <input type="hidden" name="productId" value="${status.value}">
                </spring:bind>
                <c:if test="${not empty screenModel.productIdProductModel}">
                <input type="text" name="productName" readonly="readonly" class="textBox" value="${screenModel.productIdProductModel.name}"/>
                <input name="productLookupButton" type="button" value="-o" class="button" onclick="javascript:callLookup('productName','productPopup',600,200)">
                </c:if>
                <c:if test="${empty screenModel.productIdProductModel}">
                <input type="text" name="productName" readonly="readonly" class="textBox"/>
                <input name="productLookupButton" type="button" value="-o" class="button" onclick="javascript:callLookup('productName','productPopup',600,200)" >
                </c:if>

  </tr>
  <tr>
    <td width="50%" align="right" bgcolor="F3F3F3" class="formText">Action:&nbsp;&nbsp;&nbsp; </td>
    <td width="50%" align="left" bgcolor="FBFBFB">
      <spring:bind path="screenModel.actionId">
      <input type="hidden" name="actionId" value="${status.value}">
      </spring:bind>
      <c:if test="${not empty screenModel.actionIdActionModel}">
        <input type="text" name="actionName" readonly="readonly" class="textBox" value="${screenModel.actionIdActionModel.name}"/>
        <input name="actionLookupButton" type="button" value="-o" class="button" onclick="javascript:callLookup('actionName','actionPopup',400,200)">
      </c:if>
      <c:if test="${empty screenModel.actionIdActionModel}">
        <input type="text" name="actionName" readonly="readonly" class="textBox"/>
        <input name="actionLookupButton" type="button" value="-o" class="button" onclick="javascript:callLookup('actionName','actionPopup',400,200)">
      </c:if>

</tr>
<tr>
            <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Name:&nbsp;&nbsp;</td>
            <td width="51%" align="left" bgcolor="FBFBFB">
            <spring:bind path="screenModel.name">
                <input type="text" name="${status.expression}" value="${status.value}" class="textBox"/>
            </spring:bind></td>
          </tr>
          <tr>
    <td align="right" bgcolor="#F3F3F3" class="formText">Description: &nbsp;</td>
    <td bgcolor="FBFBFB"><spring:bind path="screenModel.description"><textarea name="${status.expression}" cols="50" rows="5" class="textBox">${status.value}</textarea></spring:bind></td>
  </tr>
  <tr>
    <td align="right" bgcolor="#F3F3F3" class="formText">Status:&nbsp;&nbsp;&nbsp; </td>
            <td align="left" bgcolor="#FBFBFB" class="formText"><spring:bind path="screenModel.active">
              <input type="hidden" name="_${status.expression}"/>
              <c:if test="${not empty param.supplierId}">
              <input name="${status.expression}" type="radio" value="true" <c:if test="${status.value}">checked="checked"</c:if>
              />Active&nbsp;&nbsp;&nbsp;&nbsp;
               <input name="${status.expression}" type="radio" value="false" <c:if test="${!status.value}">checked="checked"</c:if>/>Inactive
               </c:if>
               <c:if test="${empty param.supplierId}">
               <input name="${status.expression}" type="radio" value="true" <c:if test="${!status.value}">checked="checked"</c:if>
              />Active&nbsp;&nbsp;&nbsp;&nbsp;
               <input name="${status.expression}" type="radio" value="false" <c:if test="${status.value}">checked="checked"</c:if>/>Inactive
               </c:if>
              </spring:bind>
              </td>
          </tr>
  <tr>
    <td colspan="2" align="center" bgcolor="FBFBFB">&nbsp;</td>
  </tr>

</form>
</table>
<script language="javascript" type="text/javascript">
function popupCallback(src,popupName,columnHashMap)
{
  if (src=="productName")
  {
    document.forms.screenForm.productId.value = columnHashMap.get('PK');
    document.forms.screenForm.productName.value = columnHashMap.get('name');
  }
  if (src=="actionName")
  {
    document.forms.screenForm.actionId.value = columnHashMap.get('PK');
    document.forms.screenForm.actionName.value = columnHashMap.get('name');
  }
  if (src=="inputFieldGroupName")
  {
    document.forms.screenForm.inputFieldGroupId.value = columnHashMap.get('PK');
    document.forms.screenForm.inputFieldGroupName.value = columnHashMap.get('name');
  }
  if (src=="outputFieldGroupName")
  {
    document.forms.screenForm.outputFieldGroupId.value = columnHashMap.get('PK');
    document.forms.screenForm.outputFieldGroupName.value = columnHashMap.get('name');
  }
}
function onFormSubmit(theForm)
{
  return validateScreenModel(theForm);
}
</script>
<v:javascript formName="screenModel" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>">
</script>
</body>
</html>
