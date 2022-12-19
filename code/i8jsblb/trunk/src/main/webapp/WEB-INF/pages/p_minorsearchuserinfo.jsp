<%--
  Created by IntelliJ IDEA.
  User: abubakar.farooque
  Date: 8/18/2022
  Time: 11:19 AM
  To change this template use File | Settings | File Templates.
--%>
<%@page import="com.inov8.microbank.common.vo.account.BulkCustomerUpdateVo"%>
<%@page import="com.inov8.microbank.common.model.AppUserModel"%>
<%@page import="com.inov8.microbank.common.util.UserUtils"%>
<%@page import="com.inov8.microbank.common.util.UserTypeConstantsInterface"%>
<%@page import="com.inov8.microbank.common.util.PortalConstants"%>
<%@ page import='com.inov8.microbank.common.util.*'%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@include file="/common/taglibs.jsp"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
<html>
<head>
    <meta name="decorator" content="decorator">
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/styles/extremecomponents.css"
          type="text/css">
    <script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
    <link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>

    <link rel="stylesheet" href="${contextPath}/styles/jquery-ui-custom.min.css" type="text/css">
    <script type="text/javascript" src="${contextPath}/scripts/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/jquery-ui-custom.min.js"></script>
    <script type="text/javascript">
        var jq = $.noConflict();
        jq(document).ready(
            function($)
            {
                jq('#ec_table thead .tableHeader').first().html('<input type="checkbox" id="selectAll"/>');

                $('#selectAll').click(function(event) {  //on click
                    var isSelectAll = this.checked;
                    $('.checkbox').each(function() { //loop through each checkbox
                        this.checked = isSelectAll;  //select/de-select all checkboxes with class "checkbox"
                    });

                    toggleEdit(isSelectAll);
                });

                if(${fn:length(minorUserInfoListViewModelList)} < 1){
                $('#selectAll').hide();
            }else{
                $('#selectAll').show();
            }

                $('.checkbox').click(function(event) {
                    if( this.checked )
                    {
                        toggleEdit(true);
                    }
                    else
                    {
                        $('#selectAll').prop('checked', false);//de-select Select All Checkbox
                    }

                    var totalRows = $('.checkbox').length;
                    $('.checkbox').each(function(index) {
                        if( this.checked )
                        {
                            if(index == (totalRows -1))
                            {
                                $('#selectAll').prop('checked', true);//check Select All if all checkboxes are selected
                            }
                        }
                        else
                        {
                            return false;//break the each loop
                        }
                    });

                    $('.checkbox').each(function(index) {
                        if( this.checked )
                        {
                            return false;//break the each loop
                        }
                        else
                        {
                            if(index == (totalRows -1))
                            {
                                toggleEdit(false);//If all checkboxes are unchecked then hide Edit buttons
                            }
                        }
                    });
                });//end of .checkbox click event

                var gridStatusText = $('#ec_table thead tr .statusBar').html();
                var buttonHtml = '<!--<input type="button" id="btnUpdateAccountType" value="Update Account Type"/>-->&nbsp;'+
                    '&nbsp;<input type="button" id="btnUpdateSegment" value="Update Segment"/>';
                var mergedHtml = buttonHtml+ "&nbsp;" + gridStatusText;
                function toggleEdit(toggle)
                {
                    if(toggle)
                    {
                        $('#ec_table thead tr .statusBar').html(mergedHtml);

                        $('#btnUpdateAccountType').click(function() {
                            var appUserIds="";
                            var count = $('input:checkbox.checkbox').filter(':checked').size();
                            $('input:checkbox.checkbox').filter(':checked').next().each( function(index) {
                                appUserIds=appUserIds.concat($(this).val());
                                if(index < (count-1) )
                                {
                                    appUserIds=appUserIds.concat(",");
                                }
                            });
                            //alert(appUserIds);
                            document.getElementById("accTypeAppUserIds").value=appUserIds;
                            dialogAccountType.dialog( "open" );//Open the dialog
                        });
                        $('#btnUpdateSegment').click(function() {
                            var appUserIds="";
                            var count = $('input:checkbox.checkbox').filter(':checked').size();
                            $('input:checkbox.checkbox').filter(':checked').next().each( function(index) {
                                appUserIds=appUserIds.concat($(this).val());
                                if(index < (count-1) )
                                {
                                    appUserIds=appUserIds.concat(",");
                                }
                            });
                            //alert(appUserIds);
                            document.getElementById("segmentAppUserIds").value=appUserIds;
                            dialogSegment.dialog( "open" );//Open the dialog
                        });

                    }
                    else
                    {
                        $('#ec_table thead tr .statusBar').html(gridStatusText);
                    }
                }

                var dialogAccountType = $( "#dialog-form-update-accounttype" ).dialog({//create dialog but dont make it visible
                    autoOpen: false,
                    height: 250,
                    width: 500,
                    modal: true
                });

                var dialogSegment = $( "#dialog-form-update-segment" ).dialog({//create dialog but dont make it visible
                    autoOpen: false,
                    height: 250,
                    width: 500,
                    modal: true
                });

            });
    </script>
    <%
        String readPermission = PortalConstants.MNG_GEN_ACC_READ;
        readPermission +=	"," + PortalConstants.CSR_GP_READ;
        readPermission +=	"," + PortalConstants.PG_GP_READ;
        readPermission +=	"," + PortalConstants.MNG_BB_CUST_READ;
        readPermission +=	"," + PortalConstants.RET_GP_READ;
        readPermission +=	"," + PortalConstants.ADMIN_GP_READ;

        String updatePermission = PortalConstants.MNG_GEN_ACC_UPDATE;
        updatePermission +=	"," + PortalConstants.PG_GP_UPDATE;
        updatePermission +=	"," + PortalConstants.MNG_BB_CUST_UPDATE;

        String reprintPermission = PortalConstants.RET_GP_READ;
        reprintPermission += "," + PortalConstants.ADMIN_GP_READ;
        reprintPermission +=	"," + PortalConstants.PG_GP_READ;
        reprintPermission +=	"," + PortalConstants.BB_CUST_REPRINT_FORM_READ;

        String updateL2Permission = PortalConstants.ADMIN_GP_UPDATE;
        updateL2Permission +=	"," + PortalConstants.PG_GP_UPDATE;
        updateL2Permission +=	"," + PortalConstants.MNG_L2_KYC_U;

        String readL2Permission = PortalConstants.MNG_GEN_ACC_READ;
        readL2Permission +=	"," + PortalConstants.CSR_GP_READ;
        readL2Permission +=	"," + PortalConstants.PG_GP_READ;
        readL2Permission +=	"," + PortalConstants.RET_GP_READ;
        readL2Permission +=	"," + PortalConstants.ADMIN_GP_READ;
        readL2Permission +=	"," + PortalConstants.MNG_L2_KYC_R;
    %>
    <c:set var="isRetailer"><%=UserTypeConstantsInterface.RETAILER.longValue() == UserUtils.getCurrentUser().getAppUserTypeId().longValue()%></c:set>
    <c:set var="isNotRetailer" value="${not isRetailer}"></c:set>
    <%@include file="/WEB-INF/pages/export_zip.jsp"%>
    <%-- <script type="text/javascript" src="${contextPath}/scripts/exportzip.js"></script> --%>
    <meta name="title" content="Search Minor Customer"   id="<%=ReportIDTitleEnum.SearchCustomerReport.getId()%>" />
