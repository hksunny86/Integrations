<%--
  Created by IntelliJ IDEA.
  User: Abubakar Farooque
  Date: 2/1/2022
  Time: 3:06 PM
  To change this template use File | Settings | File Templates.
--%>
<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
<%@ page import='com.inov8.microbank.common.util.PortalDateUtils'%>

<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>

<html>
<head>
    <meta name="decorator" content="decorator">
    <script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
    <script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>


    <link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
    <link type="text/css" rel="stylesheet" href="styles/ajaxtags.css" />
    <link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
    <%@include file="/common/ajax.jsp"%>
    <meta name="title" content="Debit Card Management" />
    <script language="javascript" type="text/javascript">
        var jq=$.noConflict();
        var serverDate ="<%=PortalDateUtils.getServerDate()%>";

        function openTransactionDetailWindow(transactionCode)
        {
            var action = 'p_showtransactiondetail.html?actionId='+${retriveAction}+'&transactionCodeId='+transactionCode+'&isMfs=true';
            newWindow = window.open( action , 'TransactionDetail', 'width=550,height=350,menubar=no,toolbar=no,left=150,top=150,directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
            if(window.focus) newWindow.focus();
            return false;
        }

        function error(request)
        {
            alert("An unknown error has occured. Please contact with the administrator for more details");
        }

        var childWindow;
        function closeChild()
        {
            try
            {
                if(childWindow != undefined)
                {
                    childWindow.close();
                    childWindow=undefined;
                }
            }catch(e){}
        }

        function ajaxPostFunction() {
            $('errorMsg').innerHTML = "";
            Element.hide('errorMsg');
            $('successMsg').innerHTML = $F('message');
            Element.show('successMsg');

            var msg = $F('message');
            document.getElementById(this.source).disabled=true;
            // response.setInHeader("Refresh", 4);
            try{
                var str = this.source;
                var substr = str.split("_");
                document.getElementById('status_'+substr[1]).innerHTML = "<%=PortalConstants.KEY_DEBIT_CARD_ISSUENCE_USECASE_ID%>";
            }catch(exception){
            }

            jq('html, body').animate({scrollTop : 0},1000);
            alert(msg);
            pageReload();
        };

        function pageReload()
        {
            if( window.localStorage )
            {
                window.location.reload();
            }
        };

        function ajaxErrorFunction(request, obj) {
            var msg = "Your request cannot be processed at the moment. Please try again later.";
            $('successMsg').innerHTML = "";
            Element.hide('successMsg');
            jq('html, body').animate({scrollTop : 0},1000);
        };


    </script>

</head>
<body bgcolor="#ffffff" onunload="javascript:closeChild();">

<spring:bind path="debitCardRequestsViewModel.*">

    <c:if test="${not empty status.errorMessages}">
        <div class="errorMsg">
            <c:forEach var="error" items="${status.errorMessages}">
                <c:out value="${error}" escapeXml="false" />
                <br />
            </c:forEach>
        </div>
    </c:if>

</spring:bind>

<div id="rsp" class="ajaxMsg"></div>

<c:if test="${not empty messages}">
    <div class="infoMsg" id="successMessages">
        <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false" /><br/>
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session" />
</c:if>
<div id="successMsg" class="infoMsg" style="display:none;"></div>
<div id="errorMsg" class="errorMsg" style="display:none;"></div>
<input id="message" value="777" type="hidden" />
<html:form name='debitCardRequestsForm' commandName="debitCardRequestsViewModel" method="post"
           action="debitcardmanagement.html" onsubmit="return validateFormData(this)" >
    <table width="100%" border="0">
        <tr>
            <td align="right" class="formText">Mobile #:</td>
            <td align="left"><html:input path="mobileNo" cssClass="textBox" maxlength="11" tabindex="3" /></td>
            <td align="right" class="formText">CNIC #:</td>
            <td align="left"><html:input path="cnic" cssClass="textBox" maxlength="13" tabindex="4" /></td>
        </tr>
        <tr>
            <td class="formText" align="right">Card State:</td>
            <td align="left">
                <html:select path="cardStateId" cssClass="textBox" tabindex="6" >
                    <html:option value="">---All---</html:option>
                    <c:if test="${cardStateList != null}">
                        <html:options items="${cardStateList}" itemValue="cardStateId" itemLabel="name"/>
                    </c:if>
                </html:select>
            </td>
            <td class="formText" align="right">
                Card Status:
            </td>
            <td align="left" >
                <html:select path="cardStatusId" cssClass="textBox" tabindex="6" >
                    <html:option value="">---All---</html:option>
                    <c:if test="${cardStatusList != null}">
                        <html:options items="${cardStatusList}" itemValue="cardStatusId" itemLabel="name"/>
                    </c:if>
                </html:select>
            </td>
        </tr>
        <tr>
            <td class="formText" align="right">Card Product Type:</td>
            <td align="left" >
                <html:select path="cardProductCodeId" cssClass="textBox" tabindex="6" >
                    <html:option value="">---All---</html:option>
                    <c:if test="${cardProductTypeList != null}">
                        <html:options items="${cardProductTypeList}" itemValue="cardProductCodeId" itemLabel="cardProductName"/>
                    </c:if>
                </html:select>
            </td>


            <td height="16" align="right"  class="formText">Debit Card Request:</td>
            <td width="58%" align="left">

                <html:select path="reissuance" cssClass="textBox"  tabindex="13" onkeypress="return maskNumber(this,event)"  >
                    <html:option value="">---All---</html:option>
                    <html:option value="0" label="Issuance"/>
                    <html:option value="1" label="Re-Issuance"/>
                </html:select>
            </td>
        </tr>
        <tr>
            <td align="right" class="formText">Embossing Name:</td>
            <td align="left"><html:input path="embossingName" cssClass="textBox" maxlength="11" tabindex="3" /></td>
            <td align="right" class="formText">Debit Card #:</td>
            <td align="left"><html:input path="cardNumber" cssClass="textBox" maxlength="13" tabindex="4" /></td>
        </tr>
        <tr>
            <td class="formText" align="right">
                Created On From Date:
            </td>
            <td align="left">
                <html:input path="createdOnStartDate" id="createdOnFromDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
                <img id="CreatedFromDate" tabindex="7" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                <img id="CreatedFromDate" tabindex="8" title="Clear Date" name="popcal" onclick="javascript:$('createdOnFromDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
            </td>
            <td class="formText" align="right">
                Created On To Date:
            </td>
            <td align="left">
                <html:input path="createdOnEndDate" id="createdOnToDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
                <img id="CreatedToDate" tabindex="9" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                <img id="CreatedToDate" tabindex="10" title="Clear Date" name="popcal" onclick="javascript:$('createdOnToDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
            </td>
        </tr>



        <tr>
            <td class="formText" align="right">
                Re-Issuance From Date:
            </td>
            <td align="left">
                <html:input path="reIssuancecreatedOnStartDate" id="reissuanceCreatedOnFromDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
                <img id="ReIssuanceCreatedFromDate" tabindex="11" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                <img id="ReIssuanceCreatedFromDate" tabindex="13" title="Clear Date" name="popcal" onclick="javascript:$('reissuanceCreatedOnFromDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
            </td>
            <td class="formText" align="right">
               Re-Issuance To Date:
            </td>
            <td align="left">
                <html:input path="reIssuancecreatedOnEndDate" id="reissuanceCreatedOnToDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
                <img id="ReIssuanceCreatedToDate" tabindex="13" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                <img id="ReIssuanceCreatedToDate" tabindex="14" title="Clear Date" name="popcal" onclick="javascript:$('reissuanceCreatedOnToDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
            </td>
        </tr>

            <%--<tr>--%>
            <%--<td class="formText" align="right">--%>
            <%--Updated On From Date:--%>
            <%--</td>--%>
            <%--<td align="left">--%>
            <%--<html:input path="updatedOn" id="updatedOnFromDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>--%>
            <%--<img id="updatedFromDate" tabindex="7" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />--%>
            <%--<img id="updatedFromDate" tabindex="8" title="Clear Date" name="popcal" onclick="javascript:$('updatedOnFromDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />--%>
            <%--</td>--%>
            <%--<td class="formText" align="right">--%>
            <%--Updated On To Date:--%>
            <%--</td>--%>
            <%--<td align="left">--%>
            <%--<html:input path="updatedOn" id="updatedOnToDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>--%>
            <%--<img id="updatedToDate" tabindex="9" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />--%>
            <%--<img id="updatedToDate" tabindex="10" title="Clear Date" name="popcal" onclick="javascript:$('updatedOnToDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />--%>
            <%--</td>--%>
            <%--</tr>--%>
        <tr>
            <td class="formText" align="right">Channel:</td>
            <td align="left" >
                <html:select path="channelId" cssClass="textBox" tabindex="6" >
                    <html:option value="">---All---</html:option>
                    <c:if test="${channelList != null}">
                        <html:options items="${channelList}" itemValue="appId" itemLabel="appName"/>
                    </c:if>
                </html:select>
            </td>
            <td class="formText" align="right">Segment:</td>
            <td align="left" >
                <html:select path="segmentId" cssClass="textBox" tabindex="6" >
                    <html:option value="">---All---</html:option>
                    <c:if test="${segmentList != null}">
                        <html:options items="${segmentList}" itemValue="segmentId" itemLabel="name"/>
                    </c:if>
                </html:select>
            </td>
        </tr>
        <tr>
            <td class="formText" align="right">

            </td>
            <td align="left">
                <input name="_search" type="submit" class="button" value="Search" tabindex="12"/>
                <input name="reset" type="reset"
                       onclick="javascript: window.location='debitcardmanagement.html?actionId=${retriveAction}'"
                       class="button" value="Cancel" tabindex="13" />
            </td>
            <td class="formText" align="right">

            </td>
            <td align="left">&nbsp;</td>
        </tr>
        <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
    </table></html:form>

<ec:table items="debitCardRequestsList" var="debitCardRequestsViewModel"
          action="${contextPath}/debitcardmanagement.html"
          title="" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" filterable="false">
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
        <ec:exportXls fileName="Debit Card Requests.xls" tooltip="Export Excel" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
        <ec:exportXlsx fileName="Debit Card Requests.xlsx" tooltip="Export Excel" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
        <ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da" headerTitle="Debit Card Requests"
                      fileName="Debit Card Requests.pdf" tooltip="Export PDF" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
        <ec:exportCsv fileName="Debit Card Requests.csv" tooltip="Export CSV"></ec:exportCsv>
    </authz:authorize>

    <ec:row>
        <ec:column property="embossingName" title="Embossing Name" escapeAutoFormat="true"/>
        <ec:column property="nadraName" title="Customer Name" escapeAutoFormat="true"/>
        <ec:column property="mailingAddress" title="Mailing Address" escapeAutoFormat="true"/>
        <ec:column property="mobileNo" title="Mobile #" escapeAutoFormat="True" style="text-align: center"/>
        <ec:column property="cnic" title="CNIC #" escapeAutoFormat="True" style="text-align: center"/>
        <ec:column property="segmentName" title="Segment" escapeAutoFormat="True" style="text-align: center"/>
        <ec:column property="debitCardFee" title="Fee" escapeAutoFormat="True" style="text-align: center"/>
        <ec:column property="cardNumber" title="Debit Card Number" escapeAutoFormat="True" style="text-align: center"/>
        <ec:column property="cardProductType" title="Card Product Type" escapeAutoFormat="True" style="text-align: center"/>
        <ec:column property="cardStatus" title="Card Status" escapeAutoFormat="True" style="text-align: center"/>
        <ec:column property="cardState" title="Card State" escapeAutoFormat="True" style="text-align: center"/>
        <ec:column property="channelName" title="Channel" escapeAutoFormat="true"/>
        <c:choose>
            <c:when test="${debitCardRequestsViewModel.reissuance ne 1 }">
                <ec:column property="reissuance" title="Re_Issuance" escapeAutoFormat="true" value="No"/>
            </c:when>
            <c:otherwise>
                <ec:column property="reissuance" title="Re_Issuance" escapeAutoFormat="true" value="Yes"/>
            </c:otherwise>
        </c:choose>

        <c:choose>
            <c:when test="${debitCardRequestsViewModel.isApprovedDenied ne 1 }">
                <ec:column property="isApprovedDenied" title="Approval Denied" escapeAutoFormat="true" value="No"/>
            </c:when>
            <c:otherwise>
                <ec:column property="isApprovedDenied" title="Approval Denied" escapeAutoFormat="true" value="Yes"/>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${debitCardRequestsViewModel.isReIssuanceApprovedDenied ne 1 }">
                <ec:column property="isReIssuanceApprovedDenied" title="Re-Issuance Approval Denied" escapeAutoFormat="true" value="No"/>
            </c:when>
            <c:otherwise>
                <ec:column property="isReIssuanceApprovedDenied" title="Re-Issuance Approval Denied" escapeAutoFormat="true" value="Yes"/>
            </c:otherwise>
        </c:choose>

        <c:choose>
            <c:when test="${debitCardRequestsViewModel.isApproved ne 1 }">
                <ec:column property="isApproved" title="Issuance Approved" escapeAutoFormat="true" value="No"/>
            </c:when>
            <c:otherwise>
                <ec:column property="isApproved" title="Issuance Approved" escapeAutoFormat="true" value="Yes"/>
            </c:otherwise>
        </c:choose>

        <c:choose>
            <c:when test="${debitCardRequestsViewModel.isReIssuanceApproved ne 1 }">
                <ec:column property="isReIssuanceApproved" title="Re-Issuance Approved" escapeAutoFormat="true" value="No"/>
            </c:when>
            <c:otherwise>
                <ec:column property="isReIssuanceApproved" title="Re-Issuance Approved" escapeAutoFormat="true" value="Yes"/>
            </c:otherwise>
        </c:choose>

        <ec:column property="reissuanceRequestDate" cell="date" format="dd/MM/yyyy hh:mm a" title="Re-Issuance Request Date"/>
        <ec:column property="createdByAppUser" title="Created By" escapeAutoFormat="true"/>
        <ec:column property="createdOn" cell="date" format="dd/MM/yyyy hh:mm a" title="Created On"/>
        <ec:column property="checkedByName" title="Checked By" escapeAutoFormat="true"/>
        <ec:column property="updatedOn" cell="date" format="dd/MM/yyyy hh:mm a" title="Updated On"/>


    <ec:column alias=" " viewsAllowed="html" filterable="false" sortable="false">
<%--        <authz:authorize ifAnyGranted="<%=updatePermission%>">--%>
        <c:choose>
        <c:when test="${debitCardRequestsViewModel.cardStatusId ne 2}">
        <c:choose>
        <c:when test="${debitCardRequestsViewModel.reissuance ne 1 and debitCardRequestsViewModel.isApproved ne 1 and
        debitCardRequestsViewModel.isApprovedDenied ne 1}">

        <input type="button" class="button" style="width='90px'" value="Change Info"
                   onclick="javascript:changeInfo('${contextPath}/debitcardchangeinfoform.html?debitCardId=${debitCardRequestsViewModel.debitCardId}');" />
        </c:when>

            <c:when test="${debitCardRequestsViewModel.reissuance eq 1 and debitCardRequestsViewModel.isReIssuanceApproved ne 1 and
        debitCardRequestsViewModel.isReIssuanceApprovedDenied ne 1}">

                <input type="button" class="button" style="width='90px'" value="Change Info"
                       onclick="javascript:changeInfo('${contextPath}/debitcardchangeinfoform.html?debitCardId=${debitCardRequestsViewModel.debitCardId}');" />
            </c:when>
        <c:otherwise>
            <input type="button" class="button" disabled="disabled" style="width='90px'" value="Change Info"
                   onclick="javascript:changeInfo('${contextPath}/debitcardchangeinfoform.html?debitCardId=${debitCardRequestsViewModel.debitCardId}');" />
        </c:otherwise>
        </c:choose>
        </c:when>
            <c:otherwise>
                <input type="button" class="button" disabled="disabled" style="width='90px'" value="Change Info"
                       onclick="javascript:changeInfo('${contextPath}/debitcardchangeinfoform.html?debitCardId=${debitCardRequestsViewModel.debitCardId}');" />
            </c:otherwise>
        </c:choose>
    </ec:column>

    <ec:column alias=" " viewsAllowed="html" filterable="false" sortable="false">
        <c:choose>
        <c:when test="${debitCardRequestsViewModel.cardStatusId ne 2}">
        <c:choose>
            <c:when test="${debitCardRequestsViewModel.reissuance ne 1 and debitCardRequestsViewModel.isApproved ne 1 and
        debitCardRequestsViewModel.isApprovedDenied ne 1}">
                <input type="button" class="button" value="Approve" id="btnretry_${debitCardRequestsViewModel.debitCardId}"/>
            </c:when>

            <c:when test="${debitCardRequestsViewModel.reissuance eq 1 and debitCardRequestsViewModel.isReIssuanceApproved ne 1 and
        debitCardRequestsViewModel.isReIssuanceApprovedDenied ne 1}">
                <input type="button" class="button" onclick="pageReload()" value="Approve" id="btnretry_${debitCardRequestsViewModel.debitCardId}"/>
            </c:when>

            <c:otherwise>
                <input type="button" class="button" disabled="disabled"  value="Approve" id="btnretry_${debitCardRequestsViewModel.debitCardId}"/>
            </c:otherwise>
        </c:choose>
        </c:when>
            <c:otherwise>
                <input type="button" class="button" disabled="disabled"  value="Approve" id="btnretry_${debitCardRequestsViewModel.debitCardId}"/>
            </c:otherwise>
        </c:choose>

