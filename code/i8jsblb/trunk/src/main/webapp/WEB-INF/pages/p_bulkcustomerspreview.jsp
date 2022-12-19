<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

	<head>
		<meta name="decorator" content="decorator">
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<meta name="title" content="Add Customer Bulk" />
		<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
		<script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
		<script type="text/javascript" src="${contextPath}/scripts/prototype.js"></script>
		<script type="text/javascript" src="${contextPath}/scripts/toolbar.js"></script>
	</head>

	<body bgcolor="#ffffff">
		<c:if test="${not empty messages && param.invalidRecordsCount==0}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" />
					<br />
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>
		<c:if test="${not empty bulkCustomerAccountVoList && not param.allRecordsInvalid}">
			<html:form name="bulkCustomerAccountVoForm" commandName="bulkCustomerAccountVo" method="post" action="p_bulkcustomeraccountsupload.html">
				<table width="750px" border="0">
					<tr>
						<td class="formText">
							<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_CREATE%>" />
							<input type="hidden" name="invalidRecordsCount" value="${param.invalidRecordsCount}"/>
							<input id="_save" name="_save" type="submit" value="Save Valid Records" tabindex="1" onclick="javascript:disableSubmit();"
											class="button" />&nbsp;
							<input type="button" class="button" value="Cancel" tabindex="2" onClick="javascript: window.location='p_bulkcustomeraccountsupload.html'" />
						</td>
					</tr>
				</table>
			</html:form>
		</c:if>
		<c:if test="${not empty bulkCustomerAccountVoList && param.invalidRecordsCount > 0}">
			<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_CREATE%>" />
			<input type="hidden" name="invalidRecordsCount" value="${param.invalidRecordsCount}"/>
			<table width="750px" border="0">
				<tr>
					<td class="formText" style="color:red;">File contains one or more invalid records. Kindly fix and upload again.</td>
				</tr>
				<tr>
					<td class="formText">
						<a href="${contextPath}/p_downloadInvalidCustomerBulkRecordsFile.html" tabindex="1">Export Invalid Records</a>
						<a href="${contextPath}/p_bulkcustomeraccountsupload.html">Cancel</a>
					</td>
				</tr>
			</table>
		</c:if>
		
		<ec:table items="bulkCustomerAccountVoList" action="${contextPath}/p_bulkcustomerspreview.html"
			title="" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" filterable="false" sortable="false">
			<ec:row interceptor="bulkCustomersErrorRowInterceptor">
				<ec:column property="name"/>
				<ec:column property="mobileNo" style="text-align:center;"/>
				<ec:column property="cnic" title="CNIC" style="text-align:center;"/>
				<ec:column property="cnicExpiryDate" title="CNIC Expiry Date" cell="date" format="dd/MM/yyyy" style="text-align:center;"/>
				<ec:column property="initialAppFormNo" title="Initial Application Form No" style="text-align:center;"/>
			</ec:row>
		</ec:table>
		&nbsp;<input type="button" class="button" value="Back" tabindex="48" onclick="javascript: window.location.href='p_bulkcustomeraccountsupload.html?actionId=1'" />
		<script language="javascript" type="text/javascript">
			function disableSubmit()
		    {
		    	document.forms.bulkCustomerAccountVoForm._save.disabled=true;
			    onSave(document.forms.bulkCustomerAccountVoForm,null);
		    }
		</script>
	</body>
</html>