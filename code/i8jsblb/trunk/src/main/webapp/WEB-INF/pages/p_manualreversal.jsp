<%@ page import="com.inov8.microbank.common.util.PortalConstants,com.inov8.microbank.common.util.*"%>
<%@ include file="/common/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	
	    <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>		
		<script type="text/javascript" src="${contextPath}/scripts/prototype.js"></script>	
      	<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css"/>
		<link rel="stylesheet" href="${contextPath}/styles/jquery-ui-custom.min.css" type="text/css">
		<style type="text/css">
			.eXtremeTable .textBox{
				width: 110px;
				height: 16px;
				margin-bottom: 0px;
			}
			.eXtremeTable .miniSelect{
				width: 90px !important;
				height: 17px;
				margin-bottom: 5px;
			}
			.eXtremeTable .textBoxNick {
				width: 280px;
			}			
			.eXtremeTable .textBoxNick {
				width: 280px;
			}
			.eXtremeTable input.button {
    			height: 25px !important;
			    display: inline !important;
			    padding: 1px 5px !important;
			    background: #f0f0f0 !important;
			    color: #676767 !important;
			    border: 1px solid #c4c4c4 !important;
			    cursor: pointer !important;
			    font-size: 12px !important;
			}
		</style>
	
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<meta name="decorator" content="decorator">
		<meta name="title" content="Transaction Reversal/Adjustment" />
		<%@include file="/common/ajax.jsp"%>
	   	<script type="text/javascript" src="${contextPath}/scripts/jquery-1.7.2.min.js"></script>
	   	<script type="text/javascript" src="${contextPath}/scripts/jquery-ui-custom.min.js"></script>
		<script type="text/javascript">
		var jq = $.noConflict();
		jq(document).ready(
	    	function($) {
	    	    
	    		jq(document).on('click', '#entryTable .addRow', function () {
	    	        var row = jq(this).closest('tr');
	    	        var clone = row.clone();
	    	        // clear the values
	    	        var tr = clone.closest('tr');
	    	        tr.find('td:lt(4)').html("");

	    	        tr.find('input[type=text], select').val('');
	    	        tr.find('input[type=text], select').each(function() {
	    	        	this.style.borderColor="#e6e6e6";
	    	        });
					
	    	        jq(this).closest('tr').after(clone);
					resetIdAndNameAttributes();

					var name = jq(this).closest('tr').next('tr').find('input[type=select], select').attr('name');
					var indDiff = name.indexOf("]") - name.indexOf("[") - 1;
	    	        var newRowIndex = parseInt(name.substr(name.indexOf("[") + 1 , indDiff));
	    	        
	    	        var accountNickFieldHTML = "<input id='fundTransferEntryList"+newRowIndex+".accountTittle' name='fundTransferEntryList["+newRowIndex+"].accountTittle' class='textBoxNick' type='text' readonly='true'/>";
	    	        var accountNoFieldHTML = "<input id='fundTransferEntryList"+newRowIndex+".accountNumber' name='fundTransferEntryList["+newRowIndex+"].accountNumber' class='textBox' onkeypress='return maskInteger(this,event)' onchange='clearNick("+newRowIndex+")' type='text' maxlength='25'/>&nbsp;";
	    	        var fetchBtnHTML = "<input id='fetchTitleBtn"+newRowIndex+"' name='fetchTitleBtn"+newRowIndex+"' type='button' value='Fetch Title' class='button' />";
                    
	    	        tr.find('td:eq(0)').append(accountNickFieldHTML);
	    	        tr.find('td:eq(1)').html(accountNoFieldHTML + fetchBtnHTML);
	    	        
                    var scriptNEW = document.createElement('script');
                    scriptNEW.type = 'text/javascript';
                    
                    scriptNEW.text = "new AjaxJspTag.UpdateField(\"/i8Microbank/fetchtitlemanualadjustment.html\", { "+
                    "source: \"fetchTitleBtn"+newRowIndex+"\", "+
                    "preFunction: fromACInitProgress, "+
                    "action: \"fetchTitleBtn"+newRowIndex+"\", "+
                    "target: \"fundTransferEntryList"+newRowIndex+".accountTittle,errMsg\", "+
                    "parameters: \"accountNo={fundTransferEntryList"+newRowIndex+".accountNumber},type=1\", "+
                    "parser: new ResponseXmlParser(), "+
                    "postFunction: fromACEndProgress "+
                    "});";
                    tr.find('td:eq(2)').append(scriptNEW);
	    	        
	    	    });
	    	    
	    	    jq(document).on('click', '#entryTable .removeRow', function () {
	    	        if (jq('#entryTable .addRow').length > 1) {
	    	            jq(this).closest('tr').remove();
	    	            resetIdAndNameAttributes();
	    	        }
	    	        if (jq('#entryTable .addRow').length == 1) {
		    	        var tr = jq(this).closest('tr');
		    	        tr.find('input[type=text], select').val('');
		    	        tr.find('input[type=text], select').each(function() {
		    	        	this.style.borderColor="#e6e6e6";
		    	        });
	    	        }
	    	    });
	    		
				$( "#_submit" ).click(
						function()
						{
							var isValid = true;
							resetForm();
							var totalDebits = 0.0;
							var totalCredits = 0.0;
							var rowAmountValue = 0.0;
							var trxId = document.getElementById('transactionCode').value;
							var adjType = document.getElementById('adjustmentType').value;
							var trxIdHidden = document.getElementById('trxIdLoaded').value;
							
							if(adjType == ''){
								alert("Please select Adjustment Type");
								return false;
							}
					    	if(adjType == '1' && trxId == ''){
					    		alert("Please Enter Transaction ID");
								return false;
					    	}
				     		if(trxId != "" && trxId.length != 12) {
								alert("Transaction ID must be 12 digits.");
								return false;
				     		}

							if(trxId != '' && trxIdHidden == ''){
								alert("Please load transaction details before submitting the request.");
								return false;
							}
							if(trxId != '' && trxIdHidden != '' && trxId != trxIdHidden){
								alert("Please load transaction details for transaction ID provided.");
								return false;
							}
							
							$('#entryTable tbody tr').each( function(outerIndex) {
								
								if(isValid){
									$(this).find('input[id$="accountNumber"]').each(function(){
										if(this.value.length == 0 || this.value == '' || this.value == ' '){
											isValid=false;
											this.style.borderColor="red";
											alert("Please enter Account Number.");
										}
									});
								}
								
								if(isValid){
									$(this).find('input[id$="accountTittle"]').each(function(){
										if(this.value.length == 0 || this.value == '' || this.value == ' '){
											isValid=false;
											this.style.borderColor="red";
											alert("Please fetch title before proceed.");
										}
									});
								}

								
								var selectArray = $(this).find('select');
									
								if(isValid){
									$(this).find('input[id$="amountStr"]').each(function(){
										rowAmountValue = this.value;
										if(this.value.length == 0){
											isValid=false;
											this.style.borderColor="red";
											alert("Amount is required.");
										}else if(rowAmountValue != undefined && rowAmountValue != '' && isNaN(rowAmountValue)){
					        				isValid = false;
					        				this.style.borderColor="red";
											alert("Invalid Amount value.");
										}else if(this.value == 0){
					        				isValid = false;
					        				this.style.borderColor="red";
											alert("Amount should be greater than zero.");
										}else if(!isValidDecimalNumber(this)){
											isValid=false;
											this.style.borderColor="red";
											alert("Amount should be max 2 decimal places.");
										}else if(!verifyLowerValue(this.value)){
											isValid=false;
											this.style.borderColor="red";
											alert("Amount cannot be less than 0.01");
										}else if(!verifyUpperValue(this.value)){
											isValid=false;
											this.style.borderColor="red";
											alert("Amount cannot be greater than 999999999999.99");
										}
									});
								}
								
								if(isValid && selectArray[0].value.length == 0){
									isValid=false;
									selectArray[0].style.borderColor="red";
									alert("Please select Action");
								}else{
									if(selectArray[0].value == 1){
										totalDebits += parseFloat(rowAmountValue);
									}else if(selectArray[0].value == 2){
										totalCredits += parseFloat(rowAmountValue);
									}
								}
							
							});
							
							
							if(isValid){ // total debit/Total credit must be equal
								if(totalDebits == 0){
									isValid=false;
									alert("No credit entry found. At least one debit entry must be provided.");
								}else if(totalCredits == 0){
									isValid=false;
									alert("No credit entry found. At least one credit entry must be provided");
								}else if(totalDebits.toFixed(2) != totalCredits.toFixed(2)){
									isValid=false;
									alert("Total debit and total credit amount is not equal.\nTotal Debit:"+totalDebits.toFixed(2) +"\nTotal Credit:" +totalCredits.toFixed(2));
								}
							
								if(!isValid){
									highlightEntries();
								}
							}
							
							if(isValid){ // comments length
								var commentsField = document.getElementById("comments");
								if(commentsField.value != '' && commentsField.value.length > 250){
									isValid=false;
									alert('You cannot enter more than 250 characters in Comments');	
									commentsField.value = commentsField.value.substring(0, 250);
									commentsField.style.borderColor="red";
								}
							}
							
							if(isValid && confirm('Do you want to proceed')){
                            	return true;
							}
							return false;
							
						}
					);//end of btnSave click event
	    	    	    	    
	    	    function resetForm(){
	    	    	$('#entryTable tbody tr').find(':input:not(:button)').each( function() {
	    	    		this.style.borderColor="#e6e6e6";
	    	    	});
	    	    }
	    	    function highlightEntries(){
	    	    	$('#entryTable tbody tr').find('input[id$="amountStr"], select').each( function() {
	    	    		this.style.borderColor="red";
	    	    	});
	    	    }
	    	    
	    	    function resetIdAndNameAttributes(){
	    	    	jq('#entryTable tbody tr').each(
	    	    		function(index){
	    	    			$(this).find(':input:not(:button)').each( function() {
	    	    					var name = $(this).attr('name');
	    	    					var listName = name.substr(0,name.indexOf("["));
	    	    					var propertyName = name.substr(name.indexOf("]")+2);
	    	    					name = listName + "[" + index + "]." + propertyName;
	    	    					var id = listName + index + "." + propertyName
	    	    					$(this).attr('id', id);
	    	    					$(this).attr('name', name);
    	    					}
	    	    			);
	    	    			
	    	    			$(this).find('td:eq(1)').find(':button').each( function() {
	    	    				$(this).attr('id', "fetchTitleBtn"+index);
    	    					$(this).attr('name', "fetchTitleBtn"+index);
	    	    				
    	    					var currentTR = jq(this).closest('tr');
    	    					var scriptTD = currentTR.find('td:eq(2)');
    	    					scriptTD.html("");
    	    					var scriptNEW = document.createElement('script');
    	                        scriptNEW.type = 'text/javascript';
    	                        scriptNEW.text = "new AjaxJspTag.UpdateField(\"/i8Microbank/fetchtitlemanualadjustment.html\", { "+
    	                        "source: \"fetchTitleBtn"+index+"\", "+
    	                        "preFunction: fromACInitProgress, "+
    	                        "action: \"fetchTitleBtn"+index+"\", "+
    	                        "target: \"fundTransferEntryList"+index+".accountTittle,errMsg\", "+
    	                        "parameters: \"accountNo={fundTransferEntryList"+index+".accountNumber},type=1\", "+
    	                        "parser: new ResponseXmlParser(), "+
    	                        "postFunction: fromACEndProgress "+
    	                        "});";
    	                        scriptTD.append(scriptNEW);
    	                        
	    	    			});
	    	    		}
	    	    	);
	    	    } // end of resetIdAndNameAttributes
	    	    
	    	    
	    	    

	    	}
		);
		
	function fromACInitProgress(){	
		document.getElementById('errMsg').value = "";
    	
    	return true;   
	}
	
	function fromACEndProgress(){
		var errorText = $F('errMsg');
		if(errorText != null && errorText != '' && errorText != 'null' && errorText.length > 0){
			alert(errorText);
		}
	}
	
    function loadTransactionDetails(){
    	var trxCode = document.forms[0].transactionCode.value;
    	var adjType = document.forms[0].adjustmentType.value;
    	
    	if(adjType == ''){
    		alert('Please select Adjustment Type');
    		return false;
    	}
    	
    	if(adjType == '1' && trxCode == ''){
    		alert('Please Enter Transaction ID');
    		return false;
    	}
		
    	document.location="p_manualreversal.html?trxid="+trxCode+"&adjtype="+adjType;
    }

    function isValidDecimalNumber(field) {
		var number = field.value;
	   	if(number != "" && number.length > 3 
	   			&& number.indexOf(".") != "-1"
	   			&& (number.length - number.indexOf(".") > 3) ){
	   		return false;
	   	}
	   	return true;
	}
    
    function verifyLowerValue(number) {
		if(number != "" && number < 0.01){
	   		return false;
	   	}
	   	return true;
	}
    function verifyUpperValue(number) {
		if(number != "" && number > 999999999999.99){
	   		return false;
	   	}
	   	return true;
	}
    
    function clearNick(index){
    	document.getElementById("fundTransferEntryList"+index+".accountTittle").value = "";
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
    <spring:bind path="manualReversalVO.*">
	  <c:if test="${not empty status.errorMessages}">
	    <div class="errorMsg">
	      <c:forEach var="error" items="${status.errorMessages}">
	        <c:out value="${error}" escapeXml="false"/>
	        <br/>
	      </c:forEach>
	    </div>
	  </c:if>
	</spring:bind>    
        
	<html:form id="manualReversalForm" method="post" action="p_manualreversal.html" commandName="manualReversalVO" >
		<table width="100%" border="0" cellpadding="5" cellspacing="0">

				<input type="hidden" id="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_CREATE%>"/>
	 			<input type="hidden" id="errMsg" value=""/>
              	<input type="hidden" name="trxIdLoaded" id="trxIdLoaded" value="${manualReversalVO.transactionCode}" />
              	<input type="hidden" name="actionAuthorizationId" value="${param.authId}" />
          		<input type="hidden" name="resubmitRequest" value="${param.isReSubmit}" />
          		<input type="hidden" name="usecaseId" value = "<%=PortalConstants.TRANSACTION_REVERSAL_USECASE_ID %>"/>

				<tr bgcolor="FBFBFB">
					<td align="right" class="formText" width="15%">
						Transaction ID:
					</td>
					<td align="left" width="20%">
						<html:input tabindex="1" path="transactionCode" cssClass="textBox" onkeypress="return maskInteger(this,event)"/>
					</td>
					<td align="right" class="formText" width="15%">
						<span style="color:#FF0000">*</span>Adjustment Type:
					</td>
					<td align="left">						
						<html:select path="adjustmentType" id="adjustmentType" tabindex="2" cssClass="textBox">
							<html:option value="">---All---</html:option>
							<html:option value="1">Reversal</html:option>
							<html:option value="2">Adjustment</html:option>
		 				</html:select>
					</td>

				</tr>
				<tr bgcolor="FBFBFB">
					<td align="right" class="formText">
						&nbsp;
					</td>
					<td align="left" colspan="3">
						<input type="button" class="button" value="Load Details" onclick="loadTransactionDetails();"/>
					</td>
				</tr>
				
				<c:if test="${not empty manualReversalVO.transactionCode && not empty manualReversalVO.productName}">
				
				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						Transaction ID:
					</td>
					<td align="left">
						${manualReversalVO.transactionCode}
					</td>
					<td align="right" bgcolor="F3F3F3" class="formText">
						Product:
					</td>
					<td align="left">
						${manualReversalVO.productName}
						<html:hidden path="productName"/>
					</td>
				</tr>
				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						Date:
					</td>
					<td align="left">
						<fmt:formatDate pattern="dd/MM/yyyy hh:mm a" value="${manualReversalVO.transactionDate}"/>
						<html:hidden path="transactionDate"/>
					</td>
					<td align="right" bgcolor="F3F3F3" class="formText">
						Amount:
					</td>
					<td align="left">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${manualReversalVO.transactionAmount}"/>
						<html:hidden path="transactionAmount"/>
					</td>
				</tr>
				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						Excl. Charges:
					</td>
					<td align="left">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${manualReversalVO.exclusiveCharges}"/>
						<html:hidden path="exclusiveCharges"/>
					</td>
					<td align="right" bgcolor="F3F3F3" class="formText">
						Incl. Charges:
					</td>
					<td align="left">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${manualReversalVO.inclusiveCharges}"/>
						<html:hidden path="inclusiveCharges"/>
					</td>
				</tr>
				<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						Status:
					</td>
					<td align="left" colspan="3">
						${manualReversalVO.supProcessingStatusName}
						<html:hidden path="supProcessingStatusName"/>
					</td>
				</tr>
				</c:if>
				
				<c:if test="${not empty manualReversalVO.commissionTransactionList}">
					<tr><td colspan="4">
						<div class="eXtremeTable" style="width: 40%">
							<table class="tableRegion" width="100%">	
								<tr class="odd">
									<td class="titleRow" style="width: 50%; font-weight: bold">Commission Stakeholder</td>
									<td class="titleRow" style="width: 50%; font-weight: bold">Commission Amount</td>
								</tr>
								<c:forEach items="${manualReversalVO.commissionTransactionList}" var="commissionListModel"  varStatus="status">  
									<tr class="odd">
										<td>${commissionListModel.commissionStakehoder}</td>
										<td>${commissionListModel.commissionAmount}</td>
									</tr>	
								</c:forEach>
						    </table> 
					    </div>
					</td></tr>
				</c:if>
				
				<!--Ledger Section-->
				<tr><td colspan="4">
				<div class="eXtremeTable">
					<table id="entryTable" width="100%" cellspacing="0" cellpadding="0" border="0" class="tableRegion">
						<thead>
							<tr>
								<td class="tableHeader" width="30%">Account Title</td>
								<td class="tableHeader" width="20%">Account Number</td>
								<td class="tableHeader" width="9%">Debit</td>
								<td class="tableHeader" width="9%">Credit</td>
								<td class="tableHeader" width="12%">Amount</td>
								<td class="tableHeader" width="10%">Action</td>
								<td class="tableHeader" width="%">&nbsp;</td>
							</tr>
						</thead>
						<tbody class="tableBody">
							<c:choose>
							<c:when test="${empty manualReversalVO.fundTransferEntryList}">
								<tr class="odd">
									<td>
										<input id="fundTransferEntryList0.accountTittle" name="fundTransferEntryList[0].accountTittle" class="textBoxNick" type="text" readonly="true"/>
									</td>
									<td>
										<input id="fundTransferEntryList0.accountNumber" name="fundTransferEntryList[0].accountNumber" class="textBox" onkeypress="return maskInteger(this,event)" onchange="clearNick(0)" type="text" maxlength="25"/>&nbsp;
	    	        					<input id="fetchTitleBtn0" name="fetchTitleBtn0" type="button" value="Fetch Title" class="button" />
									</td>
									<td>
										<ajax:updateField parser="new ResponseXmlParser()" 
											source="fetchTitleBtn0"
											action="fetchTitleBtn0"
											target="fundTransferEntryList0.accountTittle,errMsg"
											baseUrl="${contextPath}/fetchtitlemanualadjustment.html"
											parameters="accountNo={fundTransferEntryList0.accountNumber},type=1"
											preFunction="fromACInitProgress"
											postFunction="fromACEndProgress"></ajax:updateField>
									</td>
									<td>&nbsp;</td>
									<td align="center">
										<html:input path="fundTransferEntryList[0].amountStr" cssClass="textBox" maxlength="15" onkeypress="return checkNumeric(this,event)"/>
									</td>
									<td align="center">
										<html:select path="fundTransferEntryList[0].transactionType" cssClass="miniSelect">
											<html:option value="">--Select--</html:option>
											<html:option value="1">Debit</html:option>
											<html:option value="2">Credit</html:option>
										</html:select>
									</td>
									<td>
										<input type="button" id="fundTransferEntryList0.add" class="addRow" value="+" style="font-weight:bold;padding: 0px 12px;height: 29px;font-size:20px;" title="Add row" />
										<input type="button" id="fundTransferEntryList0.remove" class="removeRow" value="-" title="Remove row" style="background-color:#E40606 !important;color:#FFFCFC !important;font-weight:bold;padding: 0px 12px;height: 29px;font-size:20px;" />
									</td>
								</tr>
							</c:when>
							<c:otherwise>
							<c:forEach items="${manualReversalVO.fundTransferEntryList}" var="fundTransferEntry" varStatus="iterationStatus">
								<tr class="odd">
									<td>
										<c:if test="${not empty fundTransferEntry.ledgerId}">
											${fundTransferEntry.accountTittle}
											<html:hidden path="fundTransferEntryList[${iterationStatus.index}].ledgerId"/>
											<html:hidden path="fundTransferEntryList[${iterationStatus.index}].accountTittle"/>
										</c:if><c:if test="${empty fundTransferEntry.ledgerId}">
											<html:input path="fundTransferEntryList[${iterationStatus.index}].accountTittle" class="textBoxNick" type="text" readonly="true"/>
										</c:if>
									</td>
									<td>
										<c:if test="${not empty fundTransferEntry.ledgerId}">
											${fundTransferEntry.accountNumber}
											<html:hidden path="fundTransferEntryList[${iterationStatus.index}].accountNumber"/>
										</c:if><c:if test="${empty fundTransferEntry.ledgerId}">
											<html:input path="fundTransferEntryList[${iterationStatus.index}].accountNumber" class="textBox" onkeypress="return maskInteger(this,event)" type="text" maxlength="25"/>&nbsp;
		    	        					<input id="fetchTitleBtn${iterationStatus.index}" name="fetchTitleBtn${iterationStatus.index}" type="button" value="Fetch Title" class="button" />
										</c:if>
									</td>
									<td>
										<c:if test="${not empty fundTransferEntry.ledgerId}">
											<fmt:formatNumber type="number" pattern="###.##" value="${fundTransferEntry.debitAmount}"/>
											<html:hidden path="fundTransferEntryList[${iterationStatus.index}].debitAmount"/>
										</c:if><c:if test="${empty fundTransferEntry.ledgerId}">
											<ajax:updateField parser="new ResponseXmlParser()" 
												source="fetchTitleBtn${iterationStatus.index}"
												action="fetchTitleBtn${iterationStatus.index}"
												target="fundTransferEntryList${iterationStatus.index}.accountTittle,errMsg"
												baseUrl="${contextPath}/fetchtitlemanualadjustment.html"
												parameters="accountNo={fundTransferEntryList${iterationStatus.index}.accountNumber},type=1"
												preFunction="fromACInitProgress"
												postFunction="fromACEndProgress"></ajax:updateField>
										</c:if>											
									</td>
									<td>
										<fmt:formatNumber type="number" pattern="###.##" value="${fundTransferEntry.creditAmount}"/>
										<html:hidden path="fundTransferEntryList[${iterationStatus.index}].creditAmount"/>
									</td>
									<td align="center">
										<html:input path="fundTransferEntryList[${iterationStatus.index}].amountStr" cssClass="textBox" maxlength="15" onkeypress="return checkNumeric(this,event)"/>
									</td>
									<td align="center">
										<html:select path="fundTransferEntryList[${iterationStatus.index}].transactionType" cssClass="miniSelect">
											<html:option value="">--Select--</html:option>
											<html:option value="1">Debit</html:option>
											<html:option value="2">Credit</html:option>
										</html:select>
									</td>
									<td>
										<input type="button" id="fundTransferEntryList${iterationStatus.index}.add" class="addRow" value="+" style="font-weight:bold;padding: 0px 12px;height: 29px;font-size:20px;" title="Add row" />
										<input type="button" id="fundTransferEntryList${iterationStatus.index}.remove" class="removeRow" value="-" title="Remove row" style="background-color:#E40606 !important;color:#FFFCFC !important;font-weight:bold;padding: 0px 12px;height: 29px;font-size:20px;" />
									</td>
								</tr>
							</c:forEach>
							</c:otherwise>
							</c:choose>
						</tbody>
					</table>
				</div>
				</td></tr>
				<tr>
					<td align="right" class="formText">Comments:</td>
					<td colspan="3">
						<html:textarea path="comments" style="height:50px; width:400px;" rows="4" cols="50" onkeypress="return textAreaLengthCounter(this,250);" class="textBox"/>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td colspan="3">
						<input name="_submit" id="_submit" type="submit" value="Process" class="button"/> 
						<input name="reset" id="reset" type="button" value="Cancel" class="button" onclick="javascript:window.location='p_manualreversal.html';"/> 
					</td>
				</tr>
				
			</table>
		</html:form>
        
	</body>
</html>

