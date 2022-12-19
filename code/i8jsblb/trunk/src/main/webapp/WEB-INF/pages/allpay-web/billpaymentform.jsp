<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>Bill Payment</title>
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
	
	function validate() {
		
		var CSCD = document.getElementById('CSCD');	

		if(CSCD.value == '') {
			return writeError('Consumer Number is required.');	
		}else if(CSCD.value.length < 5) {
			return writeError('Consumer Number must be atleast Five Digit.');	
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
		<table cellpadding="0" cellspacing="0" height="100%" width="85%" align="center" class="container">
			<tr height="95px">
				<td>
					<jsp:include page="header.jsp"></jsp:include>
				</td>
			</tr>
			<tr id="contents" valign="top">
				<td>
					<form name="billpaymentform" method="post"
						action="<c:url value="/billpaymentform.aw"/>" onsubmit="return validate()">
						<table align="center" width="100%" border="0">
							<tr>
								<td colspan="2">&nbsp;</td>
							</tr>
							<tr>
								<td align="center" colspan="2" id="msg">
									&nbsp;
									<c:if test="${not empty valErrors}">
										<font color="#FF0000"> <c:out value="${valErrors}" />
										</font>
									</c:if>
								</td>
							</tr>
							<tr>
								<td colspan="2" align="center">
									<div align="center" class="hf">Bill Payment of ${requestScope.PNAME} </div><br>
								</td>
							</tr>
							<tr>
								<td align="right" width="50%">
									<span class="label">Consumer Number:</span>
								</td>
								<td>
									<input type="text" id="CSCD" name="CSCD" maxlength="20" size="11" style="-wap-input-format: '*N'; -wap-input-required: true" class="text" />
								</td>
							</tr>
							<tr>
								<td align="right"></td>
								<td><security:token/>&nbsp;&nbsp;<input name="next" class="mainbutton" type="submit" id="confirm" value="Next" /></td>
							</tr>
						</table>
													
								
						<input type="hidden" name="SID" id="SID" value="${param.SID}" />
						<input type="hidden" name="supplierId" id="supplierId" value="${param.supplierId}" />
						<input type="hidden" name="billServiceLabel" id="billServiceLabel" value="${param.billServiceLabel }" />
						
						<input type="hidden" name="PID" value="${requestScope.PID}"/>
						<input type="hidden" name="PNAME" value="${requestScope.PNAME}"/>
						<input type="hidden" name="dfid" value="${param.dfid}"/>
						<input type="hidden" name="TXAM" value="${param.TXAM}"/>
						<input type="hidden" name="TXAMF" value="${TXAMF}"/>
						<input type="hidden" name="reqTime" value="<%=System.currentTimeMillis()%>"/>
							
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
