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
      	<meta name="title" content="Tagged Agent Transactions Detail"/>
      	 <script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
      	<script language="javascript" type="text/javascript">
    	var jq=$.noConflict();
    	jq(document).ready(function() {
			jq('.filterButtons').hide();
			});
    </script>
   </head>
   <body bgcolor="#ffffff">
   
			  <input type="button" class="button" style="width='90px'" value="Back Page" onclick="javascript:window.location.href='${pageContext.request.contextPath}/taggedAgentTransactions.html?tID=${TID}&pkid=${appID}&cDate=${cDate}&eDate=${eDate}'" />
		     <br/>
			
			<ec:table 
				items="tDetailMasterList" 
                var = "UserManagementListViewModel"
                retrieveRowsCallback="limit"
                filterRowsCallback="limit"
                sortRowsCallback="limit" 
				action="${pageContext.request.contextPath}/taggedAgentsTransactionsList.html?actionId=${retriveAction}"
				title="">
				
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
							<ec:exportXls fileName="TaggedAgentTransactionsDetail.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
							<ec:exportXlsx fileName="TaggedAgentTransactionsDetail.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
							<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da" headerTitle="Tagged Agent Transactions Detail"
								fileName="TaggedAgentTransactionsDetail.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
							<ec:exportCsv fileName="TaggedAgentTransactionsDetail.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>		
		
		<ec:row>
				<ec:column property="transactionCode" title="Transaction ID" filterable="false">
						  <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
											<a href="p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode=${UserManagementListViewModel.transactionCode}" onClick="return openTransactionWindow('${UserManagementListViewModel.transactionCode}')">
			 									 ${UserManagementListViewModel.transactionCode}
											</a>
						</authz:authorize>
										 <authz:authorize ifNotGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
														${UserManagementListViewModel.transactionCode}
										</authz:authorize>
				</ec:column>
				<ec:column property="agent1Id" title="Sender Agent ID" filterable="false"/>
				<ec:column property="senderAgentAccountNo" title="Sender Agent Account #" filterable="false"/>
				<ec:column property="mfsId" title="Sender ID" filterable="false"/>
				<ec:column property="saleMobileNo" title="Sender Mobile # " filterable="false"/>
				<ec:column property="senderCnic" title="Sender CNIC" filterable="false"/>
				<ec:column property="deviceType" title="Sender Channel" filterable="false"/>
				<ec:column property="paymentMode" title="Payment Mode" filterable="false"/>
				<ec:column property="bankAccountNo" title="Sender Account #" filterable="false"/>
				<ec:column property="createdOn" title="Transaction Date" cell="date" format="dd/MM/yyyy hh:mm a" filterable="false"/>
				<ec:column property="updatedOn" title="Transaction Last UpdatedOn" cell="date" format="dd/MM/yyyy hh:mm a" filterable="false"/>
				<ec:column property="supplierName" title="Supplier" filterable="false"/>
				<ec:column property="productName" title="Product" filterable="false"/>
				<ec:column property="processingStatusName" title="Status" filterable="false"/>
       
       </ec:row>
	
	</ec:table>
 </body>
</html>




