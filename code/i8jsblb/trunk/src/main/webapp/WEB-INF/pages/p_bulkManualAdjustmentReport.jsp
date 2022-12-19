<%--
  Created by IntelliJ IDEA.
  User: ABC
  Date: 1/10/2017
  Time: 11:49 AM
  To change this template use File | Settings | File Templates.
--%>
<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<%@ page import='com.inov8.microbank.common.util.FinancialInstitutionConstants'%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<html>
<head>
    <script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
    <script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
    <link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
    <link rel="stylesheet"
          href="${contextPath}/styles/extremecomponents.css"
          type="text/css">
    <%@include file="/common/ajax.jsp"%>
    <meta name="title" content="Manual Adjustment Report - Bulk" />
    <meta name="decorator" content="decorator">
    <script language="javascript" type="text/javascript">
        var jq=$.noConflict();
        var serverDate ="<%=PortalDateUtils.getServerDate()%>";


        function error(request) {
            alert("An unknown error has occured. Please contact with the administrator for more details");
        }
    </script>

</head>
<body bgcolor="#ffffff">

    <div id="rsp" class="ajaxMsg"></div>

    <c:if test="${not empty messages}">
        <div class="infoMsg" id="successMessages">
            <c:forEach var="msg" items="${messages}">
                <c:out value="${msg}" escapeXml="false" /><br/>
            </c:forEach>
        </div>
        <c:remove var="messages" scope="session" />
    </c:if>


    <html:form name='bulkManualAdjustmentForm'
               commandName="bulkManualAdjustmentModel" method="post"
               action="p_bulkManualAdjustmentReport.html" onsubmit="return validateForm(this)" >

        <table width="800px" border="0">
            <tr>
                <td class="formText" width="18%" align="right">
                    Transaction ID:
                </td>
                <td align="left" colspan="3">
                    <html:input path="trxnId" cssClass="textBox" maxlength="50" tabindex="1" onkeypress="return maskNumber(this,event)"/>
                </td>
            </tr>
            <tr>
                <td class="formText" width="18%" align="right">
                    Batch ID:
                </td>
                <td align="left" width="32%" >
                    <html:input path="batchId" cssClass="textBox" maxlength="50" tabindex="1" onkeypress="return maskNumber(this,event)"/>
                </td>
                <td class="formText" width="18%" align="right">
                	Skipped By Checker
                </td>
                <td align="left" width="32%" >
                    <html:select  name = "isSkipped" path="isSkipped" class="textBox" tabindex="3">
                        <html:option value="">---All---</html:option>
                        <html:option value="1">Yes</html:option>
                        <html:option value="0">No</html:option>
                    </html:select>
                </td>
            </tr>
            <tr>

                <td class="formText" align="right" width="18%">
                    Adjustment Type:
                </td>
                <td align="left">
                    <html:select path="adjustmentType" tabindex="2" cssClass="textBox">
                        <html:option value="">---All---</html:option>
                        <c:if test="${adjustmentTypeList != null}">
                            <html:options items="${adjustmentTypeList}" itemValue="value" itemLabel="label" />
                        </c:if>
                    </html:select>
                </td>
                <td class="formText" align="right" width="18%">
                    Process Successful:
                </td>
                <td align="left">
                    <html:select  name = "isProcessed" path="isProcessed" class="textBox" tabindex="3">
                        <html:option value="">---All---</html:option>
                        <html:option value="1">Yes</html:option>
                        <html:option value="0">No</html:option>
                    </html:select>
                </td>
            </tr>
            <tr>
                <td class="formText" align="right">
                    From Account No:
                </td>
                <td align="left">
                    <html:input path="fromAccount" cssClass="textBox" maxlength="50" tabindex="4" onkeypress="return maskNumber(this,event)"/>
                </td>
                <td class="formText" align="right">
                    To Account No:
                </td>
                <td align="left" >
                    <html:input path="toAccount" cssClass="textBox" maxlength="11" tabindex="5" onkeypress="return maskNumber(this,event)"/>
                </td>
            </tr>
            <tr>
                <td class="formText" align="right">
                    From Date:
                </td>
                <td align="left">
                    <html:input path="startDate" id="startDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
                    <img id="sDate" tabindex="6" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                    <img id="sDate" tabindex="7" title="Clear Date" name="popcal"  onclick="javascript:$('startDate').value=''"   align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
                </td>
                <td class="formText" align="right">
                    To Date:
                </td>
                <td align="left">
                    <html:input path="endDate" id="endDate"  readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
                    <img id="eDate" tabindex="8" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                    <img id="eDate" tabindex="9" title="Clear Date" name="popcal"  onclick="javascript:$('endDate').value=''"   align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
                </td>
            </tr>
            <tr>
                <td class="formText" align="right">

                </td>
                <td align="left">
                    <input name="_search" type="submit" class="button" value="Search" tabindex="9"/>
                    <input name="reset" type="reset"
                           onclick="javascript: window.location='p_bulkManualAdjustmentReport.html?actionId=${retriveAction}'"
                           class="button" value="Cancel" tabindex="10" />
                </td>
                <td class="formText" align="right"></td>
                <td align="left"></td>
            </tr>

            <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
                   value="<%=PortalConstants.ACTION_RETRIEVE%>">

        </table>
    </html:form>



    <ec:table items="bulkManualAdjustmentModelList" var="bulkManualAdjustmentModel"
              action="${contextPath}/p_bulkManualAdjustmentReport.html?actionId=${retriveAction}"
              title=""
              retrieveRowsCallback="limit"
              filterRowsCallback="limit"
              sortRowsCallback="limit"
              filterable="false">
        <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
            <ec:exportXls fileName="Bulk Manual Adjustment Report.xls" tooltip="Export Excel" />
        </authz:authorize>
        <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
            <ec:exportXlsx fileName="Bulk Manual Adjustment Report.xlsx" tooltip="Export Excel" />
        </authz:authorize>
        <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
            <ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
                          headerTitle="Transaction Details"
                          fileName="Bulk Manual Adjustment Report.pdf" tooltip="Export PDF" />
        </authz:authorize>
        <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
            <ec:exportCsv fileName="Bulk Manual Adjustment Report.csv" tooltip="Export CSV"></ec:exportCsv>
        </authz:authorize>

        <ec:row>
            <ec:column property="trxnId" title="Transaction ID" escapeAutoFormat="true">
                <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
                    <a href="p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode=${bulkManualAdjustmentModel.trxnId}" onClick="return openTransactionWindow('${bulkManualAdjustmentModel.trxnId}')">
                            ${bulkManualAdjustmentModel.trxnId}
                    </a>
                </authz:authorize>
                <authz:authorize ifNotGranted="<%=PortalConstants.PERMS_TRX_DETAIL_POPUP%>">
                    ${bulkManualAdjustmentModel.trxnId}
                </authz:authorize>
            </ec:column>
            <ec:column property="batchId" title="Batch ID" style="text-align: center"/>
            <ec:column property="adjustmentType" title="Adjustment Type" style="text-align: center">
                <c:if test="${bulkManualAdjustmentModel.adjustmentType=='1'}">BB To BB</c:if>
                <c:if test="${bulkManualAdjustmentModel.adjustmentType=='2'}">BB To Core</c:if>
                <c:if test="${bulkManualAdjustmentModel.adjustmentType=='3'}">Core To BB</c:if>
            </ec:column>
            <ec:column property="fromAccount" title="From Account No." escapeAutoFormat="true" style="text-align: center"/>
            <ec:column property="fromAccountTitle" title="From Account Title" style="text-align: center"/>
            <ec:column property="toAccount" title="To Account No." escapeAutoFormat="true"  style="text-align: center"/>
            <ec:column property="toAccountTitle" title="To Account Title" style="text-align: center"/>
            <ec:column property="amount" title="Amount (Rs.)" escapeAutoFormat="true" cell="currency" format="0.00" />
            <ec:column property="createdOn" cell="date" format="dd/MM/yyyy hh:mm a" filterable="false" title="Transaction Date"/>
            <ec:column property="processingStatus" filterable="true" sortable="false" title="Process Successful" style="text-align: center"/>
            <ec:column property="isSkippedValue" filterable="true" sortable="false" title="Skipped By Checker" style="text-align: center"/>
            <ec:column property="description" filterable="false" sortable="false" title="Description"/>
            <ec:column property="errorDescription" filterable="false" sortable="false" title="Error Description"/>

        </ec:row>
    </ec:table>

    <script language="javascript" type="text/javascript">

        function openTransactionWindow(transactionCode)
        {
            var popupWidth = 550;
            var popupHeight = 350;
            var popupLeft = (window.screen.width - popupWidth)/2;
            var popupTop = (window.screen.height - popupHeight)/2;
            newWindow=window.open('p_transactionissuehistorymanagement.html?actionId=${retriveAction}&transactionCode='+transactionCode,'TransactionSummary','width='+popupWidth+',height='+popupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
            if(window.focus) newWindow.focus();
            return false;
        }



        Calendar.setup({

                inputField  : "startDate", // id of the input field
                button      : "sDate",    // id of the button
            }
        );
        Calendar.setup({

                inputField  : "endDate", // id of the input field
                button      : "eDate",    // id of the button
                isEndDate: true
            }
        );
    </script>
    <script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>

</body>
</html>
