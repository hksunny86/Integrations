<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.*'%>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<%@ page import='com.inov8.microbank.common.util.FinancialInstitutionConstants'%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>

<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<c:set var="veriflyFinancialInstitution"><%=FinancialInstitutionConstants.VERIFLY_FINANCIAL_INSTITUTION %></c:set>

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
    <%@include file="/common/ajax.jsp"%>
    <meta name="title" content="BISP Transaction Details" id="<%=ReportIDTitleEnum.TransactionDetailReport.getId()%>" />
    <script language="javascript" type="text/javascript">
        var jq=$.noConflict();
        var serverDate ="<%=PortalDateUtils.getServerDate()%>";
        var username = "<%=UserUtils.getCurrentUser().getUsername()%>";
        var appUserId= "<%=UserUtils.getCurrentUser().getAppUserId()%>";
        var email= "<%=UserUtils.getCurrentUser().getEmail()%>";

        function openTransactionDetailWindow(transactionCode)
        {
            var popupWidth = 550;
            var popupHeight = 350;
            var popupLeft = (window.screen.width - popupWidth)/2;
            var popupTop = (window.screen.height - popupHeight)/2;
            var action = 'p_showtransactiondetail.html?actionId='+${retriveAction}+'&transactionCodeId='+transactionCode+'&isMfs=true';
            newWindow = window.open( action , 'TransactionDetail','width='+popupWidth+',height='+popupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
            if(window.focus) newWindow.focus();
            return false;
        }

        function error(request)
        {
            alert("An unknown error has occured. Please contact with the administrator for more details");
        }
        function initProgress()
        {
            if(document.getElementById('ajaxMsgDiv')){
                document.getElementById('ajaxMsgDiv').style.display = "none";
            }
            if (confirm('If Transaction information is verified then press OK to continue')==true)
            {
                return true;
            }
            else

            {
                $('errorMsg').innerHTML = "";
                $('successMsg').innerHTML = "";
                Element.hide('successMsg');
                Element.hide('errorMsg');
                return false;
            }
        }

        var isErrorOccured = false;
        function resetProgress()
        {
            if(!isErrorOccured)
            {
                // clear error box
                $('errorMsg').innerHTML = "";
                Element.hide('errorMsg');
                // display success message

                Element.show('successMsg');
            }
            isErrorOccured = false;
            window.location.reload();
        }
    </script>

    <%@include file="/WEB-INF/pages/export_zip.jsp"%>
    <%-- <script type="text/javascript" src="${contextPath}/scripts/exportzip.js"></script> --%>
    <%
        String detailBtnPermission = PortalConstants.ADMIN_GP_READ;
    %>

</head>
<body bgcolor="#ffffff">

<div id="rsp" class="ajaxMsg"></div>
<div id="successMsg" class ="infoMsg" style="display:none;"></div>
<div id="errorMsg" class ="errorMsg" style="display:none;"></div>
<c:if test="${not empty sessionScope.ajaxMessageToDisplay}" >
    <div id="ajaxMsgDiv" class ="infoMsg">
        <c:out value="${sessionScope.ajaxMessageToDisplay}" /><c:remove var="ajaxMessageToDisplay" scope="session" />
    </div>
</c:if>

<c:if test="${not empty messages}">
    <div class="infoMsg" id="successMessages">
        <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false" /><br/>
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session" />
</c:if>
<html:form name="transactionDetailI8Form"
           commandName="bispExtendedTransactionDetailModel" method="post"
           action="p_bisptransactiondetailreport.html" onsubmit="return validateForm(this)" >
    <table width="950px" border="0">
        <tr>
            <td class="formText" width="25%" align="right">
                Sender Agent ID:
            </td>
            <td align="left" width="25%" >
                <html:input path="mfsId" id="mfsId" cssClass="textBox" maxlength="50" tabindex="1" onkeypress="return maskNumber(this,event)"/>
            </td>
            <td class="formText" align="right" width="25%">&nbsp;</td>
            <td align="left">&nbsp;</td>
        </tr>
        <tr>
            <td class="formText" width="25%" align="right">
                Sender Agent CNIC:
            </td>
            <td align="left" width="25%" >
                <html:input path="senderCnic" id="senderCnic" cssClass="textBox" maxlength="13" tabindex="1" onkeypress="return maskNumber(this,event)"/>
            </td>
            <td class="formText" align="right" width="25%">
                Sender Agent Mobile No:
            </td>
            <td align="left">
                <html:input path="saleMobileNo" id="saleMobileNo" cssClass="textBox" maxlength="11" tabindex="2" onkeypress="return maskNumber(this,event)"/>
            </td>
        </tr>
        <tr>
            <td class="formText" width="25%" align="right">
                Recipient Wallet ID:
            </td>
            <td align="left" width="25%" >
                <html:input path="recipientAgentAccountNo" id="recipientAgentAccountNo" cssClass="textBox" maxlength="50" tabindex="1" onkeypress="return maskNumber(this,event)"/>
            </td>
            <td class="formText" align="right" width="25%">&nbsp;</td>
            <td align="left">&nbsp;</td>
        </tr>
        <tr>
            <td class="formText" align="right">
                Recipient CNIC:
            </td>
            <td align="left">
                <html:input path="recipientCnic" id="recipientCnic" cssClass="textBox" maxlength="13" tabindex="3" onkeypress="return maskNumber(this,event)"/>
            </td>
            <td class="formText" align="right">
                Recipient Mobile No:
            </td>
            <td align="left" >
                <html:input path="recipientMobileNo" id="recipientMobileNo" cssClass="textBox" maxlength="11" tabindex="4" onkeypress="return maskNumber(this,event)"/>
            </td>
        </tr>
        <tr>
            <td class="formText" align="right">
                Transaction ID:
            </td>
            <td align="left">
                <html:input path="transactionCode" id="transactionCode" cssClass="textBox" tabindex="5" maxlength="50" onkeypress="return maskNumber(this,event)"/>
            </td>
            <td class="formText" align="right">
                Third Party Transaction Code:
            </td>
            <td align="left">
                <html:input path="fonepayTransactionCode"   id="fonepayTransactionCode" tabindex="6" cssClass="textBox" maxlength="50" />
            </td>
        </tr>
        <tr>
            <td class="formText" align="right">
                Product:
            </td>
            <td align="left">
                <html:select path="productId" cssClass="textBox" tabindex="10" >
                    <html:option value="">---All---</html:option>
                    <c:if test="${productModelList != null}">
                        <html:options items="${productModelList}" itemValue="productId" itemLabel="name"/>
                    </c:if>
                </html:select>
            </td>
            <td class="formText" align="right">&nbsp;</td>
            <td align="left">&nbsp;</td>
        </tr>
        <tr>
            <td class="formText" align="right">
                Transaction Date - Start:
            </td>
            <td align="left">
                <html:input path="startDate" id="startDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
                <img id="sDate" tabindex="11" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                <img id="sDate" tabindex="12" title="Clear Date" name="popcal"  onclick="javascript:$('startDate').value=''"   align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
            </td>
            <td class="formText" align="right">
                Transaction Date - End:
            </td>
            <td align="left">
                <html:input path="endDate" id="endDate"  readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
                <img id="eDate" tabindex="13" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                <img id="eDate" tabindex="14" title="Clear Date" name="popcal"  onclick="javascript:$('endDate').value=''"   align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
            </td>
        </tr>
        <tr>
            <td class="formText" align="right">
            </td>
            <td align="left">
                <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
                       value="<%=PortalConstants.ACTION_RETRIEVE%>">
                <input name="_search" type="submit" class="button" value="Search" tabindex="13"/>
                <input name="reset" type="reset"
                       onclick="javascript: window.location='p_bisptransactiondetailreport.html?actionId=${retriveAction}'"
                       class="button" value="Cancel" tabindex="14" />
            </td>
            <td class="formText" align="right">
            </td>
            <td align="left">
            </td>
        </tr>
    </table>
</html:form>

<ec:table items="transactionDetailPortalList" var="transactionDetailPortalModel"
          action="${contextPath}/p_bisptransactiondetailreport.html?actionId=${retriveAction}"
          title=""
          retrieveRowsCallback="limit"
          filterRowsCallback="limit"
          sortRowsCallback="limit"
          filterable="false"
>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
        <ec:exportXls fileName="BISP Transaction Details.xls" tooltip="Export Excel" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
        <ec:exportXlsx fileName="BISP Transaction Details.xlsx" tooltip="Export Excel" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
        <ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
                      headerTitle="BISP Transaction Details"
                      fileName="BISP Transaction Details.pdf" tooltip="Export PDF" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
        <ec:exportCsv fileName="transactiondetaili8management.csv" tooltip="Export CSV"></ec:exportCsv>
    </authz:authorize>
    <ec:row>
        <c:set var="_codeOne" value="${transactionDetailPortalModel.transactionCode}" />
        <ec:column property="transactionCode" filterable="false" title="Transaction ID" escapeAutoFormat="true">
            <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
                <a href="p_bisptransactiondetailreport.html?actionId=${retriveAction}&transactionCode=${transactionDetailPortalModel.transactionCode}" onClick="return openTransactionWindow('${transactionDetailPortalModel.transactionCode}')">
                        ${transactionDetailPortalModel.transactionCode}
                </a>
            </authz:authorize>
            <authz:authorize ifNotGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
                ${transactionDetailPortalModel.transactionCode}
            </authz:authorize>
        </ec:column>
        <ec:column property="fonepayTransactionCode" filterable="false" title="3rd Party Transaction ID" escapeAutoFormat="true"/>
        <ec:column property="agent1Id" filterable="false" title="Sender Agent ID" escapeAutoFormat="true"/>
        <ec:column property="senderAgentAccountNo" filterable="false" title="Sender Agent Account No." escapeAutoFormat="true" style="text-align: center"/>
        <ec:column property="mfsId" filterable="false" title="Sender ID" escapeAutoFormat="true" style="text-align: center"/>
        <ec:column property="saleMobileNo" filterable="false" title="Sender Mobile No." escapeAutoFormat="true" style="text-align: center"/>
        <ec:column property="senderCnic" filterable="false" title="Sender CNIC" escapeAutoFormat="true" style="text-align: center"/>
        <ec:column property="recipientMobileNo" filterable="false" title="Recipient Mobile No." escapeAutoFormat="true" style="text-align: center"/>
        <ec:column property="recipientCnic" filterable="false" title="Recipient CNIC" escapeAutoFormat="true" style="text-align: center"/>
        <ec:column property="createdOn" cell="date" format="dd/MM/yyyy hh:mm a" filterable="false" title="Transaction Date"/>
        <ec:column property="updatedOn" cell="date" format="dd/MM/yyyy hh:mm a" filterable="false" title="Transaction Last Updated On"/>
        <ec:column property="productName" filterable="false" title="Product"/>
        <ec:column property="transactionAmount" filterable="false" title="Amount" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
        <ec:column property="inclusiveCharges" filterable="false" title="Inclusive Charges" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
        <ec:column property="exclusiveCharges" filterable="false" title="Exclusive Charges" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
        <ec:column property="totalAmount" filterable="false" title="Total Customer Charges" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
        <ec:column property="totalBankComm" filterable="false" title="To Bank" calc="total" calcTitle="Total:"  cell="currency"  format="0.00"/>
        <ec:column property="fed" filterable="false" title="FED Charges" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
        <ec:column property="wht" filterable="false" title="W. Holding Tax" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
        <ec:column property="akblCommission" filterable="false" title="Bank Commission" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
        <ec:column property="agentCommission" filterable="false" title="Agent 1 Commission" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
        <ec:column property="agent2Commission" filterable="false" title="Agent 2 Commission" calc="total" calcTitle="Total:"  cell="currency"  format="0.00" style="text-align: right"/>
        <ec:column property="failureReason" filterable="false"  title="Failure Reason" />
        <ec:column property="macAddress" filterable="false"  title="MAC Address" />
        <ec:column property="ipAddress" filterable="false"  title="IP Address" />
        <ec:column property="imeiNumber" filterable="false"  title="IMEI Number" />
        <ec:column property="latitude" filterable="false"  title="Latitude" />
        <ec:column property="longitude" filterable="false"  title="Longitude" />
        <ec:column property="reversedByName" filterable="false"  title="Reversed By" />
        <ec:column property="reversedDate" cell="date" format="dd/MM/yyyy hh:mm a" filterable="false" title="Reversed On"/>
        <ec:column property="reversedComments" filterable="false"  title="Comments" />
        <authz:authorize ifAnyGranted="<%=PortalConstants.BISP_REVERSAL_RETRY_UPDATE%>">
            <c:choose>
                <c:when test="${transactionDetailPortalModel.processingStatusId eq 2 and (not empty transactionDetailPortalModel.bispResponseCode and transactionDetailPortalModel.bispResponseCode eq 'I8SB-500')}">
                    <ec:column alias="" title="Reverse" style="text-align: center" sortable="false" viewsAllowed="html">
                        <input type="button"  class="button" value="Reverse" id="btnReverse${_codeOne}" name="btnReverse${_codeOne}"/>
                        <input type="hidden" value="${_codeOne}" name="btnReverse${_codeOne}" id="btnReverse${_codeOne}" />
                        <ajax:htmlContent baseUrl="${contextPath}/reversebisptransaction.html"
                                          eventType="click"
                                          source="btnReverse${_codeOne}"
                                          target="successMsg"
                                          parameters="transactionCodeId=${_codeOne}"
                                          preFunction="initProgress"
                                          postFunction="resetProgress"
                        />
                    </ec:column>
                </c:when>
                <c:otherwise>
                    <ec:column alias="" title="Reverse" style="text-align: center" sortable="false" viewsAllowed="html">
                        <input type="button"  class="button" value="Reverse" id="btnReverse${_codeOne}" name="btnReverse${_codeOne}" disabled/>
                    </ec:column>
                </c:otherwise>
            </c:choose>
        </authz:authorize>
    </ec:row>
</ec:table>

<script language="javascript" type="text/javascript">

    document.forms[0].mfsId.focus();
    function openTransactionWindow(transactionCode)
    {
        var popupWidth = 550;
        var popupHeight = 350;
        var popupLeft = (window.screen.width - popupWidth)/2;
        var popupTop = (window.screen.height - popupHeight)/2;
        newWindow=window.open('p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode='+transactionCode,'TransactionSummary','width='+popupWidth+',height='+popupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
        if(window.focus) newWindow.focus();
        return false;
    }

    Calendar.setup({
        inputField : "startDate", // id of the input field
        button : "sDate", // id of the button
    });

    Calendar.setup({
        inputField : "endDate", // id of the input field
        button : "eDate", // id of the button
        isEndDate : true
    });

</script>
<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>
</body>
</html>