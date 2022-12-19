<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="decorator2">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
<meta name="title" content="Upload Entries"/>
</head>
<body>

<spring:bind path="shipmentUploadModel.*"></spring:bind>
<spring:bind path="shipmentUploadModel.*">
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
      <c:out value="${msg}" escapeXml="false"/>
      <br/>
    </c:forEach>
  </div>
  <c:remove var="messages" scope="session"/>
</c:if>

<table width="100%" border="0" cellpadding="0" cellspacing="1" onmouseover="javascript:document.forms[0].file.onfocus='';document.forms[0].file.onmouseover=''; ">
  <form name="uploadEntryForm" action="uploadentryform.html" method="POST" enctype="multipart/form-data" onsubmit="return submitForm();">

  <tr>
    <td colspan="2" align="left" bgcolor="FBFBFB" class="text" >&nbsp;</td>
  </tr>
  <tr>
    <td width="52%" align="center" bgcolor="FBFBFB" class="text" colspan="2">

      <spring:bind path="shipmentUploadModel.file">
        <input type="file" name="file" class="button" style="height: 20px;width: 200px" />      </spring:bind>
&nbsp;&nbsp;    </td>
  </tr>
  <tr>
    <td colspan="2" align="left" bgcolor="FBFBFB" class="text">&nbsp;</td>
  </tr>
  <tr>
    <td width="52%" align="right" bgcolor="FBFBFB" class="text">
      
        <spring:bind path="shipmentUploadModel.userName">
          <input type="hidden" name="${status.expression}" value="true" />
  		</spring:bind>        
          <input type="checkbox" name="$_{status.expression}" value="true"
          checked="checked" disabled="disabled" />
          ${status.errorMessage}
        
&nbsp;&nbsp;    </td>

    <td width="48%" align="left" bgcolor="F3F3F3" class="formText">User Name</td>
  </tr>
  <tr>

    <td width="52%" align="right" bgcolor="FBFBFB" class="text">
      
        <spring:bind path="shipmentUploadModel.pin">
          <input type="hidden" name="${status.expression}" value="true"/>
          <input type="checkbox" name="_${status.expression}" value="true"
          checked="checked" disabled="disabled" />
          ${status.errorMessage}        </spring:bind>
&nbsp;&nbsp;    </td>
    <td width="48%" align="left" bgcolor="F3F3F3" class="formText">Password / PIN</td>
  </tr>
  <tr>
    <td width="52%" align="right" bgcolor="FBFBFB" class="text">
      
        <spring:bind path="shipmentUploadModel.serialNo">
          <input type="hidden" name="${status.expression}" value="true"/>
          <input type="checkbox" name="_${status.expression}" value="true"
          checked="checked" disabled="disabled" />
          ${status.errorMessage}        </spring:bind>
&nbsp;&nbsp;    </td>
    <td width="48%" align="left" bgcolor="F3F3F3" class="formText">Serial number</td>
  </tr>
  <tr>
    <td width="52%" align="right" bgcolor="FBFBFB" class="text">
      
        <spring:bind path="shipmentUploadModel.additionalField1">
          <input type="hidden" name="_${status.expression}"/>
          <input type="checkbox" name="${status.expression}" value="true"
          <c:if test="${status.value}">checked="checked" </c:if> />
          ${status.errorMessage}        </spring:bind>
&nbsp;&nbsp;    </td>
    <td width="48%" align="left" bgcolor="F3F3F3" class="formText">Additional Field1</td>
  </tr>
  <tr>
    <td width="52%" align="right" bgcolor="FBFBFB" class="text">
      
        <spring:bind path="shipmentUploadModel.additionalField2">
          <input type="hidden" name="_${status.expression}" />
          <input type="checkbox" name="${status.expression}" value="true"
          <c:if test="${status.value}">checked="checked" </c:if> />
          ${status.errorMessage}        </spring:bind>
&nbsp;&nbsp;    </td>
    <td width="48%" align="left" bgcolor="F3F3F3" class="formText">Additional Field2</td>
  </tr>
  <tr>

    <input type="hidden" name="shipmentId" value="<c:out value="${param.shipmentId}"/>"/>
    <input type="hidden" name="productId" value="<c:out value="${param.productId}"/>"/>
    <input type="hidden" name="creditAmount" value="<c:out value="${param.creditAmount}"/>"/>
    <td colspan="2" align="center" bgcolor="FBFBFB">&nbsp;</td>
  </tr>
  <tr>

    <td height="24" colspan="2" align="center" bgcolor="FBFBFB" class="text">
      
        <input type="submit" name="upload" class="button" value=" Upload "/>
        


      <input name="cancel" type="button" class="button" value=" Cancel " onclick="javascript:document.forms[0].reset();"/>    </td>
    </tr>
  </form>
</table>
<script language="javascript" type="text/javascript">

function submitForm()
{ //alert('IN form submit');
  if(document.uploadEntryForm.file.value == ''){
      alert('Browse a CSV File.');
      return false;
  }
  else{
      return true;
  }
  //document.uploadEntryForm.submit();
}
</script>
</body>
</html>
