<%@page import="com.inov8.microbank.common.util.AuthenticationUtil"%>
<%@include file="/common/taglibs.jsp"%>
<%@ page import="com.inov8.microbank.common.util.PortalConstants"%>
<%@ page import="com.inov8.ola.util.LimitTypeConstants"%>
<%@ page import="com.inov8.microbank.common.util.AccountTypeCategoryEnum"%>
<c:set var="retrieveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<c:set var="agentAccountTypeCategoryIdx"><%=AccountTypeCategoryEnum.AGENT.getIndex()%></c:set>
<c:set var="customerAccountTypeCategoryIdx"><%=AccountTypeCategoryEnum.CUSTOMER.getIndex()%></c:set>
<html>
	<head>
<meta name="decorator" content="decorator2">
	<%
		String createPermission = PortalConstants.ADMIN_GP_CREATE;
		createPermission +=	"," + PortalConstants.PG_GP_CREATE;
		createPermission +=	"," + PortalConstants.MNG_CUST_AC_TP_LMT_CREATE;
		
		String readPermission = PortalConstants.ADMIN_GP_READ;
		readPermission +=	"," + PortalConstants.PG_GP_READ;
		readPermission +=	"," + PortalConstants.MNG_CUST_AC_TP_LMT_READ;

		String updatePermission = PortalConstants.ADMIN_GP_UPDATE;
		updatePermission +=	"," + PortalConstants.PG_GP_UPDATE;
		updatePermission +=	"," + PortalConstants.MNG_CUST_AC_TP_LMT_UPDATE;

		String createOrUpdatePermission = createPermission + "," + updatePermission;
		boolean isReadOnly = ! ((AuthenticationUtil.checkRightsIfAny(createPermission) || AuthenticationUtil.checkRightsIfAny(updatePermission)));

%>
<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
<script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
<script type="text/javascript" src="${contextPath}/scripts/prototype.js"></script>
<script type="text/javascript" src="${contextPath}/scripts/scriptaculous.js"></script>
<script type="text/javascript" src="${contextPath}/scripts/overlibmws.js"></script>
<script type="text/javascript" src="${contextPath}/scripts/ajaxtags-1.2-beta2.js"></script>
<script type="text/javascript" src="${contextPath}/scripts/jquery-1.7.2.min.js"></script>

