<!--Title: i8Microbank-->
<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.*,com.inov8.microbank.common.util.PortalConstants'%>
<html>
	<head>
		<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
		<c:set var="createAction"><%=PortalConstants.ACTION_CREATE %></c:set>
		<meta name="decorator" content="decorator2">
		<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
	    <script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
		<script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>

		<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
		<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
		<meta name="title" content="Search KYC" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<script type="text/javascript">
			var jq=$.noConflict();
			var serverDate ="<%=PortalDateUtils.getServerDate()%>";
		</script>
		
	<%
		String createPermission = PortalConstants.MNG_L2_KYC_C;
		String updatePermission = PortalConstants.MNG_L2_KYC_U;
		String readUpdatePermission = PortalConstants.MNG_L2_KYC_R;
	%>	
		
	</head>
	<body bgcolor="#ffffff">
	
	<div id="successMsg" class ="infoMsg" style="display:none;"></div>
		<spring:bind path="level2KycModel.*">
			<c:if test="${not empty status.errorMessages}">
				<div class="errorMsg">
					<c:forEach var="error" items="${status.errorMessages}">
						<c:out value="${error}" escapeXml="false" />
						<br />
					</c:forEach>
				</div>
			</c:if>
		</spring:bind>
		<html:form name="level2kycsearchform" commandName="level2KycModel" method="post"
			action="p_l2_kyc_searchform.html" onsubmit="return validateForm(this)">
			<table width="850px" border="0">
				<tr>
					<td align="right" class="formText">
						Initial Application Form No.:
					</td>
					<td align="left" >
						<html:input path="initialAppFormNo" cssClass="textBox" maxlength="16" tabindex="1"/>
					</td>
					<td class="formText" align="right" width="20%">
						
					</td>
					<td align="left" width="30%">
						 
					</td>
				</tr>
				<tr>
					<td align="right" class="formText">
						Start Date:
					</td>
					<td align="left">
				        <html:input path="startDate" id="startDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
						<img id="sDate" tabindex="6" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
						<img id="sDate" tabindex="7" title="Clear Date" name="popcal" onclick="javascript:$('startDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
					<td align="right" class="formText">
						End Date:
					</td>
					<td align="left">
					     <html:input path="endDate" id="endDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
					     <img id="eDate" tabindex="8" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
					     <img id="eDate" tabindex="9" title="Clear Date" name="popcal" onclick="javascript:$('endDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">
						&nbsp;
					</td>
					<td align="left">
						<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
						<input name="searchBtn" type="submit" class="button" value="Search" tabindex="10"/>
						<input name="reset" type="reset"
							onclick="javascript: window.location='p_l2_kyc_searchform.html'"
							class="button" value="Cancel" tabindex="11" />
					</td>
					<td colspan="2">
						&nbsp;
					</td>
				</tr>
			</table>
		</html:form>
		<ec:table items="kycModelList" var="resultantModel"
			retrieveRowsCallback="limit" filterRowsCallback="limit"
			sortRowsCallback="limit" title="" width="100%" filterable="false" sortable="true"
			action="${pageContext.request.contextPath}/p_l2_kyc_searchform.html">
			
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Level2 KYC Forms.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Level2 KYC Forms.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Level2 KYC Forms" fileName="Level2 KYC Forms.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Level2 KYC Forms.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			
			<ec:row>
				<ec:column property="initialAppFormNo" title="Initial Application Form No." cell="date" style="text-align:center;"/>
				<ec:column property="fundSourceIdFundSourceModel.name" title="Fund Source" cell="date" style="text-align:center;"/>
				<ec:column property="noOfTxDebit" title="Debit Trx. per month"  style="text-align:center;"/>
				<ec:column property="noOfTxCredit" title="Credit Trx. per month" style="text-align:center;"/>
				<ec:column property="valOfTxDebit" title="Debit Value per month"  style="text-align:center;"/>
				<ec:column property="valOfTxCredit" title="Credit Value per month" style="text-align:center;"/>
				<ec:column property="*" title="Action" style="text-align:center;" viewsAllowed="html">
				<authz:authorize ifAnyGranted="<%=updatePermission%>">
					<input type="button" class="button" value="Edit" onclick="window.location='p_l2_kyc_form.html?initialApplicationFormNumber=${resultantModel.initialAppFormNo}'" />
				</authz:authorize>
				<authz:authorize ifNotGranted="<%=updatePermission%>" >
					<input type="button" class="button" value="Edit" disabled="disabled" />
				</authz:authorize>
				</ec:column>
			</ec:row>
		</ec:table>
	
		<script language="javascript" type="text/javascript">
		
  		Calendar.setup(
  		{
	       inputField  : "startDate", // id of the input field
		   button      : "sDate",    // id of the button
	    }
  		);
		Calendar.setup(
	    {
	      inputField  : "endDate", // id of the input field
	      button      : "eDate",    // id of the button
		  isEndDate: true	
	    }
	    );
  	</script>
	<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>
	</body>
</html>
