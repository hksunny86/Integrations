<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<meta name="decorator" content="decorator">
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<meta name="title" content="Disbursement Upload" />
	<script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
	<script type="text/javascript" src="${contextPath}/scripts/prototype.js"></script>
	<script type="text/javascript" src="${contextPath}/scripts/toolbar.js"></script>
	<%@include file="/common/ajax.jsp"%>
	<script type="text/javascript" src="${contextPath}/scripts/jquery-1.7.2.min.js"></script>
	<script type="text/javascript">

		var _en = 'csvFile';

		function disableEnableFile(dis, en) {
			_en = en;
			document.getElementById(dis).disabled = true;
			document.getElementById(en).disabled = false;
		}

		var jq=$.noConflict();
		function sourceACInitProgress(){
			if(document.forms[0].accountType.value == ''){
				alert('Please Select Account Type');
				return false;
			}
			if(document.forms[0].sourceACNo.value == ''){
				alert('Account # is mandatory for Title Fetch');
				return false;
			}
		
			$('errorMessages').innerHTML = "";
			Element.hide('errorMessages');
			return true;
		}

		function sourceACEndProgress(){
			if(document.forms[0].sourceACNick.value == 'null' || document.forms[0].sourceACNick.value == " "){
				document.forms[0].sourceACNick.value = '';
				Element.show('errorMessages');
				$('errorMessages').innerHTML = $F('errMsg');
				scrollToTop(1000);

			}

			if(document.forms[0].errMsg.value == 'null'){
				document.forms[0].errMsg.value = '';
			}
		}

		function error() {
			alert("An unknown error has occurred. Please contact with the administrator for more details");
		}

		window.onload = resetForm;
		function resetForm()
		{
		}

		function onFormSubmit(theForm)
		{
			if(document.forms.bulkDisbursmentsForm.serviceId.value == ''){
				alert("Please select Service Type.");
				return false;
			}

			if(document.forms.bulkDisbursmentsForm.productId.value == ''){
				alert("Please select Product.");
				return false;
			}

			/*
			 if(document.forms.bulkDisbursmentsForm.csvFile.value == ''){
			 alert("Disbursements/Payments Location is required.");
			 return false;
			 }
			 if(!document.forms.bulkDisbursmentsForm.csvFile.value.endsWith('.csv')){
			 alert("Only files with extension (.csv) are allowed.");
			 return false;
			 }
			 if(getFileSize() > 2)
			 {
			 alert("File to upload can't be more than 2MB in size.");
			 return false;
			 }
			 */
			if(document.forms.bulkDisbursmentsForm.sourceACNo.value == ''){
				alert("Source Account No. is required.");
				return false;
			}else if(document.forms.bulkDisbursmentsForm.sourceACNo.value != ''){
				if(document.forms.bulkDisbursmentsForm.sourceACNick.value == ''){
					alert("Please fetch account title");
					return false;
				}
			}


			var file = document.getElementById(_en);

			if(_en == 'csvFile') {

				if(file.value == ''){
					alert("Disbursements file location is required.");
					return false;
				}
				if(!file.value.endsWith('.csv')){
					alert("Only files with extension (.csv) are allowed.");
					return false;
				}

				// if(getFileSize() > 2) {
				// 	alert("File to upload can't be more than 2MB in size.");
				// 	return false;
				// }

				if(getFileSize() <= 0) {
					alert("Kindly upload valid file.");
					return false;
				}

			} else if(_en == 'bulkDisbursementsFilePath') {

				if(document.forms.bulkDisbursmentsForm.bulkDisbursementsFilePath.value == ''){
					alert("Please select FTP File to process.");
					return false;
				}
			}

			onSave(document.forms.bulkDisbursmentsForm,null);

		}

		function disableSubmit()
		{
			document.forms.bulkDisbursmentsForm._save.disabled=true;

			onSave(document.forms.bulkDisbursmentsForm,null);
		}

		function resetLimitApplicable(productCombobox)
		{
			if(productCombobox.value == bulkDisbursementsProductId || productCombobox.value == "")//Bulk Discursements
			{
				document.getElementById('limitApplicable').checked = false;
				document.getElementById('limitApplicable').disabled = true;
			}
			else if(productCombobox.value == bulkPaymentsProductId )//Bulk Payment
			{
				document.getElementById('limitApplicable').disabled = false;
			}
		}

		/**
		 *	method : getFileSize()
		 *	author : Naseer
		 *	description : this method will return file size in mega bytes (mb)
		 */
		function getFileSize()
		{
			var file = document.getElementById('csvFile');
			if(file != null && file.files != null && file.files[0] != null)
			{
				return ((file.files[0].size/1024)/1024);
			}
			return 0;
		}

		function checkRadio(checkName) {

			if(document.forms.bulkDisbursmentsForm.serviceId.value == 15) {
				alert("You can't select "+checkName+" option with Service "+
						document.forms.bulkDisbursmentsForm.serviceId.options[document.forms.bulkDisbursmentsForm.serviceId.selectedIndex].text);
				return false;
			}
			return true;
		}
		
		function toggleLimitDiv(){
			if(jq("#serviceId").val()==13) {
				jq("#limitrow").show();
				return true;
			}
			else{
				jq("#limitrow").hide();
				jq("#limitApplicable").prop("checked",false);
				return true;
			}
		}

		function changeBMVerification(){
			if(jq("#serviceId").val()==15) {

				jq("#userType").show();

				jq("#payCashViaCnic").prop("checked",false);
				jq("#payCashViaCnic").hide();
				jq("#payCashViaCnic").closest('td').prev('td').hide();
			}
			else{
				jq("#userType").hide();

				jq("#payCashViaCnic").show();
				jq("#payCashViaCnic").closest('td').prev('td').show();
			}
		}

		function resetFromAccountNick(){
			document.getElementById('sourceACNick').value =  '';
		}
		
		function accountTypeChange(sel){
			if(sel.value == 1){
				document.getElementById('acType').value = '1';
			}else if(sel.value == 2){
				document.getElementById('acType').value =	'2';
			}
			else if(sel.value == 3){
				document.getElementById('acType').value =	'3';
			}
		}


	</script>
