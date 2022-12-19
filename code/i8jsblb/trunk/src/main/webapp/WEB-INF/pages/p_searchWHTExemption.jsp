<%--
  Created by IntelliJ IDEA.
  User: Zeeshan
  Date: 7/1/2016
  Time: 10:26 AM
  To change this template use File | Settings | File Templates.
--%>
<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<html>
<head>
    <meta name="decorator" content="decorator">
    <script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
    <script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/date-validation.js"></script> 

    <link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
    <link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
    <%@include file="/common/ajax.jsp"%>
    <meta name="title" content="Search WHT Exemption" />
    <script language="javascript" type="text/javascript">

        var jq=$.noConflict();
        var serverDate ="<%=PortalDateUtils.getServerDate()%>";

        function error(request)
        {
            alert("An unknown error has occured. Please contact with the administrator for more details");
        }
    </script>
</head>
<body bgcolor="#ffffff">
<c:if test="${not empty messages}">
    <div class="infoMsg" id="successMessages">
        <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false" /><br/>
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session" />
</c:if>

<html:form name='wHTExemptionViewForm' commandName="wHTExemptionView" method="post"
           action="p_searchWHTExemption.html" onsubmit="return onFormSubmit(this)" >
    <table width="950px" border="0">

     <tr>

        <td align="right" class="formText">
            Agent ID:
        </td>
        <td align="left" width="32%">
            <html:input path="userId" id="userId" cssClass="textBox" maxlength="7" tabindex="1" onkeypress="return maskAlphaNumeric(this, event)" />
        </td>

        <td align="right" class="formText">
            Mobile No:
        </td>
        <td align="left" width="32%">
            <html:input path="mobile" id="mobile" cssClass="textBox" maxlength="11" tabindex="2" onkeypress="return maskInteger(this, event)"/>
        </td>

    </tr>

        <tr>

            <td align="right" class="formText">
                Name:
            </td>
            <td align="left" width="32%">
                <html:input path="name" id="name" cssClass="textBox" tabindex="3" onkeypress="return maskAlphaNumericWithSp(this,event)"/>
            </td>

            <td align="right" class="formText">
                CNIC:
            </td>
            <td align="left" width="32%">
                <html:input path="cnic" id="cnic" cssClass="textBox" maxlength="13" tabindex="1" onkeypress="return maskInteger(this, event)"/>
            </td>

        </tr>

        <tr>
            <td class="formText" align="right">
                Exemption Start Date(From):
            </td>
            <td align="left">
                <html:input path="exemptionFromStartDate" id="exemptionFromStartDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
                <img id="fromStartDate" tabindex="3" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                <img id="fromStartDate" tabindex="4" title="Clear Date" name="popcal" onclick="javascript:$('exemptionFromStartDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
            </td>
            <td class="formText" align="right">
                Exemption Start Date(To):
            </td>
            <td align="left">
                <html:input path="exemptionToStartDate" id="exemptionToStartDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
                <img id="toStartDate" tabindex="5" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                <img id="toStartDate" tabindex="6" title="Clear Date" name="popcal" onclick="javascript:$('exemptionToStartDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
            </td>
        </tr>


        <tr>
            <td class="formText" align="right">
                Exemption End Date(From):
            </td>
            <td align="left">
                <html:input path="exemptionFromEndDate" id="exemptionFromEndDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
                <img id="fromEndDate" tabindex="3" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                <img id="fromEndDate" tabindex="4" title="Clear Date" name="popcal" onclick="javascript:$('exemptionFromEndDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
            </td>
            <td class="formText" align="right">
                Exemption End Date(To):
            </td>
            <td align="left">
                <html:input path="exemptionToEndDate" id="exemptionToEndDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
                <img id="toEndDate" tabindex="5" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                <img id="toEndDate" tabindex="6" title="Clear Date" name="popcal" onclick="javascript:$('exemptionToEndDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
            </td>
        </tr>

        <tr>
            <td class="formText" align="right">
                &nbsp;
            </td>
            <td align="left">
                <input name="_search" type="submit" class="button" value="Search" tabindex="11"/>
                <input name="reset" type="reset"
                       onclick="javascript: window.location='p_searchWHTExemption.html?actionId=${retriveAction}'"
                       class="button" value="Cancel" tabindex="12"/>
            </td>
            <td colspan="2">
                &nbsp;
            </td>
        </tr>
        <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
    </table></html:form>


