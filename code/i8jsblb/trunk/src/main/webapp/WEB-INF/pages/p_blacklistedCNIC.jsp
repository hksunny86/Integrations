<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.PortalConstants" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
  <c:set var="retrieveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<html>
<head>  
    <meta name="decorator" content="decorator">
    <meta name="title" content="Blacklisted CNIC (External)"/>
    <script type="text/javascript" src="${contextPath}/scripts/prototype.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
    <link type="text/css" rel="stylesheet" href="styles/ajaxtags.css"/>
    <link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
    
    <link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
    <%@include file="/common/ajax.jsp" %>
      <script language="javascript" type="text/javascript">
        var jq=$.noConflict();
        var serverDate ="<%=PortalDateUtils.getServerDate()%>";


        function error(request) {
            alert("An unknown error has occured. Please contact with the administrator for more details");
        }
    </script>
</head>
<body>
<spring:bind path="blacklistedCnicsViewModel.*">
	<div id="rsp" class="ajaxMsg"></div>
	
	 <c:if test="${not empty messages}">
        <div class="infoMsg" id="successMessages">
            <c:forEach var="msg" items="${messages}">
                <c:out value="${msg}" escapeXml="false" /><br/>
            </c:forEach>
        </div>
        <c:remove var="messages" scope="session" />
    </c:if>
    <c:if test="${not empty status.errorMessages}">
        <div class="errorMsg">
            <c:forEach var="error" items="${status.errorMessages}">
                <c:out value="${error}" escapeXml="false"/>
                <br/>
            </c:forEach>
        </div>
    </c:if>

</spring:bind>

<html:form name="blacklistedCINCViewForm" commandName="blacklistedCnicsViewModel"
           action="${contextPath}/p_blacklistedCNIC.html?actionId=${retrieveAction}"   onsubmit="return onFormSubmit(this)">
    <table width="100%">
        <tr>
            <td align="right" class="formText">CNIC : </td>
            <td align="left">
                <html:input path="cnicNo" onkeypress="return maskInteger(this,event)" cssClass="textBox" maxlength="13" tabindex="1"/>
            </td>
            <td align="right" class="formText">Blacklisted : </td>
            <td align="left">
                <html:select path="blacklisted" id="isBlacklisted"  cssClass="textBox" tabindex="2">
                    <html:option value="">[Select]</html:option>
                    <html:option value="1">Yes</html:option>
                    <html:option value="0">No</html:option>
                </html:select>
            </td>
        </tr>
        <tr>
            <td class="formText" align="right">
                Created On (Start) :
            </td>
            <td align="left">
                <html:input path="createdOnStart" id="createdOnStart" readonly="true" tabindex="-1"
                            cssClass="textBox" maxlength="2"/>
                <img id="sDate" tabindex="3" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif"
                     border="0"/>
                <img id="sDate" tabindex="4" title="Clear Date" name="popcal"
                     onclick="javascript:$('createdOnStart').value=''" align="middle" style="cursor:pointer"
                     src="images/refresh.png" border="0"/>
            </td>
            <td class="formText" align="right">
                Created On (End) :
            </td>
            <td align="left">
                <html:input path="createdOnEnd" id="createdOnEnd" readonly="true" tabindex="-1"
                            cssClass="textBox" maxlength="10"/>
                <img id="eDate" tabindex="5" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif"
                     border="0"/>
                <img id="eDate" tabindex="6" title="Clear Date" name="popcal" onclick="javascript:$('createdOnEnd').value=''"
                     align="middle" style="cursor:pointer" src="images/refresh.png" border="0"/>
            </td>
        </tr>

        <tr>
            <td class="formText" align="right">
                Updated On (Start) :
            </td>
            <td align="left">
                <html:input path="updatedOnStart" id="updatedOnStart" readonly="true" tabindex="-1"
                            cssClass="textBox" maxlength="10"/>
                <img id="bsDate" tabindex="7" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif"
                     border="0"/>
                <img id="bsDate" tabindex="8" title="Clear Date" name="popcal"
                     onclick="javascript:$('updatedOnStart').value=''" align="middle" style="cursor:pointer"
                     src="images/refresh.png" border="0"/>
            </td>
            <td class="formText" align="right">
                Updated On (End) :
            </td>
            <td align="left">
                <html:input path="updatedOnEnd" id="updatedOnEnd" readonly="true" tabindex="-1"
                            cssClass="textBox" maxlength="10"/>
                <img id="beDate" tabindex="9" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif"
                     border="0"/>
                <img id="beDate" tabindex="10" title="Clear Date" name="popcal"
                     onclick="javascript:$('updatedOnEnd').value=''" align="middle" style="cursor:pointer"
                     src="images/refresh.png" border="0"/>
            </td>

        </tr>

        <tr>
            <td align="center" colspan="4" class="formText">
                <input type="submit" class="button" id="searchButton" value="Search" tabindex="11"/>
                <input type="reset" class="button" value="Cancel" name="_reset" tabindex="12"
                       onclick="javascript: window.location='p_blacklistedCNIC.html?actionId=<%=PortalConstants.ACTION_RETRIEVE%>'"/>
            </td>
        </tr>
    </table>
