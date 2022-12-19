<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page	import="com.inov8.microbank.common.model.transactionmodule.FetchTransactionListViewModel"%>
<%@include file="/common/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>Transaction Summary</title>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/awstyle_new.css" type="text/css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/aw/aw-contents-height-setter.js"></script>
		<script language="javascript">
			
			function openPrintWindow() {

				var url = '${contextPath}'+'/recepit.aw?TRXID=${FetchTransactionListViewModel.tranCode}&PID=50002';

				printWindow = window.open(url,"receipt_print", "location=1, left=0, top=0, width=625, height=450, status=no, scrollbars=no, titlebar=no, toolbar=no, menubar=no, resizable=no");

				printWindow.focus();
			}
		</script>
	</head>
	<body oncontextmenu="return false">
		<table border="0" height="100%" width="85%" align="center" class="container" cellpadding="0" cellspacing="0">
			<tr height="95px">
				<td>
					<jsp:include page="header.jsp"></jsp:include>
				</td>
			</tr>
			<tr id="contents" valign="top">
				<td>
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td colspan="2">&nbsp;<br/><br/><br/></td>
						</tr>
						<tr>
							<td colspan="2" align="center">
								<font color="#0066b3" size="5px">${requestScope.HEADING}</font>
							</td>
						</tr>
						<tr>
							<td colspan="2">&nbsp;<br/><br/></td>
						</tr>
						<tr>
							<td width="50%" align="right">
								<span class="label">
									Product:
								</span>
							</td>
							<td width="50%" align="left">
								<span class="value-text">
									<c:out value="${FetchTransactionListViewModel.productName}" />
								</span>
							</td>
						</tr>
						<tr>
							<td width="50%" align="right">
								<span class="label">Mobile #:</span>
							</td>
							<td width="50%" align="left">
								<span class="value-text">
									<c:out value="${FetchTransactionListViewModel.notificationMobileNo}" />
								</span>
							</td>
						</tr>
						<tr>
							<td width="50%" align="right">
								<span class="label">Amount:</span>
							</td>
							<td width="50%" align="left">
								<span class="value-text">
									<c:out value="${FetchTransactionListViewModel.tranAmount}"/> PKR
								</span>
							</td>
						</tr>
						<tr>
							<td width="50%" align="right">
								<span class="label">Service Charges:</span>
							</td>
							<td width="50%" align="left">
								<span class="value-text">
									<c:out value="${FetchTransactionListViewModel.serviceCharges}"/> PKR
								</span>
							</td>
						</tr>
						<tr>
							<td width="50%" align="right">
								<span class="label">Total Amount:</span>
							</td>
							<td width="50%" align="left">
								<span class="value-text">
								PKR ${FetchTransactionListViewModel.totalTransactionAmount}
								</span>
							</td>
						</tr>
						<tr>
							<td width="50%" align="right">
								<span class="label">Transaction ID:</span>
							</td>
							<td width="50%" align="left">
								<span class="value-text">
									<c:out value="${FetchTransactionListViewModel.tranCode}" />
								</span>
							</td>
						</tr>
						<tr>
							<td width="50%" align="right">
								<span class="label">Transaction date:</span>
							</td>
							<td width="50%" align="left">
								<span class="value-text">
									<c:out value="${FetchTransactionListViewModel.transactionDateTime}" />
								</span>
							</td>
						</tr>
						<tr>
							<td colspan="2" align="center">
								<a href="#" onclick="javascript:openPrintWindow()">Receipt</a>
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