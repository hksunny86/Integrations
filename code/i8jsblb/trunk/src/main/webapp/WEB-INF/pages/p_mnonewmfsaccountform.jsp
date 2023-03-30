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
        <c:when test="${not empty param.appUserId or not empty mfsAccountModel.appUserId}">
            <meta name="title"
                  content="${pageMfsId}: ${mfsAccountModel.firstName} ${mfsAccountModel.lastName}"/>
        </c:when>
        <c:otherwise>
            <meta name="title" content="Branchless Banking Account Opening Form"/>
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

        function openPrinterWindow() {
            var appUserId = '<%= request.getParameter("appUserId") %>';
            var location = '${pageContext.request.contextPath}/p_mnonewmfsaccountform_printer.html?appUserId=' + appUserId;
            window.open(location, 'printWindow');

        }

        function openPrintWindow() {
            if (isValidFormForPrinting()) {
                var location = '${pageContext.request.contextPath}/p_mnonewmfsaccountform_print.html';
                window.open(location, 'printWindow');
            }
        }

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
<html:form name="mfsAccountForm" commandName="mfsAccountModel"
           enctype="multipart/form-data" method="POST"
           onsubmit="return onFormSubmit(this)"
           action="p_mnonewmfsaccountform.html">

    <c:if test="${not empty param.appUserId or not empty mfsAccountModel.appUserId}">
        <input type="hidden" name="isUpdate" id="isUpdate" value="true"/>
        <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
               value="<%=PortalConstants.ACTION_UPDATE%>"/>
    </c:if>
    <c:if test="${empty param.appUserId && empty mfsAccountModel.appUserId}">
        <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
               value="<%=PortalConstants.ACTION_CREATE%>"/>
    </c:if>

    <c:if test="${not empty param.appUserId }"><input type="hidden" name="appUserId" value="${param.appUserId}"/></c:if>
    <c:if test="${not empty appUserId}"><input type="hidden" name="appUserId" value="${appUserId}"/></c:if>

    <input type="hidden" name="actionAuthorizationId" value="${param.authId}"/>
    <input type="hidden" name="resubmitRequest" value="${param.isReSubmit}"/>

    <html:hidden path="accounttypeName"/>
    <html:hidden path="segmentNameStr"/>
    <html:hidden path="fundSourceStr"/>
    <html:hidden path="regStateName"/>
    <html:hidden path="permanentHomeAddCityName"/>
    <html:hidden path="taxRegimeName"/>

    <html:hidden path="<%=PortalConstants.KEY_USECASE_ID %>"/>
    <div class="infoMsg" id="errorMessages" style="display:none;"></div>
    <div id="table_form">
        <table>
            <tr>
                <td height="16" align="right" bgcolor="F3F3F3" class="formText"
                    width="25%">
                    <span style="color: #FF0000">*</span>Account Type:
                </td>
                <td align="left" bgcolor="FBFBFB" class="formText" width="25%">
                    <c:choose>
                        <c:when test="${not empty param.appUserId or not empty mfsAccountModel.appUserId}">
                            <div class="textBox" style="background: #D3D3D3; font-size:14px; ">
                                <c:if test="${mfsAccountModel.customerAccountTypeId==1}">Level 0</c:if>
                                <c:if test="${mfsAccountModel.customerAccountTypeId==2}">Level 1</c:if>
                                <c:if test="${mfsAccountModel.customerAccountTypeId==4}">HRA</c:if>
                                <c:if test="${mfsAccountModel.customerAccountTypeId==53}">BLINK</c:if>
                            </div>
                            <html:select style="display:none;" path="customerAccountTypeId" cssClass="textBox"
                                         id="customerAccountTypeId"
                                         tabindex="1" onchange="acTypeInfo(this);">
                                <html:option value="">[Select]</html:option>
                                <c:if test="${customerAccountTypeListForEdit != null}">
                                    <html:options items="${customerAccountTypeListForEdit}" itemLabel="name"
                                                  itemValue="customerAccountTypeId"/>
                                </c:if>
                            </html:select>
                        </c:when>
                        <c:otherwise>
                            <html:select path="customerAccountTypeId" cssClass="textBox" id="customerAccountTypeId"
                                         tabindex="1" onchange="acTypeInfo(this);">
                                <html:option value="">[Select]</html:option>
                                <c:if test="${customerAccountTypeList != null}">
                                    <html:options items="${customerAccountTypeList}" itemLabel="name"
                                                  itemValue="customerAccountTypeId"/>
                                </c:if>
                            </html:select>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
            <tr>
                <td height="16" align="right" bgcolor="F3F3F3" class="formText">
                    <span style="color: #FF0000">*</span>Mobile No:
                </td>
                <td bgcolor="FBFBFB" class="formText">
                    <c:choose>
                        <c:when test="${not empty param.appUserId or not empty mfsAccountModel.appUserId}">
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
                <c:if test="${not empty param.appUserId or not empty mfsAccountModel.appUserId}">
                    <td height="16" align="right" bgcolor="F3F3F3" class="formText">
                        Account Opening Date:
                    </td>
                    <td bgcolor="FBFBFB" class="formText">
                        <html:input path="createdOn" id="createdOn" readonly="true" style="background: #D3D3D3;"
                                    tabindex="3" cssClass="textBox"/>
                    </td>
                </c:if>
            </tr>
            <tr>
                <td colspan="2" align="left" bgcolor="FBFBFB">
                    <h2>
                        Applicant Information
                    </h2>
                </td>
            </tr>
            <tr>
                <td height="16" align="right" bgcolor="F3F3F3" class="formText"
                    width="25%">
                    <span style="color: #FF0000">*</span>Account Title:
                </td>
                <td align="left" bgcolor="FBFBFB" class="formText" width="25%">
                    <html:input path="name" tabindex="4" cssClass="textBox" maxlength="100"
                                onkeypress="return maskAlphaWithSp(this,event)"/>
                </td>
            </tr>
            <tr>
                <td height="16" align="right" bgcolor="F3F3F3" class="formText"
                    width="20%">
                    <span class="l0form-asteric-feilds" style="color: #FF0000">*</span>Customer Picture:
                </td>
                <td bgcolor="FBFBFB" class="text" width="20%">
                    <spring:bind path="mfsAccountModel.customerPic">
                        <c:choose>
                            <c:when test="${not empty param.appUserId}">
                                <input type="file" tabindex="5" disabled="true" id="customerPic"
                                       onchange="markNonDiscrepent(this);" name="customerPic" class="upload"/>
                            </c:when>
                            <c:otherwise>
                                <input type="file" tabindex="5" id="customerPic" onchange="markNonDiscrepent(this);"
                                       name="customerPic" class="upload"/>
                            </c:otherwise>
                        </c:choose>
                    </spring:bind>
                    <c:if test="${mfsAccountModel.registrationStateId ne 1}">
                        <c:choose>
                            <c:when test="${not empty param.appUserId}">
                                <img id="customerPicByte"
                                     src="${contextPath}/images/upload_dir/customerPic_${mfsAccountModel.appUserId}.${mfsAccountModel.customerPicExt}?time=<%=System.currentTimeMillis()%>"
                                     width="100" height="100"/>
                            </c:when>
                            <c:when test="${param.isReSubmit}">
                                <img id="customerPicByte"
                                     src="${contextPath}/images/upload_dir/authorization/customerPic_${param.authId}.${mfsAccountModel.customerPicExt}?time=<%=System.currentTimeMillis()%>"
                                     width="100" height="100"/>
                            </c:when>
                        </c:choose>
                        <div style="display: none;" bgcolor="FBFBFB" class="text discrepant" width="10%">
                            Discrepant
                            <c:choose>
                                <c:when test="${appUserTypeId == 3}">
                                    <html:checkbox title="Discrepant" disabled="true" readonly="readonly"
                                                   path="customerPicDiscrepant"/>
                                    <html:hidden id="customerPicDiscrepant2" path="customerPicDiscrepant"/>
                                </c:when>
                                <c:otherwise>
                                    <html:checkbox path="customerPicDiscrepant"/>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </c:if>
                    <c:if test="${mfsAccountModel.segmentId == 10319}">
                    <div align="left" bgcolor="F3F3F3" class="formText">
                    Maker Comments:
                     </div>
                    <div>
                    <html:textarea style="height:15px;" path="custPicMakerComments" tabindex="41" cssClass="textBox"
                                   maxlength="250"/>
                    </div>
                    <div align="left" bgcolor="F3F3F3" class="formText">
                       Checker Comments:
                    </div>
                    <div>
                        <html:textarea style="height:15px; background: #D3D3D3;"  readonly="true" path="custPicCheckerComments" tabindex="41" cssClass="textBox"
                                       maxlength="250"/>
                    </div>
                    </c:if>
                </td>
                <c:choose>
                <c:when test="${mfsAccountModel.segmentId == 10319}">
                    <td height="16" align="right" bgcolor="F3F3F3" class="formText"
                        width="25%">
                        Parent Cnic Pic:
                    </td>
                    <td bgcolor="FBFBFB" class="text" width="25%">
                        <spring:bind path="mfsAccountModel.parentCnicPic">

                            <c:choose>
                                <c:when test="${not empty param.appUserId}">
                                    <input type="file" tabindex="6" disabled="true" id="parentCnicPic"
                                           onchange="markNonDiscrepent(this);" name="parentCnicPic" class="upload"/>
                                </c:when>
                                <c:otherwise>
                                    <input type="file" tabindex="6" id="signPic" onchange="markNonDiscrepent(this);"
                                           name="parentCnicPic" class="upload"/>
                                </c:otherwise>
                            </c:choose>


                            <!-- <input type="file" tabindex="6" id="signPic" onchange="markNonDiscrepent(this);" name="signPic" class="upload" /> -->
                        </spring:bind>
                        &nbsp;&nbsp;
                        <c:if test="${mfsAccountModel.registrationStateId ne 1}">
                            <c:choose>
                                <c:when test="${not empty param.appUserId}">
                                    <img src="${contextPath}/images/upload_dir/parentCnicPic_${mfsAccountModel.appUserId}.${mfsAccountModel.parentCnicPicExt}?time=<%=System.currentTimeMillis()%>"
                                         width="100" height="100"/>
                                </c:when>
                                <c:when test="${param.isReSubmit}">
                                    <img src="${contextPath}/images/upload_dir/authorization/parentCnicPic_${param.authId}.${mfsAccountModel.parentCnicPicExt}?time=<%=System.currentTimeMillis()%>"
                                         width="100" height="100"/>
                                </c:when>
                            </c:choose>
                            <div style="display: none;" bgcolor="FBFBFB" class="text discrepant" width="10%">
                                Discrepant
                                <c:choose>
                                    <c:when test="${appUserTypeId == 3}">
                                        <html:checkbox title="Discrepant" disabled="true" readonly="readonly"
                                                       path="parentCnicPicDiscrepant"/>
                                        <html:hidden id="parentCnicPicDiscrepant2" path="parentCnicPicDiscrepant"/>
                                    </c:when>
                                    <c:otherwise>
                                        <html:checkbox title="Discrepant" path="parentCnicPicDiscrepant"/>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </c:if>
                        <c:if test="${mfsAccountModel.segmentId == 10319}">
                            <div align="left" bgcolor="F3F3F3" class="formText">
                                Maker Comments:
                            </div>
                            <div>
                                <html:textarea style="height:15px;" path="pNicPicMakerComments" tabindex="41" cssClass="textBox"
                                               maxlength="250"/>
                            </div>
                            <div align="left" bgcolor="F3F3F3" class="formText">
                                Checker Comments:
                            </div>
                            <div>
                                <html:textarea style="height:15px; background: #D3D3D3;"  readonly="true" path="pNicPicCheckerComments" tabindex="41" cssClass="textBox"
                                               maxlength="250"/>
                            </div>
                        </c:if>
                    </td>
                </c:when>

                <c:otherwise>
                <td height="16" align="right" bgcolor="F3F3F3" class="formText"
                    width="25%">
                    Signature Picture:
                </td>
                <td bgcolor="FBFBFB" class="text" width="25%">
                    <spring:bind path="mfsAccountModel.signPic">

                        <c:choose>
                            <c:when test="${not empty param.appUserId}">
                                <input type="file" tabindex="6" disabled="true" id="signPic"
                                       onchange="markNonDiscrepent(this);" name="signPic" class="upload"/>
                            </c:when>
                            <c:otherwise>
                                <input type="file" tabindex="6" id="signPic" onchange="markNonDiscrepent(this);"
                                       name="signPic" class="upload"/>
                            </c:otherwise>
                        </c:choose>


                        <!-- <input type="file" tabindex="6" id="signPic" onchange="markNonDiscrepent(this);" name="signPic" class="upload" /> -->
                    </spring:bind>
                    &nbsp;&nbsp;
                    <c:if test="${mfsAccountModel.registrationStateId ne 1}">
                        <c:choose>
                            <c:when test="${not empty param.appUserId}">
                                <img src="${contextPath}/images/upload_dir/signPic_${mfsAccountModel.appUserId}.${mfsAccountModel.signPicExt}?time=<%=System.currentTimeMillis()%>"
                                     width="100" height="100"/>
                            </c:when>
                            <c:when test="${param.isReSubmit}">
                                <img src="${contextPath}/images/upload_dir/authorization/signPic_${param.authId}.${mfsAccountModel.signPicExt}?time=<%=System.currentTimeMillis()%>"
                                     width="100" height="100"/>
                            </c:when>
                        </c:choose>
                        <div style="display: none;" bgcolor="FBFBFB" class="text discrepant" width="10%">
                            Discrepant
                            <c:choose>
                                <c:when test="${appUserTypeId == 3}">
                                    <html:checkbox title="Discrepant" disabled="true" readonly="readonly"
                                                   path="signPicDiscrepant"/>
                                    <html:hidden id="signPicDiscrepant2" path="signPicDiscrepant"/>
                                </c:when>
                                <c:otherwise>
                                    <html:checkbox title="Discrepant" path="signPicDiscrepant"/>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </c:if>
                </td>
                </c:otherwise>
                </c:choose>
            </tr>
            <tr>
                <c:choose>
                <c:when test="${mfsAccountModel.segmentId == 10319}">
                    <td height="16" align="right" bgcolor="F3F3F3" class="formText"
                        width="25%">
                        B Form Pic:
                    </td>
                    <td bgcolor="FBFBFB" class="text" width="25%">
                        <spring:bind path="mfsAccountModel.bFormPic">

                            <c:choose>
                                <c:when test="${not empty param.appUserId}">
                                    <input type="file" tabindex="6" disabled="true" id="bFormPic"
                                           onchange="markNonDiscrepent(this);" name="bFormPic" class="upload"/>
                                </c:when>
                                <c:otherwise>
                                    <input type="file" tabindex="6" id="bFormPic" onchange="markNonDiscrepent(this);"
                                           name="bFormPic" class="upload"/>
                                </c:otherwise>
                            </c:choose>


                            <!-- <input type="file" tabindex="6" id="signPic" onchange="markNonDiscrepent(this);" name="signPic" class="upload" /> -->
                        </spring:bind>
                        &nbsp;&nbsp;
                        <c:if test="${mfsAccountModel.registrationStateId ne 1}">
                            <c:choose>
                                <c:when test="${not empty param.appUserId}">
                                    <img src="${contextPath}/images/upload_dir/bFormPic_${mfsAccountModel.appUserId}.${mfsAccountModel.bFormPicExt}?time=<%=System.currentTimeMillis()%>"
                                         width="100" height="100"/>
                                </c:when>
                                <c:when test="${param.isReSubmit}">
                                    <img src="${contextPath}/images/upload_dir/authorization/bFormPic_${param.authId}.${mfsAccountModel.bFormPicExt}?time=<%=System.currentTimeMillis()%>"
                                         width="100" height="100"/>
                                </c:when>
                            </c:choose>
                            <div style="display: none;" bgcolor="FBFBFB" class="text discrepant" width="10%">
                                Discrepant
                                <c:choose>
                                    <c:when test="${appUserTypeId == 3}">
                                        <html:checkbox title="Discrepant" disabled="true" readonly="readonly"
                                                       path="bFormPicDiscrepant"/>
                                        <html:hidden id="bFormPicDiscrepant2" path="bFormPicDiscrepant"/>
                                    </c:when>
                                    <c:otherwise>
                                        <html:checkbox title="Discrepant" path="bFormPicDiscrepant"/>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </c:if>
                        <c:if test="${mfsAccountModel.segmentId == 10319}">
                            <div align="left" bgcolor="F3F3F3" class="formText">
                                Maker Comments:
                            </div>
                            <div>
                                <html:textarea style="height:15px;" path="bFormPicMakerComments" tabindex="41" cssClass="textBox"
                                               maxlength="250"/>
                            </div>
                            <div align="left" bgcolor="F3F3F3" class="formText">
                                Checker Comments:
                            </div>
                            <div>
                                <html:textarea style="height:15px; background: #D3D3D3;"  readonly="true" path="bFormPicCheckerComments" tabindex="41" cssClass="textBox"
                                               maxlength="250"/>
                            </div>
                        </c:if>
                    </td>
                </c:when>

                <c:otherwise>
                <td height="16" align="right" bgcolor="F3F3F3" class="formText"
                    width="25%">
                    <span class="l0form-asteric-feilds" style="color: #FF0000">*</span>Terms and Condition Picture:
                </td>
                <td bgcolor="FBFBFB" class="text" width="25%">
                    <spring:bind path="mfsAccountModel.tncPic">

                        <c:choose>
                            <c:when test="${not empty param.appUserId}">
                                <input type="file" tabindex="7" disabled="true" id="tncPic"
                                       onchange="markNonDiscrepent(this);" name="tncPic" class="upload"/>
                            </c:when>
                            <c:otherwise>
                                <input type="file" tabindex="7" id="tncPic" onchange="markNonDiscrepent(this);"
                                       name="tncPic" class="upload"/>
                            </c:otherwise>
                        </c:choose>


                        <!-- <input type="file" tabindex="7" id="tncPic" onchange="markNonDiscrepent(this);" name="tncPic" class="upload" /> -->
                    </spring:bind>
                    &nbsp;&nbsp;
                    <c:if test="${mfsAccountModel.registrationStateId ne 1}">
                        <c:choose>
                            <c:when test="${not empty param.appUserId}">
                                <img src="${contextPath}/images/upload_dir/tncPic_${mfsAccountModel.appUserId}.${mfsAccountModel.tncPicExt}?time=<%=System.currentTimeMillis()%>"
                                     width="100" height="100"/>
                            </c:when>
                            <c:when test="${param.isReSubmit}">
                                <img src="${contextPath}/images/upload_dir/authorization/tncPic_${param.authId}.${mfsAccountModel.tncPicExt}?time=<%=System.currentTimeMillis()%>"
                                     width="100" height="100"/>
                            </c:when>
                        </c:choose>
                        <div style="display: none;" bgcolor="FBFBFB" class="text discrepant" width="10%">
                            Discrepant
                            <c:choose>
                                <c:when test="${appUserTypeId == 3}">
                                    <html:checkbox title="Discrepant" disabled="true" readonly="readonly"
                                                   path="tncPicDiscrepant"/>
                                    <html:hidden id="tncPicDiscrepant2" path="tncPicDiscrepant"/>
                                </c:when>
                                <c:otherwise>
                                    <html:checkbox path="tncPicDiscrepant"/>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </c:if>
                    </c:otherwise>
                    </c:choose>
                </td>
                <td height="16" align="right" bgcolor="F3F3F3" class="formText"
                    width="25%">
                    <span class="l0form-asteric-feilds" style="color: #FF0000">*</span>CNIC Front:
                </td>
                <td bgcolor="FBFBFB" class="text" width="25%">
                    <spring:bind path="mfsAccountModel.cnicFrontPic">

                        <c:choose>
                            <c:when test="${not empty param.appUserId}">
                                <input type="file" tabindex="8" disabled="true" id="cnicFrontPic"
                                       onchange="markNonDiscrepent(this);" name="cnicFrontPic" class="upload"/>
                            </c:when>
                            <c:otherwise>
                                <input type="file" tabindex="8" id="cnicFrontPic" onchange="markNonDiscrepent(this);"
                                       name="cnicFrontPic" class="upload"/>
                            </c:otherwise>
                        </c:choose>


                        <!-- <input type="file" tabindex="8" id="cnicFrontPic" onchange="markNonDiscrepent(this);" name="cnicFrontPic" class="upload"/> -->
                    </spring:bind>
                    &nbsp;&nbsp;
                    <c:if test="${mfsAccountModel.registrationStateId ne 1}">
                        <c:choose>
                            <c:when test="${not empty param.appUserId}">
                                <img src="${contextPath}/images/upload_dir/cnicFrontPic_${mfsAccountModel.appUserId}.${mfsAccountModel.cnicFrontPicExt}?time=<%=System.currentTimeMillis()%>"
                                     width="100" height="100"/>
                            </c:when>
                            <c:when test="${param.isReSubmit}">
                                <img src="${contextPath}/images/upload_dir/authorization/cnicFrontPic_${param.authId}.${mfsAccountModel.cnicFrontPicExt}?time=<%=System.currentTimeMillis()%>"
                                     width="100" height="100"/>
                            </c:when>
                        </c:choose>
                        <div style="display: none;" bgcolor="FBFBFB" class="text discrepant" width="10%">
                            Discrepant
                            <c:choose>
                                <c:when test="${appUserTypeId == 3}">
                                    <html:checkbox title="Discrepant" disabled="true" readonly="readonly"
                                                   path="cnicFrontPicDiscrepant"/>
                                    <html:hidden id="cnicFrontPicDiscrepant2" path="cnicFrontPicDiscrepant"/>
                                </c:when>
                                <c:otherwise>
                                    <html:checkbox title="Discrepant" path="cnicFrontPicDiscrepant"/>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </c:if>
                    <c:if test="${mfsAccountModel.segmentId == 10319}">
                        <div align="left" bgcolor="F3F3F3" class="formText">
                            Maker Comments:
                        </div>
                        <div>
                            <html:textarea style="height:15px;" path="nicFrontPicMakerComments" tabindex="41" cssClass="textBox"
                                           maxlength="250"/>
                        </div>
                        <div align="left" bgcolor="F3F3F3" class="formText">
                            Checker Comments:
                        </div>
                        <div>
                            <html:textarea style="height:15px; background: #D3D3D3;"  readonly="true" path="nicFrontPicCheckerComments" tabindex="41" cssClass="textBox"
                                           maxlength="250"/>
                        </div>
                    </c:if>
                </td>
            </tr>

            <c:if test="${mfsAccountModel.segmentId != 10319}">
            <tr>
                <td height="16" align="right" bgcolor="F3F3F3" class="formText"
                    width="25%">
                    Source of Income Pic:
                </td>
                <td bgcolor="FBFBFB" class="text" width="25%">
                    <spring:bind path="mfsAccountModel.sourceOfIncomePic">

                        <c:choose>
                            <c:when test="${not empty param.appUserId}">
                                <input type="file" tabindex="9" id="sourceOfIncomePic"
                                       onchange="markNonDiscrepent(this);" name="sourceOfIncomePic" class="upload"/>
                            </c:when>
                            <c:otherwise>
                                <input type="file" tabindex="9" id="sourceOfIncomePic"
                                       onchange="markNonDiscrepent(this);"
                                       name="sourceOfIncomePic" class="upload"/>
                            </c:otherwise>
                        </c:choose>


                        <!-- <input type="file" tabindex="9" id="cnicBackPic" onchange="markNonDiscrepent(this);" name="cnicBackPic" class="upload" /> -->
                    </spring:bind>
                    &nbsp;&nbsp;
                    <c:if test="${mfsAccountModel.registrationStateId ne 1}">
                        <c:choose>
                            <c:when test="${not empty param.appUserId}">
                                <img src="${contextPath}/images/upload_dir/sourceOfIncomePic_${mfsAccountModel.appUserId}.${mfsAccountModel.sourceOfIncomePicExt}?time=<%=System.currentTimeMillis()%>"
                                     width="100" height="100"/>
                            </c:when>
                            <c:when test="${param.isReSubmit}">
                                <img src="${contextPath}/images/upload_dir/authorization/sourceOfIncomePic_${param.authId}.${mfsAccountModel.sourceOfIncomePicExt}?time=<%=System.currentTimeMillis()%>"
                                     width="100" height="100"/>
                            </c:when>
                        </c:choose>
                        <div style="display: none;" bgcolor="FBFBFB" class="text discrepant" width="10%">
                            Discrepant
                            <c:choose>
                                <c:when test="${appUserTypeId == 3}">
                                    <html:checkbox title="Discrepant" disabled="true" readonly="readonly"
                                                   path="sourceOfIncomePicDiscrepant"/>
                                    <html:hidden id="cnicBackPicDiscrepant2" path="sourceOfIncomePicDiscrepant"/>
                                </c:when>
                                <c:otherwise>
                                    <html:checkbox title="Discrepant" path="sourceOfIncomePicDiscrepant"/>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </c:if>
                </td>
            </tr>
            <tr class="blinklform-feilds">
                <td height="16" align="right" bgcolor="F3F3F3" class="formText"
                    width="25%">
                    Proof of Profession:
                </td>
                <td bgcolor="FBFBFB" class="text" width="25%">
                    <spring:bind path="mfsAccountModel.proofOfProfessionPic">

                        <c:choose>
                            <c:when test="${not empty param.appUserId}">
                                <input type="file" tabindex="9" disabled="true" id="proofOfProfessionPic"
                                       onchange="markNonDiscrepent(this);" name="proofOfProfessionPic" class="upload"/>
                            </c:when>
                            <c:otherwise>
                                <input type="file" tabindex="9" id="proofOfProfessionPic"
                                       onchange="markNonDiscrepent(this);"
                                       name="proofOfProfessionPic" class="upload"/>
                            </c:otherwise>
                        </c:choose>


                    </spring:bind>
                    &nbsp;&nbsp;
                    <c:if test="${mfsAccountModel.registrationStateId ne 1}">
                        <c:choose>
                            <c:when test="${not empty param.appUserId}">
                                <img src="${contextPath}/images/upload_dir/proofOfProfessionPic_${mfsAccountModel.appUserId}.${mfsAccountModel.proofOfProfessionExt}?time=<%=System.currentTimeMillis()%>"
                                     width="100" height="100"/>
                            </c:when>
                            <c:when test="${param.isReSubmit}">
                                <img src="${contextPath}/images/upload_dir/authorization/proofOfProfessionPic_${param.authId}.${mfsAccountModel.proofOfProfessionExt}?time=<%=System.currentTimeMillis()%>"
                                     width="100" height="100"/>
                            </c:when>
                        </c:choose>
                        <div style="display: none;" bgcolor="FBFBFB" class="text discrepant" width="10%">
                            Discrepant
                            <c:choose>
                                <c:when test="${appUserTypeId == 3}">
                                    <html:checkbox title="Discrepant" disabled="true" readonly="readonly"
                                                   path="proofOfProfessionPicDiscrepant"/>
                                    <html:hidden id="proofOfProfessionPicDiscrepant2"
                                                 path="proofOfProfessionPicDiscrepant"/>
                                </c:when>
                                <c:otherwise>
                                    <html:checkbox title="Discrepant" path="proofOfProfessionPicDiscrepant"/>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </c:if>
                </td>
            </tr>
        </c:if>

            <tr>
                <td height="16" align="right" bgcolor="F3F3F3" class="formText"
                    width="25%">
                    CNIC Back:
                </td>
                <td bgcolor="FBFBFB" class="text" width="25%">
                    <spring:bind path="mfsAccountModel.cnicBackPic">

                        <c:choose>
                            <c:when test="${not empty param.appUserId}">
                                <input type="file" tabindex="9" disabled="true" id="cnicBackPic"
                                       onchange="markNonDiscrepent(this);" name="cnicBackPic" class="upload"/>
                            </c:when>
                            <c:otherwise>
                                <input type="file" tabindex="9" id="cnicBackPic" onchange="markNonDiscrepent(this);"
                                       name="cnicBackPic" class="upload"/>
                            </c:otherwise>
                        </c:choose>


                        <!-- <input type="file" tabindex="9" id="cnicBackPic" onchange="markNonDiscrepent(this);" name="cnicBackPic" class="upload" /> -->
                    </spring:bind>
                    &nbsp;&nbsp;
                    <c:if test="${mfsAccountModel.registrationStateId ne 1}">
                        <c:choose>
                            <c:when test="${not empty param.appUserId}">
                                <img src="${contextPath}/images/upload_dir/cnicBackPic_${mfsAccountModel.appUserId}.${mfsAccountModel.cnicBackPicExt}?time=<%=System.currentTimeMillis()%>"
                                     width="100" height="100"/>
                            </c:when>
                            <c:when test="${param.isReSubmit}">
                                <img src="${contextPath}/images/upload_dir/authorization/cnicBackPic_${param.authId}.${mfsAccountModel.cnicBackPicExt}?time=<%=System.currentTimeMillis()%>"
                                     width="100" height="100"/>
                            </c:when>
                        </c:choose>
                        <div style="display: none;" bgcolor="FBFBFB" class="text discrepant" width="10%">
                            Discrepant
                            <c:choose>
                                <c:when test="${appUserTypeId == 3}">
                                    <html:checkbox title="Discrepant" disabled="true" readonly="readonly"
                                                   path="cnicBackPicDiscrepant"/>
                                    <html:hidden id="cnicBackPicDiscrepant2" path="cnicBackPicDiscrepant"/>
                                </c:when>
                                <c:otherwise>
                                    <html:checkbox title="Discrepant" path="cnicBackPicDiscrepant"/>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </c:if>
                    <c:if test="${mfsAccountModel.segmentId == 10319}">
                        <div align="left" bgcolor="F3F3F3" class="formText">
                            Maker Comments:
                        </div>
                        <div>
                            <html:textarea style="height:15px;" path="nicBackPicMakerComments" tabindex="41" cssClass="textBox"
                                           maxlength="250"/>
                        </div>
                        <div align="left" bgcolor="F3F3F3" class="formText">
                            Checker Comments:
                        </div>
                        <div>
                            <html:textarea style="height:15px; background: #D3D3D3;"  readonly="true" path="nicBackPicCheckerComments" tabindex="41" cssClass="textBox"
                                           maxlength="250"/>
                        </div>
                    </c:if>
                </td>

                <c:if test="${mfsAccountModel.segmentId != 10319}">

                <td class="formText l1form-feilds" style="display: none;" height="16" align="right" bgcolor="F3F3F3"
                    class="formText" width="25%">
                    <span class="l0form-asteric-feilds" style="color: #FF0000">*</span>Level 1 Form:
                </td>
                <td class="l1form-feilds" style="display: none;" bgcolor="FBFBFB" class="text" width="25%">
                    <spring:bind path="mfsAccountModel.level1FormPic">
                        <input type="file" tabindex="10" id="level1FormPic" onchange="markNonDiscrepent(this);"
                               name="level1FormPic" class="upload"/>
                    </spring:bind>
                    &nbsp;&nbsp;
                    <c:if test="${mfsAccountModel.registrationStateId ne 1}">
                        <c:choose>
                            <c:when test="${not empty param.appUserId}">
                                <img src="${contextPath}/images/upload_dir/level1FormPic_${mfsAccountModel.appUserId}.${mfsAccountModel.level1FormPicExt}?time=<%=System.currentTimeMillis()%>"
                                     width="100" height="100"/>
                            </c:when>
                        </c:choose>
                        <div style="display: none;" bgcolor="FBFBFB" class="text discrepant" width="10%">
                            Discrepant
                            <c:choose>
                                <c:when test="${appUserTypeId == 3}">
                                    <html:checkbox title="Discrepant" disabled="true" readonly="readonly"
                                                   path="level1FormPicDiscrepant"/>
                                    <html:hidden id="level1FormPicDiscrepant2" path="level1FormPicDiscrepant"/>
                                </c:when>
                                <c:otherwise>
                                    <html:checkbox title="Discrepant" path="level1FormPicDiscrepant"/>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </c:if>
                </td>
                </c:if>

                <c:if test="${mfsAccountModel.segmentId == 10319}">
                <td height="16" align="right" bgcolor="F3F3F3" class="formText"
                    width="25%">
                    Parent CNIC Back:
                </td>
                <td bgcolor="FBFBFB" class="text" width="25%">
                    <spring:bind path="mfsAccountModel.parentCnicBackPic">

                        <c:choose>
                            <c:when test="${not empty param.appUserId}">
                                <input type="file" tabindex="9" disabled="true" id="cnicBaparentBackCnicPicckPic"
                                       onchange="markNonDiscrepent(this);" name="parentCnicBackPic" class="upload"/>
                            </c:when>
                            <c:otherwise>
                                <input type="file" tabindex="9" id="parentCnicBackPic" onchange="markNonDiscrepent(this);"
                                       name="parentCnicBackPic" class="upload"/>
                            </c:otherwise>
                        </c:choose>


                        <!-- <input type="file" tabindex="9" id="cnicBackPic" onchange="markNonDiscrepent(this);" name="cnicBackPic" class="upload" /> -->
                    </spring:bind>
                    &nbsp;&nbsp;
                    <c:if test="${mfsAccountModel.registrationStateId ne 1}">
                        <c:choose>
                            <c:when test="${not empty param.appUserId}">
                                <img src="${contextPath}/images/upload_dir/parentCnicBackPic_${mfsAccountModel.appUserId}.${mfsAccountModel.parentCnicBackPicExt}?time=<%=System.currentTimeMillis()%>"
                                     width="100" height="100"/>
                            </c:when>
                            <c:when test="${param.isReSubmit}">
                                <img src="${contextPath}/images/upload_dir/authorization/parentCnicBackPic_${param.authId}.${mfsAccountModel.parentCnicBackPicExt}?time=<%=System.currentTimeMillis()%>"
                                     width="100" height="100"/>
                            </c:when>
                        </c:choose>
                        <div style="display: none;" bgcolor="FBFBFB" class="text discrepant" width="10%">
                            Discrepant
                            <c:choose>
                                <c:when test="${appUserTypeId == 3}">
                                    <html:checkbox title="Discrepant" disabled="true" readonly="readonly"
                                                   path="parentCnicBackPicDiscrepant"/>
                                    <html:hidden id="parentCnicBackPicDiscrepant2" path="parentCnicBackPicDiscrepant"/>
                                </c:when>
                                <c:otherwise>
                                    <html:checkbox title="Discrepant" path="parentCnicBackPicDiscrepant"/>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </c:if>
                    <c:if test="${mfsAccountModel.segmentId == 10319}">
                        <div align="left" bgcolor="F3F3F3" class="formText">
                            Maker Comments:
                        </div>
                        <div>
                            <html:textarea style="height:15px;" path="pNicBackPicMakerComments" tabindex="41" cssClass="textBox"
                                           maxlength="250"/>
                        </div>
                        <div align="left" bgcolor="F3F3F3" class="formText">
                            Checker Comments:
                        </div>
                        <div>
                            <html:textarea style="height:15px; background: #D3D3D3;"  readonly="true" path="pNicBackPicCheckerComments" tabindex="41" cssClass="textBox"
                                           maxlength="250"/>
                        </div>
                    </c:if>
                </td>
                </c:if>
            </tr>

            <tr>
                <td height="16" align="right" bgcolor="F3F3F3" class="formText">
                    <span style="color: #FF0000">*</span>CNIC #:
                </td>
                <td bgcolor="FBFBFB" class="formText">
                    <c:choose>
                        <c:when test="${not empty param.appUserId or not empty mfsAccountModel.appUserId}">
                            <html:input path="nic" cssClass="textBox" style="background: #D3D3D3; " readonly="true"
                                        tabindex="11" maxlength="13" onkeypress="return maskNumber(this,event)"/>
                        </c:when>
                        <c:otherwise>
                            <html:input path="nic" cssClass="textBox" tabindex="11" maxlength="13"
                                        onkeypress="return maskNumber(this,event)"/>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td height="16" align="right" bgcolor="F3F3F3" class="formText">
                    <span style="color: #FF0000">*</span>Date of Birth:
                </td>
                <td bgcolor="FBFBFB">
                    <html:input path="dob" cssClass="textBox" tabindex="12"
                                maxlength="50"/>
                    <img id="sDate" tabindex="13" name="popcal" align="top"
                         style="cursor:pointer" src="${pageContext.request.contextPath}/images/cal.gif" border="0"/>
                    <img id="sDate" tabindex="14" name="popcal" title="Clear Date"
                         onclick="javascript:$('dob').value=''" align="middle"
                         style="cursor:pointer" src="${pageContext.request.contextPath}/images/refresh.png" border="0"/>
                </td>
            </tr>
            <tr>
                <td height="16" align="right" bgcolor="F3F3F3" class="formText">
                    <span style="color: #FF0000">*</span>CNIC Expiry:
                </td>
                <td bgcolor="FBFBFB">
                    <html:input path="nicExpiryDate" cssClass="textBox" tabindex="15" maxlength="50"/>
                    <img id="nicDate" tabindex="16" name="popcal" align="top"
                         style="cursor:pointer" src="${pageContext.request.contextPath}/images/cal.gif" border="0"/>
                    <img id="nicDate" tabindex="17" name="popcal" title="Clear Date"
                         onclick="javascript:$('nicExpiryDate').value=''" align="middle"
                         style="cursor:pointer" src="${pageContext.request.contextPath}/images/refresh.png" border="0"/>
                </td>
                <c:if test="${appUserTypeId == 3}">
                    <td align="right" width="25%" bgcolor="F3F3F3" class="formText">
                        <span style="color: #FF0000">*</span>Initial Deposit:
                    </td>
                    <c:choose>
                        <c:when test="${not empty param.appUserId or not empty mfsAccountModel.appUserId}">
                            <td align="left" bgcolor="FBFBFB" class="formText" width="25%">
                                <div class="textBox"
                                     style="background: #D3D3D3; ">${mfsAccountModel.initialDeposit}</div>
                            </td>
                        </c:when>
                        <c:otherwise>
                            <td align="left" bgcolor="FBFBFB">
                                <html:input path="initialDeposit" maxlength="6" cssClass="textBox" tabindex="18"
                                            id="initialDeposit"
                                            onkeypress="return maskNumber(this,event)"/>
                            </td>
                        </c:otherwise>
                    </c:choose>
                </c:if>


                <td height="16" align="right" bgcolor="F3F3F3" class="formText"
                    width="25%">
                    <span style="color: #FF0000">*</span>Customer Catalog:
                </td>
                <td align="left" bgcolor="FBFBFB" class="formText" width="25%">
                    <html:select path="productCatalogId" cssClass="textBox"
                                 tabindex="8">
                        <html:option value="">[Select]</html:option>
                        <c:if test="${productCatalogList != null}">
                            <html:options items="${productCatalogList}" itemLabel="name" itemValue="productCatalogId"/>
                        </c:if>
                    </html:select>
                </td>


            </tr>
            <tr>
                <td height="16" align="right" bgcolor="F3F3F3" class="formText"
                    width="25%">
                    <span class="l0form-asteric-feilds" style="color: #FF0000">*</span>Original CNIC Seen:
                <td align="left" bgcolor="FBFBFB" class="formText" width="25%">
                    <html:checkbox path="cnicSeen" tabindex="19"/>
                </td>
                <c:if test="${appUserTypeId == 3 and (empty param.appUserId or empty mfsAccountModel.appUserId)}">
                    <td align="right" width="25%" bgcolor="F3F3F3" class="formText">
                        <span style="color: #FF0000">*</span>Agent PIN:
                    </td>
                    <td align="left" bgcolor="FBFBFB">
                        <html:password path="agentPIN" cssClass="textBox" tabindex="20" maxlength="4"
                                       onkeypress="return maskNumber(this,event)"/>
                    </td>
                </c:if>
            </tr>
            <c:if test="${appUserTypeId == 6}">
                <tr class="pob">
                    <td height="16" align="right" bgcolor="F3F3F3" class="formText"
                        width="25%">
                        <span style="color: #FF0000">*</span>Place of Birth:
                    <td align="left" bgcolor="FBFBFB" class="formText" width="25%">
                        <html:input path="birthPlace" id="birthPlace" cssClass="textBox" tabindex="21" maxlength="100"/>
                    </td>
                    <td height="16" align="right" bgcolor="F3F3F3" class="formText">
                        <span class="l0form-asteric-feilds" style="color: #FF0000">*</span>Gender:
                    </td>
                    <td bgcolor="FBFBFB">
                        <html:radiobuttons class="gender" tabindex="22" items="${genderList}" itemLabel="label"
                                           itemValue="value" path="gender"/>
                    </td>
                </tr>
                <tr>
                    <td height="16" align="right" bgcolor="F3F3F3" class="formText">
                        <span class="l0form-asteric-feilds" style="color: #FF0000">*</span>Father/Husband Name:
                    </td>
                    <td bgcolor="FBFBFB">
                        <html:input path="fatherHusbandName" maxlength="50" cssClass="textBox" tabindex="23"
                                    onkeypress="return maskAlphaWithSp(this,event)"/>
                    </td>
                    <td height="16" align="right" bgcolor="F3F3F3" class="formText">
                        <span style="color: #FF0000"></span>Mother's Maiden Name:
                    </td>
                    <td bgcolor="FBFBFB">
                        <html:input path="motherMaidenName" maxlength="50" cssClass="textBox" tabindex="24"
                                    onkeypress="return maskAlphaWithSp(this,event)"/>
                    </td>
                </tr>
                <tr>
                    <td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">
                        <span style="color: #FF0000">*</span>Mailing Address:
                    <td align="left" bgcolor="FBFBFB" class="formText" width="25%">
                        <html:textarea style="height:50px;" path="presentAddress" tabindex="25" cssClass="textBox"
                                       maxlength="250"/>
                    </td>
                    <td height="16" align="right" bgcolor="F3F3F3" class="formText">
                        Email Address:
                    </td>
                    <td bgcolor="FBFBFB">
                        <html:input path="email" maxlength="50" cssClass="textBox" tabindex="24"
                                    onkeypress="return maskAlphaWithSp(this,event)"/>
                    </td>

                </tr>
                <tr>
                    <td height="16" align="right" bgcolor="F3F3F3" class="formText">
                        Father Cnic:
                    </td>
                    <td bgcolor="FBFBFB">
                        <html:input path="fatherCnic" maxlength="13" cssClass="textBox" tabindex="24"/>
                    </td>
                    <td height="16" align="right" bgcolor="F3F3F3" class="formText">
                        Mother Cnic:
                    </td>
                    <td bgcolor="FBFBFB">
                        <html:input path="motherCnic" maxlength="50" cssClass="textBox" tabindex="24"
                                    onkeypress="return maskAlphaWithSp(this,event)"/>
                    </td>

                </tr>
                <tr class="blinklform-feilds">
                    <td height="16" align="right" bgcolor="F3F3F3" class="formText">
                        Expected Monthly TurnOver:
                    </td>
                    <td bgcolor="FBFBFB" class="blinklform-feilds">
                        <html:input path="expectedMonthlyTurnOver" maxlength="50" cssClass="textBox" tabindex="24"
                                    readonly="true"/>
                    </td>
                    <td height="16" align="right" bgcolor="F3F3F3" class="formText">
                        CLS Response Code:
                    </td>
                    <td bgcolor="FBFBFB" class="blinklform-feilds">
                        <html:input path="clsResponseCode" maxlength="50" cssClass="textBox" tabindex="24"
                                    readonly="true"/>
                    </td>
                </tr>
                <tr>
                    <td align="right" bgcolor="F3F3F3" class="formText">
                        <span class="l0form-asteric-feilds" style="color: #FF0000">*</span>City:
                    </td>
                    <td align="left" bgcolor="FBFBFB">
                        <html:select path="city" id="city" cssClass="textBox" tabindex="27">
                            <html:option value="">[Select]</html:option>
                            <c:if test="${cityList != null}">
                                <html:options items="${cityList}" itemLabel="name" itemValue="cityId"/>
                            </c:if>
                        </html:select>
                    </td>
                    <td height="16" align="right" bgcolor="F3F3F3" class="formText">
                        <span class="l0form-asteric-feilds" style="color: #FF0000"></span>CNIC Issuance Date:
                    </td>
                    <td bgcolor="FBFBFB">
                        <html:input path="cnicIssuanceDate" cssClass="textBox" tabindex="15" maxlength="50"/>
                        <img id="nicIssueDate" tabindex="16" name="popcal" align="top"
                             style="cursor:pointer" src="${pageContext.request.contextPath}/images/cal.gif" border="0"/>
                        <img id="nicIssueDate" tabindex="17" name="popcal" title="Clear Date"
                             onclick="javascript:$('cnicIssuanceDate').value=''" align="middle"
                             style="cursor:pointer" src="${pageContext.request.contextPath}/images/refresh.png"
                             border="0"/>
                    </td>
                </tr>
            </c:if>
            <c:if test="${appUserTypeId == 3}">
                <tr style="display:none;">
                    <td align="right" bgcolor="F3F3F3" class="formText">
                        Segment:
                    </td>
                    <td align="left" bgcolor="FBFBFB">
                        <html:select path="segmentId" cssClass="textBox" tabindex="28">
                            <c:if test="${segmentList != null}">
                                <html:options items="${segmentList}" itemLabel="name" itemValue="segmentId"/>
                            </c:if>
                        </html:select>
                    </td>
                </tr>
            </c:if>

            <c:if test="${mfsAccountModel.segmentId == 10319}">
            <tr>
                <td height="16" align="right" bgcolor="F3F3F3" class="formText"
                    width="25%">
                    <span class="l0form-asteric-feilds" style="color: #FF0000">*</span>Father BVS:
                <td align="left" bgcolor="FBFBFB" class="formText" width="25%">
                    <html:checkbox path="fatherBvs" tabindex="19" onclick="return false;"/>
                </td>
            </tr>
            </c:if>

            <c:if test="${appUserTypeId == 6}">
                <tr>
                    <td align="right" bgcolor="F3F3F3" class="formText">
                        Segment:
                    </td>
                    <td align="left" bgcolor="FBFBFB">
                        <html:select path="segmentId" cssClass="textBox"
                                     tabindex="29">
                            <c:if test="${segmentList != null}">
                                <html:options items="${segmentList}" itemLabel="name" itemValue="segmentId"/>
                            </c:if>
                        </html:select>
                    </td>

                    <td style="display: none;" height="16" align="right" bgcolor="F3F3F3" class="l1form-feilds formText"
                        width="25%">
                        <span class="l0form-asteric-feilds" style="color: #FF0000">*</span>Source of Funds:
                    </td>
                    <td style="display: none;" align="left" bgcolor="FBFBFB" class="l1form-feilds formText" width="25%">
                        <html:select path="fundsSourceId" id="fundsSource" multiple="multiple" cssClass="textBox"
                                     tabindex="30">
                            <c:if test="${fundSourceList != null}">
                                <html:options items="${fundSourceList}" itemLabel="name" itemValue="fundSourceId"/>
                            </c:if>
                        </html:select>
                    </td>
                </tr>

                <tr>
                    <td height="16" align="right" bgcolor="F3F3F3" class="formText"
                        width="25%">
                    <td align="left" bgcolor="FBFBFB" class="formText" width="25%">
                    </td>
                    <td height="16" align="right" bgcolor="F3F3F3" class="formText"
                        width="25%">
                        Filer Marking:
                    <td align="left" bgcolor="FBFBFB" class="formText" width="25%">
                        <html:radiobuttons tabindex="39" items="${filerList}" itemLabel="label" itemValue="value"
                                           path="filer"/>
                    </td>
                </tr>

                <tr>
                    <td align="right" bgcolor="F3F3F3" class="formText">
                        <span style="color: #FF0000">*</span>Tax Regime:
                    </td>
                    <td align="left" bgcolor="FBFBFB">
                        <html:select path="taxRegimeId" id="taxRegimeId" cssClass="textBox" tabindex="31">
                            <html:option value="">[Select]</html:option>
                            <c:if test="${taxRegimeList != null}">
                                <html:options items="${taxRegimeList}" itemLabel="name" itemValue="taxRegimeId"/>
                            </c:if>
                        </html:select>
                    </td>
                    <td align="right" bgcolor="F3F3F3" class="formText">
                        <span style="color: #FF0000">*</span>FED (%age):
                    </td>
                    <td align="left" bgcolor="FBFBFB">
                        <html:input path="fed" cssClass="textBox" tabindex="32" onfocus="this.blur()" readonly="true"
                                    maxlength="5"/>
                    </td>
                </tr>
                <tr class="l1form-feilds" style="display: none;">
                    <td colspan="2" align="left" bgcolor="FBFBFB">
                        <h3>Next of KIN:</h3>
                    </td>
                </tr>
                <tr class="l1form-feilds" style="display: none;">
                    <td align="right" width="25%" bgcolor="F3F3F3" class="formText">
                        Name:
                    </td>
                    <td align="left" bgcolor="FBFBFB">
                        <html:input path="nokName" cssClass="textBox" tabindex="33" maxlength="50"
                                    onkeypress="return maskAlphaWithSp(this,event)"/>
                    </td>
                </tr>
                <tr class="l1form-feilds" style="display: none;">
                    <td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">
                        Relationship:
                    </td>
                    <td align="left" bgcolor="FBFBFB" class="formText" width="25%">
                        <html:input path="nokRelationship" tabindex="34" cssClass="textBox" maxlength="50"
                                    onkeypress="return maskAlphaWithSp(this,event)"/>
                    </td>
                    <td height="16" align="right" bgcolor="F3F3F3" class="formText">
                        CNIC #:
                    </td>
                    <td bgcolor="FBFBFB" class="formText">
                        <html:input path="nokNic" cssClass="textBox" tabindex="35" maxlength="13"
                                    onkeypress="return maskNumber(this,event)"/>
                    </td>
                </tr>
                <tr class="l1form-feilds" style="display: none;">
                    <td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">
                        Address:
                    </td>
                    <td align="left" bgcolor="FBFBFB" class="formText" width="25%">
                        <html:textarea style="height:50px;" path="nokMailingAdd" tabindex="36" cssClass="textBox"/>
                    </td>
                    <td height="16" align="right" bgcolor="F3F3F3" class="formText" width="25%">
                        Mobile #:
                    </td>
                    <td align="left" bgcolor="FBFBFB" class="formText" width="25%">
                        <html:input path="nokMobile" tabindex="37" maxlength="11" cssClass="textBox"
                                    onkeypress="return maskNumber(this,event)"/>
                    </td>
                </tr>
                <tr>
                    <td height="16" align="right" bgcolor="F3F3F3" class="formText"
                        width="25%">
                        <span style="color: #FF0000">*</span>NADRA Verification:
                    <td align="left" bgcolor="FBFBFB" class="formText" width="25%">
                        <html:radiobuttons class="verisys" tabindex="38" items="${nadraVerList}" itemLabel="label"
                                           itemValue="value" path="verisysDone"/>
                    </td>
                    <td height="16" align="right" bgcolor="F3F3F3" class="formText"
                        width="25%">
                        Screening:
                    <td align="left" bgcolor="FBFBFB" class="formText" width="25%">
                        <html:radiobuttons tabindex="39" items="${screeningList}" itemLabel="label" itemValue="value"
                                           path="screeningPerformed"/>
                    </td>
                </tr>
            </c:if>
            <c:if test="${not empty param.appUserId or not empty mfsAccountModel.appUserId}">
                <tr>
                    <c:choose>
                        <c:when test="${appUserTypeId == 3}">
                            <td align="right" bgcolor="F3F3F3" class="formText">
                                <span style="color: #FF0000">*</span>Registration State:
                            </td>
                            <td align="left" bgcolor="FBFBFB" class="formText">
                                <html:select path="registrationStateId" style="display: none;" tabindex="40">
                                    <html:option value="">[Select]</html:option>
                                    <c:if test="${regStateList != null}">
                                        <html:options items="${regStateList}" itemLabel="name"
                                                      itemValue="registrationStateId"/>
                                    </c:if>
                                </html:select>
                                <c:choose>
                                    <c:when test="${mfsAccountModel.registrationStateId==2}">
                                        <div class="textBox" style="background: #D3D3D3; font-size:14px; ">Request
                                            Received
                                        </div>
                                    </c:when>
                                    <c:when test="${mfsAccountModel.registrationStateId==3}">
                                        <div class="textBox" style="background: #D3D3D3; font-size:14px; ">Approved
                                        </div>
                                    </c:when>
                                    <c:when test="${mfsAccountModel.registrationStateId==4}">
                                        <div class="textBox" style="background: #D3D3D3; font-size:14px; ">Discrepent
                                        </div>
                                    </c:when>
                                    <c:when test="${mfsAccountModel.registrationStateId==5}">
                                        <div class="textBox" style="background: #D3D3D3; font-size:14px; ">Decline</div>
                                    </c:when>
                                    <c:when test="${mfsAccountModel.registrationStateId==6}">
                                        <div class="textBox" style="background: #D3D3D3; font-size:14px; ">Rejected
                                        </div>
                                    </c:when>

                                    <c:when test="${mfsAccountModel.registrationStateId==12}">

                                        <div class="textBox" style="background: #D3D3D3; font-size:14px; ">Blink Pending

                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </c:when>
                        <c:otherwise>
                            <td align="right" bgcolor="F3F3F3" class="formText">
                                <span style="color: #FF0000">*</span>Registration State:
                            </td>
                            <td align="left" bgcolor="FBFBFB" class="formText">
                                <c:choose>
                                    <c:when test="${mfsAccountModel.registrationStateId==3}">
                                        <div class="textBox" style="background: #D3D3D3; font-size:14px; ">Approved
                                        </div>
                                        <html:hidden path="registrationStateId"/>
                                    </c:when>
                                    <c:when test="${mfsAccountModel.registrationStateId==5}">
                                        <div class="textBox" style="background: #D3D3D3; font-size:14px; ">Decline</div>
                                        <html:hidden path="registrationStateId"/>
                                    </c:when>
                                    <c:when test="${mfsAccountModel.registrationStateId==6}">
                                        <div class="textBox" style="background: #D3D3D3; font-size:14px; ">Rejected
                                        </div>
                                        <html:hidden path="registrationStateId"/>
                                    </c:when>
                                    <c:when test="${mfsAccountModel.registrationStateId==9}">
                                        <div class="textBox" style="background: #D3D3D3; font-size:14px; ">Dormant</div>
                                        <html:hidden path="registrationStateId"/>
                                    </c:when>
                                    <c:otherwise>
                                        <html:select path="registrationStateId" cssClass="textBox"
                                                     id="registrationStateId"
                                                     tabindex="18" onchange="regStateChange(this);">
                                            <html:option value="">[Select]</html:option>
                                            <c:if test="${regStateList != null}">
                                                <html:options items="${regStateList}" itemLabel="name"
                                                              itemValue="registrationStateId"/>
                                            </c:if>
                                        </html:select>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </c:otherwise>
                    </c:choose>
                    <td height="16" align="right" bgcolor="F3F3F3" class="formText"
                        width="25%">
                        Comments:
                    </td>
                    <td align="left" bgcolor="FBFBFB" class="formText" width="25%">
                        <html:textarea style="height:50px;" path="regStateComments" tabindex="41" cssClass="textBox"
                                       maxlength="250"/>
                    </td>
                </tr>
            </c:if>
            <tr>
                <c:choose>
                    <c:when test="${not empty param.appUserId or not empty mfsAccountModel.appUserId}">
                        <td align="right" bgcolor="FBFBFB" class="formText" width="25%">
                            Terms and Condition:
                        </td>
                        <td align="left" bgcolor="FBFBFB" class="formText" width="25%">
                            <input type="checkbox" name="T&C" checked="checked"/>
                        </td>
                    </c:when>
                    <c:otherwise>
                    </c:otherwise>
                </c:choose>
            </tr>
        </table>
        <div style="clear:both"></div>
    </div>

    <div id="table_form_blink">

        <table align="center" class="blinkform-fields">
            <tr class="blinklform-feilds">
                <h3 class="blinklform-feilds"><b>Limits:</b></h3>
            </tr>
            <tr class="blinklform-feilds">
                <td height="25" width="15%" align="center" bgcolor="#00BFFF" class="formText">
                    <span style="color: #FF0000"></span>Limit Type:
                </td>
                <td height="25" width="15%" align="center" bgcolor="#00BFFF" class="formText">
                    <span style="color: #FF0000"></span>Debit Limit:
                </td>
                <td height="25" width="15%" align="center" bgcolor="#00BFFF" class="formText">
                    <span style="color: #FF0000"></span>Credit Limit:
                </td>
                <td height="25" width="15%" align="center" bgcolor="#00BFFF" class="formText">
                    <span style="color: #FF0000"></span>Apply Limit:
                </td>
            </tr>
            <tr class="blinklform-feilds">
                <td bgcolor="FBFBFB">
                    <html:input path="daily" maxlength="50" width="115%" cssClass="textBox" tabindex="44"
                                readonly="true"/>
                </td>
                <td bgcolor="FBFBFB">
                    <html:input path="debitLimitDaily" maxlength="50" width="115%" cssClass="textBox" tabindex="44"/>
                </td>
                <td bgcolor="FBFBFB">
                    <html:input path="creditLimitDaily" maxlength="50" width="115%" cssClass="textBox" tabindex="44"/>
                </td>
                <td bgcolor="FBFBFB" align="center">
                    <html:checkbox path="dailyCheck"/>
                </td>
            </tr>
            <tr class="blinklform-feilds">
                <td bgcolor="FBFBFB">
                    <html:input path="monthly" maxlength="50" width="115%" cssClass="textBox" tabindex="44"
                                readonly="true"/>
                </td>
                <td bgcolor="FBFBFB">
                    <html:input path="debitLimitMonthly" maxlength="50" width="115%" cssClass="textBox" tabindex="44"/>
                </td>
                <td bgcolor="FBFBFB">
                    <html:input path="creditLimitMonthly" maxlength="50" width="115%" cssClass="textBox" tabindex="44"/>
                </td>
                <td bgcolor="FBFBFB" align="center">
                    <html:checkbox path="monthlyCheck"/>
                </td>
            </tr>
            <tr class="blinklform-feilds">
                <td bgcolor="FBFBFB">
                    <html:input path="yearly" maxlength="50" width="115%" cssClass="textBox" tabindex="44"
                                readonly="true"/>
                </td>
                <td bgcolor="FBFBFB">
                    <html:input path="debitLimitYearly" maxlength="50" width="115%" cssClass="textBox" tabindex="44"/>
                </td>
                <td bgcolor="FBFBFB">
                    <html:input path="creditLimitYearly" maxlength="50" width="115%" cssClass="textBox" tabindex="44"/>
                </td>
                <td bgcolor="FBFBFB" align="center">
                    <html:checkbox path="yearlyCheck"/>
                </td>
            </tr>
            <tr class="blinklform-feilds">
                <td bgcolor="FBFBFB">
                    <html:input path="maximumBalance" maxlength="50" width="115%" cssClass="textBox" tabindex="44"
                                readonly="true"/>
                </td>
                <td bgcolor="FBFBFB">
                    <html:input path="" maxlength="50" width="115%" cssClass="textBox" tabindex="44" readonly="true"/>
                </td>
                <td bgcolor="FBFBFB">
                    <html:input path="maximumCreditLimit" maxlength="50" width="115%" cssClass="textBox" tabindex="44"/>
                </td>
                <td bgcolor="FBFBFB" align="center">
                    <html:checkbox path="maximumBalanceCheck"/>
                </td>
            </tr>

        </table>

    </div>


    <table>
        <tr>
            <td width="100%" align="left" bgcolor="FBFBFB" colspan="4">
                <c:choose>
                    <c:when test="${mfsAccountModel.segmentId == 10319}">
                        <authz:authorize ifAnyGranted="<%=updatePermission%>">
                            <input type="submit" class="button" value="Update"
                                   tabindex="42"/>&nbsp;
                        </authz:authorize>
                        <input type="button" class="button" value="Cancel" tabindex="43"
                               onclick="javascript: window.location='p_minorsearchuserinfo.html?actionId=<%=PortalConstants.ACTION_RETRIEVE%>'"/>
                    </c:when>
                    <c:when test="${not empty param.appUserId or not empty mfsAccountModel.appUserId or mfsAccountModel != 10319}">
                        <authz:authorize ifAnyGranted="<%=updatePermission%>">
                            <input type="submit" class="button" value="Update"
                                   tabindex="42"/>&nbsp;
                        </authz:authorize>
                        <input type="button" class="button" value="Cancel" tabindex="43"
                               onclick="javascript: window.location='p_pgsearchuserinfo.html?actionId=<%=PortalConstants.ACTION_RETRIEVE%>'"/>
                    </c:when>
                    <c:otherwise>
                        <authz:authorize ifAnyGranted="<%=createPermission%>">
                            <input type="submit" class="button" value="Create Customer Account" tabindex="44"/>
                            <input type="button" class="button" value="Cancel" tabindex="45"
                                   onclick="javascript: window.location='home.html'"/>
                        </authz:authorize>
                        <authz:authorize ifNotGranted="<%=createPermission%>">
                            <input type="button" class="button"
                                   value="Create Customer Account" tabindex="-1"
                                   disabled="disabled"/>
                            <input type="button" class="button" value="Cancel"
                                   onclick="javascript: window.location='home.html'"
                                   tabindex="46"/>
                        </authz:authorize>
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </table>
</html:form>
<ajax:updateField parser="new ResponseXmlParser()" source="taxRegimeId" eventType="change" action="taxRegimeId"
                  target="fed" baseUrl="${contextPath}/p_loadfed.html"
                  parameters="taxRegimeId={taxRegimeId}"></ajax:updateField>
