<!--Author: Asad hayat http://www.leviton.com/sections/prodinfo/newprod/npleadin.htm-->

<%@ include file="/common/taglibs.jsp"%>

       
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta name="decorator" content="decorator2">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>

<script type="text/javascript">

function disablecontrolls(param){

	if(param!="DISCRETE") {
		document.forms.productForm.costPrice.disabled=true;
		document.forms.productForm.unitPrice.disabled=false;
        document.forms.productForm.minimumStockLevel.disabled=true;

		document.forms.productForm._costPrice.value=document.productForm.costPrice.value;
		document.forms.productForm._unitPrice.value=document.productForm.unitPrice.value;

		document.forms.productForm.costPrice.value="";
		document.forms.productForm.unitPrice.value="";
        document.forms.productForm.minimumStockLevel.value="";
	}
	else
	{
		document.forms.productForm.unitPrice.disabled=false;
		document.forms.productForm.costPrice.disabled=false;
        document.forms.productForm.minimumStockLevel.disabled=false;
		document.forms.productForm.costPrice.value=document.productForm._costPrice.value;
		document.forms.productForm.unitPrice.value=document.productForm._unitPrice.value;
	}

}

function clearSuccessMessage(){
    document.forms.productForm.successMessageId.value = '';
    document.forms.productForm.successMessageIdName.value = '';
}
function clearFailureMessage(){
    document.forms.productForm.failureMessageId.value = '';
    document.forms.productForm.failureMessageIdName.value = '';
}

function clearHelpLineMessage(){
    document.forms.productForm.helpLine.value = '';
    document.forms.productForm.helpLineMessageIdName.value = '';
}

function clearInstruction(){
    document.forms.productForm.instructionId.value = '';
    document.forms.productForm.instructionIdName.value = '';
}

function popupCallback(src,popupName,columnHashMap)
{

  if(src=="instructions")
  {
    document.forms.productForm.instructionId.value = columnHashMap.get('PK');
    document.forms.productForm.instructionIdName.value = columnHashMap.get('smsMessageText');
  }
  if(src=="successMessage")
  {
    document.forms.productForm.successMessageId.value = columnHashMap.get('PK');
    document.forms.productForm.successMessageIdName.value = columnHashMap.get('smsMessageText');
  }
  if(src=="HelpLineMessage")
  {
    document.forms.productForm.helpLine.value = columnHashMap.get('PK');
    document.forms.productForm.helpLineMessageIdName.value = columnHashMap.get('smsMessageText');
  }
  if(src=="failureMessage")
  {
    document.forms.productForm.failureMessageId.value = columnHashMap.get('PK');
    document.forms.productForm.failureMessageIdName.value = columnHashMap.get('smsMessageText');
  }
  if(src=="serviceName")
  {
    document.forms.productForm.serviceId.value = columnHashMap.get('PK');
    document.forms.productForm.serviceName.value = columnHashMap.get('name');
  //  document.forms.productForm.serviceTypeName.value = columnHashMap.get('serviceTypeIdServiceTypeModel.name');
    //disablecontrolls(document.forms.productForm.serviceTypeName.value);
  }
  if(src=="supplierName")
  {
    document.forms.productForm.supplierId.value = columnHashMap.get('PK');
    document.forms.productForm.supplierName.value = columnHashMap.get('name');
  }
   if(src=="queueName")
  {
    document.forms.productForm.productIntgModuleInfoId.value = columnHashMap.get('PK');
    document.forms.productForm.queueName.value = columnHashMap.get('queueName');

  }
   if(src=="notificationMessage")
  {
    document.forms.productForm.instructionId.value = columnHashMap.get('PK');
    document.forms.productForm.smsMessageText.value = columnHashMap.get('smsMessageText');
   document.forms.productForm.successMessageId.value = columnHashMap.get('PK'); 
  }
  
}
</script>
<meta name="title" content="Product"/>
</head>
<body>

<spring:bind path="productModel.*">
  <c:if test="${not empty status.errorMessages}">
    <div class="errorMsg">
      <c:forEach var="error" items="${status.errorMessages}">
        <c:out value="${error}" escapeXml="false" />
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

