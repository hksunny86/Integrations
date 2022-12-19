<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Jawwad Farooq-->
<%@include file="/common/taglibs.jsp"%>

<html>
<head>
<meta name="decorator" content="decorator2">

<meta name="title" content="Switch Finder"/>
</head>
<body>

<spring:bind path="switchFinderModel.*">
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

    <form method="post" action="switchfinderform.html" onsubmit="return onFormSubmit(this);" id="SwitchFinderForm">
    	<table width="100%" border="0" cellpadding="0" cellspacing="1">

  <c:if test="${not empty param.switchFinderId}">
    <input type="hidden" name="isUpdate" id="isUpdate" value="true"/>
    <spring:bind path="switchFinderModel.switchFinderId">
    	<input type="hidden" name="${status.expression}" value="${status.value}"/>
    </spring:bind>
    <spring:bind path="switchFinderModel.bankId">
	   <input type="hidden" name="${status.expression}" value="${status.value}"/>
	</spring:bind>
	<spring:bind path="switchFinderModel.paymentModeId">
	    <input type="hidden" name="${status.expression}" value="${status.value}"/>
	</spring:bind>
  </c:if>
  
    <tr bgcolor="FBFBFB">
      <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Switch:&nbsp;</td>
      <td align="left">
        <spring:bind path="switchFinderModel.switchId">
            <select name="${status.expression}" class="textBox">              
              <c:forEach items="${switchModelList}" var="switchModelList">
                <option value="${switchModelList.switchId}"
                  <c:if test="${status.value == switchModelList.switchId}">selected="selected"</c:if>/>
                  ${switchModelList.name}
                </option>
              </c:forEach>
            </select>
        </spring:bind>
      </td>
    </tr>
  
    <tr bgcolor="FBFBFB">
      <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Bank:&nbsp;</td>
      <td align="left">
        <spring:bind path="switchFinderModel.bankId">
            <select name="${status.expression}" class="textBox" <c:if test="${not empty param.switchFinderId}">disabled="disabled"</c:if> >>              
              <c:forEach items="${bankModelList}" var="bankModelList">
                <option value="${bankModelList.bankId}"
                  <c:if test="${status.value == bankModelList.bankId}">selected="selected"</c:if>/>
                  ${bankModelList.name}
                </option>
              </c:forEach>
            </select>
        </spring:bind>
      </td>
    </tr>
  
  
  <tr bgcolor="FBFBFB">
      <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Payment Mode:&nbsp;</td>
      <td align="left">
        <spring:bind path="switchFinderModel.paymentModeId">
            <select name="${status.expression}" class="textBox" <c:if test="${not empty param.switchFinderId}">disabled="disabled"</c:if> >>
<!-- <option value=""></option> -->              
              <c:forEach items="${paymentModeModelList}" var="paymentModeModelList">
                <option value="${paymentModeModelList.paymentModeId}"
                  <c:if test="${status.value == paymentModeModelList.paymentModeId}">selected="selected"</c:if>/>
                  ${paymentModeModelList.name}
                </option>
              </c:forEach>
            </select>
        </spring:bind>
      </td>
    </tr>
  <tr bgcolor="FBFBFB">
    <td height="19" colspan="2" align="center"></td>
  </tr>
  <tr bgcolor="FBFBFB">
    <td colspan="2" align="center">
    <GenericToolbar:toolbar formName="SwitchFinderForm" />
    </td>
  </tr>
</table>
    </form>

<script language="javascript" type="text/javascript">
document.forms.SwitchFinderForm.switchId.focus();
highlightFormElements();


function onFormSubmit(theForm) 
{
 		return validateSwitchFinderModel(theForm);
}
</script>

<v:javascript formName="switchFinderModel" staticJavascript="false" />
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>



</body>
</html>
