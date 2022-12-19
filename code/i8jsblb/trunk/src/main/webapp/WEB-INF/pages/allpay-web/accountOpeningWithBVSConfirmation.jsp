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

    <title>Cash In</title>
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

            var PIN = document.getElementById('PIN');
            var CNIC = document.getElementById('CNIC');
            var CDCUSTMOB = document.getElementById('CMOB');
            var OTP = document.getElementById('OTP');

            if(!validateNumber(PIN) || PIN.value.length < 4) {
                return writeError('Kindly Enter 4 Digit PIN Code.');
            }
            if(!validateNumber(OTP) || OTP.value.length < 5) {
                return writeError('Kindly Enter 5 Digit One Time Pin.');
            }

            if(CNIC.value != '') {
                if(!validateNumber(CNIC) || CNIC.value.length < 13) {
                    return writeError('Kindly Enter 13 Digit Customer CNIC Number.');
                }

                if((document.getElementById('cnicCustomer').innerHTML) == CNIC.value) {
                    return writeError("Account Holder's CNIC # and Walkin Customer CNIC # can't be same.");
                }

            }

            if(CDCUSTMOB.value != '') {

                if(!validateNumber(CDCUSTMOB) || CDCUSTMOB.value.length < 11) {
                    return writeError('Kindly Enter 11 Digit Customer Mobile Number.');
                }

                if((document.getElementById('CMOB').innerHTML) ==  CDCUSTMOB.value) {
                    return writeError("Account Holder's Mobile # and Walkin Customer Mobile # can't be same.");
                }

                if(CNIC.value == '') {
                    return writeError("Customer CNIC is required.");
                }
            }

            if(navigator.appName == 'Netscape') {
                document.getElementById("confirm").disabled = true;
            } else {
                document.getElementById("confirm").disabled = 'disabled';
            }

            return true;
        }

        function goHome( )
        {
            var homeUrl = "welcomescreen.aw?ACID=" + ${ACID} + "&BAID=" + ${BAID} + "&UID=" + ${sessionScope.UID};
            window.location.replace( homeUrl );
        }
    </script>

</head>
<body bgcolor="#ffffff">
<title>
    Branchless Banking Account Opening Form
</title>

<div style="height:95%">
    <jsp:include page="header.jsp"></jsp:include>
</div>
<div class="vertical-left-menu ">
    <jsp:include page="agentWebMenu.jsp"></jsp:include>
