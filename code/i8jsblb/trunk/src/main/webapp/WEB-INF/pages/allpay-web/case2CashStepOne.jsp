<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="com.inov8.microbank.common.util.CommandFieldConstants" %>
<%@include file="/common/taglibs.jsp" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>Cash Transfer</title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,IE=11,IE=10"/>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/awstyle_new.css" type="text/css">
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.7.2.min.js"></script>
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
	<script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
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
			height: 430px;

		}
	</style>
	<script language="javascript">

        function writeError(message) {
            var msg = document.getElementById('msg');
            msg.innerHTML = message;
            msg.className = 'error-msg';
            return false;
        }

        function isValidFormat(mobileNumber) {

            return (mobileNumber.substring(0, 2) == '03');
        }

        function isValidateTrxCode(code) {
            if (code.value == "") {
                return false;
            }

            var charpos = code.search("[^0-9]");
            if (charpos >= 0) {
                return false;
            }
            return true;
        }

        function validate() {

            var walikInCustCnic = document.getElementById('walikInCustCnic').value;
            var walikInCustMobNo = document.getElementById('walikInCustMobNo').value;
            var receiverCnic = document.getElementById('receiverCnic').value;
            var receiverMobNo = document.getElementById('receiverMobNo').value;
            var amount = document.getElementById('amount').value;

            if (walikInCustCnic.length == 0) {
                return writeError('Sender CNIC is required.');
            }
            if (walikInCustCnic.length != 13 || !isNumber(walikInCustCnic)) {
                return writeError('Sender CNIC must be 13 digits.');
            }

            if (walikInCustMobNo.length == 0) {
                return writeError('Sender Mobile Number is required.');
            }
            if (walikInCustMobNo.length != 11 || !isNumber(walikInCustMobNo)) {
                return writeError('Sender Mobile Number must be 11 digits.');
            }

            if (!isValidFormat(walikInCustMobNo)) {
                return writeError("Sender Mobile Number must start with 03");
            }

            if (receiverCnic.length == 0) {
                return writeError('Receiver CNIC  is required.');
            }
            if (receiverCnic.length != 13 || !isNumber(receiverCnic)) {
                return writeError('Receiver CNIC   must be 13 digits.');
            }

            if (walikInCustCnic == receiverCnic) {
                return writeError('Sender/Receiver CNIC and Mobile No information cannot be same.');
            }

            if (receiverMobNo.length == 0) {
                return writeError('Receiver Mobile Number is required.');
            }
            if (receiverMobNo.length != 11 || !isNumber(receiverMobNo)) {
                return writeError('Receiver Mobile Number must be 11 digits.');
            }

            if (!isValidFormat(receiverMobNo)) {
                return writeError("Receiver's Mobile Number must start with 03");
            }

            if (walikInCustMobNo == receiverMobNo) {
                return writeError('Sender/Receiver CNIC and Mobile No information cannot be same.');
            }

            if (amount.length == 0 || !isNumber(amount)) {
                return writeError('Amount is required in Digits.');
            }

            if (amount == 0) {
                return writeError('Amount should be greater than 0.');
            }



            if (navigator.appName == 'Netscape') {
                document.getElementById("confirm").disabled = true;
            } else {
                document.getElementById("confirm").disabled = 'disabled';
            }

            return true;
        }


	</script>

</head>
<body oncontextmenu="return false">


<div style="height:95px%">
	<jsp:include page="header.jsp"></jsp:include>
</div>
<div class="vertical-left-menu ">
	<jsp:include page="agentWebMenu.jsp"></jsp:include>
</div>
<div class="main-body-area">
	<form method="post" action="<c:url value="/case2CashStepOne.aw"/>" onsubmit="return validate()">

		<table align="center" width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td align="center" colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td align="center" colspan="2" id="msg">
					&nbsp;
					<c:if test="${not empty errors}">
										<span class="error-msg">
											<c:out value="${errors}"/>
											<br/>
										</span>
					</c:if>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<div align="center" class="hf">Money Transfer(CNIC to CNIC)</div>
				</td>
			</tr>
			<tr>
				<td align="center" colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td align="right" width="50%">
					<span style="color: #FF0000">*</span>
					<span class="label">Sender CNIC :</span>

				</td>
				<td><input type="text" id="walikInCustCnic" name="<%= CommandFieldConstants.KEY_S_W_CNIC %>"
						   value="${SWCNIC}" onkeypress="return maskNumber(this,event); return isNumberKey(event)"
						   maxlength="13" class="text"/></td>
			</tr>
			<tr>
				<td align="right" width="50%">
					<span style="color: #FF0000">*</span>
					<span class="label">Sender Mobile Number :</span>

				</td>
				<td><input type="text" id="walikInCustMobNo"
						   name="<%= CommandFieldConstants.KEY_WALKIN_SENDER_MOBILE %>" value="${SWMOB}"
						   onkeypress="return maskNumber(this,event)" maxlength="11" class="text"/></td>
			</tr>
			<tr>
				<td align="right" width="50%">
					<span style="color: #FF0000">*</span>
					<span class="label">Receiver CNIC :</span>

				</td>
				<td><input type="text" id="receiverCnic" name="<%= CommandFieldConstants.KEY_R_W_CNIC %>"
						   value="${RWCNIC}" onkeypress="return maskNumber(this,event)" maxlength="13" class="text"/>
				</td>
			</tr>
			<tr>
				<td align="right" width="50%">
					<span style="color: #FF0000">*</span>
					<span class="label">Receiver Mobile Number :</span>

				</td>
				<td><input type="text" id="receiverMobNo"
						   name="<%= CommandFieldConstants.KEY_RECIPIENT_WALKIN_MOBILE %>" value="${RWMOB}"
						   onkeypress="return maskNumber(this,event)" maxlength="11" class="text"/></td>
			</tr>
			<tr>
				<td align="right" width="50%">
					<span style="color: #FF0000">*</span>
					<span class="label">Amount :</span>

				</td>
				<td><input type="text" id="amount" name="<%= CommandFieldConstants.KEY_TX_AMOUNT %>"
						   value="${param.TXAM}" onkeypress="return maskNumber(this,event)" maxlength="7" class="text"/>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					&nbsp;<input type="hidden" name="<%= CommandFieldConstants.KEY_PROD_ID %>" value="50011"/>
					<input type="hidden" name="<%= CommandFieldConstants.KEY_U_ID %>" value="${UID}"/>
					<input type="hidden" name="<%= CommandFieldConstants.KEY_TPAM %>" value="0"/>
					<input type="hidden" name="<%= CommandFieldConstants.KEY_COMM_AMOUNT %>" value="0"/>
				</td>
			</tr>
			<tr>
				<td align="right"></td>
				<td><security:token/>
					<input class="mainbutton"
						   type="submit" id="confirm"
						   name="confirm"
						   style="width: 180px;"
						   value="Next"/>
				</td>
			</tr>
		</table>
	</form>
</div>
</body>
</html>