</head>

<body bgcolor="#ffffff">
<!--
<div align="right"><a href="productform.html" class="linktext">Add Product</a>&nbsp;&nbsp;</div>
-->
<spring:bind path="minorUserInfoListViewModel.*">

    <c:if test="${not empty status.errorMessages}">
        <div class="errorMsg">
            <c:forEach var="error" items="${status.errorMessages}">
                <c:out value="${error}" escapeXml="false" />
                <br />
            </c:forEach>
        </div>
    </c:if>

</spring:bind>

<c:if test="${not empty messages}">
    <div class="infoMsg" id="successMessages">
        <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false" />
            <br />
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session" />
</c:if>



<html:form name="userInfoListViewForm"
           commandName="minorUserInfoListViewModel" onsubmit="return validateForm(this)" >
    <table width="950px">
        <tr>
            <td align="right" class="formText">
                First Name:</td>
            <td  align="left"><html:input path="firstName" onkeypress="return maskCommon(this,event)" cssClass="textBox" tabindex="1" maxlength="50"/></td>
            <td  align="right" class="formText">Last Name:</td>
            <td  align="left"><html:input path="lastName" onkeypress="return maskCommon(this,event)" cssClass="textBox" tabindex="2" maxlength="50"/>

            </td>
        </tr>

        <tr>
            <td align="right" class="formText">
                Customer ID:</td>
            <td align="left"><html:input path="userId" onkeypress="return maskCommon(this,event)" cssClass="textBox" maxlength="11" tabindex="3" /></td>
            <td align="right" class="formText">Mobile #:</td>
            <td align="left">
                <html:input path="mobileNo" onkeypress="return maskCommon(this,event)" cssClass="textBox" maxlength="11" tabindex="4" />
            </td>
        </tr>

        <tr>
            <td align="right" class="formText">
                Father Cnic #: </td>
            <td align="left"><html:input path="fatherCnic" onkeypress="return maskCommon(this,event)" cssClass="textBox" maxlength="13" tabindex="3" /></td>
            <td align="right" class="formText">Mother Cnic #:</td>
            <td align="left">
                <html:input path="motherCnic" onkeypress="return maskCommon(this,event)" cssClass="textBox" maxlength="13" tabindex="4" />
            </td>
        </tr>

        <tr>
            <td align="right" class="formText">
                IBAN:</td>
            <td align="left"><html:input path="iban" onkeypress="return maskCommon(this,event)" cssClass="textBox" maxlength="24" tabindex="3" /></td>
            <td></td>
            <td align="left">
            </td>
        </tr>

        <tr>
            <td align="right" class="formText">
                CNIC #:</td>
            <td align="left"><html:input path="nic" onkeypress="return maskCommon(this,event)" cssClass="textBox" maxlength="13" tabindex="5" /></td>
            <c:choose>
                <c:when test="${isRetailer}">
                    <input type="hidden" name="accountOpenerId" id="accountOpenerId" value="<%=UserUtils.getCurrentUser().getAppUserId()%>"/>
                    <td colspan="2">&nbsp;</td>
                </c:when>
                <c:otherwise>
                    <td align="right" class="formText">Segment:</td>
                    <td align="left">
                        <html:select path="segment" cssClass="textBox"
                                     tabindex="1">
                            <html:option value="">[All]</html:option>
                            <c:if test="${segmentList != null}">
                                <html:options items="${segmentList}" itemLabel="name" itemValue="name" />
                            </c:if>
                        </html:select>
                    </td>
                </c:otherwise>
            </c:choose>
        </tr>
        <tr>
            <td  align="right" class="formText">
                Account Type:
            </td>
            <td align="left">
                <html:select path="customerAccountTypeId" cssClass="textBox">
                    <html:option value="">[All]</html:option>
                    <c:if test="${customerAccountTypeList != null}">
                        <html:options items="${customerAccountTypeList}" itemLabel="name" itemValue="customerAccountTypeId" />
                    </c:if>
                </html:select>
            </td>
            <td  align="right" class="formText">
                Registration Status:
            </td>
            <td align="left">
                <html:select path="registrationStateID" cssClass="textBox">
                    <html:option value="">[All]</html:option>
                    <c:if test="${registrationStateList != null}">
                        <html:options items="${registrationStateList}" itemLabel="name" itemValue="registrationStateId" />
                    </c:if>
                </html:select>
            </td>
        </tr>
        <tr>
            <td  align="right" class="formText">
                Account State:
            </td>
            <td align="left">
                <html:select path="accountStateId" cssClass="textBox">
                    <html:option value="">[All]</html:option>
                    <c:if test="${accountStateList != null}">
                        <html:options items="${accountStateList}" itemLabel="name" itemValue="accountStateId" />
                    </c:if>
                </html:select>
            </td>
            <td  align="right" class="formText">
                Intial Application Form No.:
            </td>
            <td align="left">
                <html:input path="initialAppFormNo" onkeypress="return maskCommon(this,event)" cssClass="textBox" maxlength="16" tabindex="10" />
            </td>
        </tr>
        <tr>
            <td align="right" class="formText">
                A/c Opening Method:
            </td>
            <td align="left">
                <html:select path="accountOpeningMethodId" cssClass="textBox">
                    <html:option value="">[All]</html:option>
                    <c:if test="${accountOpeningMethodList != null}">
                        <html:options items="${accountOpeningMethodList}" itemLabel="name" itemValue="accountOpeningMethodId" />
                    </c:if>
                </html:select>
            </td>
            <td align="right" class="formText">
                Mobile Network:
            </td>
            <td align="left">
                <html:select path="mobileNetworkId" cssClass="textBox">
                    <html:option value="">[All]</html:option>
                    <c:if test="${mobileNetworkList != null}">
                        <html:options items="${mobileNetworkList}" itemLabel="networkName" itemValue="mobileNetworkId" />
                    </c:if>
                </html:select>
            </td>
        </tr>
        <tr>
            <td  class="formText" align="right">
                Account Updated On-Start:
            </td>
            <td  align="left">
                <html:input path="startDate" id="startDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
                <img id="sDate" tabindex="11" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                <img id="sDate" tabindex="12" title="Clear Date" name="popcal"  onclick="javascript:$('startDate').value=''"   align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
            </td>
            <td  class="formText" align="right">
                Account Updated On-End:
            </td>
            <td  align="left">
                <html:input path="endDate" id="endDate"  readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
                <img id="eDate" tabindex="13" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                <img id="eDate" tabindex="14" title="Clear Date" name="popcal"  onclick="javascript:$('endDate').value=''"   align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
            </td>

        </tr>
        <tr>
            <td class="formText" align="right">
                Account Created On - Start:
            </td>
            <td align="left">
                <html:input path="createdStartDate" id="createdEndDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
                <img id="fromStartDate" tabindex="3" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                <img id="fromStartDate" tabindex="4" title="Clear Date" name="popcal" onclick="javascript:$('createdEndDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
            </td>
            <td class="formText" align="right">
                Account Created On - End:
            </td>
            <td align="left">
                <html:input path="createdEndDate" id="createdStartDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
                <img id="toStartDate" tabindex="5" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                <img id="toStartDate" tabindex="6" title="Clear Date" name="popcal" onclick="javascript:$('createdStartDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
            </td>
        </tr>

        <tr>
            <td align="right" class="formText">&nbsp;

            </td>

            <td align="left" colspan="2" class="formText">
                <authz:authorize ifAnyGranted="<%=readPermission%>">
                    <input type="submit" class="button" value="Search" name="_search" tabindex="6" />
                </authz:authorize>
                <authz:authorize ifNotGranted="<%=readPermission%>">
                    <input type="submit" class="button" value="Search" name="_search" tabindex="-1" disabled="disabled" />
                </authz:authorize>
                <input type="reset" class="button" value="Cancel" name="_reset" onclick="javascript: window.location='p_minorsearchuserinfo.html?actionId=${retriveAction}'" tabindex="7" />

            </td>
            <td align="left" class="formText">&nbsp;</td>
            </td>
        </tr>
    </table>
