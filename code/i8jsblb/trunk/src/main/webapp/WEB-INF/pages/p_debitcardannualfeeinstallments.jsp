<%--
  Created by IntelliJ IDEA.
  User: abubakar.farooque
  Date: 1/16/2023
  Time: 10:08 AM
  To change this template use File | Settings | File Templates.
--%>
<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
<%@ page import='com.inov8.microbank.common.util.PortalDateUtils'%>

<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <meta name="decorator" content="decorator2">
    <script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
    <script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/date-validation.js"></script>

    <link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
    <link type="text/css" rel="stylesheet" href="styles/ajaxtags.css" />
    <link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
    <%@include file="/common/ajax.jsp"%>
    <meta name="title" content="Debit Card Annual Fee- Installments" />
    <script language="javascript" type="text/javascript">

        function error(request)
        {
            alert("An unknown error has occured. Please contact with the administrator for more details");
        }

    </script>
</head>
<body>
<div id="rsp" class="ajaxMsg"></div>
<spring:bind path="cardFeeRuleModel.*">
    <c:if test="${not empty status.errorMessages}">
        <div class="errorMsg">
            <c:forEach var="error" items="${status.errorMessages}">
                <c:out value="${error}" escapeXml="false" />
                <br/>
            </c:forEach>
        </div>
    </c:if>
</spring:bind>
<c:if test="${not empty messages}">
    <div class="infoMsg" id="successMessages">
        <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session"/>
</c:if>

<html:form  name = "cardFeeRuleModelForm" id = "cardFeeRuleModelForm" commandName="cardFeeRuleModel"
            action="p_debitcardannualfeeinstallments.html?cardFeeRuleid = ${cardFeeRuleModel.cardFeeRuleId}" method="post"
            onsubmit="return validateForm(this)">
    <table width="100%" border="0" cellpadding="0" cellspacing="1">

        <c:if test="${not empty param.cardFeeRuleId}"><input type="hidden" name="appUserId" value="${param.cardFeeRuleId}"/></c:if>

        <c:if test="${not empty param.cardFeeRuleId }"><input type="hidden" name="appUserId" value="${param.cardFeeRuleId}"/></c:if>
        <c:if test="${not empty cardFeeRuleId}"><input type="hidden" name="appUserId" value="${cardFeeRuleId}"/></c:if>

        <html:hidden path="cardFeeRuleId"/>

        <tr bgcolor="FBFBFB">
            <td colspan="2" align="center">&nbsp;</td>
        </tr>
        <tr>
            <td class="formText" width="25%" align="right">
                Fee Type:
            </td>
            <td>
                <html:select path="cardFeeTypeId" cssClass="textBox" tabindex="10" >
                    <html:option value="">---All---</html:option>
                    <c:if test="${cardFeeTypeModelList != null}">
                        <html:options items="${cardFeeTypeModelList}" itemValue="cardFeeTypeId" itemLabel="name"/>
                    </c:if>
                </html:select>
            </td>
        </tr>
        <tr>
            <td class="formText" align="right">
                Segment:
            </td>
            <td align="left">
                <html:select path="segmentId" cssClass="textBox" tabindex="10" >
                    <html:option value="">---All---</html:option>
                    <c:if test="${segmentModelList != null}">
                        <html:options items="${segmentModelList}" itemValue="segmentId" itemLabel="name"/>
                    </c:if>
                </html:select>
            </td>
        </tr>
        <tr>
            <td class="formText" align="right">
                Installment Plan:
            </td>
            <td align="left">
                <html:select path="installmentPlan" cssClass="textBox" tabindex="10" >
                    <html:option value="">---All---</html:option>
                    <html:option value="QUARTERLY">Quarterly</html:option>
                    <html:option value="BI-ANNUAL">Bi-Annual</html:option>
                    <html:option value="ANNUAL">Annual</html:option>
                </html:select>
            </td>
        </tr>
        <tr>
            <td class="formText" align="right"><span style="color: #FF0000">*</span>No of Installments:</td>
            <td align="left" ><html:input tabindex="2" maxlength="11" id="noOfInstallments" path="noOfInstallments" cssClass="textBox"
                                          onkeypress="return maskNumber(this,event)"/></td>
        </tr>

        <tr bgcolor="FBFBFB">
            <td colspan="2" align="center">&nbsp;</td>
        </tr>
        <tr>
            <td align="center" colspan="2">
                <input name="_save" id="_save" type="submit" class="button"  value=" Save " tabindex="11" /> &nbsp;
                <input name="reset" type="reset" onclick="javascript: window.location='p_debitcardannualfeeinstallments.html?actionId=${retriveAction}'"
                       class="button" value="Cancel" tabindex="12" />
            </td>
        </tr>
        <tr bgcolor="FBFBFB">
            <td colspan="2" align="center">&nbsp;</td>
        </tr>
    </table>
</html:form>

<script language="javascript" type="text/javascript">
    function validateForm(form)
    {
        var _noOfInstallments = form.noOfInstallments.value;

        if(_noOfInstallments == ""){
            alert("One should be selected from Max No. of Transactions and Max Amount of Transactions")
            return false;
        }
    }

</script>

</body>
</html>
