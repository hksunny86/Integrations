<%--
  Created by IntelliJ IDEA.
  User: usret
  Date: 12/22/2021
  Time: 12:03 PM
  To change this template use File | Settings | File Templates.
--%>
<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <meta name="decorator" content="decorator">
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
    <meta name="title" content="Customer AC Nature Marking Bulk" />
    <link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
    <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/prototype.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/toolbar.js"></script>
</head>

<body bgcolor="#ffffff">
<c:if test="${not empty messages && param.invalidRecordsCount==0}">
    <div class="infoMsg" id="successMessages">
        <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false" />
            <br />
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session" />
</c:if>
<c:if test="${not empty customerACNatureMarkingBulkUploadVoList && not param.allRecordsInvalid}">
    <html:form name="customerACNatureMarkingBulkUploadVoForm" commandName="CustomerACNatureMarkingUploadVo" method="post" action="p_customeracnaturemarkingbulkupload.html">
        <table width="750px" border="0">
            <tr>
                <td class="formText">
                    <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_CREATE%>" />
                    <input type="hidden" name="invalidRecordsCount" value="${param.invalidRecordsCount}"/>
                    <input id="_save" name="_save" type="submit" value="Update Records" tabindex="1" onclick="javascript:disableSubmit();"
                           class="button" />&nbsp;
                    <input type="button" class="button" value="Cancel" tabindex="2" onClick="javascript: window.location='p_customeracnaturemarkingbulkupload.html'" />
                </td>
            </tr>
        </table>
    </html:form>
</c:if>
<c:if test="${not empty customerACNatureMarkingBulkUploadVoList && param.invalidRecordsCount > 0}">
    <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_CREATE%>" />
    <input type="hidden" name="invalidRecordsCount" value="${param.invalidRecordsCount}"/>
    <input type="hidden" name="usecaseId" value="${param.usecaseId}">
    <table width="750px" border="0">
        <tr>
            <td class="formText" style="color:red;">One or more records already exist. Kindly fix and upload again.</td>
        </tr>
        <tr>
            <td class="formText">
                <a href="${contextPath}/p_downloadcustomeracnaturemarkinngbulkrecordsfile.html" tabindex="1">Export Invalid Records</a>
                <a href="${contextPath}/p_customeracnaturemarkingbulkupload.html">Cancel</a>
            </td>
        </tr>
    </table>
</c:if>

<ec:table items="customerACNatureMarkingBulkUploadVoList" action="${contextPath}/p_customeracnaturemarkingbulkuploadpreview.html"
          title="" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" filterable="false" sortable="false">
    <ec:row interceptor="customerACNatureMarkingErrorRowInterceptor">
        <ec:column property="cnic" title="CNIC" style="text-align:center;"/>
        <ec:column property="status" title="Status" style="text-align:center;" />
    </ec:row>
</ec:table>
&nbsp;<input type="button" class="button" value="Back" tabindex="48" onclick="javascript: window.location.href='p_customeracnaturemarkingbulkupload.html?actionId=1'" />
<script language="javascript" type="text/javascript">
    function disableSubmit()
    {
        document.forms.customerACNatureMarkingBulkUploadVoForm._save.disabled=true;
        onSave(document.forms.customerACNatureMarkingBulkUploadVoForm,null);
    }
</script>
</body>
</html>

