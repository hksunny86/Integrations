<%--
  Created by IntelliJ IDEA.
  User: Malik
  Date: 8/22/2016
  Time: 11:39 AM
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
    <meta name="title" content="Mark/Unmark Blacklisted"/>

    <script type="text/javascript">

        function refreshParent() {
            window.opener.location.reload();
            window.close();
        }
    </script>
    <style>
        .header {
            background-color: #6699CC; /*308dbb original*/
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
   Mark/Unmark Blacklisted
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
                        <form id="markunmarkblacklistedform" method="post" action="p-markunmarkblacklisted.html">
                            <table width="100%" border="0" cellpadding="0" cellspacing="1">

                                <input type="hidden" name="isUpdate" id="isUpdate" value="true"/>

                                <tr bgcolor="FBFBFB">
                                    <td colspan="2" align="center">&nbsp;</td>
                                </tr>

                                <tr>
                                    <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText"><span
                                            style="color:#FF0000">*</span>
                                        <c:if test="${param.isAgent }">Agent ID</c:if>
                                        <c:if test="${param.isAgent eq 'false' and param.isHandler eq null }">Customer ID</c:if>
                                        <c:if test="${param.isHandler }">Handler ID</c:if>:

                                        &nbsp;</td>
                                    <td align="left" bgcolor="FBFBFB">
                                        <spring:bind path="blacklistMarkingVo.mfsId">
                                        <input name="mfsId" type="text" size="40" readonly="true" class="textBox"
                                               tabindex="0" maxlength="20" value="${param.mfsId }"/>
                                        </spring:bind>

                                </tr>
                                <tr>
                                    <td align="right" bgcolor="F3F3F3" class="formText">Comments:&nbsp;</td>
                                    <td align="left" bgcolor="FBFBFB">

                                        <spring:bind path="blacklistMarkingVo.comments">
                                        <textarea name="comments" cols="50" rows="5" tabindex="1"
                                                  style="width:163px; height: 102px"
                                                  onkeyup="textAreaLengthCounter(this,250);"
                                                  onkeypress="return maskCommon(this,event)"
                                                  class="textBox"></textarea>
                                        </spring:bind>
                                    </td>
                                </tr>
                                <tr bgcolor="FBFBFB">
                                    <td height="19" align="right">
                                        <input name="_save" type="submit" class="button" value="Save" tabindex="2"/>
                                    </td>
                                    <td height="19" align="left">
                                        <input name="cancel" type="button" class="button" value=" Cancel " tabindex="3"
                                               onclick="javascript:window.close();"/>
                                    </td>
                                </tr>
                                <spring:bind path="blacklistMarkingVo.encryptedAppUserId">
                                    <input type="hidden" name="encryptedAppUserId" value="${param.appUserId}">
                                </spring:bind>

                                <spring:bind path="blacklistMarkingVo.blacklisted">
                                    <input type="hidden" name="blacklisted" value="${param.blacklisted}">
                                </spring:bind>

                                <spring:bind path="blacklistMarkingVo.usecaseId">
                                    <input type="hidden" name="usecaseId" value="${param.usecaseId}">
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
