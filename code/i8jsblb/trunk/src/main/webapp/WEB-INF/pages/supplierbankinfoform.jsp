<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Asad Hayat-->
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="decorator2">
<meta name="title" content="Supplier Bank"/>

		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
		<link rel="stylesheet" type="text/css"
			href="styles/deliciouslyblue/calendar.css" />
			


<script type="text/javascript">


function popupCallback(src,popupName,columnHashMap)
{
   if(src=="supplierName")
  {
    document.forms.supplierBankInfoForm.supplierId.value = columnHashMap.get('PK');
    document.forms.supplierBankInfoForm.supplierName.value = columnHashMap.get('name');
  }
  
  if(src=='bankName')
  {
    document.forms.supplierBankInfoForm.bankId.value = columnHashMap.get('PK');
    document.forms.supplierBankInfoForm.bankName.value = columnHashMap.get('name');
  }
  
  if(src=='paymentTypeName')
  {

    document.forms.supplierBankInfoForm.paymentModeId.value = columnHashMap.get('PK');
    document.forms.supplierBankInfoForm.paymentModeName.value = columnHashMap.get('name');

  }
}
</script>
</head>
<body>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
<spring:bind path="supplierBankInfoListViewModel.*">
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

<form id="supplierBankInfoForm" method="post" action="supplierbankinfoform.html" onsubmit="return onFormSubmit(this);">
<table width="100%" border="0" cellpadding="0" cellspacing="1">

 <c:if test="${not empty param.supplierBankInfoId}">
          <input type="hidden" name="isUpdate" id="isUpdate" value="true"/>
          <spring:bind path="supplierBankInfoListViewModel.supplierId">
               <input type="hidden" name="${status.expression}" value="${status.value}"/>
          </spring:bind>
          <spring:bind path="supplierBankInfoListViewModel.bankId">
           <input type="hidden" name="${status.expression}" value="${status.value}"/>
           </spring:bind>
           <spring:bind path="supplierBankInfoListViewModel.paymentModeId">
             <input type="hidden" name="${status.expression}" value="${status.value}"/>
           </spring:bind>
           <spring:bind path="supplierBankInfoListViewModel.name">
            <input type="hidden" name="${status.expression}" value="${status.value}"/>
        	</spring:bind>
        </c:if>
        
        <spring:bind path="supplierBankInfoListViewModel.supplierBankInfoId">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>
        
         <spring:bind path="supplierBankInfoListViewModel.createdBy">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>

        <spring:bind path="supplierBankInfoListViewModel.updatedBy">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
          </spring:bind>

           <spring:bind path="supplierBankInfoListViewModel.createdOn">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
          </spring:bind>

           <spring:bind path="supplierBankInfoListViewModel.updatedOn">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
          </spring:bind>
          
          
  <tr bgcolor="FBFBFB">
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Supplier Bank Name:&nbsp;</td>
    <td><spring:bind path="supplierBankInfoListViewModel.name">
    <c:if test="${not empty param.supplierBankInfoId}">
      <input type="text" name="${status.expression}" disabled="disabled" class="textBox" maxlength="50" value="${status.value}" onkeypress="return maskAlphaWithSp(this,event)"/>
      </c:if>
       <c:if test="${empty param.supplierBankInfoId}">
      <input type="text" name="${status.expression}" class="textBox" maxlength="50" value="${status.value}" onkeypress="return maskAlphaWithSp(this,event)"/>
      </c:if>
      </spring:bind>
    </td>
  </tr> 
        

 <tr >
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Supplier:&nbsp;</td>
            <td width="50%" align="left" bgcolor="FBFBFB"><spring:bind path="supplierBankInfoListViewModel.supplierId">
                   <select name="${status.expression}" class="textBox" <c:if test="${not empty param.supplierBankInfoId}">disabled='disabled'</c:if>>
                     <c:forEach items="${supplierModelList}" var="supplierModelList">
                       <option value="${supplierModelList.supplierId}" <c:if test="${status.value == supplierModelList.supplierId}">selected="selected"</c:if>>
                       ${supplierModelList.name}
                       </option>
                     </c:forEach>
                   </select>
                 </spring:bind>
                </td>
          </tr>



<tr>
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Bank:&nbsp;</td>
	<td bgcolor="FBFBFB" >
		<spring:bind path="supplierBankInfoListViewModel.bankId">
		<select name="${status.expression}" class="textBox" <c:if test="${not empty param.supplierBankInfoId}">disabled='disabled'</c:if>>
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
            <td width="50%" align="left" bgcolor="FBFBFB"><spring:bind path="supplierBankInfoListViewModel.paymentModeId">
             <select name="${status.expression}" class="textBox" <c:if test="${not empty param.supplierBankInfoId}">disabled='disabled'</c:if>>
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
    <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Account No:&nbsp;</td>
    
    <td><spring:bind path="supplierBankInfoListViewModel.accountNo">
      <input type="text" name="${status.expression}" onkeypress="return maskAlphaNumeric(this,event)"  class="textBox" maxlength="50" value="${status.value}"/>
      </spring:bind>
    </td>
  </tr>

 
  
<tr>
    <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Active:&nbsp;</td>
    <td width="51%" align="left" bgcolor="FBFBFB" class="formText">
    <spring:bind path="supplierBankInfoListViewModel.active">
	    <input name="${status.expression}" type="checkbox" value="true"	
	    <c:if test="${status.value==true || empty param.supplierBankInfoId}">checked="checked"</c:if>
	    <c:if test="${status.value==false && not empty param._save}">unchecked="checked"</c:if>
	    />			    
     </spring:bind>
    </td>
  </tr>  
  
  
  <tr bgcolor="FBFBFB">
    <td colspan="2" align="center"> <GenericToolbar:toolbar formName="supplierBankInfoForm" /></td>
  </tr>
</table>
</form>
<script type="text/javascript">
{


  function onFormSubmit(theForm) {
    return validateSupplierBankInfoModel(theForm) ;
  }
  
  
}
</script>

<v:javascript formName="supplierBankInfoModel" staticJavascript="false"/>
<script type="text/javascript" src="<c:url
value="/scripts/validator.jsp"/>"></script>

</body>
</html>
