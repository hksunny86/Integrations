<!--Title: i8Microbank-->
<!--Author: Atif Hussain-->
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<%@page import="com.inov8.microbank.common.util.PortalConstants"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<meta name="decorator" content="decorator">
<meta name="title" content="Retry Advice Report" />
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
	<html:form name="retryAdviceListViewSearchForm" commandName="retryAdviceSearchVO" method="post" action="p_retryadvicelistviewmanagement.html"
		onsubmit="return validateForm(this);" >
		<table width="850px" border="0">
			<tr>
				<td class="formText" width="18%" align="right">Transaction ID:</td>
				<td align="left" width="32%"><html:input path="transactionCode" id="transactionCodeId" cssClass="textBox" maxlength="50" tabindex="1"
						onkeypress="return maskNumber(this,event)" /></td>
				<td class="formText" align="right">Product:</td>
				<td align="left"><html:select path="productId" cssClass="textBox" tabindex="10">
						<html:option value="">---All---</html:option>
						<c:if test="${productModelList != null}">
							<html:options items="${productModelList}" itemValue="productId" itemLabel="name" />
						</c:if>
					</html:select></td>
			</tr>
			<tr>
				<td class="formText" align="right">From Account:</td>
				<td align="left"><html:input path="fromAccount" id="fromAccountId" cssClass="textBox" maxlength="50" tabindex="3"
						onkeypress="return maskNumber(this,event)" /></td>
				<td class="formText" align="right">To Account:</td>
				<td align="left"><html:input path="toAccount" id="toAccountId" cssClass="textBox" maxlength="11" tabindex="4"
						onkeypress="return maskNumber(this,event)" /></td>
			</tr>
			<tr>
				<td class="formText" align="right">Request Date Start:</td>
				<td align="left"><html:input path="requestTimeStart" id="startDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10" /> <img
					id="sDate" tabindex="11" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" /> <img id="sDate" tabindex="12"
					title="Clear Date" name="popcal" onclick="javascript:$('startDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png"
					border="0" /></td>
				<td class="formText" align="right">Request Date End:</td>
				<td align="left"><html:input path="requestTimeEnd" id="endDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10" /> <img
					id="eDate" tabindex="13" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" /> <img id="eDate" tabindex="14"
					title="Clear Date" name="popcal" onclick="javascript:$('endDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png"
					border="0" /></td>
			</tr>
			<tr>
				<td class="formText" align="right">Advice Type:</td>
				<td align="left"><html:select path="adviceType" cssClass="textBox" tabindex="7">
						<html:option value="">---All---</html:option>
						<html:option value="Credit">Credit</html:option>
						<html:option value="Reversal">Reversal</html:option>
						<html:option value="Bill Payment Advice">Bill Payment Advice</html:option>
					<html:option value="Challan Collection">Challan Collection</html:option>
					<html:option value="Account Opening">Account Opening</html:option>
					</html:select></td>
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
	<ec:table items="resultList" var="model" action="${contextPath}/p_retryadvicelistviewmanagement.html" title="" retrieveRowsCallback="limit"
		filterRowsCallback="limit" sortRowsCallback="limit" filterable="false">
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
			<ec:exportXls fileName="Retry_Advice_List_Report.xls" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
			<ec:exportXlsx fileName="Retry_Advice_List_Report.xlsx" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
			<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da" headerTitle="View Transactions"
				fileName="Retry_Advice_List_Report.pdf" tooltip="Export PDF" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
			<ec:exportCsv fileName="Retry_Advice_List_Report.csv" tooltip="Export CSV"></ec:exportCsv>
		</authz:authorize>
		<ec:row>
			<ec:column property="transactionCode" title="Transaction ID" escapeAutoFormat="true" />
			<ec:column property="productName" title="Product Name" escapeAutoFormat="true" />
			<ec:column property="fromAccount" title="From Account" escapeAutoFormat="true" />
			<ec:column property="toAccount" title="To Account" escapeAutoFormat="true" />
			<ec:column property="transactionAmount" title="Transaction Amount" escapeAutoFormat="true" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="transmissionTime" title="Transmission Time" escapeAutoFormat="true" cell="date" format="dd/MM/yyyy hh:mm a" />
			<ec:column property="requestTime" title="Request Time" escapeAutoFormat="true" cell="date" format="dd/MM/yyyy hh:mm a" />
			<ec:column property="stan" title="Stan/RRN" escapeAutoFormat="true" />
			<ec:column property="reversalStan" title="Reversal Stan" escapeAutoFormat="true" />
			<ec:column property="reversalRequestDateTime" title="Reversal Request Time" escapeAutoFormat="true" cell="date" format="dd/MM/yyyy hh:mm a" />
			<%-- <ec:column property="responseCode" title="Response Code" escapeAutoFormat="true" /> --%>
			<ec:column property="adviceType" title="Advice Type" escapeAutoFormat="true" />
			<ec:column property="status" title="Status" escapeAutoFormat="true" />
			<ec:column title="History" alias="" viewsAllowed="html">
				<input id="btnhistory_${model.middlewareRetryAdvRprtId}"  type="button" value="View History" 
				class="button" onclick="openHistoryWindow('p-retryadvicesearchhistory.html?transactionCode=${model.transactionCode}&stan=${model.stan}', 'Retry Advice History', 500,500);"/>
			</ec:column>
			<ec:column alias="" title="" viewsAllowed="html">
				<authz:authorize ifAnyGranted="<%=PortalConstants.RPT_RETRY_ADVICE_UPDATE%>">
					<c:choose>
						<c:when test="${model.status eq 'Failed'}">
					    	<input type="button" class="button" value="Retry" id="btnretry_${model.middlewareRetryAdvRprtId}" onclick="disableRetryButton(this);"/>
						</c:when>
					    <c:otherwise>
					    	<input type="button" class="button" disabled="disabled" value="Retry" id="btnretry_${model.middlewareRetryAdvRprtId}" />
					    </c:otherwise>
					</c:choose>
					<ajax:updateField baseUrl="${contextPath}/p_retryadviceajaxrequest.html" 
					source="btnretry_${model.middlewareRetryAdvRprtId}" eventType="click"
					target="message" action="btnretry_${model.middlewareRetryAdvRprtId}" 
					parameters="middlewareRetryAdvRprtId=${model.middlewareRetryAdvRprtId}"
					parser="new ResponseXmlParser()" 
					preFunction="ajaxPreFunction"
					errorFunction="ajaxErrorFunction" 
					postFunction="ajaxPostFunction" />
				</authz:authorize>
			</ec:column>
			<ec:column alias="" title="" viewsAllowed="html">
				<authz:authorize ifAnyGranted="<%=PortalConstants.RPT_RETRY_ADVICE_UPDATE%>">
					<c:choose>
						<c:when test="${model.status eq 'Failed'}">
							<c:choose>
							<c:when test="${model.productId==50056}">
								<input type="button" class="button" value="Skip" id="btnskip_${model.middlewareRetryAdvRprtId}" style="display:none !important;"  disabled="disabled"/>
							</c:when>
							<c:otherwise>
								<input type="button" class="button" value="Skip" id="btnskip_${model.middlewareRetryAdvRprtId}"/>
							</c:otherwise>
							</c:choose>

						</c:when>
					    <c:otherwise>

							<c:choose>
								<c:when test="${model.productId==50056}">
									<input type="button" class="button" value="Skip" id="btnskip_${model.middlewareRetryAdvRprtId}" style="display:none !important;" disabled="disabled"/>
								</c:when>
								<c:otherwise>
									<input type="button" class="button" value="Skip" id="btnskip_${model.middlewareRetryAdvRprtId}" disabled="disabled"/>
								</c:otherwise>
							</c:choose>

					    </c:otherwise>
					</c:choose>
					<ajax:updateField baseUrl="${contextPath}/p_retryadviceajaxrequest.html" 
					source="btnskip_${model.middlewareRetryAdvRprtId}" eventType="click"
					target="message" action="btnskip_${model.middlewareRetryAdvRprtId}" 
					parameters="middlewareRetryAdvRprtId=${model.middlewareRetryAdvRprtId},rowAction=skip"
					parser="new ResponseXmlParser()" 
					preFunction="ajaxPreFunction"
					errorFunction="ajaxErrorFunction" 
					postFunction="ajaxPostFunction" />
				</authz:authorize>
			</ec:column>
		</ec:row>
	</ec:table>
	<script type="text/javascript">
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