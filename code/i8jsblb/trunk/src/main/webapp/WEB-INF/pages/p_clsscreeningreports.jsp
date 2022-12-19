<!--Title: i8Microbank-->
<!--Author: Atif Hussain-->
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<%@page import="com.inov8.microbank.common.util.PortalConstants"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<meta name="decorator" content="decorator">
<meta name="title" content="CLS Screening Report" />
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
<script language="javascript" type="text/javascript">
	var serverDate ="<%=PortalDateUtils.getServerDate()%>";
	var jq=$.noConflict();

	function error(request) {
		alert("An unknown error has occured. Please contact with the administrator for more details");
	}

	function init() {
		$('errorMsg').innerHTML = "";
		$('successMsg').innerHTML = "";
		Element.hide('successMsg');
		Element.hide('errorMsg');
	}

	function ajaxPreFunction() {
		var id = this.source;
		var indx = id.substr(id.indexOf("_")+1);
		document.getElementById("btnretry_" + indx).disabled=true;
		document.getElementById("btnskip_" + indx).disabled=true;
		return true;
	};

	function ajaxPostFunction() {
		$('errorMsg').innerHTML = "";
		Element.hide('errorMsg');
		$('successMsg').innerHTML = $F('message');
		Element.show('successMsg');
		
		jq('html, body').animate({scrollTop : 0},1000);
	};

	function ajaxErrorFunction(request, obj) {
		var msg = "Your request cannot be processed at the moment. Please try again later.";
		$('successMsg').innerHTML = "";
		Element.hide('successMsg');
		jq('html, body').animate({scrollTop : 0},1000);
	};
	
	function openHistoryWindow(url, title, w, h) {
	  var left = (screen.width/2)-(w/2);
	  var top = (screen.height/2)-(h/2);
	  return window.open(url, title, 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes, copyhistory=no, width='+w+', height='+h+', top='+top+', left='+left);
	} 
</script>
</head>
<body>
	<div id="successMsg" class="infoMsg" style="display:none;"></div>
	<div id="errorMsg" class="errorMsg" style="display:none;"></div>
	<input id="message" value="777" type="hidden" />
	<html:form name="retryAdviceListViewSearchForm" commandName="accountOpeningPendingSafRepoVeiwModel" method="post" action="p_clsscreeningreports.html"
		onsubmit="return validateForm(this);" >
		<table width="850px" border="0">
			<tr>
				<td class="formText" width="18%" align="right">Mobile Number:</td>
				<td align="left" width="32%"><html:input path="mobileNo" cssClass="textBox" maxlength="50" tabindex="1"
						onkeypress="return maskNumber(this,event)" /></td>
				<td class="formText" align="right">
					Cnic:
				</td>
				<td align="left">
					<html:input path="cnic" cssClass="textBox" tabindex="5" maxlength="50" onkeypress="return maskNumber(this,event)"/>
				</td>
			</tr>
			<tr>
				<td class="formText" align="right">
					Created On From Date:
				</td>
				<td align="left">
					<html:input path="createdOn" id="createdOnFromDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
					<img id="CreatedFromDate" tabindex="7" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
					<img id="CreatedFromDate" tabindex="8" title="Clear Date" name="popcal" onclick="javascript:$('createdOnFromDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
				</td>
				<td class="formText" align="right">
					Created On To Date:
				</td>
				<td align="left">
					<html:input path="createdOnToDate" id="createdOnToDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
					<img id="CreatedToDate" tabindex="9" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
					<img id="CreatedToDate" tabindex="10" title="Clear Date" name="popcal" onclick="javascript:$('createdOnToDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
				</td>
			</tr>
			<tr>
				<td class="formText" align="right"></td>
				<td align="left"><input name="_search" type="submit" class="button" value="Search" tabindex="13" /></td>
				<td class="formText" align="right"></td>
				<td align="left"></td>
			</tr>
		</table>
	</html:form>
	<!-- data table result -->
	<ec:table items="bookMeList" var="bookMeList" action="${contextPath}/p_clsscreeningreports.html" title="" retrieveRowsCallback="limit"
		filterRowsCallback="limit" sortRowsCallback="limit" filterable="false">
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
			<ec:exportXls fileName="Book Me Report.xls" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
			<ec:exportXlsx fileName="Book Me Report.xlsx" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
			<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da" headerTitle="View Transactions"
				fileName="Book Me Report.pdf" tooltip="Export PDF" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
			<ec:exportCsv fileName="Book Me Report.csv" tooltip="Export CSV"></ec:exportCsv>
		</authz:authorize>
		<ec:row>

			<ec:column property="mobileNo" title="Mobile No" escapeAutoFormat="true" />
			<ec:column property="cnic" title="Customer Cnic" escapeAutoFormat="true" />
			<ec:column property="accountState" title="Account State" escapeAutoFormat="true" />
            <ec:column property="customerName" title="Customer Name" escapeAutoFormat="true" />
			<ec:column property="registrationstate" title="Registration State" escapeAutoFormat="true" />
			<ec:column property="clsResponseCode" title="Reason Of Rejection" escapeAutoFormat="true"/>
			<ec:column property="product" title="Product" escapeAutoFormat="true" />
			<ec:column property="createdOn" title="Account Created On" escapeAutoFormat="true" cell="date" format="dd/MM/yyyy hh:mm a" />
		</ec:row>
	</ec:table>
	<script type="text/javascript">

        Calendar.setup(
            {
                inputField  : "createdOnFromDate", // id of the input field
                button      : "CreatedFromDate",    // id of the button
            }
        );
        Calendar.setup(
            {
                inputField  : "createdOnToDate", // id of the input field
                button      : "CreatedToDate",    // id of the button
                isEndDate: true
            }
        );

	</script>
	<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>
</body>
</html>