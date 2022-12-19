<%@ page import="com.inov8.microbank.common.util.PortalConstants,com.inov8.microbank.common.util.*"%>
<%@ include file="/common/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	
	    <script type="text/javascript" 
	    	src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>		
		<script type="text/javascript" 
			src="${pageContext.request.contextPath}/scripts/prototype.js"></script>	
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
        
		<link rel="stylesheet" type="text/css"
			href="styles/deliciouslyblue/calendar.css" />

		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<meta name="decorator" 
			content="decorator">
		<meta name="title" content="Manual Correction/Adjustment" />
		<%@include file="/common/ajax.jsp"%>
		<script type="text/javascript">
		
	function fromACInitProgress(){	
		if(document.forms[0].fromACNo.value == ''){
    		alert('Account # is mandatory for Title Fetch');
    		return false;
    	}
    	if(document.forms[0].adjustmentType.value == ''){
    		alert('Please select Adjustment Type');
    		return false;
    	}
    	
    	$('errorMessages').innerHTML = "";
    	Element.hide('errorMessages');
    	
    	var myElem = document.getElementById('successMessages');
    	if (myElem != null) Element.hide('successMessages');
    	
    	return true;   
	}
	
	function fromACEndProgress(){
		if(document.forms[0].fromACNick.value == 'null' || document.forms[0].fromACNick.value == ' '){
		document.forms[0].fromACNick.value = '';
		
		Element.show('errorMessages');
					
		$('errorMessages').innerHTML = $F('errMsg');
		}
		
		if(document.forms[0].errMsg.value == 'null'){
		document.forms[0].errMsg.value = '';
		}
	}
	
	function toACInitProgress(){	
		if(document.forms[0].toACNo.value == ''){
	    	alert('Account # is mandatory for Title Fetch');
	    	return false;
    	}
    	if(document.forms[0].adjustmentType.value == ''){
    		alert('Please select Adjustment Type');
    		return false;
    	}
    	
    	$('errorMessages').innerHTML = "";
    	Element.hide('errorMessages');
    	
    	var myElem = document.getElementById('successMessages');
    	if (myElem != null) Element.hide('successMessages');
    	
    	return true;   
	}
	
	function toACEndProgress(){
		if(document.forms[0].toACNick.value == 'null' || document.forms[0].toACNick.value == ' '){
		document.forms[0].toACNick.value = '';
		
		Element.show('errorMessages');
					
		$('errorMessages').innerHTML = $F('errMsg');
		}
		
		if(document.forms[0].errMsg.value == 'null'){
		document.forms[0].errMsg.value = '';
		}
	}
            
