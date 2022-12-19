<!--Author: Muhammad Atif Hussain-->
<%@page import="org.springframework.web.bind.ServletRequestUtils"%>
<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="expires" content="0" />
<meta name="decorator" content="decorator2">
	<meta http-equiv="pragma" content="no-cache" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta name="title" content="Account Opening Form -  Branchless Banking" />
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css" />
	<link href="styles/jquery-ui.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/prototype.js"></script>
	<%@include file="/common/ajax.jsp"%>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.11.0.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.maskedinput.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jquery-ui-custom.min.js"></script>
	<link rel="stylesheet" href="${contextPath}/styles/jquery-ui-custom.min.css" type="text/css">
		<script>
			var jq = $.noConflict();
			jq(document)
			.ready(function($) {
				jq(document).on('click','#acOwnershipDetailTable .addRow',
				function() {
					var row = jq(this).closest('tr');
					
					if(row.find('select').val()==""){
		            	alert("Please select Account Ownership Type before adding new row.");
		              	return false;
		            }
					
					var clone = row.clone();
					var tr = clone.closest('tr');
					tr.find('input[type=text], select').val('');
					tr.find('input[type=text], select').each(function() {this.style.borderColor = "#e6e6e6";});
					tr.find('td:eq(0)').find("input:hidden").val('');
					jq(this).closest('tr').after(clone);
					resetIdAndNameAttributes();
				});

				jq(document).on('click','#acOwnershipDetailTable .removeRow',
				function() {
					if (jq('#acOwnershipDetailTable .addRow').length > 1) {
						jq(this).closest('tr').remove();
						resetIdAndNameAttributes();
					}
				});
				
				jq(function($){
		    		   $("#commencementDate").mask("99/99/9999",{placeholder:"dd/mm/yyyy"});
		    		   $("#idExpiryDate").mask("99/99/9999",{placeholder:"dd/mm/yyyy"});
		    		   $("#dobDate").mask("99/99/9999",{placeholder:"dd/mm/yyyy"});
		    		   $("#incorporationDate").mask("99/99/9999",{placeholder:"dd/mm/yyyy"});
		    		   $("#secpRegDate").mask("99/99/9999",{placeholder:"dd/mm/yyyy"});   
		    		   $("#visaExpiryDate").mask("99/99/9999",{placeholder:"dd/mm/yyyy"});
		    		   $("[id$='dateOfBirth']").mask("99/99/9999",{placeholder:"dd/mm/yyyy"});		   
		    	});

				function resetIdAndNameAttributes()
	    	    {
	    	    	jq('#acOwnershipDetailTable tbody tr').each(function(index)
	    	    	{
    	    			$(this).find(':input:not(:button)').each( function() {
   	    					var name = $(this).attr('name');
   	    					var listName = name.substr(0,name.indexOf("["));
   	    					var propertyName = name.substr(name.indexOf("]")+2);
   	    					name = listName + "[" + index + "]." + propertyName;
   	    					var id = listName + index + "." + propertyName
   	    					$(this).attr('id', id);
   	    					$(this).attr('name', name);
    	    					
   	    					if(name.indexOf("dateOfBirth")>0)
   	    					{
   	    						Calendar.setup(
 	    				    	{
 	    				    		inputField  : id, // id of the input field
 	    				    	    ifFormat    : "%d/%m/%Y",      // the date format
 	    				    	    button      : "ownerDobDate00"+index,    // id of the button
 	    				    	    showsTime   :   false
 	    				    	});
   	    					}
	    				});
	    	    	});
	    	    }
			});
			
	function initProgress(){
    	
		if(document.forms[0].coreAccountNumber.value == ''){
    		alert('Account # is mandatory for Title Fetch');
    	return false;
    	}
    	
    	jq('errorMessages').innerHTML = "";
    	Element.hide('errorMessages');
    	
    	if(document.getElementById('successMessages') != undefined ){
    		Element.hide('successMessages');
    	}
    	
    	return true;
	}
	
	function endProgress(){
	
		if(document.forms[0].coreAccountTitle.value == 'null'){
			document.forms[0].coreAccountTitle.value = '';
			Element.show('errorMessages');
			document.getElementById('errorMessages').innerHTML = document.getElementById('errMsg').value;
			scrollToTop(1000);
		}
		
		if(document.forms[0].errMsg.value == 'null'){
			document.forms[0].errMsg.value = '';
		}
	}
			
		</script>
		<style>
.l3_table {
	font-size: 13px;
	text-align: center;
	background-color: #0292e0;
	padding: 7px;
	color: white;
}
</style>

<%
	String updatePermission = PortalConstants.ADMIN_GP_READ + "," + PortalConstants.PG_GP_READ + "," +PortalConstants.MNG_L3_ACT_UPDATE;
	String createPermission = PortalConstants.ADMIN_GP_READ + "," + PortalConstants.PG_GP_READ + "," +PortalConstants.MNG_L3_ACT_CREATE;
