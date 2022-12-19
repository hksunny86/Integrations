<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.*'%>

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
    <meta http-equiv="cache-control" content="no-cache" />
    <meta http-equiv="cache-control" content="no-store" />
    <meta http-equiv="cache-control" content="private" />
    <meta http-equiv="cache-control" content="max-age=0, must-revalidate" />
    <meta http-equiv="expires" content="now-1" />
    <meta http-equiv="pragma" content="no-cache" />

    <script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
    <script src="${pageContext.request.contextPath}/scripts/jquery-1.7.2.min.js"></script>

    <link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css" type="text/css"/>
    <meta name="title" content="Update Customer Name Authorization Detail"/>

    <script type="text/javascript">

        function onFormSubmit() {
            document.getElementById("_save").disabled = true;
            validate();
        }

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

            var status = document.getElementById("actionStatus").value;
            if(status!=4)
                document.getElementById("_save").disabled = false;
            else
                document.getElementById("_save").disabled = true;
        }
        var jq = $.noConflict();
        var doAjaxBeforeUnload = function (evt) {

            doAjaxBeforeUnloadEnabled = false;
            jq.ajax({
                url: "${contextPath}/p_mnomfsaccountdetailsajx.html?actionAuthorizationId=${param.actionAuthorizationId}",
                success: function (a) {
                    console.debug("Ajax call finished");
                },
                async: false /* Not recommended.  This is the dangerous part.  Your mileage may vary. */
            });
        }

        window.onbeforeunload = doAjaxBeforeUnload;
        $(window).unload(doAjaxBeforeUnload);


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
                    <html:form  name = "actionDetailForm" id = "actionDetailForm" commandName="actionAuthorizationModel" action="p_updateCustomerNameAauthorization.html" method="post" onsubmit="return onFormSubmit()">
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

                            <c:if test="${not approvedOrDenied }">
                                <tr>
                                    <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Escalation Level:&nbsp;&nbsp;</td>
                                    <td width="51%" align="left" bgcolor="FBFBFB">
                                        <html:input path="escalationLevel" id="escalationLevel" cssClass="textBox" maxlength="50" tabindex="1" readonly="true"/>
                                    </td>
                                </tr>
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
                                        <input name="modify" type="button" class="button" value=" Modify " onclick="javascript:doModify('p_updateCustomerNameDetail.html?cnic='+${cnic}+'&isReSubmit=true&authId='+${actionAuthorizationModel.actionAuthorizationId}+'&actionId=1');window.close();"/>
                                    </td>
                                </tr>
                            </c:if>
                            <tr bgcolor="FBFBFB">
                                <td colspan="2" align="center">&nbsp;</td>
                            </tr>
                        </table>
                    </html:form>
                </td>
            </tr>
        </table>

        <table  class="tableRegion" width="100%">
            <tr>
                <c:if test="${actionAuthorizationModel.actionStatusId == 3}">
                    <td colspan="4"><h3 class="header" id="detailHeader">Previous State</h3></td>
                </c:if>
                <c:if test="${actionAuthorizationModel.actionStatusId != 3}">
                    <td colspan="4"><h3 class="header" id="detailHeader">Modified State</h3></td>
                </c:if>
                <td colspan="4"><h3 class="header" id="detailHeader">Present State</h3> </td>
            </tr>
            <tr>
                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">First Name </td>
                <td width="13%" height="16" align="left"  class="formText">${updateCustomerNameVo.firstName }</td>
                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Last Name </td>
                <td width="13%" height="16" align="left"  class="formText">${updateCustomerNameVo.lastName }</td>

                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">First Name </td>
                <td width="13%" height="16" align="left"  class="formText">${appUserModel.firstName }</td>
                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Last Name </td>
                <td width="13%" height="16" align="left"  class="formText">${appUserModel.lastName }</td>


            </tr>



            <tr>
                <td colspan="9" align="center" bgcolor="FBFBFB">
                    <h3>
                            ${actionAuthorizationModel.usecaseName} Form
                    </h3>
                </td>
            </tr>
