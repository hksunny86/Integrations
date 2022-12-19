<%@include file="/common/taglibs.jsp"%>
<%@ page import="com.inov8.microbank.common.util.PortalConstants"%>
<c:set var="retrieveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<html>
	<head>
	<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
	<script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
	<link rel="stylesheet" href="${contextPath}/styles/jquery-ui-custom.min.css" type="text/css">
	
	<style type="text/css">
		table .textBox{
			width: 55px;
			height: 14px;
			margin-bottom: 0px;
		}
		.eXtremeTable .miniSelect{
			width: 90px !important;
			height: 16px;
			margin-bottom: 5px;
		}
		.eXtremeTable .midSelect{
			width: 120px !important;
			height: 16px;
			margin-bottom: 5px;
		}
		
	</style>

   	<script type="text/javascript" src="${contextPath}/scripts/jquery-1.7.2.min.js"></script>
   	<script type="text/javascript" src="${contextPath}/scripts/jquery-ui-custom.min.js"></script>
	<script type="text/javascript">
		var jq = $.noConflict();
		jq(document).ready(
	    	function($)
	      	{
	    	    jq(document).on('click', '#ruleTable .addRow', function () {

	    	        var row = jq(this).closest('tr');
	    	        var clone = row.clone();

	    	        // clear the values
	    	        var tr = clone.closest('tr');
	    	        tr.find('input[type=text], select').val('');
	    	        tr.find('input[type=text], select').each(function() {
	    	        	this.style.borderColor="#e6e6e6";
	    	        });
	    	        tr.find('input[type=checkbox]').attr('checked',false);
	    	        tr.find('td:eq(0)').find("input:hidden").val('');

	    	        jq(this).closest('tr').after(clone);
	    	        resetIdAndNameAttributes();
	    	    });
	    	    
	    	    
	    	    jq(document).on('click', '#ruleTable .removeRow', function () {
	    	        if (jq('#ruleTable .addRow').length > 1) {
	    	            //jq(this).closest('tr').remove();
	    	            jq(this).closest('tr').remove();
	    	            //markRowDeleted();
	    	            resetIdAndNameAttributes();
	    	        }
	    	        if (jq('#ruleTable .addRow').length == 1) {
		    	        var tr = jq(this).closest('tr');
		    	        tr.find('input[type=text], select').val('');
		    	        tr.find('input[type=text], select').each(function() {
		    	        	this.style.borderColor="#e6e6e6";
		    	        });
	    	        }
	    	    });

	    	    function resetIdAndNameAttributes()
	    	    {
	    	    	jq('#ruleTable tbody tr').each(
	    	    		function(index)
	    	    		{
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
	    	    		}
	    	    	);
	    	    }

	    	    function resetForm()
	    	    {
	    	    	$('#ruleTable tbody tr').find(':input:not(:button)').each( function() {
	    	    		this.style.borderColor="#e6e6e6";
	    	    	});
	    	    }

				$( "#btnSave" ).click(
					function()
					{
						var rowCount = $('#ruleTable tbody tr').length;
                    	var originalCount = document.getElementById('originalCount').value;
                    	if(rowCount == originalCount && !isFormDataChanged())
                      	{
                      		return false;
                      	}
                        
						var isValid = true;
						resetForm();
				      	
						$('#ruleTable tbody tr').each( function(outerIndex) {
							var selectArray = $(this).find('select');
							
							if(isValid && selectArray[0].value.length == 0){
								isValid=false;
								selectArray[0].style.borderColor="red";
								alert('Please select Channel');
							}

							if(isValid && selectArray[1].value.length == 0){
								isValid=false;
								selectArray[1].style.borderColor="red";
								alert('Please select Transfer Type');
							}

							if(isValid && selectArray[2].value.length == 0){
								isValid=false;
								selectArray[2].style.borderColor="red";
								alert('Please select Sender - Agent Group');
							}

							if(isValid && selectArray[3].value.length == 0){
								isValid=false;
								selectArray[3].style.borderColor="red";
								alert('Please select Receiver - Agent Group');
							}
							
							if(isValid && selectArray[1].value == 1 && selectArray[2].value == selectArray[3].value){
								isValid=false;
								selectArray[2].style.borderColor="red";
								selectArray[3].style.borderColor="red";
								alert('Sender and Receiver Agent Group should be different for Parent to Parent Transfer');
							}	
							
							if(isValid)
							{
								$(this).find('input[id$="rangeStarts"], input[id$="rangeEnds"]').each(function(){
									if(this.value.length == 0)
									{
										isValid=false;
										this.style.borderColor="red";
									}
								});
								if(!isValid)
								{
									alert("Slab range is required.");
								}
							}
							if(isValid)
							{
								$('#ruleTable tbody tr').each( function(outerIndex) {
									var fromValue = $(this).find('input[id$="rangeStarts"]').val();
									var toValue = $(this).find('input[id$="rangeEnds"]').val();
									
									var fromValue = parseFloat(fromValue);
									var toValue = 	parseFloat(toValue);
									
									if(fromValue == toValue){
										isValid = false;
										$(this).find('input[id$="rangeStarts"], input[id$="rangeEnds"]').each(function(){
											this.style.borderColor="red";
										});
									}
								});
								if(!isValid)
								{
									alert("From Slab Range and To Slab Range can not have the same values");
								}
							}
							if(isValid)
							{
								$('#ruleTable tbody tr').each( function(outerIndex) {
									var fromValue = $(this).find('input[id$="rangeStarts"]').val();
									var toValue = $(this).find('input[id$="rangeEnds"]').val();
									
									var fromValue = parseFloat(fromValue);
									var toValue = 	parseFloat(toValue);
									if(fromValue > toValue){
										isValid = false;
										$(this).find('input[id$="rangeStarts"], input[id$="rangeEnds"]').each(function(){
											this.style.borderColor="red";
										});
									}
								});
								if(!isValid)
								{
									alert("From Slab Range can not be greater than To Slab Range");
								}
							}
/* 							if(isValid)
							{
								var inclusiveFixAmount = $(this).find('input[id$="inclusiveFixAmount"]').val();
								var inclusivePercentAmount = $(this).find('input[id$="inclusivePercentAmount"]').val();
								var exclusiveFixAmount = $(this).find('input[id$="exclusiveFixAmount"]').val();
								var exclusivePercentAmount = $(this).find('input[id$="exclusivePercentAmount"]').val();

								if(inclusiveFixAmount.length == 0 && inclusivePercentAmount.length == 0 
										&& exclusiveFixAmount.length == 0 && exclusivePercentAmount.length == 0)
								{
									isValid = false;
									$(this).find('input[id$="inclusiveFixAmount"], input[id$="inclusivePercentAmount"], input[id$="exclusiveFixAmount"], input[id$="exclusivePercentAmount"]').each(function(){
										this.style.borderColor="red";
									});
									alert("Please provide atleast one of the Incl. Fixed, Incl. %age, Excl. Fixed or Excl. %age.");
								}
							}
 */							if(isValid){
							
								var inclusivePercentAmount = parseFloat($(this).find('input[id$="inclusivePercentAmount"]').val());
								var exclusivePercentAmount = parseFloat($(this).find('input[id$="exclusivePercentAmount"]').val());
								
								if ((inclusivePercentAmount != undefined && inclusivePercentAmount != '') && inclusivePercentAmount < 0.01 || inclusivePercentAmount > 100){
					        		alert("Invalid Inclusive Percentage Amount.");
					        		isValid = false;
					        		this.style.borderColor="red";
					        	}
								else if ((exclusivePercentAmount != undefined && exclusivePercentAmount != '') && exclusivePercentAmount < 0.01 || exclusivePercentAmount >100 ){
					        		alert("Invalid Exclusive Percentage Amount.");
					        		isValid = false;
					        		this.style.borderColor="red";
					        	}
					        	 
							}
							
							if(isValid)
							{
								$('#ruleTable tbody tr').each( function(outerIndex) {
									$(this).find('input[type="text"]').each( function(outerIndex){
										var floatVal = $(this).val();
										if(floatVal != undefined && floatVal != '' && isNaN(floatVal)){
											alert("Invalid numeric value.");
					        				isValid = false;
					        				this.style.borderColor="red";
										}else{
											if(floatVal != undefined && floatVal != '' && floatVal.length > 3 
										   			&& floatVal.indexOf(".") != "-1"
										   			&& (floatVal.length - floatVal.indexOf(".") > 3) ){
										   		alert("Value should be max 2 decimal places");
						        				isValid = false;
						        				this.style.borderColor="red";
										   	}
										}
										
									});
								});
							}
							
						});

						if(isValid)//This is the last validation check
						{
							$('#ruleTable tbody tr').each( function(outerIndex) {
								var outerSelectArray = $(this).find('select');
								var outerSelectTextArray = $(this).find('input[type="text"]');
								$('#ruleTable tbody tr').each( function(innerIndex) {
									if(outerIndex != innerIndex)
									{
										var innerSelectArray = $(this).find('select');
										var innerSelectTextArray = $(this).find('input[type="text"]');
										if( outerSelectArray[0].value == innerSelectArray[0].value && 
											outerSelectArray[1].value == innerSelectArray[1].value && 
											outerSelectArray[2].value == innerSelectArray[2].value && 
											outerSelectArray[3].value == innerSelectArray[3].value)
										{
											
											isValid = isValidSlabRange(outerSelectTextArray[0].value, outerSelectTextArray[1].value, innerSelectTextArray[0].value, innerSelectTextArray[1].value);
											if(!isValid){
												outerSelectArray.each( function() {
													this.style.borderColor="red";
												});
												
												innerSelectArray.each( function() {
													this.style.borderColor="red";
												});
											
												alert('Overlapping slab rates detected for same Channel, Transfer Type and Sender/Receiver Agent Group');
												return isValid;
											}
										}
									}
								});
								
								if(!isValid){
									return false;
								}
							});
						}

						if(isValid)
						{
							$('#agentTransferRuleVoForm').submit();
						}
					}
				);//end of btnSave click event

				$( "#btnRemoveAll" ).click(
					function()
					{
						$( "#dialog-confirm" ).dialog({
					      resizable: false,
					      height:160,
					      width:340,
					      modal: true,
					      buttons: {
					        "Remove All": function() {
					        	jq('#isUpdate').val(true);
					        	markAllRowsDeleted();
					        	$('#agentTransferRuleVoForm').submit();
					        },
					        Cancel: function() {
					          $( this ).dialog( "close" );
					        }
					      }
					    });
					}
				);//end of btnRemoveAll click event
	      	}
	    );
	</script>
	<script type="text/javascript">
	function markRowDeleted(index){
		var s = index;
		//alert(document.getElementById("agentTransferRuleModelList"+index+".isDeleted").value);
		//document.getElementById("agentTransferRuleModelList"+index+".isDeleted").value=1;
	}
	
	function markAllRowsDeleted(){
		var inputs = document.getElementsByTagName("input"); //or document.forms[0].elements;  
        var cbs = []; //will contain all checkboxes  
        var checked = []; //will contain all checked checkboxes
        var result = true;
        for (var i = 0; i < inputs.length; i++) {  
          if (inputs[i].type == "hidden") {  
             if ( inputs[i].id != "" && inputs[i].id.indexOf(".isDeleted") > -1){
            	 inputs[i].value=1;
            	 //alert(inputs[i].id);
             }
              
          }  
        }
	}
	
	function isValidSlabRange(outerStart, outerEnd, innerStart, innerEnd){
		var result = true;
		var outerStartD = parseFloat(outerStart);
		var outerEndD = parseFloat(outerEnd);
		var innerStartD = parseFloat(innerStart);
		var innerEndD = parseFloat(innerEnd);
		
		if(innerStartD > outerStartD && innerStartD < outerEndD){
			return false;
		}
		
		if(innerStartD > outerStartD && innerEndD < outerEndD){
			return false;
		}
		if(innerStartD < outerStartD && innerEndD >= outerEndD){
			return false;
		}
		if(innerEndD == outerStartD ){
			return false;
		}
		if(innerStartD == outerStartD || innerEndD == outerEndD ){
			return false;
		}
		
		return result;
	}
	</script>
	<meta name="decorator" content="decorator2">
	<meta name="title" content="Agent 2 Agent Transfer Rules"/>
	</head>
	<body bgcolor="#ffffff">
		<div class="infoMsg" id="errorMessages" style="display:none;"></div>
	  	<div id="successMsg" class ="infoMsg" style="display:none;"></div>
		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
			    <c:forEach var="msg" items="${messages}">
			      <c:out value="${msg}" escapeXml="false"/>
			      <br/>
			    </c:forEach>
			</div>
	  		<c:remove var="messages" scope="session"/>
		</c:if>
		<html:form id="agentTransferRuleVoForm" name="agentTransferRuleVoForm" commandName="agentTransferRuleVo" action="p_agenttransferruleform.html">
			<table border="0" width="100%">
				<tr>
					<td colspan="2">
						<div class="eXtremeTable">
							<table id="ruleTable" width="100%" cellspacing="0" cellpadding="0" border="0">
								<thead>
									<tr>
										<td class="tableHeader" width="10%">Channel</td>
										<td class="tableHeader" width="12%">TransferType</td>
										<td class="tableHeader" width="12%">Sender - Agent Group</td>
										<td class="tableHeader" width="12%">Receiver - Agent Group</td>
										<td class="tableHeader" width="7%">Slab(From)</td>
										<td class="tableHeader" width="7%">Slab(To)</td>
										<td class="tableHeader" width="7%">Incl. Fixed</td>
										<td class="tableHeader" width="7%">Incl. %age</td>
										<td class="tableHeader" width="7%">Excl. Fixed</td>
										<td class="tableHeader" width="7%">Excl. %age</td>
										<td class="tableHeader">3rd Party Check</td>
										<td class="tableHeader" width="7%">Action</td>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${agentTransferRuleVo.agentTransferRuleModelList}" var="agentTransferRuleModel" varStatus="iterationStatus">
										<tr>
											<td align="center">
												<html:hidden path="agentTransferRuleModelList[${iterationStatus.index}].agentTransferRuleId"/>
												
												<html:select path="agentTransferRuleModelList[${iterationStatus.index}].deviceTypeId" cssClass="miniSelect">
													<html:option value="5">Mobile App</html:option>
												</html:select>
											</td>
											<td align="center">
												<html:select path="agentTransferRuleModelList[${iterationStatus.index}].transferTypeId" cssClass="midSelect">
													<html:option value="">--Select--</html:option>
													<html:option value="1">Parent to Parent</html:option>
													<html:option value="2">Parent to Child</html:option>
													<html:option value="3">Child to Parent</html:option>
													<html:option value="4">Child to Child</html:option>
												</html:select>
											</td>
											<td align="center">
												<html:select path="agentTransferRuleModelList[${iterationStatus.index}].senderGroupId" cssClass="midSelect">
													<html:option value="">--Select--</html:option>
													<html:options items="${agentGroupList}" itemLabel="groupTitle" itemValue="agentTaggingId" />
												</html:select>
											</td>
											<td align="center">
												<html:select path="agentTransferRuleModelList[${iterationStatus.index}].receiverGroupId" cssClass="midSelect">
													<html:option value="">--Select--</html:option>
													<html:options items="${agentGroupList}" itemLabel="groupTitle" itemValue="agentTaggingId" />
												</html:select>
											</td>
											<td align="center">
												<html:input path="agentTransferRuleModelList[${iterationStatus.index}].rangeStarts" cssClass="textBox" maxlength="7" onkeypress="return maskNumber(this,event)" />
											</td>
											<td align="center">
												<html:input path="agentTransferRuleModelList[${iterationStatus.index}].rangeEnds" cssClass="textBox" maxlength="7" onkeypress="return maskNumber(this,event)" />
											</td>
											<td align="center">
												<html:input path="agentTransferRuleModelList[${iterationStatus.index}].inclusiveFixAmount" cssClass="textBox" maxlength="7" onkeypress="return maskNumber(this,event)" />
											</td>
											<td align="center">
												<html:input path="agentTransferRuleModelList[${iterationStatus.index}].inclusivePercentAmount" cssClass="textBox" maxlength="5" onkeypress="return maskNumber(this,event)" />
											</td>
											<td align="center">
												<html:input path="agentTransferRuleModelList[${iterationStatus.index}].exclusiveFixAmount" cssClass="textBox" maxlength="7" onkeypress="return maskNumber(this,event)" />
											</td>
											<td align="center">
												<html:input path="agentTransferRuleModelList[${iterationStatus.index}].exclusivePercentAmount" cssClass="textBox" maxlength="5" onkeypress="return maskNumber(this,event)" />
											</td>
											<td align="center">
												<html:checkbox path="agentTransferRuleModelList[${iterationStatus.index}].thirdPartyCheck"/>
											</td>
											<td>
												<input type="button" id="agentTransferRuleModelList${iterationStatus.index}.add" class="addRow" value="+" style="font-weight:bold;padding: 0px 12px;height: 29px;font-size:20px;" title="Add row" />
												<input type="button" id="agentTransferRuleModelList${iterationStatus.index}.remove" class="removeRow" value="-" title="Remove row" style="background-color:#E40606 !important;color:#FFFCFC !important;font-weight:bold;padding: 0px 12px;height: 29px;font-size:20px;" onClick="markRowDeleted(${iterationStatus.index});" />	
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</td>
				</tr>
				<tr>
					<td align="center" colspan="2">
						<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_UPDATE%>">
						<input type="hidden" name="isUpdate" id="isUpdate" value="false"/>
						<input type="hidden" name="originalCount" id="originalCount" value="${agentTransferRuleVo.agentTransferRuleModelList.size()}"/>
						
						<input type="button" value="Save" class="button" id="btnSave"/>
						<c:if test="${not empty  agentTransferRuleVo.agentTransferRuleModelList}">
							<input type="button" value="Remove All" class="button" id="btnRemoveAll"/>
						</c:if>
						<input name="reset" type="reset" onclick="javascript: window.location='home.html'" class="button" value="Cancel" />
					</td>
				</tr>
			</table>
		</html:form>

		<div id="dialog-confirm" title="Confirmation" style="display: none;">
		  <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>Are you sure you want to Remove all rules?</p>
		</div>
	</body>
</html>
