<!--Author: Mohammad Shehzad Ashraf-->
<!--Modified By: Usman Ashraf -->

<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<link rel='stylesheet' href="${contextPath}/styles/style.css" type='text/css'>
	<link rel="stylesheet" type="text/css" media="all" href="${contextPath}/styles/decorator-style/theme.css"/>
	<link rel="stylesheet" type="text/css" media="print" href="${contextPath}/styles/decorator-style/print.css"/>
	<link rel='stylesheet' href="${contextPath}/styles/style_js.css" type='text/css'>
	<script type="text/javascript" src="${contextPath}/scripts/global.js"></script>
	<script type="text/javascript" src="${contextPath}/scripts/toolbar.js"></script>
	
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
			<c:when test="${not empty param.appUserId}">
				<meta name="title"
					content="${level2AccountModel.applicantDetailModelEditMode.customerAccountName}" />
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
        <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.11.0.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.maskedinput.js"></script>
        
        <link rel="stylesheet" href="${contextPath}/styles/jquery-ui-custom.min.css" type="text/css">
        <script type="text/javascript" src="${contextPath}/scripts/jquery-ui-custom.min.js"></script>
	<script>
	var jq = $.noConflict();
	jq(document).ready(
		function($) {
			
			jq("#applicantDetailModelEditMode_residentialAddress").keyup(function(){
			    if($(this).val().length > 250){
			        $(this).val($(this).val().substr(0, 249));
			}
			});
			
			jq("#applicantDetailModelEditMode_buisnessAddress").keyup(function(){
			    if($(this).val().length > 250){
			        $(this).val($(this).val().substr(0, 249));
			}
			});
			
		});
	jq(function($){
		   $("#applicantDetailModelEditMode_dob").mask("99/99/9999",{placeholder:"dd/mm/yyyy"});
		   $("#applicantDetailModelEditMode_idExpiryDate").mask("99/99/9999",{placeholder:"dd/mm/yyyy"});
		   $("#applicantDetailModelEditMode_visaExpiryDate").mask("99/99/9999",{placeholder:"dd/mm/yyyy"})
		   
		});
	</script>
		
		<script type="text/javascript">

	  function proceed(){
		  			var result = onFormSubmit();
		  			var myForm = document.forms.level2ApplicantForm;
		  			var rowId = "${level2AccountModel.applicantDetailModelEditMode.index}";

		  			if(result == true){
		  		    	myForm.action="p_l2_applicantformpopup.html";

					   	var rows = self.opener.document.getElementById("applicantDetailsTable");
					   	
					   	var tableRef = self.opener.document.getElementById('applicantDetailsTable').getElementsByTagName('tbody')[0];

					   	myTR = self.opener.document.createElement("TR");
						
					   	var nextRow = rows.rows.length;

			  			if( rowId != undefined && rowId != null &&  rowId != ""){
							//removeRow(rowId/* document.getElementById("applicantDetailModelEditMode_idNumber").value */);
							removeRow(rowId);
							nextRow = rowId;
						}
					   	
					   	
					   /* 	if(nextRow == 1){
					   		nextRow = 0;
					   	} */
					   	//alert("Next Row: " + nextRow);
						//serialNo
/*						srNoTD=self.opener.document.createElement("TD");
						srNoTD.setAttribute("align","center"); 
						if(tableRef != null){
							
							srNoTEXT = self.opener.document.createTextNode(tableRef.rows.length);
						}else{
							
							srNoTEXT = self.opener.document.createTextNode("1");
						}
						srNoTD.appendChild(srNoTEXT); 
						myTR.appendChild(srNoTD);*/
						//business name
						busniessNameTD=self.opener.document.createElement("TD");
						busniessNameTD.setAttribute("align","center");
						businessNameTEXT = self.opener.document.createTextNode(document.getElementById("applicantDetailModelEditMode_name").value); 
						busniessNameTD.appendChild(businessNameTEXT); 
						myTR.appendChild(busniessNameTD);
						//MobileNo
						nameTD=self.opener.document.createElement("TD");
						nameTD.setAttribute("align","center");
						nameTEXT = self.opener.document.createTextNode(document.getElementById("applicantDetailModelEditMode_mobileNo").value); 
						nameTD.appendChild(nameTEXT); 
						myTR.appendChild(nameTD);
						//cnic number 
						nicTD=self.opener.document.createElement("TD");
						nicTD.setAttribute("align","center");
						nicTEXT = self.opener.document.createTextNode(document.getElementById("applicantDetailModelEditMode_idNumber").value); 
						nicTD.appendChild(nicTEXT); 
						myTR.appendChild(nicTD);
						//nic expirty 
						nicExpiryTD=self.opener.document.createElement("TD");
						nicExpiryTD.setAttribute("align","center");
						nicExpiryTEXT = self.opener.document.createTextNode(document.getElementById("applicantDetailModelEditMode_idExpiryDate").value); 
						nicExpiryTD.appendChild(nicExpiryTEXT); 
						myTR.appendChild(nicExpiryTD);
						//Nationality
						birthTD=self.opener.document.createElement("TD");
						birthTD.setAttribute("align","center");
						birthTEXT = self.opener.document.createTextNode(document.getElementById("applicantDetailModelEditMode_nationality").value); 
						birthTD.appendChild(birthTEXT); 
						myTR.appendChild(birthTD);
						myTR.setAttribute("id", nextRow);
						//edit button
						/*editTD=self.opener.document.createElement("TD");
						editTD.setAttribute("align","center");
						editTEXT = self.opener.document.createTextNode("<a href='#'>here</a>"); 
						editTD.appendChild(editTEXT); 
						myTR.appendChild(editTD);*/
						
						editTD=self.opener.document.createElement("TD");
						editTD.setAttribute("align","center");
						var oImg=self.opener.document.createElement("img");
						oImg.setAttribute('src', '${pageContext.request.contextPath}/images/buttons/edit.gif');
						oImg.setAttribute('alt', 'na');
						//oImg.setAttribute('height', '1px');
						//oImg.setAttribute('width', '1px');
						oImg.setAttribute('id', 'editBtn'); 
						oImg.setAttribute('name', 'editBtn');
						oImg.setAttribute('align', 'top');
						oImg.setAttribute('style', 'cursor:pointer');
						oImg.setAttribute('onclick', "popupwindow('p_l2_applicantformpopup.html?idNumber="+document.getElementById("applicantDetailModelEditMode_idNumber").value+"&isEdit=true&index="+nextRow+"', 'Edit Applicant', 955,600);");
						editTD.appendChild(oImg);
						myTR.appendChild(editTD);
						//hello alert("calling removeRow: " + document.getElementById("applicantDetailModelEditMode_idNumber").value);
						//here was remove row
						if(tableRef!=null){
							tableRef.appendChild(myTR);
						}else{
							rows.appendChild(myTR);	
						}
						
						myForm.submit();
		  		    }else{
		  		    	return false;
		  		    }
			}
	  
	  function removeRow(rowIndex){
		  /* try {
	            var table = self.opener.document.getElementById("applicantDetailsTable");
	            
	            if(table != null)
	            {
		        	table.deleteRow(rowIndex);
	            }else{
	            	alert("Sorry something went wrong with this browser");
	            }
	            }catch(e) {
	                alert(e);
	            } */
	            
	            var table = self.opener.document.getElementById("applicantDetailsTable");
	            
	            for (var i = 0, row; row = table.rows[i]; i++) {
	            	if(row.id==rowIndex){
	            		table.deleteRow(row.rowIndex);
	            	}
            	}     
		  
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
		
		function isNumberKey(evt){
		    var charCode = (evt.which) ? evt.which : event.keyCode
		    if (charCode > 31 && (charCode < 48 || charCode > 57))
		        return false;
		    return true;
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
		String createPermission = PortalConstants.ADMIN_GP_CREATE;
		createPermission +=	"," + PortalConstants.PG_GP_CREATE;
		createPermission +=	"," + PortalConstants.MNG_L2_CUST_CREATE;

		String updatePermission = PortalConstants.ADMIN_GP_UPDATE;
		updatePermission +=	"," + PortalConstants.PG_GP_UPDATE;
		updatePermission +=	"," + PortalConstants.MNG_L2_CUST_UPDATE;
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
			<html:form name="level2ApplicantForm" commandName="level2AccountModel"
				enctype="multipart/form-data" method="POST">

				<c:if test="${not empty level2AccountModel.applicantDetailModelEditMode.applicantDetailId or param.isEdit}">
					<input type="hidden" name="isUpdate" id="isUpdate" value="true" />
					<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
						value="<%=PortalConstants.ACTION_UPDATE%>" />
				</c:if>
				<c:if test="${empty level2AccountModel.applicantDetailModelEditMode.applicantDetailId and not param.isEdit}">
					<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
						value="<%=PortalConstants.ACTION_CREATE%>" />
				</c:if>

				<html:hidden path="<%=PortalConstants.KEY_USECASE_ID %>" />
				<div class="infoMsg" id="errorMessages" style="display:none;"></div>
				<div id="table_form">
                	<div class="center">
                	
                	<table>
                	<tr >
                			<td colspan="4" align="left" bgcolor="FBFBFB">
								<h2>
									Personal Information - <small>(for Joint Account/Guardian)</small>
								</h2>
							</td>
                		</tr>
                		<tr>
                			<input type="hidden" name="applicantDetailModelEditMode.applicantTypeId" value="2"/>
                 			<input type="hidden" id="applicantDetailModelEditMode_applicantDetailId" name="applicantDetailModelEditMode.applicantDetailId" value="${level2AccountModel.applicantDetailModelEditMode.applicantDetailId}"/>
                 			<html:hidden path="applicantDetailModelEditMode.versionNo"/>
                 			<html:hidden path="applicantDetailModelEditMode.createdOn"/>
                 			<html:hidden path="applicantDetailModelEditMode.createdBy"/>
                 			
                 			<!-- below hidden fields added by Turab for maker-checker issue -->
                 			<html:hidden path="applicantDetailModelEditMode.titleTxt" id="applicantDetailModelEditMode_titleTxt"/>
                 			<html:hidden path="applicantDetailModelEditMode.idTypeName" id="applicantDetailModelEditMode_idTypeName"/>
							<html:hidden path="applicantDetailModelEditMode.birthPlaceName" id="applicantDetailModelEditMode_birthPlaceName"/>
							<html:hidden path="applicantDetailModelEditMode.occupationName" id="applicantDetailModelEditMode_occupationName"/>
							<html:hidden path="applicantDetailModelEditMode.professionName" id="applicantDetailModelEditMode_professionName"/>
							<html:hidden path="applicantDetailModelEditMode.maritalStatusName" id="applicantDetailModelEditMode_maritalStatusName"/>
							<html:hidden path="applicantDetailModelEditMode.residentialCityName" id="applicantDetailModelEditMode_residentialCityName"/>
							<html:hidden path="applicantDetailModelEditMode.buisnessCityName" id="applicantDetailModelEditMode_buisnessCityName"/>
							<html:hidden path="applicantDetailModelEditMode.index" id="applicantDetailModelEditMode_index"/>
                			<!-- end by Turab -->
                			<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Joint Account:
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="30%">
								<html:select path="applicantDetailModelEditMode.jointAc" cssClass="textBox" id="applicantDetailModelEditMode_jointAc"
									tabindex="16">
										<c:if test="${binaryOpt != null}">
											<html:options items="${binaryOpt}" itemLabel="label" itemValue="value" />
				 						</c:if>
								</html:select>
							</td>
							
							<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">
								Either Only:
							</td>
							<td bgcolor="FBFBFB" width="30%">
 								<html:checkbox id="applicantDetailModelEditMode_eitherOnly" path="applicantDetailModelEditMode.eitherOnly" tabindex="8" />
							</td>
                			
						</tr>
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Title:
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:select path="applicantDetailModelEditMode.title" cssClass="textBox" id="applicantDetailModelEditMode_title"
									tabindex="16">
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
 								<html:radiobuttons class="applicantDetailModelEditMode_gender" tabindex="27" items="${genderList}" itemLabel="label" itemValue="value" path="applicantDetailModelEditMode.gender" />
							</td>
						</tr>
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								<span style="color: #FF0000">*</span>Applicant Name:
							</td>
							<td bgcolor="FBFBFB">
								<html:input id="applicantDetailModelEditMode_name" path="applicantDetailModelEditMode.name" cssClass="textBox" tabindex="24" onkeypress="return maskAlphaWithSp(this,event)" maxlength="50"/>
							</td>
							
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								
							</td>
							<td bgcolor="FBFBFB">
								
							</td>
							
						</tr>
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								<span style="color: #FF0000">*</span>ID Document Type:
							</td>
							<td height="16" align="left" bgcolor="F3F3F3" class="formText">
								<c:choose>
									<c:when test="${not empty level2AccountModel.applicantDetailModelEditMode.applicantDetailId}">
										<input type="hidden" id="applicantDetailModelEditMode_idType" name="applicantDetailModelEditMode.idType" value="${level2AccountModel.applicantDetailModelEditMode.idType}"/>
										<div class="textBox" id="divIdTypeName" style="background: #D3D3D3; ">${level2AccountModel.applicantDetailModelEditMode.idTypeName}</div>
									</c:when>
									<c:otherwise>
										<html:select path="applicantDetailModelEditMode.idType" cssClass="textBox" id="applicantDetailModelEditMode_idType" onchange="toggleVisaExpiry();">
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
									<c:when test="${not empty level2AccountModel.applicantDetailModelEditMode.applicantDetailId}">
										<input type="hidden" id="applicantDetailModelEditMode_idNumber" name="applicantDetailModelEditMode.idNumber" value="${level2AccountModel.applicantDetailModelEditMode.idNumber}"/>
										<div class="textBox" style="background: #D3D3D3; ">${level2AccountModel.applicantDetailModelEditMode.idNumber}</div>
									</c:when>
									<c:otherwise>
										<html:input id="applicantDetailModelEditMode_idNumber" path="applicantDetailModelEditMode.idNumber" cssClass="textBox" tabindex="24" maxlength="13"/>
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
								<spring:bind path="level2AccountModel.applicantDetailModelEditMode.idFrontPic">
									<input type="file" tabindex="5" id="applicantDetailModelEditMode_idFrontPic" name="applicantDetailModelEditMode.idFrontPic" class="upload"/>
								</spring:bind>
								&nbsp;&nbsp;
								<c:choose>
									<c:when test="${not empty level2AccountModel.applicantDetailModelEditMode.idNumber}">
											<img src="${contextPath}/images/upload_dir/idFrontPic2_${level2AccountModel.appUserId}.gif?time=<%=System.currentTimeMillis()%>"
												width="100" height="100" />
									</c:when>
								</c:choose>
							</td>
						
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span style="color: #FF0000">*</span>ID Document Back Image:
							</td>
							<td bgcolor="FBFBFB" class="text" width="25%">
								<spring:bind path="level2AccountModel.applicantDetailModelEditMode.idBackPic">
									<input type="file" tabindex="6" id="applicantDetailModelEditMode_idBackPic" name="applicantDetailModelEditMode.idBackPic" class="upload" />
								</spring:bind>
								&nbsp;&nbsp;
								<c:choose>
									<c:when test="${not empty level2AccountModel.applicantDetailModelEditMode.idNumber}">
											<img src="${contextPath}/images/upload_dir/idBackPic2_${level2AccountModel.appUserId}.gif?time=<%=System.currentTimeMillis()%>"
												width="100" height="100" />
									</c:when>
								</c:choose>
							</td>
						</tr> --%>
						
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								Date of ID Expiry:
							</td>
							<td bgcolor="FBFBFB">
								<html:input id="applicantDetailModelEditMode_idExpiryDate" path="applicantDetailModelEditMode.idExpiryDate" cssClass="textBox" tabindex="25" maxlength="50" />
								<img id="nicDate1" tabindex="31" name="popcal" align="top"
									style="cursor:pointer"
									src="${pageContext.request.contextPath}/images/cal.gif"
									border="0" tabindex="23" />
								<img id="nicDate1" tabindex="32" name="popcal" title="Clear Date"
									onclick="javascript:$('applicantDetailModelEditMode_idExpiryDate').value=''" align="top"
									style="cursor:pointer" tabindex="33"
									src="${pageContext.request.contextPath}/images/refresh.png"
									border="0" />
							</td>
							
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								NTN #:
							</td>
							<td bgcolor="FBFBFB">
								<html:input id="applicantDetailModelEditMode_ntn" path="applicantDetailModelEditMode.ntn" cssClass="textBox" tabindex="24" maxlength="8" onkeypress="return isNumberKey(event)"/>
							</td>
						</tr>
						
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								Date of Birth:
							</td>
							<td bgcolor="FBFBFB">
								<html:input id="applicantDetailModelEditMode_dob" path="applicantDetailModelEditMode.dob" cssClass="textBox" tabindex="23"
									maxlength="50"/>
								<img id="dobDate1" tabindex="37" name="popcal" align="top"
									style="cursor:pointer"
									src="${pageContext.request.contextPath}/images/cal.gif"
									border="0" tabindex="3" />
								<img id="dobDate1" tabindex="37" name="popcal" title="Clear Date"
									onclick="javascript:$('applicantDetailModelEditMode_dob').value=''" align="top"
									style="cursor:pointer" tabindex="37"
									src="${pageContext.request.contextPath}/images/refresh.png"
									border="0" />
							</td>
						
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Place of Birth:</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<%-- <html:input id="applicantDetailModelEditMode_birthPlace" onkeypress="return maskAlphaWithSp(this,event)" path="applicantDetailModelEditMode.birthPlace" tabindex="7" cssClass="textBox" maxlength="50"/> --%>
								<html:select id="applicantDetailModelEditMode_birthPlace" path="applicantDetailModelEditMode.birthPlace" cssClass="textBox"
									tabindex="35">
									<html:option value="">[Select]</html:option>
									<c:if test="${cityList != null}">
											<html:options items="${cityList}" itemLabel="name" itemValue="cityId" />
				 					</c:if>
								</html:select>
							</td>
						</tr>
						
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
									Date of Visa Expiry:
								</td>
								<td bgcolor="FBFBFB">
									<html:input id="applicantDetailModelEditMode_visaExpiryDate" path="applicantDetailModelEditMode.visaExpiryDate" cssClass="textBox" tabindex="25" maxlength="50" />
									<img id="visaExpiry" tabindex="31" name="popcal" align="top"
										style="cursor:pointer"
										src="${pageContext.request.contextPath}/images/cal.gif"
										border="0" tabindex="23" />
									<img id="visaExpiry2" tabindex="32" name="popcal" title="Clear Date"
										onclick="javascript:$('applicantDetailModelEditMode_visaExpiryDate').value=''" align="top"
										style="cursor:pointer" tabindex="33"
										src="${pageContext.request.contextPath}/images/refresh.png"
										border="0" />
							</td>
							
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								Mother's Maiden Name:
							</td>
							<td bgcolor="FBFBFB">
								<html:input id="applicantDetailModelEditMode_motherMaidenName" path="applicantDetailModelEditMode.motherMaidenName" cssClass="textBox" tabindex="24" maxlength="50" onkeypress="return maskAlphaWithSp(this,event)" />
							</td>
						</tr>
						
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								Residential Status:
							</td>
							<td bgcolor="FBFBFB">
 								<html:radiobuttons class="applicantDetailModelEditMode_residentialStatus" tabindex="27" items="${residentialStatusList}" itemLabel="label" itemValue="value" path="applicantDetailModelEditMode.residentialStatus" />
							</td>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								Nationality
							</td>
							<td bgcolor="FBFBFB">
								<html:input id="applicantDetailModelEditMode_nationality" path="applicantDetailModelEditMode.nationality" cssClass="textBox" tabindex="24" maxlength="50" onkeypress="return maskAlphaWithSp(this,event)" />
							</td>
							
						</tr>
						
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								US Citizen
							</td>
							<td bgcolor="FBFBFB">
								<%-- <html:select path="applicantDetailModelEditMode.usCitizen" cssClass="textBox"
								id="applicantDetailModelEditMode_usCitizen"	tabindex="16">
									<html:option value="">[Select]</html:option>
									<html:option value="1">Yes</html:option>
									<html:option value="0">No</html:option>	
								</html:select> --%>
								<html:radiobuttons class="applicantDetailModelEditMode_usCitizen" tabindex="16" items="${binaryOpt}" itemLabel="label" itemValue="value" path="applicantDetailModelEditMode.usCitizen" />
							</td>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Home Number:</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input id="applicantDetailModelEditMode_contactNo" path="applicantDetailModelEditMode.contactNo" tabindex="7" cssClass="textBox" maxlength="11" onkeypress="return isNumberKey(event)"/>
							</td>
							
						</tr>
						
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Phone Number:
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input id="applicantDetailModelEditMode_landLineNo" path="applicantDetailModelEditMode.landLineNo" maxlength="11" tabindex="7" cssClass="textBox" onkeypress="return maskInteger(this,event)" />
							</td>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								<span style="color: #FF0000">*</span>Mobile Number:
							</td>
							<td bgcolor="FBFBFB" class="formText">
								<c:choose>
									<c:when test="${not empty level2AccountModel.applicantDetailModelEditMode.applicantDetailId}">
										<html:hidden id="applicantDetailModelEditMode_mobileNo" value="${level2AccountModel.applicantDetailModelEditMode.mobileNo}" path="applicantDetailModelEditMode.mobileNo" />
										<div class="textBox" style="background: #D3D3D3; ">${level2AccountModel.applicantDetailModelEditMode.mobileNo}</div>
									</c:when>
									<c:otherwise>
										<html:input path="applicantDetailModelEditMode.mobileNo" cssClass="textBox" tabindex="12" id="applicantDetailModelEditMode_mobileNo"
											maxlength="11" onkeypress="return isNumberKey(event)"/>
									</c:otherwise>
								</c:choose>
							</td>
							
						</tr>
						
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Fax Number:</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input id="applicantDetailModelEditMode_fax" path="applicantDetailModelEditMode.fax" tabindex="7" cssClass="textBox" onkeypress="return maskInteger(this,event)" maxlength="11"/>
							</td>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Email:</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:input path="applicantDetailModelEditMode.email" id="applicantDetailModelEditMode_email" tabindex="7" cssClass="textBox" maxlength="50" />
							</td>
						</tr>
						
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								Father Name/Husband Name:
							</td>
							<td bgcolor="FBFBFB">
								<html:input path="applicantDetailModelEditMode.fatherHusbandName" id="applicantDetailModelEditMode_fatherHusbandName" onkeypress="return maskAlphaWithSp(this,event)" cssClass="textBox" tabindex="22" maxlength="50"/>
							</td>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								Name of Employer/Business:
							</td>
							<td bgcolor="FBFBFB">
								<html:input id="applicantDetailModelEditMode_employerName" path="applicantDetailModelEditMode.employerName" onkeypress="return maskAlphaWithSp(this,event)" cssClass="textBox" tabindex="22" maxlength="50"/>
							</td>
						</tr>
						
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								Occupation:
							</td>
							<td bgcolor="FBFBFB">
								<html:select id="applicantDetailModelEditMode_occupation" path="applicantDetailModelEditMode.occupation" cssClass="textBox"
									tabindex="35">
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
								<html:select id="applicantDetailModelEditMode_profession" path="applicantDetailModelEditMode.profession" cssClass="textBox"
									tabindex="35">
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
 								<html:radiobuttons class="applicantDetailModelEditMode_mailing" tabindex="27" items="${mailingAddressTypeList}" itemLabel="label" itemValue="value" path="applicantDetailModelEditMode.mailingAddressType" />
							</td>		
						</tr>
						
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Residential Address:</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:textarea style="height:50px;" id="applicantDetailModelEditMode_residentialAddress" path="applicantDetailModelEditMode.residentialAddress" tabindex="7" cssClass="textBox" />
							</td>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								Office/Business Address:</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:textarea style="height:50px;" id="applicantDetailModelEditMode_buisnessAddress" path="applicantDetailModelEditMode.buisnessAddress" tabindex="7" cssClass="textBox" />
							</td>
						</tr>
						
						<tr>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText">
								City:
							</td>
							<td bgcolor="FBFBFB">
								<html:select id="applicantDetailModelEditMode_buisnessCity" path="applicantDetailModelEditMode.buisnessCity" cssClass="textBox"
									tabindex="35">
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
								<html:select id="applicantDetailModelEditMode_residentialCity" path="applicantDetailModelEditMode.residentialCity" cssClass="textBox"
									tabindex="35">
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
								<html:select id="applicantDetailModelEditMode_maritalStatus" path="applicantDetailModelEditMode.maritalStatus" cssClass="textBox"
									tabindex="35">
									<html:option value="">[Select]</html:option>
									<c:if test="${maritalStatusList != null}">
											<html:options items="${maritalStatusList}" itemLabel="label" itemValue="value" />
				 					</c:if>
										
								</html:select>
							</td>
							<%-- <td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span style="color: #FF0000">*</span>VeriSys Done:
							</td>
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:checkbox id="applicantDetailModelEditMode_verisysDone" path="applicantDetailModelEditMode.verisysDone" tabindex="8" />
							</td> --%>
						</tr>
						<tr id="screeningRow">
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								<span id="screeningMandatory" style="color: #FF0000"></span>Screening:
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:radiobuttons tabindex="27" class="screening" items="${screeningList}" itemLabel="label" itemValue="value" path="applicantDetailModelEditMode.screeningPerformed" id="applicantDetailModelEditMode_screeningPerformed" />
							</td>
							<td height="16" align="right" bgcolor="F3F3F3" class="formText"
								width="25%">
								NADRA Verification:
							<td align="left" bgcolor="FBFBFB" class="formText" width="25%">
								<html:radiobuttons class="verisys" tabindex="27" items="${nadraVerList}" itemLabel="label" itemValue="value" path="applicantDetailModelEditMode.verisysDone" id="applicantDetailModelEditMode_verisysDone" />
							</td>
						</tr>
						<tr>
    						<td colspan="2" align="right"><input type="button" value="Save" onclick="proceed();" /></td>
    						<td colspan="2" align="left"><input type="button" value="Close" onclick="window.close();" /></td>
    					</tr>
						</table>
    				</div>
				    
    				<div style="clear:both"></div>
				</div>
					
			</html:form>
		
		
		<script language="javascript" type="text/javascript">


//The following code is written because 'Others' value was selected dy default.
var isSubmit = '<%= request.getParameter("_save") %>';
var isAction = '<%= request.getParameter("actionId") %>';

function checkOthersValue(comboBoxId,fieldId){
	var comboBox = document.getElementById(comboBoxId);
	var field = document.getElementById(fieldId);
	var index = comboBox.length -1;
	if(comboBox != null)
	comboBox.options[index].selected = 'selected';
	if(field != null)
	field.style.display = 'inline'; 
}
function checkNumericData(){
	if (! IsNumeric(document.forms.level3AccountForm.mobileNo.value)){
		alert ("Mobile number must contain numeric data");
		return false;
	}
	onSave(document.forms.level3AccountForm,null);

}

function IsNumeric(sText)

{
   var ValidChars = "0123456789";
   var IsNumber=true;
   var Char;

 
   for (i = 0; i < sText.length && IsNumber == true; i++) 
      { 
      Char = sText.charAt(i); 
      if (ValidChars.indexOf(Char) == -1) 
         {
         IsNumber = false;
         }
      }
   return IsNumber;
   
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
 
		var currServerDate = '<%=PortalDateUtils.currentFormattedDate("dd/MM/yyyy")%>';
   		if( trim(theForm.applicantDetailModelEditMode_dob.value) != '' && isDateGreater(theForm.applicantDetailModelEditMode_dob.value,currServerDate)){
			alert('Future date of birth is not allowed.');
			document.getElementById('dobDate1').focus();
			return false;
   		}else if( isAgeLessThan18Years(theForm.applicantDetailModelEditMode_dob.value)){
			alert('Applicant age cannot be less than 18 years');
			document.getElementById('dobDate1').focus();
			return false;
		}
   		else if(trim(theForm.applicantDetailModelEditMode_dob.value) != '' && (!isValidDate(theForm.applicantDetailModelEditMode_dob.value,"Date of Birth"))){
   			document.getElementById('applicantDetailModelEditMode_dob').focus();
   			return false;
   		}
		
   		if( trim(theForm.applicantDetailModelEditMode_idExpiryDate.value) != '' && isDateSmaller(theForm.applicantDetailModelEditMode_idExpiryDate.value,currServerDate)){
			alert('ID Document Expiry should not be less than current date.');
			document.getElementById('applicantDetailModelEditMode_idExpiryDate').focus();
			return false;
		}
   		else if(trim(theForm.applicantDetailModelEditMode_idExpiryDate.value) != '' && (!isValidDate(theForm.applicantDetailModelEditMode_idExpiryDate.value,"Date of ID Expiry"))){
   			document.getElementById('applicantDetailModelEditMode_idExpiryDate').focus();
   			return false;
   		}
   		
   		if(trim(theForm.applicantDetailModelEditMode_visaExpiryDate.value) != '' && (!isValidDate(theForm.applicantDetailModelEditMode_visaExpiryDate.value,"Date of Visa Expiry"))){
   			document.getElementById('applicantDetailModelEditMode_visaExpiryDate').focus();
   			return false;
   		}
   			
		
   	//for main appliant
			var appDtlId = document.getElementById("applicantDetailModelEditMode_applicantDetailId").value;
		if(appDtlId == undefined || appDtlId == "" || appDtlId == 0){
			var applicant1DetailModel_idType1 = document.getElementById('applicantDetailModelEditMode_idType');
			var fieldValue1 = theForm.applicantDetailModelEditMode_idType.options[theForm.applicantDetailModelEditMode_idType.selectedIndex].text;
			var idNumber1 = document.getElementById('applicantDetailModelEditMode_idNumber');
			if(!checkDocumentIDLength(applicant1DetailModel_idType1,fieldValue1,idNumber1)) {
				return false;
			}
		}
		
      	
		
      	return true;
  }
      


  	function regStateChange(sel)
      {
  		document.getElementById('screeningRow').style.display = '';
  		document.getElementById('screeningPerformed1').disabled = false ;
  		
  		if(sel.value==2)//in recieved case
  		{
  			 document.getElementById('screeningRow').style.display = 'none';
  			 document.getElementById('screeningPerformed1').disabled = true ; 
  		}
  		if(sel.value==5)//in declined case
  		{
  			 document.getElementById('screeningPerformed1').disabled = true ; 
  		}
  	}

	function onFormSubmit()
     {
      			var theForm = document.forms.level2ApplicantForm;
      			
      		//below checks added by Turab for maker checker issue
  				if(document.getElementById('applicant1DetailModel_title'))
     				theForm.applicantDetailModelEditMode_titleTxt.value = theForm.applicantDetailModelEditMode_title.options[theForm.applicantDetailModelEditMode_title.selectedIndex].innerHTML;

  				var applicantDetailId = document.getElementById("applicantDetailModelEditMode_applicantDetailId").value;
  				if(applicantDetailId != "" && applicantDetailId != undefined){
	     			theForm.applicantDetailModelEditMode_idTypeName.value = document.getElementById("divIdTypeName").innerHTML;
	     		}else{
	     			if(document.getElementById('applicantDetailModelEditMode_idType'))
		     			theForm.applicantDetailModelEditMode_idTypeName.value = theForm.applicantDetailModelEditMode_idType.options[theForm.applicantDetailModelEditMode_idType.selectedIndex].innerHTML;	     			
	     		}
	     		
	     		if(document.getElementById('applicantDetailModelEditMode_birthPlace'))
	     			theForm.applicantDetailModelEditMode_birthPlaceName.value = theForm.applicantDetailModelEditMode_birthPlace.options[theForm.applicantDetailModelEditMode_birthPlace.selectedIndex].innerHTML;
	     		if(document.getElementById('applicantDetailModelEditMode_occupation'))
	     			theForm.applicantDetailModelEditMode_occupationName.value = theForm.applicantDetailModelEditMode_occupation.options[theForm.applicantDetailModelEditMode_occupation.selectedIndex].innerHTML;
	     		if(document.getElementById('applicantDetailModelEditMode_profession'))
	     			theForm.applicantDetailModelEditMode_professionName.value = theForm.applicantDetailModelEditMode_profession.options[theForm.applicantDetailModelEditMode_profession.selectedIndex].innerHTML;
	     		if(document.getElementById('applicantDetailModelEditMode_maritalStatus'))
	     			theForm.applicantDetailModelEditMode_maritalStatusName.value = theForm.applicantDetailModelEditMode_maritalStatus.options[theForm.applicantDetailModelEditMode_maritalStatus.selectedIndex].innerHTML;
	     		if(document.getElementById('applicantDetailModelEditMode_residentialCity'))
	     			theForm.applicantDetailModelEditMode_residentialCityName.value = theForm.applicantDetailModelEditMode_residentialCity.options[theForm.applicantDetailModelEditMode_residentialCity.selectedIndex].innerHTML;
	     		if(document.getElementById('applicantDetailModelEditMode_buisnessCity'))
	     			theForm.applicantDetailModelEditMode_buisnessCityName.value = theForm.applicantDetailModelEditMode_buisnessCity.options[theForm.applicantDetailModelEditMode_buisnessCity.selectedIndex].innerHTML;
  			//end by Turab
      			
      			if(
//     					doRequired( theForm.applicantDetailModelEditMode_title, 'Title' )
      					//&& doRequired( theForm.applicantDetailModelEditMode_jointAc, 'Joint Account' )
          				//&& doRequired( theForm.applicantDetailModelEditMode_usCitizen, 'US Citizen' )
//         				&& doRequiredGroup( 'applicantDetailModelEditMode_usCitizen', 'US Citizen' )
    		      		doRequired( theForm.applicantDetailModelEditMode_name, 'Applicant Name' ) 
//     					&& doRequired( theForm.applicantDetailModelEditMode_motherMaidenName, 'Mother Maiden Name' )
 //   		      		&& doRequired( theForm.applicantDetailModelEditMode_occupation, 'Applicant Occupations' )
    		      		&& doRequired( theForm.applicantDetailModelEditMode_idType, 'Applicant ID Type' )
    		      		&& doRequired( theForm.applicantDetailModelEditMode_idNumber, 'Applicant ID Number' ) 
//    					&& doRequired( theForm.applicantDetailModelEditMode_idExpiryDate, 'Applicant ID Expiry' ) 
//   					&& doRequired( theForm.applicantDetailModelEditMode_dob, 'Applicant Date of Birth' )
//    					&& doRequired( theForm.applicantDetailModelEditMode_birthPlace, 'Applicant Place of Birth' ) 
//    					&& doRequiredGroup( 'applicantDetailModelEditMode_gender', 'Applicant Gender' ) 
//   					&& doRequiredGroup( 'applicantDetailModelEditMode_residentialStatus', 'Residential Status' )
//    					&& doRequiredGroup( 'applicantDetailModelEditMode_mailing', 'Mailing Address' )
    					//&& doRequired( theForm.applicantDetailModelEditMode_buisnessAddress, 'Applicant Business Address' ) 
    					//&& doRequired( theForm.applicantDetailModelEditMode_residentialAddress, 'Applicant Residential Address' )
    					&& validateRequiredMailingAddress('applicantDetailModelEditMode_mailing')
//    					&& doRequired( theForm.applicantDetailModelEditMode_nationality, 'Nationality' )
//    					&& doRequired( theForm.applicantDetailModelEditMode_fatherHusbandName, 'Father/Husband name' )
//    					&& doRequired( theForm.applicantDetailModelEditMode_employerName, 'Name of Employer/Business' )
//    					&& doRequired( theForm.applicantDetailModelEditMode_maritalStatus, 'Marital Status' )
						&& doRequired( theForm.applicantDetailModelEditMode_mobileNo, 'Mobile No.' )
    		      		&& isValidMobileNo( theForm.applicantDetailModelEditMode_mobileNo)
    		      		&& isValidMinLength( theForm.applicantDetailModelEditMode_mobileNo, 'Mobile No.', 11 )
    					&& isValidEmail(theForm.applicantDetailModelEditMode_email, 'Applicant Email')
                        && isValidMinLength(theForm.applicantDetailModelEditMode_contactNo, 'Applicant Home Number', 11)
                        && isValidMinLength(theForm.applicantDetailModelEditMode_landLineNo, 'Applicant Phone Number', 11)
                        && isValidMinLength(theForm.applicantDetailModelEditMode_fax, 'Applicant Fax Number', 11)
//                        && doRequiredGroup( 'verisys', 'Verisys' )
                        )
					
      			
      				
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
    	  
      	if( field.value == '' || field.value.trim('').length == 0 )
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

	  
	  function isValidMobileNo(field){
		  if(field.type == "hidden"){
    		  return true;
    	  }
    	  if( field.value != '' )
        	{
        		var mobileNumber = field.value;
        		if(mobileNumber.charAt(0)!="0" || mobileNumber.charAt(1)!="3"){
        			alert("Mobile Number should start from 03XXXXXXXXX");
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
      			return false;
      		}
      	}
       	return true;
      }

      //This method is a copy of code from submitForm() method
      function isValidFormForPrinting()
	  {
		var theForm = document.forms.level3AccountForm;
		if( doRequired( theForm.name, 'Title of Account' ) && doRequired( theForm.residentialAddress, 'Mailing Address' )
			&& doRequired( theForm.country, 'Country' ) && doRequired( theForm.presentAddCityId, 'City/District' )
			&& doRequired( theForm.presentAddPostalOfficeId, 'Postal Code' ) && doRequired( theForm.customerAccountTypeId, 'Account Type' ) && doRequired( theForm.mobileNo, 'Mobile No' ) 
			&& doRequired( theForm.registrationStateId, 'Registration State' )  )
			{
					var currServerDate = '<%=PortalDateUtils.currentFormattedDate("dd/MM/yyyy")%>';
			   		if( trim(theForm.applicantDetailModelEditMode.dob.value) != '' && isDateGreater(theForm.applicantDetailModelEditMode.dob.value,currServerDate)){
						alert('Future date of birth is not allowed.');
						document.getElementById('dobDate1').focus();
						return false;
			   		}else if( isAgeLessThan18Years(theForm.applicantDetailModelEditMode.dob.value)){
						alert('Applicant age cannot be less than 18 years');
						document.getElementById('dobDate1').focus();
						return false;
					}
					
					if(!isDobGreaterThan60Years(theForm.applicantDetailModelEditMode.dob.value) ){
						if(theForm.applicantDetailModelEditMode.idExpiryDate.value == null || trim(theForm.applicantDetailModelEditMode.idExpiryDate.value) == ''){
							alert('ID Document Expiry is a required field.');
							return false;
						}
					}
					
			   		if( trim(theForm.applicantDetailModelEditMode.idExpiryDate.value) != '' && isDateSmaller(theForm.applicantDetailModelEditMode.idExpiryDate.value,currServerDate)){
						alert('ID Document Expiry should not be less than current date.');
						document.getElementById('idExpiryDate').focus();
						return false;
					}
					
					
					
					
					if( trim(theForm.applicant2DetailModel.dob.value) != '' && isDateGreater(theForm.applicant2DetailModel.dob.value,currServerDate)){
						alert('Future date of birth is not allowed.');
						document.getElementById('dobDate2').focus();
						return false;
			   		}else if( isAgeLessThan18Years(theForm.applicant2DetailModel.dob.value)){
						alert('Applicant age cannot be less than 18 years');
						document.getElementById('dobDate2').focus();
						return false;
					}
					
					if(!isDobGreaterThan60Years(theForm.applicant2DetailModel.dob.value) ){
						if(theForm.applicant2DetailModel.idExpiryDate.value == null || trim(theForm.applicant2DetailModel.idExpiryDate.value) == ''){
							alert('ID Document Expiry is a required field.');
							return false;
						}
					}
					
			   		if( trim(theForm.applicant2DetailModel.idExpiryDate.value) != '' && isDateSmaller(theForm.applicant2DetailModel.idExpiryDate.value,currServerDate)){
						alert('ID Document Expiry should not be less than current date.');
						document.getElementById('idExpiryDate').focus();
						return false;
					}
					
					if( trim(theForm.applicant3DetailModel.dob.value) != '' && isDateGreater(theForm.applicant3DetailModel.dob.value,currServerDate)){
						alert('Future date of birth is not allowed.');
						document.getElementById('dobDate3').focus();
						return false;
			   		}else if( isAgeLessThan18Years(theForm.applicant3DetailModel.dob.value)){
						alert('Applicant age cannot be less than 18 years');
						document.getElementById('dobDate3').focus();
						return false;
					}
					
					if(!isDobGreaterThan60Years(theForm.applicant3DetailModel.dob.value) ){
						if(theForm.applicant3DetailModel.idExpiryDate.value == null || trim(theForm.applicant3DetailModel.idExpiryDate.value) == ''){
							alert('ID Document Expiry is a required field.');
							return false;
						}
					}
					
			   		if( trim(theForm.applicant3DetailModel.idExpiryDate.value) != '' && isDateSmaller(theForm.applicant3DetailModel.idExpiryDate.value,currServerDate)){
						alert('ID Document Expiry should not be less than current date.');
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
			 if(jq("#applicantDetailModelEditMode_jointAc").val()=='1'){
			    jq("#applicantDetailModelEditMode_eitherOnly").prop('checked', true);
			    jq("#applicantDetailModelEditMode_eitherOnly").prop('disabled', true);
			}else{
				jq("#applicantDetailModelEditMode_eitherOnly").prop('checked', false);
				jq("#applicantDetailModelEditMode_eitherOnly").prop('disabled', false);
			}
			 
			 toggleVisaExpiry();
		}

		jq("#applicantDetailModelEditMode_jointAc").change(function(){
			if(jq(this).val()=='1'){
			    jq("#applicantDetailModelEditMode_eitherOnly").prop('checked', true);
			    jq("#applicantDetailModelEditMode_eitherOnly").prop('disabled', true);
			}else{
				jq("#applicantDetailModelEditMode_eitherOnly").prop('checked', false);
				jq("#applicantDetailModelEditMode_eitherOnly").prop('disabled', false);
			}
		});
	
		function toggleVisaExpiry(){
			var selectedOption = document.getElementById("applicantDetailModelEditMode_idType");
			if(selectedOption.value == 3){
				document.getElementById("visaExpiry").style.cssText = "cursor:pointer; margin-left:3px;";
				document.getElementById("visaExpiry2").style.cssText = "cursor:pointer; margin-left:3px;";
			}else{
				document.getElementById("visaExpiry").style.display="none";
				document.getElementById("visaExpiry2").style.display="none";
				document.getElementById("applicantDetailModelEditMode_visaExpiryDate").value="";
			}
		}
		
		function validateRequiredMailingAddress(className)
		  {
		        var elements = document.getElementsByClassName(className);
		   
		   if(elements[0].checked  == true){ 
		    if(trim(document.getElementById("applicantDetailModelEditMode_residentialAddress").value)== ''){
		     alert("Residential Address is required field.");
		     return false;
		    }
		   }
		   else if(elements[1].checked  == true){
		    if(trim(document.getElementById("applicantDetailModelEditMode_buisnessAddress").value)== ''){
		     alert("Office/Business Address is required field.");
		     return false;
		    }
		   }
		   return true;
		  }
		
      Calendar.setup(
      {
        inputField  : "applicantDetailModelEditMode_idExpiryDate", // id of the input field
        ifFormat    : "%d/%m/%Y",      // the date format
        button      : "nicDate1",    // id of the button
        showsTime   :   false
      }
      );
      
      Calendar.setup(
      {
        inputField  : "applicantDetailModelEditMode_dob", // id of the input field
        ifFormat    : "%d/%m/%Y",      // the date format
        button      : "dobDate1",    // id of the button
        showsTime   :   false
      }
      );
      
      Calendar.setup(
    	      {
    	        inputField  : "applicantDetailModelEditMode_visaExpiryDate", // id of the input field
    	        ifFormat    : "%d/%m/%Y",      // the date format
    	        button      : "visaExpiry",    // id of the button
    	        showsTime   :   false
    	      }
    	      );
      
     window.onload = init();
      </script>
	</body>

</html>




