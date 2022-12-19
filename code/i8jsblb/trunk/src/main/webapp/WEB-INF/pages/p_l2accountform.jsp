<!--Author: Mohammad Shehzad Ashraf-->
<!--Modified By: Usman Ashraf -->
<!--Modified By: Abu Turab -->

<%@page import="org.springframework.web.bind.ServletRequestUtils"%>
<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

	<head>
<meta name="decorator" content="decorator2">
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>

		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/prototype.js"></script>
		<link rel="stylesheet" type="text/css"
			href="styles/deliciouslyblue/calendar.css" />

		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="expires" content="0" />
		<meta http-equiv="pragma" content="no-cache" />
		<c:choose>
			<c:when test="${not empty level2AccountModel.appUserId}">
				<meta name="title"
					content="${pageMfsId}: ${level2AccountModel.applicant1DetailModel.name}" />
			</c:when>
			<c:otherwise>
				<meta name="title" content="Account Opening Form - Branchless Banking" />
			</c:otherwise>
		</c:choose>
		
		<style>
                #table_form{
                                display:block;
                                width:100%;
                                height:auto;
                                margin:0px;
                                padding:0px;
                }
                #table_form .left{
                                display:block;
                                width:49%;
                                float:left;
                }
                #table_form .right{
                                display:block;
                                width:49%;
                                float:right;
                }              
		</style>
		
		<%@include file="/common/ajax.jsp"%>
		<!-- turab added below for jquery -->
        <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.11.0.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.maskedinput.js"></script>
   
        <link rel="stylesheet" href="${contextPath}/styles/jquery-ui-custom.min.css" type="text/css">
        <script type="text/javascript" src="${contextPath}/scripts/jquery-ui-custom.min.js"></script>

    <script>
	var jq = $.noConflict();
	jq(document).ready(
		function($) {
			
			jq("#applicant1DetailModel_presentAddHouseNo").keyup(function(){
			    if($(this).val().length > 250){
			        $(this).val($(this).val().substr(0, 249));
			}
			});
			
			jq("#applicant1DetailModel_buisnessAddress").keyup(function(){
			    if($(this).val().length > 250){
			        $(this).val($(this).val().substr(0, 249));
			}
			});
			
			
			jq("#nokDetailVOModel_nokMailingAdd").keyup(function(){
			    if($(this).val().length > 250){
			        $(this).val($(this).val().substr(0, 249));
			}
			});
			
			jq("#regStateComments").keyup(function(){
			    if($(this).val().length > 250){
			        $(this).val($(this).val().substr(0, 249));
			}
			});
			
			jq(document).on('click', '#acOwnershipDetailTable .addRow', function () {
    	        var row = jq(this).closest('tr');
    	        var slct = row.find('select');
    	        if(slct[0].value==""){
    	        	alert("Please select Account Ownership Type before adding new row.");
    	        	return false;
    	        }
    	        var selectArray = row.find('input');
				if(selectArray[7].value == ""){ //name
    				alert("Please enter Name from ownership detail section.");
    				return false;
    			}
				
				if(slct[1].value==""){
    	        	alert("Please select ID Document Type before adding new row.");
    	        	return false;
    	        }
				if(selectArray[8].value == ""){ //id number
    				alert("Please enter the ID Document Number in ownership detail section");
    				return false;
    			}
    	        if(selectArray[9].value == ""){ //dob
    				alert("Please enter the Date of Birth in ownership detail section");
    				return false;
    			}
    	        
    	        var clone = row.clone();

    	        // clear the values
    	        var tr = clone.closest('tr');
    	        tr.find('input[type=text], select').val('');
    	        tr.find('input[type=text], select').each(function() {
    	        	this.style.borderColor="#e6e6e6";
    	        });

    	        tr.find('td:eq(0)').find("input:hidden").val('');

    	        jq(this).closest('tr').after(clone);
    	        resetIdAndNameAttributes();
    	    });

    	    jq(document).on('click', '#acOwnershipDetailTable .removeRow', function () {
    	        if (jq('#acOwnershipDetailTable .addRow').length > 1) {
    	            jq(this).closest('tr').remove();
    	            resetIdAndNameAttributes();
    	        }
    	    });
    	    
    		
    		jq(function($){
    		   $("#applicant1DetailModel_idExpiryDate").mask("99/99/9999",{placeholder:"dd/mm/yyyy"});
    		   $("#applicant1DetailModel_dob").mask("99/99/9999",{placeholder:"dd/mm/yyyy"});
    		   $("#applicant1DetailModel_visaExpiryDate").mask("99/99/9999",{placeholder:"dd/mm/yyyy"});
    		   $("[id$='dateOfBirth']").mask("99/99/9999",{placeholder:"dd/mm/yyyy"});
    		   
    		});
    	    
    	   function resetIdAndNameAttributes()
    	    {
    	    	jq('#acOwnershipDetailTable tbody tr').each(
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
    	    					
    	    					if(name.indexOf("dateOfBirth")>0){

    	    						Calendar.setup(
      	    				    	      {
      	    				    	        inputField  : id, // id of the input field
      	    				    	        ifFormat    : "%d/%m/%Y",      // the date format
      	    				    	        button      : "ownerDobDate00"+index,    // id of the button
      	    				    	        showsTime   :   false
      	    				    	      }
      	    				    	      );
    	    					}
    	    					
	    					}
    	    			);
    	    		}
    	    	);
    	    }
		
		function setValues(){
			alert("setting values");
			var newRow = $("#applicantDetailsTable tr:last");
			alert(newRow.html());
	        var hiddenFields = newRow.find('td').find("input:hidden");
	        alert(hiddenFields.length);
	        $(hiddenFields[0]).val($(applicantDetailModelEditMode_name).val());// business name
	        //$(hiddenFields[1]).val($(applicantDetailModelEditMode_buisnessName).val());// business name
	        //$(hiddenFields[2]).val($(applicantDetailModelEditMode_buisnessName).val());// business name
	        //$(hiddenFields[3]).val($(applicantDetailModelEditMode_buisnessName).val());// business name
	        $(hiddenFields[4]).val($(applicantDetailModelEditMode_ntn).val());// NTN
	        $(hiddenFields[5]).val($(applicantDetailModelEditMode_name).val());// account owner name
	        $(hiddenFields[6]).val($(applicantDetailModelEditMode_occupation).val());// occupation
	        $(hiddenFields[7]).val($(applicantDetailModelEditMode_nic).val());// NIC
	        $(hiddenFields[8]).val($(applicantDetailModelEditMode_idExpiryDate).val());// NIC Expiry
	        $(hiddenFields[9]).val($(applicantDetailModelEditMode_fatherHusbandName).val());// fatherHusband Name
	        $(hiddenFields[10]).val($(applicantDetailModelEditMode_dob).val());// DOB
	        $(hiddenFields[11]).val($(applicantDetailModelEditMode_birthPlace).val());// birth place
	        $(hiddenFields[12]).val($(applicantDetailModelEditMode_gender).val());// Gender
	        $(hiddenFields[13]).val($(applicantDetailModelEditMode_email).val());// Email
	        $(hiddenFields[14]).val($(applicantDetailModelEditMode_landLineNo).val());// Landline No
	        $(hiddenFields[15]).val($(applicantDetailModelEditMode_contactNo).val());// cnotact No
	        $(hiddenFields[16]).val($(applicantDetailModelEditMode_fax).val());// fax
	        $(hiddenFields[17]).val($(applicantDetailModelEditMode_buisnessAddress).val());// business address
	        $(hiddenFields[18]).val($(applicantDetailModelEditMode_buisnessPostalOfficeId).val());// business postal office
	        $(hiddenFields[19]).val($(applicantDetailModelEditMode_presentAddHouseNo).val());// present address 
	        $(hiddenFields[20]).val($(applicantDetailModelEditMode_presentAddPostalOfficeId).val());// postal address
	        
	        //applicantDetailModelEditMode_customerPic
	        //alert($(hiddenFields[21].id.val());
	        var clone = $(applicantDetailModelEditMode_customerPic).clone();
	        
    		//clone.attr('id', $(hiddenFields[21].id);
    		//$('#field2_area').html(clone);
    		//$(hiddenFields[21]) = clone;
	        
//	        $(hiddenFields[21]).val($(applicantDetailModelEditMode_customerPic).val());// customerPic
	        $(hiddenFields[22]).val($(applicantDetailModelEditMode_tncPic).val());// terms and condition pic
	        $(hiddenFields[23]).val($(applicantDetailModelEditMode_signPic).val());// signature pic
	        $(hiddenFields[24]).val($(applicantDetailModelEditMode_cnicFrontPic).val());// cnic front pic
	        $(hiddenFields[25]).val($(applicantDetailModelEditMode_cnicBack).val());// cnic back pic
	        
		}
		
		var doAjaxBeforeUnloadEnabled = true; // We hook into window.onbeforeunload and bind some jQuery events to confirmBeforeUnload.  This variable is used to prevent us from showing both messages during a single event.
		var doAjaxBeforeUnload = function (evt) {
	    if (!doAjaxBeforeUnloadEnabled) {
	        return;
	    }
			    
			    doAjaxBeforeUnloadEnabled = false;
			    jq.ajax({
			        url: "${contextPath}/p_mnomfsaccountdetailsajx.html?appUserId=${level2AccountModel.appUserId}",
			        success: function (a) {
			            console.debug("Ajax call finished");
			        },
			        async: false /* Not recommended.  This is the dangerous part.  Your mileage may vary. */
			    });
			}
			
			window.onbeforeunload = doAjaxBeforeUnload;
		    $(window).unload(doAjaxBeforeUnload);

	});
	</script>		
		
		<script type="text/javascript">
			var customerEntityTypeCorporate=false;
				
      		function showHideOthers(selectBoxId,othersId){
		    	return true;
		  	}
		function openPrinterWindow(){
			var appUserId = '<%= request.getParameter("appUserId") %>';
			var location ='${pageContext.request.contextPath}/p_mnonewmfsaccountform_printer.html?appUserId='+ appUserId; 
			window.open(location,'printWindow');
			
		}
		function openPrintWindow()
		{
			if( isValidFormForPrinting() )
			{
				var location ='${pageContext.request.contextPath}/p_mnonewmfsaccountform_print.html'; 
				window.open(location,'printWindow');
			}
		}
		function isNumericWithHyphin(code) {
			return ((code >= zeroCharCode && code <= nineCharCode)
			|| 
			code == 46 ||   /*delete */
			code == 8 || 	/*back */
			code == 36 ||	/*home */
			code == 35 || 	/*end */
			code == 37 || 	/* left arrow */
			code == 39 || 	/* right arrow  */
			code == 38 || 	/* up arrow */
			code == 40 ||   /* down arrow */
			code == 9  ||	/* tab */	
			code == 13 ||   /* enter */
			code == 16 ||   /* shift */
			code == 17 ||   /* ctrl */
			code == 18 ||   /* alt */
			code == minusCharCode /* minus character */
			);
			}
		
		function maskCNIC(obj, e) {
			var code = e.charCode ? e.charCode : e.keyCode;
			if (!isNumericWithHyphin(code)||  code == 38 || code == 35 || code == 36 || code == 37  || code == 39 || code == 40) {
				return false;
   			}
   			return true;
		}

		/**
		*	method : getFileSize()
		*	author : kashif
		*	description : this method will return file size in mega bytes (mb)
		*/
		function getFileSize(file) {

		    if(file != null && file.files != null && file.files[0] != null) {
		    
				return ((file.files[0].size/1024)/1024);
			}
			
			return 0;
		}
		
		function isValidFED(){
			var val = document.getElementById("fed").value;
			if(val == "" || val == null && val == undefined){
				return true;
			}
			
			var count = occurrences(val, ".");
			if(count > 1){
				return false;				
			}
			
			var x = parseFloat(val);
			if (isNaN(x) || x < 0 || x > 100) {
			    return false;
			}
			return true;
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
		
		function checkDocumentIDLength(applicant1DetailModel_idType, fieldValue, idNumber) {


			//var applicant1DetailModel_idType = document.getElementById('applicant1DetailModel_idType');
			//var fieldValue = applicant1DetailModel_idType.options[applicant1DetailModel_idType.selectedIndex].text;
			
			//var idNumber = document.getElementById('applicant1DetailModel_idNumber');
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
		
      </script>
		<%
		String createPermission = PortalConstants.MNG_GEN_ACC_CREATE;
		createPermission +=	"," + PortalConstants.PG_GP_CREATE;
		createPermission +=	"," + PortalConstants.MNG_L2_CUST_CREATE;
		createPermission +=	"," + PortalConstants.RET_GP_CREATE;
		createPermission +=	"," + PortalConstants.ADMIN_GP_CREATE;
		createPermission +=	"," + PortalConstants.HOME_PAGE_QUICK_BB_CUST_CREATE;

		String updatePermission = PortalConstants.MNG_GEN_ACC_UPDATE;
		updatePermission +=	"," + PortalConstants.PG_GP_UPDATE;
		updatePermission +=	"," + PortalConstants.MNG_L2_CUST_UPDATE;

		String printPermission = PortalConstants.BB_CUST_PRINT_FORM_READ;
		printPermission += "," + PortalConstants.PG_GP_READ;
		printPermission += "," + PortalConstants.MNG_GEN_ACC_CREATE;
		printPermission +=	"," + PortalConstants.RET_GP_READ;
		printPermission +=	"," + PortalConstants.ADMIN_GP_READ;

		String reprintPermission = PortalConstants.BB_CUST_REPRINT_FORM_READ;
		reprintPermission += "," + PortalConstants.PG_GP_READ;
		reprintPermission += "," + PortalConstants.MNG_GEN_ACC_CREATE;
		reprintPermission += "," + PortalConstants.RET_GP_CREATE;
		reprintPermission += "," + PortalConstants.ADMIN_GP_CREATE;
	 %>

	</head>
	<body bgcolor="#ffffff">

		<div id="successMsg" class="infoMsg" style="display: none;"></div>
		<spring:bind path="level2AccountModel.*">
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
			<html:form name="level2AccountForm" commandName="level2AccountModel"
				enctype="multipart/form-data" method="POST"
				onsubmit="return onFormSubmit(this)"
				action="p_l2accountform.html">
				
				<c:if test="${not empty level2AccountModel.appUserId}">
					<input type="hidden" name="isUpdate" id="isUpdate" value="true" />
					<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
						value="<%=PortalConstants.ACTION_UPDATE%>" />
				</c:if>
				<c:if test="${empty level2AccountModel.appUserId}">
					<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
						value="<%=PortalConstants.ACTION_CREATE%>" />
				</c:if>

				<html:hidden path="mfsId" value="${pageMfsId}"/>
				<html:hidden path="accounttypeName"/>
				<html:hidden path="accountPurpose"/>				
				<html:hidden path="segmentName"/>
				<html:hidden path="regStateName"/>
				<html:hidden path="taxRegimeName"/>
				<html:hidden path="acNatureName"/>
				<html:hidden path="applicant1DetailModel.titleTxt" id="applicant1DetailModel_titleTxt"/>
				<html:hidden path="applicant1DetailModel.idTypeName" id="applicant1DetailModel_idTypeName"/>
				<html:hidden path="applicant1DetailModel.birthPlaceName" id="applicant1DetailModel_birthPlaceName"/>
				<html:hidden path="applicant1DetailModel.occupationName" id="applicant1DetailModel_occupationName"/>
				<html:hidden path="applicant1DetailModel.professionName" id="applicant1DetailModel_professionName"/>
				<html:hidden path="applicant1DetailModel.maritalStatusName" id="applicant1DetailModel_maritalStatusName"/>
				<html:hidden path="applicant1DetailModel.residentialCityName" id="applicant1DetailModel_residentialCityName"/>
				<html:hidden path="applicant1DetailModel.buisnessCityName" id="applicant1DetailModel_buisnessCityName"/>
				<html:hidden path="nokDetailVOModel.nokIdTypeName" id="nokDetailVOModel_nokIdTypeName"/>

				<input type="hidden" id="appUserId" name="appUserId" value="${level2AccountModel.appUserId}">
				<input type="hidden" name="actionAuthorizationId" value="${param.authId}" />
          		<input type="hidden" name="resubmitRequest" value="${param.isReSubmit}" />
				
				<html:hidden path="<%=PortalConstants.KEY_USECASE_ID %>" />
				<div class="infoMsg" id="" style="display:none;"></div>
				<div id="table_form">
                	<div class="center">
                	<table>
                		<tr>
                			<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span style="color: #FF0000">*</span>Initial Application Form Number:
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<%-- <html:input path="initialAppFormNo" cssClass="textBox" id="initialAppFormNo" readonly="true"
									tabindex="1" /> --%>
								<c:choose>
									<c:when test="${not empty level2AccountModel.initialAppFormNo}">
										<html:hidden path="initialAppFormNo" id="initialAppFormNo" />
										<div class="textBox" style="background: #D3D3D3; ">${level2AccountModel.initialAppFormNo}</div>
									</c:when>
									<c:otherwise>
										<html:input path="initialAppFormNo" cssClass="textBox" id="initialAppFormNo" readonly="true"
											tabindex="1" />
									</c:otherwise>
								</c:choose>
									
							</td>

						
						
						<c:if test="${not empty level2AccountModel.appUserId}">
						
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								Mobile A/C #:
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<div class="textBox" style="background: #D3D3D3; ">${level2AccountModel.mfsId}</div>
							</td>
						</c:if>
						</tr>
                		<tr >
                			<td colspan="4" align="left" bgcolor="FBFBFB">
								<h2>
									Account Relationship Information
								</h2>
							</td>
                		</tr>
                		<tr>
                			<td align="right" width="25%" bgcolor="F3F3F3" class="formText">
								<span style="color: #FF0000">*</span>Purpose of Account:
							</td>
							<td align="left" bgcolor="FBFBFB">
								<html:select path="accountPurposeId" cssClass="textBox"
									tabindex="2">
									<html:option value="">[Select]</html:option>
										<c:if test="${accountPurposeList != null}">
											<html:options items="${accountPurposeList}" itemLabel="name" itemValue="accountPurposeId" />
				 						</c:if>
								</html:select>
							</td>
							
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span style="color: #FF0000">*</span>Type of Account:
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								
								
								<c:choose>
									<c:when test="${not empty level2AccountModel.appUserId}">
										<div class="textBox" style="background: #D3D3D3;" id="customerAccountTypeName">${level2AccountModel.accounttypeName}</div>
										<html:hidden path="customerAccountTypeId" value="${level2AccountModel.customerAccountTypeId}" />
									</c:when>
									<c:otherwise>
										<html:select path="customerAccountTypeId" cssClass="textBox" id="customerAccountTypeId"
											tabindex="3" onchange="acTypeInfo(this);">
											<html:option value="">[Select]</html:option>
												<c:if test="${customerAccountTypeList != null}">
													<html:options items="${customerAccountTypeList}" itemLabel="name" itemValue="customerAccountTypeId" />
					 							</c:if>
											</html:select>
									</c:otherwise>
								</c:choose>
								
							</td>
                		
						</tr>
						<tr>
							<%-- <td height="16" align="right" bgcolor="F3F3F3" class="formText">
								<span style="color: #FF0000">*</span>Customer/Entities Type:
							</td>
							<td bgcolor="FBFBFB">
								<html:select path="businessTypeId" cssClass="textBox"
									tabindex="16" onchange="changeEntityType(this)">
									<html:option value="">[Select]</html:option>
										<c:if test="${corporateBusinessTypeList != null}">
											<optgroup label="Corporate">
												<html:options items="${corporateBusinessTypeList}" itemLabel="name" itemValue="businessTypeId" />
				 							</optgroup>
				 						</c:if>
				 						<c:if test="${individualBusinessTypeList != null}">
											<optgroup label="Individual">
												<html:options items="${individualBusinessTypeList}" itemLabel="name" itemValue="businessTypeId" />
				 							</optgroup>
				 						</c:if>
								</html:select>
							</td> --%>
							
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span style="color: #FF0000">*</span>Nature of Account:
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:select path="acNature" cssClass="textBox" id="accountNature"
									tabindex="4">
									<html:option value="">[Select]</html:option>
										<c:if test="${accountTypeList != null}">
											<html:options items="${accountTypeList}" itemLabel="name" itemValue="accountNatureId" selected="selected"/>
				 						</c:if>
								</html:select>
							</td>
							<td align="right" bgcolor="F3F3F3" class="formText">
								<span style="color: #FF0000">*</span>Segment:
							</td>
							<td align="left" bgcolor="FBFBFB">
								<html:select path="segmentId" cssClass="textBox"
									tabindex="5">
										<html:option value="">[Select]</html:option>
										<c:if test="${segmentList != null}">
											<html:options items="${segmentList}" itemLabel="name" itemValue="segmentId" />
				 						</c:if>
								</html:select>
							</td>
						</tr>
						<tr>
							
						
							<%-- <td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">
								<span style="color: #FF0000">*</span>ID Document Seen:
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:checkbox id="cnicSeen" path="cnicSeen"/>
							</td> --%>
						</tr> 
						<tr>
						<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span style="color: #FF0000">*</span>Account Title:
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input path="customerAccountName" id="customerAccountName" tabindex="6" cssClass="textBox" maxlength="50" onkeypress="return maskAlphaNumericWithSp(this,event)" />
							</td>
							
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span style="color: #FF0000">*</span>Currency:
							</td>
							
							<c:if test="${not empty level2AccountModel.appUserId}">
								<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
									<div class="textBox" style="background: #D3D3D3; ">PKR</div>
									<html:hidden path="currency" value="586" />
								</td>
							</c:if>
							<c:if test="${empty level2AccountModel.appUserId}">
								<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
									<html:select path="currency" cssClass="textBox" tabindex="7">
										<html:option value="">[Select]</html:option>
										<html:option selected="selected" value="586" >PKR</html:option>
									</html:select>
								</td>
							</c:if>
						</tr>
						
						<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span style="color: #FF0000">*</span>Tax Regime:
						</td>
						<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:select path="taxRegimeId" cssClass="textBox"
									tabindex="8">
									<html:option value="">[Select]</html:option>
										<c:if test="${regimeList != null}">
											<html:options items="${regimeList}" itemLabel="name" itemValue="taxRegimeId" />
				 						</c:if>
								</html:select>
						</td>
							
						<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span style="color: #FF0000">*</span>FED (%age):
							</td>
							<td align="left" bgcolor="FBFBFB"><html:input path="fed" id="fed" cssClass="textBox" style="background: #D3D3D3; font-size:14px;font-weight: bold;" onfocus="this.blur()" readonly="true" maxlength="5"/></td>
						</tr>
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span style="color: #FF0000">*</span>Customer Catalog:
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:select path="productCatalogId" cssClass="textBox"
									tabindex="8">
									<html:option value="">[Select]</html:option>
										<c:if test="${productCatalogList != null}">
											<html:options items="${productCatalogList}" itemLabel="name" itemValue="productCatalogId" />
				 						</c:if>
								</html:select>
							</td>
						</tr>
						<tr >
                			<td colspan="4" align="left" bgcolor="FBFBFB">
								<h2>
									Personal Information - <small>(Primary)</small>
								</h2>
							</td>
                		</tr>
                		<tr>
                		<input type="hidden" name="applicant1DetailModel.applicantTypeId" value="1"/>
                 			<input type="hidden" name="applicant1DetailModel.applicantDetailId" value="${level2AccountModel.applicant1DetailModel.applicantDetailId}"/>
                 			<html:hidden path="applicant1DetailModel.versionNo"/>
                 			<html:hidden path="applicant1DetailModel.createdOn"/>
                 			<html:hidden path="applicant1DetailModel.createdBy"/>
						<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Title:
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:select path="applicant1DetailModel.title" cssClass="textBox" id="applicant1DetailModel_title"
									tabindex="10">
									<html:option value="">[Select]</html:option>
										<c:if test="${titleList != null}">
											<html:options items="${titleList}" itemLabel="label" itemValue="value" />
				 						</c:if>
								</html:select>
							</td>
							
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								Gender:
							</td>
							<td bgcolor="FBFBFB">
 								<html:radiobuttons class="applicant1DetailModel_gender" tabindex="11" items="${genderList}" itemLabel="label" itemValue="value" path="applicant1DetailModel.gender" />
							</td>
						</tr>
						
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								<span style="color: #FF0000">*</span>Applicant Name:
							</td>
							<td bgcolor="FBFBFB">
								<html:input id="applicant1DetailModel_name" path="applicant1DetailModel.name" cssClass="textBox" tabindex="12" onkeypress="return maskAlphaWithSp(this,event)" maxlength="50"/>
							</td>
							
						</tr>
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								<span style="color: #FF0000">*</span>ID Document Type:
							</td>
							<td height="16" align="left" bgcolor="F3F3F3" class="formText">
							<c:choose>
							<c:when test="${not empty level2AccountModel.appUserId}">
								<input type="hidden" id="applicant1DetailModel_idType" name="applicant1DetailModel.idType" value="${level2AccountModel.applicant1DetailModel.idType}"/>
								<div class="textBox" id="divIdTypeName" style="background: #D3D3D3; ">${level2AccountModel.applicant1DetailModel.idTypeName}</div>
							</c:when>
							<c:otherwise>
								<html:select path="applicant1DetailModel.idType" tabindex="13" cssClass="textBox" id="applicant1DetailModel_idType"
								onchange="toggleVisaExpiry();">
											<html:option value="">[Select]</html:option>
											<html:options items="${documentTypeList}" itemLabel="label" itemValue="value"/>
								</html:select>
								</c:otherwise>
							</c:choose>
							</td>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								<span style="color: #FF0000">*</span>ID Document Number:
							</td>
							<td bgcolor="FBFBFB" class="formText">
								<c:choose>
									<c:when test="${not empty level2AccountModel.appUserId}">
										<input type="hidden" id="applicant1DetailModel_idNumber" name="applicant1DetailModel.idNumber" value="${level2AccountModel.applicant1DetailModel.idNumber}"/>
										<div class="textBox" style="background: #D3D3D3; ">${level2AccountModel.applicant1DetailModel.idNumber}</div>
									</c:when>
									<c:otherwise>
										<html:input id="applicant1DetailModel_idNumber" path="applicant1DetailModel.idNumber" cssClass="textBox" tabindex="14" maxlength="13"/>
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
						
						<%-- <tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span style="color: #FF0000">*</span>ID Document Front Image:
							</td>
							<td bgcolor="FBFBFB" class="text" width="25%">
								<spring:bind path="level2AccountModel.applicant1DetailModel.idFrontPic">
									<input type="file" tabindex="5" id="applicant1DetailModel_idFrontPic" name="applicant1DetailModel.idFrontPic" class="upload"/>
								</spring:bind>
								&nbsp;&nbsp;
								<c:choose>
									<c:when test="${not empty param.appUserId}">
											<img src="${contextPath}/images/upload_dir/idFrontPic1_${level2AccountModel.appUserId}.gif?time=<%=System.currentTimeMillis()%>"
												width="100" height="100" />
									</c:when>
								</c:choose>
							</td>
						
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span style="color: #FF0000">*</span>ID Document Back Image:
							</td>
							<td bgcolor="FBFBFB" class="text" width="25%">
								<spring:bind path="level2AccountModel.applicant1DetailModel.idBackPic">
									<input type="file" tabindex="6" id="applicant1DetailModel_idBackPic" name="applicant1DetailModel.idBackPic" class="upload" />
								</spring:bind>
								&nbsp;&nbsp;
								<c:choose>
									<c:when test="${not empty param.appUserId}">
											<img src="${contextPath}/images/upload_dir/idBackPicPic1_${level2AccountModel.appUserId}.gif?time=<%=System.currentTimeMillis()%>"
												width="100" height="100" />
									</c:when>
								</c:choose>
							</td>
						</tr> --%>
						
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								<span style="color: #FF0000">*</span>Date of ID Expiry:
							</td>
							<td bgcolor="FBFBFB">
								<html:input id="applicant1DetailModel_idExpiryDate" path="applicant1DetailModel.idExpiryDate" cssClass="textBox" tabindex="25" maxlength="50" />
								<img id="nicDate1" tabindex="15" name="popcal" align="top"
									style="cursor:pointer"
									src="${pageContext.request.contextPath}/images/cal.gif"
									border="0" tabindex="16" />
								<img id="nicDate1" tabindex="17" name="popcal" title="Clear Date"
									onclick="javascript:$('applicant1DetailModel_idExpiryDate').value=''" align="middle"
									style="cursor:pointer" tabindex="18"
									src="${pageContext.request.contextPath}/images/refresh.png"
									border="0" />
							</td>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								NTN #:
							</td>
							<td bgcolor="FBFBFB">
								<html:input id="applicant1DetailModel_ntn" path="applicant1DetailModel.ntn" cssClass="textBox" tabindex="19" maxlength="8" onkeypress="return isNumberKey(event)"/>
							</td>
						</tr>
						
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								<span style="color: #FF0000">*</span>Date of Birth:
							</td>
							<td bgcolor="FBFBFB">
								<html:input id="applicant1DetailModel_dob" path="applicant1DetailModel.dob" cssClass="textBox" tabindex="20"
									maxlength="50"/>
								<img id="dobDate1" tabindex="21" name="popcal" align="top"
									style="cursor:pointer"
									src="${pageContext.request.contextPath}/images/cal.gif"
									border="0" tabindex="3" />
								<img id="dobDate1" tabindex="21" name="popcal" title="Clear Date"
									onclick="javascript:$('applicant1DetailModel_dob').value=''" align="middle"
									style="cursor:pointer" tabindex="21"
									src="${pageContext.request.contextPath}/images/refresh.png"
									border="0" />
							</td>
						
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Place of Birth:</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input id="applicant1DetailModel_birthPlace" path="applicant1DetailModel.birthPlace" tabindex="7" cssClass="textBox" maxlength="100"/>
							</td>
						</tr>
						
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								Date of Visa Expiry:
							</td>
							<td bgcolor="FBFBFB">
								<html:input id="applicant1DetailModel_visaExpiryDate" path="applicant1DetailModel.visaExpiryDate" cssClass="textBox" tabindex="25" maxlength="50" />
								<img id="visaExpiry" tabindex="23" name="popcal" align="top"
									style="cursor:pointer"
									src="${pageContext.request.contextPath}/images/cal.gif"
									border="0" tabindex="24" />
								<img id="visaExpiry2" tabindex="25" name="popcal" title="Clear Date"
									onclick="javascript:$('applicant1DetailModel_visaExpiryDate').value=''" align="top"
									style="cursor:pointer" tabindex="26"
									src="${pageContext.request.contextPath}/images/refresh.png"
									border="0" />
							</td>
							
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								Mother's Maiden Name:
							</td>
							<td bgcolor="FBFBFB">
								<html:input id="applicant1DetailModel_motherMaidenName" path="applicant1DetailModel.motherMaidenName" cssClass="textBox" tabindex="27" maxlength="50" onkeypress="return maskAlphaWithSp(this,event)"/>
							</td>
						</tr>
						
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								Residential Status:
							</td>
							<td bgcolor="FBFBFB">
 								<html:radiobuttons class="applicant1DetailModel_residentialStatus" tabindex="28" items="${residentialStatusList}" itemLabel="label" itemValue="value" path="applicant1DetailModel.residentialStatus" />
							</td>
							
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								US Citizen:
							</td>
							<td bgcolor="FBFBFB">
								<%-- <html:select path="applicant1DetailModel.usCitizen" cssClass="textBox" id="applicant1DetailModel_usCitizen"
									tabindex="29">
									<html:option value="">[Select]</html:option>
									<html:option value="1">Yes</html:option>
									<html:option value="0">No</html:option>	
								</html:select> --%>
								<html:radiobuttons class="applicant1DetailModel_usCitizen" tabindex="29" items="${binaryOpt}" itemLabel="label" itemValue="value" path="applicant1DetailModel.usCitizen" />
							</td>
						</tr>
						
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								Nationality
							</td>
							<td bgcolor="FBFBFB">
								<html:input id="applicant1DetailModel_nationality" path="applicant1DetailModel.nationality" cssClass="textBox" tabindex="30" maxlength="50" onkeypress="return maskAlphaWithSp(this,event)"/>
							</td>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Phone Number:
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input id="applicant1DetailModel_landLineNo" path="applicant1DetailModel.landLineNo" maxlength="11" tabindex="31" cssClass="textBox" onkeypress="return maskInteger(this,event)" />
							</td>
						</tr>
						
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Home Number:</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input id="applicant1DetailModel_contactNo" path="applicant1DetailModel.contactNo" tabindex="32" cssClass="textBox" maxlength="11" onkeypress="return isNumberKey(event)"/>
							</td>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Fax Number:</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input id="applicant1DetailModel_fax" path="applicant1DetailModel.fax" tabindex="33" cssClass="textBox" onkeypress="return maskInteger(this,event)" maxlength="11"/>
							</td>
						</tr>
						
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								<span style="color: #FF0000">*</span>Mobile Number:
							</td>
							<td bgcolor="FBFBFB" class="formText">
								<c:choose>
									<c:when test="${not empty level2AccountModel.appUserId}">
										<input type="hidden" name="mobileNo" value="${level2AccountModel.mobileNo}"/>
										<div class="textBox" style="background: #D3D3D3; ">${level2AccountModel.mobileNo}</div>
									</c:when>
									<c:otherwise>
										<html:input path="mobileNo" cssClass="textBox" tabindex="34"
											maxlength="11" onkeypress="return isNumberKey(event)"/>
									</c:otherwise>
								</c:choose>
							</td>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Email:</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input path="applicant1DetailModel.email" id="applicant1DetailModel_email" maxlength="50" tabindex="35" cssClass="textBox" />
							</td>
						</tr>
						
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								Father Name/Husband Name:
							</td>
							<td bgcolor="FBFBFB">
								<html:input path="applicant1DetailModel.fatherHusbandName" id="applicant1DetailModel_fatherHusbandName" onkeypress="return maskAlphaWithSp(this,event)" cssClass="textBox" tabindex="36" maxlength="50"/>
							</td>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								Name of Employer/Business:
							</td>
							<td bgcolor="FBFBFB">
								<html:input path="applicant1DetailModel.employerName" id="applicant1DetailModel_employerName" onkeypress="return maskAlphaWithSp(this,event)" cssClass="textBox" tabindex="37" maxlength="50"/>
							</td>
						</tr>
						
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								Occupation:
							</td>
							<td bgcolor="FBFBFB">
								<html:select id="applicant1DetailModel_occupation" path="applicant1DetailModel.occupation" cssClass="textBox"
									tabindex="38">
									<html:option value="">[Select]</html:option>
										<c:if test="${occupationList != null}">
											<html:options items="${occupationList}" itemLabel="name" itemValue="occupationId" />
				 						</c:if>
								</html:select>
							</td>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								Profession:
							</td>
							<td bgcolor="FBFBFB">
								<html:select id="applicant1DetailModel_profession" path="applicant1DetailModel.profession" cssClass="textBox"
									tabindex="39">
									<html:option value="">[Select]</html:option>
										<c:if test="${professionList != null}">
											<html:options items="${professionList}" itemLabel="name" itemValue="professionId" />
				 						</c:if>
								</html:select>
							</td>
						</tr>
						
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								Mailing Address:
							</td>
							<td bgcolor="FBFBFB">
 								<html:radiobuttons class="applicant1DetailModel_mailing" tabindex="40" items="${mailingAddressTypeList}" itemLabel="label" itemValue="value" path="applicant1DetailModel.mailingAddressType" />
							</td>		
						</tr>
						
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Residential Address:</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:textarea style="height:50px;" id="applicant1DetailModel_presentAddHouseNo" path="applicant1DetailModel.residentialAddress" tabindex="41" cssClass="textBox" />
							</td>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Office/Business Address:</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:textarea style="height:50px;" id="applicant1DetailModel_buisnessAddress" path="applicant1DetailModel.buisnessAddress" tabindex="42" cssClass="textBox" />
							</td>
						</tr>
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								City:
							</td>
							<td bgcolor="FBFBFB">
								<html:select id="applicant1DetailModel_residentialCity" path="applicant1DetailModel.residentialCity" cssClass="textBox"
									tabindex="44">
									<html:option value="">[Select]</html:option>
									<c:if test="${cityList != null}">
											<html:options items="${cityList}" itemLabel="name" itemValue="cityId" />
				 					</c:if>
								</html:select>
							</td>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								City:
							</td>
							<td bgcolor="FBFBFB">
								<html:select id="applicant1DetailModel_buisnessCity" path="applicant1DetailModel.buisnessCity" cssClass="textBox"
									tabindex="43">
									<html:option value="">[Select]</html:option>
									<c:if test="${cityList != null}">
											<html:options items="${cityList}" itemLabel="name" itemValue="cityId" />
				 					</c:if>
								</html:select>
							</td>
						</tr>
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								Marital Status:
							</td>
							<td bgcolor="FBFBFB">
								<html:select id="applicant1DetailModel_maritalStatus" path="applicant1DetailModel.maritalStatus" cssClass="textBox"
									tabindex="45">
									<html:option value="">[Select]</html:option>
									<c:if test="${maritalStatusList != null}">
											<html:options items="${maritalStatusList}" itemLabel="label" itemValue="value" />
				 					</c:if>
								</html:select>
							</td>
							<%-- <td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span style="color: #FF0000">*</span>NADRA Verification:
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:checkbox path="isVeriSysDone" tabindex="8" />
							</td> --%>
						</tr>
						
						<tr >
                			<td colspan="4" align="left" bgcolor="FBFBFB">
								<h2>
									Next of KIN
								</h2>
							</td>
                		</tr>
						
						<tr>
							<td align="right" width="25%" bgcolor="F3F3F3" class="formText">
								Name of Next of KIN:
							</td>
							<td align="left" bgcolor="FBFBFB">
								<html:input path="nokDetailVOModel.nokName" maxlength="50" cssClass="textBox" tabindex="46" id="nokDetailVOModel_nokName"
									 onkeypress="return maskAlphaWithSp(this,event)" />
							</td>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Relationship with Next of KIN:
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input path="nokDetailVOModel.nokRelationship" maxlength="50" tabindex="47" cssClass="textBox" id="nokDetailVOModel_nokRelationship"
									 onkeypress="return maskAlphaWithSp(this,event)" />
							</td>
							
						</tr>
						
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								ID Document Type:
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:select tabindex="49" path="nokDetailVOModel.nokIdType" cssClass="textBox" id="nokDetailVOModel_nokIdType">
											<html:option value="">[Select]</html:option>
											<html:options items="${documentTypeList}" itemLabel="label" itemValue="value"/>
								</html:select>
							</td>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								ID Document Number:
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input path="nokDetailVOModel.nokIdNumber" tabindex="50" cssClass="textBox" id="nokDetailVOModel_nokIdNumber"
									maxlength="13"  />
							</td>
							
							
						</tr>
						
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
									width="25%">
									Telephone/Mobile No. of Next of KIN:
								</td>
								<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
									<html:input path="nokDetailVOModel.nokContactNo" tabindex="48" cssClass="textBox" maxlength="11" id="nokDetailVOModel_nokContactNo"
										onkeypress="return isNumberKey(event)" />
							</td>
							
							
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Next of KIN's Address:
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:textarea style="height:50px;" id="nokDetailVOModel_nokMailingAdd" path="nokDetailVOModel.nokMailingAdd" tabindex="51" cssClass="textBox" />
							</td>
						</tr>
                		

						<c:set var="accountModelLevel2" scope="session" value="${level2AccountModel}"/> 
						<tr>
						<td colspan="4">
						
						
                		<!-- adding applicant grid -->
						<div class="eXtremeTable">
						<table class="tableRegion" width="100%" id="applicantDetailsTable">
							<thead>
								<tr>
									<!-- <td height="35" align="center" class="tableHeader"><b>Sr.#</b></td> -->
							        <td height="35" align="center" class="tableHeader"><b>Business Name</b></td>
							        <td height="35" align="center" class="tableHeader"><b>Mobile Number</b></td>
							        <td height="35" align="center" class="tableHeader"><b>ID Document Number</b></td>
							        <td height="35" align="center" class="tableHeader"><b>ID Document Expiry</b></td>
							        <td height="35" align="center" class="tableHeader"><b>Nationality</b></td>
							        <td height="35" align="center" class="tableHeader"><b>Action</b></td>
						    	</tr>
					    	</thead>
					    	<c:if test="${level2AccountModel.applicantDetailModelList!=null}">
							<c:forEach varStatus="status"  var="applicantDetailModel" items="${level2AccountModel.applicantDetailModelList}">
					  			<tr id="${status.index+1}">
					  				<html:hidden path="applicantDetailModelList[${status.index}].applicantDetailId"/>
									<html:hidden path="applicantDetailModelList[${status.index}].createdBy"/>
									<html:hidden path="applicantDetailModelList[${status.index}].versionNo"/>
					  				<html:hidden path="applicantDetailModelList[${status.index}].index" value="${status.index+1}"/>
					  				<td align="center">${applicantDetailModel.name}</td>
					  				<td align="center">${applicantDetailModel.mobileNo}</td>
					  				<td align="center">${applicantDetailModel.idNumber}</td>
					  				<td align="center"><fmt:formatDate pattern="dd-MM-yyyy" value="${applicantDetailModel.idExpiryDate}" /></td>
					  				<td align="center">${applicantDetailModel.nationality}
					  					
					  				</td>
					  				<td align="center">
					  				<img id="editBtn" name="editBtn" align="top"
										style="cursor:pointer"
										src="${pageContext.request.contextPath}/images/buttons/edit.gif"
										border="0" onclick="popupwindow('p_l2_applicantformpopup.html?idNumber=${applicantDetailModel.idNumber}&isEdit=true&index=${status.index+1}', 'Edit Applicant', 955,600);" />&nbsp;
									</td>
					  			</tr>  	
					    	</c:forEach>
					    	</c:if>
						</table>
						</div>
						<!-- end of grid -->
                		</td>
						</tr>
						<tr><td colspan="4" align="right"><input onclick="popupwindow('p_l2_applicantformpopup.html', 'Add/Edit Applicant', 955,600);" type="button" value="Add Applicant" class="button" /></td></tr>
						
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
										<td height="35" align="center"  class="tableHeader"><b><span style="color: #FF0000">*</span>Type of Ownership</b></td>
										<td height="35" align="center"  class="tableHeader"><span style="color: #FF0000">*</span>Name</b></td>
										<td height="35" align="center"  class="tableHeader"><span style="color: #FF0000">*</span><b>ID Type</b></td>
										<td height="35" align="center"  class="tableHeader"><span style="color: #FF0000">*</span><b>Number</b></td>
										<td height="35" align="center"  class="tableHeader"><span style="color: #FF0000">*</span><b>DOB</b></td>
										<td height="35" align="center"  class="tableHeader"><b>OFAC/UNSC/INTERNAL</b></td>
										<td height="35" align="center" class="tableHeader"><b>PEP</b></td>
										<td height="35" align="center" class="tableHeader"><b>Verisys Done</b></td>
										<td height="35" align="center" class="tableHeader"><b>Action</b></td>
									</tr>
								</thead>
								<tbody>
							<c:forEach items="${level2AccountModel.acOwnershipDetailModelList}" var="acOwnershipDetailModel" varStatus="iterationStatus">
								<tr>
									<%-- <td>
										${iterationStatus.index}
									</td> --%>
									<td align="center">
										<html:hidden path="acOwnershipDetailModelList[${iterationStatus.index}].acOwnershipDetailId"/>
										<html:hidden path="acOwnershipDetailModelList[${iterationStatus.index}].createdBy"/>
										<html:hidden path="acOwnershipDetailModelList[${iterationStatus.index}].createdOn"/>
										<html:hidden path="acOwnershipDetailModelList[${iterationStatus.index}].updatedBy"/>
										<html:hidden path="acOwnershipDetailModelList[${iterationStatus.index}].updatedOn"/>
										<html:hidden path="acOwnershipDetailModelList[${iterationStatus.index}].isDeleted"/>
										<html:hidden path="acOwnershipDetailModelList[${iterationStatus.index}].versionNo"/>
										<html:select cssStyle="max-width:100px;" path="acOwnershipDetailModelList[${iterationStatus.index}].acOwnershipTypeId" cssClass="textBox">
											<html:option value="">--All--</html:option>
											<html:options items="${ownerShipTypeList}" itemLabel="label" itemValue="value"/>
										</html:select>
									</td>
									<td align="center">
										<html:input cssStyle="max-width:130px;" path="acOwnershipDetailModelList[${iterationStatus.index}].name" cssClass="textBox" maxlength="50" onkeypress="return maskAlphaWithSp(this,event)" />
									</td>
									<td align="center">
										<html:select cssStyle="max-width:100px;" path="acOwnershipDetailModelList[${iterationStatus.index}].idDocumentType" cssClass="textBox">
											<html:option value="">[Select]</html:option>
											<html:options items="${documentTypeList}" itemLabel="label" itemValue="value"/>
										</html:select>
									</td>
									<td align="center">
										<html:input cssStyle="max-width:130px;" path="acOwnershipDetailModelList[${iterationStatus.index}].idDocumentNo" cssClass="textBox" maxlength="13"/>
									</td>

									<td align="center">
									<html:input cssStyle="max-width:80px;" path="acOwnershipDetailModelList[${iterationStatus.index}].dateOfBirth" cssClass="textBox" 
											maxlength="50" />
									</td>
									<td align="center" >
										<html:checkbox path="acOwnershipDetailModelList[${iterationStatus.index}].ofac" />
									</td>
									<td align="center">
										<%-- <html:checkbox path="acOwnershipDetailModelList[${iterationStatus.index}].pep" tabindex="8" /> --%>
										<html:select cssStyle="max-width:80px;" path="acOwnershipDetailModelList[${iterationStatus.index}].pep" cssClass="textBox">
										<c:if test="${binaryOpt != null}">
											<html:options items="${binaryOpt}" itemLabel="label" itemValue="value" />
				 						</c:if>
										</html:select>
									</td>
									<td align="center">
										<html:checkbox path="acOwnershipDetailModelList[${iterationStatus.index}].verisysDone" />
									</td>
									<td>
										<input type="button" id="acOwnershipDetailModelList${iterationStatus.index}.add" class="addRow" value="+" style="font-weight:bold;padding: 0px 12px;height: 29px;font-size:20px;" title="Add row" />
										<input type="button" id="acOwnershipDetailModelList${iterationStatus.index}.remove" class="removeRow" value="-" title="Remove row" style="background-color:#E40606 !important;color:#FFFCFC !important;font-weight:bold;padding: 0px 12px;height: 29px;font-size:20px;" />	
									</td>
								</tr>
							</c:forEach>
						</tbody>
						</table> 
						</div>
						</td>
						</tr>
						
					<!-- 	<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>Account Opening Form:</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%"></td>
							<td height="16" align="right" class="formText" width="25%"></td>
							<td align="left" class="formText" width="25%"></td>
						</tr> -->
						<tr>
							<td colspan="4">
								<h2>Relationship Details <small>(to be filled by Bank Staff)</small></h2>
							</td>
						</tr>
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">
								<span style="color: #FF0000">*</span>Emp ID:
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input path="empId" cssClass="textBox" style="background: #D3D3D3; font-size:14px;font-weight: bold;" readonly="true"/>
								</td>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">
								<span style="color: #FF0000">*</span>Name of Employee:
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input path="employeeName" cssClass="textBox" style="background: #D3D3D3; font-size:14px;font-weight: bold;"  readonly="true"/>
							</td>
						</tr>
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>Date of Account Opening:</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input id="accountOpeningDate" path="kycOpeningDate" style="background: #D3D3D3; font-size:14px;font-weight: bold;" readonly="true" cssClass="textBox" 
									maxlength="50" /> </td>
							<td height="16" align="right" class="formText" width="25%"></td>
							<td align="left" class="formText" width="25%"></td>
						</tr>
						
						<c:if test="${not empty level2AccountModel.appUserId}">
                		<tr>
							<td align="right" bgcolor="F3F3F3" class="formText">
								<span style="color: #FF0000">*</span>Registration State:
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText">
								<c:choose>
								<c:when test="${level2AccountModel.registrationStateId==3}">
								<div class="textBox" style="background: #D3D3D3; font-size:14px; ">Approved</div>
									<html:hidden path="registrationStateId" />	
								</c:when>
								<c:when test="${level2AccountModel.registrationStateId==5}">
								<div class="textBox" style="background: #D3D3D3; font-size:14px; ">Decline</div>
									<html:hidden path="registrationStateId" />	
								</c:when>
								<c:when test="${level2AccountModel.registrationStateId==6}">
								<div class="textBox" style="background: #D3D3D3; font-size:14px; ">Rejected</div>
									<html:hidden path="registrationStateId" />	
								</c:when>
								<c:otherwise>					
									<html:select path="registrationStateId" cssClass="textBox" id="registrationStateId"
										 onchange="regStateChange(this);">
										<html:option value="">[Select]</html:option>
											<c:if test="${regStateList != null}">
												<html:options items="${regStateList}" itemLabel="name" itemValue="registrationStateId" />
					 						</c:if>
									</html:select>
								</c:otherwise>
							</c:choose>
							</td>
							
							<c:if test="${not empty level2AccountModel.appUserId}">
						
                				<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Comments:
								</td>
								<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:textarea style="height:50px;" path="regStateComments" tabindex="1" cssClass="textBox"/>
								</td>
						
							</c:if>
						</tr>
						<tr id="screeningRow">
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span id="screeningMandatory" style="color: #FF0000">*</span>Screening:
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:radiobuttons tabindex="27" class="screening" items="${screeningList}" itemLabel="label" itemValue="value" path="screeningPerformed" />
							</td>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span style="color: #FF0000">*</span>NADRA Verification:
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:radiobuttons class="verisys" tabindex="27" items="${nadraVerList}" itemLabel="label" itemValue="value" path="isVeriSysDone" />
							</td>
						</tr>
						
							
						</c:if>
					
				</table>
    				</div>
				    <div class="center">
				   
				    </div>
    				<div style="clear:both"></div>
				</div>
					<table>
							<tr>
								<td width="100%" align="left" bgcolor="FBFBFB" colspan="4">
								<c:if test="${not empty level2AccountModel.initialAppFormNo}">
											<c:choose>
												<c:when test="${not empty level2AccountModel.appUserId}">
													<authz:authorize ifAnyGranted="<%=updatePermission%>">
														<input type="submit" class="button" value="Update" tabindex="53" />&nbsp;
													</authz:authorize>
													<input type="button" class="button" value="Cancel" tabindex="54" onclick="javascript: window.location='p_pgsearchuserinfo.html?actionId=<%=PortalConstants.ACTION_RETRIEVE%>'" />
												</c:when>
												<c:otherwise>
													<authz:authorize ifAnyGranted="<%=createPermission%>">
														<input type="submit" class="button" value="Create Customer Account" tabindex="53"  />
														<input type="button" class="button" value="Cancel" tabindex="54" onclick="javascript: window.location='home.html'"/>
													</authz:authorize>
													<authz:authorize ifNotGranted="<%=createPermission%>">
														<input type="button" class="button"
															value="Create Customer Account" tabindex="-1"
															disabled="disabled" />
														<input type="button" class="button" value="Cancel" onclick="javascript: window.location='home.html'"
															tabindex="55" />
													</authz:authorize>
												</c:otherwise>
											</c:choose>
								</c:if>
										</td>
									</tr>
								</table>
			</html:form>
		<ajax:updateField parser="new ResponseXmlParser()" source="taxRegimeId" eventType="change" action="taxRegimeId" target="fed" baseUrl="${contextPath}/p_loadfed.html" parameters="taxRegimeId={taxRegimeId}" ></ajax:updateField>
		<form action="${contextPath}/p_pgsearchuserinfo.html" method="post"
			name="userInfoListViewForm" id="userInfoListViewModel">
			<input type="hidden"
				value="<c:out value="${level2AccountModel.searchMfsId}"/>"
				id="userId" name="userId" />
			<input type="hidden"
				value="<c:out value="${level2AccountModel.searchFirstName}"/>"
				id="name" name="firstName" />
			<input type="hidden"
				value="<c:out value="${level2AccountModel.searchLastName}"/>"
				id="lastName" name="lastName" />
			<input type="hidden"
				value="<c:out value="${level2AccountModel.searchNic}"/>" id="nic"
				name="nic" />
				
			<input type="hidden"
				value="<c:out value="${level2AccountModel.customerAccountTypeId}"/>" id="customerAccountTypeId"
				name="customerAccountTypeId" />
		</form>
		<script language="javascript" type="text/javascript">


<%-- //The following code is written because 'Others' value was selected dy default.
var isSubmit = '<%= request.getParameter("_save") %>';
var isAction = '<%= request.getParameter("actionId") %>'; --%>

function popupwindow(url, title, w, h) {
	  var left = (screen.width/2)-(w/2);
	  var top = (screen.height/2)-(h/2);
	  return window.open(url, title, 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes, copyhistory=no, width='+w+', height='+h+', top='+top+', left='+left);
	} 

function isNumberKey(evt){
    var charCode = (evt.which) ? evt.which : event.keyCode
    if (charCode > 31 && (charCode < 48 || charCode > 57))
        return false;
    return true;
}

function toggleVisaExpiry(){
	var selectedOption = document.getElementById("applicant1DetailModel_idType");
	if(selectedOption.value == 3){
		document.getElementById("visaExpiry").style.cssText = "cursor:pointer; margin-left:3px;";
		document.getElementById("visaExpiry2").style.cssText = "cursor:pointer; margin-left:3px;";
	}else{
		document.getElementById("visaExpiry").style.display="none";
		document.getElementById("visaExpiry2").style.display="none";
		document.getElementById("applicant1DetailModel_visaExpiryDate").value="";
	}
}

function checkOthersValue(comboBoxId,fieldId){
	var comboBox = document.getElementById(comboBoxId);
	var field = document.getElementById(fieldId);
	var index = comboBox.length -1;
	if(comboBox != null)
	comboBox.options[index].selected = 'selected';
	if(field != null)
	field.style.display = 'inline'; 
}

      <c:choose>
		<c:when test="${not empty level2AccountModel.appUserId}">
        </c:when>
        <c:otherwise>
	      function onCancel(a, b)
          {
             document.forms[0].reset();
          }
      	</c:otherwise>
      </c:choose>
      
      function customTrim(str){
      if(str != null){
	      while(strVar.charAt(strVar.length-1)==' ')
			strVar=strVar.substring(0,strVar.length-1);
	
		while(strVar.charAt(0)==' ')
			strVar=strVar.substring(1,strVar.length);
		}
		return str;      
      }
      function validateComboWithOthers(selectBoxId,othersId,fieldName){

		    return true;
		  }
		  
		function calculate_age(birth_month,birth_day,birth_year){
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

	  function isDobGreaterThan60Years(dateOfBirth){
		if(trim(dateOfBirth) != null && dateOfBirth != '' && dateOfBirth.length == 10){
			dateOfBirth = dateOfBirth.split("/");
	  		var birthDay = dateOfBirth[0];
	  		var birthMonth = dateOfBirth[1];
	  		var birthYear = dateOfBirth[2];
	  		var age = calculate_age(birthMonth,birthDay,birthYear);
			if(age > 60){
				return true;
			}
	  	}	
	  }
      function submitForm(theForm)
      {
      if(getFileSize(theForm.applicant1DetailModel_customerPic) > 2) {
      	alert("Applicant 1 Customer Picture can't be more than 2MB in size.");
      	return false;
      }

      if(getFileSize(theForm.applicant1DetailModel_tncPic) > 2) {
      	alert("Applicant 1 Terms and Condition can't be more than 2MB in size.");
      	return false;
      }

      if(getFileSize(theForm.applicant1DetailModel_signPic) > 2) {
      	alert("Applicant 1 Signature Picture can't be more than 2MB in size.");
      	return false;
      }

      if(getFileSize(theForm.applicant1DetailModel_cnicFrontPic) > 2) {
      	alert("Applicant 1 CNIC Front Picture can't be more than 2MB in size.");
      	return false;
      }

      if(getFileSize(theForm.applicant1DetailModel_cnicBack) > 2) {
      	alert("Applicant 1 CNIC Back Picture can't be more than 2MB in size.");
      	return false;
      }

      var isNewCustomer = true;
      if(${not empty level2AccountModel.appUserId && not empty level2AccountModel.appUserId}){
      	var isNewCustomer = false;
      }

		var currServerDate = '<%=PortalDateUtils.currentFormattedDate("dd/MM/yyyy")%>';
   		if( trim(theForm.applicant1DetailModel_dob.value) != '' && isDateGreater(theForm.applicant1DetailModel_dob.value,currServerDate)){
			alert('Future date of birth is not allowed.');
			document.getElementById('dobDate1').focus();
			return false;
   		}else if( isAgeLessThan18Years(theForm.applicant1DetailModel_dob.value)){
			alert('Customer age cannot be less than 18 years');
			document.getElementById('dobDate1').focus();
			return false;
		}
   		else if(!isValidDate(theForm.applicant1DetailModel_dob.value,"Date of Birth")){
   			document.getElementById('dobDate1').focus();
   			return false;
   		}
   		
		
   		if( trim(theForm.applicant1DetailModel_idExpiryDate.value) != '' && isDateSmaller(theForm.applicant1DetailModel_idExpiryDate.value,currServerDate)){
			alert('ID Expiry should not be less than current date.');
			document.getElementById('applicant1DetailModel_idExpiryDate').focus();
			return false;
		}
   		else if(!isValidDate(theForm.applicant1DetailModel_idExpiryDate.value,"Date of ID Expiry")){
   			document.getElementById('applicant1DetailModel_idExpiryDate').focus();
   			return false;
   		}
   		
   		if(trim(theForm.applicant1DetailModel_visaExpiryDate.value) != '' && (!isValidDate(theForm.applicant1DetailModel_visaExpiryDate.value,"Date of Visa Expiry"))){
   			document.getElementById('applicant1DetailModel_visaExpiryDate').focus();
   			return false;
   		}
		
   		
      	var isValid = true;
		if(
		!validateComboWithOthers('presentAddDistrictId','presentDistOthers','Present Home District/Tehsil/Town')
		||!validateComboWithOthers('presentAddCityId','presentCityOthers','Present Home City')
		|| !validateComboWithOthers('permanentAddDistrictId','permanentDistOthers','Permanent Home District/Tehsil/Town')
		|| !validateComboWithOthers('permanentAddCityId','permanentAddCityOthers','Permanent Home City')
		|| !validateComboWithOthers('fundsSourceId','fundSourceOthers','Source of Funds')
		){
			return false;
		}	
		if(!validateFormChar(theForm)){
      		return false;
      	}
      	//submitting form
        if (confirm('Are you sure you want to proceed?')==true){
          return true;
        }else{
          return false;
        }
  }

	function onFormSubmit(theForm) {
				
      			var theForm = document.forms.level2AccountForm;
      			 //check date of birth of ownership detail's persons
      			var dateCheck=true;
    		    jq('#acOwnershipDetailTable tbody tr').each( function() {
    				var selectArray = jq(this).find('input');
    				if(selectArray[9].value.indexOf("/")>0){
    					selectArray[9].style.borderColor="";
    					if( isAgeLessThan18Years(selectArray[9].value)){
    						alert('The person in ownership section cannot be less than 18 years');
    						dateCheck = false;
    						//selectArray[9].style.borderColor="red";
    						return false;
    					}
    					else if(!isValidDate(selectArray[9].value,"in ownership section")){
    						dateCheck = false;
    			   			return false;
    			   		}
    				}
    				});
    		    
    		    if(!dateCheck){
    		    	return false;
    		    }
    		    
    		    /* var rowCheck = true;
    		    jq('#acOwnershipDetailTable tbody tr').each( function() {
        	        var row = jq(this).closest('tr');
        	        var slct = row.find('select');
        	        if(slct[0].value==""){
        	        	//alert("Please select Account Ownership Type.");
        	        	rowCheck = false;
        	        	return false;
        	        }
        	        var selectArray = row.find('input');
    				if(selectArray[7].value == ""){ //name
        				//alert("Please enter Name from ownership detail section.");
        				rowCheck = false;
        				return false;
        			}
    				
    				if(slct[1].value==""){
        	        	//alert("Please select ID Document Type.");
        	        	rowCheck = false;
        	        	return false;
        	        }
    				if(selectArray[8].value == ""){ //id number
        				//alert("Please enter the ID Document Number in ownership detail section");
        				rowCheck = false;
        				return false;
        			}
        	        if(selectArray[9].value == ""){ //dob
        				//alert("Please enter the Date of Birth in ownership detail section");
        				rowCheck = false;
        				return false;
        			}
    		    });
    		    
    		    var accountPurposeId = document.getElementById("accountPurposeId").value;
    		    if(!rowCheck && accountPurposeId != "" && accountPurposeId == 1){
    		    	alert("Please fill all the mandatory fields in Account Ownership Detail Section.");
    		    	return false;
    		    } */
    		    
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
    		    	alert('Additional Information for Corporate/Business Accounts fields missing');
    		    	return false;
    		    }
    		    
    			if(doRequired( theForm.accountPurposeId, 'Purpose of Account' )
    				&& doRequired( theForm.customerAccountTypeId, 'Type of Account' )
    				//&& doRequired( theForm.businessTypeId, 'Customer/Entities Type' )
    				&& doRequired( theForm.accountNature, 'Nature of Account' )
      				&& doRequired( theForm.segmentId, 'Segment' )
      				&& doRequired( theForm.customerAccountName, 'Account Title' )
      				&& doRequired( theForm.currency, 'Currency' )
      				&& doRequired( theForm.taxRegimeId, 'Tax Regime' )
      				
      				&& doRequired( theForm.productCatalogId, 'Product Catalog' )
      				
      				&& doRequired( theForm.fed, 'FED')
//     				&& doRequired( theForm.applicant1DetailModel_title, 'Title' )
      				//&& doRequired( theForm.applicant1DetailModel_usCitizen, 'US Citizen' )
//      				&& doRequiredGroup( 'applicant1DetailModel_usCitizen', 'US Citizen' )
		      		&& doRequired( theForm.mobileNo, 'Mobile No.' ) 
		      		&& isValidMinLength( theForm.mobileNo, 'Mobile No.', 11 )
		      		&& isValidMinLength(theForm.applicant1DetailModel_landLineNo, 'Phone Number', 11)
		      		&& isValidMobileNo( theForm.mobileNo)
		      		
		      		&& doRequired( theForm.applicant1DetailModel_name, 'Applicant Name' ) 
 //					&& doRequired( theForm.applicant1DetailModel_motherMaidenName, 'Mother Maiden Name' )
//		      		&& doRequired( theForm.applicant1DetailModel_occupation, 'Applicant Occupations' )
		      		&& doRequired( theForm.applicant1DetailModel_idType, 'Applicant ID Document Type' )
		      		&& doRequired( theForm.applicant1DetailModel_idNumber, 'Applicant ID Document Number' )
		      		//&& isValidMinLength(theForm.applicant1DetailModel_idNumber, 'Applicant ID Document Number', 50)
					&& doRequired( theForm.applicant1DetailModel_idExpiryDate, 'Applicant ID Expiry' ) 
					&& doRequired( theForm.applicant1DetailModel_dob, 'Applicant Date of Birth' )
//					&& doRequired( theForm.applicant1DetailModel_birthPlace, 'Applicant Place of Birth' ) 
//					&& doRequiredGroup( 'applicant1DetailModel_gender', 'Applicant Gender' ) 
//					&& doRequiredGroup( 'applicant1DetailModel_residentialStatus', 'Residential Status' )
//					&& doRequiredGroup( 'applicant1DetailModel_mailing', 'Mailing Address' )
					//&& doRequired( theForm.applicant1DetailModel_buisnessAddress, 'Applicant Business Address' ) 
					//&& doRequired( theForm.applicant1DetailModel_presentAddHouseNo, 'Applicant Residential Address' )
					&& validateRequiredMailingAddress('applicant1DetailModel_mailing')
//					&& doRequired( theForm.applicant1DetailModel_nationality, 'Nationality' )
//					&& doRequired( theForm.applicant1DetailModel_fatherHusbandName, 'Father/Husband name' )
//					&& doRequired( theForm.applicant1DetailModel_employerName, 'Name of Employer/Business' )
//					&& doRequired( theForm.applicant1DetailModel_maritalStatus, 'Marital Status' )
//					&& doRequired( theForm.nokDetailVOModel_nokName, 'Next of KIN name' )
//					&& doRequired( theForm.nokDetailVOModel_nokRelationship, 'Relationship with KIN' )
//					&& doRequired( theForm.nokDetailVOModel_nokContactNo, 'Telephone/Mobile No. of Next of KIN' )
					&& isValidMinLength( theForm.nokDetailVOModel_nokContactNo, 'Telephone/Mobile No. of Next of KIN',11 )
//					&& doRequired( theForm.nokDetailVOModel_nokIdType, 'ID Document Type of KIN' )
//					&& doRequired( theForm.nokDetailVOModel_nokIdNumber, 'ID Document Number of KIN' )
//					&& doRequired( theForm.nokDetailVOModel_nokMailingAdd, 'Next of KIN Address' )
					&& isValidMinLength(theForm.mobileNo, 'Mobile No.', 11)
					&& isValidEmail(theForm.applicant1DetailModel_email, 'Applicant Email')
                    && isValidMinLength(theForm.applicant1DetailModel_contactNo, 'Applicant Home Number', 11)
                    && isValidMinLength(theForm.applicant1DetailModel_fax, 'Applicant Fax Number', 11)
                    ){
						
					}else {
						return false;
					}

				//for main appliant
				var appUser = document.getElementById("appUserId").value;
    			if(appUser == undefined || appUser == "" || appUser == 0){
					var applicant1DetailModel_idType = document.getElementById('applicant1DetailModel_idType');
	    			var fieldValue = applicant1DetailModel_idType.options[applicant1DetailModel_idType.selectedIndex].text;
	    			var idNumber = document.getElementById('applicant1DetailModel_idNumber');
					if(!checkDocumentIDLength(applicant1DetailModel_idType,fieldValue,idNumber)) {
						return false;
					}
    			}
				//for next of KIN
    			var nok_idType = document.getElementById('nokDetailVOModel_nokIdType');
    			var nok_fieldValue = theForm.nokDetailVOModel_nokIdType.options[theForm.nokDetailVOModel_nokIdType.selectedIndex].text;
    			var nok_idNumber = document.getElementById('nokDetailVOModel_nokIdNumber');
    			
    			if(nok_fieldValue!=undefined && nok_fieldValue!='[Select]' && (nok_idNumber.value==undefined || nok_idNumber.value==''))
				{
					alert("Must provide NOK ID Document Number for selected NOK ID Document Type");
					document.getElementById("nokDetailVOModel_nokIdNumber").focus();
					return false;
				}
				
				if(nok_idNumber.value!=undefined && nok_idNumber.value!='' && (nok_fieldValue==undefined || nok_fieldValue=='[Select]'))
				{
					alert("NOK ID Document Type is not provided");
					document.getElementById("nokDetailVOModel_nokIdType").focus();
					return false;
				}
				
				if(!checkDocumentIDLength(nok_idType,nok_fieldValue,nok_idNumber)) {
					return false;
				}
				
      			if(!isValidFED()){
      				alert("Please input a valid FED amount.");
      				document.getElementById("fed").focus();
      				return false;
      			}
    			
      			
      			var appUserId = document.getElementById("appUserId").value;

				if(appUserId != undefined && appUserId != "" && appUserId!= -1)
		       	{
		       		if(doRequired( theForm.registrationStateId, 'Registration State' )){
		        	}
		       		else
		       		{
		        		return false;
		        	}
				}
				
				if(appUserId != "" && appUserId != undefined){
					if( doRequiredGroup( 'verisys', 'NADRA Verification' ) ){
						
					}else{
						return false;
					}
			       
				}
				
		       if(appUserId != "" && appUserId!=-1 && document.getElementById('registrationStateId')!=undefined
		    		   && document.getElementById('registrationStateId').value=="3")
				{
		    	   if( doRequiredGroup( 'screening', 'Screening Matched' ) ){
						
					}else{
						return false;
					}
		    	}

	     		if(document.getElementById('accountPurposeId').value != '')
	     			theForm.accountPurpose.value = theForm.accountPurposeId.options[theForm.accountPurposeId.selectedIndex].innerHTML;
	     		else
	     			theForm.accountPurpose.value = '';
	     		if(document.getElementById('taxRegimeId'))
	     			theForm.taxRegimeName.value = theForm.taxRegimeId.options[theForm.taxRegimeId.selectedIndex].innerHTML;
	     		if(document.getElementById('segmentId'))
	     			theForm.segmentName.value = theForm.segmentId.options[theForm.segmentId.selectedIndex].innerHTML;
	     		if(document.getElementById('accountNature'))
	     			theForm.acNatureName.value = theForm.accountNature.options[theForm.accountNature.selectedIndex].innerHTML;
	     		
	     		if(appUserId != "" && appUserId != undefined) {
	     			theForm.accounttypeName.value = document.getElementById('customerAccountTypeName').innerHTML;
	     		}else{
	     			if(document.getElementById('customerAccountTypeId'))
	     				theForm.accounttypeName.value = theForm.customerAccountTypeId.options[theForm.customerAccountTypeId.selectedIndex].innerHTML;
	     		}
	     				
	     		if(document.getElementById('applicant1DetailModel_title')){
	     			//theForm.applicant1DetailModel_titleTxt.value = theForm.applicant1DetailModel_title.options[theForm.applicant1DetailModel_title.selectedIndex].innerHTML;
	     			 if(theForm.applicant1DetailModel_title.selectedIndex>0)
                        theForm.applicant1DetailModel_titleTxt.value = theForm.applicant1DetailModel_title.options[theForm.applicant1DetailModel_title.selectedIndex].innerHTML;
                    else
                        theForm.applicant1DetailModel_titleTxt.value = "";
				}
	     		if(appUserId != "" && appUserId != undefined){
	     			theForm.applicant1DetailModel_idTypeName.value = document.getElementById("divIdTypeName").innerHTML;
	     		}else{
	     			if(document.getElementById('applicant1DetailModel_idType'))
		     			theForm.applicant1DetailModel_idTypeName.value = theForm.applicant1DetailModel_idType.options[theForm.applicant1DetailModel_idType.selectedIndex].innerHTML;	     			
	     		}
	     		
	     		if(document.getElementById('applicant1DetailModel_occupation') && document.getElementById('applicant1DetailModel_occupation').value != '')
	     			theForm.applicant1DetailModel_occupationName.value = theForm.applicant1DetailModel_occupation.options[theForm.applicant1DetailModel_occupation.selectedIndex].innerHTML;
	     		
	     		if(document.getElementById('applicant1DetailModel_profession').value != '')
	     			theForm.applicant1DetailModel_professionName.value = theForm.applicant1DetailModel_profession.options[theForm.applicant1DetailModel_profession.selectedIndex].innerHTML;
	     		else
	     			theForm.applicant1DetailModel_professionName.value = '';
	     		
	     		if(document.getElementById('applicant1DetailModel_maritalStatus') && document.getElementById('applicant1DetailModel_maritalStatus').value != '')
	     			theForm.applicant1DetailModel_maritalStatusName.value = theForm.applicant1DetailModel_maritalStatus.options[theForm.applicant1DetailModel_maritalStatus.selectedIndex].innerHTML;
	     		
	     		if(document.getElementById('applicant1DetailModel_residentialCity').value != ''){
	     			theForm.applicant1DetailModel_residentialCityName.value = theForm.applicant1DetailModel_residentialCity.options[theForm.applicant1DetailModel_residentialCity.selectedIndex].innerHTML;
	     		}else{
	     			theForm.applicant1DetailModel_residentialCityName.value = '';
	     		}
	     			
	     		if(document.getElementById('applicant1DetailModel_buisnessCity').value != ''){
	     			theForm.applicant1DetailModel_buisnessCityName.value = theForm.applicant1DetailModel_buisnessCity.options[theForm.applicant1DetailModel_buisnessCity.selectedIndex].innerHTML;
	     		}else{
	     			theForm.applicant1DetailModel_buisnessCityName.value = '';
	     		}
	     				
	     		if(document.getElementById('nokDetailVOModel_nokIdType') && document.getElementById('nokDetailVOModel_nokIdType').value != '')
	     			theForm.nokDetailVOModel_nokIdTypeName.value = theForm.nokDetailVOModel_nokIdType.options[theForm.nokDetailVOModel_nokIdType.selectedIndex].innerHTML;		
				
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
		
				var rowCheck = true;
    		    jq('#acOwnershipDetailTable tbody tr').each( function() {
        	        var row = jq(this).closest('tr');
        	        var slct = row.find('select');
        	        if(slct[0].value==""){
        	        	//alert("Please select Account Ownership Type.");
        	        	rowCheck = false;
        	        	return false;
        	        }
        	        var selectArray = row.find('input');
    				if(selectArray[7].value == ""){ //name
        				//alert("Please enter Name from ownership detail section.");
        				rowCheck = false;
        				return false;
        			}
    				
    				if(slct[1].value==""){
        	        	//alert("Please select ID Document Type.");
        	        	rowCheck = false;
        	        	return false;
        	        }
    				if(selectArray[8].value == ""){ //id number
        				//alert("Please enter the ID Document Number in ownership detail section");
        				rowCheck = false;
        				return false;
        			}
        	        if(selectArray[9].value == ""){ //dob
        				//alert("Please enter the Date of Birth in ownership detail section");
        				rowCheck = false;
        				return false;
        			}
    		    });
    		    
    		    var accountPurposeId = document.getElementById("accountPurposeId").value;
    		    if(!rowCheck && null != accountPurposeId && accountPurposeId != "" && accountPurposeId == 1){
    		    	alert("Please fill all the mandatory fields in Account Ownership Detail Section.");
    		    	return false;
    		    }		
		       
	   if(submitForm(theForm)){
			return true;
		}else{
			return false;
		}
      }
      
      function doRequired( field, label )
      {
    	if(field == null || field == undefined){
    		return true;
    	}  
    	  
      	if( field.value.trim() == '' || field.value.length == 0 )
      	{
      		alert( label + ' is required field.' );
      		field.focus();
      		return false;
      	}
      	return true;
      }
      
      function doRequiredGroup( className, label )
      {
      	selected = false;
      	var element;
      	var elements = document.getElementsByClassName(className);
		for(i=0; i<elements.length; i++) {
			if(elements[i].checked == false){
				selected = false;
				element = elements[i];
			}else{
				return true;
			}
		}
		if(selected == false){
			alert( label + ' is required field.' );
			element.focus();
		}
      	return selected;
      }
      
      function isValidMinLength( field, label, minlength )
      {
    /* 	if(field.type=="hidden"){
    		return true;
    	}  */ 
      	if( field.value != '' && field.value.length < minlength )
      	{
      		alert( label + ' cannot be less than ' + minlength + ' digits' );
      		field.focus();
      		return false;
      	}
      	return true;
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
      //This method is a copy of code from submitForm() method
      function isValidFormForPrinting()
	  {
		var theForm = document.forms.level2AccountForm;
		if( doRequired( theForm.name, 'Title of Account' ) && doRequired( theForm.presentAddHouseNo, 'Mailing Address' )
			&& doRequired( theForm.country, 'Country' ) && doRequired( theForm.presentAddCityId, 'City/District' )
			&& doRequired( theForm.presentAddPostalOfficeId, 'Postal Code' ) && doRequired( theForm.customerAccountTypeId, 'Account Type' ) && doRequired( theForm.mobileNo, 'Mobile No' ) 
			&& doRequired( theForm.accountNature, 'Nature of Account' ) && doRequired( theForm.segmentId, 'Segment' ) && doRequired( theForm.registrationStateId, 'Registration State' )  )
			{
					var currServerDate = '<%=PortalDateUtils.currentFormattedDate("dd/MM/yyyy")%>';
			   		if( trim(theForm.applicant1DetailModel.dob.value) != '' && isDateGreater(theForm.applicant1DetailModel.dob.value,currServerDate)){
						alert('Future date of birth is not allowed.');
						document.getElementById('dobDate1').focus();
						return false;
			   		}else if( isAgeLessThan18Years(theForm.applicant1DetailModel.dob.value)){
						alert('Customer age cannot be less than 18 years');
						document.getElementById('dobDate1').focus();
						return false;
					}
					
					if(!isDobGreaterThan60Years(theForm.applicant1DetailModel.dob.value) ){
						if(theForm.applicant1DetailModel.idExpiryDate.value == null || trim(theForm.applicant1DetailModel.idExpiryDate.value) == ''){
							alert('ID Expiry is a required field.');
							return false;
						}
					}
					
			   		if( trim(theForm.applicant1DetailModel.idExpiryDate.value) != '' && isDateSmaller(theForm.applicant1DetailModel.idExpiryDate.value,currServerDate)){
						alert('ID Expiry should not be less than current date.');
						document.getElementById('idExpiryDate').focus();
						return false;
					}
					
					
					
					
					if( trim(theForm.applicant2DetailModel.dob.value) != '' && isDateGreater(theForm.applicant2DetailModel.dob.value,currServerDate)){
						alert('Future date of birth is not allowed.');
						document.getElementById('dobDate2').focus();
						return false;
			   		}else if( isAgeLessThan18Years(theForm.applicant2DetailModel.dob.value)){
						alert('Customer age cannot be less than 18 years');
						document.getElementById('dobDate2').focus();
						return false;
					}
					
					if(!isDobGreaterThan60Years(theForm.applicant2DetailModel.dob.value) ){
						if(theForm.applicant2DetailModel.idExpiryDate.value == null || trim(theForm.applicant2DetailModel.idExpiryDate.value) == ''){
							alert('ID Expiry is a required field.');
							return false;
						}
					}
					
			   		if( trim(theForm.applicant2DetailModel.idExpiryDate.value) != '' && isDateSmaller(theForm.applicant2DetailModel.idExpiryDate.value,currServerDate)){
						alert('ID Expiry should not be less than current date.');
						document.getElementById('idExpiryDate').focus();
						return false;
					}
					
					if( trim(theForm.applicant3DetailModel.dob.value) != '' && isDateGreater(theForm.applicant3DetailModel.dob.value,currServerDate)){
						alert('Future date of birth is not allowed.');
						document.getElementById('dobDate3').focus();
						return false;
			   		}else if( isAgeLessThan18Years(theForm.applicant3DetailModel.dob.value)){
						alert('Customer age cannot be less than 18 years');
						document.getElementById('dobDate3').focus();
						return false;
					}
					
					if(!isDobGreaterThan60Years(theForm.applicant3DetailModel.dob.value) ){
						if(theForm.applicant3DetailModel.idExpiryDate.value == null || trim(theForm.applicant3DetailModel.idExpiryDate.value) == ''){
							alert('ID Expiry is a required field.');
							return false;
						}
					}
					
			   		if( trim(theForm.applicant3DetailModel.idExpiryDate.value) != '' && isDateSmaller(theForm.applicant3DetailModel.idExpiryDate.value,currServerDate)){
						alert('ID Expiry should not be less than current date.');
						document.getElementById('idExpiryDate').focus();
						return false;
					}
					
					
					var cnicVal = theForm.idNumber.value;
					if(cnicVal != null){
						if( trim(cnicVal) != '' && cnicVal.length == 15){
							var firstHyphin = cnicVal.substring(5,6);
							var secondHyphin = cnicVal.substring(13,14);
							if(firstHyphin != '-' || secondHyphin != '-'){
							alert('CNIC No is not in proper format. Format should be #####-#######-#');
							document.getElementById('nic').focus();
							return false;
							}
						}
					}
					
			      	var isValid = true;
					if(!validateFormChar(theForm)){
			      		return false;
			      	}
		      	return true;
			}
			return false;
		}
		
		function selFundSource(sel){
			if(sel.value == 10){
				document.getElementById('fundSourceNarration').style.display = '';
			} else {
				document.getElementById('fundSourceNarration').style.display = 'none';
			}
		}
		
		function selTxMode(sel){
			if(sel!=undefined && sel.value == 5){
				document.getElementById('otherTxMode').style.display = '';
			} else {
				document.getElementById('otherTxMode').style.display = 'none';
			}
		}
		
		function acTypeInfo(sel){
			if(sel.value == 1){
				var pob = document.getElementsByClassName('pob');
				for(i=0; i<pob.length; i++) {
					pob[i].style.display = 'none';
				}
				var cols = document.getElementsByClassName('higherAC');
				for(i=0; i<cols.length; i++) {
					cols[i].style.display = 'none';
				}
			}else if(sel.value == 2){
				var pob = document.getElementsByClassName('pob');
				for(i=0; i<pob.length; i++) {
					pob[i].style.display =    '';
				}
				var cols = document.getElementsByClassName('higherAC');
					  for(i=0; i<cols.length; i++) {
					    cols[i].style.display =    'none';
					  }
			}else{
				var pob = document.getElementsByClassName('pob');
				for(i=0; i<pob.length; i++) {
					pob[i].style.display =    '';
				}
				var cols = document.getElementsByClassName('higherAC');
					  for(i=0; i<cols.length; i++) {
					    cols[i].style.display =    '';
					  }
			}
		}
		
	function isValidMinLength( field, label, minlength )
      {
      	if( field.value != '' && field.value.length < minlength )
      	{
      		alert( label + ' cannot be less than ' + minlength + ' digits' );
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
      			return false;
      		}
      	}
       	return true;
      }
      

      
		// ON CHANGE Customer/Entities Type:
		function changeEntityType(selectBox)
    	{
			var op = selectBox.options[selectBox.selectedIndex];
			var optgroup = op.parentNode;
			if(optgroup.label!=undefined && optgroup.label=="Corporate")
			{
				customerEntityTypeCorporate	=	true;
				$('strRegPlace').setStyle({
					display: 'inline'
				});
				$('strRegNum').setStyle({
					display: 'inline'
				});
				$('strRegDate').setStyle({
					display: 'inline'
				});
			}
			else
			{
				customerEntityTypeCorporate=false;

				$('strRegPlace').setStyle({
					display: 'none'
				});
				$('strRegNum').setStyle({
					display: 'none'
				});
				$('strRegDate').setStyle({
					display: 'none'
				});

			}
   		}
      
		 function validateRequiredMailingAddress(className)
		  {
		        var elements = document.getElementsByClassName(className);
		   
		   if(elements[0].checked  == true){ 
		    if(trim(document.getElementById("applicant1DetailModel_presentAddHouseNo").value)== ''){
		     alert("Residential Address is required field.");
		     return false;
		    }
		   }
		   else if(elements[1].checked  == true){
		    if(trim(document.getElementById("applicant1DetailModel_buisnessAddress").value)== ''){
		     alert("Office/Business Address is required field.");
		     return false;
		    }
		   }
		   return true;
		  }
     
      Calendar.setup(
      {
        inputField  : "applicant1DetailModel_idExpiryDate", // id of the input field
        ifFormat    : "%d/%m/%Y",      // the date format
        button      : "nicDate1",    // id of the button
        showsTime   :   false
      }
      );
      
      Calendar.setup(
      {
        inputField  : "applicant1DetailModel_dob", // id of the input field
        ifFormat    : "%d/%m/%Y",      // the date format
        button      : "dobDate1",    // id of the button
        showsTime   :   false
      }
      );
      Calendar.setup(
      {
        inputField  : "applicant1DetailModel_visaExpiryDate", // id of the input field
        ifFormat    : "%d/%m/%Y",      // the date format
        button      : "visaExpiry",    // id of the button
        showsTime   :   false
      }
      );
     
      Calendar.setup(
      {
        inputField  : "acOwnershipDetailModelList0.dateOfBirth", // id of the input field
        ifFormat    : "%d/%m/%Y",      // the date format
        button      : "ownerDobDate00",    // id of the button
        showsTime   :   false
      }
      );
      
      
     //window.onload = acTypeInfo(document.forms.level2AccountForm.customerAccountTypeId);
     window.onload = toggleVisaExpiry();
      </script>
	</body>

</html>