<%@include file="/common/taglibs.jsp"%>
<%@page import='com.inov8.microbank.common.util.PortalConstants'%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<%@page import="com.inov8.microbank.webapp.action.portal.transactiondetaili8module.ReportTypeEnum"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<c:set var="reportTitle"><%=ReportTypeEnum.valueOf( request.getParameter( "reportType" ).toUpperCase() ).getTitle()%></c:set>
<html>
	<head>
<meta name="decorator" content="decorator">
		<script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
		<script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
		<script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
		<script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
		<script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>

		
		<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css" />
		<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
		<meta name="title" content="${reportTitle}" />
		<script language="javascript" type="text/javascript">
		var jq=$.noConflict();
		var serverDate ="<%=PortalDateUtils.getServerDate()%>";
		function error(request)
	      {
	      	alert("An unknown error has occured. Please contact with the administrator for more details");
	      }
        </script>
</head>
<body bgcolor="#ffffff">
	
		<html:form name='transactionDetailI8Form'
			commandName="extendedTransactionDetailPortalListModel" method="post"
			action="p_marketingcustomerretailpayment.html"
			onsubmit="return validateForm(this)">
			<input type='hidden' name='reportType' value='${param.reportType}' />
			<table width="750px" border="0">
			<tr>
				<td align="right" class="formText">Transaction ID:</td>
				<td align="left" colspan="3"><html:input path="transactionCode"
						id="transactionCode" cssClass="textBox" maxlength="12"
						tabindex="1" onkeypress="return maskInteger(this,event)" />
				</td>
			</tr>
			<tr>
				<td class="formText" align="right" width="15%">Customer MSISDN:</td>
				<td align="left" width="32%"><html:input path="saleMobileNo"
						id="saleMobileNo" cssClass="textBox" maxlength="11" tabindex="2"
						onkeypress="return maskNumber(this,event)" />
				</td>
				<td class="formText" align="right" width="20%">Agent MSISDN:</td>
				<td align="left"><html:input path="recipientMobileNo"
						id="recipientMobileNo" cssClass="textBox" maxlength="11"
						tabindex="3" onkeypress="return maskNumber(this,event)" />
				</td>
			</tr>
			<tr>
				<td class="formText" align="right">Start
					Date:</td>
				<td align="left"><html:input path="startDate" id="startDate"
						readonly="true" tabindex="-1" cssClass="textBox" maxlength="10" />
					<img id="sDate" tabindex="4" name="popcal" align="top"
					style="cursor:pointer" src="images/cal.gif" border="0" /> <img
					id="sDate" tabindex="5" title="Clear Date" name="popcal"
					onclick="javascript:$('startDate').value=''" align="middle"
					style="cursor:pointer" src="images/refresh.png" border="0" />
				</td>


				<td class="formText" align="right">End
					Date:</td>
				<td align="left"><html:input path="endDate" id="endDate"
						readonly="true" tabindex="-1" cssClass="textBox" maxlength="10" />
					<img id="eDate" tabindex="6" name="popcal" align="top"
					style="cursor:pointer" src="images/cal.gif" border="0" /> <img
					id="eDate" tabindex="7" title="Clear Date" name="popcal"
					onclick="javascript:$('endDate').value=''" align="middle"
					style="cursor:pointer" src="images/refresh.png" border="0" />
				</td>
			</tr>
			<tr>
				<td class="formText" align="right">&nbsp;</td>
				<td align="left">
					<input name="_search" type="submit" class="button" value="Search" tabindex="8"/>
					<input name="reset" type="reset"
						onclick="javascript: window.location='p_marketingcustomerretailpayment.html?actionId=${retriveAction}&reportType=${param.reportType}'"
						class="button" value="Cancel" tabindex="9" />
				</td>
				<td colspan="2">&nbsp;</td>
			</tr>
			<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
				value="<%=PortalConstants.ACTION_RETRIEVE%>">
		</table>
		</html:form>
	

	<ec:table items="transactionDetailPortalList"
		var="transactionDetailPortalModel"
		action="${contextPath}/p_marketingcustomerretailpayment.html?actionId=${retriveAction}"
		title="" retrieveRowsCallback="limit" filterRowsCallback="limit"
		sortRowsCallback="limit" filterable="false">
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
			<ec:exportXls fileName="${reportTitle}.xls" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
			<ec:exportXlsx fileName="${reportTitle}.xlsx" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
			<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
			headerTitle="${reportTitle}" fileName="${reportTitle}.pdf" tooltip="Export PDF" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
			<ec:exportCsv fileName="${reportTitle}.csv" tooltip="Export CSV"></ec:exportCsv>
		</authz:authorize>
		<ec:row>
			<ec:column property="createdOn" cell="date" format="dd/MM/yyyy"
				title="Date" />
			<ec:column property="createdOn" cell="date" format="hh:mm a"
				sortable="false" alias="Time" width="55px" />
			<ec:column property="transactionCode" title="Transaction ID" escapeAutoFormat="true">
				<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
					<a href="p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode=${transactionDetailPortalModel.transactionCode}" onclick="return openTransactionWindow('${transactionDetailPortalModel.transactionCode}')">
					  ${transactionDetailPortalModel.transactionCode}
					</a>
				</authz:authorize>
				<authz:authorize ifNotGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
					${transactionDetailPortalModel.transactionCode}
				</authz:authorize>
			</ec:column>
			<ec:column property="saleMobileNo" title="Customer MSISDN" escapeAutoFormat="true" 
				style="text-align: center" />
			<ec:column property="recipientMobileNo" title="Agent MSISDN" escapeAutoFormat="true"
				style="text-align: center" />
			<ec:column property="productName" title="Transaction" />
			<ec:column property="agent2Id" title="Merchant ID" escapeAutoFormat="true" />
			<ec:column property="transactionAmount" title="Transactional Amount"
				calc="total" calcTitle="Total:" cell="currency" format="0.00"
				style="text-align: right" />
			<ec:column property="exclusiveCharges" title="Service Charges"
				calc="total" calcTitle="Total:" cell="currency" format="0.00"
				style="text-align: right" />
			<ec:column property="totalAmount" title="Total Amount" calc="total"
				calcTitle="Total:" cell="currency" format="0.00"
				style="text-align: right" />
			<ec:column property="processingStatusName" title="Transaction Status" />
		</ec:row>
	</ec:table>
	<script language="javascript" type="text/javascript">
			document.forms[0].transactionCode.focus();
			function openTransactionWindow(transactionCode)
			{
		        var popupWidth = 550;
				var popupHeight = 350;
				var popupLeft = (window.screen.width - popupWidth)/2;
				var popupTop = (window.screen.height - popupHeight)/2;     
		        newWindow=window.open('p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode='+transactionCode,'TransactionSummary', 'width='+popupWidth+',height='+popupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
			    if(window.focus) newWindow.focus();
			    return false;
			}
	      function validateForm(form){
	        	var currentDate = "<%=PortalDateUtils.getServerDate()%>";
	        	var _fDate = form.startDate.value;
		  		var _tDate = form.endDate.value;
		  		var startlbl = "Start Date";
		  		var endlbl   = "End Date";
	        	var isValid = validateDateRangeMandatory(_fDate,_tDate,startlbl,endlbl,currentDate);
	        	if( isValid )
	        	{
	        		isValid = validateFormChar(form);
	        	}
	        	return isValid;
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
