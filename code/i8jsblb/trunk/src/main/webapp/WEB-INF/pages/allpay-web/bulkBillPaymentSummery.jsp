<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page
	import="com.inov8.microbank.common.model.transactionmodule.FetchTransactionListViewModel"%>

<%@include file="/common/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/awstyle_new.css" type="text/css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/aw/aw-contents-height-setter.js"></script>
		<title>Collection Payment Summary</title>
		
		<script language="javascript">
			
			function openPrintWindow() {
	
				var url = '${contextPath}'+'/recepit.aw?TRXID=${TRXID}&PID=${PID}';
	
				printWindow = window.open(url,"receipt_print", "location=1, left=0, top=0, width=625, height=400, status=no, scrollbars=no, titlebar=no, toolbar=no, menubar=no, resizable=no");
	
				printWindow.focus();
			}
	
		</script>		
		
	</head>
	<body>
		<table align="center" bgcolor="white" border="0" cellpadding="0" cellspacing="0" height="100%" width="85%" class="container">
			<tr height="95px">
				<td>
					<jsp:include page="header.jsp"></jsp:include>
				</td>
			</tr>
			<tr id="contents" valign="top">
				<td>
					<table align="center" width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td colspan="6">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="6" align="center">
								<font color="#0066b3" size="5px">Bulk Bill Payment Summery</font>
							</td>
						</tr>
						<tr>
							<td colspan="6">&nbsp;</td>
						</tr>
						<tr>
							<td align="center" colspan="6">
								<span class="success-msg">${requestScope.HEADING}</span>									
							</td>
						</tr>
						<tr>
                            <td colspan="6">
								<div style="border: none;" align="center">
	                                <table align="center" width="80%" border="0" cellpadding="0" cellspacing="0">
										<tr>
											<td width="10%" align="center"><span class="label">Consumer #</span></td>
											<td width="15%" align="center"><span class="label">Company</span></td>
											<td width="10%" align="center"><span class="label">Mobile #</span></td>
											<td width="10%" align="center"><span class="label">Bill Amount</span></td>
											<td width="15%" align="center"><span class="label">Transaction Code</span></td>	
											<td width="20%" align="center"><span class="label">Status</span></td>
										</tr>		
	                                </table>	
								</div>								
							</td>
						</tr>
                        
						<tr>
                            <td colspan="6">
								<div style="overflow:scroll; border: thin;height:250px;" align="center">								
	                                <table align="center" width="80%" border="0" cellpadding="0" cellspacing="0">	
		                                <c:forEach items="${bulkBillPaymentVOList}" var="bulkBillPaymentVO">
		                                    <tr>
		                                        <td width="10%" align="center"><span class="value-text">${bulkBillPaymentVO.consumerNumber}</span></td>
		                                        <td width="15%" align="center"><span class="value-text">${bulkBillPaymentVO.productName}</span></td>
		                                        <td width="10%" align="center"><span class="value-text">${bulkBillPaymentVO.mobileNumber}</span></td>
		                                        <td width="10%" align="center"><span class="value-text">${bulkBillPaymentVO.billAmount}</span></td>
		                                        <td width="15%" align="center"><span class="value-text">${bulkBillPaymentVO.transactionCode}</span></td>
		                                        <td width="20%" align="center"><span class="value-text">${bulkBillPaymentVO.supProcessingStatus}</span></td>
		                                    </tr>
		                                </c:forEach>		                                
	                                </table>		
								</div>				
							</td>
						</tr>

						<tr>
							<td colspan="6" align="center"><br/>Back To : 
								<a href="bulkBillPayment.aw">Bulk Bill Payment</a>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr valign="bottom">
				<td>
					<jsp:include page="footer.jsp"></jsp:include>
				</td>
			</tr>
		</table>
	</body>
</html>