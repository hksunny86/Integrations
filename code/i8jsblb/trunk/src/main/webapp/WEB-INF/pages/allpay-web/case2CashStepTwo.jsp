<%@page
		import="com.inov8.microbank.common.model.smartmoneymodule.SmAcctInfoListViewModel"%>
<%@page import="java.util.ArrayList"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>Pay Cash</title>
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

            var transactionId = document.getElementById('transactionId');

            if(transactionId.value.length < 12) {
                return writeError('Transaction Id length must be 12 Digits');
            }

            if(!validateNumber(transactionId)) {
                return writeError('Transaction Id Must be Numeric');
            }

            if(navigator.appName == 'Netscape') {
                document.getElementById("confirm").disabled = true;
            }

            return true;
        }

        function openPrintWindow() {

            var url = '${contextPath}'+'/recepit.aw?TRXID=${requestScope.TRXID}&PID=50011';
            printWindow = window.open(url,"receipt_print", "location=1, left=0, top=0, width=625, height=500, status=no, scrollbars=no, titlebar=no, toolbar=no, menubar=no, resizable=no");

            printWindow.focus();
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
				<td colspan="2" align="center">
					<br></br><br></br>
					<font color="#ed0282" size="5px">Successful Transfer</font>
				</td>
			</tr>
			<tr>
				<td align="center" colspan="2" id="msg">
					&nbsp;
					<c:if test="${not empty errors}">
										<span class="error-msg">
											<c:out value="${errors}" />
											<br/>
										</span>
					</c:if>
				</td>
			</tr>


			<tr>
				<td align="right" width="50%"><span class="label">Sender Mobile No :</span></td>
				<td><span class="value-text">${requestScope.SWMOB}</span></td>
			</tr>

			<tr>
				<td align="right" width="50%"><span class="label">Sender CNIC :</span></td>
				<td><span class="value-text">${requestScope.SWCNIC}</span></td>
			</tr>

			<tr>
				<td align="right" width="50%"><span class="label">Receiver Mobile No :</span></td>
				<td><span class="value-text">${requestScope.RWMOB}</span></td>
			</tr>

			<tr>
				<td align="right" width="50%"><span class="label">Receiver CNIC :</span></td>
				<td><span class="value-text">${requestScope.RWCNIC}</span></td>
			</tr>

			<tr>
				<td align="right" width="50%"><span class="label">Amount :</span></td>
				<td><span class="value-text">${requestScope.TXAMF}</span></td>
			</tr>

			<tr>
				<td align="right" width="50%"><span class="label">Charges :</span></td>
				<td><span class="value-text">${requestScope.TPAMF}</span></td>
			</tr>
			<tr>
				<td align="right" width="50%"><span class="label">Total Amount :</span></td>
				<td><span class="value-text">${TAMTF}</span></td>
			</tr>

			<tr>
				<td align="right" width="50%"><span class="label">Transaction ID  :</span></td>
				<td><span class="value-text">${requestScope.TRXID}</span></td>
			</tr>

			<tr>
				<td align="right" width="50%"><span class="label">Date & Time  :</span></td>
				<td><span class="value-text">${requestScope.DATEF}</span></td>
			</tr>







			<tr>
				<td align="center" colspan="2">
					<a href="#" onclick="javascript:openPrintWindow()">Receipt</a>
				</td>
			</tr>
		</table>
	</form>
</div>
</body>
</html>