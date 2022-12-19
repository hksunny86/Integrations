<%@include file="/common/taglibs.jsp"%>
<jsp:directive.page import="com.inov8.microbank.webapp.action.retailermodule.SearchTaggedAgentsController"/>
<%@ page import='com.inov8.microbank.common.util.*' %>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<!--Author: Tayyab Islam -->

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css" type="text/css"/>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
      	<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css"/>
      	<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
      	<meta name="title" content="Tagged Agents"/>
      	<script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
		<script type="text/javascript">
			var jq=$.noConflict();
			jq(document).ready(function() {
				jq('.filterButtons').hide();
				});
			
			
		</script>
   
   </head>
  
   <body bgcolor="#ffffff">
   							<input type="button" class="button" style="width='90px'" value="Back Page" onclick="javascript:window.location.href='${pageContext.request.contextPath}/searchAgentGroupReport.html';" />				  
   									</br>
 										 <table class="tableRegion" width="100%">
	   											<tr>
													<td class="titleRow" style="font-weight: bold" width="50%">Group Title:</td>
		   											<td> <c:out value="${parentAgentDetail.groupTitle}"/></td>
												</tr>
												<tr>	
													<td class="titleRow" style="width: 150px; font-weight: bold">Parent Agent Mobile No.</td>
		   											<td> <c:out value="${parentAgentDetail.mobileNumber}"/></td>
												</tr>
												<tr> 	
													<td class="titleRow" style="width: 150px; font-weight: bold">Parent Agent ID:</td>
		   											<td> <c:out value="${parentAgentDetail.parrentId}"/></td>
												</tr>
												<tr>	
													<td class="titleRow" style="width: 150px; font-weight: bold">Parent Agent Business Name:</td>
		   											<td> <c:out value="${parentAgentDetail.businessName}"/></td>
												</tr>
										 </table> 
		
	    <ec:table 
		items="taggedAgentsList" 
                var = "UserManagementListViewModel"
                retrieveRowsCallback="limit"
                filterRowsCallback="limit"
                sortRowsCallback="limit" 
				action="${pageContext.request.contextPath}/searchTaggedAgents.html?actionId=${retriveAction}"
				title="">
				
				<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
								<ec:exportXls fileName="TaggedAgents.xls" tooltip="Export Excel"/>
				</authz:authorize>
				<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
								<ec:exportXlsx fileName="TaggedAgents.xlsx" tooltip="Export Excel" />
				</authz:authorize>
				<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
								<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da" headerTitle="Tagged Agents Detail Report"
				fileName="Tagged Agents Detail Report.pdf" tooltip="Export PDF" />
				</authz:authorize>
				<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
								<ec:exportCsv fileName="TaggedAgents.csv" tooltip="Export CSV"></ec:exportCsv>
				</authz:authorize>		
		
				<ec:row>
		
						<c:set var="taggedAgentId" value="${UserManagementListViewModel.taggedAgentID}"></c:set>
						<c:set var="PK" value="${UserManagementListViewModel.pk}"></c:set>
		   				<ec:column property="taggedAgentID" title="ID" filterable="false"/>
						<ec:column property="cnic" title="CNIC" filterable="false"/>
						<ec:column property="mobileNumber" title="Mobile #" filterable="false"/>		   
		   				<ec:column property="balanceNumeric" title="Balance" cell="currency" format="0.00"  filterable="false"/> 
		   				<ec:column property="taggedAgentBusinessName" title="Business Name" filterable="false"/>
		    			<ec:column property="totalTransaction" title="Total Transactions" filterable="false">
		   						 <a href="${pageContext.request.contextPath}/taggedAgentTransactions.html?tID=${taggedAgentId}&pkid=${PK}&cDate=${cDate}&eDate=${eDate}"><c:out value="${UserManagementListViewModel.totalTransaction}"></c:out></a>
		   			    </ec:column> 
		    	
			
      		  </ec:row>
	</ec:table>
	    
	</body>
</html>




