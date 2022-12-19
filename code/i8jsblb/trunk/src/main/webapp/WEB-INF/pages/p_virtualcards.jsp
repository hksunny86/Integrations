<%--
  Created by IntelliJ IDEA.
  User: Attique
  Date: 7/25/2017
  Time: 2:05 PM
  To change this template use File | Settings | File Templates.
--%>
<%@include file="/common/taglibs.jsp"%>
<%@ page import="com.inov8.microbank.common.util.PortalConstants" %>
<%@ page import="com.inov8.microbank.common.util.PortalDateUtils" %>
<%@ page import="com.inov8.microbank.common.util.UserTypeConstantsInterface" %>
<%@ page import="com.inov8.microbank.common.util.UserUtils" %>
<html:form>
<head>
    <meta name="decorator" content="decorator-simple">
    <script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>

<%--
    F:\Inov8Work\JSFonePayBranch\src\main\webapp\scripts\commonFormValidator.js
--%>

    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar_new.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar-setup.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/lang/calendar-en.js"></script>
    <link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
    <%@include file="/common/ajax.jsp"%>
    <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
    <script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
    <meta name="title" content="Virtual Cards" />
    <script language="javascript" type="text/javascript">
        var jq=$.noConflict();
        var serverDate ="<%=PortalDateUtils.getServerDate()%>";
        function error(request)
        {
            alert("An unknown error has occured. Please contact with the administrator for more details");
        }
    </script>

</head>

<body>

<c:if test="${not empty messages}">
    <div class="infoMsg" id="successMessages">
        <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false" /><br/>
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session" />
</c:if>
    <div class="infomsg">

    </div>

<html:form name='virtualCardsViewForm' id="virtualCardsViewForm" commandName="virtualCardReportModel" method="post"
           action="p_virtualcards.html" onsubmit="return validateForm(this)" >
    <table width="750px" border="0">
        <tr>
            <td align="right" class="formText" width="18%">
                CNIC:
            </td>
            <td align="left" width="32%">
                <html:input path="cnicNo" id="cnic" cssClass="textBox" maxlength="13" tabindex="1" onkeypress="return maskNumber(this,event)"/>
            </td>
            <td align="right" class="formText" width="18%">
                Mobile No.:
            </td>
            <td align="left" width="32%">
                <html:input path="mobileNo" id="mobileNo" cssClass="textBox" maxlength="11" tabindex="2" onkeypress="return maskNumber(this,event)"/>
            </td>
        </tr>
        <tr>
            <td align="right" class="formText" width="18%">
                Customer ID:
            </td>
            <td align="left" width="32%">
                <html:input path="customerId" id="customerId" cssClass="textBox" maxlength="13" tabindex="1" onkeypress="return maskNumber(this,event)"/>
            </td>
            <td align="right" class="formText" width="18%">
                Card No:
            </td>
            <td align="left" width="32%">
                <html:input path="cardNo" id="cardNo" cssClass="textBox" maxlength="13" tabindex="1" onkeypress="return maskNumber(this,event)"/>
            </td>
        </tr>
        <tr>
            <td align="right" class="formText">
                Registered From:
            </td>
            <td align="left">
                <html:input path="startDate" id="startDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
                <img id="sDate" tabindex="3" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                <img id="sDate" tabindex="4" title="Clear Date" name="popcal" align="middle" onclick="javascript:$('startDate').value=''" style="cursor:pointer" src="images/refresh.png" border="0" />
            </td>
            <td align="right" class="formText">
                Registered To:
            </td>
            <td align="left">
                <html:input path="endDate" id="endDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
                <img id="eDate" tabindex="5" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                <img id="eDate" tabindex="6" title="Clear Date" name="popcal" align="middle" style="cursor:pointer" onclick="javascript:$('endDate').value=''" src="images/refresh.png" border="0" />
            </td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td align="left">
                <input id="search" name="_search" type="submit" class="button" value="Search" tabindex="7" />
                <input name="reset" type="reset"
                       onclick="javascript: window.location='p_virtualcards.html'"
                       class="button" value="Cancel" tabindex="8" />
            </td>
            <td colspan="2">&nbsp;</td>
        </tr>
<%--        <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">--%>
    </table>
