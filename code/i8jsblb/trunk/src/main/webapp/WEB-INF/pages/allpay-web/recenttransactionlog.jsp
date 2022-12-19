
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>Recent Transaction Log</title>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/awstyle_new.css" type="text/css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/aw/aw-contents-height-setter.js"></script>
	</head>
	<body oncontextmenu="return false">
		<table border="0" height="100%" width="85%" align="center" bgcolor="white">
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
					<div align="center" class="hf">Recent Transaction Log</div>
				</td>
			</tr>
			<tr height="60%" valign="top">
				<td>
					<c:if test="${empty FetchTransactionListViewModel}">
						<table align="center" width="100%" border="0">
							<tr>
								<td align="center">
									<span class="error-msg">No transaction(s) found!</span>											
								</td>
							</tr>
						</table>
					</c:if>
					<table align="center" width="100%" border="0">
						<tr>
							<td>		
								<div align="center" style="height:300px;overflow:scroll;" >
									<table border="0" width="100%" cellpadding="0" cellspacing="1">
					
										<c:forEach items="${FetchTransactionListViewModel}"	var="transactionModel">
										<tr>
											<td align="left">
												<c:if test="${not empty transactionModel.tranCode}">
												<span class="label">
													Transaction code:
												</span>
												<span class="value-text">
													<c:out value="${transactionModel.tranCode}" />
												</span>
												<br>
												</c:if>								
												<c:if test="${not empty transactionModel.transactionDateTime}">
												<span class="label">
													Transaction date:
												</span>
												<span class="value-text">
													<c:out value="${transactionModel.transactionDateTime}" />
												</span>
												<br>
												</c:if>
												
												<c:if test="${not empty transactionModel.tranAmount}">
												<span class="label">
													Amount paid:
												</span>
												<span class="value-text">
													PKR 
													<c:out value="${transactionModel.tranAmount}" />
												</span>
												<br>
												</c:if>
												
												<c:if test="${not empty transactionModel.productName}">
												<span class="label">
													Product:
												</span>
												<span class="value-text">
													<c:out value="${transactionModel.productName}" />
												</span>
												<br>
												</c:if>
												<hr width="100%" size="1" />
												</td>
											</tr>
										</c:forEach>
									</table>
								</div>
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
