<!--Author: Mohammad Shehzad Ashraf-->
<!--Modified By: Waqar Ashraf -->

<%@include file="/common/taglibs.jsp" %>
<%@page import="com.inov8.microbank.common.util.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <meta name="decorator" content="decorator2">
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>

    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/prototype.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.11.0.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.maskedinput.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-ui-custom.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselect.min.js"></script>
    <link rel="stylesheet" type="text/css"
          href="styles/deliciouslyblue/calendar.css"/>
    <link rel="stylesheet" type="text/css" href="styles/jquery.multiselect.css"/>
    <link rel="stylesheet" type="text/css" href="styles/jquery-ui-custom.min.css"/>

    <meta http-equiv="Content-Type"
          content="text/html; charset=iso-8859-1"/>
    <meta http-equiv="cache-control" content="no-cache"/>
    <meta http-equiv="expires" content="0"/>
    <meta http-equiv="pragma" content="no-cache"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,IE=11,IE=10"/>
    <c:choose>
        <c:when test="${not empty param.appUserId or not empty mfsDebitCardModel.appUserId}">
            <meta name="title"
                  content="${pageMfsId}: ${mfsDebitCardModel.nadraName}"/>
        </c:when>
        <c:otherwise>
            <meta name="title" content="Debit Card Requests Change Form"/>
        </c:otherwise>
    </c:choose>

    <style>
        #table_form {
            display: block;
            width: 100%;
            height: auto;
            margin: 0px;
            padding: 0px;
        }

        #table_form .left {
            display: block;
            width: 49%;
            float: left;
        }

        #table_form .right {
            display: block;
            width: 49%;
            float: right;
        }
    </style>

    <%@include file="/common/ajax.jsp" %>
    <script language="javascript" type="text/javascript">
        var jq = $.noConflict();

        jq(function ($) {
            $("#dob").mask("99/99/9999", {placeholder: "dd/mm/yyyy"});
            $("#nicExpiryDate").mask("99/99/9999", {placeholder: "dd/mm/yyyy"});
            $("#cnicIssuanceDate").mask("99/99/9999", {placeholder: "dd/mm/yyyy"});

        });

        <%--function openPrinterWindow() {--%>
        <%--    var appUserId = '<%= request.getParameter("appUserId") %>';--%>
        <%--    var location = '${pageContext.request.contextPath}/p_mnonewmfsaccountform_printer.html?appUserId=' + appUserId;--%>
        <%--    window.open(location, 'printWindow');--%>

        <%--}--%>

        <%--function openPrintWindow() {--%>
        <%--    if (isValidFormForPrinting()) {--%>
        <%--        var location = '${pageContext.request.contextPath}/p_mnonewmfsaccountform_print.html';--%>
        <%--        window.open(location, 'printWindow');--%>
        <%--    }--%>
        <%--}--%>

        function isNumericWithHyphin(code) {
            return ((code >= zeroCharCode && code <= nineCharCode)
                ||
                code == 46 ||   /*delete */
                code == 8 || 	/*back */
                code == 36 ||	/*home */
                code == 35 || 	/*end */
                code == 37 || 	/* left arrow */
                code == 39 || 	/* right arrow  */
                code == 38 || 	/* up arrow */
                code == 40 ||   /* down arrow */
                code == 9 ||	/* tab */
                code == 13 ||   /* enter */
                code == 16 ||   /* shift */
                code == 17 ||   /* ctrl */
                code == 18 ||   /* alt */
                code == minusCharCode /* minus character */
            );
        }

        function maskCNIC(obj, e) {
            var code = e.charCode ? e.charCode : e.keyCode;
            if (!isNumericWithHyphin(code) || code == 38 || code == 35 || code == 36 || code == 37 || code == 39 || code == 40) {
                return false;
            }
            return true;
        }

        /**
         *    method : getFileSize()
         *    author : kashif
         *    description : this method will return file size in mega bytes (mb)
         */
        function getFileSize(file) {

            if (file != null && file.files != null && file.files[0] != null) {

                return ((file.files[0].size / 1024) / 1024);
            }

            return 0;
        }

    </script>
    <%
        String createPermission = PortalConstants.MNG_GEN_ACC_CREATE;
        createPermission += "," + PortalConstants.PG_GP_CREATE;
        createPermission += "," + PortalConstants.MNG_BB_CUST_CREATE;
        createPermission += "," + PortalConstants.RET_GP_CREATE;
        createPermission += "," + PortalConstants.ADMIN_GP_CREATE;
        createPermission += "," + PortalConstants.HOME_PAGE_QUICK_BB_CUST_CREATE;

        String updatePermission = PortalConstants.MNG_GEN_ACC_UPDATE;
        updatePermission += "," + PortalConstants.PG_GP_UPDATE;
        updatePermission += "," + PortalConstants.MNG_BB_CUST_UPDATE;

        String printPermission = PortalConstants.BB_CUST_PRINT_FORM_READ;
        printPermission += "," + PortalConstants.PG_GP_READ;
        printPermission += "," + PortalConstants.MNG_GEN_ACC_CREATE;
        printPermission += "," + PortalConstants.RET_GP_READ;
        printPermission += "," + PortalConstants.ADMIN_GP_READ;

        String reprintPermission = PortalConstants.BB_CUST_REPRINT_FORM_READ;
        reprintPermission += "," + PortalConstants.PG_GP_READ;
        reprintPermission += "," + PortalConstants.MNG_GEN_ACC_CREATE;
        reprintPermission += "," + PortalConstants.RET_GP_CREATE;
        reprintPermission += "," + PortalConstants.ADMIN_GP_CREATE;
    %>

