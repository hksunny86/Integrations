<%@include file="/common/taglibs.jsp" %>
<%@ page import='com.inov8.microbank.common.util.PortalConstants' %>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %>
</c:set>

<html>
<head>
    <meta name="decorator" content="decorator">
    <link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css" type="text/css"/>
    <meta name="title" content=""/>

</head>
<body bgcolor="#ffffff" onunload="javascript:closeChild();">

<div class="">
    <table class="tableRegion">
        <tr class="odd">
            <td class="">
                Usecase Levels cannot be changed until resolution of following authorization requests. Kindly resolve
                following requests to proceed.
            </td>
        </tr>
    </table>
</div>

<ec:table items="actionAuthorizationModellist" var="actionAuthorizationModel"
          action="${contextPath}/p_conflictedauthorizationrequests.html"
          title="s" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" filterable="false">
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
        <ec:exportXls fileName="Conflicted Authorization Requests.xls" tooltip="Export Excel"/>
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
        <ec:exportXlsx fileName="Conflicted Authorization Requests.xlsx" tooltip="Export Excel"/>
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
        <ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
                      headerTitle="Conflicted Authorization Requests"
                      fileName="Conflicted Authorization Requests.pdf" tooltip="Export PDF"/>
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
        <ec:exportCsv fileName="Conflicted Authorization Requests.csv" tooltip="Export CSV"></ec:exportCsv>
    </authz:authorize>

    <ec:row>
        <ec:column property="actionAuthorizationId" title="Action ID" escapeAutoFormat="True"
                   style="text-align: center"/>
        <ec:column property="relationUsecaseIdUsecaseModel.name" title="Action Type" escapeAutoFormat="True"
                   style="text-align: center"/>
        <ec:column property="relationActionStatusIdActionStatusModel.name" title="Authorization Status"
                   escapeAutoFormat="True" style="text-align: center"/>
        <ec:column property="escalationLevel" title="Escalation Level" escapeAutoFormat="True"
                   style="text-align: center"/>
        <ec:column property="relationCreatedByAppUserModel.username" title="Created By" escapeAutoFormat="true"/>
        <ec:column property="createdOn" cell="date" format="dd/MM/yyyy hh:mm a" title="Created On"/>
        <ec:column alias="" title="" style="text-align: center" sortable="false" viewsAllowed="html">
            <input type="button" class="button" value="Resolve"
                   name="ActionDetail${actionAuthorizationModel.actionAuthorizationId}"
                   onclick="openActionDetailWindow(${actionAuthorizationModel.actionAuthorizationId})"/>
        </ec:column>

    </ec:row>
</ec:table>
<div class="" style="text-align: center; padding-top: 10px;">
    <input type="button" class="button" value="Back"
           onclick="javascript: window.location='p_usecasemanagementform.html?usecaseId=${param.usecaseId}'"/>
</div>