<ec:table items="whtExemptionModelList" var="whtExemptionViewModel"
          action="${contextPath}/p_searchWHTExemption.html?actionId=${retriveAction}"
          title="" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" filterable="false" sortable="false" rowsDisplayed="100" showPagination="false">
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
        <ec:exportXls fileName="WHT Exemption Report.xls" tooltip="Export Excel"/>
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
        <ec:exportXlsx fileName="WHT Exemption Report.xlsx" tooltip="Export Excel" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
        <ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
                      headerTitle="WHT Exemption Report" fileName="WHT Exemption Report.pdf" tooltip="Export PDF"/>
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
        <ec:exportCsv fileName="WHT Exemption Report.csv" tooltip="Export CSV"></ec:exportCsv>
    </authz:authorize>
    <ec:row>
        <ec:column property="userId" title="Agent ID" style="text-align: center"/>
        <ec:column property="name" title="Name" style="text-align: center"/>
        <ec:column property="mobile" title="Mobile" style="text-align: center"/>
        <ec:column property="cnic" title="CNIC" style="text-align: center"/>
        <ec:column property="exemptionStartDate" title="Exp. Start Date" style="text-align: center" format="dd/MM/yyyy" cell="date"/>
        <ec:column property="exemptionEndDate" title="Exp. End Date" style="text-align: center" format="dd/MM/yyyy" cell="date"/>
    </ec:row>
</ec:table>



<script language="javascript" type="text/javascript">


    function onFormSubmit(form) {

        var _startExemptionFromDate = form.exemptionFromStartDate.value;
        var _startExemptionToDate = form.exemptionToStartDate.value;
        startlbl = "Exemption start Date(From)";
        endlbl   = "Exemption start Date(To)";
        var isValidStartDate = isValidIssuanceRange(_startExemptionFromDate,_startExemptionToDate,startlbl,endlbl,currentDate);

        var _endExemptionFromDate = form.exemptionFromEndDate.value;
        var _endExemptionToDate = form.exemptionToEndDate.value;
        startlbl = "Exemption End Date(From)";
        endlbl   = "Exemption End Date(To)";
        var isValidEndDate = isValidIssuanceRange(_endExemptionFromDate,_endExemptionToDate,startlbl,endlbl,currentDate);


        isValid = isValidStartDate && isValidEndDate;

        return isValid;
    }

    function isValidExpiryRange(_fDate,_tDate,startlbl,endlbl,serverDate)
    {
        if((_fDate==undefined || _fDate=="" || _fDate==null) && ( _tDate==undefined  || _tDate=="" || _tDate==null))
            return true;

        var isValid = true;
        serverDate= getJsDate( serverDate );

        if(_fDate!=undefined && _fDate!="" && _tDate!=undefined  && _tDate!="" )
        {
            var fDate = getJsDate( _fDate );
            var tDate = getJsDate( _tDate );

            if(!(fDate<=tDate)) {
                alert(startlbl+" should be less than or equal to "+endlbl);
                isValid = false;
            }
        }

        return isValid;
    }


    function isValidIssuanceRange(_fDate,_tDate,startlbl,endlbl,serverDate)
    {
        if((_fDate==undefined || _fDate=="" || _fDate==null) && ( _tDate==undefined  || _tDate=="" || _tDate==null))
            return true;

        var isValid = true;
        serverDate= getJsDate( serverDate );

        if(_fDate!=undefined && _fDate!="")
        {
            var fDate = getJsDate( _fDate );
            if(fDate > serverDate){
                alert(startlbl+" can't be in future.");
                isValid = false;
            }
        }

        if(_fDate!=undefined && _fDate!="" && _tDate!=undefined  && _tDate!="" )
        {
            var fDate = getJsDate( _fDate );
            var tDate = getJsDate( _tDate );

            if(!(fDate<=tDate)) {
                alert(startlbl+" should be less than or equal to "+endlbl);
                isValid = false;
            }
        }

        return isValid;

    }

    Calendar.setup( {inputField  : "exemptionFromStartDate",button : "fromStartDate",showsTime : false,ifFormat : "%d/%m/%Y"} );
    Calendar.setup( {inputField  : "exemptionToStartDate",button : "toStartDate", isEndDate: true , showsTime : false,ifFormat : "%d/%m/%Y" } );
    Calendar.setup( {inputField  : "exemptionFromEndDate",button : "fromEndDate",showsTime : false,ifFormat : "%d/%m/%Y"} );
    Calendar.setup( {inputField  : "exemptionToEndDate",button : "toEndDate", isEndDate: true,showsTime : false,ifFormat : "%d/%m/%Y" } );
    var currentDate = "<%=PortalDateUtils.getServerDate()%>";


</script>


</body>
</html>