<form action="${contextPath}/p_pgsearchuserinfo.html" method="post"
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
           name="nic"/>
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
    <c:when test="${not empty param.appUserId or not empty mfsAccountModel.appUserId}">
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

        if(${mfsAccountModel.segmentId != 10319}) {
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
            return false;
        }
    }

    function onFormSubmit(theForm) {

        if(${mfsAccountModel.segmentId == 10319}){
            doRequired(theForm.dob, 'Date of Birth')
            && doRequired(theForm.nic, 'CNIC #')
            && IsNumeric(theForm.nic.value, 'CNIC #')
            && IsNumeric(theForm.fatherCnic.value, 'Father Cnic')
            && IsNumeric(theForm.motherCnic.value, 'Mother Cnic')
            && isValidMinLength(theForm.nic, 'CNIC #', 13)
            && doRequired(theForm.cnicSeen, 'Original CNIC Seen')
            && doRequiredGroup('gender', 'Gender')
            && doRequired(theForm.city, 'City')
            && doRequired(theForm.customerAccountTypeId, 'Account Type')
            && doRequired(theForm.name, 'Account Title')
            && doRequired(theForm.mobileNo, 'Mobile No.')
            && isValidMinLength(theForm.mobileNo, 'Mobile No.', 11)
            && isValidMobileNo(theForm.mobileNo)
            && doRequired(theForm.nicExpiryDate, 'CNIC Expiry')
            && doRequired(theForm.productCatalogId, 'Product Catalog')
            && doRequired(theForm.birthPlace, 'Place of Birth')
            // && doRequired( theForm.motherMaidenName, 'Mother\'s Maiden Name' )
            && doRequired(theForm.presentAddress, 'Mailing Address')
            && doRequired(theForm.taxRegimeId, 'Tax Regime')
            && doRequired(theForm.fed, 'FED (%age)')
            && isWithinLimit(theForm.fed, 'FED (%age)', 100)
            && validateDecimal(theForm.fed, 'FED (%age)')
        }
        else {

        var accTypeId = document.getElementById('customerAccountTypeId').value;
        var updateCustomerFlag = false;
        if (${not empty param.appUserId && not empty mfsAccountModel.appUserId}) {
            updateCustomerFlag = true;

        }
            if (updateCustomerFlag == false) {

                //New Customer Case
                //Level 0 account opening validations
                if ((accTypeId == 1)

                    && doRequired(theForm.dob, 'Date of Birth')
                    && doRequired(theForm.nic, 'CNIC #')
                    && IsNumeric(theForm.nic.value, 'CNIC #')
                    && IsNumeric(theForm.fatherCnic.value, 'Father Cnic')
                    && IsNumeric(theForm.motherCnic.value, 'Mother Cnic')
                    && isValidMinLength(theForm.nic, 'CNIC #', 13)
                    && doRequired(theForm.cnicSeen, 'Original CNIC Seen')
                    && doRequiredGroup('gender', 'Gender')
                    && doRequired(theForm.city, 'City')
                    //**************************************************************
                    && doRequired(theForm.customerPic, 'Customer Picture')
                    && isValidPicOrPdf(theForm.customerPic, 'Customer Picture')
                    && isValidPicOrPdf(theForm.signPic, 'Signature Picture')
                    && doRequired(theForm.tncPic, 'T&C Picture')
                    && isValidPicOrPdf(theForm.tncPic, 'T&C Picture')
                    && doRequired(theForm.cnicFrontPic, 'CNIC Front Picture')
                    && isValidPicOrPdf(theForm.cnicFrontPic, 'CNIC Front Picture')
                    && isValidPicOrPdf(theForm.cnicBackPic, 'CNIC Back Picture')
                    //**************************************************************
                    && doRequired(theForm.customerAccountTypeId, 'Account Type')
                    && doRequired(theForm.name, 'Account Title')
                    && doRequired(theForm.mobileNo, 'Mobile No.')
                    && isValidMinLength(theForm.mobileNo, 'Mobile No.', 11)
                    && isValidMobileNo(theForm.mobileNo)
                    && doRequired(theForm.nicExpiryDate, 'CNIC Expiry')
                    && doRequired(theForm.productCatalogId, 'Product Catalog')
                    && doRequired(theForm.birthPlace, 'Place of Birth')
                    // && doRequired( theForm.motherMaidenName, 'Mother\'s Maiden Name' )
                    && doRequired(theForm.presentAddress, 'Mailing Address')
                    && doRequired(theForm.taxRegimeId, 'Tax Regime')
                    && doRequired(theForm.fed, 'FED (%age)')
                    && isWithinLimit(theForm.fed, 'FED (%age)', 100)
                    && validateDecimal(theForm.fed, 'FED (%age)')
                ) {

                } else {

                    if (accTypeId == 1) {

                        return false;
                    } else {
                        //Level 1 Account opening validations
                        if ((accTypeId == 2) && doRequired(theForm.customerAccountTypeId, 'Account Type')
                            && doRequired(theForm.name, 'Account Title')
                            && doRequired(theForm.mobileNo, 'Mobile No.')
                            && isValidMinLength(theForm.mobileNo, 'Mobile No.', 11)
                            && isValidMobileNo(theForm.mobileNo)
                            && doRequired(theForm.nic, 'CNIC #')
                            && IsNumeric(theForm.nic.value, 'CNIC #')
                            && IsNumeric(theForm.fatherCnic.value, 'Father Cnic')
                            && IsNumeric(theForm.motherCnic.value, 'Mother Cnic')
                            && isValidMinLength(theForm.nic, 'CNIC #', 13)
                            && doRequired(theForm.nicExpiryDate, 'CNIC Expiry')
                            && doRequired(theForm.productCatalogId, 'Product Catalog')
                            && doRequired(theForm.dob, 'Date of Birth')
                            && doRequired(theForm.birthPlace, 'Place of Birth')
                            // && doRequired( theForm.motherMaidenName, 'Mother\'s Maiden Name' )
                            && doRequired(theForm.presentAddress, 'Mailing Address')
                            && doRequired(theForm.taxRegimeId, 'Tax Regime')
                            && doRequired(theForm.fed, 'FED (%age)')
                            && isWithinLimit(theForm.fed, 'FED (%age)', 100)
                            && validateDecimal(theForm.fed, 'FED (%age)')
                        ) {

                        } else {

                            return false;
                        }

                    }


                }
            } else {

                //Existing Customer/Update Case...
                if ((updateCustomerFlag == true) && (accTypeId == 1)
                    && doRequired(theForm.dob, 'Date of Birth')
                    && doRequired(theForm.nic, 'CNIC #')
                    && IsNumeric(theForm.nic.value, 'CNIC #')
                    && IsNumeric(theForm.fatherCnic.value, 'Father Cnic')
                    && IsNumeric(theForm.motherCnic.value, 'Mother Cnic')
                    && isValidMinLength(theForm.nic, 'CNIC #', 13)
                    && doRequired(theForm.cnicSeen, 'Original CNIC Seen')
                    && doRequiredGroup('gender', 'Gender')
                    // && doRequired(theForm.fatherHusbandName, 'Father/Husband Name')
                    && doRequired(theForm.city, 'City')
                    //**************************************************************
                    //&& doRequired( theForm.customerPic, 'Customer Picture' )
                    && isValidPicOrPdf(theForm.customerPic, 'Customer Picture')
                    && isValidPicOrPdf(theForm.signPic, 'Signature Picture')
                    //&& doRequired( theForm.tncPic, 'T&C Picture' )
                    && isValidPicOrPdf(theForm.tncPic, 'T&C Picture')
                    //&& doRequired( theForm.cnicFrontPic, 'CNIC Front Picture')
                    && isValidPicOrPdf(theForm.cnicFrontPic, 'CNIC Front Picture')
                    && isValidPicOrPdf(theForm.cnicBackPic, 'CNIC Back Picture')
                    //**************************************************************
                    && doRequired(theForm.customerAccountTypeId, 'Account Type')
                    && doRequired(theForm.name, 'Account Title')
                    && doRequired(theForm.mobileNo, 'Mobile No.')
                    && isValidMinLength(theForm.mobileNo, 'Mobile No.', 11)
                    && isValidMobileNo(theForm.mobileNo)
                    && doRequired(theForm.nicExpiryDate, 'CNIC Expiry')
                    && doRequired(theForm.productCatalogId, 'Product Catalog')
                    && doRequired(theForm.birthPlace, 'Place of Birth')
                    // && doRequired( theForm.motherMaidenName, 'Mother\'s Maiden Name' )
                    && doRequired(theForm.presentAddress, 'Mailing Address')
                    && doRequired(theForm.taxRegimeId, 'Tax Regime')
                    && doRequired(theForm.fed, 'FED (%age)')
                    && isWithinLimit(theForm.fed, 'FED (%age)', 100)
                    && validateDecimal(theForm.fed, 'FED (%age)')
                ) {
                } else {

                    if (accTypeId == 1) {
                        return false;
                    } else {

                        //Level 1 / HRA Account opening validations
                        if ((accTypeId == 2 || accTypeId == 4 || accTypeId == 53) && doRequired(theForm.customerAccountTypeId, 'Account Type')
                            && doRequired(theForm.name, 'Account Title')
                            && doRequired(theForm.mobileNo, 'Mobile No.')
                            && isValidMinLength(theForm.mobileNo, 'Mobile No.', 11)
                            && isValidMobileNo(theForm.mobileNo)
                            && doRequired(theForm.nic, 'CNIC #')
                            && IsNumeric(theForm.nic.value, 'CNIC #')
                            && IsNumeric(theForm.fatherCnic.value, 'Father Cnic')
                            && IsNumeric(theForm.motherCnic.value, 'Mother Cnic')
                            && isValidMinLength(theForm.nic, 'CNIC #', 13)
                            && doRequired(theForm.nicExpiryDate, 'CNIC Expiry')
                            && doRequired(theForm.productCatalogId, 'Product Catalog')
                            && doRequired(theForm.dob, 'Date of Birth')
                            && doRequired(theForm.birthPlace, 'Place of Birth')
                            // && doRequired( theForm.motherMaidenName, 'Mother\'s Maiden Name' )
                            && doRequired(theForm.presentAddress, 'Mailing Address')
                            && doRequired(theForm.taxRegimeId, 'Tax Regime')
                            && doRequired(theForm.fed, 'FED (%age)')
                            && isWithinLimit(theForm.fed, 'FED (%age)', 100)
                            && validateDecimal(theForm.fed, 'FED (%age)')
                        ) {

                        } else {
                            return false;
                        }

                    }


                }


            }
        }

        if (theForm.initialDeposit != undefined) {
            if (!(doRequired(theForm.initialDeposit, 'Intial Deposit') && doRequired(theForm.agentPIN, 'Agent PIN'))) {
                return false;
            }
        } else {
            if ( /* doRequired( theForm.birthPlace, 'Place of Birth' )
					&& doRequired( theForm.motherMaidenName, 'Mother\'s Maiden Name' )
					&& doRequired( theForm.presentAddress, 'Mailing Address' )
					&& doRequired (theForm.taxRegimeId, 'Tax Regime')
					&& doRequired (theForm.fed, 'FED (%age)')
					&& isWithinLimit(theForm.fed, 'FED (%age)', 100) s
					&& validateDecimal(theForm.fed, 'FED (%age)') */
                doRequiredGroup('verisys', 'Verisys Done')) {
                if (theForm.customerAccountTypeId.value == 2) {
                    if (isValidEmail(theForm.email, 'Applicant Email')) {

                    } else {

                        return false;
                    }
                }

            } else {
                return false;
            }
        }

        if (theForm.name.value != '' && theForm.customerPicByte == undefined) {
            if (theForm.customerAccountTypeId.value == 2) {
                /* if(!doRequired( theForm.level1FormPic, 'Level 1 Form Picture'))
                {
                    return false;
                }
                if(!isValidPicOrPdf(theForm.cnicBackPic, 'Level 1 Form Picture') ){
                    return false;
                } */
            }
        } else {
        }

        var appUserId = '<%= request.getParameter("appUserId") %>';

        if (document.getElementById('customerAccountTypeId'))
            theForm.accounttypeName.value = theForm.customerAccountTypeId.options[theForm.customerAccountTypeId.selectedIndex].innerHTML;
        if (document.getElementById('segmentId'))
            theForm.segmentNameStr.value = theForm.segmentId.options[theForm.segmentId.selectedIndex].innerHTML;
        /*  if(document.getElementById('fundsSourceId'))
         theForm.fundSourceStr.value = theForm.fundsSourceId.options[theForm.fundsSourceId.selectedIndex].innerHTML;
         if(document.getElementById('city'))
       theForm.permanentHomeAddCityName.value = theForm.city.options[theForm.city.selectedIndex].innerHTML; */
        if (document.getElementById('taxRegimeId'))
            theForm.taxRegimeName.value = theForm.taxRegimeId.options[theForm.taxRegimeId.selectedIndex].innerHTML;

        if (appUserId != "null" && appUserId != '') {
            if (${appUserTypeId == 3} &&
            ${mfsAccountModel.registrationStateId==2} )
            {
                theForm.regStateName.value = 'Request Received';
            }
        else
            if (${appUserTypeId == 3} &&
            ${mfsAccountModel.registrationStateId==3} )
            {
                theForm.regStateName.value = 'Approved';
            }
        else
            if (${appUserTypeId == 53} &&
            ${mfsAccountModel.registrationStateId==53} )
            {
                theForm.regStateName.value = 'Request Received';
            }
        else
            if (${appUserTypeId == 3} &&
            ${mfsAccountModel.registrationStateId==4} )
            theForm.regStateName.value = 'Discrepent';
        else
            if (${appUserTypeId == 3} &&
            ${mfsAccountModel.registrationStateId==5} )
            theForm.regStateName.value = 'Decline';
        else
            if (${appUserTypeId == 3} &&
            ${mfsAccountModel.registrationStateId==6} )
            theForm.regStateName.value = 'Rejected';
        else
            if (${mfsAccountModel.registrationStateId==3})
                theForm.regStateName.value = 'Approved';
            else if (${mfsAccountModel.registrationStateId==5})
                theForm.regStateName.value = 'Decline';
            else if (${mfsAccountModel.registrationStateId==6})
                theForm.regStateName.value = 'Rejected';
            else
                theForm.regStateName.value = theForm.registrationStateId.options[theForm.registrationStateId.selectedIndex].innerHTML;
        } else {
        }


        if (appUserId != "null" && appUserId != '') {
            if (doRequired(theForm.registrationStateId, 'Registration State')) {

            } else {
                return false;
            }
        } else {
        }

        if ((accTypeId == 1) && (!document.getElementById('cnicSeen1').checked)) {
            alert("If you have seen Original CNIC, then kindly select check box.");
            return false;
        }

        if (!isFormDataChanged()) {
            return false;
        }

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

    //This method is a copy of code from submitForm() method
    function isValidFormForPrinting() {
        var theForm = document.forms.mfsAccountForm;
        if (doRequired(theForm.name, 'Name')
            && doRequired(theForm.nic, 'CNIC #')
            && doRequired(theForm.presentAddress, 'Mailing Address')
            && doRequired(theForm.dob, 'Date of Birth')
            && doRequired(theForm.presentAddCityId, 'City/District')
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
                document.getElementById('parentCnicPicDiscrepant1').checked = '';
                document.getElementById('bformPicDiscrepant1').checked = '';
                document.getElementById('tncPicDiscrepant1').checked = '';
                document.getElementById('signPicDiscrepant1').checked = '';
                document.getElementById('cnicFrontPicDiscrepant1').checked = '';
                document.getElementById('cnicBackPicDiscrepant1').checked = '';
                document.getElementById('parentCnicBackPicDiscrepan1').checked = '';
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
        document.getElementById('parentCnicPicDiscrepant2').value = document.getElementById('parentCnicPicDiscrepant1').value;
        document.getElementById('bformPicDiscrepant2').value = document.getElementById('bformPicDiscrepant1').value;
        document.getElementById('tncPicDiscrepant2').value = document.getElementById('tncPicDiscrepant1').value;
        document.getElementById('signPicDiscrepant2').value = document.getElementById('signPicDiscrepant1').value;
        document.getElementById('cnicFrontPicDiscrepant2').value = document.getElementById('cnicFrontPicDiscrepant1').value;
        document.getElementById('cnicBackPicDiscrepant2').value = document.getElementById('cnicBackPicDiscrepant1').value;
        document.getElementById('parentCnicBackPicDiscrepant2').value = document.getElementById('parentCnicBackPicDiscrepant1').value;

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

    function isValidEmail(field, label) {
        if (field.value != '' && field.value.length != 0) {
            if (!isEmail(field)) {
                alert(label + ' is not valid Email.');
                return false;
            }
        }
        return true;
    }

    Calendar.setup(
        {
            inputField: "dob", // id of the input field
            ifFormat: "%d/%m/%Y",      // the date format
            button: "sDate",    // id of the button
            showsTime: false
        }
    );
    Calendar.setup(
        {
            inputField: "nicExpiryDate", // id of the input field
            ifFormat: "%d/%m/%Y",      // the date format
            button: "nicDate",    // id of the button
            showsTime: false
        }
    );
    Calendar.setup(
        {
            inputField: "cnicIssuanceDate", // id of the input field
            ifFormat: "%d/%m/%Y",      // the date format
            button: "nicIssueDate",    // id of the button
            showsTime: false
        }
    );

    jq("#fundsSource").multiselect({
        noneSelectedText: "[Select]",
        minWidth: "210px"
    });
    window.onload = acTypeInfo(document.forms.mfsAccountForm.customerAccountTypeId);
    window.onload = regStateChange(document.forms.mfsAccountForm.registrationStateId);
</script>
</body>

</html>
