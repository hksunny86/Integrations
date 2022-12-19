<!--Title: i8Microbank Demo-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Ahmad Iqbal-->
<%@include file="/common/taglibs.jsp"%>
<spring:bind path="transactionModel.*">
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
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
<meta name="title" content="Retailer to Retailer Credit Transfer"/>
</head>
<body>
<GenericToolbar:toolbar formName="transactionForm"  />
<table width="100%"  border="0" cellpadding="0" cellspacing="1">
<form id="transactionForm" method="post" action="<c:url value="/retailerretailercreditform.html"/>" onsubmit="return onFormSubmit(this)">

        <spring:bind path="transactionModel.transactionId">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>

        <spring:bind path="transactionModel.createdOn">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>

        <spring:bind path="transactionModel.updatedOn">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>

        <spring:bind path="transactionModel.versionNo">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>

        <spring:bind path="transactionModel.createdBy">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>

        <spring:bind path="transactionModel.updatedBy">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>

        <spring:bind path="transactionModel.transactionTypeId">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>

        <spring:bind path="transactionModel.transactionCodeId">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>

        <spring:bind path="transactionModel.totalCommissionAmount">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>

        <spring:bind path="transactionModel.fromDistContactName">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>

        <spring:bind path="transactionModel.fromDistContactMobNo">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>

        <spring:bind path="transactionModel.toDistContactName">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>

        <spring:bind path="transactionModel.toDistContactMobNo">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>

        <spring:bind path="transactionModel.customerMobileNo">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>

        <spring:bind path="transactionModel.bankResponseCode">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>

  <tr>
    <td colspan="2" align="center" bgcolor="#FBFBFB">&nbsp;</td>
  </tr>
  <tr>
    <td align="right" bgcolor="F3F3F3" class="formText">From Retailer:&nbsp;&nbsp; </td>
    <td align="left" bgcolor="FBFBFB">
    <spring:bind path="transactionModel.fromRetContactId">
        <input type="hidden" name="${status.expression}" value="${status.value}"/>
    </spring:bind>
    <spring:bind path="transactionModel.fromRetContactName">
      <input type="text" name="fromRetContactName" class="textBox" value="${status.value}">
    </spring:bind>
    <input name="fromRetailerContactLookupButton" type="button" value="-o" class="button" onclick="javascript:callLookup('fromRetContactName','retailerContactPopup',400,200,'retailerContactPopup.head=' + true)">
    <spring:bind path="transactionModel.fromRetContactMobNo">
      <input type="hidden" name="fromRetContactMobNo">
    </spring:bind>
    </td>
  </tr>
  <tr>
    <td align="right" bgcolor="F3F3F3" class="formText">To Retailer:&nbsp;&nbsp; </td>
    <td align="left" bgcolor="FBFBFB">
    <spring:bind path="transactionModel.toRetContactId">
        <input type="hidden" name="${status.expression}" value="${status.value}"/>
    </spring:bind>
    <spring:bind path="transactionModel.toRetContactName">
      <input type="text" name="toRetContactName" class="textBox" >
    </spring:bind>
    <input name="toRetailerContactLookupButton" type="button" value="-o" class="button" onclick="javascript:callLookup('toRetContactName','retailerContactPopup',400,200,'retailerContactPopup.head=' + true)">
    <spring:bind path="transactionModel.toRetContactMobNo">
      <input type="hidden" name="toRetContactMobNo">
    </spring:bind>
    </td>
  </tr>
  <tr>
    <td width="50%" align="right" bgcolor="F3F3F3" class="formText">Device Type Name:&nbsp;&nbsp; </td>
    <td width="50%" align="left" bgcolor="FBFBFB">
    <spring:bind path="transactionModel.deviceTypeId">
    <input type="hidden" name="deviceTypeId" value="${status.value}">
    </spring:bind>
    <input type="text" name="deviceTypeName" readonly="readonly" class="textBox"/>
    <input name="deviceTypeLookupButton" type="button" value="-o" class="button" onclick="javascript:callLookup('deviceTypeName','deviceTypePopup',400,200)" >
    </td>
  </tr>
  <tr>
    <td width="50%" align="right" bgcolor="F3F3F3" class="formText">Payment Mode:&nbsp;&nbsp; </td>
    <td width="50%" align="left" bgcolor="FBFBFB">
    <spring:bind path="transactionModel.paymentModeId">
    <input type="hidden" name="${status.expression}" value="${status.value}">
    </spring:bind>
    <input type="text" name="paymentModeName" readonly="readonly" class="textBox"/>
    <input name="paymentModeLookupButton" type="button" value="-o" class="button" onclick="javascript:callLookup('paymentModeName','paymentModePopup',400,200)" >
    </td>
  </tr>
  <tr>
    <td align="right" bgcolor="F3F3F3" class="formText">Credit Amount:&nbsp;&nbsp; </td>
    <td align="left" bgcolor="FBFBFB">
    <spring:bind path="transactionModel.totalAmount">
      <input type="text" name="${status.expression}" class="textBox" value="${status.value}">
    </spring:bind>
    </td>
  </tr>
  <tr>
    <td align="right" bgcolor="F3F3F3" class="formText">Transaction Amount:&nbsp;&nbsp; </td>
    <td align="left" bgcolor="FBFBFB">
    <spring:bind path="transactionModel.transactionAmount">
      <input type="text" name="${status.expression}" class="textBox" value="${status.value}">
    </spring:bind>
    </td>
  </tr>

  <tr>
    <td colspan="2" align="center" bgcolor="FBFBFB">&nbsp;</td>
  </tr>
  <tr bgcolor="FBFBFB">
            <td colspan="2" align="center"><input name="_save" type="submit" class="button" value=" Submit " onclick="bCancel=false">&nbsp;&nbsp;<input name="_cancel" type="button" class="button" value=" Cancel " onclick="window.location.href='home.html';"></td>
          </tr>
</form>
</table>
<script type="text/javascript" language="javascript">
Form.focusFirstElement($('transactionForm'));
highlightFormElements();
function popupCallback(src,popupName,columnHashMap)
{
  if(src=='retailerName')
  {
    document.forms.transactionForm.retailerId.value = columnHashMap.get('PK');
    document.forms.transactionForm.retailerName.value = columnHashMap.get('name');
    populateRetailerContact(document.forms.transactionForm.retailerContactId.value);
  }
  if(src=='toRetContactName')
  {

    document.forms.transactionForm.toRetContactId.value = columnHashMap.get('PK');
    document.forms.transactionForm.toRetContactName.value = columnHashMap.get('name');
    document.forms.transactionForm.toRetContactMobNo.value = columnHashMap.get('mobileNo');

  }
  if(src=='fromRetContactName')
  {

    document.forms.transactionForm.fromRetContactId.value = columnHashMap.get('PK');
    document.forms.transactionForm.fromRetContactName.value = columnHashMap.get('name');
    document.forms.transactionForm.fromRetContactMobNo.value = columnHashMap.get('mobileNo');

  }
  if(src=='paymentTypeName')
  {
    document.forms.transactionForm.paymentModeId.value = columnHashMap.get('PK');
    document.forms.transactionForm.paymentModeName.value = columnHashMap.get('name');
  }
  if(src=='deviceTypeName')
  {

    document.forms.transactionForm.deviceTypeId.value = columnHashMap.get('PK');
    document.forms.transactionForm.deviceTypeName.value = columnHashMap.get('name');

  }
}

function onFormSubmit(theForm) {

  return validateTransactionModel(theForm);
}
</script>
<v:javascript formName="transactionModel" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>">
</script>
</body>
</html>
