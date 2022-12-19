<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
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
		<title>Transaction Summary</title>
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

			function openPrintWindow() {
	
				var url = '${contextPath}'+'/recepit.aw?TRXID=${TRXID}&PID=${PID}';
				printWindow = window.open(url,"receipt_print", "location=1, left=0, top=0, width=625, height=425, status=no, scrollbars=no, titlebar=no, toolbar=no, menubar=no, resizable=no");
				printWindow.focus();
			}

            function printDiv(divName) {
			    document.getElementById("printBtn").style.display="none";
                document.getElementById("balancefield1").style.display="none";
                document.getElementById("balancefield2").style.display="none";
                var printContents = document.getElementById(divName).innerHTML;
                var originalContents = document.body.innerHTML;

                document.body.innerHTML = printContents;

                window.print();

                document.body.innerHTML = originalContents;

                document.getElementById("balancefield1").style.display="";
                document.getElementById("balancefield2").style.display="";

                window.location = "welcomescreen.aw";
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
	<div class="main-body-area" id="printDiv">
		<table border="0" height="100%" width="85%" align="center" bgcolor="white" cellpadding="0" cellspacing="0" class="container">

			<tr id="contents" valign="top">
				<td>
					<table align="center" width="100%" border="0">

						<c:if test="${PID == 50056 or PID == 10245132 or PID == 10245131 or PID == 10245133}">
						<tr>
							<td colspan="2" align="center"><br><br>
								<div><center><img style="width: 10%" src="images/aw/itc_logo.jpg"/></center></div>
							    <font color="#0066b3" size="5px">Transaction Summary</font><br><br>
							</td>
						</tr>
						</c:if>
						<c:if test="${PID != 50056 and PID != 10245132 and PID != 10245131 and PID != 10245133}">
							<tr>
								<td colspan="2" align="center"><br><br>
									<font color="#0066b3" size="5px">Bill Payment Transaction Summary</font><br><br>
								</td>
							</tr>
						</c:if>
						<tr>
							<td colspan="2" align="center">
								<span class="success-msg">
									${requestScope.Heading} ${requestScope.Name}
								</span><br><br>
							</td>
						</tr>
						<c:forEach items="${responseData}" var="responseData">
							<tr>
								<td align="right" width="50%" <c:if test="${responseData.key == 'Balance'}"><c:out value="id=balancefield1"/></c:if>
								><span class="label">${responseData.key} :</span></td>
								<td <c:if test="${responseData.key == 'Balance'}"><c:out value="id=balancefield2"/></c:if>
								><span class="value-text">${responseData.value}</span></td>
							</tr>
			 			</c:forEach>
						<c:if test="${PID != 50056 and PID != 10245132 and PID != 10245131 and PID != 10245133}">
						<tr>
							<td colspan="2" align="center">
								<a href="#" onclick="javascript:openPrintWindow()">Receipt</a>
							</td>
						</tr>			 
						<tr>
							<td colspan="2" align="center"><br/>Back To : 
								<a href="billPaymentDispatcherController.aw?PID=${PRODUCT_ID}&SID=${SID}&PNAME=${PNAME}">${PNAME}</a>
							</td>
						</tr>
						</c:if>
						<c:if test="${PID == 50056 or PID == 10245132 or PID == 10245131 or PID == 10245133}">
							<tr>
							<td colspan="2" align="center">
								<input type="button" onclick="printDiv('printDiv')" value="Print" id="printBtn" />
							</td>
							</tr>
						</c:if>

					</table>
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
