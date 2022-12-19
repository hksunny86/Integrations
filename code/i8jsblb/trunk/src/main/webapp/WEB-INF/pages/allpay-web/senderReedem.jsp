<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.inov8.microbank.common.util.CommandFieldConstants" %>
<%@include file="/common/taglibs.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Sender Reedem </title>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/awstyle_new.css" type="text/css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.7.2.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/scripts/aw/aw-contents-height-setter.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js">
    </script>
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
        }
    </style>
    <script language="javascript">

        function writeError(message) {
            var msg = document.getElementById('msg');
            msg.innerHTML = message;
            msg.className = 'error-msg';
            return false;
        }

        function isValidFormat(mobileNumber) {

            return (mobileNumber.substring(0, 2) == '03');
        }

        function isValidateTrxCode(code) {
            if (code.value == "") {
                return false;
            }

            var charpos = code.search("[^0-9]");
            if (charpos >= 0) {
                return false;
            }
            return true;
        }

        function validateNumber(field) {

            if (field == "") {
                return false;
            }

            var charpos = field.search("[^0-9]");
            if (charpos >= 0) {
                return false;
            }
            return true;
        }

        function validate() {

            var senderCnic = document.getElementById('SWCNIC').value;
            var senderMobile = document.getElementById('SWMOB').value;
            var transacationId = document.getElementById('TRXID').value;




            if ( transacationId == "" ) {

                return writeError('Kindly Enter Transaction ID');
            }


            if (senderCnic.length == 0) {
                return writeError('Sender CNIC is required.');
            }
            if (senderCnic.length != 13 || !isNumber(senderCnic)) {
                return writeError('Sender CNIC must be 13 digits.');
            }

            if (senderMobile.length == 0) {
                return writeError('Sender Mobile Number is required.');
            }
            if (senderMobile.length != 11 || !isNumber(senderMobile)) {
                return writeError('Sender Mobile Number must be 11 digits.');
            }

            if (!isValidFormat(senderMobile)) {
                return writeError("Sender Mobile Number must start with 03");
            }

            if (navigator.appName == 'Netscape') {
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
    <form method="post" action="<c:url value="/senderReedem.aw"/>" onsubmit="return validate()">

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
                    <div align="center" class="hf">Sender Redeem</div>
                </td>
            </tr>
            <tr>
                <td align="center" colspan="2">&nbsp;</td>
            </tr>
            <tr>
                <td align="right">
                    <span style="color: #FF0000">*</span><span class="label">Transaction ID:</span>
                </td>
                <td>
                    <input type="text" id="<%= CommandFieldConstants.KEY_TX_ID %>"
                           name="<%=CommandFieldConstants.KEY_TX_ID %>" size="20"
                           onkeypress="return maskAlphaNumeric(this,event); return isAlphanumeric(event)"
                           style="" value="" class="text"/>
                </td>
            </tr>
            <tr>
                <td align="right" width="50%">
                    <span style="color: #FF0000">*</span>
                    <span class="label">Sender CNIC :</span>

                </td>
                <td><input type="text" id="<%=CommandFieldConstants.KEY_S_W_CNIC %>"
                           name="<%=CommandFieldConstants.KEY_S_W_CNIC %>"
                           value="${SWCNIC}" onkeypress="return maskNumber(this,event); return isNumberKey(event)"
                           size="20"
                           maxlength="13" class="text"/></td>
            </tr>
            <tr>
                <td align="right" width="50%">
                    <span style="color: #FF0000">*</span>
                    <span class="label">Sender Mobile Number :</span>

                </td>
                <td><input type="text" id="<%= CommandFieldConstants.KEY_WALKIN_SENDER_MOBILE %>"
                           name="<%= CommandFieldConstants.KEY_WALKIN_SENDER_MOBILE %>"
                           value="${SWMOB}"
                           size="20"
                           onkeypress="return maskNumber(this,event)" maxlength="11" class="text"/></td>
            </tr>

            <tr>
                <td align="right"></td>
                <td><security:token/>
                    <input class="mainbutton"
                           type="submit" id="confirm"
                           name="confirm"
                           style="width: 29%;"
                           value="Next"/>
                </td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>
