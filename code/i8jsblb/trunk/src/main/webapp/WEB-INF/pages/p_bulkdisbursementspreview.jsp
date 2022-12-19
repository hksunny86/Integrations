<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

	<head>
		<meta name="decorator" content="decorator">
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<meta name="title" content="Bulk Disbursements/Payments" />
		<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
		<script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
		<script type="text/javascript" src="${contextPath}/scripts/prototype.js"></script>
		<script type="text/javascript" src="${contextPath}/scripts/toolbar.js"></script>
		<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css" />
	</head>

	<body bgcolor="#ffffff">
		<c:set var="bulkPaymentPrododuct" value="<%=ProductConstantsInterface.BULK_PAYMENT%>"/>
		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" />
					<br />
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>

		<%-- <c:if test="${not empty invalidBulkDisbursementsModelList}">
			<script type="text/JavaScript">
			window.open("/i8Microbank/downloadFile.html"); 
			</script>
		</c:if> --%>
		<c:if test="${not empty bulkDisbursementsModelList}">
			<html:form name="bulkDisbursmentsForm" commandName="bulkDisbursementsVOModel" method="post" action="p_bulkdisbursements.html">
				<input type="hidden" name="productId" value="${param.productId}"/>
				<input type="hidden" name="allRecordsValid" value="${param.allRecordsValid}"/>
				<table width="750px" border="0">
					<c:choose>
						<c:when test="${param.allRecordsValid}">
							<tr>
								<td class="formText">
									<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_CREATE%>" />
									<input id="_save" type="button" name="_save" value="  Save  " tabindex="1" onclick="javascript:disableSubmit();"
													class="button" />&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="button" class="button" value="Cancel" tabindex="2" onClick="javascript: window.location='p_bulkdisbursements.html'" />
								</td>
							</tr>
						</c:when>
						<c:otherwise>
							<tr>
								<td class="formText" style="color:red;">
									File contains one or more invalid records. Kindly fix and upload again. 
								</td>
							</tr>
							<tr>
								<td class="formText">
									<a href="${contextPath}/p_downloadinvalidrecordsfile.html?productId=${param.productId}">Export Invalid Records</a>
								</td>
							</tr>
						</c:otherwise>
					</c:choose>
				</table>
			</html:form>
		</c:if>
		<ec:table items="bulkDisbursementsModelList" var="bulkDisbursementsModel" action="${contextPath}/p_bulkdisbursementspreview.html?productId=${param.productId}&allRecordsValid=${param.allRecordsValid}"
			title="" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" filterable="false" sortable="false">
			<ec:row interceptor="bulkDisbursementErrorRowInterceptor">
				<ec:column property="employeeNo"/>
				<ec:column property="name"/>
				<c:if test="${bulkPaymentPrododuct eq param.productId}">
					<ec:column property="cnic" title="CNIC"/>
				</c:if>
				<ec:column property="mobileNo"/>
				<ec:column property="type" style="text-align:center;"/>
				<ec:column property="chequeNo"/>
				<ec:column property="amount" style="text-align:right;"/>
				<ec:column property="paymentDate" cell="date" format="dd/MM/yyyy" style="text-align:center;"/>
				<ec:column property="description"/>
			</ec:row>
		</ec:table>
		<input type="button" class="button" value="Back" tabindex="48" onclick="javascript: window.location.href='p_bulkdisbursements.html?actionId=1'" />
		<script language="javascript" type="text/javascript">
			function disableSubmit()
		    {
		    	document.forms.bulkDisbursmentsForm._save.disabled=true;
		    	
			    onSave(document.forms.bulkDisbursmentsForm,null);
		    }
		</script>
	</body>
</html>