</script>
		
	</head>
	<body>
		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages" >
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" />
					<br />
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>

        <div class="infoMsg" id="errorMessages" style="display:none;"></div>     
    <spring:bind path="manualAdjustmentModel.*">
	  <c:if test="${not empty status.errorMessages}">
	    <div class="errorMsg">
	      <c:forEach var="error" items="${status.errorMessages}">
	        <c:out value="${error}" escapeXml="false"/>
	        <br/>
	      </c:forEach>
	    </div>
	  </c:if>
	</spring:bind>    
        
		<table width="100%" border="0" cellpadding="0" cellspacing="1">

			<html:form id="manualAdjustmentForm" method="post"
				action="p_manualadjustment.html"
				commandName="manualAdjustmentModel" onsubmit="return onFormSubmit(this);">

	 			<input type="hidden" id="errMsg" value=""/>
                <input type="hidden" id="message" name="message" value=""/>
                <input type="hidden" id="errorMesg" name="errorMesg" value=""/>
                
                <input type="hidden" id="acType1" name="acType1" />
               	<input type="hidden" id="acType2" name="acType2" />
               	
               	<input type="hidden" name="actionAuthorizationId" value="${param.authId}" />
          		<input type="hidden" name="resubmitRequest" value="${param.isReSubmit}" />
          		<input type="hidden" name="usecaseId" value = "<%=PortalConstants.MANUAL_ADJUSTMENT_USECASE_ID %>"/>

				<tr bgcolor="FBFBFB">
					<td colspan="2" align="center">
						&nbsp;
					</td>
				</tr>
				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						Transaction ID:
					</td>
					<td align="left">
						<html:input tabindex="1" path="transactionCodeId" cssClass="textBox" onkeypress="return maskInteger(this,event)"/>
					</td>
				</tr>
				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						<span style="color:#FF0000">*</span>Adjustment Type:
					</td>
					<td align="left">						
						<html:select path="adjustmentType" id="adjustmentType" onchange="adjustmentTypeChange(this);" tabindex="2" cssClass="textBox">
							<html:option value="">---All---</html:option>
								<c:if test="${adjustmentTypeList != null}">
									<html:options items="${adjustmentTypeList}" itemValue="value" itemLabel="label" />
		 						</c:if>
		 				</html:select>
					</td>
				</tr>
				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						<span style="color:#FF0000">*</span>From Account #:
					</td>
					<td align="left">
						<html:input path="fromACNo" id="fromACNo" cssClass="textBox" tabindex="3" onkeypress="return maskInteger(this,event)" maxlength="25" disabled="false" />
						<input id="fetchFromACButton" name="fetchButton" type="button" value="Fetch Title" class="button"	 />
					</td>
				</tr>
				<tr>
					<td height="16" align="right" bgcolor="F3F3F3" class="formText">
						<span style="color:#FF0000">*</span>From Account Nick: 
					</td>
					<td width="58%" align="left" bgcolor="FBFBFB">
						<input name="fromACNick" id="fromACNick" type="text" class="textBox" maxlength="35" readonly="true" />
					</td>
				</tr>
				
				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						<span style="color:#FF0000">*</span>To Account #:
					</td>
					<td align="left">
						<html:input path="toACNo" id="toACNo" cssClass="textBox" tabindex="4" onkeypress="return maskInteger(this,event)" maxlength="25" disabled="false" />
						<input id="fetchToACButton" name="fetchButton" type="button" value="Fetch Title" class="button"	 />
					</td>
				</tr>
				<tr>
					<td height="16" align="right" bgcolor="F3F3F3" class="formText">
						<span style="color:#FF0000">*</span>To Account Nick: 
					</td>
					<td width="58%" align="left" bgcolor="FBFBFB">
						<input name="toACNick" id="toACNick" type="text" class="textBox" maxlength="35" readonly="true" />
					</td>
				</tr>
				
				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						<span style="color:#FF0000">*</span>Amount:
					</td>
					<td align="left">
						<html:input path="amount" tabindex="5" cssClass="textBox" onkeypress="return checkNumeric(this,event)" maxlength="15"/>
					</td>
				</tr>
				<tr>
					<td align="right" bgcolor="F3F3F3" class="formText">
						Comments:
					</td>
					<td height="16" colspan="2" align="left">
						<html:textarea tabindex="6" path="comments" rows="4" cols="21" onkeypress="return textAreaLengthCounter(this,100);"/>
					</td>
				</tr>
				<tr>
					<td class="formText" align="right"></td>
					<td align="left">
						<input name="_save" id="_save" tabindex="7" type="submit" class="button" value="Transfer Fund"/> 
						<input name="reset" tabindex="8" type="reset" class="button" value="Cancel" />
					</td>
				</tr>
			</html:form>
		</table>
        
	   <ajax:updateField parser="new ResponseXmlParser()" source="fromACNo" action="fetchFromACButton" target="fromACNick,errMsg" baseUrl="${contextPath}/fetchtitlemanualadjustment.html" parameters="accountNo={fromACNo},type={acType1}" preFunction="fromACInitProgress" postFunction="fromACEndProgress"></ajax:updateField>
	   <ajax:updateField parser="new ResponseXmlParser()" source="toACNo" action="fetchToACButton" target="toACNick,errMsg" baseUrl="${contextPath}/fetchtitlemanualadjustment.html" parameters="accountNo={toACNo},type={acType2}" preFunction="toACInitProgress" postFunction="toACEndProgress"></ajax:updateField>
     
     <script>
     	function onFormSubmit(theForm) {
     		
     		var transactionCode = theForm.transactionCodeId.value;
     		
     		if(transactionCode != "" && transactionCode.length != 12) {

				alert('Transaction Code must be 12 digit code.');
				return false;
     		}
     		
			if(doRequired( theForm.adjustmentType, 'Adjustment Type' )
				&& doRequired( theForm.fromACNo, 'From Account #' ) && doRequired( theForm.fromACNick, 'From Account Nick' )
				&& doRequired( theForm.toACNo, 'To Account #' ) && doRequired( theForm.toACNick, 'To Account Nick' )
				&& doRequired( theForm.amount, 'Amount' )
				&& validDecimalNumber( theForm.amount, 'Amount' )) {
					if(theForm.fromACNo.value == theForm.toACNo.value)
					{
						alert('From Account # and To Account # cannot be same.');
						return false;
					}
	 				document.getElementById("_save").disabled = true;
					 return true;
			}
		    return false;
		}
		
		function doRequired( field, label ) {
			if( field.value == '' || field.value.length == 0 ) {
		      	alert( label + ' is required field.' );
		      	return false;
		    }
		    return true;
		}
		
		function validDecimalNumber(field, label) {
			var number = field.value;
			
		   	if(number != "" && number.length > 3 
		   			&& number.indexOf(".") != "-1"
		   			&& (number.length - number.indexOf(".") > 3) ){
		   		alert( label + ' should be max 2 decimal places' );
		   		return false;
		   	}
		   	return true;
		}
		
		function adjustmentTypeChange(sel){
			if(sel.value == 1){
				document.getElementById('acType1').value = '1';
				document.getElementById('acType2').value = '1';
			}else if(sel.value == 2){
				document.getElementById('acType1').value = 	'1';
				document.getElementById('acType2').value =	'2';
			}else if(sel.value == 3){
				document.getElementById('acType1').value =  '2';
				document.getElementById('acType2').value =	'1';
			}else if(sel.value == 4){
				document.getElementById('acType1').value =  '3';
				document.getElementById('acType2').value =	'3';
			}
				  
		}

		window.onload = adjustmentTypeChange(document.forms.manualAdjustmentForm.adjustmentType);

    	document.getElementById('amount').value = '';
     </script>   
        
        
	</body>
</html>

