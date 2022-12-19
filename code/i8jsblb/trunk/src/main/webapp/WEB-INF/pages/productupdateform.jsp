<!--Author: Asad hayat http://www.leviton.com/sections/prodinfo/newprod/npleadin.htm-->

<%@ include file="/common/taglibs.jsp"%>
<%@ page import="com.inov8.microbank.common.util.*" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<meta name="decorator" content="decorator2">
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<%@include file="/common/ajax.jsp"%>
	<link type="text/css" rel="stylesheet" href="styles/ajaxtags.css" />
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
	<script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>

	<meta name="title" content="Edit Product"/>
	<c:if test="${not empty param.productId or not empty productId}">
		<meta name="title" content="Edit Product" />
	</c:if>
	<c:if test="${empty param.productId and empty productId}">
		<meta name="title" content="Add Product" />
	</c:if>

	<%
		String createPermission = PortalConstants.MNG_PRODUCT_CREATE;
		createPermission +=	"," + PortalConstants.PG_GP_CREATE;
		createPermission +=	"," + PortalConstants.ADMIN_GP_CREATE;


		String updatePermission = PortalConstants.MNG_PRODUCT_UPDATE;
		updatePermission +=	"," + PortalConstants.PG_GP_UPDATE;
		updatePermission +=	"," + PortalConstants.ADMIN_GP_UPDATE;
	%>



	<script language="javascript" type="text/javascript">


	</script>

</head>
<body>

<spring:bind path="productModel.*">
	<c:if test="${not empty status.errorMessages}">
		<div class="errorMsg">
			<c:forEach var="error" items="${status.errorMessages}">
				<c:out value="${error}" escapeXml="false" />
				<br/>
			</c:forEach>
		</div>
	</c:if>
</spring:bind>
<c:if test="${not empty messages}">
	<div class="infoMsg" id="successMessages">
		<c:forEach var="msg" items="${messages}">
			<c:out value="${msg}" escapeXml="false"/><br />
		</c:forEach>
	</div>
	<c:remove var="messages" scope="session"/>
</c:if>

