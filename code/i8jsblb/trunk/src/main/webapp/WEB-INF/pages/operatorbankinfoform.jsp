<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Asad Hayat-->
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="decorator2">
<meta name="title" content="Operator Bank Info"/>

		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
		<link rel="stylesheet" type="text/css"
			href="styles/deliciouslyblue/calendar.css" />
</head>
<body>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
<spring:bind path="operatorBankInfoListViewModel.*">
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

<form id="operatorBankInfoForm" method="post" action="operatorbankinfoform.html" onsubmit="return onFormSubmit(this);">
<table width="100%" border="0" cellpadding="0" cellspacing="1">

 <c:if test="${not empty param.operatorBankInfoId}">
          <input type="hidden" name="isUpdate" id="isUpdate" value="true"/>
          <spring:bind path="operatorBankInfoListViewModel.operatorId">
               <input type="hidden" name="${status.expression}" value="${status.value}"/>
          </spring:bind>
          <spring:bind path="operatorBankInfoListViewModel.bankId">
           <input type="hidden" name="${status.expression}" value="${status.value}"/>
           </spring:bind>
           <spring:bind path="operatorBankInfoListViewModel.paymentModeId">
             <input type="hidden" name="${status.expression}" value="${status.value}"/>
           </spring:bind>
           <spring:bind path="operatorBankInfoListViewModel.name">
            <input type="hidden" name="${status.expression}" value="${status.value}"/>
        	</spring:bind>
        </c:if>
        
        <spring:bind path="operatorBankInfoListViewModel.operatorBankInfoId">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>
        
         <spring:bind path="operatorBankInfoListViewModel.createdBy">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>

        <spring:bind path="operatorBankInfoListViewModel.updatedBy">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
          </spring:bind>

           <spring:bind path="operatorBankInfoListViewModel.createdOn">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
          </spring:bind>

           <spring:bind path="operatorBankInfoListViewModel.updatedOn">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
          </spring:bind>
          
          
  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Operator Bank Name:&nbsp;</td>
    <td><spring:bind path="operatorBankInfoListViewModel.name">
    <c:if test="${not empty param.operatorBankInfoId}">
      <input type="text" name="${status.expression}" disabled="disabled" class="textBox" maxlength="50" value="${status.value}" onkeypress="return maskAlphaWithSp(this,event)"/>
      </c:if>
       <c:if test="${empty param.operatorBankInfoId}">
      <input type="text" name="${status.expression}" class="textBox" maxlength="50" value="${status.value}" onkeypress="return maskAlphaWithSp(this,event)"/>
      </c:if>
      </spring:bind>
    </td>
  </tr> 
        

 <tr >
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Operator:&nbsp;</td>
            <td width="50%" align="left" bgcolor="FBFBFB"><spring:bind path="operatorBankInfoListViewModel.operatorId">
                   <select name="${status.expression}" class="textBox" <c:if test="${not empty param.operatorBankInfoId}">disabled='disabled'</c:if>>
                     <c:forEach items="${operatorModelList}" var="operatorModelList">
                       <option value="${operatorModelList.operatorId}" <c:if test="${status.value == operatorModelList.operatorId}">selected="selected"</c:if>>
                       ${operatorModelList.name}
                       </option>
                     </c:forEach>
                   </select>
                 </spring:bind>
                </td>
          </tr>



<tr>
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Bank:&nbsp;</td>
	<td bgcolor="FBFBFB" >
		<spring:bind path="operatorBankInfoListViewModel.bankId">
		<select name="${status.expression}" class="textBox" <c:if test="${not empty param.operatorBankInfoId}">disabled='disabled'</c:if>>
                     <c:forEach items="${bankModelList}" var="bankModelList">
                       <option value="${bankModelList.bankId}" <c:if test="${status.value == bankModelList.bankId}">selected="selected"</c:if>>
                       ${bankModelList.name}
                       </option>
                     </c:forEach>
                   </select>
		</spring:bind>
	</td>
  </tr>

<tr>
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Payment Mode:&nbsp;</td>
            <td width="50%" align="left" bgcolor="FBFBFB"><spring:bind path="operatorBankInfoListViewModel.paymentModeId">
             <select name="${status.expression}" class="textBox" <c:if test="${not empty param.operatorBankInfoId}">disabled='disabled'</c:if>>
                     <c:forEach items="${paymentModeModelList}" var="paymentModeModelList">
                       <option value="${paymentModeModelList.paymentModeId}" <c:if test="${status.value == paymentModeModelList.paymentModeId}">selected="selected"</c:if>>
                       ${paymentModeModelList.name}
                       </option>
                     </c:forEach>
                   </select>
                   </spring:bind>
            </td>
          </tr>

 
  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Receiving Account No:&nbsp;</td>
    
    <td><spring:bind path="operatorBankInfoListViewModel.receivingAccountNo">
      <input type="text" name="${status.expression}" class="textBox" onkeypress="return maskAlphaNumeric(this,event)" maxlength="50" value="${status.value}"/>
      </spring:bind>
    </td>
  </tr>

<tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Paying Account No:&nbsp;</td>
    
    <td><spring:bind path="operatorBankInfoListViewModel.payingAccountNo">
      <input type="text" name="${status.expression}" class="textBox" onkeypress="return maskAlphaNumeric(this,event)" maxlength="50" value="${status.value}"/>
      </spring:bind>
    </td>
  </tr>

  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Merchant Catagory:&nbsp;</td>
    <td><spring:bind path="operatorBankInfoListViewModel.merchantCategory">
      <input type="text" name="${status.expression}" class="textBox"  onkeypress="return maskNumber(this,event)" maxlength="50" value="${status.value}"/>
      </spring:bind>
    </td>
  </tr>
  

  
  
  <tr bgcolor="FBFBFB">
    <td colspan="2" align="center"> <GenericToolbar:toolbar formName="operatorBankInfoForm" /></td>
  </tr>
</table>
</form>
<script type="text/javascript">
{


  function onFormSubmit(theForm) {
    return validateOperatorBankInfoModel(theForm) ;
  }
  
  
}
</script>

<v:javascript formName="operatorBankInfoModel" staticJavascript="false"/>
<script type="text/javascript" src="<c:url
value="/scripts/validator.jsp"/>"></script>

</body>
</html>