</head>
<body bgcolor="#ffffff">

<div id="successMsg" class="infoMsg" style="display: none;"></div>
<spring:bind path="mfsDebitCardModel.*">
    <c:if test="${not empty status.errorMessages}">
        <div class="errorMsg">
            <c:forEach var="error" items="${status.errorMessages}">
                <c:out value="${error}" escapeXml="false"/>
                <br/>
            </c:forEach>
        </div>
    </c:if>
</spring:bind>

<c:if test="${not empty messages}">
    <div class="infoMsg" id="successMessages">
        <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false"/>
            <br/>
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session"/>
</c:if>
<html:form name="debitCardChangeInfoForm" commandName="mfsDebitCardModel"
           enctype="multipart/form-data" method="POST"
           onsubmit="return onFormSubmit(this)"
           action="debitcardchangeinfoform.html">

<%--    <c:if test="${not empty param.appUserId or not empty mfsDebitCardModel.appUserId}">--%>
<%--        <input type="hidden" name="isUpdate" id="isUpdate" value="true"/>--%>
<%--        <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"--%>
<%--               value="<%=PortalConstants.ACTION_UPDATE%>"/>--%>
<%--    </c:if>--%>
<%--    <c:if test="${empty param.appUserId && empty mfsDebitCardModel.appUserId}">--%>
<%--        <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"--%>
<%--               value="<%=PortalConstants.ACTION_CREATE%>"/>--%>
<%--    </c:if>--%>

<%--    <c:if test="${not empty param.appUserId }"><input type="hidden" name="appUserId" value="${param.appUserId}"/></c:if>--%>
<%--    <c:if test="${not empty appUserId}"><input type="hidden" name="appUserId" value="${appUserId}"/></c:if>--%>

    <input type="hidden" name="actionAuthorizationId" value="${param.authId}"/>
    <input type="hidden" name="resubmitRequest" value="${param.isReSubmit}"/>

