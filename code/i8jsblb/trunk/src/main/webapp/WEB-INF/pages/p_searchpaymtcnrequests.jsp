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
    <meta name="title" content="Review PayMTNC Requests/AIS Retry" />
    <%@include file="/common/ajax.jsp"%>

</head>
<body bgcolor="#ffffff" onunload="javascript:closeChild();">
<div id="smsContent" class="ajaxMsg"></div>

<div id="ajaxMsgDiv" class="ajaxMsg"></div>

<c:if test="${not empty messages}">
    <div class="infoMsg" id="successMessages">
        <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false" /><br/>
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session" />
</c:if>


<script type="text/javascript">
    function initProgress()
    {
        if(document.getElementById('ajaxMsgDiv')){
            document.getElementById('ajaxMsgDiv').style.display = "none";
        }
        if (confirm('If Request information is verified then press OK to continue')==true)
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
    function ajaxPreFunction() {
        var id = this.source;
        var indx = id.substr(id.indexOf("_")+1);
        document.getElementById("btnretry_" + indx).disabled=true;
        if (confirm('If Request information is verified then press OK to continue')==true)
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
        return true;
    };

    function ajaxPostFunction() {
        // $('errorMsg').innerHTML = "";
        // Element.hide('errorMsg');
        // $('successMsg').innerHTML = $F('message');
        // Element.show('successMsg');
        //
        // jq('html, body').animate({scrollTop : 0},1000);

        window.location.reload();
    };

    function ajaxErrorFunction(request, obj) {
        var msg = "Your request cannot be processed at the moment. Please try again later.";
        $('successMsg').innerHTML = "";
        Element.hide('successMsg');
        jq('html, body').animate({scrollTop : 0},1000);
    };

    function resetProgressDeactivate(){
        window.location.reload();
    }
</script>

<html:form name='searchPayMtncRequests' commandName="SearchPayMTCNRequestController" method="post"
           action="${contextPath}/p_searchpaymtcnrequests.html" onsubmit="return validateFormData(this)" >
    <table width="100%" border="0">
        <tr>
            <td align="right" class="formText">Mobile #:</td>
            <td align="left"><html:input path="mobileNo" cssClass="textBox" maxlength="11" tabindex="3" /></td>
            <td align="right" class="formText">CNIC #:</td>
            <td align="left"><html:input path="cnic" cssClass="textBox" maxlength="13" tabindex="4" /></td>
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
        <tr>
            <td class="formText" align="right"></td>
            <td align="left">
                <input name="_search" type="submit" class="button" value="Search" tabindex="12"/>
                <input name="reset" type="reset"
                       onclick="javascript: window.location='p_searchpaymtcnrequests.html?actionId=${retriveAction}'"
                       class="button" value="Cancel" tabindex="13" />
            </td>
            <td class="formText" align="right">

            </td>
            <td align="left">&nbsp;</td>
        </tr>
        <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
    </table></html:form>

<ec:table items="reqList" var="model"
          action="${contextPath}/p_searchpaymtcnrequests.html"
          title="" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" filterable="false">
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
        <ec:exportXls fileName="Pay MTCN.xls" tooltip="Export Excel" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
        <ec:exportXlsx fileName="Pay MTCN.xlsx" tooltip="Export Excel" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
        <ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da" headerTitle="List Authorizations"
                      fileName="Pay MTCN Requests.pdf" tooltip="Export PDF" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
        <ec:exportCsv fileName="Pay MTCN.csv" tooltip="Export CSV"></ec:exportCsv>
    </authz:authorize>
    <ec:row>
        <ec:column property="mobileNo" title="Mobile No" escapeAutoFormat="True" style="text-align: center"/>
        <ec:column property="cnic" title="CNIC" escapeAutoFormat="true"/>
        <ec:column property="thirdPartyResponseCode" title="Response Code" escapeAutoFormat="true"/>
        <ec:column property="rrn" title="Retrieval Reference Number" escapeAutoFormat="true"/>
        <ec:column property="createdOn" title="Created On" escapeAutoFormat="true"/>
        <ec:column property="updatedOn" title="Updated On" escapeAutoFormat="true"/>
<%--            <ec:column property="payMtncReqId" alias="" title="Retry PayMTCN" style="text-align: center" filterable="false" sortable="false" viewsAllowed="html">--%>
<%--                <authz:authorize ifAnyGranted="<%=PortalConstants.RPT_PAY_MTNC_UPDATE%>">--%>
<%--                    <c:choose>--%>
<%--                        <c:when test="${model.isValid == 1 && model.isAccountOpening == null}">--%>
<%--                            <input type="button" class="button" value="Retry PayMTCN Request"--%>
<%--                                   id="paymtcnId${payMtncReqId}" />--%>
<%--                            <input type="hidden" value="${payMtncReqId}" name="Id${payMtncReqId}" id="Id${payMtncReqId}" />--%>
<%--                            <ajax:htmlContent baseUrl="${contextPath}/p_resendpaymtcnrequest.html"--%>
<%--                                              source="paymtcnId${payMtncReqId}"--%>
<%--                                              target="smsContent"--%>
<%--                                              parameters="paymtcnId=${model.payMtncReqId}"--%>
<%--                                              preFunction="initProgress"--%>
<%--                                              postFunction="resetProgressDeactivate"  />--%>
<%--                        </c:when>--%>
<%--                        <c:when test="${model.isValid == 1 && model.isAccountOpening == 1}">--%>
<%--                            <input type="button" class="button" value="Retry AIS Request"--%>
<%--                                   id="paymtcnId${payMtncReqId}" />--%>
<%--                            <input type="hidden" value="${payMtncReqId}" name="Id${payMtncReqId}" id="Id${payMtncReqId}" />--%>
<%--                            <ajax:htmlContent baseUrl="${contextPath}/p_resendpaymtcnrequest.html"--%>
<%--                                              source="paymtcnId${payMtncReqId}"--%>
<%--                                              target="smsContent"--%>
<%--                                              parameters="paymtcnId=${model.payMtncReqId}"--%>
<%--                                              preFunction="initProgress"--%>
<%--                                              postFunction="resetProgressDeactivate"  />--%>
<%--                        </c:when>--%>
<%--                        <c:otherwise>--%>
<%--                            <c:if test="${model.isValid == 0 && model.isAccountOpening == null}">--%>
<%--                                <input type="button" class="button" value="Retry PayMTCN Request"--%>
<%--                                       id="paymtcnIdDisable"--%>
<%--                                       disabled="disabled" />--%>
<%--                            </c:if>--%>

<%--                            <c:if test="${model.isValid == 0 && model.isAccountOpening == 1}">--%>
<%--                            <input type="button" class="button" value="Retry AIS Request"--%>
<%--                                   id="paymtcnIdDisable"--%>
<%--                                   disabled="disabled" />--%>
<%--                            </c:if>--%>
<%--                        </c:otherwise>--%>
<%--                    </c:choose>--%>
<%--                </authz:authorize>--%>
<%--                <authz:authorize ifNotGranted="<%=PortalConstants.RPT_PAY_MTNC_UPDATE%>">--%>
<%--                    <input type="button" class="button" value="Retry PayMTCN Request"--%>
<%--                           id="paymtcnIdDisable"--%>
<%--                           disabled="disabled" />--%>
<%--                </authz:authorize>--%>
<%--            </ec:column>--%>
        <ec:column alias="" title="Retry PayMTCN" viewsAllowed="html">
            <authz:authorize ifAnyGranted="<%=PortalConstants.RPT_PAY_MTNC_UPDATE%>">
                <c:choose>
                    <c:when test="${model.isValid == 1 && model.isAccountOpening == null}">
                        <input type="button" class="button" value="Retry PayMTCN Request" id="btnretry_${model.payMtncReqId}" onclick="disableRetryButton(this);"/>
                    </c:when>
                    <c:when test="${model.isValid == 1 && model.isAccountOpening == 1}">
                        <input type="button" class="button" value="Retry AIS Request" id="btnretry_${model.payMtncReqId}" onclick="disableRetryButton(this);"/>
                    </c:when>
                    <c:otherwise>

                        <c:if test="${model.isValid == 0 && model.isAccountOpening == null}">
                            <input type="button" class="button" disabled="disabled" value="Retry PayMTCN Request" id="btnretry_${model.payMtncReqId}" />
                        </c:if>

                        <c:if test="${model.isValid == 0 && model.isAccountOpening == 1}">
                        <input type="button" class="button" disabled="disabled" value="Retry AIS Request" id="btnretry_${model.payMtncReqId}" />
                        </c:if>

                    </c:otherwise>
                </c:choose>
                <ajax:updateField baseUrl="${contextPath}/p_resendpaymtcnrequest.html"
                                  source="btnretry_${model.payMtncReqId}" eventType="click"
                                  target="message" action="btnretry_${model.payMtncReqId}"
                                  parameters="paymtcnId=${model.payMtncReqId}"
                                  parser="new ResponseXmlParser()"
                                  preFunction="ajaxPreFunction"
                                  errorFunction="ajaxErrorFunction"
                                  postFunction="ajaxPostFunction" />
            </authz:authorize>
        </ec:column>
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
