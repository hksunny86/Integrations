<%--
  Created by IntelliJ IDEA.
  User: Muhammad Aqeel
  Date: 1/12/2022
  Time: 5:37 PM
  To change this template use File | Settings | File Templates.
--%>

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
        <c:when test="${not empty param.appUserId or not empty mfsAccountModel.appUserId}">
            <meta name="title"
                  content="${pageMfsId}: ${mfsAccountModel.name}"/>
        </c:when>
        <c:otherwise>
            <meta name="title" content="Update Account To Blink "/>
        </c:otherwise>
    </c:choose>
    <%@include file="/common/ajax.jsp" %>
    <script language="javascript" type="text/javascript">
        var jq = $.noConflict();

        jq(function ($) {
            $("#dob").mask("99/99/9999", {placeholder: "dd/mm/yyyy"});
            ``
            $("#cnicIssuanceDate").mask("99/99/9999", {placeholder: "dd/mm/yyyy"});

        });

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

        function getFileSize(file) {

            if (file != null && file.files != null && file.files[0] != null) {

                return ((file.files[0].size / 1024) / 1024);
            }

            return 0;
        }

        function cback(checkbox) {
            var checkboxes = document.querySelectorAll(".cnicBackDes")
            checkboxes.forEach((item) => {
                if (item !== checkbox) item.checked = false
            })
        }
        function sig(checkbox) {
            var checkboxes = document.querySelectorAll(".signDes")
            checkboxes.forEach((item) => {
                if (item !== checkbox) item.checked = false
            })
        }
        function placeofbirth(checkbox) {
            var checkboxes = document.querySelectorAll(".pleaseofbirthDes")
            checkboxes.forEach((item) => {
                if (item !== checkbox) item.checked = false
            })
        }
        function cfront(checkbox) {
            var checkboxes = document.querySelectorAll(".cnicFrontDes")
            checkboxes.forEach((item) => {
                if (item !== checkbox) item.checked = false
            })
        }

        function cpic(checkbox){
            var checkboxes = document.querySelectorAll(".customerDes")
            checkboxes.forEach((item) =>{
                if(item !== checkbox) item.checked = false
            })
        }

        function cname(checkbox){
            var checkboxes = document.querySelectorAll(".cnameDes")
            checkboxes.forEach((item) =>{
                if(item !== checkbox) item.checked = false
            })
        }

        /* function cnic(checkbox) {
             var checkboxes = document.querySelectorAll(".cnicDes")
             checkboxes.forEach((item) => {
                 if (item !== checkbox) item.checked = false
             })
         }*/

        function pop(checkbox) {
            var checkboxes = document.querySelectorAll(".popDes")
            checkboxes.forEach((item) => {
                if (item !== checkbox) item.checked = false
            })
        }

        function fh(checkbox) {
            var checkboxes = document.querySelectorAll(".fhDes")
            checkboxes.forEach((item) => {
                if (item !== checkbox) item.checked = false
            })
        }

        function mailing(checkbox) {

            var checkboxes = document.querySelectorAll(".mailingDes")
            checkboxes.forEach((item) => {
                if (item !== checkbox) item.checked = false
            })
        }

        function emaildes(checkbox) {

            var checkboxes = document.querySelectorAll(".emailDes")
            checkboxes.forEach((item) => {
                if (item !== checkbox) item.checked = false
            })
        }
        function motherNamedes(checkbox) {

            var checkboxes = document.querySelectorAll(".motherNameDes")
            checkboxes.forEach((item) => {
                if (item !== checkbox) item.checked = false
            })
        }

        function source(checkbox) {
            var checkboxes = document.querySelectorAll(".sourceDes")
            checkboxes.forEach((item) => {
                if (item !== checkbox) item.checked = false
            })
        }
        function sIncome(checkbox) {
            var checkboxes = document.querySelectorAll(".incomeDes")
            checkboxes.forEach((item) => {
                if (item !== checkbox) item.checked = false
            })
        }

        /*function turnover(checkbox) {
            var checkboxes = document.querySelectorAll(".turnOverDes")
            checkboxes.forEach((item) => {
                if (item !== checkbox) item.checked = false
            })

        }*/

        function mob(checkbox) {
            var checkboxes = document.querySelectorAll(".mobDes")
            checkboxes.forEach((item) => {
                if (item !== checkbox) item.checked = false
            })
        }


        /*  function poa(checkbox) {
              var checkboxes = document.querySelectorAll(".poaDes")
              checkboxes.forEach((item) => {
                  if (item !== checkbox) item.checked = false
              })
          }*/

         function pa(checkbox) {
             var checkboxes = document.querySelectorAll(".paddressDes")
             checkboxes.forEach((item) => {
                 if (item !== checkbox) item.checked = false
             })
         }

        setTimeout(function () {
            document.getElementById("successMessages").innerHTML = "";
        }, 7000);
    </script>

    <%

        String updatePermission = PortalConstants.UPD_ACC_BLINK_UPDATE;
