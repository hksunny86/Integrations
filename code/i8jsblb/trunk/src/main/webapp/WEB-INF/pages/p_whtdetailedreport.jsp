<%--
  Created by IntelliJ IDEA.
  User: Attique
  Date: 8/22/2017
  Time: 10:34 AM
  To change this template use File | Settings | File Templates.
--%>
<%@include file="/common/taglibs.jsp"%>
<%@ page import="com.inov8.microbank.common.util.PortalConstants" %>
<%@ page import="com.inov8.microbank.common.util.PortalDateUtils" %>
<%@ page import="com.inov8.microbank.common.util.UserTypeConstantsInterface" %>
<%@ page import="com.inov8.microbank.common.util.UserUtils" %>
<html>
<head>
    <meta name="decorator" content="decorator-simple">
    <script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/prototype.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/date-validation.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/popup.js"></script>
    <link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
    <link rel="stylesheet" type="text/css" href="${contextPath}/styles/deliciouslyblue/calendar.css" />
    <script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
    <meta name="title" content="WHT Detailed Report" />
    <script language="javascript" type="text/javascript">
        var jq=$.noConflict();
        var serverDate ="<%=PortalDateUtils.getServerDate()%>";
        function error(request)
        {
            alert("An unknown error has occured. Please contact with the administrator for more details");
        }
    </script>
    <%
        String txHistoryPermission = PortalConstants.ADMIN_GP_READ + "," + PortalConstants.PG_GP_READ + "," +PortalConstants.RPT_SRCH_WALK_IN_TX_HIST_READ;
        String cashPaymentPermission = PortalConstants.ADMIN_GP_READ + "," + PortalConstants.PG_GP_READ + "," + PortalConstants.RPT_SRCH_WALK_IN_CP_READ;
    %>
</head>
<body>

<div id="rsp" class="ajaxMsg"></div>

<c:if test="${not empty messages}">
    <div class="infoMsg" id="successMessages">
        <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false" /><br/>
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session" />
</c:if>