<html:form commandName="productModel" id="productForm" method="post" action="productupdateform.html" onsubmit="return validateForm(this);" >
	<table width="100%"  border="0" cellpadding="0" cellspacing="1">

		<input type="hidden" name="resubmitRequest" value="${param.isReSubmit}" />

			<%-- <html:hidden path="serviceId" /> --%>
		<html:hidden path="productId" />
		<html:hidden path="versionNo" />
		<html:hidden path="createdBy" />
		<html:hidden path="updatedBy" />
		<html:hidden path="createdOn" />
		<html:hidden path="updatedOn" />
		<html:hidden path="productIntgModuleInfoId" />
		<html:hidden path="productIntgVoId" />
		<html:hidden path="instructionId" />
		<html:hidden path="successMessageId" />
		<html:hidden path="failureMessageId" />
		<html:hidden path="helpLine" />
		<html:hidden path="costPrice" />
		<html:hidden path="unitPrice" />
		<html:hidden path="fixedDiscount" />
		<html:hidden path="percentDiscount" />
		<html:hidden path="minimumStockLevel" />
		<html:hidden path="comments" />
		<html:hidden path="batchMode" />
		<html:hidden path="consumerLabel" />
		<html:hidden path="doValidate" />
		<html:hidden path="amtRequired" />
		<html:hidden path="productCode" />
		<html:hidden path="categoryCode" />
		<html:hidden path="relationCategoryIdCategoryModel.categoryId" />


		<tr bgcolor="FBFBFB">
			<td colspan="2" align="center">&nbsp;</td>
		</tr>
		<tr>
			<td width="25%" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Service:&nbsp;</td>
			<td width="25%" align="left" bgcolor="FBFBFB">
				<c:choose>
					<c:when test="${not empty param.productId or not empty productId}">
						<c:choose>
							<c:when test="${productModel.serviceIdServiceModel.serviceTypeId!=10}">
								<html:select path="serviceId" cssClass="textBox" disabled="true"  >
									<html:options items="${allServiceList}" itemLabel="name" itemValue="serviceId"/>
								</html:select>
								<html:hidden path="serviceId" />
							</c:when>
							<c:otherwise>
								<html:select path="serviceId" cssClass="textBox" disabled="true" >
									<html:options items="${serviceModelList}" itemLabel="name" itemValue="serviceId"/>
								</html:select>
								<html:hidden path="serviceId" />
							</c:otherwise>
						</c:choose>
					</c:when>
					<c:otherwise>
						<html:select path="serviceId" cssClass="textBox">
							<html:option value="">---SELECT---</html:option>
							<html:options items="${serviceModelList}" itemLabel="name" itemValue="serviceId"/>
						</html:select>
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td width="50%" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Supplier:&nbsp;</td>
			<td width="50%" align="left" bgcolor="FBFBFB">
				<html:select path="supplierId" cssClass="textBox" disabled="disabled">
					<html:option value="">---SELECT---</html:option>
					<html:options items="${SupplierModelList}" itemLabel="name" itemValue="supplierId"/>
				</html:select>
			</td>
		</tr>
		<tr >
			<td align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Product Name:&nbsp;</td>
			<td align="left" bgcolor="FBFBFB">
				<html:input path="name" cssClass="textBox" id = "prodName" size="40" maxlength="40" disabled="disabled"/>
			</td>
		</tr>
		<tr>
			<td align="right" bgcolor="F3F3F3" class="formText">Description:&nbsp;</td>
			<td align="left" bgcolor="FBFBFB">
				<html:textarea path="description" cols="50" rows="5" cssStyle="width:163px; height: 102px" onkeyup="textAreaLengthCounter(this,250);" onkeypress="return maskCommon(this,event)" cssClass="textBox" />
			</td>
		</tr>

		<tr>
			<td width="25%" align="right" bgcolor="F3F3F3" class="formText"><span id="corporateAccMendatoryCheck" style="color:#FF0000">*</span>Corporate A/c:&nbsp;</td>
			<td width="25%" align="left" bgcolor="FBFBFB">
				<html:select path="commissionStakeHolderId" cssClass="textBox">
					<html:option value="">---SELECT---</html:option>
					<html:options items="${commissionStakeholderModelCorporateTypeList}" itemLabel="name" itemValue="commissionStakeholderId"/>
				</html:select>
			</td>
			<td width="25%" align="right" bgcolor="F3F3F3" class="formText"></td>
			<td width="25%" align="left" bgcolor="FBFBFB"></td>
		</tr>

		<tr>
			<td height="25%" align="right" bgcolor="F3F3F3" class="formText">Is Taxable:</td>
			<td align="left" bgcolor="FBFBFB">
				<html:checkbox id="taxable" path="taxable" onchange="toggleWHTClause();"/>
			</td>

		</tr>
		<tr>
			<td width="25%" align="right" bgcolor="F3F3F3" class="tax formText" style="display: none;"><span style="color:#FF0000">*</span>Applicable WHT Clause:&nbsp;</td>
			<td width="25%" align="left" bgcolor="FBFBFB" style="display: none;" class="tax formText">
				<html:select path="whtConfigId" cssClass="textBox">
					<html:option value="">---SELECT---</html:option>
					<html:options items="${wHTConfigModelList}" itemLabel="title" itemValue="whtConfigId"/>
				</html:select>
			</td>
		</tr>

		<!-- added by atif hussain -->
		<tr>
			<td height="16" align="right" bgcolor="F3F3F3" class="formText">Deduct charges from Third Party:</td>
			<td align="left" bgcolor="FBFBFB">
					<html:checkbox id="linkChk" path="inclChargesCheck" onchange="toggleAccountLink();"/>
		</tr>
		<tr id="accountNoRow" style="display:none;">
			<td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Oracle #:</td>
			<td align="left" bgcolor="FBFBFB">
					<html:input id="accountNo" tabindex="8" path="accountNo" cssClass="textBox" onkeypress="return maskInteger(this,event)" maxlength="11" size="30" />
		</tr>
		<tr id="accountNickRow" style="display:none;">
			<td height="16" align="right" bgcolor="F3F3F3" class="formText"><span style="color:#FF0000">*</span>Account Title:</td>
			<td width="58%" align="left" bgcolor="FBFBFB">
				<html:input name="accountNick" id="accountNick" type="text" class="textBox" path="accountNick" size="30" maxlength="40" tabindex="4" /></td>
		</tr>
		<!-- end of addition -->
		<tr>
			<td align="right" bgcolor="F3F3F3" class="formText">Default Minimum Limit:&nbsp;</td>
			<td align="left" bgcolor="FBFBFB">
				<html:input path="minLimit" size="14" cssClass="textBox" maxlength="7" onkeypress="return checkNumeric(this,event)"/>
			</td>
		</tr>
		<tr>
			<td align="right" bgcolor="F3F3F3" class="formText">Default Maximum Limit:&nbsp;</td>
			<td align="left" bgcolor="FBFBFB">
				<html:input path="maxLimit" size="14" cssClass="textBox" maxlength="7" onkeypress="return checkNumeric(this,event)"/>
			</td>
		</tr>
		<tr>
			<td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Active:&nbsp;</td>
			<td width="51%" align="left" bgcolor="FBFBFB" class="formText">
				<html:checkbox path="active"/>
			</td>
		</tr>
		<!-- Default Charges section -->
		<tr>
			<td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Default Charges:&nbsp;</td>
			<td width="51%" align="left" bgcolor="FBFBFB" class="formText">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">&nbsp;</td>
			<td width="51%" align="left" bgcolor="FBFBFB" class="formText">
				Fixed&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;%age of Trx Amount&nbsp;
			</td>
		</tr>
		<tr>
			<td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Exclusive:&nbsp;</td>
			<td width="51%" align="left" bgcolor="FBFBFB" class="formText">
				<html:input path="exclusiveFixAmount" size="14" class="textBox" maxlength="7" onkeypress="return checkNumeric(this,event)" />
				<input type="reset" value="+" disabled="disabled">
				<html:input path="exclusivePercentAmount" size="14" class="textBox" maxlength="5" onkeypress="return checkNumeric(this,event)" />
			</td>
		</tr>
		<tr>
			<td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Inclusive:&nbsp;</td>
			<td width="51%" align="left" bgcolor="FBFBFB" class="formText">
				<html:input path="inclusiveFixAmount" size="14" class="textBox" maxlength="7" onkeypress="return checkNumeric(this,event)" />
				<input type="reset" value="+" disabled="disabled">
				<html:input path="inclusivePercentAmount" size="14" class="textBox" maxlength="5" onkeypress="return checkNumeric(this,event)" />
			</td>
		</tr>

		<tr>
			<td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Default Share Rates:&nbsp;</td>
			<td width="51%" align="left" bgcolor="FBFBFB" class="formText">
				&nbsp;
			</td>
		</tr>

			<%-- <tr>
                    <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">&nbsp;</td>
                    <td width="51%" align="left" bgcolor="FBFBFB" class="formText">
                    <html:checkbox id="isFed" path="isFed" onchange="toggleFED();" label="FED apply?"/>
                    </td>
                  </tr>
                  <tr>
                      <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">FED Rate :&nbsp;</td>
                    <td width="51%" align="left" bgcolor="FBFBFB" class="formText">
                    <html:input id="fedShare" path="fedShare" cssClass="textBox" onkeypress="return checkNumeric(this,event);" maxlength="5" />
                    </td>
                  </tr>
                  </tr>
                  <tr>
                      <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">W.H. Rate (%):&nbsp;</td>
                    <td width="51%" align="left" bgcolor="FBFBFB" class="formText">
                    <html:input id="withHoldingShare" path="withHoldingShare" cssClass="textBox" onkeypress="return checkNumeric(this,event);" maxlength="5" />
                </td>

              </tr> --%>


		<tr>
			<!-- <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">&nbsp;</td> -->
			<td align="center" bgcolor="FBFBFB" class="eXtremeTable" colspan="4">
				<table class="tableRegion" id = "sharesDefault" style="width: 50% !important;">
					<thead>
					<tr>
						<td class="tableHeader" width="40%"><h4>Stakeholder</h4></td>
						<td class="tableHeader" width="50%"><h4>Share (%)</h4></td>
						<td class="tableHeader" width="20%"><h4>W.H</h4></td>
					</tr>
					</thead>
					<input type="hidden" name="totalRowsOfShares" id="totalRowsOfShares" value="${fn:length(commStakeholderListViewModelList)}"/>
					<tbody>
					<td class="formText" width="40%">&nbsp;</td>
					<td class="formText" width="50%">&nbsp;</td>
					<td class="formText" width="20%">&nbsp;</td>
					<c:forEach items="${commStakeholderListViewModelList}" var="commStakeholderListViewModel" varStatus="commStakeholderListViewModelStatus">
						<c:set var="rowCssClass" value="odd" scope="page"/>
						<c:if test="${commStakeholderListViewModelStatus.count%2!=0}">
							<c:set var="rowCssClass" value="even" scope="page"/>
						</c:if>
						<tr id="row_${commStakeholderListViewModelStatus.index}" class="${rowCssClass}">
							<td class="formText">

								&nbsp;&nbsp;<c:out value="${commStakeholderListViewModel.name}"/>

							</td>
							<td width="15%">
								<html:input id="stakeHolderId_${commStakeholderListViewModel.commissionStakeholderId}" cssClass="textBox" path="productIdCommissionShSharesDefaultModelList[${commStakeholderListViewModelStatus.index}].commissionShare" onkeypress="return checkNumeric(this,event);" maxlength="5" />
								<html:hidden path="productIdCommissionShSharesDefaultModelList[${commStakeholderListViewModelStatus.index}].commissionShSharesDefaultId" />
								<html:hidden path="productIdCommissionShSharesDefaultModelList[${commStakeholderListViewModelStatus.index}].productId" />
								<html:hidden path="productIdCommissionShSharesDefaultModelList[${commStakeholderListViewModelStatus.index}].commissionStakeholderId"  value = "${commStakeholderListViewModel.commissionStakeholderId}"/>
							</td>
							<td align="center">
								<c:if test="${commStakeholderListViewModel.commissionStakeholderId != 50036 and commStakeholderListViewModel.commissionStakeholderId != 50020 and commStakeholderListViewModel.commissionStakeholderId != 50042}">
									<html:checkbox id="checkBox_${commStakeholderListViewModel.commissionStakeholderId}" path="productIdCommissionShSharesDefaultModelList[${commStakeholderListViewModelStatus.index}].isWhtApplicable" onchange="toggleWH();" />
								</c:if>
							</td>
						</tr>
					</c:forEach>
					<!--tr><td><a href="p_productcustomizeshare.html?productId=${productModel.productId}">customize share rates</a></td></tr-->
					</tbody>
				</table>
			</td>
		</tr>
		<tr bgcolor="FBFBFB">
			<td height="19" colspan="4" align="center">

				<c:choose>
					<c:when test="${not empty param.productId or not empty productId and empty param.isReadOnly}">
						<authz:authorize ifAnyGranted="<%=updatePermission%>">
							<input name="_save" type="submit" class="button" value="Update"/>
							<input type="hidden" name="isUpdate" id="isUpdate" value="true"/>
							<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_UPDATE%>" />						</authz:authorize>
						<authz:authorize ifNotGranted="<%=updatePermission%>">
							<input name="_save" type="submit" class="button" value="Update" disabled="disabled"/>

						</authz:authorize>
						<input type="button" name="cancel" value="Cancel" class="button" onclick="javascript:window.location='productsearch.html'" />
					</c:when>
					<c:when test="${param.isReadOnly}">
						<input type="button" name=" Close " value=" Close " class="button" onclick="window.close();" />
					</c:when>
					<c:otherwise>
						<authz:authorize ifAnyGranted="<%=createPermission%>">
							<input name="_save" type="submit" class="button" value="Save"/>
							<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_CREATE%>" />
						</authz:authorize>
						<authz:authorize ifNotGranted="<%=createPermission%>">
							<input name="_save" type="submit" class="button" value="Save" disabled="disabled"/>
						</authz:authorize>
						<input type="button" name="cancel" value="Cancel" class="button" onclick="javascript:window.location='productsearch.html?actionId=2'" />
					</c:otherwise>
				</c:choose>

			</td>
		</tr>

		<!-- <tr bgcolor="FBFBFB">
        <td height="19" colspan="2" align="center">
            <input name="_save" type="submit" class="button" value="Save"/>
            <input name="reset" type="reset" onclick="javascript: window.location='productsearch.html?actionId=2'" class="button" value="Cancel" />
            </td>
        </tr>	 -->
	</table>