<%--            <tr>--%>
<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Mobile No.</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText">${mfsAccountModel.mobileNo }</td>--%>

<%--                <td width="12%" height="16" align="left"  class="formText"></td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText"></td>--%>

<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Mobile No.</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText">${currentMfsAccountModel.mobileNo }</td>--%>
<%--            </tr>--%>
<%--            <tr>--%>
<%--                <td colspan="9" align="center" bgcolor="FBFBFB">--%>
<%--                    <h5>Applicant Details</h5>--%>
<%--                </td>--%>
<%--            </tr>--%>
<%--            <tr>--%>
<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Account Title</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText">${mfsAccountModel.name }</td>--%>

<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Customer Picture</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText">--%>
<%--                    <img src="${contextPath}/images/upload_dir/authorization/customerPic_${actionAuthorizationModel.actionAuthorizationId}.gif" width="100" height="100" />--%>
<%--                </td>--%>

<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Account Title</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText">${currentMfsAccountModel.name }</td>--%>

<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Customer Picture</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText">--%>
<%--                    <img src="${contextPath}/images/upload_dir/customerPic_${currentMfsAccountModel.appUserId}.gif" width="100" height="100" />--%>
<%--                </td>--%>
<%--            </tr>--%>
<%--            <tr>--%>
<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">CNIC Front</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText">--%>
<%--                    <img src="${contextPath}/images/upload_dir/authorization/cnicFrontPic_${actionAuthorizationModel.actionAuthorizationId}.gif" width="100" height="100" />--%>
<%--                </td>--%>

<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">CNIC Back</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText">--%>
<%--                    <img src="${contextPath}/images/upload_dir/authorization/cnicBackPic_${actionAuthorizationModel.actionAuthorizationId}.gif" width="100" height="100" />--%>
<%--                </td>--%>

<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">CNIC Front</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText">--%>
<%--                    <img src="${contextPath}/images/upload_dir/cnicFrontPic_${currentMfsAccountModel.appUserId}.gif" width="100" height="100" />--%>
<%--                </td>--%>

<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">CNIC Back</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText">--%>
<%--                    <img src="${contextPath}/images/upload_dir/cnicBackPic_${currentMfsAccountModel.appUserId}.gif" width="100" height="100" />--%>
<%--                </td>--%>
<%--            </tr>--%>

<%--            <tr>--%>
<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Source Of Income</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText">--%>
<%--                    <img src="${contextPath}/images/upload_dir/authorization/sourceOfIncomePic_${actionAuthorizationModel.actionAuthorizationId}.gif" width="100" height="100" />--%>
<%--                </td>--%>

<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Proof Of Profession</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText">--%>
<%--                    <img src="${contextPath}/images/upload_dir/authorization/proofOfProfessionPic_${actionAuthorizationModel.actionAuthorizationId}.gif" width="100" height="100" />--%>
<%--                </td>--%>

<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Source Of Income</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText">--%>
<%--                    <img src="${contextPath}/images/upload_dir/sourceOfIncomePic_${currentMfsAccountModel.appUserId}.gif" width="100" height="100" />--%>
<%--                </td>--%>

<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Proof Of Profession</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText">--%>
<%--                    <img src="${contextPath}/images/upload_dir/proofOfProfessionPic_${currentMfsAccountModel.appUserId}.gif" width="100" height="100" />--%>
<%--                </td>--%>
<%--            </tr>--%>

<%--            <tr>--%>
<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Terms and Condition Picture</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText">--%>
<%--                    <img src="${contextPath}/images/upload_dir/authorization/tncPic_${actionAuthorizationModel.actionAuthorizationId}.gif" width="100" height="100" />--%>
<%--                </td>--%>
<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Signature Picture</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText">--%>
<%--                    <img src="${contextPath}/images/upload_dir/authorization/signPic_${actionAuthorizationModel.actionAuthorizationId}.gif" width="100" height="100" />--%>
<%--                </td>--%>