</html:form>

<ec:table filterable="false" items="blacklistedCNICViewModelList" var="model" retrieveRowsCallback="limit"
          filterRowsCallback="limit" sortRowsCallback="limit"
          action="${contextPath}/p_blacklistedCNIC.html?actionId=${retrieveAction}"
          title="" autoIncludeParameters="false">
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
        <ec:exportXls fileName="Blacklisted CNIC Report.xls" tooltip="Export Excel"/>
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
        <ec:exportXlsx fileName="Blacklisted CNIC Report.xlsx" tooltip="Export Excel"/>
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
        <ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
                      headerTitle="Blacklisted CNIC Report" fileName="Blacklisted CNIC Report.pdf"
                      tooltip="Export PDF"/>
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
        <ec:exportCsv fileName="Blacklisted CNIC Report.csv" tooltip="Export CSV"></ec:exportCsv>
    </authz:authorize>
    <ec:row>
        <ec:column property="cnicNo" title="CNIC" escapeAutoFormat="true"/>
        <ec:column property="status" title="Blacklisted" escapeAutoFormat="true" sortable="false"/>
        <ec:column property="createdOn" title="Marked On" cell="date" format="dd/MM/yyyy hh:mm:ss"/>
        <ec:column property="createdByName" title="Marked By"/>

        <ec:column property="updatedOn" title="Last updated On" cell="date" format="dd/MM/yyyy hh:mm:ss"/>
        <ec:column property="updatedByName" title="Last updated By"/>

        <ec:column property="comments" title="Comments" width="250px;"/>
    </ec:row>
</ec:table>

<script language="javascript" type="text/javascript">

 

    function onFormSubmit(form) {


        var _createdOnStartDate = form.createdOnStart.value;
        var _createdOnEndDate = form.createdOnEnd.value;
       
        startlbl = "Created On (Start) :";
        endlbl   = "Created On (End) :";
        var isValidCreatedOn = isValidRange(_createdOnStartDate, _createdOnEndDate, startlbl, endlbl ,serverDate);

        var _updatedOnStartDate = form.updatedOnStart.value;
        var _updatedOnEnd = form.updatedOnEnd.value;
        startlbl = "Updated On (Start) :";
        endlbl   = "Updated On (End) :";
        var isValidUpdated = isValidRange(_updatedOnStartDate,_updatedOnEnd,startlbl,endlbl,serverDate);


        isValid = isValidStartDate && isValidEndDate;

        return isValid;
    }

    function isValidRange(_fDate,_tDate,startlbl,endlbl,serverDate)
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

   
   Calendar.setup(
            {
                inputField  : "createdOnStart", // id of the input field
                button      : "sDate"    // id of the button
            }
    );
    Calendar.setup(
            {
                inputField  : "createdOnEnd", // id of the input field
                button      : "eDate",    // id of the button
                isEndDate: true
            }
    );

    Calendar.setup(
            {
                inputField  : "updatedOnStart", // id of the input field
                button      : "bsDate"    // id of the button
            }
    );
    Calendar.setup(
            {
                inputField  : "updatedOnEnd", // id of the input field
                button      : "beDate",    // id of the button
                isEndDate: true
            }
    );

</script>
<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>

</body>
</html>

