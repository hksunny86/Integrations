<!--Title: i8Microbank Demo-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Ahmad Iqbal-->
<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="decorator2">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
<meta name="title" content="Agent Credit Transfer"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
</head>
<body>
<%@include file="/common/ajax.jsp"%>

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


<form id="inov8distributorcreditform" method="post" action="inov8distributorcreditform.html" onsubmit="return onFormSubmit(this)">
<table width="100%"  border="0" cellpadding="0" cellspacing="1">

  <input type="hidden"  name="isUpdate" id="isUpdate" value="false"/>

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

        <spring:bind path="transactionModel.fromDistContactId">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>

        <spring:bind path="transactionModel.fromRetContactId">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>

        <spring:bind path="transactionModel.retailerId">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>

        <spring:bind path="transactionModel.transactionTypeId">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>

        <spring:bind path="transactionModel.transactionCodeId">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>

        <spring:bind path="transactionModel.customerId">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>

        <spring:bind path="transactionModel.smartMoneyAccountId">
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

        <spring:bind path="transactionModel.fromRetContactName">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>

        <spring:bind path="transactionModel.fromRetContactMobNo">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>

        <spring:bind path="transactionModel.fromDistContactMobNo">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>

        <spring:bind path="transactionModel.toRetContactId">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>

        <spring:bind path="transactionModel.toRetContactName">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>

        <spring:bind path="transactionModel.toRetContactMobNo">
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
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Receiving Agent :&nbsp;&nbsp; </td>
    <td align="left" bgcolor="FBFBFB">

    <spring:bind path="transactionModel.toDistContactId">
        <input type="hidden" name="${status.expression}" value="${status.value}"/>
    </spring:bind>
    <spring:bind path="transactionModel.toDistContactName">
        <input type="hidden" name="${status.expression}" value="${status.value}"/>
    </spring:bind>
        
    
    <spring:bind path="transactionModel.distributorId">
      
    
    <select name="${status.expression}" class="textBox" id="${status.expression}">
							        <c:forEach items="${distributorModelList}" var="distributorModel">
									<option value="${distributorModel.distributorId}"
										<c:if test="${status.value == Agent Model.distributorId}">selected="selected"</c:if>>
										${distributorModel.name}
									</option>
								</c:forEach>
						        </select>
    
    </spring:bind>
    </td>
  </tr>
  
  <tr>
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Credit Amount:&nbsp;&nbsp; </td>
    <td align="left" bgcolor="FBFBFB">
    <spring:bind path="transactionModel.totalAmount">
    <c:if test="${not empty transactionAmount}">
      <input type="text" name="${status.expression}" maxlength="14" class="textBox" value="${transactionAmount}" onkeypress="return maskNumber(this,event)"/>
    </c:if>
    <c:if test="${empty transactionAmount}">
      <input type="text" name="${status.expression}" maxlength="14" class="textBox" value="${status.value}" onkeypress="return maskNumber(this,event)"/>
    </c:if>
    </spring:bind>
    
    </td>
  </tr>
  

  <tr>
    <td colspan="2" align="center" bgcolor="FBFBFB">&nbsp;</td>
  </tr>
  <tr bgcolor="FBFBFB">
            <td colspan="2" align="center">
					<GenericToolbar:toolbar formName="inov8distributorcreditform"/>            
            </td>
          </tr>

</table>
</form>

<script type="text/javascript" language="javascript">

highlightFormElements();
function popupCallback(src,popupName,columnHashMap)
{

  if(src=='distributorName')
  {
  

    document.forms.inov8distributorcreditform.toDistContactId.value = columnHashMap.get('PK');
    document.forms.inov8distributorcreditform.toDistContactName.value = columnHashMap.get('firstName') + '' + columnHashMap.get('lastName');
    document.forms.inov8distributorcreditform.toDistContactMobNo.value = columnHashMap.get('mobileNo');
    document.forms.inov8distributorcreditform.distributorName.value = columnHashMap.get('distributorName');
    document.forms.inov8distributorcreditform.distributorId.value=columnHashMap.get('distributorId');
  }

  if(src=='paymentModeName')
  {
    document.forms.inov8distributorcreditform.paymentModeId.value = columnHashMap.get('PK');
    document.forms.inov8distributorcreditform.paymentModeName.value = columnHashMap.get('name');
  }
  if(src=='deviceTypeName')
  {
    document.forms.inov8distributorcreditform.deviceTypeId.value = columnHashMap.get('PK');
    document.forms.inov8distributorcreditform.deviceTypeName.value = columnHashMap.get('name');
  }
}

function onFormSubmit(theForm) {



  return validateInov8distributorcreditform(theForm);
}
</script>
<v:javascript formName="inov8distributorcreditform" staticJavascript="false"/>
<script type="text/javascript" 
                  src="<c:url value="/scripts/validator.jsp"/>"></script>


</body>
</html>
