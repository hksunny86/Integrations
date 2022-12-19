<%--
  Created by IntelliJ IDEA.
  User: usret
  Date: 12/21/2021
  Time: 1:17 PM
  To change this template use File | Settings | File Templates.
--%>
<%@include file="/common/taglibs.jsp"%>
<%@page language="java" pageEncoding="ISO-8859-1" import="com.inov8.microbank.common.util.*"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta name="decorator" content="decorator">
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
    <meta name="title" content="Customer AC Nature Marking Bulk Upload" />
    <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/jquery-1.7.2.min.js"></script>
    <script type="text/javascript">
        var jq = $.noConflict();
        jq(document).ready(
            function($)
            {
                $( "#csvFile" ).focus();
                $( "#btnSave" ).click(
                    function() {
                        var isValidForm = true;
                        if($("#csvFile" ).val().length == 0)
                        {
                            alert( "CSV File is required." );
                            isValidForm = false;
                        }
                        else if(!$("#csvFile" ).val().endsWith('.csv'))
                        {
                            alert("Only files with extension (.csv) are allowed.");
                            isValidForm = false;
                        }
                        else if(getFileSize() > 2)
                        {
                            alert("CSV File size can't be greater than 2MB.");
                            isValidForm = false;
                        }

                        if( isValidForm )
                        {
                            $("#customerACNatureMarkingBulkUploadVoForm").submit();
                        }
                        return isValidForm;
                    });

            }
        );

        /**
         *	method : getFileSize()
         *	author : Naseer
         *	description : this method will return file size in mega bytes (mb)
         */
        function getFileSize()
        {
            var file = document.getElementById('csvFile');
            if(file != null && file.files != null && file.files[0] != null)
            {
                return ((file.files[0].size/1024)/1024);
            }
            return 0;
        }
    </script>
</head>
<body>
<c:if test="${not empty messages}">
    <div class="infoMsg" id="successMessages">
        <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false" />
            <br />
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session" />
</c:if>

<%--<c:remove var="bulkCustomerAccountVoList" scope="session" />--%>

<html:form id="customerACNatureMarkingBulkUploadVoForm" commandName="customerACNatureMarkingBulkUploadVo" enctype="multipart/form-data"
           method="POST" action="p_customeracnaturemarkingbulkupload.html">
    <table width="100%" border="0" cellpadding="0" cellspacing="1">

        <tr style="height: 35px">
            <td align="right" bgcolor="F3F3F3" class="formText"
                width="25%">
                <span style="color: #FF0000">*</span>Account Nature:
            </td>
            <td align="left" bgcolor="FBFBFB" width="50%">
                <html:radiobuttons tabindex="27" items="${acNatureMarkingList}" checked="true" itemLabel="label" itemValue="value" path="customerACNature" />
            </td>
        </tr>
        <tr><td> </td><td></td></tr>
        <tr>
            <td align="right" bgcolor="F3F3F3" class="formText"
                width="25%">
                <span style="color: #FF0000">*</span>CSV File:
            </td>
            <td bgcolor="FBFBFB" class="text">
                <spring:bind path="customerACNatureMarkingBulkUploadVo.csvFile">
                    <input type="file" id="csvFile" name="csvFile" tabindex="1" class="upload"/>
                </spring:bind>
            </td>
        </tr>
        <tr>
            <td width="50%">&nbsp;</td>
            <td align="left">
                <input type="hidden" name="isUpdate" value="true"/>
                <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_UPDATE%>" />
                <input type="button" id="btnSave" value="  Preview  " tabindex="2" class="button" />
            </td>
        </tr>
    </table>
</html:form>
</body>
</html>

