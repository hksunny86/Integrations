<!--Title: i8Microbank-->
<jsp:directive.page import="com.inov8.microbank.common.util.PortalConstants" />
<!--Description: Backened Application for POS terminal-->
<!--Author: Mohammad Shehzad Ashraf-->
<%@include file="/common/taglibs.jsp"%>
<c:set var="retriveAction">
	<%=PortalConstants.ACTION_RETRIEVE%>
</c:set>

<html>

	<head>
<meta name="decorator" content="decorator">
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/prototype.js"></script>
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
		<meta name="title" content="Escalated to Inov8" />

	</head>

	<body bgcolor="#ffffff">
		<!--
        <div align="right"><a href="productform.html" class="linktext">Add Product</a>&nbsp;&nbsp;</div>
	   -->

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
			<html:form name="escToInov8ProductListViewForm"
				onsubmit="return validateForm()"
				commandName="extendedEscToInov8ProductListViewModel"
				action="p_prescalatetoinov8.html">
				<tr>
					<td width="33%" align="right" class="formText">
						Inov8 MWallet ID:
					</td>
					<td width="17%" align="left">
						<html:input onkeypress="return maskCommon(this,event)"
							path="mfsId" cssClass="textBox" tabindex="1" maxlength="8" />
					</td>
					<td width="16%" align="right" class="formText">
						Mobile #:
					</td>
					<td width="34%" align="left">
						<html:input onkeypress="return maskCommon(this,event)"
							path="currentMobileNo" cssClass="textBox" tabindex="2"
							maxlength="11" />
					</td>
				</tr>

				<tr>
					<td align="right" class="formText">
						Issue Code:
					</td>
					<td align="left">
						<html:input onkeypress="return maskCommon(this,event)"
							path="issueCode" cssClass="textBox" maxlength="50" tabindex="3" />
					</td>
					<td align="right" class="formText">
						Start Date:
					</td>
					<td align="left">
						<html:input path="startDate" tabindex="-1" readonly="true"
							cssClass="textBox" />
						<img id="sDate" tabindex="4" name="popcal" align="top"
							style="cursor:pointer" src="images/cal.gif" border="0" />
						<img id="sDate" tabindex="5" name="popcal" title="Clear Date"
							onclick="javascript:$('startDate').value=''" align="middle"
							style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr>

				<tr>
					<td align="right" class="formText">
						Transaction Code:
					</td>
					<td align="left">
						<html:input onkeypress="return maskCommon(this,event)"
							path="transactionCode" cssClass="textBox" tabindex="6"
							maxlength="50" />
					</td>
					<td align="right" class="formText">
						End Date:
					</td>
					<td align="left">
						<html:input path="endDate" tabindex="-1" readonly="true"
							cssClass="textBox" />
						<img id="eDate" tabindex="7" name="popcal" align="top"
							style="cursor:pointer" src="images/cal.gif" border="0" />
						<img id="eDate" tabindex="8" name="popcal" title="Clear Date"
							onclick="javascript:$('endDate').value=''" align="middle"
							style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr>
				<tr>
					<td align="right" class="formText">
						Product:
					</td>
					<td align="left">

						<html:select path="productId" cssClass="textBox" tabindex="9">
							<html:option value="">---All---</html:option>
							<c:if test="${custDisputeProductListViewModelList!=null}">
								<html:options items="${custDisputeProductListViewModelList}"
									itemLabel="productName" itemValue="productId" />
							</c:if>
						</html:select>


					</td>
					<td align="right" class="formText">
						&nbsp;
					</td>
					<td align="left">
						&nbsp;
					</td>
				</tr>


				<tr>
					<td align="right" class="formText">
						Bank:
					</td>
					<td align="left">

						<html:select path="bankId" cssClass="textBox" tabindex="9">
							<html:option value="">---All---</html:option>
							<c:if test="${bankModelList!=null}">
								<html:options items="${bankModelList}" itemLabel="name"
									itemValue="bankId" />
							</c:if>
						</html:select>
					</td>
					<td align="right" class="formText">
						&nbsp;
					</td>
					<td align="left">
						&nbsp;
					</td>
				</tr>


				<tr>
					<td align="right" class="formText">
						&nbsp;
					</td>

					<td align="left" class="formText">
						<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
							value="<%=PortalConstants.ACTION_RETRIEVE%>" />
						<security:isauthorized
							action="<%=PortalConstants.ACTION_RETRIEVE%>">
							<jsp:attribute name="ifAccessAllowed">
								<input type="submit" tabindex="10" class="button" value="Search"
									name="_search" />
							</jsp:attribute>
							<jsp:attribute name="ifAccessNotAllowed">
								<input type="submit" tabindex="-1" class="button" value="Search"
									name="_search" disabled="disabled" />

							</jsp:attribute>
						</security:isauthorized>
						<input type="button" tabindex="11" class="button" value="Cancel"
							name="cancel"
							onClick="javascript: window.location='p_prescalatetoinov8.html?actionId=${retriveAction}';" />
					</td>
					<td align="left" class="formText">
						&nbsp;
					</td>
					<td align="left" class="formText">
						&nbsp;
					</td>
				</tr>
			</html:form>
		</table>


		<ec:table filterable="false"
			items="escToInov8ProductListViewModelList"
			var="escToInov8ProductListViewModel" retrieveRowsCallback="limit"
			filterRowsCallback="limit" sortRowsCallback="limit"
			action="${pageContext.request.contextPath}/p_prescalatetoinov8.html?actionId=${retriveAction}"
			title="">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Escalated to Inov8.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Escalated to Inov8.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView"
					headerBackgroundColor="#b6c2da" headerTitle="Escalated to Inov8" fileName="Escalated to Inov8.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Escalated to Inov8.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>
				<ec:column filterable="false" title="Issue Code"
					property="issueCode">
					<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
						value="<%=PortalConstants.ACTION_RETRIEVE%>" />
					<security:isauthorized
						action="<%=PortalConstants.ACTION_RETRIEVE%>">
						<jsp:attribute name="ifAccessAllowed">
							<a href="p_issuehistorymanagement.html"
								onclick="return openIssueWindow('${escToInov8ProductListViewModel.issueCode}')">
								<c:out value="${escToInov8ProductListViewModel.issueCode}"></c:out>
							</a>
						</jsp:attribute>
						<jsp:attribute name="ifAccessNotAllowed">
							<c:out value="${escToInov8ProductListViewModel.issueCode}"></c:out>
						</jsp:attribute>
					</security:isauthorized>
				</ec:column>
				<ec:column title="Transaction Code" property="transactionCode" escapeAutoFormat="true"
					filterable="false">

					<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
							<a href="p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode=${escToInov8ProductListViewModel.transactionCode}"
								onclick="return openTransactionWindow('${escToInov8ProductListViewModel.transactionCode}')">
								<c:out value="${escToInov8ProductListViewModel.transactionCode}"></c:out>
							</a>
					</authz:authorize>
					<authz:authorize ifNotGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
							<c:out value="${escToInov8ProductListViewModel.transactionCode}"></c:out>
					</authz:authorize>
				</ec:column>
				<ec:column filterable="false" property="mfsId" title="Inov8 MWallet ID" />
				<ec:column filterable="false" property="saleMobileNo" escapeAutoFormat="true"
					title="Subscriber Number" />
				<ec:column filterable="false" property="notificationMobileNo" escapeAutoFormat="true"
					title="Sale Number" />
				<ec:column property="createdOn" filterable="false" cell="date"
					format="dd/MM/yyyy hh:mm a" title="Date" />
				<ec:column filterable="false" property="productName" title="Product" />
				<ec:column property="bankName" filterable="false" title="Bank" />
				<ec:column property="bankAccountNoLastFive" filterable="false" title="Account No."  escapeAutoFormat="true"/>
				<ec:column property="amount" calc="total" calcTitle="Total:"
					cell="currency" format="0.00" filterable="false" title="Amount" />
				<ec:column property="processingStatusName" filterable="false"
					title="Status" />
				<ec:column filterable="false" property="issueStatusName"
					title="Inov8 Status" style="text-align: center" />
			</ec:row>
		</ec:table>
		<script language="javascript" type="text/javascript">
			    highlightFormElements();
                document.forms[0].mfsId.focus();
                
				function openTransactionWindow(transactionCode){
				    newWindow=window.open('p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode='+transactionCode,'TransactionSummary', 'width=550,height=350,menubar=no,toolbar=no,left=150,top=150,directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
				    if(window.focus) newWindow.focus();
				    return false;
				}
				function openIssueWindow(issueCode){
				    newWindow=window.open('p_issuehistorymanagement.html?actionId=${retriveAction}&issueCode='+issueCode,'IssueHistory', 'width=550,height=350,menubar=no,toolbar=no,left=150,top=150,directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
				    if(window.focus) newWindow.focus();
				    return false;
				}
				
				function confirmUpdateStatus(link)
				{
				    if (confirm('Are you sure you want to update status?')==true)
				    {
				      window.location.href=link;
				    }
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
        button      : "eDate" ,   // id of the button
		isEndDate: true
	  }
      );				
				
	</script>
	</body>
</html>
