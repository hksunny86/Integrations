<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="com.inov8.microbank.common.util.CommandFieldConstants" %>
<%@include file="/common/taglibs.jsp" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Account Opening</title>
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

        function validate() {

            var CNIC = document.getElementById('CNIC').value;
            var CMOB = document.getElementById('CMOB').value;

            if (CNIC.length == 0) {
                return writeError('Customer CNIC is required.');
            }
            if (CNIC.length != 13 || !isNumber(CNIC)) {
                return writeError('Customer CNIC must be 13 digits.');
            }

            if (CMOB.length == 0) {
                return writeError('Customer Mobile Number is required.');
            }
            if (CMOB.length != 11 || !isNumber(CMOB)) {
                return writeError('Customer Mobile Number must be 11 digits.');
            }

            if (!isValidFormat(CMOB)) {
                return writeError("Customer Mobile Number must start with 03");
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
    <form method="post" action="<c:url value="/accountOpeningWithBVS.aw"/>" onsubmit="return validate()">

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
                    <div align="center" class="hf">Branchless Banking Account Opening Form</div>
                </td>
            </tr>
            <tr>
                <td align="center" colspan="2">&nbsp;</td>
            </tr>
            <tr>
                <td align="right" width="50%">
                    <span style="color: #FF0000">*</span>
                    <span class="label">Customer CNIC :</span>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${not empty IS_LINKED_REQ}">
                            <input type="text" id="CNIC" name="CNIC"
                                   value="${param.CNIC}" onkeypress="return maskNumber(this,event); return isNumberKey(event)"
                                   maxlength="13" class="text" disabled/>
                        </c:when>
                        <c:otherwise>
                            <input type="text" id="CNIC" name="CNIC"
                                   value="${param.CNIC}" onkeypress="return maskNumber(this,event); return isNumberKey(event)"
                                   maxlength="13" class="text"/>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
            <tr>
                <td align="right" width="50%">
                    <span style="color: #FF0000">*</span>
                    <span class="label">Customer Mobile Number :</span>

                </td>
                <td>
                    <c:choose>
                        <c:when test="${not empty IS_LINKED_REQ}">
                            <input type="text" id="CMOB"
                               name="CMOB" value="${param.CMOB}"
                               onkeypress="return maskNumber(this,event)" maxlength="11" class="text" disabled/>
                        </c:when>
                        <c:otherwise>
                            <input type="text" id="CMOB"
                                   name="CMOB" value="${param.CMOB}"
                                   onkeypress="return maskNumber(this,event)" maxlength="11" class="text"/>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
            <tr>
                <td align="right" width="50%">
                    <span class="label">Initial Deposit:</span>
                </td>
                <td>
                    <input type="text" id="initialAmount"
                           name="initialAmount" value="${param.iAmount}"
                           onkeypress="return maskNumber(this,event)" maxlength="10" class="text"/>
                </td>

            </tr>
            <tr>
                <td colspan="2">
                    &nbsp;<input type="hidden" name="<%= CommandFieldConstants.KEY_PROD_ID %>" value="50011"/>
                    <input type="hidden" name="<%= CommandFieldConstants.KEY_U_ID %>" value="${UID}"/>
                    <input type="hidden" name="<%= CommandFieldConstants.KEY_TPAM %>" value="0"/>
                    <input type="hidden" name="<%= CommandFieldConstants.KEY_COMM_AMOUNT %>" value="0"/>
                    <input type="hidden" name="<%= CommandFieldConstants.KEY_CUSTOMER_MOBILE %>" value="${param.CMOB}"/>
                    <input type="hidden" name="<%= CommandFieldConstants.KEY_CNIC %>" value="${param.CNIC}"/>
                    <input type="hidden" name="IS_LINKED_REQ" value="${IS_LINKED_REQ}"/>
                    <input type="hidden" name="CW_AMOUNT" value="${param.TXAM}"/>
                    <input type="hidden" name="IS_UPGRADE" value="${IS_UPGRADE}"/>
                </td>
            </tr>
            <tr>
                <td align="right"></td>
                <td><security:token/>
                    <input class="mainbutton"
                           type="submit" id="confirm"
                           name="confirm"
                           style="width: 180px;"
                           value="Next"/>
                </td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>