//        updatePermission += "," + PortalConstants.PG_GP_UPDATE;
//        updatePermission += "," + PortalConstants.MNG_BB_CUST_UPDATE;
//        updatePermission += "," + PortalConstants.UPD_ACC_BLINK_UPDATE;

    %>
</head>

<body bgcolor="#ffffff">

<div id="successMsg" class="infoMsg" style="display: none;"></div>
<spring:bind path="mfsAccountModel.*">
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


<html:form name="updateAccountMerchantForm"
           commandName="mfsAccountModel"
           enctype="multipart/form-data" method="POST"
           action="p_updateaccounttomerchantdetail.html">
    <c:if test="${not empty param.appUserId or not empty mfsAccountModel.appUserId}">
        <input type="hidden" name="isUpdate" id="isUpdate" value="true"/>
        <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
               value="<%=PortalConstants.ACTION_UPDATE%>"/>
    </c:if>
    <c:if test="${empty param.appUserId && empty mfsAccountModel.appUserId}">
        <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
               value="<%=PortalConstants.ACTION_CREATE%>"/>
    </c:if>
    <input type="hidden" id= "printCheck" name="printCheck" value="0"/>

    <c:if test="${not empty param.appUserId }"><input type="hidden" name="appUserId" value="${appUserId}"/></c:if>
    <c:if test="${not empty param.appUserId}"><input type="hidden" name="appUserId" value="${appUserId}"/></c:if>

    <input type="hidden" name="actionAuthorizationId" value="${param.authId}"/>
    <input type="hidden" name="resubmitRequest" value="${param.isReSubmit}"/>

    <html:hidden path="accounttypeName"/>
    <html:hidden path="segmentNameStr"/>
    <html:hidden path="fundSourceStr"/>
    <html:hidden path="regStateName"/>
    <html:hidden path="taxRegimeName"/>

    <html:hidden path="<%=PortalConstants.KEY_USECASE_ID %>"/>
    <table width="950px">
        <tr>
            <td colspan="2" align="left" bgcolor="FBFBFB">
                <h2>
                    Applicant Information
                </h2>
            </td>
        </tr>
        <tr>
            <td align="right" class="formText" bgcolor="F3F3F3"><span style="color: #FF0000">*</span>Mobile No:</td>
            <td align="left"><b>
                <html:input path="mobileNo" tabindex="3" style="background: #D3D3D3;"  cssClass="textBox" maxlength="100" id="mobileNo"
                            readonly="true"/></b>
            </td>

            <td height="16" align="right" bgcolor="F3F3F3" class="formText"
                width="25%">
                <span style="color: #FF0000">*</span>Account Title:
            </td>
            <td align="left">
                <html:input path="name" tabindex="4" cssClass="textBox" maxlength="100" id="titlename"
                            onkeypress="return maskAlphaWithSp(this,event)"/><br/>

            </td>
        </tr>

        <tr>
            <td height="16" align="right" bgcolor="F3F3F3" class="formText"
                width="20%">
                <span style="color: #FF0000">*</span>Customer Picture:
            </td>
            <td bgcolor="FBFBFB" class="text" width="20%">
                <c:if test="${mfsAccountModel.registrationStateId ne 1}">
                    <c:choose>
                        <c:when test="${not empty appUserId}">
                            <img id="customerPicByte"
                                 src="${contextPath}/images/upload_dir/customerPic_${appUserId}.${mfsAccountModel.customerPicExt}?time=<%=System.currentTimeMillis()%>"
                                 width="100" height="100"/><br>
                            Approved<html:checkbox title="Approved" path="customerPicApp" id="customerapp"
                                                   class="customerDes" onclick="cpic(this)"/>
                            Rejected<html:checkbox title="Rejected" path="customerPicRej" id="customerrej"
                                                   class="customerDes" onclick="cpic(this)"/>
                        </c:when>
                        <c:when test="${isReSubmit}">
                            <img id="customerPicByte"
                                 src="${contextPath}/images/upload_dir/authorization/customerPic_${param.authId}.${mfsAccountModel.customerPicExt}?time=<%=System.currentTimeMillis()%>"
                                 width="100" height="100"/>
                        </c:when>
                    </c:choose>
                    
                </c:if>
            </td>
            <td height="16" align="right" bgcolor="F3F3F3" class="formText"
                width="25%">
                <span class="l0form-asteric-feilds" style="color: #FF0000">*</span>CNIC Front:
            </td>
            <td bgcolor="FBFBFB" class="text" width="25%">
                <c:if test="${mfsAccountModel.registrationStateId ne 1}">
                    <c:choose>
                        <c:when test="${not empty appUserId}">
                            <img src="${contextPath}/images/upload_dir/cnicFrontPic_${mfsAccountModel.appUserId}.${mfsAccountModel.cnicFrontPicExt}?time=<%=System.currentTimeMillis()%>"
                                 width="100" height="100"/><br>

                            Approved<html:checkbox title="Approved" path="cnicFrontApp" id="cnicfrontappo"
                                                   class="cnicFrontDes" onclick="cfront(this)"/>
                            Rejected<html:checkbox title="Rejected" path="cnicFrontRej" id="cnicfrontreje"
                                                   class="cnicFrontDes" onclick="cfront(this)"/>
                        </c:when>
                        <c:when test="${param.isReSubmit}">
                            <img src="${contextPath}/images/upload_dir/authorization/cnicFrontPic_${param.authId}.${mfsAccountModel.cnicFrontPicExt}?time=<%=System.currentTimeMillis()%>"
                                 width="100" height="100"/><br>

                            Approved<html:checkbox title="Approved" path="cnicFrontApp" id="cnicfrontresubmitapp"
                                                   class="cnicFrontDes" onclick="cfront(this)"/>
                            Rejected<html:checkbox title="Rejected" path="cnicFrontRej" id="cnicfrontresubmitrej"
                                                   class="cnicFrontDes" onclick="cfront(this)"/>

                        </c:when>
                    </c:choose>
                </c:if>
            </td>
        </tr>


        <tr>
            <td height="16" align="right" bgcolor="F3F3F3" class="formText"
                width="25%">
                CNIC Back:
            </td>
            <td bgcolor="FBFBFB" class="text" width="25%">
                <c:if test="${mfsAccountModel.registrationStateId ne 1}">
                    <c:choose>
                        <c:when test="${not empty appUserId}">
                            <img src="${contextPath}/images/upload_dir/cnicBackPic_${mfsAccountModel.appUserId}.${mfsAccountModel.cnicBackPicExt}?time=<%=System.currentTimeMillis()%>"
                                 width="100" height="100"/><br>
                            Approved<html:checkbox title="Approved" path="cnicBackApp" id="cnicBackapp"
                                                   class="cnicBackDes" onclick="cback(this)"/>
                            Rejected<html:checkbox title="Rejected" path="cnicBackRej" id="cnicBackrej"
                                                   class="cnicBackDes" onclick="cback(this)"/>
                        </c:when>
                        <c:when test="${param.isReSubmit}">
                            <img src="${contextPath}/images/upload_dir/authorization/cnicBackPic_${param.authId}.${mfsAccountModel.cnicBackPicExt}?time=<%=System.currentTimeMillis()%>"
                                 width="100" height="100"/><br>

                            Approved<html:checkbox title="Approved" path="cnicBackApp" id="cnicBacksubmitapp"
                                                   class="cnicBackDes" onclick="cback(this)"/>
                            Rejected<html:checkbox title="Rejected" path="cnicBackRej" id="cnicBacksubmitrej"
                                                   class="cnicBackDes" onclick="cback(this)"/>

                        </c:when>
                    </c:choose>
                </c:if>
            </td>
        </tr>
        <tr>
            <td height="16" align="right" bgcolor="F3F3F3" class="formText">
                <span style="color: #FF0000">*</span>CNIC #:
            </td>
            <td bgcolor="FBFBFB">
                <c:choose>
                    <c:when test="${not empty param.appUserId or not empty mfsAccountModel.appUserId}">
                        <html:input path="nic" cssClass="textBox" style="background: #D3D3D3;" readonly="true"
                                    tabindex="11" maxlength="13" onkeypress="return maskNumber(this,event)"/>
                    </c:when>
                    <c:otherwise>
                        <html:input path="nic" cssClass="textBox" tabindex="11" maxlength="13"
                                    onkeypress="return maskNumber(this,event)"/>
                    </c:otherwise>
                </c:choose>
            </td>

            <td height="16" align="right" bgcolor="F3F3F3" class="formText"
                width="25%">
                <span style="color: #FF0000">*</span>Business Name:
            <td align="left" bgcolor="FBFBFB" width="25%">
                <html:input path="businessName" id="businessName" cssClass="textBox" tabindex="21" maxlength="100" style="background: #D3D3D3;" readonly="true"/><br>
            Approved<html:checkbox title="Approved" path="nameApp" id="nameapp" class="cnameDes"
                                   onclick="cname(this)"/>
            Rejected<html:checkbox title="Rejected" path="nameRej" id="namerej" class="cnameDes"
                                   onclick="cname(this)"/>
            </td>
        </tr>
        
        <tr>
            <td height="16" align="right" bgcolor="F3F3F3" class="formText"
                width="25%">
                <span style="color: #FF0000">*</span>Latitude:
            <td align="left" bgcolor="FBFBFB" class="formText" width="25%">
                <html:input path="latitude" id="latitude" cssClass="textBox" tabindex="21" maxlength="100" style="background: #D3D3D3;"
                            readonly="true"/>
            </td>
            <td align="right" class="formText" bgcolor="FBFBFB"><span style="color: #FF0000">*</span>Longitude:</td>
            <td align="left">
                <html:input path="longitude" id="longitude" cssClass="textBox" tabindex="21" style="background: #D3D3D3;" maxlength="100"
                            readonly="true"/>
            </td>

        </tr>

        <tr>
            <td height="16" align="right" bgcolor="F3F3F3" class="formText"
                width="25%">
                <span style="color: #FF0000">*</span>Business Address:
            <td align="left" bgcolor="FBFBFB" width="25%">
                <html:input path="address1" id="address1" cssClass="textBox" tabindex="21" maxlength="100" style="background: #D3D3D3;" readonly="true"/><br>
                   Approved<html:checkbox title="Approved" path="pAddressApp" id="permanentaddressapp" class="paddressDes"
                                                 onclick="pa(this)"/>
                          Rejected<html:checkbox title="Rejected" path="pAddressRej" id="permanentaddressrej" class="paddressDes"
                                                 onclick="pa(this)"/>
            </td>
            <td align="right" class="formText" bgcolor="FBFBFB"><span style="color: #FF0000">*</span>Type Of Business:
            </td>
            <td align="left">
                <html:input path="accountPurposeName" id="accountPurposeName" cssClass="textBox" style="background: #D3D3D3;" tabindex="21"
                            readonly="true"
                            maxlength="100"/>
            </td>

        </tr>

       
        
        <tr>
            <td height="16" align="right" bgcolor="F3F3F3" class="formText">
                Expected Monthly TurnOver:
            </td>
            <td bgcolor="FBFBFB">
                <html:input path="expectedMonthlyTurnOver" maxlength="50" cssClass="textBox" style="background: #D3D3D3;" tabindex="24"
                            readonly="true"/>
            </td>
        </tr>
        
        <tr>
            <td height="16" align="right" bgcolor="F3F3F3" class="formText">
                City:
            </td>
            <td align="left" bgcolor="FBFBFB">
                <html:input path="city" cssClass="textBox" tabindex="15" maxlength="50" style="background: #D3D3D3;" readonly="true" />

            </td>


            <td height="16" align="right" bgcolor="F3F3F3" class="formText"
                width="25%">
                Comments:
            </td>
            <td align="left" bgcolor="FBFBFB" class="formText" width="25%">
                <html:textarea style="height:50px;" path="comments" tabindex="41" cssClass="textBox"
                               maxlength="250"/>
            </td>

        </tr>
        
        <c:if test="${not empty param.appUserId or not empty mfsAccountModel.appUserId}">
            <tr>
                <td align="right" bgcolor="F3F3F3" class="formText">
                    <span style="color: #FF0000">*</span>Merchant Customer State:
                </td>
                <td align="left" bgcolor="FBFBFB" class="formText">
                    <html:select path="registrationStateId" cssClass="textBox"
                                 id="registrationStateId"
                                 tabindex="18" onchange="regStateChange(this);">
                        <html:option value="">[Select]</html:option>
                        <c:if test="${regStateList != null}">
                            <html:options items="${regStateList}" itemLabel="name"
                                          itemValue="registrationStateId"/>
                        </c:if>
                    </html:select>

                </td>

                <td height="16" align="right" bgcolor="F3F3F3" class="formText"
                    width="25%">
                    Registration Comments:
                </td>
                <td align="left" bgcolor="FBFBFB" class="formText" width="25%">
                    <html:textarea style="height:50px;" path="regStateComments" tabindex="41" cssClass="textBox"
                                   maxlength="250"/>
                </td>

            </tr>
        </c:if>
        

    </table>
    <div style="clear:both"></div>
    </div>
    <table>
        <tr>
            <td width="100%" align="left" bgcolor="FBFBFB" colspan="4">
                <authz:authorize ifAnyGranted="<%=updatePermission%>">
                    <input type="submit" class="button" value="Update" onclick="return checkFormSubmit(this)" id="mainButton"
                           tabindex="42"/>&nbsp;
                </authz:authorize>
                <input type="button" class="button" value="Cancel" tabindex="43"
                       onclick="javascript: window.location='p_updateaccounttoMerchantinfo.html?actionId=<%=PortalConstants.ACTION_RETRIEVE%>'"/>
            </td>
        </tr>
    </table>
