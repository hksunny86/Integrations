<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.*'%>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE%></c:set>
<html>
<head>
<meta name="decorator" content="decorator">
<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
<script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
<script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
<script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
<script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css" />
<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
<%@include file="/common/ajax.jsp"%>
<meta name="title" content="FED Transaction wise Report" id="<%=ReportIDTitleEnum.FEDTransactionwiseReport.getId()%>" />
<script language="javascript" type="text/javascript">
      		var jq=$.noConflict();
			var serverDate ="<%=PortalDateUtils.getServerDate()%>";
			var username = "<%=UserUtils.getCurrentUser().getUsername()%>";
        	var appUserId= "<%=UserUtils.getCurrentUser().getAppUserId()%>";
       	 	var email= "<%=UserUtils.getCurrentUser().getEmail()%>";
	function error(request) {
		alert("An unknown error has occured. Please contact with the administrator for more details");
	}
</script>
	<%@include file="/WEB-INF/pages/export_zip.jsp"%>
	<%-- <script type="text/javascript" src="${contextPath}/scripts/exportzip.js"></script> --%>
</head>
<body bgcolor="#ffffff">
	<c:if test="${not empty messages}">
		<div class="infoMsg" id="successMessages">
			<c:forEach var="msg" items="${messages}">
				<c:out value="${msg}" escapeXml="false" />
				<br />
			</c:forEach>
		</div>
		<c:remove var="messages" scope="session" />
	</c:if>
	<html:form name='whtReportTransactionwiseRptForm' commandName="fedBreakupDetailListViewModel" method="post" action="p_fedbreakupdetailreport.html" onsubmit="return validateForm(this)">
		<table width="750px" border="0">
			<tr>
				<td align="right" class="formText">Start Date:</td>
				<td align="left"><html:input path="startDate" id="startDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10" /> <img id="sDate" tabindex="2" name="popcal" align="top"
					style="cursor:pointer" src="images/cal.gif" border="0" /> <img id="sDate" tabindex="3" title="Clear Date" name="popcal" onclick="javascript:$('startDate').value=''" align="middle"
					style="cursor:pointer" src="images/refresh.png" border="0" /></td>
				<td align="right" class="formText">End Date:</td>
				<td align="left"><html:input path="endDate" id="endDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10" /> <img id="eDate" tabindex="7" name="popcal" align="top"
					style="cursor:pointer" src="images/cal.gif" border="0" /> <img id="eDate" tabindex="8" title="Clear Date" name="popcal" onclick="javascript:$('endDate').value=''" align="middle"
					style="cursor:pointer" src="images/refresh.png" border="0" /></td>
			</tr>