<script language="javascript" type="text/javascript">

    function openActionDetailWindow(actionAuthorizationId) {
        var usecaseId = ${param.usecaseId};
        var url = null;
        var popupWidth = 700;
        var popupHeight = 550;
        var popupLeft = (window.screen.width - popupWidth) / 2;
        var popupTop = (window.screen.height - popupHeight) / 2;


        if (usecaseId == <%=PortalConstants.ONE_TIME_PIN_RESET_USECASE_ID %>)
            url = 'p_resettransactioncodeauthorizationDetail.html?resolveRequest=true&actionAuthorizationId=' + actionAuthorizationId;
        else if (usecaseId ==<%=PortalConstants.RESEND_SMS_USECASE_ID %>)
            url = 'p-resendsmsauthorizationdetail.html?resolveRequest=true&actionAuthorizationId=' + actionAuthorizationId;
        else if ((usecaseId ==<%=PortalConstants.RESET_AGENT_PASSWORD_PORTAL_USECASE_ID%>) || (usecaseId ==<%=PortalConstants.RESET_USER_PASSWORD_PORTAL_USECASE_ID%>))
            url = 'p-resetportalpasswordauthorizationdetail.html?resolveRequest=true&actionAuthorizationId=' + actionAuthorizationId;
        else if ((usecaseId ==<%=PortalConstants.DEACTIVATE_CUSTOMER_USECASE_ID%>) || (usecaseId ==<%=PortalConstants.BLOCK_CUSTOMER_USECASE_ID%>)
            || (usecaseId ==<%=PortalConstants.REACTIVATE_CUSTOMER_USECASE_ID%>) || (usecaseId ==<%=PortalConstants.UNBLOCK_CUSTOMER_USECASE_ID%>)
            || (usecaseId ==<%=PortalConstants.DEACTIVATE_AGENT_USECASE_ID%>) || (usecaseId ==<%=PortalConstants.BLOCK_AGENT_USECASE_ID%>)
            || (usecaseId ==<%=PortalConstants.REACTIVATE_AGENT_USECASE_ID%>) || (usecaseId ==<%=PortalConstants.UNBLOCK_AGENT_USECASE_ID%>))
            url = 'p-lockunlockaccountauthorizationdetail.html?resolveRequest=true&actionAuthorizationId=' + actionAuthorizationId;
        else if (usecaseId ==<%=PortalConstants.LINK_PAYMENT_MODE_USECASE_ID %>)
            url = 'p-linkpaymentmodeauthorizationdetail.html?resolveRequest=true&actionAuthorizationId=' + actionAuthorizationId;
        else if (usecaseId == <%=PortalConstants.DELETE_PAYMENT_MODE_USECASE_ID %> || usecaseId == <%=PortalConstants.DELINK_PAYMENT_MODE_USECASE_ID%>
            || usecaseId ==<%=PortalConstants.RELINK_PAYMENT_MODE_USECASE_ID%>)
            url = 'p-allpaypaymentmodeauthorizationdetail.html?resolveRequest=true&actionAuthorizationId=' + actionAuthorizationId;
        else if (usecaseId ==<%=PortalConstants.I8_USER_MANAGEMENT_CREATE_USECASE_ID %>)
            url = 'p-createnewuserauthorizationdetailfrom.html?resolveRequest=true&actionAuthorizationId=' + actionAuthorizationId;
        else if (usecaseId ==<%=PortalConstants.I8_USER_MANAGEMENT_UPDATE_USECASE_ID %>)
            url = 'p-updateuserauthorizationdetailform.html?resolveRequest=true&actionAuthorizationId=' + actionAuthorizationId;
        else if (usecaseId ==<%=PortalConstants.RETAILER_CONTACT_FORM_USECASE_ID%>)
            url = 'p-createagentauthorizationdetailform.html?resolveRequest=true&actionAuthorizationId=' + actionAuthorizationId;
        else if (usecaseId ==<%=PortalConstants.RETAILER_FORM_USECASE_ID%>)
            url = 'p-createfranchiseauthorizationdetailform.html?resolveRequest=true&actionAuthorizationId=' + actionAuthorizationId;
        else if (usecaseId ==<%=PortalConstants.RETAILER_FORM_UPDATE_USECASE_ID%>)
            url = 'p-updatefranchiseauthorizationdetailform.html?resolveRequest=true&actionAuthorizationId=' + actionAuthorizationId;
        else if (usecaseId ==<%=PortalConstants.MFS_ACCOUNT_CREATE_USECASE_ID%>)
            url = 'p-createcustomerauthorizationdetail.html?resolveRequest=true&actionAuthorizationId=' + actionAuthorizationId;
        else if (usecaseId ==<%=PortalConstants.MFS_ACCOUNT_UPDATE_USECASE_ID%>)
            url = 'p-updatecustomerauthorizationdetail.html?resolveRequest=true&actionAuthorizationId=' + actionAuthorizationId;
        else if (usecaseId == <%=PortalConstants.USER_GROUP_CREATE_USECASE_ID%> || usecaseId ==<%=PortalConstants.USER_GROUP_UPDATE_USECASE_ID%>)
            url = 'p-usergroupauthorizationdetail.html?resolveRequest=true&actionAuthorizationId=' + actionAuthorizationId;
        else if (usecaseId ==<%=PortalConstants.MANUAL_ADJUSTMENT_USECASE_ID%>)
            url = 'p-manualadjustmentauthorizationdetail.html?actionAuthorizationId=' + actionAuthorizationId + '&resolveRequest=true';
        else if (usecaseId ==<%=PortalConstants.CREATE_L2_USECASE_ID%>)
            url = 'p-createl2accountauthorizationdetail.html?actionAuthorizationId=' + actionAuthorizationId + '&resolveRequest=true';
        else if (usecaseId ==<%=PortalConstants.UPDATE_L2_USECASE_ID%>)
            url = 'p-updatel2accountauthorizationdetail.html?actionAuthorizationId=' + actionAuthorizationId + '&resolveRequest=true';
        else if (usecaseId ==<%=PortalConstants.CREATE_L3_USECASE_ID%>)
            url = 'p-createl3accountauthorizationdetail.html?actionAuthorizationId=' + actionAuthorizationId + '&resolveRequest=true';
        else if (usecaseId ==<%=PortalConstants.UPDATE_L3_USECASE_ID%>)
            url = 'p-updatel3accountauthorizationdetail.html?actionAuthorizationId=' + actionAuthorizationId + '&resolveRequest=true';
        else if (usecaseId ==<%=PortalConstants.UPDATE_P2P_DETAILS_USECASE_ID%>)
            url = 'p-updatep2ptxauthorizationdetail.html?actionAuthorizationId=' + actionAuthorizationId + '&resolveRequest=true';
        else if (usecaseId ==<%=PortalConstants.UPDATE_USECASE%>)
            url = 'p-updateusecaseauthorizationdetail.html?actionAuthorizationId=' + actionAuthorizationId + '&resolveRequest=true';
        else if (usecaseId == <%=PortalConstants.UPDATE_CUSTOMER_MOBILE_USECASE_ID%> || usecaseId == <%=PortalConstants.UPDATE_AGENT_MOBILE_USECASE_ID%> || usecaseId ==<%=PortalConstants.UPDATE_HANDLER_MOBILE_USECASE_ID%>)
            url = 'p-updatemobileauthorizationdetail.html?actionAuthorizationId=' + actionAuthorizationId + '&escalateRequest=true&resolveRequest=false';
        else if (usecaseId == <%=PortalConstants.UPDATE_CUSTOMER_ID_DOC_NO_USECASE_ID%> || usecaseId == <%=PortalConstants.UPDATE_AGENT_ID_DOC_NO_USECASE_ID%> || usecaseId ==<%=PortalConstants.UPDATE_HANDLER_ID_DOC_NO_USECASE_ID%>)
            url = 'p-updatecnicauthorizationdetail.html?actionAuthorizationId=' + actionAuthorizationId + '&escalateRequest=true&resolveRequest=false';
        else if (usecaseId ==<%=PortalConstants.UPDATE_AGENT_GUOUP_TAGGING_USECASE_ID%>)
            url = 'p-agenttaggingauthorizationdetail.html?actionAuthorizationId=' + actionAuthorizationId + '&resolveRequest=true';
        else if (usecaseId ==<%=PortalConstants.CREATE_AGENT_GUOUP_TAGGING_USECASE_ID%>)
            url = 'p-agenttaggingauthorizationdetail.html?actionAuthorizationId=' + actionAuthorizationId + '&resolveRequest=true';
        else if (usecaseId ==<%=PortalConstants.TRANS_REDEMPTION_USECASE_ID%>)
            url = 'p-trxredeemauthorizationdetail.html?actionAuthorizationId=' + actionAuthorizationId + '&resolveRequest=true';
        else if (usecaseId ==<%=PortalConstants.TRANSACTION_REVERSAL_USECASE_ID%>)
            url = 'p-manualreversalauthorizationdetail.html?actionAuthorizationId=' + actionAuthorizationId + '&resolveRequest=true';
        else if (usecaseId ==<%=PortalConstants.BULK_MANUAL_ADJUSTMENT_USECASE_ID%>)
            url = 'p-bulkmanualadjustmentauthorizationdetail.html?actionAuthorizationId=' + actionAuthorizationId + '&resolveRequest=true';
        else if (usecaseId ==<%=PortalConstants.UPDATE_ACCOUNT_TO_BLINK_USECASE_ID%>)
            url = 'p-updatecustomerauthorizationdetail.html?actionAuthorizationId=' + actionAuthorizationId + '&resolveRequest=true';
        else if (usecaseId ==<%=PortalConstants.UPDATE_CUSTOMER_NAME_USECASE_ID%>)
            url = 'p_updateCustomerNameAauthorization.html?actionAuthorizationId='+actionAuthorizationId+'&resolveRequest=true';
        newWindow = window.open(url, 'Action Detail Window', 'width=' + popupWidth + ',height=' + popupHeight + ',menubar=no,toolbar=no,left=' + popupLeft + ',top=' + popupTop + ',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
        if (window.focus) newWindow.focus();
        return false;
    }

    function closeChild() {
        try {
            if (childWindow != undefined) {
                childWindow.close();
                childWindow = undefined;
            }
        } catch (e) {
        }
    }
</script>
</body>
</html>