<%--        <c:choose>--%>
<%--            <c:when test="${debitCardRequestsViewModel.cardStatusId ne 4 and debitCardRequestsViewModel.cardStatusId ne 1 and--%>
<%--            debitCardRequestsViewModel.cardStatusId ne 6 and debitCardRequestsViewModel.cardStatusId ne 3}">--%>
<%--                <input type="button" class="button" value="Approve" id="btnretry_${debitCardRequestsViewModel.appUserId}"/>--%>
<%--            </c:when>--%>
<%--            <c:otherwise>--%>
<%--                <input type="button" class="button" disabled="disabled"  value="Approve" id="btnretry_${debitCardRequestsViewModel.appUserId}"/>--%>

<%--            </c:otherwise>--%>
<%--        </c:choose>--%>
         <ajax:updateField baseUrl="${contextPath}/p_approvedDebitCardRequests.html"
                          source="btnretry_${debitCardRequestsViewModel.debitCardId}" eventType="click"
                          target="message" action="btnretry_${debitCardRequestsViewModel.debitCardId}"
                          parameters="debitCardId=${debitCardRequestsViewModel.debitCardId}"
                          parser="new ResponseXmlParser()"
                          errorFunction="ajaxErrorFunction"
                          postFunction="ajaxPostFunction" />
    </ec:column>

        <ec:column alias=" " viewsAllowed="html" filterable="false" sortable="false">
            <c:choose>
            <c:when test="${debitCardRequestsViewModel.cardStatusId ne 2}">
            <c:choose>
                <c:when test="${debitCardRequestsViewModel.reissuance ne 1 and debitCardRequestsViewModel.isApproved ne 1 and
        debitCardRequestsViewModel.isApprovedDenied ne 1}">
                    <input type="button" class="button" value="Approval Denied" id="btndeny_${debitCardRequestsViewModel.debitCardId}"/>
                </c:when>

                <c:when test="${debitCardRequestsViewModel.reissuance eq 1 and debitCardRequestsViewModel.isReIssuanceApproved ne 1 and
        debitCardRequestsViewModel.isReIssuanceApprovedDenied ne 1}">
                    <input type="button" class="button" value="Approval Denied" id="btndeny_${debitCardRequestsViewModel.debitCardId}"/>
                </c:when>

                <c:otherwise>
                    <input type="button" class="button" disabled="disabled"  value="Approval Denied" id="btndeny_${debitCardRequestsViewModel.debitCardId}"/>
                </c:otherwise>
            </c:choose>
            </c:when>
                <c:otherwise>
                    <input type="button" class="button" disabled="disabled"  value="Approval Denied" id="btndeny_${debitCardRequestsViewModel.debitCardId}"/>
                </c:otherwise>
            </c:choose>
