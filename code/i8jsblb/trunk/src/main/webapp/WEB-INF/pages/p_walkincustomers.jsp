<%@include file="/common/taglibs.jsp"%>
<%@ page import="com.inov8.microbank.common.util.PortalConstants" %>
<%@ page import="com.inov8.microbank.common.util.PortalDateUtils" %>
<%@ page import="com.inov8.microbank.common.util.UserTypeConstantsInterface" %>
<%@ page import="com.inov8.microbank.common.util.UserUtils" %>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<c:set var="isBank"><%=UserTypeConstantsInterface.BANK.longValue() == UserUtils.getCurrentUser().getAppUserTypeId().longValue()%></c:set>
<html>
	<head>
<meta name="decorator" content="decorator-simple">
	<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
	<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
	<script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
	<meta name="title" content="Search Beneficiary(Walk-in)" />
	<script language="javascript" type="text/javascript">
		var jq=$.noConflict();
		var serverDate ="<%=PortalDateUtils.getServerDate()%>";
	      function error(request)
	      {
	      	alert("An unknown error has occured. Please contact with the administrator for more details");
	      }
        </script>
	<%
		String txHistoryPermission = PortalConstants.ADMIN_GP_READ + "," + PortalConstants.PG_GP_READ + "," +PortalConstants.RPT_SRCH_WALK_IN_TX_HIST_READ;
		String cashPaymentPermission = PortalConstants.ADMIN_GP_READ + "," + PortalConstants.PG_GP_READ + "," + PortalConstants.RPT_SRCH_WALK_IN_CP_READ;
 	%>
	</head>
	<body bgcolor="#ffffff">
		<div id="rsp" class="ajaxMsg"></div>

		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" /><br/>
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>
		
			<html:form name='walkInCustomerViewForm' commandName="extendedWalkInCustomerViewModel" method="post"
				action="p_walkincustomers.html" onsubmit="return validateForm(this)" >
				<table width="750px" border="0">
				<tr>
					<td align="right" class="formText" width="18%">
						CNIC:
					</td>
					<td align="left" width="32%">
						<html:input path="cnic" id="cnic" cssClass="textBox" maxlength="13" tabindex="1" onkeypress="return maskNumber(this,event)"/>
					</td>
					<td align="right" class="formText" width="18%">
						Mobile No.:
					</td>
					<td align="left" width="32%">
						<html:input path="mobileNo" id="mobileNo" cssClass="textBox" maxlength="11" tabindex="2" onkeypress="return maskNumber(this,event)"/>
					</td>
				</tr>
				<tr>
					<td align="right" class="formText">
						Registered From:
					</td>
					<td align="left">
				        <html:input path="startDate" id="startDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
						<img id="sDate" tabindex="3" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
						<img id="sDate" tabindex="4" title="Clear Date" name="popcal" align="middle" onclick="javascript:$('startDate').value=''" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
					<td align="right" class="formText">
						Registered To:
					</td>
					<td align="left">
				        <html:input path="endDate" id="endDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
						<img id="eDate" tabindex="5" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
						<img id="eDate" tabindex="6" title="Clear Date" name="popcal" align="middle" style="cursor:pointer" onclick="javascript:$('endDate').value=''" src="images/refresh.png" border="0" />
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td align="left">
						<input id="search" name="_search" type="submit" class="button" value="Search" tabindex="7" />
						<input name="reset" type="reset"
							onclick="javascript: window.location='p_walkincustomers.html?actionId=${retriveAction}'"
							class="button" value="Cancel" tabindex="8" />
					</td>
					<td colspan="2">&nbsp;</td>
				</tr>
				<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
			</table>
		</html:form>
	

		<ec:table items="walkInCustomerViewModelList" var="walkInCustomerViewModel"
		action="${pageContext.request.contextPath}/p_walkincustomers.html"
		title="" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" filterable="false" width="1100px">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Walk-in-Customers.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Walk-in-Customers.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="Walk-in Customers"
					fileName="Walk-in-Customers.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Walk-in-Customers.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>
				<ec:column property="createdOn" cell="date" format="dd/MM/yyyy hh:mm a" title="Registered On" width="14%"/>
				<ec:column property="registeredBy" title="Registered By Agent ID"/>
				<ec:column property="cnic" title="CNIC" escapeAutoFormat="True" style="text-align: right"/>
				<ec:column property="mobileNo" title="Tagged MSISDN" escapeAutoFormat="True" style="text-align: right"/>

				<ec:column property="transactionsAsSender" title="Transactions As Sender" format="0" calc="total" calcTitle="Total:" style="text-align: right"/>
				<ec:column property="debitLimit" title="Remaning Debit Limit" cell="currency"  format="0.00"  style="text-align: right"/>
				<ec:column property="transactionsAsReceiver" title="Transactions As Receiver" format="0" calc="total" calcTitle="Total:" style="text-align: right"/>
				<ec:column property="creditLimit" title="Remaning Credit Limit" cell="currency"  format="0.00"  style="text-align: right"/>
				<ec:column property="noOfTransactions" title="Total Transactions" format="0" calc="total" calcTitle="Total:" style="text-align: right"/>
				<ec:column property="lastTransactionDate" cell="date" format="dd/MM/yyyy hh:mm a" title="Last Transaction On" width="14%"/>
					<ec:column alias="" title="Transaction History" sortable="false" viewsAllowed="html" style="text-align: center">
					<c:choose>
						<c:when test="${walkInCustomerViewModel.noOfTransactions == 0}">
							<input type="button" class="button" value="Transaction History" name="TransactionHistory${walkInCustomerViewModel.cnic}" disabled="disabled"/>
						</c:when>
						<c:otherwise>
							<authz:authorize ifAnyGranted="<%=txHistoryPermission%>">
								<input type="button" class="button" value="Transaction History" name="TransactionHistory${walkInCustomerViewModel.cnic}"  
								onclick="javascript:document.location='p_walkincustomerltransactiondetails.html?actionId=2&cnic=${walkInCustomerViewModel.cnic}'" />
							</authz:authorize>
							<authz:authorize ifNotGranted="<%=txHistoryPermission%>">
								<input type="button" class="button" value="Transaction History" name="TransactionHistory${walkInCustomerViewModel.cnic}"  
								disabled="disabled"/>
							</authz:authorize>
						</c:otherwise>
					</c:choose>
				</ec:column>
			</ec:row>
		</ec:table>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/searchFormValidator.js"></script>
		<script language="javascript" type="text/javascript">
			document.forms[0].cnic.focus();

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