</head>

<body bgcolor="#ffffff">

<div id="successMsg" class="infoMsg" style="display: none;"></div>
<div id="errorMsg" class="errorMsg" style="display: none;"></div>

<spring:bind path="bulkDisbursementsVOModel.*">
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

<c:remove var="bulkDisbursementsModelList" scope="session" />
<html:form name="bulkDisbursmentsForm" commandName="bulkDisbursementsVOModel" enctype="multipart/form-data" method="POST"
		   action="p_bulkdisbursements.html">
	 <input type="hidden" id="flag" value="true"/>
	<input type="hidden" id="errMsg" value=""/>
	 <input type="hidden" id="acType" name="acType" />
	<input type="hidden" id="message" name="message" value=""/>
	<input type="hidden" id="errorMesg" name="errorMesg" value=""/>
	<input type="hidden" name="csvAbsPath" value="${csvAbsPath}" />
	<input type="hidden" name="isUpdate" id="isUpdate" value="true" />
	<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_UPDATE%>" />
	<div class="infoMsg" id="errorMessages" style="display:none;"></div>
	<table width="100%" border="0" cellpadding="0" cellspacing="1">
		<tr>
			<td align="right" bgcolor="F3F3F3" class="formText" width="50%">
				<span style="color: #FF0000">*</span>Service:			</td>
			<td align="left" bgcolor="FBFBFB" class="text">
				<html:select id="serviceId" path="serviceId" cssClass="textBox" tabindex="1">
					<html:option value="">--- Select ---</html:option>
					<c:if test="${serviceModelList != null}">
						<html:options items="${serviceModelList}" itemValue="id" itemLabel="label"/>
					</c:if>
				</html:select>			</td>
		</tr>
		<tr>
			<td align="right" bgcolor="F3F3F3" class="formText" width="50%">
				<span style="color: #FF0000">*</span>Product:			</td>
			<td align="left" bgcolor="FBFBFB">
				<html:select path="productId" cssClass="textBox"  id="productId" tabindex="2">
					<html:option value="0">---Select---</html:option>
					<c:if test="${productList != null}">
						<html:options items="${productList}" itemValue="id" itemLabel="label"/>
					</c:if>
				</html:select>
			</td>
		</tr>
		<tr id="userType" style="display: none;">
			<td align="right" bgcolor="F3F3F3" class="formText" width="50%">
				<span style="color: #FF0000">*</span>User Type:			</td>
			<td align="left" bgcolor="FBFBFB">
				<html:select path="appUserTypeId" cssClass="textBox"  id="appUserTypeId" tabindex="3">
					<c:if test="${appUserTypeList != null}">
						<html:options items="${appUserTypeList}" itemValue="appUserTypeId" itemLabel="name"/>
					</c:if>
				</html:select>
			</td>
		</tr>