<%--    <html:hidden path="accounttypeName"/>--%>
<%--    <html:hidden path="segmentNameStr"/>--%>
<%--    <html:hidden path="fundSourceStr"/>--%>
<%--    <html:hidden path="regStateName"/>--%>
<%--    <html:hidden path="permanentHomeAddCityName"/>--%>
<%--    <html:hidden path="taxRegimeName"/>--%>

    <html:hidden path="<%=PortalConstants.KEY_USECASE_ID %>"/>
    <div class="infoMsg" id="errorMessages" style="display:none;"></div>
    <div id="table_form">
        <table>
            <tr>
                <td colspan="2" align="left" bgcolor="FBFBFB">
                    <h2>
                        Applicant Information
                    </h2>
                </td>
            </tr>
            <tr>
                <td height="16" align="right" bgcolor="F3F3F3" class="formText">
                    <span style="color: #FF0000">*</span>Mobile No:
                </td>
                <td bgcolor="FBFBFB" class="formText">
                    <c:choose>
                        <c:when test="${not empty param.appUserId or not empty mfsDebitCardModel.appUserId}">
                            <html:input path="mobileNo" cssClass="textBox" tabindex="2"
                                        maxlength="11" readonly="true" style="background: #D3D3D3;"
                                        onkeypress="return maskNumber(this,event)"/>
                        </c:when>
                        <c:otherwise>
                            <html:input path="mobileNo" cssClass="textBox" tabindex="2"
                                        maxlength="11" onkeypress="return maskNumber(this,event)"/>
                        </c:otherwise>
                    </c:choose>
                </td>

                <td height="16" align="right" bgcolor="F3F3F3" class="formText"
                    width="25%">
                    <span style="color: #FF0000">*</span>Nadra Name:
                <td align="left" bgcolor="FBFBFB" class="formText" width="25%">
                <html:input path="nadraName" cssClass="textBox" tabindex="2"
                            maxlength="11" readonly="true" style="background: #D3D3D3;"
                            onkeypress="return maskNumber(this,event)"/>
                </td>
            </tr>

            <tr>
                <td height="16" align="right" bgcolor="F3F3F3" class="formText">
                    <span style="color: #FF0000">*</span>CNIC #:
                </td>
                <td bgcolor="FBFBFB" class="formText">
                    <c:choose>
                        <c:when test="${not empty param.appUserId or not empty mfsDebitCardModel.appUserId}">
                            <html:input path="cnic" cssClass="textBox" style="background: #D3D3D3; " readonly="true"
                                        tabindex="11" maxlength="13" onkeypress="return maskNumber(this,event)"/>
                        </c:when>
                        <c:otherwise>
                            <html:input path="cnic" cssClass="textBox" tabindex="11" maxlength="13"
                                        onkeypress="return maskNumber(this,event)"/>
                        </c:otherwise>
                    </c:choose>
                </td>

                <td height="16" align="right" bgcolor="F3F3F3" class="formText">
                    <span style="color: #FF0000">*</span>Channel Name:
                </td>
                <td bgcolor="FBFBFB" class="formText">
                    <html:input path="channelName" cssClass="textBox" tabindex="2"
                                maxlength="11" readonly="true" style="background: #D3D3D3;"
                                onkeypress="return maskNumber(this,event)"/>
                </td>
            </tr>

            <tr>
                <td height="16" align="right" bgcolor="F3F3F3" class="formText"
                    width="25%">
                    <span style="color: #FF0000">*</span>Embosing Name:
                <td align="left" bgcolor="FBFBFB" class="formText" width="25%">
                    <html:input path="embossingName" id="embossingName" cssClass="textBox" tabindex="21" maxlength="19"/>
                </td>
                <td align="right" class="formText" bgcolor="F3F3F3"><span style="color: #FF0000">*</span>Card Number:</td>
                <td align="left">
                        <html:input path="cardNumber" cssClass="textBox" tabindex="2"
                                    maxlength="11" readonly="true" style="background: #D3D3D3;"
                                    onkeypress="return maskNumber(this,event)"/>
                </td>

            </tr>

            <tr>
                <td align="right" bgcolor="F3F3F3" class="formText">
                    <span style="color: #FF0000">*</span>Card Product Type:
                </td>
                <td align="left" bgcolor="FBFBFB" class="formText">
                    <html:select path="cardProductCodeId" cssClass="textBox" tabindex="2"
                                maxlength="11"
                                onkeypress="return maskNumber(this,event)">
                        <html:option value="">[Select]</html:option>
                        <c:if test="${cardProductTypeList != null}">
                            <html:options items="${cardProductTypeList}" itemLabel="cardProductName"
                                          itemValue="cardProductCodeId"/>
                        </c:if>
                    </html:select>

                </td>

                <td height="16" align="right"  class="formText">Debit Card Printing City</td>
                <td width="58%" align="left">

                    <html:select path="city" cssClass="textBox"  tabindex="13" onkeypress="return maskNumber(this,event)"  >
                        <html:option value="">---All---</html:option>
                        <html:option value="Islamabad" label="Islamabad"/>
                        <html:option value="Karachi" label="Karachi"/>
                    </html:select>
                </td>
            </tr>

            <tr>
                <td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">
                    <span style="color: #FF0000">*</span>Mailing Address:
                <td align="left" bgcolor="FBFBFB" class="formText" width="25%">
                    <html:textarea style="height:50px;" path="mailingAddress" tabindex="25" cssClass="textBox" id="mAddress"
                                   maxlength="250" minlength="12"/>
                </td>
                <td height="16" align="right" bgcolor="F3F3F3" class="formText">
                    <span style="color: #FF0000">*</span>Segment Name:
                </td>
                <td bgcolor="FBFBFB" class="formText">
                            <html:input path="segmentName" cssClass="textBox" tabindex="2"
                                        maxlength="11" readonly="true" style="background: #D3D3D3;"
                                        onkeypress="return maskNumber(this,event)"/>
                </td>
            </tr>


            <c:if test="${not empty param.appUserId or not empty mfsDebitCardModel.appUserId}">
                <tr>
                    <td align="right" bgcolor="F3F3F3" class="formText">
                        <span style="color: #FF0000">*</span>Card State:
                    </td>
                    <td align="left" bgcolor="FBFBFB" class="formText">
                    <html:input path="cardState" cssClass="textBox" tabindex="2"
                                maxlength="11" readonly="true" style="background: #D3D3D3;"
                                onkeypress="return maskNumber(this,event)"/>
                    </td>

                <%--                    <td align="left" bgcolor="FBFBFB" class="formText">--%>