<%-- 			<tr>
				<td align="right" class="formText" width="18%">Product:</td>
				<td align="left">
					<html:select path="productId" id="productId" cssClass="textBox" tabindex="3">
						<html:option value="">[Select]</html:option>
						<html:options items="${productModelList}" itemLabel="name" itemValue="productId"/>
					</html:select>
				</td>
			</tr>
 --%>			<tr>
				<td class="formText" align="right">&nbsp;</td>
				<td align="left"><input name="_search" type="submit" class="button" value="Search" tabindex="4" /> <input name="reset" type="reset"
					onclick="javascript: window.location='p_fedbreakupdetailreport.html?actionId=${retriveAction}'" class="button" value="Cancel" tabindex="5" /></td>
			</tr>
			<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
		</table>
	</html:form>
	<ec:table items="resultList" var="model" action="${contextPath}/p_fedbreakupdetailreport.html?actionId=${retriveAction}" title=""
		retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" filterable="false">
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
			<ec:exportXls fileName="FED Transaction wise Report.xls" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
			<ec:exportXlsx fileName="FED Transaction wise Report.xlsx" tooltip="Export Excel" />
		</authz:authorize>
		<%-- <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
			<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da" headerTitle="Transaction WHT Report" fileName="FED_Breakup_Detail.pdf" tooltip="Export PDF" />
		</authz:authorize>  By Hassan : Fomatting issue due to excessive colunms--%>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
			<ec:exportCsv fileName="FED Transaction wise Report.csv" tooltip="Export CSV"></ec:exportCsv>
		</authz:authorize>
		<ec:row>
			<ec:column property="transactionCode" filterable="false" title="Transaction ID"/>
			<ec:column property="senderAgentId" filterable="false" title="Sender Agent ID"/>
			<ec:column property="senderId" filterable="false" title="Sender ID"/>
			<ec:column property="agent2Id" title="Agent2 ID" />
			<ec:column property="senderMobileNo" filterable="false" title="Sender Mobile No." />
			<ec:column property="senderCnic" filterable="false" title="Sender CNIC"/>
			<ec:column property="senderChannel" filterable="false" title="Sender Channel" escapeAutoFormat="true"/>
			<ec:column property="accountNo" filterable="false" title="Account No."/>
			<ec:column property="paymentMode" filterable="false" title="Payment Mode" escapeAutoFormat="true"/>
			<ec:column property="transactionDate" filterable="false" cell="date" title="Transaction Date" format="dd/MM/yyyy"/>
			<ec:column property="product" filterable="false" title="Product" escapeAutoFormat="true"/>
			<ec:column property="recipientId" filterable="false" title="Recipient ID"/>
			<ec:column property="recipientAccountNo" filterable="false" title="Recipient Account No."/>
			<ec:column property="recipientMobileNo" filterable="false" title="Recipient Mobile No."/>
			<ec:column property="recipientCnic" filterable="false" title="Recipient CNIC" />
			<ec:column property="amount" filterable="false" title="Amount" escapeAutoFormat="true" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="inclusiveCharges" filterable="false" title="Inclusive Charges" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="exclusiveCharges" filterable="false" title="Exclusive Charges" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="fedComm" filterable="false" title=" Federal Excise Duty" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="netFee" filterable="false" title="Net Fee/Charges" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="bankComm" filterable="false" title="Bank Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="agent1Comm" filterable="false" title="Agent Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
            <ec:column property="agent2Comm" filterable="false" title="Agent2 Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="agentRetUserId" title="Agent Retention User ID" />
			<ec:column property="agentRetGrossComm" filterable="false" title="Agent Retention Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="salesTeamComm" filterable="false" title=" Sales Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			
			<ec:column property="hierarchyA1UserId" title="Hierarchy1 Level1 User ID" />
			<ec:column property="hierarchyA1GrossCom" title="Hierarchy1 Level1 Gross Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="hierarchyB1UserId" title="Hierarchy1 Level2 User ID" />
			<ec:column property="hierarchyB1GrossCom" title="Hierarchy1 Level2 Gross Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="hierarchyC1UserId" title="Hierarchy1 Level3 User ID" />
			<ec:column property="hierarchyC1GrossCom" title="Hierarchy1 Level3 Gross Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="hierarchyD1UserId" title="Hierarchy1 Level4 User ID" />
			<ec:column property="hierarchyD1GrossCom" title="Hierarchy1 Level4 Gross Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			
			<ec:column property="hierarchyA2UserId" title="Hierarchy2 Level1 User ID" />
			<ec:column property="hierarchyA2GrossCom" title="Hierarchy2 Level1 Gross Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="hierarchyB2UserId" title="Hierarchy2 Level2 User ID" />
			<ec:column property="hierarchyB2GrossCom" title="Hierarchy2 Level2 Gross Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="hierarchyC2UserId" title="Hierarchy2 Level3 User ID" />
			<ec:column property="hierarchyC2GrossCom" title="Hierarchy2 Level3 Gross Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="hierarchyD2UserId" title="Hierarchy2 Level4 User ID" />
			<ec:column property="hierarchyD2GrossCom" title="Hierarchy2 Level4 Gross Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			
			<ec:column property="othersComm" filterable="false" title="Other Stakeholders Commission" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="transactionStatus" filterable="false" title="Status"/>
		</ec:row>
	</ec:table>
	<script language="javascript" type="text/javascript">
		Calendar.setup({
			inputField : "startDate", // id of the input field
			button : "sDate" // id of the button
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
