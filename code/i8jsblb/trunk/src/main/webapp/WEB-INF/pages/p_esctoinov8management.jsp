<!--Author: Basit Mehr -->
<%@include file="/common/taglibs.jsp"%>
<%@ page
	import='com.inov8.microbank.common.util.*,com.inov8.microbank.common.util.PortalConstants'%>

<c:set var="veriflyFinancialInstitution" value="<%=FinancialInstitutionConstants.VERIFLY_FINANCIAL_INSTITUTION%>"/>
<html>
	<head>
<meta name="decorator" content="decorator">
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
		<link rel="stylesheet" type="text/css"
			href="styles/deliciouslyblue/calendar.css" />
		<link rel="stylesheet"
			href="${pageContext.request.contextPath}/styles/extremecomponents.css"
			type="text/css">
		<%@include file="/common/ajax.jsp"%>
		<meta name="title" content="Escalate to Inov8" />
	</head>

	<body bgcolor="#ffffff">
		<spring:bind path="extendedEscToInov8ViewModel.*">
			<c:if test="${not empty status.errorMessages}">
				<div class="errorMsg">
					<c:forEach var="error" items="${status.errorMessages}">
						<c:out value="${error}" escapeXml="false" />
						<br />
					</c:forEach>
				</div>
			</c:if>
		</spring:bind>

		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" />
					<br />
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>

		<table width="100%">
			<html:form name="escToInov8Form" action="p_esctoinov8management.html"
				onsubmit="return validateForm()" method="post"
				commandName="extendedEscToInov8ViewModel">
				<tr>
					<td width="33%" align="right" class="formText">
						Inov8 MWallet ID:
					</td>
					<td width="17%" align="left">
						<html:input onkeypress="return maskCommon(this,event)"
							tabindex="1" path="mfsId" id="mfsId" cssClass="textBox"
							maxlength="8" />
						${status.errorMessage}
					</td>
					<td width="16%" align="right" class="formText">
						Start Date:
					</td>
					<td width="34%" align="left">
						<html:input readonly="true" path="startDate" tabindex="-1"
							cssClass="textBox" maxlength="10" />
						<img id="sDate" tabindex="2" readonly="true" name="popcal"
							align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
						<img id="sDate" tabindex="3" name="popcal" title="Clear Date"
							align="middle" onclick="javascript:$('startDate').value=''"
							align="middle" style="cursor:pointer" src="images/refresh.png"
							border="0" />
					</td>
				</tr>
				<tr>
					<td align="right" class="formText">
						Issue Code:
					</td>
					<td align="left">
						<html:input onkeypress="return maskCommon(this,event)"
							tabindex="4" path="issueCode" id="issueCode" cssClass="textBox"
							maxlength="50" />
					</td>
					<td align="right" class="formText">
						End Date:
					</td>
					<td align="left">
						<html:input path="endDate" cssClass="textBox" tabindex="-1"
							maxlength="10" readonly="true" />
						<img tabindex="5" id="eDate" readonly="true" name="popcal"
							align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
						<img tabindex="6" id="eDate" name="popcal" title="Clear Date"
							align="middle" onclick="javascript:$('endDate').value=''"
							align="middle" style="cursor:pointer" src="images/refresh.png"
							border="0" />
					</td>
				</tr>

				<tr>
					<td class="formText" align="right">
						Transaction Code:
					</td>
					<td align="left">
						<html:input onkeypress="return maskCommon(this,event)"
							tabindex="7" path="transactionCode" cssClass="textBox"
							maxlength="50" />
						${status.errorMessage}
					</td>
					<td class="formText" align="right"></td>
					<td align="left">
					</td>
				</tr>

				<tr>
					<td class="formText" align="right">
						Supplier:
					</td>
					<td align="left">
						<html:select path="supplierId" cssClass="textBox" tabindex="8">
							<html:option value="">---All---</html:option>
							<c:if test="${supplierModelList != null}">
								<html:options items="${supplierModelList}"
									itemValue="supplierId" itemLabel="name" />
							</c:if>
						</html:select>
						${status.errorMessage}
					</td>
					<td class="formText" align="right"></td>
					<td align="left"></td>
				</tr>


				<tr>
					<td class="formText" align="right">
						Product:
					</td>
					<td align="left">
						<html:select path="productId" cssClass="textBox" tabindex="9">
							<html:option value="">---All---</html:option>
							<c:if test="${productModelList != null}">
								<html:options items="${productModelList}" itemValue="productId"
									itemLabel="name" />
							</c:if>
						</html:select>
						${status.errorMessage}
					</td>
					<td class="formText" align="right"></td>
					<td align="left"></td>
				</tr>

				<tr>
					<td class="formText" align="right">
						Authorization Code:
					</td>
					<td align="left">
						<html:input onkeypress="return maskCommon(this,event)"
							tabindex="10" path="authorizationCode" cssClass="textBox"
							maxlength="50" />
						${status.errorMessage}
					</td>
					<td colspan="2" align="left" class="formText">
						&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
				</tr>

				<tr>
					<td align="left" height="10px">
						&nbsp;
					</td>
					<td>
						
						<authz:authorize ifAnyGranted="<%=PortalConstants.ESC_TO_I8_RPT_READ%>">
							<input tabindex="11" name="_search" type="submit" class="button"
								value="Search" />
						</authz:authorize>
						<authz:authorize ifNotGranted="<%=PortalConstants.ESC_TO_I8_RPT_READ%>">
							<input tabindex="-1" name="_search" type="submit" class="button"
								value="Search" disabled="disabled" />
						</authz:authorize>
						
						<input tabindex="12" name="reset" type="reset" class="button"
							value="Cancel"
							onClick="javascript:window.location='p_esctoinov8management.html';" />
					</td>
					<td></td>
					<td></td>
				</tr>
			</html:form>
		</table>

		<ec:table filterable="false" items="escToInov8ViewModelList"
			var="escToInov8Model" retrieveRowsCallback="limit"
			filterRowsCallback="limit" sortRowsCallback="limit"
			action="${pageContext.request.contextPath}/p_esctoinov8management.html"
			title="">
			
			<authz:authorize ifAnyGranted="<%=PortalConstants.EXP_RPT_PDF_XLS_READ%>">
				<ec:exportXls fileName="Escalate to Inov8.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Escalate to Inov8.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView"
					headerBackgroundColor="#b6c2da" headerTitle="Escalate to Inov8" fileName="Escalate to Inov8.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Escalate to Inov8.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>

			<ec:row>
				<ec:column property="issueCode" title="Issue Code"
					filterable="false" sortable="true">

					<authz:authorize ifAnyGranted="<%=PortalConstants.TX_RPT_READ%>">
						<a href=""
							onclick="return openIssueWindow('${escToInov8Model.issueCode}','${retriveAction}')">
							${escToInov8Model.issueCode} </a>
					</authz:authorize>
					<authz:authorize ifNotGranted="<%=PortalConstants.TX_RPT_READ%>">
				          ${escToInov8Model.issueCode}
					</authz:authorize>
										
				</ec:column>
				<ec:column property="transactionCode" title="Transaction Code"
					filterable="false" sortable="true">

					<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
						<a
							href="p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode=${escToInov8Model.transactionCode}"
							onclick="return openTransactionWindow('${escToInov8Model.transactionCode}','${retriveAction}')">
							${escToInov8Model.transactionCode} </a>
					</authz:authorize>
					<authz:authorize ifNotGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
		 		            ${escToInov8Model.transactionCode}
					</authz:authorize>
					
				</ec:column>
				<ec:column property="mfsId" sortable="true" filterable="false" />
				<ec:column property="accountNick" title="Account Nick"
					filterable="false" />
				<ec:column property="createdOn" title="Date/Time" cell="date"
					parse="yyyy-MM-dd" format="dd/MM/yyyy hh:mm a" filterable="false" />
				<ec:column property="supplierName" title="Supplier"
					filterable="false" />
				<ec:column property="productName" title="Product" filterable="false" />
				<ec:column property="amount" title="Amount" filterable="false"
					calc="total" calcTitle="Total:" cell="currency" format="0.00" />
				<ec:column property="serviceCharges" title="Service Charges"
					filterable="false" calc="total" cell="currency" format="0.00" />
				<ec:column property="bankAccountNoLastFive" title="Account No." filterable="false" />
				<ec:column property="totalAmount" title="Total Amount Charged"
					filterable="false" calc="total" cell="currency" format="0.00" />
				<ec:column property="authorizationCode" title="Authorization Code"
					filterable="false" />
				<ec:column property="processingStatusName" filterable="false"  title="Status" />
				<ec:column property="veriflyStatus" alias=" " title="Verifly Status" style="text-align: center"
					filterable="false" sortable="false" >
				</ec:column>
