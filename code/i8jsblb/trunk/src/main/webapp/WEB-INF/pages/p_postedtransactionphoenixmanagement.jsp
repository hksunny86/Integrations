<%@ page import='com.inov8.microbank.common.util.*'%>
<%@page import="com.inov8.microbank.common.util.PortalConstants"%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<%@include file="/common/taglibs.jsp"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>

<html>
	<head>
<meta name="decorator" content="decorator">
   		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script> 
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
		<script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
		
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/deliciouslyblue/calendar.css" />	    
		<meta name="title" content="Posted Transactions RDV" id="<%=ReportIDTitleEnum.PostedTransactionsRDV.getId()%>" />
		<%@include file="/common/ajax.jsp"%>
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
	</head>
	<body bgcolor="#ffffff">
	<div id="successMsg" class ="infoMsg" style="display:none;"></div>
		<c:if test="${not empty messages}">
	    	<div class="infoMsg" id="successMessages">
	      		<c:forEach var="msg" items="${messages}">
		        	<c:out value="${msg}" escapeXml="false"/>
		        	<br/>
	      		</c:forEach>
	    	</div>
	    	<c:remove var="messages" scope="session"/>
	  	</c:if>

		<html:form name="phoenixPostedTransactionSearchForm" commandName="extendedPostedTransactionViewModel" method="post" action="p_postedtransactionphoenixmanagement.html" onsubmit="return validateForm(this)">
			<input type="hidden" name="appUserId" value="${param.appUserId}">
			<table border="0" width="750px" >
				<tr>
					<td align="right" class="formText">
						Transaction ID:
					</td>
					<td align="left" colspan="3">
						<html:input path="transactionCode" id="transactionCode" cssClass="textBox" maxlength="12" tabindex="1" onkeypress="return maskInteger(this,event)" />
					</td>
				</tr>
				<tr>
					<td align="right" class="formText">
						From Account:
					</td>
					<td align="left">
						<html:input path="fromAccount" id="fromAccount" cssClass="textBox" maxlength="14" tabindex="2" onkeypress="return maskInteger(this,event)"/>
					</td>
					<td align="right" class="formText">
						To Account:
					</td>
					<td align="left">
						<html:input path="toAccount" id="toAccount" cssClass="textBox" maxlength="14" tabindex="3" onkeypress="return maskInteger(this,event)"/>
					</td>
				</tr>
				<tr>
					<td align="right" class="formText" width="18%">
						Transaction Type:
					</td>
					<td align="left" width="32%">
						<html:select path="intgTransactionTypeId" id="intgTransactionTypeId" cssClass="textBox" tabindex="4">
							<html:option value="">---All---</html:option>
							<c:if test="${intgTransactionTypeModelList != null}">
								<html:options items="${intgTransactionTypeModelList}" itemValue="intgTransactionTypeId" itemLabel="transactionType"/>
							</c:if>
						</html:select>
					</td>
					<td class="formText" align="right" width="18%">
						Product:
					</td>
					<td align="left" width="32%">
						<html:select path="productId" cssClass="textBox" tabindex="5">
							<html:option value="">---All---</html:option>
							<c:if test="${productModelList != null}">
								<html:options items="${productModelList}" itemValue="productId" itemLabel="name"/>
							</c:if>
						</html:select>
					</td>
				</tr>
				<tr>
					<td align="right" class="formText">
						RRN:
					</td>
					<td align="left">
						<html:input path="refCode" id="refCode" cssClass="textBox" maxlength="16" tabindex="6" onkeypress="return maskInteger(this,event)"/>
					</td>
					<td align="right" class="formText">
						Consumer No.:
					</td>
					<td align="left">
						<html:input path="consumerNo" id="consumerNo" cssClass="textBox" maxlength="16" tabindex="7" onkeypress="return maskInteger(this,event)"/>
					</td>
				</tr>
				<tr>
					<td align="right" class="formText">
						Start Date:
					</td>
					<td align="left">
				        <html:input path="startDate" id="startDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
						<img id="sDate" tabindex="8" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
						<img id="sDate" tabindex="9" title="Clear Date" name="popcal" onclick="javascript:$('startDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0"/>
					</td>
					<td align="right" class="formText">
						End Date:
					</td>
					<td align="left">
					     <html:input path="endDate" id="endDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
					     <img id="eDate" tabindex="10" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
					     <img id="eDate" tabindex="11" title="Clear Date" name="popcal" onclick="javascript:$('endDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0"/>
					</td>
				</tr>
			  <tr>
				<td></td>
			    <td align="left" >
			    	<input type="submit" class="button" value="Search" tabindex="12" name="_search"/>
			    	<input type="reset" class="button" value="Cancel" name="_cancel" tabindex="12" onClick="javascript: window.location='p_postedtransactionphoenixmanagement.html'">
			   	</td>
			  </tr>
	 	 	</table>
		</html:form>

		<ec:table filterable="false" items="postedTransactionViewModelList" var="postedTransactionViewModel" retrieveRowsCallback="limit" filterRowsCallback="limit" 
		sortRowsCallback="limit" action="${pageContext.request.contextPath}/p_postedtransactionphoenixmanagement.html" title="" width="850px">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Posted Transactions RDV.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Posted Transactions RDV.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="Posted Transactions RDV" fileName="Posted Transactions RDV.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Posted Transactions RDV.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>
				<ec:column property="createdOn" alias="Date" cell="date" format="dd/MM/yyyy hh:mm a"/>
				<ec:column property="transactionCode" filterable="false" title="Transaction ID" escapeAutoFormat="true">
				  	<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
						<a href="p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode=${postedTransactionViewModel.transactionCode}" onclick="return openTransactionWindow('${postedTransactionViewModel.transactionCode}')">
						  ${postedTransactionViewModel.transactionCode}
						</a>
					</authz:authorize>
					<authz:authorize ifNotGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
						${postedTransactionViewModel.transactionCode}
					</authz:authorize>
				</ec:column>
				<ec:column property="transactionType" title="Transaction Type" style="text-align: center"/>
				<ec:column property="productName" title="Product" style="text-align: center"/>
				<ec:column property="refCode" title="RRN" style="text-align: center" escapeAutoFormat="true"/>
				<ec:column property="consumerNo" title="Consumer No." style="text-align: center" escapeAutoFormat="true"/>
				<ec:column property="fromAccount" title="From Account #" escapeAutoFormat="true"/>
				<ec:column property="toAccount" title="To Account #" escapeAutoFormat="true"/>
				<ec:column property="amount" title="Amount" calc="total" calcTitle="Total:" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="responseCode" title="Response Code" style="text-align: center"/>
				<ec:column property="responseCodeNarration"/>
				<ec:column property="stan" title="STAN" style="text-align: center" escapeAutoFormat="true"/>
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
		        newWindow=window.open('p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode='+transactionCode,'TransactionSummary','width='+popupWidth+',height='+popupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
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
