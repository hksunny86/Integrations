<!--Title: i8Microbank-->
<!--Author: Atif Hussain-->
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<%@page import="com.inov8.microbank.common.util.PortalConstants"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
	<meta name="decorator" content="decorator">
	<meta name="title" content="Disbursement Details" />
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

		function ajaxPostFunction() {
			$('errorMsg').innerHTML = "";
			Element.hide('errorMsg');
			$('successMsg').innerHTML = $F('message');
			Element.show('successMsg');

			document.getElementById(this.source).disabled=true;

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

		function deleteRow(t) {
			var row = t.parentNode.parentNode;
			alert(document.getElementById("ec_table"));
			document.getElementById("ec_table").deleteRow(row.rowIndex);
			console.log(row);
		}
	</script>
</head>
<body>

<c:if test="${not empty messages}">
	<div class="infoMsg" id="successMessages">
		<c:forEach var="msg" items="${messages}">
			<c:out value="${msg}" escapeXml="false" />
			<br />
		</c:forEach>
	</div>
	<c:remove var="messages" scope="session" />
</c:if>

<div id="successMsg" class="infoMsg" style="display:none;"></div>
<div id="errorMsg" class="errorMsg" style="display:none;"></div>
<input id="message" value="777" type="hidden" />
<html:form name="retryAdviceListViewSearchForm" commandName="bulkDisbursementsVOModel" method="post" action="p_bulkPaymentDisbReport.html"
		   onsubmit="return onFormSubmit(this);" >
	<table width="850px" border="0">

		<tr>
			<td class="formText" width="18%" align="right">Service:</td>
			<td align="left" width="32%">
				<html:select id="serviceId" path="serviceId" cssClass="textBox" tabindex="1">
					<html:option value="">--- Select ---</html:option>
					<c:if test="${serviceModelList != null}">
						<html:options items="${serviceModelList}" itemValue="id" itemLabel="label"/>
					</c:if>
				</html:select>
			</td>

			<td class="formText" align="right">Validation Status:</td>
			<td align="left">
				<html:select id="validRecord" path="validRecord" cssClass="textBox" tabindex="7">
					<html:option value="">---All---</html:option>
					<html:option value="0">Invalid</html:option>
					<html:option value="1">Valid</html:option>
				</html:select>				</td>
		</tr>

		<tr>
			<td class="formText" align="right">Product:</td>
			<td align="left">
				<html:select path="productId" cssClass="textBox"  id="productId" tabindex="1">
					<html:option value="">---Select---</html:option>
					<c:if test="${productList != null}">
						<html:options items="${productList}" itemValue="id" itemLabel="label"/>
					</c:if>
				</html:select>
			</td>
			<td class="formText" align="right">Batch Number:</td>

			<td align="left"><html:input path="batchNumber" id="batchNumber" cssClass="textBox" maxlength="50" tabindex="3" onkeypress="return maskInteger(this,event)" /></td>
			</td>
		</tr>
		<tr>
			<td class="formText" align="right"></td>
			<td align="left" colspan="3">
			</td>

		<tr>
			<td align="right" class="formText">File Upload From Date:</td>
			<td align="left">
				<html:input path="uploadFromDate" id="uploadFromDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
				<img id="uFDate" tabindex="5" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
				<img id="uFDate" tabindex="6" title="Clear Date" name="popcal" onclick="javascript:$('uploadFromDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
			</td>
			<td align="right" class="formText">File Upload To Date:</td>
			<td align="left">
				<html:input path="uploadToDate" id="uploadToDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
				<img id="uTDate" tabindex="7" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
				<img id="uTDate" tabindex="8" title="Clear Date" name="popcal" onclick="javascript:$('uploadToDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
			</td>
		</tr>

		</tr>
		<tr>
			<td align="center" colspan="4"><input name="_search" type="submit" class="button" value="Search" tabindex="9" /></td>
		</tr>
	</table>

	<ajax:select source="serviceId" target="productId"
				 baseUrl="${contextPath}/p_productRefData.html" errorFunction="error"
				 parameters="serviceId={serviceId}"/>

</html:form>
<!-- data table result -->
<ec:table items="bulkDisbursementsModelList" var="model" action="${contextPath}/p_bulkPaymentDisbReport.html" title="" retrieveRowsCallback="limit"
		  filterRowsCallback="limit" sortRowsCallback="limit" filterable="false">
	<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
		<ec:exportXls fileName="Bulk Payment/Disbursement Summary View.xls" tooltip="Export Excel" />
	</authz:authorize>
	<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
		<ec:exportXlsx fileName="Bulk Payment/Disbursement Summary View.xlsx" tooltip="Export Excel" />
	</authz:authorize>
	<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
		<ec:exportCsv fileName="Bulk Payment/Disbursement Summary View.csv" tooltip="Export CSV"></ec:exportCsv>
	</authz:authorize>
	<ec:row>

		<ec:column title="Batch Number" property="batchNumber" escapeAutoFormat="true">
			<authz:authorize ifAnyGranted="<%=PortalConstants.BULK_DISBURSEMENTS_CREATE%>">
				<a href="#" onclick="return openBulkFileInfoSummaryViewWindow('${model.fileInfoId}', '${model.productName}')">${model.batchNumber}</a>
			</authz:authorize>
		</ec:column>

		<ec:column title="Service" property="serviceName" escapeAutoFormat="true" />
		<ec:column title="Product" property="productName" escapeAutoFormat="true" />
		<ec:column title="Source A/C #" property="sourceACNo" escapeAutoFormat="true" >
			<%--${model.fileInfoIdBulkDisbursementsFileInfoModel.sumAccountNumber}--%>
		</ec:column>
		<ec:column title="Payment Date" property="paymentDate" escapeAutoFormat="true" cell="date" format="dd/MM/yyyy" />
		<ec:column title="Employee No" property="employeeNo" escapeAutoFormat="true" />
		<ec:column title="Name" property="name" escapeAutoFormat="true" />
		<ec:column title="Mobile #" property="mobileNo" escapeAutoFormat="true" />
		<ec:column title="CNIC" property="cnic" escapeAutoFormat="true" />
		<ec:column title="Cheque No" property="chequeNo" escapeAutoFormat="true" />
		<ec:column title="Amount" property="amount" style="text-align: right;" escapeAutoFormat="true" />
		<ec:column title="Description" property="description" escapeAutoFormat="true" />

		<c:choose>

			<c:when test="${model.limitApplicable}">
				<ec:column property="limitApplicable" title="Limit Applicable" style="text-align: center" escapeAutoFormat="True" sortable="false" value="Yes" />
			</c:when>
			<c:otherwise>
				<ec:column property="limitApplicable" title="Limit Applicable" style="text-align: center" escapeAutoFormat="True" sortable="false" value="No" />
			</c:otherwise>

		</c:choose>

		<c:choose>
			<c:when test="${model.payCashViaCnic}">
				<ec:column property="payCashViaCnic" title="Via CNIC" style="text-align: center" escapeAutoFormat="True" sortable="false" value="Yes" />
			</c:when>
			<c:otherwise>
				<ec:column property="payCashViaCnic" title="Via CNIC" style="text-align: center" escapeAutoFormat="True" sortable="false" value="No" />
			</c:otherwise>
		</c:choose>
		<c:choose>
			<c:when test="${model.validRecord}">
				<ec:column property="validRecord" title="Status" style="text-align: center" escapeAutoFormat="True" sortable="false" value="Valid" />
			</c:when>
			<c:otherwise>
				<ec:column property="validRecord" title="Status" style="text-align: center" escapeAutoFormat="True" sortable="false" value="Invalid" />
			</c:otherwise>
		</c:choose>
		<ec:column title="Failure Reason" property="reason" escapeAutoFormat="true" />

	</ec:row>
</ec:table>

<script type="text/javascript" language="javascript">
	/*
	 Calendar.setup({
	 inputField : "createdOn", // id of the input field
	 button : "sDate", // id of the button
	 });
	 */

	function openBulkFileInfoSummaryViewWindow(fileId, productName) {

		var popupWidth = 750;
		var poupHeight = 350;
		var popupLeft = (window.screen.width - popupWidth)/2;
		var popupTop = (window.screen.height - poupHeight)/2;
		newWindow=window.open('p_bulkfileinfosummaryviewpopup.html?id='+fileId+'&productName='+productName,'File Summary','width='+popupWidth+',height='+poupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
		if(window.focus) newWindow.focus();
		return false;
	}


	Calendar.setup(
			{
				inputField  : "uploadFromDate", // id of the input field
				button      : "uFDate",    // id of the button
			}
	);
	Calendar.setup(
			{
				inputField  : "uploadToDate", // id of the input field
				button      : "uTDate",    // id of the button
				isEndDate: true
			}
	);
	/*jq(document).ready(function(){
		jq(document).keydown(function(event) {
			if (event.ctrlKey==true && (event.which == '118' || event.which == '86')) {
				//alert('thou. shalt. not. PASTE!');
				event.preventDefault();
			}
		});
	});*/
	function onFormSubmit(form) {

		if(jq('#batchNumber').val()) {

			if (!IsNumericData(jq('#batchNumber').val(),'Batch#')) {
				return false;
			}
			else{
				return validateForm(form);
			}
		}
		else {
		return	validateForm(form);
		}
	}

</script>
<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>
</body>
</html>