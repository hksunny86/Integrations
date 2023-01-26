<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<%@page import="com.inov8.microbank.common.util.PortalConstants"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
    <meta name="decorator" content="decorator">
    <meta name="title" content="Customer Device Activate/De-Activate" />
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/prototype.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/date-validation.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/popup.js"></script>
    <link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
    <link rel="stylesheet" type="text/css" href="${contextPath}/styles/deliciouslyblue/calendar.css" />
    <%@include file="/common/ajax.jsp"%>
    <script type="text/javascript" src="${contextPath}/scripts/jquery-1.7.2.min.js"></script>
    <script language="javascript" type="text/javascript">
        var serverDate ="<%=PortalDateUtils.getServerDate()%>";
        var jq=$.noConflict();

        function error(request) {
            alert("An unknown error has occured. Please contact with the administrator for more details");
        }

        function init() {
            $('errorMsg').innerHTML = "";
            Element.hide('errorMsg');
            $('successMsg').innerHTML = $F('message');
            Element.show('successMsg');

        }

        <%--function ajaxPostFunction() {--%>
        <%--    $('errorMsg').innerHTML = "";--%>
        <%--    Element.hide('errorMsg');--%>
        <%--    $('successMsg').innerHTML = $F('message');--%>
        <%--    Element.show('successMsg');--%>

        <%--    document.getElementById(this.source).disabled=true;--%>
        <%--    try{--%>
        <%--        var str = this.source;--%>
        <%--        var substr = str.split("_");--%>
        <%--        document.getElementById('status_'+substr[1]).innerHTML = "<%=PortalConstants.IBFT_STATUS_IN_PROGRESS%>";--%>
        <%--        window.location.reload();--%>
        <%--    }catch(exception){--%>
        <%--    }--%>

        <%--    jq('html, body').animate({scrollTop : 0},1000);--%>
        <%--};--%>


        function resetProgressWebService() {
            if (!isErrorOccured) {
                // clear error box
                $('errorMsg').innerHTML = "";
                Element.hide('errorMsg');
                // display success message
                $('successMsg').innerHTML = $F('message');
                Element.show('successMsg');

                var text = jq('#successMsg').text();
                var err = "An error has occurered. Please try again later";
                if (text.indexOf("approval against reference Action ID") == -1 && text.indexOf("authorization request already exist with  Action ID ") == -1 && text.indexOf(err) == -1) {
                    window.location.reload();
                } else {

                }

            }
            isErrorOccured = false;
        }

        function initProgress() {
            if (document.getElementById('ajaxMsgDiv')) {
                document.getElementById('ajaxMsgDiv').style.display = "none";
            }
            if (confirm('If information is verified then press OK to continue') == true) {
                return true;
            } else {
                $('errorMsg').innerHTML = "";
                $('successMsg').innerHTML = "";
                Element.hide('successMsg');
                Element.hide('errorMsg');
                return false;
            }
        }

        var isErrorOccured = false;

        function resetProgress() {
            if (!isErrorOccured) {
                // clear error box
                $('errorMsg').innerHTML = "";
                Element.hide('errorMsg');
                // display success message

                Element.show('successMsg');
            }
            isErrorOccured = false;
        }

        jq = $.noConflict();
        function ajaxErrorFunction(request, obj) {
            var msg = "Your request cannot be processed at the moment. Please try again later.";
            $('successMsg').innerHTML = "";
            Element.hide('successMsg');
            jq('html, body').animate({scrollTop : 100},1000);
        };

        function openHistoryWindow(url, title, w, h) {
            var left = (screen.width/2)-(w/2);
            var top = (screen.height/2)-(h/2);
            return window.open(url, title, 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes, copyhistory=no, width='+w+', height='+h+', top='+top+', left='+left);
        }
    </script>
</head>
<body>
<div id="successMsg" class="infoMsg" style="display:none;"></div>
<div id="errorMsg" class="errorMsg" style="display:none;"></div>
<input id="message" value="777" type="hidden" />
<html:form name="refferalCustomerForm" commandName="CustomerDeviceVerificationUpdateController"  method="post" action="p_deviceverificationUpdate.html"
           onsubmit="return validateForm(this);" >
    <table width="850px" border="0">
        <tr>
            <td align="right" class="formText">Mobile #:</td>
            <td align="left">
                <html:input path="mobileNo" onkeypress="return maskCommon(this,event)" cssClass="textBox" maxlength="11" tabindex="4" readonly="true"/>
            </td>

            <td align="right" class="formText">Status:</td>
            <td align="left">
                <html:select path="approvalStatus" cssClass="textBox" tabindex="9">
                    <html:option value="">---All---</html:option>
                    <html:option value="ACTIVE">ACTIVE</html:option>
                    <html:option value="IN-ACTIVE">IN-ACTIVE</html:option>
                </html:select>
            </td>
        <tr>

        <tr>
            <td align="right" class="formText">Device ID #:</td>
            <td align="left">
                <html:input path="id" onkeypress="return maskCommon(this,event)" cssClass="textBox" maxlength="11" tabindex="4" readonly="true"/>
            </td>

        <td height="16" align="right" class="formText"
            width="25%">
            Comments:
        </td>
        <td align="left"class="formText" width="25%">
            <html:textarea style="height:50px;" path="remarks" tabindex="41" cssClass="textBox"
                           maxlength="250"/>
        </td>

        <tr>



        </tr>

        <tr> <td height="16" align="right" class="formText"
                 width="25%">
            Device Name:
        </td>
            <td align="left"  class="formText" width="25%">
                <html:input  path="deviceName" tabindex="4" cssClass="textBox"
                               maxlength="250" readonly="true"/>
            </td></tr>


        <tr>
            <td class="formText" align="right"></td>
            <td align="left">

                <input name="_save" type="submit" class="button" value="Save"/>
                <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
                       value="<%=PortalConstants.ACTION_CREATE%>"/>
                <input type="button" name="cancel" value="Cancel" class="button"
                       onclick="javascript:window.location='p_deviceverificationUpdate.html?actionId=2'"/>

            </td>
            <td class="formText" align="right">

            </td>
            <td align="left">&nbsp;</td>
        </tr>
        <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
    </table>
</html:form>
<!-- data table result -->
<%--<ec:table items="reqList" var="model"--%>
<%--          action="${contextPath}/p_deviceverification.html?actionId=${retriveAction}"--%>
<%--          title="" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" filterable="false">--%>

<%--    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">--%>
<%--        <ec:exportXls fileName="Salary Disbursement.xls" tooltip="Export Excel" />--%>
<%--    </authz:authorize>--%>
<%--    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">--%>
<%--        <ec:exportXlsx fileName="Salary Disbursement.xlsx" tooltip="Export Excel" />--%>
<%--    </authz:authorize>--%>
<%--    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">--%>
<%--        <ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da" headerTitle="List Authorizations"--%>
<%--                      fileName="Salary Disbursement Requests.pdf" tooltip="Export PDF" />--%>
<%--    </authz:authorize>--%>
<%--    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">--%>
<%--        <ec:exportCsv fileName="Salary Disbursement.csv" tooltip="Export CSV"></ec:exportCsv>--%>
<%--    </authz:authorize>--%>


<%--    <ec:row>--%>

<%--        <ec:column property="mobileNo" title="Mobile Number" escapeAutoFormat="true"/>--%>
<%--        <ec:column property="id" title="Device ID" escapeAutoFormat="true"/>--%>
<%--        <ec:column property="unquieIdentifier" title="Unquie Identifier" escapeAutoFormat="true"/>--%>
<%--        <ec:column property="deviceName" title="Device Name" escapeAutoFormat="true"/>--%>
<%--        <ec:column property="requestType" title="Request Type" escapeAutoFormat="true"/>--%>
<%--        <ec:column property="approvalStatus" title="Approval Status" escapeAutoFormat="true"/>--%>
<%--        <ec:column property="remarks" title="Remarks" escapeAutoFormat="true"/>--%>
<%--        <ec:column property="requestedDate" title="Request Date" escapeAutoFormat="true"/>--%>
<%--        <ec:column property="requestedTime" title="Request Time" escapeAutoFormat="true"/>--%>
<%--        <ec:column property="fullName" title="Custmer Name" escapeAutoFormat="true"/>--%>
<%--        <ec:column property="fatherHusbandName" title="Father Husband Name" escapeAutoFormat="true"/>--%>
<%--        <ec:column property="address" title="Address" escapeAutoFormat="true"/>--%>
<%--        <ec:column property="cnic" title="Cnic" escapeAutoFormat="true"/>--%>
<%--        <ec:column property="dob"  filterable="false" cell="date" format="dd/MM/yyyy" title="Dob" escapeAutoFormat="true"/>--%>
<%--        <ec:column property="cnicIssuanceDate"  filterable="false" cell="date"	format="dd/MM/yyyy" title="Cnic Issuance Date" escapeAutoFormat="true"/>--%>
<%--        <ec:column property="cnicExpiryDate"  filterable="false" cell="date"	format="dd/MM/yyyy" title="Cnic Expiry Date" escapeAutoFormat="true"/>--%>





<%--        <ec:column alias="" title=""  style="text-align: center" sortable="false" viewsAllowed="html">--%>
<%--            <input type="button" class="button" style="width='90px'" value="Change Info"--%>
<%--                   onclick="javascript:changeInfo('${contextPath}/p-savesalarydisbursementform.html?appUserId=${model.appUserId}');" />--%>
<%--        </ec:column>--%>

<%--    </ec:row>--%>
<%--</ec:table>--%>
<script type="text/javascript">
    // Calendar.setup({
    //     inputField : "startDate", // id of the input field
    //     button : "sDate", // id of the button
    // });
    // Calendar.setup({
    //     inputField : "endDate", // id of the input field
    //     button : "eDate", // id of the button
    //     isEndDate : true
    // });
</script>
<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>
</body>
</html>