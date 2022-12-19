<%--
  Created by IntelliJ IDEA.
  User: usret
  Date: 1/6/2022
  Time: 3:26 PM
  To change this template use File | Settings | File Templates.
--%>
<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.*'%>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>


<c:choose>
    <c:when test="${!empty status}">
        <c:set var="_status" value="${status}"/>
    </c:when>
    <c:otherwise>
        <c:set var="_status" value="${param.status}"/>
    </c:otherwise>
</c:choose>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <style>
        .fake-link {
            color: blue;
            text-decoration: underline;
            cursor: pointer;
            float: right;
            padding: 0px 1%;
        }
    </style>
    <meta http-equiv="Content-Type"
          content="text/html; charset=iso-8859-1" />
    <meta http-equiv="cache-control" content="no-cache" />
    <meta http-equiv="expires" content="0" />
    <meta http-equiv="pragma" content="no-cache" />


    <script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
    <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
    <link rel="stylesheet" href="${contextPath}/styles/jquery-ui-custom.min.css" type="text/css">
    <script type="text/javascript" src="${contextPath}/scripts/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/jquery-ui-custom.min.js"></script>



    <%-- <script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script> --%>
    <%-- 	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script> --%>
    <script src="${pageContext.request.contextPath}/scripts/jquery-1.7.2.min.js"></script>

    <link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css" type="text/css"/>
    <meta name="title" content="Create Customer Authorization Detail"/>

    <script type="text/javascript">
        var jq=$.noConflict();

        jq(document).ready(function()
        {

            jq('input[type="checkbox"]').on('change', function() {

                // Change event fired..
                var count = 0;
                var chkboxes = document.getElementsByName('checkedList');
                var status = document.getElementById("actionStatus").value
                for( var i=0; i < chkboxes.length; i++ ) {
                    if(chkboxes[i].checked == true) {
                        count++;
                    }

                }

                if(status == 4 || (status == 3 && count == 0))
                    jq('input[type="submit"]').prop('disabled', true);
                else
                    jq('input[type="submit"]').prop('disabled', false);

            });

        });

        function refreshParent() {
            window.opener.document.forms[0].submit();
            window.close();
        }

        function disableSubmit(){
            if(!${param.resolveRequest}){
                document.getElementById("_save").disabled = true;
            }
        }
        function onChangeStatus(){
            // var status = document.getElementById("actionStatus").value;
            // var chkboxes = document.getElementsByName('checkedList');
            // var cnt = 0;
            // for( var i=0; i < chkboxes.length; i++ ) {
            //     if(chkboxes[i].checked == true) {
            //         cnt++;
            //     }
            // }
            // if(status == 4 || (status == 3 && cnt == 0))
            //     document.getElementById("_save").disabled = true;
            // else
            //     document.getElementById("_save").disabled = false;
            var status = document.getElementById("actionStatus").value;
            if(status!=4)
                document.getElementById("_save").disabled = false;
            else
                document.getElementById("_save").disabled = true;

        }

        function onFormSubmit() {
            document.getElementById("_save").disabled = true;
            validate();
        }

        function selectAll() {
            var chkboxes = document.getElementsByName('checkedList');
            var status = document.getElementById("actionStatus").value
            for( var i=0; i < chkboxes.length; i++ ) {
                if(chkboxes[i].checked == false) {
                    chkboxes[i].checked = "checked";

                }

            }
            if(status !=4){
                document.getElementById("_save").disabled = false;
            }
        }

        function selectNone() {
            var chkboxes = document.getElementsByName('checkedList');
            for( var i=0; i < chkboxes.length; i++ ) {
                if(chkboxes[i].checked == true) {
                    chkboxes[i].checked = "";
                }
            }
            var status = document.getElementById("actionStatus").value;
            if(status == 3){
                document.getElementById("_save").disabled = true;
            }
        }

    </script>
    <style>
        .header {
            background-color: #6699CC;/*308dbb original*/
            color: white;
            font-family:verdana, arial, helvetica, sans-serif;/*verdana, arial, helvetica, sans-serif*/
            font-size: 11px;
            font-weight: bold;
            text-align: center;
            padding-right: 3px;
            padding-left: 3px;
            padding-top: 4px;
            padding-bottom: 4px;
            margin: 0px;
            border-right-style: solid;
            border-right-width: 1px;
            border-color: white;
        }
    </style>
    <%
        String updatePermission = PortalConstants.ADMIN_GP_UPDATE;
        updatePermission += "," + PortalConstants.MNG_USECASE_UPDATE;
        updatePermission += "," + PortalConstants.PG_GP_UPDATE;
        updatePermission += "," + PortalConstants.ACTION_AUTHORIZATION_UPDATE;
        String createPermission = PortalConstants.BULK_SUM_VIEW_POPUP_CREATE;
    %>

