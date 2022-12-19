<%@ page import='com.inov8.microbank.common.util.*,com.inov8.microbank.common.util.PortalConstants'%>
<%@include file="/common/taglibs.jsp"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<c:set var="createAction"><%=PortalConstants.ACTION_CREATE %></c:set>

<html>
  <head>
<meta name="decorator" content="decorator">
    
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
	  <script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
	  <script language="javascript" type="text/javascript">
		  var jq=$.noConflict();
	  </script>
<meta name="title" content="Microbank Settlement Accounts" />
<%@include file="/common/ajax.jsp"%>

	<%
		String bbStatementPermission = PortalConstants.ADMIN_GP_READ;
		bbStatementPermission += "," + PortalConstants.PG_GP_READ;
		bbStatementPermission += "," + PortalConstants.RPT_SETTLEMENT_BB_STMT_READ;
	%>
  </head>
  
  <body bgcolor="#ffffff">
	<div id="successMsg" class ="infoMsg" style="display:none;"></div>
	<div id="errorMsg" class ="errorMsg" style="display:none;"></div>  
   <c:if test="${not empty messages}">
    <div class="infoMsg" id="successMessages">
        <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session"/>
</c:if>
    
    
    	<ec:table items="settlementAccountsList" var="SettlementAccountModel" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" filterable="false" sortable="false"
					action="${pageContext.request.contextPath}/p_settlementbbaccounts.html?actionId=${retriveAction}" title="">
				
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="SettlementAccounts.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="SettlementAccounts.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da" headerTitle="SettlementAccounts"
				fileName="SettlementAccounts.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="SettlementAccounts.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>		
		
		<ec:row>
		
			
			<ec:column property="accountNumber" title="Account Number" filterable="false" sortable="false"/>
			<ec:column property="accountTitle" title="Title" filterable="false" sortable="false"/>
			<ec:column property="accountTypeName" title="Account Type" filterable="false" sortable="false"  style="text-align: right"/>
			<ec:column property="productName" title="Product" filterable="false" sortable="false"  style="text-align: right"/>
			<ec:column property="balance" title="Balance" filterable="false" sortable="false"  style="text-align: right" cell="currency" format="0.00"/>

			<authz:authorize ifAnyGranted="<%=bbStatementPermission%>">
		    	<ec:column property="accountId" title=" " filterable="false" sortable="false" viewsAllowed="html" style="text-align: center">
					<input type="button" class="button" value="View Ledger" name="BBStatement${SettlementAccountModel.accountId}"
						onclick="javascript:document.location='p_settlementbbstatement.html?actionId=2&accountId=${SettlementAccountModel.accountId}&accountTitle=${SettlementAccountModel.accountTitle}&accountNumber=${SettlementAccountModel.accountNumber}'" />
		    	</ec:column>
		    </authz:authorize>
		    
        </ec:row>
	</ec:table>
    
    
  </body>
</html>
