
<jsp:directive.page import="com.inov8.microbank.common.util.PortalDateUtils"/><!--Author: Rizwan ur Rehman -->

<%@include file="/common/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta name="decorator" content="decorator2">

<script type="text/javascript">

function error(request)
{
     	alert("An unknown error has occured. Please contact with the administrator for more details");
}

function checkForInvalid(formObj)
{

  var commTypeSelectedIndex = formObj.commissionTypeId.selectedIndex;
  var commissionTypeValue = formObj.commissionTypeId[commTypeSelectedIndex].text;

  if( commissionTypeValue == 'PERCENTAGE' && formObj.rate.value > 100)
  {
    alert("Commission percentage cannot be greater than 100%");
    formObj.rate.value = "";
    formObj.rate.focus();
    return false;
  }
  return true;
}


function checkCommissionType(formObj)
{
  var commTypeSelectedIndex = formObj.commissionTypeId.selectedIndex;
  var commissionTypeValue = formObj.commissionTypeId[commTypeSelectedIndex].text;
  if(commissionTypeValue == 'PERCENTAGE')
  {
    if(formObj.rate.value != "" && formObj.rate.value > 100)
    {
      alert("Commission percentage cannot be greater than 100%");
      formObj.rate.value="";
      formObj.rate.focus();
      return false;
    }
  }
  return true;
}

</Script>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<meta name="title" content="Transaction Charges"/>

<input type="hidden" name="insertPageFlag" value="${insertFlag}"/>

</head>
<body>
<%@include file="/common/ajax.jsp"%>
<spring:bind path="commissionRateModel.*">
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
      <c:out value="${msg}" escapeXml="false"/><br/>
    </c:forEach>
  </div>
  <c:remove var="messages" scope="session"/>
</c:if>


