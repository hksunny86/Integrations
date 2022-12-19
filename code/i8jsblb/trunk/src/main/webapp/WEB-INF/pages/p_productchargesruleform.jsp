<%@include file="/common/taglibs.jsp"%>
<%@ page import="com.inov8.microbank.common.util.PortalConstants"%>
<c:set var="retrieveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<html>
	<head>
	<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
	<style type="text/css">
		table .textBox
		{
			width: 70px;
			height: 16px;
			margin-bottom: 0px;
		}
	</style>
	<script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
	<link rel="stylesheet" href="${contextPath}/styles/jquery-ui-custom.min.css" type="text/css">
   	<script type="text/javascript" src="${contextPath}/scripts/jquery-1.7.2.min.js"></script>
   	<script type="text/javascript" src="${contextPath}/scripts/jquery-ui-custom.min.js"></script>
	<script type="text/javascript">
		var jq = $.noConflict();
		jq(document).ready(
	    	function($)
	      	{
	    	    jq(document).on('click', '#productChargesRuleTable .addRow', function () {
                    
	    	    	$('input[type="button"]').prop('disabled', false);
	    	        var row = jq(this).closest('tr');
	    	        var clone = row.clone();

	    	        // clear the values
	    	        var tr = clone.closest('tr');
	    	        tr.find('input[type=text], select').val('');
	    	        tr.find('input[type=text], select').each(function() {
	    	        	this.style.borderColor="#e6e6e6";
	    	        });

	    	        tr.find('td:eq(0)').find("input:hidden").val('');
	    	        //alert(tr.siblings(":first").andSelf().find("input:hidden").val(''));

	    	        jq(this).closest('tr').after(clone);
	    	        resetIdAndNameAttributes();
	    	    });

	    	    jq(document).on('click', '#productChargesRuleTable .removeRow', function () {
	    	    	$('input[type="button"]').prop('disabled', false);
	    	        if (jq('#productChargesRuleTable .addRow').length > 1) {
	    	            //jq(this).closest('tr').remove();
	    	            jq(this).closest('tr').remove();
	    	            //markRowDeleted();
	    	            resetIdAndNameAttributes();
	    	        }
	    	    });
	    	    
	    	    jq('input[type="text"]').on('change', function() {

				     // Change event fired..
				     jq('input[type="button"]').prop('disabled', false);
				});
				
				jq('select').on('change', function() {

				     // Change event fired..
				     jq('input[type="button"]').prop('disabled', false);
				});

	    	    
	    	    
	    	    function resetIdAndNameAttributes()
	    	    {
	    	    	jq('#productChargesRuleTable tbody tr').each(
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
	    	    	$('#productChargesRuleTable tbody tr').find(':input:not(:button)').each( function() {
	    	    		this.style.borderColor="#e6e6e6";
	    	    	});
	    	    }

				$( "#btnSave" ).click(
					function()
					{
						var isValid = true;
						resetForm();
						$('#productChargesRuleTable tbody tr').each( function(outerIndex) {
							var selectArray = $(this).find('select');
							if(selectArray[0].value.length == 0 && selectArray[1].value.length == 0 && selectArray[2].value.length == 0)
							{
								isValid=false;
								selectArray.each( function() {
								
										this.style.borderColor="red";
									}
								);
								alert('Please select atleast one of the Channel, Segment or Agent Network.');
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
								$('#productChargesRuleTable tbody tr').each( function(outerIndex) {
									var sameValueArray = $(this).find('input');
									var fromValue = parseFloat(sameValueArray[4].value);
									var toValue = 	parseFloat(sameValueArray[5].value);
									if(fromValue == toValue){
										isValid = false;
										this.style.borderColor="red";
									}
								});
								if(!isValid)
								{
									alert("From Slab Range and To Slab Range can not have the same values");
								}
							}
							if(isValid)
							{
								$('#productChargesRuleTable tbody tr').each( function(outerIndex) {
									var greaterValue = $(this).find('input');
									var fromValue = parseFloat(greaterValue[4].value);
									var toValue = parseFloat(greaterValue[5].value);
									if(fromValue > toValue){
										isValid = false;
										this.style.borderColor="red";
									}
								});
								if(!isValid)
								{
									alert("From Slab Range can not be greater than To Slab Range");
								}
							}
							if(isValid)
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
							if(isValid){
							
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
								$('#productChargesRuleTable tbody tr').each( function(outerIndex) {
									$(this).find('input[type="text"]').each( function(outerIndex){
										var floatVal = $(this).val();
										if(floatVal != undefined && floatVal != '' && isNaN(floatVal)){
											alert("Invalid numeric value.");
					        				isValid = false;
					        				this.style.borderColor="red";
										}
									});
								});
							}
							
						});

						if(isValid)//This is the last validation check
						{
							$('#productChargesRuleTable tbody tr').each( function(outerIndex) {
								var outerSelectArray = $(this).find('select');
								var outerSelectTextArray = $(this).find('input');
								$('#productChargesRuleTable tbody tr').each( function(innerIndex) {
									if(outerIndex != innerIndex)
									{
										var innerSelectArray = $(this).find('select');
										var innerSelectTextArray = $(this).find('input');
										if(outerSelectArray[0].value == innerSelectArray[0].value && outerSelectArray[1].value == innerSelectArray[1].value && outerSelectArray[2].value == innerSelectArray[2].value)
										{
											//alert("inside.." + innerSelectTextArray[4].value);
											
											isValid = isValidSlabRange(outerSelectTextArray[4].value, outerSelectTextArray[5].value, innerSelectTextArray[4].value, innerSelectTextArray[5].value);
											if(!isValid){
											outerSelectArray.each( function() {
													this.style.borderColor="red";
												}
											);
											innerSelectArray.each( function() {
													this.style.borderColor="red";
												}
											);
											
											//alert('Two rows cannot have same Channel, Segment and Agent Network.');
											alert('Two rows having same Channel, Segment and Agent Network can not have overlapping slab rates');
											return isValid;
											}
										}
									}
								});
								if(!isValid)
								{
									return false;
								}
							});
						}

						if(isValid)
						{
							$('#productChargesRuleVoForm').submit();
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
					        	$('#productChargesRuleVoForm').submit();
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
		//alert(document.getElementById("productChargesRuleModelList"+index+".isDeleted").value);
		//document.getElementById("productChargesRuleModelList"+index+".isDeleted").value=1;
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
	<meta name="title" content="Custom Product Charges"/>
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
		<html:form id="productChargesRuleVoForm" name="productChargesRuleVoForm" commandName="productChargesRuleVo" action="p_productchargesruleform.html">
			<html:hidden path="productId"/>
			<html:hidden path="productName"/>
			<table border="0" width="100%">
				<tr>
					<td colspan="2">
						<h2> <spring:eval expression="productChargesRuleVo.productName"/> </h2>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<div class="eXtremeTable">
							<table id="productChargesRuleTable" width="100%" cellspacing="0" cellpadding="0" border="0">
								<thead>
									<tr>
										<td class="tableHeader">
											Channel
										</td>
										<td class="tableHeader">
											Segment
										</td>
										<td class="tableHeader">
											Agent Network
										</td>
										<td class="tableHeader">
											Slab(From)
										</td>
										<td class="tableHeader">
											Slab(To)
										</td>
										<td class="tableHeader">
											Incl. Fixed
										</td>
										<td class="tableHeader">
											Incl. %age
										</td>
										<td class="tableHeader">
											Excl. Fixed
										</td>
										<td class="tableHeader">
											Excl. %age
										</td>
										<td class="tableHeader">
											Action
										</td>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${productChargesRuleVo.productChargesRuleModelList}" var="productChargesRuleModel" varStatus="iterationStatus">
										<tr>
											<td align="center">
												<html:hidden path="productChargesRuleModelList[${iterationStatus.index}].productChargesRuleId"/>
												<html:hidden path="productChargesRuleModelList[${iterationStatus.index}].isDeleted"/>
												<html:hidden path="productChargesRuleModelList[${iterationStatus.index}].productId"/>
												<html:hidden path="productChargesRuleModelList[${iterationStatus.index}].versionNo"/>
												<html:select path="productChargesRuleModelList[${iterationStatus.index}].deviceTypeId" cssClass="textBox">
													<html:option value="">--All--</html:option>
													<html:options items="${deviceTypeModelList}" itemLabel="name" itemValue="deviceTypeId"/>
												</html:select>
											</td>
											<td align="center">
												<html:select path="productChargesRuleModelList[${iterationStatus.index}].segmentId" cssClass="textBox">
													<html:option value="">--All--</html:option>
													<html:options items="${segmentModelList}" itemLabel="name" itemValue="segmentId"/>
												</html:select>
											</td>
											<td align="center">
												<html:select path="productChargesRuleModelList[${iterationStatus.index}].distributorId" cssClass="textBox">
													<html:option value="">--All--</html:option>
													<html:options items="${distributorModelList}" itemLabel="name" itemValue="distributorId"/>
												</html:select>
											</td>
											<td align="center">
												<html:input path="productChargesRuleModelList[${iterationStatus.index}].rangeStarts" cssClass="textBox" maxlength="7" onkeypress="return maskNumber(this,event)" />
											</td>
											<td align="center">
												<html:input path="productChargesRuleModelList[${iterationStatus.index}].rangeEnds" cssClass="textBox" maxlength="7" onkeypress="return maskNumber(this,event)" />
											</td>
											<td align="center">
												<html:input path="productChargesRuleModelList[${iterationStatus.index}].inclusiveFixAmount" cssClass="textBox" maxlength="7" onkeypress="return maskNumber(this,event)" />
											</td>
											<td align="center">
												<html:input path="productChargesRuleModelList[${iterationStatus.index}].inclusivePercentAmount" cssClass="textBox" maxlength="5" onkeypress="return maskNumber(this,event)" />
											</td>
											<td align="center">
												<html:input path="productChargesRuleModelList[${iterationStatus.index}].exclusiveFixAmount" cssClass="textBox" maxlength="7" onkeypress="return maskNumber(this,event)" />
											</td>
											<td align="center">
												<html:input path="productChargesRuleModelList[${iterationStatus.index}].exclusivePercentAmount" cssClass="textBox" maxlength="5" onkeypress="return maskNumber(this,event)" />
											</td>
											<td>
												<authz:authorize ifAnyGranted="<%= PortalConstants.MNG_PRDCT_CHARGES_UPDATE %>">
													<input type="button" id="productChargesRuleModelList${iterationStatus.index}.add" class="addRow" value="+" style="font-weight:bold;padding: 0px 12px;height: 29px;font-size:20px;" title="Add row" />
													<input type="button" id="productChargesRuleModelList${iterationStatus.index}.remove" class="removeRow"
														   value="-" title="Remove row"
														   style="background-color:#E40606 !important;color:#FFFCFC !important;font-weight:bold;
													   padding: 0px 12px;height: 29px;font-size:20px;" onClick="markRowDeleted(${iterationStatus.index});" />
												</authz:authorize>
												<authz:authorize ifAnyGranted="<%= PortalConstants.MNG_PRDCT_LMTS_UPDATE %>">
													<input type="button" id="productChargesRuleModelList${iterationStatus.index}.add" class="addRow" value="+" style="font-weight:bold;padding: 0px 12px;height: 29px;font-size:20px;" title="Add row" />
													<input type="button" id="productChargesRuleModelList${iterationStatus.index}.remove" class="removeRow"
														   value="-" title="Remove row"
														   style="background-color:#E40606 !important;color:#FFFCFC !important;font-weight:bold;
													   padding: 0px 12px;height: 29px;font-size:20px;" onClick="markRowDeleted(${iterationStatus.index});" />
												</authz:authorize>
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
						<authz:authorize ifAnyGranted="<%= PortalConstants.MNG_PRDCT_CHARGES_UPDATE %>">
							<input type="button" value="Save" class="button" id="btnSave" disabled="disabled" />
						</authz:authorize>
						<authz:authorize ifAnyGranted="<%= PortalConstants.MNG_PRDCT_LMTS_UPDATE %>">
							<input type="button" value="Save" class="button" id="btnSave" disabled="disabled" />
						</authz:authorize>
						<c:if test="${not empty  productChargesRuleVo.productChargesRuleModelList}">
							<authz:authorize ifAnyGranted="<%= PortalConstants.MNG_PRDCT_CHARGES_DELETE %>">
								<input type="button" value="Remove All" class="button" id="btnRemoveAll"/>
							</authz:authorize>
							<authz:authorize ifAnyGranted="<%= PortalConstants.MNG_PRDCT_LMTS_UPDATE%>">
								<input type="button" value="Remove All" class="button" id="btnRemoveAll"/>
							</authz:authorize>
						</c:if>
						<authz:authorize ifAnyGranted="<%= PortalConstants.MNG_PRDCT_LMTS_UPDATE %>">
							<input name="reset" type="reset" onclick="javascript: window.location='productupdateform.html?productId=${param.productId}'" class="button" value="Cancel" />
						</authz:authorize>
						<authz:authorize ifAnyGranted="<%= PortalConstants.MNG_PRDCT_LMT_READ %>">
							<input name="reset" type="reset" onclick="javascript: window.location='productsearch.html'" class="button" value="Cancel" />
						</authz:authorize>
					</td>
				</tr>
			</table>
		</html:form>

		<div id="dialog-confirm" title="Confirmation" style="display: none;">
		  <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>Are you sure you want to Remove all rules?</p>
		</div>
	</body>
</html>
