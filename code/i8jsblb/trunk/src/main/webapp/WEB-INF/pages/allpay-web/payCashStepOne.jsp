<!DOCTYPE html>
<%@ page language="java" import="com.inov8.microbank.common.util.CommandFieldConstants" %>
<%@include file="/common/taglibs.jsp" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>${param.PNAME}</title>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,IE=11,IE=10"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/awstyle_new.css" type="text/css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.7.2.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/scripts/aw/aw-contents-height-setter.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/scripts/aw/aw-contents-height-setter.js"></script>

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

        var commissionOption = '';

        function enableSelect(radio) {

            var jq = jQuery.noConflict();

            if (radio.value == '0') {
                jq("#RWCNIC").attr("disabled", "disabled");
                jq("#TRXID").removeAttr("disabled");
                jq("#cnic").val("");
            }

            if (radio.value == '1') {

                jq("#TRXID").attr("disabled", "disabled");
                jq("#RWCNIC").removeAttr("disabled");
                jq("#TRXID").val("");
                jq("#walkInCnic").val("");

            }

            commissionOption = radio.value;
            console.log(jq("#walkInCnic").value)

        }

        function writeError(message) {
            var msg = document.getElementById('msg');
            msg.innerHTML = message;
            msg.className = 'error-msg';
            return false;
        }

        function isValidFormat(mobileNumber) {

            return (mobileNumber.substring(0, 2) == '03');
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

        function enableRadio(radioId, textField) {

            var _radio = document.getElementById(radioId);
            var _textField = document.getElementById(textField);

            _textField.value = '';
            _radio.checked = true;

            commissionOption = _radio.value;
        }


        function validate() {

            console.log(jq(isPayCashCnicAllowed));
            if (jq(isPayCashCnicAllowed)) {


                var optionselected = false;

                if (document.getElementById('transactionId')!= null && document.getElementById('transactionId').checked) {
                    var txtField = document.getElementById('TRXID');
                    var CNIC = document.getElementById('walkInCnic');

                    if (txtField.value.length < 12) {
                        return writeError('Transaction Id length must be 12 Digits');
                    }

                    if (CNIC.value.length != 13) {
                        return writeError('Walkin CNIC length must be 13 Digits');
                    }

                    if (!validateNumber(CNIC)) {
                        return writeError('Walkin CNIC Must be Numeric');
                    }
                    optionselected = true;
                }

                else if (document.getElementById('receiverCNIC').checked) {
                    var CNIC = document.getElementById('RWCNIC');

                    if (CNIC.value.length != 13) {
                        return writeError('CNIC length must be 13 Digits');
                    }

                    if (!validateNumber(CNIC)) {
                        return writeError('CNIC Must be Numeric');
                    }

                    optionselected = true;
                }

                if (!optionselected) {
                    return writeError('Kindly select any option to process.');
                }
            }

            else {
                var tId = document.getElementById('TRXID');
                var CNIC = document.getElementById('walkInCnic');
                if (tId != null) {
                    if (tId.value.length == 0)
                        return writeError('Transaction Id is Required');

                    if (tId.value.length < 12)
                        return writeError('Transaction Id length must be 12 Digits');
                }

                if (CNIC.value.length != 13) {
                    return writeError('Walkin  CNIC length must be 13 Digits');
                }

                if (!validateNumber(CNIC)) {
                    return writeError('Walkin CNIC Must be Numeric');
                }


            }


            if (navigator.appName == 'Netscape') {
                document.getElementById("confirm").disabled = true;
            } else {
                document.getElementById("confirm").disabled = 'disabled';
            }

            return true;
        }


        function isNumberKey(evt) {
            var charCode = (evt.which) ? evt.which : event.keyCode
            if (charCode > 31 && (charCode < 48 || charCode > 57))
                return false;
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
    <form method="post" action="<c:url value="/payCashStepOne.aw"/>" onsubmit="return validate()">
        <input type="hidden" name="PID" value="${param.PID}"/>
        <input type="hidden" name="dfid" value="${param.dfid}"/>
        <input type="hidden" name="TXAM" value="${param.TXAM}"/>
        <input type="hidden" name="TXAMF" value="${TXAMF}"/>
        <input type="hidden" name="MOBN" value="${MOBN}"/>
        <input type="hidden" name="PNAME" value="${param.PNAME}"/>
        <input type="hidden" name="reqTime" value="<%=System.currentTimeMillis()%>"/>
        <input type="hidden" name="CAMT" value="${requestScope.CAMT}"/>
        <input type="hidden" name="TPAM" value="${requestScope.TPAM}"/>
        <input type="hidden" name="MOBN" value="${requestScope.MOBN}"/>
        <input type="hidden" name="RPNAME" value="${requestScope.RPNAME}"/>
        <input type="hidden" name="TAMT" value="${requestScope.TAMT}"/>
        <input type="hidden" name="CAMTF" value="${requestScope.CAMTF}"/>
        <input type="hidden" name="TPAMF" value="${requestScope.TPAMF}"/>
        <input type="hidden" name="TAMTF" value="${requestScope.TAMTF}"/>
        <input type="hidden" name="BAMT" value="${TXAM}"/>
        <table align="center" width="30%" border="0" cellpadding="0" cellspacing="0">
            <tr>
                <td colspan="2">&nbsp;</td>
            </tr>
            <tr>
                <td align="center" colspan="3" id="msg">&nbsp;

                    <c:if test="${not empty errors}">
                        <font color="#FF0000"> <c:out value="${errors}"/></font>
                    </c:if>

                </td>
            </tr>
            <tr>
                <td colspan="3">
                    <div align="center" valign="top" class="hf">Receive Money</div>
                </td>
            </tr>
            <tr>
                <td colspan="2">&nbsp;</td>
            </tr>

            <input type="hidden" name="isPayCashCnicAllowed" id="isPayCashCnicAllowed" value=${ isPayCashCnicAllowed}/>

            <c:if test="${ isPayCashCnicAllowed}">
                <tr>


                    <td align="right">
                        <input type="radio" name="BYCNIC" id="transactionId" value="0"
                               onclick="enableSelect(this)"/>
                    </td>
                    <td align="left" style="width: 150px;">
                        By Transaction ID
                    </td>
                    <td align="left">
                        <input type="text" name="<%= CommandFieldConstants.KEY_TX_ID %>"
                               id="<%= CommandFieldConstants.KEY_TX_ID %>" maxlength="12" size="13" class="text"
                               onkeypress="return maskAlphaNumeric(this,event); return isAlphanumeric(event)"
                               type="text"
                               style="width: 160px;"
                               onclick="enableRadio('transactionId', 'RWCNIC')"/>

                    </td>
                <tr>
                    <td colspan="2" align="left" style="width: 120px;">
                        <span style="color: #FF0000">*</span>
                        <span class="label">Walkin CNIC:</span>

                    </td>
                    <td><input type="text" id="walkInCnic" name="<%= CommandFieldConstants.KEY_R_W_CNIC %>"
                               onkeypress="return isNumberKey(event)"
                               maxlength="13" class="text" style="width: 160px;"
                    /></td>
                </tr>
                </tr>

                <tr>
                    <td align="right">
                        <input type="radio" name="BYCNIC" id="receiverCNIC" value="1"
                               onclick="enableSelect(this)"/>
                    </td>
                    <td align="left" style="width: 80px;padding-left: 10px;">
                        By CNIC
                    </td>
                    <td align="left">
                        <input type="text" name="<%= CommandFieldConstants.KEY_R_W_CNIC %>"
                               id="<%= CommandFieldConstants.KEY_R_W_CNIC %>" maxlength="13" size="13"
                               class="text" type="text"
                               style="width: 160px;"
                               onclick="enableRadio('receiverCNIC', 'TRXID')"/>

                    </td>
                </tr>

            </c:if>

            <c:if test="${not isPayCashCnicAllowed}">


                <tr>

                    <td colspan="2" align="left" style="width: 115px;">
                        <span style="color: #FF0000">*</span>
                        Transaction ID
                    </td>
                    <td align="left">
                        <input type="text" name="<%= CommandFieldConstants.KEY_TX_ID %>"
                               id="<%= CommandFieldConstants.KEY_TX_ID %>" maxlength="12" size="13" class="text"
                               onkeypress="return maskAlphaNumeric(this,event); return isAlphanumeric(event)"
                               type="text"
                               style="width: 182px;"
                               required="true";
                        />

                    </td>
                </tr>

              <%--   <tr>
                    <td colspan="2" align="left" style="width: 120px;">
                        <span style="color: #FF0000">*</span>
                        <span class="label">Walkin CNIC:</span>

                    </td>
                    <td><input type="text" id="walkInCnic" name="<%= CommandFieldConstants.KEY_R_W_CNIC %>"
                               onkeypress="return isNumberKey(event)"
                               maxlength="13" class="text"/></td>
                </tr> --%>
            </c:if>

            <tr>
                <td align="right" colspan="2"></td>
                <td><security:token/>
                    <input class="mainbutton"
                           type="submit" id="confirm"
                           name="confirm"
                           style="width: 160px;"
                           value="Next"/>
                </td>
            </tr>
        </table>

        <input type="hidden" name="PID" id="PID" value="${param.PID}"/>
        <!-- ${param.PID } ${param.PNAME} ${param.dfid }-->
        <input type="hidden" name="dfid" id="dfid" value="${param.dfid}"/>
        <input type="hidden" name="PNAME" id="PNAME" value="${param.PNAME}"/>
    </form>
</div>
</body>
</html>