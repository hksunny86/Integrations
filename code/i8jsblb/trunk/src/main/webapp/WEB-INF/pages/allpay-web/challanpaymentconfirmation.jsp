<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/taglibs.jsp"%>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>Challan Information</title>
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
	
	function isValidFormat(mobileNumber) {
				
		return (mobileNumber.substring(0,2) == '03') ;
	}	
	
	function validate() {
		
		var PIN = document.getElementById('PIN');	

		if(PIN.value == '') {
			return writeError('There is no PIN entered, Kindly Enter 4 Digit PIN Code.');		
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
		<table align="center" border="0" cellpadding="0" cellspacing="0" height="100%" width="85%" class="container">

			<tr id="contents" valign="top">
				<td>
					<form name="form1" method="post" onsubmit="return validate()" action="<c:url value="/challanpaymentconfirmation.aw"/>">

								<table align="center" width="100%" border="0">
									<tr>
										<td colspan="2">&nbsp;</td>
									</tr>
									<tr>
										<td align="center" colspan="2" id="msg">&nbsp;				
											<div>							
											<c:if test="${not empty errors}">
												<font color="#FF0000"> <c:out value="${errors}" /> <br /> </font>
											</c:if>
											</div>
										</td>
									</tr>
									<tr>
										<td colspan="2" align="center">
											<div><center><img style="width: 10%" src="images/aw/itc_logo.jpg"/></center></div>
											<div align="center" class="hf">${PNAME}</div><br>
										</td>
									</tr>
									<tr>
										<td align="right" width="50%"><span class="label">Transaction Type : </span></td>
										<td width="50%"><span class="value-text">${PNAME}</span></td>
									</tr>
									<tr>
										<td align="right"><span class="label">Challan No.:</span></td>
										<td><span class="value-text">${CONSUMER}</span><input type="hidden" name="CONSUMER" value="${CONSUMER}" /></td>
									</tr>
									<tr>
										<td align="right"><span class="label">Customer Mobile No.:</span></td>
										<td><span class="value-text">${CMOB}</span><input type="hidden" name="CMOB" value="${CMOB}" /></td>
									</tr>
									<tr>
										<td align="right"><span class="label">Due Date : </span></td>
										<td><span class="value-text">${DUEDATE}</span><input type="hidden" name="DUEDATE" value="${DUEDATE}" /></td>
									</tr>
									<tr>
										<td align="right"><span class="label">Amount : </span></td>
										<td><span class="value-text">${BAMT} PKR</span><input type="hidden" name="BAMT" value="${BAMT}" /></td>
									</tr>
									<tr>
										<td align="right"><span class="label">Charges : </span></td>
										<td><span class="value-text">${TPAMF} PKR</span><input type="hidden" name="TPAM" value="${TPAMF}" /></td>
									</tr>
									<tr>
										<td align="right"><span class="label">Total Amount : </span></td>
										<td><span class="value-text">${TAMTF} PKR</span><input type="hidden" name="TAMTF" value="${TAMTF}" /></td>
									</tr>
									<tr>
										<td align="right"><span class="label">Status : </span></td>
										<td>
											<span class="value-text">
												<c:if test='${BPAID == "1"}'>
													<span class="value-text">
														Paid
													</span>
												</c:if>
												<c:if test='${BPAID == "0"}'>
													<span class="value-text">
														UnPaid
													</span>
												</c:if>							
											</span><input type="hidden" name="BPAID" value="${BPAID}" />
										</td>
									</tr>
									<c:if test='${BPAID == "0"}'>
									<tr>
										<td align="right">
											<span style="color: #FF0000">*</span>
											<span class="label">PIN : </span>
										</td>
										<td><input type="password" name="PIN" id="PIN" maxlength="4" class="text" onkeypress="return maskInteger(this, event)"/></td>
									</tr>		
									<tr>
										<td align="right"></td>
										<td>
											<security:token/>
											<input class="mainbutton" type="submit" id="confirm" name="confirm" value="Confirm"/>
										</td>
									</tr>
									</c:if>
									<c:if test='${BPAID == "1"}'>
										<tr>
											<td align="right"></td>
											<td>
												<security:token/>
												<input class="mainbutton" type="button" id="backbtn" name="backbtn" value="Back" onclick="window.location='challanpaymentform.aw'"/>
											</td>
										</tr>
									</c:if>
								</table>
								

						<input type="hidden" name="PID" id="PID" value="${PID }" />
						<input type="hidden" name="PNAME" id="PNAME" value="${PNAME }" />
						<input type="hidden" name="UID" id="UID" value="${UID}" />
						<input type="hidden" name="AMOB" id="AMOB" value="${AMOB}" />
						<input type="hidden" name="CONSUMER" id="CONSUMER" value="${CONSUMER}" />
						<input type="hidden" name="BAMTF" id="BAMTF" value="${BAMTF}" />
						<input type="hidden" name="BPAID" id="BPAID" value="${BPAID}" />
						<input type="hidden" name="DUEDATE" id="DUEDATE" value="${DUEDATE}" />
						<input type="hidden" name="DUEDATEF" id="DUEDATEF" value="${DUEDATEF}" />
						<input type="hidden" name="ISOVERDUE" id="ISOVERDUE" value="${ISOVERDUE}" />
						<input type="hidden" name="TAMT" id="TAMT" value="${TAMT}" />
						<input type="hidden" name="TAMTF" id="TAMTF" value="${TAMTF}" />
						<input type="hidden" name="CAMTF" id="CAMTF" value="${CAMTF}" />
						<input type="hidden" name="TXAM" id="TXAM" value="${BAMT}"/>

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