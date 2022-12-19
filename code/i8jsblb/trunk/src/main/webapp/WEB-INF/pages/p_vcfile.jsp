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

    <meta name="title" content="Vc File Report" />
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

<html:form name='vcfileForm' commandName="SearchVcFileController" method="post"
           action="${contextPath}/p_vcfile.html" onsubmit="return validateForm(this);" >
    <table width="100%" border="0">
<%--        <tr>--%>
<%--            <td>--%>
<%--            <td align="right" class="formText">Channel #:</td>--%>
<%--            <td align="left"><html:input path="vcFileId" cssClass="textBox" maxlength="11" tabindex="3" /></td>--%>
<%--            </td>--%>
<%--        </tr>--%>
<%--&lt;%&ndash;        <td>&ndash;%&gt;--%>
<%--&lt;%&ndash;            <td align="right" class="formText">Transaction Origin #:</td>&ndash;%&gt;--%>
<%--&lt;%&ndash;            <td align="left"><html:input path="transactionOrigin" cssClass="textBox" maxlength="11" tabindex="3" /></td>&ndash;%&gt;--%>
<%--&lt;%&ndash;        </td>&ndash;%&gt;--%>

<%--&lt;%&ndash;            <td>&ndash;%&gt;--%>
<%--&lt;%&ndash;            <td align="right" class="formText">Authorization Number#:</td>&ndash;%&gt;--%>
<%--&lt;%&ndash;            <td align="left"><html:input path="authorizationNumber" cssClass="textBox" maxlength="11" tabindex="3" /></td>&ndash;%&gt;--%>
<%--&lt;%&ndash;            </td>&ndash;%&gt;--%>
<%--        </tr>--%>
<%--        <tr>--%>
<%--        <td>--%>
<%--            <td align="right" class="formText">Transaction Type #:</td>--%>
<%--            <td align="left"><html:input path="transactionType" cssClass="textBox" maxlength="11" tabindex="3" /></td>--%>
<%--        </td>--%>
<%--        </tr>--%>

    <tr>
        <td class="formText" align="right">
            Created On - Start:
        </td>
        <input type="hidden" id="stDate" name="stDate">
        <input type="hidden" id="edDate" name="edDate">
        <td align="left">
            <html:input path="start" id="startDate" readonly="true" tabindex="7" cssClass="textBox" maxlength="20"/>
            <img id="sDate" tabindex="11" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif"
                 border="0"/>
            <img id="sDate" tabindex="12" title="Clear Date" name="popcal"
                 onclick="javascript:$('startDate').value=''" align="middle" style="cursor:pointer"
                 src="images/refresh.png" border="0"/>
        </td>
        <td class="formText" align="right">
            Created On - End:
        </td>
        <td align="left">
            <html:input path="end" id="endDate" name="endDate" readonly="true" tabindex="8" cssClass="textBox"
                        maxlength="20"/>
            <img id="eDate" tabindex="13" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif"
                 border="0"/>
            <img id="eDate" tabindex="14" title="Clear Date" name="popcal"
                 onclick="javascript:$('endDate').value=''" align="middle" style="cursor:pointer"
                 src="images/refresh.png" border="0"/>
        </td>
    </tr>
        <tr>
            <td class="formText" align="right"></td>
            <td align="left">
                <input name="_search" type="submit" class="button" value="Search" tabindex="12"/>
                <input name="reset" type="reset"
                       onclick="javascript: window.location='p_vcfile.html?actionId=${retriveAction}'"
                       class="button" value="Cancel" tabindex="13" />
            </td>
            <td class="formText" align="right">

            </td>
            <td align="left">&nbsp;</td>
        </tr>
        <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
    </table></html:form>

<ec:table items="reqList" var="model"
          action="${contextPath}/p_vcfile.html?actionId=${retriveAction}"
          title="" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" filterable="false">

    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
        <ec:exportXls fileName="Salary Disbursement.xls" tooltip="Export Excel" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
        <ec:exportXlsx fileName="Salary Disbursement.xlsx" tooltip="Export Excel" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
        <ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da" headerTitle="List Authorizations"
                      fileName="Salary Disbursement Requests.pdf" tooltip="Export PDF" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
        <ec:exportCsv fileName="Salary Disbursement.csv" tooltip="Export CSV"></ec:exportCsv>
    </authz:authorize>


    <ec:row>

        <ec:column property="deviceNumber" title="Device Number" escapeAutoFormat="true"/>
        <ec:column property="walletNumber" title="Wallet Number" escapeAutoFormat="true"/>
        <ec:column property="mobileNo" title="Mobile Number" escapeAutoFormat="true"/>
        <ec:column property="transactionCode" title="Transaction Code" escapeAutoFormat="true"/>
        <ec:column property="transactionType" title="Transaction Type" escapeAutoFormat="true"/>
        <ec:column property="transactionDate" title="Transaction Date" escapeAutoFormat="true"/>
        <ec:column property="transactionAmount" title="Transaction Amount" escapeAutoFormat="true"/>
        <ec:column property="runBal" title="Running Balance" escapeAutoFormat="true"/>


        <%--        <ec:column alias="" title=""  style="text-align: center" sortable="false" viewsAllowed="html">--%>
<%--            <input type="button" class="button" style="width='90px'" value="Save"--%>
<%--                   onclick="javascript:changeInfo('${contextPath}/p-savesalarydisbursementform.html?appUserId=${model.appUserId}');" />--%>
<%--        </ec:column>--%>

    </ec:row>
</ec:table>




<script language="javascript" type="text/javascript">



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

    function changeInfo(link)
    {
        if (confirm('If customer information is verified then press OK to continue.')==true)
        {
            window.location.href=link;
        }
    }


</script>
<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>

<script language="javascript" type="text/javascript">

    function disableSubmit()
    {
        document.forms.vcfile._save.disabled=true;

        onSave(document.forms.vcfile,null);
    }

    function validateCheckBoxes()
    {
        var state=false;

        jq("input[type='checkbox']:checked").each(
            function() {

                if(this.name!='active')
                {
                    state=true;
                }
            }
        );

        return state;
    }

    function submitForm(theForm)
    {
        if(!isFormDataChanged())
        {
            return;
        }

        var currServerDate = '<%=PortalDateUtils.currentFormattedDate("dd/MM/yyyy")%>';

        _form = theForm;

        document.vcfile.submit();
    }
    Calendar.setup({
        inputField: "startDate", // id of the input field
        button: "sDate", // id of the button
    });

    Calendar.setup({
        inputField: "endDate", // id of the input field
        button: "eDate", // id of the button
        isEndDate: true
    });


</script>

</body>
</html>
