<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.*'%>
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
        <script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
        
		<link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
		<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
		<meta name="title" content="Agent Closing Balance Report"   id="<%=ReportIDTitleEnum.AgentClosingBalanceReport.getId()%>"  />
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
	</head>
	<body bgcolor="#ffffff">
		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" /><br/>
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>
		
			<html:form name='agentClosingBalanceViewForm' commandName="extendedAgentClosingBalanceViewModel" method="post"
				action="p_financeagentclosingbalance.html" onsubmit="return validateForm(this)" >
				<table width="750px" border="0">
				<tr>
					<td align="right" class="formText">
						Agent ID:
					</td>
					<td align="left" colspan="3">
						<html:input path="userId" id="userId" cssClass="textBox" maxlength="11" tabindex="1" onkeypress="return maskNumber(this,event)"/>
						${status.errorMessage}
					</td>
				</tr>
				<tr>
					<td align="right" class="formText" width="18%">
						Start Date:
					</td>
					<td align="left" width="32%">
				        <html:input path="startDate" id="startDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
						<img id="sDate" tabindex="5" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
						<img id="sDate" tabindex="6" title="Clear Date" name="popcal" onclick="javascript:$('startDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
					<td align="right" class="formText" width="18%">
						End Date:
					</td>
					<td align="left" width="32%">
					     <html:input path="endDate" id="endDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
					     <img id="eDate" tabindex="7" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
					     <img id="eDate" tabindex="8" title="Clear Date" name="popcal" onclick="javascript:$('endDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
					</td>
				</tr>
				<tr>
					<td class="formText" align="right">
						&nbsp;
					</td>
					<td align="left">
						<input name="_search" type="submit" class="button" value="Search" tabindex="9"/>
						<input name="reset" type="reset"
							onclick="javascript: window.location='p_financeagentclosingbalance.html?actionId=${retriveAction}'"
							class="button" value="Cancel" tabindex="10" />
					</td>
					<td colspan="2">
						&nbsp;
					</td>
				</tr>
				<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
			</table>
			</html:form>
		

		<ec:table items="agentClosingBalanceViewModelList" var="agentClosingBalanceViewModel"
		action="${contextPath}/p_financeagentclosingbalance.html?actionId=${retriveAction}"
		title="" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" filterable="false">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Agent Closing Balance Report.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Agent Closing Balance Report.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="Agent Closing Balance Report" fileName="Agent Closing Balance Report.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Agent Closing Balance Report.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>
				<ec:column property="statsDate"  title="Date" cell="date" format="dd/MM/yyyy"  escapeAutoFormat="true"/>
				<ec:column property="userId" title="Agent ID" escapeAutoFormat="true"/>
				<%-- <ec:column alias="Location" sortable="false">&nbsp;</ec:column> --%>
				<ec:column property="startDayBalance" title="Opening Balance" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="balanceReceived" title="Receiving" cell="currency" format="0.00" style="text-align: right"/>
				<ec:column property="balanceDisbursed" title="Withdrawals" cell="currency" format="0.00" style="text-align: right"/>
 			    <ec:column property="endDayBalance" title="Closing Balance"cell="currency" format="0.00" style="text-align: right" />
			</ec:row>
		</ec:table>

		<script language="javascript" type="text/javascript">

			document.forms[0].userId.focus();
	       			
      		Calendar.setup(
      		{
		       inputField  : "startDate", // id of the input field
		       ifFormat    : "%d/%m/%Y",      // the date format
			   button      : "sDate",    // id of the button
			   showsTime   :   false
		    }
      		);
			Calendar.setup(
		    {
		      inputField  : "endDate", // id of the input field
		      ifFormat    : "%d/%m/%Y",      // the date format		      
		      button      : "eDate",    // id of the button
		      showsTime   :   false
		    }
		    );
      	</script>
		<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>
	</body>
</html>
