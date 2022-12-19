<!--Author: Mohammad Shehzad Ashraf-->
<!--Modified By: Usman Ashraf -->

<%@include file="/common/taglibs.jsp"%>
<%@page import="org.springframework.web.bind.ServletRequestUtils"%>
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
	<!-- turab added below for jquery -->
        <link rel="stylesheet" href="${contextPath}/styles/jquery-ui-custom.min.css" type="text/css">
        <script type="text/javascript" src="${contextPath}/scripts/jquery-ui-custom.min.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.11.0.js"></script>
	<script>
	var jq = $.noConflict();
	jq(document).ready(
		function($) {
			
		$( "#MyDialog" ).dialog({
			autoOpen: false,
			height: 600,
			width: 780,
			modal: true,
			buttons: {
				"Save": function() {
		    	        var row = $("#applicantDetailsTable tr:eq(1)");
		    	        var clone = row.clone();
		    	        // clear the values
		    	        var tr = clone.closest('tr');
		    	        tr.find('input[type=text]').val('');
		    	        tr.find('td:eq(0)').find("input:hidden").val('');
		    	        //setting values 
		    	        $("#applicantDetailsTable").append(tr);
		    	        //reset IDs and Name Attributes
		    	        resetIdAndNameAttributes();
		    	        
		    	        //set the values to the newly added row
		    	        setValues();
		    	        

					alert('write code here to paste to parent window');
					$( this ).dialog( "close" );
				},
				"Cancel": function() {
					$( this ).dialog( "close" );
				}
			},
			close: function() {
				$('#firstname').val("");
				$('#lastname').val("");
			}
		});
		$( "#addBtn" ).click(function() { $( "#MyDialog" ).dialog( "open" ); });
		
		function resetIdAndNameAttributes()
	    {
			alert(123);
	    	$('#applicantDetailsTable tbody tr').each(
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
		
		function setValues(){
			alert("setting values");
			var newRow = $("#applicantDetailsTable tr:last");
			alert(newRow.html());
	        var hiddenFields = newRow.find('td').find("input:hidden");
	        alert(hiddenFields.length);
	        $(hiddenFields[0]).val($(applicantDetailModelEditMode_buisnessName).val());// business name
	        $(hiddenFields[4]).val($(applicantDetailModelEditMode_ntn).val());// NTN
	        $(hiddenFields[5]).val($(applicantDetailModelEditMode_name).val());// account owner name
	        $(hiddenFields[6]).val($(applicantDetailModelEditMode_occupation).val());// occupation
	        $(hiddenFields[7]).val($(applicantDetailModelEditMode_nic).val());// NIC
	        $(hiddenFields[8]).val($(applicantDetailModelEditMode_nicExpiryDate).val());// NIC Expiry
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
	        

	        var clone = $(applicantDetailModelEditMode_customerPic).clone();

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
			        url: "${contextPath}/p_mnomfsaccountdetailsajx.html?appUserId=${param.appUserId}",
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
		<link rel="stylesheet" type="text/css"
			href="styles/deliciouslyblue/calendar.css" />

		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="expires" content="0" />
		<meta http-equiv="pragma" content="no-cache" />
		<c:choose>
			<c:when test="${not empty param.appUserId}">
				<meta name="title"
					content="${level3AccountModel.applicant1DetailModel.buisnessName}" />
			</c:when>
			<c:otherwise>
				<meta name="title" content="Branchless Banking Account Opening Form" />
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
		<script type="text/javascript">

			var customerEntityTypeCorporate=false;
	  function popupwindow(url, title, w, h) {
			  var left = (screen.width/2)-(w/2);
			  var top = (screen.height/2)-(h/2);
			  return window.open(url, title, 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes, copyhistory=no, width='+w+', height='+h+', top='+top+', left='+left);
			} 
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
		
      </script>
		<%
		String createPermission = PortalConstants.ADMIN_GP_CREATE;
		createPermission +=	"," + PortalConstants.PG_GP_CREATE;
		createPermission +=	"," + PortalConstants.MNG_L3_ACT_CREATE;

		String updatePermission = PortalConstants.ADMIN_GP_CREATE;
		updatePermission +=	"," + PortalConstants.PG_GP_CREATE;
		updatePermission +=	"," + PortalConstants.MNG_L3_ACT_CREATE;
	 %>

	</head>
	<body bgcolor="#ffffff" >
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
			<html:form name="level3AccountForm" commandName="level3AccountModel"
				enctype="multipart/form-data" method="POST"
				onsubmit="return onFormSubmit(this)"
				action="p_l3accountform.html">

				<c:if test="${not empty param.appUserId}">
					<input type="hidden" name="isUpdate" id="isUpdate" value="true" />
					<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
						value="<%=PortalConstants.ACTION_UPDATE%>" />
				</c:if>
				<c:if test="${empty param.appUserId}">
					<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
						value="<%=PortalConstants.ACTION_CREATE%>" />
				</c:if>

				<input type="hidden" name="appUserId" value="${param.appUserId}">
				<html:hidden path="<%=PortalConstants.KEY_USECASE_ID %>" />
				<div class="infoMsg" id="errorMessages" style="display:none;"></div>
				<div id="table_form">
                	<div class="center">
                	<table>
                		
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span style="color: #FF0000">*</span>Account Type:
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:select path="customerAccountTypeId" cssClass="textBox" id="customerAccountTypeId"
									tabindex="11" onchange="acTypeInfo(this);">
									<html:option value="">[Select]</html:option>
										<c:if test="${customerAccountTypeList != null}">
											<html:options items="${customerAccountTypeList}" itemLabel="name" itemValue="customerAccountTypeId" />
				 						</c:if>
								</html:select>
							</td>
						
						<c:if test="${not empty param.appUserId}">
                		
							<td align="right" bgcolor="F3F3F3" class="formText">
								<span style="color: #FF0000">*</span>Registration State:
							</td>
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
									<html:select path="registrationStateId" cssClass="textBox" id="registrationStateId"
										tabindex="18" onchange="regStateChange(this);">
										<html:option value="">[Select]</html:option>
											<c:if test="${regStateList != null}">
												<html:options items="${regStateList}" itemLabel="name" itemValue="registrationStateId" />
					 						</c:if>
									</html:select>
								</c:otherwise>
							</c:choose>

							</td>
						
						<tr id="screeningRow">
							<td align="right" bgcolor="F3F3F3" class="formText" style="height:30px;"><span id="screeningMandatory" style="color: #FF0000">*</span>Screening Performed:</td>
							<td align="left" bgcolor="FBFBFB" class="formText"><html:checkbox path="screeningPerformed" /></td>
							<c:if test="${not empty param.appUserId}">
						
                			<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Comments:
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:textarea style="height:50px;" path="regStateComments" tabindex="1" cssClass="textBox"/>
							</td>
						
						</c:if>
						</tr>
						</c:if>
						<c:if test="${not empty param.appUserId}">
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								<span style="color: #FF0000">*</span>Date:
							</td>
							<td bgcolor="FBFBFB" class="formText">
								<html:input type="date" style="background: #D3D3D3;" disabled="true" path="createdOn" tabindex="1" cssClass="textBox"/>
							</td>
						
						</c:if>
						<c:if test="${not empty param.appUserId}">
						
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								JS A/C #:
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<div class="textBox" style="background: #D3D3D3; ">${level3AccountModel.accountNo}</div>
							</td>
						</tr>
						</c:if>
                		<tr >
                			<td colspan="4" align="left" bgcolor="FBFBFB">
								<h2>
									L3 Account Opening Form:
								</h2>
							</td>
                		</tr>
                		<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								<span style="color: #FF0000">*</span>Mobile No:
							</td>
							<td bgcolor="FBFBFB" class="formText">
								<c:choose>
									<c:when test="${not empty param.appUserId}">
										<input type="hidden" id="mobileNo" name="mobileNo" value="${level3AccountModel.mobileNo}"/>
										<div class="textBox" style="background: #D3D3D3; ">${level3AccountModel.mobileNo}</div>
									</c:when>
									<c:otherwise>
										<html:input path="mobileNo" cssClass="textBox" tabindex="12"
											maxlength="11" onkeypress="return maskNumber(this,event)"/>
									</c:otherwise>
								</c:choose>
							</td>
						
						<c:if test="${not empty param.appUserId}">
						
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								Mobile A/C #:
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<div class="textBox" style="background: #D3D3D3; ">${level3AccountModel.mfsId}</div>
							</td>
						</c:if>
						</tr>
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								<span style="color: #FF0000">*</span>Customer/Entities Type:
							</td>
							<td bgcolor="FBFBFB">
								<html:select path="businessTypeId" cssClass="textBox"
									tabindex="16"  onchange="changeEntityType(this)">
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
							</td>
						</tr> 
                		<tr>
                			<td colspan="4" align="left" bgcolor="FBFBFB">
								<h3>
									APPLICANT 1:
								</h3>
							</td>
                		</tr>
                		<tr>
                			<input type="hidden" name="applicant1DetailModel.applicantTypeId" value="1"/>
                 			<input type="hidden" name="applicant1DetailModel.applicantDetailId" value="${level3AccountModel.applicant1DetailModel.applicantDetailId}"/>
                 			<html:hidden path="applicant1DetailModel.versionNo"/>
                 			<html:hidden path="applicant1DetailModel.createdOn"/>
                 			<html:hidden path="applicant1DetailModel.createdBy"/>
                			<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span style="color: #FF0000">*</span>Business Name:
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input path="applicant1DetailModel.buisnessName" id="applicant1DetailModel_buisnessName" tabindex="1" cssClass="textBox" onkeypress="return maskAlphaNumericWithSp(this,event)"/>
							</td>
						
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span style="color: #FF0000">*</span>Customer Picture:
							</td>
							<td bgcolor="FBFBFB" class="text" width="25%">
								<spring:bind path="level3AccountModel.applicant1DetailModel.customerPic">
									<input type="file" tabindex="2" id="applicant1DetailModel_customerPic" name="applicant1DetailModel.customerPic" class="upload" />
								</spring:bind>
								&nbsp;&nbsp;
							<c:choose>
								<c:when test="${not empty param.appUserId}">
										<img id="applicant1DetailModel_customerPicByte" src="${contextPath}/images/upload_dir/customerPic1_${level3AccountModel.appUserId}.gif?time=<%=System.currentTimeMillis()%>"
											width="100" height="100" />
								</c:when>
							</c:choose>
							</td>
						</tr>
 						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span style="color: #FF0000">*</span>Terms and Condition Picture:
							</td>
							<td bgcolor="FBFBFB" class="text" width="25%">
								<spring:bind path="level3AccountModel.applicant1DetailModel.tncPic">
									<input type="file" tabindex="3" id="applicant1DetailModel_tncPic" name="applicant1DetailModel.tncPic" class="upload" />
								</spring:bind>
								&nbsp;&nbsp;
								<c:choose>
									<c:when test="${not empty param.appUserId}">
											<img src="${contextPath}/images/upload_dir/tncPic1_${level3AccountModel.appUserId}.gif?time=<%=System.currentTimeMillis()%>"
												width="100" height="100" />
									</c:when>
								</c:choose>
							</td>
						
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span style="color: #FF0000">*</span>Signature Picture:
							</td>
							<td bgcolor="FBFBFB" class="text" width="25%">
								<spring:bind path="level3AccountModel.applicant1DetailModel.signPic">
									<input type="file" tabindex="4" id="applicant1DetailModel_signPic" name="applicant1DetailModel.signPic" class="upload" />
								</spring:bind>
								&nbsp;&nbsp;
								<c:choose>
									<c:when test="${not empty param.appUserId}">
											<img src="${contextPath}/images/upload_dir/signPic1_${level3AccountModel.appUserId}.gif?time=<%=System.currentTimeMillis()%>"
												width="100" height="100" />
									</c:when>
								</c:choose>
							</td>
						</tr>
 						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span style="color: #FF0000">*</span>CNIC Front:
							</td>
							<td bgcolor="FBFBFB" class="text" width="25%">
								<spring:bind path="level3AccountModel.applicant1DetailModel.cnicFrontPic">
									<input type="file" tabindex="5" id="applicant1DetailModel_cnicFrontPic" name="applicant1DetailModel.cnicFrontPic" class="upload"/>
								</spring:bind>
								&nbsp;&nbsp;
								<c:choose>
									<c:when test="${not empty param.appUserId}">
											<img src="${contextPath}/images/upload_dir/cnicFrontPic1_${level3AccountModel.appUserId}.gif?time=<%=System.currentTimeMillis()%>"
												width="100" height="100" />
									</c:when>
								</c:choose>
							</td>
						
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span style="color: #FF0000">*</span>CNIC Back:
							</td>
							<td bgcolor="FBFBFB" class="text" width="25%">
								<spring:bind path="level3AccountModel.applicant1DetailModel.cnicBack">
									<input type="file" tabindex="6" id="applicant1DetailModel_cnicBack" name="applicant1DetailModel.cnicBack" class="upload" />
								</spring:bind>
								&nbsp;&nbsp;
								<c:choose>
									<c:when test="${not empty param.appUserId}">
											<img src="${contextPath}/images/upload_dir/cnicBackPic1_${level3AccountModel.appUserId}.gif?time=<%=System.currentTimeMillis()%>"
												width="100" height="100" />
									</c:when>
								</c:choose>
							</td>
						</tr>
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								NTN #:
							</td>
							<td bgcolor="FBFBFB">
								<html:input id="applicant1DetailModel_ntn" path="applicant1DetailModel.ntn" cssClass="textBox" tabindex="24" maxlength="13" onkeypress="return maskNumber(this,event)"/>
							</td>
						
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								<span style="color: #FF0000">*</span>Account Owner Name:
							</td>
							<td bgcolor="FBFBFB">
								<html:input id="applicant1DetailModel_name" path="applicant1DetailModel.name" cssClass="textBox" tabindex="24" onkeypress="return maskAlphaWithSp(this,event)" maxlength="50"/>
							</td>
						</tr>
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								<span style="color: #FF0000">*</span>Occupations:
							</td>
							<td bgcolor="FBFBFB">
								<html:select id="applicant1DetailModel_occupation" path="applicant1DetailModel.occupation" cssClass="textBox"
									tabindex="35">
									<html:option value="">[Select]</html:option>
										<c:if test="${occupationList != null}">
											<html:options items="${occupationList}" itemLabel="name" itemValue="occupationId" />
				 						</c:if>
								</html:select>
							</td>
						
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								<span style="color: #FF0000">*</span>CNIC #:
							</td>
							<td bgcolor="FBFBFB" class="formText">
								<c:choose>
									<c:when test="${not empty param.appUserId}">
										<input type="hidden" id="applicant1DetailModel_nic" name="applicant1DetailModel.nic" value="${level3AccountModel.applicant1DetailModel.nic}"/>
										<div class="textBox" style="background: #D3D3D3; ">${level3AccountModel.applicant1DetailModel.nic}</div>
									</c:when>
									<c:otherwise>
										<html:input id="applicant1DetailModel_nic" path="applicant1DetailModel.nic" cssClass="textBox" tabindex="24" maxlength="13" onkeypress="return maskNumber(this,event)"/>
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								<span style="color: #FF0000">*</span>CNIC Expiry:
							</td>
							<td bgcolor="FBFBFB">
								<html:input id="applicant1DetailModel_nicExpiryDate" path="applicant1DetailModel.nicExpiryDate" cssClass="textBox" tabindex="25" readonly="true" maxlength="50" />
								<img id="nicDate1" tabindex="31" name="popcal" align="top"
									style="cursor:pointer"
									src="${pageContext.request.contextPath}/images/cal.gif"
									border="0" tabindex="23" />
								<img id="nicDate1" tabindex="32" name="popcal" title="Clear Date"
									onclick="javascript:$('applicant1DetailModel_nicExpiryDate').value=''" align="middle"
									style="cursor:pointer" tabindex="33"
									src="${pageContext.request.contextPath}/images/refresh.png"
									border="0" />
							</td>
						
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								Father/Husband Name:
							</td>
							<td bgcolor="FBFBFB">
								<html:input path="applicant1DetailModel.fatherHusbandName" cssClass="textBox" tabindex="22" onkeypress="return maskAlphaWithSp(this,event)" maxlength="50"/>
							</td>
						</tr>
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								<span style="color: #FF0000">*</span>Date of Birth:
							</td>
							<td bgcolor="FBFBFB">
								<html:input id="applicant1DetailModel_dob" path="applicant1DetailModel.dob" readonly="true" cssClass="textBox" tabindex="23"
									maxlength="50"/>
								<img id="dobDate1" tabindex="37" name="popcal" align="top"
									style="cursor:pointer"
									src="${pageContext.request.contextPath}/images/cal.gif"
									border="0" tabindex="3" />
								<img id="dobDate1" tabindex="37" name="popcal" title="Clear Date"
									onclick="javascript:$('applicant1DetailModel_dob').value=''" align="middle"
									style="cursor:pointer" tabindex="37"
									src="${pageContext.request.contextPath}/images/refresh.png"
									border="0" />
							</td>
						
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span style="color: #FF0000">*</span>Place of Birth:
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input id="applicant1DetailModel_birthPlace" path="applicant1DetailModel.birthPlace" tabindex="7" cssClass="textBox" onkeypress="return maskAlphaWithSp(this,event)" maxlength="50"/>
							</td>
						</tr>
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								<span style="color: #FF0000">*</span>Gender:
							</td>
							<td bgcolor="FBFBFB">
 								<html:radiobuttons class="applicant1DetailModel_gender" tabindex="27" items="${genderList}" itemLabel="label" itemValue="value" path="applicant1DetailModel.gender" />
							</td>
						
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Email:
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input id="applicant1DetailModel_email" path="applicant1DetailModel.email" tabindex="7" cssClass="textBox" />
							</td>
						</tr>
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Phone #:
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input id="applicant1DetailModel_landLineNo" path="applicant1DetailModel.landLineNo" maxlength="11" onkeypress="return maskInteger(this,event)" tabindex="7" cssClass="textBox" />
							</td>
						
							<%-- <td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Mobile #:
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input id="applicant1DetailModel_contactNo" path="applicant1DetailModel.contactNo" tabindex="7" cssClass="textBox" maxlength="11" onkeypress="return maskNumber(this,event)"/>
							</td> --%>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Fax #:
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input path="applicant1DetailModel.fax" tabindex="7" cssClass="textBox" onkeypress="return maskNumber(this,event)" maxlength="11"/>
							</td>
						</tr>
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span style="color: #FF0000">*</span>Business Address:
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:textarea style="height:50px;" id="applicant1DetailModel_buisnessAddress" path="applicant1DetailModel.buisnessAddress" tabindex="7" cssClass="textBox" />
							</td>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Postal Code:
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input path="applicant1DetailModel.buisnessPostalOfficeId" tabindex="7" cssClass="textBox" onkeypress="return maskInteger(this,event)" maxlength="5"/>
							</td>
						</tr>
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span style="color: #FF0000">*</span>Mailing Address:
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:textarea style="height:50px;" id="applicant1DetailModel_presentAddHouseNo" path="applicant1DetailModel.presentAddHouseNo" tabindex="7" cssClass="textBox" />
							</td>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Postal Code:
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input path="applicant1DetailModel.presentAddPostalOfficeId" tabindex="7" cssClass="textBox" onkeypress="return maskInteger(this,event)" maxlength="5"/>
							</td>
						</tr>
						<%-- <tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Fax #:
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input path="applicant1DetailModel.fax" tabindex="7" cssClass="textBox" onkeypress="return maskNumber(this,event)" maxlength="11"/>
							</td>
						</tr> --%>
						<c:set var="accountModelLevel3" scope="session" value="${level3AccountModel}"/> 
						<tr>
						<td colspan="4">
						
						
                		<!-- adding grid -->
	<div class="eXtremeTable">
	<table class="tableRegion" width="100%" id="applicantDetailsTable">
		<thead>
			<tr>
				<!-- <td height="35" align="center" class="tableHeader"><b>Sr.#</b></td> -->
		        <td height="35" align="center" class="tableHeader"><b>Business Name</b></td>
		        <td height="35" align="center" class="tableHeader"><b>Account Owner</b></td>
		        <td height="35" align="center" class="tableHeader"><b>CNIC #</b></td>
		        <td height="35" align="center" class="tableHeader"><b>CNIC Exp.</b></td>
		        <td height="35" align="center" class="tableHeader"><b>Birth Place</b></td>
		        <td height="35" align="center" class="tableHeader"><b>Action</b></td>
	    	</tr>
    	</thead>
    	<c:if test="${level3AccountModel.applicantDetailModelList!=null}">
		<c:forEach varStatus="status" var="applicantDetailModel" items="${level3AccountModel.applicantDetailModelList}">
  			<tr>
  			<html:hidden path="applicantDetailModelList[${status.index}].applicantDetailId"/>
			<html:hidden path="applicantDetailModelList[${status.index}].createdBy"/>
			<html:hidden path="applicantDetailModelList[${status.index}].versionNo"/>
<%--   				<td  align="center">

  				<html:hidden path="applicantDetailModelList[${status.index}].buisnessName"/>
				<html:hidden path="applicantDetailModelList[${status.index}].ntn"/>
				<html:hidden path="applicantDetailModelList[${status.index}].name" />
				<html:hidden path="applicantDetailModelList[${status.index}].occupation" />
				<html:hidden path="applicantDetailModelList[${status.index}].nic"/>
				<html:hidden path="applicantDetailModelList[${status.index}].nicExpiryDate"/>
				<html:hidden path="applicantDetailModelList[${status.index}].fatherHusbandName"/>
				<html:hidden path="applicantDetailModelList[${status.index}].dob"/>
				<html:hidden path="applicantDetailModelList[${status.index}].birthPlace"/>
				<html:hidden path="applicantDetailModelList[${status.index}].gender"/>
				<html:hidden path="applicantDetailModelList[${status.index}].email"/>
				<html:hidden path="applicantDetailModelList[${status.index}].landLineNo"/>
				<html:hidden path="applicantDetailModelList[${status.index}].contactNo"/>
				<html:hidden path="applicantDetailModelList[${status.index}].fax"/>
				<html:hidden path="applicantDetailModelList[${status.index}].buisnessAddress"/>
				<html:hidden path="applicantDetailModelList[${status.index}].buisnessPostalOfficeId"/>
				<html:hidden path="applicantDetailModelList[${status.index}].presentAddHouseNo"/>
				<html:hidden path="applicantDetailModelList[${status.index}].presentAddPostalOfficeId"/>
  				${status.count}</td>
  				 --%>
  				<td align="center">${applicantDetailModel.buisnessName}</td>
  				<td align="center">${applicantDetailModel.name}</td>
  				<td align="center">${applicantDetailModel.nic}</td>
  				<td align="center"><fmt:formatDate pattern="dd-MM-yyyy" value="${applicantDetailModel.nicExpiryDate}" /></td>
  				<td align="center">${applicantDetailModel.birthPlace}</td>
  				<td align="center">
  				<img id="editBtn" name="editBtn" align="top"
					style="cursor:pointer"
					src="${pageContext.request.contextPath}/images/buttons/edit.gif"
					border="0" onclick="popupwindow('p_l3_applicantformpopup.html?cnic=${applicantDetailModel.nic}', 'Edit Applicant', 800,800);" />&nbsp;
				</td>
  			</tr>  	
    	</c:forEach>
    	</c:if>
	</table>
	</div>
	<!-- end of grid -->
                		</td>
						</tr>
						<tr><td colspan="4" align="right"><input onclick="popupwindow('p_l3_applicantformpopup.html', 'Add/Edit Applicant', 800,800);" type="button" value="Add Applicant" class="button" /></td></tr>
						<tr>
                			<td colspan="4" align="left" bgcolor="FBFBFB">
								<h3>
									Relationship with Other Banks:
								</h3>
							</td>
                		</tr>
                		<tr  >
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Bank Name:
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input path="otherBankName" tabindex="7" cssClass="textBox" onkeypress="return maskAlphaWithSp(this,event)"/>
							</td>
						
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Bank Branch/Address:
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:textarea style="height:50px;" path="otherBankAddress" tabindex="7" cssClass="textBox" />
							</td>
						</tr>
						<tr  >
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Account #:
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input path="otherBankACNo" maxlength="30" tabindex="7" cssClass="textBox" onkeypress="return maskInteger(this,event)" />
							</td>
						</tr>
						<tr  >
                			<td colspan="4" align="left" bgcolor="FBFBFB">
								<h3>
									Details of Business:
								</h3>
							</td>
                		</tr>
                		<tr>
                			<input type="hidden" name="businessDetailModel.businessDetailId" value="${level3AccountModel.businessDetailModel.businessDetailId}"/>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Average Monthly Income:
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input path="businessDetailModel.avgMonthlyIncome" tabindex="7" cssClass="textBox" onkeypress="return maskInteger(this,event)" />
							</td>
						
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Min (PKR):
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input path="businessDetailModel.minMonthlyIncome" tabindex="7" cssClass="textBox" onkeypress="return maskInteger(this,event)" />
							</td>
						</tr>
						<tr  >
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Max (PKR):
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input path="businessDetailModel.maxMonthlyIncome" tabindex="7" cssClass="textBox" onkeypress="return maskInteger(this,event)" />
							</td>
						
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Monthly Turnover (PKR):
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input path="businessDetailModel.annualMonthlyTurnover" tabindex="7" cssClass="textBox" onkeypress="return maskInteger(this,event)" />
							</td>
						</tr>
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Major Buyer (Goods / Services):
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:textarea style="height:50px;" path="businessDetailModel.majorBuyer" tabindex="7" cssClass="textBox" />
							</td>
						
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Details Suppliers / Customers:
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:textarea style="height:50px;" path="businessDetailModel.supplierCustomer" tabindex="7" cssClass="textBox" />
							</td>
						</tr>
						<tr  >
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Indicate all other types of accounts held with JSBL:
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:textarea style="height:50px;" path="businessDetailModel.otherACwithJSBL" tabindex="7" cssClass="textBox" />
							</td>
						</tr>
						
						</table>
    				</div>
				    <div class="center">
				    <table width="100%">
						<tr>
                			<td colspan="4" align="left" bgcolor="FBFBFB">
								<h3>
									INDIVIDUAL ACCOUNT:
								</h3>
							</td>
                		</tr>
                		<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Type of Customer:
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:select path="customerTypeId" cssClass="textBox"
									tabindex="34">
									<html:option value="">[Select]</html:option>
										<c:if test="${customerTypeList != null}">
											<html:options items="${customerTypeList}" itemLabel="name" itemValue="customerTypeId" />
				 						</c:if>
								</html:select>
							</td>
						
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span style="color: #FF0000">*</span>Political Exposed People(PEP):
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:select  cssClass="textBox" path="publicFigure" tabindex="7">
								   <html:option value="" label="[Select]"/>
								   <html:options items="${publicFigureOpt}" />
								</html:select>
							</td>
						</tr>
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span style="color: #FF0000">*</span>Source of Funds:
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:select path="fundsSourceId" cssClass="textBox"
									tabindex="35" onchange="selFundSource(this);">
									<html:option value="">[Select]</html:option>
										<c:if test="${fundSourceList != null}">
											<html:options items="${fundSourceList}" itemLabel="name" itemValue="fundSourceId" />
				 						</c:if>
								</html:select>
							</td>
						
						<div id="fundSourceNarration" style="display:none;">
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Comments (if any):
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input path="fundSourceNarration" tabindex="1" cssClass="textBox"/>
							</td>
						</div>	
						</tr>
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span style="color: #FF0000">*</span>Usual Mode of Transaction:
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:select path="transactionModeId" cssClass="textBox"
									tabindex="35" onchange="selTxMode(this);">
									<html:option value="">[Select]</html:option>
										<c:if test="${transactionModeList != null}">
											<html:options items="${transactionModeList}" itemLabel="name" itemValue="transactionModeId" />
				 						</c:if>
								</html:select>
							</td>
						
						<div id="otherTxMode" style="display:none;">
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Comments (if any):
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input path="otherTransactionMode" tabindex="1" cssClass="textBox"/>
							</td>
							</div>
						</tr>
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Type of Account:
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<div class="textBox" style="background: #D3D3D3; ">Current</div>
							</td>
						
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Currency:
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<div class="textBox" style="background: #D3D3D3; ">PKR</div>
							</td>
						</tr>
						<tr>
							<td colspan="4" align="left" bgcolor="FBFBFB">
								<h3>
									Operating Instructions:
								</h3>
							</td>
						</tr>
						<tr  >
							<td colspan="4" align="left" bgcolor="FBFBFB">
								<h4>
									List of Mobile #:
								</h4>
							</td>
						</tr>
						<tr  >
							<td align="right" width="25%" bgcolor="F3F3F3" class="formText">
								Mobile 1:
							</td>
							<td align="left" bgcolor="FBFBFB" width="25%">
								<html:input path="mobile1" cssClass="textBox" tabindex="41" maxlength="11" onkeypress="return maskNumber(this,event)"/>
							</td>
						
							<td align="right" width="25%" bgcolor="F3F3F3" class="formText">
								Mobile 2:
							</td>
							<td align="left" bgcolor="FBFBFB" width="25%">
								<html:input path="mobile2" cssClass="textBox" tabindex="41" maxlength="11" onkeypress="return maskNumber(this,event)"/>
							</td>
						</tr>
						<tr  >
							<td align="right" width="25%" bgcolor="F3F3F3" class="formText">
								Mobile 3:
							</td>
							<td align="left" bgcolor="FBFBFB" width="25%">
								<html:input path="mobile3" cssClass="textBox" tabindex="41" maxlength="11" onkeypress="return maskNumber(this,event)"/>
							</td>
						
							<td align="right" width="25%" bgcolor="F3F3F3" class="formText">
								Mobile 4:
							</td>
							<td align="left" bgcolor="FBFBFB" width="25%">
								<html:input path="mobile4" cssClass="textBox" tabindex="41" maxlength="11" onkeypress="return maskNumber(this,event)"/>
							</td>
						</tr>
						<tr  >
							<td align="right" width="25%" bgcolor="F3F3F3" class="formText">
								Mobile 5:
							</td>
							<td align="left" bgcolor="FBFBFB" width="25%">
								<html:input path="mobile5" cssClass="textBox" tabindex="41" maxlength="11" onkeypress="return maskNumber(this,event)"/>
							</td>
						
							<td align="right" width="25%" bgcolor="F3F3F3" class="formText">
								Mobile 6:
							</td>
							<td align="left" bgcolor="FBFBFB" width="25%">
								<html:input path="mobile6" cssClass="textBox" tabindex="41" maxlength="11" onkeypress="return maskNumber(this,event)"/>
							</td>
						</tr>
						<tr  >
							<td align="right" width="25%" bgcolor="F3F3F3" class="formText">
								Purpose of Account:
							</td>
							<td align="left" bgcolor="FBFBFB" width="25%">
								<html:select path="accountReason" cssClass="textBox"
									tabindex="35">
									<html:option value="">[Select]</html:option>
										<c:if test="${accountReasonList != null}">
											<html:options items="${accountReasonList}" itemLabel="name" itemValue="accountReasonId" />
				 						</c:if>
								</html:select>
							</td>
						
							<td align="right" width="25%" bgcolor="F3F3F3" class="formText">
								Salary:
							</td>
							<td align="left" bgcolor="FBFBFB" width="25%">
								<html:input path="salary" cssClass="textBox" tabindex="41"/>
							</td>
						</tr>
						<tr  >
							<td align="right" width="25%" bgcolor="F3F3F3" class="formText">
								Buisness Income:
							</td>
							<td align="left" bgcolor="FBFBFB" width="25%">
								<html:input path="buisnessIncome" cssClass="textBox" tabindex="41" onkeypress="return maskInteger(this,event)"/>
							</td>
						
							<td align="right" width="25%" bgcolor="F3F3F3" class="formText">
								Other Income:
							</td>
							<td align="left" bgcolor="FBFBFB" width="25%">
								<html:input path="otherIncome" cssClass="textBox" tabindex="41" onkeypress="return maskInteger(this,event)"/>
							</td>
						</tr>
						<tr  >
							<td colspan="4" align="left" bgcolor="FBFBFB">
								<h3>
									Next of KIN:
								</h3>
							</td>
						</tr>
						<tr  >
							<td align="right" width="25%" bgcolor="F3F3F3" class="formText">
								Name:
							</td>
							<td align="left" bgcolor="FBFBFB" width="25%">
								<html:input path="nokDetailVOModel.nokName" cssClass="textBox" tabindex="41"
									 onkeypress="return maskAlphaWithSp(this,event)" />
							</td>
						
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Relationship:
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input path="nokDetailVOModel.nokRelationship" tabindex="47" cssClass="textBox"
									 onkeypress="return maskAlphaWithSp(this,event)" />
							</td>
						</tr>
						<tr  >
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Address:
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:textarea style="height:50px;" path="nokDetailVOModel.nokMailingAdd" tabindex="42" cssClass="textBox" />
							</td>
						
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Phone #:
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input path="nokDetailVOModel.nokContactNo" tabindex="46" cssClass="textBox" maxlength="11"
									onkeypress="return maskNumber(this,event)" />
							</td>
						</tr>
						<tr  >
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Mobile #:
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input id="nokMobile" path="nokDetailVOModel.nokMobile" tabindex="46" cssClass="textBox" maxlength="11"
									onkeypress="return maskNumber(this,event)" />
							</td>
						
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Comments:
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input path="comments" tabindex="46" cssClass="textBox" />
							</td>
						</tr>
						<tr  >
                			<td colspan="4" align="left" bgcolor="FBFBFB">
								<h3>
									Additional Details:
								</h3>
							</td>
                		</tr>
                		<tr  >
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Sales Tax Registration No:
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input path="additionalDetailVOModel.salesTaxRegNo" maxlength="30" tabindex="7" cssClass="textBox" onkeypress="return maskInteger(this,event)" />
							</td>
						
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Membership No. of CC / Trade Body:
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input path="additionalDetailVOModel.membershipNoTradeBody" maxlength="30" tabindex="7" cssClass="textBox" onkeypress="return maskInteger(this,event)" />
							</td>
						</tr>
						<tr  >
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span id="strRegDate" style="color: #FF0000">*</span>Incorporation Registration Date:
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input id="additionalDetailVOModel_incorporationDate" path="additionalDetailVOModel.incorporationDate" readonly="true" cssClass="textBox" tabindex="23"
									maxlength="50"/>
								<img id="incDate" tabindex="37" name="popcal" align="top"
									style="cursor:pointer"
									src="${pageContext.request.contextPath}/images/cal.gif"
									border="0" tabindex="3" />
								<img id="incDate" tabindex="37" name="popcal" title="Clear Date"
									onclick="javascript:$('additionalDetailVOModel_incorporationDate').value=''" align="middle"
									style="cursor:pointer" tabindex="37"
									src="${pageContext.request.contextPath}/images/refresh.png"
									border="0" />
								
							</td>
						
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span id="strRegNum" style="color: #FF0000">*</span>Registration #:
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input id="additionalDetailVOModel_secpRegNo" path="additionalDetailVOModel.secpRegNo" maxlength="30" tabindex="7" cssClass="textBox" onkeypress="return maskInteger(this,event)"/>
							</td>
						</tr>
						<tr  >
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span id="strRegPlace" style="color: #FF0000">*</span>Registration Place:
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input id="additionalDetailVOModel_registrationPlace" path="additionalDetailVOModel.registrationPlace" tabindex="8" cssClass="textBox" maxlength="50" onkeypress="return maskAlphaWithSp(this,event)"/>
							</td>
						</tr>
						
						</table>
				    </div>
    				<div style="clear:both"></div>
				</div>
					<table>
							<tr>
								<td width="100%" align="left" bgcolor="FBFBFB" colspan="4">
											<c:choose>
												<c:when test="${not empty param.appUserId}">
													<authz:authorize ifAnyGranted="<%=updatePermission%>">
														<input type="button" class="button" value="Update"
															tabindex="53"
															onclick="javascript:onSave(document.forms.level3AccountForm,null);" />&nbsp;
													</authz:authorize>
													<input type="button" class="button" value="Cancel" tabindex="54" onclick="javascript: window.location='p_l3accountsmanagement.html'" />
												</c:when>
												<c:otherwise>
													<authz:authorize ifAnyGranted="<%=createPermission%>">
														<input type="button" class="button" value="Create Account" tabindex="53" onclick="javascript:onSave(document.forms.level3AccountForm,null);" />
														<input type="button" class="button" value="Cancel" tabindex="54" onclick="javascript: window.location='home.html'"/>
													</authz:authorize>
													<authz:authorize ifNotGranted="<%=createPermission%>">
														<input type="button" class="button"
															value="Create Account" tabindex="-1"
															disabled="disabled" />
														<input type="button" class="button" value="Cancel" onclick="javascript: window.location='home.html'"
															tabindex="55" />
													</authz:authorize>
												</c:otherwise>
											</c:choose>
										</td>
									</tr>
								</table>
								
			<%-- <div id="MyDialog" title="Add/Edit Applicant">
			<table>
                		<tr>
                			<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span style="color: #FF0000">*</span>Business Name:
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<input name="applicantDetailModelEditMode_buisnessName" id="applicantDetailModelEditMode_buisnessName" type="text" id="	" tabindex="1" cssClass="textBox" onkeypress="return maskAlphaWithSp(this,event)"/>
							</td>
						
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span style="color: #FF0000">*</span>Customer Picture:
							</td>
							<td bgcolor="FBFBFB" class="text" width="25%">
									<input type="file" tabindex="2" id="applicantDetailModelEditMode_customerPic" name="applicantDetailModelEditMode.customerPic" class="upload" />
							</td>
						</tr>
 						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span style="color: #FF0000">*</span>Terms and Condition Picture:
							</td>
							<td bgcolor="FBFBFB" class="text" width="25%">
									<input type="file" tabindex="3" id="applicantDetailModelEditMode_tncPic" name="applicantDetailModelEditMode.tncPic" class="upload" />
							</td>
						
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span style="color: #FF0000">*</span>Signature Picture:
							</td>
							<td bgcolor="FBFBFB" class="text" width="25%">
									<input type="file" tabindex="4" id="applicantDetailModelEditMode_signPic" name="applicantDetailModelEditMode.signPic" class="upload" />
							</td>
						</tr>
 						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span style="color: #FF0000">*</span>CNIC Front:
							</td>
							<td bgcolor="FBFBFB" class="text" width="25%">
									<input type="file" tabindex="5" id="applicantDetailModelEditMode_cnicFrontPic" name="applicantDetailModelEditMode.cnicFrontPic" class="upload"/>
							</td>
						
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span style="color: #FF0000">*</span>CNIC Back:
							</td>
							<td bgcolor="FBFBFB" class="text" width="25%">
									<input type="file" tabindex="6" id="applicantDetailModelEditMode_cnicBack" name="applicantDetailModelEditMode.cnicBack" class="upload" />
							</td>
						</tr>
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								NTN #:
							</td>
							<td bgcolor="FBFBFB">
								<input type="text" id="applicantDetailModelEditMode_ntn" cssClass="textBox" tabindex="7" maxlength="13" onkeypress="return maskNumber(this,event)"/>
							</td>
						
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								<span style="color: #FF0000">*</span>Account Owner Name:
							</td>
							<td bgcolor="FBFBFB">
								<input name="applicantDetailModelEditMode_name" id="applicantDetailModelEditMode_name" type="text" cssClass="textBox" tabindex="8" onkeypress="return maskAlphaWithSp(this,event)"/>
							</td>
						</tr>
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								<span style="color: #FF0000">*</span>Occupations:
							</td>
							<td bgcolor="FBFBFB">
								<select name="applicantDetailModelEditMode_occupation" id="applicantDetailModelEditMode_occupation" tabindex="9" cssClass="textBox"
									tabindex="35">
									<option value="">[Select]</option>
									<!-- this whole segnemnt of dialog needs to be in Form to get dynamic values -->
									<option value="1">Salaried</option>
								</select>
							</td>
						
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								<span style="color: #FF0000">*</span>CNIC #:
							</td>
							<td bgcolor="FBFBFB" class="formText">
								<input name="applicantDetailModelEditMode_nic" id="applicantDetailModelEditMode_nic" type="text" tabindex="10" cssClass="textBox" maxlength="13" onkeypress="return maskNumber(this,event)"/>
							</td>
						</tr>
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								<span style="color: #FF0000">*</span>CNIC Expiry:
							</td>
							<td bgcolor="FBFBFB">
								<input id="applicantDetailModelEditMode_nicExpiryDate" name="applicantDetailModelEditMode_nicExpiryDate" type="text" cssClass="textBox" tabindex="11" readonly="true" maxlength="50" />
								<img id="nicDate1" tabindex="31" name="popcal" align="top"
									style="cursor:pointer"
									src="${pageContext.request.contextPath}/images/cal.gif"
									border="0" tabindex="23" />
								<img id="nicDate1" tabindex="32" name="popcal" title="Clear Date"
									onclick="javascript:$('applicantDetailModelEditMode_nicExpiryDate').value=''" align="middle"
									style="cursor:pointer" tabindex="33"
									src="${pageContext.request.contextPath}/images/refresh.png"
									border="0" />
							</td>
						
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								Father/Husband Name:
							</td>
							<td bgcolor="FBFBFB">
								<input type="text" id="applicantDetailModelEditMode_fatherHusbandName" name="applicantDetailModelEditMode_fatherHusbandName" cssClass="textBox" tabindex="12" onkeypress="return maskAlphaWithSp(this,event)"/>
							</td>
						</tr>
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								<span style="color: #FF0000">*</span>Date of Birth:
							</td>
							<td bgcolor="FBFBFB">
								<input name="applicantDetailModelEditMode_dob" id="applicantDetailModelEditMode_dob" type="text" readonly="true" cssClass="textBox" tabindex="13"
									maxlength="50"/>
								<img id="dobDate1" tabindex="37" name="popcal" align="top"
									style="cursor:pointer"
									src="${pageContext.request.contextPath}/images/cal.gif"
									border="0" tabindex="3" />
								<img id="dobDate1" tabindex="37" name="popcal" title="Clear Date"
									onclick="javascript:$('applicantDetailModelEditMode_dob').value=''" align="middle"
									style="cursor:pointer" tabindex="37"
									src="${pageContext.request.contextPath}/images/refresh.png"
									border="0" />
							</td>
						
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span style="color: #FF0000">*</span>Place of Birth:
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<input name="applicantDetailModelEditMode_birthPlace" id="applicantDetailModelEditMode_birthPlace" type="text" tabindex="14" cssClass="textBox" onkeypress="return maskAlphaWithSp(this,event)" />
							</td>
						</tr>
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								Gender:
							</td>
							<td bgcolor="FBFBFB">
 								<input id="applicantDetailModelEditMode_gender" name="applicantDetailModelEditMode_gender" type="radio" tabindex="15" name="gender" value="M"  />
 								<input id="applicantDetailModelEditMode_gender" name="applicantDetailModelEditMode_gender" type="radio" tabindex="16" name="gender" value="F" />
							</td>
						
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Email:
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<input name="applicantDetailModelEditMode_email" id="applicantDetailModelEditMode_email" type="text" tabindex="17" cssClass="textBox" />
							</td>
						</tr>
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span style="color: #FF0000">*</span>Phone #:
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<input name="applicantDetailModelEditMode_landLineNo" id="applicantDetailModelEditMode_landLineNo" type="text" maxlength="11" onkeypress="return maskInteger(this,event)" tabindex="18" cssClass="textBox" />
							</td>
						
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Mobile #:
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<input name="applicantDetailModelEditMode_contactNo" id="applicantDetailModelEditMode_contactNo" type="text" tabindex="19" cssClass="textBox" maxlength="11" onkeypress="return maskNumber(this,event)"/>
							</td>
						</tr>
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Fax #:
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<input name="applicantDetailModelEditMode_fax" id="applicantDetailModelEditMode_fax" type="text" tabindex="19" cssClass="textBox" onkeypress="return maskNumber(this,event)" maxlength="11"/>
							</td>
						
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span style="color: #FF0000">*</span>Business Address:
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<textarea style="height:50px;" name="applicantDetailModelEditMode_buisnessAddress" id="applicantDetailModelEditMode_buisnessAddress" tabindex="20" cssClass="textBox"></textarea>
							</td>
						</tr>
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Postal Code:
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<input name="applicantDetailModelEditMode_buisnessPostalOfficeId" id="applicantDetailModelEditMode_buisnessPostalOfficeId" type="text" tabindex="21" cssClass="textBox" onkeypress="return maskInteger(this,event)"/>
							</td>
						
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span style="color: #FF0000">*</span>Mailing Address:
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<textarea style="height:50px;" name="applicantDetailModelEditMode_presentAddHouseNo" id="applicantDetailModelEditMode_presentAddHouseNo"  tabindex="22" cssClass="textBox"></textarea>
							</td>
						</tr>
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Postal Code:
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<input id="applicantDetailModelEditMode_presentAddPostalOfficeId" name="applicantDetailModelEditMode_presentAddPostalOfficeId" type="text" tabindex="23" cssClass="textBox" onkeypress="return maskInteger(this,event)"/>
							</td>
							<td bgcolor="FBFBFB" colspan="2">&nbsp;</td>
						</tr>
						</table>
		</div> --%>
		</html:form>
		<form action="${contextPath}/p_pgsearchuserinfo.html" method="post"
			name="userInfoListViewForm" id="userInfoListViewModel">
			<input type="hidden"
				value="<c:out value="${level3AccountModel.searchMfsId}"/>"
				id="userId" name="userId" />
			<input type="hidden"
				value="<c:out value="${level3AccountModel.searchFirstName}"/>"
				id="name" name="firstName" />
			<input type="hidden"
				value="<c:out value="${level3AccountModel.searchLastName}"/>"
				id="lastName" name="lastName" />
			<input type="hidden"
				value="<c:out value="${level3AccountModel.searchNic}"/>" id="nic"
				name="nic" />
				
			<input type="hidden"
				value="<c:out value="${level3AccountModel.customerAccountTypeId}"/>" id="customerAccountTypeId"
				name="nic" />
		</form>
		<script language="javascript" type="text/javascript">


//The following code is written because 'Others' value was selected dy default.
var isSubmit = '<%= request.getParameter("_save") %>';
var isAction = '<%= request.getParameter("actionId") %>';
var appUserId = <%= ServletRequestUtils.getLongParameter(request, "appUserId", -1)%>;

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
		<c:when test="${not empty param.appUserId}">
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
      /*if(getFileSize(theForm.applicant2DetailModel_customerPic) > 2) {
      	alert("Applicant 2 Customer Picture can't be more than 2MB in size.");
      	return false;
      }
      if(getFileSize(theForm.applicant3DetailModel_customerPic) > 2) {
      	alert("Applicant 3 Customer Picture can't be more than 2MB in size.");
      	return false;
      }*/
      if(getFileSize(theForm.applicant1DetailModel_tncPic) > 2) {
      	alert("Applicant 1 Terms and Condition can't be more than 2MB in size.");
      	return false;
      }
      /*if(getFileSize(theForm.applicant2DetailModel_tncPic) > 2) {
      	alert("Applicant 2 Terms and Condition can't be more than 2MB in size.");
      	return false;
      }
      if(getFileSize(theForm.applicant3DetailModel_tncPic) > 2) {
      	alert("Applicant 3 Terms and Condition can't be more than 2MB in size.");
      	return false;
      }*/
      if(getFileSize(theForm.applicant1DetailModel_signPic) > 2) {
      	alert("Applicant 1 Signature Picture can't be more than 2MB in size.");
      	return false;
      }
      /*if(getFileSize(theForm.applicant2DetailModel_signPic) > 2) {
      	alert("Applicant 2 Signature Picture can't be more than 2MB in size.");
      	return false;
      }
      if(getFileSize(theForm.applicant3DetailModel_signPic) > 2) {
      	alert("Applicant 3 Signature Picture can't be more than 2MB in size.");
      	return false;
      }*/
      if(getFileSize(theForm.applicant1DetailModel_cnicFrontPic) > 2) {
      	alert("Applicant 1 CNIC Front Picture can't be more than 2MB in size.");
      	return false;
      }
      /*if(getFileSize(theForm.applicant2DetailModel_cnicFrontPic) > 2) {
      	alert("Applicant 2 CNIC Front Picture can't be more than 2MB in size.");
      	return false;
      }
      if(getFileSize(theForm.applicant3DetailModel_cnicFrontPic) > 2) {
      	alert("Applicant 3 CNIC Front Picture can't be more than 2MB in size.");
      	return false;
      }*/
      if(getFileSize(theForm.applicant1DetailModel_cnicBack) > 2) {
      	alert("Applicant 1 CNIC Back Picture can't be more than 2MB in size.");
      	return false;
      }
      /*if(getFileSize(theForm.applicant2DetailModel_cnicBack) > 2) {
      	alert("Applicant 2 CNIC Back Picture can't be more than 2MB in size.");
      	return false;
      }
      if(getFileSize(theForm.applicant3DetailModel_cnicBack) > 2) {
      	alert("Applicant 3 CNIC Back Picture can't be more than 2MB in size.");
      	return false;
      }*/
      var isNewCustomer = true;
      if(${not empty param.appUserId && not empty level3AccountModel.appUserId}){
      	var isNewCustomer = false;
      }

		var currServerDate = '<%=PortalDateUtils.currentFormattedDate("dd/MM/yyyy")%>';
   		if( trim(theForm.applicant1DetailModel_dob.value) != '' && isDateGreater(theForm.applicant1DetailModel_dob.value,currServerDate)){
			alert('Future date of birth is not allowed.');
			document.getElementById('dobDate1').focus();
			return false;
   		}else if( isAgeLessThan18Years(theForm.applicant1DetailModel_dob.value)){
			alert('Applicant 1 age cannot be less than 18 years');
			document.getElementById('dobDate1').focus();
			return false;
		}
   		
   	//incorporation registration future date is not allowed
   		if( trim(theForm.additionalDetailVOModel_incorporationDate.value) != '' && isDateGreater(theForm.additionalDetailVOModel_incorporationDate.value,currServerDate)){
			alert('Future Incorporation Registration Date is not allowed.');
			document.getElementById('additionalDetailVOModel_incorporationDate').focus();
			return false;
   		}
		
		/*if( trim(theForm.applicant2DetailModel_dob.value) != '' && isDateGreater(theForm.applicant2DetailModel_dob.value,currServerDate)){
			alert('Future date of birth is not allowed.');
			document.getElementById('dobDate2').focus();
			return false;
   		}else if( isAgeLessThan18Years(theForm.applicant2DetailModel_dob.value)){
			alert('Customer age cannot be less than 18 years');
			document.getElementById('dobDate2').focus();
			return false;
		}
		
		if( trim(theForm.applicant3DetailModel_dob.value) != '' && isDateGreater(theForm.applicant3DetailModel_dob.value,currServerDate)){
			alert('Future date of birth is not allowed.');
			document.getElementById('dobDate3').focus();
			return false;
   		}else if( isAgeLessThan18Years(theForm.applicant3DetailModel_dob.value)){
			alert('Customer age cannot be less than 18 years');
			document.getElementById('dobDate3').focus();
			return false;
		}*/
		
   		if( trim(theForm.applicant1DetailModel_nicExpiryDate.value) != '' && isDateSmaller(theForm.applicant1DetailModel_nicExpiryDate.value,currServerDate)){
			alert('CNIC Expiry should not be less than current date.');
			document.getElementById('nicExpiryDate1').focus();
			return false;
		}
		
		/*if( trim(theForm.applicant2DetailModel_nicExpiryDate.value) != '' && isDateSmaller(theForm.applicant2DetailModel_nicExpiryDate.value,currServerDate)){
			alert('CNIC Expiry should not be less than current date.');
			document.getElementById('nicExpiryDate2').focus();
			return false;
		}
		
		if( trim(theForm.applicant3DetailModel_nicExpiryDate.value) != '' && isDateSmaller(theForm.applicant3DetailModel_nicExpiryDate.value,currServerDate)){
			alert('CNIC Expiry should not be less than current date.');
			document.getElementById('nicExpiryDate3').focus();
			return false;
		}*/
		
		
      	var isValid = true;
		if(
		!validateComboWithOthers('presentAddDistrictId','presentDistOthers','Present Home District/Tehsil/Town')
		||!validateComboWithOthers('presentAddCityId','presentCityOthers','Present Home City')
		|| !validateComboWithOthers('permanentAddDistrictId','permanentDistOthers','Permanent Home District/Tehsil/Town')
		|| !validateComboWithOthers('permanentAddCityId','permanentAddCityOthers','Permanent Home City')
		|| !doRequired( theForm.publicFigure, 'Political Exposed People(PEP)')
	    || !doRequired( theForm.fundsSourceId, 'Source of Funds')
	    || !doRequired( theForm.transactionModeId, 'Usual Mode of Transaction')
		|| !validateComboWithOthers('fundsSourceId','fundSourceOthers','Source of Funds')
		){
			return false;
		}
		if(customerEntityTypeCorporate)
		{
 			if(doRequired( theForm.additionalDetailVOModel_incorporationDate, 'Incorporation Registration Date' )
 					&& doRequired( theForm.additionalDetailVOModel_secpRegNo, 'Registration #' )
 					&& doRequired( theForm.additionalDetailVOModel_registrationPlace, 'Registration Place' )
 					)
 			{}
 			else
 			{
 				return false;
 			}
		}
		if(!validateFormChar(theForm)){
      		return false;
      	}
		
		if(appUserId!=-1 && document.getElementById('registrationStateId')!=undefined && document.getElementById('registrationStateId').value=="3")
		{
	       	if(!document.getElementById('screeningPerformed1').checked)
    		{
	       		alert("Kindly check Screening Performed.");   
	    		return false;
    		}
    	}
		
      	//submitting form
        if (confirm('Are you sure you want to proceed?')==true){
          return true;
        }else{
          return false;
        }
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

				 function onFormSubmit(theForm)
      			{
      			
      			var theForm = document.forms.level3AccountForm;
      			
      			if(doRequired( theForm.customerAccountTypeId, 'Account Type' )
		      		&& doRequired( theForm.mobileNo, 'Mobile No.' )
		      		&& doRequired( theForm.businessTypeId, 'Customer/Entities Type' )
		      		&& doRequired( theForm.applicant1DetailModel_buisnessName, 'Applicant 1 Business Name' ) 
		      		&& doRequired( theForm.applicant1DetailModel_name, 'Applicant 1 Account Owner Name' ) 
		      		&& doRequired( theForm.applicant1DetailModel_occupation, 'Applicant 1 Occupations' ) 
		      		&& doRequired( theForm.applicant1DetailModel_nic, 'Applicant 1 CNIC #' )
					&& doRequired( theForm.applicant1DetailModel_nicExpiryDate, 'Applicant 1 CNIC Expiry' ) 
					&& doRequired( theForm.applicant1DetailModel_dob, 'Applicant 1 Date of Birth' )
					&& doRequired( theForm.applicant1DetailModel_birthPlace, 'Applicant 1 Place of Birth' )
					&& doRequiredGroup( 'applicant1DetailModel_gender', 'Applicant 1 Gender' )   
					//&& doRequired( theForm.applicant1DetailModel_landLineNo, 'Applicant 1 Phone #' ) 
					&& doRequired( theForm.applicant1DetailModel_buisnessAddress, 'Applicant 1 Business Address' )
                    && doRequired( theForm.applicant1DetailModel_presentAddHouseNo, 'Applicant 1 Mailing Address' )
                    && isValidMinLength(theForm.mobileNo, 'Mobile No.', 11)
                    //&& isValidMinLength(theForm.applicant1DetailModel_contactNo, 'Applicant 1 Mobile #', 11)
                    && isValidMinLength(theForm.applicant1DetailModel_nic, 'Applicant 1 CNIC #', 13)
                    && isValidMinLength(theForm.mobile1, 'Mobile 1', 11)
                    && isValidMinLength(theForm.mobile2, 'Mobile 2', 11)
                    && isValidMinLength(theForm.mobile3, 'Mobile 3', 11)
                    && isValidMinLength(theForm.mobile4, 'Mobile 4', 11)
                    && isValidMinLength(theForm.mobile5, 'Mobile 5', 11)
                    && isValidMinLength(theForm.mobile6, 'Mobile 6', 11)
                    && isValidMinLength(theForm.nokMobile, 'NOK Mobile #', 11)
					&& isValidEmail(theForm.applicant1DetailModel_email, 'Applicant 1 Email') ){
						
					}else {
						return false;
					}
      			
      			
      			if(theForm.applicant1DetailModel_buisnessName.value != '' && theForm.applicant1DetailModel_customerPicByte==undefined){
      				if( doRequired( theForm.applicant1DetailModel_customerPic, 'Applicant 1 Customer Picture' )
					&& isValidPicOrPdf(theForm.applicant1DetailModel_customerPic, 'Applicant 1 Customer Picture')
					&& doRequired( theForm.applicant1DetailModel_tncPic, 'Applicant 1 T&C Picture' )
					&& isValidPicOrPdf(theForm.applicant1DetailModel_tncPic, 'Applicant 1 T&C Picture')
					&& doRequired( theForm.applicant1DetailModel_signPic, 'Applicant 1 Signature Picture' )
					&& isValidPicOrPdf(theForm.applicant1DetailModel_signPic, 'Applicant 1 Signature Picture')
					&& doRequired( theForm.applicant1DetailModel_cnicFrontPic, 'Applicant 1 CNIC Front Picture' )
 					&& isValidPicOrPdf(theForm.applicant1DetailModel_cnicFrontPic, 'Applicant 1 CNIC Front Picture')
					&& doRequired( theForm.applicant1DetailModel_cnicBack, 'Applicant 1 CNIC Back Picture' )
					&& isValidPicOrPdf(theForm.applicant1DetailModel_cnicBack, 'Applicant 1 CNIC Back Picture') ){
						
					}else {
						return false;
					}
      			}
      			
      			/*if(theForm.applicant2DetailModel_buisnessName.value != '' && theForm.applicant2DetailModel_customerPicByte==undefined){
      				if( doRequired( theForm.applicant2DetailModel_customerPic, 'Applicant 2 Customer Picture' )
      				&& isValidPicOrPdf(theForm.applicant2DetailModel_customerPic, 'Applicant 2 Customer Picture')
      				&& doRequired( theForm.applicant2DetailModel_tncPic, 'Applicant 2 T&C Picture' )
      				&& isValidPicOrPdf(theForm.applicant2DetailModel_tncPic, 'Applicant 2 T&C Picture')
					&& doRequired( theForm.applicant2DetailModel_signPic, 'Applicant 2 Signature Picture' )
					&& isValidPicOrPdf(theForm.applicant2DetailModel_signPic, 'Applicant 2 Signature Picture')
					&& doRequired( theForm.applicant2DetailModel_cnicFrontPic, 'Applicant 2 CNIC Front Picture' )
					&& isValidPicOrPdf(theForm.applicant2DetailModel_cnicFrontPic, 'Applicant 2 CNIC Front Picture')
					&& doRequired( theForm.applicant2DetailModel_cnicBack, 'Applicant 2 CNIC Back Picture' )
					&& doRequired( theForm.applicant2DetailModel_name, 'Applicant 2 Account Owner Name' )
					&& doRequired( theForm.applicant2DetailModel_nic, 'Applicant 2 CNIC #' ) 
                    && isValidMinLength(theForm.applicant2DetailModel_contactNo, 'Applicant 2 Mobile #', 11)
                    && isValidMinLength(theForm.applicant2DetailModel_nic, 'Applicant 2 CNIC #', 13)
					&& isValidEmail(theForm.applicant2DetailModel_email, 'Applicant 2 Email')){
						
					}else {
						return false;
					}
      			}
      			
      			if(theForm.applicant3DetailModel_buisnessName.value != '' && theForm.applicant3DetailModel_customerPicByte==undefined){
      				if( doRequired( theForm.applicant3DetailModel_customerPic, 'Applicant 3 Customer Picture' )
      				&& isValidPicOrPdf(theForm.applicant3DetailModel_customerPic, 'Applicant 3 Customer Picture')		
      				&& doRequired( theForm.applicant3DetailModel_tncPic, 'Applicant 3 T&C Picture' )
      				&& isValidPicOrPdf(theForm.applicant3DetailModel_tncPic, 'Applicant 3 T&C Picture')
					&& doRequired( theForm.applicant3DetailModel_signPic, 'Applicant 3 Signature Picture' )
					&& isValidPicOrPdf(theForm.applicant3DetailModel_signPic, 'Applicant 3 Signature Picture')
					&& doRequired( theForm.applicant3DetailModel_cnicFrontPic, 'Applicant 3 CNIC Front Picture' )
					&& isValidPicOrPdf(theForm.applicant3DetailModel_cnicFrontPic, 'Applicant 3 CNIC Front Picture')
					&& isValidPicOrPdf(theForm.applicant3DetailModel_cnicBack, 'Applicant 2 CNIC Back Picture')
					&& doRequired( theForm.applicant3DetailModel_cnicBack, 'Applicant 3 CNIC Back Picture' )  
					&& isValidPicOrPdf(theForm.applicant3DetailModel_cnicBack, 'Applicant 3 CNIC Back Picture')
					&& doRequired( theForm.applicant3DetailModel_name, 'Applicant 3 Account Owner Name' )
					&& doRequired( theForm.applicant3DetailModel_nic, 'Applicant 3 CNIC #' )
					&& isValidEmail(theForm.applicant3DetailModel_email, 'Applicant 3 Email')
                    && isValidMinLength(theForm.applicant3DetailModel_contactNo, 'Applicant 3 Mobile #', 11)
                    && isValidMinLength(theForm.applicant3DetailModel_nic, 'Applicant 3 CNIC #', 13)){
						
					}else {
						return false;
					}
      			}*/
      			
		       if(appUserId!= -1){
		        if(doRequired( theForm.registrationStateId, 'Registration State' )){
		        
		        }else{
		         return false;
		        }
		       }
				
			      	if(submitForm(theForm)){
						return true;
					}else{
						return false;
					}
		      }
      
      

      
      function doRequired( field, label )
      {
      	if( field.value == '' || field.value.length == 0 )
      	{
      		alert( label + ' is required field.' );
      		return false;
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

      //This method is a copy of code from submitForm() method
      function isValidFormForPrinting()
	  {
		var theForm = document.forms.level3AccountForm;
		if( doRequired( theForm.name, 'Title of Account' ) && doRequired( theForm.presentAddHouseNo, 'Mailing Address' )
			&& doRequired( theForm.country, 'Country' ) && doRequired( theForm.presentAddCityId, 'City/District' )
			&& doRequired( theForm.presentAddPostalOfficeId, 'Postal Code' ) && doRequired( theForm.customerAccountTypeId, 'Account Type' ) && doRequired( theForm.mobileNo, 'Mobile No' ) 
			&& doRequired( theForm.registrationStateId, 'Registration State' )  )
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
						if(theForm.applicant1DetailModel.nicExpiryDate.value == null || trim(theForm.applicant1DetailModel.nicExpiryDate.value) == ''){
							alert('CNIC Expiry is a required field.');
							return false;
						}
					}
					
			   		if( trim(theForm.applicant1DetailModel.nicExpiryDate.value) != '' && isDateSmaller(theForm.applicant1DetailModel.nicExpiryDate.value,currServerDate)){
						alert('CNIC Expiry should not be less than current date.');
						document.getElementById('nicExpiryDate').focus();
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
						if(theForm.applicant2DetailModel.nicExpiryDate.value == null || trim(theForm.applicant2DetailModel.nicExpiryDate.value) == ''){
							alert('CNIC Expiry is a required field.');
							return false;
						}
					}
					
			   		if( trim(theForm.applicant2DetailModel.nicExpiryDate.value) != '' && isDateSmaller(theForm.applicant2DetailModel.nicExpiryDate.value,currServerDate)){
						alert('CNIC Expiry should not be less than current date.');
						document.getElementById('nicExpiryDate').focus();
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
						if(theForm.applicant3DetailModel.nicExpiryDate.value == null || trim(theForm.applicant3DetailModel.nicExpiryDate.value) == ''){
							alert('CNIC Expiry is a required field.');
							return false;
						}
					}
					
			   		if( trim(theForm.applicant3DetailModel.nicExpiryDate.value) != '' && isDateSmaller(theForm.applicant3DetailModel.nicExpiryDate.value,currServerDate)){
						alert('CNIC Expiry should not be less than current date.');
						document.getElementById('nicExpiryDate').focus();
						return false;
					}
					
					
					var cnicVal = theForm.nic.value;
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
			if(sel.value == 5){
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
     
		function init()
		{
			 window.onload = acTypeInfo(document.forms.level3AccountForm.customerAccountTypeId);
		     window.onload = selTxMode(document.forms.level3AccountForm.transactionModeId);
		     window.onload = selFundSource(document.forms.level3AccountForm.fundsSourceId);
		     window.onload = changeEntityType(document.forms.level3AccountForm.businessTypeId);
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

      Calendar.setup(
      {
        inputField  : "applicant1DetailModel_nicExpiryDate", // id of the input field
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
        inputField  : "additionalDetailVOModel_incorporationDate", // id of the input field
        ifFormat    : "%d/%m/%Y",      // the date format
        button      : "incDate",    // id of the button
        showsTime   :  false
      }
      );
      
     window.onload = init();
      </script>
	</body>

</html>




