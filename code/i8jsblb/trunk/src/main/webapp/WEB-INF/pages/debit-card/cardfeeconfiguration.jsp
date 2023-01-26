<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/ajax.jsp"%>
<%@ page import='com.inov8.microbank.common.util.*'%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<c:set var="createAction"><%=PortalConstants.ACTION_CREATE %></c:set>
<c:set var="CUSTOMER"><%=UserTypeConstantsInterface.CUSTOMER.longValue()%></c:set>
<c:set var="RETAILER"><%=UserTypeConstantsInterface.RETAILER.longValue()%></c:set>
<html>
<head>
    <link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
    <style type="text/css">
        table .textBox
        {
            width: 70px;
            height: 16px;
            margin-bottom: 0px;
        }
    </style>
    <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
    <link rel="stylesheet" href="${contextPath}/styles/jquery-ui-custom.min.css" type="text/css">
    <script type="text/javascript" src="${contextPath}/scripts/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/jquery-ui-custom.min.js"></script>
    <script type="text/javascript">
        var CUSTOMER = <%=UserTypeConstantsInterface.CUSTOMER%>;
        <%--var RETAILER = <%=UserTypeConstantsInterface.RETAILER%>;--%>
    </script>
    <script type="text/javascript" src="${contextPath}/scripts/debitcard/p_cardfeeconfigform.js"></script>

    <script type="text/javascript" src="${contextPath}/scripts/prototype.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/scriptaculous.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/overlibmws.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/ajaxtags-1.2-beta2.js"></script>

    <meta name="decorator" content="decorator2">
    <meta name="title" content="Debit Card Fee Configuration"/>
    <%--<%

        String updatePermission = PortalConstants.MNG_CARD_FEE_CREATE;
        updatePermission +=	"," + PortalConstants.PG_GP_UPDATE;
        updatePermission +=	"," + PortalConstants.ADMIN_GP_UPDATE;
    %>--%>