<html:form name='whtDetailedReportform' commandName="whtDetailedReportModel" method="post"
           action="p_whtdetailedreport.html" onsubmit="return validateThisForm(this)" >
    <table width="800px"  border="0">
        <tr>
            <td align="right" class="formText" width="18%">
                Sending Agent Id:
            </td>
            <td align="left" width="32%">
                <html:input path="agent1Id" id="ag" cssClass="textBox"  tabindex="1" onkeypress="return maskNumber(this,event)"/>
            </td>
            <td align="right" class="formText" width="20%">
                Sending Agent Mobile#:
            </td>
            <td align="left" width="24%">
                <html:input path="agent1Mobile" id="cnic" cssClass="textBox"  tabindex="1" onkeypress="return maskNumber(this,event)"/>
            </td>
        </tr>
        <tr>
            <td align="right" class="formText" width="18%">
                Sending Customer Id:
            </td>
            <td align="left" width="32%">
                <html:input path="senderId" id="cnic" cssClass="textBox"  tabindex="1" onkeypress="return maskNumber(this,event)"/>
            </td>
            <td align="right" class="formText" width="20%">
                Sender Customer Mobile#:
            </td>
            <td align="left" width="24%">
                <html:input path="senderMobile" id="cnic" cssClass="textBox"  tabindex="1" onkeypress="return maskNumber(this,event)"/>
            </td>
        </tr>
        <tr>
            <td align="right" class="formText" width="18%">
                Receving Agent Id:
            </td>
            <td align="left" width="32%">
                <html:input path="agent2Id" id="cnic" cssClass="textBox"  tabindex="1" onkeypress="return maskNumber(this,event)"/>
            </td>
            <td align="right" class="formText" width="18%">
                Receiving Agent Mobile#:
            </td>
            <td align="left" width="32%">
                <html:input path="agent2Mobile" id="cnic" cssClass="textBox"  tabindex="1" onkeypress="return maskNumber(this,event)"/>
            </td>
        </tr>
        <tr>
            <td align="right" class="formText" width="18%">
                Receiving Customer Id:
            </td>
            <td align="left" width="32%">
                <html:input path="recepientId" id="cnic" cssClass="textBox"  tabindex="1" onkeypress="return maskNumber(this,event)"/>
            </td>
            <td align="right" class="formText" width="18%">
                Receiving Customer Mobile#:
            </td>
            <td align="left" width="32%">
                <html:input path="recepientMobile" id="cnic" cssClass="textBox"  tabindex="1" onkeypress="return maskNumber(this,event)"/>
            </td>
        </tr>
        <tr>
            <td align="right" class="formText" width="18%">
                Sending Agent CNIC:
            </td>
            <td align="left" width="32%">
                <html:input path="agent1Cnic" id="cnic" cssClass="textBox"  tabindex="1" onkeypress="return maskNumber(this,event)"/>
            </td>
            <td align="right" class="formText" width="18%">
                Sending Customer CNIC:
            </td>
            <td align="left" width="32%">
                <html:input path="senderCnic" id="cnic" cssClass="textBox"  tabindex="1" onkeypress="return maskNumber(this,event)"/>
            </td>
        </tr>
        <tr>
            <td align="right" class="formText" width="18%">
                Receiving Agent CNIC:
            </td>
            <td align="left" width="32%">
                <html:input path="agent2Cnic" id="cnic" cssClass="textBox"  tabindex="1" onkeypress="return maskNumber(this,event)"/>
            </td>
            <td align="right" class="formText" width="18%">
                Receiving Customer CNIC:
            </td>
            <td align="left" width="32%">
                <html:input path="recepientCnic" id="cnic" cssClass="textBox"  tabindex="1" onkeypress="return maskNumber(this,event)"/>
            </td>
        </tr>








        <tr>
            <td align="right" class="formText" width="18%">
                Transaction Id:
            </td>
            <td align="left" width="32%">
                <html:input path="transactionId" id="cnic" cssClass="textBox"  tabindex="1" onkeypress="return maskNumber(this,event)"/>
            </td>
            <td align="right" class="formText" width="18%">
                Status:
            </td>
            <td align="left" width="32%">
                <html:select id="serviceId" path="statusId" cssClass="textBox" tabindex="1">
                    <html:option value="">--- All ---</html:option>
                    <c:if test="${supplierStatus != null}">
                        <html:options items="${supplierStatus}" itemValue="supProcessingStatusId" itemLabel="name"/>
                    </c:if>
                </html:select>
            </td>
        </tr>


        <tr>
            <td align="right" class="formText" width="18%">
                Product:
            </td>
            <td align="left" width="32%">
                <html:select id="serviceId" path="productId" cssClass="textBox" tabindex="1">
                    <html:option value="">--- All ---</html:option>
                    <c:if test="${productList != null}">
                        <html:options items="${productList}" itemValue="productId" itemLabel="name"/>
                    </c:if>
                </html:select>
            </td>
            <td align="right" class="formText" width="18%">
                Tax Type:
            </td>
            <td align="left" width="32%">
                <html:select id="whtConfigId" path="whtConfigId" cssClass="textBox" tabindex="1">
                    <html:option value="">--- All ---</html:option>
                    <c:if test="${whtConfigList != null}">
                        <html:options items="${whtConfigList}" itemValue="value" itemLabel="label"/>
                    </c:if>
                </html:select>
            </td>
        </tr>
        <tr>
            <td align="right" class="formText" width="18%">
                Tax Payer Category:
            </td>
            <td align="left" width="32%">
                <html:select id="whtConfigId" path="taxPayerCategory" cssClass="textBox" tabindex="1">
                    <html:option value="">--- All ---</html:option>
                    <c:if test="${filerList != null}">
                        <html:options items="${filerList}" itemValue="value" itemLabel="label"/>
                    </c:if>
                </html:select>
            </td>

        </tr>


        <tr>
            <td align="right" class="formText">
                <span class="asterisk">*</span>
                Transaction Last Updated From:
            </td>
            <td align="left">
                <html:input path="transactionStartDate" id="startDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
                <img id="sDate" tabindex="3" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                <img id="sDate" tabindex="4" title="Clear Date" name="popcal" align="middle" onclick="javascript:$('startDate').value=''" style="cursor:pointer" src="images/refresh.png" border="0" />
            </td>
            <td align="right" class="formText">
                <span class="asterisk">*</span>
                Transaction Last Updated To:
            </td>
            <td align="left">
                <html:input path="transactionEndDate" id="endDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
                <img id="eDate" tabindex="5" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                <img id="eDate" tabindex="6" title="Clear Date" name="popcal" align="middle" style="cursor:pointer" onclick="javascript:$('endDate').value=''" src="images/refresh.png" border="0" />
            </td>
        </tr>


        <tr>
            <td>&nbsp;</td>
            <td align="left">
                <input id="search" name="_search" type="submit" class="button" value="Search" tabindex="7" />
                <input name="reset" type="reset"
                       onclick="javascript: window.location='p_whtdetailedreport.html'"
                       class="button" value="Cancel" tabindex="8" />
            </td>
            <td colspan="2">&nbsp;</td>
        </tr>
        <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
    </table>