</html:form>

<ec:table filterable="false" items="minorUserInfoListViewModelList"
          var="minorUserInfoListViewModel" retrieveRowsCallback="limit"
          filterRowsCallback="limit" sortRowsCallback="limit"
          action="${pageContext.request.contextPath}/p_minorsearchuserinfo.html?actionId=${retriveAction}"
          title="">
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
        <ec:exportXls fileName="Minor User Info List.xls" tooltip="Export Excel" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
        <ec:exportXlsx fileName="Minor User Info List.xlsx" tooltip="Export Excel" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
        <ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
                      headerTitle="List Customer Accounts" fileName="User Info List.pdf" tooltip="Export PDF" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
        <ec:exportCsv fileName="Minor User Info List.csv" tooltip="Export CSV"></ec:exportCsv>
    </authz:authorize>
    <ec:row>
        <c:set var="minorUserInfoListViewAppUserId">
            <security:encrypt strToEncrypt="${minorUserInfoListViewModel.appUserId}"/>
        </c:set>
        <ec:column title=" " sortable="false" property="appUserId" viewsAllowed="html">
            <input type="checkbox" class="checkbox"/>
            <input type="hidden" value="${minorUserInfoListViewModel.appUserId}">
        </ec:column>
        <ec:column filterable="false" property="accountTitle" title="Name"  />
        <ec:column property="userId" title="Customer ID" filterable="false" />
        <ec:column filterable="false" property="mobileNo" title="Mobile #" escapeAutoFormat="true"/>
        <ec:column property="iban" title="IBAN" filterable="false" />
        <ec:column filterable="false" property="customerAccountType" title="Account Type" />
        <c:if test="${isNotRetailer}">
            <ec:column property="fullAddress"  filterable="false" title="Address" sortable="false"/>
            <ec:column property="fullAddressCity"  filterable="false" title="City" sortable="false"/>
        </c:if>
        <ec:column property="dob" filterable="false" cell="date"
                   format="dd/MM/yyyy" alias="DOB" />
        <ec:column property="nic" filterable="false" title="NIC #"  escapeAutoFormat="true">

            <c:choose>
                <c:when test="${minorUserInfoListViewModel.isAccountClosed =='CLOSED UNSETTLED' || minorUserInfoListViewModel.isAccountClosed =='CLOSED SETTLED'}">
                    ${minorUserInfoListViewModel.nic}
                </c:when>
                <c:otherwise>
                    <a href="p_verisysdata.html?actionId=${retriveAction}&id=${minorUserInfoListViewModel.nic}" onClick="return openVerisysWindow('${minorUserInfoListViewModel.nic}')">
                            ${minorUserInfoListViewModel.nic}
                    </a>
                </c:otherwise>
            </c:choose>

            <%--		<c:if test="${minorUserInfoListViewModel.isAccountClosed =='CLOSED UNSETTLED'}">
                        ${minorUserInfoListViewModel.nic}
                    </c:if>
                    <c:if test="${minorUserInfoListViewModel.isAccountClosed =='CLOSED SETTLED'}">
                        ${minorUserInfoListViewModel.nic}
                    </c:if>--%>



        </ec:column>
        <c:if test="${isNotRetailer}">
            <ec:column property="segment" filterable="false" title="Segment"  escapeAutoFormat="true"/>
        </c:if>

        <ec:column property="accountOpenedBy" filterable="false" title="Opened By"  escapeAutoFormat="true"/>
        <ec:column property="accountOpeningDate" filterable="false" cell="date"	format="dd/MM/yyyy" title="A/C Opening Date" />
        <ec:column property="accountOpeningMethodName" filterable="false" title="A/c Opening Method"  escapeAutoFormat="true"/>
        <ec:column property="registrationState" filterable="false" title="Registration Status"  escapeAutoFormat="true"/>
        <ec:column property="accountState" filterable="false" title="Account State"  escapeAutoFormat="true"/>
        <ec:column property="accountEnabled" filterable="false" title="Status (Active/Deactive)"  escapeAutoFormat="true"/>
        <ec:column property="isAccountLocked" filterable="false" title="Status (Locked/Unlocked)"  escapeAutoFormat="true"/>
        <ec:column property="isAccountClosed" filterable="false" title="Status (Open/Closed)" escapeAutoFormat="true"/>
        <ec:column property="isCredentialExpired" filterable="false" title="Credential Expired"  escapeAutoFormat="true"/>
        <ec:column property="initialAppFormNo" filterable="false" title="Application Form No"  escapeAutoFormat="true"/>
        <ec:column property="companyName" filterable="false" title="Company Name"  escapeAutoFormat="true"/>
        <ec:column  property="accountUpdatedOn" filterable="false" cell="date"	format="dd/MM/yyyy" title="A/C Updated On" />
        <ec:column  property="accountUpdatedBy" title="Updated By" />
        <ec:column  property="stockTrading" title="Stock Trading" />
        <ec:column  property="MutualFunds" title="Mutual Funds" />
        <ec:column  property="clsResponseCode" title="CLS Response Code" />



        <ec:column alias=" " viewsAllowed="html" filterable="false" sortable="false">

            <c:if test="${minorUserInfoListViewModel.isAccountLocked=='LOCKED'}">
                <input type="button" class="button" style="width='90px'"
                       value="Locked" disabled="disabled"		 />
            </c:if>

            <c:if test="${minorUserInfoListViewModel.isAccountLocked=='UNLOCKED'}">
                <c:if test="${minorUserInfoListViewModel.isAccountClosed=='OPEN'}">
                    <c:choose>
                        <c:when test="${minorUserInfoListViewModel.customerAccountTypeId==1 || minorUserInfoListViewModel.customerAccountTypeId==2 || minorUserInfoListViewModel.customerAccountTypeId==4 ||minorUserInfoListViewModel.customerAccountTypeId==53}">
                            <authz:authorize ifAnyGranted="<%=updatePermission%>">
                                <input type="button" class="button" style="width='90px'" value="Change Info"
                                       onclick="javascript:changeInfo('${contextPath}/p_mnonewmfsaccountform.html?appUserId=${minorUserInfoListViewModel.appUserId}');" />
                            </authz:authorize>
                        </c:when>
                        <c:otherwise>
                            <authz:authorize ifAnyGranted="<%=updateL2Permission%>">
                                <input type="button" class="button" style="width:90px" value="Change Info"
                                       onclick="javascript:changeInfo('${contextPath}/p_l2_kyc_form.html?appUserId=${minorUserInfoListViewModel.appUserId}');" />
                            </authz:authorize>
                            <authz:authorize ifNotGranted="<%=updateL2Permission%>">
                                <input type="button" class="button" style="width:90px" value="Change Info" disabled="disabled"
                                       onclick="javascript:changeInfo('${contextPath}/p_l2_kyc_form.html?appUserId=${minorUserInfoListViewModel.appUserId}');" />
                            </authz:authorize>
                        </c:otherwise>
                    </c:choose>
                </c:if>
                <c:if test="${minorUserInfoListViewModel.isAccountClosed =='CLOSED UNSETTLED'}">
                    <input type="button" class="button" style="width='90px'"
                           value="Change Info" disabled="disabled" />
                </c:if>
                <c:if test="${minorUserInfoListViewModel.isAccountClosed =='CLOSED SETTLED'}">
                    <input type="button" class="button" style="width='90px'"
                           value="Change Info" disabled="disabled" />
                </c:if>

            </c:if>
            <c:if test="${not minorUserInfoListViewModel.isAccountLocked }">


            </c:if>


        </ec:column>

        <authz:authorize ifNotGranted="<%=updatePermission%>">
            <ec:column alias=" " viewsAllowed="html" filterable="false" sortable="false">
                <c:if test="${not minorUserInfoListViewModel.isAccountLocked}">
                    <input type="button" class="button" style="width='90px'"
                           value="Change Info" disabled="disabled" />
                </c:if>
                <c:if test="${minorUserInfoListViewModel.isAccountLocked}">
                    <input type="button" class="button" style="width='90px'"
                           value="Locked" disabled="disabled" />
                </c:if>
            </ec:column>
        </authz:authorize>
        <c:if test="${isNotRetailer}">
            <ec:column alias=" " viewsAllowed="html" filterable="false" sortable="false">
                <c:choose>
                    <c:when test="${minorUserInfoListViewModel.customerAccountTypeId==1 || minorUserInfoListViewModel.customerAccountTypeId==2 || minorUserInfoListViewModel.customerAccountTypeId==4 || minorUserInfoListViewModel.customerAccountTypeId==53}">
                        <input type="button" class="button" style="width='90px'" value="View Details" onclick="javascript:window.location.href='${pageContext.request.contextPath}/p_mnomfsaccountdetails.html?appUserId=${minorUserInfoListViewModel.appUserId}&actionId=2';" />
                    </c:when>
                    <c:otherwise>
                        <input type="button" class="button" style="width='90px'" value="View Details." onclick="javascript:window.location.href='${pageContext.request.contextPath}/p_level2accountdetails.html?appUserId=${minorUserInfoListViewModel.appUserId}&actionId=2';" />
                    </c:otherwise>
                </c:choose>
            </ec:column>
        </c:if>


    </ec:row>
