<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.*' %>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<!--Author: Tayyab Islam -->

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css" type="text/css"/>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
      	<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css"/>
      	<meta name="title" content="Tagged Agent Transactions"/>
      	<script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
		<script type="text/javascript">
			var jq=$.noConflict();
			jq(document).ready(function() {
				jq('.filterButtons').hide();
				});
			
			
		</script>
   </head>
   <body bgcolor="#ffffff">
                               <input type="button" class="button" style="width='90px'" value="Back Page" onclick="javascript:window.location.href='${pageContext.request.contextPath}/searchTaggedAgents.html?pID=${agentTaggingId}&cDate=${cDate}&eDate=${eDate};'" />				  
                              </br> 
                                                  
     <table class="tableRegion" width="100%">
   		
	   		<tr>
				<td class="titleRow" style="font-weight: bold" width="50%">Agent ID:</td>
		   		<td> <c:out value="${tAgentId}"/></td>
			</tr>
			
			<tr>	
				<td class="titleRow" style="width: 150px; font-weight: bold">Business Name:</td>
		   		<td> <c:out value="${taggedAgentBusinessName}"/></td>
			</tr>
			
			<tr>	
				<td class="titleRow" style="width: 150px; font-weight: bold">Mobile No.</td>
		   		<td> <c:out value="${taggedAgentMobileNo}"/></td>
			</tr>
			<tr>	
				<td class="titleRow" style="width: 150px; font-weight: bold">Total Balance</td> 
		   		<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance}"/></td>
			</tr> 
				
	</table>

		
	    
	    <ec:table 
		items="taggedAgentTransactionsList" 
                var = "UserManagementListViewModel"
                retrieveRowsCallback="limit"
                filterRowsCallback="limit"
                sortRowsCallback="limit" 
				action="${pageContext.request.contextPath}/taggedAgentTransactions.html?actionId=${retriveAction}"
				title="">
				
		    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="TaggedAgentTransactions.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="TaggedAgentTransactions.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da" headerTitle="Tagged Agent Transactions"
				fileName="TaggedAgentTransactions.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="TaggedAgentTransactions.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>		
		
		     <ec:row>
		     
		             <c:set var="productId" value="${UserManagementListViewModel.productId}"></c:set>

		             <ec:column property="productOrService" title="Product / Service(Consolidated)" filterable="false">
		             			<a href="${pageContext.request.contextPath}/taggedAgentsTransactionsList.html?pid=${productId}&tid=${tAgentId}&appUserId=${appUserId}&cDate=${cDate}&eDate=${eDate}"><c:out value="${UserManagementListViewModel.productOrService}"></c:out></a>
		             </ec:column>
			         <ec:column property="transactionCount" title="Transaction Count(Consolidated)" filterable="false"/>
			         <ec:column property="transactionAmount" title="Transaction Amount(Consolidated)" filterable="false"/>
            </ec:row>
	 </ec:table>
 </body>
</html>