</head>
<body bgcolor="#ffffff" onload="javascript:disableSubmit();">
<h3 class="header" id="logHeader">Action Authorization Detail </h3>
<c:choose>
    <c:when test="${not empty _status}">
        <c:choose>
            <c:when test="${_status eq 'success'}">

                <div class="">
                    <table class="tableRegion">
                        <tr class="odd">
                            <td class="formText">

                                <c:if test="${actionAuthorizationModel.actionStatusId == 3}">
                                    Action is authorized successfully. Changes are saved.<p>Click the button below to close the window</p>
                                </c:if>
                                <c:if test="${actionAuthorizationModel.actionStatusId != 3 }">
                                    Record Updated successfully.<p>Click the button below to close the window</p>
                                </c:if>
                            </td>
                        </tr>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <div class="">
                    <table class="tableRegion">
                        <tr class="odd">
                            <td class="formText"><c:out value="${message}" /></td>
                        </tr>
                    </table>
                </div>
            </c:otherwise>

        </c:choose>
        <div class="eXtremeTable" style="text-align: center; padding-top: 10px;">
            <input type ="button" class="button" value = "Close Window" onclick = "refreshParent()"/>
        </div>
    </c:when>

    <c:otherwise>
        <c:if test="${(actionAuthorizationModel.actionStatusId == 3) || (actionAuthorizationModel.actionStatusId == 5) || (actionAuthorizationModel.actionStatusId == 6) || (actionAuthorizationModel.actionStatusId == 7)|| (actionAuthorizationModel.actionStatusId == 8)|| (actionAuthorizationModel.actionStatusId == 9)}">
            <c:set var="approvedOrDenied" value="true" scope="page" />
        </c:if>
        <table class="tableRegion" width="100%">
        <tr class="titleRow">
        <td>
        <html:form  name = "actionDetailForm" id = "actionDetailForm" commandName="actionAuthorizationModel" action="p-bulkdisbursementauthorizationdetail.html" method="post"  onsubmit="return onFormSubmit()">
            <table width="100%" border="0" cellpadding="0" cellspacing="1">

            <tr bgcolor="FBFBFB">
            <td colspan="2" align="center">&nbsp;</td>
            </tr>
            <tr>
            <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Action ID:&nbsp;&nbsp;</td>
            <td width="51%" align="left" bgcolor="FBFBFB">
            <html:input path="actionAuthorizationId" id="actionAuthorizationId" cssClass="textBox" maxlength="50" tabindex="1" readonly="true" />
            </td>
            </tr>
            <tr>
            <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Action Type:&nbsp;&nbsp;</td>
            <td width="51%" align="left" bgcolor="FBFBFB">
            <html:input path="usecaseName" id="usecaseName" cssClass="textBox" maxlength="50" tabindex="1" readonly="true"/>
            </td>
            </tr>

            <tr>
            <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Created By:&nbsp;&nbsp;</td>
            <td width="51%" align="left" bgcolor="FBFBFB">
            <html:input path="createdByUsername" id="createdByUsername" cssClass="textBox" maxlength="50" tabindex="1" readonly="true"/>
            </td>
            </tr>
            <tr>
            <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Created On:&nbsp;&nbsp;</td>
            <td width="51%" align="left" bgcolor="FBFBFB">
            <html:input path="createdOn" id="createdOn" cssClass="textBox" maxlength="50" tabindex="1" readonly="true"/>
            </td>
            </tr>

            <c:if test="${not approvedOrDenied }">
                <tr>
                    <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Escalation Level:&nbsp;&nbsp;</td>
                    <td width="51%" align="left" bgcolor="FBFBFB">
                        <html:input path="escalationLevel" id="escalationLevel" cssClass="textBox" maxlength="50" tabindex="1" readonly="true"/>
                    </td>
                </tr>
                </tr>

                <tr>
                    <td align="right" bgcolor="F3F3F3" class="formText">Initiator Comments:&nbsp;&nbsp; </td>
                    <td align="left" bgcolor="FBFBFB">
                        <html:textarea path="comments"   id="comments"  onkeypress="return maskCommon(this,event)" onkeyup="textAreaLengthCounter(this,250);" cssClass="textBox" rows="6" readonly="true" cssStyle="overflow: auto; width: 163px; height: 102px;"/>
                    </td>
                </tr>
                <tr>
                    <td align="right" bgcolor="F3F3F3" class="formText">Authorization Status:&nbsp;&nbsp; </td>
                    <td align="left" bgcolor="FBFBFB">
                        <html:select path="actionStatusId" id="actionStatus" onchange="onChangeStatus()">
                            <html:options items="${actionStatusModel}" itemLabel="name" itemValue="actionStatusId" />
                        </html:select>
                    </td>
                </tr>

                <tr>
                    <td align="right" bgcolor="F3F3F3" class="formText">Authorizer Comments:&nbsp;&nbsp; </td>
                    <td align="left" bgcolor="FBFBFB">
                        <html:textarea path="checkerComments"   id="checkerComments" onkeyup="textAreaLengthCounter(this,250);"  onkeypress="return maskCommon(this,event)" cssClass="textBox" rows="6"  readonly="${approvedOrDenied }"  cssStyle="overflow: auto; width: 163px; height: 102px;"  />
                    </td>
                </tr>

                <tr bgcolor="FBFBFB">
                    <td colspan="2" align="center">&nbsp;</td>
                </tr>
                <tr>

                    <input type="hidden" name="escalateRequest" value="${param.escalateRequest}">
                    <input type="hidden" name="resolveRequest"  value="${param.resolveRequest}">
                    <td align="center" colspan="2">
                        <authz:authorize ifAnyGranted="<%=updatePermission%>">
                            <input name="_save" id="_save" type="submit" class="button"  value=" Update " /> &nbsp;
                        </authz:authorize>
                        <authz:authorize ifNotGranted="<%=updatePermission%>">
                            <input name="_save" id="_save" type="submit" class="button"  value=" Update " disabled="disabled" /> &nbsp;
                        </authz:authorize>
                        <input name="cancel" type="button" class="button" value=" Cancel " onclick="javascript:window.close();"/>
                    </td>
                </tr>
            </c:if>
            <c:if test="${isAssignedBack}">
                <tr bgcolor="FBFBFB">
                    <td colspan="2" align="center">&nbsp;</td>
                </tr>
                <tr>
                    <td align="center" colspan="2">
                        <input name="modify" type="button" class="button" value=" Modify " onclick="javascript:doModify('p_transactionredemptionform.html?isReSubmit=true&transactionCode=${transactionCode}&authId=${actionAuthorizationModel.actionAuthorizationId}&actionId=1','_blank');window.close();"/>
                    </td>
                </tr>
            </c:if>
            <tr bgcolor="FBFBFB">
            <td colspan="2" align="center">&nbsp;</td>
            </tr>
            </table>

            <table  class="tableRegion" width="100%">

            </table>

            </html:form>
            <div class="eXtremeTable"><h3 class="header" id="PostingInMicroBank">Bulk Disbursements</h3></div>

            <table border="0" cellpadding="5px;" style="font-weight: bold;">
                <tr>
                    <c:forEach items="${disbursementFileInfoViewModel}" var="infoViewModel"  varStatus="status">
                    <td colspan="4" bgcolor="FBFBFB" style="vertical-align: middle">
                            <h3><span style="font-weight: bold">${infoViewModel.serviceName} - ${infoViewModel.productName}
                  (${infoViewModel.batchNumber})</span></h3>
