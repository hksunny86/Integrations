<%--
  Created by IntelliJ IDEA.
  User: Abubakar Farooque
  Date: 2/19/2020
  Time: 10:35 AM
  To change this template use File | Settings | File Templates.
--%>
<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
<%@ page import='com.inov8.microbank.common.util.PortalDateUtils'%>

<c:set var="retriveAction"><%=PortalConstants.ACTION_CREATE %></c:set>
<head>
    <meta name="decorator" content="decorator">
    <script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
    <script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
    <link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
    <link type="text/css" rel="stylesheet" href="styles/ajaxtags.css" />
    <link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
    <meta name="title" content="AirTime Top-Up" />
    <%@include file="/common/ajax.jsp"%>

    <%@include file="/WEB-INF/pages/export_zip.jsp"%>

</head>

<html>
<body bgcolor="#ffffff" >
<html:form name="addTopUpRates" commandName="conversionRateModel" method="POST"
           action="addTopUpRates.html" onsubmit="return validateAirTimeForm()" >
    <table width="100%" border="0" cellpadding="0" cellspacing="1">
        <tr>
            <td class="formText" width="25%" align="right" bgcolor="F3F3F3"><span style="color: #FF0000">*</span>
                Dollar Conversion Rate:
            </td>
            <td align="left" width="25%" >
                <html:input path="dollarRate" id="rate" cssClass="textBox" maxlength="50" tabindex="1" type="number" step=".01" onkeypress="return maskNumber(this,event)"/>
            </td>
        </tr>
        <tr>
            <td class="formText" align="right" bgcolor="F3F3F3"><span style="color: #FF0000">*</span>Top-up Rate:</td>
            <td align="left" ><html:input tabindex="2" maxlength="11" id="topUpRate" path="rate" type="number" step=".01" cssClass="textBox"/></td>
        </tr>
        <tr>
            <td class="formText" align="right"></td>
            <td align="left">
                <input name="_update" type="submit" class="button" value="Save" tabindex="12" />


                <input name="reset" type="reset"
                       onclick="javascript: window.location='addTopUpRates.html?actionId=${retriveAction}'"
                       class="button" value="Cancel" tabindex="13" />
            </td>
            <td align="right">&nbsp;</td>
        </tr>
        <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_CREATE%>">
    </table>
</html:form>

<script type="text/javascript">
    function validateAirTimeForm()
    {
        //alert("alert notification");
        var x = document.getElementById("rate").value;
        var y = document.getElementById("topUpRate").value;
        // alert(y);
        if (x <= 0) {
            alert("Dollar Rate must be Greater/Equal One.");
            return false;
        }
        if(y <= 0)
        {
            alert("Top-Rate must be Greater/Equal One.");
            return false;
        }
        return true;
    }
</script>
</body>
</html>