<form id="productForm" method="post" action="productform.html" onsubmit="return onFormSubmit(this)">
<table width="100%"  border="0" cellpadding="0" cellspacing="1">

        <c:if test="${not empty param.productId}">
        
          <input type="hidden" name="isUpdate" id="isUpdate" value="true"/>
          
          <spring:bind path="productModel.name">
              <input type="hidden" name="${status.expression}" value="${status.value}"/>
          </spring:bind>
          
          <spring:bind path="productModel.supplierId">
              <input type="hidden" name="${status.expression}" value="${status.value}"/>
          </spring:bind>
          
          <spring:bind path="productModel.serviceId">
              <input type="hidden" name="${status.expression}" value="${status.value}"/>
          </spring:bind>
          
        </c:if>

	<input type="hidden" name="_costPrice"/>
	<input type="hidden" name="_unitPrice"/>

	<spring:bind path="productModel.productId">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>


		


         <spring:bind path="productModel.versionNo">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>

         <spring:bind path="productModel.createdBy">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
        </spring:bind>

        <spring:bind path="productModel.updatedBy">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
          </spring:bind>

           <spring:bind path="productModel.createdOn">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
          </spring:bind>

           <spring:bind path="productModel.updatedOn">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
          </spring:bind>
          
		
		<tr bgcolor="FBFBFB">
            <td colspan="2" align="center">&nbsp;</td>
          </tr>
		<tr>
            <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Product:&nbsp;</td>
            <td align="left" bgcolor="FBFBFB"><spring:bind path="productModel.name">
              <c:if test="${empty param.productId}">
                <input name="${status.expression}" type="text" size="40" class="textBox" maxlength="50"  value="${status.value}" />
              </c:if>
              <c:if test="${not empty param.productId}">
                <input name="${status.expression}" type="text" size="40" class="textBox" maxlength="50" value="${status.value}" disabled="disabled"/>
              </c:if>
            </spring:bind></td>
          </tr>

           
	  
          <tr >
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Supplier:&nbsp;</td>
            <td width="50%" align="left" bgcolor="FBFBFB">
                <spring:bind path="productModel.supplierId">
                    <select name="${status.expression}" class="textBox" <c:if test="${not empty param.productId}">disabled="disabled"</c:if> >
                     <c:forEach items="${SupplierModelList}" var="supplierModel">
                       <option value="${supplierModel.supplierId}" <c:if test="${status.value == supplierModel.supplierId}"> selected="selected"</c:if>>
                           ${supplierModel.name}
                       </option>
                     </c:forEach>
                   </select>
                </spring:bind>
              </td>
          </tr>


		  <tr >
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Service:&nbsp;</td>
            <td width="50%" align="left" bgcolor="FBFBFB">
                <spring:bind path="productModel.serviceId">
		            <select name="${status.expression}" class="textBox" <c:if test="${not empty param.productId}">disabled="disabled"</c:if>>
                     <c:forEach items="${ServiceModelList}" var="serviceModel">
                       <option value="${serviceModel.serviceId}" <c:if test="${status.value == serviceModel.serviceId}"> selected="selected"</c:if>>
                           ${serviceModel.name}
                       </option>
                     </c:forEach>
                   </select>
                </spring:bind>
            </td>
            </tr>

            <tr>
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Product Integration Module:&nbsp;</td>
            <td width="50%" align="left" bgcolor="FBFBFB">
              <spring:bind path="productModel.productIntgModuleInfoId">
                  <select name="${status.expression}" class="textBox">
                     <c:forEach items="${ProductIntgModuleInfoModelList}" var="productIntgModuleInfoModel">
                       <option value="${productIntgModuleInfoModel.productIntgModuleInfoId}" <c:if test="${status.value == productIntgModuleInfoModel.productIntgModuleInfoId}"> selected="selected"</c:if>>
                           ${productIntgModuleInfoModel.name}
                       </option>
                     </c:forEach>
                   </select>
              </spring:bind>
            </td>
          </tr>

          <tr >
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText">Product Integration VO:&nbsp;</td>
            <td width="50%" align="left" bgcolor="FBFBFB">
              <spring:bind path="productModel.productIntgVoId">
                  <select name="${status.expression}" class="textBox">
                     <option value=""></option>
                     <c:forEach items="${ProductIntgVoModelList}" var="productIntgVoModel">
                       <option value="${productIntgVoModel.productIntgVoId}" <c:if test="${status.value == productIntgVoModel.productIntgVoId}"> selected="selected"</c:if>>
                           ${productIntgVoModel.name}
                       </option>
                     </c:forEach>
              </spring:bind>
            </td>
            </tr>

			<tr >
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Instructions:&nbsp;</td>
            <td width="50%" align="left" bgcolor="FBFBFB">
            <%-- 
              <spring:bind path="productModel.instructionId">
                  
                  <select name="${status.expression}" class="textBox">
                     <div id="someId" style="overflow:auto;WIDTH: 804px;HEIGHT: 147px;">
                     <c:forEach items="${instructionNotificationMessageModelList}" var="notificationMessageModel">
                       <option value="${notificationMessageModel.notificationMessageId}" <c:if test="${status.value == notificationMessageModel.notificationMessageId}"> selected="selected"</c:if>>
                           ${notificationMessageModel.smsMessageText}
                       </option>
                     </c:forEach>
                     </div>
                  </select>
              </spring:bind>
              --%>
              <spring:bind path="productModel.instructionId">
                  <input type="hidden" id="instructionId" name="instructionId" value="${status.value}"/>
              </spring:bind>
              <c:set var="_instructionText" value="" scope="page"/>
              <c:forEach items="${instructionNotificationMessageModelList}" var="notificationMessageModel">
                  <c:if test="${notificationMessageModel.notificationMessageId == productModel.instructionId}">
                     <c:set var="_instructionText" value="${notificationMessageModel.smsMessageText}" scope="page"/>
                  </c:if>
              </c:forEach>
              <textarea class="textBox" readonly="readonly" name="instructionIdName" id="instructionIdName"><c:out value="${_instructionText}"/></textarea>
              
              <input name="instructionLookupButton" type="button" value="-o" class="button" onClick="javascript:callLookup('instructions','instructionsPopup',400,200,'instructionsPopup.messageTypeId=3')"/>
              <img title="Clear Instruction" onclick="clearInstruction()"  style="cursor:pointer" src="${pageContext.request.contextPath}/images/refresh.png" border="0" /> 
            </td>
            </tr>
            
            <tr >
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Success Message:&nbsp;</td>
            <td width="50%" align="left" bgcolor="FBFBFB">
              <%-- 
              <spring:bind path="productModel.successMessageId">
                  <select name="${status.expression}" class="textBox">
                     
                     <c:forEach items="${successNotificationMessageModelList}" var="notificationMessageModel">
                       <option value="${notificationMessageModel.notificationMessageId}" <c:if test="${status.value == notificationMessageModel.notificationMessageId}"> selected="selected"</c:if>>
                           ${notificationMessageModel.smsMessageText}
                       </option>
                     </c:forEach>
              </spring:bind>
              --%>
              <spring:bind path="productModel.successMessageId">
                  <input type="hidden" id="successMessageId" name="successMessageId" value="${status.value}"/>
              </spring:bind>
              <c:set var="_successMessageText" value="" scope="page"/>
              <c:forEach items="${successNotificationMessageModelList}" var="successMessageModel">
                  <c:if test="${successMessageModel.notificationMessageId == productModel.successMessageId}">
                     <c:set var="_successMessageText" value="${successMessageModel.smsMessageText}" scope="page"/>
                  </c:if>
              </c:forEach>
              <textarea class="textBox" readonly="readonly" name="successMessageIdName" id="successMessageIdName"><c:out value="${_successMessageText}"/></textarea>
              
              <input name="successMessageLookupButton" type="button" value="-o" class="button" onClick="javascript:callLookup('successMessage','instructionsPopup',400,200,'instructionsPopup.messageTypeId=2')"/>
              <img title="Clear Success Message" onclick="clearSuccessMessage()"  style="cursor:pointer" src="${pageContext.request.contextPath}/images/refresh.png" border="0" /> 
            </td>
            </tr>
            
            <tr >
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText">Failure Message:&nbsp;</td>
            <td width="50%" align="left" bgcolor="FBFBFB">
              <%-- 
              <spring:bind path="productModel.failureMessageId">
                  <select name="${status.expression}" class="textBox">
                     <option value=""></option>
                     <c:forEach items="${failureNotificationMessageModelList}" var="notificationMessageModel">
                       <option value="${notificationMessageModel.notificationMessageId}" <c:if test="${status.value == notificationMessageModel.notificationMessageId}"> selected="selected"</c:if>>
                           ${notificationMessageModel.smsMessageText}
                       </option>
                     </c:forEach>
              </spring:bind>
              --%>
              <spring:bind path="productModel.failureMessageId">
                  <input type="hidden" id="failureMessageId" name="failureMessageId" value="${status.value}"/>
              </spring:bind>
              <c:set var="_failureMessageText" value="" scope="page"/>
              <c:forEach items="${failureNotificationMessageModelList}" var="failureMessageModel">
                  <c:if test="${failureMessageModel.notificationMessageId == productModel.failureMessageId}">
                     <c:set var="_failureMessageText" value="${failureMessageModel.smsMessageText}" scope="page"/>
                  </c:if>
              </c:forEach>
              
              <textarea class="textBox" readonly="readonly" name="failureMessageIdName" id="failureMessageIdName"><c:out value="${_failureMessageText}"/></textarea>
              <input name="failureMessageLookupButton" type="button" value="-o" class="button" onClick="javascript:callLookup('failureMessage','instructionsPopup',400,200,'instructionsPopup.messageTypeId=1')"/>
              <img title="Clear Failure Message" onclick="clearFailureMessage()"  style="cursor:pointer" src="${pageContext.request.contextPath}/images/refresh.png" border="0" /> 
            </td>
            </tr>
            
            
            <tr >
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Helpline Message:&nbsp;</td>
            <td width="50%" align="left" bgcolor="FBFBFB">

              <spring:bind path="productModel.helpLine">
                  <input type="hidden" id="helpLine" name="helpLine" value="${status.value}"/>
              </spring:bind>
              <c:set var="_helpLineText" value="" scope="page"/>
              <c:forEach items="${helpLineNotificationMessageModelList}" var="helpLineMessageModel">
                  <c:if test="${helpLineMessageModel.notificationMessageId == productModel.helpLine}">
                     <c:set var="_helpLineText" value="${helpLineMessageModel.smsMessageText}" scope="page"/>
                  </c:if>
              </c:forEach>
              
              <textarea class="textBox" readonly="readonly" name="helpLineMessageIdName" id="helpLineMessageIdName"><c:out value="${_helpLineText}"/></textarea>
              <input name="helpLineMessageLookupButton" type="button" value="-o" class="button" onClick="javascript:callLookup('HelpLineMessage','instructionsPopup',400,200,'instructionsPopup.messageTypeId=4')"/>
              <img title="Clear Helpline Message" onclick="clearHelpLineMessage()"  style="cursor:pointer" src="${pageContext.request.contextPath}/images/refresh.png" border="0" /> 
            </td>
            </tr>
            
            
                    
          <tr >
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText">Cost Price:&nbsp;</td>
            <td width="50%" align="left" bgcolor="FBFBFB">
              <spring:bind path="productModel.costPrice">
					<c:if test="${not empty param.productId}">
                  <input type="text" name="${status.expression}" class="textBox" maxlength="14" value="<c:out value="${costPrice}"></c:out>" onkeypress="return maskNumber(this,event)"/>
                </c:if>
                <c:if test="${empty param.productId}">
                  <input type="text" name="${status.expression}" class="textBox" maxlength="14" value="${status.value}" onkeypress="return maskNumber(this,event)"/>
                </c:if>
              </spring:bind>
            </td>
          </tr>
          
          
          <tr>
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText">Unit Price:&nbsp;</td>
            <td width="50%" align="left" bgcolor="FBFBFB">

              <spring:bind path="productModel.unitPrice">
                <c:if test="${not empty param.productId}">
                  <input type="text" name="${status.expression}" id="${status.expression}" class="textBox" maxlength="14" value="<c:out value="${unitPrice}"></c:out>" onkeypress="return maskNumber(this,event)"/>
                </c:if>
                <c:if test="${empty param.productId}">
                  <input type="text" name="${status.expression}" id="${status.expression}" class="textBox" maxlength="14" value="${status.value}" onkeypress="return maskNumber(this,event)"/>
                </c:if>
              </spring:bind>
            </td>
          </tr>


          <tr>
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText">Discount: Rs.&nbsp;</td>
            <td width="50%" align="left" bgcolor="FBFBFB">
               <spring:bind path="productModel.fixedDiscount">
                 <c:if test="${not empty param.productId}">
	               <input type="text" id="${status.expression}" name="${status.expression}" class="textBox" style="width:75px" maxlength="14" value="<c:out value="${fixedDiscount}"></c:out>" onkeypress="return maskNumber(this,event)"/>
                 </c:if> 	           
                <c:if test="${empty param.productId}">
                    <input type="text" id="${status.expression}" name="${status.expression}" class="textBox" style="width:75px" maxlength="14" value="${status.value}" onkeypress="return maskNumber(this,event)"/>
                </c:if>                 
	           </spring:bind>    
               +
               <spring:bind path="productModel.percentDiscount">
                  <c:if test="${not empty param.productId}">
                     <input type="text" id="${status.expression}" name="${status.expression}" class="textBox" style="width:75px" maxlength="4" value="<c:out value="${percentDiscount}"></c:out>" onkeypress="return maskNumber(this,event)"/>
                  </c:if>  
                <c:if test="${empty param.productId}">
                    <input type="text" id="${status.expression}" name="${status.expression}" class="textBox"  style="width:75px" maxlength="4" value="${status.value}" onkeypress="return maskNumber(this,event)"/>
                </c:if>                 
               </spring:bind>    
               %                                                     
            </td>
          </tr>   
          
          <tr>
            <td width="50%" align="right" bgcolor="F3F3F3" class="formText">Stockout Quantity:&nbsp;</td>
            <td width="50%" align="left" bgcolor="FBFBFB">
            
            
              <spring:bind path="productModel.minimumStockLevel">
                <input type="text" name="${status.expression}" class="textBox" maxlength="10" value="${status.value}" onkeypress="return maskNumber(this,event)"/>
              </spring:bind>
            </td>
          </tr>

          <tr>
            <td align="right" bgcolor="F3F3F3" class="formText">Description:&nbsp;</td>
            <td align="left" bgcolor="FBFBFB"><spring:bind path="productModel.description"><textarea name="${status.expression}" cols="50" rows="5" onkeyup="textAreaLengthCounter(this,250);" onkeypress="return maskCommon(this,event)" class="textBox">${status.value}</textarea></spring:bind></td>
          </tr>
          
          <tr>
            <td align="right" bgcolor="F3F3F3" class="formText">Comments:&nbsp;</td>
            <td align="left" bgcolor="FBFBFB"><spring:bind path="productModel.comments"><textarea name="${status.expression}" cols="50" rows="5" onkeyup="textAreaLengthCounter(this,250);" onkeypress="return maskCommon(this,event)" class="textBox">${status.value}</textarea></spring:bind></td>
          </tr>
           
          <tr>
            <td align="right" bgcolor="F3F3F3" class="formText">Batch Mode:&nbsp;</td>
            <td align="left" bgcolor="FBFBFB">
                <spring:bind path="productModel.batchMode">
                    <input type="hidden" name="_<c:out value="${status.expression}"/>" value="visible" />
                    <input type="checkbox" name="<c:out value="${status.expression}"/>" value="true" <c:if test="${status.value}">checked</c:if> />
                </spring:bind>
            </td>
          </tr>
           
