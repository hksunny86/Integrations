<!--Title: i8Microbank-->
<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.*,com.inov8.microbank.common.util.PortalConstants'%>
<html>
<head>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE%></c:set>
<c:set var="createAction"><%=PortalConstants.ACTION_CREATE%></c:set>
<meta name="decorator" content="decorator">
<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
<script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
<script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
<script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
<script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css" />
<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
<meta name="title" content="Search Agent/Merchant" />
<script type="text/javascript">
	var jq=$.noConflict();
	var serverDate ="<%=PortalDateUtils.getServerDate()%>";
</script>
<%
	String updatePermission = PortalConstants.ADMIN_GP_UPDATE;
	updatePermission += "," + PortalConstants.PG_GP_UPDATE;
	updatePermission += "," + PortalConstants.MNG_L3_AMDF_UPDATE;
	updatePermission += "," + PortalConstants.MNG_L3_KYC_UPDATE;
	updatePermission += "," + PortalConstants.MNG_L3_KYC_CREATE;
	updatePermission += "," + PortalConstants.MNG_L3_ACT_CREATE;

	
	String readPermission = PortalConstants.ADMIN_GP_UPDATE;
	readPermission +=	"," + PortalConstants.PG_GP_UPDATE;	
	readPermission +=	"," + PortalConstants.MNG_L3_AMDF_READ;
	readPermission +=	"," + PortalConstants.MNG_L3_KYC_READ;
	readPermission +=	"," + PortalConstants.MNG_L3_ACT_READ;
%>
</head>
<body bgcolor="#ffffff">
	<div id="successMsg" class="infoMsg" style="display:none;"></div>
	<c:if test="${not empty messages}">
		<div class="infoMsg" id="successMessages">
			<c:forEach var="msg" items="${messages}">
				<c:out value="${msg}" escapeXml="false" />
				<br />
			</c:forEach>
		</div>
		<c:remove var="messages" scope="session" />
	</c:if>
	<html:form name="agentMerchantDetailModelExtendedForm" commandName="agentMerchantDetailViewModel" method="post" action="p_agentmerchantmanagement.html" onsubmit="return validateForm(this)">
		<table width="750px" border="0">
			<tr>
				<td align="right" class="formText">Initial App. Form No.:</td>
				<td align="left"><html:input path="initialAppFormNo" cssClass="textBox" maxlength="16" tabindex="1" onkeypress="return maskAlphaNumericWithSpdashforAH(this,event)" /></td>
				<td class="formText" align="right" width="20%">Business Name:</td>
				<td align="left" width="30%"><html:input path="businessName" cssClass="textBox" maxlength="50" tabindex="3" onkeypress="return maskAlphaNumericWithSp(this,event)" /></td>
			</tr>
			<tr>
				<td align="right" class="formText">Start Date:</td>
				<td align="left"><html:input path="startDate" id="startDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10" /> <img id="sDate" tabindex="6" name="popcal" align="top"
					style="cursor:pointer" src="images/cal.gif" border="0" /> <img id="sDate" tabindex="7" title="Clear Date" name="popcal" onclick="javascript:$('startDate').value=''" align="middle"
					style="cursor:pointer" src="images/refresh.png" border="0" /></td>
				<td align="right" class="formText">End Date:</td>
				<td align="left"><html:input path="endDate" id="endDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10" /> <img id="eDate" tabindex="8" name="popcal" align="top"
					style="cursor:pointer" src="images/cal.gif" border="0" /> <img id="eDate" tabindex="9" title="Clear Date" name="popcal" onclick="javascript:$('endDate').value=''" align="middle"
					style="cursor:pointer" src="images/refresh.png" border="0" /></td>
			</tr>
			<tr>
				<td class="formText" align="right">&nbsp;</td>
				<td align="left"><input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>"> <input name="_search" type="submit" class="button"
					value="Search" tabindex="10" /> <input name="reset" type="reset" onclick="javascript: window.location='p_agentmerchantmanagement.html?actionId=${retriveAction}'" class="button" value="Cancel"
					tabindex="11" /></td>
				<td colspan="2">&nbsp;</td>
			</tr>
		</table>
	</html:form>
	<ec:table items="agentMerchantModelList" var="agentMerchantModel" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit"
		action="${contextPath}/p_agentmerchantmanagement.html" title="" width="100%" filterable="false" sortable="true" rowsDisplayed="15" showPagination="true">
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
			<ec:exportXls fileName="AgentMerchant.xls" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
			<ec:exportXls fileName="AgentMerchant.xlsx" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
			<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da" headerTitle="Agent/Merchant" fileName="Agent/Merchant.pdf" tooltip="Export PDF" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
			<ec:exportCsv fileName="AgentMerchant.csv" tooltip="Export CSV"></ec:exportCsv>
		</authz:authorize>
		<ec:row>
			<ec:column property="initialAppFormNo" title="Initial App. Form No." />
			<ec:column property="businessName" title="Business Name" />
			<ec:column property="userName" title="User Name" />
			<ec:column property="empId" title="Employee ID" />
			<ec:column property="empName" title="Employee Name" />
			<authz:authorize ifAnyGranted="<%=updatePermission%>">
				<ec:column property="agentMerchantDetailId" title=" " viewsAllowed="html" sortable="false">
					<input type="button" class="button" value="Edit" onclick="javascript: window.location='p_agentmerchantdetailsform.html?initAppFormNumber=${agentMerchantModel.initialAppFormNo}'" />
				</ec:column>
			</authz:authorize>
			<authz:authorize ifNotGranted="<%=updatePermission%>">
				<ec:column property="agentMerchantDetailId" title=" " viewsAllowed="html" sortable="false">
					<authz:authorize ifAnyGranted="<%=readPermission%>">
						<input type="button" class="button" value="View Details" onclick="javascript: window.location='p_agentmerchantdetailsform.html?initAppFormNumber=${agentMerchantModel.initialAppFormNo}'" />
					</authz:authorize>
					<authz:authorize ifNotGranted="<%=readPermission%>">
						<input type="button" class="button" disabled="disabled" value="Edit" onclick="javascript: window.location='p_agentmerchantdetailsform.html?initAppFormNumber=${agentMerchantModel.initialAppFormNo}'" />
					</authz:authorize>
				</ec:column>
			</authz:authorize>
		</ec:row>
	</ec:table>
	<script language="javascript" type="text/javascript">
		Calendar.setup({
			inputField : "startDate", // id of the input field
			button : "sDate", // id of the button
		});
		Calendar.setup({
			inputField : "endDate", // id of the input field
			button : "eDate", // id of the button
			isEndDate : true
		});
	</script>
	<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>
</body>
</html>
