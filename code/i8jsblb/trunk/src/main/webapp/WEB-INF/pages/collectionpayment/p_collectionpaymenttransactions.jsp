<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>

<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<html>
	<head>
<meta name="decorator" content="decorator">
	<script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/date-validation.js"></script>
	<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
	<link type="text/css" rel="stylesheet" href="styles/ajaxtags.css" />
	<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
	<%@include file="/common/ajax.jsp"%>
	<meta name="title" content="Collection Payment Transactions" />
   
	</head>
	<body bgcolor="#ffffff">
		<table width="750px" border="0">
			<html:form name='collectionPaymentsViewForm' commandName="collectionPaymentsViewModel" method="post"
				action="p_collectionpaymenttransactions.html" onsubmit="return validateForm(this)" >
				<tr>
					<td class="formText" align="right">
						Product:
					</td>
					<td align="left" colspan="3">
						<html:select path="productId" id="productId" cssClass="textBox" tabindex="1">
							<html:option value="">---All---</html:option>
							<c:if test="${productModelList != null}">
								<html:options items="${productModelList}" itemValue="productId" itemLabel="name"/>
							</c:if>
						</html:select>
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">
						Transaction ID:
					</td>
					<td align="left">
						<html:input path="transactionCode" id="transactionCode" cssClass="textBox" maxlength="20" tabindex="2" onkeypress="return maskInteger(this,event)"/>
					</td>
					<td class="formText" align="right">
						Order ID:
					</td>
					<td align="left" >
						<html:input path="consumerNo" id="consumerNo" cssClass="textBox" maxlength="20" tabindex="3" onkeypress="return maskAlphaNumeric(this,event)"/>
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">
						Customer/Agent ID:
					</td>
					<td align="left">
						<html:input path="mfsId" id="mfsId" cssClass="textBox" maxlength="20" tabindex="4" onkeypress="return maskInteger(this,event)"/>
					</td>
					<td class="formText" align="right">
						Customer/Agent Mobile No.:
					</td>
					<td align="left" >
						<html:input path="saleMobileNo" id="saleMobileNo" cssClass="textBox" maxlength="11" tabindex="5" onkeypress="return maskInteger(this,event)"/>
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">
						Walkin Depositor Mobile No.:
					</td>
					<td align="left">
						<html:input path="walkinMobileNo" id="walkinMobileNo" cssClass="textBox" tabindex="6" maxlength="50" onkeypress="return maskInteger(this,event)"/>
					</td>
					<td class="formText" align="right">
						Transaction Status:
					</td>
					<td align="left">
						<html:select path="supProcessingStatusId" cssClass="textBox" tabindex="7">
							<html:option value="">---All---</html:option>
							<c:if test="${supplierProcessingStatusModelList != null}">
								<html:options items="${supplierProcessingStatusModelList}" itemValue="supProcessingStatusId" itemLabel="name"/>
							</c:if>
						</html:select>
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">
						Start Date:
					</td>
					<td align="left">
				        <html:input path="dateRangeHolderModel.fromDate" id="startDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
						<img id="sDate" tabindex="8" name="popcal" align="top" style="cursor:pointer" src="${contextPath}/images/cal.gif" border="0" />
						<img id="sDate" tabindex="9" title="Clear Date" name="popcal" onclick="javascript:$('startDate').value=''" align="middle" style="cursor:pointer" src="${contextPath}/images/refresh.png" border="0"/>
					</td>
					<td class="formText" align="right">
						End Date:
					</td>
					<td align="left">
					     <html:input path="dateRangeHolderModel.toDate" id="endDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
					     <img id="eDate" tabindex="10" name="popcal" align="top" style="cursor:pointer" src="${contextPath}/images/cal.gif" border="0"/>
					     <img id="eDate" tabindex="11" title="Clear Date" name="popcal" onclick="javascript:$('endDate').value=''" align="middle" style="cursor:pointer" src="${contextPath}/images/refresh.png" border="0"/>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td align="left">
						<input name="_search" type="submit" class="button" value="Search" tabindex="12"/>
					<input name="reset" type="reset"
						onclick="javascript: window.location='p_collectionpaymenttransactions.html?actionId=${retriveAction}'"
						class="button" value="Cancel" tabindex="13"/>
					</td>
					<td colspan="2">
					&nbsp;
					</td>
				</tr>
				<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
			</html:form>
		</table>

		<ec:table items="collectionPaymentsViewModelList" action="${contextPath}/p_collectionpaymenttransactions.html"
		title="" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" sortable="true" filterable="false">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Collection Payment Transactions.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Collection Payment Transactions.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Collection Payment Transactions" fileName="Collection Payment Transactions.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Collection Payment Transactions.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>
				<ec:column property="createdOn" cell="date" format="dd/MM/yyyy" title="Date"/>
				<ec:column property="createdOn" cell="date" alias="Time" format="hh:mm a" title="Time" sortable="false"/>
				<ec:column property="productName" title="Product"/>
				<ec:column property="transactionCode" title="Transaction ID" escapeAutoFormat="true"/>
				<ec:column property="consumerNo" title="Order ID" escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="mfsId" title="Customer/Agent ID" escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="saleMobileNo" title="Customer/Agent Mobile No." escapeAutoFormat="T=true" style="text-align: center"/>
				<ec:column property="walkinMobileNo" title="Walkin Depositor Mobile No." escapeAutoFormat="true" style="text-align: center"/>
				<ec:column property="transactionAmount" title="Transaction Amount" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="serviceChargesInclusive" title="Inclusive Charges" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="serviceChargesExclusive" title="Exclusive Charges" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
 			    <ec:column property="totalAmount" title="Total Amount" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="supplierProcessingStatus" title="Transaction Status"/>
			</ec:row>
		</ec:table>

		<script language="javascript" type="text/javascript">

			document.forms[0].productId.focus();

	        function validateForm(form){
	        	var currentDate = "<%=PortalDateUtils.getServerDate()%>";
	        	var _fDate = form.startDate.value;
		  		var _tDate = form.endDate.value;
		  		var startlbl = "Start Date";
		  		var endlbl   = "End Date";
	        	var isValid = validateDateRange(_fDate,_tDate,startlbl,endlbl,currentDate);
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
	</body>
</html>