%>
</head>
<body bgcolor="#ffffff">
	<div id="successMsg" class="infoMsg" style="display: none;"></div>
	<spring:bind path="level3AccountModel.*">
		<c:if test="${not empty status.errorMessages}">
			<div class="errorMsg">
				<c:forEach var="error" items="${status.errorMessages}">
					<c:out value="${error}" escapeXml="false" />
					<br />
				</c:forEach>
			</div>
		</c:if>
	</spring:bind>
	<c:if test="${not empty messages}">
		<div class="infoMsg" id="successMessages">
			<c:forEach var="msg" items="${messages}">
				<c:out value="${msg}" escapeXml="false" />
				<br />
			</c:forEach>
		</div>
		<c:remove var="messages" scope="session" />
	</c:if>
	<div class="infoMsg" id="errorMessages" style="display:none;"></div>
	<html:form name="level3AccountOpeningForm" commandName="level3AccountModel" method="POST" onsubmit="return validateForm(this)"
		action="${pageContext.request.contextPath}/p_l3accountopeningform.html">
		<c:set var="accountModelLevel3" scope="session" value="${level3AccountModel}" />
		<input type="hidden" id="retailerContactIdHidden" name="retailerContactIdHidden" value="${level3AccountModel.retailerContactId}">

		<c:if test="${not empty level3AccountModel.retailerContactId}">
			<input type="hidden" name="isUpdate" id="isUpdate" value="true" />
			<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_UPDATE%>" />
		</c:if>
		<c:if test="${empty level3AccountModel.retailerContactId}">
			<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_CREATE%>" />
		</c:if>
		<html:hidden path="appUserId" />
		<html:hidden path="retailerContactId" />
		<html:hidden path="accounttypeName"/>
		<html:hidden path="accountPurpose"/>				
		<html:hidden path="segmentName"/>
		<html:hidden path="regStateName"/>
		<html:hidden path="taxRegimeName"/>
		<html:hidden path="businessAddCityName"/>
		<html:hidden path="businessTypeName"/>
		<html:hidden path="businessNatureName"/>
		<html:hidden path="locationTypeName"/>
		<html:hidden path="locationSizeName"/>
		<html:hidden path="corresspondanceAddCityName"/>
		<html:hidden path="distributorName"/>
		<html:hidden path="acNatureName"/>
		<html:hidden path="applicant1DetailModel.applicantTypeId"  id="applicant1_applicantTypeId"/>
		<html:hidden path="applicant1DetailModel.applicantDetailId"  id="applicant1_applicantDetailId"/>
		<html:hidden path="applicant1DetailModel.versionNo"/>
		<html:hidden path="applicant1DetailModel.titleTxt" id="applicant1_titleTxt"/>
		<html:hidden path="applicant1DetailModel.idTypeName" id="applicant1_idTypeName"/>
		<html:hidden path="applicant1DetailModel.birthPlaceName" id="applicant1_birthPlaceName"/>
		<html:hidden path="applicant1DetailModel.occupationName" id="applicant1_occupationName"/>
		<html:hidden path="applicant1DetailModel.professionName" id="applicant1_professionName"/>
		<html:hidden path="applicant1DetailModel.maritalStatusName" id="applicant1_maritalStatusName"/>
		<html:hidden path="applicant1DetailModel.residentialCityName" id="applicant1_residentialCityName"/>
		<html:hidden path="applicant1DetailModel.buisnessCityName" id="applicant1_buisnessCityName"/>
		<html:hidden path="nokDetailVOModel.nokIdTypeName" id="applicant1_nokIdTypeName"/>
		
		<html:hidden path="createdOn" />
		<html:hidden path="createdBy" />
		
		<input type="hidden" name="actionAuthorizationId" value="${param.authId}" />
        <input type="hidden" name="resubmitRequest" value="${param.isReSubmit}" />
		<html:hidden path="<%=PortalConstants.KEY_USECASE_ID %>" />

		<table width="100%">
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>Initial Application Form
					Number:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
					<div class="textBox" style="background: #D3D3D3; font-size:14px; ">${level3AccountModel.initialAppFormNo}</div>
					<html:hidden path="initialAppFormNo" />
				</td>
				<td height="16" align="right" class="formText" width="25%"></td>
				<td align="left" class="formText" width="25%"></td>
			</tr>
			<tr>
				<td colspan="4">
					<h2>Account Relationship Information</h2>
				</td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>Purpose of Account:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:select path="accountPurposeId" cssClass="textBox">
						<html:option value="">[Select]</html:option>
						<c:if test="${accountPurposeList != null}">
							<html:options items="${accountPurposeList}" itemLabel="name" itemValue="accountPurposeId" />
						</c:if>
					</html:select></td>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>Type of Account:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:select path="acNature" cssClass="textBox">
						<c:if test="${accountTypeList != null}">
							<html:options items="${accountTypeList}" itemLabel="name" itemValue="accountNatureId" />
						</c:if>
					</html:select></td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>Account Title:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input path="acTitle" cssClass="textBox" onkeypress="return maskAlphaNumericWithSp(this,event)" maxlength="100"/></td>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>Currency:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:select path="currencyId" cssClass="textBox">
						<c:if test="${currencyList != null}">
							<html:options items="${currencyList}" itemLabel="name" itemValue="currencyCodeId" />
						</c:if>
					</html:select></td>
			</tr>
			<tr>
				<td align="right" bgcolor="F3F3F3" class="formText"><span style="color: #FF0000">*</span>Tax Regime:</td>
				<td align="left" bgcolor="FBFBFB"><html:select path="taxRegimeId" id="taxRegimeId" cssClass="textBox" >
						<html:option value="">[Select]</html:option>
						<c:if test="${taxRegimeList != null}">
							<html:options items="${taxRegimeList}" itemLabel="name" itemValue="taxRegimeId" />
						</c:if>
					</html:select></td>
				<td align="right" bgcolor="F3F3F3" class="formText"><span style="color: #FF0000">*</span>FED (%age):</td>
				<td align="left" bgcolor="FBFBFB"><html:input path="fed" id="fed" cssClass="textBox" style="background: #D3D3D3; font-size:14px;font-weight: bold;" onfocus="this.blur()" readonly="true" maxlength="5"/></td>
			</tr>
			<tr>
				<td colspan="4">
					<h2>Business Information</h2> 
				</td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>Business Name:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
					<div class="textBox" style="background: #D3D3D3; font-size:14px; height: 75px;">${level3AccountModel.businessName}</div>
					<html:hidden path="businessName" />
				</td>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>Business Address:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:textarea style="height:50px;" path="businessAddress" cssClass="textBox" maxlength="250"/></td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>Business City:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:select path="businessAddCity" cssClass="textBox">
						<html:option value="">[Select]</html:option>
						<c:if test="${cityList != null}">
							<html:options items="${cityList}" itemLabel="name" itemValue="cityId" />
						</c:if>
					</html:select></td>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Business Postal Code:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input path="businessPostalCode" cssClass="textBox" maxlength="5" onkeypress="return maskInteger(this,event)"/></td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>Business Commencement Date:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input path="commencementDate" cssClass="textBox"
						maxlength="50" /> <img id="dateBtn1" name="popcal" align="top" style="cursor:pointer"
					src="${pageContext.request.contextPath}/images/cal.gif" border="0" /> <img name="popcal" title="Clear Date"
					onclick="javascript:$('commencementDate').value=''" align="middle" style="cursor:pointer" border="0"
					src="${pageContext.request.contextPath}/images/refresh.png" /></td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Business Inc. Date:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input path="incorporationDate" cssClass="textBox"
						maxlength="50" /> <img id="dateBtn2" name="popcal" align="top" style="cursor:pointer"
					src="${pageContext.request.contextPath}/images/cal.gif" border="0"  /> <img name="popcal" title="Clear Date"
					onclick="javascript:$('incorporationDate').value=''" align="middle" style="cursor:pointer" border="0"
					src="${pageContext.request.contextPath}/images/refresh.png" /></td>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">SECP Reg. Number:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input path="secpRegNo" cssClass="textBox" maxlength="50" onkeypress="return maskInteger(this,event)"/></td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">SECP Reg. Date:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input path="secpRegDate" cssClass="textBox"
						 maxlength="50" /> <img id="dateBtn3" name="popcal" align="top" style="cursor:pointer"
					src="${pageContext.request.contextPath}/images/cal.gif" border="0"  /> <img name="popcal" title="Clear Date"
					onclick="javascript:$('secpRegDate').value=''" align="middle" style="cursor:pointer" border="0"
					src="${pageContext.request.contextPath}/images/refresh.png" /></td>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">NTN Number:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input path="ntn" cssClass="textBox" maxlength="8" onkeypress="return maskInteger(this,event)"/></td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Sales Tax Number:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input path="salesTaxRegNo" cssClass="textBox" maxlength="50" onkeypress="return maskAlphaNumeric(this,event)"/></td>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Membership # of CC/Trade
					Body:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input path="membershipNoTradeBody" cssClass="textBox" maxlength="50"/></td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Trade Body:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input path="tradeBody" cssClass="textBox" onkeypress="return maskAlphaNumericWithSp(this,event)" maxlength="50"/></td>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>Business Type:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
				
					<html:select multiple="single" path="businessTypeId" id="businessTypeId" cssClass="textBox">
						<html:option value="">[Select]</html:option>
						<c:forEach var="businessType" items="${businessTypeList}" varStatus="businessTypeIndex">
				        	<optgroup label="${businessType.key}">
				           	<html:options items="${businessType.value}" itemLabel="name" itemValue="businessTypeId" />        
				           </optgroup>
				        </c:forEach>        
					</html:select>
				
				</td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>Business Nature:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
					
					<html:select multiple="single" path="businessNatureId" cssClass="textBox">
						<html:option value="">[Select]</html:option>
						<c:forEach var="businessNature" items="${businessNatureList}" varStatus="businessNatureIndex">
				        	<optgroup label="${businessNature.key}">
				           	<html:options items="${businessNature.value}" itemLabel="name" itemValue="businessNatureId" />        
				           </optgroup>
				        </c:forEach>        
					</html:select>
					
					</td>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>Location Type:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:select path="locationTypeId" cssClass="textBox">
						<html:option value="">[Select]</html:option>
						<c:if test="${locationTypeList != null}">
							<html:options items="${locationTypeList}" itemLabel="name" itemValue="locationTypeId" />
						</c:if>
					</html:select></td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>Location Size:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:select path="locationSizeId" cssClass="textBox">
						<html:option value="">[Select]</html:option>
						<c:if test="${locationSizeList != null}">
							<html:options items="${locationSizeList}" itemLabel="name" itemValue="locationSizeId" />
						</c:if>
					</html:select></td>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>Established Since:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:select path="estSince" cssClass="textBox">
						<html:option value="">[Select]</html:option>
						<c:if test="${yearList != null}">
							<html:options items="${yearList}" itemLabel="label" itemValue="value" />
						</c:if>
					</html:select></td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>Corresspondance Address:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:textarea style="height:50px;" path="corresspondanceAddress"
						cssClass="textBox" maxlength="250"/></td>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>Corresspondance City:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:select path="corresspondanceAddCity" cssClass="textBox">
						<html:option value="">[Select]</html:option>
						<c:if test="${cityList != null}">
							<html:options items="${cityList}" itemLabel="name" itemValue="cityId" />
						</c:if>
					</html:select></td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Corresspondance Postal Code:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input path="corresspondancePostalCode" cssClass="textBox" maxlength="5" onkeypress="return maskInteger(this,event)"/></td>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Corresspondance Phone Number:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input id="corresspondance_landLineNo" path="landLineNo" cssClass="textBox" maxlength="11" onkeypress="return maskInteger(this,event)"/></td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Latitude:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input path="latitude" cssClass="textBox" maxlength="20"/></td>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Longitude:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input path="longitude" cssClass="textBox" maxlength="20"/></td>
			</tr>
			<tr>
				<td colspan="4">
					<h2>Personal Information - Applicant 1</h2>
				</td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Title:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:select id="applicant1_title" path="applicant1DetailModel.title"
						cssClass="textBox">
						<html:option value="">[Select]</html:option>
						<c:if test="${titleList != null}">
							<html:options items="${titleList}" itemLabel="label" itemValue="value" />
						</c:if>
					</html:select></td>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Gender:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:radiobuttons id="applicant1_gender" class="applicant1DetailModel_gender"
						 items="${genderList}" path="applicant1DetailModel.gender" itemLabel="label" itemValue="value" /></td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>Applicant Name:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input id="applicant1_name" path="applicant1DetailModel.name"
						cssClass="textBox" onkeypress="return maskAlphaWithSp(this,event)" maxlength="50"/></td>
				<td height="16" align="right" class="formText" width="25%"></td>
				<td align="left" class="formText" width="25%"></td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>ID Document Type:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
					<c:if test="${empty level3AccountModel.retailerContactId}">
						<html:select id="applicant1_idType" path="applicant1DetailModel.idType" cssClass="textBox" onchange="toggleVisaExpiry();">
							<html:option value="">[Select]</html:option>
							<c:if test="${idTypeList != null}">
								<html:options items="${idTypeList}" itemLabel="label" itemValue="value" />
							</c:if>
						</html:select>
					</c:if>
					<c:if test="${not empty level3AccountModel.retailerContactId}">
						<div class="textBox" style="background: #D3D3D3; font-size:14px; ">${level3AccountModel.applicant1DetailModel.idTypeName}</div>
						<html:hidden path="applicant1DetailModel.idType" id="applicant1_idType"/>
					</c:if>
				</td>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>ID Document Number:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
					<c:if test="${empty level3AccountModel.retailerContactId}">
						<html:input id="applicant1_idNumber" path="applicant1DetailModel.idNumber"
							cssClass="textBox" maxLength="13"/>
					</c:if>
					<c:if test="${not empty level3AccountModel.retailerContactId}">
						<div class="textBox" style="background: #D3D3D3; font-size:14px; ">${level3AccountModel.applicant1DetailModel.idNumber}</div>
						<html:hidden path="applicant1DetailModel.idNumber" id="applicant1_idNumber" />
					</c:if>
				</td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>Date of ID Expiry:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input id="idExpiryDate" path="applicant1DetailModel.idExpiryDate"
						cssClass="textBox"  maxlength="50" /> <img id="dateBtn4" name="popcal" align="top"
					style="cursor:pointer" src="${pageContext.request.contextPath}/images/cal.gif" border="0"  /> <img name="popcal"
					title="Clear Date" onclick="javascript:$('idExpiryDate').value=''" align="middle" style="cursor:pointer" border="0"
					src="${pageContext.request.contextPath}/images/refresh.png" /></td>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Date of Visa Expiry:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input id="visaExpiryDate" path="applicant1DetailModel.visaExpiryDate"
						 cssClass="textBox"  maxlength="50" /> <img id="visaExpiryDateBtn" name="popcal" align="top"
					style="cursor:pointer" src="${pageContext.request.contextPath}/images/cal.gif" border="0"  /> <img id="visaExpiryDateBtn2" name="popcal"
					title="Clear Date" onclick="javascript:$('visaExpiryDate').value=''" align="middle" style="cursor:pointer" border="0"
					src="${pageContext.request.contextPath}/images/refresh.png" /></td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>Date of Birth:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input id="dobDate" path="applicant1DetailModel.dob"
						cssClass="textBox"  maxlength="50" /> <img id="dateBtn6" name="popcal" align="top" style="cursor:pointer"
					src="${pageContext.request.contextPath}/images/cal.gif" border="0"  /> <img name="popcal" title="Clear Date"
					onclick="javascript:$('dobDate').value=''" align="middle" style="cursor:pointer" border="0"
					src="${pageContext.request.contextPath}/images/refresh.png" /></td>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Place of Birth:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
					<html:select id="applicant1DetailModel_birthPlace" path="applicant1DetailModel.birthPlace" cssClass="textBox"
									tabindex="35">
						<html:option value="">[Select]</html:option>
						<c:if test="${cityList != null}">
								<html:options items="${cityList}" itemLabel="name" itemValue="cityId" />
	 					</c:if>
					</html:select>
				</td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">NTN Number:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input id="applicant1_ntn" path="applicant1DetailModel.ntn"
						cssClass="textBox" maxlength="8" onkeypress="return maskInteger(this,event)"/></td>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Mother's Maiden Name:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input id="applicant1_motherMaidenName"
						path="applicant1DetailModel.motherMaidenName" cssClass="textBox" onkeypress="return maskAlphaWithSp(this,event)" maxlength="50"/></td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Residential Status:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:radiobuttons id="applicant1_residentialStatus"
						class="applicant1DetailModel_residentialStatus"  items="${residanceStatusList}" itemLabel="label" itemValue="value"
						path="applicant1DetailModel.residentialStatus" /></td>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">US Citizen:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
					<html:radiobuttons id="applicant1_gender" class="applicant1DetailModel_usCitizen"
						 items="${binaryOpt}" path="applicant1DetailModel.usCitizen" itemLabel="label" itemValue="value" />
				</td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Nationality:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input id="applicant1_nationality"
						path="applicant1DetailModel.nationality" cssClass="textBox" maxlength="20" onkeypress="return maskAlpha(this,event)"/></td>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Phone Number:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input id="applicant1_contactNo" path="applicant1DetailModel.contactNo"
						cssClass="textBox" maxlength="11" onkeypress="return maskInteger(this,event)"/></td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Home Number:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input id="applicant1_landLineNo" path="applicant1DetailModel.landLineNo"
						cssClass="textBox" maxlength="11" onkeypress="return maskInteger(this,event)"/></td>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Fax Number:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input id="applicant1_fax" path="applicant1DetailModel.fax"
						cssClass="textBox" maxlength="11" onkeypress="return maskInteger(this,event)"/></td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>Mobile Number:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
					<c:if test="${empty level3AccountModel.retailerContactId}">
						<html:input id="applicant1_mobileNo" path="applicant1DetailModel.mobileNo"
							cssClass="textBox" maxlength="11" onkeypress="return maskInteger(this,event)"/>
					</c:if>
					<c:if test="${not empty level3AccountModel.retailerContactId}">
						<div class="textBox" style="background: #D3D3D3; font-size:14px; ">${level3AccountModel.applicant1DetailModel.mobileNo}</div>
						<html:hidden id="applicant1_mobileNo" path="applicant1DetailModel.mobileNo" />
					</c:if>
				</td>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Email:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input id="applicant1_email" path="applicant1DetailModel.email"
						cssClass="textBox" maxlength="50"/></td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Father's Name/Husband Name:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input id="applicant1_fatherHusbandName"
						path="applicant1DetailModel.fatherHusbandName" cssClass="textBox" onkeypress="return maskAlphaWithSp(this,event)" maxlength="50"/></td>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Name of Employer/Business:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input id="applicant1_employerName"
						path="applicant1DetailModel.employerName" cssClass="textBox" onkeypress="return maskAlphaWithSp(this,event)" maxlength="50"/></td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Occupation:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:select id="applicant1_occupation"
						path="applicant1DetailModel.occupation" cssClass="textBox">
						<html:option value="">[Select]</html:option>
						<c:if test="${occupationList != null}">
							<html:options items="${occupationList}" itemLabel="name" itemValue="occupationId" />
						</c:if>
					</html:select></td>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Profession:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:select id="applicant1_profession"
						path="applicant1DetailModel.profession" cssClass="textBox">
						<html:option value="">[Select]</html:option>
						<c:if test="${professionList != null}">
							<html:options items="${professionList}" itemLabel="name" itemValue="professionId" />
						</c:if>
					</html:select></td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Mailing Address:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:radiobuttons id="applicant1_mailingAddressType"
						class="applicant1DetailModel_mailingAddressType" itemLabel="label" itemValue="value" 
						path="applicant1DetailModel.mailingAddressType" items="${mailingAddTypeList}" /></td>
				<td height="16" align="right" class="formText" width="25%"></td>
				<td align="left" class="formText" width="25%"></td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Residential Address:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:textarea id="applicant1DetailModel_residentialAddress" style="height:50px;"
						path="applicant1DetailModel.residentialAddress" cssClass="textBox" maxlength="250"/></td>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Office/Business Address:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:textarea style="height:50px;"
						id="applicant1DetailModel_buisnessAddress" path="applicant1DetailModel.buisnessAddress" cssClass="textBox" maxlength="250"/></td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Residential City:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:select path="applicant1DetailModel.residentialCity" id="applicant1_residentialCity" cssClass="textBox">
						<html:option value="">[Select]</html:option>
						<c:if test="${cityList != null}">
							<html:options items="${cityList}" itemLabel="name" itemValue="cityId" />
						</c:if>
					</html:select></td>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Office/Business City:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:select path="applicant1DetailModel.buisnessCity" id="applicant1_buisnessCity" cssClass="textBox">
						<html:option value="">[Select]</html:option>
						<c:if test="${cityList != null}">
							<html:options items="${cityList}" itemLabel="name" itemValue="cityId" />
						</c:if>
					</html:select></td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Marital Status:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:select id="applicant1_maritalStatus"
						path="applicant1DetailModel.maritalStatus" cssClass="textBox">
						<html:option value="">[Select]</html:option>
						<c:if test="${maritalStatusList != null}">
							<html:options items="${maritalStatusList}" itemLabel="label" itemValue="value" />
						</c:if>
					</html:select></td>
			</tr>
			<tr>
				<td colspan="4">
					<h2>Next of KIN Information:</h2>
				</td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Name of Next of Kin:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input id="applicant1_nokName" path="nokDetailVOModel.nokName"
						cssClass="textBox" onkeypress="return maskAlphaWithSp(this,event)" maxlength="50"/></td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Relationship with Next of KIN:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input id="applicant1_nokRelationship"
						path="nokDetailVOModel.nokRelationship"  cssClass="textBox" onkeypress="return maskAlphaWithSp(this,event)" /></td>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Telephone/Mobile No. of Next
					of KIN:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input id="applicant1_nokContactNo" path="nokDetailVOModel.nokMobile"
						 cssClass="textBox" maxlength="11" onkeypress="return maskInteger(this,event)" /></td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">ID Document Type:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:select id="applicant1_nokIdType" path="nokDetailVOModel.nokIdType"
						cssClass="textBox">
						<html:option value="">[Select]</html:option>
						<c:if test="${idTypeList != null}">
							<html:options items="${idTypeList}" itemLabel="label" itemValue="value" />
						</c:if>
					</html:select></td>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">ID Document Number:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input id="applicant1_nokIdNumber" path="nokDetailVOModel.nokIdNumber"
						cssClass="textBox" maxLength="13"/></td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Next of Kin' Address:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:textarea id="applicant1_nokMailingAdd" style="height:50px;"
						path="nokDetailVOModel.nokMailingAdd"  cssClass="textBox" maxlength="250"/></td>
				<td height="16" align="right" class="formText" width="25%"></td>
				<td align="left" class="formText" width="25%"></td>
			</tr>
			<tr>
				<td colspan="4" align="right">
					<div class="eXtremeTable">
						<table class="tableRegion" width="100%" id="applicantDetailsTable">
							<thead>
								<tr>
									<td height="35" align="center" class="tableHeader"><b>Business Name</b></td>
									<td height="35" align="center" class="tableHeader"><b>Mobile Number</b></td>
									<td height="35" align="center" class="tableHeader"><b>ID Document Number</b></td>
									<td height="35" align="center" class="tableHeader"><b>ID Document Expiry</b></td>
									<td height="35" align="center" class="tableHeader"><b>Nationality</b></td>
									<td height="35" align="center" class="tableHeader"><b>Action</b></td>
								</tr>
							</thead>
							<c:if test="${level3AccountModel.applicantDetailModelList!=null}">
								<c:forEach varStatus="status" var="applicantDetailModel" items="${level3AccountModel.applicantDetailModelList}">
									<tr id="${status.index+1}">
										<html:hidden path="applicantDetailModelList[${status.index}].applicantDetailId" />
										<html:hidden path="applicantDetailModelList[${status.index}].createdBy" />
										<html:hidden path="applicantDetailModelList[${status.index}].versionNo" />
										<html:hidden path="applicantDetailModelList[${status.index}].index" value="${status.index+1}"/>
										<td align="center">${applicantDetailModel.name}</td>
										<td align="center">${applicantDetailModel.mobileNo}</td>
										<td align="center">${applicantDetailModel.idNumber}</td>
										<td align="center"><fmt:formatDate pattern="dd-MM-yyyy" value="${applicantDetailModel.idExpiryDate}" /></td>
										<td align="center">${applicantDetailModel.nationality}</td>
										<td align="center"><img id="editBtn" name="editBtn" align="top" style="cursor:pointer"
											src="${pageContext.request.contextPath}/images/buttons/edit.gif" border="0"
											onclick="popupwindow('p_l3_applicantformpopup.html?idNumber=${applicantDetailModel.idNumber}&rowId=${applicantDetailModel.idNumber}&isEdit=true&index=${status.index+1}', 'Edit Applicant', 800,800);" />&nbsp;</td>
									</tr>
								</c:forEach>
							</c:if>
						</table>
						<table>
							<tr>
								<td colspan="6" align="right"><input onclick="popupwindow('p_l3_applicantformpopup.html', 'Add/Edit Applicant', 800,800);" type="button"
									value="Add Applicant" class="button" /></td>
							</tr>
						</table>
					</div>
				</td>
			</tr>
			<tr>
				<td colspan="4">
					<h2>Additional Information for Corporate/Business Accounts</h2>
				</td>
			</tr>
			<tr>
				<td colspan="4" align="right">
					<div class="eXtremeTable">
						<table width="100%" class="tableRegion" id="acOwnershipDetailTable">
							<thead>
								<tr>
									<!-- <td height="35" align="center"  class="tableHeader"><b></b>Sr. No.</b></td> -->
									<td height="35" align="center" class="tableHeader"><b>Type of Ownership</b></td>
									<td height="35" align="center" class="tableHeader">Name</b></td>
									<td height="35" align="center" class="tableHeader"><b>ID Type</b></td>
									<td height="35" align="center" class="tableHeader"><b>Number</b></td>
									<td height="35" align="center" class="tableHeader"><b>DOB</b></td>
									<td height="35" align="center" class="tableHeader"><b>OFAC?</b></td>
									<td height="35" align="center" class="tableHeader"><b>PEP?</b></td>
									<td height="35" align="center" class="tableHeader"><b>VeriSys</b></td>
									<td height="35" align="center" class="tableHeader"><b>Action</b></td>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${level3AccountModel.acOwnershipDetailModelList}" var="acOwnershipDetailModel" varStatus="iterationStatus">
									<tr>
										<td align="center"><html:hidden path="acOwnershipDetailModelList[${iterationStatus.index}].acOwnershipDetailId" /> <html:hidden
												path="acOwnershipDetailModelList[${iterationStatus.index}].createdBy" /> <html:hidden
												path="acOwnershipDetailModelList[${iterationStatus.index}].createdOn" /> <html:hidden
												path="acOwnershipDetailModelList[${iterationStatus.index}].updatedBy" /> <html:hidden
												path="acOwnershipDetailModelList[${iterationStatus.index}].updatedOn" /> <html:hidden
												path="acOwnershipDetailModelList[${iterationStatus.index}].isDeleted" /> <html:hidden
												path="acOwnershipDetailModelList[${iterationStatus.index}].versionNo" /> <html:select cssStyle="max-width:100px;"
												path="acOwnershipDetailModelList[${iterationStatus.index}].acOwnershipTypeId" cssClass="textBox">
												<html:option value="">--All--</html:option>
												<html:options items="${ownerShipTypeList}" itemLabel="label" itemValue="value" />
											</html:select></td>
										<td align="center"><html:input cssStyle="max-width:130px;" path="acOwnershipDetailModelList[${iterationStatus.index}].name"
												cssClass="textBox" maxlength="50" onkeypress="return maskAlphaWithSp(this,event)" /></td>
										<td align="center"><html:select cssStyle="max-width:100px;" path="acOwnershipDetailModelList[${iterationStatus.index}].idDocumentType"
												cssClass="textBox">
												<html:option value="">[Select]</html:option>
												<html:options items="${documentTypeList}" itemLabel="label" itemValue="value" />
											</html:select></td>
										<td align="center"><html:input cssStyle="max-width:130px;" path="acOwnershipDetailModelList[${iterationStatus.index}].idDocumentNo"
												cssClass="textBox" maxlength="13"/></td>
										<td align="center"><html:input path="acOwnershipDetailModelList[${iterationStatus.index}].dateOfBirth" 
												cssClass="textBox"  maxlength="50" /></td>
										<td align="center"><html:checkbox path="acOwnershipDetailModelList[${iterationStatus.index}].ofac"  /></td>
										
										<td align="center">
											<html:select cssStyle="max-width:80px;" path="acOwnershipDetailModelList[${iterationStatus.index}].pep" cssClass="textBox">
												<c:if test="${binaryOpt != null}">
													<html:options items="${binaryOpt}" itemLabel="label" itemValue="value" />
						 						</c:if>
											</html:select>
										</td>
										
										
										<td align="center"><html:checkbox path="acOwnershipDetailModelList[${iterationStatus.index}].verisysDone"  /></td>
										<td><input type="button" id="acOwnershipDetailModelList${iterationStatus.index}.add" class="addRow" value="+"
											style="font-weight:bold;padding: 0px 12px;height: 29px;font-size:20px;" title="Add row" /> <input type="button"
											id="acOwnershipDetailModelList${iterationStatus.index}.remove" class="removeRow" value="-" title="Remove row"
											style="background-color:#E40606 !important;color:#FFFCFC !important;font-weight:bold;padding: 0px 12px;height: 29px;font-size:20px;" /></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</td>
			</tr>
			<tr>
				<td colspan="4">
					<h2>Relationship Details</h2>
				</td>
			</tr>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>Emp ID:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input id="employeeID" path="employeeID" cssClass="textBox"
						readonly="true" style="background: #D3D3D3; font-size:14px;font-weight: bold;"/></td>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>Name of Employee:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input id="employeeName" path="employeeName" cssClass="textBox"
						readonly="true" onkeypress="return maskAlphaNumericWithSp(this,event)" style="background: #D3D3D3; font-size:14px;font-weight: bold;"/></td>
			</tr>
			<c:if test="${not level3AccountModel.accountLinked and not empty level3AccountModel.retailerContactId}">
				<tr>
					<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Core Banking Account No.:</td>
					<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
						<html:input id="coreAccountNumber" path="coreAccountNumber" cssClass="textBox" onkeypress="return maskInteger(this,event)"/>
						<input id="fetchButton" name="fetchButton" type="button" value="Fetch Title" class="button"	 />
					</td>
					<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Account Title:</td>
					<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input id="coreAccountTitle" path="coreAccountTitle" cssClass="textBox" readonly="true" style="background: #D3D3D3; font-size:14px;font-weight: bold;" onkeypress="return maskAlphaNumericWithSp(this,event)"/></td>
				</tr>
			</c:if>
			<c:if test="${level3AccountModel.accountLinked and not empty level3AccountModel.retailerContactId}">
				<tr>
					<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Core Banking Account No.:</td>
					<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
						<div class="textBox" style="background: #D3D3D3; font-size:14px; ">${level3AccountModel.coreAccountNumber}</div>
						<html:hidden id="coreAccountNumber" path="coreAccountNumber" />
					</td>
					<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Account Title:</td>
					<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
						<div class="textBox" style="background: #D3D3D3; font-size:14px; ">${level3AccountModel.coreAccountTitle}</div>
						<html:hidden id="coreAccountTitle" path="coreAccountTitle" />
					</td>
				</tr>
			</c:if>
			<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>Date of Account Opening:</td>
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
					<html:input id="accountOpeningDate" path="accountOpeningDate" readonly="true"
						cssClass="textBox"  maxlength="50" style="background: #D3D3D3; font-size:14px; font-weight: bold;"/>
				</td>
				<td height="16" align="right" class="formText" width="25%">BVS Enable:</td>
				<td align="left" class="formText" width="25%">
				<html:checkbox path="bvsEnable"/>
				</td>
			</tr>
			
						<tr>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText"
					width="25%">
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
				</td>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText"
					width="25%">
					Filer Marking:
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
					<html:radiobuttons tabindex="39" items="${filerList}" itemLabel="label" itemValue="value" path="filer" />
				</td>
				
			</tr>
			
			<tr id="screeningRow">
				<td height="16" align="right" bgcolor="F3F3F3" class="formText"
					width="25%">
					<span id="screeningMandatory" style="color: #FF0000">*</span>Screening:
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
					<html:radiobuttons  class="screening" items="${screeningList}" itemLabel="label" itemValue="value" path="applicant1DetailModel.screeningPerformed" />
				</td>
				<td height="16" align="right" bgcolor="F3F3F3" class="formText"
					width="25%">
					<span style="color: #FF0000">*</span>NADRA Verification:
				<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
					<html:radiobuttons class="verisys" tabindex="27" items="${nadraVerList}" itemLabel="label" itemValue="value" path="verisysDone" />
				</td>
			</tr>
			<c:if test="${not empty level3AccountModel.retailerContactId}">
				<tr>
					<td colspan="4">
						<h2>Registeration Status</h2>
					</td>
				</tr>
				<c:if test="${not empty level3AccountModel.retailerContactId }">
					<tr>
						<td align="right" bgcolor="F3F3F3" class="formText"><span style="color: #FF0000">*</span>Registration State:</td>
						<td align="left" bgcolor="FBFBFB" class="formText">
							<c:choose>
								<c:when test="${level3AccountModel.registrationStateId==3}">
									<div class="textBox" style="background: #D3D3D3; font-size:14px; ">Approved</div>
									<html:hidden path="registrationStateId" />
								</c:when>
								<c:when test="${level3AccountModel.registrationStateId==5}">
									<div class="textBox" style="background: #D3D3D3; font-size:14px; ">Decline</div>
									<html:hidden path="registrationStateId" />
								</c:when>
								<c:when test="${level3AccountModel.registrationStateId==6}">
									<div class="textBox" style="background: #D3D3D3; font-size:14px; ">Rejected</div>
									<html:hidden path="registrationStateId" />
								</c:when>
								<c:otherwise>
									<html:select path="registrationStateId" cssClass="textBox" id="registrationStateId" tabindex="18" onchange="regStateChange(this)">
										<html:option value="">[Select]</html:option>
										<c:if test="${regStateList != null}">
											<html:options items="${regStateList}" itemLabel="name" itemValue="registrationStateId" />
										</c:if>
									</html:select>
								</c:otherwise>
							</c:choose>
								</td>
						<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Comments:</td>
						<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:textarea style="height:50px;" path="regStateComments" tabindex="1" cssClass="textBox" /></td>
					</tr>
				</c:if>
				<tr>
					<td colspan="2" align="right">
					
						<authz:authorize ifAnyGranted="<%=updatePermission%>">
							<input type="submit" value="Update Agent" />
						</authz:authorize>
						<authz:authorize ifNotGranted="<%=updatePermission%>">
							<input type="submit" value="Update Agent" disabled="disabled" class="button"/>
						</authz:authorize>
					</td>
					<td>
						<input type="reset" class="button" tabindex="11"  value="Cancel"
						onclick="javascript:window.location='p_l3accountsmanagement.html'"/>
					</td>
						
				
				</tr>
			</c:if>
			<c:if test="${empty level3AccountModel.retailerContactId}">
				<tr>
					<td colspan="4" align="center">
					<authz:authorize ifAnyGranted="<%=createPermission%>">
						<input type="submit" value="Create Agent" />
					</authz:authorize>
					<authz:authorize ifNotGranted="<%=createPermission %>">
						<input type="submit" value="Create Agent" disabled="disabled"/>						
					</authz:authorize>			
					</td>
				</tr>
			</c:if>
		</table>
	</html:form>
	<ajax:updateField parser="new ResponseXmlParser()" source="taxRegimeId" eventType="change" action="taxRegimeId" target="fed" baseUrl="${contextPath}/p_loadfed.html" parameters="taxRegimeId={taxRegimeId}" ></ajax:updateField>
	<input type="hidden" id="errMsg"/>
	<ajax:updateField parser="new ResponseXmlParser()" source="coreAccountNumber" action="fetchButton" target="coreAccountTitle,errMsg" baseUrl="${contextPath}/fetchtitledata.html" parameters="accountNo={coreAccountNumber},mobileNumber={applicant1_mobileNo}" preFunction="initProgress" postFunction="endProgress" ></ajax:updateField>
	<script type="text/javascript">
		function popupwindow(url, title, w, h) {
			var left = (screen.width / 2) - (w / 2);
			var top = (screen.height / 2) - (h / 2);
			return window
					.open(
							url,
							title,
							'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes, copyhistory=no, width='
									+ w
									+ ', height='
									+ h
									+ ', top='
									+ top
									+ ', left=' + left);
		}

		function doRequired(field, label) {
			if (field.value == '' || field.value.trim('').length == 0) {
				alert(label + ' is required field.');
				field.focus();
				return false;
			}
			return true;
		}

		function validateForm(theForm) {
				
	     		if(document.getElementById('accountPurposeId'))
	     			theForm.accountPurpose.value = theForm.accountPurposeId.options[theForm.accountPurposeId.selectedIndex].innerHTML;
	     		if(document.getElementById('taxRegimeId'))
	     			theForm.taxRegimeName.value = theForm.taxRegimeId.options[theForm.taxRegimeId.selectedIndex].innerHTML;
	     		if(document.getElementById('segmentId'))
	     			theForm.segmentName.value = theForm.segmentId.options[theForm.segmentId.selectedIndex].innerHTML;
	     		if(document.getElementById('businessAddCity'))
	     			theForm.businessAddCityName.value = theForm.businessAddCity.options[theForm.businessAddCity.selectedIndex].innerHTML;	
	     		if(document.getElementById('businessTypeId'))
	     			theForm.businessTypeName.value = theForm.businessTypeId.options[theForm.businessTypeId.selectedIndex].innerHTML;	
	     		if(document.getElementById('businessNatureId'))
	     			theForm.businessNatureName.value = theForm.businessNatureId.options[theForm.businessNatureId.selectedIndex].innerHTML;
	     		if(document.getElementById('locationTypeId'))
	     			theForm.locationTypeName.value = theForm.locationTypeId.options[theForm.locationTypeId.selectedIndex].innerHTML;	
	     		if(document.getElementById('locationSizeId'))
	     			theForm.locationSizeName.value = theForm.locationSizeId.options[theForm.locationSizeId.selectedIndex].innerHTML;
	     		if(document.getElementById('corresspondanceAddCity'))
	     			theForm.corresspondanceAddCityName.value = theForm.corresspondanceAddCity.options[theForm.corresspondanceAddCity.selectedIndex].innerHTML;
	     		if(document.getElementById('acNature'))
	     			theForm.acNatureName.value = theForm.acNature.options[theForm.acNature.selectedIndex].innerHTML;
	     		if(document.getElementById('applicant1_title').value!="")
	     			theForm.applicant1_titleTxt.value = theForm.applicant1_title.options[theForm.applicant1_title.selectedIndex].innerHTML;
	     		
	     		if(document.getElementById('applicant1_idType').type!='hidden'){
					if(document.getElementById('applicant1_idType'))
	     				theForm.applicant1_idTypeName.value = theForm.applicant1_idType.options[theForm.applicant1_idType.selectedIndex].innerHTML;
				}
	     		
	     		if(document.getElementById('applicant1DetailModel_birthPlace').value!="")
	     			theForm.applicant1_birthPlaceName.value = theForm.applicant1DetailModel_birthPlace.options[theForm.applicant1DetailModel_birthPlace.selectedIndex].innerHTML;
	     		if(document.getElementById('applicant1_occupation').value!="")
	     			theForm.applicant1_occupationName.value = theForm.applicant1_occupation.options[theForm.applicant1_occupation.selectedIndex].innerHTML;
	     		
	     		if(document.getElementById('applicant1_profession').value!="")
	     			theForm.applicant1_professionName.value = theForm.applicant1_profession.options[theForm.applicant1_profession.selectedIndex].innerHTML;
	     			
	     		if(document.getElementById('applicant1_maritalStatus').value!="")
	     			theForm.applicant1_maritalStatusName.value = theForm.applicant1_maritalStatus.options[theForm.applicant1_maritalStatus.selectedIndex].innerHTML;
	     		
	     		if(document.getElementById('applicant1_residentialCity').value!="")
	     			theForm.applicant1_residentialCityName.value = theForm.applicant1_residentialCity.options[theForm.applicant1_residentialCity.selectedIndex].innerHTML;
	     			
	     		if(document.getElementById('applicant1_buisnessCity').value!="")
	     			theForm.applicant1_buisnessCityName.value = theForm.applicant1_buisnessCity.options[theForm.applicant1_buisnessCity.selectedIndex].innerHTML;
	     			
	     		if(document.getElementById('applicant1_nokIdType').value!="")
	     			theForm.applicant1_nokIdTypeName.value = theForm.applicant1_nokIdType.options[theForm.applicant1_nokIdType.selectedIndex].innerHTML;	
			
				var appUserId = document.getElementById("appUserId").value;
			
				if(appUserId!="null" && appUserId!=''){
				     if(document.getElementById('registrationStateId').value=="3" ){
				     	theForm.regStateName.value = 'Approved';
				     }
				     else if(document.getElementById('registrationStateId').value=="4"){
				     	theForm.regStateName.value = 'Discrepent'; 
				     }
				     else if(document.getElementById('registrationStateId').value=="5" ){
				     	theForm.regStateName.value = 'Decline';	
				     }
				     else if(document.getElementById('registrationStateId').value=="6" ){
				     	theForm.regStateName.value = 'Rejected';
				     }	
				     else{
		     			theForm.regStateName.value = theForm.registrationStateId.options[theForm.registrationStateId.selectedIndex].innerHTML;
		     		}
		       }else{
		       		theForm.regStateName.value = 'Request Received'; 
		       }
				
				var checkRow = checkRowData();
	
				if(!checkRow){
    		    	return false;
				} 
					
				var dateCheck=true;
          		jq('#acOwnershipDetailTable tbody tr').each( function() {
	        		var selectArray = jq(this).find('input');
	        		if(selectArray[9].value.indexOf("/")>0)
	        		{
	         		
	         			selectArray[9].style.borderColor="";
	         			
	         			if( isAgeLessThan18Years(selectArray[9].value))
	         			{
	          				alert('The person in additional information section cannot be less than 18 years');
							dateCheck = false;
					        return false;
	         			}
	         			else if(!isValidDate(selectArray[9].value,"in additional information section")){
    						dateCheck = false;
    			   			return false;
    			   		}
	        		}
        		});
          
	          if(!dateCheck){
	           return false;
	          }
          
          		//check the ID Doc type and value for ACOwnershipDetail section also
		        var idtypeCheck = true;
		        jq('#acOwnershipDetailTable tbody tr').each( function() {
			        var selectArray1 = jq(this).find('select');
			        var field = selectArray1[1];
			        var selectArray2 = jq(this).find('input');
			        var idNumber = selectArray2[8];
			        var fieldValue1 = selectArray1[1].options[selectArray1[1].selectedIndex].text;
			       
			        if(!checkDocumentIDLength(field,fieldValue1,idNumber)) {
			        	idtypeCheck = false;
			         	return idtypeCheck;
			        }
		        });
          
	          if(!idtypeCheck){
	           return false;
	          }
			
				
				if (doRequired(theForm.initialAppFormNo,
					'Initial Application Form Number')
					&& doRequired(theForm.accountPurposeId,'Purpose of account')
					&& doRequired(theForm.acNature, 'Type of Account')
					&& doRequired(theForm.acTitle, 'Account Title')
					&& doRequired(theForm.currencyId, 'Currency')
					&& doRequired(theForm.taxRegimeId, 'Tax Regime')
					&& doRequired(theForm.fed, 'FED')
					&& isValidFED()
					&& doRequired(theForm.businessName, 'Business Name')
					&& doRequired(theForm.businessAddress, 'Business Address')
					&& doRequired(theForm.businessAddCity, 'Business City')
					&& doRequired(theForm.commencementDate,'Business Commencement Date')
					&& isValidBusinessCommencementDate()
					//&& doRequired(theForm.incorporationDate,'Business Inc.Date')
					&& isValidBusinessIncDate()
					//&& doRequired(theForm.secpRegNo, 'SECP Reg. Number')
					//&& doRequired(theForm.secpRegDate, 'SECP Reg. Date')
					&& isValidSECPRegDate()
					//&& doRequired(theForm.ntn, 'NTN Number')
					&& isValidMinLength(theForm.ntn, 'NTN Number', 8)
					//&& doRequired(theForm.salesTaxRegNo, 'Sales Tax Number')
					//&& doRequired(theForm.membershipNoTradeBody,'Membership # of CC/Trade Body')
					//&& doRequired(theForm.tradeBody, 'Trade Body')
					&& doRequired(theForm.businessTypeId, 'Business Type')
					&& doRequired(theForm.businessNatureId, 'Business Nature')
					&& doRequired(theForm.locationTypeId, 'Location Type')
					&& doRequired(theForm.locationSizeId, 'Location Size')
					&& doRequired(theForm.estSince, 'Established Since')
					&& doRequired(theForm.corresspondanceAddress,'Corresspondance Address')
					&& doRequired(theForm.corresspondanceAddCity,'Corresspondance City')
					&& isValidMinLength(theForm.corresspondance_landLineNo, 'Corresspondance Phone Number', 11)
					

					//Applicant 1 

	//				&& doRequired(theForm.applicant1_title, 'Applicant 1 Title')
	//				&& doRequiredGroup('applicant1DetailModel_gender', 'Applicant 1 Applicant Gender' )
					&& doRequired(theForm.applicant1_name, 'Applicant 1 Name')
					&& validateAlphaWithSp(theForm.applicant1_name, 'Applicant 1 Name')
					&& doRequired(theForm.applicant1_idType, 'Applicant 1 ID Document Type')
					&& doRequired(theForm.applicant1_idNumber,'Applicant 1 ID Document Number')
					&& doRequired(theForm.idExpiryDate, 'Applicant 1 Date of ID Expiry')
					&& isValidApplicant1IdExpiry()
					&& isValidApplicant1VisaExpiry()
					&& isValidApplicant1DOB()
					&& doRequired(theForm.dobDate, 'Applicant 1 Date of Birth')
	//				&& doRequired(theForm.applicant1DetailModel_birthPlace,'Applicant 1 Place of Birth')
	//				&& doRequired(theForm.applicant1_ntn, 'Applicant 1 NTN Number')
                   
                    && IsNumericData(theForm.applicant1_ntn.value, 'Applicant 1 NTN Number')
                    && isValidMinLength(theForm.applicant1_ntn, 'Applicant 1 NTN Number', 8)
	//				&& doRequired(theForm.applicant1_motherMaidenName,'Applicant 1 Mother\'s Maiden Name')
					&& validateAlphaWithSp(theForm.applicant1_motherMaidenName,'Applicant 1 Mother\'s Maiden Name')

	//				&& doRequiredGroup('applicant1DetailModel_residentialStatus', 'Applicant 1 Residential Status' )
	//				&& doRequiredGroup('applicant1DetailModel_usCitizen', 'Applicant 1 US Citizen' )
	//				&& doRequired(theForm.applicant1_nationality, 'Applicant 1 Nationality')
					&& validateAlphaWithSp(theForm.applicant1_nationality, 'Applicant 1 Nationality')
					&& isValidMinLength(theForm.applicant1_contactNo, 'Phone Number', 11)
					&& IsNumericData(theForm.applicant1_contactNo.value, 'Phone Number')
					&& isValidMinLength(theForm.applicant1_landLineNo, 'Home Number', 11)
					&& IsNumericData(theForm.applicant1_landLineNo.value, 'Home Number')
					&& isValidMinLength(theForm.applicant1_fax, 'Fax Number', 11)
					&& IsNumericData(theForm.applicant1_fax.value, 'Fax Number')
	//				&& doRequiredGroup('applicant1DetailModel_mailingAddressType', 'Applicant 1 Mailing Address' )
					&& validateRequiredMailingAddress('applicant1DetailModel_mailingAddressType')
					&& doRequired(theForm.applicant1_mobileNo, 'Applicant 1 Mobile Number')
					&& IsNumericData(theForm.applicant1_mobileNo.value, 'Mobile No.')
					&& isValidMobileNo( theForm.applicant1_mobileNo)
					&& isValidMinLength(theForm.applicant1_mobileNo, 'Mobile No.', 11)
					&& isValidEmail(theForm.applicant1_email, 'Applicant 1 Email')
	//				&& doRequired(theForm.applicant1_fatherHusbandName,'Applicant 1 Father\'s Name/Husband Name')
					&& validateAlphaWithSp(theForm.applicant1_fatherHusbandName,'Applicant 1 Father\'s Name/Husband Name')
	
	//				&& doRequired(theForm.applicant1_employerName,'Applicant 1 Name of Employer/Business')
					&& validateAlphaWithSp(theForm.applicant1_employerName,'Applicant 1 Name of Employer/Business')

	//				&& doRequired(theForm.applicant1_occupation, 'Applicant 1 Occupation')
	//				&& doRequired(theForm.applicant1_profession, 'Applicant 1 Profession')
	//				&& doRequired(theForm.applicant1_maritalStatus,'Applicant 1 Marital Status')
	//				&& doRequired(theForm.applicant1_nokName, 'Name of Next of KIN')
	//				&& doRequired(theForm.applicant1_nokRelationship,'Relationship with Next of KIN')
	//				&& doRequired(theForm.applicant1_nokContactNo,'Telephone/Mobile No. of Next of KIN')
					&& isValidMinLength(theForm.applicant1_nokContactNo, 'Telephone/Mobile No. of Next of KIN', 11)
	//				&& doRequired(theForm.applicant1_nokIdType,'Next of KIN ID Document Type')
	//				&& doRequired(theForm.applicant1_nokIdNumber,'Next of KIN ID Document Number')
	//				&& doRequired(theForm.applicant1_nokMailingAdd,'Next of Kin Address')
					&& doRequired(theForm.employeeID, 'Emp ID')
					&& doRequired(theForm.employeeName, 'Name of Employee')
					&& doRequired(theForm.accountOpeningDate,'Date of Account Opening')) 
				{
				
					if(document.getElementById('retailerContactId')== undefined || document.getElementById('retailerContactId')==''
					|| document.getElementById('retailerContactId').value == 0){
						//for main appliant
		    			var applicant1DetailModel_idType = document.getElementById('applicant1_idType');
		    			var fieldValue = applicant1DetailModel_idType.options[applicant1DetailModel_idType.selectedIndex].text;
		    			var idNumber = document.getElementById('applicant1_idNumber');
						if(!checkDocumentIDLength(applicant1DetailModel_idType,fieldValue,idNumber)) {
							return false;
						}
					}
					
					//for next of KIN
					var nok_idType = document.getElementById('applicant1_nokIdType');
					var nok_idNumber = document.getElementById('applicant1_nokIdNumber');
					var nok_fieldValue = theForm.applicant1_nokIdType.options[theForm.applicant1_nokIdType.selectedIndex].text;
					
					if(nok_fieldValue!=undefined && nok_fieldValue!='[Select]' && (nok_idNumber.value==undefined || nok_idNumber.value==''))
					{
						alert("Must provide NOK ID Document Number for selected NOK ID Document Type");
						document.getElementById("applicant1_nokIdNumber").focus();
						return false;
					}
					
					if(nok_idNumber.value!=undefined && nok_idNumber.value!='' && (nok_fieldValue==undefined || nok_fieldValue=='[Select]'))
					{
						alert("NOK ID Document Type is not provided");
						document.getElementById("applicant1_nokIdType").focus();
						return false;
					}
					
					if(!checkDocumentIDLength(nok_idType,nok_fieldValue,nok_idNumber)) {
						return false;
					}
				
					if(theForm.coreAccountNumber!=undefined)
			    	{
			    		if(trim(theForm.coreAccountNumber.value)!='' && trim(theForm.coreAccountTitle.value)==''){
			    			alert("Core banking account information is incomplete.");
			    			theForm.coreAccountNumber.focus();
			    			return false;
		    			}
			    	}
			    	if(!validateIdNumbers())
			    	{
			    		return false;
			    	}
			    	
			    	if(!validateFormChar(theForm)){
			    		return false;
			    	}
			    	
			    	if(theForm.registrationStateId!=undefined)	
		     		{
		     			if(!doRequired(theForm.registrationStateId, 'Registration State')){
		     				return false;
		     			}
		       		}
			    	
			    	
					if (confirm('Are you sure you want to proceed?')==true){
			        	return true;
			        }
			        else{
			        	return false;
			        }
			} else {
				return false;
			}
		}
		
	
	function checkRowData(){
		
	   var validData = true;     
	    jq('#acOwnershipDetailTable tbody tr').each( function() {
	        var selectArray = jq(this).find('select');
	        var inputArray = jq(this).find('input');
	        
	        if(selectArray[0].value ||selectArray[1].value||(selectArray[2].value==1) ||inputArray[7].value ||inputArray[8].value ||inputArray[9].value==1){
	        	
		        if((selectArray[0].value=="") ||(selectArray[1].value=="") ||(selectArray[2].value=="") ||(inputArray[7].value=="") ||(inputArray[8].value=="") ||(inputArray[9].value=="")){
		        	alert("Please fill all the mandatory fields in Account Ownership Detail Section.");
		        	validData = false;
		        }
	        } 
	    			
	    });
	  return validData;  
	}	
		
	function isValidMobileNo(field){
		  if(field.type == "hidden"){
    		  return true;
    	  }
    	  if( field.value != '' )
        	{
        		var mobileNumber = field.value;
        		if(mobileNumber.charAt(0)!="0" || mobileNumber.charAt(1)!="3"){
        			alert("Mobile Number should start from 03XXXXXXXXX");
        			field.focus();
        			return false;
        		}
        	}
        	return true;
      }
		
		function checkDocumentIDLength(applicant1DetailModel_idType, fieldValue, idNumber) {

			var errorMessages = document.getElementById('successMsg');

			var size = 0;
			var AN = false;
			
			if(fieldValue == 'CNIC') {
				size = 13;
			} else if(fieldValue == 'NICOP') {
				size = 13;
			} else if(fieldValue == 'Passport') {
				size = 9;
				AN = true;
			} else if(fieldValue == 'NARA') {
				size = 13;
			} else if(fieldValue == 'POC') {
				size = 13;
			} else if(fieldValue == 'SNIC') {
				size = 13;
			} 
			
			if(applicant1DetailModel_idType.selectedIndex > 0) {

				return validateIDDocumentType(idNumber, fieldValue,size,AN);
			}
			
			return true;
		}
		
		function validateIDDocumentType(field, fname, size, AN){

		    if(field.value==""){
		        return false; 
		    }
		    
		    var re = null;
		    
		    if(!AN) {
		    	re = "[^0-9]";
		    } else {
		    	re = "[^A-Za-z0-9]";		    	
		    }
		    
		    var flag = true;
		    
            for(var i=0; i<field.value.length;i++){
                var charpos = field.value[i].search(re);
                if(charpos >= 0){
                   flag=false;
					break;       
                }
            }  		    
            
		    if(!flag & AN) { 
				alert(fname + ' ID Document Number contains invalid character.');
				field.focus();
		        return false; 
		    } else if(!flag & !AN) { 
				alert(fname + ' ID Document Number must be numeric.');
				field.focus();
		        return false; 
		    }
			
		    if(field.value.length != size) {
		    	alert(fname + ' ID Document Number length must be '+size);
				field.focus();
		        return (false);	
		    }  else {
		        return (true);
		    }
		}		
		
		function validateRequiredMailingAddress(className)
		{
	      	var elements = document.getElementsByClassName(className);
			
			if(elements[0].checked  == true){
				if(trim(document.getElementById("applicant1DetailModel_residentialAddress").value)== ''){
					alert("Residential Address is required field.");
					elements[0].focus();
					return false;
				}
			}
			else if(elements[1].checked  == true){
				if(trim(document.getElementById("applicant1DetailModel_buisnessAddress").value)== ''){
					alert("Office/Business Address is required field.");
					elements[1].focus();
					return false;
				}
			}
			return true;
		}
		
		function regStateChange(sel)
	    {
			if(document.getElementById('screeningRow')!=undefined)
			{	
				document.getElementById('screeningRow').style.display = '';
				document.getElementById('screeningPerformed1').disabled = false ;
				document.getElementById('screeningMandatory').style.display = '';
				
				if(sel.value==2)//in recieved case
				{
					 document.getElementById('screeningRow').style.display = 'none';
					 document.getElementById('screeningPerformed1').disabled = true ; 
				}
				else if(sel.value==3){
					document.getElementById('screeningMandatory').style.display = '';
					document.getElementById('screeningPerformed1').disabled = false ;
				}
				if(sel.value == 4 ||sel.value == 5 || sel.value == 6){
					
					document.getElementById('screeningMandatory').style.display = 'none';
					document.getElementById('screeningPerformed1').disabled = true ;
				}
			}
		}
		
		function validateIdNumbers()
		{
			if(document.forms[0].applicant1_idType.value==document.forms[0].applicant1_nokIdType.value)
			{
				if(document.forms[0].applicant1_idNumber.value==document.forms[0].applicant1_nokIdNumber.value)
				{
					alert("Applicant 1 ID Document details cannot be same as Applicant 1 NOK ID Document details.");
					return false;
				}
			}
			return true;
		}
		
		jq("#businessAddress").keyup(function(){
			    if(jq(this).val().length > 250){
			        jq(this).val(jq(this).val().substr(0, 249));
		}});
		
		jq("#corresspondanceAddress").keyup(function(){
			    if(jq(this).val().length > 250){
			        jq(this).val(jq(this).val().substr(0, 249));
		}});
		
		jq("#applicant1DetailModel_residentialAddress").keyup(function(){
			    if(jq(this).val().length > 250){
			        jq(this).val(jq(this).val().substr(0, 249));
		}});
		
		jq("#applicant1DetailModel_buisnessAddress").keyup(function(){
			    if(jq(this).val().length > 250){
			        jq(this).val(jq(this).val().substr(0, 249));
		}});
		
		jq("#applicant1_nokMailingAdd").keyup(function(){
			    if(jq(this).val().length > 250){
			        jq(this).val(jq(this).val().substr(0, 249));
		}});
		jq("#regStateComments").keyup(function(){
			    if(jq(this).val().length > 250){
			        jq(this).val(jq(this).val().substr(0, 249));
		}});
		
		function isValidFED(){
			var flag=true;
			var val = document.getElementById("fed").value;
			if(val == "" || val == null && val == undefined){
				flag=true;
			}
			
			var count = occurrences(val, ".");
			if(count > 1){
				flag= false;				
			}
			
			var x = parseFloat(val);
			if (isNaN(x) || x < 0 || x > 100) {
			    flag= false;
			}
			
			if(!flag){
				alert('Please input a valid FED amount.')
			}
			return flag;
		}
		
		function occurrences(string, subString, allowOverlapping){

		    string+=""; subString+="";
		    if(subString.length<=0) return string.length+1;

		    var n=0, pos=0;
		    var step=(allowOverlapping)?(1):(subString.length);

		    while(true){
		        pos=string.indexOf(subString,pos);
		        if(pos>=0){ n++; pos+=step; } else break;
		    }
		    return(n);
		}
		
		function isValidMinLength( field, label, minlength )
      	{
	      	if( field.value != '' && field.value.length < minlength )
	      	{
	      		alert( label + ' cannot be less than ' + minlength + ' digits' );
	      		field.focus();
	      		return false;
	      	}
	      	return true;
      	}

		function isValidEmail( field, label )
	    {
	    	if( field.value != '' && field.value.length != 0 )
	      	{
	      		if(!isEmail(field))
	      		{
	      			alert( label + ' is not valid Email.' );
	      			field.focus();
	      			return false;
	      		}
	      	}
	       	return true;
	    }
	    
	    function isValidMinLength( field, label, minlength )
      	{
	      	if( field.value != '' && field.value.length < minlength )
	      	{
	      		alert( label + ' cannot be less than ' + minlength + ' digits' );
	      		field.focus();
	      		return false;
	      	}
	      	return true;
      	}

      	function isValidEmail( field, label )
      	{
	      	if( field.value != '' && field.value.length != 0 )
	      	{
	      		if(!isEmail(field))
	      		{
	      			alert( label + ' is not valid Email.' );
	      			field.focus();
	      			return false;
	      		}
	      	}
	       	return true;
		}
      

		function doRequiredGroup( className, label )
	    {
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
		
		
		var currServerDate = '<%=PortalDateUtils.currentFormattedDate("dd/MM/yyyy")%>';
		function isValidApplicant1IdExpiry(){
	   		
	   		if( trim(document.forms[0].idExpiryDate.value) != '' && isDateSmaller(document.forms[0].idExpiryDate.value,currServerDate)){
				alert('Applicant 1 ID Expiry should not be less than current date.');
				document.getElementById('idExpiryDate').focus();
				return false;
			}
	   		else if(!isValidDate(document.forms[0].idExpiryDate.value,"Date of ID Expiry")){
	   			document.getElementById('idExpiryDate').focus();
	   			return false;
	   		}
			return true;
		}
		   
		function isValidApplicant1VisaExpiry(){
	   		
	   		if( trim(document.forms[0].visaExpiryDate.value) != '' && isDateSmaller(document.forms[0].visaExpiryDate.value,currServerDate)){
				alert('Applicant 1 Visa Expiry should not be less than current date.');
				document.getElementById('visaExpiryDate').focus();
				return false;
			}
	   		else if(trim(document.forms[0].visaExpiryDate.value) != '' && (!isValidDate(document.forms[0].visaExpiryDate.value,"Date of ID Expiry"))){
	   			document.getElementById('visaExpiryDate').focus();
	   			return false;
	   		}
			return true;
		}
		
		function isValidApplicant1DOB()
		{
			if( trim(document.forms[0].dobDate.value) != '' && 
				isDateGreater(document.forms[0].dobDate.value,currServerDate)){
				alert('Applicant 1 Future date of birth is not allowed.');
				document.getElementById('dobDate').focus();
				return false;
	   		}else if( isAgeLessThan18Years(document.forms[0].dobDate.value)){
				alert('Applicant 1 age cannot be less than 18 years');
				document.getElementById('dobDate').focus();
				return false;
			}
	   		else if(!isValidDate(document.forms[0].dobDate.value,"Date of Birth")){
	   			document.getElementById('dobDate').focus();
	   			return false;
	   		}
			return true;
		}
		
		function isAgeLessThan18Years(dateOfBirth){
			if(trim(dateOfBirth) != null && dateOfBirth != '' && dateOfBirth.length == 10){
				dateOfBirth = dateOfBirth.split("/");
		  		var birthDay = dateOfBirth[0];
		  		var birthMonth = dateOfBirth[1];
		  		var birthYear = dateOfBirth[2];
		  		var age = calculate_age(birthMonth,birthDay,birthYear);
				if(age < 18){
					return true;
				}
			}
			return false;
	  	}
	  
		function calculate_age(birth_month,birth_day,birth_year)
		{
    		var today_date = '<%=PortalDateUtils.currentFormattedDate("dd/MM/yyyy")%>';
			today_date = today_date.split("/");
    		today_year = today_date[2];
    		today_month = today_date[1];
    		today_day = today_date[0];
    		age = today_year - birth_year;
    		if ( today_month < birth_month){
        		age--;
   			}
    		if ((birth_month == today_month) && (today_day < birth_day)){ 
        		age--;
    		}
    		return age;
		}
		
				
		function isNumberKey(evt){
		    var charCode = (evt.which) ? evt.which : event.keyCode
		    if (charCode > 31 && (charCode < 48 || charCode > 57))
		        return false;
		    return true;
		}
		
		function isValidBusinessCommencementDate()
		{
			if( trim(document.forms[0].commencementDate.value) != '' && 
				isDateGreater(document.forms[0].commencementDate.value,currServerDate)){
				alert('Business Commencement Future date is not allowed.');
				document.getElementById('commencementDate').focus();
				return false;
	   		}
			else if(trim(document.forms[0].commencementDate.value) != '' && (!isValidDate(document.forms[0].commencementDate.value,"Business Commencement Date"))){
	   			document.getElementById('commencementDate').focus();
	   			return false;
	   		}	
			return true;
		}
		
		function isValidBusinessIncDate()
		{
			if( trim(document.forms[0].incorporationDate.value) != '' && 
				isDateGreater(document.forms[0].incorporationDate.value,currServerDate)){
				alert('Business Inc. Future date is not allowed.');
				document.getElementById('incorporationDate').focus();
				return false;
	   		}
			else if(trim(document.forms[0].incorporationDate.value) != '' && (!isValidDate(document.forms[0].incorporationDate.value,"Business Inc. Date"))){
	   			document.getElementById('incorporationDate').focus();
	   			return false;
	   		}
			return true;
		}
		
		function isValidSECPRegDate()
		{
			if( trim(document.forms[0].secpRegDate.value) != '' && 
				isDateGreater(document.forms[0].secpRegDate.value,currServerDate)){
				alert('SECP Reg. Future date is not allowed.');
				document.getElementById('secpRegDate').focus();
				return false;
	   		}
			else if(trim(document.forms[0].secpRegDate.value) != '' && (!isValidDate(document.forms[0].secpRegDate.value,"SECP Reg. Date"))){
	   			document.getElementById('secpRegDate').focus();
	   			return false;
	   		}
			return true;
		}
		
		function toggleVisaExpiry()
		{
			var selectedOption = document.getElementById("applicant1_idType");
			if(selectedOption.value == 3){
				document.getElementById("visaExpiryDateBtn").style.cssText = "cursor:pointer; margin-left:3px;";
			  	document.getElementById("visaExpiryDateBtn2").style.cssText = "cursor:pointer; margin-left:3px;";
			}
			else
			{
			 	document.getElementById("visaExpiryDateBtn").style.display="none";
			 	document.getElementById("visaExpiryDateBtn2").style.display="none";
			 	document.getElementById("visaExpiryDate").value="";
			}
		}
		
		jq("#coreAccountNumber").change(function(){
			    document.forms[0].coreAccountTitle.value = '';
		});
		
		Calendar.setup({
			inputField : "commencementDate", // id of the input field
			ifFormat : "%d/%m/%Y", // the date format
			button : "dateBtn1", // id of the button
			showsTime : false
		});

		Calendar.setup({
			inputField : "incorporationDate", // id of the input field
			ifFormat : "%d/%m/%Y", // the date format
			button : "dateBtn2", // id of the button
			showsTime : false
		});

		Calendar.setup({
			inputField : "secpRegDate", // id of the input field
			ifFormat : "%d/%m/%Y", // the date format
			button : "dateBtn3", // id of the button
			showsTime : false
		});

		Calendar.setup({
			inputField : "idExpiryDate", // id of the input field
			ifFormat : "%d/%m/%Y", // the date format
			button : "dateBtn4", // id of the button
			showsTime : false
		});

		Calendar.setup({
			inputField : "visaExpiryDate", // id of the input field
			ifFormat : "%d/%m/%Y", // the date format
			button : "visaExpiryDateBtn", // id of the button
			showsTime : false
		});

		Calendar.setup({
			inputField : "dobDate", // id of the input field
			ifFormat : "%d/%m/%Y", // the date format
			button : "dateBtn6", // id of the button
			showsTime : false
		});

		Calendar.setup({
			inputField : "acOwnershipDetailModelList0.dateOfBirth", // id of the input field
			ifFormat : "%d/%m/%Y", // the date format
			button : "ownerDobDate00", // id of the button
			showsTime : false
		});
		
		window.onload = toggleVisaExpiry();
	</script>
</body>
</html>
