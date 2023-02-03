<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
<%@ page import='com.inov8.microbank.common.util.PortalDateUtils'%>

<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>

<html>
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
    <meta name="title" content="Review Debit Card Requests" />
    <script language="javascript" type="text/javascript">
        var jq=$.noConflict();
        var serverDate ="<%=PortalDateUtils.getServerDate()%>";

        function openTransactionDetailWindow(transactionCode)
        {
            var action = 'p_showtransactiondetail.html?actionId='+${retriveAction}+'&transactionCodeId='+transactionCode+'&isMfs=true';
            newWindow = window.open( action , 'TransactionDetail', 'width=550,height=350,menubar=no,toolbar=no,left=150,top=150,directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
            if(window.focus) newWindow.focus();
            return false;
        }

        function error(request)
        {
            alert("An unknown error has occured. Please contact with the administrator for more details");
        }

        var childWindow;
        function closeChild()
        {
            try
            {
                if(childWindow != undefined)
                {
                    childWindow.close();
                    childWindow=undefined;
                }
            }catch(e){}
        }
    </script>

</head>
<body bgcolor="#ffffff" onunload="javascript:closeChild();">

<div id="rsp" class="ajaxMsg"></div>

<c:if test="${not empty messages}">
    <div class="infoMsg" id="successMessages">
        <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false" /><br/>
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session" />
</c:if>

<html:form name='debitCardIssuenceRequestCheckerForm' commandName="debitCardReqSearchController" method="post"
           action="searchDebitCardRequests.html" onsubmit="return validateFormData(this)" >
    <table width="100%" border="0">
        <tr>
            <td align="right" class="formText">Mobile #:</td>
            <td align="left"><html:input path="mobileNo" cssClass="textBox" maxlength="11" tabindex="3" /></td>
            <td align="right" class="formText">CNIC #:</td>
            <td align="left"><html:input path="nic" cssClass="textBox" maxlength="13" tabindex="4" /></td>
        </tr>
        <tr>
            <td class="formText" align="right">Card State:</td>
            <td align="left">
                <html:select path="cardStateId" cssClass="textBox" tabindex="6" >
                    <html:option value="">---All---</html:option>
                    <c:if test="${cardStateList != null}">
                        <html:options items="${cardStateList}" itemValue="cardStateId" itemLabel="name"/>
                    </c:if>
                </html:select>
            </td>
            <td class="formText" align="right">
                Card Status:
            </td>
            <td align="left" >
                <html:select path="cardStatusId" cssClass="textBox" tabindex="6" >
                    <html:option value="">---All---</html:option>
                    <c:if test="${cardStatusList != null}">
                        <html:options items="${cardStatusList}" itemValue="cardStatusId" itemLabel="name"/>
                    </c:if>
                </html:select>
            </td>
        </tr>
        <tr>
            <td class="formText" align="right">
                Created By:
            </td>
            <td align="left">
                <html:select path="createdByAppUserId" cssClass="textBox" tabindex="6" >
                    <html:option value="">---All---</html:option>
                    <c:if test="${bankUserAppUserModelList != null}">
                        <html:options items="${bankUserAppUserModelList}" itemValue="appUserId" itemLabel="username"/>
                    </c:if>
                </html:select>
            </td>
            <td class="formText" align="right">
                Checked By:
            </td>
            <td align="left" >
                <html:select path="updatedByAppUserId" cssClass="textBox" tabindex="6" >
                    <html:option value="">---All---</html:option>
                    <c:if test="${bankUserAppUserModelList != null}">
                        <html:options items="${bankUserAppUserModelList}" itemValue="appUserId" itemLabel="username"/>
                    </c:if>
                </html:select>
            </td>
        </tr>
        <tr>
            <td class="formText" align="right">
                Created On From Date:
            </td>
            <td align="left">
                <html:input path="createdOnStartDate" id="createdOnFromDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
                <img id="CreatedFromDate" tabindex="7" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                <img id="CreatedFromDate" tabindex="8" title="Clear Date" name="popcal" onclick="javascript:$('createdOnFromDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
            </td>
            <td class="formText" align="right">
                Created On To Date:
            </td>
            <td align="left">
                <html:input path="createdOnEndDate" id="createdOnToDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
                <img id="CreatedToDate" tabindex="9" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                <img id="CreatedToDate" tabindex="10" title="Clear Date" name="popcal" onclick="javascript:$('createdOnToDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
            </td>
        </tr>
        <%--<tr>--%>
            <%--<td class="formText" align="right">--%>
                <%--Updated On From Date:--%>
            <%--</td>--%>
            <%--<td align="left">--%>
                <%--<html:input path="updatedOn" id="updatedOnFromDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>--%>
                <%--<img id="updatedFromDate" tabindex="7" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />--%>
                <%--<img id="updatedFromDate" tabindex="8" title="Clear Date" name="popcal" onclick="javascript:$('updatedOnFromDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />--%>
            <%--</td>--%>
            <%--<td class="formText" align="right">--%>
                <%--Updated On To Date:--%>
            <%--</td>--%>
            <%--<td align="left">--%>
                <%--<html:input path="updatedOn" id="updatedOnToDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>--%>
                <%--<img id="updatedToDate" tabindex="9" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />--%>
                <%--<img id="updatedToDate" tabindex="10" title="Clear Date" name="popcal" onclick="javascript:$('updatedOnToDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />--%>
            <%--</td>--%>
        <%--</tr>--%>
        <tr>
            <td class="formText" align="right">Channel:</td>
            <td align="left" >
                <html:select path="channelId" cssClass="textBox" tabindex="6" >
                    <html:option value="">---All---</html:option>
                    <c:if test="${channelList != null}">
                        <html:options items="${channelList}" itemValue="appId" itemLabel="appName"/>
                    </c:if>
                </html:select>
            </td>
        </tr>
        <tr>
            <td class="formText" align="right">

            </td>
            <td align="left">
                <input name="_search" type="submit" class="button" value="Search" tabindex="12"/>
                <input name="reset" type="reset"
                       onclick="javascript: window.location='searchDebitCardRequests.html?actionId=${retriveAction}'"
                       class="button" value="Cancel" tabindex="13" />
            </td>
            <td class="formText" align="right">

            </td>
            <td align="left">&nbsp;</td>
        </tr>
        <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
    </table></html:form>

<ec:table items="debitCardList" var="debitCardModel"
          action="${contextPath}/searchDebitCardRequests.html"
          title="" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" filterable="false">
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
        <ec:exportXls fileName="Debit Card Requests.xls" tooltip="Export Excel" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
        <ec:exportXlsx fileName="Debit Card Requests.xlsx" tooltip="Export Excel" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
        <ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da" headerTitle="Debit Card Requests"
                      fileName="Debit Card Requests.pdf" tooltip="Export PDF" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
        <ec:exportCsv fileName="Debit Card Requests.csv" tooltip="Export CSV"></ec:exportCsv>
    </authz:authorize>

    <ec:row>
        <ec:column property="customerName" title="Customer Name" escapeAutoFormat="True" style="text-align: center"/>
        <ec:column property="cardNo" title="Debit Card No" escapeAutoFormat="true"/>
        <ec:column property="embossingName" title="Embossing Name" escapeAutoFormat="true"/>
        <ec:column property="motherName" title="Mother Name" escapeAutoFormat="true"/>
        <ec:column property="nicExpiry" title="CNIC Expiry" escapeAutoFormat="true"/>
        <ec:column property="mailingAddress" title="Mailing Address" escapeAutoFormat="true"/>
        <ec:column property="mobileNo" title="Mobile #" escapeAutoFormat="True" style="text-align: center"/>
        <ec:column property="nic" title="CNIC #" escapeAutoFormat="True" style="text-align: center"/>
        <ec:column property="cardStatus" title="Card Status" escapeAutoFormat="True" style="text-align: center"/>
        <ec:column property="cardState" title="Card State" escapeAutoFormat="True" style="text-align: center"/>
        <ec:column property="channel" title="Channel" escapeAutoFormat="true"/>
        <ec:column property="createdBy" title="Created By" escapeAutoFormat="true"/>
        <ec:column property="createdOn" cell="date" format="dd/MM/yyyy hh:mm a" title="Created On"/>
        <ec:column property="updatedBy" title="Checked By" escapeAutoFormat="true"/>
        <ec:column property="updatedOn" cell="date" format="dd/MM/yyyy hh:mm a" title="Updated On"/>
    </ec:row>
</ec:table>


<script language="javascript" type="text/javascript">

    Calendar.setup(
        {
            inputField  : "createdOnFromDate", // id of the input field
            button      : "CreatedFromDate",    // id of the button
        }
    );
    Calendar.setup(
        {
            inputField  : "createdOnToDate", // id of the input field
            button      : "CreatedToDate",    // id of the button
            isEndDate: true
        }
    );


    function openActionDetailWindow(debitCardId)
    {
        var url = null;
        var popupWidth = 800;
        var popupHeight = 550;
        var popupLeft = (window.screen.width - popupWidth)/2;
        var popupTop = (window.screen.height - popupHeight)/2;

        url = 'updateDebitCardRequests.html?debitCardId='+debitCardId;
        childWindow=window.open(url,'Action Detail Window', 'width='+popupWidth+',height='+popupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
        if(window.focus) childWindow.focus();
        return false;
    }

    function validateFormData(form){
        var isValid=validateForm(form);
        if(isValid){
            var _fDate =undefined;
            var _tDate = undefined;

            if(form.createdOnFromDate!=undefined)
            {
                _fDate = form.createdOnFromDate.value;
            }
            if(form.createdOnToDate!=undefined)
            {
                _tDate = form.createdOnToDate.value;
            }

            var startlbl = "Created On From Date";
            var endlbl = "Created On To Date";

            isValid=	isValidDateRange(_fDate,_tDate,startlbl,endlbl,serverDate);
        }
        return isValid;
    }

</script>
<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>

</body>
</html>