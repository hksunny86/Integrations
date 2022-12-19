<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="com.inov8.microbank.webapp.action.allpayweb.Encryption"%>
<%@include file="/common/taglibs.jsp"%>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>Cash In</title>
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
		
		var PIN = document.getElementById('PIN');	
		var CNIC = document.getElementById('CNIC');	
		var CDCUSTMOB = document.getElementById('CDCUSTMOB');	

		if(!validateNumber(PIN) || PIN.value.length < 4) {
			return writeError('Kindly Enter 4 Digit PIN Code.');		
		}		

		if(CNIC.value != '') {
			if(!validateNumber(CNIC) || CNIC.value.length < 13) {
				return writeError('Kindly Enter 13 Digit Walkin Customer CNIC Number.');		
			}
			
			if((document.getElementById('cnicCustomer').innerHTML) == CNIC.value) {
				return writeError("Account Holder's CNIC # and Walkin Customer CNIC # can't be same.");
			}
			
		}
		
		if(CDCUSTMOB.value != '') {
			
			if(!validateNumber(CDCUSTMOB) || CDCUSTMOB.value.length < 11) {
				return writeError('Kindly Enter 11 Digit Walkin Customer Mobile Number.');		
			}	
			
			if((document.getElementById('CMOB').innerHTML) ==  CDCUSTMOB.value) {
				return writeError("Account Holder's Mobile # and Walkin Customer Mobile # can't be same.");
			}
			
			if(CNIC.value == '') {
				return writeError("Walkin Customer CNIC is required.");
			}
		}			
		
		if(navigator.appName == 'Netscape') {		
			document.getElementById("confirm").disabled = true;
		} else {
			document.getElementById("confirm").disabled = 'disabled';
		}
		
		return true;
	}

	function goHome( )
	{
		var homeUrl = "mainmenu.aw?ACID=" + ${ACID} + "&BAID=" + ${BAID} + "&UID=" + ${sessionScope.UID};
		window.location.replace( homeUrl );
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
		<form method="post" action="<c:url value="/productpurchaseconfirmation.aw"/>" onsubmit="return validate()">
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
						<div align="center" class="hf">Cash In</div>
					</td>
				</tr>
				<tr>
					<td align="center" colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td align="right" width="50%"><span class="label">Mobile #:</span></td>
					<td width="50%"><span class="value-text" id="CMOB">&nbsp;&nbsp;<c:out value="${param.CMOB}"></c:out></span></td>
				</tr>
				<tr>
					<td align="right"><span class="label">CNIC Number:</span></td>
					<td><span class="value-text" id='CNIC'>&nbsp;&nbsp;<c:out value="${CNIC}"></c:out></span></td>
				</tr>
				<tr>
					<td align="right"><span class="label">Amount:</span></td>
					<td><span class="value-text">&nbsp;&nbsp;<fmt:formatNumber maxFractionDigits="2" type="currency" currencySymbol="PKR " value="${param.TXAM}"></fmt:formatNumber></span></td>
				</tr>
                <c:if test="${not empty isValid and isValid eq 1}">
                    <tr>
                        <td align="right"><span class="label">Charges:</span></td>
                        <td><span class="value-text" id='CAMT'>&nbsp;&nbsp;<c:out value="${CAMT}"></c:out></span></td>
                    </tr>
                    <tr>
                        <td align="right"><span class="label">Total Amount:</span></td>
                        <td><span class="value-text" id='TAMT'>&nbsp;&nbsp;<c:out value="${TAMT}"></c:out></span></td>
                    </tr>
                    <tr>
                        <td align="right"><span class="label">Transaction ID:</span></td>
                        <td><span class="value-text" id='TRXID'>&nbsp;&nbsp;<c:out value="${TRXID}"></c:out></span></td>
                    </tr>
                    <tr>
                        <td align="right"><span class="label">Date & Time:</span></td>
                        <td><span class="value-text" id='TIMEF'>&nbsp;&nbsp;<c:out value="${TIMEF}"></c:out></span></td>
                    </tr>
                </c:if>
				<tr>
                    <c:if test="${not empty isValid and isValid eq 0}">
                        <td align="right">
                            <span class="label">PIN Code:*</span>
                        </td>
                        <td><input type="password" id="PIN" name="PIN" maxlength="4" autocomplete="off" class="text"/></td>
                    </c:if>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td align="right"></td>
					<td><security:token/>
                        <c:if test="${not empty isValid and isValid eq 0}">
                            <input class="mainbutton" type="submit" id="confirm" name="confirm" value="Confirm"/>
                            <input class="mainbutton" type="button" name="cancel" value="Cancel" onclick="goHome( );"/>
                        </c:if>
					</td>
				</tr>
			</table>

			<c:if test="${not empty errors}">
				<input type="hidden" name="PID" value="<%= request.getParameter("PID") %>"/>
				<input type="hidden" name="dfid" value="<%= request.getParameter("dfid") %>"/>
				<input type="hidden" name="PNAME" value="<%= request.getParameter("PNAME") %>"/>
			</c:if>
			<c:if test="${empty errors}">
				<input type="hidden" name="PID" value="<%= Encryption.decrypt(request.getParameter("PID")) %>"/>
				<input type="hidden" name="dfid" value="<%= Encryption.decrypt(request.getParameter("dfid")) %>"/>
				<input type="hidden" name="PNAME" value="<%= Encryption.decrypt(request.getParameter("NAME")) %>"/>
			</c:if>

            <input type="hidden" name="TRXID" value="${param.TXAM}"/>
			<input type="hidden" name="TXAM" value="${param.TXAM}"/>
			<input type="hidden" name="TXAMF" value="${TXAMF}"/>
			<input type="hidden" name="CMOB" value="${CMOB}"/>
			<input type="hidden" name="CNIC" value="${CNIC}"/>
			<input type="hidden" name="reqTime" value="<%=System.currentTimeMillis()%>"/>

			<input type="hidden" name="CAMT" value="${CAMT}"/>
			<input type="hidden" name="CAMTF" value="${CAMTF}"/>
			<input type="hidden" name="TPAM" value="${TPAM}"/>
			<input type="hidden" name="TPAMF" value="${TPAMF}"/>
			<input type="hidden" name="CMOB" value="${MOBN}"/>
			<input type="hidden" name="RPNAME" value="${RPNAME}"/>
			<input type="hidden" name="TAMT" value="${TAMT}"/>
			<input type="hidden" name="TAMTF" value="${TAMTF}"/>
			<input type="hidden" name="BAMT" value="${TXAM}"/>
			<input type="hidden" name="PIN_RETRY_COUNT" value="${PIN_RETRY_COUNT}"/>
		</form>
	</div>
	</body>
</html>
