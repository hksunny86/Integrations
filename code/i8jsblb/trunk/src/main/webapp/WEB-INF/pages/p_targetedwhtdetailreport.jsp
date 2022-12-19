<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ page import="com.inov8.microbank.common.util.PortalConstants" %>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<%@include file="/common/taglibs.jsp"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'p_targetedwhtdetailreport.jsp' starting page</title>
    
	<!-- <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page"> -->
	<meta name="decorator" content="decorator">
	<meta name="title" content="Targeted WHT Detail" />
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/prototype.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/date-validation.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/popup.js"></script>
	<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
	<link rel="stylesheet" type="text/css" href="${contextPath}/styles/deliciouslyblue/calendar.css" />
	<%@include file="/common/ajax.jsp"%>
	<script type="text/javascript" src="${contextPath}/scripts/jquery-1.7.2.min.js"></script>

  </head>
  
  <body>
  
  
  
   	<html:form name="wHTSummaryForm" commandName="targetedWhtDetailReportModel" method="post" action="p_targetedwhtdetailreport.html"
		onsubmit="return validateThisForm(this);" >
		<table width="850px" border="0">
			<tr>
				<td class="formText" width="18%" align="right">TaxPayer Mobile #:</td>
				<td align="left">
					<html:input tabindex="1" maxlength="11" path="mobileNo" cssClass="textBox" onkeypress="return maskInteger(this,event)"/>
				</td>
				<td class="formText" width="18%" align="right">TaxPayer CNIC:</td>
				<td align="left">
					<html:input tabindex="2" maxlength="15" path="taxPayerCnic" cssClass="textBox"/>
				</td>
			</tr>
			<tr>
				<td class="formText" width="18%" align="right"><span class="asterisk">*</span>Start Date:</td>
				<td align="left" width="32%">
					<html:input path="startDate" id="startDate" readonly="true" tabindex="3" cssClass="textBox" maxlength="10" /> <img
						id="sDate" tabindex="4" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" /> <img id="sDate" tabindex="5"
						title="Clear Date" name="popcal" onclick="javascript:$('startDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png"
						border="0" />
				</td>
				<td class="formText" align="right"><span class="asterisk">*</span>End Date:</td>
				<td align="left"><html:input path="endDate" id="endDate" readonly="true" tabindex="6" cssClass="textBox" maxlength="10" /> <img
					id="eDate" tabindex="7" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" /> <img id="eDate" tabindex="8"
					title="Clear Date" name="popcal" onclick="javascript:$('endDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png"
					border="0" /></td>
			</tr>
			<tr>
				<td class="formText" align="right"></td>
				<td align="left"><input name="_search" type="submit" class="button" value="Search" tabindex="9" />
				<input name="_reset" type="reset" class="button" value="Reset" tabindex="10" /></td>
				<td class="formText" align="right"></td>
				<td align="left"></td>
			</tr>
		</table>
	</html:form>
	
	
		<!-- data table result -->
	<ec:table items="resultList" var="model" action="${contextPath}/p_targetedwhtdetailreport.html" title="" retrieveRowsCallback="limit"
		filterRowsCallback="limit" sortRowsCallback="limit" filterable="false">
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
			<ec:exportXls fileName="Targeted_WHT_Detail_Report.xls" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
			<ec:exportXlsx fileName="Targeted_WHT_Detail_Report.xlsx" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
			<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da" headerTitle="Targeted WHT Detail Report"
				fileName="Targeted_WHT_Detail_Report.pdf" tooltip="Export PDF" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
			<ec:exportCsv fileName="Agent_WHT_Report_231A.csv" tooltip="Export CSV"></ec:exportCsv>
		</authz:authorize>
		<ec:row>
			<ec:column property="transactionCode" title="Transaction ID" escapeAutoFormat="true"/>
			<ec:column property="userId" title="User ID" escapeAutoFormat="true"/>
			<ec:column property="appUserTypeId" title="User Type ID" escapeAutoFormat="true"/>
			<ec:column property="taxPayerName" title="Tax Payer Name" escapeAutoFormat="true"/>
			<ec:column property="taxPayerCnic" title="Tax Payer CNIC" escapeAutoFormat="true"/>
			<ec:column property="mobileNo" title="Tax Payer Mobile #" escapeAutoFormat="true" />
			<ec:column property="taxPayerAddress" title="Address" escapeAutoFormat="true" />
			<ec:column property="taxPayerCategory" title="Tax Payer Category" escapeAutoFormat="true" />
			<ec:column property="productName" title="Product Name" escapeAutoFormat="true" />
			<ec:column property="createdOn" title="Created ON" escapeAutoFormat="true" cell="date" format="dd/MM/yyyy hh:mm a" sortable="true"/>
			<ec:column property="wht" title="WHT" escapeAutoFormat="true" />
			<ec:column property="whtRate" title="WHT Rate" escapeAutoFormat="true" />
			<ec:column property="transactionAmount" title="Transaction Amount" escapeAutoFormat="true" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
			<ec:column property="dailyTransactionAmount" title="Daily Transaction Amount" escapeAutoFormat="true" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
		
			
		</ec:row>
	</ec:table>
	
	
	
	

  
  <script type="text/javascript">
		function validateThisForm(form){
			var currentDate = "<%=PortalDateUtils.getServerDate()%>";
	        var _fDate = form.startDate.value;
	  		var _tDate = form.endDate.value;
		  	var startlbl = "Start Date";
		  	var endlbl   = "End Date";
	        var isValid = validateDateRangeMandatory(_fDate,_tDate,startlbl,endlbl,currentDate);
	        return isValid;
	    }
	
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
