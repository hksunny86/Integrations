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

    <meta name="title" content="Customer Device Verification Report" />
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

<html:form name='refferalCustomerForm' commandName="CustomerDeviceVerificationController" method="post"
           action="${contextPath}/p_deviceverification.html" onsubmit="return validateForm(this);" >
    <table width="100%" border="0">
        <tr>
            <td align="right" class="formText">Mobile #:</td>
            <td align="left">
                <html:input path="mobileNo" onkeypress="return maskCommon(this,event)" cssClass="textBox" maxlength="11" tabindex="4" />
            </td>

        <tr>

        </tr>
        <tr>
            <td class="formText" align="right"></td>
            <td align="left">
                <input name="_search" type="submit" class="button" value="Search" tabindex="12"/>
                <input name="reset" type="reset"
                       onclick="javascript: window.location='p_deviceverification.html?actionId=${retriveAction}'"
                       class="button" value="Cancel" tabindex="13" />
            </td>
            <td class="formText" align="right">

            </td>
            <td align="left">&nbsp;</td>
        </tr>
        <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
    </table></html:form>

<ec:table items="reqList" var="model"
          action="${contextPath}/p_deviceverification.html?actionId=${retriveAction}"
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

        <ec:column property="mobileNo" title="Mobile Number" escapeAutoFormat="true"/>
        <ec:column property="id" title="Device ID" escapeAutoFormat="true"/>
        <ec:column property="unquieIdentifier" title="Unquie Identifier" escapeAutoFormat="true"/>
        <ec:column property="deviceName" title="Device Name" escapeAutoFormat="true"/>
        <ec:column property="requestType" title="Request Type" escapeAutoFormat="true"/>
        <ec:column property="approvalStatus" title="Approval Status" escapeAutoFormat="true"/>
        <ec:column property="remarks" title="Remarks" escapeAutoFormat="true"/>
        <ec:column property="requestedDate" title="Request Date" escapeAutoFormat="true"/>
        <ec:column property="requestedTime" title="Request Time" escapeAutoFormat="true"/>
        <ec:column property="fullName" title="Custmer Name" escapeAutoFormat="true"/>
        <ec:column property="fatherHusbandName" title="Mother Maiden Name" escapeAutoFormat="true"/>
        <ec:column property="address" title="Birth Place" escapeAutoFormat="true"/>
        <ec:column property="cnic" title="Cnic" escapeAutoFormat="true"/>
        <ec:column property="dob"  filterable="false" cell="date" format="dd/MM/yyyy" title="Dob" escapeAutoFormat="true"/>
        <ec:column property="cnicIssuanceDate"  filterable="false" cell="date"	format="dd/MM/yyyy" title="Cnic Issuance Date" escapeAutoFormat="true"/>
        <ec:column property="cnicExpiryDate"  filterable="false" cell="date"	format="dd/MM/yyyy" title="Cnic Expiry Date" escapeAutoFormat="true"/>





        <ec:column alias="" title=""  style="text-align: center" sortable="false" viewsAllowed="html">
            <c:choose>
                <c:when test="${model.approvalStatus == 'A'}">

                    <input type="button" class="button" style="width='90px'" value="Change Info" disabled="disabled"
                           onclick="javascript:changeInfo('${contextPath}/p_deviceverificationUpdate.html?mobileNo=${model.mobileNo}&deviceID=${model.id}&deviceName=${model.deviceName}');" />
                </c:when>
                <c:otherwise>
                    <input type="button" class="button" style="width='90px'" value="Change Info"
                           onclick="javascript:changeInfo('${contextPath}/p_deviceverificationUpdate.html?mobileNo=${model.mobileNo}&deviceID=${model.id}&deviceName=${model.deviceName}');" />
                </c:otherwise>
            </c:choose>

        </ec:column>

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
