<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
<%@ page import='com.inov8.microbank.common.util.PortalDateUtils'%>

<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

	<head>
<meta name="decorator" content="decorator2">
		<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
	<script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/date-validation.js"></script>
    
	<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
	<link type="text/css" rel="stylesheet" href="styles/ajaxtags.css" />
	<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
	<%@include file="/common/ajax.jsp"%>
	<meta name="title" content="Velocity Rules" />
	 <script language="javascript" type="text/javascript">

      function error(request)
      {
      	alert("An unknown error has occured. Please contact with the administrator for more details");
      }
     
    </script>
	</head>
	<body>
	<div id="rsp" class="ajaxMsg"></div>
	<spring:bind path="velocityRuleModel.*">
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
	
	<html:form  name = "velocityRuleModelForm" id = "velocityRuleModelForm" commandName="velocityRuleModel" action="p_velocityruleform.html" method="post" onsubmit="return validateForm(this)">
       <table width="100%" border="0" cellpadding="0" cellspacing="1">
       		
	        <html:hidden path="velocityRuleId"/>
	        <html:hidden path="createdOn"/>
	        <html:hidden path="updatedOn"/>
	        <html:hidden path="updatedBy"/>
	        <html:hidden path="createdBy"/>
	        <html:hidden path="versionNo"/>
	        
	        <tr bgcolor="FBFBFB">
	             <td colspan="2" align="center">&nbsp;</td>
            </tr>            
           <tr>
             <td  bgcolor="F3F3F3" class="formText" width="50%" align="right">Product:&nbsp;&nbsp; </td>
             <td align="left" bgcolor="FBFBFB" width="50%">
				<html:select path="productId" id="productId" cssClass="textBox" tabindex="1">
					<html:option value="">---All---</html:option>
					<html:options items="${productList}" itemLabel="name" itemValue="productId"/>
				</html:select>
             </td>
		   <tr>
			   <td  bgcolor="F3F3F3" class="formText" width="50%" align="right">Account Type:&nbsp;&nbsp; </td>
			   <td align="left" bgcolor="FBFBFB" width="50%">
				   <html:select path="customerAccountTypeId" id="customerAccountTypeId" cssClass="textBox" tabindex="1">
					   <html:option value="">---All---</html:option>
					   <html:options items="${olaCustomerAccountTypeList}" itemLabel="name" itemValue="customerAccountTypeId"/>
				   </html:select>
			   </td>
           </tr>
           <tr>
             <td  bgcolor="F3F3F3" class="formText" width="50%" align="right">Channel:&nbsp;&nbsp; </td>
             <td align="left" bgcolor="FBFBFB" width="50%"> 	
				<html:select path="deviceTypeId" id="deviceTypeId" cssClass="textBox" tabindex="2">
					<html:option value="">---All---</html:option>
					<html:options items="${deviceTypeList}" itemLabel="name" itemValue="deviceTypeId"/>
				</html:select>										
             </td>
           </tr>
            <tr>
             <td align="right" bgcolor="F3F3F3" class="formText">Segment:&nbsp;&nbsp; </td>
             <td align="left" bgcolor="FBFBFB">	
				<html:select path="segmentId" id="segmentId" cssClass="textBox" tabindex="3">
					<html:option value="">---All---</html:option>
					<html:options items="${segmentList}" itemLabel="name" itemValue="segmentId" />									
				</html:select>										
             </td>
           </tr>
            <tr>
             <td align="right" bgcolor="F3F3F3" class="formText">Agent Network:&nbsp;&nbsp; </td>
             <td align="left" bgcolor="FBFBFB">	
				<html:select path="distributorId"  cssClass="textBox" tabindex="4">
					<html:option value="">---All---</html:option>
					<html:options items="${distributorList}" itemLabel="name" itemValue="distributorId"/>
				</html:select>										
             </td>
           </tr>  
           <tr>
             <td align="right" bgcolor="F3F3F3" class="formText">Agent Level:&nbsp;&nbsp; </td>
             <td align="left" bgcolor="FBFBFB">	
				<html:select path="distributorLevelId"  cssClass="textBox" tabindex="5">
					<html:option value="">---All---</html:option>
					<html:options items="${distributorLevelList}" itemLabel="name" itemValue="distributorLevelId"/>
				</html:select>										
             </td>
           </tr>  
           <tr>
             <td align="right" bgcolor="F3F3F3" class="formText" width="50%" align="right"><span class="asterisk">*</span>Limit type:&nbsp;&nbsp; </td>
             <td align="left" bgcolor="FBFBFB">	
					&nbsp;&nbsp;<html:radiobutton path="limitTypeId" value="1" class="limitTypeClass"/>&nbsp;&nbsp;Daily
				 &nbsp;&nbsp;<html:radiobutton path="limitTypeId" value="2" class="limitTypeClass"/>&nbsp;&nbsp;Monthly
				 &nbsp;&nbsp;<html:radiobutton path="limitTypeId" value="3" class="limitTypeClass"/>&nbsp;&nbsp;Yearly
			 </td>
           </tr>
            <tr>
             <td align="right" bgcolor="F3F3F3" class="formText">Max No. of Transactions:&nbsp;&nbsp; </td>
             <td align="left" bgcolor="FBFBFB">	
				<html:input id="maxNoOfTransaction"  path="maxNoOfTransaction" tabindex="6" cssClass="textBox" maxlength="10" onkeypress="return maskInteger(this,event)"/>															
             </td>
           </tr>
            <tr>
             <td align="right" bgcolor="F3F3F3" class="formText">Max Amount of Transactions:&nbsp;&nbsp; </td>
             <td align="left" bgcolor="FBFBFB">	
				<html:input id="maxAmountOfTransaction"  path="maxAmountOfTransaction" tabindex="7" cssClass="textBox" maxlength="10" onkeypress="return maskInteger(this,event)"/>										
             </td>
           </tr>
            <tr>
             <td align="right" bgcolor="F3F3F3" class="formText"><span class="asterisk">*</span>Error Message:&nbsp;&nbsp; </td>
             <td align="left" bgcolor="FBFBFB">	
				<html:input id="errorMsg"  path="errorMsg" tabindex="8" cssClass="textBox" maxlength="50" onkeypress="return maskAlphaNumericWithSp(this,event)"/>										
             </td>
           </tr>    
           <tr>
           	<td align="right" bgcolor="F3F3F3" class="formText">Active :</td>
            <td align="left"  bgcolor="FBFBFB">                
	             <html:checkbox  path="isActive" tabindex="10"/>	         
            </td>
           </tr>
           
           <tr bgcolor="FBFBFB">
             <td colspan="2" align="center">&nbsp;</td>
           </tr>
           <tr>    
	           <td align="center" colspan="2">	
	           		<input name="_save" id="_save" type="submit" class="button"  value=" Save " tabindex="11" /> &nbsp;
	           		<input name="reset" type="reset" onclick="javascript: window.location='p_velocityrulemanagement.html?actionId=2'"
							class="button" value="Cancel" tabindex="12" />
	           </td>
           </tr>
            <tr bgcolor="FBFBFB">
             <td colspan="2" align="center">&nbsp;</td>
           </tr>
    </table>
