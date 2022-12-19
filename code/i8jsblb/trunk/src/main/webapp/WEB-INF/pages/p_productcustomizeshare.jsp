<!--Author: Asad hayat http://www.leviton.com/sections/prodinfo/newprod/npleadin.htm-->

<%@ include file="/common/taglibs.jsp"%>
<%@ page import="com.inov8.microbank.common.util.*, java.util.*, com.inov8.microbank.common.model.CommissionShSharesRuleModel "%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<meta name="decorator" content="decorator2">
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
	<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>

    </script>
    <link rel="stylesheet" href="${contextPath}/styles/jquery-ui-custom.min.css" type="text/css">
        <script type="text/javascript" src="${contextPath}/scripts/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="${contextPath}/scripts/jquery-ui-custom.min.js"></script>

	<meta name="title" content="Product Shares Rules" />
	<script type="text/javascript">
        var jq = $.noConflict();
        jq(document).ready(
            function($)
            {
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

                                    window.location.href='${contextPath}/p_productcustomizeshare.html?productId=2510785&actionName=deleteRules';

                                },
                                Cancel: function() {
                                    $( this ).dialog( "close" );
                                }
                            }
                        });
                    }
                );//end of btnRemoveAll click event
                jq('input[type="text"]').on('change', function() {

                    // Change event fired..
                    jq('input[type="submit"]').prop('disabled', false);
                });
                jq('input[type="checkbox"]').on('change', function() {

                    // Change event fired..
                    jq('input[type="submit"]').prop('disabled', false);
                });

                jq('select').on('change', function() {

                    // Change event fired..
                    jq('input[type="submit"]').prop('disabled', false);
                });
            }
        );
	</script>
</head>
<body>

