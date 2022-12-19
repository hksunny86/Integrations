<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<%@page import="com.inov8.microbank.common.util.PortalConstants"%>
<%@include file="/common/taglibs.jsp"%>
<html>
	<head>
		<meta name="decorator" content="decorator">
		<meta name="title" content="Disbursement File Summary" />

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
			var jq=$.noConflict();

			function error(request) {
				alert("An unknown error has occurred. Please contact with the administrator for more details.");
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

		<html:form name="disbursementFindInfo" commandName="disbursementFileInfoViewModel" method="post" action="p_disbursementFileInfo.html"
				   onsubmit="return validateForm(this);" >
			<table width="80%">

				<tr>
					<td class="formText" align="right">Service:</td>
					<td align="left">
						<html:select id="serviceId" path="serviceId" cssClass="textBox" tabindex="1">
							<html:option value="">--- Select ---</html:option>
							<c:if test="${serviceList != null}">
								<html:options items="${serviceList}" itemValue="id" itemLabel="label"/>
							</c:if>
						</html:select>
					</td>
					<td class="formText" align="right">Product:</td>
					<td align="left">
						<html:select path="productId" cssClass="textBox"  id="productId" tabindex="2">
							<html:option value="">---Select---</html:option>
							<c:if test="${productList != null}">
								<html:options items="${productList}" itemValue="id" itemLabel="label"/>
							</c:if>
						</html:select>
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">Batch Number:</td>
					<td align="left">
						<html:input path="batchNumber" id="batchNumber" cssClass="textBox" maxlength="50" tabindex="3" onkeypress="return maskNumber(this,event)" />
					</td>

					<td class="formText" align="right">Status:</td>
					<td align="left">
						<html:select id="status" path="status" cssClass="textBox" tabindex="4">
							<html:option value="">---All---</html:option>
							<c:if test="${statusMap != null}">
								<c:forEach items="${statusMap}" var="itemrow">
									<html:option value='${itemrow.key}'>${itemrow.value}</html:option>
								</c:forEach>
							</c:if>
						</html:select>
					</td>
				</tr>

				<tr>
					<td align="right" class="formText">File Upload From Date:</td>
					<td align="left">
						<html:input path="createdOnStart" id="createdOnStart" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
						<img id="uFDate" tabindex="5" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
						<img id="uFDate" tabindex="6" title="Clear Date" name="popcal" onclick="javascript:$('createdOnStart').value=''"
							 align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
					<td align="right" class="formText">File Upload To Date:</td>
					<td align="left">
						<html:input path="createdOnEnd" id="createdOnEnd" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
						<img id="uTDate" tabindex="7" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
						<img id="uTDate" tabindex="8" title="Clear Date" name="popcal" onclick="javascript:$('createdOnEnd').value=''"
							 align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr>

				<tr>
					<td colspan="4" align="center">
						<input name="_search" type="submit" class="button" value="Search" tabindex="9" />
						<input type="reset" class="button" value="Cancel" onclick="javascript: window.location='p_disbursementFileInfo.html'" tabindex="10" />
					</td>
				</tr>
			</table>

			<ajax:select source="serviceId" target="productId"
				baseUrl="${contextPath}/p_productRefData.html" errorFunction="error"
				parameters="serviceId={serviceId}"/>

		</html:form>

		<ec:table items="disbursementFileInfoViewModelList" var="model" action="${contextPath}/p_disbursementFileInfo.html" title="" retrieveRowsCallback="limit"
			filterRowsCallback="limit" sortRowsCallback="limit" filterable="false">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Disbursement File Info Summary.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Disbursement File Info Summary.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Disbursement File Info Summary.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>

				<ec:column title="Batch Number" property="batchNumber" escapeAutoFormat="true">
					<a href="#" onclick="return openBulkFileInfoSummaryViewWindow('${model.disbursementFileInfoId}', '${model.productName}')">
						${model.batchNumber}
				</ec:column>

				<ec:column title="Service" property="serviceName" escapeAutoFormat="true" />
				<ec:column title="Product" property="productName" escapeAutoFormat="true" />
				<ec:column title="Source A/C #" property="sourceAccountNumber" escapeAutoFormat="true" />
				<ec:column title="Upload Date" property="createdOn" escapeAutoFormat="true" cell="date" format="dd/MM/yyyy hh:mm:ss"  sortable="true"/>
				<ec:column title="Uploaded By" property="createdByName" escapeAutoFormat="true" />
				<ec:column title="Total Records" property="totalRecords" cell="currency" format="#,###.##" style="text-align:right;" escapeAutoFormat="true" />
				<ec:column title="Valid Records" property="validRecords" cell="currency" format="#,###.##" style="text-align:right;" escapeAutoFormat="true" />
				<ec:column title="Invalid Records" property="invalidRecords" cell="currency" format="#,###.##" style="text-align:right;" escapeAutoFormat="true" />
				<ec:column title="Status" property="statusStr" sortable="false" escapeAutoFormat="true" />
				<ec:column title="Total Amount" property="totalAmount" cell="currency" format="#,###.##" style="text-align:right;" escapeAutoFormat="true" />
				<ec:column title="Total Charges" property="totalCharges" cell="currency" format="#,###.##" style="text-align:right;" escapeAutoFormat="true" />
				<ec:column title="Total FED" property="totalFed" cell="currency" format="#,###.##" style="text-align:right;" escapeAutoFormat="true" />
			</ec:row>
		</ec:table>

		<script type="text/javascript" language="javascript">

			function openBulkFileInfoSummaryViewWindow(fileId, productName) {

				var popupWidth = 750;
				var poupHeight = 450;
				var popupLeft = (window.screen.width - popupWidth)/2;
				var popupTop = (window.screen.height - poupHeight)/2;
				newWindow=window.open('p_bulkfileinfosummaryviewpopup.html?id='+fileId+'&productName='+productName,'File Summary','width='+popupWidth+',height='+poupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
				if(window.focus) newWindow.focus();
				return false;
			}


			Calendar.setup(
					{
						inputField  : "createdOnStart", // id of the input field
						button      : "uFDate",    // id of the button
					}
			);
			Calendar.setup(
					{
						inputField  : "createdOnEnd", // id of the input field
						button      : "uTDate",    // id of the button
						isEndDate: true
					}
			);

		</script>
		<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>
	</body>
</html>