</html:form>


    <ec:table items="virtualCardReportModelList" var="VirtualCardReportModel"
              action="${pageContext.request.contextPath}/p_virtualcards.html"
              title="" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" filterable="false" width="1100px">
        <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
            <ec:exportXls fileName="Walk-in-Customers.xls" tooltip="Export Excel" />
        </authz:authorize>
        <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
            <ec:exportXlsx fileName="Walk-in-Customers.xlsx" tooltip="Export Excel" />
        </authz:authorize>
        <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
            <ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
                          headerTitle="Walk-in Customers"
                          fileName="Walk-in-Customers.pdf" tooltip="Export PDF" />
        </authz:authorize>
        <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
            <ec:exportCsv fileName="Walk-in-Customers.csv" tooltip="Export CSV"></ec:exportCsv>
        </authz:authorize>
        <ec:row>
            <ec:column property="customerId" title="Customer ID" escapeAutoFormat="True" style="text-align: right"/>
            <ec:column property="cnicNo" title="CNIC" escapeAutoFormat="True" style="text-align: right"/>
            <ec:column property="mobileNo" title="Mobile" escapeAutoFormat="True" style="text-align: right"/>
            <ec:column property="name" title="Name" escapeAutoFormat="True" style="text-align: right"/>
            <ec:column property="cardNo" title="Card No" escapeAutoFormat="True" style="text-align: right"/>
            <ec:column property="cardExpiry" title="Card Expiry" escapeAutoFormat="True" style="text-align: right"/>
            <ec:column alias=""  title="Block Status" sortable="false" viewsAllowed="html" style="text-align: center">
                <div class="lbl${VirtualCardReportModel.virtualCardId}">
                    ${VirtualCardReportModel.isBlocked}
                </div>

                    </ec:column>
          <%--  <ec:column property="isBlocked" title="Status" viewsAllowed="html"   styleClass="lbl${VirtualCardReportModel.virtualCardId}" value="lbl${VirtualCardReportModel.virtualCardId}" escapeAutoFormat="True" style="text-align: right"/>--%>
            <ec:column alias="" title="Block" sortable="false" viewsAllowed="html" style="text-align: center">

                        <c:choose>
                            <c:when test='${VirtualCardReportModel.isBlocked=="YES"}'>
                                <input type="hidden" value="1" id="hid${VirtualCardReportModel.virtualCardId}" >
                                <input id="btn${VirtualCardReportModel.virtualCardId}" type="button" class="button" value="Un Block" name="Retag${VirtualCardReportModel.virtualCardId}"
                                       onclick="block(${VirtualCardReportModel.virtualCardId})"
                                        />

                            </c:when>
                            <c:otherwise>
                                <input type="hidden" value="0" id="hid${VirtualCardReportModel.virtualCardId}" >
                                <input id="btn${VirtualCardReportModel.virtualCardId}" type="button" class="button" value="Block" name="Retag${VirtualCardReportModel.virtualCardId}"
                                       onclick="block(${VirtualCardReportModel.virtualCardId})"
                                />
                            </c:otherwise>

                </c:choose>
            </ec:column>
<%--            <ec:column property="createdOn" cell="date" format="dd/MM/yyyy hh:mm a" title="Registered On" width="14%"/>--%>
            <%--<ec:column property="registeredBy" title="Registered By Agent ID"/>--%>


<%--            <ec:column property="mobileNo" title="Tagged MSISDN" escapeAutoFormat="True" style="text-align: right"/>--%>

<%--            <ec:column property="transactionsAsSender" title="Transactions As Sender" format="0" calc="total" calcTitle="Total:" style="text-align: right"/>
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
        <%--<ajax:htmlContent baseUrl="${contextPath}/p_blockunblockvirtualcard.html"
                          source="Retag${VirtualCardReportModel.virtualCardId}"
                          target="successMsg"
                          parameters="virtualCardId={${VirtualCardReportModel.virtualCardId}},isBlocked={${VirtualCardReportModel.isBlocked}}"


        />--%>
    </ec:table>



</html:form>



<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/searchFormValidator.js"></script>
<script language="javascript" type="text/javascript">
    document.forms[0].cnic.focus();

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

    function block(id){
        hid="#hid"+id;
        var status;
        if(jq(hid).val()=="0")
        status="NO";
        else
        status="YES";
    jq.ajax({
        method: "POST",
        url: "${contextPath}/p_blockunblockvirtualcard.html",

        data: { vrtualCardId: id, isBlocked: status },
        method:"GET",
        success:function(response){
            //alert(response);
            btn="#btn"+id;
            lbl=".lbl"+id;
            hid="#hid"+id;
            jq('.infomsg').html(response);
            jq(btn).prop('disabled', false);
            if(jq(hid).val()=="0"){
                jq(hid).val("1");
                jq(btn).val("Un Block");
                jq(lbl).html("YES");
            }
            else{
                jq(hid).val("0");
                jq(btn).val("Block");
                jq(lbl).html("NO");
            }


        },
        beforeSend:function(){
            btn="#btn"+id;
            lbl=".lbl"+id;
            //alert(btn);
            jq(btn).prop('disabled', true);

        },
        error:function(response){
            alert(jq('.infomsg').html(response));
            jq('.infomsg').html(response);

        }
    });
    }

</script>

<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>



</body>
</html>
