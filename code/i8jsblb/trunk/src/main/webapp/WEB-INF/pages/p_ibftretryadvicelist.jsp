<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<%@page import="com.inov8.microbank.common.util.PortalConstants"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<meta name="decorator" content="decorator">
<meta name="title" content="IBFT Retry Advice Report" />
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
		try{
			var str = this.source;
			var substr = str.split("_");
			document.getElementById('status_'+substr[1]).innerHTML = "<%=PortalConstants.IBFT_STATUS_IN_PROGRESS%>";
		}catch(exception){
		}
		
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
	<html:form name="ibftRetryAdviceListForm" commandName="iBFTRetryAdviceListViewModel" method="post" action="p_ibftretryadvicelist.html"
		onsubmit="return validateForm(this);" >
		<table width="850px" border="0">
			<tr>
				<td class="formText" width="18%" align="right">Transaction ID:</td>
				<td align="left" width="32%"><html:input path="transactionCode" id="transactionCodeId" cssClass="textBox" maxlength="50" tabindex="1"
						onkeypress="return maskNumber(this,event)" /></td>
				
				<td class="formText" align="right">STAN:</td>
				<td align="left">
					<html:input path="stan" id="stan" cssClass="textBox" maxlength="15" tabindex="20" onkeypress="return maskNumber(this,event)" />
				</td>
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
				<td class="formText" align="right"></td>
				<td align="left"><input name="_search" type="submit" class="button" value="Search" tabindex="13" /></td>
				<td class="formText" align="right"></td>
				<td align="left"></td>
			</tr>
		</table>
	</html:form>
	<!-- data table result -->
	<ec:table items="resultList" var="model" action="${contextPath}/p_ibftretryadvicelist.html" title="" retrieveRowsCallback="limit"
		filterRowsCallback="limit" sortRowsCallback="limit" filterable="false">
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
			<ec:exportXls fileName="IBFT_Retry_Advice_List_Report.xls" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
			<ec:exportXlsx fileName="IBFT_Retry_Advice_List_Report.xlsx" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
			<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da" headerTitle="View Transactions"
				fileName="IBFT_Retry_Advice_List_Report.pdf" tooltip="Export PDF" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
			<ec:exportCsv fileName="IBFT_Retry_Advice_List_Report.csv" tooltip="Export CSV"></ec:exportCsv>
		</authz:authorize>
		<ec:row>
			<ec:column property="requestTime" title="Request Time" escapeAutoFormat="true" cell="date" format="dd/MM/yyyy hh:mm:ss a" />
			<ec:column property="mobileNo" title="Mobile No" escapeAutoFormat="true" />
			<ec:column property="accountNo" title="Account No" escapeAutoFormat="true" />
			<ec:column property="transactionAmount" title="Amount" escapeAutoFormat="true" cell="currency" format="0.00" style="text-align: right"/>
			<ec:column property="createdOn" title="Transmission Time" escapeAutoFormat="true" cell="date" format="dd/MM/yyyy hh:mm:ss a" />
			<ec:column property="stan" title="Stan" escapeAutoFormat="true" />
			<ec:column property="retrievalReferenceNumber" title="RRN" escapeAutoFormat="true" />
			<ec:column property="transactionCode" title="Transaction ID" escapeAutoFormat="true" />
			<ec:column property="productId" title="Product ID" escapeAutoFormat="true" />
			<ec:column property="status" title="Status" escapeAutoFormat="true" viewsDenied="html"/>
			<ec:column title="Status" escapeAutoFormat="true" alias="" viewsAllowed="html">
				<span id="status_${model.ibftRetryAdviceId}">${model.status}</span>
			</ec:column>
			
			<ec:column title="History" alias="" viewsAllowed="html">
				<fmt:formatDate value="${model.requestTime}" var="formatedDate" type="date" pattern="yyyy-MM-dd HH:mm:ss"/>
				<input id="btnhistory_${model.ibftRetryAdviceId}"  type="button" value="View History" 
				class="button" onclick="openHistoryWindow('p-ibftretryadvicehistory.html?stan=${model.stan}&reqdate=${formatedDate}', 'IBFT Retry Advice History', 500,500);"/>
			</ec:column>
			
			<ec:column alias="" title="" viewsAllowed="html">
				<authz:authorize ifAnyGranted="<%=PortalConstants.IBFT_RETRY_ADVICE_UPDATE%>">
					<c:choose>
						<c:when test="${model.status eq 'Failed'}">
					    	<input type="button" class="button" value="Retry" id="btnretry_${model.ibftRetryAdviceId}"/>
						</c:when>
					    <c:otherwise>
					    	<input type="button" class="button" disabled="disabled" value="Retry" id="btnretry_${model.ibftRetryAdviceId}" />
					    </c:otherwise>
					</c:choose>
					<ajax:updateField baseUrl="${contextPath}/p_ibftretryadviceajaxrequest.html" 
					source="btnretry_${model.ibftRetryAdviceId}" eventType="click"
					target="message" action="btnretry_${model.ibftRetryAdviceId}" 
					parameters="ibftRetryAdviceId=${model.ibftRetryAdviceId}"
					parser="new ResponseXmlParser()" 
					errorFunction="ajaxErrorFunction" 
					postFunction="ajaxPostFunction" />
				</authz:authorize>
			</ec:column>



			<ec:column alias="" title="" viewsAllowed="html">
				<authz:authorize ifAnyGranted="<%=PortalConstants.IBFT_RETRY_ADVICE_UPDATE%>">
					<c:choose>
						<c:when test="${model.status eq 'Failed'}">
							<input type="button" class="button" value="Clear SAF" id="btnclear_${model.ibftRetryAdviceId}"/>
						</c:when>
						<c:otherwise>
							<input type="button" class="button" disabled="disabled" value="Clear SAF" id="btnclear_${model.ibftRetryAdviceId}" />
						</c:otherwise>
					</c:choose>
					<ajax:updateField baseUrl="${contextPath}/p_ibftsafclearadviceajaxrequest.html"
									  source="btnclear_${model.ibftRetryAdviceId}" eventType="click"
									  target="message" action="btnclear_${model.ibftRetryAdviceId}"
									  parameters="ibftRetryAdviceId=${model.ibftRetryAdviceId}"
									  parser="new ResponseXmlParser()"
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