<%--            <c:choose>--%>
<%--                <c:when test="${debitCardRequestsViewModel.cardStatusId ne 4 and debitCardRequestsViewModel.cardStatusId ne 1 and--%>
<%--                debitCardRequestsViewModel.cardStatusId ne 6 and debitCardRequestsViewModel.cardStatusId ne 3}">--%>
<%--                    <input type="button" class="button" value="Approval Denied" id="btndeny_${debitCardRequestsViewModel.appUserId}"/>--%>
<%--                </c:when>--%>
<%--                <c:otherwise>--%>
<%--                    <input type="button" class="button" disabled="disabled"  value="Approval Denied" id="btndeny_${debitCardRequestsViewModel.appUserId}"/>--%>

<%--                </c:otherwise>--%>
<%--            </c:choose>--%>
            <ajax:updateField baseUrl="${contextPath}/p_approveDeniedDebitCardRequests.html"
                              source="btndeny_${debitCardRequestsViewModel.debitCardId}" eventType="click"
                              target="message" action="btndeny_${debitCardRequestsViewModel.debitCardId}"
                              parameters="debitCardId=${debitCardRequestsViewModel.debitCardId}"
                              parser="new ResponseXmlParser()"
                              errorFunction="ajaxErrorFunction"
                              postFunction="ajaxPostFunction"/>
        </ec:column>
    </ec:row>

    <%--        </authz:authorize>--%>

