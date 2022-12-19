<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.IssueTypeStatusConstantsInterface"%>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
<%@ page import='com.inov8.microbank.common.util.PortalDateUtils'%>
<%@ page import='com.inov8.microbank.common.util.FinancialInstitutionConstants'%>

<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<c:set var="veriflyFinancialInstitution"><%=FinancialInstitutionConstants.VERIFLY_FINANCIAL_INSTITUTION %></c:set>

<html>
	<head>
<meta name="decorator" content="decorator">
	<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
	<script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
    
	<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
	<link type="text/css" rel="stylesheet" href="styles/ajaxtags.css" />
	<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
	<%@include file="/common/ajax.jsp"%>
	<meta name="title" content="Stakeholder WHT Report" />
    <script language="javascript" type="text/javascript">
		var jq=$.noConflict();
		var serverDate ="<%=PortalDateUtils.getServerDate()%>";
		
      function openTransactionDetailWindow(transactionCode)
	  {
	      var action = 'p_showtransactiondetail.html?actionId='+${retriveAction}+'&transactionCodeId='+transactionCode+'&isMfs=true';
             newWindow = window.open( action , 'TransactionDetail', 'width=550,height=350,menubar=no,toolbar=no,left=150,top=150,directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
             if(window.focus) newWindow.focus();
                   return false;
	  }

      function error(request)
      {
      	alert("An unknown error has occured. Please contact with the administrator for more details");
      }
     
    </script>
	<%
		String chargebackUpdatePermission = PortalConstants.ADMIN_GP_UPDATE;
		chargebackUpdatePermission += "," + PortalConstants.PG_GP_UPDATE;
		chargebackUpdatePermission += "," + PortalConstants.CSR_GP_UPDATE;
		chargebackUpdatePermission += "," + PortalConstants.REQ_CHARGEBACK_UPDATE;
 	%>

	</head>
	<body bgcolor="#ffffff" onUnload="javascript:closeChild();">

		<div id="rsp" class="ajaxMsg"></div>

		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" /><br/>
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>
		
			<html:form name='extendedAllTransactionsForm' commandName="whtStakeholderViewModel" method="post" action="p_whtReportStakeholderWise.html" onsubmit="return validateThisForm(this)" >
				<table width="770px" border="0">
				<tr>
					<td class="formText" align="right">
						<span class="asterisk">*</span>Start Date:
					</td>
					<td align="left">
				        <html:input path="startDate" id="startDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
							<img id="sDate" tabindex="7" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
							<img id="sDate" tabindex="8" title="Clear Date" name="popcal" onclick="javascript:$('startDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
					<td class="formText" align="right">
						<span class="asterisk">*</span>End Date:
					</td>
					<td align="left">
					     <html:input path="endDate" id="endDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
					     <img id="eDate" tabindex="9" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
					     <img id="eDate" tabindex="10" title="Clear Date" name="popcal" onclick="javascript:$('endDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">

					</td>
					<td align="left">
						<input name="_search" type="submit" class="button" value="Search" tabindex="12"/>
						<input name="reset" type="reset"
							onclick="javascript: window.location='p_whtReportStakeholderWise.html?actionId=${retriveAction}'"
							class="button" value="Cancel" tabindex="13" />
					</td>
					<td class="formText" align="right">

					</td>
					<td align="left">&nbsp;</td>
				</tr>
				<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
				</table>
			</html:form>
		

		<ec:table items="whtStakeholderViewModelList" var="whtStakeholderViewModel" action="${contextPath}/p_whtReportStakeholderWise.html"
		title="" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" filterable="false">
		
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="WHT Stakeholder Report.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="WHT Stakeholder Report.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da" headerTitle="WHT Stakeholder Report"
					fileName="WHT Stakeholder Report.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="WHT Stakeholder Report.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>

			<ec:row>
				<ec:column property="paymentSection" title="Payment Section" escapeAutoFormat="True" style="text-align: center"/>
				<ec:column property="taxPayer" title="Tax Payer" escapeAutoFormat="True" style="text-align: center"/>
				<ec:column property="taxableAmount" title="Taxable Amount" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="taxAmount" title="Tax Amount" cell="currency" format="0.00" style="text-align: right"/>
				<!--ec:column property="createdOn" cell="date" format="dd/MM/yyyy hh:mm a" title="Date"/-->

			</ec:row>
		</ec:table>


		<script language="javascript" type="text/javascript">

			function validateThisForm(form){
				var currentDate = "<%=PortalDateUtils.getServerDate()%>";
		        var _fDate = form.startDate.value;
		  		var _tDate = form.endDate.value;
			  	var startlbl = "Start Date";
			  	var endlbl   = "End Date";
		        var isValid = validateDateRangeMandatory(_fDate,_tDate,startlbl,endlbl,currentDate);
		        return isValid;
		    }
			
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

			var childWindow;
			function openChargebackWindow1(btnName, transactionId, transactionCodeId, transactionCode, issueTypeStatusId, winId)
			{
				var popupWidth = 550;
				var popupHeight = 350;
				var popupLeft = (window.screen.width - popupWidth)/2;
				var popupTop = (window.screen.height - popupHeight)/2;
				childWindow = window.open('p-issueupdateform.html?<%=IssueTypeStatusConstantsInterface.MGMT_PAGE_BTN_NAME%>='+btnName+'&transactionId='+transactionId+'&transactionCodeId='+transactionCodeId+'&transactionCode='+transactionCode+'&<%=IssueTypeStatusConstantsInterface.REQUEST_PARAMETER_NAME%>='+issueTypeStatusId,winId,'width='+popupWidth+',height='+popupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+',directories=no,scrollbars=no,resizable=no,status=no');
			}

	       function closeChild()
	       {
	          try
	              {
	              if(childWindow != undefined)
	               {
	                   childWindow.close();
	                   childWindow=undefined;
	               }
			      }catch(e){}
	      }
	      
	      function changeInfo(link) {
				if (confirm('If customer information is verified then press OK to continue.')==true) {
					window.location.href=link;
				}
			}
	      
      	</script>
      	<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>
	</body>
</html>