<spring:bind path="productShSharesRuleVo.*">
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
<c:set var="retriveAction"><%=PortalConstants.ACTION_UPDATE%></c:set>
<html:form commandName="productShSharesRuleVo"
		   id="productShSharesRuleForm" method="post"
		   action="p_productcustomizeshare.html" onsubmit="return validateForm(this);">
	<input type="hidden" name="isUpdate" id="isUpdate" value="true" />
	<input type="hidden" name="selectedDeviceTypeId" id="selectedDeviceTypeId" />
	<input type="hidden" name="selectedSegmentId" id="selectedSegmentId" />
	<input type="hidden" name="selectedDistributorId" id="selectedDistributorId" />
	<input type="hidden" name="selectedSharesRuleIds" id="selectedSharesRuleIds" />
	<input type="hidden" name="productId" id="productId" value="${productShSharesRuleVo.productId}" />
	<table width="100%" border="0" cellpadding="0" cellspacing="1">
		<tr bgcolor="FBFBFB">
			<td colspan="2" align="center">&nbsp;</td>
		</tr>

		<tr>
			<td>
				<table id="commissionShSharesItems" name="commissionShSharesItems"
					   align="center">
					<tr>
						<td align="right" bgcolor="F3F3F3" class="formText"><span
								style="color:#FF0000">*</span>Product Name:&nbsp;</td>
						<td align="left" bgcolor="FBFBFB"><html:input path="productName"
																	  cssClass="textBox" size="40" maxlength="50" readonly="true" />
						</td>
					</tr>

					<tr>
						<td width="49%" height="16" align="right" bgcolor="F3F3F3"
							class="formText">Channel:&nbsp;</td>
						<td width="51%" align="left" bgcolor="FBFBFB" class="formText">
							<html:select cssClass="textBox" path="deviceTypeId">
								<html:option value="">-- All --</html:option>
								<html:options items="${channelModelList}"
											  itemValue="deviceTypeId" itemLabel="name" />
							</html:select>
						</td>

					</tr>

					<tr>
						<td width="49%" height="16" align="right" bgcolor="F3F3F3"
							class="formText">Segment:&nbsp;</td>
						<td width="51%" align="left" bgcolor="FBFBFB" class="formText">
							<html:select cssClass="textBox" path="segmentId">
								<html:option value="">-- All --</html:option>
								<html:options items="${segmentModelList}" itemValue="segmentId"
											  itemLabel="name" />
							</html:select>
						</td>

					</tr>

					<tr>
						<td width="49%" height="16" align="right" bgcolor="F3F3F3"
							class="formText">Agent Network:&nbsp;</td>
						<td width="51%" align="left" bgcolor="FBFBFB" class="formText">
							<html:select cssClass="textBox" path="distributorId">
								<html:option value="">-- All --</html:option>
								<html:options items="${distributorModelList}" itemValue="distributorId"
											  itemLabel="name" />
							</html:select>
						</td>

					</tr>


						<%-- <tr>
							<td width="49%" height="16" align="right" bgcolor="F3F3F3"
								class="formText"><html:checkbox id="isFed" path="fedApply"
									label="FED apply?" onchange="toggleFED();" /></td>
							<td width="51%" align="left" bgcolor="FBFBFB" class="formText">

							</td>
						</tr>
						<tr>
							<td width="49%" height="16" align="right" bgcolor="F3F3F3"
								class="formText">FED Rate :&nbsp;</td>
							<td width="51%" align="left" bgcolor="FBFBFB" class="formText">
								<html:input id="FedShare" path="FedShare" cssClass="textBox"
									onkeypress="return checkNumeric(this,event);" maxlength="5" />
							</td>

						</tr>
						<tr>
							<td width="49%" height="16" align="right" bgcolor="F3F3F3"
								class="formText">W.H Rate (%):&nbsp;</td>
							<td width="51%" align="left" bgcolor="FBFBFB" class="formText">
								<html:input id="WhShare" path="WhShare" cssClass="textBox"
									onkeypress="return checkNumeric(this,event);" maxlength="5" />
							</td>
						</tr> --%>
					<tr>
						<!-- <td width="49%" height="16" align="right" bgcolor="F3F3F3"
                            class="formText">&nbsp;</td> -->
						<td align="center" bgcolor="FBFBFB" class="eXtremeTable" colspan="2">

							<table cellpadding="0" cellspacing="0" class="tableRegion"
								   id="sharesDefault">
								<thead>
								<tr>
									<td class="tableHeader" width="40%">Stakeholder</td>
									<td class="tableHeader" width="50%">Share (%)</td>
									<td class="tableHeader" width="20%">W.H</td>
								</tr>
								</thead>
								<input type="hidden" name="totalRowsOfShares"
									   id="totalRowsOfShares"
									   value="${fn:length(commissionShSharesRuleModel)}" />
								<tbody>
								<td class="formText" width="40%">&nbsp;</td>
								<td class="formText" width="50%">&nbsp;</td>
								<td class="formText" width="20%">&nbsp;</td>
								<c:forEach items="${commStakeholderModelList}"
										   var="commStakeholderListViewModel"
										   varStatus="commStakeholderListViewModelStatus">
									<c:set var="rowCssClass" value="odd" scope="page" />
									<c:if test="${commStakeholderListViewModelStatus.count%2!=0}">
										<c:set var="rowCssClass" value="even" scope="page" />
									</c:if>

									<tr id="row_${commStakeholderListViewModelStatus.index}"
										class="${rowCssClass}">
										<td class="formText">&nbsp;&nbsp;<c:out
												value="${commStakeholderListViewModel.name}" />
										</td>
										<td width="15%">
											<html:input
													id="stakeHolderId_${commStakeholderListViewModel.commissionStakeholderId}"
													cssClass="textBox"
													path="commissionShSharesRuleModel[${commStakeholderListViewModelStatus.index}].commissionShare"
													onkeypress="return checkNumeric(this,event);" maxlength="5" />
											<html:hidden
													path="commissionShSharesRuleModel[${commStakeholderListViewModelStatus.index}].commissionShSharesRuleId" />
											<html:hidden
													path="commissionShSharesRuleModel[${commStakeholderListViewModelStatus.index}].productId"
													value="${productShSharesVo.productId}" />
											<html:hidden
													path="commissionShSharesRuleModel[${commStakeholderListViewModelStatus.index}].commissionStakeholderId"
													value="${commStakeholderListViewModel.commissionStakeholderId}" />
										</td>
										<td align="center"><c:if
												test="${commStakeholderListViewModel.commissionStakeholderId != 50036 and commStakeholderListViewModel.commissionStakeholderId != 50020 and commStakeholderListViewModel.commissionStakeholderId != 50042}">
											<html:checkbox
													id="checkBox_${commStakeholderListViewModel.commissionStakeholderId}"
													path="commissionShSharesRuleModel[${commStakeholderListViewModelStatus.index}].isWhtApplicable"
													onchange="toggleWH();" />
										</c:if></td>
										<html:hidden path="commissionShSharesRuleModel[${commStakeholderListViewModelStatus.index}].versionNo" />
									</tr>

								</c:forEach>
								</tbody>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr bgcolor="FBFBFB">
			<td height="19" colspan="2" align="center">
				<authz:authorize ifAnyGranted="<%= PortalConstants.MNG_PRDCT_LMTS_UPDATE %>">
					<input name="_save" type="submit" class="button" value="Save" disabled="disabled" tabindex="4" />
				</authz:authorize>
				<authz:authorize ifAnyGranted="<%= PortalConstants.MNG_PRDCT_SHARES_UPDATE %>">
					<input name="_save" type="submit" class="button" value="Save" disabled="disabled" tabindex="4" />
				</authz:authorize>
				<authz:authorize ifAnyGranted="<%= PortalConstants.MNG_PRDCT_LMTS_UPDATE %>">
					<input	name="reset" type="reset"
							  onclick="javascript: window.location='productupdateform.html?productId=${productShSharesRuleVo.productId}'"
							  class="button" value="Cancel" />
				</authz:authorize>
				<authz:authorize ifAnyGranted="<%= PortalConstants.MNG_PRDCT_SHARES_READ %>">
					<input	name="reset" type="reset"
							  onclick="javascript: window.location='productsearch.html'"
							  class="button" value="Cancel" />
				</authz:authorize>
				<authz:authorize ifAnyGranted="<%= PortalConstants.MNG_PRDCT_SHARES_DELETE %>">
					<c:if test="${productShSharesRuleVo.isEdit}">
						<input type="button" value="Remove Rule" onclick="javascript:if(confirm('Are you sure you want to delete the product share rule?')) window.location.href='${contextPath}/p_productcustomizeshare.html?segmentId=${productShSharesRuleVo.segmentId}&deviceTypeId=${productShSharesRuleVo.deviceTypeId}&productId=${productShSharesRuleVo.productId}&distributorId=${productShSharesRuleVo.distributorId}&actionName=deleteRules';" class="button" id="btnRemoveAlll" />
					</c:if>
				</authz:authorize>
				<authz:authorize ifAnyGranted="<%= PortalConstants.MNG_PRDCT_LMTS_UPDATE %>">
					<c:if test="${productShSharesRuleVo.isEdit}">
						<input type="button" value="Remove Rule" onclick="javascript:if(confirm('Are you sure you want to delete the product share rule?')) window.location.href='${contextPath}/p_productcustomizeshare.html?segmentId=${productShSharesRuleVo.segmentId}&deviceTypeId=${productShSharesRuleVo.deviceTypeId}&productId=${productShSharesRuleVo.productId}&distributorId=${productShSharesRuleVo.distributorId}&actionName=deleteRules';" class="button" id="btnRemoveAlll" />
					</c:if>
				</authz:authorize>
			</td>
		</tr>
	</table>
