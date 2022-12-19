<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.inov8.microbank.common.model.transactionmodule.FetchTransactionListViewModel"%>
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
			
			function openPrintWindow() {

				var url = '${contextPath}'+'/recepit.aw?TRXID=${param.TRXID}&PID=${param.PID}';

				printWindow = window.open(url,"receipt_print", "location=1, left=0, top=0, width=625, height=400, status=no, scrollbars=no, titlebar=no, toolbar=no, menubar=no, resizable=no");

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
		<table border="0" height="100%" width="85%" align="center" class="container" cellpadding="0" cellspacing="0">

			<tr id="contents" valign="top">
				<td>
					<table align="center" width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td colspan="2">&nbsp;</td>
						</tr>						<tr>
							<td colspan="2" align="center"><br><br>
							    <font color="#0066b3" size="5px">${requestScope.Heading} ${requestScope.Name}</font><br><br>
							</td>
						</tr>
                        <tr>
                            <td colspan="2" align="center">
                                <div align="center" class="hf">Transaction Successful!!</div><br>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">&nbsp;</td>
                        </tr>
						<c:forEach items="${responseData}" var="responseData">
							<tr>
								<td align="right" width="50%"><span class="label">${responseData.key} :</span></td>
								<td><span class="value-text">${responseData.value}</span></td>
							</tr>
			 			</c:forEach>
						<tr>
							<td colspan="2" align="center">
								<a href="#" onclick="javascript:openPrintWindow()">Receipt</a>
							</td>
						</tr>
						
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
