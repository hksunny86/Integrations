<%--
  Created by IntelliJ IDEA.
  User: Abubakar Farooque
  Date: 10/8/2019
  Time: 4:13 PM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="com.inov8.microbank.webapp.action.allpayweb.Encryption" %>
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

            var CMOB = document.getElementById("CMOB");
            var TXAM = document.getElementById("TXAM");

            if(!validateNumber(TXAM)) {
                return writeError("Kindly Enter Valid Amount.");
            }

            if(TXAM.value.length > 11) {
                return writeError("Kindly Enter Valid Amount range, 11 Digits Maximum.");
            }

            if(!validateNumber(CMOB) || CMOB.value.length != 11) {
                return writeError("Kindly Enter 11 Digit Mobile #.");
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
<div style="height:95px%">
    <jsp:include page="header.jsp"></jsp:include>
</div>
<div class="vertical-left-menu ">
    <jsp:include page="agentWebMenu.jsp"></jsp:include>
</div>
<div class="main-body-area">
    <form action="<c:url value="/customer2CashOutInfo.aw"/>" method="post" onsubmit="return validate();">
        <table align="center" width="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
                <td colspan="2">&nbsp;</td>
            </tr>
            <tr>
                <td align="center" colspan="2" id="msg">&nbsp;

                    <c:if test="${not empty errors}">
                        <font color="#FF0000"> <c:out value="${errors}" /></font>
                    </c:if>

                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <div align="center" class="hf">Cash Out (By IVR)</div>
                </td>
            </tr>
            <tr>
                <td colspan="2">&nbsp;</td>
            </tr>
            <tr>
                <td align="right">
                    <span style="color: #FF0000">*</span>
                    <span class="label">Enter Mobile #:</span>
                </td>
                <td>
                    <input type="text" id="CMOB" name="CMOB" maxlength="11" size="11" style="" value="${param.CMOB }" class="text"/>
                </td>
            </tr>
            <tr>
                <td align="right" width="50%">
                    <span style="color: #FF0000">*</span>
                    <span class="label">Enter Amount:</span>
                </td>
                <td>
                    <input type="text" id="TXAM" name="TXAM" maxlength="5" size="11" value="${param.TXAM}" class="text"/>
                </td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td align="right"></td>
                <td><security:token/>
                    <input class="mainbutton"
                           type="submit" id="confirm"
                           name="confirm"
                           style="width: 100px;"
                           value="Next"/>
                </td>
            </tr>
        </table>

        <%--<input type="hidden" name="PID" id="PID" value="${param.PID}" /><!-- ${param.PID } ${param.PNAME} ${param.dfid }-->--%>
        <input type="hidden" name="dfid" id="dfid" value="${param.dfid}" />
        <input type="hidden" name="PNAME" id="PNAME" value="${param.PNAME}" />
    </form>
</div>
</body>
</html>

