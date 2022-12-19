
<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>



<html>
	<head>
<meta name="decorator" content="decorator">


		<v:javascript formName="retailerContactModel" staticJavascript="false" />
		<script type="text/javascript"
			src="<c:url value="/scripts/validator.jsp"/>"></script>

		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<c:choose>
			<c:when test="${not empty param.retailerContactId}">
				<meta name="title" content="Update Agent" />
			</c:when>
			<c:otherwise>
				<meta name="title" content="New Agent SignUp" />
			</c:otherwise>
		</c:choose>

		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
		<script type="text/javascript"
			 src="${pageContext.request.contextPath}/scripts/prototype.js"></script>
		<link rel="stylesheet" type="text/css"
			href="styles/deliciouslyblue/calendar.css" />
		<link rel="stylesheet"
			href="${pageContext.request.contextPath}/styles/extremecomponents.css"
			type="text/css">
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/toolbar.js"></script>


		<script language="javascript" type="text/javascript">
		
		function loadPartnerGroup()
		{	
			var obj = new AjaxJspTag.Select("/i8Microbank/partnerTypeFormRefDataController.html", {
			parameters: "id={retailerId},appUserTypeId=3,actionType=2",target: "partnerGroupId",
			source: "retailerId",errorFunction: error});
			obj.execute();
			<c:if test="${empty param.retailerContactId}">
				document.forms.RetailerContactForm._save.disabled = false;
			</c:if>
			<c:if test="${not empty param.retailerContactId}">
				document.forms.RetailerContactForm._edit.disabled = false;
			</c:if>
		}
		function lockSave(){
			<c:if test="${empty param.retailerContactId}">
				document.forms.RetailerContactForm._save.disabled = true;
			</c:if>
			<c:if test="${not empty param.retailerContactId}">
				document.forms.RetailerContactForm._edit.disabled = true;
			</c:if>
			return true;
		}
		
	      function error(request)
	      {
	      	alert("An unknown error has occured. Please contact with the administrator for more details");
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
		
      </script>
      
       <style type="text/css">
         /*div#main{
                  padding-top: 0px;
                  padding-left: 60px;
                  width: 1000px;
            }*/
      </style>
	<%
		String createPermission = PortalConstants.ADMIN_GP_CREATE;
		createPermission +=	"," + PortalConstants.PG_GP_CREATE;
		
		String updatePermission = PortalConstants.ADMIN_GP_UPDATE;
		updatePermission +=	"," + PortalConstants.PG_GP_UPDATE;
	 %>
      
	</head>
	<body>
		<%@include file="/common/ajax.jsp"%>
		<spring:bind path="retailerContactListViewFormModel.*">
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


		<table width="100%" border="0" cellpadding="0" cellspacing="1">
			<form name="RetailerContactForm" method="POST"
				action="allpayRetailerAccountForm.html"
				onsubmit="return onFormSubmit(this)">

				<spring:bind path="retailerContactListViewFormModel.appUserId">
					<input type="hidden" name="${status.expression}"
						value="${status.value}" />
				</spring:bind>
				<spring:bind path="retailerContactListViewFormModel.oldNic">
					<input type="hidden" name="${status.expression}"
						value="${status.value}" />
				</spring:bind>

				<c:if test="${empty param.retailerContactId}">
					<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_CREATE%>" />
				</c:if>
				<c:if test="${not empty param.retailerContactId}">
					<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_UPDATE%>" />
					<input type="hidden" name="isUpdate" id="isUpdate" value="true" />
				</c:if>
				<input type="hidden" name="<%=PortalConstants.KEY_USECASE_ID%>" value="<%=PortalConstants.RETAILER_CONTACT_FORM_USECASE_ID%>" />

				<spring:bind
					path="retailerContactListViewFormModel.retailerContactId">
					<input type="hidden" name="${status.expression}"
						value="${status.value}" />
				</spring:bind>
				<spring:bind path="retailerContactListViewFormModel.versionNo">
					<input type="hidden" name="${status.expression}"
						value="${status.value}" />
				</spring:bind>
			
			<tr bgcolor="FBFBFB">
				<td colspan="6" align="center">
					&nbsp;
				</td>
			</tr>
			
			<tr bgcolor="FBFBFB">
				<td width="15%" align="right" bgcolor="F3F3F3" class="formText">
					<span style="color:#FF0000">*</span>User Name:&nbsp;
				</td>
				<td width="17%" align="left">
					<c:choose>
					<c:when test="${not empty param.retailerContactId}">
						<spring:bind path="retailerContactListViewFormModel.username">
							<input type="text" name="${status.expression}" class="textBox" tabindex="1"
										maxlength="50" value="${status.value}" readonly="readonly" style="background-color:lightgray;"/>
						</spring:bind>
					</c:when>
					<c:otherwise>
						<spring:bind path="retailerContactListViewFormModel.username">
							<input type="text" name="${status.expression}" class="textBox" tabindex="1"
										maxlength="50" value="${status.value}" />
						</spring:bind>
					</c:otherwise>
					</c:choose>
				</td>
				
				<td width="10%" align="right" bgcolor="F3F3F3" class="formText">
					<span style="color:#FF0000">*</span> Password:&nbsp;
				</td>
				<td width="17%" align="left">
					<c:choose>
					<c:when test="${not empty param.retailerContactId}">
						<spring:bind path="retailerContactListViewFormModel.password">
							<input type="password" name="${status.expression}" class="textBox" tabindex="2"
								maxlength="4000" value=""  readonly="readonly" style="background-color:lightgray;"/>
						</spring:bind>
					</c:when>
					<c:otherwise>
						<spring:bind path="retailerContactListViewFormModel.password">
							<input type="password" name="${status.expression}" class="textBox" tabindex="2"
								maxlength="4000" value="" />
						</spring:bind>
					</c:otherwise>
					</c:choose>
				</td>
				
				<td width="17%" align="right" bgcolor="F3F3F3" class="formText">
					<span style="color:#FF0000">*</span>Confirm Password:&nbsp;
				</td>
				<td width="27%" align="left">
					<c:choose>
					<c:when test="${not empty param.retailerContactId}">
						<input type="password" name="confirmPassword" class="textBox" tabindex="3"
							maxlength="4000" value="" readonly="readonly" style="background-color:lightgray;"/>
					</c:when>
					<c:otherwise>
						<input type="password" name="confirmPassword" class="textBox" tabindex="3"
							maxlength="4000" value="${status.value}" />
					</c:otherwise>
					</c:choose>
				</td>
			</tr>
			
			<tr>
				<td align="right" bgcolor="#F3F3F3" class="formText">
					<span style="color:#FF0000">*</span>Area:&nbsp;
				</td>
				<td  align="left">
					<spring:bind path="retailerContactListViewFormModel.areaId">
						<c:if test="${not empty param.retailerContactId}"><input type="hidden" name="${status.expression}" value="${status.value}" /></c:if>
						<select name="${status.expression}" class="textBox" tabindex="4"
							id="${status.expression}" 
							<c:if test="${not empty param.retailerContactId}"> disabled </c:if>
							>
							<c:if test="${empty areaModelList}">
								<option value="" />
							</c:if>
							<c:if test="${not empty areaModelList}">
							
							<c:forEach items="${areaModelList}" var="areaModelList">
								<option value="${areaModelList.areaId}"
									<c:if test="${status.value == areaModelList.areaId}">selected="selected"</c:if>>
									${areaModelList.name}
								</option>
							</c:forEach>
							
							</c:if>
						</select>
					</spring:bind>
				</td>
				
				<td align="right" bgcolor="#F3F3F3" class="formText">
					<span style="color:#FF0000">*</span>Branch/Franchise:&nbsp;
				</td>
				<td align="left">
				
					<spring:bind path="retailerContactListViewFormModel.retailerId">
						<c:if test="${not empty param.retailerContactId}"><input type="hidden" name="${status.expression}" value="${status.value}" /></c:if>
						<select name="${status.expression}" class="textBox" tabindex="5"
							id="${status.expression}" 
							<c:if test="${not empty param.retailerContactId}"> disabled </c:if>
							 >
							<c:if test="${empty retailerModelList}">
								<option value="" />
							</c:if>
							<c:if test="${not empty retailerModelList}">
							
								<c:forEach items="${retailerModelList}" var="retailerModel">
									<option value="${retailerModel.retailerId}"
										<c:if test="${status.value == retailerModel.retailerId}">selected="selected"</c:if>>
										${retailerModel.name}
									</option>
								</c:forEach>
							</c:if>
						</select>
					</spring:bind>
				</td>
			
				<td align="right" bgcolor="F3F3F3" class="formText">
					<span style="color:#FF0000">*</span>Partner Group:&nbsp;
				</td>
				<td align="left" bgcolor="FBFBFB">
					<spring:bind path="retailerContactListViewFormModel.partnerGroupId">
						<c:if test="${not empty param.retailerContactId}"><input type="hidden" name="${status.expression}" value="${status.value}" /></c:if>
						<select name="${status.expression}" class="textBox" tabindex="6"
							id="${status.expression}" 
							<c:if test="${not empty param.retailerContactId}"> disabled </c:if>
							>
							<c:if test="${empty partnerGroupModelList}">
								<option value="" />
							</c:if>
							<c:if test="${not empty partnerGroupModelList}">
							
							<c:forEach items="${partnerGroupModelList}"
								var="partnerGroupModelList">
								<option value="${partnerGroupModelList.partnerGroupId}"
									<c:if test="${status.value == partnerGroupModelList.partnerGroupId}">selected="selected"</c:if>>
									${partnerGroupModelList.name}
								</option>
							</c:forEach>
							
							</c:if>
						</select>
					</spring:bind>
				</td>
			</tr>
			
			<tr>
				<td align="right" bgcolor="#F3F3F3" class="formText">
					<span style="color:#FF0000">*</span>First Name:&nbsp;
				</td>
				<td align="left">
					<spring:bind path="retailerContactListViewFormModel.firstName">
						<input type="text" name="${status.expression}" tabindex="7"
							value="${status.value}" class="textBox" maxlength="50"
							onkeypress="return maskAlphaWithSp(this,event)" 
						/>
					</spring:bind>
				</td>
				
				<td align="right" bgcolor="#F3F3F3" class="formText">
					<span style="color:#FF0000">*</span>Last Name:&nbsp;
				</td>
				<td align="left">
					<spring:bind path="retailerContactListViewFormModel.lastName">
						<input type="text" name="${status.expression}" tabindex="8"
							value="${status.value}" class="textBox" maxlength="50"
							onkeypress="return maskAlphaWithSp(this,event)" 
						/>
					</spring:bind>
				</td>
			
				<td align="right" bgcolor="F3F3F3"
					class="formText">
					Is Sub Agent:&nbsp;
				</td>
				<td align="left" bgcolor="FBFBFB" class="formText">
					<c:choose>
					<c:when test="${not empty param.retailerContactId}">
						<spring:bind path="retailerContactListViewFormModel.head">
							<input name="${status.expression}" type="checkbox" value="true" tabindex="9"
							<c:if test="${retailerContactListViewFormModel.head==true}">checked="checked" </c:if>  disabled="true" style="background-color:lightgray;" />
						</spring:bind>
					</c:when>
					<c:otherwise>
						<spring:bind path="retailerContactListViewFormModel.head">
							<input name="${status.expression}" type="checkbox" value="true" tabindex="9"
							<c:if test="${retailerContactListViewFormModel.head==true}">checked="checked" </c:if> />
						</spring:bind>
					</c:otherwise>
					</c:choose>
				</td>
			</tr>
			
			<tr bgcolor="FBFBFB">
				<td colspan="6" align="center">
					&nbsp;
				</td>
			</tr>
			
			<tr bgcolor="#FBFBFB">
				<td align="right" bgcolor="#F3F3F3" class="formText">
					Application No:&nbsp;
				</td>
				<td align="left">
					<spring:bind path="retailerContactListViewFormModel.applicationNo">
						<input type="text" name="${status.expression}" tabindex="10"
							value="${status.value}" class="textBox" maxlength="50"
							onkeypress="return maskNumber(this,event)" 
							readonly="readonly" style="background-color: lightgray;"/>
					</spring:bind>
				</td>
				
				<td align="right" bgcolor="#F3F3F3" class="formText">
					<span style="color:#FF0000">*</span>MSISDN:&nbsp;
				</td>
				<td align="left">
					<c:choose>
						<c:when test="${not empty param.retailerContactId}">
							<spring:bind path="retailerContactListViewFormModel.mobileNumber">
								<input type="text" name="${status.expression}" tabindex="11"
									value="${status.value}" class="textBox" maxlength="11"
									onkeypress="return maskNumber(this,event)" readonly="readonly" style="background-color:lightgray;"/>
							</spring:bind>
						</c:when>
						<c:otherwise>
							<spring:bind path="retailerContactListViewFormModel.mobileNumber">
								<input type="text" name="${status.expression}" tabindex="11"
									value="${status.value}" class="textBox" maxlength="11"
									onkeypress="return maskNumber(this,event)" />
							</spring:bind>
						</c:otherwise>
					</c:choose>
				</td>
				
				<td align="right" bgcolor="F3F3F3" class="formText">
					<span style="color: rgb(255, 0, 0);">*</span> Account Opening Date:&nbsp;
				</td>
				<td align="left">
					<spring:bind path="retailerContactListViewFormModel.accountOpeningDate">
						<input type="text" id="${status.expression}" name="${status.expression}" class="textBox" tabindex="12"
							 maxlength="10" value="${status.value}" readonly="readonly" />
					</spring:bind>

					<img id="acctOpenDate" tabindex="13" name="popcal" align="top"
						style="cursor:pointer"
						src="${pageContext.request.contextPath}/images/cal.gif" border="0" />
					<img id="acctOpenDate" tabindex="14" name="popcal"
						title="Clear Date" onclick="javascript:$('accountOpeningDate').value=''"
						align="middle" style="cursor:pointer"
						src="${pageContext.request.contextPath}/images/refresh.png"
						border="0" />
				</td>
			</tr>
			
			<tr bgcolor="#FBFBFB">
				<td align="right" bgcolor="#F3F3F3" class="formText">
					<span style="color:#FF0000">*</span>NTN No:&nbsp;
				</td>
				<td align="left">
					<spring:bind path="retailerContactListViewFormModel.ntnNo">
						<input type="text" name="${status.expression}" tabindex="15"
							value="${status.value}" class="textBox" maxlength="13"
							onkeypress="return maskNumber(this,event)" />
					</spring:bind>
				</td>
				
				<td align="right" bgcolor="#F3F3F3" class="formText">
					<span style="color:#FF0000">*</span>CNIC No:&nbsp;
				</td>
				<td align="left">
					<spring:bind path="retailerContactListViewFormModel.cnicNo">
						<input type="text" name="${status.expression}" tabindex="16"
							value="${status.value}" class="textBox" maxlength="13"
							onkeypress="return maskNumber(this,event)" />
					</spring:bind>
				</td>
				
				<td align="right" bgcolor="F3F3F3" class="formText">
					<span style="color: rgb(255, 0, 0);">*</span> CNIC Expiry Date:&nbsp;
				</td>
				<td>
					<spring:bind path="retailerContactListViewFormModel.cnicExpiryDate">
						<input type="text" id="${status.expression}" name="${status.expression}" class="textBox" tabindex="17"
							 maxlength="10" value="${status.value}" readonly="readonly" />
					</spring:bind>

					<img id="nicExpiryDate" tabindex="18" name="popcal" align="top" 
						style="cursor:pointer"
						src="${pageContext.request.contextPath}/images/cal.gif" border="0" />
					<img id="nicExpiryDate" tabindex="19" name="popcal"
						title="Clear Date" onclick="javascript:$('cnicExpiryDate').value=''"
						align="middle" style="cursor:pointer"
						src="${pageContext.request.contextPath}/images/refresh.png"
						border="0" />
				</td>
			</tr>
			
			<tr bgcolor="#FBFBFB">
				<td align="right" bgcolor="#F3F3F3" class="formText">
					<span style="color:#FF0000">*</span>Contact No:&nbsp;
				</td>
				<td align="left">
					<spring:bind path="retailerContactListViewFormModel.contactNo">
						<input type="text" name="${status.expression}" tabindex="20"
							value="${status.value}" class="textBox" maxlength="13"
							onkeypress="return maskNumber(this,event)" />
					</spring:bind>
				</td>
				
				<td align="right" bgcolor="#F3F3F3" class="formText">
					<span style="color:#FF0000">*</span>Land Line No:&nbsp;
				</td>
				<td align="left">
					<spring:bind path="retailerContactListViewFormModel.landLineNo">
						<input type="text" name="${status.expression}" tabindex="21"
							value="${status.value}" class="textBox" maxlength="13"
							onkeypress="return maskNumber(this,event)" />
					</spring:bind>
				</td>
				
				<td align="right" bgcolor="#F3F3F3" class="formText">
					Mobile No:&nbsp;
				</td>
				<td align="left">
					<spring:bind path="retailerContactListViewFormModel.zongMsisdn">
						<input type="text" name="${status.expression}" tabindex="22"
							value="${status.value}" class="textBox" maxlength="11"
							onkeypress="return maskNumber(this,event)" />
					</spring:bind>
				</td>
			</tr>
			
			<tr bgcolor="#FBFBFB">
				<td align="right" bgcolor="#F3F3F3" class="formText">
					<span style="color:#FF0000">*</span>Business Name:&nbsp;
				</td>
				<td align="left">
					<spring:bind path="retailerContactListViewFormModel.businessName">
						<input type="text" name="${status.expression}" tabindex="23"
							value="${status.value}" class="textBox" maxlength="50"
							onkeypress="return maskCommon(this,event)" />
					</spring:bind>
				</td>
			</tr>
			
			<tr><td colspan="6" align="left"><b>Business Address:</b></td></tr>
			
			<tr bgcolor="#FBFBFB">
				<td align="right" bgcolor="#F3F3F3" class="formText">
					<span style="color:#FF0000">*</span>Shop No:&nbsp;
				</td>
				<td align="left">
					<spring:bind path="retailerContactListViewFormModel.shopNo">
						<input type="text" name="${status.expression}" tabindex="24"
							value="${status.value}" class="textBox" maxlength="10"
							onkeypress="return maskCommon(this,event)" />
					</spring:bind>
				</td>
				
				<td align="right" bgcolor="#F3F3F3" class="formText">
					<span style="color:#FF0000">*</span>Market / Plaza:&nbsp;
				</td>
				<td align="left">
					<spring:bind path="retailerContactListViewFormModel.marketPlaza">
						<input type="text" name="${status.expression}" tabindex="25"
							value="${status.value}" class="textBox" maxlength="50"
							onkeypress="return maskCommon(this,event)" />
					</spring:bind>
				</td>
			</tr>
			
			<tr bgcolor="#FBFBFB">
				<td align="right" bgcolor="#F3F3F3" class="formText">
					<span style="color:#FF0000">*</span>District / Tehsil / Town:&nbsp;
				</td>
				<td align="left">
					<spring:bind path="retailerContactListViewFormModel.districtTehsilTown">
						<select name="${status.expression}" class="textBox"
							id="${status.expression}" tabindex="26">
							<c:if test="${empty districtModelList}">
								<option value="" />
							</c:if>
							
							<c:if test="${not empty districtModelList}">
							
							<c:forEach items="${districtModelList}" var="districtModelList">
								<option value="${districtModelList.districtId}"
									<c:if test="${status.value == districtModelList.districtId}">selected="selected"</c:if>>
									${districtModelList.name}
								</option>
							</c:forEach>
						
							</c:if>
						</select>
					</spring:bind>
				</td>
				
				<td align="right" bgcolor="#F3F3F3" class="formText">
					<span style="color:#FF0000">*</span>City / Village:&nbsp;
				</td>
				<td align="left">
					<spring:bind path="retailerContactListViewFormModel.cityVillage">
						<select name="${status.expression}" class="textBox"
							id="${status.expression}" tabindex="27">
							<c:if test="${empty cityModelList}">
								<option value="" />
							</c:if>
							<c:if test="${not empty cityModelList}">
							<c:forEach items="${cityModelList}" var="cityModelList">
								<option value="${cityModelList.cityId}"
									<c:if test="${status.value == cityModelList.cityId}">selected="selected"</c:if>>
									${cityModelList.name}
								</option>
							</c:forEach>
							</c:if>
						
						</select>
					</spring:bind>
				</td>
			</tr>
			
			<tr bgcolor="#FBFBFB">
				<td align="right" bgcolor="#F3F3F3" class="formText">
					<span style="color:#FF0000">*</span>Post Office:&nbsp;
				</td>
				<td align="left">
					<spring:bind path="retailerContactListViewFormModel.postOffice">
						<select name="${status.expression}" class="textBox"
							id="${status.expression}" tabindex="28">
							<c:if test="${empty postalOfficeModelList}">
								<option value="" />
							</c:if>
							
							<c:if test="${not empty postalOfficeModelList}">
							<c:forEach items="${postalOfficeModelList}" var="postalOfficeModelList">
								<option value="${postalOfficeModelList.postalOfficeId}"
									<c:if test="${status.value == postalOfficeModelList.postalOfficeId}">selected="selected"</c:if>>
									${postalOfficeModelList.name}
								</option>
							</c:forEach>
							</c:if>
						</select>
					</spring:bind>
				</td>
				
				<td align="right" bgcolor="#F3F3F3" class="formText">
					<span style="color:#FF0000">*</span>NTN No:&nbsp;
				</td>
				<td align="left">
					<spring:bind path="retailerContactListViewFormModel.ntnNumber">
						<input type="text" name="${status.expression}" tabindex="29"
							value="${status.value}" class="textBox" maxlength="13"
							onkeypress="return maskNumber(this,event)" />
					</spring:bind>
				</td>
			</tr>
			
			<tr bgcolor="#FBFBFB">
				<td align="right" bgcolor="#F3F3F3" class="formText">
					<span style="color:#FF0000">*</span>Nearest Landmark:&nbsp;
				</td>
				<td align="left">
					<spring:bind path="retailerContactListViewFormModel.nearestLandmark">
						<input type="text" name="${status.expression}" tabindex="30"
							value="${status.value}" class="textBox" maxlength="100"
							onkeypress="return maskCommon(this,event)" />
					</spring:bind>
				</td>
			</tr>
			
			<tr>
				<td colspan="6" align="center">
					&nbsp;
				</td>
			</tr>
			
			<tr bgcolor="#FBFBFB">
				<td align="right" bgcolor="#F3F3F3" class="formText">
					<span style="color:#FF0000">*</span>Nature of Business:&nbsp;
				</td>
				<td align="left">
					<spring:bind path="retailerContactListViewFormModel.natureOfBusiness">
						<select name="${status.expression}" class="textBox"
							id="${status.expression}" tabindex="31">
							<c:if test="${empty natureOfBusinessModelList}">
								<option value="" />
							</c:if>
							
							<c:if test="${not empty natureOfBusinessModelList}">
							<c:forEach items="${natureOfBusinessModelList}" var="natureOfBusinessModelList">
								<option value="${natureOfBusinessModelList.natureOfBusinessId}"
									<c:if test="${status.value == natureOfBusinessModelList.natureOfBusinessId}">selected="selected"</c:if>>
									${natureOfBusinessModelList.name}
								</option>
							</c:forEach>
							</c:if>
							
						</select>
					</spring:bind>
				</td>
			</tr>
			<tr>
				<td colspan="6" align="center">&nbsp;</td>
			</tr>
			<tr>
				<td colspan="6">
					
				<authz:authorize ifAnyGranted="<%=createPermission%>">
					<c:if test="${empty param.retailerContactId}">
						<input type="button" name="_save" value="  Save  " tabindex="32"
							onclick="javascript:onSave(document.forms.RetailerContactForm,null);"
							class="button" />
						
						<input type="button" name="_cancel" value=" Cancel " tabindex="33"
							onclick="javascript:document.location='allpayRetailerAccountForm.html';"
							class="button" />
					</c:if>
				</authz:authorize>
				<authz:authorize ifAnyGranted="<%=updatePermission%>">
					<c:if test="${not empty param.retailerContactId}">
						<input type="button" name="_edit" value="  Update  " tabindex="32"
							onclick="javascript:onSave(document.forms.RetailerContactForm,null);"
							class="button" />
				
						<input type="button" name="_print" class="button" value="Print Form"  tabindex="34" onclick="openPrinterWindow();"/>
					</c:if>
				</authz:authorize>
				</td>
			</tr>
			</form>
		</table>


		<ajax:select source="areaId" target="retailerId"
			baseUrl="${contextPath}/retailercontactformrefdata.html"
			parameters="areaId={areaId},rType=1" preFunction="lockSave" postFunction="loadPartnerGroup"
			errorFunction="error" />

		<ajax:select source="retailerId" target="partnerGroupId"
			baseUrl="${contextPath}/partnerTypeFormRefDataController.html"
			parameters="id={retailerId},appUserTypeId=3,actionType=2"
			errorFunction="error" />


		<script type="text/javascript" language="javascript">
		Form.focusFirstElement($('RetailerContactForm'));
		function revertDropdowns(theForm)
{
	
	theForm.reset();
	var obj = new AjaxJspTag.Select("/i8Microbank/retailercontactformrefdata.html", {
	parameters: "areaId={areaId},rType=1",
	target: "retailerId",
	postFunction: "loadPartnerGroup",
	source: "areaId",
	errorFunction: error
	});
	obj.execute();
	
	

}
		
		
		
function popupCallback(src,popupName,columnHashMap,param)
{
  if (src=="retailerName")
  {
    
    document.forms.RetailerContactForm.retailerId.value = columnHashMap.get('PK');
    document.forms.RetailerContactForm.retailerName.value = columnHashMap.get('name');
  }
  
  
  if (src=="areaName")
  {
    document.forms.RetailerContactForm.areaId.value = columnHashMap.get('PK');
    document.forms.RetailerContactForm.areaName.value = columnHashMap.get('name');
  }
}


function isDateGreaterOrEqualForCard(from, to) {

	var fromDate;
	var toDate;

	var result = false;

//  format dd/mm/yyyy
	fromDate = from.substring(6,10) + from.substring(3,5) + from.substring(0,2);
	toDate = to.substring(6,10)   + to.substring(3,5) + to.substring(0,2);
	if( fromDate != toDate && fromDate > toDate ) {
		result = true;
	}

	return result;
}

function onFormSubmit(Form)
{
  	if( document.forms.RetailerContactForm.isUpdate == null || document.forms.RetailerContactForm.isUpdate.value == 'false' ){
	  	if (document.forms.RetailerContactForm.username.value==''){
	  			alert("User Name is a required field");
			    document.forms.RetailerContactForm.username.focus();
			    return false;
	  	}
		if( document.forms.RetailerContactForm.password.value != document.forms.RetailerContactForm.confirmPassword.value ){			
			alert("Password and Confirm Password do not match");
			document.forms.RetailerContactForm.password.focus();
		    return false;
				//return validateRetailerContactModel(document.forms.RetailerContactForm);
		}
		
		if( document.forms.RetailerContactForm.password.value == '' || document.forms.RetailerContactForm.confirmPassword.value == '' ){
			alert("Password and Confirm Password are required.");
			document.forms.RetailerContactForm.password.focus();
			return false;
			//return validateRetailerContactModel(document.RetailerContactForm);
		} 
 		if(document.forms.RetailerContactForm.password.value != document.forms.RetailerContactForm.confirmPassword.value){
			alert("Password and Confirm Password do not match");
			document.forms.RetailerContactForm.password.focus();
			return false;
		}
		
		
	}
		if (document.forms.RetailerContactForm.areaId.value==''){
  			alert("Area is a required field. Please Select");
		   document.forms.RetailerContactForm.areaId.focus();
		   return false;
  		}
  		if (document.forms.RetailerContactForm.retailerId.value==''){
  			alert("Branch/Franchise is a required field. Please Select");
		   document.forms.RetailerContactForm.retailerId.focus();
		   return false;
  		}
  		if (document.forms.RetailerContactForm.partnerGroupId.value==''){
  			alert("Partner Group is a required field. Please Select");
		   document.forms.RetailerContactForm.partnerGroupId.focus();
		   return false;
  		}
		if (document.forms.RetailerContactForm.firstName.value==''){
  			alert("First Name is a required field");
		   document.forms.RetailerContactForm.firstName.focus();
		   return false;
  		}
  		if (document.forms.RetailerContactForm.lastName.value==''){
  			alert("Last Name is a required field");
		   document.forms.RetailerContactForm.lastName.focus();
		   return false;
  		}
  		
  	if( document.forms.RetailerContactForm.isUpdate == null || document.forms.RetailerContactForm.isUpdate.value == 'false' ){
  		if (document.forms.RetailerContactForm.mobileNumber.value==''){
  			alert("MSISDN is a required field");
  			document.forms.RetailerContactForm.mobileNumber.focus();
		   return false;
		}else if (document.forms.RetailerContactForm.mobileNumber.value.length != 11){
			alert ("MSISDN must be 11 characters.");
			return false;
		}
  		var zongNoVal = document.forms.RetailerContactForm.mobileNumber.value;
		if(zongNoVal != null ){
			if( trim(zongNoVal) != ''){
				var noStartsWith = zongNoVal.substring(0,2);
				if(noStartsWith != '03'){
					alert('MSISDN always starts with 03');
					document.forms.RetailerContactForm.mobileNumber.focus();
					return false;
				}
			}
		}
  	}	
  		
  		if (document.forms.RetailerContactForm.accountOpeningDate.value==''){
  			alert("Account Opening Date is a required field");
		   document.forms.RetailerContactForm.accountOpeningDate.focus();
		   return false;
  		}
  		var currServerDate = '<%=PortalDateUtils.currentFormattedDate("dd/MM/yyyy")%>';
   		if( trim(document.forms.RetailerContactForm.accountOpeningDate.value) != '' && isDateGreater(document.forms.RetailerContactForm.accountOpeningDate.value,currServerDate)){
			alert("Account Opening Date should not be greater than current date.");
			document.getElementById('accountOpeningDate').focus();
			return false;
		}
  		
  		
  		if (document.forms.RetailerContactForm.ntnNo.value==''){
  			alert("NTN No number is a required field");
		   document.forms.RetailerContactForm.ntnNo.focus();
		   return false;
  	}
  		if (document.forms.RetailerContactForm.cnicNo.value==''){
  			alert("CNIC No is a required field");
		   document.forms.RetailerContactForm.cnicNo.focus();
		   return false;
  	}
  		var cnicVal = document.forms.RetailerContactForm.cnicNo.value;
		if(cnicVal != null){
			if( trim(cnicVal) != '' && cnicVal.length == 15){
				var firstHyphin = cnicVal.substring(5,6);
				var secondHyphin = cnicVal.substring(13,14);
				if(firstHyphin != '-' || secondHyphin != '-'){
				alert('CNIC No is not in proper format. Format should be #####-#######-#');
				document.forms.RetailerContactForm.cnicNo.focus();
				return false;
				}
			}
		}
	  	if (document.forms.RetailerContactForm.cnicNo.value.length < 13){
	  			alert("CNIC No can not be less than 13 characters.");
			   document.forms.RetailerContactForm.cnicNo.focus();
			   return false;
	  	}else if (document.forms.RetailerContactForm.cnicNo.value.length > 13){
	  			alert("CNIC No can not be more than 13 characters.");
			   document.forms.RetailerContactForm.cnicNo.focus();
			   return false;
	  	}
  		if (document.forms.RetailerContactForm.cnicExpiryDate.value==''){
  			alert("CNIC Expiry Date is a required field");
		   document.forms.RetailerContactForm.cnicExpiryDate.focus();
		   return false;
  		}
  		if( trim(document.forms.RetailerContactForm.cnicExpiryDate.value) != '' && isDateSmaller(document.forms.RetailerContactForm.cnicExpiryDate.value,currServerDate)){
			alert("CNIC Expiry should not be less than current date.");
			document.getElementById('cnicExpiryDate').focus();
			return false;
		}

  	
  	
  	
  	  		if (document.forms.RetailerContactForm.contactNo.value==''){
  			alert("Contact No is a required field");
  			document.forms.RetailerContactForm.contactNo.focus();
		   return false;
  	}
  			if (document.forms.RetailerContactForm.landLineNo.value==''){
  			alert("LandLine No is a required field");
  			document.forms.RetailerContactForm.landLineNo.focus();
		   return false;
  	}
  			if (document.forms.RetailerContactForm.businessName.value==''){
  			alert("Business Name is a required field");
  			document.forms.RetailerContactForm.businessName.focus();
		   return false;
  	}
  			if (document.forms.RetailerContactForm.shopNo.value==''){
  			alert("Shop No is a required field");
  			document.forms.RetailerContactForm.shopNo.focus();
		   return false;
  	}
  			if (document.forms.RetailerContactForm.marketPlaza.value==''){
  			alert("Market / Plaza is a required field");
  			document.forms.RetailerContactForm.marketPlaza.focus();
		   return false;
  	}
  			if (document.forms.RetailerContactForm.districtTehsilTown.value==''){
  			alert("District / Tehsil / Town is a required field");
  			document.forms.RetailerContactForm.districtTehsilTown.focus();
		   return false;
  	}
  			if (document.forms.RetailerContactForm.cityVillage.value==''){
  			alert("City / Village is a required field");
  			document.forms.RetailerContactForm.cityVillage.focus();
		   return false;
  	}
  			if (document.forms.RetailerContactForm.postOffice.value==''){
  			alert("Post Office is a required field");
  			document.forms.RetailerContactForm.postOffice.focus();
		   return false;
  	}
  			if (document.forms.RetailerContactForm.ntnNumber.value==''){
  			alert("NTN No is a required field");
  			document.forms.RetailerContactForm.ntnNumber.focus();
		   return false;
  	}
  			if (document.forms.RetailerContactForm.nearestLandmark.value==''){
  			alert("Nearest Landmark is a required field");
  			document.forms.RetailerContactForm.nearestLandmark.focus();
		   return false;
  	}
    
    return true;
}
    
    highlightFormElements();
    if( document.forms[0].isUpdate== null )
        document.forms[0].areaId.focus();
    else
        document.forms[0].firstName.focus();
	
	Calendar.setup(
      {
        inputField  : "accountOpeningDate", // id of the input field
        ifFormat    : "%d/%m/%Y",      // the date format
        button      : "acctOpenDate"    // id of the button
      }
      );
      
    Calendar.setup(
      {
        inputField  : "cnicExpiryDate", // id of the input field
        ifFormat    : "%d/%m/%Y",      // the date format
        button      : "nicExpiryDate"    // id of the button
      }
      ); 
      
      function openPrinterWindow(){
			var retailerContactId = '<%= request.getParameter("retailerContactId") %>';
			var location ='${pageContext.request.contextPath}/allpayRetailerAccountForm_printer.html?retailerContactId='+ retailerContactId; 
			window.open(location,'printWindow');
			
		} 
    
</script>


	</body>
</html>
