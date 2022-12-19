
<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>Credit Transfer</title>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/awstyle_new.css" type="text/css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/aw/aw-contents-height-setter.js"></script>
	</head>

	<body oncontextmenu="return false">
		<table border="0" height="100%" width="85%" align="center" class="container">
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
					<marquee>
						<div class="text">
							<c:out value="${sessionScope.TSTR} " />
						</div>
					</marquee>
				</td>
			</tr>
			<tr>
				<td>
					<div align="center" class="hf">
						<c:out value="${param.productname}" />
						Credit Transfer Form
					</div>
				</td>
			</tr>
			<tr height="60%" valign="top">
				<td>
					<form name="billpaymentform" method="post"
						action="<c:url value="/allpaycredittransfer.aw"/>">
						<input type="hidden" name="PID" value="${param.PID}">
						<input type="hidden" name="dfid" value="${param.dfid}">
						<input type="hidden" name="dfid" value="${param.UID}">
						<input type="hidden" name="usty" value="${param.USTY}">
						<input type="hidden" name="productname" value="${param.productname}">
						<div class="text">
							Enter Amount:
						</div>
						<input type="text" name="TXAM" maxlength="6" size="11"
							style="-wap-input-format: '*N'; -wap-input-required: true" value="${amount}" class="text"/>
						<div class="text">
			
							<c:if test="${sessionScope.USTY == 4}">
									Enter Agent/Retailer ID:
								</c:if>
							<c:if test="${sessionScope.USTY == 3}">
									Enter Retailer ID:
									</c:if>
						</div>
						<input type="text" name="APID" maxlength="12" size="11"
							style="-wap-input-format: 'NNNNNNNNNNNN'; -wap-input-required: true" value="${allPayId}" class="text"/>
						<div>
							<input name="next" class="mainbutton" type="submit" id="next" value="Next" />
						</div>
					</form>
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
