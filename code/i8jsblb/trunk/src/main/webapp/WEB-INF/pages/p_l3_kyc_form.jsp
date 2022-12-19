<!--Author: Abu Turab-->
<%@page import="org.springframework.web.bind.ServletRequestUtils"%>
<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="decorator2">
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>

	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/prototype.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.11.0.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-ui-custom.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselect.min.js"></script>
	<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css" />
	<link rel="stylesheet" type="text/css" href="styles/jquery.multiselect.css" />
	<link rel="stylesheet" type="text/css" href="styles/jquery-ui-custom.min.css" />

	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="expires" content="0" />
	<meta http-equiv="pragma" content="no-cache" />

	<meta name="title" content="KYC FORM (For Companies/Partnerships/Proprietorship/NGOs/Trusts/Societies/Clubs/Entities)" />

	<style>
#table_form {
	display: block;
	width: 100%;
	height: auto;
	margin: 0px;
	padding: 0px;
}

#table_form .left {
	display: block;
	width: 49%;
	float: left;
}

#table_form .right {
	display: block;
	width: 49%;
	float: right;
}
h1 {
	display: block;
	padding: 20px 0px;
	margin: 0px !important;
	margin-bottom: 30px !important;
	font-size: 1.9em;
	/* font-size: 26px; */
	background-color: #ecf0f1;
	color: #f47b20;
	text-align: left;
	word-break: initial;
	line-height: initial;
}
</style>

	<%@include file="/common/ajax.jsp"%>
	<!-- turab added below for jquery -->

	<script>
		var jq = $.noConflict();
		jq(document).ready(function($) {
			jq("#modeOfTx").multiselect({
				noneSelectedText : "[Select]",
				minWidth : "210px"
			});
		});
	</script>


<%
	String readPermission = PortalConstants.ADMIN_GP_READ + "," + PortalConstants.PG_GP_READ + ","+PortalConstants.MNG_L3_ACT_READ;
	String updatePermission = PortalConstants.ADMIN_GP_READ + "," + PortalConstants.PG_GP_READ + "," +PortalConstants.MNG_L3_KYC_UPDATE;
	String createPermission = PortalConstants.ADMIN_GP_READ + "," + PortalConstants.PG_GP_READ + "," +PortalConstants.MNG_L3_KYC_CREATE;

%>

