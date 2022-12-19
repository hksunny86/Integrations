<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
<%@ page import='com.inov8.microbank.common.util.PortalDateUtils'%>

<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>

<html>
<head>
    <meta name="decorator" content="decorator">
    <script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
    <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
    <link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
    <link type="text/css" rel="stylesheet" href="styles/ajaxtags.css" />
    <link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">

    <meta name="title" content="Customer Referal  Report" />
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

<html:form name='refferalCustomerForm' commandName="RefferalCustomerController" method="post"
           action="${contextPath}/p_refferalcustomer.html" onsubmit="return validateForm(this);" >
    <table width="100%" border="0">
        <tr>
            <td align="right" class="formText">Mobile #:</td>
            <td align="left">
                <html:input path="sender_mobile" onkeypress="return maskCommon(this,event)" cssClass="textBox" maxlength="11" tabindex="4" />
            </td>

    <tr>

    </tr>
        <tr>
            <td class="formText" align="right"></td>
            <td align="left">
                <input name="_search" type="submit" class="button" value="Search" tabindex="12"/>
                <input name="reset" type="reset"
                       onclick="javascript: window.location='p_refferalcustomer.html?actionId=${retriveAction}'"
                       class="button" value="Cancel" tabindex="13" />
            </td>
            <td class="formText" align="right">

            </td>
            <td align="left">&nbsp;</td>
        </tr>
        <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
    </table></html:form>

<ec:table items="reqList" var="model"
          action="${contextPath}/p_refferalcustomer.html?actionId=${retriveAction}"
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

        <ec:column property="sender_mobile" title="Sender mobile" escapeAutoFormat="true"/>
        <ec:column property="receiver_mobile" title="Receiver mobile" escapeAutoFormat="true"/>
        <ec:column property="referral_Date" title="Referral Date" escapeAutoFormat="true"/>
        <ec:column property="receiver_Referral_Name" title="Receiver Referral Name" escapeAutoFormat="true"/>
        <ec:column property="receiver_Status" title="Receiver Status" escapeAutoFormat="true"/>
        <ec:column property="receiver_Tranx_Amount" title="Receiver TranxAmount" escapeAutoFormat="true"/>
        <ec:column property="deleted" title="Deleted" escapeAutoFormat="true"/>
        <ec:column property="creditPayment_Amount" title="CreditPayment Amount" escapeAutoFormat="true"/>
        <ec:column property="receiver_Debit_Amount" title="Receiver Debit Amount" escapeAutoFormat="true"/>
        <ec:column property="tranx_Type" title="Tranx Type" escapeAutoFormat="true"/>
        <ec:column property="tranx_Date" title="Tranx Date" escapeAutoFormat="true"/>
        <ec:column property="tranx_From_Referre_Account" title="Tranx From Referre Account" escapeAutoFormat="true"/>
        <ec:column property="tranx_To_Account_By_Referre" title="Tranx To Account By Referre" escapeAutoFormat="true"/>
        <ec:column property="receiver_Signup_Name" title="Receiver Signup Name" escapeAutoFormat="true"/>
        <ec:column property="signup_Date" title="Signup Date" escapeAutoFormat="true"/>




        <%--        <ec:column alias="" title=""  style="text-align: center" sortable="false" viewsAllowed="html">--%>
<%--            <input type="button" class="button" style="width='90px'" value="Save"--%>
<%--                   onclick="javascript:changeInfo('${contextPath}/p-savesalarydisbursementform.html?appUserId=${model.appUserId}');" />--%>
<%--        </ec:column>--%>

    </ec:row>
</ec:table>




<script language="javascript" type="text/javascript">





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
