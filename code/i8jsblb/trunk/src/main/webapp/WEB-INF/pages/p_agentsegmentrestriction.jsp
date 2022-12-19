<%@include file="/common/taglibs.jsp" %>
<%@ page import='com.inov8.microbank.common.util.*' %>
<%@ page import='com.inov8.microbank.common.util.PortalConstants' %>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils" %>
<%@ page import='com.inov8.microbank.common.util.FinancialInstitutionConstants' %>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %>
</c:set>

<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %>
</c:set>
<c:set var="veriflyFinancialInstitution"><%=FinancialInstitutionConstants.VERIFLY_FINANCIAL_INSTITUTION %>
</c:set>

<html>
<head>
    <meta name="decorator" content="decorator">
    <script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
    <script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
    <link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
    <script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
    <link rel="stylesheet"
          href="${contextPath}/styles/extremecomponents.css"
          type="text/css">
    <%@include file="/common/ajax.jsp" %>
    <meta name="title" content="Agent Segment Tagging" id="<%=ReportIDTitleEnum.TransactionDetailReport.getId()%>"/>
    <script language="javascript" type="text/javascript">
        var jq = $.noConflict();
        var serverDate = "<%=PortalDateUtils.getServerDate()%>";
        var username = "<%=UserUtils.getCurrentUser().getUsername()%>";
        var appUserId = "<%=UserUtils.getCurrentUser().getAppUserId()%>";
        var email = "<%=UserUtils.getCurrentUser().getEmail()%>";

        function error(request) {
            alert("An unknown error has occured. Please contact with the administrator for more details");
        }
    </script>

    <%@include file="/WEB-INF/pages/export_zip.jsp" %>
    <%-- <script type="text/javascript" src="${contextPath}/scripts/exportzip.js"></script> --%>
    <%
        String detailBtnPermission = PortalConstants.ADMIN_GP_READ;
    %>


</head>
<body bgcolor="#ffffff">

<spring:bind path="agentSegmentRestriction.*">
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
            <c:out value="${msg}" escapeXml="false"/>
            <br/>
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session"/>
</c:if>

<%--<div id="successMsg" class="infoMsg" style="display:none;"></div>--%>
<%--<div id="errorMsg" class="errorMsg" style="display:none;"></div>--%>
<%--<input id="message" value="777" type="hidden"/>--%>
<html:form name="transactionDetailI8Form"
           commandName="agentSegmentRestriction" method="post"
           action="p_agentsegmentrestriction.html" onsubmit="return onFormSubmit(this)">
    <table width="950px" border="0">
        <tr>
            <td class="formText" width="25%" align="right">
                <span style="color: #FF0000">*</span> Agent ID:
            </td>
            <%--<td align="left" width="25%">--%>
                <%--<html:input path="agentID" cssClass="textBox" tabindex="5" maxlength="50"--%>
                            <%--onkeypress="return maskNumber(this,event)"/>--%>
            <%--</td>--%>
            <td class="formText">
                <c:choose>
                    <c:when test="${not empty param.agentID or not empty agentSegmentRestriction.agentID}">
                        <html:input path="agentID" cssClass="textBox" tabindex="2"
                                    maxlength="11" readonly="true" style="background: #D3D3D3;"  onkeypress="return maskNumber(this,event)"/>
                    </c:when>
                    <c:otherwise>
                        <html:input path="agentID" cssClass="textBox" tabindex="2"
                                    maxlength="11" onkeypress="return maskNumber(this,event)"/>
                    </c:otherwise>
                </c:choose>
            </td>


            <%--<td height="16" align="right" class="formText">Active:</td>--%>
            <%--<td width="58%" align="left"><html:checkbox path="IsActive" tabindex="15"/>--%>

        </tr>

        <tr>
            <td class="formText" align="right">
                Segment:
            </td>
            <td align="left">
                <html:select path="segmentId" cssClass="textBox" tabindex="10">
                    <html:option value="">---All---</html:option>
                    <c:if test="${segmentModelList != null}">
                        <html:options items="${segmentModelList}" itemValue="segmentId" itemLabel="name"/>
                    </c:if>
                </html:select>
            </td>
            <%--<td class="formText" align="right">--%>
                <%--Product:--%>
            <%--</td>--%>
            <%--<td align="left">--%>
                <%--<html:select path="productId" cssClass="textBox" tabindex="10">--%>
                    <%--<html:option value="">---All---</html:option>--%>
                    <%--<c:if test="${productModelList != null}">--%>
                        <%--<html:options items="${productModelList}" itemValue="productId" itemLabel="name"/>--%>
                    <%--</c:if>--%>
                <%--</html:select>--%>
            <%--</td>--%>
        </tr>


        <tr>
            <%--<td class="formText" align="right">--%>
                <%--Segment:--%>
            <%--</td>--%>
            <%--<td align="left">--%>
                <%--<html:select path="segmentId" cssClass="textBox" tabindex="10">--%>
                    <%--<html:option value="">---All---</html:option>--%>
                    <%--<c:if test="${segmentModelList != null}">--%>
                        <%--<html:options items="${segmentModelList}" itemValue="segmentId" itemLabel="name"/>--%>
                    <%--</c:if>--%>
                <%--</html:select>--%>
            <%--</td>--%>
            <td class="formText" align="right">
                Product:
            </td>
            <td align="left">
                <html:select path="productId" cssClass="textBox" tabindex="10">
                    <html:option value="">---All---</html:option>
                    <c:if test="${productModelList != null}">
                        <html:options items="${productModelList}" itemValue="productId" itemLabel="name"/>
                    </c:if>
                </html:select>
            </td>
        </tr>

        <%--<tr>--%>
            <%--<td class="formText" align="right">--%>
                <%--Created On:--%>
            <%--</td>--%>
            <%--<td align="left">--%>
                <%--<html:input path="createdOn" id="createdOnFromDate" readonly="true" tabindex="-1" cssClass="textBox"--%>
                            <%--maxlength="10"/>--%>
                <%--<img id="CreatedFromDate" tabindex="7" name="popcal" align="top" style="cursor:pointer"--%>
                     <%--src="images/cal.gif" border="0"/>--%>
                <%--<img id="CreatedFromDate" tabindex="8" title="Clear Date" name="popcal"--%>
                     <%--onclick="javascript:$('createdOnFromDate').value=''" align="middle" style="cursor:pointer"--%>
                     <%--src="images/refresh.png" border="0"/>--%>
            <%--</td>--%>
            <%--<td class="formText" align="right">--%>
                <%--updated On:--%>
            <%--</td>--%>
            <%--<td align="left">--%>
                <%--<html:input path="updatedOn" id="createdOnToDate" readonly="true" tabindex="-1" cssClass="textBox"--%>
                            <%--maxlength="10"/>--%>
                <%--<img id="CreatedToDate" tabindex="9" name="popcal" align="top" style="cursor:pointer"--%>
                     <%--src="images/cal.gif" border="0"/>--%>
                <%--<img id="CreatedToDate" tabindex="10" title="Clear Date" name="popcal"--%>
                     <%--onclick="javascript:$('createdOnToDate').value=''" align="middle" style="cursor:pointer"--%>
                     <%--src="images/refresh.png" border="0"/>--%>
            <%--</td>--%>
        <%--</tr>--%>
        <tr>
            <td class="formText" align="right">
            </td>
            <td align="left">

                <input name="_save" type="submit" class="button" value="Save"/>
                <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
                       value="<%=PortalConstants.ACTION_CREATE%>"/>
                <input type="button" name="cancel" value="Cancel" class="button"
                       onclick="javascript:window.location='p_agentsegmentrestriction.html?actionId=2'"/>

            </td>

            <td class="formText" align="right">
            </td>
            <td align="left">
            </td>
        </tr>
    </table>