<script type="text/javascript">
	var jq = $.noConflict();
	jq(document)
			.ready(
		    	function($)
		      	{
		    		
		    		$( "#dormantMarkingEnabled1" ).change(
							function() {
								if ($("#dormantMarkingEnabled1").prop('checked') == true) {
										$("#dormantTimePeriod").removeAttr("disabled");
								} else {
										$("#dormantTimePeriod").attr("disabled", "disabled");
										$('#dormantTimePeriod').val("");
								}
							});

					if ($("#dormantMarkingEnabled1").prop('checked') == true) {
						$("#dormantTimePeriod").removeAttr("disabled");
					} else {
						$("#dormantTimePeriod").attr("disabled", "disabled");
					}
		    		
		      		$( "#isCategoryCustomerAccountType" ).focus();

		      		var isSubAccountType = <%= request.getAttribute("isSubAccountType") %>;
		      		if(isSubAccountType)
	      			{
		      			$("#parentAccountTypeRow").show();
	      			}
		      		else
	      			{
		      			$("#parentAccountTypeRow").hide();
	      			}

		      		$("#isCategoryCustomerAccountType").change(
		      			function()
		      			{
		      				var agentAccountTypeCategoryIdx = <%=AccountTypeCategoryEnum.AGENT.getIndex()%>;
		      				var selectedIdx = $(this).val();
		      				if( selectedIdx != "" && agentAccountTypeCategoryIdx == selectedIdx )
	      					{
	      						$('#parentAccountTypeRow').show();
	      					}
		      				else
	      					{
		      					$('#parentAccountTypeId').val("");
		      					$('#parentAccountTypeRow').hide();
	      					}
						}
		      		);
		      		var limitTypeIdMaxBalance = <%=LimitTypeConstants.MAXIMUM%>;
		      		var maxBalanceRowId = "row_" + limitTypeIdMaxBalance;
					
		      		var debiltLimitId = "#debit_limit_" + limitTypeIdMaxBalance;
		      		$( debiltLimitId ).attr("disabled", true)
		      		$( ".tableRegion :checkbox" ).change(
		      			function()
		      			{
		      				$(this).closest("tr").find("input:text:not("+debiltLimitId+")").attr("readonly", !this.checked);
		      			}
		      		);
					
					var isReadOnly = <%=isReadOnly%>;
		      		if( isReadOnly )
		      		{
						$("#olaCustomerAccountTypeForm :input").attr( "disabled", true );
						$("#cancel").attr( "disabled", false );
					}
					

					$( "#btnSave" ).click(
						function()
						{
							var isValidForm = true;
							

							//isValidForm = isCheckBoxCorrect();
							
							//var ipt = $('#tableLimits tr').filter(':has(:checkbox:checked)').find('input');
							$.each($('#tableLimits tr').filter(':has(:checkbox:checked)') .find('input[type !=hidden]'),
								       function () {
								            if($(this).val() != 'true' && $(this).val() != 'false' && $(this).prop('disabled') ==false){
								            	//alert($(this).prop('disabled'));
								            	$(this).css({ "borderColor": "" });
								            	if($(this).val() == "" || $(this).val() == undefined || $(this).val() == null){
								            		alert("While applying limits, you need to input its value as well");
								            		$(this).css({ "borderColor": "red" });
								            		isValidForm = false;
								            		return isValidForm;
								            	}
								            }
								       });

							$.each($('#tableLimits tr').find('input[type=text]'),
								function () {
									if($(this).prop('disabled') == false){
								    	$(this).css({ "borderColor": "" });
								    	if($(this).val() != "" && $(this).val() != undefined || $(this).val() != null){
								        	if(!isInteger( $(this).val()) ){
							            		alert("Invalid limit value");
							            		$(this).css({ "borderColor": "red" });
							            		isValidForm = false;
							            		return isValidForm;
								            }
								       	}
									}
							});
							
							
							if(!isValidForm){
								return isValidForm;
							}
													
							if( $( "#isCategoryCustomerAccountType" ).val().length == 0 )
							{
								alert( "Account Type Category is required." );
								isValidForm = false;
							}
							else if ($("#accountType").val().length == 0) {
								alert("Account Type is required.");
								isValidForm = false;
							}
							else if ($("#accountNo").val().length == 0)
							{
								alert("Oracle # is required.");
								isValidForm = false;
							}else if(!isInteger($("#accountNo").val()))
							{
								alert("Oracle # is invalid.");
								isValidForm = false;
							}else if ($("#accountNick").val().length == 0)
							{
								alert("Account Title is required.");
								isValidForm = false;
							}else if(document.getElementById("dormantMarkingEnabled1").checked)
							{
								if($("#dormantTimePeriod").val().length == 0)
								{
									alert("You need to enter \'Dormant Time Period\' when Dormant Marking is Enabled");
									$("#dormantTimePeriod").focus();
									isValidForm = false;
								}
							}
							else
							{
								$.each($(".tableRegion tr").filter(":has(:checkbox:checked)") .find("input:text:not("+debiltLimitId+")"),
// 								$( ".tableRegion input:text:not("+debiltLimitId+")").each(
									function()
									{
										var minDebitCreditLimit = <%=PortalConstants.MIN_DEBIT_CREDIT_LIMIT%>;
										var limitType = $(this).closest("tr").find("td").filter(":first").find("span").html();
										if( $( this ).val().length == 0 )
										{
											var currentRowId = $(this).closest("tr").attr("id");
											if( maxBalanceRowId == currentRowId )
											{
												alert( limitType + " Credit Limit is required." );
											}
											else
											{
												alert( limitType + " Debit and Credit Limits are required." );
											}
											isValidForm = false;
										}
										else if( $( this ).val() <= minDebitCreditLimit )
										{
											alert( limitType + " Debit and Credit Limits should be greater than " + minDebitCreditLimit + "." );
											$(this).css({ "borderColor": "red" });
											isValidForm = false;
										}
										return isValidForm;
});
							}
							
							if( isValidForm )
							{
									$( "#olaCustomerAccountTypeForm" ).submit();
							}
						}
					);		      		
		      	}
		    );

		</script>
		<script type="text/javascript">
		 function validateAccountType(e) {
	            
	            if ((e.keyCode >= 48 && e.keyCode <= 57) ||
	               (e.keyCode >= 65 && e.keyCode <= 90)  ||
	               (e.keyCode >= 97 && e.keyCode <= 122) ||
	               (e.keyCode == 189 || e.keyCode == 190))
	                return true;

	            return false;
	        }
		 
		 function isCheckBoxCorrect(){
		        var result = true;
		        if(document.getElementById("limitTypeVoList0.applicable1") != undefined && document.getElementById("limitTypeVoList0.applicable1").checked){
		        	if(document.getElementById("debit_limit_1").value == undefined || document.getElementById("debit_limit_1").value==""
		        			|| document.getElementById("limitTypeVoList0.creditLimit").value == undefined || document.getElementById("limitTypeVoList0.creditLimit").value == ""){
		        		result = false;
		        	}
		        }
		        if(document.getElementById("limitTypeVoList1.applicable1") != undefined && document.getElementById("limitTypeVoList1.applicable1").checked){
		        	if(document.getElementById("debit_limit_2").value == undefined || document.getElementById("debit_limit_2").value==""
		        			|| document.getElementById("limitTypeVoList1.creditLimit").value == undefined || document.getElementById("limitTypeVoList1.creditLimit").value == ""){
		        		result = false;
		        	}
		        }
		        if(document.getElementById("limitTypeVoList2.applicable1") != undefined && document.getElementById("limitTypeVoList2.applicable1").checked){
		        	if(document.getElementById("debit_limit_3").value == undefined || document.getElementById("debit_limit_3").value==""
		        			|| document.getElementById("limitTypeVoList2.creditLimit").value == undefined || document.getElementById("limitTypeVoList2.creditLimit").value == ""){
		        		result = false;
		        	}
		        }
		        if(document.getElementById("limitTypeVoList3.applicable1") != undefined && document.getElementById("limitTypeVoList3.applicable1").checked){
		        	if(/*document.getElementById("debit_limit_4").value == undefined || document.getElementById("debit_limit_4").value==""
		        			||*/ document.getElementById("limitTypeVoList3.creditLimit").value == undefined || document.getElementById("limitTypeVoList3.creditLimit").value == ""){
		        		result = false;
		        	}
		        }
		        
		        if(result == false){
		        	alert("In order to apply limit, you need to provide its debit and credit limits as well");
		        }
		        
		        return result;
	        }
		 
		 function checkLimit(field){
			 if(field.readOnly ==true){
				 alert("please select apply limit first to enter value");
			 }
		 }
		</script>

		<meta name="title" content="Account Types"/>
		
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
		<div style="width:100%">
			<html:form id="olaCustomerAccountTypeForm" name="olaCustomerAccountTypeForm" commandName="olaCustomerAccountTypeVo" action="p_customeraccounttypeform.html">
				<table border="0" width="70%">
					<tr>
						<td align="right" bgcolor="#f3f3f3" class="formText" width="50%">
							<span class="asterisk">*</span>Account Type Category:
						</td>
						<td bgcolor="#fbfbfb">
							<html:select path="isCategoryCustomerAccountType" cssClass="textBox" tabindex="1" disabled="${not empty param.customerAccountTypeId}">
								<html:option value="">[Select]</html:option>
								<html:option value="0"><%=AccountTypeCategoryEnum.AGENT.getName()%></html:option>
								<html:option value="1"><%=AccountTypeCategoryEnum.CUSTOMER.getName()%></html:option>
							</html:select>
						</td>
					</tr>
					<tr>
						<td align="right" bgcolor="#f3f3f3" class="formText" width="50%">
							<span class="asterisk">*</span>Account Type:
						</td>
						<td bgcolor="#fbfbfb">
							<html:input id="accountType" path="name" cssClass="textBox" maxlength="50" size="30" tabindex="2" onkeypress="return maskAlphaNumericWithSpDtHyphen(this,event);"/>
						</td>
					</tr>
					<tr id="parentAccountTypeRow" style="display:none;">
						<td align="right" bgcolor="#f3f3f3" class="formText" width="50%">
							Parent Account Type:
						</td>
						<td bgcolor="#fbfbfb">
							<html:select path="parentAccountTypeId" cssClass="textBox" tabindex="3" disabled="${not empty param.customerAccountTypeId}">
								<html:option value="">---All---</html:option>
								<c:if test="${accountTypeModelList != null}">
									<html:options items="${accountTypeModelList}" itemValue="customerAccountTypeId" itemLabel="name"/>
								</c:if>
							</html:select>
							<c:if test="${not empty param.customerAccountTypeId}">
								<html:hidden path="parentAccountTypeId"/>
							</c:if>
						</td>
					</tr>
				<tr>
					<td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Oracle #:</td>
					<td align="left" bgcolor="FBFBFB">
						<authz:authorize ifAnyGranted="<%=createPermission%>">
							<html:input id="accountNo" tabindex="4" path="accountNo" cssClass="textBox" onkeypress="return maskInteger(this,event)" maxlength="11" size="30"/>
							<input id="oldAccNum" name="oldAccNum" type="hidden" value="${olaCustomerAccountTypeVo.accountNo}" cssClass="textBox" tabindex="" />
						</authz:authorize>
						 <authz:authorize ifNotGranted="<%=createPermission%>">
							<input id="accountNo" value="${olaCustomerAccountTypeVo.accountNo}" tabindex="5" onkeypress="return maskNumber(this,event)" maxlength="50" disabled="true" />
							<input id="oldAccNum" name="oldAccNum" type="hidden" value="${olaCustomerAccountTypeVo.accountNo}" cssClass="textBox" />
						</authz:authorize>
					</td>
				</tr>
				<tr>
					<td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Account Title:</td>
					<td width="58%" align="left" bgcolor="FBFBFB"><input name="accountNick" id="accountNick" type="text" class="textBox" value="${olaCustomerAccountTypeVo.accountNick}" size="30" maxlength="50" tabindex="6" /></td>
				</tr>
				<tr>
					<td align="right" bgcolor="#f3f3f3" class="formText">
						Active:
					</td>
					<td bgcolor="#fbfbfb">
						<html:checkbox path="active" tabindex="7"/>
					</td>
				</tr>
				
					<tr>
						<td  align="right" bgcolor="F3F3F3" class="formText">Dormant Marking Enabled:</td>
						<td><html:checkbox path="dormantMarkingEnabled" tabindex="8"/></td>

						<td  align="right" bgcolor="F3F3F3" class="formText">Dormant Time Period (Months):</td>
						<td >
							<html:input path="dormantTimePeriod" cssClass="textBox" maxlength="2" tabindex="9" onkeypress="return maskInteger(this,event);"/>
						</td>
					</tr>
				
				</table>
				<h2>&nbsp;&nbsp;Limits</h2>
				<div class="eXtremeTable">
					<table cellpadding="0" cellspacing="0" class="tableRegion" id="tableLimits">
						<thead>
							<tr>
								<td class="tableHeader">Limit Type</td>
								<td class="tableHeader">Apply Limit</td>
								<td class="tableHeader">Debit Limit</td>
								<td class="tableHeader">Credit Limit</td>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${olaCustomerAccountTypeVo.limitTypeVoList}" var="limitTypeVo" varStatus="limitTypeVoStatus">
								<c:set var="rowCssClass" value="odd" scope="page"/>
								<c:if test="${limitTypeVoStatus.count%2!=0}">
								    <c:set var="rowCssClass" value="even" scope="page"/>
								</c:if>
								
								<c:if test="${not empty param.customerAccountTypeId}">
									<html:hidden path="limitTypeVoList[${limitTypeVoStatus.index}].debitLimitId"/>
									<html:hidden path="limitTypeVoList[${limitTypeVoStatus.index}].creditLimitId"/>
								</c:if>
								
								<tr id="row_${limitTypeVo.limitTypeId}" class="${rowCssClass}">
									<td>
										<span>${limitTypeVo.name}</span>
										<html:hidden path="limitTypeVoList[${limitTypeVoStatus.index}].name"/>
									</td>
									<td align="center">
										<html:hidden path="limitTypeVoList[${limitTypeVoStatus.index}].limitTypeId"/>
										<html:checkbox path="limitTypeVoList[${limitTypeVoStatus.index}].applicable"/>
									</td>
									<td width="15%">
										<html:input cssClass="textBox" path="limitTypeVoList[${limitTypeVoStatus.index}].debitLimit" id="debit_limit_${limitTypeVo.limitTypeId}" onclick="checkLimit(this);" onkeypress="return maskInteger(this,event);" maxlength="10" />
									</td>
									<td width="15%">
										<html:input cssClass="textBox" path="limitTypeVoList[${limitTypeVoStatus.index}].creditLimit" onclick="checkLimit(this);" onkeypress="return maskInteger(this,event);" maxlength="10"/>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>

				<table align="center">
					<tr>
						<td align="right" width="50%">
							<authz:authorize ifAnyGranted="<%=createOrUpdatePermission%>">
								<input type="button" value="Save" class="button" id="btnSave"/>
							</authz:authorize>
							<authz:authorize ifNotGranted="<%=createOrUpdatePermission%>">
								<input type="button" value="Save" class="button" disabled="disabled"/>								
							</authz:authorize>
						</td>
						<td>
							<input type="reset" id="cancel" value="Cancel" class="button" onclick="javascript:window.location='p_customeraccounttypemanagement.html?actionId=${retrieveAction}'"/>
						</td>
					</tr>
				</table>
				<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="${param.actionId}">
				<c:if test="${not empty param.customerAccountTypeId}">
	             <input type="hidden" name="isUpdate" id="isUpdate" value="true"/>
	             <input type="hidden" name="customerAccountTypeId" value="${param.customerAccountTypeId}"/>
	             <input type="hidden" name="isCustomerAccountType" value="${param.isCustomerAccountType}"/>
	           </c:if>
			</html:form>
	</div>
	</body>
</html>
