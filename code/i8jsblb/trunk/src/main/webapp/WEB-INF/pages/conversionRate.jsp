<!--Author: Mohammad Shehzad Ashraf -->
<%@ page import='com.inov8.microbank.common.util.*,com.inov8.microbank.common.util.PortalConstants'%>
<%@include file="/common/taglibs.jsp"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<c:set var="createAction"><%=PortalConstants.ACTION_CREATE %></c:set>

<html>
<head>
    <meta name="decorator" content="decorator">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
    <meta name="title" content="AirTime Top-Up" />
    <%@include file="/common/ajax.jsp"%>
    <script type="text/javascript" src="${contextPath}/scripts/jquery-1.7.2.min.js"></script>
    <script type="text/javascript">
        var jq=$.noConflict();
        function initProgress()
        {
            $('errorMsg').innerHTML = "";
            $('successMsg').innerHTML = "";
            Element.hide('successMsg');
            Element.hide('errorMsg');
            if($('successMessages') != null){
                $('successMessages').innerHTML = "";
                Element.hide('successMessages');
            }
            if (confirm('If customer information is verified then press OK to continue')==true)
            {
                return true;
            }
            else
            {
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
        }
    </script>

    <%
        String createPermission = PortalConstants.ADMIN_GP_CREATE;
        createPermission +=	"," + PortalConstants.PG_GP_CREATE;
        createPermission +=	"," + PortalConstants.MNG_USRS_CREATE;

        String updatePermission = PortalConstants.ADMIN_GP_UPDATE;
        updatePermission +=	"," + PortalConstants.PG_GP_UPDATE;
        updatePermission +=	"," + PortalConstants.MNG_USRS_UPDATE;

        String userGroupUpdatePermission = PortalConstants.ADMIN_GP_UPDATE;
        userGroupUpdatePermission += "," + PortalConstants.PG_GP_UPDATE;
        userGroupUpdatePermission += "," + PortalConstants.MNG_USR_GRPS_UPDATE;
    %>
</head>

<body bgcolor="#ffffff">
<div id="successMsg" class ="infoMsg" style="display:none;"></div>
<div id="errorMsg" class ="errorMsg" style="display:none;"></div>
<c:if test="${not empty messages}">
    <div class="infoMsg" id="successMessages">
        <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session"/>
</c:if>
<table border="0" width="100%" cellpadding="0" cellspacing="0">
    <tr>
        <td align="right">
            <authz:authorize ifAnyGranted="<%=createPermission%>">
                <a href="javascript: window.location='addTopUpRates.html?actionId=${createAction}'" class="linktext">Add Top Up Rate</a>
            </authz:authorize>
        </td>
    </tr>
</table>

    <ec:table items="airTimeTopUpViewList"
              var="airTimeTopViewModel"
              retrieveRowsCallback="limit"
              filterRowsCallback="limit"
              sortRowsCallback="limit"
              action="${pageContext.request.contextPath}/conversionRate.html?actionId=${retriveAction}"
              title="" filterable="false">

        <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
            <ec:exportXls fileName="Users.xls" tooltip="Export Excel"/>
        </authz:authorize>
        <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
            <ec:exportXlsx fileName="Users.xlsx" tooltip="Export Excel" />
        </authz:authorize>
        <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
            <ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da" headerTitle="Users"
                          fileName="Users.pdf" tooltip="Export PDF" />
        </authz:authorize>
        <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
            <ec:exportCsv fileName="Users.csv" tooltip="Export CSV"></ec:exportCsv>
        </authz:authorize>

        <ec:row>
        <ec:column property="rateTypeName" title="Name" escapeAutoFormat="True" style="text-align: center"/>
        <ec:column property="rate" title="Dollar Rate" escapeAutoFormat="true"/>
        <ec:column property="startDate" cell="date" format="dd/MM/yyyy hh:mm a" title="Start Date"/>
        <ec:column property="endDate" cell="date" format="dd/MM/yyyy hh:mm a" title="End Date"/>
        <ec:column property="createdBy" title="Created By" escapeAutoFormat="true"/>
    </ec:row>
    </ec:table>

<script type="text/javascript">
    function validateAirTimeForm()
    {
        //alert("alert notification");
        var x = document.getElementById("rate").value;
        var y = document.getElementById("topUpRate").value;
        // alert(y);
        if (x <= 0) {
            alert("Dollar Rate must be Greater/Equal One.");
            return false;
        }
        if(y <= 0)
        {
            alert("Top-Rate must be Greater/Equal One.");
            return false;
        }
        return true;
    }
</script>
</body>

</html>




