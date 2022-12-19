<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/awstyle_new.css" type="text/css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/aw/aw-contents-height-setter.js"></script>
		<title>Transaction Summary</title>

		<script language="javascript">
		tansferInTransactionSummary
			function openPrintWindow() {
	
				var url = '${contextPath}'+'/recepit.aw?TRXID=${requestScope.TRXID}&PID=${param.PID}';
				printWindow = window.open(url,"receipt_print", "location=1, left=0, top=0, width=625, height=425, status=no, scrollbars=no, titlebar=no, toolbar=no, menubar=no, resizable=no");
				printWindow.focus();
			}
	
		</script>		

	</head>
	<body oncontextmenu="return false">
		<table border="0" height="100%" width="85%" align="center" bgcolor="white" cellpadding="0" cellspacing="0" class="container">
			<tr height="95px">
				<td>
					<jsp:include page="header.jsp"></jsp:include>
				</td>
			</tr>
			<tr id="contents" valign="top">
				<td>
					<table align="center" width="100%" border="0">
						<tr>
							<td colspan="2" align="center"><br><br>
							    <font color="#0066b3" size="5px">${requestScope.Heading} Transaction Summary</font><br><br>
							</td>
						</tr>
						<tr>
							<td colspan="2" align="center">
								<span class="success-msg">
									${requestScope.Heading} ${requestScope.Name}
								</span><br><br>
							</td>
						</tr>
						<c:forEach items="${responseData}" var="responseData">
							<tr>
								<td align="right" width="50%"><span class="label">${responseData.key} :</span></td>
								<td><span class="value-text">${responseData.value}</span></td>
							</tr>
			 			</c:forEach>
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