<%--                        <html:select path="cardStateId" cssClass="textBox"--%>
<%--                                     id="cardStateId"--%>
<%--                                     tabindex="18">--%>
<%--                            <html:option value="">[Select]</html:option>--%>
<%--                            <c:if test="${cardStateList != null}">--%>
<%--                                <html:options items="${cardStateList}" itemLabel="name"--%>
<%--                                              itemValue="cardStateId"/>--%>
<%--                            </c:if>--%>
<%--                        </html:select>--%>

<%--                    </td>--%>

                    <td align="right" bgcolor="F3F3F3" class="formText">
                        <span style="color: #FF0000">*</span>Card Status:
                    </td>
                    <td align="left" bgcolor="FBFBFB" class="formText">
                    <html:input path="cardStatus" cssClass="textBox" tabindex="2"
                                maxlength="11" readonly="true" style="background: #D3D3D3;"
                                onkeypress="return maskNumber(this,event)"/>
                    </td>

                <%--                    <td align="left" bgcolor="FBFBFB" class="formText">--%>
<%--                        <html:select path="cardStatusId" cssClass="textBox"--%>
<%--                                     id="cardStatusId"--%>
<%--                                     tabindex="18">--%>
<%--                            <html:option value="">[Select]</html:option>--%>
<%--                            <c:if test="${cardStatusList != null}">--%>
<%--                                <html:options items="${cardStatusList}" itemLabel="name"--%>
<%--                                              itemValue="cardStatusId"/>--%>
<%--                            </c:if>--%>
<%--                        </html:select>--%>

<%--                    </td>--%>
                </tr>
            </c:if>

        </table>
    </div>

    <table>
        <tr>
            <td width="100%" align="left" bgcolor="FBFBFB" colspan="4">
<%--                <c:choose>--%>
<%--                    <c:whe  n test="${not empty param.appUserId or not empty mfsDebitCardModel.appUserId}">--%>
<%--                        <authz:authorize ifAnyGranted="<%=updatePermission%>">--%>
                            <input type="submit" class="button" value="Update" tabindex="42"/>&nbsp;
<%--                        </authz:authorize>--%>
                        <input type="button" class="button" value="Cancel" tabindex="43"
                               onclick="javascript: window.location='debitcardmanagement.html?actionId=<%=PortalConstants.ACTION_RETRIEVE%>'"/>
<%--                    </c:when>--%>
<%--                </c:choose>--%>
            </td>
        </tr>
    </table>
</html:form>
<%--<ajax:updateField parser="new ResponseXmlParser()" source="taxRegimeId" eventType="change" action="taxRegimeId"--%>
<%--                  target="fed" baseUrl="${contextPath}/p_loadfed.html"--%>
<%--                  parameters="taxRegimeId={taxRegimeId}"></ajax:updateField>--%>
<ajax:updateField parser="new ResponseXmlParser()" source="taxRegimeId" eventType="change" action="taxRegimeId"
                  target="fed" baseUrl="${contextPath}/debit-card/debitcardmanagement.html"
                  parameters="taxRegimeId={taxRegimeId}"></ajax:updateField>
<form action="${contextPath}/debit-card/debitcardmanagement.html" method="post"
      name="userInfoListViewForm" id="debitCardRequestsViewModel">
<%--<form action="${contextPath}/p_pgsearchuserinfo.html" method="post"--%>
<%--      name="userInfoListViewForm" id="userInfoListViewModel">--%>
<%--    <input type="hidden"--%>
<%--           value="<c:out value="${mfsDebitCardModel.searchMfsId}"/>"--%>
<%--           id="userId" name="userId"/>--%>
<%--    <input type="hidden"--%>
<%--           value="<c:out value="${mfsDebitCardModel.searchFirstName}"/>"--%>
<%--           id="name" name="firstName"/>--%>
<%--    <input type="hidden"--%>
<%--           value="<c:out value="${mfsDebitCardModel.searchLastName}"/>"--%>
<%--           id="lastName" name="lastName"/>--%>
<%--    <input type="hidden"--%>
<%--           value="<c:out value="${mfsDebitCardModel.searchNic}"/>" id="nic"--%>
<%--           name="nic"/>--%>

<%--    <input type="hidden"--%>
<%--           value="<c:out value="${mfsDebitCardModel.customerAccountTypeId}"/>" id="customerAccountTypeId"--%>
<%--           name="nic"/>--%>
    <input type="hidden"
           value="<c:out value="${mfsDebitCardModel.mobileNo}"/>"
           id="mobileNo" name="mobileNo"/>
    <input type="hidden"
           value="<c:out value="${mfsDebitCardModel.embossingName}"/>"
           id="embossingName" name="embossingName"/>
    <input type="hidden"
           value="<c:out value="${mfsDebitCardModel.cardNumber}"/>"
           id="cardNumber" name="cardNumber"/>
    <input type="hidden"
           value="<c:out value="${mfsDebitCardModel.cnic}"/>" id="cnic"
           name="cnic"/>
