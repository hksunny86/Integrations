<!--Author:Asad Hayat-->
<%@ include file="/common/taglibs.jsp"%>

<spring:bind path="actionModel.*">
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
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<meta name="title" content="Action"/>

</head>
<body>

<table width="100%"  border="0" cellpadding="0" cellspacing="1">
<form id="actionForm" method="post" action="actionform.html" onSubmit="return onFormSubmit(this)">

<c:if test="${not empty param.actionId}">
  <input type="hidden"  name="isUpdate" id="isUpdate" value="true"/>
</c:if>

<spring:bind path="actionModel.actionId">
  <input type="hidden" name="${status.expression}" value="${status.value}"/>
</spring:bind>

<spring:bind path="actionModel.createdBy">
  <input type="hidden" name="${status.expression}" value="${status.value}"/>
</spring:bind>

<spring:bind path="actionModel.updatedBy">
  <input type="hidden" name="${status.expression}" value="${status.value}"/>
</spring:bind>

<spring:bind path="actionModel.createdOn">
  <input type="hidden" name="${status.expression}" value="${status.value}"/>
</spring:bind>

<spring:bind path="actionModel.updatedOn">
  <input type="hidden" name="${status.expression}" value="${status.value}"/>
</spring:bind>

<spring:bind path="actionModel.versionNo">
  <input type="hidden" name="${status.expression}" value="${status.value}"/>
</spring:bind>


 <tr bgcolor="FBFBFB">
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText"><p>Action Name:&nbsp;</p></td>
            <td width="50%" align="left">
		    <spring:bind path="actionModel.name">
			    <input type="text" name="${status.expression}" value="${status.value}" class="textBox" maxlength="50">
		    </spring:bind>
	    </td>
          </tr>

 <tr bgcolor="FBFBFB" >
            <td align="right" bgcolor="F3F3F3" class="formText" ><p>Description:&nbsp;</p></td>
            <td align="left">

	    <spring:bind path="actionModel.description">
		    <textarea name="${status.expression}" class="textBox" cols="50" rows="5">${status.value}</textarea>
	    </spring:bind>

	    </td>
	  </tr>

           <tr bgcolor="FBFBFB" >
            <td align="right" bgcolor="F3F3F3" class="formText" ><p>Comments:&nbsp;</p></td>
            <td align="left">

	    <spring:bind path="actionModel.comments">
		    <textarea name="${status.expression}" class="textBox" cols="50" rows="5">${status.value}</textarea>
	    </spring:bind>

	    </td>
	  </tr>

           <tr bgcolor="FBFBFB" >
            <td align="right" bgcolor="F3F3F3" class="formText">Status:&nbsp;</td>
            <td align="left" class="formText">

	      <spring:bind path="actionModel.active">
		<input type="hidden" name="_${status.expression}"/>
              <c:if test="${not empty param.actionId}">
              <input name="${status.expression}" type="radio" value="true" <c:if test="${status.value}">checked="checked"</c:if>
              />Active&nbsp;&nbsp;&nbsp;&nbsp;
               <input name="${status.expression}" type="radio" value="false" <c:if test="${!status.value}">checked="checked"</c:if>/>Inactive
               </c:if>
               <c:if test="${empty param.actionId}">
               <input name="${status.expression}" type="radio" value="true" <c:if test="${!status.value}">checked="checked"</c:if>
              />Active&nbsp;&nbsp;&nbsp;&nbsp;
               <input name="${status.expression}" type="radio" value="false" <c:if test="${status.value}">checked="checked"</c:if>/>Inactive
               </c:if>
	      </spring:bind>
            </td>
          </tr>
          <tr>
          <td>
          <GenericToolbar:toolbar formName="actionForm" />
          </td>
          </tr>
</form>
</table>

<script type="text/javascript">



function onFormSubmit(theForm) {
return validateActionModel(theForm);
}

</script>

<v:javascript formName="actionModel" staticJavascript="false"/>
<script type="text/javascript" src="<c:url
value="/scripts/validator.jsp"/>"></script>

</body>


</html>