</head>
<body bgcolor="#ffffff" id="body-content">
<div class="infoMsg" id="errorMessages" style="display:none;"></div>
<div id="successMsg" class ="infoMsg" style="display:none;"></div>
<c:if test="${not empty messages}">
    <div class="infoMsg" id="successMessages">
        <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false"/>
            <br/>
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session"/>
</c:if>
<html:form id="cardFeeRuleVOForm" name="cardFeeRuleVOForm" commandName="cardFeeRuleVO" action="cardfeeconfiguration.html">
    <table border="0" width="100%">
        <tr>
            <td colspan="2">
                <div class="eXtremeTable">
                    <table id="cardFeeConfigurationTable" width="100%" cellspacing="0" cellpadding="0" border="0">
                        <thead>
                        <tr>
                            <td class="tableHeader">
                                Card Type
                            </td>
                            <td class="tableHeader">
                                Card Product Type
                            </td>
                            <td class="tableHeader">
                                Applicant Type
                            </td>
                            <td class="tableHeader">
                                Segment
                            </td>
                            <td class="tableHeader">
                                Agent Network
                            </td>
                            <td class="tableHeader">
                                Account Type
                            </td>
                            <td class="tableHeader">
                                Fee Type
                            </td>
                            <td class="tableHeader">
                                Amount
                            </td>
                            <td class="tableHeader">
                                Action
                            </td>
                        </tr>
                        </thead>
                        <tbody class="tableBody">
                        <c:forEach items="${cardFeeRuleVO.cardFeeRuleModelList}" var="cardFeeRuleModel" varStatus="iterationStatus">
                            <tr>
                                <td align="center">
                                    <html:hidden path="cardFeeRuleModelList[${iterationStatus.index}].cardFeeRuleId"/>
                                    <html:hidden path="cardFeeRuleModelList[${iterationStatus.index}].isDeleted"/>
                                    <html:hidden path="cardFeeRuleModelList[${iterationStatus.index}].versionNo"/>
                                    <html:select path="cardFeeRuleModelList[${iterationStatus.index}].cardTypeId" cssClass="textBox">
                                        <html:option value="">--All--</html:option>
                                        <html:options items="${cardTypeModelList}" itemLabel="name" itemValue="cardTypeId"/>
                                    </html:select>
                                </td>
                                <td align="center">
                                    <html:select path="cardFeeRuleModelList[${iterationStatus.index}].cardProductCodeId" cssClass="textBox" onchange="">
                                        <html:option value="">--All--</html:option>
                                        <html:options items="${cardProdCodeModelList}" itemLabel="cardProductName" itemValue="cardProductCodeId"/>
                                    </html:select>
                                </td>
                                <td align="center">
                                    <html:select path="cardFeeRuleModelList[${iterationStatus.index}].appUserTypeId" cssClass="textBox" onchange="">
                                        <html:option value="">--All--</html:option>
                                        <html:options items="${appUserTypeModelList}" itemLabel="name" itemValue="appUserTypeId"/>
                                    </html:select>
                                </td>

                                <td align="center">
                                    <html:select path="cardFeeRuleModelList[${iterationStatus.index}].segmentId" cssClass="textBox">
                                        <html:option value="">--All--</html:option>
                                        <html:options items="${segmentModelList}" itemLabel="name" itemValue="segmentId"/>
                                    </html:select>
                                </td>

                                <td align="center">
                                    <html:select path="cardFeeRuleModelList[${iterationStatus.index}].distributorId" cssClass="textBox">
                                        <html:option value="">--All--</html:option>
                                        <html:options items="${distributorModelList}" itemLabel="name" itemValue="distributorId"/>
                                    </html:select>
                                </td>

                                <td align="center">
                                    <html:select path="cardFeeRuleModelList[${iterationStatus.index}].accountTypeId" cssClass="textBox">
                                        <html:option value="">--All--</html:option>
                                        <html:options items="${allAccountTypeModelList}" itemLabel="name" itemValue="customerAccountTypeId"/>
                                    </html:select>
                                </td>

                                <td align="center">
                                    <html:select path="cardFeeRuleModelList[${iterationStatus.index}].cardFeeTypeId" cssClass="textBox">
                                        <html:option value="">--All--</html:option>
                                        <html:options items="${cardFeeTypeModelList}" itemLabel="name" itemValue="cardFeeTypeId"/>
                                    </html:select>
                                </td>
                                <td align="center">
                                    <html:input path="cardFeeRuleModelList[${iterationStatus.index}].amount" cssClass="textBox" maxlength="10" onkeypress="return maskNumber(this,event)" cell="currency"  format="###,###,##0.00" style="text-align: right"/>
                                </td>
                                <td>
                                    <input type="button" value="Installments" class="button"
                                           onClick="return openDebitCardFeeAnnualWindow('${cardFeeRuleModel.cardFeeRuleId}')"/>
                                </td>

                                <authz:authorize ifAnyGranted="<%= PortalConstants.MNG_CARD_FEE_UPDATE %>">
                                    <td align="right">
                                        <input type="button" id="cardFeeRuleModelList${iterationStatus.index}.add" class="addRow" value="+" style="font-weight:bold;padding: 0px 12px;height: 29px;font-size:20px;" title="Add row" />
                                        <input type="button" id="cardFeeRuleModelList${iterationStatus.index}.remove" class="removeRow" value="-" title="Remove row" style="background-color:#E40606 !important;color:#FFFCFC !important;font-weight:bold;padding: 0px 12px;height: 29px;font-size:20px;" onClick="markRowDeleted(${iterationStatus.index});" />
                                    </td>
                                </authz:authorize>
                            </tr>

                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </td>
        </tr>
        <tr>
            <td align="center" colspan="2">

                <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_UPDATE%>">
                <input type="hidden" name="isUpdate" id="isUpdate" value="false"/>

                <authz:authorize ifAnyGranted="<%= PortalConstants.MNG_CARD_FEE_UPDATE %>">
                    <input type="button" value="Save" class="button" id="btnSave"/> &nbsp;
                </authz:authorize>
                <authz:authorize ifNotGranted="<%=PortalConstants.MNG_CARD_FEE_UPDATE%>">
                    <input type="button" value="Save" class="button" id="btnSave" disabled="disabled"/> &nbsp;
                </authz:authorize>
                <authz:authorize ifAnyGranted="<%= PortalConstants.MNG_CARD_FEE_DELETE %>">
                    <c:if test="${not empty  cardFeeRuleVO.cardFeeRuleModelList}">
                        <input type="button" value="Remove All" class="button" id="btnRemoveAll"/>
                    </c:if>
                </authz:authorize>
                <input name="reset" type="reset" onclick="javascript: window.location='cardfeeconfiguration.html'" class="button" value="Cancel" />
            </td>
        </tr>
    </table>
</html:form>

<script type="text/javascript" language="javascript">

    function openDebitCardFeeAnnualWindow(cardFeeRuleId) {

        newWindow=window.open('p_debitcardannualfeeinstallments.html?cardFeeRuleId='+cardFeeRuleId);
        if(window.focus) newWindow.focus();
        return false;
    }
    <%--<div style="visibility: hidden">
        <select id="customerAccountTypes">
            <option label="--All--"></option>
            <c:forEach var="customerAccountTypeModel" items="${customerAccountTypeModelList}">
                <option label="${customerAccountTypeModel.name}" value="${customerAccountTypeModel.customerAccountTypeId}"></option>
            </c:forEach>
        </select>
        <select id="agentAccountTypes">
            <option label="--All--"></option>
            <c:forEach var="agentAccountTypeModel" items="${agentAccountTypeModelList}">
                <option label="${agentAccountTypeModel.name}" value="${agentAccountTypeModel.customerAccountTypeId}"></option>
            </c:forEach>
        </select>
    </div>--%>
</script>

<div id="dialog-confirm" title="Confirmation" style="display: none;">
    <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>Are you sure you want to Remove all rules?</p>
</div>
</body>
</html>
