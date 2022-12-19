<!--Title: i8Microbank-->
<!--Author: Asad Hayat-->
<%@include file="/decorators/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="decorator2">

<script type="text/javascript" src="scripts/global.js"></script>

<v:javascript formName="issueModel"
staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript"
src="<c:url value="/scripts/validator.jsp"/>"></script>
<meta name="title" content="Dispute"/>
<script type="text/javascript">
isIssue = <%=request.getParameter("isIssue")%>;
if(isIssue == true)
{

  var btn =  eval("window.opener.document.forms[1].Disputed<%=request.getParameter("transactionId")%>");
  btn.disabled = "disabled";
  window.close();
}

</script>
</head>

<div id="page">
        <div id="content" class="clearfix">
          <div id="main">
            <h1>Dispute</h1>
<body>
<spring:bind path="issueModel.*">
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


	<html:form  name = "issueForm" id = "issueForm" commandName="issueModel" action="p-issueform.html" method="post" onsubmit="return onFormSubmit(this)" onreset = "javascript:checkIssue(${isIssue});">

       <table width="100%" border="0" cellpadding="0" cellspacing="1">
        <html:hidden path="createdOn"/>
        <html:hidden path="updatedOn"/>
        <html:hidden path="createdBy"/>
        <html:hidden path="updatedBy"/>
        <html:hidden path="custTransCode"/>
         <html:hidden path="transactionId"/>

        <tr bgcolor="FBFBFB">
             <td colspan="2" align="center">&nbsp;</td>
           </tr>



           <tr>
             <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Transaction Code:&nbsp;&nbsp;</td>
             <td width="51%" align="left" bgcolor="FBFBFB">
               <html:input path="transactionCodeId" cssClass = "textBox" readonly = "true"/>
<%--                 <input type="text" name="${status.expression}" value="${status.value}" class="textBox" disabled="disabled"/>--%>

             </td>
           </tr>



           <tr>
             <td align="right" bgcolor="F3F3F3" class="formText">Comments:&nbsp;&nbsp; </td>
             <td align="left" bgcolor="FBFBFB">
               <html:textarea path="comments" cssClass = "textBox"/>
<%--                 <textarea name="${status.expression}" cols="50" rows="5" class="textBox">${status.value}</textarea>--%>

             </td>
           </tr>


            <tr>
           <td align="right">
           <input name="_save" type="submit" class="button"  align="right" value=" Esclate ">&nbsp;
           </td>
           <td>
           <input name="cancel" type="button" class="button" value=" Cancel " onclick="javascript:window.close();">
           </td>
           </tr>
           </table>
</html:form>


<script type="text/javascript">



function onFormSubmit(theForm) {
return validateIssueModel(theForm);
}

</script>

<v:javascript formName="issueModel" staticJavascript="false"/>
<script type="text/javascript" src="<c:url
value="/scripts/validator.jsp"/>"></script>

</body>


</html>