<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Terms and Condition Picture</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText">--%>
<%--                    <img src="${contextPath}/images/upload_dir/tncPic_${currentMfsAccountModel.appUserId}.gif" width="100" height="100" />--%>
<%--                </td>--%>
<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Signature Picture</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText">--%>
<%--                    <img src="${contextPath}/images/upload_dir/signPic_${currentMfsAccountModel.appUserId}.gif" width="100" height="100" />--%>
<%--                </td>--%>
<%--            </tr>--%>
<%--            <c:if test="${mfsAccountModel.customerAccountTypeId == 2}">--%>
<%--                <tr>--%>
<%--                    <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Level 1 Form Picture</td>--%>
<%--                    <td width="13%" height="16" align="left"  class="formText">--%>
<%--                        <img src="${contextPath}/images/upload_dir/authorization/level1FormPic_${actionAuthorizationModel.actionAuthorizationId}.gif" width="100" height="100" />--%>
<%--                    </td>--%>

<%--                    <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText"></td>--%>
<%--                    <td width="13%" height="16" align="left"  class="formText"></td>--%>

<%--                    <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Level 1 Form Picture</td>--%>
<%--                    <td width="13%" height="16" align="left"  class="formText">--%>
<%--                        <img src="${contextPath}/images/upload_dir/level1FormPic_${currentMfsAccountModel.appUserId}.gif" width="100" height="100" />--%>
<%--                    </td>--%>
<%--                </tr>--%>
<%--            </c:if>--%>
<%--            <tr>--%>
<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">CNIC #</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText">${mfsAccountModel.nic}</td>--%>

<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Date of Birth</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText"><fmt:formatDate  type="both"--%>
<%--                                                                                            dateStyle="short" timeStyle="short" value="${mfsAccountModel.dob}" /></td>--%>

<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">CNIC #</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText">${currentMfsAccountModel.nic}</td>--%>

<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Date of Birth</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText"><fmt:formatDate  type="both"--%>
<%--                                                                                            dateStyle="short" timeStyle="short" value="${currentMfsAccountModel.dob}" /></td>--%>

<%--            <tr>--%>
<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">CNIC Expiry</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText"><fmt:formatDate  type="both"--%>
<%--                                                                                            dateStyle="short" timeStyle="short" value="${mfsAccountModel.nicExpiryDate}" /></td>--%>

<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Original CNIC Seen</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText">--%>
<%--                    Yes--%>
<%--                </td>--%>

<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">CNIC Expiry</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText"><fmt:formatDate  type="both"--%>
<%--                                                                                            dateStyle="short" timeStyle="short" value="${currentMfsAccountModel.nicExpiryDate}" /></td>--%>

<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Original CNIC Seen</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText">--%>
<%--                    Yes--%>
<%--                </td>--%>
<%--            </tr>--%>
<%--            <tr>--%>
<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Initial Deposit</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText">${mfsAccountModel.initialDeposit}</td>--%>

<%--                <td width="12%" height="16" align="left"  class="formText"></td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText"></td>--%>

<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Initial Deposit</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText">${mfsAccountModel.initialDeposit}</td>--%>
<%--            </tr>--%>

<%--            <tr>--%>
<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Place of Birth</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText">${mfsAccountModel.birthPlaceName }</td>--%>

<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Gender</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText">--%>
<%--                    <c:if test="${mfsAccountModel.gender=='M'}">--%>
<%--                        Male--%>
<%--                    </c:if>--%>
<%--                    <c:if test="${mfsAccountModel.gender=='F'}">--%>
<%--                        Female--%>
<%--                    </c:if>--%>
<%--                    <c:if test="${mfsAccountModel.gender=='K'}">--%>
<%--                        Khwaja Sira--%>
<%--                    </c:if>--%>
<%--                </td>--%>

<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Place of Birth</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText">${currentMfsAccountModel.birthPlaceName }</td>--%>

