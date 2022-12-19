<%@include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>Welcome ${fName}&nbsp;${lName}</title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,IE=11,IE=10"/>
	<link rel="stylesheet"
		  href="${pageContext.request.contextPath}/styles/awstyle_new.css"
		  type="text/css">
	<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/jquery-1.7.2.min.js"></script>
	<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/aw/aw-contents-height-setter.js"></script>
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
	<table align="center" width="100%" border="0">
		<tr>
			<td>
				<div align="center" class="hf">
					Welcome
					<c:out value="${fName}"/>
					<c:out value="${lName} "/>

				</div>
			</td>
		</tr>
		<tr>
			<td>
				<div align="center">
					<font color="#ed0282"> <c:out value="${msg} "/>
					</font>
				</div>
			</td>

			<c:remove var="messages" scope="session" />
		</tr>
	</table>
</div>
</body>
</html>