<!DOCTYPE html>
<%@page import="com.inov8.microbank.common.util.CommandFieldConstants" %>
<%@include file="/common/taglibs.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Cash Transfer</title>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,IE=11,IE=10"/>
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
            top: 300px;

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

        function isNumberKey(evt) {
            var charCode = (evt.which) ? evt.which : event.keyCode
            if (charCode > 31 && (charCode < 48 || charCode > 57))
                return false;
            return true;
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
            var jq = jQuery.noConflict();

            if (jq("#divBVSImage").css('display') === 'none') {
                return writeError('Kindly Add Thumb Impression.');
            }


            var pin = document.getElementById('pin').value;
            if (pin.length == 0) {
                return writeError('MPIN is required.');
            }
            if (pin.length != 4 || !isNumber(pin)) {
                return writeError('MPIN must be 4 digits.');
            }


            if (navigator.appName == 'Netscape') {
                document.getElementById("confirm").disabled = true;
            } else {
                document.getElementById("confirm").disabled = 'disabled';
            }

            return true;
        }

        function goBack() {
            var homeUrl = "welcomescreen.aw";
            window.location.replace(homeUrl);
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
    <form method="post" action="<c:url value="/case2CashStepOneInfo.aw"/>" onsubmit="return validate()">

        <table align="center" width="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
                <td align="center" colspan="2">&nbsp;<security:token/>&nbsp;&nbsp;</td>
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
                    <div align="center" class="hf">Confirm Transaction</div>
                </td>
            </tr>
            <tr>
                <td align="center" colspan="2">&nbsp;</td>
            </tr>
            <tr>
                <td align="right" width="50%">
                    <span class="label">Receiver City  :</span>
                    <span style="color: #FF0000"></span>
                </td>
                <td>
                    <select class="actionType" tabindex="1" id = "RECEIVER_CITY_1" name="RECEIVER_CITY_1">
                        <option value="">---All---</option>
                        <c:if test="${citiesList != null}">
                            <c:forEach var="city" items="${citiesList}">
                                <option value="${city}" name="${city}">${city}</option>
                            </c:forEach>
                        </c:if>
                    </select>
                </td>
            </tr>
            <tr>
                <td align="right" width="50%">
                    <span class="label">Sender City  :</span>
                    <span style="color: #FF0000"></span>
                </td>
                <td>
                    <span class="value-text">${SENDER_CITY}</span>
                    <input type="hidden" id="senderCity"
                           name="<%= CommandFieldConstants.KEY_SENDER_CITY %>" value="${SENDER_CITY}"
                           maxlength="100" class="text"/></td>
            </tr>
            <tr>
                <td align="right" width="50%">
                    <span class="label">Sender Mobile No  :</span>
                    <span style="color: #FF0000"></span>
                </td>
                <td>
                    <span class="value-text">${SWMOB}</span>
                    <input type="hidden" id="walikInCustMobNo"
                           name="<%= CommandFieldConstants.KEY_WALKIN_SENDER_MOBILE %>" value="${SWMOB}"
                           maxlength="11" class="text"/></td>
            </tr>
            <tr>
                <td align="right" width="50%">
                    <span class="label">Sender CNIC :</span>
                    <span style="color: #FF0000"></span>
                </td>
                <td>
                    <span class="value-text">${SWCNIC}</span>
                    <input type="hidden" id="walikInCustCnic" name="<%= CommandFieldConstants.KEY_S_W_CNIC %>"
                           value="${SWCNIC}"
                           maxlength="13" class="text"/></td>
            </tr>

            <tr>
                <td align="right" width="50%">
                    <span class="label">Receiver Mobile No :</span>
                    <span style="color: #FF0000"></span>
                </td>
                <td>
                    <span class="value-text">${RWMOB}</span>
                    <input type="hidden" id="receiverMobNo"
                           name="<%= CommandFieldConstants.KEY_RECIPIENT_WALKIN_MOBILE %>" value="${RWMOB}"
                           maxlength="11" class="text"/></td>
            </tr>

            <tr>
                <td align="right" width="50%">
                    <span class="label">Receiver CNIC :</span>
                    <span style="color: #FF0000"></span>
                </td>
                <td>
                    <span class="value-text">${RWCNIC}</span>
                    <input type="hidden" id="receiverCnic" name="<%= CommandFieldConstants.KEY_R_W_CNIC %>"
                           value="${RWCNIC}" maxlength="13" class="text"/>
                </td>
            </tr>

            <tr>
                <td align="right" width="50%">
                    <span class="label">Amount :</span>
                    <span style="color: #FF0000"></span>
                </td>
                <td>
                    <span class="value-text">${TXAMF}</span>
                    <input type="hidden" name="<%= CommandFieldConstants.KEY_TXAMF %>" value="${TXAMF}"/>
                    <input type="hidden" id="amount" name="<%= CommandFieldConstants.KEY_TX_AMOUNT %>" value="${TXAM}"
                           maxlength="5" class="text"/></td>
            </tr>

            <tr>
                <td align="right" width="50%">
                    <span class="label">Charges :</span>
                </td>
                <td>

                    <span class="value-text">${TPAMF}</span>
                    <input type="hidden" name="<%= CommandFieldConstants.KEY_TPAMF %>" value="${TPAMF}"/>
                    <input type="hidden" name="<%= CommandFieldConstants.KEY_CAMT %>" value="${CAMT}"/>
                </td>

            </tr>
            <tr>
                <td align="right" width="50%">
                    <span class="label">Total Amount :</span>
                </td>
                <td>
                    <span class="value-text">${TAMTF}</span>
                    <input type="hidden" name="<%= CommandFieldConstants.KEY_TAMTF %>" value="${TAMTF}"/>

                </td>
            </tr>

            <c:if test="${ISRECBVS == 1}">

                <input type="hidden" name="<%= CommandFieldConstants.KEY_IS_BVS_REQUIRED %>"
                       id="<%= CommandFieldConstants.KEY_IS_BVS_REQUIRED %>" value="${ISRECBVS}"/>
                <tr>
                    <td align="right" width="50%">

                        <div>
                            <input type="button" value="Enter Thumb Image" id="buttonBVS">
                        </div>

                    </td>
                    <td>

                        <div id="divBVSImage" style="display: none;">

                            <img id="OutputImage" id='file' name="OutputImage" alt="Finger Print" src=""
                                 width="142"
                                 height="142"/>
                            <input type="hidden" name="FINGER_TEMPLATE" id="FINGER_TEMPLATE">
                            <input type="Hidden" name="SampleImage" id="SampleImage" value="">
                            <select cssClass="textBox"
                                    id="FINGER_INDEX" name="FINGER_INDEX"
                                    required>
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
                                <option value="10">Left Little Finger</option>


                            </select>
                            <img id="image"/>


                        </div>
                    </td>
                </tr>
            </c:if>
            <tr>
                <td align="right" width="50%">
                    <span style="color: #FF0000">*</span>
                    <span class="label">MPIN :</span>
                </td>
                <td><input type="password" id="pin" name="<%= CommandFieldConstants.KEY_PIN %>"
                           onkeypress="return isNumberKey(event)" maxlength="4" size="6"/></td>
            </tr>
            <tr>
                <td colspan="2">
                    &nbsp;<input type="hidden" name="<%= CommandFieldConstants.KEY_PROD_ID %>" value="50011"/>
                    <input type="hidden" name="<%= CommandFieldConstants.KEY_U_ID %>" value="${UID}"/>
                    <input type="hidden" name="<%= CommandFieldConstants.KEY_TPAM %>" value="${TPAM}"/>
                    <input type="hidden" name="<%= CommandFieldConstants.KEY_MAN_OT_PIN %>" value="${MANUAL_OTPIN}"/>
                    <input type="hidden" name="<%= CommandFieldConstants.KEY_CAMT %>" value="${CAMT}"/>
                    <input type="hidden" name="<%= CommandFieldConstants.KEY_IS_BVS_REQUIRED %>" value="1"/>
                    <input type="hidden" name="<%= CommandFieldConstants.KEY_ENCRYPTION_TYPE %>" value="1"/>
                </td>
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <input class="mainbutton" type="submit" id="confirm" name="confirm" value="Confirm"/>
                    <input type="button" name="cancel" value="Cancel" class="button" onclick="goBack()"/>
                </td>
            </tr>
        </table>
    </form>
    <!-- Our activeX object -->
    <OBJECT id="OurActiveX" name="OurActiveX" classid="clsid:121C3E0E-DC6E-45dc-952B-A6617F0FAA32" VIEWASTEXT
            codebase="${contextPath}/scripts/cabinet/OurActiveX.cab"></OBJECT>
</div>
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
                alert("Problem extracting  template at the moment . Please make sure finger print scanner is attached properly and try again after some time.\n");
            }
            else {
                document.querySelector("#FINGER_TEMPLATE").value = template;
                var image = document.OurActiveX.Image;
                document.querySelector("#SampleImage").value = image;
                document.querySelector("#OutputImage").src = "data:image/Bmp;base64," + image;
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

    if (jq("#divBVSImage").css('display') === 'none') {
        var homeUrl = "welcomescreen.aw";
        var bvsProcessCheck =   confirm("This transaction will perform via BVS for recipient. Do you want to continue ?");
        if (bvsProcessCheck != true) {
            window.location.replace(homeUrl);
        }


        if (navigator.appName == 'Microsoft Internet Explorer' || !!(navigator.userAgent.match(/Trident/) || navigator.userAgent.match(/rv 11/))) {
        }
        else {
            alert("Currently \'Biometric Verification\' is supported with \'Internet Explorer\' only.\nPlease use \'Internet Explorer\' to perform biomertic verification.\n");
            window.location.replace(homeUrl);
        }
    }




</script>
</body>

</html>