</html:form>

<ec:table items="whtDetailViewModelList" var="whtDetailModel"
          action="${pageContext.request.contextPath}/p_whtdetailedreport.html"
          title="" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" filterable="false" width="1100px">
    <%--<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
        <ec:exportXls fileName="Fed-Detail-Report.xls" tooltip="Export Excel" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
        <ec:exportXlsx fileName="Fed-Detail-Report.xlsx" tooltip="Export Excel" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
        <ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
                      headerTitle="Fed Detail Report"
                      fileName="Fed-Detail-Report.pdf" tooltip="Export PDF" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
        <ec:exportCsv fileName="Fed-Detail-Report.csv" tooltip="Export CSV"></ec:exportCsv>
    </authz:authorize>--%>
    <ec:row>
        <%--  <ec:column property="createdOn" cell="date" format="dd/MM/yyyy hh:mm a" title="Registered On" width="14%"/>
          <ec:column property="registeredBy" title="Registered By Agent ID"/>

          --%>
        <ec:column property="taxPayerId" title="Tax Payer Id" filterable="false" escapeAutoFormat="True" style="text-align: left"/>

        <ec:column property="accountType" title="Account Type" escapeAutoFormat="True" style="text-align: left"/>
         <ec:column property="productName" title="Product" escapeAutoFormat="True" style="text-align: left"/>
        <ec:column property="taxPayerCategory" title="Tax Payer Category" filterable="false" escapeAutoFormat="True" style="text-align: left"/>
                <ec:column property="taxRate" title="Tax Rate %" escapeAutoFormat="True" style="text-align: right">
                    ${whtDetailModel.taxRate}
                </ec:column>

        <ec:column property="transactionId" title="Transaction Id" escapeAutoFormat="True" style="text-align: left"/>

        <ec:column property="transactionDate" title="Transaction Date" cell="date" format="dd/MM/yyyy"  style="text-align: left"/>
        <ec:column property="transactionLastUpdatedOn" title="Last Updated On" cell="date" format="dd/MM/yyyy"  style="text-align: left"/>

        <ec:column property="agent1Cnic" title="Agent 1 Cnic" escapeAutoFormat="True" style="text-align: left"/>
        <ec:column property="agent1Id" title="Agent 1 Id" escapeAutoFormat="True" style="text-align: left"/>
        <ec:column property="agent1Mobile" title="Agent 1 Mobile" escapeAutoFormat="True" style="text-align: left"/>
        <ec:column property="senderCnic" title="Sender Cnic" escapeAutoFormat="True" style="text-align: left"/>
        <ec:column property="senderId" title="Sender Id" escapeAutoFormat="True" style="text-align: left"/>
        <ec:column property="senderMobile" title="Sender Mobile" escapeAutoFormat="True" style="text-align: left"/>
        <ec:column property="agent2Cnic" title="Agent 2 Cnic" escapeAutoFormat="True" style="text-align: left"/>
        <ec:column property="agent2Id" title="Agent 2 Id" escapeAutoFormat="True" style="text-align: left"/>
        <ec:column property="agent2Mobile" title="Agent 2 Mobile" escapeAutoFormat="True" style="text-align: left"/>
        <ec:column property="recepientCnic" title="Recepient Cnic" escapeAutoFormat="True" style="text-align: left"/>
        <ec:column property="recepientId" title="Recepient Id" escapeAutoFormat="True" style="text-align: left"/>
        <ec:column property="recepientMobile" title="Recepient Mobile" escapeAutoFormat="True" style="text-align: left"/>
        <ec:column property="amount" title="Amount" calc="total" calcTitle="Total:" format="0.00" escapeAutoFormat="True" style="text-align: right"/>
        <ec:column property="inclusiveCharges" calc="total" calcTitle="Total:" format="0.00" title="Inclusive Charges" escapeAutoFormat="True" style="text-align: right"/>
        <ec:column property="exclusiveCharges" calc="total" calcTitle="Total:" format="0.00" title="Exclusive Charges" escapeAutoFormat="True" style="text-align: right"/>
        <ec:column property="bankComission" calc="total" calcTitle="Total:" format="0.00" title="Bank Comission" escapeAutoFormat="True" style="text-align: right"/>
        <ec:column property="a231" title="231A" escapeAutoFormat="True" calc="total" calcTitle="Total:" format="0.00" style="text-align: right"/>
        <ec:column property="p236" title="236P" escapeAutoFormat="True" style="text-align: right" calc="total" calcTitle="Total:" format="0.00" />
        <ec:column property="agent1233" title="Agent1 233/2" calc="total" calcTitle="Total:" format="0.00" escapeAutoFormat="True" style="text-align: right"/>
        <ec:column property="agent2233" title="Agent2 233/2" calc="total" calcTitle="Total:" format="0.00" escapeAutoFormat="True" style="text-align: right"/>
        <ec:column property="agent1GrossComission" title="Agent 1 Gross Comission" calc="total" calcTitle="Total:" format="0.00" escapeAutoFormat="True" style="text-align: right"/>
        <ec:column property="agent1Wht" title="Agent 1 WHT" calc="total" calcTitle="Total:" format="0.00" escapeAutoFormat="True" style="text-align: right"/>
        <ec:column property="agent1NetComission" title="Agent 1 Net Comission" calc="total" calcTitle="Total:" format="0.00" escapeAutoFormat="True" style="text-align: right"/>
        <ec:column property="agent2GrossComission" title="Agent 2 Gross Comission" calc="total" calcTitle="Total:" format="0.00" escapeAutoFormat="True" style="text-align: right"/>
        <ec:column property="agent2Wht" title="Agent 2 WHT" calc="total" calcTitle="Total:" format="0.00" escapeAutoFormat="True" style="text-align: right"/>
        <ec:column property="agent2NetComission" title="Agent 2 Net Comission" calc="total" calcTitle="Total:" format="0.00" escapeAutoFormat="True" style="text-align: right"/>
        <ec:column property="salesTeamGrossComission" title="SalesTeam Comission" calc="total" calcTitle="Total:" format="0.00" escapeAutoFormat="True" style="text-align: right"/>
        <ec:column property="salesTeamWht" title="SalesTeam WHT" escapeAutoFormat="True" calc="total" calcTitle="Total:" format="0.00" style="text-align: right"/>
        <ec:column property="salesTeamNetComission" title="SalesTeam Net Comission" escapeAutoFormat="True" style="text-align: right" calc="total" calcTitle="Total:" format="0.00"/>
        <ec:column property="blbSettlementComission" title="BLB Settlement Comission" escapeAutoFormat="True" style="text-align: right" calc="total" calcTitle="Total:" format="0.00"/>
        <ec:column property="totalWht" title="Total WHT" calc="total" calcTitle="Total:" format="0.00" escapeAutoFormat="True" style="text-align: right"/>
        <ec:column property="statusName" title="Status" escapeAutoFormat="True" style="text-align: right"/>





        <%-- <ec:column property="mobileNo" title="Tagged MSISDN" escapeAutoFormat="True" style="text-align: right"/>

         <ec:column property="transactionsAsSender" title="Transactions As Sender" format="0" calc="total" calcTitle="Total:" style="text-align: right"/>
         <ec:column property="debitLimit" title="Remaning Debit Limit" cell="currency"  format="0.00"  style="text-align: right"/>
         <ec:column property="transactionsAsReceiver" title="Transactions As Receiver" format="0" calc="total" calcTitle="Total:" style="text-align: right"/>
         <ec:column property="creditLimit" title="Remaning Credit Limit" cell="currency"  format="0.00"  style="text-align: right"/>
         <ec:column property="noOfTransactions" title="Total Transactions" format="0" calc="total" calcTitle="Total:" style="text-align: right"/>
         <ec:column property="lastTransactionDate" cell="date" format="dd/MM/yyyy hh:mm a" title="Last Transaction On" width="14%"/>
         <ec:column alias="" title="Re-Tag" sortable="false" viewsAllowed="html" style="text-align: center">
             <c:choose>
                 <c:when test="${walkInCustomerViewModel.noOfTransactions == 0}">
                     <input type="button" class="button" value="Re-Tag" name="Retag${walkInCustomerViewModel.cnic}" disabled="disabled"/>
                 </c:when>
                 <c:otherwise>
                     <c:choose>
                         <c:when test="${isBank}">
                             <input type="button" class="button" value="Re-Tag" name="Retag${walkInCustomerViewModel.cnic}"
                                    onclick="javascript:document.location='p_retagmobilecnic.html?actionId=2&appUserId=${walkInCustomerViewModel.appUserId}&cnic=${walkInCustomerViewModel.cnic}&mobileNo=${walkInCustomerViewModel.mobileNo}'" />

                         </c:when>
                         <c:otherwise>
                             <input type="button" class="button" value="Re-Tag" name="Retag${walkInCustomerViewModel.cnic}" disabled="disabled"/>
                         </c:otherwise>
                     </c:choose>
                     &lt;%&ndash;								<input type="button" class="button" value="Re-Tag" name="Retag${walkInCustomerViewModel.cnic}"
                                                            onclick="javascript:document.location='p_retagmobilecnic.html?actionId=2&appUserId=${walkInCustomerViewModel.appUserId}&cnic=${walkInCustomerViewModel.cnic}&mobileNo=${walkInCustomerViewModel.mobileNo}'" />	&ndash;%&gt;

                 </c:otherwise>
             </c:choose>
         </ec:column>
         <ec:column alias="" title="Transaction History" sortable="false" viewsAllowed="html" style="text-align: center">
             <c:choose>
                 <c:when test="${walkInCustomerViewModel.noOfTransactions == 0}">
                     <input type="button" class="button" value="Transaction History" name="TransactionHistory${walkInCustomerViewModel.cnic}" disabled="disabled"/>
                 </c:when>
                 <c:otherwise>
                     <authz:authorize ifAnyGranted="<%=txHistoryPermission%>">
                         <input type="button" class="button" value="Transaction History" name="TransactionHistory${walkInCustomerViewModel.cnic}"
                                onclick="javascript:document.location='p_walkincustomerltransactiondetails.html?actionId=2&cnic=${walkInCustomerViewModel.cnic}'" />
                     </authz:authorize>
                     <authz:authorize ifNotGranted="<%=txHistoryPermission%>">
                         <input type="button" class="button" value="Transaction History" name="TransactionHistory${walkInCustomerViewModel.cnic}"
                                disabled="disabled"/>
                     </authz:authorize>
                 </c:otherwise>
             </c:choose>
         </ec:column>--%>
    </ec:row>
</ec:table>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/searchFormValidator.js"></script>
<script language="javascript" type="text/javascript">
    document.forms[0].ag.focus();
    function validateThisForm(form){
        var currentDate = "<%=PortalDateUtils.getServerDate()%>";
        var _fDate = form.startDate.value;
        var _tDate = form.endDate.value;
        var startlbl = "Start Date";
        var endlbl   = "End Date";
        var isValid = validateDateRangeMandatory(_fDate,_tDate,startlbl,endlbl,currentDate);
        /*if(isValid){
           isValid= validateForm(form);
        }*/
        return isValid;
    }

    Calendar.setup(
            {
                inputField  : "startDate", // id of the input field
                button      : "sDate"    // id of the button
            }
    );
    Calendar.setup(
            {
                inputField  : "endDate", // id of the input field
                button      : "eDate",    // id of the button
                isEndDate: true
            }
    );

</script>
<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>




</body>
</html>
