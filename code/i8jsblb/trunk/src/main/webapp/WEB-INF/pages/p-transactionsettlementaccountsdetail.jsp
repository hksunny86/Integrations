<%@include file="/common/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<!--Author: Hassan javaid -->

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css" type="text/css"/>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
      	<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css"/>
      	<meta name="title" content="Transaction Issue History"/>
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
				</td>
			</tr>
			<tr>	
				<td class="titleRow" style="width: 150px; font-weight: bold">Transaction Completed On</td>
		   		<td> 
		   			<c:out value="${transactionDetailPortalListModel.updatedOn}"/>
				</td>
			</tr>
			<tr>	
				<td class="titleRow" style="width: 150px; font-weight: bold">Agent ID</td>
		   		<td> 
		   			<c:out value="${transactionDetailPortalListModel.agent1Id}"/>
				</td>
			</tr>
			<tr> 	
				<td class="titleRow" style="width: 150px; font-weight: bold">Sender Customer ID</td>
		   		<td> 
		   			<c:out value="${transactionDetailPortalListModel.mfsId}"/>
				</td>
			</tr>
			<tr>	
				<td class="titleRow" style="width: 150px; font-weight: bold">Sender Customer CNIC</td>
		   		<td> 
		   			<c:out value="${transactionDetailPortalListModel.senderCnic}"/>
				</td>
			</tr>
			<tr>	
				<td class="titleRow" style="width: 150px; font-weight: bold">Sender Customer Mobile No</td>
		   		<td> 
		   			<c:out value="${transactionDetailPortalListModel.saleMobileNo}"/>
				</td>
			</tr>	
			<tr>
				<td class="titleRow" style="width: 150px; font-weight: bold">Recipient Customer ID</td>
		   		<td> 
		   			<c:out value="${transactionDetailPortalListModel.recipientMfsId}"/>
				</td>
			</tr>
			<tr>	
				<td class="titleRow" style="width: 150px; font-weight: bold">Recipient Customer CNIC</td>
		   		<td> 
		   			<c:out value="${transactionDetailPortalListModel.recipientCnic}"/>
				</td>
			</tr>
			<tr>	
				<td class="titleRow" style="width: 150px; font-weight: bold">Recipient Customer Mobile No</td>
		   		<td> 
		   			<c:out value="${transactionDetailPortalListModel.recipientMobileNo}"/>
				</td>
			</tr>
			
		</table>
		</div>
		
		<h3 class="header" id="PostingInMicroBank">Tax, Fee, Income, and Commission distribution</h3>
		<div class="eXtremeTable" class="eXtremeTable">
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
		<div class="eXtremeTable">
		<h3 class="header" id="PostingInMicroBank">Posting in MicroBank</h3>
		</div><div class="eXtremeTable">
		<table class="tableRegion" width="100%">	
		<tr class="odd">
			<td class="titleRow" style="width: 90px; font-weight: bold" >Transaction ID</td>
			<td class="titleRow" style="width: 90px; font-weight: bold">Account title</td>
			<td class="titleRow" style="width: 90px; font-weight: bold">Debit</td>
			<td class="titleRow" style="width: 90px; font-weight: bold">Credit</td>
			<td class="titleRow" style="width: 90px; font-weight: bold">Balance</td>
		</tr>
		<c:forEach items="${settlementBBStatementList}" var="settlementBBStatementModel"  varStatus="status">  
		<tr class="odd">
			<td>${settlementBBStatementModel.transactionCode}</td>
			<td>${settlementBBStatementModel.accountTittle}</td>
			<td>${settlementBBStatementModel.debitAmount}</td>
			<td>${settlementBBStatementModel.creditAmount}</td>
			<td>${settlementBBStatementModel.balanceAfterTransaction}</td>
		</tr>	
		</c:forEach>
	    </table> 
	    </div>
	    <div class="eXtremeTable">
	    <h3 class="header" id="PostingInMicroBank">Posting in T24</h3>
		</div><div class="eXtremeTable">
		<table class="tableRegion" width="100%">	
		<tr class="odd">
			<td class="titleRow" style="width: 90px; font-weight: bold" >Transaction ID</td>
			<td class="titleRow" style="width: 90px; font-weight: bold">From Account No.</td>
			<td class="titleRow" style="width: 90px; font-weight: bold">From Account Title</td>
			<td class="titleRow" style="width: 90px; font-weight: bold">To Account No</td>
			<td class="titleRow" style="width: 90px; font-weight: bold">To Account Title</td>
			<td class="titleRow" style="width: 90px; font-weight: bold">Amount</td>
			<td class="titleRow" style="width: 90px; font-weight: bold">Response Code</td>
		</tr>
		<c:forEach items="${postedTransRDVList}" var="postedTransRDVListModel"  varStatus="status">  
		<tr class="odd">
			<td>${postedTransRDVListModel.transactionCode}</td>
			<td>${postedTransRDVListModel.fromAccount}</td>
			<td>From Account title</td>
			<td>${postedTransRDVListModel.toAccount}</td>
			<td>${postedTransRDVListModel.toAccount}</td>
			<td>${postedTransRDVListModel.amount}</td>
			<td>${postedTransRDVListModel.responseCode}</td>
		</tr>	
		</c:forEach>
	    </table>
	    </div>
	    <div class="eXtremeTable">
	    <h3 class="header" id="PostingInMicroBank">Posting in Oracle Financial  </h3>	
	    </div><div class="eXtremeTable">
		<table class="tableRegion" width="100%">	
		<tr class="odd">
			<td class="titleRow" style="width: 90px; font-weight: bold" >Transaction ID</td>
			<td class="titleRow" style="width: 90px; font-weight: bold">From Oracle No.</td>
			<td class="titleRow" style="width: 90px; font-weight: bold">From A/C Title</td>
			<td class="titleRow" style="width: 90px; font-weight: bold">To Oracle No.</td>
			<td class="titleRow" style="width: 90px; font-weight: bold">To Account Title</td>
			<td class="titleRow" style="width: 90px; font-weight: bold">Amount</td>
			<td class="titleRow" style="width: 90px; font-weight: bold">OF File Generated</td>
		</tr>
		<c:forEach items="${ofTransList}" var="ofTransListModel"  varStatus="status">  
		<tr class="odd">
			<td>${ofTransListModel.transactionId}</td>
			<td>${ofTransListModel.fromAccountNo}</td>
			<td>${ofTransListModel.fromAccountTitle}</td>
			<td>${ofTransListModel.toAccountNo}</td>
			<td>${ofTransListModel.toAccountTitle}</td>
			<td>${ofTransListModel.amount}</td>
			<td>${ofTransListModel.statusName}</td>
		</tr>	
		</c:forEach>
	    </table>  
		
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