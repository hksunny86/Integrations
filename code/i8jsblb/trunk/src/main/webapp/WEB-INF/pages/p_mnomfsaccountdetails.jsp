<!--Author: omar-->
<!--Modified by: Turab-->

<%@include file="/common/taglibs.jsp" %>
<%@page import="com.inov8.microbank.common.util.PortalConstants" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,IE=11,IE=10"/>
    <meta name="decorator" content="decorator">

    <link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/scripts/colorbox.css"/>
    <script src="${pageContext.request.contextPath}/scripts/jquery-1.7.2.min.js"></script>
    <script src="${pageContext.request.contextPath}/scripts/jquery.colorbox.js"></script>
    <script>
        var jq = $.noConflict();
        jq(document).ready(function ($) {
            //Examples of how to assign the Colorbox event to elements
            jq(".group1").colorbox({rel: 'group1'});

            jq(".group1").colorbox({
                inline: true,
                innerWidth: 600,
                height: 300,
                onLoad: function () {
                    // Remove print if it is re-opened after ajax
                    $("#cboxPrint").remove();
                },
            });

            jq(".group1").colorbox({
                iframe: true,
                innerWidth: 800,
                height: "95%",
                onComplete: function () {
                    jq("#cboxContent iframe").ready(function () {
                        // Add print box once content loaded
                        jq("#cboxCurrent").append($('<img style="cursor: pointer;" title="Print" id="cboxPrint" src="${contextPath}/images/ah_images/print.png" />'));
                        jq("#cboxPrint").click(function () {
                            printprepare();
                        });
                    });
                },
            });

            $('#ec_table tbody tr').filter(':odd').addClass('odd');
            $('#ec_table tbody tr').filter(':even').addClass('even');
        });

        var doAjaxBeforeUnloadEnabled = true; // We hook into window.onbeforeunload and bind some jQuery events to confirmBeforeUnload.  This variable is used to prevent us from showing both messages during a single event.


        var doAjaxBeforeUnload = function (evt) {

            if (!doAjaxBeforeUnloadEnabled) {

                return;
            }

            doAjaxBeforeUnloadEnabled = false;
            jq.ajax({
                url: "${contextPath}/p_mnomfsaccountdetailsajx.html?appUserId=${mfsAccountModel.appUserId}",
                success: function (a) {
                    console.debug("Ajax call finished");
                },
                async: false /* Not recommended.  This is the dangerous part.  Your mileage may vary. */
            });
        }

        window.onbeforeunload = doAjaxBeforeUnload;
        jq(window).unload(doAjaxBeforeUnload);

        function printprepare() {
            try {
                var print_frame = document.createElement('iframe');
                print_frame.src = '#';
                print_frame.style = "width:0pt; height:0pt; border: none;";
                document.body.appendChild(print_frame);
                var print_head = jq("#cboxContent iframe").contents().find("head").html();
                var print_body = jq("#cboxContent iframe").contents().find("body").html()
                var print_doc = (print_frame.contentWindow || print_frame.contentDocument);
                if (print_doc.document) print_doc = print_doc.document;
                // Write printable content
                print_doc.write("<html><head>");
                print_doc.write(print_head);
                print_doc.write('</head><body onload="this.focus(); this.print();">');
                print_doc.write(print_body);
                print_doc.write("</body></html>");
                print_doc.close();
            } catch (e) {
                alert("printing in error state");
                self.print();
            }
        }

        function img_find() {
            var imgs = document.getElementsByTagName("img");
            var imgSrcs = [];

            for (var i = 0; i < imgs.length; i++) {
                imgSrcs.push(imgs[i].src);
            }

            return imgSrcs;
        }

        function print_imgs() {
            var imgSrcs = img_find();
            try {
                var print_frame = document.createElement('iframe');
                print_frame.src = '#';
                print_frame.style = "width:0pt; height:0pt; border: none;";
                document.body.appendChild(print_frame);

                var print_doc = (print_frame.contentWindow || print_frame.contentDocument);
                if (print_doc.document) print_doc = print_doc.document;
                // Write printable content
                print_doc.write("<html><head>");
                print_doc.write("<title>printing all images</title>");
                print_doc.write('</head><body onload="this.focus(); this.print();">');
                for (var i = 0; i < imgSrcs.length; i++) {
                    print_doc.write("<img src='" + imgSrcs[i] + "' /><br>");
                }
                print_doc.write("</body></html>");
                print_doc.close();
            } catch (e) {
                alert("printing in error state");
                self.print();
            }
        }
    </script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/prototype.js"></script>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
    <meta name="title" content="Customer Details"/>
    <%@include file="/common/ajax.jsp" %>
    <%
        String backButtonPermission = PortalConstants.CSR_GP_READ + "," + PortalConstants.HOME_PAGE_QUICK_SEARCH_CUST_READ;

        String editButtonsPermission = PortalConstants.MNG_GEN_ACC_UPDATE;
        editButtonsPermission += "," + PortalConstants.PG_GP_UPDATE;
        editButtonsPermission += "," + PortalConstants.DEACTIVATE_BB_CUST_UPDATE;
        editButtonsPermission += "," + PortalConstants.REACTIVATE_BB_CUST_UPDATE;
        editButtonsPermission += "," + PortalConstants.BLOCK_BB_CUST_UPDATE;
        editButtonsPermission += "," + PortalConstants.UNBLOCK_BB_CUST_UPDATE;
        editButtonsPermission += "," + PortalConstants.REGENERATE_BB_CUST_PIN_UPDATE;
        editButtonsPermission += "," + PortalConstants.CSR_GP_UPDATE;

        String deactivatePermission = PortalConstants.MNG_GEN_ACC_UPDATE;
        deactivatePermission += "," + PortalConstants.CSR_GP_UPDATE;
        deactivatePermission += "," + PortalConstants.PG_GP_UPDATE;
        deactivatePermission += "," + PortalConstants.DEACTIVATE_BB_CUST_UPDATE;

        String activatePermission = PortalConstants.MNG_GEN_ACC_UPDATE;
        activatePermission += "," + PortalConstants.PG_GP_UPDATE;
        activatePermission += "," + PortalConstants.REACTIVATE_BB_CUST_UPDATE;

        String unlockPermission = PortalConstants.MNG_GEN_ACC_UPDATE;
        unlockPermission += "," + PortalConstants.CSR_GP_UPDATE;
        unlockPermission += "," + PortalConstants.PG_GP_UPDATE;
        unlockPermission += "," + PortalConstants.UNBLOCK_BB_CUST_UPDATE;

        String lockPermission = PortalConstants.MNG_GEN_ACC_UPDATE;
        lockPermission += "," + PortalConstants.PG_GP_UPDATE;
        lockPermission += "," + PortalConstants.BLOCK_BB_CUST_UPDATE;

        String updatePINPermission = PortalConstants.FRGT_BNK_PIN_UPDATE;
        updatePINPermission += "," + PortalConstants.CHNG_BNK_PIN_U;
        updatePINPermission += "," + PortalConstants.CSR_GP_UPDATE;
        updatePINPermission += "," + PortalConstants.PG_GP_UPDATE;

        String regeneratePINPermission = PortalConstants.REGENERATE_BB_CUST_PIN_UPDATE;

        String bbStmtPermission = PortalConstants.ADMIN_GP_READ;
        bbStmtPermission += "," + PortalConstants.CSR_GP_READ;
        bbStmtPermission += "," + PortalConstants.PG_GP_READ;
        bbStmtPermission += "," + PortalConstants.CUST_BB_STMT_READ;

        String txHistPermission = PortalConstants.ADMIN_GP_READ;
        txHistPermission += "," + PortalConstants.CSR_GP_READ;
        txHistPermission += "," + PortalConstants.PG_GP_READ;
        txHistPermission += "," + PortalConstants.CUST_TX_HIST_READ;

        String cashPaymentPermission = PortalConstants.CSR_GP_READ;
        cashPaymentPermission += "," + PortalConstants.PG_GP_READ;
        cashPaymentPermission += "," + PortalConstants.CUST_CP_REQ_READ;

        String complaintPermission = PortalConstants.CSR_GP_CREATE + "," + PortalConstants.ADD_COMP_CREATE;
        String accountClosingPermission = PortalConstants.MNG_CUST_ACC_CLOSURE_UPDATE;

        String closeAccountPermission = PortalConstants.MNG_CUST_ACC_CLOSURE_UPDATE;
        closeAccountPermission += "," + PortalConstants.PG_GP_UPDATE;
        closeAccountPermission += "," + PortalConstants.ADMIN_GP_READ;


        String blacklistMarkingPermission = PortalConstants.MNG_BLKLST_MRKNG_UPDATE;

        String accountAsSavingMarkingPermission = PortalConstants.MNG_ACC_SAVING_MRKNG_UPDATE;
        accountAsSavingMarkingPermission += "," + PortalConstants.PG_GP_UPDATE;
        accountAsSavingMarkingPermission += "," + PortalConstants.ADMIN_GP_READ;

        String debitBlockPermission = PortalConstants.MNG_DEBIT_BLK_CUST_U;


        String deactivateWebServicePermission = PortalConstants.MNG_GEN_ACC_UPDATE;
        deactivateWebServicePermission += "," + PortalConstants.CSR_GP_UPDATE;
        deactivateWebServicePermission += "," + PortalConstants.PG_GP_UPDATE;
        deactivateWebServicePermission += "," + PortalConstants.DEACTIVATE_WEB_SERVICE_BB_CUST_UPDATE;

        String activateWebServicePermission = PortalConstants.MNG_GEN_ACC_UPDATE;
        activateWebServicePermission += "," + PortalConstants.CSR_GP_UPDATE;
        activateWebServicePermission += "," + PortalConstants.PG_GP_UPDATE;
        activateWebServicePermission += "," + PortalConstants.ACTIVATE_WEB_SERVICE_BB_CUST_UPDATE;

        String updateCustomerLoginPINPermission = PortalConstants.REGENERATE_CUST_LOGIN_PIN;
        updateCustomerLoginPINPermission += "," + PortalConstants.CSR_GP_UPDATE;
        updateCustomerLoginPINPermission += "," + PortalConstants.PG_GP_UPDATE;

        String updateDormantPermission = PortalConstants.MNG_CUST_DORMANT_UPDATE;
        updateDormantPermission += "," + PortalConstants.PG_GP_UPDATE;
        updateDormantPermission += "," + PortalConstants.ADMIN_GP_UPDATE;

        String referrer = request.getParameter("referrer");
        if (null == referrer)
            referrer = request.getHeader("Referer");
    %>
    <script type="text/javascript">
        function maskCNIC(obj, e) {
            var code = e.charCode ? e.charCode : e.keyCode;
            if (!isNumericWithHyphin(code) || code == 38 || code == 35 || code == 36 || code == 37 || code == 39 || code == 40) {
                return false;
            }
            return true;
        }

        function initProgress() {
            if (document.getElementById('ajaxMsgDiv')) {
                document.getElementById('ajaxMsgDiv').style.display = "none";
            }
            if (confirm('If customer information is verified then press OK to continue') == true) {
                return true;
            } else {
                $('errorMsg').innerHTML = "";
                $('successMsg').innerHTML = "";
                Element.hide('successMsg');
                Element.hide('errorMsg');
                return false;
            }
        }

        var isErrorOccured = false;

        function resetProgress() {
            if (!isErrorOccured) {
                // clear error box
                $('errorMsg').innerHTML = "";
                Element.hide('errorMsg');
                // display success message

                Element.show('successMsg');
            }
            isErrorOccured = false;
        }

        jq = $.noConflict();

        function resetProgressWebService() {
            if (!isErrorOccured) {
                // clear error box
                $('errorMsg').innerHTML = "";
                Element.hide('errorMsg');
                // display success message
                Element.show('successMsg');

                var text = jq('#successMsg').text();
                var err = "An error has occurered. Please try again later";
                if (text.indexOf("approval against reference Action ID") == -1 && text.indexOf("authorization request already exist with  Action ID ") == -1 && text.indexOf(err) == -1) {
                    window.location.reload();
                } else {

                }

            }
            isErrorOccured = false;
        }

        function resetProgressDeactivate() {
            window.location.reload();
            /*		    if(!isErrorOccured){
             // clear error box
             $('errorMsg').innerHTML = "";
             Element.hide('errorMsg');
             $('successMsg').innerHTML = $F('message');
             // display success message
             Element.show('successMsg');

             var isDisable = $('btn_actDeact').getAttribute('disableAfterClick');
             if(isDisable=='true'){
             document.getElementById('btn_actDeact').disabled = true;
             }
             //Element.hide('btn_actDeact');
             }
             isErrorOccured = false;
             */
        }

        function resetProgressLock() {
            window.location.reload();
            /*		    if(!isErrorOccured){
             // clear error box
             $('errorMsg').innerHTML = "";
             Element.hide('errorMsg');
             $('successMsg').innerHTML = $F('lock_message');
             // display success message
             Element.show('successMsg');

             var isDisable = $('btn_lockUnlock').getAttribute('disableAfterClick');
             if(isDisable=='true'){
             document.getElementById('btn_lockUnlock').disabled = true;
             }
             //Element.hide('btn_actDeact');
             }
             isErrorOccured = false;
             window.location.reload();
             */
        }

        var childWindow;

        function openAccountClosingWindow(appUserId, customerId) {
            var popupWidth = 550;
            var popupHeight = 350;
            var popupLeft = (window.screen.width - popupWidth) / 2;
            var popupTop = (window.screen.height - popupHeight) / 2;
            childWindow = window.open('p-accountclosingupdateform.html?appUserId=' + appUserId + '&customerId=' + customerId + '&appUserTypeID=2', '_blank', 'width=' + popupWidth + ',height=' + popupHeight + ',menubar=no,toolbar=no,left=' + popupLeft + ',top=' + popupTop + ',directories=no,scrollbars=no,resizable=no,status=no');

        }

        function markAccountAsSavingWindow(appUserId, customerId) {
            var popupWidth = 550;
            var popupHeight = 350;
            var popupLeft = (window.screen.width - popupWidth) / 2;
            var popupTop = (window.screen.height - popupHeight) / 2;
            childWindow = window.open('p-markaccountassaving.html?appUserId=' + appUserId + '&customerId=' + customerId + '&appUserTypeID=2', '_blank', 'width=' + popupWidth + ',height=' + popupHeight + ',menubar=no,toolbar=no,left=' + popupLeft + ',top=' + popupTop + ',directories=no,scrollbars=no,resizable=no,status=no');

        }

        function openRemoveDormancyWindow(appUserId, customerId) {
            var popupWidth = 550;
            var popupHeight = 350;
            var popupLeft = (window.screen.width - popupWidth) / 2;
            var popupTop = (window.screen.height - popupHeight) / 2;
            childWindow = window.open('p-removedormancy.html?appUserId=' + appUserId + '&customerId=' + customerId + '&appUserTypeID=2', '_blank', 'width=' + popupWidth + ',height=' + popupHeight + ',menubar=no,toolbar=no,left=' + popupLeft + ',top=' + popupTop + ',directories=no,scrollbars=no,resizable=no,status=no');

        }
        function openlockunlockWindow(encryptAppUserId, usecaseId, actionId, mfsId, isLockUnlock) {
            var popupWidth = 550;
            var popupHeight = 350;
            var popupLeft = (window.screen.width - popupWidth) / 2;
            var popupTop = (window.screen.height - popupHeight) / 2;

            var btnLabel;
            if (usecaseId ==<%=PortalConstants.REACTIVATE_CUSTOMER_USECASE_ID%>) {
                btnLabel = document.getElementById('btn_actDeact').value.trim();

                if (btnLabel == "Deactivate/Reactivate")
                    usecaseId =<%=PortalConstants.DEACTIVATE_CUSTOMER_USECASE_ID%>;
            } else if (usecaseId ==<%=PortalConstants.UNBLOCK_CUSTOMER_USECASE_ID%>) {
                btnLabel = document.getElementById('btn_lockUnlock').value.trim();
                if (btnLabel == "Block/UnBlock")
                    usecaseId =<%=PortalConstants.BLOCK_CUSTOMER_USECASE_ID%>;
            }


            var url = 'p-lockunlockaccountform.html?appUserId=' + encryptAppUserId + '&usecaseId=' + usecaseId + '&mfsId=' + mfsId + '&actionId=' + actionId + '&isLockUnlock=' + isLockUnlock + '&isAgent=false';
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


        //Mark blacklisted script
        function openmarkunmarkBlacklistWindow(encryptAppUserId, usecaseId, actionId, mfsId, blacklisted) {
            var popupWidth = 550;
            var popupHeight = 350;
            var popupLeft = (window.screen.width - popupWidth) / 2;
            var popupTop = (window.screen.height - popupHeight) / 2;

            var btnLabel = document.getElementById('btn_blacklisted').value.trim();
            if (blacklisted == "true") {
                usecaseId =<%=PortalConstants.UNMARK_BLACKLISTED_USECASE_ID%>;
                blacklisted = false;
            } else {
                usecaseId =<%=PortalConstants.MARK_BLACKLISTED_USECASE_ID%>;
                blacklisted = true;
            }
            ;

            var url = 'p-markunmarkblacklisted.html?appUserId=' + encryptAppUserId + '&mfsId=' + mfsId + '&actionId=' + actionId + '&blacklisted=' + blacklisted + '&isAgent=false' + '&usecaseId=' + usecaseId;
            newWindow = window.open(url, 'Action Detail Window', 'width=' + popupWidth + ',height=' + popupHeight + ',menubar=no,toolbar=no,left=' + popupLeft + ',top=' + popupTop + ',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
            if (window.focus) newWindow.focus();
            return false;
        }

        function openactivatedeactivatewebserviceWindow(encryptAppUserId, usecaseId, actionId, mfsId, webServiceEnabled) {
            var popupWidth = 550;
            var popupHeight = 350;
            var popupLeft = (window.screen.width - popupWidth) / 2;
            var popupTop = (window.screen.height - popupHeight) / 2;

            var btnLabel = document.getElementById('btn_webserDeact').value.trim();
            if (webServiceEnabled == "true") {
                usecaseId =<%=PortalConstants.DEACTIVATE_WEB_SERVICE_USECASE_ID%>;
                webServiceEnabled = false;
            } else {
                usecaseId =<%=PortalConstants.ACTIVATE_WEB_SERVICE_USECASE_ID%>;
                webServiceEnabled = true;
            }
            ;

            var url = 'p_activatedeactivatewebserviceform.html?appUserId=' + encryptAppUserId + '&mfsId=' + mfsId + '&actionId=' + actionId + '&webServiceEnabled=' + webServiceEnabled + '&isAgent=false' + '&usecaseId=' + usecaseId;
            newWindow = window.open(url, 'Action Detail Window', 'width=' + popupWidth + ',height=' + popupHeight + ',menubar=no,toolbar=no,left=' + popupLeft + ',top=' + popupTop + ',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
            if (window.focus) newWindow.focus();
            return false;
        }

        function openDebitBlockWindow(encryptAppUserId, usecaseId, actionId, mfsId, mobileNo, debitBlockAmount, isDebitBlocked) {
            var popupWidth = 550;
            var popupHeight = 180;
            var popupLeft = (window.screen.width - popupWidth) / 2;
            var popupTop = (window.screen.height - popupHeight) / 2;

            var url = 'p-debitblock.html?appUserId=' + encryptAppUserId + '&mfsId=' + mfsId + '&actionId=' + actionId + '&isAgent=false' + '&usecaseId=' + usecaseId + '&mobileNo=' + mobileNo + '&debitBlockAmount=' + debitBlockAmount + '&isDebitBlocked=' + isDebitBlocked;
            //alert(url);
            newWindow = window.open(url, 'Action Detail Window', 'width=' + popupWidth + ',height=' + popupHeight + ',menubar=no,toolbar=no,left=' + popupLeft + ',top=' + popupTop + ',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
            if (window.focus) newWindow.focus();
            return false;
        }

    </script>
</head>

<body bgcolor="#ffffff" onunload="javascript:closeChild();">

<div id="profile_table_header">

    <div class="row_div">
        <div class="heading">Quick Actions:</div>
        <c:choose>
            <c:when test="${not mfsAccountModel.accountClosedUnsettled and not mfsAccountModel.accountClosedSettled }">

                <c:choose>
                    <c:when test="${not empty param.appUserId}">

                        <authz:authorize ifAnyGranted="<%=editButtonsPermission%>">

                            <c:set var="disableLockBtnAfterClick" value="true"/>
                            <c:set var="disableDeactivateBtnAfterClick" value="true"/>

                            <c:set var="disableDeactivateWebSeviceBtnAfterClick" value="true"/>

                            <authz:authorize ifAnyGranted="<%=lockPermission%>">
                                <authz:authorize ifAnyGranted="<%=unlockPermission%>">
                                    <c:set var="disableLockBtnAfterClick" value="false"/>
                                </authz:authorize>
                            </authz:authorize>
                            <authz:authorize ifAnyGranted="<%=deactivatePermission%>">
                                <authz:authorize ifAnyGranted="<%=activatePermission%>">
                                    <c:set var="disableDeactivateBtnAfterClick" value="false"/>
                                </authz:authorize>
                            </authz:authorize>

                            <authz:authorize ifAnyGranted="<%=deactivateWebServicePermission%>">
                                <authz:authorize ifAnyGranted="<%=activateWebServicePermission%>">
                                    <c:set var="disableDeactivateWebSeviceBtnAfterClick" value="false"/>
                                </authz:authorize>
                            </authz:authorize>

                            <authz:authorize ifAnyGranted="<%=deactivatePermission%>">
                                <input id="btn_actDeact" type="button" class="button" value="Deactivate/Reactivate"
                                       disableAfterClick="${disableDeactivateBtnAfterClick}"
                                       onclick="openlockunlockWindow('${param.appUserId}',usecaseId.value,actionID.value,'${mfsAccountModel.customerId}','false')"/>
                            </authz:authorize>
                            <authz:authorize ifNotGranted="<%=deactivatePermission%>">
                                <input id="btn_actDeact" type="button" class="button" value="Deactivate/Reactivate"
                                       disabled="disabled"/>
                            </authz:authorize>

                            <c:if test="${mfsAccountModel.registrationStateId == 3 || mfsAccountModel.registrationStateId == 2 || mfsAccountModel.registrationStateId == 7 ||mfsAccountModel.registrationStateId == 11|| mfsAccountModel.registrationStateId == 14 || mfsAccountModel.hraRegistrationStateId == 3 || mfsAccountModel.hraRegistrationStateId == 2}">
                                <authz:authorize ifAnyGranted="<%=lockPermission%>">
                                    <input id="btn_lockUnlock" type="button" class="button" value="   Block/UnBlock  "
                                           disableAfterClick="${disableLockBtnAfterClick}"
                                           onclick="openlockunlockWindow('${param.appUserId}',usecaseIdLock.value,actionID.value,'${mfsAccountModel.customerId}','true')"/>
                                </authz:authorize>
                                <authz:authorize ifNotGranted="<%=lockPermission%>">
                                    <input id="btn_lockUnlock" type="button" class="button"
                                           value="    Block/UnBlock    " disabled="disabled"/>
                                </authz:authorize>

                                <%--Blacklisting By shawaiz--%>
                                <c:choose>
                                    <c:when test="${blacklisted}">
                                        <authz:authorize ifAnyGranted="<%=blacklistMarkingPermission%>">
                                            <input id="btn_blacklisted" type="button" class="button"
                                                   value="Unmark Blacklisted"
                                                   disableAfterClick="${disableBlacklistBtnAfterClick}"
                                                   onclick="openmarkunmarkBlacklistWindow('${param.appUserId}',usecaseIdBlacklisted.value,actionID.value,'${mfsAccountModel.customerId}','${blacklisted}')"/>&nbsp;
                                        </authz:authorize>
                                        <authz:authorize ifNotGranted="<%=blacklistMarkingPermission%>">
                                            <input id="btn_blacklisted" type="button" class="button"
                                                   value="Unmark Blacklisted" disabled="disabled"/>
                                        </authz:authorize>
                                    </c:when>
                                    <c:otherwise>
                                        <authz:authorize ifAnyGranted="<%=blacklistMarkingPermission%>">
                                            <input id="btn_blacklisted" type="button" class="button"
                                                   value="Mark Blacklisted"
                                                   disableAfterClick="${disableBlacklistBtnAfterClick}"
                                                   onclick="openmarkunmarkBlacklistWindow('${param.appUserId}',usecaseIdBlacklisted.value,actionID.value,'${mfsAccountModel.customerId}','${blacklisted}')"/>
                                        </authz:authorize>
                                        <authz:authorize ifNotGranted="<%=blacklistMarkingPermission%>">
                                            <input id="btn_blacklisted" type="button" class="button"
                                                   value="Mark Blacklisted" disabled="disabled"/>
                                        </authz:authorize>
                                    </c:otherwise>
                                </c:choose>

                                <%--MarkAccountAsSaving By Masab--%>
                                <%--								<c:choose>--%>
                                <%--									<c:when test="${accountNatureId}">--%>
                                <%--										<authz:authorize ifAnyGranted="<%=accountAsSavingMarkingPermission%>">--%>
                                <%--											<input id="btn_accountIsSaving" type="button" class="button" value="Unmark As Saving" disableAfterClick="${disableAsSavingBtnAfterClick}" onclick="markAccountAsSavingWindow('${param.appUserId}',usecaseIdAsSaving.value,actionID.value,'${mfsAccountModel.customerId}','${accountNatureId}')"/>&nbsp;--%>
                                <%--										</authz:authorize>--%>
                                <%--										<authz:authorize ifNotGranted="<%=accountAsSavingMarkingPermission%>">--%>
                                <%--											<input id="btn_accountIsSaving" type="button" class="button" value="Unmark As Saving" disabled="disabled" />--%>
                                <%--										</authz:authorize>--%>
                                <%--									</c:when>--%>
                                <%--									<c:otherwise>--%>
                                <%--										<authz:authorize ifAnyGranted="<%=accountAsSavingMarkingPermission%>">--%>
                                <%--											<input id="btn_accountIsSaving" type="button" class="button" value="Mark As Saving" disableAfterClick="${disableAsSavingBtnAfterClick}" onclick="markAccountAsSavingWindow('${param.appUserId}',usecaseIdAsSaving.value,actionID.value,'${mfsAccountModel.customerId}','${accountNatureId}')"/>--%>
                                <%--										</authz:authorize>--%>
                                <%--										<authz:authorize ifNotGranted="<%=accountAsSavingMarkingPermission%>">--%>
                                <%--											<input id="btn_accountIsSaving" type="button" class="button" value="Mark As Saving" disabled="disabled" />--%>
                                <%--										</authz:authorize>--%>
                                <%--									</c:otherwise>--%>
                                <%--								</c:choose>--%>

                                <%--                                &lt;%&ndash;MarkAccountAsSaving By Ahsan&ndash;%&gt;--%>
                                <%--                                <c:choose>--%>
                                <%--                                    <c:when test="${blacklisted}">--%>
                                <%--                                        <authz:authorize ifAnyGranted="<%=blacklistMarkingPermission%>">--%>
                                <%--                                            <input id="btn_blacklisted" type="button" class="button" value="Unmark Blacklisted" disableAfterClick="${disableBlacklistBtnAfterClick}" onclick="openmarkunmarkBlacklistWindow('${param.appUserId}',usecaseIdBlacklisted.value,actionID.value,'${mfsAccountModel.customerId}','${blacklisted}')"/>&nbsp;--%>
                                <%--                                        </authz:authorize>--%>
                                <%--                                        <authz:authorize ifNotGranted="<%=blacklistMarkingPermission%>">--%>
                                <%--                                            <input id="btn_blacklisted" type="button" class="button" value="Unmark Blacklisted" disabled="disabled" />--%>
                                <%--                                        </authz:authorize>--%>
                                <%--                                    </c:when>--%>
                                <%--                                    <c:otherwise>--%>
                                <%--                                        <authz:authorize ifAnyGranted="<%=blacklistMarkingPermission%>">--%>
                                <%--                                            <input id="btn_blacklisted" type="button" class="button" value="Mark Blacklisted" disableAfterClick="${disableBlacklistBtnAfterClick}" onclick="openmarkunmarkBlacklistWindow('${param.appUserId}',usecaseIdBlacklisted.value,actionID.value,'${mfsAccountModel.customerId}','${blacklisted}')"/>--%>
                                <%--                                        </authz:authorize>--%>
                                <%--                                        <authz:authorize ifNotGranted="<%=blacklistMarkingPermission%>">--%>
                                <%--                                            <input id="btn_blacklisted" type="button" class="button" value="Mark Blacklisted" disabled="disabled" />--%>
                                <%--                                        </authz:authorize>--%>
                                <%--                                    </c:otherwise>--%>
                                <%--                                </c:choose>--%>


                                <authz:authorize ifAnyGranted="<%=debitBlockPermission%>">
                                    <c:choose>
                                        <c:when test="${mfsAccountModel.isDebitBlocked}">
                                            <input id="btn_debitblock" type="button" class="button"
                                                   value="Debit Unblock"
                                                   onclick="openDebitBlockWindow('${param.appUserId}',1237,actionID.value,'${pageMfsId}', '${mfsAccountModel.zongNo}', '${mfsAccountModel.debitBlockAmount}', '${mfsAccountModel.isDebitBlocked}')"/>
                                        </c:when>
                                        <c:when test="${!mfsAccountModel.isDebitBlocked}">
                                            <input id="btn_debitblock" type="button" class="button" value="Debit Block"
                                                   onclick="openDebitBlockWindow('${param.appUserId}',1238,actionID.value,'${pageMfsId}', '${mfsAccountModel.zongNo}', '${mfsAccountModel.debitBlockAmount}', '${mfsAccountModel.isDebitBlocked}')"/>
                                        </c:when>
                                    </c:choose>
                                </authz:authorize>
                                <authz:authorize ifNotGranted="<%=debitBlockPermission%>">
                                    <input id="btn_debitblock_disabled" type="button" class="button"
                                           value="Manage Debit Block" disabled="disabled"/>
                                </authz:authorize>

                                <%-- <authz:authorize ifAnyGranted="<%=PortalConstants.UPDATE_CUSTOMER_MOBILE%>">
                                <c:if test="${deviceAccEnabled and not deviceAccLocked}">
                                    <input type="button" class="button" value="Update Mobile No."  onclick="javascript:document.location='p_mobilehistory.html?appUserId=${param.appUserId}&customerId=${mfsAccountModel.customerId}&mobileNo=${mfsAccountModel.mobileNo}';"  />
                                </c:if>
                                <c:if test="${not deviceAccEnabled or deviceAccLocked}">
                                    <input type="button" class="button" value="Update Mobile No." disabled="disabled" />
                                </c:if>
                                </authz:authorize>
                                <authz:authorize ifAnyGranted="<%=PortalConstants.UPDATE_CUSTOMER_CNIC%>">
                                <c:if test="${deviceAccEnabled and not deviceAccLocked}">
                                    <input type="button" class="button" value="Update CNIC"  onclick="javascript:document.location='p_cnichistory.html?appUserId=${param.appUserId}&customerId=${mfsAccountModel.customerId}&nic=${mfsAccountModel.nic}';"  />
                                </c:if>
                                <c:if test="${not deviceAccEnabled or deviceAccLocked}">
                                    <input type="button" class="button" value="Update CNIC" disabled="disabled" />
                                </c:if>
                                </authz:authorize> --%>
                            </c:if>

                            <authz:authorize ifAnyGranted="<%=PortalConstants.UPDATE_CUSTOMER_MOBILE%>">
                                <c:if test="${not deviceAccLocked}">
                                    <input type="button" class="button" value="Update Mobile No."
                                           onclick="javascript:document.location='p_mobilehistory.html?appUserId=${param.appUserId}&customerId=${mfsAccountModel.customerId}&mobileNo=${mfsAccountModel.mobileNo}';"/>
                                </c:if>
                                <c:if test="${deviceAccLocked}">
                                    <input type="button" class="button" value="Update Mobile No." disabled="disabled"/>
                                </c:if>
                            </authz:authorize>
                            <authz:authorize ifAnyGranted="<%=PortalConstants.UPDATE_CUSTOMER_CNIC%>">
                                <c:if test="${not deviceAccLocked}">
                                    <input type="button" class="button" value="Update CNIC"
                                           onclick="javascript:document.location='p_cnichistory.html?appUserId=${param.appUserId}&customerId=${mfsAccountModel.customerId}&nic=${mfsAccountModel.nic}';"/>
                                </c:if>
                                <c:if test="${deviceAccLocked}">
                                    <input type="button" class="button" value="Update CNIC" disabled="disabled"/>
                                </c:if>
                            </authz:authorize>

                            <authz:authorize ifAnyGranted="<%=closeAccountPermission%>">
                                <input id="btn_closeAccount" type="button" class="button" value="Close Account"
                                       onclick="openAccountClosingWindow('${mfsAccountModel.appUserId}','${mfsAccountModel.customerId}')"/>
                            </authz:authorize>
                            <authz:authorize ifNotGranted="<%=closeAccountPermission%>">
                                <input id="btn_closeAccount" type="button" class="button" value="Close Account"
                                       disabled="disabled"/>
                            </authz:authorize>

                            <authz:authorize ifAnyGranted="<%=accountAsSavingMarkingPermission%>">
                                <input id="btn_accountIsSaving" type="button" class="button" value="Mark AccountType"
                                       onclick="markAccountAsSavingWindow('${mfsAccountModel.appUserId}','${mfsAccountModel.customerId}')"/>
                            </authz:authorize>
                            <authz:authorize ifNotGranted="<%=accountAsSavingMarkingPermission%>">
                                <input id="btn_accountIsSaving" type="button" class="button" value="Mark AccountType"
                                       disabled="disabled"/>
                            </authz:authorize>

                            <input type="hidden" id="usecaseId"
                                   value="<%=PortalConstants.REACTIVATE_CUSTOMER_USECASE_ID%>"/>
                            <input type="hidden" id="usecaseIdLock"
                                   value="<%=PortalConstants.UNBLOCK_CUSTOMER_USECASE_ID%>"/>
                            <input type="hidden" id="actionID" value="<%=PortalConstants.ACTION_UPDATE%>"/>
                            <input type="hidden" id="message" value=""/>
                            <input type="hidden" id="appUsrId_${param.appUserId}" value="${param.appUserId}"/>
                            <input type="hidden" id="referrer" value="${referrer}"/>
                            <input type="hidden" id="usecaseIdBlacklisted"
                                   value="<%=PortalConstants.MARK_BLACKLISTED_USECASE_ID%>"/>
                            <%--							<input type="hidden" id="usecaseIdAsSaving" value="<%=PortalConstants.MARK_ACCOUNT_AS_SAVING%>"/>--%>
                            <input type="hidden" id="usecaseIdWebServiceActDeact"
                                   value="<%=PortalConstants.DEACTIVATE_WEB_SERVICE_USECASE_ID%>"/>
                            <!--
                            <ajax:updateField
                                    baseUrl="${contextPath}/p_pgdeactivatedeactivate.html"
                                    source="btn_actDeact"
                                    target="btn_actDeact,message"
                                    action="btn_actDeact"
                                    parameters="appUsrId={appUsrId_${param.appUserId}},usecaseId={usecaseId},btn=btn_actDeact,actionId={actionID}"
                                    parser="new ResponseXmlParser()"
                                    preFunction="initProgress"
                                    postFunction="resetProgressDeactivate"
                                    errorFunction="globalAjaxErrorFunction"
                            />

                            <input type="hidden" id="isLockUnlock" value="true"/>
                            <input type="hidden" id="lock_message" value=""/>
                            <ajax:updateField
                                    baseUrl="${contextPath}/p_pgdeactivatedeactivate.html"
                                    source="btn_lockUnlock"
                                    target="btn_lockUnlock,lock_message"
                                    action="btn_lockUnlock"
                                    parameters="appUsrId={appUsrId_${param.appUserId}},usecaseId={usecaseIdLock},btn=btn_lockUnlock,isLockUnlock={isLockUnlock},actionId={actionID}"
                                    parser="new ResponseXmlParser()"
                                    preFunction="initProgress"
                                    postFunction="resetProgressLock"
                                    errorFunction="globalAjaxErrorFunction"
                            />

                            -->

                            <c:set var="smartMoneyAccountId"><security:encrypt strToEncrypt="${smAccountId}"/></c:set>
                            <c:if test="${mfsAccountModel.registrationStateId == 3 or mfsAccountModel.registrationStateId == 2 or mfsAccountModel.registrationStateId == 14}">
                                <authz:authorize ifAnyGranted="<%=regeneratePINPermission%>">
                                    <c:choose>
                                        <c:when test="${(not empty smAccountId) && veriflyRequired  && (not deviceAccLocked)}">
                                            <input type="button" class="button" value="Regenerate PIN"
                                                   id="btnSmAcId${smAccountId}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="button" class="button" value="Regenerate PIN"
                                                   id="btnSmAcId${smAccountId}" disabled="disabled"/>
                                        </c:otherwise>
                                    </c:choose>
                                </authz:authorize>
                                <authz:authorize ifNotGranted="<%=regeneratePINPermission%>">
                                    <input type="button" class="button" value="Regenerate PIN"
                                           id="btnSmAcId${smAccountId}" disabled="disabled"/>
                                </authz:authorize>
                                <authz:authorize ifAnyGranted="<%=updatePINPermission%>">
                                    <c:choose>
                                        <c:when test="${(not empty smAccountId) && veriflyRequired && deviceAccEnabled && (not deviceAccLocked) && (not credentialsExpired)}">
                                            <input type="button" class="button" value="Change PIN" id="btnChngPin"/>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="button" class="button" value="Change PIN" id="btnChngPin"
                                                   disabled="disabled"/>
                                        </c:otherwise>
                                    </c:choose>
                                </authz:authorize>
                                <authz:authorize ifNotGranted="<%=updatePINPermission%>">
                                    <input type="button" class="button" value="Change PIN" id="btnChngPin"
                                           disabled="disabled"/>
                                </authz:authorize>
                                <input type="hidden" value="${param.appUserId}" name="appUser${param.appUserId}"
                                       id="appUser${param.appUserId}"/>
                                <input type="hidden" value="${mfsAccountModel.zongNo}" name="${mfsAccountModel.zongNo}"
                                       id="${mfsAccountModel.zongNo}"/>
                                <input type="hidden" value="${smAccountId}" name="smAcId${smAccountId}"
                                       id="smAcId${smAccountId}"/>
                                <input type="hidden" id="usecaseId${param.appUserId}"
                                       value="<%=PortalConstants.FORGOT_VERIFLY_PIN_USECASE_ID%>"/>
                                <ajax:htmlContent baseUrl="${contextPath}/p_forgotveriflypinChange.html"
                                                  eventType="click"
                                                  source="btnSmAcId${smAccountId}"
                                                  target="successMsg"
                                                  parameters="appUserId={appUser${param.appUserId}},mfsId=${pageMfsId},smAcId={smAcId${smAccountId}},usecaseId={usecaseId${param.appUserId}},actionId={actionID},mobileNo=${mfsAccountModel.zongNo}"
                                                  errorFunction="globalAjaxErrorFunction"
                                                  preFunction="initProgress"
                                                  postFunction="resetProgress"
                                />

                                <ajax:htmlContent baseUrl="${contextPath}/p_pinchange.html"
                                                  eventType="click"
                                                  source="btnChngPin"
                                                  target="successMsg"
                                                  parameters="mobileNo={${mfsAccountModel.zongNo}},actionId={actionID}"
                                                  errorFunction="globalAjaxErrorFunction"
                                                  preFunction="initProgress"
                                                  postFunction="resetProgress"
                                />
                            </c:if>
                        </authz:authorize>
                        <c:if test="${mfsAccountModel.registrationStateId == 3 or mfsAccountModel.registrationStateId == 14}">
                            <authz:authorize ifNotGranted="<%=editButtonsPermission%>">
                                <c:choose>
                                    <c:when test="${not deviceAccLocked}">
                                        <input id="btn_lockUnlock" type="button" class="button" value="    Block    "
                                               disabled="disabled"/>
                                    </c:when>
                                    <c:otherwise>
                                        <input id="btn_lockUnlock" type="button" class="button" value="  Unblock  "
                                               disabled="disabled"/>
                                    </c:otherwise>
                                </c:choose>

                                <c:choose>
                                    <c:when test="${deviceAccEnabled}">
                                        <input id="btn_actDeact" type="button" class="button" value="Deactivate"
                                               disabled="disabled"/>
                                    </c:when>
                                    <c:otherwise>
                                        <input id="btn_actDeact" type="button" class="button" value="Reactivate"
                                               disabled="disabled"/>
                                    </c:otherwise>
                                </c:choose>
                                <input type="button" class="button" value="Regenerate PIN"
                                       id="btnSmAcId${smartMoneyAccountId}" disabled="disabled"/>
                            </authz:authorize>
                        </c:if>

                    </c:when>
                </c:choose>
            </c:when>
        </c:choose>

        <c:choose>
            <c:when test="${webServiceEnabled}">
                <authz:authorize ifAnyGranted="<%=deactivateWebServicePermission%>">
                    <input id="btn_webserDeact" type="button" class="button" value="Deactivate Web Service"
                           disableAfterClick="${disableDeactivateBtnAfterClick}"/>
                </authz:authorize>
                <authz:authorize ifNotGranted="<%=deactivateWebServicePermission%>">
                    <input id="btn_webserDeact" type="button" class="button" value="Deactivate Web Service"
                           disabled="disabled"/>
                </authz:authorize>
            </c:when>
            <c:otherwise>

                <authz:authorize ifAnyGranted="<%=activateWebServicePermission%>">
                    <input id="btn_webserDeact" type="button" class="button" value="Activate Web Service"
                           disableAfterClick="${disableDeactivateBtnAfterClick}"/>
                </authz:authorize>
                <authz:authorize ifNotGranted="<%=activateWebServicePermission%>">
                    <input id="btn_webserDeact" type="button" class="button" value="Activate Web Service"
                           disabled="disabled"/>
                </authz:authorize>
            </c:otherwise>
        </c:choose>

        <ajax:htmlContent baseUrl="${contextPath}/p_activatedeactivatewebserviceform.html"
                          eventType="click"
                          source="btn_webserDeact"
                          target="successMsg"
                          parameters="appUserId={appUser${param.appUserId}},usecaseId={usecaseIdWebServiceActDeact},actionId={actionID},webServiceEnabled=${webServiceEnabled}"
                          errorFunction="globalAjaxErrorFunction"
                          preFunction="initProgress"
                          postFunction="resetProgressWebService"
        />

        <authz:authorize ifAnyGranted="<%=updateCustomerLoginPINPermission%>">
            <c:choose>
                <c:when test="${deviceAccEnabled && (not deviceAccLocked)}">
                    <input type="button" class="button" value="Regenerate Login PIN" id="btnRegPin${param.appUserId}"/>
                </c:when>
                <c:otherwise>
                    <input type="button" class="button" value="Regenerate Login PIN" id="btnRegPin${param.appUserId}"
                           disabled="disabled"/>
                </c:otherwise>
            </c:choose>
            <input type="hidden" value="${param.appUserId}" name="appUser${param.appUserId}"
                   id="appUser${param.appUserId}"/>
            <ajax:htmlContent baseUrl="${contextPath}/changecustomeruserdevicepin.html"
                              eventType="click"
                              source="btnRegPin${param.appUserId}"
                              target="successMsg"
                              parameters="appUserId={appUser${param.appUserId}},mfsId=${pageMfsId}"
                              errorFunction="globalAjaxErrorFunction"
                              preFunction="initProgress"
                              postFunction="resetProgress"
            />
        </authz:authorize>

        <authz:authorize ifAnyGranted="<%=updateDormantPermission%>">
            <c:choose>
                <c:when test="${not empty param.appUserId and  (mfsAccountModel.registrationStateId == 9 or mfsAccountModel.hraRegistrationStateId == 9 or mfsAccountModel.registrationStateId == 11)}">
                    <input id="btn_removeDormancy" type="button" class="button" value="Restore from Dormancy"
                           onclick="openRemoveDormancyWindow('${mfsAccountModel.appUserId}','${mfsAccountModel.customerId}')"/>
                </c:when>
                <c:otherwise>
                    <input id="btn_removeDormancy" type="button" class="button" value="Restore from Dormancy"
                           disabled="disabled"/>
                </c:otherwise>
            </c:choose>

            <ajax:htmlContent baseUrl="${contextPath}/changedormantstate.html?appUserId=${param.appUserId}"
                              eventType="click"
                              source="appUserId${param.appUserId}"
                              target="successMsg"
                              parameters="appUserId={appUserId${param.appUserId}}"
                              errorFunction="globalAjaxErrorFunction"
                              preFunction="initProgress"
                              postFunction="resetProgressWebService"
            />
        </authz:authorize>


        <!--Ahsan Raza-->



        <c:choose>
            <c:when test="${isCustomerUSSDEnabled}">
                <input type="button" class="button" value="Update Customer Cnic Expiry"
                       id="btn_updatecnicexpiry${isCustomerUSSDEnabled}"/>
            </c:when>
            <c:otherwise>
                <input type="button" class="button" value="Update Customer Cnic Expiry"
                       id="btn_updatecnicexpiry${isCustomerUSSDEnabled}"/>
            </c:otherwise>
        </c:choose>
        <input type="hidden" value="${param.appUserId}" name="appUser${param.appUserId}"
               id="appUser${param.appUserId}"/>
        <ajax:htmlContent baseUrl="${contextPath}/updateCnicExpiry.html"
                          eventType="click"
                          source="btn_updatecnicexpiry${isCustomerUSSDEnabled}"
                          target="successMsg"
                          parameters="appUserId={appUser${param.appUserId}},mfsId=${mfsAccountModel.mfsId},isEnabled=${isCustomerUSSDEnabled},customerAppUserId=${param.appUserId}"
                          errorFunction="globalAjaxErrorFunction"
                          preFunction="initProgress"
                          postFunction="resetProgressWebService"
        />
        <!--Ahsan Raza End-->

        <!--UnBlock JS Wallet Device-->

        <c:choose>
            <c:when test="${mfsAccountModel.deviceStatus==('UN-Locked')}">
                <input type="button" class="button" value="UnBlock JS Wallet Device"
                       id="btn_unblockjswallet${isCustomerUSSDEnabled}"  disabled="disabled"/>
            </c:when>
            <c:otherwise>
                <input type="button" class="button" value="UnBlock JS Wallet Device"
                       id="btn_unblockjswallet${isCustomerUSSDEnabled}"/>
            </c:otherwise>
        </c:choose>
        <input type="hidden" value="${param.appUserId}" name="appUser${param.appUserId}"
               id="appUser${param.appUserId}"/>
        <ajax:htmlContent baseUrl="${contextPath}/unBlockJsWallet.html"
                          eventType="click"
                          source="btn_unblockjswallet${isCustomerUSSDEnabled}"
                          target="successMsg"
                          parameters="appUserId={appUser${param.appUserId}},mfsId=${mfsAccountModel.mfsId},isEnabled=${isCustomerUSSDEnabled},customerAppUserId=${param.appUserId}"
                          errorFunction="globalAjaxErrorFunction"
                          preFunction="initProgress"
                          postFunction="resetProgressWebService"
        />

        <!--UnBlock JS Wallet Device-->



        <!--UnBlock JS Wallet Device-->

        <c:choose>
            <c:when test="${isCustomerUSSDEnabled}">
                <input type="button" class="button" value="UnBlock Zindigi Device"
                       id="btn_unblockzindigi${isCustomerUSSDEnabled}"/>
            </c:when>
            <c:otherwise>
                <input type="button" class="button" value="UnBlock Zindigi Device"
                       id="btn_unblockzindigi${isCustomerUSSDEnabled}"/>
            </c:otherwise>
        </c:choose>
        <input type="hidden" value="${param.appUserId}" name="appUser${param.appUserId}"
               id="appUser${param.appUserId}"/>
        <ajax:htmlContent baseUrl="${contextPath}/unBlockZindigi.html"
                          eventType="click"
                          source="btn_unblockzindigi${isCustomerUSSDEnabled}"
                          target="successMsg"
                          parameters="appUserId={appUser${param.appUserId}},mfsId=${mfsAccountModel.mfsId},isEnabled=${isCustomerUSSDEnabled},customerAppUserId=${param.appUserId}"
                          errorFunction="globalAjaxErrorFunction"
                          preFunction="initProgress"
                          postFunction="resetProgressWebService"
        />

        <!--UnBlock JS Wallet Device-->

        <c:choose>
            <c:when test="${isCustomerUSSDEnabled}">
                <input type="button" class="button" value="Disable Customer USSD"
                       id="btn_agentUssd${isCustomerUSSDEnabled}"/>
            </c:when>
            <c:otherwise>
                <input type="button" class="button" value="Enable Customer USSD"
                       id="btn_agentUssd${isCustomerUSSDEnabled}"/>
            </c:otherwise>
        </c:choose>
        <input type="hidden" value="${param.appUserId}" name="appUser${param.appUserId}"
               id="appUser${param.appUserId}"/>
        <ajax:htmlContent baseUrl="${contextPath}/enableDisableAgentUSSD.html"
                          eventType="click"
                          source="btn_agentUssd${isCustomerUSSDEnabled}"
                          target="successMsg"
                          parameters="appUserId={appUser${param.appUserId}},mfsId=${mfsAccountModel.mfsId},isEnabled=${isCustomerUSSDEnabled},customerAppUserId=${param.appUserId}"
                          errorFunction="globalAjaxErrorFunction"
                          preFunction="initProgress"
                          postFunction="resetProgressWebService"
        />


        <input type="button" class="button" value="Back" tabindex="49"
               onclick="javascript:document.location='p_pgsearchuserinfo.html?actionId=2';"/>
        <div style="clear:both;"></div>
    </div>

    <div class="row_div">
        <div class="heading">View Customer Details:</div>


        <c:if test="${mfsAccountModel.registrationStateId == 10
					|| mfsAccountModel.registrationStateId == 3 or mfsAccountModel.registrationStateId == 2
					|| mfsAccountModel.hraRegistrationStateId == 3 || mfsAccountModel.hraRegistrationStateId == 2 || mfsAccountModel.registrationStateId == 14}">

            <c:if test="${mfsAccountModel.registrationStateId == 3 or mfsAccountModel.registrationStateId == 2 or mfsAccountModel.registrationStateId == 10 or mfsAccountModel.registrationStateId == 14}">
                <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_REQ_CHARGEBACK_READ%>">
                    <input type="button" class="button" value="View Chargeback" tabindex="47"
                           onclick="javascript:document.location='p_customerchargebacklist.html?searchKey=${pageMfsId}&actionId=2';"/>&nbsp;
                </authz:authorize>
                <authz:authorize ifNotGranted="<%=PortalConstants.PERMS_REQ_CHARGEBACK_READ%>">
                    <input type="button" class="button" value="View Chargeback" tabindex="47" disabled="disabled"/>&nbsp;
                </authz:authorize>
            </c:if>
            <authz:authorize ifAnyGranted="<%=bbStmtPermission%>">
                <input type="button" class="button" value="View BB Statement" tabindex="48"
                       onclick="javascript:document.location='viewcustomerbbstatementmanagement.html?appUserId=${param.appUserId}&actionId=2&referrer=<%=referrer%>'"/>&nbsp;
            </authz:authorize>
            <authz:authorize ifAnyGranted="<%=txHistPermission%>">
                <input type="button" class="button" value="Transaction History" tabindex="49"
                       onclick="javascript:document.location='p-customertransactiondetails.html?actionId=2&mfsId=${pageMfsId}&mobileNo=${mfsAccountModel.zongNo}&appUserId=${param.appUserId}&referrer=<%=referrer%>'"/>&nbsp;
            </authz:authorize>
            <!--<authz:authorize ifAnyGranted="<%=cashPaymentPermission%>">
            <input type="button" class="button" value="Cash Payment Requests" tabindex="50" onclick="javascript:document.location='p_cashwithdrawaltransdetails.html?actionId=2&appUserId=${param.appUserId}&registered=true&referrer=<%=referrer%>'" />&nbsp;
        </authz:authorize>
            commented on request of QA-->
            <authz:authorize ifAnyGranted="<%=complaintPermission%>">
                <input type="button" class="button" value="Add Complaint" tabindex="51"
                       onclick="javascript:document.location='p_complaintform.html?appUserId=${param.appUserId}&actionId=1&usrType=2';"/>&nbsp;
            </authz:authorize>
        </c:if>
        <c:if test="${ mfsAccountModel.registrationStateId == 4 or mfsAccountModel.registrationStateId == 5 or mfsAccountModel.registrationStateId == 6 or mfsAccountModel.registrationStateId == 9 or mfsAccountModel.registrationStateId == 11 }">
            <authz:authorize ifAnyGranted="<%=bbStmtPermission%>">
                <input type="button" class="button" value="View BB Statement" tabindex="48"
                       onclick="javascript:document.location='viewcustomerbbstatementmanagement.html?appUserId=${param.appUserId}&actionId=2&referrer=<%=referrer%>'"/>&nbsp;
            </authz:authorize>
        </c:if>


        <c:choose>
            <c:when test="${mfsAccountModel.accountClosedUnsettled and not mfsAccountModel.accountClosedSettled}">
                <authz:authorize ifAnyGranted="<%=closeAccountPermission%>">
                    <input id="btn_settlement" type="button" class="button" value="Settle Account"
                           onclick="openAccountClosingWindow('${mfsAccountModel.appUserId}','${mfsAccountModel.customerId}')"/>
                </authz:authorize>
                <authz:authorize ifNotGranted="<%=closeAccountPermission%>">
                    <input id="btn_settlement" type="button" class="button" value="Settle Account" disabled="disabled"/>
                </authz:authorize>
            </c:when>
        </c:choose>
        <div style="clear:both;"></div>
    </div>
</div>

<div id="successMsg" class="infoMsg" style="display:none;"></div>
<div id="errorMsg" class="errorMsg" style="display:none;"></div>
<c:if test="${not empty sessionScope.ajaxMessageToDisplay}">
    <div id="ajaxMsgDiv" class="infoMsg">
        <c:out value="${sessionScope.ajaxMessageToDisplay}"/><c:remove var="ajaxMessageToDisplay" scope="session"/>
    </div>
</c:if>

<spring:bind path="mfsAccountModel.*">
    <c:if test="${not empty status.errorMessages}">
        <div class="errorMsg">
            <c:forEach var="error" items="${status.errorMessages}">
                <c:out value="${error}" escapeXml="false"/>
                <br/>
            </c:forEach>
        </div>
    </c:if>
</spring:bind>

<c:if test="${not empty messages}">
    <div class="infoMsg" id="successMessages">
        <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false"/>
            <br/>
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session"/>
</c:if>
<!---******************************START LEVEL 0 FORM   ********************************************************-->


<table class="profile_table" width="98%" border="0" cellpadding="0" cellspacing="1" style="margin:0px auto;">

    <tr>
        <td width="50%" valign="top">
            <fieldset>
                <legend>Profile Details</legend>
                <%--<legend><c:out value="${mfsAccountModel.accounttypeName}" /> Details</legend>--%>
                <table width="100%" border="0" cellpadding="0" cellspacing="1">
                    <tr>
                        <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Mobile No:</td>
                        <td width="60%" align="left" bgcolor="FBFBFB">${mfsAccountModel.zongNo}</td>
                    </tr>
                    <tr>
                        <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">CNIC #:</td>
                        <td width="60%" align="left" bgcolor="FBFBFB">${mfsAccountModel.nic}</td>
                    </tr>
                    <tr>
                        <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">CNIC Expiry:</td>
                        <td width="60%" align="left" bgcolor="FBFBFB"><fmt:formatDate dateStyle="medium"
                                                                                      value="${mfsAccountModel.nicExpiryDate}"/></td>
                    </tr>
                    `
                    <tr>
                        <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Segment:</td>
                        <td width="60%" align="left" bgcolor="FBFBFB">${mfsAccountModel.segmentNameStr}</td>
                    </tr>
                    <tr>
                        <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Registration
                            State:
                        </td>
                        <td width="60%" align="left" bgcolor="FBFBFB">${mfsAccountModel.regStateName}</td>
                    </tr>
                    <tr>
                        <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Current Status:</td>
                        <td width="60%" align="left" bgcolor="FBFBFB">
                            <c:choose>
                                <c:when test="${mfsAccountModel.accountClosedUnsettled and not mfsAccountModel.accountClosedSettled}">Closed Unsettled</c:when>
                                <c:when test="${mfsAccountModel.accountClosedUnsettled and mfsAccountModel.accountClosedSettled}">Closed Settled</c:when>
                                <c:when test="${deviceAccLocked && not mfsAccountModel.accountClosedUnsettled}">Blocked</c:when>
                                <c:when test="${not deviceAccEnabled && not mfsAccountModel.accountClosedUnsettled}">Deactivated</c:when>
                                <c:when test="${credentialsExpired && not mfsAccountModel.accountClosedUnsettled}">Credentials Expired</c:when>
                                <c:when test="${not deviceAccLocked && deviceAccEnabled && not credentialsExpired && not mfsAccountModel.accountClosedUnsettled}">Active</c:when>
                                <c:otherwise>Unknown</c:otherwise>
                            </c:choose>
                            ${statusDetails}
                        </td>
                    </tr>
                    <tr>
                        <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">NADRA
                            Verification:
                        </td>
                        <td width="60%" align="left" bgcolor="FBFBFB">
                            <c:if test="${mfsAccountModel.verisysDone}">Yes</c:if>
                            <c:if test="${!mfsAccountModel.verisysDone}">No</c:if>
                        </td>
                    </tr>
                    <tr>
                        <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Screening:</td>
                        <td width="60%" align="left" bgcolor="FBFBFB">
                            <c:if test="${mfsAccountModel.screeningPerformed}">Match</c:if>
                            <c:if test="${!mfsAccountModel.screeningPerformed}">Not Match</c:if>
                        </td>
                    </tr>
                    <tr>
                        <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Original CNIC
                            Seen:
                        </td>
                        <td width="60%" align="left" bgcolor="FBFBFB">Yes</td>
                    </tr>


                    <tr>
                        <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Reason Of Blocking:
                        </td>
                        <td width="60%" align="left" bgcolor="FBFBFB">${mfsAccountModel.reasonOfBlock}</td>
                    </tr>

                    <tr>
                        <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Device Status:</td>
                        <td width="60%" align="left" bgcolor="FBFBFB">${mfsAccountModel.deviceStatus}</td>
                    </tr>
                </table>
            </fieldset>
            <fieldset>
                <legend>Customer Details</legend>
                <table width="100%" border="0" cellpadding="0" cellspacing="1">
                    <tr>
                        <td align="center">
                            <style>
                                .captionOrange, .captionBlack {
                                    color: #fff;
                                    font-size: 20px;
                                    line-height: 30px;
                                    text-align: center;
                                    border-radius: 4px;
                                }

                                .captionOrange {
                                    background: #EB5100;
                                    background-color: rgba(235, 81, 0, 0.6);
                                }

                                .captionBlack {
                                    font-size: 16px;
                                    background: #000;
                                    background-color: rgba(0, 0, 0, 0.4);
                                }

                                a.captionOrange, A.captionOrange:active, A.captionOrange:visited {
                                    color: #ffffff;
                                    text-decoration: none;
                                }

                                a.captionOrange:hover {
                                    color: #eb5100;
                                    text-decoration: underline;
                                    background-color: #eeeeee;
                                    background-color: rgba(238, 238, 238, 0.7);
                                }

                                .bricon {
                                    background: url(${contextPath}/images/jssor/browser-icons.png);
                                }
                            </style>
                            <!-- use jssor.slider.min.js instead for release -->
                            <!-- jssor.slider.min.js = (jssor.core.js + jssor.utils.js + jssor.slider.js) -->
                            <script type="text/javascript"
                                    src="${pageContext.request.contextPath}/scripts/jssor.core.js"></script>
                            <script type="text/javascript"
                                    src="${pageContext.request.contextPath}/scripts/jssor.utils.js"></script>
                            <script type="text/javascript"
                                    src="${pageContext.request.contextPath}/scripts/jssor.slider.js"></script>

                            <script>
                                jssor_sliderb_starter = function (containerId) {
                                    //Reference http://www.jssor.com/development/slider-with-slideshow-no-jquery.html
                                    //Reference http://www.jssor.com/development/tool-slideshow-transition-viewer.html

                                    var _SlideshowTransitions = [
                                        //Fade in R
                                        {
                                            $Duration: 1200,
                                            x: -0.3,
                                            $During: {$Left: [0.3, 0.7]},
                                            $Easing: {
                                                $Left: $JssorEasing$.$EaseInCubic,
                                                $Opacity: $JssorEasing$.$EaseLinear
                                            },
                                            $Opacity: 2
                                        }
                                        //Fade out L
                                        , {
                                            $Duration: 1200,
                                            x: 0.3,
                                            $SlideOut: true,
                                            $Easing: {
                                                $Left: $JssorEasing$.$EaseInCubic,
                                                $Opacity: $JssorEasing$.$EaseLinear
                                            },
                                            $Opacity: 2
                                        }
                                    ];

                                    var options = {
                                        $AutoPlay: true,                                    //[Optional] Whether to auto play, to enable slideshow, this option must be set to true, default value is false
                                        $AutoPlaySteps: 1,                                  //[Optional] Steps to go for each navigation request (this options applys only when slideshow disabled), the default value is 1
                                        $AutoPlayInterval: 4000,                            //[Optional] Interval (in milliseconds) to go for next slide since the previous stopped if the slider is auto playing, default value is 3000
                                        $PauseOnHover: 1,                               //[Optional] Whether to pause when mouse over if a slider is auto playing, 0 no pause, 1 pause for desktop, 2 pause for touch device, 3 pause for desktop and touch device, 4 freeze for desktop, 8 freeze for touch device, 12 freeze for desktop and touch device, default value is 1

                                        $ArrowKeyNavigation: true,   			            //[Optional] Allows keyboard (arrow key) navigation or not, default value is false
                                        $SlideDuration: 500,                                //[Optional] Specifies default duration (swipe) for slide in milliseconds, default value is 500
                                        $MinDragOffsetToSlide: 20,                          //[Optional] Minimum drag offset to trigger slide , default value is 20
                                        //$SlideWidth: 600,                                 //[Optional] Width of every slide in pixels, default value is width of 'slides' container
                                        //$SlideHeight: 300,                                //[Optional] Height of every slide in pixels, default value is height of 'slides' container
                                        $SlideSpacing: 0, 					                //[Optional] Space between each slide in pixels, default value is 0
                                        $DisplayPieces: 1,                                  //[Optional] Number of pieces to display (the slideshow would be disabled if the value is set to greater than 1), the default value is 1
                                        $ParkingPosition: 0,                                //[Optional] The offset position to park slide (this options applys only when slideshow disabled), default value is 0.
                                        $UISearchMode: 1,                                   //[Optional] The way (0 parellel, 1 recursive, default value is 1) to search UI components (slides container, loading screen, navigator container, arrow navigator container, thumbnail navigator container etc).
                                        $PlayOrientation: 1,                                //[Optional] Orientation to play slide (for auto play, navigation), 1 horizental, 2 vertical, 5 horizental reverse, 6 vertical reverse, default value is 1
                                        $DragOrientation: 3,                                //[Optional] Orientation to drag slide, 0 no drag, 1 horizental, 2 vertical, 3 either, default value is 1 (Note that the $DragOrientation should be the same as $PlayOrientation when $DisplayPieces is greater than 1, or parking position is not 0)

                                        $SlideshowOptions: {                                //[Optional] Options to specify and enable slideshow or not
                                            $Class: $JssorSlideshowRunner$,                 //[Required] Class to create instance of slideshow
                                            $Transitions: _SlideshowTransitions,            //[Required] An array of slideshow transitions to play slideshow
                                            $TransitionsOrder: 1,                           //[Optional] The way to choose transition to play slide, 1 Sequence, 0 Random
                                            $ShowLink: true                                    //[Optional] Whether to bring slide link on top of the slider when slideshow is running, default value is false
                                        },

                                        $BulletNavigatorOptions: {                                //[Optional] Options to specify and enable navigator or not
                                            $Class: $JssorBulletNavigator$,                       //[Required] Class to create navigator instance
                                            $ChanceToShow: 2,                               //[Required] 0 Never, 1 Mouse Over, 2 Always
                                            $Lanes: 1,                                      //[Optional] Specify lanes to arrange items, default value is 1
                                            $SpacingX: 10,                                   //[Optional] Horizontal space between each item in pixel, default value is 0
                                            $SpacingY: 10                                    //[Optional] Vertical space between each item in pixel, default value is 0
                                        },

                                        $ArrowNavigatorOptions: {
                                            $Class: $JssorArrowNavigator$,              //[Requried] Class to create arrow navigator instance
                                            $ChanceToShow: 2,                                //[Required] 0 Never, 1 Mouse Over, 2 Always
                                            $AutoCenter: 2                                 //[Optional] Auto center navigator in parent container, 0 None, 1 Horizontal, 2 Vertical, 3 Both, default value is 0
                                        },

                                        $ThumbnailNavigatorOptions: {
                                            $Class: $JssorThumbnailNavigator$,              //[Required] Class to create thumbnail navigator instance
                                            $ChanceToShow: 2,                               //[Required] 0 Never, 1 Mouse Over, 2 Always
                                            $ActionMode: 0,                                 //[Optional] 0 None, 1 act by click, 2 act by mouse hover, 3 both, default value is 1
                                            $DisableDrag: true                              //[Optional] Disable drag or not, default value is false
                                        }
                                    };

                                    var jssor_sliderb = new $JssorSlider$(containerId, options);
                                    //responsive code begin
                                    //you can remove responsive code if you don't want the slider scales while window resizes
                                    function ScaleSlider() {
                                        var parentWidth = jssor_sliderb.$Elmt.parentNode.clientWidth;
                                        if (parentWidth)
                                            jssor_sliderb.$SetScaleWidth(Math.min(parentWidth, 400)); //turab it was 600
                                        else
                                            $JssorUtils$.$Delay(ScaleSlider, 30);
                                    }

                                    ScaleSlider();
                                    $JssorUtils$.$AddEvent(window, "load", ScaleSlider);


                                    if (!navigator.userAgent.match(/(iPhone|iPod|iPad|BlackBerry|IEMobile)/)) {
                                        $JssorUtils$.$OnWindowResize(window, ScaleSlider);
                                    }

                                    //if (navigator.userAgent.match(/(iPhone|iPod|iPad)/)) {
                                    //    $JssorUtils$.$AddEvent(window, "orientationchange", ScaleSlider);
                                    //}
                                    //responsive code end
                                };
                            </script>
                            <!-- Jssor Slider Begin -->
                            <!-- You can move inline styles to css file or css block. -->
                            <c:if test="${mfsAccountModel.customerAccountTypeId == 1 or mfsAccountModel.customerAccountTypeId == 2 or mfsAccountModel.customerAccountTypeId == 53}">
                            <div id="sliderb_container"
                                 style="position: relative; width: 600px; height: 300px; overflow: hidden; ">

                                <!-- Loading Screen -->
                                <div u="loading" style="position: absolute; top: 0px; left: 0px;">
                                    <div style="filter: alpha(opacity=70); opacity:0.7; position: absolute; display: block;
                background-color: #000; top: 0px; left: 0px;width: 100%;height:100%;">
                                    </div>
                                    <div style="position: absolute; display: block; background: url(${contextPath}/images/jssor/loading.gif) no-repeat center center;
                                            top: 0px; left: 0px;width: 100%;height:100%;">
                                    </div>
                                </div>

                                <!-- Slides Container -->
                                <div u="slides" style="cursor: move; position: absolute; left: 0px; top: 0px; width: 600px; height: 300px;
            overflow: hidden;">
                                    <div>
                                        <a class="group1"
                                           href="${contextPath}/images/upload_dir/customerPic_${mfsAccountModel.appUserId}.${mfsAccountModel.customerPicExt}?time=<%=System.currentTimeMillis()%>"
                                           title="Customer">
                                            <img u=image
                                                 src="${contextPath}/images/upload_dir/customerPic_${mfsAccountModel.appUserId}.${mfsAccountModel.customerPicExt}?time=<%=System.currentTimeMillis()%>"
                                                 height="300px" width="600px"/>
                                        </a>
                                        <div u="thumb">Customer</div>
                                    </div>
                                    <div>
                                        <a class="group1"
                                           href="${contextPath}/images/upload_dir/cnicBackPic_${mfsAccountModel.appUserId}.${mfsAccountModel.cnicBackPicExt}?time=<%=System.currentTimeMillis()%>"
                                           title="CNIC Back">
                                            <img u=image
                                                 src="${contextPath}/images/upload_dir/cnicBackPic_${mfsAccountModel.appUserId}.${mfsAccountModel.cnicBackPicExt}?time=<%=System.currentTimeMillis()%>"
                                                 height="300px" width="600px"/>
                                        </a>
                                        <div u="thumb">CNIC Back</div>
                                    </div>
                                    <div>
                                        <a class="group1"
                                           href="${contextPath}/images/upload_dir/cnicFrontPic_${mfsAccountModel.appUserId}.${mfsAccountModel.cnicFrontPicExt}?time=<%=System.currentTimeMillis()%>"
                                           title="CNIC Front">
                                            <img u=image
                                                 src="${contextPath}/images/upload_dir/cnicFrontPic_${mfsAccountModel.appUserId}.${mfsAccountModel.cnicFrontPicExt}?time=<%=System.currentTimeMillis()%>"
                                                 height="300px" width="600px"/>
                                        </a>
                                        <div u="thumb">CNIC Front</div>
                                    </div>
                                    <div>
                                        <a class="group1"
                                           href="${contextPath}/images/upload_dir/signPic_${mfsAccountModel.appUserId}.${mfsAccountModel.signPicExt}?time=<%=System.currentTimeMillis()%>"
                                           title="Signature">
                                            <img u=image
                                                 src="${contextPath}/images/upload_dir/signPic_${mfsAccountModel.appUserId}.${mfsAccountModel.signPicExt}?time=<%=System.currentTimeMillis()%>"
                                                 height="300px" width="600px"/>
                                        </a>
                                        <div u="thumb">Signature</div>
                                    </div>
                                    <div>
                                        <a class="group1"
                                           href="${contextPath}/images/upload_dir/tncPic_${mfsAccountModel.appUserId}.${mfsAccountModel.tncPicExt}?time=<%=System.currentTimeMillis()%>"
                                           title="TNC">
                                            <img u=image
                                                 src="${contextPath}/images/upload_dir/tncPic_${mfsAccountModel.appUserId}.${mfsAccountModel.tncPicExt}?time=<%=System.currentTimeMillis()%>"
                                                 height="300px" width="600px"/>
                                        </a>
                                        <div u="thumb">TNC</div>
                                    </div>

                                    <c:if test="${mfsAccountModel.customerAccountTypeId eq 2}">
                                        <div>
                                            <a class="group1"
                                               href="${contextPath}/images/upload_dir/level1FormPic_${mfsAccountModel.appUserId}.${mfsAccountModel.level1FormPicExt}?time=<%=System.currentTimeMillis()%>"
                                               title="Level 1 Form">
                                                <img u=image
                                                     src="${contextPath}/images/upload_dir/level1FormPic_${mfsAccountModel.appUserId}.${mfsAccountModel.level1FormPicExt}?time=<%=System.currentTimeMillis()%>"
                                                     height="300px" width="600px"/>
                                            </a>
                                            <div u="thumb">Level 1 Form</div>
                                        </div>
                                    </c:if>

                                </div>

                                <!-- ThumbnailNavigator Skin Begin -->
                                <div u="thumbnavigator" class="sliderb-T"
                                     style="position: absolute; bottom: 0px; left: 0px; height:45px; width:600px;">
                                    <div style="filter: alpha(opacity=40); opacity:0.4; position: absolute; display: block;
                background-color: #000000; top: 0px; left: 0px; width: 100%; height: 100%;">
                                    </div>
                                    <!-- Thumbnail Item Skin Begin -->
                                    <div u="slides">
                                        <div u="prototype"
                                             style="position: absolute; width: 600px; height: 45px; top: 0; left: 0;">
                                            <thumbnailtemplate
                                                    style="position: absolute; width: 100%; height: 100%; top: 0; left: 0; font-family: verdana; font-weight: normal; color:#fff; line-height: 45px; font-size:20px; padding-left:10px;"></thumbnailtemplate>
                                        </div>
                                    </div>
                                    <!-- Thumbnail Item Skin End -->
                                </div>
                                <!-- ThumbnailNavigator Skin End -->

                                <!-- Bullet Navigator Skin Begin -->
                                <!-- jssor slider bullet navigator skin 01 -->
                                <style>
                                    /*
                                    .jssorb01 div           (normal)
                                    .jssorb01 div:hover     (normal mouseover)
                                    .jssorb01 .av           (active)
                                    .jssorb01 .av:hover     (active mouseover)
                                    .jssorb01 .dn           (mousedown)
                                    */
                                    .jssorb01 div, .jssorb01 div:hover, .jssorb01 .av {
                                        filter: alpha(opacity=70);
                                        opacity: .7;
                                        overflow: hidden;
                                        cursor: pointer;
                                        border: #000 1px solid;
                                    }

                                    .jssorb01 div {
                                        background-color: gray;
                                    }

                                    .jssorb01 div:hover, .jssorb01 .av:hover {
                                        background-color: #d3d3d3;
                                    }

                                    .jssorb01 .av {
                                        background-color: #fff;
                                    }

                                    .jssorb01 .dn, .jssorb01 .dn:hover {
                                        background-color: #555555;
                                    }
                                </style>
                                <!-- bullet navigator container -->
                                <div u="navigator" class="jssorb01"
                                     style="position: absolute; bottom: 16px; right: 10px;">
                                    <!-- bullet navigator item prototype -->
                                    <div u="prototype" style="POSITION: absolute; WIDTH: 12px; HEIGHT: 12px;"></div>
                                </div>
                                <!-- Bullet Navigator Skin End -->

                                <!-- Arrow Navigator Skin Begin -->
                                <style>
                                    /* jssor slider arrow navigator skin 05 css */
                                    /*
                                    .jssora05l              (normal)
                                    .jssora05r              (normal)
                                    .jssora05l:hover        (normal mouseover)
                                    .jssora05r:hover        (normal mouseover)
                                    .jssora05ldn            (mousedown)
                                    .jssora05rdn            (mousedown)
                                    */
                                    .jssora05l, .jssora05r, .jssora05ldn, .jssora05rdn {
                                        position: absolute;
                                        cursor: pointer;
                                        display: block;
                                        background: url(${contextPath}/images/jssor/a17.png) no-repeat;
                                        overflow: hidden;
                                    }

                                    .jssora05l {
                                        background-position: -10px -40px;
                                    }

                                    .jssora05r {
                                        background-position: -70px -40px;
                                    }

                                    .jssora05l:hover {
                                        background-position: -130px -40px;
                                    }

                                    .jssora05r:hover {
                                        background-position: -190px -40px;
                                    }

                                    .jssora05ldn {
                                        background-position: -250px -40px;
                                    }

                                    .jssora05rdn {
                                        background-position: -310px -40px;
                                    }
                                </style>
                                <!-- Arrow Left -->
                                <span u="arrowleft" class="jssora05l"
                                      style="width: 40px; height: 40px; top: 123px; left: 8px;">
        </span>
                                <!-- Arrow Right -->
                                <span u="arrowright" class="jssora05r"
                                      style="width: 40px; height: 40px; top: 123px; right: 8px">
        </span>
                                <!-- Arrow Navigator Skin End -->
                                <a style="display: none" href="http://www.jssor.com">jquery responsive slider</a>
                                <!-- Trigger -->
                                <script>
                                    jssor_sliderb_starter('sliderb_container');
                                </script>
                            </div>

                            <!-- Jssor Slider End -->

                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" align="center"><input onclick="print_imgs();" class="button" type="button"
                                                              title="Print all the images which are displayed in slide show"
                                                              value="Print All"></td>
                    </tr>
                    </c:if>

                    <tr>
                        <td>
                            <table width="100%" border="0" cellpadding="0" cellspacing="1">
                                <tr>
                                    <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Name:
                                    </td>
                                    <td width="60%" align="left" bgcolor="FBFBFB">${mfsAccountModel.name}</td>
                                </tr>

                                <tr>
                                    <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">
                                        Father/Husband Name:
                                    </td>
                                    <td width="60%" align="left"
                                        bgcolor="FBFBFB">${mfsAccountModel.fatherHusbandName}</td>
                                </tr>
                                <tr>
                                    <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Mother's
                                        Maiden Name:
                                    </td>
                                    <td width="60%" align="left"
                                        bgcolor="FBFBFB">${mfsAccountModel.motherMaidenName}</td>
                                </tr>
                                <tr>
                                    <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Date of
                                        Birth:
                                    </td>
                                    <td width="60%" align="left" bgcolor="FBFBFB"><fmt:formatDate dateStyle="medium"
                                                                                                  value="${mfsAccountModel.dob}"/></td>
                                </tr>
                                <tr>
                                    <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Place of
                                        Birth:
                                    </td>
                                    <td width="60%" align="left" bgcolor="FBFBFB">${mfsAccountModel.birthPlace}</td>
                                </tr>
                                <tr>
                                    <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">
                                        Gender:
                                    </td>
                                    <td width="60%" align="left" bgcolor="FBFBFB">${mfsAccountModel.gender}</td>
                                </tr>
                                <c:if test="${mfsAccountModel.customerAccountTypeId == 2}">
                                    <tr>
                                        <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">
                                            Email:
                                        </td>
                                        <td width="60%" align="left" bgcolor="FBFBFB">${mfsAccountModel.email}</td>
                                    </tr>
                                </c:if>
                                <tr>
                                    <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Mobile
                                        #:
                                    </td>
                                    <td width="60%" align="left" bgcolor="FBFBFB">${mfsAccountModel.mobileNo}</td>
                                </tr>
                                <tr>
                                    <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Mailing
                                        Address:
                                    </td>
                                    <td width="60%" align="left" bgcolor="FBFBFB">${mfsAccountModel.presentAddress}</td>
                                </tr>
                                <tr>
                                    <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">City:
                                    </td>
                                    <td width="60%" align="left" bgcolor="FBFBFB">${mfsAccountModel.city}</td>
                                </tr>
                                <c:if test="${mfsAccountModel.customerAccountTypeId == 2}">
                                    <tr>
                                        <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">
                                            Source of Funds:
                                        </td>
                                        <td width="60%" align="left" bgcolor="FBFBFB">
                                            <c:forEach var="fundSourceName" items="${mfsAccountModel.fundSourceName}">
                                                ${fundSourceName}<br>
                                            </c:forEach>
                                        </td>
                                    </tr>
                                </c:if>
                                <tr>
                                    <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Tax
                                        Regime:
                                    </td>
                                    <td width="60%" align="left" bgcolor="FBFBFB">
                                        ${mfsAccountModel.taxRegimeName}
                                    </td>
                                </tr>
                                <tr>
                                    <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">FED:
                                    </td>
                                    <td width="60%" align="left" bgcolor="FBFBFB">
                                        ${mfsAccountModel.fed}
                                    </td>
                                </tr>
                                <tr>
                                    <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Initial
                                        Deposit:
                                    </td>
                                    <td width="60%" align="left" bgcolor="FBFBFB">${mfsAccountModel.initialDeposit}</td>
                                </tr>
                                <tr>
                                    <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Terms
                                        And Condition:
                                    </td>
                                    <td width="60%" align="left" bgcolor="FBFBFB">Yes</td>
                                </tr>
                                <tr>
                                    <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">
                                        Comments:
                                    </td>
                                    <td width="60%" align="left" bgcolor="FBFBFB">${mfsAccountModel.comments}</td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </fieldset>
        </td>
        <td width="50%" valign="top">
            <fieldset class="${fn:toLowerCase(mfsAccountModel.accountState)}">
                <legend>${mfsAccountModel.accounttypeName} A/C</legend>
                <table width="100%" border="0" cellpadding="0" cellspacing="1">
                    <tr>
                        <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Account State:</td>
                        <td width="60%" align="left" bgcolor="FBFBFB"><span
                                class="state">${mfsAccountModel.accountState}</span></td>
                    </tr>
                    <tr>
                        <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Remaining Limits:
                        </td>
                        <td width="60%" align="left" bgcolor="FBFBFB">
                            <table width="95%" border="0" cellpadding="0" cellspacing="1"
                                   style="margin:0px auto; border:1px solid #999;">
                                <tr align="center">
                                    <td width="33%" height="23"><b>Limits</b></td>
                                    <td width="34%" bgcolor="#8ec6e5"><b>Debit</b></td>
                                    <td width="33%" bgcolor="#8ec6e5"><b>Credit</b></td>
                                </tr>
                                <tr align="center">
                                    <td width="33%" height="23" bgcolor="#8ec6e5"><b>Daily</b></td>
                                    <td width="34%" bgcolor="#f8f8f8">${mfsAccountModel.remainingDailyDebitLimit}</td>
                                    <td width="33%" bgcolor="#f8f8f8">${mfsAccountModel.remainingDailyCreditLimit}</td>
                                </tr>
                                <tr align="center">
                                    <td width="33%" height="23" bgcolor="#8ec6e5"><b>Monthly</b></td>
                                    <td width="34%" bgcolor="#f8f8f8">${mfsAccountModel.remainingMonthlyDebitLimit}</td>
                                    <td width="33%"
                                        bgcolor="#f8f8f8">${mfsAccountModel.remainingMonthlyCreditLimit}</td>
                                </tr>
                                <tr align="center">
                                    <td width="33%" height="23" bgcolor="#8ec6e5"><b>Yearly</b></td>
                                    <td width="34%" bgcolor="#f8f8f8">${mfsAccountModel.remainingYearlyDebitLimit}</td>
                                    <td width="33%" bgcolor="#f8f8f8">${mfsAccountModel.remainingYearlyCreditLimit}</td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </fieldset>
            <c:if test="${isHraAccount == true}">
                <fieldset class="${fn:toLowerCase(mfsAccountModel.hraAccountState)}">
                    <legend>Home Remittance A/C</legend>
                    <table width="100%" border="0" cellpadding="0" cellspacing="1">
                        <tr>
                            <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Account State:
                            </td>
                            <td width="60%" align="left" bgcolor="FBFBFB"><span
                                    class="state">${mfsAccountModel.hraAccountState}</span></td>
                        </tr>
                        <tr>
                            <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Remaining
                                Limits:
                            </td>
                            <td width="60%" align="left" bgcolor="FBFBFB">
                                <table width="95%" border="0" cellpadding="0" cellspacing="1"
                                       style="margin:0px auto; border:1px solid #999;">
                                    <tr align="center">
                                        <td width="33%" height="23"><b>Limits</b></td>
                                        <td width="34%" bgcolor="#8ec6e5"><b>Debit</b></td>
                                        <td width="33%" bgcolor="#8ec6e5"><b>Credit</b></td>
                                    </tr>
                                    <tr align="center">
                                        <td width="33%" height="23" bgcolor="#8ec6e5"><b>Daily</b></td>
                                        <td width="34%"
                                            bgcolor="#f8f8f8">${mfsAccountModel.remainingHRADailyDebitLimit}</td>
                                        <td width="33%"
                                            bgcolor="#f8f8f8">${mfsAccountModel.remainingHRADailyCreditLimit}</td>
                                    </tr>
                                    <tr align="center">
                                        <td width="33%" height="23" bgcolor="#8ec6e5"><b>Monthly</b></td>
                                        <td width="34%"
                                            bgcolor="#f8f8f8">${mfsAccountModel.remainingHRAMonthlyDebitLimit}</td>
                                        <td width="33%"
                                            bgcolor="#f8f8f8">${mfsAccountModel.remainingHRAMonthlyCreditLimit}</td>
                                    </tr>
                                    <tr align="center">
                                        <td width="33%" height="23" bgcolor="#8ec6e5"><b>Yearly</b></td>
                                        <td width="34%"
                                            bgcolor="#f8f8f8">${mfsAccountModel.remainingHRAYearlyDebitLimit}</td>
                                        <td width="33%"
                                            bgcolor="#f8f8f8">${mfsAccountModel.remainingHRAYearlyCreditLimit}</td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </fieldset>
            </c:if>
            <c:if test="${isHraAccount == true}">
                <fieldset class="${fn:toLowerCase(mfsAccountModel.hraAccountState)}">
                    <legend>Remittance Details</legend>
                    <table width="100%" border="0" cellpadding="0" cellspacing="1">
                        <tr>
                            <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Next of KIN
                                Mobile No.
                            </td>
                            <td width="60%" align="left" bgcolor="FBFBFB">${mfsAccountModel.hraNokMobile}</td>
                        </tr>
                        <tr>
                            <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Transaction
                                Purpose
                            </td>
                            <td width="60%" align="left" bgcolor="FBFBFB">${mfsAccountModel.hraTransactionPurpose}</td>
                        </tr>
                        <tr>
                            <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Occupation</td>
                            <td width="60%" align="left" bgcolor="FBFBFB">${mfsAccountModel.hraOccupation}</td>
                        </tr>
                        <tr>
                            <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Remitters</td>
                            <td width="60%" align="left" bgcolor="FBFBFB">
                                <table width="95%" border="0" cellpadding="0" cellspacing="1"
                                       style="margin:0px auto; border:1px solid #999;">
                                    <tr align="center">
                                        <td width="34%" height="23" bgcolor="#8ec6e5"><b>Location</b></td>
                                        <td width="33%" height="23" bgcolor="#8ec6e5"><b>Relation</b></td>
                                    </tr>
                                    <c:forEach items="${customerRemitterModelList}" var="customerRemitterModel"
                                               varStatus="iterationStatus">
                                        <tr align="center">
                                            <td width="34%" bgcolor="#f8f8f8"
                                                align="center">${customerRemitterModel.remittanceLocation}</td>
                                            <td width="33%" bgcolor="#f8f8f8"
                                                align="center">${customerRemitterModel.relationship}</td>
                                        </tr>
                                    </c:forEach>
                                </table>
                            </td>
                        </tr>
                    </table>
                        <%--</div>--%>
                </fieldset>
            </c:if>
            <c:if test="${isBlinkAccount == true}">
                <fieldset class="${fn:toLowerCase(mfsAccountModel.accountState)}">
                    <legend>BLink Details</legend>
                    <table width="100%" border="0" cellpadding="0" cellspacing="1">
                        <tr>
                            <td width="40%" height="32" align="right" bgcolor="F3F3F3" class="formText">Next of KIN
                                Name.
                            </td>
                            <td width="60%" align="left" bgcolor="FBFBFB">${mfsAccountModel.nokName}</td>
                        </tr>
                    </table>
                        <%--</div>--%>
                </fieldset>
            </c:if>

        </td>
    </tr>
</table>
<!---******************************END LEVEL 0 FORM ********************************************************-->

</body>
</html>