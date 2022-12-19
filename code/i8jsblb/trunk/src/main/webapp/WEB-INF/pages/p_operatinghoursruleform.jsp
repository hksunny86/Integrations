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
	<meta name="title" content="Operating Hours Rules/Agent Location Verification" />
	 <script language="javascript" type="text/javascript">

      function error(request)
      {
      	alert("An unknown error has occured. Please contact with the administrator for more details");
      }
     
    </script>
	</head>
	<body>
	<div id="rsp" class="ajaxMsg"></div>
	<spring:bind path="operatingHoursRuleModel.*">
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
	
	<html:form  name = "operatingHoursRuleModelForm" id = "operatingHoursRuleModelForm" commandName="operatingHoursRuleModel" action="p_operatinghoursruleform.html" method="post" onsubmit="return validateForm(this)">
       <table width="100%" border="0" cellpadding="0" cellspacing="1">
       		
	        <html:hidden path="operatingHoursRuleId"/>
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
		   </tr>
		  <%-- <tr>

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
               </tr>--%>
                <tr>
					<td align="right" bgcolor="F3F3F3" class="formText" width="50%" align="right">Product Allowed Time in 24H Format&nbsp;&nbsp; </td>
				</tr>
		   <tr>
			   <td align="right" bgcolor="F3F3F3" class="formText">Allowed Time From:</td>
			   <td align="left" bgcolor="FBFBFB">

				   <html:select path="allowedStartingHours" cssClass="textBox"  id="allowedStartingHours" tabindex="2">
					   <html:option value="">---SELECT---</html:option>
					   <html:option value="00">00:00</html:option>
					   <html:option value="01">01:00</html:option>
					   <html:option value="02">02:00</html:option>
					   <html:option value="03">03:00</html:option>
					   <html:option value="04">04:00</html:option>
					   <html:option value="05">05:00</html:option>
					   <html:option value="06">06:00</html:option>
					   <html:option value="07">07:00</html:option>
					   <html:option value="08">08:00</html:option>
					   <html:option value="09">09:00</html:option>
					   <html:option value="10">10:00</html:option>
					   <html:option value="11">11:00</html:option>
					   <html:option value="12">12:00</html:option>
					   <html:option value="13">13:00</html:option>
					   <html:option value="14">14:00</html:option>
					   <html:option value="15">15:00</html:option>
					   <html:option value="16">16:00</html:option>
					   <html:option value="17">17:00</html:option>
					   <html:option value="18">18:00</html:option>
					   <html:option value="19">19:00</html:option>
					   <html:option value="20">20:00</html:option>
					   <html:option value="21">21:00</html:option>
					   <html:option value="22">22:00</html:option>
					   <html:option value="23">23:00</html:option>
					   <%--<c:forEach begin="1" end="24" varStatus="loop">
						   <html:option value="${loop.count}">${loop.count}</html:option>
					   </c:forEach>--%>
				   </html:select>
			   </td>
			   <td align="right"  bgcolor="FBFBFB" width="20%"></td>
		   </tr>
		   <tr>
			   <td align="right" bgcolor="F3F3F3" class="formText">Allowed Time To :</td>
			   <td align="left" bgcolor="FBFBFB">

				   <html:select path="allowedEndingHours" cssClass="textBox"  id="allowedEndingHours" tabindex="2">
					   <html:option value="">---SELECT---</html:option>
					   <html:option value="01">01:00</html:option>
					   <html:option value="02">02:00</html:option>
					   <html:option value="03">03:00</html:option>
					   <html:option value="04">04:00</html:option>
					   <html:option value="05">05:00</html:option>
					   <html:option value="06">06:00</html:option>
					   <html:option value="07">07:00</html:option>
					   <html:option value="08">08:00</html:option>
					   <html:option value="09">09:00</html:option>
					   <html:option value="10">10:00</html:option>
					   <html:option value="11">11:00</html:option>
					   <html:option value="12">12:00</html:option>
					   <html:option value="13">13:00</html:option>
					   <html:option value="14">14:00</html:option>
					   <html:option value="15">15:00</html:option>
					   <html:option value="16">16:00</html:option>
					   <html:option value="17">17:00</html:option>
					   <html:option value="18">18:00</html:option>
					   <html:option value="19">19:00</html:option>
					   <html:option value="20">20:00</html:option>
					   <html:option value="21">21:00</html:option>
					   <html:option value="22">22:00</html:option>
					   <html:option value="23">23:00</html:option>
					   <html:option value="24">24:00</html:option>
					   <%--<c:forEach begin="1" end="24" varStatus="loop">
						   <html:option value="${loop.count}">${loop.count}</html:option>
					   </c:forEach>--%>
				   </html:select>
			   </td>
			   <td align="right"  bgcolor="FBFBFB" width="20%"></td>
		   </tr>
		   		<%--<tr>
					<html:select path="allowedStartingHours" cssClass="textBox"  id="allowedStartingHours" tabindex="2">
						<html:option value="0">---SELECT---</html:option>
						<c:forEach begin="1" end="${23}" varStatus="loop">
							<html:option value="${00:00}">${00:00}</html:option>
							<html:option value="${01:00}">${01:00}</html:option>
							<html:option value="${02:00}">${02:00}</html:option>
							<html:option value="${03:00}">${03:00}</html:option>
					</c:forEach>
					</html:select>
				</tr>--%>
		   		<%--<tr>
                 <td align="right" bgcolor="F3F3F3" class="formText">Start Time:&nbsp;&nbsp; </td>
                 <td align="left" bgcolor="FBFBFB">
                    <html:input id="allowedStartingHours"  path="allowedStartingHours" tabindex="2" cssClass="" maxlength="2" onkeypress="return maskInteger(this,event)"/>
                 </td>
					&lt;%&ndash;<td align="right" bgcolor="F3F3F3" class="formText">:&nbsp;&nbsp; </td>&ndash;%&gt;
					<td align="left" bgcolor="FBFBFB">
						<html:input id="allowedStartingMins"  path="allowedStartingMins" tabindex="2" cssClass="textBox" maxlength="2" onkeypress="return maskInteger(this,event)"/>
					</td>
               </tr>--%>
		  <%-- <tr>
			   <td align="right" bgcolor="F3F3F3" class="formText">Ending Time:&nbsp;&nbsp; </td>
			   <td align="left" bgcolor="FBFBFB">
				   <html:input id="allowedEndingHours"  path="allowedEndingHours" tabindex="6" cssClass="textBox" maxlength="2" onkeypress="return maskInteger(this,event)"/>
			   </td>
			   <td align="right" bgcolor="F3F3F3" class="formText">:&nbsp;&nbsp; </td>
			   <td align="left" bgcolor="FBFBFB">
				   <html:input id="allowedEndingMins"  path="allowedEndingMins" tabindex="6" cssClass="textBox" maxlength="2" onkeypress="return maskInteger(this,event)"/>
			   </td>
		   </tr>--%>
			   <%--<tr>
                <td align="right" bgcolor="F3F3F3" class="formText">Max Amount of Transactions:&nbsp;&nbsp; </td>
                <td align="left" bgcolor="FBFBFB">
                   <html:input id="maxAmountOfTransaction"  path="maxAmountOfTransaction" tabindex="7" cssClass="textBox" maxlength="10" onkeypress="return maskInteger(this,event)"/>
                </td>
              </tr>--%>
            <tr>
             <td align="right" bgcolor="F3F3F3" class="formText"><span class="asterisk">*</span>Error Message:&nbsp;&nbsp; </td>
             <td align="left" bgcolor="FBFBFB">	
				<html:input id="errorMsg"  path="errorMsg" tabindex="8" cssClass="textBox" maxlength="50" onkeypress="return maskAlphaNumericWithSp(this,event)"/>										
             </td>
           </tr>
		   <tr>
			   <td align="right" bgcolor="F3F3F3" class="formText">Verify Agent Location On Transaction :</td>
			   <td align="left"  bgcolor="FBFBFB">
				   <html:checkbox  path="isLocVrfReq"  id ="isLocVrfReq"  value = "${isLocVrfReq}" tabindex="10"/>
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
	           		<input name="reset" type="reset" onclick="javascript: window.location='p_operatinghoursrulemanagement.html?actionId=2'"
							class="button" value="Cancel" tabindex="12" />
	           </td>
           </tr>
            <tr bgcolor="FBFBFB">
             <td colspan="2" align="center">&nbsp;</td>
           </tr>
    </table>