</html:form>

<div class="eXtremeTable">
	<table class="tableRegion" width="100%">
		<thead>
		<tr>
			<td class="tableHeader">Sr.#</td>
			<td class="tableHeader">Channel</td>
			<td class="tableHeader">Segment</td>
			<td class="tableHeader">Distributor</td>
			<td class="tableHeader">Stakeholder</td>
			<td class="tableHeader">Share(%)</td>
			<td class="tableHeader">W.H</td>
			<!-- <td class="tableHeader">FED</td> -->
			<td class="tableHeader">Action</td>
		</tr>
		</thead>
		<c:forEach items="${commissionShSharesRuleModelList}" var="commissionShSharesRuleModel" varStatus="status">
			<tr>
				<td  align="center">${status.count}</td>
				<td align="center">
					<c:choose>
						<c:when test="${commissionShSharesRuleModel.deviceTypeIdDeviceTypeModel.name != null}">
							${commissionShSharesRuleModel.deviceTypeIdDeviceTypeModel.name}
						</c:when>
						<c:otherwise>All</c:otherwise>
					</c:choose>
				</td>
				<td align="center">
					<c:choose>
						<c:when test="${commissionShSharesRuleModel.segmentIdSegmentModel.name != null}">
							${commissionShSharesRuleModel.segmentIdSegmentModel.name}
						</c:when>
						<c:otherwise>All</c:otherwise>
					</c:choose>
				</td>
				<td align="center">
					<c:choose>
						<c:when test="${commissionShSharesRuleModel.distributorModel.name != null}">
							${commissionShSharesRuleModel.distributorModel.name}
						</c:when>
						<c:otherwise>All</c:otherwise>
					</c:choose>
				</td>
				<td align="center">${commissionShSharesRuleModel.commissionStakeholderIdCommissionStakeholderModel.name}</td>
				<td align="center">${commissionShSharesRuleModel.commissionShare}</td>
				<c:if test="${commissionShSharesRuleModel.isWhtApplicable==true}">
					<td align="center"><input type="checkbox" style="textBox" checked="checked" disabled="disabled" /></td>
				</c:if>
				<c:if test="${commissionShSharesRuleModel.isWhtApplicable==null or commissionShSharesRuleModel.isWhtApplicable==false}">
					<td align="center"><input type="checkbox" style="textBox" disabled="disabled" /></td>
				</c:if>
					<%-- <c:if test="${commissionShSharesRuleModel.isFedApplicable==true}">
                        <td align="center"><input type="checkbox" style="textBox" checked="checked" disabled="disabled" /></td>
                    </c:if>
                    <c:if test="${commissionShSharesRuleModel.isFedApplicable==null or commissionShSharesRuleModel.isFedApplicable==false}">
                        <td align="center"><input type="checkbox" style="textBox" disabled="disabled" /></td>
                    </c:if> --%>
				<td align="center">
						<%--<c:if test="${commissionShSharesRuleModelList[status.index].deviceTypeId != commissionShSharesRuleModelList[status.count].deviceTypeId
                        or commissionShSharesRuleModelList[status.index].segmentId != commissionShSharesRuleModelList[status.count].segmentId
                        or commissionShSharesRuleModelList[status.index].distributorId != commissionShSharesRuleModelList[status.count].distributorId}">--%>
							<authz:authorize ifAnyGranted="<%= PortalConstants.MNG_PRDCT_SHARES_UPDATE %>">
								<input type="button" class="button" style="width='90px'" value="Edit" onclick="javascript:window.location.href='${contextPath}/p_productcustomizeshare.html?segmentId=${commissionShSharesRuleModel.segmentId}&deviceTypeId=${commissionShSharesRuleModel.deviceTypeId}&productId=${commissionShSharesRuleModel.productId }&distributorId=${commissionShSharesRuleModel.distributorId}&actionName=EditRules';" />
							</authz:authorize>
							<authz:authorize ifAnyGranted="<%= PortalConstants.MNG_PRDCT_LMTS_UPDATE %>">
								<input type="button" class="button" style="width='90px'" value="Edit" onclick="javascript:window.location.href='${contextPath}/p_productcustomizeshare.html?segmentId=${commissionShSharesRuleModel.segmentId}&deviceTypeId=${commissionShSharesRuleModel.deviceTypeId}&productId=${commissionShSharesRuleModel.productId }&distributorId=${commissionShSharesRuleModel.distributorId}&actionName=EditRules';" />
							</authz:authorize>

						<%--</c:if>--%>
				</td>
			</tr>
		</c:forEach>
	</table>
