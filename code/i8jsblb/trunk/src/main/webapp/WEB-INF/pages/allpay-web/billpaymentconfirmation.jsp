<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/taglibs.jsp"%>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>Customer Billing Information</title>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/awstyle_new.css" type="text/css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/aw/aw-contents-height-setter.js"></script>
		
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
		var CDCUSTMOB = document.getElementById('CDCUSTMOB')

		if(PIN.value == '') {
			return writeError('There is no PIN entered, Kindly Enter 4 Digit PIN Code.');		
		}

		if(!validateNumber(PIN) || PIN.value.length < 4) {
			return writeError('Kindly Enter 4 Digit PIN Code.');		
		}	
		
		if( CDCUSTMOB.value == '')
		{
			return writeError("Walkin Customer Mobile Number is required.");
		}
		else if(!validateNumber(CDCUSTMOB) || (!validateNumber(CDCUSTMOB) || CDCUSTMOB.value.length < 11)) {
				return writeError("Kindly Enter 11 Digit Walkin Customer Mobile Number.");		
		}		
		if(!isValidFormat(CDCUSTMOB.value)) {
			return writeError("Walkin Customer's Mobile # must start with 03");
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
		<table align="center" border="0" cellpadding="0" cellspacing="0" height="100%" width="85%" class="container">
			<tr height="95px">
				<td>
					<jsp:include page="header.jsp"></jsp:include>
				</td>
			</tr>
			<tr id="contents" valign="top">
				<td>
					<form name="form1" method="post" onsubmit="return validate()" action="<c:url value="/billpaymentconfirmation.aw"/>">

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
											<div align="center" class="hf">Bill Payment of ${param.PNAME} Successfull</div><br>
										</td>
									</tr>
									<tr>
										<td align="right" width="50%"><span class="label">Product Name : </span></td>
										<td width="50%"><span class="value-text">${param.PNAME}</span></td>
									</tr>
									<tr>
										<td align="right"><span class="label">Consumer No.:</span></td>
										<td><span class="value-text">${param.CSCD}</span><input type="hidden" name="CSCD" value="${param.CSCD}" /></td>
									</tr>
									<tr>
										<td align="right"><span class="label">Billing Month : </span></td>
										<td><span class="value-text">${BDATE}</span><input type="hidden" name="BDATE" value="${BDATE}" /></td>
									</tr>
									<tr>
										<td align="right"><span class="label">Due Date : </span></td>
										<td><span class="value-text">${LBDATEF}</span><input type="hidden" name="LBDATEF" value="${LBDATEF}" /></td>
									</tr>
									<tr>
										<td align="right"><span class="label">Total Amount : </span></td>
										<td><span class="value-text">${TAMTF} PKR</span><input type="hidden" name="TAMTF" value="${TAMTF}" /></td>
									</tr>
									<tr>
										<td align="right"><span class="label">Bill Paid Status : </span></td>
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
									<tr>
										<td align="right">
											<span style="color: #FF0000">*</span>
											<span class="label">Walk-in Customer Mobile No.:</span>
										</td>
										<td><input type="text" id="CDCUSTMOB" name="CDCUSTMOB" maxlength="11" value="${CDCUSTMOB}" class="text"/></td>
									</tr>		
									<tr>
										<td align="right">
											<span style="color: #FF0000">*</span>
											<span class="label">PIN : </span>
										</td>
										<td><input type="password" name="PIN" id="PIN" maxlength="4" class="text"/></td>
									</tr>		
									<tr>
										<td align="right"></td>
										<td>
										
											<c:set var="disabled" scope="page" value="disabled='disabled'"/>
											<c:if test='${BPAID == "0" || BDATEOD == "0"}'>			
												<c:set var="disabled" scope="page" value=""/>
											</c:if>
											<security:token/><input class="mainbutton" type="submit" id="confirm" name="confirm" value="Confirm" ${disabled}/>			
										</td>
									</tr>
								</table>
								
						<input type="hidden" name="SID" id="SID" value="${param.SID}" />
						<input type="hidden" name="supplierId" id="supplierId" value="${param.supplierId}" />
						<input type="hidden" name="PID" id="PID" value="${param.PID }" />
						<input type="hidden" name="dfid" id="dfid" value="${param.dfid }" />
						<input type="hidden" name="PNAME" id="PNAME" value="${param.PNAME }" />
						<input type="hidden" name="billServiceLabel" id="billServiceLabel" value="${param.billServiceLabel }" />
						
						<input type="hidden" name="UID" id="UID" value="${requestScope.UID }" />
						<input type="hidden" name="ACID" id="ACID" value="${requestScope.ACID}" />
						<input type="hidden" name="BAID" id="BAID" value="${requestScope.BAID}" />
						<input type="hidden" name="TXAM" value="${param.TXAM}"/>
						<input type="hidden" name="TXAMF" value="${TXAMF}"/>
						<input type="hidden" name="MOBN" value="${MOBN}"/>			
						<input type="hidden" name="reqTime" value="<%=System.currentTimeMillis()%>"/>
						<input type="hidden" name="ACID" value="${param.ACID}"/>
							
						<input type="hidden" name="CAMT" value="${requestScope.CAMT}"/>
						<input type="hidden" name="TPAM" value="${requestScope.TPAM}"/>
						<input type="hidden" name="MOBN" value="${requestScope.MOBN}"/>
						<input type="hidden" name="RPNAME" value="${requestScope.RPNAME}"/>
						<input type="hidden" name="TAMT" value="${requestScope.TAMT}"/>
						<input type="hidden" name="CAMTF" value="${requestScope.CAMTF}"/>
						<input type="hidden" name="TPAMF" value="${requestScope.TPAMF}"/>
						<input type="hidden" name="TAMTF" value="${requestScope.TAMTF}"/>
						<input type="hidden" name="BAMT" value="${requestScope.BAMT}"/>		
						
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