<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<%@page import="com.inov8.microbank.common.util.PortalConstants"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
    <meta name="decorator" content="decorator">
    <meta name="title" content="Agent Segment De-Activate" />
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
<html:form name="gentSegmentForm" commandName="AgentSegmentDeActiveController"  method="post" action="p_agentsegmentactivedeactive.html"
           onsubmit="return validateForm(this);" >
    <table width="850px" border="0">
        <tr>
            <td class="formText">
            <td align="right" class="formText">
                Agent ID:</td>
            <td align="left"><html:input path="agentID" onkeypress="return maskCommon(this,event)" cssClass="textBox" maxlength="13" tabindex="3" /></td>
            </td>


                <%--<td height="16" align="right" class="formText">Active:</td>--%>
                <%--<td width="58%" align="left"><html:checkbox path="IsActive" tabindex="15"/>--%>

        </tr>


        <tr>
            <td class="formText" align="right">
            </td>
            <td align="left">
                <input name="_search" type="submit" class="button" value="Search" tabindex="12"/>
                <input name="reset" type="reset"
                       onclick="javascript: window.location='p_agentsegmentactivedeactive.html?actionId=${retriveAction}'"
                       class="button" value="Cancel" tabindex="13" />
            </td>

            <td class="formText" align="right">
            </td>
            <td align="left">
            </td>
        </tr>
    </table>
</html:form>
<!-- data table result -->
<ec:table items="reqList" var="model" action="${contextPath}/p_agentsegmentactivedeactive.html" title="" retrieveRowsCallback="limit"
          filterRowsCallback="limit" sortRowsCallback="limit" filterable="false">
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
        <ec:exportXls fileName="Agent_Segment_De_Activate.xls" tooltip="Export Excel" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
        <ec:exportXlsx fileName="Agent_Segment_De_Activate.xlsx" tooltip="Export Excel" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
        <ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da" headerTitle="View Transactions"
                      fileName="Agent_Segment_De_Activate.pdf" tooltip="Export PDF" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
        <ec:exportCsv fileName="Agent_Segment_De_Activate.csv" tooltip="Export CSV"></ec:exportCsv>
    </authz:authorize>
    <ec:row>
        <ec:column property="agentID" title="AGENT ID" escapeAutoFormat="true"/>
        <ec:column property="segmentId" title="Segment ID" escapeAutoFormat="true"/>
        <ec:column property="name" title="Segment Name" escapeAutoFormat="true"/>
        <ec:column property="productId" title="Product ID" escapeAutoFormat="true"/>
        <ec:column property="productName" title="Product Name" escapeAutoFormat="true"/>
        <ec:column property="retailerId" title="Retailer ID" escapeAutoFormat="true"/>
        <ec:column property="retailerContactId" title="Retailer Contact ID" escapeAutoFormat="true"/>
        <ec:column property="createdOn" title="Created On" escapeAutoFormat="true"/>
        <ec:column property="updatedOn" title="Updated On" escapeAutoFormat="true"/>
        <ec:column property="createdBy" title="Created By" escapeAutoFormat="true"/>
        <ec:column property="updatedBy" title="Updated By" escapeAutoFormat="true"/>




        <ec:column alias="" title="" viewsAllowed="html">
            <authz:authorize ifAnyGranted="<%=PortalConstants.IBFT_RETRY_ADVICE_UPDATE%>">
                <c:choose>
                    <c:when test="${model.isActive eq true}">
                        <input type="button" class="button" value="De-Active Segment" id="btnclear_${model.agentSegmentExceptionId}"/>
                    </c:when>
                    <c:otherwise>
                        <input type="button" class="button" disabled="disabled" value="De-Active Segment" id="btnclear_${model.agentSegmentExceptionId}" />
                    </c:otherwise>
                </c:choose>
                <ajax:updateField baseUrl="${contextPath}/p_deactiveagentsegmentajaxrequest.html"
                                  source="btnclear_${model.agentSegmentExceptionId}" eventType="click"
                                  target="message" action="btnclear_${model.agentSegmentExceptionId}"
                                  parameters="ibftRetryAdviceId=${model.agentSegmentExceptionId}"
                                  parser="new ResponseXmlParser()"
                                  errorFunction="ajaxErrorFunction"
                                  preFunction="initProgress"
                                  postFunction="resetProgressWebService" />
            </authz:authorize>
        </ec:column>

    </ec:row>
</ec:table>
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