</ec:table>


<script language="javascript" type="text/javascript">

    function changeInfo(link)
    {
        // if (confirm('If customer information is verified then press OK to continue.')==true)
        // {
            window.location.href=link;
        // }
    }

    Calendar.setup(
        {
            inputField  : "createdOnFromDate", // id of the input field
            button      : "CreatedFromDate",    // id of the button
        }
    );


    Calendar.setup(
        {
            inputField  : "reissuanceCreatedOnFromDate", // id of the input field
            button      : "ReIssuanceCreatedFromDate",    // id of the button
        }
    );
    Calendar.setup(
        {
            inputField  : "createdOnToDate", // id of the input field
            button      : "CreatedToDate",    // id of the button
            isEndDate: true
        }
    );


    Calendar.setup(
        {
            inputField  : "reissuanceCreatedOnToDate", // id of the input field
            button      : "ReIssuanceCreatedToDate",    // id of the button
            isEndDate: true
        }
    );


    function openActionDetailWindow(debitCardId)
    {
        var url = null;
        var popupWidth = 800;
        var popupHeight = 550;
        var popupLeft = (window.screen.width - popupWidth)/2;
        var popupTop = (window.screen.height - popupHeight)/2;

        url = 'updateDebitCardRequests.html?debitCardId='+debitCardId;
        childWindow=window.open(url,'Action Detail Window', 'width='+popupWidth+',height='+popupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
        if(window.focus) childWindow.focus();
        return false;
    }

    function validateFormData(form){
        var isValid=validateForm(form);
        if(isValid){
            var _fDate =undefined;
            var _tDate = undefined;

            if(form.createdOnFromDate!=undefined)
            {
                _fDate = form.createdOnFromDate.value;
            }
            if(form.createdOnToDate!=undefined)
            {
                _tDate = form.createdOnToDate.value;
            }

            var startlbl = "Created On From Date";
            var endlbl = "Created On To Date";

            isValid=	isValidDateRange(_fDate,_tDate,startlbl,endlbl,serverDate);
        }
        return isValid;
    }

</script>
<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>

</body>
</html>