<%--				<ec:column alias=" " title="Verifly Status" style="text-align: center"--%>
<%--					filterable="false" sortable="false" >--%>
<%--				    --%>
<%--				    <c:if test="${veriflyRequired}">--%>
<%--                        <c:choose>--%>
<%--                           <c:when test="${escToInov8Model.veriflyStatus == veriflyFinancialInstitution}">--%>
<%--                              OK --%>
<%--                           </c:when>--%>
<%--                           <c:otherwise></c:otherwise>--%>
<%--                        </c:choose>--%>
<%--                    </c:if>--%>
<%--				</ec:column>--%>
				<ec:column property="issueStatusName" style="text-align: center"
					title="Inov8 Status" filterable="false" />
			</ec:row>
		</ec:table>
		<ajax:select source="supplierId" target="productId"
			baseUrl="${contextPath}/p_refData.html"
			parameters="supplierId={supplierId},rType=1" />
		<script type="text/javascript">
      highlightFormElements();
      document.forms[0].mfsId.focus();
      
      function openTransactionWindow(transactionCode){
		newWindow=window.open('p_transactionissuehistorymanagement.html?transactionCode='+transactionCode,'TransactionSummary', 'width=550,height=350,menubar=no,toolbar=no,left=150,top=150,directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
		if(window.focus) newWindow.focus();
		return false;
      }
      function openIssueWindow(issueCode){
		newWindow=window.open('p_issuehistorymanagement.html?issueCode='+issueCode,'IssueHistory', 'width=550,height=350,menubar=no,toolbar=no,left=150,top=150,directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
		if(window.focus) newWindow.focus();
		return false;
      }
      
	        function validateForm(){
	        	return validateFormChar(document.forms[0]);
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
