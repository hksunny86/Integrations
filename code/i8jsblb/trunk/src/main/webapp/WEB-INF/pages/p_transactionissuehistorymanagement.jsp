<%@include file="/common/taglibs.jsp"%>
<jsp:directive.page import="com.inov8.microbank.common.model.portal.issuemodule.TransIssueHistoryListViewModel"/>
<jsp:directive.page import="com.inov8.microbank.common.model.portal.issuemodule.IssueHistoryListModel"/>
<jsp:directive.page import="com.inov8.microbank.common.model.portal.issuemodule.IssueHistoryStatusModel"/>
<%@ page import='com.inov8.microbank.common.util.*' %>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<!--Author: Hassan javaid -->

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css" type="text/css"/>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
      	<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css"/>
      	<meta name="title" content="Transaction Summary"/>
      	<style>
			.header {
				background-color: #6699CC;/*308dbb original*/
				color: white;
				font-family:verdana, arial, helvetica, sans-serif;/*verdana, arial, helvetica, sans-serif*/
				font-size: 11px;
				font-weight: bold;
				text-align: center;
				padding-right: 3px;
				padding-left: 3px;
				padding-top: 4px;
				padding-bottom: 4px;
				margin: 0px;
				border-right-style: solid;
				border-right-width: 1px;
				border-color: white;
			}		
	</style>
   </head>
   <c:set var="C_OPEN" scope="page">
		<security:encrypt strToEncrypt="<%=IssueTypeStatusConstantsInterface.CHARGEBACK_OPEN.toString()%>"/>
	</c:set>
	<c:set var="C_NEW" scope="page">
		<security:encrypt strToEncrypt="<%=IssueTypeStatusConstantsInterface.CHARGEBACK_NEW.toString()%>"/>
	</c:set>
	<c:set var="D_OPEN" scope="page">
		<security:encrypt strToEncrypt="<%=IssueTypeStatusConstantsInterface.DISPUTE_OPEN.toString()%>"/>
	</c:set>
	<c:set var="D_NEW" scope="page">
		<security:encrypt strToEncrypt="<%=IssueTypeStatusConstantsInterface.DISPUTE_NEW.toString()%>"/>
	</c:set>
	
   <body bgcolor="#ffffff">
   <div class="eXtremeTable">
   		<table class="tableRegion" width="100%">
   		
	   		<tr>
				<td class="titleRow" style="font-weight: bold" width="50%">Product</td>
		   		<td> 
		   			<c:out value="${transactionDetailPortalListModel.productName}"/>
				</td>
			</tr>
			<tr>	
				<td class="titleRow" style="width: 150px; font-weight: bold" width="50%">Transaction Initiated On</td>
		   		<td>
					<c:out value="${transactionDetailPortalListModel.createdOn}"/>
		   			<%--<fmt:formatDate value="${transactionDetailPortalListModel.createdOn}" pattern="dd/MM/yyyy hh:mm a" />--%>
				</td>
			</tr>
			<tr>	
				<td class="titleRow" style="width: 150px; font-weight: bold">Transaction Completed On</td>
		   		<td>
					<c:out value="${transactionDetailPortalListModel.updatedOn}"/>
		   			<%--<fmt:formatDate value="${transactionDetailPortalListModel.updatedOn}" pattern="dd/MM/yyyy hh:mm a" />--%>
				</td>
			</tr>
			<tr>	
				<td class="titleRow" style="width: 150px; font-weight: bold">Agent ID</td>
		   		<td> 
		   			<c:out value="${transactionDetailPortalListModel.agent1Id}"/>
				</td>
			</tr>
			<tr> 	
				<td class="titleRow" style="width: 150px; font-weight: bold">Sender ID</td>
		   		<td> 
		   			<c:out value="${transactionDetailPortalListModel.mfsId}"/>
				</td>
			</tr>
			<tr>	
				<td class="titleRow" style="width: 150px; font-weight: bold">Sender CNIC</td>
		   		<td> 
		   			<c:out value="${transactionDetailPortalListModel.senderCnic}"/>
				</td>
			</tr>
			<tr>	
				<td class="titleRow" style="width: 150px; font-weight: bold">Sender Mobile No</td>
		   		<td> 
		   			<c:out value="${transactionDetailPortalListModel.saleMobileNo}"/>
				</td>
			</tr>	
			<tr>
				<td class="titleRow" style="width: 150px; font-weight: bold">Recipient ID</td>
		   		<td> 
		   			<c:out value="${transactionDetailPortalListModel.recipientMfsId}"/>
				</td>
			</tr>
			<tr>	
				<td class="titleRow" style="width: 150px; font-weight: bold">Recipient CNIC</td>
		   		<td> 
		   			<c:out value="${transactionDetailPortalListModel.recipientCnic}"/>
				</td>
			</tr>
			<tr>	
				<td class="titleRow" style="width: 150px; font-weight: bold">Recipient Mobile No</td>
		   		<td> 
		   			<c:out value="${transactionDetailPortalListModel.recipientMobileNo}"/>
				</td>
			</tr>
			
		</table>
		
		<c:if test="${issueHistoryListModelList!=null}">
		 
			<table>
				<tr>
					<c:forEach items="${issueHistoryListModelList}" var="issueHistoryListModelListOuter">
						<table class="tableRegion" style="padding-top: 5px; padding-bottom: 5px" width="100%">
			   				<tr>
				   				<td class="titleRow" style="width: 125px; font-weight: bold">Issue Code</td>
			   					<td colspan="2"><c:out value="${issueHistoryListModelListOuter.issueCode}"/></td>
							</tr>
							<tr>
								<td class="titleRow" style="width: 125px; font-weight: bold">Issue Date</td>
			   					<td colspan="2">
			   					<%-- <c:out value="${issueHistoryListModelListOuter.issueDate}"/>  --%>
			   					<%
			   						IssueHistoryListModel issue = (IssueHistoryListModel)pageContext.getAttribute("issueHistoryListModelListOuter");
			   						out.println(PortalDateUtils.formatDate(issue.getIssueDate(), "dd/MM/yyyy hh:mm a"));
			   					 %>
			   					</td>
							</tr>
							<tr>
	   							<td class="titleRow" style="width:125px;">Date</td>
			   					<td class="titleRow" style="text-align: center; width:125px;">Status</td>
			   					<td class="titleRow" >Comments</td>
							</tr>
							<c:forEach items="${issueHistoryListModelListOuter.issueHistoryStatusModellist}" var="issueHistoryStatusModel"  varStatus="x">
								<c:set var="issueHistoryStatusIssueTypeStatusId">
									<security:encrypt strToEncrypt="${issueHistoryStatusModel.issueTypeStatusId}"/>
								</c:set>

								<c:set var="rowCssClass" value="even"/>
				   				<c:if test="${x.count%2!=0}">
			   						<c:set var="rowCssClass" value="odd" scope="page"/>
		   						</c:if>
					   			<tr class="${rowCssClass}" onmouseover="this.className='highlight'"  onmouseout="this.className='${rowCssClass}'">
		   							<td style="width: 125px;">	
				   						<%-- <c:out value="${issueHistoryStatusModel.issueHistoryDate}"/> --%>
				   					<%
				   						IssueHistoryStatusModel issueHist = (IssueHistoryStatusModel)pageContext.getAttribute("issueHistoryStatusModel");
				   						out.println(PortalDateUtils.formatDate(issueHist.getIssueHistoryDate(), "dd/MM/yyyy hh:mm a"));
				   					 %>
				   					</td>
				   					<td style="text-align: center; width:125px;">
			   							<c:choose>
					   						<c:when test="${issueHistoryStatusIssueTypeStatusId eq C_NEW 
					   						|| issueHistoryStatusIssueTypeStatusId eq D_NEW 
					   						|| issueHistoryStatusIssueTypeStatusId eq C_OPEN 
					   						|| issueHistoryStatusIssueTypeStatusId eq D_OPEN}">
					   								<c:if test="${issueHistoryStatusIssueTypeStatusId eq C_NEW}">Charged Back</c:if>
					   								<c:if test="${issueHistoryStatusIssueTypeStatusId eq D_NEW}">Disputed</c:if>
					   								<c:if test="${issueHistoryStatusIssueTypeStatusId eq D_OPEN || issueHistoryStatusIssueTypeStatusId eq C_OPEN}">Escalated</c:if>
							   				</c:when>		
					   							<c:otherwise>
							   						<c:out value="${issueHistoryStatusModel.statusName}"/>
					   							</c:otherwise>
			  							</c:choose>
				   					</td>
				   					<td>
				   						<c:out value="${issueHistoryStatusModel.comments}"/>
				   					</td>
				   				</tr>
			   			</c:forEach>
		   			</table>
				</c:forEach>
				</tr>
			</table>
			
   		</c:if>
   		</div>
		<c:if test="${commissionList!=null}">
		<h3 class="header" id="PostingInMicroBank">Tax, Fee, Income, and Commission distribution</h3>
		<div class="eXtremeTable">
		<table class="tableRegion" width="100%">	
		<tr class="odd">
			<td class="titleRow" style="width: 90px; font-weight: bold" >Transaction ID</td>
			<td class="titleRow" style="width: 90px; font-weight: bold">Product</td>
			<td class="titleRow" style="width: 90px; font-weight: bold">Commission Stakeholder</td>
			<td class="titleRow" style="width: 90px; font-weight: bold">Commission Amount</td>
		</tr>
		<c:forEach items="${commissionList}" var="commissionListModel"  varStatus="status">  
		<tr class="odd">
			<td>${commissionListModel.transactionId}</td>
			<td>${commissionListModel.product}</td>
			<td>${commissionListModel.commissionStakehoder}</td>
			<td>${commissionListModel.commissionAmount}</td>
		</tr>	
		</c:forEach>
	    </table> 
	    </div>
	    </c:if>
	   	<c:if test="${settlementBBStatementList !=null}">
		<div class="eXtremeTable">
		<h3 class="header" id="PostingInMicroBank">Posting in MicroBank</h3>
		</div><div class="eXtremeTable">
		<table class="tableRegion" width="100%">	
		<tr class="odd">
			<td class="titleRow" style="width: 90px; font-weight: bold" >Transaction ID</td>
			<td class="titleRow" style="width: 90px; font-weight: bold" >Transaction Date</td>
			<td class="titleRow" style="width: 90px; font-weight: bold">Account title</td>
			<td class="titleRow" style="width: 90px; font-weight: bold">Account Number</td>
			<td class="titleRow" style="width: 90px; font-weight: bold">Debit</td>
			<td class="titleRow" style="width: 90px; font-weight: bold">Credit</td>
			<td class="titleRow" style="width: 90px; font-weight: bold">Balance</td>
			<td class="titleRow" style="width: 90px; font-weight: bold"> Leg</td>
			<td class="titleRow" style="width: 90px; font-weight: bold">Summary</td>
			<td class="titleRow" style="width: 90px; font-weight: bold">Is Processed by SAF</td>
			<td class="titleRow" style="width: 90px; font-weight: bold">SAF Status</td>
		</tr>
		<c:forEach items="${settlementBBStatementList}" var="settlementBBStatementModel"  varStatus="status">  
		<tr class="odd">
			<td>${settlementBBStatementModel.transactionCode}</td>
			<td><fmt:formatDate pattern="dd/MM/yyyy hh:mm a" value="${settlementBBStatementModel.transactionTime}" /></td>
			<td>${settlementBBStatementModel.accountTittle}</td>
			<td>${settlementBBStatementModel.accountNumber}</td>
			<td><fmt:formatNumber type="number"  value="${settlementBBStatementModel.debitAmount}" /></td>
			<td><fmt:formatNumber type="number"  value="${settlementBBStatementModel.creditAmount}" /></td>
			<td>
				<c:if test="${empty settlementBBStatementModel.isProcessedByQueue || (!settlementBBStatementModel.isProcessedByQueue) || (settlementBBStatementModel.isProcessedByQueue && settlementBBStatementModel.queueStatus == 1)}">
					<fmt:formatNumber value="${settlementBBStatementModel.balanceAfterTransaction}" pattern="###.##"/>
				</c:if>
			</td>	
			<td>
				<c:if test="${empty settlementBBStatementModel.category}"> 1st Leg</c:if>
				<c:if test="${settlementBBStatementModel.category == 0 }"> 1st Leg</c:if>
				<c:if test="${settlementBBStatementModel.category == 1 }"> 1st Leg </c:if>	
				<c:if test="${settlementBBStatementModel.category == 2 }"> Unclaimed</c:if>
				<c:if test="${settlementBBStatementModel.category == 3 }"> 2nd Leg</c:if>
				<c:if test="${settlementBBStatementModel.category == 4 }"> Receive Cash</c:if>
				<c:if test="${settlementBBStatementModel.category == 5 }"> Received in Account</c:if>
				<c:if test="${settlementBBStatementModel.category == 6 }"> Redemption Request</c:if>
				<c:if test="${settlementBBStatementModel.category == 7 }"> Sender Redeem</c:if>
				<c:if test="${settlementBBStatementModel.category == 8 }"> A/C Closed/ Blacklisted</c:if>
				<c:if test="${settlementBBStatementModel.category == 9 }"> Manual Reversal</c:if>
				<c:if test="${settlementBBStatementModel.category == 10 }"> Manual Adjustment</c:if>
                <c:if test="${settlementBBStatementModel.category == 11 }"> Auto Reversal</c:if>
			</td>
			<td>${settlementBBStatementModel.transactionSummaryText}</td>
			<td>
				<c:if test="${settlementBBStatementModel.isProcessedByQueue != null}">
					<c:if test="${settlementBBStatementModel.isProcessedByQueue}"> Yes</c:if>	
					<c:if test="${!settlementBBStatementModel.isProcessedByQueue}"> No</c:if>	
				</c:if>
			</td>
			<td>
				<c:if test="${settlementBBStatementModel.isProcessedByQueue}">
					<c:if test="${settlementBBStatementModel.queueStatus == 0 }"> Pending</c:if>
					<c:if test="${settlementBBStatementModel.queueStatus == 1 }"> Complete</c:if>	
				</c:if>
			</td>
		</tr>	
		</c:forEach>
	    </table> 
	    </div>
	    </c:if>
	    <c:if test="${postedTransRDVList !=null}">
	    <div class="eXtremeTable">
	    <h3 class="header" id="PostingInMicroBank">Posting in T24</h3>
		</div><div class="eXtremeTable">
		<table class="tableRegion" width="100%">	
		<tr class="odd">
			<td class="titleRow" style="width: 90px; font-weight: bold" >Transaction ID</td>
			<td class="titleRow" style="width: 90px; font-weight: bold">Transaction Type</td>
			<td class="titleRow" style="width: 90px; font-weight: bold">From Account No.</td>
			<td class="titleRow" style="width: 90px; font-weight: bold">To Account No</td>
			<td class="titleRow" style="width: 90px; font-weight: bold">Amount</td>
			<td class="titleRow" style="width: 90px; font-weight: bold">Response Code</td>
			<td class="titleRow" style="width: 90px; font-weight: bold">Stan</td>
		</tr>
		<c:forEach items="${postedTransRDVList}" var="postedTransRDVListModel"  varStatus="status">  
		<tr class="odd">
			<td>${postedTransRDVListModel.transactionCode}</td>
			<td>${postedTransRDVListModel.transactionType}</td>
			<td>${postedTransRDVListModel.fromAccount}</td>
			<td>${postedTransRDVListModel.toAccount}</td>
			<td><fmt:formatNumber type="number" pattern="###.##" value="${postedTransRDVListModel.amount}" /></td>
			<td>${postedTransRDVListModel.responseCode}</td>
			<td>${postedTransRDVListModel.stan}</td>
		</tr>	
		</c:forEach>
	    </table>
	    </div>
	    </c:if>
	     <c:if test="${ofTransList !=null}">
	    <div class="eXtremeTable">
	    <h3 class="header" id="PostingInMicroBank">Posting in Oracle Financial  </h3>	
	    </div><div class="eXtremeTable">
		<table class="tableRegion" width="100%">	
		<tr class="odd">
			<td class="titleRow" style="width: 90px; font-weight: bold" >Transaction ID</td>
			<td class="titleRow" style="width: 90px; font-weight: bold" >Transaction Date</td>
			<td class="titleRow" style="width: 90px; font-weight: bold">Account Number</td>
			<td class="titleRow" style="width: 90px; font-weight: bold">Account Title</td>
			<td class="titleRow" style="width: 90px; font-weight: bold">Debit Amount</td>
			<td class="titleRow" style="width: 90px; font-weight: bold">Credit Amount</td>
		</tr>
		<c:forEach items="${ofTransList}" var="ofTransListModel"  varStatus="status">  
		<tr class="odd">
			<td>${ofTransListModel.transactionId}</td>
			<td><fmt:formatDate pattern="dd/MM/yyyy hh:mm a" value="${ofTransListModel.createdOn}" /></td>
			<td>${ofTransListModel.accountNo}</td>
			<td>${ofTransListModel.accountTitle}</td>
			<td><fmt:formatNumber type="number" pattern="###.##" value="${ofTransListModel.debitAmount}" /></td>
			<td><fmt:formatNumber type="number" value="${ofTransListModel.creditAmount}" /></td>
		</tr>	
		</c:forEach>
	    </table> 
	    </c:if> 
		
   		<table width="100%">
	   		<tr align="center">
	   			<td align="center" style="padding-top: 10px">
				   		<input type ="button" class="popbutton" value ="Close Window" onclick = "window.close();"/>
	   			</td>
	   		</tr>
   		</table>
   		</div>
	</body>
</html>