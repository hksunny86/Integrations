<%--
  Created by IntelliJ IDEA.
  User: Yasir Shabbir
  Date: 5/9/2017
  Time: 6:08 PM
  To change this template use File | Settings | File Templates.
--%>
<!--Author: Yasir Shabbir-->


<!DOCTYPE html>
<%@include file="/common/taglibs.jsp" %>
<html>

<head>
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE11">
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

    <title>HRA A/C Opening</title>
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
            var PIN = document.getElementById('PIN').value;
            var sourceOfIncome = document.getElementById('SOI').value;
            var occupation = document.getElementById('OCCUPATION').value;
            var acPurpose = document.getElementById('TRX_PUR').value;
            var nokName = document.getElementById('KIN_NAME').value;
            var nokCNIC = document.getElementById('KIN_CNIC').value;
            var nokMOB = document.getElementById('Kin_MOB_NO').value;
            var nokRelation = document.getElementById('KIN_RELATIONSHIP').value;
            var originatorLocation_1 = document.getElementById('ORG_LOC1').value;
            var originatorRelationship = document.getElementById('ORG_REL1').value;
            if (sourceOfIncome == '') {
                return writeError('Kindly Select Source of Income.');
            }

            if (acPurpose == '') {
                return writeError('Kindly select Purpose of Account.');
            }
            if (occupation == '') {
                return writeError('Occupation is required.');
            }
            if (nokName == '') {
                return writeError('Next of Kin Name is required.');
            }
            if(PIN == '')
            {
                return writeError('Pin is Required.');
            }
            if(PIN != '')
            {
                if (PIN.length < 4) {
                    return writeError('Kindly Enter 4 Digit PIN Code.');
                }
            }
            if (nokCNIC == '') {
                return writeError("Next of Kin CNIC is required.");
            }
            if(nokCNIC != '')
            {
                if (nokCNIC.length < 13) {
                    return writeError('Kindly Enter 13 Digit Next of Kin CNIC Number.');
                }
            }

            if (nokMOB == '') {
                return writeError("Next of Kin Mobile # is required.");
            }

            if(nokMOB != '')
            {
                if (nokMOB.length < 11) {
                    return writeError('Kindly Enter 11 Digit Next of Kin Mobile Number.');
                }
            }

            if(nokRelation == '')
            {
                return writeError('Next of Kin Relationship is Required.');
            }

            if (originatorLocation_1 == '') {
                return writeError('Originator Location is Required.');
            }

            if (originatorRelationship == '') {
                return writeError('Originator Relationship is Required.');
            }

            if (navigator.appName == 'Netscape') {
                document.getElementById("confirm").disabled = true;
            } else {
                document.getElementById("confirm").disabled = 'disabled';
            }

            return true;
        }

        function goHome() {
            var homeUrl = "welcomescreen.aw?ACID=" + ${ACID} +"&BAID=" + ${BAID} +"&UID=" + ${sessionScope.UID};
            window.location.replace(homeUrl);
        }
    </script>

</head>
<body bgcolor="#ffffff">
<title>
    HRA Account Opening Form
</title>

<div style="height:95%">
    <jsp:include page="header.jsp"></jsp:include>
</div>
<div class="vertical-left-menu ">
    <jsp:include page="agentWebMenu.jsp"></jsp:include>