</html:form>


<%--<ajax:select source="supplierId" target="productId"--%>
<%--baseUrl="${contextPath}/p_refData.html"--%>
<%--parameters="supplierId={supplierId},rType=1,actionId=${retriveAction}" errorFunction="error"/>--%>


<script language="javascript" type="text/javascript">

    <%--document.forms[0].mfsId.focus();--%>
    <%--function openTransactionWindow(transactionCode)--%>
    <%--{--%>
    <%--var popupWidth = 550;--%>
    <%--var popupHeight = 350;--%>
    <%--var popupLeft = (window.screen.width - popupWidth)/2;--%>
    <%--var popupTop = (window.screen.height - popupHeight)/2;--%>
    <%--newWindow=window.open('p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode='+transactionCode,'TransactionSummary','width='+popupWidth+',height='+popupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');--%>
    <%--if(window.focus) newWindow.focus();--%>
    <%--return false;--%>
    <%--}--%>


    // Calendar.setup(
    //     {
    //         inputField: "createdOnFromDate", // id of the input field
    //         button: "CreatedFromDate",    // id of the button
    //     }
    // );
    // Calendar.setup(
    //     {
    //         inputField: "createdOnToDate", // id of the input field
    //         button: "CreatedToDate",    // id of the button
    //         isEndDate: true
    //     }
    // );

    // Calendar.setup( {inputField  : "UpdtFromStartDate",button : "fromStartDate",} );
    // Calendar.setup( {inputField  : "UpdtToStartDate",button : "toStartDate", isEndDate: true } );

    function onFormSubmit(theForm) {

        var theForm = document.forms.transactionDetailI8Form;

        if(doRequired( theForm.agentID, 'AgentId' )
            && doRequired( theForm.segmentId, 'SegmentId' )
            && doRequired( theForm.productId, 'ProductId' )
        ){

        }else {
            return false;

        }

    }

    function doRequired( field, label )
    {
        if( field.value == '' || field.value.length == 0 )
        {
            alert( label + ' is required field.' );
            field.focus();
            return false;
        }
        return true;
    }

</script>
</body>
</html>