<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Gender</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText">--%>
<%--                    <c:if test="${currentMfsAccountModel.gender=='M'}">--%>
<%--                        Male--%>
<%--                    </c:if>--%>
<%--                    <c:if test="${currentMfsAccountModel.gender=='F'}">--%>
<%--                        Female--%>
<%--                    </c:if>--%>
<%--                    <c:if test="${currentMfsAccountModel.gender=='K'}">--%>
<%--                        Khwaja Sira--%>
<%--                    </c:if>--%>
<%--                </td>--%>
<%--            </tr>--%>
<%--            <tr>--%>
<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Father/Husband Name</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText">${mfsAccountModel.fatherHusbandName }</td>--%>

<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Mother's Maiden Name</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText">${mfsAccountModel.motherMaidenName }</td>--%>

<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Father/Husband Name</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText">${currentMfsAccountModel.fatherHusbandName }</td>--%>

<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Mother's Maiden Name</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText">${currentMfsAccountModel.motherMaidenName }</td>--%>
<%--            </tr>--%>
<%--            <tr>--%>
<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Mailing Address</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText">${mfsAccountModel.presentAddress }</td>--%>

<%--                <td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">City</td>--%>
<%--                <td width="25%" height="16" align="left"  class="formText">${mfsAccountModel.city }</td>--%>

<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Mailing Address</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText">${currentMfsAccountModel.presentAddress }</td>--%>

<%--                <td width="25%" height="16" align="left" bgcolor="F3F3F3" class="formText">City</td>--%>
<%--                <td width="25%" height="16" align="left"  class="formText">${currentMfsAccountModel.city }</td>--%>
<%--            </tr>--%>
<%--            <c:if test="${mfsAccountModel.customerAccountTypeId == 2}">--%>
<%--                <tr>--%>
<%--                    <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Email</td>--%>
<%--                    <td width="13%" height="16" align="left"  class="formText">${mfsAccountModel.email }</td>--%>

<%--                    <td width="12%" height="16" align="left"  class="formText"></td>--%>
<%--                    <td width="13%" height="16" align="left"  class="formText"></td>--%>

<%--                    <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Email</td>--%>
<%--                    <td width="13%" height="16" align="left"  class="formText">${currentMfsAccountModel.email }</td>--%>
<%--                </tr>--%>
<%--                <tr>--%>
<%--                    <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Source of Funds</td>--%>
<%--                    <td width="13%" height="16" align="left"  class="formText">--%>
<%--                        <c:forEach var="fundSourceName" items="${mfsAccountModel.fundSourceName}">--%>
<%--                            ${fundSourceName}<br>--%>
<%--                        </c:forEach>--%>
<%--                    </td>--%>

<%--                    <td width="12%" height="16" align="left"  class="formText"></td>--%>
<%--                    <td width="13%" height="16" align="left"  class="formText"></td>--%>

<%--                    <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Source of Funds</td>--%>
<%--                    <td width="13%" height="16" align="left"  class="formText">--%>
<%--                        <c:forEach var="fundSourceName" items="${currentMfsAccountModel.fundSourceName}">--%>
<%--                            ${fundSourceName}<br>--%>
<%--                        </c:forEach>--%>
<%--                    </td>--%>
<%--                </tr>--%>
<%--            </c:if>--%>
<%--            <tr>--%>
<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Tax Regime</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText">${mfsAccountModel.taxRegimeName }</td>--%>

<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">FED(%age)</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText">${mfsAccountModel.fed }</td>--%>

<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Tax Regime</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText">${currentMfsAccountModel.taxRegimeName }</td>--%>

<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">FED(%age)</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText">${currentMfsAccountModel.fed }</td>--%>
<%--            </tr>--%>
<%--            <c:if test="${mfsAccountModel.customerAccountTypeId == 2}">--%>
<%--                <tr>--%>
<%--                    <td colspan="9" align="center" bgcolor="FBFBFB">--%>
<%--                        <h5>--%>
<%--                            Next of KIN:--%>
<%--                        </h5>--%>
<%--                    </td>--%>
<%--                </tr>--%>
<%--                <tr>--%>
<%--                    <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Name</td>--%>
<%--                    <td width="13%" height="16" align="left"  class="formText">${mfsAccountModel.nokName }</td>--%>