</ec:table>
<%request.setAttribute("bulkCustomerUpdateVo", new BulkCustomerUpdateVo());%>
<div id="dialog-form-update-segment" title="Bulk Update Customer for segment">
    <html:form commandName="bulkCustomerUpdateVo" action="p_bulkcustomerupdate.html?update=segment" onsubmit="return validateUpdateBulkSegmentForm(this);">
        <input type="hidden" name="segmentAppUserIds" id="segmentAppUserIds">
        <table>
            <tr>
                <td colspan="2">
                    <div class="infoMsg" id="successMessagesCust" style="background-image: none; background-color: rgb(217, 237, 247); display: none;">
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <div id="emptyRow1">&nbsp;</div>
                </td>
            </tr>
            <tr>
                <td align="right" bgcolor="F3F3F3" class="formText" width="50%">
                    <span style="color: #FF0000">*</span>Segment:
                </td>
                <td align="left" bgcolor="FBFBFB" class="text">
                    <html:select id="segmentId2" path="segmentId" cssClass="textBox" tabindex="1">
                        <html:option value="">[All]</html:option>
                        <c:if test="${segmentList != null}">
                            <html:options items="${segmentList}" itemLabel="name" itemValue="segmentId" />
                        </c:if>
                    </html:select>
                </td>
            </tr>
            <tr><td colspan="2">&nbsp;</td></tr>
            <tr><td colspan="2">&nbsp;</td></tr>
            <tr>
                <td align="center" colspan="2">
                    <input type="submit" value="Save" class="button" id="btnSave2"/>
                </td>
                <!--<td align="left">
                    <button title="close" value=" Cancel " role="button" class="button"> Cancel </button>
                    <input type="button" value="Cancel" />
                </td>-->
            </tr>
        </table>
    </html:form>