</div>
<div class="main-body-area">
<%--<html:form  name = "accountOPening" id = "accountOPening" commandName="mfsAccountModel" action="accountOpeningWithBVSConfirmation.aw" method="post" onsubmit="return validate()">--%>
    <form method="post" action="<c:url value="/accountOpeningWithBVSConfirmation.aw"/>" onsubmit="return validate()">
        <table align="center" width="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
                <td align="center" colspan="2">&nbsp;</td>
                <td>&nbsp;</td>
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
                <td>&nbsp;</td>
            </tr>
            <c:if test="${empty isValid}">
                <tr>
                    <td colspan="5">
                        <div align="center" class="hf">Branchless Banking Account Opening Form</div>
                    </td>
                    <td>&nbsp;</td>
                </tr>
            </c:if>
            <tr>
                <td align="center" colspan="2">&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <c:if test="${not empty isValid and isValid eq 1}">
                <tr>
                    <td>&nbsp;</td>
                    <td>
                        <div align="center" class="hf">Customer Branchless Banking Account Opened Successfully.</div>
                    </td>
                </tr>
                <tr>
                    <td align="center"></td>
                    <td><security:token/>
                        <div align="center" class="hf">
                            <input class="mainbutton" type="button" name="OK" value="OK" style="width: 180px;" onclick="goHome( );"/>
                        </div>
                    </td>
                </tr>
            </c:if>
            <c:if test="${not empty isValid and isValid eq 0}">
                <tr>
                    <td align="right" width="50%">
                        <span style="color: #FF0000">*</span>
                        <span class="label">Customer CNIC:</span>
                    </td>
                    <td><span class="value-text" id='CNIC' name="CNIC"><c:out value="${CNIC}"></c:out></span></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td align="right" width="50%">
                        <span style="color: #FF0000">*</span>
                        <span class="label">Customer Mobile No:</span>
                    </td>
                    <td width="50%"><span class="value-text" id="CMOB" name="CMOB"><c:out value="${CMOB}"></c:out></span></td>
                </tr>
                <tr>
                        <td align="right" width="50%">
                            <span style="color: #FF0000">*</span>
                            <span class="label">One Time PIN:</span>
                        </td>
                    <c:choose>
                        <c:when test="${not empty otpValid and otpValid eq 0}">
                            <td><input type="password" id="OTP" name="OTP" value="${param.OTP}" onkeypress="return maskNumber(this,event)"
                                       maxlength="5" class="text" /></td>
                        </c:when>
                        <c:otherwise>
                            <td><span class="value-text" id ="OTP_1" name="OTP"><c:out value="*****"></c:out></span></td>
                        </c:otherwise>
                    </c:choose>
                </tr>
                <tr>
                    <td align="right" width="50%">
                        <span style="color: #FF0000">*</span>
                        <span class="label">Finger Index:</span>
                    </td>
                    <td align="left" class="td-width">
                        <c:choose>
                            <c:when test="${not empty param.appUserId or not empty appUserId}">
                                <div class="textBox" style="background: #D3D3D3; font-size:14px; ">
                                    <c:if test="${mfsAccountModel.fingerIndex==1}">Right Thumb</c:if>
                                    <c:if test="${mfsAccountModel.fingerIndex==2}">Right Index Finger</c:if>
                                    <c:if test="${mfsAccountModel.fingerIndex==3}">Right Middle Finger</c:if>
                                    <c:if test="${mfsAccountModel.fingerIndex==4}">Right Ring Finger</c:if>
                                    <c:if test="${mfsAccountModel.fingerIndex==5}">Right Little Finger</c:if>
                                    <c:if test="${mfsAccountModel.fingerIndex==6}">Left Thumb</c:if>
                                    <c:if test="${mfsAccountModel.fingerIndex==7}">Left Index Finger</c:if>
                                    <c:if test="${mfsAccountModel.fingerIndex==8}">Left Middle Finger</c:if>
                                    <c:if test="${mfsAccountModel.fingerIndex==9}">Left Ring Finger</c:if>
                                    <c:if test="${mfsAccountModel.fingerIndex==10}">Left little Finger</c:if>
                                </div>
                                <select style="display:none;" path="fingerIndex"
                                        cssClass="textBox"
                                        id="fingerIndex"
                                        tabindex="6">
                                    <option value="">[Select]</option>
                                    <option value="">Right Thumb</option>
                                    <option value="2">Right Index Finger</option>
                                    <option value="3">Right Middle Finger</option>
                                    <option value="4">Right Ring Finger</option>
                                    <option value="5">Right Little Finger</option>
                                    <option value="6">Left Thumb</option>
                                    <option value="7">Left Index Finger</option>
                                    <option value="8">Left Middle Finger</option>
                                    <option value="9">Left Ring Finger</option>
                                    <option value="10">Left little Finger</option>
                                </select>
                            </c:when>
                            <c:otherwise>
                                <select name = "fingerIndex" path="fingerIndex" cssClass="textBox" id="fingerIndex"
                                        tabindex="6">
                                    <option value="">[Select]</option>
                                    <option value="1">Right Thumb</option>
                                    <option value="2">Right Index Finger</option>
                                    <option value="3">Right Middle Finger</option>
                                    <option value="4">Right Ring Finger</option>
                                    <option value="5">Right Little Finger</option>
                                    <option value="6">Left Thumb</option>
                                    <option value="7">Left Index Finger</option>
                                    <option value="8">Left Middle Finger</option>
                                    <option value="9">Left Ring Finger</option>
                                    <option value="10">Left little Finger</option>

                                </select>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td align="right" class="td-width">
                        <input type="button" value="Enter Thumb Image" id="buttonBVS" >
                    </td>
                    <td align="left" class="td-width">


                        <div id="divBVSImage" style="display: none;">

                            <img id="OutputImage" name="OutputImage" alt="Finger Print" src=""
                                 width="142"
                                 height="142"/>
                            <input type="hidden" name="thumbImpressionPic" id="thumbImpressionPic"
                                   value="{thumbImpressionPic}">
                            <input type="Hidden" name="SampleImage" id="SampleImage" value="">
                            <img id="image"/>

                        </div>

                    </td>
                </tr>
                <tr>
                    <td align="right" width="50%">
                        <span style="color: #FF0000">*</span>
                        <span class="label">Agent MPIN:</span>
                    </td>
                    <c:choose>
                        <c:when test="${not empty mpinValid and mpinValid eq 0}">
                            <td><input type="password" id="PIN" name="PIN" maxlength="4" autocomplete="off" class="text"/></td>
                        </c:when>
                        <c:otherwise>
                            <td><span class="value-text" name="PIN"><c:out value="****"></c:out></span></td>
                        </c:otherwise>
                    </c:choose>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td align="right"></td>
                    <td><security:token/>
                        <input class="mainbutton" type="submit" id="confirm" name="confirm" value="Confirm"/>
                        <input class="mainbutton" type="button" name="cancel" value="Cancel" onclick="goHome( );"/>
                    </td>
                </tr>
            </c:if>
        </table>
        <input type="hidden" name="PIN_RETRY_COUNT" value="${PIN_RETRY_COUNT}"/>
        <input type="hidden" name="CMOB" value="${CMOB}"/>
        <input type="hidden" name="CNIC" value="${CNIC}"/>
        <input type="hidden" name="otpValid" value="${otpValid}"/>
        <input type="hidden" name="mpinValid" value="${mpinValid}"/>
        <input type="hidden" name="IS_LINKED_REQ" value="${IS_LINKED_REQ}"/>
        <input type="hidden" name="CW_AMOUNT" value="${param.CW_AMOUNT}"/>
        <input type="hidden" name="initialAmount" value="${param.initialAmount}"/>
    </form>

    <script language="javascript" type="text/javascript">

        function IsActiveXSupported() {
            var isSupported = false;

            alert("Welcome");
            if(window.ActiveXObject) {
                isSupported = true;
            }

            alert("1");
            if("ActiveXObject" in window) {
                return true;
            }

            try {
                var xmlDom = new ActiveXObject("Microsoft.XMLDOM");
                alert("2");
                isSupported = true;
            } catch (e) {
                if (e.name === "TypeError" || e.name === "Error") {
                    isSupported = true;
                }
            }

            alert("Supported: " + isSupported);
            return isSupported;
        }

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

        function onFormSubmit(theForm) {
            var jq = jQuery.noConflict();

            if (theForm.customerAccountTypeId.value == 2) {
                if (!doRequired(theForm.fundSourceId, 'Source Of Fund')) {
                    return false;
                }
            }

            if (doRequired(theForm.customerAccountTypeId, 'Account Type')
                && doRequired(theForm.fingerIndex, 'Finger Index')
                && doRequired(theForm.thumbImpressionPic, 'Thumb Impression')
                && doRequired(theForm.agentPIN, 'Agent Pin') && validateNumber(
                    theForm.agentPIN, 'Agent Pin')

            ) {

            } else {
                return false;
            }

            if (jq("#divBVSImage").css('display') === 'none') {
                alert('Thumb Impression is required field.');
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


<!-- Attaching to an ActiveX event-->
<script language="javascript">

    var jq = jQuery.noConflict();

    jq("#buttonBVS").click(function () {
        OpenActiveX();
    });

    function OpenActiveX() {
        try {

            document.OurActiveX.Open(); //Running method from activeX
            //getting parameter to the ActiveX
            var template = document.OurActiveX.MyParam;

            if (template === "") {
                alert(
                    "Problem extracting  template at the moment . Please try again after some time");
            }
            else {
                document.getElementById("thumbImpressionPic").value = template;
                var image = document.OurActiveX.Image;
                document.getElementById("SampleImage").value = image;
                document.getElementById("OutputImage").src = "data:image/Bmp;base64," + image;
                //document.scan.submit();
                var e = document.getElementById("divBVSImage");
                if (e.style.display == 'none')
                    e.style.display = 'block';

            }

        }
        catch (Err) {
            alert(Err.description);
        }
    }


</script>

<!-- Our activeX object -->
<OBJECT id="OurActiveX" name="OurActiveX" classid="clsid:121C3E0E-DC6E-45dc-952B-A6617F0FAA32"
        VIEWASTEXT
        codebase="${contextPath}/scripts/cabinet/OurActiveX.cab"></OBJECT>

</body>

</html>