</head>
<body bgcolor="#ffffff">

	<div id="successMsg" class="infoMsg" style="display: none;"></div>
	<spring:bind path="level3KycModel.*">
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
	<html:form name="level3KycForm" commandName="level3KycModel" enctype="multipart/form-data" method="POST" action="p_l3_kyc_form.html">
		<div class="infoMsg" id="errorMessages" style="display:none;"></div>

		<c:if test="${not empty level3KycModel.kycId}">
			<input type="hidden" name="isUpdate" id="isUpdate" value="true" />
			<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_UPDATE%>" />
		</c:if>
		<c:if test="${empty level3KycModel.kycId}">
			<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_CREATE%>" />
		</c:if>
		
		<html:hidden path="<%=PortalConstants.KEY_USECASE_ID%>" />
		<html:hidden path="kycId" />
		<html:hidden path="createdBy" />
		<html:hidden path="updatedBy" />
		<html:hidden path="createdOn" />
		<html:hidden path="updatedOn" />
		<html:hidden path="versionNo" />

		<div id="table_form">
			<div class="center">
				<table>
					<tr>
						<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%"><span style="color: #FF0000">*</span>Application Form Number:</td>
						<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input id="initialAppFormNo" path="initialAppFormNo" cssClass="textBox"
								tabindex="1" maxlength="16" readonly="true" style="background: #D3D3D3; font-size:14px; "/></td>
					</tr>
					<tr>
						<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Main Source of Funds to be Credited into Account:</td>
						<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:select path="fundSourceId" cssClass="textBox" id="fundSourceId"
								tabindex="2">
								<html:option value="">[Select]</html:option>
								<c:if test="${fundSourceList != null}">
									<html:options items="${fundSourceList}" itemLabel="name" itemValue="fundSourceId" />
								</c:if>
							</html:select></td>
					</tr>
					<tr>
						<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Expected Number of Transactions per Month
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Debit(#):</td>
						<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input id="noOfTxDebit" path="noOfTxDebit" cssClass="textBox"
								tabindex="3" maxlength="10" onkeypress="return maskInteger(this,event)"/></td>
					</tr>
					<tr>
						<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Credit(#):</td>
						<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input id="noOfTxCredit" path="noOfTxCredit" cssClass="textBox"
								tabindex="4" maxlength="10" onkeypress="return maskInteger(this,event)"/></td>
					</tr>
					<tr>
						<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Expected Value(amount) of Transactions per Month
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Debit(Rs.):</td>
						<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input id="valOfTxDebit" path="valOfTxDebit" cssClass="textBox"
								tabindex="5" maxlength="10" onkeypress="return maskInteger(this,event)"/></td>
					</tr>
					<tr>
						<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Credit(Rs.):</td>
						<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:input id="valOfTxCredit" path="valOfTxCredit" cssClass="textBox"
								tabindex="6" maxlength="10" onkeypress="return maskInteger(this,event)"/></td>
					</tr>
					<tr>
						<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Expected Mode of Transactions:</td>
						<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:select path="expectedModeOfTransaction" cssClass="textBox"
								id="modeOfTx" tabindex="7" multiple="multiple">
								<c:if test="${transactionModeList != null}">
									<html:options items="${transactionModeList}" itemLabel="name" itemValue="transactionModeId" />
								</c:if>
							</html:select></td>
					</tr>

					<tr>
						<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Account Risk Level:</td>
						<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:select path="riskLevel" cssClass="textBox" tabindex="8">
								<html:option value="">[Select]</html:option>
								<html:option value="1">Low</html:option>
								<html:option value="2">Medium</html:option>
								<html:option value="3">High</html:option>
							</html:select></td>
					</tr>
					<tr>
						<td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">Comments by RM/AM:</td>
						<td align="left" bgcolor="FBFBFB" class="formText" width="25%"><html:textarea style="height:50px;" id="comments" path="comments"
								tabindex="9" cssClass="textBox" /></td>
					</tr>
				</table>
			</div>

			<table>
				<tr>
					<td>
						<c:if test="${empty level3KycModel.kycId}">
							<authz:authorize ifAnyGranted="<%=createPermission %>">
								<input type="submit" class="button" value="Create Account" tabindex="10" />
							</authz:authorize>
							<authz:authorize ifNotGranted="<%=createPermission %>">
								<input type="submit" class="button" value="Create Account" disabled="disabled" tabindex="10" />
							</authz:authorize>	
						
						
						</c:if>
						<c:if test="${not empty level3KycModel.kycId}">
							
							<authz:authorize ifAnyGranted="<%=updatePermission%>">
								<input type="submit" class="button" value="Update Account" tabindex="10" />
							</authz:authorize>
							<authz:authorize ifNotGranted="<%=updatePermission%>">
								<authz:authorize ifAnyGranted="<%=readPermission%>">
									<input type="button" class="button" value="Goto L3 Account Form" name="L3Form${level3KycModel.initialAppFormNo}"  
								onclick="javascript:document.location='p_l3accountopeningform.html?actionId=1&initAppFormNumber=${level3KycModel.initialAppFormNo}'" />			
								</authz:authorize>
								<authz:authorize ifNotGranted="<%=readPermission%>">
									<input type="button" class="button" value="Goto L3 Account Form" name="L3Form${level3KycModel.initialAppFormNo}" disabled="disabled"/>			
								</authz:authorize>	
							</authz:authorize>
						</c:if>
					</td>
					<td>
						<input type="reset" class="button" tabindex="11"  value="Cancel"
						onclick="javascript:window.location='p_agentmerchantdetailsform.html?initAppFormNumber=${level3KycModel.initialAppFormNo}'"/>
					</td>
				</tr>
			</table>
	</html:form>
	<script type="text/javascript">
		jq("#comments").keyup(function(){
			    if(jq(this).val().length > 250){
			        jq(this).val(jq(this).val().substr(0, 249));
		}});
		
	</script>
</body>
</html>