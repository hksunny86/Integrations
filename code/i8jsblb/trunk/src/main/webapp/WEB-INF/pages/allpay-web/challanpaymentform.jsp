<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>Pay Challan</title>
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
	
	function validate() {
		
		var CSCD = document.getElementById('CONSUMER');
        var CMOB = document.getElementById('CMOB');

		if(CSCD.value == '') {
            CSCD.focus();
            return writeError('Challan Number is required.');
		}else if(CSCD.value.length < 5) {
            CSCD.focus();
            return writeError('Challan Number must be atleast Five Digit.');
		}

        if(CMOB.value == '') {
            CMOB.focus();
            return writeError('Mobile Number is required.');
        }else if(CMOB.value.length < 11) {
            CMOB.focus();
            return writeError('Mobile Number must be 11 Digit.');
        }else if (!isValidMobileNo(CMOB)){
		    return writeError("Mobile Number should start from 03XXXXXXXXX")
		}
		
		if(navigator.appName == 'Netscape') {		
			document.getElementById("confirm").disabled = true;
		} else {
			document.getElementById("confirm").disabled = 'disabled';
		}
		
		return true;
	}

    function isValidMobileNo(field){
        if(field.type == "hidden"){
            return true;
        }
        if( field.value != '' )
        {
            var mobileNumber = field.value;
            if(mobileNumber.charAt(0)!="0" || mobileNumber.charAt(1)!="3"){
                field.focus();
                return false;
            }
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
		<table cellpadding="0" cellspacing="0" height="100%" width="85%" align="center" class="container">
			<tr id="contents" valign="top">
				<td>
					<form name="billpaymentform" method="post"
						action="<c:url value="/challanpaymentform.aw"/>" onsubmit="return validate()">
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
									<c:if test="${not empty errors}">
										<span class="error-msg">
											<c:out value="${errors}"/>
											<br/>
										</span>
									</c:if>
								</td>
							</tr>
							<tr>
								<td colspan="2" align="center">
									<div><center><img style="width: 10%" src="images/aw/itc_logo.jpg"/></center></div>
									<div align="center" class="hf">${PNAME}</div><br>
								</td>
							</tr>
							<tr>
								<td align="right" width="50%">
									<span class="label">${LABEL}:</span>
								</td>
								<td>
									<input type="text" id="CONSUMER" name="CONSUMER" maxlength="24" size="11" style="-wap-input-format: '*N'; -wap-input-required: true; width: 170px" class="text" onkeypress="return maskAlphaNumeric(this, event)"/>
								</td>
							</tr>
							<tr>
								<td align="right" width="50%">
									<span class="label">Mobile Number:</span>
								</td>
								<td>
									<input type="text" id="CMOB" name="CMOB" maxlength="11" size="11" style="-wap-input-format: '*N'; -wap-input-required: true; width: 170px" class="text" onkeypress="return maskInteger(this, event)" />
								</td>
							</tr>
							<tr>
								<td align="right"></td>
								<td><security:token/>&nbsp;&nbsp;<input name="next" class="mainbutton" type="submit" id="confirm" value="Next" /></td>
							</tr>
						</table>

						<input type="hidden" name="PID" value=${PID} />
						<input type="hidden" name="PNAME" value=${PNAME} />
						<input type="hidden" name="LABEL" value=${LABEL} />
						<input type="hidden" name="DTID" value="8" />
						<input type="hidden" name="reqTime" value="<%=System.currentTimeMillis()%>"/>

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