</div>

<div id="dialog-form-update-accounttype" title="Bulk Update Customer for account type">
    <html:form commandName="bulkCustomerUpdateVo" action="p_bulkcustomerupdate.html?update=account" onsubmit="return validateUpdateBulkAccountForm(this);">
        <input type="hidden" id="accTypeAppUserIds" name="accTypeAppUserIds" />
        <table>
            <tr>
                <td colspan="2">
                    <div class="infoMsg" id="successMessagesAcct" style="background-image: none; background-color: rgb(217, 237, 247); display: none;">
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <div id="emptyRow2">&nbsp;</div>
                </td>
            </tr>
            <tr>
                <td align="right" bgcolor="F3F3F3" class="formText" width="50%">
                    <span style="color: #FF0000">*</span>Customer Account Type:
                </td>
                <td align="left" bgcolor="FBFBFB" class="text">
                    <html:select id="customerAccountType2" path="customerAccountTypeId" cssClass="textBox">
                        <html:option value="">[All]</html:option>
                        <c:if test="${customerAccountTypeList != null}">
                            <html:options items="${customerAccountTypeList}" itemLabel="name" itemValue="customerAccountTypeId" />
                        </c:if>
                    </html:select>
                </td>
            </tr>
            <tr><td colspan="2">&nbsp;</td></tr>
            <tr><td colspan="2">&nbsp;</td></tr>
            <tr>
                <td align="center" colspan="2">
                    <input type="submit" value="Save" class="button" id="btnSave1"/>
                </td>
                <!--<td align="left">
                    <button title="close" value=" Cancel " role="button" class="button"> Cancel </button>
                    <input type="button" value="Cancel" />
                </td>-->
            </tr>
        </table>
    </html:form>