</html:form>

<ajax:select source="distributorId" target="distributorLevelId"
		baseUrl="${contextPath}/p_getDistributorRefData.html"
		parameters="distributorId={distributorId},actionId=${retriveAction}" errorFunction="error"/>		
	
	<script language="javascript" type="text/javascript">
		function validateForm(form)
		{	        	
	     	var _velocityRuleId = form.velocityRuleId.value;
	     	var _productId = form.productId.value;
	     	var _deviceTypeId = form.deviceTypeId.value;
	     	var _segmentId = form.segmentId.value;
            var _distributorId = form.distributorId.value;
            var _customerAccountTypeId = form.customerAccountTypeId.value;
	     	var _limitTypeId = form.limitTypeId.value;
	     	var _maxNoOfTransaction = form.maxNoOfTransaction.value;
	     	var _maxAmountOfTransaction = form.maxAmountOfTransaction.value;
	     	var _errorMsg = form.errorMsg.value;
	     	
	
	     	if(_productId=="" && _deviceTypeId=="" && _segmentId=="" && _distributorId=="" && _customerAccountTypeId=="")
	     	{
	     		alert("Atleast one field should be selected from Product, Account Type, Channel, Segment, Agent Network")
	     		return false;
	     	}
	     	
	     	if(!doRequiredGroup( 'limitTypeClass', 'Limit Type' )){
	     		return false;
	     	}
	     	
	     	if(_maxNoOfTransaction == "" && _maxAmountOfTransaction=="" ){
	     		alert("One should be selected from Max No. of Transactions and Max Amount of Transactions")
	     		return false;
	     	}
	     	else if(_maxNoOfTransaction != "" && _maxAmountOfTransaction!="" ){
	     		alert("One should be selected from Max No. of Transactions and Max Amount of Transactions")
	     		return false;
	     	}
	     	else if((_maxNoOfTransaction <= 0 && _maxAmountOfTransaction =="")){
	     		alert("Max No. of Transactions should be greater than 0")
	     		return false;
	     	}
	     	else if((_maxNoOfTransaction == "" && _maxAmountOfTransaction<=0)){
	     		alert("Max Amount. of Transaction should be greater than 0")
	     		return false;
	     	}
	     	else if(!isInteger(_maxNoOfTransaction) ){
	     		alert("Invalid value for Max No. of Transactions.")
	     		return false;
	     	}
	     	else if(!isInteger(_maxAmountOfTransaction) ){
	     		alert("Invalid value for Max Amount of Transactions.")
	     		return false;
	     	}
	     	
	     	if(_errorMsg==""){
	     		alert("Kindly provide Error Message ")
	     		return false;
	     	}
	     	
	        var message;
	        if(_velocityRuleId!=""){
	        	message="System will reset the existing velocity rule on Edit. Are you sure you want to modify the existing velocity rule?";
	        	 if (confirm(message)==true)
		          return true;
		        else
		          return false;
	        }

		}
		
      function doRequiredGroup( className, label ) {
      	selected = false;
      	var elements = document.getElementsByClassName(className);
		for(i=0; i<elements.length; i++) {
			if(elements[i].checked == false){
				selected = false; 
			}else{
				return true;
			}
		}
		if(selected == false){
			alert( label + ' is required field.' );
		}
      	return selected;
      }

	</script>
	
	</body>
</html>