</html:form>

<%--<ajax:select source="distributorId" target="distributorLevelId"
		baseUrl="${contextPath}/p_getDistributorRefData.html"
		parameters="distributorId={distributorId},actionId=${retriveAction}" errorFunction="error"/>--%>
	
	<script language="javascript" type="text/javascript">
		function validateForm(form)
		{	        	
	     	var _operatingHoursRuleId = form.operatingHoursRuleId.value;
	     	var _productId = form.productId.value;
	     	/*var _deviceTypeId = form.deviceTypeId.value;
	     	var _segmentId = form.segmentId.value;
            var _distributorId = form.distributorId.value;
            var _customerAccountTypeId = form.customerAccountTypeId.value;*/
            var _allowedStrartingHours = document.getElementById("allowedStartingHours").value
            var _allowedStartingHours2 = parseInt(form.allowedStartingHours.value) || 0;
			var _allowedEndingHours = parseInt(form.allowedEndingHours.value) || 0;
			var _isLocVrfReq = form.isLocVrfReq.value;
	     	/*var _limitTypeId = form.limitTypeId.value;
	     	var _maxNoOfTransaction = form.maxNoOfTransaction.value;
	     	var _maxAmountOfTransaction = form.maxAmountOfTransaction.value;*/
	     	var _errorMsg = form.errorMsg.value;

			//if(_productId=="" && _deviceTypeId=="" && _segmentId=="" && _distributorId=="" && _customerAccountTypeId=="")
	     	if(_productId=="" )
	     	{
	     		alert("Product should be selected")
	     		return false;
	     	}
		/*	if(_allowedStrartingHours=="" && _isLocVrfReq==false )
			{
				alert("Operating Hours or Agent Location Verification should be set")
				return false;
			}
			if(_allowedEndingHours=="" )
			{
				alert("Allowed Time To should be selected")
				return false;
			}*/
			if(_allowedEndingHours < _allowedStrartingHours  )
			{
				alert("Allowed Time From should be smaller than Allowed Time To ")
				return false;
			}
	     	/*alert ("Strating hours time1  "+_allowedStrartingHours);
			alert ("Strating hours time2  "+_allowedStartingHours2);
			alert("Ending"+_allowedEndingHours);*/
	     	/*if(!doRequiredGroup( 'limitTypeClass', 'Limit Type' )){
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
	     	}*/
	     	
	     	if(_errorMsg==""){
	     		alert("Kindly provide Error Message ")
	     		return false;
	     	}
	     	
	        var message;
	        if(_operatingHoursRuleId!=""){
	        	message="System will reset the existing  rule on Edit. Are you sure you want to modify the existing rule?";
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
