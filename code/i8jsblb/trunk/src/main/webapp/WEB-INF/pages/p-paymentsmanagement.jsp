
<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.*'%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<html>
	<head>
<meta name="decorator" content="decorator">
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
		<script type="text/javascript" 
			src="${contextPath}/scripts/date-validation.js"></script>
		<link rel="stylesheet" type="text/css"
			href="styles/deliciouslyblue/calendar.css" />
		<link rel="stylesheet"
			href="${pageContext.request.contextPath}/styles/extremecomponents.css"
			type="text/css">
		<%@include file="/common/ajax.jsp"%>

		<meta name="title" content="View Chargebacks" />
	</head>
	<body bgcolor="#ffffff">
		<div id="smsContent" class="ajaxMsg"></div>
		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" />
					<br />
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>
		<table width="85%">
			<html:form name='chargebackForm'
				commandName="extendedChargebackListViewModel" method="post"
				action="p-chargebackmanagement.html">



				<tr>
					<td class="formText" align="right">
						Inov8 MWallet ID
					</td>
					<td align="left">
						<html:input path="userId" id="userId" cssClass="textBox"
							maxlength="10" />
						${status.errorMessage}
					</td>
					<td class="formText" align="right">
						Start Date
					</td>
					<td align="left">
						<html:input path="startDate" id="startDate" cssClass="textBox"
							maxlength="10" />
						&nbsp;
						<button id="sDate" type="button" class="button"
							onClick="javascript:resetExportParameters('p-chargebackmanagement');">
							...
						</button>
						${status.errorMessage}
					</td>
				</tr>

				<tr>
					<td class="formText" align="right">
						Transaction Code
					</td>
					<td align="left">
						<html:input path="transactionCode" id="transactionCode"
							cssClass="textBox" maxlength="10" />
						${status.errorMessage}
					</td>
					<td class="formText" align="right">
						End Date
					</td>
					<td align="left">
						<html:input path="endDate" id="endDate" cssClass="textBox"
							maxlength="10" />
						&nbsp;
						<button id="eDate" type="button" class="button"
							onClick="javascript:resetExportParameters('p-chargebackmanagement');">
							...
						</button>
						${status.errorMessage}
					</td>
				</tr>

				<tr>
					<td class="formText" align="right">
						Supplier
					</td>
					<td align="left">
						<html:select path="supplierId" cssClass="textBox">
							<html:option value="">---All---</html:option>
							<c:if test="${supplierModelList != null}">
								<html:options items="${supplierModelList}"
									itemValue="supplierId" itemLabel="name" />
							</c:if>
						</html:select>

					</td>
					<td class="formText" align="right"></td>
					<td align="left"></td>
				</tr>

				<tr>
					<td class="formText" align="right">
						Product
					</td>
					<td align="left">

						<html:select path="productId" cssClass="textBox">
							<html:option value="">---All---</html:option>
							<c:if test="${productModelList != null}">
								<html:options items="${productModelList}" itemValue="productId"
									itemLabel="name" />
							</c:if>
						</html:select>
					</td>
					<td class="formText" align="right"></td>
					<td align="left"></td>
				</tr>

				<tr>

					<td class="formText" align="right">
						Authorization Code
					</td>
					<td align="left">
						<html:input path="authorizationCode" id="bankResponseCode"
							cssClass="textBox" maxlength="10" />
						${status.errorMessage}
					</td>

				</tr>


				<tr>
					<td colspan="2" align="left" class="formText">
						&nbsp;&nbsp;&nbsp;&nbsp;
						<input name="submit" type="submit" class="button"
							onClick="javascript:resetExportParameters('p-chargebackmanagement');"
							value="Search" />
						<input name="reset" type="reset" class="button" value="Cancel"
							onClick="javascript:resetExportParameters('p-chargebackmanagement');" />
					</td>

				</tr>
			</html:form>
		</table>

		<c:set var="RIFC"
			value="<%=IssueTypeStatusConstantsInterface.CHARGEBACK_RIFC%>"
			scope="page" />
		<c:set var="bogus"
			value="<%=IssueTypeStatusConstantsInterface.CHARGEBACK_INVALID%>"
			scope="page" />
		<c:set var="escalate"
			value="<%=IssueTypeStatusConstantsInterface.CHARGEBACK_OPEN%>"
			scope="page" />
		<c:set var="open"
			value="<%=IssueTypeStatusConstantsInterface.CHARGEBACK_NEW%>"
			scope="page" />

		<ec:table items="transactionDetailModelList"
			var="transactionDetailModel"
			action="${pageContext.request.contextPath}/p-transactiondetailmanagement.html"
			title="" retrieveRowsCallback="limit" filterRowsCallback="limit"
			sortRowsCallback="limit">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="View Chargebacks.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="View Chargebacks.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="View Chargebacks" fileName="View Chargebacks.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="View Chargebacks.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>
				<ec:column property="transactionCode" filterable="false"
					alias="Transaction Code" escapeAutoFormat="true" />
				<ec:column property="userId" filterable="false" alias="Inov8 MWallet ID" />
				<ec:column property="accountNick" filterable="false"
					alias="Account Nick" />
				<ec:column property="paymentMode" filterable="false"
					alias="Payment Mode" />
				<ec:column property="createdOn" cell="date" format="dd/MM/yyyy"
					filterable="false" alias="Date" />
				<ec:column property="supplier" filterable="false" alias="Supplier" />
				<ec:column property="product" filterable="false" alias="Product" />
				<ec:column property="amount" filterable="false" alias="Amount" />
				<ec:column property="serviceCharges" filterable="false"
					alias="service Charges" />
				<ec:column property="totalAmountCharged" filterable="false"
					alias="Total Amount Charged" />
				<ec:column property="authorizationCode" filterable="false"
					alias="Authorization Code" />
				<ec:column title="Verifly Status" filterable="false"
					alias="Authorization Code">
          OK
          </ec:column>
				<ec:column property="issue" title="Resolve In Favour Of Customer"
					filterable="false" sortable="false" viewsDenied="xls">
					<c:if test="${transactionDetailModel.issueTypeStatusId == open}">
						<input type="button" class="button" value="Resolve"
							name="ChargebackRIFC${transactionDetailModel.transactionId}"
							onclick="window.open('p-issueupdateform.html?<%=IssueTypeStatusConstantsInterface.MGMT_PAGE_BTN_NAME%>=ChargebackRIFC${transactionDetailModel.transactionId}&transactionId=${transactionDetailModel.transactionId}&transactionCodeId=${transactionDetailModel.transactionCodeId}&issueId=${transactionDetailModel.issueId}&<%=IssueTypeStatusConstantsInterface.REQUEST_PARAMETER_NAME%>=<%=IssueTypeStatusConstantsInterface.CHARGEBACK_RIFC%>','Chargeback','width=400,height=350,menubar=no,toolbar=no,left=150,top=150,directories=no,status=yes,scrollbars=no,resizable=yes,status=no');">
					</c:if>
					<c:if test="${transactionDetailModel.issueTypeStatusId == RIFC}">YES</c:if>&nbsp;
       </ec:column>

				<ec:column property="issue" title="Resolve In Favour Of Merchant"
					filterable="false" sortable="false" viewsDenied="xls">
					<c:if test="${transactionDetailModel.issueTypeStatusId == open}">
						<input type="button" class="button" value="Resolve"
							name="ChargebackRIFM${transactionDetailModel.transactionId}"
							onclick="window.open('p-issueupdateform.html?<%=IssueTypeStatusConstantsInterface.MGMT_PAGE_BTN_NAME%>=ChargebackRIFM${transactionDetailModel.transactionId}&transactionId=${transactionDetailModel.transactionId}&transactionCodeId=${transactionDetailModel.transactionCodeId}&issueId=${transactionDetailModel.issueId}&<%=IssueTypeStatusConstantsInterface.REQUEST_PARAMETER_NAME%>=<%=IssueTypeStatusConstantsInterface.CHARGEBACK_INVALID%>','Chargeback','width=400,height=350,menubar=no,toolbar=no,left=150,top=150,directories=no,status=yes,scrollbars=no,resizable=yes,status=no');">
					</c:if>
					<c:if test="${transactionDetailModel.issueTypeStatusId == bogus}">YES</c:if>&nbsp;
       </ec:column>

				<ec:column property="issue" title="Escalate To Inov8"
					filterable="false" sortable="false" viewsDenied="xls">
					<c:if test="${transactionDetailModel.issueTypeStatusId == open}">
						<input type="button" class="button" value="Escalate"
							name="ChargebackETOI8${transactionDetailModel.transactionId}"
							onclick="window.open('p-issueupdateform.html?<%=IssueTypeStatusConstantsInterface.MGMT_PAGE_BTN_NAME%>=ChargebackETOI8${transactionDetailModel.transactionId}&transactionId=${transactionDetailModel.transactionId}&transactionCodeId=${transactionDetailModel.transactionCodeId}&issueId=${transactionDetailModel.issueId}&<%=IssueTypeStatusConstantsInterface.REQUEST_PARAMETER_NAME%>=<%=IssueTypeStatusConstantsInterface.CHARGEBACK_OPEN%>','Chargeback','width=400,height=350,menubar=no,toolbar=no,left=150,top=150,directories=no,status=yes,scrollbars=no,resizable=yes,status=no');">
					</c:if>
					<c:if
						test="${transactionDetailModel.issueTypeStatusId == escalate}">YES</c:if>&nbsp;
       </ec:column>



			</ec:row>
		</ec:table>
		<ajax:select source="supplierId" target="productId"
			baseUrl="${contextPath}/p_refData.html"
			parameters="supplierId={supplierId},actionId=${retriveAction}" />
		<script type="text/javascript">
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