<table width="100%"  border="0" cellpadding="0" cellspacing="1">

  <form method="post" action="<c:url value="/commissionrateform.html"/>" id="commissionRateForm"
  onsubmit="return onFormSubmit(this)">

    <c:if test="${not empty param.commissionRateId}">
      <input type="hidden"  name="isUpdate" id="isUpdate" value="true"/>
      <spring:bind path="commissionRateModel.productId">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
      </spring:bind>
      <spring:bind path="commissionRateModel.segmentId">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
      </spring:bind>
      <spring:bind path="commissionRateModel.commissionStakeholderId">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
      </spring:bind>
      <spring:bind path="commissionRateModel.paymentModeId">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
      </spring:bind>
      <spring:bind path="commissionRateModel.transactionTypeId">
          <input type="hidden" name="${status.expression}" value="${status.value}"/>
      </spring:bind>
    </c:if>
    
    <spring:bind path="commissionRateModel.commissionReasonId">
        <input type="hidden" name="commissionReason" id="commissionReason" value="${status.value}"/>
    </spring:bind>
    
    
    <spring:bind path="commissionRateModel.commissionRateId">
      <input type="hidden" name="${status.expression}" value="${status.value}"/>
    </spring:bind>

    <spring:bind path="commissionRateModel.createdBy">
      <input type="hidden" name="${status.expression}" value="${status.value}"/>
    </spring:bind>

    <spring:bind path="commissionRateModel.updatedBy">
      <input type="hidden" name="${status.expression}" value="${status.value}"/>
    </spring:bind>

    <spring:bind path="commissionRateModel.createdOn">
      <input type="hidden" name="${status.expression}" value="${status.value}"/>
    </spring:bind>

    <spring:bind path="commissionRateModel.updatedOn">
      <input type="hidden" name="${status.expression}" value="${status.value}"/>
    </spring:bind>

    <spring:bind path="commissionRateModel.versionNo">
      <input type="hidden" name="${status.expression}" value="${status.value}"/>
    </spring:bind>



    <tr bgcolor="FBFBFB">
      <td colspan="2" align="center">&nbsp;</td>
    </tr>

    <tr bgcolor="FBFBFB">
      <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Charges Type:&nbsp;</td>
      <td align="left">
        <spring:bind path="commissionRateModel.commissionTypeId">
            <select name="${status.expression}" id="${status.expression}" class="textBox">              
              <c:forEach items="${commissionTypeModelList}" var="commissionTypeModelList">
                <option value="${commissionTypeModelList.commissionTypeId}"
                  <c:if test="${status.value == commissionTypeModelList.commissionTypeId}">selected="selected"</c:if>/>
                  ${commissionTypeModelList.name}
                </option>
              </c:forEach>
            </select>
        </spring:bind>
      </td>
    </tr>
	<tr bgcolor="FBFBFB">
	      <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Segment:&nbsp;</td>
	      <td align="left">
	        <spring:bind path="commissionRateModel.segmentId">
	            <select name="${status.expression}" class="textBox" <c:if test="${not empty param.commissionRateId}">disabled='disabled'</c:if>>              
	              <c:forEach items="${segmentModelList}" var="segmentModelList">
	                <option value="${segmentModelList.segmentId}"
	                  <c:if test="${status.value == segmentModelList.segmentId}">selected="selected"</c:if>/>
	                  ${segmentModelList.name}
	                </option>
	              </c:forEach>
	            </select>
	        </spring:bind>
	      </td>
	    </tr>

    <tr bgcolor="FBFBFB">
      <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Product:&nbsp;</td>
      <td align="left">
        <spring:bind path="commissionRateModel.productId">
            <select name="${status.expression}" class="textBox" <c:if test="${not empty param.commissionRateId}">disabled='disabled'</c:if>>              
              <c:forEach items="${productModelList}" var="productModelList">
                <option value="${productModelList.productId}"
                  <c:if test="${status.value == productModelList.productId}">selected="selected"</c:if>/>
                  ${productModelList.name}
                </option>
              </c:forEach>
            </select>
        </spring:bind>
      </td>
    </tr>


  

            <tr bgcolor="FBFBFB">
      <td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Charges Method:&nbsp;</td>
      <td align="left">
        <spring:bind path="commissionRateModel.commissionReasonId">
            <select name="${status.expression}" id="${status.expression}" class="textBox" >              
              <c:forEach items="${commissionReasonModelList}" var="commissionReasonModelList">
                <option value="${commissionReasonModelList.commissionReasonId}"
                  <c:if test="${status.value == commissionReasonModelList.commissionReasonId}">selected="selected"</c:if>/>
                  ${commissionReasonModelList.name}
                </option>
              </c:forEach>
            </select>
        </spring:bind>
      </td>
    </tr>



    <tr bgcolor="FBFBFB">
      <td width="50%" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Slab Range Starts:&nbsp;</td>
      <td width="50%" align="left"><spring:bind path="commissionRateModel.rangeStarts">

	  <c:if test="${not empty param.commissionRateId}">
		<input type="text" name="${status.expression}" class="textBox" value="${status.value}"  onkeypress="return maskNumber(this,event)"  escapeAutoFormat="true" maxlength="14"/>  
	  </c:if>
	  
	  <c:if test="${empty param.commissionRateId}">
		<input type="text" name="${status.expression}" class="textBox" value="${status.value}"  onkeypress="return maskNumber(this,event)"  escapeAutoFormat="true" maxlength="14"/>	  
	  </c:if>
        

        </spring:bind>
      </td>
    </tr>
    <tr bgcolor="FBFBFB">
      <td width="50%" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Slab Range Ends:&nbsp;</td>
      <td width="50%" align="left">
        <spring:bind path="commissionRateModel.rangeEnds">
        
          
	  <c:if test="${not empty param.commissionRateId}">
		<input type="text" name="${status.expression}" class="textBox" value="${status.value}"  onkeypress="return maskNumber(this,event)" escapeAutoFormat="true"  maxlength="14"/>  
	  </c:if>
	  
	  <c:if test="${empty param.commissionRateId}">
		<input type="text" name="${status.expression}" class="textBox" value="${status.value}"  onkeypress="return maskNumber(this,event)" escapeAutoFormat="true"  maxlength="14"/>	  
	  </c:if>
        

        </spring:bind>
      </td>
    </tr>
    <tr bgcolor="FBFBFB">
      <td width="50%" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Rate:&nbsp;</td>
      <td width="50%" align="left">
        <spring:bind path="commissionRateModel.rate">
        
          
	  <c:if test="${not empty param.commissionRateId}">
		<input type="text" name="${status.expression}" class="textBox" value="${status.value}"  onkeypress="return maskNumber(this,event)" escapeAutoFormat="true"  maxlength="14"/>  
	  </c:if>
	  
	  <c:if test="${empty param.commissionRateId}">
		<input type="text" name="${status.expression}" class="textBox" value="${status.value}"  onkeypress="return maskNumber(this,event)" escapeAutoFormat="true" maxlength="14"/>	  
	  </c:if>
        

        </spring:bind>
      </td>
    </tr>

    <tr>
      <td align="right" bgcolor="#F3F3F3" class="formText">Description:&nbsp;</td>
      <td align="left" bgcolor="#FBFBFB">
        <spring:bind path="commissionRateModel.description">
          <textarea name="${status.expression}" cols="50" rows="5" onkeyup="textAreaLengthCounter(this,250);" onkeypress="return maskCommon(this,event)" class="textBox">${status.value}</textarea>
        </spring:bind>
      </td>
    </tr>

    <tr>
      <td align="right" bgcolor="#F3F3F3" class="formText">Comments:&nbsp;</td>
      <td align="left" bgcolor="#FBFBFB">
        <spring:bind path="commissionRateModel.comments">
          <textarea name="${status.expression}" cols="50" rows="5" onkeyup="textAreaLengthCounter(this,250);" onkeypress="return maskCommon(this,event)" class="textBox">${status.value}</textarea>
        </spring:bind>
      </td>
    </tr>

 <tr>
    <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Active:&nbsp;</td>
    <td width="51%" align="left" bgcolor="FBFBFB" class="formText">
    <spring:bind path="commissionRateModel.active">
	    <input name="${status.expression}" type="checkbox" value="true" 	
	    <c:if test="${status.value==true}">checked="checked"</c:if>
	    <c:if test="${empty param.commissionRateId}">checked="checked"</c:if>
	    />			    
     </spring:bind>
    </td>
  </tr>

      <tr>
      <td colspan="2" align="middle">
      
      <tr>
    <td colspan="2" align="center" bgcolor="FBFBFB">
    				<input type= "button" name = "_save" value="  Save  " onclick="javascript:onSave(document.forms.commissionRateForm,null);" class="button"/>		  
 <c:if test="${empty param.commissionRateId}">
 	<%--
 		<input type= "button" name = "_cancel" value=" Cancel " onclick="javascript:revertDropdowns(document.forms.commissionRateForm);" class="button"/>
 	--%>
 	<input type= "button" name = "_cancel" value=" Cancel " onclick="javascript:window.location='commissionratemanagement.html';" class="button"/>