<%-- 		<tr>
			<td align="right" bgcolor="F3F3F3" class="formText" width="50%"><span style="color: #FF0000">*</span>Account Type [Core/BLB]:</td>
			<td align="left" bgcolor="FBFBFB" class="text">

				<html:radiobutton id="accountType" name="accountType" value="1" path="accountType"
								  onclick="document.getElementById('accountTypeValue').value = this.value;document.getElementById('sourceACNick').value='';document.getElementById('sourceACNo').value='';"/>Core Account

				<html:radiobutton id="accountType" name="accountType" value="2" path="accountType" checked = "true"
								  onclick="document.getElementById('accountTypeValue').value = this.value;document.getElementById('sourceACNick').value='';document.getElementById('sourceACNo').value='';"/>BLB Account

				<input id="accountTypeValue" name="accountTypeValue" type="hidden" />
			</td>
		</tr> --%>
		
		<tr bgcolor="FBFBFB">
					<td align="right" bgcolor="F3F3F3" class="formText">
						<span style="color:#FF0000">*</span>Account Type:
					</td>
					<td align="left">						
						<html:select path="accountType" id="accountType" onchange="accountTypeChange(this);" tabindex="2" cssClass="textBox">
							<html:option value="">---All---</html:option>
							<html:option value="1">BLB Account</html:option>
							<html:option value="2">Core Account</html:option>
							<html:option value="3">GL Account</html:option>
						</html:select>
					</td>
				</tr>

		<tr bgcolor="FBFBFB">
			<td align="right" bgcolor="F3F3F3" class="formText">
				<span style="color: #FF0000">*</span>Source Account (Core/BLB) #:
			</td>
			<td align="left">
				<html:input path="sourceACNo" id="sourceACNo" value="" cssClass="textBox" tabindex="4" onkeypress="return maskNumber(this,event)"
							maxlength="50" disabled="false"  onchange="resetFromAccountNick();"/>
				<input id="fetchSourceACButton" name="fetchButton" type="button" value="Fetch Title" class="button" /></td>
		</tr>
		<tr>
			<td height="16" align="right" bgcolor="F3F3F3" class="formText">
				<span style="color: #FF0000">*</span>Source Account Nick:			</td>
			<td width="58%" align="left" bgcolor="FBFBFB">
				<html:input path="sourceACNick" id="sourceACNick" name="sourceACNick" cssClass="textBox" readonly="true" style="background: #D3D3D3;"/>			</td>
		</tr>
		<tr>
			<td align="right" bgcolor="F3F3F3" class="formText" width="50%"><span style="color: #FF0000">*</span>File Source:</td>
			<td align="left" bgcolor="FBFBFB" class="text">

				<html:radiobutton id="fileSourceFTP" name="fileSourceFTP" value="0" path="fileSourceFTP"
								  onclick="disableEnableFile('bulkDisbursementsFilePath','csvFile')"/>Upload

				<html:radiobutton id="fileSourceFTP" name="fileSourceFTP" value="1" path="fileSourceFTP"
								  onclick="disableEnableFile('csvFile', 'bulkDisbursementsFilePath')"/>FTP
			</td>
		</tr>

		<tr>
			<td align="right" bgcolor="F3F3F3" class="formText" width="50%">Select File:</td>
			<td align="left" bgcolor="FBFBFB" class="text">
				<html:select id="bulkDisbursementsFilePath" path="bulkDisbursementsFilePath" cssClass="textBox" tabindex="2" disabled="true">
					<html:option value="">--- Select ---</html:option>
					<c:if test="${bulkDisbursementsFilePathList != null}">

						<c:forEach items="${bulkDisbursementsFilePathList}" var="filePath">
							<option value="${filePath}"><c:out value="${filePath}"/></option>
						</c:forEach>

						<html:options items="${fileInfoModelList}" itemValue="bulkDisbursementsFileInfoId" itemLabel="filePath"/>
					</c:if>
				</html:select>			</td>
		</tr>

		<tr>
			<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">
				Disbursements File Location:			</td>
			<td bgcolor="FBFBFB" class="text">

				<spring:bind path="bulkDisbursementsVOModel.csvFile">
					<input type="file" id="csvFile" name="csvFile" tabindex="4" class="upload"/>
				</spring:bind>
				&nbsp;&nbsp;			</td>
		</tr>

		<tr id="limitrow" style="display:none;">
			<td align="right" bgcolor="F3F3F3" class="formText" width="50%">Apply Limits:</td>
			<td align="left" bgcolor="FBFBFB" class="text"><html:checkbox id="limitApplicable" path="limitApplicable" tabindex="5"/></td>
		</tr>
		<%-- <tr>
			<td align="right" bgcolor="F3F3F3" class="formText" width="50%">Pay Cash via CNIC:</td>
			<td align="left" bgcolor="FBFBFB" class="text"><html:checkbox id="payCashViaCnic" path="payCashViaCnic" tabindex="5" onclick="return checkRadio('Pay Cash via CNIC')"/></td>
		</tr> --%>
		<!--
						<tr>
							<td align="right" bgcolor="F3F3F3" class="formText" width="50%">
								Pay Cash Via CNIC:
							</td>
							<td align="left" bgcolor="FBFBFB" class="text">
								<html:checkbox id="payCashViaCnic" path="payCashViaCnic" tabindex="6"/>
							</td>
						</tr>
						 -->
			<%--<tr>
                <td align="right" bgcolor="F3F3F3" class="formText" width="50%">Chrages / File</td>
                <td align="left" bgcolor="FBFBFB" class="text">
                    <html:input path="chargesPerFile" value="" cssClass="textBox" style="background: #D3D3D3;"/>			</td>
            </tr>
            <tr>
                <td align="right" bgcolor="F3F3F3" class="formText" width="50%">FED / File</td>
                <td align="left" bgcolor="FBFBFB" class="text">
                    <html:input path="fedPerFile" value="" cssClass="textBox" style="background: #D3D3D3;"/>			</td>
            </tr>--%>
		<tr>
			<td width="50%">&nbsp;</td>
			<td align="left">
				<input type="button" name="_save" value="  Submit  " tabindex="7"
					   onclick="javascript:onFormSubmit(document.forms.bulkDisbursmentsForm);" class="button" />			</td>
		</tr>
	</table>
	<ajax:select source="serviceId" target="productId"
				 baseUrl="${contextPath}/p_productRefData.html" errorFunction="error"
				 parameters="serviceId={serviceId}" postFunction="changeBMVerification" preFunction="toggleLimitDiv"/>
</html:form>

<%-- <ajax:updateField parser="new ResponseXmlParser()" source="sourceACNo, accountTypeValue" action="fetchSourceACButton" target="sourceACNick,errMsg" baseUrl="${contextPath}/fetchtitle.html" parameters="accountNo={sourceACNo},type={accountTypeValue}" preFunction="sourceACInitProgress" postFunction="sourceACEndProgress"></ajax:updateField>
 --%>
 <ajax:updateField parser="new ResponseXmlParser()" source="sourceACNo" action="fetchSourceACButton" target="sourceACNick,errMsg" baseUrl="${contextPath}/fetchtitlemanualadjustment.html" parameters="accountNo={sourceACNo},type={acType},isBulk={flag},productId={productId}" preFunction="sourceACInitProgress" postFunction="sourceACEndProgress"></ajax:updateField>
</body>

</html>