<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page
	import="com.inov8.microbank.common.model.smartmoneymodule.SmAcctInfoListViewModel"%>
<%@include file="/common/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Till Balance Required</title>
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

			function validate() {

				var tbrAmount = document.getElementById('TBAM');

				if(!validateNumber(tbrAmount)) {
					return writeError('Amount Must be Numeric');		
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
		<table align="center" bgcolor="white" border="0" cellpadding="0" cellspacing="0" height="100%" width="85%" class="container">
			<tr height="95px">
				<td>
					<jsp:include page="header.jsp"></jsp:include>
				</td>
			</tr>
			<tr id="contents" valign="top">
				<td>
					<form method="post" action='<c:url value="/tillBalance.aw"/>' onsubmit="return validate()">

						<table id="contents" align="center" width="100%" border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td colspan="2">&nbsp;<br/><br/></td>
							</tr>
							<tr>
								<td align="center" colspan="2" id="msg" class="error-msg"> &nbsp;
									<c:if test="${not empty errors}">
										<c:out value="${errors}" />
									</c:if>
								</td>
							</tr>
							<tr>
								<td colspan="2"><br/>
									<div align="center" class="hf">Till Balance Required</div><br/>
								</td>
							</tr>
							<tr>
								<td align="right" width="50%"><span class="label">Cash in Hand:</span></td>
								<td><input type="text" maxlength="7" size="7" id="TBAM" name="TBAM" autocomplete="off" class="text"/></td>
							</tr>
							<tr>
								<td></td>
								<td><security:token/>&nbsp;&nbsp;<input class="mainbutton" size="7" type="submit" id="confirm" name="confirm" value="Confirm" ></td>
							</tr>
						</table>
						<input type="hidden" name="DTID" value="${requestScope.DTID}"/>
						<input type="hidden" name="APID" value="${requestScope.APID}"/>
						<input type="hidden" name="BAID" value="${requestScope.BAID}"/>
						<input type="hidden" name="ACID" value="${requestScope.ACID}"/>
						<input type="hidden" name="UID" value="${requestScope.UID}"/>
						<input type="hidden" name="productName" value="${requestScope.FetchTransactionListViewModel.productName}"/>
						<input type="hidden" name="tranAmount" value="${requestScope.FetchTransactionListViewModel.tranAmount}"/>
						<input type="hidden" name="transactionDateTime" value="${requestScope.FetchTransactionListViewModel.transactionDateTime}"/>
						
					</form>
				</td>
			</tr>
			<tr valign="bottom">
				<td>
					<table height="78px" width="100%" cellpadding="0" cellspacing="0">
						<tr><td>&nbsp;</td></tr>
					</table>
				</td>
			</tr>
		</table>
	</body>
</html>