</c:if>
 <c:if test="${not empty param.commissionRateId}">
	<%--
	<input type= "reset" name = "_cancel" value=" Cancel "  class="button" />
	--%>
	<input type= "button" name = "_cancel" value=" Cancel " onclick="javascript:window.location='commissionratemanagement.html';" class="button"/>
</c:if>
    
  </tr>
  
  </form>
</table>



	<ajax:select 
		source="commissionStakeholderId" target="commissionReasonId"
		baseUrl="${contextPath}/commissionRateFormRefDataController.html"
		parameters="commissionStakeholderId={commissionStakeholderId},actionType=2" errorFunction="error"/>
		

				
<script language="javascript" type="text/javascript">


function revertDropdowns(theForm)
{
	
	
	var obj = new AjaxJspTag.Select(
	"/i8Microbank/commissionRateFormRefDataController.html", {
	parameters: "commissionStakeholderId={commissionStakeholderId},actionType=1",
	target: "stakeholderBankInfoId",
	source: "commissionStakeholderId",
	errorFunction: error
	});
	theForm.reset();
	obj.execute();
	
	

}

   

   
   highlightFormElements();
   document.forms[0].commissionTypeId.focus();

function isDateGreaterOrEqualForCard(from, to) {

	var fromDate;
	var toDate;

	var result = false;

//  format dd/mm/yyyy
	fromDate = from.substring(6,10) + from.substring(3,5) + from.substring(0,2);
	toDate = to.substring(6,10)   + to.substring(3,5) + to.substring(0,2);
	if( fromDate != toDate && fromDate > toDate ) {
		result = true;
	}

	return result;
}