<%--                  </span></h3>--%>
                    </td>
                </tr>
                <tr>
                    <td align="right" class="formText" width="25%">File Name:</td>
                    <td align="left" bgcolor="FBFBFB" class="text" width="25%">${infoViewModel.filename}</td>
                    <td align="right" class="formText" width="25%">File Path:</td>
                    <td align="left" bgcolor="FBFBFB" width="25%">${infoViewModel.filePath}</td>
                </tr>

                <tr>
                    <td align="right" class="formText">Uploaded On:</td>
                    <td align="left" bgcolor="FBFBFB"><fmt:formatDate pattern="dd/MM/yyyy hh:mm:ss" value="${infoViewModel.createdOn}"/></td>
                    <td align="right" class="formText">File Status:</td>
                    <td align="left" bgcolor="FBFBFB">${infoViewModel.statusStr}</td>
                </tr>
                <tr>
                    <td colspan="4"><span style="height: 30px;"/> </td>
                </tr>
                <tr>
                    <td align="right" class="formText">Total Records:</td>
                    <td align="left" bgcolor="FBFBFB"><fmt:formatNumber pattern="#,###.##" value="${infoViewModel.totalRecords}"/></td>
                </tr>
                <tr>
                    <td align="right" class="formText">Valid Records:</td>
                    <td align="left" bgcolor="FBFBFB"><fmt:formatNumber pattern="#,###.##" value="${infoViewModel.validRecords}"/></td>