</div>
<script language="javascript" type="text/javascript">
    function validateForm(form){
        var isValid = true;
        if(document.getElementById("segmentId").value == "" && document.getElementById("deviceTypeId").value == "" && document.getElementById("distributorId").value == "" ){
            alert("Please select atleast one of the Channel, Segment or Agent Network");
            isValid = false;
            return isValid;
        }
        /*else if (document.getElementById("WhShare").value != "" && document.getElementById("WhShare").value > 100.0){
            alert("W.H value can not exceed 100%");
        }else if (document.getElementById("WhShare").value != "" && document.getElementById("WhShare").value == 0.0){
            alert("W.H. value can be 1%	- 100%");
        }*/
        else if (!isValidDefaultCommissionShares()){
            alert("Total commission share should be equal to 100%");
            isValid = false;
        }else if(!isWHCorrect()){
            alert("You're applying W.H. but the share value for W.H is not provided, please enter amount for W.H.");
            document.getElementById("WhShare").focus();
            isValid = false;
        }else if(!isWhAndShareCorrect()){
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
                if ( inputs[i].checked == 1 && inputs[i].id !="isFed" && inputs[i].id !=""){
                    if( document.getElementById("WhShare").value=="" ){
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
        }else{
            return false;
        }


    }




    /* if (document.getElementById("isFed").checked==true){
        document.getElementById("FedShare").disabled=false;
    }else{
        document.getElementById("FedShare").disabled=true;
        document.getElementById("FedShare").style.background ="lightgrey";
    }
    toggleWH(); */



    /* function toggleFED(){
          if (document.getElementById("FedShare").disabled==true){
              document.getElementById("FedShare").disabled=false;
              document.getElementById("FedShare").style.background ="white";
          }else{
              document.getElementById("FedShare").disabled=true;
              document.getElementById("FedShare").style.background ="lightgrey";
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
        if(selectedBox == 0){
            document.getElementById("WhShare").disabled=true;
            document.getElementById("WhShare").style.background="lightgrey";
        }else{
            document.getElementById("WhShare").disabled=false;
            document.getElementById("WhShare").style.background="white";
        }
    }

    function isWhAndShareCorrect(){
        var inputs = document.getElementsByTagName("input"); //or document.forms[0].elements;
        var cbs = []; //will contain all checkboxes
        var checked = []; //will contain all checked checkboxes
        var result = true;
        for (var i = 0; i < inputs.length; i++) {
            if (inputs[i].type == "checkbox") {
                if ( inputs[i].checked == 1 && inputs[i].id !="isFed" && inputs[i].id != "active1" && inputs[i].id != "" && inputs[i].id != null){
                    var strId = inputs[i].id.replace("checkBox", "stakeHolderId");
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
<div id="dialog-confirm" title="Confirmation" style="display: none;">
	<p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>Are you sure you want to Remove all rules?</p>
</div>
</body>
</html>
