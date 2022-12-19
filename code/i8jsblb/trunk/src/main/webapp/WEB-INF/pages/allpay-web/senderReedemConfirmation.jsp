<%@ page import="com.inov8.microbank.common.util.CommandFieldConstants" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Sender Reedem Confirmation</title>
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
            height:500px;

        }
        .main-body-area {
            width: 80%;
            float: left;
            padding: 15px;
            height: 430px;
        }
    </style>

    <script language="javascript">

        function goHome() {
            var homeUrl = "welcomeScreen.aw";
            window.location.replace(homeUrl);
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



            var pin = document.getElementById('PIN');
            var otp = document.getElementById('OTPIN');

            if(otp.value.length < 4) {
                return writeError('OTP length must be 4 digits');
            }
            if(!validateNumber(otp)) {
                return writeError('OTP must be numeric');
            }

            if(pin.value.length < 4) {
                return writeError('MPIN length must be 4 digits');
            }
            if(!validateNumber(pin)) {
                return writeError('MPIN must be numeric');
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
    <form name="billpaymentform" method="post" action='<c:url value="/senderReedemConfirmation.aw"/>' onsubmit="return validate()">
        <table align="center" width="100%" border="0">
            <tr>
                <td colspan="2" align="center">&nbsp;<br></td>
            </tr>
            <tr>
                <td align="center" colspan="2" id='msg'>
                    &nbsp;
                    <c:if test="${not empty errors}">
                        <font color="#FF0000"> <c:out value="${errors}" />
                        </font>
                    </c:if>
                </td>
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <div align="center" class="hf">Sender Reedem Confirmation<input type='hidden' name='PID' value='${PID}'/></div><br>
                </td>
            </tr>

            <tr>
                <td align="right" width="50%">
                    <span class="label">Transaction ID :</span>
                </td>
                <td><span class="value-text">${TRXID}</span><input type='hidden' name='TRXID' value='${TRXID}'/></td>
            </tr>

            <tr>
                <td align="right" width="50%">
                    <span class="label">Sender CNIC :</span>
                </td>
                <td><span class="value-text">${SWCNIC}</span><input type='hidden' name='SWCNIC' value='${SWCNIC}'/></td>
            </tr>
            <tr>
                <td align="right" width="50%">
                    <span class="label">Sender Mobile Number :</span>
                </td>
                <td><span class="value-text">${SWMOB}</span><input type='hidden' name='SWMOB' value='${SWMOB}'/></td>
            </tr>
            <tr>
                <td align="right" width="50%">
                    <span class="label">Receiver CNIC :</span>
                </td>
                <td><span class="value-text">${RWCNIC}</span><input type='hidden' name='RWCNIC' value='${RWCNIC}'/></td>
            </tr>
            <tr>
                <td align="right" width="50%">
                    <span class="label">Receiver Mobile Number :</span>
                </td>
                <td><span class="value-text">${RWMOB}</span><input type='hidden' name='RWMOB' value='${RWMOB}'/><input type='hidden' name='RCMOB' value='${CMOB}'/></td>
            </tr>




            <tr>
                <td align="right" width="50%">
                    <span class="label">Total Amount :</span>
                </td>
                <td><span class="value-text">${TAMT}</span><input type='hidden' name='TAMT' value='${TAMT}'/></td>
            </tr>

            <tr>
                <td align="right" width="50%">
                    <span class="label">DATE & Time :</span>
                </td>
                <td><span class="value-text">${DATEF} ${TIMEF}</span><input type='hidden' name='DATEF' value='${DATEF}'/>
                    <input type='hidden' name='TIMEF' value='${TIMEF}'/>
                </td>
            </tr>


            <tr>
                <td align="right" width="50%"><span style="color: #FF0000">*</span><span class="label">MPIN :</span></td>
                <td><input type="password" maxlength="4" size="6" id="PIN" name="PIN" value="" autocomplete="off"/></td>
            </tr>
            <tr>
                <td align="right">
                    <input class="mainbutton" type="submit" id="confirm" name="confirm"
                           value="Confirm"/>
                </td>
                <td><security:token/>

                    <input class="mainbutton" type="button" name="cancel" value="Cancel"
                           onclick="goHome( );"/>
                </td>
            </tr>
        </table>
    </form>

</div>

</body>
</html>