<%--                    <td align="right" class="formText">Invalid Records:</td>--%>
<%--                    <td align="left" bgcolor="FBFBFB"><fmt:formatNumber pattern="#,###.##" value="${infoViewModel.invalidRecords}"/></td>--%>
                </tr>
                <tr>
                    <td align="right" class="formText">Disbursement To:</td>
                    <td align="left" bgcolor="FBFBFB">${infoViewModel.appUserTypeName}</td>
                    <td align="right" class="formText">Disbursement Amount:</td>
                    <td align="left" bgcolor="FBFBFB"> <fmt:formatNumber pattern="#,###.##" value="${infoViewModel.totalAmount}"/></td>
                </tr>
                <tr>
                    <td align="right" class="formText">Total Charges:</td>
                    <td align="left" bgcolor="FBFBFB"><fmt:formatNumber pattern="#,###.##" value="${infoViewModel.totalCharges}"/></td>
                    <td align="right"class="formText">Total FED:</td>
                    <td align="left" bgcolor="FBFBFB"><fmt:formatNumber pattern="#,###.##" value="${infoViewModel.totalFed}"/></td>
                </tr>
                <tr>
                    <td align="right" class="formText">Total Amount:</td>
                    <td align="left" bgcolor="FBFBFB">
                        <fmt:formatNumber pattern="#,###.##" value="${infoViewModel.totalAmount + infoViewModel.totalCharges}"/> </td>
                </tr>
                <tr>
                    <td colspan="4"><span style="height: 30px;"/> </td>
                </tr>

                <c:if test="${infoViewModel.status != 10}">
                    <tr style="padding-top: 20px;">
                        <td colspan="4" bgcolor="FBFBFB">
                            <h3><span style="font-weight: bold">File Settlement Status</span> </h3>
                        </td>
                    </tr>
                    <c:if test="${infoViewModel.serviceId == 13}">
                        <tr>
                            <td align="right" class="formText">Total A/c Created :</td>
                            <td align="left" bgcolor="FBFBFB" class="formText"><fmt:formatNumber pattern="#,###.##" value="${totalCreated}"/></td>
                        </tr>
                    </c:if>
                    <tr>
                        <td align="right" class="formText">Total Settled :</td>
                        <td align="left" bgcolor="FBFBFB" class="formText"><fmt:formatNumber pattern="#,###.##" value="${totalSettled}"/></td>
                    </tr>
                    <tr>
                        <td align="right" class="formText">First Settled On:</td>
                        <td align="left" bgcolor="FBFBFB" class="formText"><fmt:formatDate pattern="dd/MM/yyyy hh:mm:ss" value="${firstSettledOn}"/></td>
                        <td align="right" class="formText">Last Settled On :</td>
                        <td align="left" bgcolor="FBFBFB" class="formText"><fmt:formatDate pattern="dd/MM/yyyy hh:mm:ss" value="${lastSettledOn}"/></td>
                    </tr>
                </c:if>
                    </table>
                        </c:forEach>

                    </td>
                </tr>
            </table>

            <tr>
                <td class="formText">
                    <a href="${contextPath}/p_downloadBulkDisbursementValidRecordsfile.html" tabindex="1">Export Valid Records</a>
                    <a href="${contextPath}/p_customeracnaturemarkingbulkupload.html">Cancel</a>
                </td>
            </tr>

            <h3 class="header" id="historyHeader">Action Authorization History </h3>
            <div class="eXtremeTable">
                <table class="tableRegion" width="100%">
                    <tr class="odd">
                        <td class="titleRow" style="width: 90px; font-weight: bold" >Escalation Level</td>
                        <td class="titleRow" style="width: 90px; font-weight: bold">Authorization Status</td>
                        <td class="titleRow" style="width: 90px; font-weight: bold">Checked By</td>
                        <td class="titleRow" style="width: 90px; font-weight: bold">Checked On</td>
                        <td class="titleRow" style="width: 90px; font-weight: bold">Authorizer Comments</td>
                        <td class="titleRow" style="width: 90px; font-weight: bold">Intimated On</td>
                        <td class="titleRow" style="width: 90px; font-weight: bold">Intimated To</td>
                    </tr>
                    <c:forEach items="${actionAuthorizationHistoryModelList}" var="actionHistory"  varStatus="status">
                        <tr class="odd">
                            <td>${actionHistory.escalationLevel}</td>
                            <td>${actionHistory.relationActionStatusIdActionStatusModel.name }</td>
                            <td>${actionHistory.relationCheckedByAppUserModel.username }</td>
                            <td><fmt:formatDate  type="both"
                                                 dateStyle="short" timeStyle="short" value="${actionHistory.checkedOn}" /></td>
                            <td>${actionHistory.checkerComments}</td>
                            <td><fmt:formatDate  type="both"
                                                 dateStyle="short" timeStyle="short" value="${actionHistory.intimatedOn}" /></td>
                            <td>${actionHistory.intimatedTo}</td>
                        </tr>
                    </c:forEach>
                </table>
            </div>

            </c:otherwise>
        </c:choose>

</body>
</html>
