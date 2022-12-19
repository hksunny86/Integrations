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

    <meta name="title" content="Update Customer Name "/>
    <%@include file="/common/ajax.jsp" %>

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
<spring:bind path="updateCustomerNameVo.*">
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


<html:form name="updateCustomerNameForm"
           commandName="updateCustomerNameVo"
           onsubmit="return checkFormSubmit(this)"
           action="p_updateCustomerNameDetail.html"
           enctype="multipart/form-data" method="POST">
    <c:if test="${not empty appUserId}">
        <input type="hidden" name="isUpdate" id="isUpdate" value="true"/>
        <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
               value="<%=PortalConstants.ACTION_UPDATE%>"/>
    </c:if>
    <c:if test="${empty appUserId}">
        <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
               value="<%=PortalConstants.ACTION_CREATE%>"/>
    </c:if>

    <c:if test="${not empty appUserId }"><input type="hidden" name="appUserId" value="${appUserId}"/></c:if>
    <c:if test="${not empty appUserId}"><input type="hidden" name="appUserId" value="${appUserId}"/></c:if>



    <input type="hidden" name="actionAuthorizationId" value="${param.authId}"/>
    <input type="hidden" name="updateCustomerNameId" id="updateCustomerNameId" value="${updateCustomerNameVo.updateCustomerNameId}" />
    <input type="hidden" name="resubmitRequest" value="${isReSubmit}"/>
    <input type="hidden" name="usecaseId" value="<%=PortalConstants.UPDATE_CUSTOMER_NAME_USECASE_ID%>"/>

    <table width="950px">
        <tr>
            <td align="right" class="formText" bgcolor="FBFBFB"><span style="color: #FF0000">*</span>Mobile No:</td>
            <td align="left"><b> <html:input path="mobileNo" cssClass="textBox" tabindex="15" maxlength="11" readonly="true"/></b>
            </td>
            <td align="right" class="formText" bgcolor="FBFBFB"><span style="color: #FF0000">*</span>Cnic:</td>
            <td align="left"><b> <html:input path="cnic" cssClass="textBox" tabindex="15" maxlength="13" readonly="true"/></b>
            </td>
        </tr>
        <tr>
            <td align="right" class="formText" bgcolor="FBFBFB"><span style="color: #FF0000">*</span>First Name:  </td>
            <td align="left"><b> <html:input path="firstName"  cssClass="textBox" tabindex="15" maxlength="50" readonly="true"/></b>
            </td>
            <td align="right" class="formText" bgcolor="FBFBFB"><span style="color: #FF0000">*</span>Last Name:</td>
            <td align="left"><b> <html:input path="lastName" cssClass="textBox" tabindex="15" maxlength="50" readonly="true"/></b>
            </td>
        </tr>
        <tr>
            <td align="right" class="formText" bgcolor="FBFBFB"><span style="color: #FF0000">*</span>Nadra Name:</td>
            <td align="left"><b> <html:input path="nadraName"  cssClass="textBox" tabindex="15" maxlength="11" readonly="true"  /></b>
            </td>
        </tr>

    </table>
    <table>
        <tr>
            <td width="100%" align="left" bgcolor="FBFBFB" colspan="4">
                <c:choose>
                    <c:when test="${not empty appUserId}">
                        <authz:authorize ifAnyGranted="<%=updatePermission%>">
                            <input type="submit" class="button" value="Update" onclick="return checkFormSubmit(this)"
                                   tabindex="42"/>&nbsp;
                        </authz:authorize>
                        <input type="button" class="button" value="Cancel" tabindex="43"
                               onclick="javascript: window.location='p_updateCustomerNameInfo.html'"/>
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
<script language="javascript" type="text/javascript">


    function checkFormSubmit() {
        if (
            document.getElementById("mobileNo").value == "" || document.getElementById("mobileNo").value == null
        ) {
            alert("Mobile Number is null");
            return false;
        }
        if (document.getElementById("cnic").value == null || document.getElementById("cnic").value == "") {
            alert("Cnic is Required");
            return false;
        }
        if (document.getElementById("firstName").value == null || document.getElementById("firstName").value == "") {
            alert("FirsT Name is Required");
            return false;
        }
        if (document.getElementById("lastName").value == null || document.getElementById("lastName").value == "") {
            alert("Last Name is Required");
            return false;
        }
    }
</script>
</body>
</html>