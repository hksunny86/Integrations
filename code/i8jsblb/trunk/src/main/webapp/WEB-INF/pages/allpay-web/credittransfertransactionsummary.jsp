
<%@page
	import="com.inov8.microbank.common.model.transactionmodule.FetchTransactionListViewModel"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>Transaction Summary</title>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/awstyle_new.css" type="text/css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/aw/aw-contents-height-setter.js"></script>
	</head>
	<body oncontextmenu="return false">
		<table border="0" height="100%" width="85%" align="center" class="container" cellpadding="0" cellspacing="0">
				<tr height="95px">
					<td>
						<table border="0" width="100%">
							<tr>
								<td valign="bottom" width="35%">
									<a href="mainmenu.aw?ACID=${requestScope.ACID}&BAID=${requestScope.BAID}&UID=${requestScope.UID}" title="Home">Home</a>
									&nbsp;|&nbsp;
									<a href="javascript: history.go(-1)" title="Go Back">Go Back</a>
								</td>
								<td width="30%">
									<img src="images/aw/aw_logo.png">
								</td>
								<td width="35%">&nbsp;</td>	
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<hr width="100%" size="1" align="center" />
					</td>
				</tr>
				<tr>
					<td>
						<div align="center" class="hf">Transaction Summary</div>
					</td>
				</tr>
				<tr height="60%" valign="top">
					<td>
						<marquee>
							<div class="text">
								<c:out value="${sessionScope.TSTR} " />
							</div>
						</marquee>
						<hr width="100%" size="1">
						<label class="sh">
							Credit Transfer
						</label>
						<br>
				
						<label class="sh">
							Transaction code:
						</label>
						<label class="st">
							<c:out value="${FetchTransactionListViewModel.tranCode}" />
						</label>
						<br>
						<label class="sh">
							Amount:
						</label>
						<label class="st">
							<fmt:formatNumber maxFractionDigits="2" type="currency"
								currencySymbol="PKR "
								value="${FetchTransactionListViewModel.tranAmount}"></fmt:formatNumber>
						</label>
						<br>
				
				
						<label class="sh">
							Transaction date:
						</label>
						<label class="st">
							<c:out value="${FetchTransactionListViewModel.transactionDateTime}" />
						</label>
						<br>
				
						<!-- <label class="sh">
							Supplier:
						</label>
						<label class="st">
						<c:out value="${FetchTransactionListViewModel.supplierName}" />
						</label>
						<br> -->
				
						<!-- <label class="sh">
							Paid using:
						</label>
						<label class="st">
						<c:out value="${FetchTransactionListViewModel.name}" />
						</label>
						<br> -->
						<!-- <label class="sh">
							Auth code:</label>		
						<label class="st">
						<c:out value="${FetchTransactionListViewModel.bankResponseCode}" />
						</label>
						<br>
						-->
						<label class="sh">
							Time:
						</label>
						<label class="st">
							<c:out value="${timef}" />
						</label>
						<br>
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
