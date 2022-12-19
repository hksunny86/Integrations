<%@page	import="com.inov8.microbank.common.util.CommandFieldConstants" %>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>My Commission</title>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
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

			var commissionOption = '';
		
			function enableSelect(radio) {
				
				var dailyList = document.getElementById('dailyList');
				var weeklyList = document.getElementById('weeklyList');
				var monthlyList = document.getElementById('monthlyList');
				
				if(radio.value == 'Daily') {
					dailyList.disabled = false;
					weeklyList.selectedIndex = 0;
					weeklyList.disabled = true;
					monthlyList.selectedIndex = 0;
					monthlyList.disabled = true;
				}
				
				if(radio.value == 'Weekly') {
					dailyList.selectedIndex = 0;
					dailyList.disabled = true;
					weeklyList.disabled = false;
					monthlyList.selectedIndex = 0;
					monthlyList.disabled = true;
				}
				
				if(radio.value == 'Monthly') {
					dailyList.selectedIndex = 0;
					dailyList.disabled = true;
					weeklyList.selectedIndex = 0;
					weeklyList.disabled = true;
					monthlyList.disabled = false;
				}
				
				commissionOption = radio.value;
			}
			
			function validate() {
		
				var retValue = true;
				var msg = '';
				
				var dailyList = document.getElementById('dailyList');
				var weeklyList = document.getElementById('weeklyList');
				var monthlyList = document.getElementById('monthlyList');
				var pin = document.getElementById('pin');
		
				if(commissionOption == '') {
					msg = ('Kindly Select Commission Option (Daily/Weekly/Monthly).');	
					retValue = false;
				} 
				if(retValue && commissionOption == 'Daily' && dailyList.selectedIndex == 0) {
					msg = 'Kindly Select '+commissionOption+' Criteria';
					retValue = false;
				} 
				if(retValue && commissionOption == 'Weekly' && weeklyList.selectedIndex == 0) {
					msg = 'Kindly Select '+commissionOption+' Criteria';
					retValue = false;
				} 
				if(retValue && commissionOption == 'Monthly' && monthlyList.selectedIndex == 0) {
					msg = 'Kindly Select '+commissionOption+' Criteria';
					retValue = false;
				}
				
				if(retValue && (!validateNumber(pin) || pin.value.length < 4)) {
					msg = 'Kindly Enter 4 Digit PIN Code.';		
					retValue = false;
				}
				
				if(!retValue) {
					writeError(msg);
					//alert(msg);
				}
						
				return retValue;
			}
			
			function validateNumber(field) {
			
				var charpos = field.value.search("[^0-9]"); 
				
				if(field.value=="") {
					return false; 
				}
				
				if(charpos >= 0) { 
					return false; 
				} 
				
				return true;
			}
			
			function writeError(message) {
				var msg = document.getElementById('msg');
				msg.innerHTML = message;
				msg.className = 'error-msg';
				return false;
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
		<table align="center" bgcolor="white" border="0" cellpadding="0" cellspacing="0" height="100%" width="85%" class="container">
			<tr height="95px">

			</tr>
			<tr id="contents" valign="top">
				<td>
					<form method='post' action='<c:url value="/myCommission.aw"/>' onsubmit='return validate()'>
						<table width="80%" border="0" align="center">
							<tr>
								<td align="center" colspan="3">&nbsp;</td>
							</tr>
							<tr>
								<td align="center" colspan="3" id="msg">&nbsp;									
									<c:if test="${not empty errors}">
										<font color="#FF0000"><c:out value="${errors}" /> <br /> </font>
									</c:if>
								</td>
							</tr>
							<tr>
								<td colspan="3">
									<div align="center" valign="top" class="hf">My Commission</div>
								</td>
							</tr>
							<tr>
								<td align="center" colspan="3">&nbsp;</td>
							</tr>
						</table>
						<table width="20%" border="0" align="center">
							<tr>
								<td width="2%" align="right"><input type="radio" name="order" value="Daily" onClick="enableSelect(this)" /></td>
								<td width="10%" align="right">Daily</td>
								<td width="10%"><select id="dailyList" name="commissionOption" style="width:125px" disabled="disabled">
										<option value="0" selected>- SELECT -</option>
										<option value="1">Today</option>
										<option value="2">Yesterday</option>
								</select></td>
							</tr>
							<tr>
								<td align="right"><input type="radio" name="order" value="Weekly" onClick="enableSelect(this)">
								</td>
								<td align="right">Weekly</td>
								<td>
									<select id="weeklyList" name="commissionOption" style="width:125px" disabled="disabled">
										<option value="0" selected>- SELECT -</option>
										<option value="3">Current Week</option>
										<option value="4">Previous Week</option>
									</select>
								</td>
							</tr>
							<tr>
								<td align="right"><input type="radio" name="order" value="Monthly" onClick="enableSelect(this)">
								</td>
								<td align="right">Monthly</td>
								<td>
									<select id="monthlyList" name="commissionOption"
										style="width:125px" disabled="disabled">
											<option value="0" selected>- SELECT -</option>
											<option value="5">Current Month</option>
											<option value="6">Previous Month</option>
									</select>
								</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td align="right">PIN <span style="color: #FF0000">*</span></td>
								<td>
									<input type="password" id="pin" name="PIN" style="width:125px" maxlength="4" />
								</td>
							</tr>
							<tr style="height:30px">
								<td>&nbsp;</td>
								<td>&nbsp;</td>
								<td>
									<input type="submit" value="Submit">
								</td>
							</tr>
						</table>
					</form>
				</td>
			</tr>
			<%-- <tr valign="bottom">
				<td>
					<jsp:include page="footer.jsp"></jsp:include>
				</td>
			</tr> --%>
		</table>
	</div>
	</body>
</html>