</div>

<script language="javascript" type="text/javascript">
    highlightFormElements();
    document.forms[0].firstName.focus();
    function changeInfo(link)
    {
        if (confirm('If customer information is verified then press OK to continue.')==true)
        {
            window.location.href=link+'&searchFirstName='+$F('firstName')+'&searchLastName='+$F('lastName')+'&searchMfsId='+$F('userId')+'&searchNic='+$F('nic');
        }
    }

    function validateForm(){
        return validateFormChar(document.forms[0]);
    }

    function openPrinterWindow(appUserId,actionId){
        var location ='${pageContext.request.contextPath}/p_mnonewmfsaccountform_printer.html?actionId='+actionId+'&appUserId='+ appUserId;
        window.open(location,'printWindow');
    }

    function closeAccountTypeDialog(){
        alert("closing account type dialog");
        document.getElementById("dialog-form-update-accounttype").style.display="none";
        dialogAccountType.dialog( "close" );
    }

    function validateUpdateBulkAccountForm(){
        isValid=true;
        if(document.getElementById("customerAccountType2").value==""){
            //alert("Please select an account type which needs to be up*dated on selected records");
            document.getElementById("successMessagesAcct").innerHTML="Please select an account type which needs to be updated on selected records";
            document.getElementById("successMessagesAcct").style.display="block";
            isValid=false;
        }

        return isValid;
    }
    function openVerisysWindow(cnic)
    {
        var popupWidth = 550;
        var popupHeight = 350;
        var popupLeft = (window.screen.width - popupWidth)/2;
        var popupTop = (window.screen.height - popupHeight)/2;
        newWindow=window.open('p_verisysdata.html?actionId=${retriveAction}&id='+cnic,'Nadra Verisys Data','width='+popupWidth+',height='+popupHeight+',menubar=no,toolbar=no,left='+popupLeft+',top='+popupTop+',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
        if(window.focus) newWindow.focus();
        return false;
    }
    function validateUpdateBulkSegmentForm(){
        isValid=true;
        if(document.getElementById("segmentId2").value==""){
            //alert("Please select a segment which needs to be updated on selected records");
            document.getElementById("successMessagesCust").innerHTML="Please select a segment which needs to be updated on selected records";
            document.getElementById("successMessagesCust").style.display="block";
            isValid=false;
        }

        return isValid;
    }

    Calendar.setup({
        inputField : "startDate", // id of the input field
        button : "sDate", // id of the button
    });

    Calendar.setup({
        inputField : "endDate", // id of the input field
        button : "eDate", // id of the button
        isEndDate : true
    });
    Calendar.setup( {inputField  : "createdEndDate",button : "fromStartDate",} );
    Calendar.setup( {inputField  : "createdStartDate",button : "toStartDate", isEndDate: true } );


</script>
<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>

</body>
</html>

