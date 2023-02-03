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

    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css" type="text/css"/>
    <meta name="title" content="Account Closing"/>

    <script type="text/javascript">
        function validateCloseForm()
        {
            var input = document.getElementById("acType").value;
            if(input == '')
                document.getElementById("saveButton").disabled = true;
            else
                document.getElementById("saveButton").disabled = false;
        }
        function backButtonOverrideBody(e)
        {
            if (navigator.appName != "Microsoft Internet Explorer"){
                window.forward();
            }
            else{
                history.forward();
            }

        }



        function setFocus()
        {
            try
            {
                if($('comments')!=null)
                {
                    $('comments').focus();
                }
            }catch(e){}
        }
        function validate()
        {
            try
            {
                if($F('comments')=="")
                {
                    alert('Comments is a required field');
                    $('comments').focus();
                    return false;
                }
                else
                {
                    if($F('comments').length>250)
                    {
                        alert('Maximum characters should not be more than 250');
                        $('comments').focus();
                        return false;
                    }
                    else
                    {
                        //if there is illegal special character
                        if(!validateFormChar(document.forms[0]))
                            return false;

                        $('_save').disabled = true;
                        return true;
                    }
                }
            }catch(e){}
        }

        function refreshParent() {
            window.opener.location.reload();
            window.close();
        }
    </script>
    <style>
        .header {
            background-color: #0292e0;/*308dbb original*/
            color: white;
            font-family:arial, helvetica, sans-serif;/*verdana, arial, helvetica, sans-serif*/
            font-size: 14px;
            text-align: center;
            padding: 10px 3px;
            margin: 0px;
            border-right-style: solid;
            border-right-width: 1px;
            border-color: white;
        }
    </style>
</head>
<body bgcolor="#ffffff" onload="javascript:setFocus();backButtonOverrideBody(event);">
<h3 class="header" id="logHeader">
    Remove Dormancy From Account
</h3>

<c:choose>
    <c:when test="${not empty _status}">
        <c:choose>
            <c:when test="${_status eq 'success'}">

                <div class="eXtremeTable">
                    <table class="tableRegion">
                        <tr class="odd">
                            <td class="formText">

                                Dormancy Removed successfully.<p>Click the button below to close the window</p>
                                    <%--<c:if test="${appUserModel.accountClosedUnsettled and not appUserModel.accountClosedSettled }">
                                    </c:if>--%>
                            </td>
                        </tr>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <div class="eXtremeTable">
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
        <div class="eXtremeTable">
            <table class="tableRegion" width="100%">
                <tr class="titleRow">
                    <td>
                        <html:form  name = "removeDormancy" id = "removeDormancy" commandName="appUserModel" action="p-removedormancy.html" method="post" onsubmit="return validate()">
                            <table width="100%" border="0" cellpadding="0" cellspacing="1">

                                <tr bgcolor="FBFBFB">
                                    <td colspan="2" align="center">&nbsp;</td>
                                </tr>
                                <tr>
                                    <td width="49%" height="16" align="right" bgcolor="F3F3F3" class="formText">Customer ID:&nbsp;&nbsp;</td>
                                    <td width="51%" align="left" bgcolor="FBFBFB">
                                        <input type="text" name="customerId" value="${appUserModel.customerId}" class="textBox" readonly="readonly" size="15"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="right" bgcolor="F3F3F3" class="formText">
                                        <span style="color:#FF0000">*</span>Account Type: &nbsp;
                                    </td>
                                    <td>
                                        <select  id="acType" name="acType" onchange="showCustomer()" tabindex="1" >
                                            <option value="" selected>---SELECT---</option>
                                            <c:forEach items="${accountTypeList}" var="accountType">
                                                <option value="${accountType}">${accountType}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="right" bgcolor="F3F3F3" class="formText">Comments:&nbsp;&nbsp; </td>
                                    <td align="left" bgcolor="FBFBFB">
                                        <c:if test="${ not appUserModel.accountClosedUnsettled}">
                                            <html:textarea path="closingComments" onkeypress="return maskCommon(this,event)" cssClass="textBox" rows="6" cssStyle="overflow: auto; width: 163px; height: 102px;"></html:textarea>
                                        </c:if>
                                        <c:if test="${appUserModel.accountClosedUnsettled}">
                                            <html:textarea path="settlementComments" onkeypress="return maskCommon(this,event)" cssClass="textBox" rows="6" cssStyle="overflow: auto; width: 163px; height: 102px;"></html:textarea>
                                        </c:if>
                                    </td>
                                </tr>
                                <tr bgcolor="FBFBFB">
                                    <td colspan="2" align="center">&nbsp;</td>
                                </tr>
                                <tr>
                                    <input type="hidden" path="appUserId" name="appUserId" value="${appUserModel.appUserId}"/>
                                    <input type="hidden" name="isUpdate" id="isUpdate" value="true"/>

                                    <td align="center" colspan="2">
                                        <input name="_save" type="submit" id="saveButton" class="button"  value=" Submit "/> &nbsp;
                                        <input name="cancel" type="button" class="button" value=" Cancel " onclick="javascript:window.close();"/>
                                    </td>
                                </tr>
                            </table>
                        </html:form>
                    </td>
                </tr>
            </table>
        </div>
    </c:otherwise>
</c:choose>
</body>
</html>