<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page	import="com.inov8.microbank.common.util.CommandFieldConstants" %>
<%@include file="/common/taglibs.jsp"%>
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
		
				if(!validateNumber(PIN) || PIN.value.length < 4) {
					return writeError('Kindly Enter 4 Digit PIN.');		
				}
				
				if(navigator.appName == 'Netscape') {	
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
	
		<table align="center" bgcolor="white" border="0" cellpadding="0" cellspacing="0" height="100%" width="85%" class="container">
			
			<tr id="contents" valign="top">
				<td>
					<form method="post" action="<c:url value="/payCashStepTwo.aw"/>" onsubmit="return validate()">			
						
						<table align="center" width="100%" border="0">
							<tr>
								<td colspan="2" align="center">
									&nbsp;
								</td>
							</tr>
							<tr>
								<td align="center" colspan="2" id='msg'>
									&nbsp;
									<c:if test="${not empty errors}">
										<font color="#FF0000"> <c:out value="${errors}" />
										</font>									
									</c:if>								
								</td>
							</tr>
							<tr>
								<td colspan="2" align="center">
									<div align="center" class="hf">${requestScope.TRANSACTION_TYPE} Confirmation</div><br>
								</td>
							</tr>
							<tr>
								<td align="right" width="50%"><span class="label">Customer Name :</span></td>
								<td>
									<span class="value-text">${requestScope.CUSTOMER_NAME}</span>
									<input type="hidden" name="MS_ISDN_MOBILE" value="${requestScope.MS_ISDN_MOBILE}"/>									
								</td>
							</tr>
							<tr>
								<td align="right"><span class="label">CNIC :</span></td>
								<td>
									<span class="value-text">${requestScope.MS_CUSTOMER_CNIC}</span>					
								</td>
							</tr>
							<tr>
								<td align="right"><span class="label">Phone/Mobile # :</span></td>
								<td>
									<span class="value-text">${requestScope.MS_ISDN_MOBILE}</span>					
								</td>
							</tr>
							<tr>
								<td align="right"><span class="label">Transaction ID :</span></td>
								<td>
									<span class="value-text">${requestScope.TRXID}</span>
									<input type="hidden" name="<%= CommandFieldConstants.KEY_TX_ID %>" value="${requestScope.TRXID}"/>
								</td>
							</tr>
							<tr>
								<td align="right"><span class="label">Date :</span></td>
								<td><span class="value-text">${requestScope.TRANSACTION_DATE}</span></td>
							</tr>
							<tr>
								<td align="right"><span class="label">Time :</span></td>
								<td><span class="value-text">${requestScope.TRANSACTION_TIME}</span></td>
							</tr>
							<tr>
								<td align="right"><span class="label">Amount :</span></td>
								<td><span class="value-text">${requestScope.TRANSACTION_AMOUNT}</span></td>
							</tr>
							<!--
							<tr>
								<td align="right"><span class="label">Service Charges :</span></td>
								<td><span class="value-text">${requestScope.DEDUCTION_AMOUNT}</span></td>
							</tr>
							<tr>
								<td align="right"><span class="label">Total Amount :</span></td>
								<td>
									<span class="value-text">${requestScope.TOTAL_AMOUNT}</span>
								</td>
							</tr>
							-->
							<tr>
								<td align="right"><span class="label">PIN :</span><span style="color: #FF0000">*</span></td>
								<td><input type="password" id="<%= CommandFieldConstants.KEY_PIN %>" name="<%= CommandFieldConstants.KEY_PIN %>" size="9" maxlength="4" class="text"/></td>
							</tr>
							<tr>
								<td align="right"><input type="hidden" name="<%= CommandFieldConstants.KEY_PROD_ID %>" value="${requestScope.PID}"/></td>
								<td><security:token/><input class="mainbutton" type="submit" id="confirm" name="confirm" value="Confirm"/></td>
							</tr>
						</table>

					</form>
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