</html:form>

<input type="button" class="button" value="Define Product Limit Rules" onclick="javascript:window.location.href='${contextPath}/productlimitsruleform.html?productId=${productModel.productId}&productName=${productModel.name}';"/>
<input type="button" value="Define Product Charges Rules" class="button" onclick="javascript:window.location.href='${contextPath}/p_productchargesruleform.html?productId=${productModel.productId}&productName=${productModel.name}';" />
<input type="button" value="Define Product Shares Rules" class="button" onclick="javascript: window.location='p_productcustomizeshare.html?productId=${productModel.productId}';" />
<script language="javascript" type="text/javascript">
    function validateForm(form){

        if(!isFormDataChanged())
        {
            return false;
        }

        var serviceId = document.getElementById("serviceId").value;
        var supplierId = document.getElementById("supplierId").value;
        var productName= document.getElementById("prodName").value;
        var settlementAcc = document.getElementById("commissionStakeHolderId").value;

        var minLimit = parseInt(document.getElementById("minLimit").value) || 0;
        var maxLimit = parseInt(document.getElementById("maxLimit").value) || 0;

        var fixExclusiveAmount = parseFloat(document.getElementById("exclusiveFixAmount").value) || 0;
        var fixExclusivePercentage = parseFloat(document.getElementById("exclusivePercentAmount").value) || 0;
        var fixInclusiveAmount = parseFloat(document.getElementById("inclusiveFixAmount").value) || 0;
        var fixInclusivePercentage = parseFloat(document.getElementById("inclusivePercentAmount").value) || 0;

        var accountNo = document.getElementById("accountNo").value;
        var accountNick = document.getElementById("accountNick").value;
        var linkChkValue = document.getElementById("linkChk").checked;

        var taxableChkValue = document.getElementById("taxable").checked;
        var whtClause =document.getElementById("whtConfigId").value;



        var isValid = true;


        if(serviceId==""){
            alert("Service is not selected");
            document.getElementById("serviceId").focus();
            isValid = false;
        }
        else if(supplierId==""){
            alert("Supplier is not selected");
            document.getElementById("supplierId").focus();
            isValid = false;
        }
        else if(productName==""){
            alert("Provide Product Name");
            document.getElementById("prodName").focus();
            isValid = false;
        }
        else if(serviceId==15 && settlementAcc==""){
            alert("Corporate A/c is not selected");
            document.getElementById("commissionStakeHolderId").focus();
            isValid = false;
        }
        else if (taxableChkValue && whtClause=="")
        {
            alert("Applicable WHT Clause is required.");
            document.getElementById("whtConfigId").focus();
            isValid = false;
        }

        else if(serviceId==13 && settlementAcc==""){
            alert("Corporate A/c is not selected");
            document.getElementById("commissionStakeHolderId").focus();
            isValid = false;
        }
        else if(minLimit == "" && maxLimit=="" )
        {
            alert("Provide Maximum Limit and Minimum Limit");
            document.getElementById("minLimit").focus();
            //
            isValid = false;
        }
        else if(minLimit != "" && maxLimit=="" )
        {
            alert("Provide Maximum Limit");
            document.getElementById("maxLimit").focus();
            isValid = false;
        }
        // else if(maxLimit != "" && minLimit=="" ){
        //     alert("Provide Minimum Limit");
        //     document.getElementById("minLimit").focus();
        //     isValid = false;
        // }
        /* commented by muhammad atif
            else if(maxLimit==minLimit){
            alert("Minimum Limit and Maximum Limit cannot be equal");
            isValid = false;
        } */
        // else if(minLimit<1){
        //     alert("Minimum Limit should be greater or equal to 1");
        //     isValid = false;
        // }
        else if(maxLimit < minLimit){
            alert("Maximum Limit should be greater than Minimum Limit");
            isValid = false;
        }
        else if ( fixExclusiveAmount != "" && fixExclusiveAmount < 1 ){
            alert("Exclusive Fix Amount should be greater or equal to 1");
            isValid = false;
        }
        else if ( fixInclusiveAmount != "" && fixInclusiveAmount < 1 ){
            alert("Inclusive Fix Amount should be greater or equal to 1");
            isValid = false;
        }
        else if ((fixExclusivePercentage != "" && fixExclusivePercentage < 0.01 ) || fixExclusivePercentage >100 ){
            alert("Invalid Exclusive Percentage Amount.");
            isValid = false;
        }
        else if ((fixInclusivePercentage != "" && fixInclusivePercentage < 0.01 ) || fixInclusivePercentage > 100){
            alert("Invalid Inclusive Percentage Amount.");
            isValid = false;
        }
        /* added by atif hussain */
        else if (linkChkValue && accountNick.length == 0)
        {
            alert("Account Title is required.");
            isValid = false;
        }
        else if (linkChkValue && accountNo.length == 0)
        {
            alert("Oracle # is required.");
            isValid = false;
        }
        else if(linkChkValue && !isInteger(accountNo) )
        {
            alert("Oracle # is invalid.");
            isValid = false;
        }
        /* end of addition by atif */

        /* else if(fixExclusiveAmount == "" && fixInclusiveAmount == "" && fixExclusivePercentage=="" && fixInclusivePercentage =="" && !isNoDefaultChangesAndSharesDefiend()){
            alert("You need to provide either of the following fields\nExclusive Fix Amount\nInclusive Fix Amount\nExclusive Percentage\nInclusive Percentage Amount");
            isValid = false;
        } */
        else if(fixExclusiveAmount == "" && fixInclusiveAmount == "" && fixExclusivePercentage=="" && fixInclusivePercentage =="" && isNoDefaultChangesAndSharesDefiend()){
            return isValid;
        }
        /* else if(document.getElementById("isFed").checked==1 && document.getElementById("fedShare").value==""){
            alert("You have applied FED, please enter the amount for FED");
            document.getElementById("fedShare").focus();
            return false;
        }else if((document.getElementById("isFed").checked==1 && document.getElementById("fedShare").value > 100.0) || (document.getElementById("isFed").checked==1 && document.getElementById("fedShare").value == 0.0)){
            alert("FED value should be between 1 - 100");
            isValid = false;
        } else if(document.getElementById("withHoldingShare").value != "" && document.getElementById("withHoldingShare").value > 100.0){
            alert("W.H. value can not exceed more than 100%");
            isValid = false;
        }else if(document.getElementById("withHoldingShare").disabled==false && document.getElementById("withHoldingShare").value != "" && document.getElementById("withHoldingShare").value == 0.0){
            alert("W.H. value can not exceed more than 1% - 100%");
            isValid = false;
        }*/
        else if (!isValidDefaultCommissionShares()){
            alert("Total commission share should be equal to 100%");
            isValid = false;
        }/*else if(document.getElementById("stakeHolderId_50020").value=="" || document.getElementById("stakeHolderId_50020").value < 1){ //check if bank commission is given; as it is mandatory
	        		alert("Bank Commission is mandatory field, please enter Bank Commission");
	        		isValid = false;
	        	}else if(document.getElementById("stakeHolderId_50028").value=="" ||document.getElementById("stakeHolderId_50028").value < 1 ){//check if agent commission is given; as it is mandatory
	        		alert("Agent Commission is mandatory field, please enter Agent Commission");
	        		isValid = false;
	        	}*/
        /* else if(!isWHCorrect()){
            alert("You're applying W.H. but the share value for W.H is not provided, please enter amount for W.H.");
            document.getElementById("withHoldingShare").focus();
            isValid = false;
        } */else if(!isWhAndShareCorrect()){
            alert("To apply W.H you need to enter its share value as well");
            isValid = false;
        }
        return isValid;
    }

    function isWHCorrect(){
        var inputs = document.getElementsByTagName("input"); //or document.forms[0].elements;
        var cbs = []; //will contain all checkboxes
        var checked = []; //will contain all checked checkboxes
        var result = true;
        for (var i = 0; i < inputs.length; i++) {
            if (inputs[i].type == "checkbox") {
                if ( inputs[i].checked == 1 && inputs[i].id !="isFed" && inputs[i].id != "active1" && inputs[i].id != "linkChk" && inputs[i].id != ""){
                    if( document.getElementById("withHoldingShare").value=="" ){
                        result = false;
                    }
                }

            }
        }

        return result;
    }

    function isNoDefaultChangesAndSharesDefiend( ){
        var result = true;
        var inputs = document.getElementsByTagName("input"); //or document.forms[0].elements;
        var cbs = []; //will contain all checkboxes
        for (var i = 0; i < inputs.length; i++) {
            if (inputs[i].type == "text") {
                if ( inputs[i].id.indexOf("stakeHolderId_") > -1 ){
                    if( inputs[i].value != "" || inputs[i].value > 0){
                        result = false;
                    }
                }

            }
        }
        return result;
    }





    function isValidDefaultCommissionShares(){
        sum = 0.0;
        var inputs = document.getElementsByTagName("input"); //or document.forms[0].elements;
        var cbs = []; //will contain all checkboxes
        for (var i = 0; i < inputs.length; i++) {
            if (inputs[i].type == "text") {
                if ( inputs[i].id.indexOf("stakeHolderId_") > -1 ){
                    if(inputs[i].id != "stakeHolderId_50042" && inputs[i].id != "stakeHolderId_50036" && inputs[i].value != "" ){
                        sum = sum + parseFloat(inputs[i].value);
                    }
                }

            }
        }
        if (sum == 100.0){
            return true;
        }
        else if(sum == 0.0){
            return true;
        }
        else{
            return false;
        }


    }



    function replaceall(str,replace,with_this)
    {
        var str_hasil ="";
        var temp;

        for(var i=0;i<str.length;i++) // not need to be equal. it causes the last change: undefined..
        {
            if (str[i] == replace)
            {
                temp = with_this;
            }
            else
            {
                temp = str[i];
            }

            str_hasil += temp;
        }

        return str_hasil;
    }

    //document.getElementById("checkBox_WH").disabled=true;
    /* if (document.getElementById("isFed").checked==true){
        document.getElementById("fedShare").disabled=false;
    }else{
        document.getElementById("fedShare").disabled=true;
        document.getElementById("fedShare").style.background ="lightgrey";
    } */
    //toggleWH();

    /*    function toggleFED(){
           if (document.getElementById("fedShare").disabled==true){
               document.getElementById("fedShare").disabled=false;
               document.getElementById("fedShare").style.background ="white";
           }else{
               document.getElementById("fedShare").value=0.0;
               document.getElementById("fedShare").disabled=true;
               document.getElementById("fedShare").style.background ="lightgrey";
           }
       } */

    function toggleWH(){
        var inputs = document.getElementsByTagName("input"); //or document.forms[0].elements;
        var cbs = []; //will contain all checkboxes
        var checked = []; //will contain all checked checkboxes
        for (var i = 0; i < inputs.length; i++) {
            if (inputs[i].type == "checkbox") {
                if ( inputs[i].id.indexOf("checkBox_") > -1){
                    cbs.push(inputs[i]);
                }

            }
        }
        selectedBox = 0;
        var totalWHcheckBox = cbs.length;
        for (var j = 0; j < cbs.length; j++){
            if (cbs[j].checked == 1){
                selectedBox++;
            }
        }

        /* if(selectedBox == 0){
            document.getElementById("withHoldingShare").value="";
            document.getElementById("withHoldingShare").disabled=true;
            document.getElementById("withHoldingShare").style.background="lightgrey";
        }else{
            document.getElementById("withHoldingShare").disabled=false;
            document.getElementById("withHoldingShare").style.background="white";
        } */
    }

    window.onload = function() {
        toggleWHTClause();
        toggleAccountLink();
    };


    function toggleWHTClause()
    {
        var checkbox	=	document.getElementById("taxable");
        if (checkbox.checked)
        {
            var elems = document.getElementsByClassName('tax');
            for(var i = 0; i < elems.length; i++) {
                elems[i].style.display = '';
            }


        }
        else
        {
            var elems = document.getElementsByClassName('tax');
            for(var i = 0; i < elems.length; i++) {
                elems[i].style.display = 'none';
            }

            document.getElementById("whtConfigId").value='';
        }
    }

    function toggleAccountLink()
    {
        var checkbox	=	document.getElementById("linkChk");
        if (checkbox.checked)
        {
            document.getElementById("accountNoRow").style.display="table-row";
            document.getElementById("accountNickRow").style.display="table-row";
        }
        else
        {
            document.getElementById("accountNoRow").style.display="none";
            document.getElementById("accountNickRow").style.display="none";
        }
    }

    function isWhAndShareCorrect(){
        var inputs = document.getElementsByTagName("input"); //or document.forms[0].elements;
        var cbs = []; //will contain all checkboxes
        var checked = []; //will contain all checked checkboxes
        var result = true;
        for (var i = 0; i < inputs.length; i++) {
            if (inputs[i].type == "checkbox") {
                if ( inputs[i].checked == 1 && inputs[i].id !="isFed" && inputs[i].id != "active1" && inputs[i].id != "linkChk" && inputs[i].id != ""){
                    var strId = inputs[i].id.replace("checkBox", "stakeHolderId");
                    //alert(strId + ": " + inputs[i].id);
                    if( document.getElementById(strId).value=="" ){
                        result = false;
                        return result;
                    }
                }

            }
        }

        return result;
    }
</script>
</body>
</html>