<%--                    <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Relationship</td>--%>
<%--                    <td width="13%" height="16" align="left"  class="formText">${mfsAccountModel.nokRelationship }</td>--%>

<%--                    <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Name</td>--%>
<%--                    <td width="13%" height="16" align="left"  class="formText">${currentMfsAccountModel.nokName }</td>--%>

<%--                    <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Relationship</td>--%>
<%--                    <td width="13%" height="16" align="left"  class="formText">${currentMfsAccountModel.nokRelationship }</td>--%>
<%--                </tr>--%>
<%--                <tr>--%>
<%--                    <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Address</td>--%>
<%--                    <td width="13%" height="16" align="left"  class="formText">${mfsAccountModel.nokMailingAdd }</td>--%>

<%--                    <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">CNIC</td>--%>
<%--                    <td width="13%" height="16" align="left"  class="formText">${mfsAccountModel.nokNic }</td>--%>

<%--                    <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Address</td>--%>
<%--                    <td width="13%" height="16" align="left"  class="formText">${currentMfsAccountModel.nokMailingAdd }</td>--%>

<%--                    <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">CNIC</td>--%>
<%--                    <td width="13%" height="16" align="left"  class="formText">${currentMfsAccountModel.nokNic }</td>--%>
<%--                </tr>--%>
<%--                <tr>--%>
<%--                    <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Mobile No</td>--%>
<%--                    <td width="13%" height="16" align="left"  class="formText">${mfsAccountModel.nokMobile }</td>--%>

<%--                    <td width="12%" height="16" align="left"  class="formText"></td>--%>
<%--                    <td width="13%" height="16" align="left"  class="formText"></td>--%>

<%--                    <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Mobile No</td>--%>
<%--                    <td width="13%" height="16" align="left"  class="formText">${currentMfsAccountModel.nokMobile }</td>--%>
<%--                </tr>--%>
<%--            </c:if>--%>
<%--            <tr>--%>
<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Nadra Verification</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText">--%>
<%--                    <c:if test="${mfsAccountModel.verisysDone}">--%>
<%--                        Positive--%>
<%--                    </c:if>--%>
<%--                    <c:if test="${not mfsAccountModel.verisysDone}">--%>
<%--                        Negative--%>
<%--                    </c:if>--%>
<%--                </td>--%>
<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Screening Performed</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText">--%>
<%--                    <c:if test="${mfsAccountModel.screeningPerformed}">--%>
<%--                        Match--%>
<%--                    </c:if>--%>
<%--                    <c:if test="${not mfsAccountModel.screeningPerformed}">--%>
<%--                        Not Match--%>
<%--                    </c:if>--%>
<%--                </td>--%>

<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Nadra Verification</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText">--%>
<%--                    <c:if test="${currentMfsAccountModel.verisysDone}">--%>
<%--                        Positive--%>
<%--                    </c:if>--%>
<%--                    <c:if test="${not currentMfsAccountModel.verisysDone}">--%>
<%--                        Negative--%>
<%--                    </c:if>--%>
<%--                </td>--%>
<%--                <td width="12%" height="16" align="left" bgcolor="F3F3F3" class="formText">Screening Performed</td>--%>
<%--                <td width="13%" height="16" align="left"  class="formText">--%>
<%--                    <c:if test="${currentMfsAccountModel.screeningPerformed}">--%>
<%--                        Match--%>
<%--                    </c:if>--%>
<%--                    <c:if test="${not currentMfsAccountModel.screeningPerformed}">--%>
<%--                        Not Match--%>
<%--                    </c:if>--%>
<%--                </td>--%>
<%--            </tr>--%>
        </table>

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