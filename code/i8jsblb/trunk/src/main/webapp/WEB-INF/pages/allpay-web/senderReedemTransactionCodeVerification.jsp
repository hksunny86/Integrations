<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.inov8.microbank.common.util.CommandFieldConstants" %>
<%@include file="/common/taglibs.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Pay Cash</title>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,IE=11,IE=10"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/awstyle_new.css" type="text/css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.7.2.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/scripts/aw/aw-contents-height-setter.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/jquery.navgoco.css" type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/demo.css" type="text/css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.cookie.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.cookie.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.navgoco.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.navgoco.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/agentWebMenu.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/highlight.pack.js"></script>
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
            background-color: #e0e2e5;
        }
    </style>

    <script language="javascript">

        function writeError(message) {
            var msg = document.getElementById('msg');
            msg.innerHTML = message;
            msg.className = 'error-msg';
            return false;
        }


        function validateNumber(field) {

            if (field.value == "") {
                return false;
            }

            var charpos = field.value.search("[^0-9]");
            if (charpos >= 0) {
                return false;
            }
            return true;
        }

        function validate() {

            var TXCD = document.getElementById('OTPIN');

            if (!validateNumber(TXCD) || TXCD < 5) {
                return writeError('Kindly Enter 5 Digit Transaction Code.');
            }

            return true;
        }
    </script>

</head>
<body>

<div style="height:95px%">
    <jsp:include page="header.jsp"></jsp:include>
</div>
<div class="vertical-left-menu ">
    <jsp:include page="agentWebMenu.jsp"></jsp:include>
</div>
<div class="main-body-area">
    <form method="post" action="<c:url value="/senderReedemTransactionCodeVerificationController.aw"/>" onsubmit="return validate()">

        <table align="center" width="80%" border="0">
            <tr>
                <td align="center" colspan="2" id="msg">
                    &nbsp;
                    <c:if test="${not empty errors}">
                        <font color="#FF0000">
                            <c:out value="${errors}"/>
                            <br/>
                        </font>
                    </c:if>
                </td>
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <div align="center" class="hf">Transaction Code Verification</div>
                    <br>
                </td>
            </tr>
            <tr>
                <td align="right" width="50%"><span style="color: #FF0000">*</span><span class="label">Transaction Code :</span></td>
                <td align="left" width="50%"><input type="text" id="<%= CommandFieldConstants.KEY_ONE_TIME_PIN %>"
                                                    name="<%= CommandFieldConstants.KEY_ONE_TIME_PIN %>" size="5"
                                                    maxlength="5"
                                                    class="text"/></td>
            </tr>
            <tr>
                <td align="right"></td>
                <td><security:token/><input class="mainbutton" type="submit" id="confirm" name="confirm" value="Submit">
                </td>
            </tr>
        </table>
        <input type="hidden" name="<%= CommandFieldConstants.KEY_PROD_ID %>" value="${PID}"/>
        <input type="hidden" name="<%= CommandFieldConstants.KEY_TX_ID %>" value="${TRXID}"/>
        <input type="hidden" name="<%= CommandFieldConstants.KEY_PIN %>" value="${PIN}"/>
        <input type="hidden" name="SWCNIC" value="${SWCNIC}"/>
        <input type="hidden" name="SWMOB" value="${SWMOB}"/>
        <input type="hidden" name="RWCNIC" value="${RWCNIC}"/>
        <input type="hidden" name="RWMOB" value="${RWMOB}"/>
        <input type="hidden" name="TAMT" value="${TAMT}"/>
        <input type="hidden" name="DATEF" value="${DATEF}"/>
        <input type="hidden" name="TIMEF" value="${TIMEF}"/>
        <input type="hidden" name="ENCT" value="1"/>


    </form>

</div>
</body>
</html>