</form>
<script language="javascript" type="text/javascript">

    //The following code is written because 'Others' value was selected dy default.
    var isSubmit = '<%= request.getParameter("_save") %>';
    var isAction = '<%= request.getParameter("actionId") %>';

    function checkOthersValue(comboBoxId, fieldId) {
        var comboBox = document.getElementById(comboBoxId);
        var field = document.getElementById(fieldId);
        var index = comboBox.length - 1;
        if (comboBox != null)
            comboBox.options[index].selected = 'selected';
        if (field != null)
            field.style.display = 'inline';
    }

    function checkNumericData() {
        if (!IsNumeric(document.forms.mfsAccountForm.mobileNo.value)) {
            alert("Mobile number must contain numeric data");
            return false;
        }
        onSave(document.forms.mfsAccountForm, null);

    }

    function IsNumeric(sText, label) {
        var ValidChars = "0123456789";
        var IsNumber = true;
        var Char;

        for (i = 0; i < sText.length && IsNumber == true; i++) {
            Char = sText.charAt(i);
            if (ValidChars.indexOf(Char) == -1) {
                alert(label + ' is not a valid number');
                IsNumber = false;
            }
        }
        return IsNumber;

    }

    <c:choose>
    <c:when test="${not empty param.appUserId or not empty mfsDebitCardModel.appUserId}">
    </c:when>
    <c:otherwise>

    function onCancel(a, b) {
        document.forms[0].reset();
    }

    </c:otherwise>
    </c:choose>

    function customTrim(str) {
        if (str != null) {
            while (strVar.charAt(strVar.length - 1) == ' ')
                strVar = strVar.substring(0, strVar.length - 1);

            while (strVar.charAt(0) == ' ')
                strVar = strVar.substring(1, strVar.length);
        }
        return str;
    }

    function validateComboWithOthers(selectBoxId, othersId, fieldName) {

        return true;
    }

    function calculate_age(birth_month, birth_day, birth_year) {
        var today_date = '<%=PortalDateUtils.currentFormattedDate("dd/MM/yyyy")%>';
        today_date = today_date.split("/");
        today_year = today_date[2];
        today_month = today_date[1];
        today_day = today_date[0];
        age = today_year - birth_year;
        if (today_month < birth_month) {
            age--;
        }
        if ((birth_month == today_month) && (today_day < birth_day)) {
            age--;
        }
        return age;
    }

    function isAgeLessThan18Years(dateOfBirth) {
        if (trim(dateOfBirth) != null && dateOfBirth != '' && dateOfBirth.length == 10) {
            dateOfBirth = dateOfBirth.split("/");
            var birthDay = dateOfBirth[0];
            var birthMonth = dateOfBirth[1];
            var birthYear = dateOfBirth[2];
            var age = calculate_age(birthMonth, birthDay, birthYear);
            if (age < 18) {
                return true;
            }
        }
        return false;
    }

    function isDobGreaterThan60Years(dateOfBirth) {
        if (trim(dateOfBirth) != null && dateOfBirth != '' && dateOfBirth.length == 10) {
            dateOfBirth = dateOfBirth.split("/");
            var birthDay = dateOfBirth[0];
            var birthMonth = dateOfBirth[1];
            var birthYear = dateOfBirth[2];
            var age = calculate_age(birthMonth, birthDay, birthYear);
            if (age > 60) {
                return true;
            }
        }
    }

    function submitForm(theForm) {

        if (validateFormChar(theForm)) {
            return false;//Allow special characters to incorporate Urdu changes
        }
        if (getFileSize(theForm.customerPic) > 2) {
            alert("Customer Picture can't be more than 2MB in size.");
            return false;
        }
        if (getFileSize(theForm.tncPic) > 2) {
            alert("Terms and Condition can't be more than 2MB in size.");
            return false;
        }
        if (getFileSize(theForm.signPic) > 2) {
            alert("Signature Picture can't be more than 2MB in size.");
            return false;
        }
        if (getFileSize(theForm.cnicFrontPic) > 2) {
            alert("CNIC Front Picture can't be more than 2MB in size.");
            return false;
        }
        if (getFileSize(theForm.cnicBackPic) > 2) {
            alert("CNIC Back Picture can't be more than 2MB in size.");
            return false;
        }
        if (getFileSize(theForm.level1FormPic) > 2) {
            alert("Level 1 Form Picture can't be more than 2MB in size.");
            return false;
        }
        var isNewCustomer = true;
        if (${not empty param.appUserId && not empty mfsDebitCardModel.appUserId}) {
            var isNewCustomer = false;
        }

        var currServerDate = '<%=PortalDateUtils.currentFormattedDate("dd/MM/yyyy")%>';
        if (trim(theForm.dob.value) != '' && isDateGreater(theForm.dob.value, currServerDate)) {
            alert('Future date of birth is not allowed.');
            document.getElementById('sDate').focus();
            return false;
        } else if (isAgeLessThan18Years(theForm.dob.value)) {
            alert('Customer age cannot be less than 18 years');
            document.getElementById('sDate').focus();
            return false;
        }
        /* else if(!isValidDate(theForm.dob.value,"Date of Birth")){
            document.getElementById('sDate').focus();
            return false;
        } */
        if (trim(theForm.nicExpiryDate.value) != '' && isDateSmaller(theForm.nicExpiryDate.value, currServerDate)) {
            alert('CNIC Expiry should not be less than current date.');
            document.getElementById('nicExpiryDate').focus();
            return false;
        } else if (!isValidDate(theForm.nicExpiryDate.value, "CNIC Expiry")) {
            document.getElementById('nicExpiryDate').focus();
            return false;
        }

        if (trim(theForm.cnicIssuanceDate.value) != '' && isDateGreater(theForm.cnicIssuanceDate.value, currServerDate)) {
            alert('CNIC Issuanace date should not be greater than current date.');
            document.getElementById('cnicIssuanceDate').focus();
            return false;
        } else if (trim(theForm.cnicIssuanceDate.value) != '' && !isValidDate(theForm.cnicIssuanceDate.value, "CNIC Issuance Date")) {
            document.getElementById('cnicIssuanceDate').focus();
            return false;
        }
        var cnicVal = theForm.nic.value;
        if (cnicVal != null) {
            if (trim(cnicVal) != '' && cnicVal.length == 15) {
                var firstHyphin = cnicVal.substring(5, 6);
                var secondHyphin = cnicVal.substring(13, 14);
                if (firstHyphin != '-' || secondHyphin != '-') {
                    alert('CNIC No is not in proper format. Format should be #####-#######-#');
                    document.getElementById('nic').focus();
                    return false;
                }
            }
        }


        var isValid = true;
        if (
            !validateComboWithOthers('presentAddDistrictId', 'presentDistOthers', 'Present Home District/Tehsil/Town')
            || !validateComboWithOthers('presentAddCityId', 'presentCityOthers', 'Present Home City')
            || !validateComboWithOthers('permanentAddDistrictId', 'permanentDistOthers', 'Permanent Home District/Tehsil/Town')
            || !validateComboWithOthers('permanentAddCityId', 'permanentAddCityOthers', 'Permanent Home City')
            || !validateComboWithOthers('fundsSourceId', 'fundSourceOthers', 'Source of Funds')
        ) {
            return false;
        }
        if (validateFormChar(theForm)) {
            return false;//Allow special characters to incorporate Urdu changes
        }
        /*if((!validateAlphaWithSp(theForm.name,"Account Title")) || (!validateAlphaWithSp(theForm.fatherHusbandName,"Father/Husband Name")) || (!validateAlphaWithSp(theForm.motherMaidenName,"Mother's Maiden Name")))
        {
              return false;
          }*/
        //submitting form
        if (confirm('Are you sure you want to proceed?') == true) {
            return true;
        } else {
            return false;
        }
    }

    function onFormSubmit(theForm) {
        if (!isFormDataChanged()) {
            return false;
        }

        if(!isAlphanumeric(document.forms[0].embossingName)){
            alert("Special characters are not allowed for Embossing Name.");
            return false;
        }

        // doRequired(theForm.dob, 'Date of Birth')
        doRequired(theForm.nic, 'CNIC #')
        IsNumeric(theForm.nic.value, 'CNIC #')
        isValidMinLength(theForm.nic, 'CNIC #', 13)
        doRequired(theForm.name, 'Account Title')
        doRequired(theForm.mobileNo, 'Mobile No.')
        isValidMinLength(theForm.mobileNo, 'Mobile No.', 11)
        isValidMobileNo(theForm.mobileNo)
        doRequired(theForm.presentAddress, 'Mailing Address')
        doRequired(theForm.embossingName, 'Embossing Name')



        if (submitForm(theForm)) {
            return true;
        } else {
            return false;
        }
    }

    function doRequired(field, label) {
        if (field.value.trim() == '' || field.value.length == 0) {
            alert(label + ' is required field.');
            field.focus();
            return false;
        }
        return true;
    }
    //
    // function isValidPicOrPdf(field, label) {
    //
    //     if (field.value.trim() != '' || field.value.length != 0) {
    //         if (!field.value.match(/\.(jpg|jpeg|png|gif)$/)) {
    //             alert(label + ' is not valid picture file format e.g. jpg, jpeg, png, gif are supported formats');
    //             return false;
    //         }
    //     }
    //     return true;
    // }

    // function doRequiredGroup(className, label) {
    //     selected = false;
    //     var elements = document.getElementsByClassName(className);
    //     for (i = 0; i < elements.length; i++) {
    //         if (elements[i].checked == false) {
    //             selected = false;
    //         } else {
    //             return true;
    //         }
    //     }
    //     if (selected == false) {
    //         alert(label + ' is required field.');
    //     }
    //     return selected;
    // }

    function isValidMobileNo(field) {
        if (field.type == "hidden") {
            return true;
        }
        if (field.value != '') {
            var mobileNumber = field.value;
            if (mobileNumber.charAt(0) != "0" || mobileNumber.charAt(1) != "3") {
                alert("Mobile Number should start from 03XXXXXXXXX");
                field.focus();
                return false;
            }
        }
        return true;
    }

    //This method is a copy of code from submitForm() method
    <%--function isValidFormForPrinting() {--%>
    <%--    var theForm = document.forms.mfsAccountForm;--%>
    <%--    if (doRequired(theForm.name, 'Name')--%>
    <%--        && doRequired(theForm.nic, 'CNIC #')--%>
    <%--        && doRequired(theForm.presentAddress, 'Mailing Address')--%>
    <%--        && doRequired(theForm.dob, 'Date of Birth')--%>
    <%--        && doRequired(theForm.presentAddCityId, 'City/District')--%>
    <%--        && doRequired(theForm.customerAccountTypeId, 'Account Type')--%>
    <%--        && doRequired(theForm.mobileNo, 'Mobile No')--%>
    <%--        && doRequired(theForm.accountNature, 'Nature of Account')--%>
    <%--        && doRequired(theForm.segmentId, 'Segment')--%>
    <%--        && doRequired(theForm.registrationStateId, 'Registration State')) {--%>

    <%--        var currServerDate = '<%=PortalDateUtils.currentFormattedDate("dd/MM/yyyy")%>';--%>
    <%--        if (trim(theForm.dob.value) != '' && isDateGreater(theForm.dob.value, currServerDate)) {--%>
    <%--            alert('Future date of birth is not allowed.');--%>
    <%--            document.getElementById('sDate').focus();--%>
    <%--            return false;--%>
    <%--        } else if (isAgeLessThan18Years(theForm.dob.value)) {--%>
    <%--            alert('Customer age cannot be less than 18 years');--%>
    <%--            document.getElementById('sDate').focus();--%>
    <%--            return false;--%>
    <%--        }--%>

    <%--        if (!isDobGreaterThan60Years(theForm.dob.value)) {--%>
    <%--            if (theForm.nicExpiryDate.value == null || trim(theForm.nicExpiryDate.value) == '') {--%>
    <%--                alert('CNIC Expiry is a required field.');--%>
    <%--                return false;--%>
    <%--            }--%>
    <%--        }--%>

    <%--        if (trim(theForm.nicExpiryDate.value) != '' && isDateSmaller(theForm.nicExpiryDate.value, currServerDate)) {--%>
    <%--            alert('CNIC Expiry should not be less than current date.');--%>
    <%--            document.getElementById('nicExpiryDate').focus();--%>
    <%--            return false;--%>
    <%--        }--%>
    <%--        var cnicVal = theForm.nic.value;--%>
    <%--        if (cnicVal != null) {--%>
    <%--            if (trim(cnicVal) != '' && cnicVal.length == 15) {--%>
    <%--                var firstHyphin = cnicVal.substring(5, 6);--%>
    <%--                var secondHyphin = cnicVal.substring(13, 14);--%>
    <%--                if (firstHyphin != '-' || secondHyphin != '-') {--%>
    <%--                    alert('CNIC No is not in proper format. Format should be #####-#######-#');--%>
    <%--                    document.getElementById('nic').focus();--%>
    <%--                    return false;--%>
    <%--                }--%>
    <%--            }--%>
    <%--        }--%>

    <%--        var isValid = true;--%>
    <%--        if (!validateFormChar(theForm)) {--%>
    <%--            return false;--%>
    <%--        }--%>
    <%--        return true;--%>
    <%--    }--%>
    <%--    return false;--%>
    <%--}--%>

    function acTypeInfo(sel) {
        if (sel != null && sel != '') {
            if (sel.value == 1) {
                var elems = document.getElementsByClassName('l1form-feilds');
                for (var i = 0; i < elems.length; i++) {
                    elems[i].style.display = 'none';
                }

                var elemsAsteric = document.getElementsByClassName('l0form-asteric-feilds');
                for (var i = 0; i < elemsAsteric.length; i++) {
                    elemsAsteric[i].style.display = '';
                }


            } else if (sel.value == 2) {
                var elems = document.getElementsByClassName('l1form-feilds');
                for (var i = 0; i < elems.length; i++) {
                    elems[i].style.display = '';
                }

                var elemsAsteric = document.getElementsByClassName('l0form-asteric-feilds');
                for (var i = 0; i < elemsAsteric.length; i++) {
                    elemsAsteric[i].style.display = 'none';
                }
            } else if (sel.value == 53) {
                var elems = document.getElementsByClassName('l1form-feilds');
                for (var i = 0; i < elems.length; i++) {
                    elems[i].style.display = '';
                }

                var elemsAsteric = document.getElementsByClassName('l0form-asteric-feilds');
                for (var i = 0; i < elemsAsteric.length; i++) {
                    elemsAsteric[i].style.display = 'none';
                }
            }
            if (sel.value == 53) {
                var elems = document.getElementsByClassName('blinklform-feilds');
                for (var i = 0; i < elems.length; i++) {
                    elems[i].style.display = '';
                }

                var elemsAsteric = document.getElementsByClassName('l0form-asteric-feilds');
                for (var i = 0; i < elemsAsteric.length; i++) {
                    elemsAsteric[i].style.display = 'none';
                }
            } else {
                var elems = document.getElementsByClassName('blinklform-feilds');
                for (var i = 0; i < elems.length; i++) {
                    elems[i].style.display = 'none';
                }
            }
        }

    }

    function regStateChange(sel) {
        if (sel != null && sel != '') {
            if (sel.value == 4) {
                var elems = document.getElementsByClassName('text discrepant');
                for (var i = 0; i < elems.length; i++) {
                    elems[i].style.display = '';
                }
            } else {
                var elems = document.getElementsByClassName('text discrepant');
                for (var i = 0; i < elems.length; i++) {
                    elems[i].style.display = 'none';
                }
                document.getElementById('customerPicDiscrepant1').checked = '';
                document.getElementById('tncPicDiscrepant1').checked = '';
                document.getElementById('signPicDiscrepant1').checked = '';
                document.getElementById('cnicFrontPicDiscrepant1').checked = '';
                document.getElementById('cnicBackPicDiscrepant1').checked = '';
                document.getElementById('level1FormPicDiscrepant1').checked = '';
            }

        }


    }

    function markNonDiscrepent(sel) {
        if (sel != null && sel != '') {
            document.getElementById(sel.id + 'Discrepant1').checked = '';
            document.getElementById(sel.id + 'Discrepant2').value = "false";
        }
    }

    function defaultValues() {

        var theForm = document.forms.mfsAccountForm;
        document.getElementById('customerPicDiscrepant2').value = document.getElementById('customerPicDiscrepant1').value;
        document.getElementById('tncPicDiscrepant2').value = document.getElementById('tncPicDiscrepant1').value;
        document.getElementById('signPicDiscrepant2').value = document.getElementById('signPicDiscrepant1').value;
        document.getElementById('cnicFrontPicDiscrepant2').value = document.getElementById('cnicFrontPicDiscrepant1').value;
        document.getElementById('cnicBackPicDiscrepant2').value = document.getElementById('cnicBackPicDiscrepant1').value;

        if (theForm.customerAccountTypeId.value == 2) {
            document.getElementById('level1FormPicDiscrepant2').value = document.getElementById('level1FormPicDiscrepant1').value;
        }

    }

    function checkDecimal(field, label) {
        if (field.value.match(decimal)) {
            return true;
        } else {
            alert('Please enter valid ' + label);
            return false;
        }
    }

    function validateDecimal(field, label) {
        var RE = /^\d*\.?\d*$/;
        if (!RE.test(field.value)) {
            alert('Please enter a valid ' + label);
            return flase;
        }
        return true;
    }

    // function isWithinLimit(field, label, limit) {
    //     if (field.value != '' && field.value > limit) {
    //         alert(label + ' cannot be greater than ' + limit);
    //         return false;
    //     }
    //     return true;
    // }

    function isValidMinLength(field, label, minlength) {
        if (field.value != '' && field.value.length < minlength) {
            alert(label + ' cannot be less than ' + minlength + ' digits');
            return false;
        }
        return true;
    }




    // jq("#fundsSource").multiselect({
    //     noneSelectedText: "[Select]",
    //     minWidth: "210px"
    // });
    // window.onload = acTypeInfo(document.forms.mfsAccountForm.customerAccountTypeId);
    // window.onload = regStateChange(document.forms.mfsAccountForm.registrationStateId);
</script>
</body>

</html>