</div>
<div class="main-body-area">
    <%--<html:form  name = "accountOPening" id = "accountOPening" commandName="mfsAccountModel" action="accountOpeningWithBVSConfirmation.aw" method="post" onsubmit="return validate()">--%>
    <form method="post" action="<c:url value="/hraaccountopeningconfirmation.aw"/>" onsubmit="return validate()">
        <table align="center" width="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
                <td width="25%" align="center" colspan="2" id="msg">
                    <c:if test="${not empty errors}">
										<span class="error-msg">
											<c:out value="${errors}"/>
											<br/>
										</span>
                    </c:if>
                </td>
                <td width="25%">&nbsp;</td>
                <td width="25%">&nbsp;</td>
            </tr>
            <tr>
                <td width="25%" colspan="4">
                    <div align="center" class="hf">HRA Account Opening Form</div>
                </td>
            </tr>
            <tr>
                <td width="25%">&nbsp;</td>
                <td width="25%">&nbsp;</td>
                <td width="25%">&nbsp;</td>
                <td width="25%">&nbsp;</td>
            </tr>
            <c:if test="${not empty isValid and isValid eq 1}">
                <tr>
                    <td width="25%">&nbsp;</td>
                    <td width="25%"><span class="value-text" id='mesg' name="mesg"><c:out value="${param.mesg}"></c:out></span></td>
                    <td width="25%">&nbsp;</td>
                    <td width="25%">&nbsp;</td>
                </tr>
                <tr>
                    <td width="25%" align="center"></td>
                    <td width="25%"><security:token/>
                        <input class="mainbutton" type="button" name="OK" value="OK" style="width: 180px;"
                               onclick="goHome( );"/>
                    </td>
                    <td width="25%">&nbsp;</td>
                    <td width="25%">&nbsp;</td>
                </tr>
            </c:if>
            <c:if test="${not empty isValid and isValid eq 0}">
                <tr>
                    <td width="25%" align="right" >
                        <span class="label">Customer CNIC:</span>
                    </td>
                    <td width="25%" align="left" >
                        <span class="value-text" id='CNIC' name="CNIC"><c:out value="${CNIC}"></c:out></span>
                    </td>
                    <td width="25%" align="right" >
                        <span class="label">Customer Mobile No:</span>
                    </td>
                    <td width="25%" align="left" ><span class="value-text" id="CMOB" name="CMOB"><c:out
                            value="${CMOB}"></c:out></span>
                    </td>
                </tr>
                <tr>
                    <td width="25%" align="right" >
                        <span class="label">Customer Name:</span>
                    </td>
                    <td width="25%" align="left" ><span class="value-text" id="CNAME" name="CNAME"><c:out
                            value="${CNAME}"></c:out></span></td>
                    <td width="25%" align="right" >
                        <span class="label">Father Name:</span>
                    </td>
                    <td width="25%" align="left" ><span class="value-text" id="FATHER_HUSBND_NAME"
                                                       name="FATHER_HUSBND_NAME"><c:out value="${FNAME}"></c:out></span>
                    </td>
                </tr>
                <tr>
                    <td width="25%" align="right" >
                        <span class="label">Date of Birth:</span>
                    </td>
                    <td width="25%" align="left" ><span class="value-text" id="CDOB" name="CDOB"><c:out
                            value="${CDOB}"></c:out></span></td>
                    <td width="25%" align="right" >&nbsp;</td>
                    <td width="25%" align="left" >&nbsp;</td>
                </tr>
                <tr>
                    <td width="25%" align="right" >
                        <span style="color: #FF0000">*</span>
                        <span class="label">Source of Income:</span>
                    </td>
                    <td width="25%" align="left" >
                        <select id="SOI" name="SOI" class="textBox">
                            <option value="" selected="selected">---All---</option>
                            <option value="Family Support">Family Support</option>
                            <option value="Freelancer">Freelancer</option>
                        </select>
                    </td>
                    <td width="25%" align="right" >
                        <span style="color: #FF0000">*</span>
                        <span class="label">Occupation:</span>
                    </td>
                    <td width="25%" align="left" >
                        <input type="text" id="OCCUPATION"
                               name="OCCUPATION" value="${param.OCCUPATION}" class="text"/>
                    </td>
                </tr>
                <tr>
                    <td width="25%" align="right" >
                        <span style="color: #FF0000">*</span>
                        <span class="label">Purpose of Account:</span>
                    </td>
                    <td width="25%" align="left" >
                        <select id="TRX_PUR" name="TRX_PUR" class="textBox">
                            <option value="" selected="selected">---All---</option>
                            <option value="Personal">Personal</option>
                            <option value="Business">Business</option>
                        </select>
                    </td>
                    <td width="25%">&nbsp;</td>
                    <td width="25%">&nbsp;</td>
                </tr>
                <tr>
                    <td width="25%" colspan="4">
                        <div align="center" class="hf">Next of Kin</div>
                    </td>
                </tr>
                <tr>
                    <td width="25%" align="right" >
                        <span style="color: #FF0000">*</span>
                        <span class="label">Name:</span>
                    </td>
                    <td width="25%" align="left" >
                        <input type="text" id="KIN_NAME"
                               name="KIN_NAME" value="${param.KIN_NAME}" class="text"/>
                    </td>
                    <td width="25%" align="right" >
                        <span style="color: #FF0000">*</span>
                        <span class="label">Mobile No:</span>
                    </td>
                    <td width="25%" align="left" >
                        <input type="text" id="Kin_MOB_NO"
                               name="Kin_MOB_NO" value="${param.Kin_MOB_NO}"
                               onkeypress="return maskNumber(this,event)" maxlength="11" class="text"/>
                    </td>
                </tr>
                <tr>
                    <td width="25%" align="right" >
                        <span style="color: #FF0000">*</span>
                        <span class="label">CNIC:</span>
                    </td>
                    <td width="25%" align="left" >
                        <input type="text" id="KIN_CNIC"
                               name="KIN_CNIC" value="${param.KIN_CNIC}"
                               onkeypress="return maskNumber(this,event)" maxlength="13" class="text"/>
                    </td>
                    <td width="25%" align="right" >
                        <span style="color: #FF0000">*</span>
                        <span class="label">Relationship:</span>
                    </td>
                    <td width="25%" align="left" >
                        <input type="text" id="KIN_RELATIONSHIP"
                               name="KIN_RELATIONSHIP" value="${param.KIN_RELATIONSHIP}" class="text"/>
                    </td>
                </tr>
                <tr>
                    <td width="25%" colspan="4">
                        <div align="center" class="hf">Originator Information</div>
                    </td>
                </tr>
                <tr>
                    <td width="25%" align="left" colspan="4">
                        <span style="color: blue; font-size: 14px; font-weight: 500">Originator 1</span>
                    </td>
                </tr>
                <tr>
                    <td width="25%" align="right" >
                        <span style="color: #FF0000">*</span>
                        <span class="label">International Remittance Location:</span>
                    </td>
                    <td width="25%" align="left" >
                        <input type="text" id="ORG_LOC1"
                               name="ORG_LOC1" value="${param.ORG_LOC_1}" class="text"/>
                    </td>
                    <td width="25%" align="right" >
                        <span style="color: #FF0000">*</span>
                        <span class="label">Relationship With Originator:</span>
                    </td>
                    <td width="25%" align="left" >
                        <select id="ORG_REL1" name="ORG_REL1" class="textBox">
                            <option value="" selected="selected">---All---</option>
                            <option value="Spouse">Spouse</option>
                            <option value="Children">Children</option>
                            <option value="Father">Father</option>
                            <option value="Mother">Mother</option>
                            <option value="Relative">Relative</option>
                            <option value="Friend">Friend</option>
                            <option value="Sister">Sister</option>
                            <option value="Brother">Brother</option>
                            <option value="Others">Others</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td width="25%" align="left" colspan="4">
                        <span style="color: blue; font-size: 14px; font-weight: 500">Originator 2</span>
                    </td>
                </tr>
                <tr>
                    <td width="25%" align="right" >
                        <span class="label">International Remittance Location:</span>
                    </td>
                    <td width="25%" align="left" >
                        <input type="text" id="ORG_LOC2"
                               name="ORG_LOC2" value="${param.ORG_LOC_2}" class="text"/>
                    </td>
                    <td width="25%" align="right" >
                        <span class="label">Relationship With Originator:</span>
                    </td>
                    <td width="25%" align="left" >
                        <select id="ORG_REL2" name="ORG_REL2" class="textBox">
                            <option value="" selected="selected">---All---</option>
                            <option value="Spouse">Spouse</option>
                            <option value="Children">Children</option>
                            <option value="Father">Father</option>
                            <option value="Mother">Mother</option>
                            <option value="Relative">Relative</option>
                            <option value="Friend">Friend</option>
                            <option value="Sister">Sister</option>
                            <option value="Brother">Brother</option>
                            <option value="Others">Others</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td width="25%" align="left" colspan="4">
                        <span style="color: blue; font-size: 14px; font-weight: 500">Originator 3</span>
                    </td>
                </tr>
                <tr>
                    <td width="25%" align="right" >
                        <span class="label">International Remittance Location:</span>
                    </td>
                    <td width="25%" align="left" >
                        <input type="text" id="ORG_LOC3"
                               name="ORG_LOC3" value="${param.ORG_LOC_3}" class="text"/>
                    </td>
                    <td width="25%" align="right" >
                        <span class="label">Relationship With Originator:</span>
                    </td>
                    <td width="25%" align="left" >
                        <select id="ORG_REL3" name="ORG_REL3" class="textBox">
                            <option value="" selected="selected">---All---</option>
                            <option value="Spouse">Spouse</option>
                            <option value="Children">Children</option>
                            <option value="Father">Father</option>
                            <option value="Mother">Mother</option>
                            <option value="Relative">Relative</option>
                            <option value="Friend">Friend</option>
                            <option value="Sister">Sister</option>
                            <option value="Brother">Brother</option>
                            <option value="Others">Others</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td width="25%" align="left" colspan="4">
                        <span style="color: blue; font-size: 14px; font-weight: 500">Originator 4</span>
                    </td>
                </tr>
                <tr>
                    <td width="25%" align="right" >
                        <span class="label">International Remittance Location:</span>
                    </td>
                    <td width="25%" align="left" >
                        <input type="text" id="ORG_LOC4"
                               name="ORG_LOC4" value="${param.ORG_LOC_4}" class="text"/>
                    </td>
                    <td width="25%" align="right" >
                        <span class="label">Relationship With Originator:</span>
                    </td>
                    <td width="25%" align="left" >
                        <select id="ORG_REL4" name="ORG_REL4" class="textBox">
                            <option value="" selected="selected">---All---</option>
                            <option value="Spouse">Spouse</option>
                            <option value="Children">Children</option>
                            <option value="Father">Father</option>
                            <option value="Mother">Mother</option>
                            <option value="Relative">Relative</option>
                            <option value="Friend">Friend</option>
                            <option value="Sister">Sister</option>
                            <option value="Brother">Brother</option>
                            <option value="Others">Others</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td width="25%" align="left" colspan="4">
                        <span style="color: blue; font-size: 14px; font-weight: 500">Originator 5</span>
                    </td>
                </tr>
                <tr>
                    <td width="25%" align="right" >
                        <span class="label">International Remittance Location:</span>
                    </td>
                    <td width="25%" align="left" >
                        <input type="text" id="ORG_LOC5"
                               name="ORG_LOC5" value="${param.ORG_LOC_5}" class="text"/>
                    </td>
                    <td width="25%" align="right" >
                        <span class="label">Relationship With Originator:</span>
                    </td>
                    <td width="25%" align="left" >
                        <select id="ORG_REL5" name="ORG_REL5" class="textBox">
                            <option value="" selected="selected">---All---</option>
                            <option value="Spouse">Spouse</option>
                            <option value="Children">Children</option>
                            <option value="Father">Father</option>
                            <option value="Mother">Mother</option>
                            <option value="Relative">Relative</option>
                            <option value="Friend">Friend</option>
                            <option value="Sister">Sister</option>
                            <option value="Brother">Brother</option>
                            <option value="Others">Others</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td width="25%">&nbsp;</td>
                    <td width="25%">&nbsp;</td>
                    <td width="25%">&nbsp;</td>
                    <td width="25%">&nbsp;</td>
                </tr>
                <tr>
                    <td width="25%" align="right" colspan="2">
                        <span style="color: #FF0000">*</span>
                        <span class="label">Agent MPIN:</span>
                    </td>
                    <c:choose>
                        <c:when test="${not empty mpinValid and mpinValid eq 0}">
                            <td width="25%"><input type="password" id="PIN" name="PIN" maxlength="4" autocomplete="off"
                                       class="text"/></td>
                            <td width="25%">&nbsp;</td>
                        </c:when>
                        <c:otherwise>
                            <td width="25%"><span class="value-text" name="PIN"><c:out value="****"></c:out></span></td>
                            <td width="25%">&nbsp;</td>
                        </c:otherwise>
                    </c:choose>
                </tr>
                <tr>
                    <td width="25%">&nbsp;</td>
                    <td width="25%">&nbsp;</td>
                    <td width="25%">&nbsp;</td>
                    <td width="25%">&nbsp;</td>
                </tr>
                <tr>
                    <td width="25%" colspan="4" style="text-align: center"><security:token/>
                        <input class="mainbutton" type="submit" id="confirm" name="confirm" value="Confirm"/>
                        <input class="mainbutton" type="button" name="cancel" value="Cancel" onclick="goHome( );"/>
                    </td>
                </tr>
                <tr>
                    <td width="25%">&nbsp;</td>
                    <td width="25%">&nbsp;</td>
                    <td width="25%">&nbsp;</td>
                    <td width="25%">&nbsp;</td>
                </tr>
                <tr>
                    <td width="25%">&nbsp;</td>
                    <td width="25%">&nbsp;</td>
                    <td width="25%">&nbsp;</td>
                    <td width="25%">&nbsp;</td>
                </tr>
            </c:if>
        </table>
        <input type="hidden" name="PIN_RETRY_COUNT" value="${PIN_RETRY_COUNT}"/>
        <input type="hidden" name="CMOB" value="${CMOB}"/>
        <input type="hidden" name="CNIC" value="${CNIC}"/>
        <input type="hidden" name="CNAME" value="${CNAME}"/>
        <input type="hidden" name="CDOB" value="${CDOB}"/>
        <input type="hidden" name="FATHER_HUSBND_NAME" value="${FNAME}"/>
        <input type="hidden" name="otpValid" value="${otpValid}"/>
        <input type="hidden" name="mpinValid" value="${mpinValid}"/>
        <input type="hidden" name="IS_LINKED_REQ" value="${IS_LINKED_REQ}"/>
        <input type="hidden" name="CW_AMOUNT" value="${param.CW_AMOUNT}"/>
        <input type="hidden" name="initialAmount" value="${param.initialAmount}"/>
    </form>

    <script language="javascript" type="text/javascript">

        function validateNumber(field, label) {

            if (field.value == "") {
                return false;
            }

            var charpos = field.value.search("[^0-9]");
            if (charpos >= 0) {
                alert('Please enter valid ' + label);
                return false;
            }
            return true;
        }

        function doRequired(field, label) {
            if (field.value.trim() == '' || field.value.length == 0) {
                alert(label + ' is required field.');
                field.focus();
                return false;
            }
            return true;
        }

        function acTypeInfo(sel) {
            if (sel.value == 1) {
                var elems = document.getElementsByClassName('l1form-feilds');
                for (var i = 0; i < elems.length; i++) {
                    elems[i].style.display = 'none';
                }
            } else if (sel.value == 2) {
                var elems = document.getElementsByClassName('l1form-feilds');
                for (var i = 0; i < elems.length; i++) {
                    elems[i].style.display = '';
                }
            }
        }

        if (navigator.appName == 'Microsoft Internet Explorer' || !!(navigator.userAgent.match(
                /Trident/) || navigator.userAgent.match(/rv 11/))) {
            // document.getElementById("citizenNumber").onfocusout = OpenActiveX();
        }
        else {
            alert("For now biometric verification is supported with internet explorer only");
        }

        var n = 100;
        setInterval(function () {
            if (n == 100) {
                document.getElementById("resetButton").click();
                n = 1000000000000000000000;
            }
        }, n);


    </script>
</div>

</body>

</html>