</html:form>
<ajax:updateField parser="new ResponseXmlParser()" source="taxRegimeId" eventType="change" action="taxRegimeId"
                  target="fed" baseUrl="${contextPath}/p_updateaccounttoMerchantinfo.html"
                  parameters="taxRegimeId={taxRegimeId}"></ajax:updateField>
<form action="${contextPath}/p_updateaccounttoMerchantinfo.html" method="post"
      name="userInfoListViewForm" id="userInfoListViewModel">
    <input type="hidden"
           value="<c:out value="${mfsAccountModel.searchMfsId}"/>"
           id="userId" name="userId"/>
    <input type="hidden"
           value="<c:out value="${mfsAccountModel.searchFirstName}"/>"
           id="name" name="firstName"/>
    <input type="hidden"
           value="<c:out value="${mfsAccountModel.searchLastName}"/>"
           id="lastName" name="lastName"/>
    <input type="hidden"
           value="<c:out value="${mfsAccountModel.searchNic}"/>" id="nic"
           name="nic"/>

    <input type="hidden"
           value="<c:out value="${mfsAccountModel.customerAccountTypeId}"/>" id="customerAccountTypeId"
           name="customerAccountTypeId"/>
</form>
<script language="javascript" type="text/javascript">

    setTimeout(function () {
        document.getElementById("successMessages").style.display = "none";
        document.getElementById("successMsg").style.display = "none";
    }, 7000);


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
    $(function(){

    })
    function valueChanged()
    {
        if(document.getElementById("coupon_question").checked == false){
            document.getElementById("pdfButton").hidden = true;
            document.getElementById("mainButton").hidden = false;
            document.getElementById("printCheck").value='0';
        }
        if(document.getElementById("coupon_question").checked == true){
            document.getElementById("pdfButton").hidden = false;
            document.getElementById("mainButton").hidden = true;
            document.getElementById("printCheck").value='1';
        }
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


    function onCancel(a, b) {
        document.forms[0].reset();
    }

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
        if (${not empty param.appUserId && not empty mfsAccountModel.appUserId}) {
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
        if (!validateFormChar(theForm)) {
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
            alert("Are you sure you want to proceed? error");
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

    function isValidPicOrPdf(field, label) {

        if (field.value.trim() != '' || field.value.length != 0) {
            if (!field.value.match(/\.(jpg|jpeg|png|gif)$/)) {
                alert(label + ' is not valid picture file format e.g. jpg, jpeg, png, gif are supported formats');
                return false;
            }
        }
        return true;
    }

    function doRequiredGroup(className, label) {
        selected = false;
        var elements = document.getElementsByClassName(className);
        for (i = 0; i < elements.length; i++) {
            if (elements[i].checked == false) {
                selected = false;
            } else {
                return true;
            }
        }
        if (selected == false) {
            alert(label + ' is required field.');
        }
        return selected;
    }

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

    function doRequired(field, label) {
        if (field.value.trim() == '' || field.value.length == 0) {
            alert(label + ' is required field.');
            field.focus();
            return false;
        }
        return true;
    }

    //This method is a copy of code from submitForm() method
    function isValidFormForPrinting() {
        var theForm = document.forms.mfsAccountForm;
        if (doRequired(theForm.name, 'Name')
            && doRequired(theForm.nic, 'CNIC #')
            && doRequired(theForm.presentAddress, 'Mailing Address')
            && doRequired(theForm.dob, 'Date of Birth')
            && doRequired(theForm.customerAccountTypeId, 'Account Type')
            && doRequired(theForm.mobileNo, 'Mobile No')
            && doRequired(theForm.accountNature, 'Nature of Account')
            && doRequired(theForm.segmentId, 'Segment')
            && doRequired(theForm.registrationStateId, 'Registration State')) {

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

            if (!isDobGreaterThan60Years(theForm.dob.value)) {
                if (theForm.nicExpiryDate.value == null || trim(theForm.nicExpiryDate.value) == '') {
                    alert('CNIC Expiry is a required field.');
                    return false;
                }
            }

            if (trim(theForm.nicExpiryDate.value) != '' && isDateSmaller(theForm.nicExpiryDate.value, currServerDate)) {
                alert('CNIC Expiry should not be less than current date.');
                document.getElementById('nicExpiryDate').focus();
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
            if (!validateFormChar(theForm)) {
                return false;
            }
            return true;
        }
        return false;
    }

    /* function defaultValues() {

         var theForm = document.forms.mfsAccountForm;
         document.getElementById('customerPicDiscrepant2').value = document.getElementById('customerPicDiscrepant1').value;
         document.getElementById('tncPicDiscrepant2').value = document.getElementById('tncPicDiscrepant1').value;
         document.getElementById('signPicDiscrepant2').value = document.getElementById('signPicDiscrepant1').value;
         document.getElementById('cnicFrontPicDiscrepant2').value = document.getElementById('cnicFrontPicDiscrepant1').value;
         document.getElementById('cnicBackPicDiscrepant2').value = document.getElementById('cnicBackPicDiscrepant1').value;

         if (theForm.customerAccountTypeId.value == 2) {
             document.getElementById('level1FormPicDiscrepant2').value = document.getElementById('level1FormPicDiscrepant1').value;
         }

     }*/

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
            } else if (sel.value == 2) {
                var elems = document.getElementsByClassName('l1form-feilds');
                for (var i = 0; i < elems.length; i++) {
                    elems[i].style.display = '';
                }

                var elemsAsteric = document.getElementsByClassName('l0form-asteric-feilds');
                for (var i = 0; i < elemsAsteric.length; i++) {
                    elemsAsteric[i].style.display = 'none';
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

    function isWithinLimit(field, label, limit) {
        if (field.value != '' && field.value > limit) {
            alert(label + ' cannot be greater than ' + limit);
            return false;
        }
        return true;
    }

    function isValidMinLength(field, label, minlength) {
        if (field.value != '' && field.value.length < minlength) {
            alert(label + ' cannot be less than ' + minlength + ' digits');
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

    function isValidEmail(field, label) {
        if (field.value != '' && field.value.length != 0) {
            if (!isEmail(field)) {
                alert(label + ' is not valid Email.');
                return false;
            }
        }
        return true;
    }

    // Calendar.setup(
    //     {
    //         inputField: "dob", // id of the input field
    //         ifFormat: "%d/%m/%Y",      // the date format
    //         button: "sDate",    // id of the button
    //         showsTime: false
    //     }
    // );

    jq("#fundsSource").multiselect({
        noneSelectedText: "[Select]",
        minWidth: "210px"
    });
    window.onload = acTypeInfo(document.forms.mfsAccountForm.customerAccountTypeId);
    window.onload = regStateChange(document.forms.mfsAccountForm.registrationStateId);
</script>
</body>
</html>