<%--
  Created by IntelliJ IDEA.
  User: DELL
  Date: 10/15/2021
  Time: 4:16 PM
  To change this template use File | Settings | File Templates.
--%>
<%@include file="/common/taglibs.jsp" %>

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
    <meta http-equiv="cache-control" content="no-cache"/>
    <meta http-equiv="cache-control" content="no-store"/>
    <meta http-equiv="cache-control" content="private"/>
    <meta http-equiv="cache-control" content="max-age=0, must-revalidate"/>
    <meta http-equiv="expires" content="now-1"/>
    <meta http-equiv="pragma" content="no-cache"/>

    <script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css" type="text/css"/>
    <meta name="title" content="Manage Debit Block"/>

    <script type="text/javascript">

        function refreshParent() {
            window.opener.location.reload();
            window.close();
        }

        function onFormSubmit(theForm) {

            var theForm = document.forms.debitblockform;
            var flag = true;
            /* if(theForm.amount.value != undefined && theForm.amount.value != "" && theForm.amount.value == 0){
                alert("Please input valid Amount\n");
                flag = false;
            } */

            return flag;
        }

        function submitBlock(){
            var theForm = document.forms.debitblockform;
            theForm.isDebitBlocked.value=true;
            theForm.submit();
        }

        function submitUnblock(){
            var theForm = document.forms.debitblockform;
            theForm.isDebitBlocked.value=false;
            theForm.submit();
        }

    </script>
    <style>
        .header {
            background-color: #0981be; /*308dbb original*/
            color: white;
            font-family: verdana, arial, helvetica, sans-serif; /*verdana, arial, helvetica, sans-serif*/
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
</head>
<body bgcolor="#ffffff">
<h3 class="header" id="logHeader">
    Manage Debit Block
</h3>

<c:choose>
    <c:when test="${not empty _status}">
        <c:choose>
            <c:when test="${_status eq 'success'}">

                <div class="">
                    <table class="tableRegion">
                        <tr class="odd">
                            <td class="formText">
                                    ${message }<p>Click the button below to close the window</p>

                            </td>
                        </tr>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <div class="eXtremeTable">
                    <table class="tableRegion">
                        <tr class="odd">
                            <td class="formText"><c:out value="${message}"/></td>
                        </tr>
                    </table>
                </div>
            </c:otherwise>

        </c:choose>
        <div class="eXtremeTable" style="text-align: center; padding-top: 10px;">
            <input type="button" class="button" value="Close Window" onclick="javascript:refreshParent();"/>
        </div>
    </c:when>
    <c:otherwise>
        <div class="eXtremeTable">
            <table class="tableRegion" width="100%">
                <tr class="titleRow">
                    <td>
                        <form id="debitblockform" method="post" action="p-debitblock.html" onsubmit="return onFormSubmit(this);">
                            <table width="100%" border="0" cellpadding="0" cellspacing="1">

                                <input type="hidden" name="isUpdate" id="isUpdate" value="true"/>
                                <input type="hidden" name="actionAuthorizationId" value="${param.authId}" />
                                <input type="hidden" name="resubmitRequest" value="${param.isReSubmit}" />
                                <input type="hidden" name="isDebitBlocked" id="isDebitBlocked" />

                                <tr bgcolor="FBFBFB">
                                    <td colspan="2" align="center">&nbsp;</td>
                                </tr>

                                <tr>
                                    <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText"><span
                                            style="color:#FF0000">*</span>
                                        <c:if test="${param.isAgent }">Agent Mobile No.</c:if>
                                        <c:if test="${param.isAgent eq 'false' and param.isHandler eq null }">Customer Mobile No.</c:if>
                                        <c:if test="${param.isHandler }">Handler Mobile No.</c:if>:

                                        &nbsp;</td>
                                    <td align="left" bgcolor="FBFBFB">
                                        <spring:bind path="debitBlockVo.mfsId">
                                        <input name="mfsId" type="text" size="40" readonly="true" class="textBox"
                                               tabindex="0" maxlength="50" value="${param.mobileNo }"/>
                                        </spring:bind>

                                </tr>
                                <c:if test="${!empty param.debitBlockAmount}">
                                    <fmt:formatNumber var="formattedAmount" type="number" pattern="#.##" maxFractionDigits="2" value="${param.debitBlockAmount}"/>
                                    <c:set var="varDebitBlockAmount" value="${formattedAmount}"/>
                                </c:if>
                                <tr>
                                    <td align="right" bgcolor="F3F3F3" class="formText">Amount:&nbsp;</td>
                                    <td align="left" bgcolor="FBFBFB">
                                        <spring:bind path="debitBlockVo.amount">
                                            <input name="amount" type="text" size="40" class="textBox"
                                                   tabindex="0" maxlength="10" value="${varDebitBlockAmount}" onkeypress="return maskNumber(this,event)" />
                                        </spring:bind>
                                    </td>
                                </tr>
                                <tr bgcolor="FBFBFB"></tr>
                                <tr bgcolor="FBFBFB"></tr>
                                <tr bgcolor="FBFBFB"></tr>
                                <tr bgcolor="FBFBFB">
                                    <td height="19" align="right">
                                        <input name="_block" type="button" class="button" value=" Block " tabindex="2" onclick="submitBlock();"/>
                                    </td>
                                    <td height="19" align="left">
                                        <c:if test="${param.isDebitBlocked=='true'}">
                                            <input name="_unblock" type="button" class="button" value="Unblock" tabindex="3" onclick="submitUnblock();"/>
                                        </c:if>
                                        <c:if test="${empty param.isDebitBlocked or param.isDebitBlocked=='false'}">
                                            <input name="cancel" type="reset" class="button" value=" Cancel " tabindex="4"
                                                   onclick="javascript:window.close();"/>
                                        </c:if>
                                    </td>
                                </tr>
                                <spring:bind path="debitBlockVo.encryptedAppUserId">
                                    <input type="hidden" name="encryptedAppUserId" value="${param.appUserId}">
                                </spring:bind>

                                <spring:bind path="debitBlockVo.usecaseId">
                                    <input type="hidden" name="usecaseId" value="${param.usecaseId}">
                                </spring:bind>

                                <spring:bind path="debitBlockVo.mobileNo">
                                    <input type="hidden" name="mobileNo" value="${param.mobileNo}">
                                </spring:bind>

                            </table>
                        </form>
                    </td>
                </tr>
            </table>
        </div>
    </c:otherwise>
</c:choose>
</body>
</html>

