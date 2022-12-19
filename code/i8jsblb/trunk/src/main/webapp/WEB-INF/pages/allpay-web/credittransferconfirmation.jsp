
<%@page
	import="com.inov8.microbank.common.model.smartmoneymodule.SmAcctInfoListViewModel"%>
<%@page import="java.util.ArrayList"%>
<%@include file="/common/taglibs.jsp"%>
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
		<table align="center" border="0" cellpadding="0" cellspacing="0" height="100%" width="85%" class="container">
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
					<div align="center" class="hf">Credit Transfer Confirmation</div>
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
					<c:if test="${not empty errors}">
						<font color="#FF0000">
							<c:out value="${errors}" />
							<br/>
						</font>
					</c:if>
					<form method="post" action="<c:url value="/credittransferconfirmation.aw"/>">
						
						<span class="sh">
						Credit Transfer
						</span>
			
						<input type="hidden" name="UID" value="${param.UID}"/>
						<input type="hidden" name="APID" id="APID" value="${param.APID}"/>
						<input type="hidden" name="TXAM" value="${param.TXAM}"/>
						<input type="hidden" name="TXAMF" value="${TXAMF}"/>
						<input type="hidden" name="MOBN" value="${MOBN}"/>
						<input type="hidden" name="BAID" value="${param.BAID}"/>
						<input type="hidden" name="PNAME" value="${param.PNAME}"/>
						<input type="hidden" name="reqTime" value="<%=System.currentTimeMillis()%>"/>			
						<br/>
						<span class="label">Retailer ID:
						</span>
						<span class="value-text">		
						<c:out value="${param.APID}"></c:out>
						</span>
						<br/>	
						<span class="label">Amount:
						</span>
						<span class="st">	
						PKR  	
						<c:out value="${param.TXAM}"></c:out>
						</span>
						<br/>																			
						<hr width="100%" size="1">
						<input class="mainbutton" type="submit" name="confirm" value="Confirm">
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
