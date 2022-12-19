<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="com.inov8.microbank.webapp.action.allpayweb.Encryption"%>
<%@include file="/common/taglibs.jsp"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Cash Out</title>
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
        function goHome( )
        {
            var homeUrl = "agentweblogin.aw?UID=" + ${sessionScope.UID};
            window.location.replace( homeUrl );
        }

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
            var OTP = document.getElementById("OTPIN");
            var PIN = document.getElementById("PIN");

            if(!validateNumber(OTP)) {
                return writeError("Kindly Enter Valid OTP.");
            }

            if(OTP.value.length != 4) {
                return writeError("Kindly Enter Valid OTP.");
            }

            if(!validateNumber(PIN) || PIN.value.length != 4) {
                return writeError("Kindly Enter valid MPIN.");
            }

            if(navigator.appName == 'Netscape') {
                document.getElementById("confirm").disabled = true;
            } else {
                document.getElementById("confirm").disabled = 'disabled';
            }

            return true;
        }

        function openPrintWindow() {

            var url = '${contextPath}'+'/recepit.aw?TRXID=${TRXID}&PID=${PID}';

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
    <form method="post" action="<c:url value="/hraCashWithdrawalConfirmation.aw" />" onsubmit="return validate();">
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
                    <div align="center" class="hf">HRA Cash WithDrawal (Customer)</div>
                </td>
            </tr>
            <tr>
                <td align="center" colspan="2">&nbsp;</td>
            </tr>
            <tr>
                <td align="right" width="50%"><span class="label">Customer Mobile #:</span></td>
                <td width="50%"><span class="value-text" id="CMOB">&nbsp;&nbsp;<c:out value="${CMOB}"></c:out></span></td>
            </tr>
            <tr>
                <td align="right"><span class="label">Customer NIC:</span></td>
                <td><span class="value-text" id='CNIC'>&nbsp;&nbsp;<c:out value="${CNIC}"></c:out></span></td>
            </tr>
            <tr>
                <td align="right"><span class="label">Customer Title:</span></td>
                <td><span class="value-text" id='CNAME'>&nbsp;&nbsp;<c:out value="${CNAME}"></c:out></span></td>
            </tr>
            <tr>
                <td align="right"><span class="label">Amount:</span></td>
                <td><span class="value-text">&nbsp;&nbsp;<fmt:formatNumber maxFractionDigits="2" type="currency" currencySymbol="PKR " value="${TXAM}">
                </fmt:formatNumber></span></td>
            </tr>
            <tr>
                <td align="right"><span class="label">Charges:</span></td>
                <td><span class="value-text" id='CAMT'>&nbsp;&nbsp;<c:out value="${TPAM}"></c:out></span></td>
            </tr>
            <tr>
                <td align="right"><span class="label">Total Amount:</span></td>
                <td><span class="value-text" id='TAMT'>&nbsp;&nbsp;<c:out value="${TAMT}"></c:out></span></td>
            </tr>
            <c:if test="${not empty isComplete and isComplete eq 1}">
                <tr>
                    <td align="right"><span class="label">Transaction ID:</span></td>
                    <td><span class="value-text" id='TRXID'>&nbsp;&nbsp;<c:out value="${TRXID}"></c:out></span></td>
                </tr>
                <tr>
                    <td align="right"><span class="label">Date & Time:</span></td>
                    <td><span class="value-text" id='TIMEF'>&nbsp;&nbsp;<c:out value="${TIMEF}"></c:out></span></td>
                </tr>

                <tr>
                    <td>&nbsp;</td>
                    <td align="center" colspan="2">
                        <a href="#" onclick="javascript:openPrintWindow()">Receipt</a>
                    </td>
                </tr>
            </c:if>
            <c:if test="${not empty isValidOTP and isValidOTP eq 0}">
                <tr>
                    <td align="right">
                        <span style="color: #FF0000">*</span>
                        <span class="label">One Time Pin:</span>
                    </td>
                    <td>
                        <input type="password" id="OTPIN" name="OTPIN" maxlength="4" value="${param.OTP}" size="4" style="" class="text"/>
                    </td>
                </tr>
            </c:if>
            <tr>
                <c:if test="${not empty isValid and isValid eq 0}">
                    <td align="right">
                        <span class="label">MPIN:*</span>
                    </td>
                    <td><input type="password" id="PIN" name="PIN" maxlength="4" size="5" autocomplete="off" value="${param.PIN}" class="text"/></td>
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

        <input type="hidden" name="TRXID" value="${param.TRXID}"/>
        <input type="hidden" name="TXAM" value="${TXAM}"/>
        <input type="hidden" name="TXAMF" value="${TXAMF}"/>
        <input type="hidden" name="CMOB" value="${CMOB}"/>
        <input type="hidden" name="CNIC" value="${CNIC}"/>
        <input type="hidden" name="PID" value="${PID}"/>
        <input type="hidden" name="reqTime" value="<%=System.currentTimeMillis()%>"/>

        <input type="hidden" name="CAMT" value="${CAMT}"/>
        <input type="hidden" name="CAMTF" value="${CAMTF}"/>
        <input type="hidden" name="TPAM" value="${TPAM}"/>
        <input type="hidden" name="TPAMF" value="${TPAMF}"/>
        <input type="hidden" name="CMOB" value="${CMOB}"/>
        <input type="hidden" name="CNAME" value="${CNAME}"/>
        <input type="hidden" name="TAMT" value="${TAMT}"/>
        <input type="hidden" name="TAMTF" value="${TAMTF}"/>
        <input type="hidden" name="BAMT" value="${TXAM}"/>
        <input type="hidden" name="PIN_RETRY_COUNT" value="${PIN_RETRY_COUNT}"/>
    </form>
</div>
</body>
</html>
