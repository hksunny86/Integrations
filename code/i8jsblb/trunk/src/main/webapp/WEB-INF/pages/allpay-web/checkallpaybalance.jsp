<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>Check Balance</title>
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
	<script language="javascript">

		function writeError(message) {
			var msg = document.getElementById('msg');
			msg.innerHTML = message;
			msg.className = 'error-msg';
			return false;
		}


		function validateNumber(field){

			if(field.value==""){
				return false;
			}

			var charpos = field.value.search("[^0-9]");
			if(charpos >= 0) {
				return false;
			}
			return true;
		}

		function validate() {

			var code = document.getElementById('PIN');

			if(code.value.length < 4) {
				return writeError('MPIN Code length must be 4 digits');
			}

			if(!validateNumber(code)) {
				return writeError('MPIN Code must be numeric');
			}
			if(navigator.appName == 'Netscape') {
				document.getElementById("confirm").disabled = true;
			} else {
				document.getElementById("confirm").disabled = 'disabled';
			}
			return true;
		}
	</script>
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
					<html:form method="post" action="checkallpaybalance.aw" onsubmit="return validate()" commandName="agentWebFormBean">

						<table align="center" width="100%" border="0">
							<tr>
								<td align="center" id="msg" style="font-family:Arial, Helvetica, sans-serif;">
									&nbsp;
									<c:if test="${not empty errors}">
										<span class="error-msg"> <c:out value="${errors}" /> <br /> </span>
									</c:if>

									<c:if test="${empty errors}">
										<c:if test="${not empty BALF}">
											<br />
											<label class="sh">
												You have PKR
											</label>
											<label class="st">
												<c:out value="${BALF}" />
											</label>
											<label class="sh">
												in your account
											</label>
											<br />
										</c:if>
									</c:if>
								</td>
							</tr>
							<tr>
								<td >
									<div align="center" class="hf">Check Balance</div>
								</td>
							</tr>

                            <tr>
                                <td align="right" width="50%">
                                    <input checked="checked" type="radio"  name="ACCTYPE" id="ACCTYPE" value="1">
                                    <span class="label" style="padding: 0px !important;">Branchless Account</span></td>
                                <td colspan="2" align="left">
                                    <input type="radio" name="ACCTYPE" id="ACCTYPE" value="0" style="margin-right: 0px !important;margin-left: 20px;">
                                    <span class="label" style="padding-left: 0px !important;">Core Account</span>
                                </td>
                            </tr>

                            <tr>
								<td align="right" width="50%"><span class="label">MPIN :</span></td>
								<td width="50%"><html:password id='bankPin' path="bankPin" alt="Enter Bank PIN" autocomplete="off" cssClass="text" maxlength="4" size="10" /></td>
							</tr>
							<tr>
								<td><html:hidden id='deviceTypeId' path="deviceTypeId"/></td>
								<td><security:token/>&nbsp;&nbsp;<input class="mainbutton" size="7" type="submit" id="confirm" name="confirm" value="Confirm" ></td>
							</tr>
						</table>

					</html:form>
					<%-- <jsp:include page="footer.jsp"></jsp:include> --%>
		</div>
	</body>
</html>