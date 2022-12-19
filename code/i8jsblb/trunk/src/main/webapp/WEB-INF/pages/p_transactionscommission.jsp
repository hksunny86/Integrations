<%@ page import='com.inov8.microbank.common.util.*'%>
<%@page import="com.inov8.microbank.common.util.PortalConstants"%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<%@include file="/common/taglibs.jsp"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
<meta name="decorator" content="decorator">
		<script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
        <script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
        <%@include file="/common/ajax.jsp"%>
		<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
		<link rel="stylesheet" type="text/css" href="${contextPath}/styles/extremecomponents.css"/>
		<script language="javascript" type="text/javascript">
			var jq=$.noConflict();
			var serverDate ="<%=PortalDateUtils.getServerDate()%>";
			var username = "<%=UserUtils.getCurrentUser().getUsername()%>";
        	var appUserId= "<%=UserUtils.getCurrentUser().getAppUserId()%>";
        	var email= "<%=UserUtils.getCurrentUser().getEmail()%>";
	      function error(request)
	      {
	      	alert("An unknown error has occured. Please contact with the administrator for more details");
	      }
        </script>
        <%@include file="/WEB-INF/pages/export_zip.jsp"%>
		<%-- <script type="text/javascript" src="${contextPath}/scripts/exportzip.js"></script> --%>
		<meta name="title" content="Commission Reconciliation"   id="<%=ReportIDTitleEnum.CommissionReconciliation.getId()%>" />
	</head>
	<body bgcolor="#ffffff">
		<html:form name="commissionTransactionViewForm" commandName="commissionTransactionViewModel" onsubmit="return validateForm(this)" action="p_transactionscommission.html">
			<table width="800px">
				<tr>
					<td align="right" class="formText" width="22%">Transaction ID:</td>
					<td width="30%">
						<html:input path="transactionId" id="transactionId" cssClass="textBox" maxlength="12" tabindex="1" onkeypress="return maskInteger(this,event)"/>
					</td>
					<td class="formText" align="right" width="18%">
						Commission Stakeholder:
					</td>
					<td align="left" width="30%">
						<html:select id="commissionStakeholderId" path="commissionStakeholderId" cssClass="textBox" tabindex="2">
							<html:option value="">---All---</html:option>
							<html:options items="${commissionStakeholderModelList}" itemLabel="name" itemValue="commissionStakeholderId"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<td align="right" class="formText">
						Supplier:
					</td>
					<td align="left">
						<html:select path="supplierId" cssClass="textBox" tabindex="3">
							<html:option value="">---All---</html:option>
							<c:if test="${supplierModelList != null}">
								<html:options items="${supplierModelList}" itemValue="supplierId" itemLabel="name"/>
							</c:if>
						</html:select>
					</td>
					<td align="right" class="formText">
						Product:
					</td>
					<td align="left">
						<html:select path="productId" cssClass="textBox" tabindex="4">
							<html:option value="">---All---</html:option>
							<c:if test="${productModelList != null}">
								<html:options items="${productModelList}" itemValue="productId" itemLabel="name"/>
							</c:if>
						</html:select>
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">
						Start Date:
					</td>
					<td align="left">
				        <html:input path="startDate" id="startDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
						<img id="sDate" tabindex="5" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
						<img id="sDate" tabindex="6" title="Clear Date" name="popcal" onclick="javascript:$('startDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
					<td class="formText" align="right">
						End Date:
					</td>
					<td align="left">
					     <html:input path="endDate" id="endDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
					     <img id="eDate" tabindex="7" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
					     <img id="eDate" tabindex="8" title="Clear Date" name="popcal" onclick="javascript:$('endDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td align="left" class="formText">
						<input type="submit" class="button" value="Search" name="_search" tabindex="9"/>
						<input type="reset" class="button" value="Cancel" name="_reset" onclick="javascript: window.location='p_transactionscommission.html'" tabindex="10"/>
					</td>
					<td colspan="2">&nbsp;</td>
				</tr>
			</table>
		</html:form>

		<ec:table filterable="false" items="commissionTransactionViewModelList" var="commissionTransactionViewModel" retrieveRowsCallback="limit" filterRowsCallback="limit"
		sortRowsCallback="limit" action="${contextPath}/p_transactionscommission.html">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Commission Reconciliation.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Commission Reconciliation.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="Commission Reconciliation" fileName="Commission Reconciliation.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Commission Reconciliation.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>
				<ec:column property="transactionId" title="Transaction ID" escapeAutoFormat="true">
				<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
					<a href="p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode=${commissionTransactionViewModel.transactionId}" onclick="return openTransactionWindow('${commissionTransactionViewModel.transactionId}')">
					  ${commissionTransactionViewModel.transactionId}
					</a>
				</authz:authorize>
				<authz:authorize ifNotGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
					${commissionTransactionViewModel.transactionId}
				</authz:authorize>
				</ec:column>
				<ec:column property="createdOn" cell="date" format="dd/MM/yyyy hh:mm a" title="Transaction Date"/>
				<ec:column property="commissionStakehoder" escapeCommas="true"/>
				<ec:column property="product" title="Transaction"/>
				<ec:column property="commissionAmount" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
			</ec:row>
		</ec:table>

		<ajax:select source="supplierId" target="productId" baseUrl="${contextPath}/p_refData.html"
		parameters="supplierId={supplierId},rType=1,actionId=${retriveAction}" errorFunction="error"/>

		<script language="javascript" type="text/javascript">
	        document.forms[0].transactionId.focus();

			function openTransactionWindow(transactionId)
			{
				var popupWidth = 550;
				var popupHeight = 350;
				var popupLeft = (window.screen.width - popupWidth)/2;
				var popupTop = (window.screen.height - popupHeight)/2;
		        newWindow=window.open('p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode='+transactionId,'TransactionIssueHistory', 'width='+popupWidth+',height='+popupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
			    if(window.focus) newWindow.focus();
			    return false;
			}

      		Calendar.setup(
      		{
		       inputField  : "startDate", // id of the input field
		       button      : "sDate"    // id of the button
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
