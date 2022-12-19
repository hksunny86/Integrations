<!--Author: Naseer Ullah-->
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<%@page import="com.inov8.microbank.common.util.PortalConstants"%>
<%@include file="/common/taglibs.jsp"%>

<html>
	<head>
<meta name="decorator" content="decorator">
        <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
		<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
		<script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
		<meta name="title" content="Account Type Limits"/>
		<script type="text/javascript">
			var jq=$.noConflict();
			var serverDate ="<%=PortalDateUtils.getServerDate()%>";
		</script>
	</head>
	<body bgcolor="#ffffff">
		<html:form action="p_customertransactionlimits.html" commandName="olaCustomerTxLimitViewModel"
		onsubmit="return validateForm(this);">
			<table width="750px">
				<tr>
					<td align="right" class="formText">Account Type:</td>
					<td colspan="3">
						<html:select id="olaCustomerAccountTypeId" path="olaCustomerAccountTypeId" cssClass="textBox" tabindex="1">
							<html:option value="">---All---</html:option>
							<html:options items="${olaCustomerAccountTypeModelList}" itemLabel="name" itemValue="customerAccountTypeId"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<td align="right" class="formText">Transaction Type:</td>
					<td>
						<html:select id="olaTransactionTypeId" path="olaTransactionTypeId" cssClass="textBox" tabindex="2">
							<html:option value="">---All---</html:option>
							<html:options items="${olaTransactionTypeModelList}" itemLabel="name" itemValue="transactionTypeId"/>
						</html:select>
					</td>
					<td align="right" class="formText">Limit Type:</td>
					<td>
						<html:select id="limitTypeId" path="limitTypeId" cssClass="textBox" tabindex="3">
							<html:option value="">---All---</html:option>
							<html:options items="${limitTypeModelList}" itemLabel="name" itemValue="limitTypeId"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td align="left" class="formText">
						<input type="submit" class="button" value="Search" name="_search" tabindex="4"/>
						<input type="reset" class="button" value="Cancel" name="_reset" onclick="javascript: window.location='p_customertransactionlimits.html'" tabindex="5"/>
					</td>
					<td colspan="2">&nbsp;</td>
				</tr>
			</table>
		</html:form>
		<ec:table filterable="false" items="olaCustomerTxLimitViewModelList" var="olaCustomerTxLimitViewModel" action="${contextPath}/p_customertransactionlimits.html"
			retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" width="70%">
			<ec:row>
				<ec:column property="customerAccountType" title="Account Type"/>
				<ec:column property="olaTransactionType" title="Transaction Type"/>
				<ec:column property="limitType"/>
				<ec:column property="limitMinimum" title="Minimum Limit" style="text-align: right"/>
				<ec:column property="limitMaximum" title="Maximum Limit" style="text-align: right"/>
			</ec:row>
		</ec:table>
		<script type="text/javascript">
			document.forms[0].olaCustomerAccountTypeId.focus();
		</script>
		<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>
	</body>
</html>
