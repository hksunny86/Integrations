<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>Notification</title>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,IE=11,IE=10"/>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/awstyle_new.css" type="text/css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/aw/aw-contents-height-setter.js"></script>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/jquery.navgoco.css" type="text/css">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/demo.css" type="text/css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.cookie.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.cookie.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.navgoco.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.navgoco.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/agentWebMenu.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/highlight.pack.js"></script>

		<style type="text/css">
			* {
				box-sizing: border-box;
			}

			.header {
				border: 1px solid red;
				padding: 15px;
			}

			.vertical-left-menu {
				width: 20%;
				float: left;
				padding: 15px;
				overflow-y: scroll;
				height: 500px;

			}

			.main-body-area {
				width: 80%;
				float: left;
				padding: 15px;
				height: 450px;

			}
		</style>
	</head>

	<body oncontextmenu="return false">
	<div style="height:95px%">
		<jsp:include page="header.jsp"></jsp:include>
	</div>
	<div class="vertical-left-menu ">
		<jsp:include page="agentWebMenu.jsp"></jsp:include>
	</div>
	<div class="main-body-area">
		<table border="0" height="100%" width="85%" align="center" class="container" cellpadding="0" cellspacing="0">
			<tr height="95px">
				<td>
					<%--<jsp:include page="header.jsp"></jsp:include>--%>
				</td>
			</tr>
			<tr id="contents" valign="top">
				<td>
					<table align="center" width="100%" border="0">
						<tr>
							<td align="center">
								<br></br><br></br>
								<c:if test="${not empty errors}">
									<font color="red" size="5px">${requestScope.HEADING}</font>
								</c:if>
								<c:if test="${not empty mesg}">
									<font color="#0066b3" size="5px">${requestScope.HEADING}</font>
								</c:if>
							    <br></br>						
							</td>
						</tr>
						<tr>
							<td align="center">
								<c:if test="${not empty errors}">
									<span class="error-msg">
										<c:out value="${errors}" />
									</span>
								</c:if>
								<c:if test="${not empty mesg}">
									<span class="success-msg">
										<c:out value="${mesg}" />
									</span>
								</c:if>							
							</td>
						</tr>
					</table>
					<input type="hidden" name="UID" id="UID" value="${UID}">
				</td>
			</tr>
			<tr valign="bottom">
				<td>
					<jsp:include page="footer.jsp"></jsp:include>
				</td>
			</tr>
		</table>
	</div>
	</body>
</html>
