
<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.*,com.inov8.microbank.common.util.PortalConstants'%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<c:set var="defaultAction"><%=PortalConstants.ACTION_DEFAULT %></c:set>


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
		<script type="text/javascript" 
			src="${pageContext.request.contextPath}/scripts/date-validation.js"></script>	
		<link rel="stylesheet" type="text/css"
			href="styles/deliciouslyblue/calendar.css" />
		<link rel="stylesheet"
			href="${pageContext.request.contextPath}/styles/extremecomponents.css"
			type="text/css">

		<%@include file="/common/ajax.jsp"%>
		<meta name="title" content="Regenerate Transaction Code" />


	<%
		String resendTxCodePermission = PortalConstants.ADMIN_GP_UPDATE + "," + PortalConstants.PG_GP_UPDATE+ "," + PortalConstants.CSR_GP_UPDATE + "," + PortalConstants.REGENERATE_TX_CODE_UPDATE;
		String resendTxCodePermissionReadOnly = PortalConstants.REGENERATE_TX_CODE_READ;
		String resetTxCodePermission =  PortalConstants.ADMIN_GP_UPDATE + "," + PortalConstants.PG_GP_UPDATE+ "," + PortalConstants.CSR_GP_UPDATE + "," + PortalConstants.RESET_TX_CODE_UPDATE;
	 %>
		
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
		<table width="750px" border="0">
			<html:form name='miniTransactionViewModelForm'
				commandName="miniTransactionViewModel" method="post"
				action="p_cashwithdrawaltransdetails.html" onsubmit="return validateForm(this)">
				<tr>
					<%-- <td class="formText" align="right" width="18%">
						Agent ID:
					</td>
					<td align="left">
						<html:input path="agentUserId" id="agentUserId"  cssClass="textBox" maxlength="11" tabindex="1" onkeypress="return maskNumber(this,event)"/> 
					</td> --%>
					<td class="formText" align="right"  width="18%">
						Transaction ID:
					</td>
					<td align="left">
						<html:input path="transactionCode" id="transactionCode" cssClass="textBox" tabindex="3" maxlength="50" onkeypress="return maskNumber(this,event)"/> 
					</td>
					<%-- <td class="formText" align="right">
						Customer Mobile No:
					</td>
					<td align="left" >
						<html:input path="customerMobileNo" id="customerMobileNo" cssClass="textBox" maxlength="11" tabindex="2" onkeypress="return maskNumber(this,event)"/> 
					</td> --%>
				</tr>		
				<%--<tr>
					
					<td class="formText" align="right">
						Status:
					</td>
					<td align="left">
						<html:select path="miniTransactionState" cssClass="textBox" tabindex="4" >
							<html:option value="">---All---</html:option>
							<c:if test="${miniTransactionStateList != null}">
								<html:options items="${miniTransactionStateList}" itemValue="name" itemLabel="name"/>
							</c:if>
						</html:select>
					</td> 
				</tr>--%>
				<tr>
				<td class="formText" align="right">
						Sender Mobile No.:
					</td>
					<td align="left">
						<html:input path="senderMobileNo" id="senderMobileNo" cssClass="textBox" tabindex="2" maxlength="11" onkeypress="return maskNumber(this,event)"/> 
					</td>
				
				<td class="formText" align="right">
						Sender CNIC:
					</td>
					<td align="left">
						<html:input path="senderCNIC" id="senderCNIC" cssClass="textBox" tabindex="3" maxlength="13" onkeypress="return maskNumber(this,event)"/> 
					</td>
				</tr>
				<tr>
				<td class="formText" align="right">
						Transaction Status:
					</td>
					<td align="left">
				        <html:select path="transactionStatusId" cssClass="textBox" tabindex="4" >
							<html:option value="">---All---</html:option>
							<html:option value="4">In-process</html:option>
							<html:option value="5">Reversed</html:option>
						</html:select>
					</td>
					<%-- <td class="formText" align="right">
						Start Date:
					</td>
					<td align="left">
				        <html:input path="dateRangeHolderModel.fromDate" id="startDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="7"/>
							<img id="sDate" tabindex="7" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
							<img id="sDate" tabindex="8" title="Clear Date" name="popcal"  onclick="javascript:$('startDate').value=''"   align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
					<td class="formText" align="right">
						End Date:
					</td>
					<td align="left">
					     <html:input path="dateRangeHolderModel.toDate" id="endDate"  readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
					     <img id="eDate" tabindex="9" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
					     <img id="eDate" tabindex="10" title="Clear Date" name="popcal"  onclick="javascript:$('endDate').value=''"   align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td> --%>
				</tr>
				<tr>
					<td class="formText" align="right">

					</td>
					<td align="left">
						<input name="_search" type="submit" class="button" value="Search" tabindex="11"  /> 
						<input name="reset" type="reset" 
						onclick="javascript: window.location='p_cashwithdrawaltransdetails.html?actionId=${retriveAction}&customerCnic=${param.customerCnic}&appUserId=${param.appUserId}&registered=${param.registered}&referrer=${param.referrer}'"
						class="button" value="Cancel" tabindex="12" />
					</td>
					<td class="formText" align="right">

					</td>
					<td align="left">
					</td>
				</tr>

				<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
					value="<%=PortalConstants.ACTION_RETRIEVE%>">
				<input type="hidden" name="customerCnic" value="${param.customerCnic}">
				<input type="hidden" name="appUserId" value="${param.appUserId}">
				<input type="hidden" name="registered" value="${param.registered}">
				<input type="hidden" name="home" value="${param.home}">
				<input type="hidden" name="referrer" value="${param.referrer}"/>
			</html:form>
		</table>
		
		<ec:table filterable="false" items="miniTransactionViewModelList"
			var="transactionModel" width="100%"
			action="${pageContext.request.contextPath}/p_cashwithdrawaltransdetails.html?actionId=${retriveAction}"
			title="" retrieveRowsCallback="limit" filterRowsCallback="limit"
			sortRowsCallback="limit">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Cash Payment Requests.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Cash Payment Requests.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf headerBackgroundColor="#b6c2da" headerTitle="Cash Payments" fileName="Cash Payment Requests.pdf"
					tooltip="Export PDF" view="com.inov8.microbank.common.util.CustomPdfView" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Cash Payment Requests.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>
				<c:set var="transactionDetailTransactionId">
					<security:encrypt strToEncrypt="${transactionModel.miniTransactionId}"/>
				</c:set>
				<c:set var="transactionDetailTransactionCodeId">
					<security:encrypt strToEncrypt="${transactionModel.transactionCodeId}"/>
				</c:set>				
				
				
				<ec:column width="15%" property="transactionCode" filterable="false" title="Transaction ID" escapeAutoFormat="true"/>
				<ec:column width="25%" property="createdOn" cell="date" format="dd/MM/yyyy hh:mm a" filterable="false" title="Date" />
				<%-- <ec:column width="15%" property="commandName" filterable="false" title="Type"/> --%>
				<ec:column width="15%" property="senderMobileNo" filterable="false" title="Sender Mobile No"/>
				<ec:column width="15%" property="senderCNIC" filterable="false" title="Sender CNIC"/>
				<ec:column width="15%" property="agentUserId" filterable="false" title="Agent ID" escapeAutoFormat="true"/>
				<ec:column width="15%" property="agentName" filterable="false" title="Agent Name" escapeAutoFormat="true"/>
				<ec:column width="15%" property="agentMobileNo" filterable="false" title="Agent Mobile No" escapeAutoFormat="true"/>
				<ec:column width="15%" property="recipientMobileNo" filterable="false" title="Reciepient Mobile No"/>
				<ec:column width="15%" property="recipientCNIC" filterable="false" title="Reciepient CNIC"/>
				<ec:column width="15%" property="productName" filterable="false" title="Product"/>
				<ec:column width="10%" property="tamt" filterable="false" title="Amount" style="text-align:right"/>
				<ec:column width="10%" property="camt" filterable="false" title="Charges" style="text-align:right"/>
				<ec:column width="15%" property="transactionStatusName" filterable="false" title="Status"/>
				<%-- <ec:column width="15%" property="customerUserId" filterable="false" title="Customer ID" escapeAutoFormat="true"/>
				<ec:column width="15%" property="customerMobileNo" filterable="false" title="Customer Mobile No" escapeAutoFormat="true"/> --%>
				
				
				

				<authz:authorize ifAnyGranted="<%=resendTxCodePermission%>">
					<ec:column alias="" title="Regenerate Transaction Code" style="text-align: center" filterable="false" sortable="false" viewsAllowed="html">
						<c:choose>
							 <c:when test="${transactionModel.transactionStatusId == 4 || transactionModel.transactionStatusId== 5}">
								<input type="button" class="button" value="Regenerate Transaction Code"
									id="resendSms${transactionDetailTransactionId}" />
									<input type="hidden" value="${transactionDetailTransactionId}" name="tId${transactionDetailTransactionId}" id="tId${transactionDetailTransactionId}" />
					<ajax:htmlContent baseUrl="${contextPath}/p_resendSms.html"
						source="resendSms${transactionDetailTransactionId}"
						target="smsContent"
						parameters="tId={tId${transactionDetailTransactionId}},commandId=${transactionModel.commandId},appUserId=${param.appUserId}" />
						</c:when>
						<c:otherwise>
								<input type="button" class="button" value="Regenerate Transaction Code"
									id="resendSms${transactionDetailTransactionId}"
									disabled="disabled" />					
						</c:otherwise>
					</c:choose>
					</ec:column>
				</authz:authorize>
				<authz:authorize ifNotGranted="<%=resendTxCodePermission%>">
					<ec:column alias="" title="Regenerate Transaction Code" style="text-align: center" filterable="false" sortable="false" viewsAllowed="html">
						
								<input type="button" class="button" value="Regenerate Transaction Code"
									id="resendSms${transactionDetailTransactionId}"
									disabled="disabled" />					
					</ec:column>
				</authz:authorize>
				<%-- <authz:authorize ifAnyGranted="<%=resetTxCodePermission%>">
					<ec:column alias="" title="Reset Transaction Code" style="text-align: center" filterable="false" sortable="false" viewsAllowed="html">
						<input type="button" class="button" value="Reset Transaction Code" 
						onclick="javascript:document.location='p_resettransactioncode.html?actionId=${retriveAction}&customerCnic=${param.customerCnic}&appUserId=${param.appUserId}&registered=${param.registered}&referrer=${param.referrer}&transactionId=${transactionModel.transactionCode}'" />
					</ec:column>
				</authz:authorize>
				<authz:authorize ifNotGranted="<%=resetTxCodePermission%>">
					<ec:column alias="" title="Reset Transaction Code" style="text-align: center" filterable="false" sortable="false" viewsAllowed="html">
						
								<input type="button" class="button" value="Reset Transaction Code"
									id="resendSms${transactionDetailTransactionId}"
									disabled="disabled" />					
					</ec:column>
				</authz:authorize> --%>
			</ec:row>
		</ec:table>
		<c:choose>
			<c:when test="${param.registered == 'true'}">
				<input type="button" class="button" value="Back" tabindex="48" onclick="javascript: window.location.href='p_mnomfsaccountdetails.html?appUserId=${param.appUserId}&actionId=2&referrer=${param.referrer}'" />
			</c:when>
			<c:when test="${not empty param.home}">
				<input type="button" class="button" value="Back" tabindex="48" onclick="javascript: window.location.href='home.html'" />
			</c:when>
			<c:otherwise>
				<input type="button" class="button" value="Back" tabindex="48" onclick="javascript: window.location.href='p_walkincustomers.html?actionId=2'" />
			</c:otherwise>
		</c:choose>
		<script language="javascript" type="text/javascript">

			function validateForm(form){
	        	var currentDate = "<%=PortalDateUtils.getServerDate()%>";
	        	var _fDate = form.startDate.value;
		  		var _tDate = form.endDate.value;
		  		var startlbl = "Start Date";
		  		var endlbl   = "End Date";
	        	var isValid = validateDateRange(_fDate,_tDate,startlbl,endlbl,currentDate);
	        	
	        	if( isValid )
	        	{
	        		isValid = validateFormChar(form);
	        	}
	        	return isValid;
	        }
      		/*Calendar.setup(
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
		    );*/
      	</script>
	</body>
</html>