<tr>
    <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Active:&nbsp;</td>
    <td width="51%" align="left" bgcolor="FBFBFB" class="formText">
    <spring:bind path="productModel.active">
	    <input name="${status.expression}" type="checkbox" value="true" 	
	    <c:if test="${status.value==true}">checked="checked"</c:if>
	    <c:if test="${empty param.productId && empty param._save }">checked="checked"</c:if>
	    <c:if test="${status.value==false && not empty param._save }">unchecked="checked"</c:if>
	    />			    
     </spring:bind>
    </td>
  </tr>

          <tr bgcolor="FBFBFB">
    <td height="19" colspan="2" align="center"><GenericToolbar:toolbar formName="productForm"/></td>
          
          
 </table>
	</form>

  <script type="text/javascript">

highlightFormElements();
if( document.forms[0].isUpdate == null )
    document.forms[0].name.focus();
else
    document.forms[0].productIntgModuleInfoId.focus();

function onFormSubmit(theForm) {
    /*if(!validateFormChar(theForm)){
      	return false;
    }*/
    var instructionId = document.forms[0].instructionId.value;
    var successMessageId = document.forms[0].successMessageId.value;
    var failureMessageId = document.forms[0].failureMessageId.value;

    if(parseFloat(document.getElementById("percentDiscount").value)>100.0 || parseFloat(document.getElementById("percentDiscount").value) < 0.0){
 	     alert("Discount amount cannot be greater than price of the product");
         return false;
    }
   
        
    if(document.getElementById("unitPrice").value=='' && parseFloat(document.getElementById("percentDiscount").value)==100.0){
    	if(document.getElementById("fixedDiscount").value!='' && parseFloat(document.getElementById("fixedDiscount").value)>0.0){      	    
    	   alert("Discount amount cannot be greater than price of the product");
    	   return false;    		
    	}
    }
    else{
       unitPrice = parseFloat(document.getElementById("unitPrice").value);    
       percentDiscount = parseFloat(document.getElementById("percentDiscount").value);            
       fixedDiscount = parseFloat(document.getElementById("fixedDiscount").value);                      
              
       if((fixedDiscount+(unitPrice*(percentDiscount/100))) > unitPrice){
           alert("Discount amount cannot be greater than price of the product");
      	   return false;       
       }    
    }     
    return validateProductModel(theForm);
}
</script>

<v:javascript formName="productModel" staticJavascript="false"/>
<script type="text/javascript" src="<c:url
value="/scripts/validator.jsp"/>"></script>


</body>
</html>