function dataTypecheck(theForm)
{

  var rate =theForm.rate;  
  var commTypeMenu =  document.getElementById("commissionTypeId"); 
  var commTypeValue = commTypeMenu.options[commTypeMenu.selectedIndex].text;
  var commissionReasonId = document.getElementById("commissionReason");  
  var commissionReasonMenu =  document.getElementById("commissionReasonId"); 
  var commissionReasonIdText = commissionReasonMenu.options[commissionReasonMenu.selectedIndex].text;  
  var commissionReasonIdValue = commissionReasonMenu.options[commissionReasonMenu.selectedIndex].value;
  commissionReasonId.value = commissionReasonIdValue;


  if( "FIXED"== commTypeValue.toUpperCase() && 
  ( "BANK COMMISSION SHARING" == commissionReasonIdText.toUpperCase() || "BANK SERVICE FEE SHARING" == commissionReasonIdText.toUpperCase() ) )
  {
  	alert("Commission Type must be Percentage");
    commTypeMenu.focus();
    return false;
  }


var rateValue=theForm.rate.value;
if(!isValidRate(rate,14,2))
{
alert("Rate is incorrect");

theForm.rate.value=rateValue;
return false;
}


return true;

}
   function onFormSubmit(theForm)
   { 
   	 var rangeStarts = parseInt(document.forms[0].rangeStarts.value) || 0;
   	 var rangeEnds = parseInt(document.forms[0].rangeEnds.value) || 0;
   	 var rate = parseInt(document.forms[0].rate.value) || 0;
   	 
   	 if(rangeStarts == '' && rangeEnds=='' && rate=='' ){
   	 	alert("Provide Slab Range Starts, Slab Range Ends and Rate");
   	 	return false;
   	 }
   	 else if(rangeStarts == '' && rangeEnds==''){
   	 	alert("Provide Slab Range Starts, Slab Range Ends and Rate");
   	 	return false;
   	 }
   	 else if(rangeStarts == ''){
   	 	alert("Provide Slab Range Starts");
   	 	return false;
   	 }
   	  else if(rangeEnds == ''){
   	 	alert("Provide Slab Range Ends");
   	 	return false;
   	 }
   	  else if(rate == ''){
   	 	alert("Provide Rate");
   	 	return false;
   	 }
   	 else if(parseFloat(rangeEnds) <= parseFloat(rangeStarts)){
   	 		alert("Slab Range Starts should be less than Slab Range Ends");
   	 		return false;
   	 	}
   	 
   	 		 
   	  
   	    	 
   	 
   	 
	 var currServerDate = '<%=PortalDateUtils.currentFormattedDate("dd/MM/yyyy")%>';
     var comissionTypeId = document.forms[0].commissionTypeId.value;
    
    
         if(comissionTypeId=='2' && rate!='')
         {
             rate = parseFloat(rate);
             if(rate>100){
                 alert("Rate cannot be greater than 100%");
                 return false;
             }
         }else if(comissionTypeId == '1' && rate != ''){
         	rate = parseFloat(rate);
         	if(rate >= parseFloat(rangeEnds)){
         		alert("Rate cannot be greater or equal to Slab Range Ends");
         		return false;
         	}
         }
         
        
         return true;
     }// end else
   


			



</script>


</body>
</html>

